package com.text.rabbitmq;

import com.springboot.Application;
import com.springboot.config.RabbitmqConfig;
import com.springboot.rabbitmq.consumer.RabbitmqConsumerTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/*消息发送者测试*/
@RunWith(SpringRunner.class)
/*主启动类名*/
@SpringBootTest(classes = {RabbitmqConsumerTest.class})
public class RabbitmqConsumerTest {
    
    private static Logger logger = LoggerFactory.getLogger(RabbitmqConsumerTest.class);
    
    /**消费消息测试:因为RabbitmqConsumerDirect与RabbitmqConsumerTopic在同一个目录下,所以@SpringBootApplication无论写在哪个
     * 类上Spring启动时会同时启动这两个消费者,SpringApplication.run(Application.class)也不行,SpringBoot约定大于配置,会扫描
     * Application类及子目录下所有类文件*/
    @Test
    public void consumerTest() {
        //---------------------------RabbitMQ--启动消息消费者------------------------------
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        RabbitmqConsumerTopic producer = context.getBean(RabbitmqConsumerTopic.class);
        logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~producer=" + producer);
    }
    
}
