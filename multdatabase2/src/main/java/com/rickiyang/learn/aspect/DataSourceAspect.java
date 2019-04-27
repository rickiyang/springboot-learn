package com.rickiyang.learn.aspect;


import com.rickiyang.learn.annotation.TargetDataSource;
import com.rickiyang.learn.config.dbConfig.DataSourceTypeManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Auther: rickiyang
 * @Date: 2019/4/24 23:13
 * @Description:
 */
@Aspect
@Component
public class DataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Around("execution(public * com.rickiyang.learn.service..*.*(..))")
    public Object around(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method targetMethod = methodSignature.getMethod();
        if (targetMethod.isAnnotationPresent(TargetDataSource.class)) {
            String targetDataSource = targetMethod.getAnnotation(TargetDataSource.class).dataSource();
            DataSourceTypeManager.setDataSource(targetDataSource);
        }
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            logger.error("DataSourceAspect exception",throwable);
            return null;
        }
    }
}
