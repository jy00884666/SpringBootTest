package com.springboot.config;

import com.springboot.annotations.ConcurrentAdvice;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/*AOP切面配置类*/

/**
 * 1.配置了@Component的类不会为其生成CGLIB代理class,类中@Bean注解的方法没有通过动态代理来代理,调用@Bean方法返回的是不同实例,
 * 需要使用@Autowired注解类解决多例问题
 *
 * 2.配置了@Configuration的类会为其生成CGLIB代理class,类中@Bean注解的方法会被包装成CGLIB的wrapper,调用@Bean方法返回的是相同实例
 * 其工作原理是:如果方式是首次被调用那么原始的方法体会被执行并且结果对象会被注册到Spring上下文中。
 */
@Configuration
/*启动SpringBoot时@Autowired ConcurrentAdvice不会报错,但是这里不导入的话编译会有一个错误,原因是ConcurrentAdvice类不在同级或者子目录下,又不想改IDEA配置,所以折中一下了*/
@Import(ConcurrentAdvice.class)
public class AnnotationsConcurrentConfig {
    
    private static Logger logger = LoggerFactory.getLogger(AnnotationsConcurrentConfig.class);
    
    @Autowired
    private ConcurrentAdvice concurrentAdvice;
    
    /**
     * Spring AOP中有两个PointcutAdvisor
     * ——RegexpMethodPointcutAdvisor和 NameMatchMethodPointcutAdvisor，它们都在org.springframework.aop.support包中，
     * 都可以过滤要拦截的方法，配置方法也大致相同，其中一个最主要的区别：
     * RegexpMethodPointcutAdvisor：需要加上完整的类名和方法名，例如：com.xw.methodname或com.*.methodname或.*methodname。
     * NameMatchMethodPointcutAdvisor：只需要方法名，不用加类名：*methodname。
     */
    @Bean
    public RegexpMethodPointcutAdvisor regexpMethodPointcutAdvisor() {
        RegexpMethodPointcutAdvisor regexpMethodPointcutAdvisor = new RegexpMethodPointcutAdvisor();
        // 基于正则表达式的方法拦截 ..*表示包或其子包下所有类
        regexpMethodPointcutAdvisor.setPatterns("com.springboot.service..*");
        // 切面处理类
        regexpMethodPointcutAdvisor.setAdvice(concurrentAdvice);
        return regexpMethodPointcutAdvisor;
    }
    
}
