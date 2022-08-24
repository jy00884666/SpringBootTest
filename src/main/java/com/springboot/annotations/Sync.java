/**
 * Copyright (c) 2015, ShangHai HOWBUY INVESTMENT MANAGEMENT Co., Ltd. All right reserved. THIS IS
 * UNPUBLISHED PROPRIETARY SOURCE CODE OF HOWBUY INVESTMENT MANAGEMENT CO., LTD. THE CONTENTS OF
 * THIS FILE MAY NOT BE DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN
 * PART, WITHOUT THE PRIOR WRITTEN PERMISSION OF HOWBUY INVESTMENT MANAGEMENT CO., LTD.
 */

package com.springboot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 并发控制的注解,自定义注解
 */
// Target 设置注解权限,定义此注解可以申明在哪里 METHOD:方法上,TYPE:类上
@Target({ElementType.METHOD, ElementType.TYPE})
// Retention生命周期 Runtime:运行时
@Retention(RetentionPolicy.RUNTIME)
/**在定义一个作用于类的注解时候，如果希望该注解也作用于其子类，那么可以用@Inherited 来进行修饰
 * 结论：被 @Inherited 注解修饰的注解，如果作用于某个类上，其子类是可以继承的该注解的。
 * 反之，如果一个注解没有被 @Inherited注解所修饰，那么他的作用范围只能是当前类，其子类是不能被继承的*/
@Inherited
public @interface Sync {
    
    /**
     * shashijie 2017-01-25 表示:同步锁的KEY值,若没有默认为方法名
     * @return String
     */
    public String name() default "";
    
    /**
     * shashijie 2017-01-25 表示:同步锁的key = 方法名+第几个入参的值,数组形式可以是多个
     * @return int[]
     */
    public int[] paramIndexs() default {};
    
}
