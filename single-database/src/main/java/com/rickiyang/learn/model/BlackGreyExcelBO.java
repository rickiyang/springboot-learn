package com.rickiyang.learn.model;

import com.rickiyang.learn.aop.BeanFieldAnnotation;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: rickiyang
 * @date: 2019/03/16
 * @description: 定义Field顺序
 * BeanFieldAnnotation 注解用来标识该字段在Excel中的列位置
 */

@Data
@NoArgsConstructor
public class BlackGreyExcelBO {

    @BeanFieldAnnotation(order = 1)
    private String value;

    @BeanFieldAnnotation(order = 2)
    private String dateType;

    @BeanFieldAnnotation(order = 3)
    private String category;

    @BeanFieldAnnotation(order = 4)
    private String cheatType;

    @BeanFieldAnnotation(order = 5)
    private String fromSource;

    @BeanFieldAnnotation(order = 6)
    private String referItem;

    @BeanFieldAnnotation(order = 7)
    private String orderId;

    @BeanFieldAnnotation(order = 8)
    private Date expireTime;

}
