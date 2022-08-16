package com.springboot.rabbitmq.consumer;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.stereotype.Component;

import java.util.Map;

/*消息消费者  topic 正则模式 */
@Component
/*监听哪个队列  队列名称*/
@RabbitListener(queues = "com.topic.testQueue")
public class RabbitmqConsumerTopic {
    
    private static Logger logger = LoggerFactory.getLogger(RabbitmqConsumerTopic.class);
    
    @RabbitHandler
    public void process(String testMessage, Message message) {
        System.out.println("topic消费者收到消息 : testMessage=" + testMessage.toString());
        // 消息的编号
        long msgTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("topic消费者收到消息 : msgTag=" + msgTag);
        System.out.println("topic消费者收到消息 : message=" + message.toString());
    }
    
    @RabbitHandler
    public void process(Map testMessage, Message message) {
        System.out.println("topic消费者收到消息 : Map=" + JSON.toJSONString(testMessage));
        // 消息的编号
        long msgTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("topic消费者收到消息 : msgTag=" + msgTag);
        System.out.println("topic消费者收到消息 : message=" + message.toString());
    }
    
}
