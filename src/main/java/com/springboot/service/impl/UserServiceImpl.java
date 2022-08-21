package com.springboot.service.impl;

import com.springboot.annotations.Sync;
import com.springboot.rabbitmq.consumer.topic.RabbitmqConsumerTopicOnly;
import com.springboot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*把普通pojo实例化到spring容器中,相当于配置文件中的 <bean id="" class=""/> */
@Component
public class UserServiceImpl implements UserService {
    
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Override
    public String test() throws Exception {
        logger.info("执行了userServiceImpl的test()方法");
        if (true) {
            //throw new NullPointerException("抛个异常玩玩");
        }
        return "userService.test()";
    }
    
    /*测试并发*/
    @Sync
    @Override
    public void concurrentTest() {
        logger.info("执行了concurrentTest()方法");
    }
}
