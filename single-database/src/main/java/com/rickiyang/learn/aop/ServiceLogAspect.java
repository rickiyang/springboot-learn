package com.rickiyang.learn.aop;

import com.alibaba.fastjson.JSON;
import com.rickiyang.learn.common.LogBuffer;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 打印Service的方法调用
 */
@Aspect
@Component
@Log4j2
public class ServiceLogAspect {

    @Pointcut("execution(public * com.rickiyang.learn..*Service.*(..))")
    public void serviceImplPoint() {
    }

    @Around("serviceImplPoint()")
    public Object doServiceAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object[] params = joinPoint.getArgs();
        LogBuffer lb1 = LogBuffer.create(null);
        lb1.append("className", joinPoint.getSignature().getDeclaringTypeName());
        lb1.append("methodName", joinPoint.getSignature().getName());
        lb1.append("params", Arrays.toString(params));
        log.info(lb1.toString());
        Object result = joinPoint.proceed();
        LogBuffer lb2 = LogBuffer.create(null);
        lb2.append("className", joinPoint.getSignature().getDeclaringTypeName());
        lb2.append("methodName", joinPoint.getSignature().getName());
        lb2.append("result", JSON.toJSONString(result));
        lb2.append("耗时", (System.currentTimeMillis() - startTime) + "(ms)");
        log.info(lb2.toString());
        return result;
    }

}
