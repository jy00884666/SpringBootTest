package com.text.rabbitmq;

import com.springboot.Application;
import com.springboot.config.RabbitmqConfig;
import com.springboot.rabbitmq.consumer.direct.RabbitmqConsumerDirectOnly;
import com.springboot.rabbitmq.consumer.simple.SimpleRabbitmqConsumer;
import com.springboot.rabbitmq.consumer.topic.RabbitmqConsumerTopicOnly;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/*消息发送者测试*/
/*让 Junit运行Spring的测试环境,得到 Spring环境的上下文的支持*/
@RunWith(SpringRunner.class)
/* 主启动类名,使用webEnvironment配置WEB环境时,就必须加上@SpringBootApplication注解
 * 或者@SpringBootConfiguration 或者 @EnableAutoConfiguration 两个注解
 *
 * webEnvironment配置web环境,如下:
 * SpringBootTest.WebEnvironment.DEFINED_PORT:固定端口，与application.yml配置一致
 * SpringBootTest.WebEnvironment.NONE: (默认状态)不是web环境
 * SpringBootTest.WebEnvironment.RANDOM_PORT:随机端口*/
@SpringBootTest(classes = {RabbitmqConsumerTest.class}/*, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT*/)
/*表名Spirng容器启动类*/
/*@SpringBootApplication*/
public class RabbitmqConsumerTest {
    
    private static Logger logger = LoggerFactory.getLogger(RabbitmqConsumerTest.class);
    
    /**
     * 消费消息测试:因为RabbitmqConsumerDirect与RabbitmqConsumerTopic在同一个目录下,所以@SpringBootApplication无论写在哪个
     * 类上Spring启动时会同时启动这两个消费者,SpringApplication.run(Application.class)也不行,SpringBoot约定大于配置,会扫描
     * Application类及子目录下所有类文件
     */
    @Test
    public void consumerTest() {
        //---------------------------RabbitMQ--启动消息消费者------------------------------
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
    }
    
    /*----------------只启动某个消费者,类的包路径必须隔离开,而且类上需要有 @SpringBootApplication注解---------------*/
    @Test
    public void consumerTopicOnlyTest() {
        // 只启动-topic-消息消费者
        ConfigurableApplicationContext context = SpringApplication.run(RabbitmqConsumerTopicOnly.class);
    }
    
    @Test
    public void consumerDirectOnlyTest() throws Exception {
        // 只启动-Direct-消息消费者
        ConfigurableApplicationContext context = SpringApplication.run(RabbitmqConsumerDirectOnly.class);
    }
    
    /*------------------------------------手动确认消息-------------------------------*/
    @Test
    public void simpleRabbitmqConsumerTest() {
        ConfigurableApplicationContext context = SpringApplication.run(SimpleRabbitmqConsumer.class);
    }
    
}
