package com.springboot.config;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/*AOP切面配置类*/

/**
 * 1.配置了@Component的类不会为其生成CGLIB代理class,类中@Bean注解的方法没有通过动态代理来代理,调用@Bean方法返回的是不同实例,
 * 需要使用@Autowired注解类解决多例问题
 *
 * 2.配置了@Configuration的类会为其生成CGLIB代理class,类中@Bean注解的方法会被包装成CGLIB的wrapper,调用@Bean方法返回的是相同实例
 * 其工作原理是:如果方式是首次被调用那么原始的方法体会被执行并且结果对象会被注册到Spring上下文中。
 */
@Configuration
/**
 * @Aspect:作用是把当前类标识为一个切面供容器读取
 * @Pointcut（切入点）：Pointcut是植入Advice的触发条件。每个Pointcut的定义包括2部分，
 *             一是表达式，二是方法签名。方法签名必须是 public及void型。可以将Pointcut中的方法看作是一个被Advice
 *             引用的助记符，因为表达式不直观，因此我们可以通过方法签名的方式为 此表达式命名。
 *             因此Pointcut中的方法只需要方法签名，而不需要在方法体内编写实际代码。
 * @Around：环绕增强，目标方法执行前后，都进行增强,相当于MethodInterceptor
 * @AfterReturning返回通知：在方法正常结束后,返回结果之后执行,可以访问方法的返回值，相当于AfterReturningAdvice，方法正常退出时执行
 * @Before前置通知：在增强方法调用之前实现，例如我想对一个aop()方法进行增强，那么前置通知方法就是会在aop()方法之前执行
 * @AfterThrowing异常通知：在方法抛出异常之后，相当于ThrowsAdvice
 * @After后置通知:方法最后执行完毕的时候执行的，不管方法有没有发生异常，它都会执行
 * Joinpoint（连接点）：连接点就是我们想要去增强的方法，该方法就是一个连接点
 */
@Aspect
public class AopLogConfig {
    
    private static Logger logger = LoggerFactory.getLogger(AopLogConfig.class);
    
    // 切入点配置和切入点表达式,(第一个*表示返回值,第二个*表示任意类,第三个*表示任意方法,(..)表示任意参数)
    @Pointcut(value = "execution(* com.springboot.service.*.*(..))")
    public void demo() {
    }
    
    // 前置通知
    @Before("demo()")
    public void before(JoinPoint joinPoint) {
        logger.info("before开始执行joinPoint={}", JSON.toJSONString(joinPoint.getSignature()));
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        logger.info("[注解-前置通知]:" + className + "类的" + methodName + "方法开始了," +
                "方法的参数值为:" + JSON.toJSONString(joinPoint.getArgs()));
    }
    
    // 后置通知
    @After("demo()")
    public void after(JoinPoint joinPoint) {
        logger.info("after开始执行joinPoint={}", JSON.toJSONString(joinPoint.getSignature()));
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        logger.info("[注解-后置通知]:" + className + "类的" + methodName + "不管是否正常执行,一定会返回的");
    }
    
    // 返回通知
    @AfterReturning(value = "demo()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        logger.info("afterReturning开始执行joinPoint={}", JSON.toJSONString(joinPoint.getSignature()));
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        logger.info("[注解-返回通知]:" + className + "类的" + methodName + "方法正常结束了,返回值是" + result);
    }
    
    // 异常通知
    @AfterThrowing(value = "demo()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        logger.info("afterThrowing开始执行joinPoint={}", JSON.toJSONString(joinPoint.getSignature()));
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        logger.info("[注解-异常通知]:" + className + "类的" + methodName + "方法执行时遇见异常了" + e.getMessage(), e);
    }
    
    // 环绕通知
    @Around("demo()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.info("==============================开始记录日志==start=============================");
        // ----------------前置---------------
        long startTime = System.currentTimeMillis();
        logger.info("环绕通知前置proceedingJoinPoint={}", JSON.toJSONString(proceedingJoinPoint.getSignature()));
        
        // getSignature());是获取到这样的信息 :修饰符+ 包名+组件名(类名) +方法名
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        // 获取执行方法实例
        Method method = methodSignature.getMethod();
        // 判断方法上是否存在注解，并获取方法上面注解的实例
        if (method.isAnnotationPresent(Component.class)) {
            // getAnnotation:方法如果存在这样的注释，则返回指定类型的元素的注释，否则为null
            Component logAnnotation = method.getAnnotation(Component.class);
            logger.info("注解测试value:{}", logAnnotation.value());
            logger.info("注解测试class:{}", logAnnotation.getClass());
        }
        // proceedingJoinPoint.getTarget():获取切入点所在目标对象
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        // 方法名称
        String methodName = methodSignature.getName();
        logger.info("[注解-环绕通知]：{}", className + "类的." + methodName + "()方法准备开始执行");
        // 这里返回的是切入点方法的参数列表
        Object[] args = proceedingJoinPoint.getArgs();
        if (args.length > 0) {
            String params = JSON.toJSONString(args[0]);
            logger.info("请求的参数是:{}", params);
        } else {
            logger.info("没有入参");
        }
        
        //-------------------执行----------------
        Object result = null;
        /**
         * 环绕通知=前置+目标方法执行+后置，proceed方法就是用于启动目标方法执行的
         * Proceedingjoinpoint 继承了 JoinPoint。是在JoinPoint的基础上暴露出 proceed 这个方法。proceed很重要，这个是aop代理链执行的方法。
         * 暴露出这个方法，就能支持 aop:around 这种切面（而其他的几种切面只需要用到JoinPoint，，这也是环绕通知和前置、后置通知方法的一个最大区别。这跟切面类型有关）
         * */
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable th) {
            logger.error("环绕异常通知:", th);
            //throw new Throwable(th);
        } finally {
            // ----------------------后置-------------------
            long time = System.currentTimeMillis() - startTime;
            logger.info("环绕通知后置result={}", JSON.toJSONString(result));
            logger.info("执行时间总共为:{}毫秒", time);
            logger.info("===============================结束记录日志==end===================================");
        }
        
        return result;
    }
    
}
