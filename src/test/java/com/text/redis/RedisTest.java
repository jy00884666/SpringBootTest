package com.text.redis;

import com.alibaba.fastjson.JSON;
import com.springboot.Application;
import com.springboot.bean.UserInfo;
import com.springboot.service.UserService;
import com.springboot.utils.RedisUtils;
import com.text.annotations.ConcurrentTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
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
public class RedisTest {
    
    private static Logger logger = LoggerFactory.getLogger(RedisTest.class);
    
    @Autowired
    private RedisUtils redisUtils;
    
    /*redis操作*/
    @Test
    public void redisTest() {
        long startTime = System.currentTimeMillis();
        redisUtils.set("a1", "测试");
        logger.info("redis存入数据key=a1,value=测试");
        logger.info("redis中获取a1={}", redisUtils.get("a1"));
        long time = System.currentTimeMillis() - startTime;
        logger.info("执行时间总共为:{}毫秒", time);
        
        // 获取不存在的key 返回null
        logger.info("redis中获取a2={}", redisUtils.get("a2"));
        
        // 存入对象
        UserInfo userInfo = new UserInfo();
        userInfo.setName("a1");
        userInfo.setAge(180);
        userInfo.setAddress("aaa");
        // 有效时间10秒
        redisUtils.set("userInfo", userInfo, 10L);
        logger.info("redis中获取userInfo={}", redisUtils.get("userInfo"));
        try {
            // 休息10秒
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            logger.error("线程", e);
        }
        // 超过有效时间,失效
        logger.info("redis中获取userInfo={}", redisUtils.get("userInfo"));
    }
}
