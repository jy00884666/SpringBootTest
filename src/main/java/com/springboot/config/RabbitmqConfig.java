package com.springboot.config;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

/*RabbitMQ配置类,注释后表示Spring启动时不会加载配置项目,不会使用RabbitMQ*/
//@Configuration
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
    /**@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)多例模式:这个是说在每次注入的时候会自动创建一个新的bean实例
     * @Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)单例模式:在整个应用中只能创建一个实例
     * @Scope(value=WebApplicationContext.SCOPE_GLOBAL_SESSION)全局session中的一般不常用
     * @Scope(value=WebApplicationContext.SCOPE_APPLICATION)在一个web应用中只创建一个实例
     * @Scope(value=WebApplicationContext.SCOPE_REQUEST)在一个请求中创建一个实例
     * @Scope(value=WebApplicationContext.SCOPE_SESSION)每次创建一个会话中创建一个实例
     * 里面还有个属性
     *    proxyMode=ScopedProxyMode.INTERFACES创建一个JDK代理模式
     *    proxyMode=ScopedProxyMode.TARGET_CLASS基于类的代理模式
     *    proxyMode=ScopedProxyMode.NO（默认）不进行代理*/
    //@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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
        // 消息转换器,发送 || 接受
        //template.setMessageConverter(messageConverter());
        return template;
    }
    
    /**
     * 申明消费者,可以用 @RabbitListener注解的方式,可以向这样用@Bean方式申明设置参数,然后使用
     * @RabbitListener(queues = "com.direct.testManualQueue", containerFactory = "simpleRabbitListenerContainerFactory")
     * 来监消息队列(消息消费者)
     *
     * 使用RabbitMq,发送消息确认,需要使用rabbitTemplate的ConfirmCallback方法和ReturnCallback方法,网上大多配置为全局修改,
     * 如果需要对单个生产者进行配置,则需要将rabbitTemplate设置为多例,在需要单独配置的生产者
     * 使用@PostConstruct注解进行设置rabbitTemplate的ConfirmCallback方法和ReturnCallback方法
     */
    @Bean(name = "simpleRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(ConnectionFactory connection) {
        // 申明消费者
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connection);
        // NONE:不确认,MANUAL:手动确认,AUTO:自动确认(默认)
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 默认消费者线程数量
        //factory.setConcurrentConsumers(5);
        // 最大消费者线程数量
        //factory.setMaxConcurrentConsumers(10);
        // 每次给消费者发送的消息数量(设置预取数量等同于原生写法channel.basicQos(1);)
        factory.setPrefetchCount(1);
        return factory;
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
    
    /*消息转换器*/
    /*配合template.setMessageConverter(messageConverter());一起使用,
     *单独注解@Bean就被Spring管理了,即使没有设置setMessageConverter也一样会在消息发送和接收的时候被执行*/
    //@Bean
    public MessageConverter messageConverter() {
        return new MessageConverter() {
            /*发送消息时参数转换成什么样子*/
            @Override
            public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
                /**入参:
                 * Object o:发送消息时的message,rabbitTemplate.convertAndSend(exchangeName, rountingKey, message,
                 * correlationData);
                 * messageProperties:消息配置*/
                logger.info("转换消息内容o={}", JSON.toJSONString(o));
                logger.info("此次消息配置messageProperties={}", JSON.toJSONString(messageProperties));
                
                Message message = new Message(JSON.toJSONBytes(o), messageProperties);
                return message;
            }
            
            /*接收消息时参数转换成什么样子*/
            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                logger.info("接收消息内容信息message={}", JSON.toJSONString(message));
                // 消息内容
                byte[] body = message.getBody();
                logger.info("接受消息内容body={}", (Object) JSON.parseObject(body, Object.class));
                // 消息配置
                MessageProperties messageProperties = message.getMessageProperties();
                return JSON.parseObject(body, Object.class);
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
    
    /*-----------------------------------手动确认消息队列----------------------------------*/
    @Bean(name = "manualQueue")
    public Queue manualQueue() {
        // 正常队列和死信进行绑定,转发到死信队列，配置参数
        Map<String, Object> map = new HashMap<>();
        // 3种方式 任选其一,选择其他方式之前,先把交换机和队列删除了,在启动项目,否则报错。
        // 方式一
        // 死信交换器名称，过期或被删除（因队列长度超长或因空间超出阈值）的消息可指定发送到该交换器中；
        map.put("x-dead-letter-exchange", "com.direct.testDeadExchange");
        // 死信消息路由键，在消息发送到死信交换器时会使用该路由键，如果不设置，则使用消息的原来的路由键值
        map.put("x-dead-letter-routing-key", "com.direct.testDeadRountingKey");
        // 方式二
        // 消息的过期时间，单位：毫秒；达到时间 放入死信队列
        //map.put("x-message-ttl",5000);
        // 方式三
        // 队列最大长度，超过该最大值，则将从队列头部开始删除消息；放入死信队列一条数据
        //map.put("x-max-length",3);
        return new Queue("com.direct.testManualQueue", true, false, false, map);
    }
    
    @Bean(name = "manualExchange")
    public DirectExchange manualExchange() {
        return new DirectExchange("com.direct.testManualExchange", true, false);
    }
    
    @Bean(name = "bindingManual")
    public Binding bindingManual(@Qualifier(value = "manualQueue") Queue directQueue,
                                 @Qualifier(value = "manualExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue).to(directExchange).with("com.direct.testManualRountingKey");
    }
    
    /* ----------申明死信队列---------
     * 创建一个普通队列时，通过添加配置绑定另一个交换机(死信交换机)，在普通队列发生异常时，
     * 消息就通过死信交换机转发到绑定它的队列里，这个绑定死信交换机的队列就是死信队列
     * 三种情况会走死信交换机
     * 1.信息消费者确认失败,也未将消息重新放入队列
     * 2.信息过期,可以设置消费任务超时时间
     * 3.信息消费队列溢出
     */
    @Bean(name = "deadQueue")
    public Queue deadQueue() {
        return new Queue("com.direct.testDeadQueue", true, false, false);
    }
    
    /*死信交换机*/
    @Bean(name = "deadExchange")
    public DirectExchange deadExchange() {
        return new DirectExchange("com.direct.testDeadExchange", true, false);
    }
    
    /*绑定死信队列和死信交换机,这里交换机与rountiongkey都需要与正常队列和死信进行绑定时的对应上*/
    @Bean(name = "bindingDead")
    public Binding bindingDead(@Qualifier(value = "deadQueue") Queue directQueue,
                               @Qualifier(value = "deadExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue).to(directExchange).with("com.direct.testDeadRountingKey");
    }
    
}
