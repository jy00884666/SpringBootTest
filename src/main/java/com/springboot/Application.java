package com.springboot;

import com.springboot.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/*放置在Springboot启动类上，表明该类是开启Springboot容器的入口，它是一个复合注解。里面包含了包扫描，自动注入，配置注入的功能，下面就给大家介绍里面的注解*/
@SpringBootApplication
/*扫描路径,加载Bean到Spring容器中,basePackages = {"com.springtest", "com.springboottest"}*/
@ComponentScan(basePackages = "com.springboot.service")
public class Application {
    
    public static void main(String[] args) {
        // 启动类入口
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class);
        // 从 SpringBoot 容器中获取对象
        UserService userService = applicationContext.getBean(UserService.class);
        System.out.println(userService.test());
    }
    
}
