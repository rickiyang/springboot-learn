package com.rickiyang.learn.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author yangyue
 * @Date Created in 下午3:12 2019/3/18
 * @Modified by:
 * @Description: 拦截器的统一配置入口
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(CorsInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(TraceInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public CorsInterceptor CorsInterceptor() {
        return new CorsInterceptor();
    }

    @Bean
    public TraceInterceptor TraceInterceptor() {
        return new TraceInterceptor();
    }
}
