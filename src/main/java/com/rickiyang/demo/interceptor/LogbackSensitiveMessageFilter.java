package com.rickiyang.demo.interceptor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import com.rickiyang.demo.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 过滤包含敏感信息的日志
 */
@Slf4j
@Component
public class LogbackSensitiveMessageFilter extends AbstractMatcherFilter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        boolean isMatche = StringUtil.hasPhoneNumber(event.getFormattedMessage());
        if (isMatche) {
            log.warn(String.format("[Sensitive Log]:%s", StringUtil.replacePhoneNumber(event.getFormattedMessage(),
                    "XXXXXXXXXXX")));
            return onMatch;
        }
        return onMismatch;
    }

}
