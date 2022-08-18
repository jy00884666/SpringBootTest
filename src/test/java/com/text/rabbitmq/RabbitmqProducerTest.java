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
/*让 JUnit 运行 Spring 的测试环境， 得到 Spring 环境的上下文的支持*/
@RunWith(SpringRunner.class)
/*主启动类名*/
@SpringBootTest(classes = {RabbitmqConfig.class, RabbitmqProducer.class})
public class RabbitmqProducerTest {
    
    private static Logger logger = LoggerFactory.getLogger(RabbitmqProducerTest.class);
    
    /*消息生产者*/
    @Autowired
    private RabbitmqProducer producer;
    
    /**
     * 发送消息测试,junit测试时不能在外层(如 com/springboot/... 或 com/springboot/consumer/...)启动类上写
     * @SpringBootTest(classes = Application.class),因为SpringBoot启动加载时会同时扫描这样就会创建消息消费者,
     * 如要使用junit单独启动某个消费者,需要将启动类新建在深一点的包路径下,参考 RabbitmqConsumerTest例子
     */
    @Test
    public void sendTest() {
        /**
         * 入参1:消息id
         * 入参2:交换机名称
         * 入参3:消息队列与交换机绑定的BindingKey
         * 入参4:消息内容
         * */
        producer.send("com.topic.TestExchange", "com.topic.testRountingKey1", "消息内容a1", "发送消息id1");
        producer.send("com.topic.TestExchange", "com.topic.testRountingKey2", "消息内容a2", "发送消息id2");
        /*------------------单对单生产消费消息--------------*/
        producer.send("com.direct.TestExchange", "com.direct.testRountingKey", "消息内容a3", "发送消息id3");
        
        /*MAP入参*/
        Map map = new HashMap();
        map.put("id", "111");
        map.put("name", "消息内容a4");
        producer.send("com.topic.TestExchange", "com.topic.testRountingKey4", map, "发送消息id4");
        map.clear();
        map.put("id", "222");
        map.put("name", "消息内容a5");
        producer.send("com.direct.TestExchange", "com.direct.testRountingKey", map, "发送消息id5");
        
        /*----------手动消息确认----死信消息例子----------*/
        producer.send("com.direct.testManualExchange", "com.direct.testManualRountingKey", "消息内容a6", "发送消息id6");
        map.clear();
        map.put("id", "777");
        map.put("name", "消息内容a7");
        producer.send("com.direct.testManualExchange", "com.direct.testManualRountingKey", map, "发送消息id7");
        
    }
    
}
