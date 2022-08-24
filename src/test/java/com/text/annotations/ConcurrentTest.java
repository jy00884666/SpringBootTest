package com.text.annotations;

import com.springboot.Application;
import com.springboot.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
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
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConcurrentTest {
    
    private static Logger logger = LoggerFactory.getLogger(ConcurrentTest.class);
    
    /*非并发*/
    @Test
    public void myTest() throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        UserService userService = context.getBean(UserService.class);
        userService.test();
    }
    
    /*测试并发*/
    @Test
    public void consumerTest() {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        UserService userService = context.getBean(UserService.class);
        userService.concurrentTest();
    }
    
}
