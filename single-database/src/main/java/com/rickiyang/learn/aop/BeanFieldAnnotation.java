package com.rickiyang.learn.aop;

import java.lang.annotation.*;

/**
 * @author: yangyue
 * @date: 2019/03/16
 * @description: 定义Field顺序
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface BeanFieldAnnotation {

    /**
     * 标注Field顺序，规定从1开始且小数在前
     * @return
     */
    int order();

}
