package com.springboot.service.impl;

import com.springboot.service.UserService;
import org.springframework.stereotype.Component;

/*把普通pojo实例化到spring容器中,相当于配置文件中的 <bean id="" class=""/> */
@Component
public class UserServiceImpl implements UserService {
    
    public String test() {
        return "userService.test()";
    }
}
