package com.springboot;

import com.springboot.annotations.ConcurrentAdvice;
import com.springboot.bean.UserInfo;
import com.springboot.service.LambdaService;
import com.springboot.service.UserService;
import com.springboot.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PreDestroy;

/**
 * 放置在Springboot启动类上，表明该类是开启Springboot容器的入口，它是一个复合注解。里面包含了包扫描，自动注入，配置注入的功能
 * @SringBootConfiguration 声明配置类
 * @EnableAutoConfiguration 扫描读取.properties配置
 * @ComponentScan 扫描路径, 但只会扫描该类所在目录以及子目录, 如需手动指定则需要添加属性 (scanBasePackages = {"com.springboot", "com.utils"})
 */
@SpringBootApplication
public class Application {
    
    private static Logger logger = LoggerFactory.getLogger(Application.class);
    
    public static void main(String[] args) throws Exception {
        // 打印默认编码
        System.out.println("打印默认编码:" + System.getProperty("file.encoding"));
        
        // 启动类入口
        //ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        // 从 SpringBoot 容器中获取对象
        /*UserInfo userInfo = context.getBean(UserInfo.class);
        logger.info("userInfo={}", userInfo);*/
        
        
        
        // 从 SpringBoot 容器中获取对象
        //UserService userService = context.getBean(UserService.class);
        //logger.info("测试UserService:"+userService.test());
        /*---------------------测试并发-----------------------*/
        //userService.concurrentTest();
        
        
        
        /*-------------------手动关闭应用方法1-----------------------*/
        //logger.info("手动关闭springboot服务");
        //context.close();
        
        
        
        /*----------------匿名内部类写法----------------*/
        /*LambdaService lambdaService = new LambdaService() {
            public String outprint(Integer i, Integer j) throws Exception {
                return "匿名内部类写法" + String.valueOf(i + j);
            }
        };
        logger.info(lambdaService.outprint(1, 2));*/
        
        /*-----------------Lambda表达式写法-------------*/
        /**
         * 1、=右边的类型会根据左边的函数式接口类型自动推断；
         * 2、如果形参列表为空，只需保留()；
         * 3、如果形参只有1个，()可以省略，只需要参数的名称即可；
         * 4、如果执行语句只有1句，且无返回值，{}可以省略，若有返回值，则若想省去{}，则必须同时省略return，且执行语句也保证只有1句；
         * 5、形参列表的数据类型会自动推断；
         * 6、lambda不会生成一个单独的内部类文件；
         * 7、lambda表达式若访问了局部变量，则局部变量必须是final的，若是局部变量没有加final关键字，系统会自动添加，此后在修改该局部变量，会报错；
         * */
        /*LambdaService lambdaTest = (Integer i, Integer j) -> {
            return "Lambda表达式写法" + String.valueOf(i + j);
        };
        logger.info(lambdaTest.outprint(1, 2));*/
        
        /**
         * 方法引用： 可以快速的将一个Lambda表达式的实现指向一个已经实现的方法
         * 语法：方法的隶属者::方法名
         * 注意：
         * 1.引用的方法中，参数数量和类型一定要和接口中定义的方法一致
         * 2.返回值的类型也一定要和接口中的方法一致
         * */
        /*LambdaService lambdaService1 = lambdaTest::outprint;
        logger.info(lambdaService1.outprint(4, 5));*/
        

    }
    
    /**
     * 服务关闭的时候会销毁 Bean，此时就会调用 bean 中 PreDestroy 标识的方法
     */
    @PreDestroy
    public void shutdownApp() {
        logger.info("springboot应用关闭！");
    }
}
