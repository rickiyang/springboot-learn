package com.rickiyang.learn.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注册过滤器
 * FilterRegistrationBean 可以设置过滤器的使用顺序
 * 相当于
 */
@Configuration
public class RegistrationConfigure {

    @Autowired
    LogbackFilter logbackFilter;
    @Autowired
    MethodCostFilter methodCostFilter;

    @Bean
    public FilterRegistrationBean<MethodCostFilter> actuatorFilterRegistration() {
        FilterRegistrationBean<MethodCostFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(methodCostFilter);
        registration.addUrlPatterns(
                "/*"
        );
        registration.setName("methodCostFilter");
        registration.setOrder(1);
        return registration;
    }


    @Bean
    public FilterRegistrationBean<LogbackFilter> logbackFilterRegistration() {
        FilterRegistrationBean<LogbackFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(logbackFilter);
        registration.addUrlPatterns(
                "/*"
        );
        registration.setName("logbackFilter");
        registration.setOrder(2);
        return registration;
    }
}
