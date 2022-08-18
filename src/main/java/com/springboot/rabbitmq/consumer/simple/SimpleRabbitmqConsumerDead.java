package com.springboot.rabbitmq.consumer.simple;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.springboot.config.RabbitmqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/* 死亡通行队列,交换机,简称"死信",消息消费者 direct 模式 */
@Component
/*监听哪个队列,queues:队列名称,containerFactory:用哪个工厂名称*/
@RabbitListener(queues = "com.direct.testDeadQueue", containerFactory = "simpleRabbitListenerContainerFactory")
/*表名Spirng容器启动类*/
@SpringBootApplication
/*为了方便junit启动时不报错,这里需要@import配置类,如果启动SpirngBoot这里可以不用@import*/
@Import(RabbitmqConfig.class)
public class SimpleRabbitmqConsumerDead {
    
    private static Logger logger = LoggerFactory.getLogger(SimpleRabbitmqConsumerDead.class);
    
    /*如果不手动确认消息的话,消息将一直停留在消息队列中,等待消费即使已经被消费过了*/
    @RabbitHandler
    public void process(String testMessage, Message message, Channel channel) throws IOException {
        logger.info("com.direct.testDeadQueue死信消费者收到消息 : testMessage=" + testMessage);
        
        /* 考虑性能问题,确认和退回建议都用批量,积累到一定程度一起确认与回退,入参1只要传入到这个id节点,节点前的会被统一处理
         * 手动确认消息,入参1:消息唯一标识,入参2:是否批量确认
         * 第一个参数DeliveryTag中如果输入3，则消息DeliveryTag小于等于3的，这个Channel的，都会被确认,前提第二参数得传true*/
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        logger.info("com.direct.testDeadQueue死信消费者确认消息");
    }
    
    @RabbitHandler
    public void process(Map testMessage, Message message, Channel channel) throws IOException {
        logger.info("com.direct.testDeadQueue死信消费者收到消息 : Map=" + JSON.toJSONString(testMessage));
        
        /* 考虑性能问题,确认和退回建议都用批量,积累到一定程度一起确认与回退,入参1只要传入到这个id节点,节点前的会被统一处理
         * 手动确认消息,入参1:消息唯一标识,入参2:是否批量确认
         * 第一个参数DeliveryTag中如果输入3，则消息DeliveryTag小于等于3的，这个Channel的，都会被确认,前提第二参数得传true*/
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        logger.info("com.direct.testDeadQueue死信消费者确认消息 : Map");
    }
    
}
