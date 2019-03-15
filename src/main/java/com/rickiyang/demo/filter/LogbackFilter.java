package com.rickiyang.demo.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author yangyue
 * @Date Created in 下午5:34 2019/3/15
 * @Modified by:
 * @Description:
 **/
@Component
@Slf4j
public class LogbackFilter implements Filter {

    private static final String UNIQUE_ID = "traceId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        boolean bInsertMDC = insertMDC();
        try {
            chain.doFilter(request, response);
        } finally {
            if (bInsertMDC) {
                MDC.remove(UNIQUE_ID);
            }
        }
    }

    private boolean insertMDC() {
        String uuid = UUID.randomUUID().toString();
        MDC.put(UNIQUE_ID, uuid);
        return true;
    }


    @Override
    public void destroy() {

    }
}
