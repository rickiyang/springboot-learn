package com.rickiyang.learn.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * 过滤器 对每一个进来的方法统计调用时间
 */
@Component
@Slf4j
public class MethodCostFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        log.info("method = {},cost = {} ms", request.getRemoteHost(), System.currentTimeMillis() - start);
    }

    @Override
    public void destroy() {

    }
}
