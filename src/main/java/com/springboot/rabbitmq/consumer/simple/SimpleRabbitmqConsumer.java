package com.springboot.rabbitmq.consumer.simple;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/*消息消费者  topic 正则模式 */
@Component
/*监听哪个队列  队列名称*/
@RabbitListener(queues = "com.direct.testManualQueue", containerFactory = "simpleRabbitListenerContainerFactory")
/*表名Spirng容器启动类*/
@SpringBootApplication
public class SimpleRabbitmqConsumer {
    
    private static Logger logger = LoggerFactory.getLogger(SimpleRabbitmqConsumer.class);
    
    /*如果不手动确认消息的话,消息将一直停留在消息队列中,等待消费即使已经被消费过了*/
    @RabbitHandler
    public void process(String testMessage, Message message, Channel channel) throws IOException {
        logger.info("com.direct.testManualQueue消费者收到消息 : testMessage=" + testMessage);
        // 手动确认,入参1:消息唯一标识,入参2:是否批量确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
    
    @RabbitHandler
    public void process(Map testMessage, Message message, Channel channel) throws IOException {
        logger.info("com.direct.testManualQueue消费者收到消息 : Map=" + JSON.toJSONString(testMessage));
        // 手动确认,入参1:消息唯一标识,入参2:是否批量确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
    
}
