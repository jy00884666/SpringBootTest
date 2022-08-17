package com.springboot.config;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*RabbitMQ配置类*/
@Configuration
public class RabbitmqConfig {
    
    private static Logger logger = LoggerFactory.getLogger(RabbitmqConfig.class);
    
    /*-------------------------配置文件中获取值 src/main/resources/application.properties---------------------------*/
    @Value("${spring.rabbitmq.host}")
    private String host;
    
    @Value("${spring.rabbitmq.port}")
    private int port;
    
    @Value("${spring.rabbitmq.virtualHost}")
    private String virtualHost;
    
    @Value("${spring.rabbitmq.username}")
    private String username;
    
    @Value("${spring.rabbitmq.password}")
    private String password;
    
    @Value("${spring.rabbitmq.publisherConfirmsType}")
    private Boolean publisherConfirmsType;
    
    @Value("${spring.rabbitmq.publisherReturnsType}")
    private Boolean publisherReturnsType;
    
    @Value("${spring.rabbitmq.mandatory}")
    private Boolean mandatory;
    
    /*声明连接*/
    @Bean
    public ConnectionFactory connectionFactory() {
        // 创建连接
        CachingConnectionFactory con = new CachingConnectionFactory();
        // 主机
        con.setHost(host);
        // 端口
        con.setPort(port);
        // 虚拟主机
        con.setVirtualHost(virtualHost);
        // 用户名
        con.setUsername(username);
        // 密码
        con.setPassword(password);
        // 是否开启消息确认机制,若使用confirm-callback,必须要配置publisherConfirms为true
        con.setPublisherConfirms(publisherConfirmsType);
        // 是否开启 Return 机制,若使用return-callback,必须要配置publisherReturns为true
        con.setPublisherReturns(publisherReturnsType);
        return con;
    }
    
