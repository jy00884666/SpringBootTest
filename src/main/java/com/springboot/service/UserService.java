package com.springboot.service;

import com.springboot.annotations.Sync;

public interface UserService {
    
    public String test() throws Exception;
    
    /*测试并发*/
    public void concurrentTest();
}
