package com.rickiyang.learn.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 拦截所有请求，在请求的开始阶段放入生成的traceId，在请求处理结束后
 * 将MDC中的traceId清理掉
 */
@Component
public class TraceInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse reponse, Object arg2, Exception arg3)
			throws Exception {
		MDC.clear();
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		String traceId = UUID.randomUUID().toString();
		if(StringUtils.isNotBlank(traceId)){
			MDC.put("traceId", traceId);
		}
		return true;
	}

}
