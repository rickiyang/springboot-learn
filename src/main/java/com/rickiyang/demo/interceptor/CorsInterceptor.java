package com.rickiyang.demo.interceptor;

import com.alibaba.fastjson.JSON;
import com.rickiyang.demo.common.BaseResponse;
import com.rickiyang.demo.common.CodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author yangyue
 * @description 跨域相关配置
 */
@Component
@Slf4j
public class CorsInterceptor implements HandlerInterceptor {

    private static final String ACCESS_URL = "https://www.baidu.com";

    private static final String OPTIONS = "OPTIONS";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String origin = request.getHeader("Origin");
        if (StringUtils.isBlank(origin)) {
            origin = ACCESS_URL;
        }
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Method", "POST,GET,OPTIONS");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "100");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Cache-Control,Pragma,Content-Type,token,clientType");
        String method = request.getMethod();
        log.info("当前请求method为：{}", method);
        if (OPTIONS.equalsIgnoreCase(method)) {
            log.info("options请求，返回成功");
            response.getWriter().write(JSON.toJSONString(BaseResponse.success(CodeEnum.SUCCESS)));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
