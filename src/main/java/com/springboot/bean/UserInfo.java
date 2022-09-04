package com.springboot.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserInfo {
    
    private Integer id;
    
    private String name;
    
    private String address;
    
    private Integer age;
    
}
