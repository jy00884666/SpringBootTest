package com.springboot.rabbitmq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/*消息生产者*/
@Component
/*代表返回的是json格式的数据，这个注解是Spring4之后新加的注解，原来返回json格式的数据需要@ResponseBody配合@Controller一起使用*/
@RestController
public class RabbitmqProducer {
    
    private static Logger logger = LoggerFactory.getLogger(RabbitmqProducer.class);
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /*发送消息*/
    public void send(String dataId, String exchangeName, String rountingKey, Object message) {
        // 这里可以用子类继承CorrelationData将message信息包裹进去
        CorrelationData correlationData = new CorrelationData();
        // 消息id
        correlationData.setId(dataId);
        // 发送,exchangeName:交换机名称,rountingKey:消息队列与交换机绑定的BindingKey,message:消息内容,correlationData=消息id
        rabbitTemplate.convertAndSend(exchangeName, rountingKey, message, correlationData);
    }
    
    /** 等同于@RequestMapping(value = “/sendTopicMessage”, method = RequestMethod.GET)
     * 访问地址 http://localhost:8080/sendTopicMessage   */
    @GetMapping("/sendTopicMessage")
    public String sendTopicMessage() {
        /**
         * 入参1:消息id
         * 入参2:交换机名称
         * 入参3:消息队列与交换机绑定的BindingKey
         * 入参4:消息内容
         * */
        send("发送消息id1", "com.topic.TestExchange", "com.topic.testRountingKey1", "消息内容a1");
        send("发送消息id2", "com.topic.TestExchange", "com.topic.testRountingKey2", "消息内容a2");
        /*单对单生产消费消息*/
        send("发送消息id3", "com.direct.TestExchange", "com.direct.testRountingKey", "消息内容a3");
        
        /*MAP入参*/
        Map map = new HashMap<>();
        map.put("id", "1111");
        map.put("name", "消息内容a4");
        send("发送消息id4", "com.topic.TestExchange", "com.topic.testRountingKey3", map);
        return "消息发送OK";
    }
}