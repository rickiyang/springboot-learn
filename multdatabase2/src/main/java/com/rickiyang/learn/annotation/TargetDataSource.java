package com.rickiyang.learn.annotation;


import java.lang.annotation.*;

/**
 * @Auther: rickiyang
 * @Date: 2019/4/24 22:34
 * @Description:
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String dataSource() default "";
}