    /*使用RabbitTemplate进行收发消息将十分的方便*/
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) throws Exception {
        // 构造方法声明
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        // 也可以通过方法存入RabbitMQ连接
        //template.setConnectionFactory(connectionFactory);
        /**启用 Confirm 机制,ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调
         * 如果消息没有到exchange,则confirm回调,ack=false
         * 如果消息到达exchange,则confirm回调,ack=true*/
        template.setConfirmCallback(confirmCallback());
        /**
         * 是否开启消息处理失败回调,使用return-callback时必须设置mandatory为true，或者在配置中设置mandatory-expression的值为true
         * 当mandatory标志位设置为true时,如果exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息
         * 那么broker会调用returnCallback()方法将消息返还给生产者
         * 当mandatory设置为false时，出现上述情况broker会直接将消息丢弃
         */
        template.setMandatory(mandatory);
        // 消息处理失败回调方法
        template.setReturnCallback(returnCallback());
        return template;
    }
    
    /**
     * ConfirmCallback：消息发送成功的回调
     * 每一条发到rabbitmq server的消息都会调一次confirm方法。
     * 如果消息成功到达exchange，则ack参数为true，反之为false；
     * cause参数是错误信息；
     * CorrelationData可以理解为context，在发送消息时传入的这个参数，此时会拿到。
     */
    @Bean
    /*容器中唯一*/
    @ConditionalOnMissingBean(value = RabbitTemplate.ConfirmCallback.class)
    public RabbitTemplate.ConfirmCallback confirmCallback() {
        return new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                logger.info("消息是否被成功处理:ack={},cause={},correlationData={}", ack, cause,
                        JSON.toJSONString(correlationData));
                /**这里会遇到一个问题，当ConfirmCallback被调用，且ack参数为false时，意味着这条消息可能发送失败了，
                 * 那我可能想把这条消息在这里保存下来，比如打条日志，以免消息丢失，但对于ConfirmCallback，
                 * 是不能像ReturnCallback一样直接拿到message的。
                 * 所以，我们可以写个子类继承CorrelationData类,多加入一个message属性,发送时将Message作为属性存入子类中*/
                logger.info("消息唯一标识: {}", correlationData);
                logger.info("确认状态: {}", ack);
                logger.info("造成原因: {}", cause);
            }
        };
    }
    
    /**
     * ReturnCallback：发生异常时的消息回调
     * 成功到达exchange，但routing不到任何queue时会调用。
     * 可以看到这里能直接拿到message，exchange，routingKey信息。
     */
    @Bean
    /*容器中唯一*/
    @ConditionalOnMissingBean(value = RabbitTemplate.ReturnCallback.class)
    public RabbitTemplate.ReturnCallback returnCallback() {
        return new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange,
                                        String routingKey) {
                logger.info("无法找到一个合适的queue存储消息,消息返回给生产者:message={},exchange={},routingKey={}",
                        message, exchange, routingKey);
                logger.info("消息主体: {}", message);
                logger.info("回复编码: {}", replyCode);
                logger.info("回复内容: {}", replyText);
                logger.info("交换机: {}", exchange);
                logger.info("路由键: {}", routingKey);
            }
        };
    }
    
    /*---------------------------------创建消息队列与交换机并绑定----------------------------------*/
    /*----------------------------------正则表达topic--------------------------------------*/
    // 消息队列起名：com.topic.testQueue
    @Bean(name = "topicQueue")
    public Queue topicQueue() {
        /**
         * durable:是否持久化,默认是false,持久化队列：队列在内存中,服务器挂掉后,队列就没了;
         *          true:服务器重启后,队列将会重新生成.注意:只是队列持久化,不代表队列中的消息持久化
         * exclusive:队列是否专属,默认也是false,只能被当前创建的连接使用,而且当连接关闭后队列即被删除。此参考优先级高于durable
         * autoDelete:是否自动删除,默认false,当没有生产者或者消费者使用此队列，该队列是否会自动删除。
         *            如果还没有消费者从该队列获取过消息或者监听该队列,那么该队列不会删除.
         *            只有在有消费者从该队列获取过消息后,该队列才有可能自动删除(当所有消费者都断开连接,不管消息是否获取完)
         * arguments: null,队列的配置,具体如下
         *            Message TTL : 消息生存期
         *            Auto expire : 队列生存期
         *            Max length : 队列可以容纳的消息的最大条数
         *            Max length bytes : 队列可以容纳的消息的最大字节数
         *            Overflow behaviour : 队列中的消息溢出后如何处理
         *            Dead letter exchange : 溢出的消息需要发送到绑定该死信交换机的队列
         *            Dead letter routing key : 溢出的消息需要发送到绑定该死信交换机,并且路由键匹配的队列
         *            Maximum priority : 最大优先级
         *            Lazy mode : 懒人模式
         *      例如: Map<String, Object> args = new HashMap<>();
         *            args.put("x-max-length",10);
         *            args.put("x-overflow", "reject-publish");//拒绝发布,丢弃最新发布的消息
         */
        return new Queue("com.topic.testQueue", true, false, false);
    }
    
    // Exchange交换机起名：com.topic.TestExchange
    @Bean(name = "topicExchange")
    public TopicExchange topicExchange() {
        /**
         * exchange：名称
         * type：类型,无需设置
         * durable：是否持久化，true:当RabbitMQ崩溃了重启后exchange仍然存在,false:RabbitMQ关闭后，没有持久化的Exchange将被清除
         *          设置exchange为持久化之后，并不能保证消息不丢失，因为此时发送往exchange中的消息并不是持久化的，
         *          需要配置delivery_mode=2指明message为持久的。
         * autoDelete：是否自动删除，true:如果没有与之绑定的Queue，直接删除
         * internal：是否内置的，如果为true，只能通过Exchange到Exchange,方法设置值在父类,没有构造方法设置值
         * arguments：结构化参数
         * */
        return new TopicExchange("com.topic.TestExchange", true, false);
    }
    
    // 绑定,将队列和交换机绑定,要是消息携带的路由键RountingKey是以com.topic.开头,都会分发到该队列
    @Bean(name = "bindingTopic")
    public Binding bindingTopic(@Qualifier(value = "topicQueue") Queue topicQueue,
                                @Qualifier(value = "topicExchange") TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue).to(topicExchange).with("com.topic.#");
    }
    
    /*---------------------------------------单对单Direct-----------------------------*/
    // 消息队列起名：com.direct.testQueue
    @Bean(name = "directQueue")
    public Queue directQueue() {
        /**
         * durable:是否持久化,默认是false,持久化队列：队列在内存中,服务器挂掉后,队列就没了;
         *          true:服务器重启后,队列将会重新生成.注意:只是队列持久化,不代表队列中的消息持久化
         * exclusive:队列是否专属,默认也是false,只能被当前创建的连接使用,而且当连接关闭后队列即被删除。此参考优先级高于durable
         * autoDelete:是否自动删除,默认false,当没有生产者或者消费者使用此队列，该队列是否会自动删除。
         *            如果还没有消费者从该队列获取过消息或者监听该队列,那么该队列不会删除.
         *            只有在有消费者从该队列获取过消息后,该队列才有可能自动删除(当所有消费者都断开连接,不管消息是否获取完)
         * arguments: null,队列的配置,具体如下
         *            Message TTL : 消息生存期
         *            Auto expire : 队列生存期
         *            Max length : 队列可以容纳的消息的最大条数
         *            Max length bytes : 队列可以容纳的消息的最大字节数
         *            Overflow behaviour : 队列中的消息溢出后如何处理
         *            Dead letter exchange : 溢出的消息需要发送到绑定该死信交换机的队列
         *            Dead letter routing key : 溢出的消息需要发送到绑定该死信交换机,并且路由键匹配的队列
         *            Maximum priority : 最大优先级
         *            Lazy mode : 懒人模式
         *      例如: Map<String, Object> args = new HashMap<>();
         *            args.put("x-max-length",10);
         *            args.put("x-overflow", "reject-publish");//拒绝发布,丢弃最新发布的消息
         */
        return new Queue("com.direct.testQueue", true, false, false);
    }
    
    // Exchange交换机起名：com.direct.TestExchange
    @Bean(name = "directExchange")
    public DirectExchange directExchange() {
        /**
         * exchange：名称
         * type：类型,无需设置
         * durable：是否持久化，true:当RabbitMQ崩溃了重启后exchange仍然存在,false:RabbitMQ关闭后，没有持久化的Exchange将被清除
         *          设置exchange为持久化之后，并不能保证消息不丢失，因为此时发送往exchange中的消息并不是持久化的，
         *          需要配置delivery_mode=2指明message为持久的。
         * autoDelete：是否自动删除，true:如果没有与之绑定的Queue，直接删除
         * internal：是否内置的，如果为true，只能通过Exchange到Exchange,方法设置值在父类,没有构造方法设置值
         * arguments：结构化参数
         * */
        return new DirectExchange("com.direct.TestExchange", true, false);
    }
    
    // 绑定,将队列和交换机绑定,并设置用于匹配键：com.direct.testRountingKey
    @Bean(name = "bindingDirect")
    public Binding bindingDirect(@Qualifier(value = "directQueue") Queue directQueue,
                                 @Qualifier(value = "directExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue).to(directExchange).with("com.direct.testRountingKey");
    }
    
}
