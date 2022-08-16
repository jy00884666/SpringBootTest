package com.text.rabbitmq;

import com.springboot.config.RabbitmqConfig;
import com.springboot.rabbitmq.producer.RabbitmqProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/*消息发送者测试*/
@RunWith(SpringRunner.class)
/*主启动类名*/
@SpringBootTest(classes = {RabbitmqConfig.class, RabbitmqProducer.class})
public class RabbitmqProducerTest {
    
    private static Logger logger = LoggerFactory.getLogger(RabbitmqProducerTest.class);
    
    @Autowired
    private RabbitmqProducer producer;
    
    /*发送消息测试,junit测试时不能写@SpringBootTest(classes = Application.class),因为类启动加载会同时创建消息消费者*/
    @Test
    public void sendTest() {
        /**
         * 入参1:消息id
         * 入参2:交换机名称
         * 入参3:消息队列与交换机绑定的BindingKey
         * 入参4:消息内容
         * */
        producer.send("发送消息id1", "com.topic.TestExchange", "com.topic.testRountingKey1", "消息内容a1");
        producer.send("发送消息id2", "com.topic.TestExchange", "com.topic.testRountingKey2", "消息内容a2");
        /*单对单生产消费消息*/
        producer.send("发送消息id3", "com.direct.TestExchange", "com.direct.testRountingKey", "消息内容a3");
        
        /*MAP入参*/
        Map map = new HashMap();
        map.put("id", "1111");
        map.put("name", "消息内容a3");
        producer.send("发送消息id3", "com.topic.TestExchange", "com.topic.testRountingKey3", map);
        
    }
    
}
