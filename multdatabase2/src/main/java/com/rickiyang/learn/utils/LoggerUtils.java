package com.rickiyang.learn.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志调用工具类
 */

public class LoggerUtils {
	
	private static Logger infoLogger = LoggerFactory.getLogger("info.do");	  //存储info日志
	private static Logger errorLogger = LoggerFactory.getLogger("error.do");  //存储error和exception日志
    private static Logger remoteLogger = LoggerFactory.getLogger("remote.do");//调用第三方接口的日志都打印在该日志中
    
	public static void info(String content){
		infoLogger.info(content);
	}
	
	public static void info(String format,Object...objects){
		infoLogger.info(format, objects);
	}
	
	public static void error(String content){
		errorLogger.error(content);
	}
	
	public static void error(String format,Object...objects){
		errorLogger.error(format,objects);
	}

	public static void error(String content, Throwable throwable) {
		errorLogger.error(content, throwable);
	}
	
	public static void exception(Throwable e){
		if(e == null){
			return;
		}
		errorLogger.error(getExceptionInfo(null, e));
	}

    public static void exception(String error, Throwable e){
        if(e == null){
            return;
        }
        errorLogger.error(getExceptionInfo(error, e));
    }
    
    public static void remoteLog(String content){
    	remoteLogger.info(content);
    }
    
    public static void remoteLog(String format,Object...objects){
    	remoteLogger.info(format, objects);
    }
    
	private static String getExceptionInfo(String description, Throwable e) {
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isNotBlank(description)) {
			sb.append(description).append("\n");
		}
		sb.append(e.getMessage()).append("\n");
		StackTraceElement[] traces = e.getStackTrace(); 
		 for(int i=0;i<traces.length;i++){
			 if(i != traces.length - 1){
			 sb.append(traces[i].toString().trim()).append("\n");
			 }
		 }
		return sb.toString();
	}
   
}
