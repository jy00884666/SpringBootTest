package com.springboot.rabbitmq.consumer.simple;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.springboot.config.RabbitmqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/*消息消费者  direct 模式 */
@Component
/*监听哪个队列,queues:队列名称,containerFactory:用哪个工厂名称*/
@RabbitListener(queues = "com.direct.testManualQueue", containerFactory = "simpleRabbitListenerContainerFactory")
/*表名Spirng容器启动类,为了方便junit启动*/
@SpringBootApplication
/*为了方便junit启动时不报错,这里需要@import配置类,如果启动SpirngBoot这里可以不用@import*/
@Import(RabbitmqConfig.class)
public class SimpleRabbitmqConsumer {
    
    private static Logger logger = LoggerFactory.getLogger(SimpleRabbitmqConsumer.class);
    
    /*如果不手动确认消息的话,消息将一直停留在消息队列中,等待消费即使已经被消费过了*/
    @RabbitHandler
    public void process(String testMessage, Message message, Channel channel) throws IOException {
        logger.info("com.direct.testManualQueue消费者收到消息 : testMessage=" + testMessage);
        
        /* 考虑性能问题,确认和退回建议都用批量,积累到一定程度一起确认与回退,入参1只要传入到这个id节点,节点前的会被统一处理
         * 手动确认消息,入参1:消息唯一标识,入参2:是否批量确认
         * 第一个参数DeliveryTag中如果输入3，则消息DeliveryTag小于等于3的，这个Channel的，都会被确认,前提第二参数得传true*/
        //channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        
        /* 实际开发应用中需要考虑消息消费者死循环环问题,即只有一个消费者消费这个queues,那么有可能消息一直被消费,退回
         * 所以需要考虑消息处理计数器,让另一个程序兜底处理
         * 批量退回, 入参1:消息唯一标识
         *          入参2:是否批量
         *          入参3:消息是否回到消息队列,如果是false消息会被放到"死亡通信交换机"里,如果没配置"死信交换机"则消息会消失
         * 第一个参数DeliveryTag中如果输入3，则消息DeliveryTag小于等于3的，这个Channel的，都会被拒收,前提第二参数得传true*/
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        // 单个退回,入参1:消息唯一标识,入参2:消息是否回到消息队列
        //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        logger.info("com.direct.testManualQueue消费者拒绝消息 : testMessage=" + testMessage);
    }
    
    @RabbitHandler
    public void process(Map testMessage, Message message, Channel channel) throws IOException {
        logger.info("com.direct.testManualQueue消费者收到消息 : Map=" + JSON.toJSONString(testMessage));
        
        /* 考虑性能问题,确认和退回建议都用批量,积累到一定程度一起确认与回退,入参1只要传入到这个id节点,节点前的会被统一处理
         * 手动确认消息,入参1:消息唯一标识,入参2:是否批量确认
         * 第一个参数DeliveryTag中如果输入3，则消息DeliveryTag小于等于3的，这个Channel的，都会被确认,前提第二参数得传true*/
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        logger.info("com.direct.testManualQueue消费者确认消息 : Map");
    }
    
}
