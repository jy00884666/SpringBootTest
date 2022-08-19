package com.springboot.rabbitmq.consumer.direct;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Map;

/*消息消费者  Direct 单对单模式 */
@Component
/*监听哪个队列,queues:队列名称,可以监听多个消息队列,用数组*/
@RabbitListeners({@RabbitListener(queues = "com.direct.testQueue")})
/*表名Spirng容器启动类,为了方便junit启动*/
@SpringBootApplication
public class RabbitmqConsumerDirectOnly {
    
    private static Logger logger = LoggerFactory.getLogger(RabbitmqConsumerDirectOnly.class);
    
    @RabbitHandler
    public void process(String testMessage, Message message) {
        System.out.println("direct消费者收到消息only: testMessage=" + testMessage.toString());
        // 消息的编号
        long msgTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("direct消费者收到消息only: msgTag=" + msgTag);
        System.out.println("direct消费者收到消息only: message=" + message.toString());
    }
    
    @RabbitHandler
    public void process(Map testMessage, Message message) {
        System.out.println("direct消费者收到消息only: Map=" + JSON.toJSONString(testMessage));
        // 消息的编号
        long msgTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("direct消费者收到消息only: msgTag=" + msgTag);
        System.out.println("direct消费者收到消息only: message=" + message.toString());
    }
    
}
