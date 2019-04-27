package com.rickiyang.learn.exception;

/**
 * @ClassName: BizException
 * @Description: 业务异常类
 *               逻辑service层处理系统异常后转化成业务异常，需要在controller层统一捕获处理、转化成BaseResponse对象返回给前段
 *
 */
public class BizException extends RuntimeException {

	/**
	 * 序列化标识
	 */
	private static final long serialVersionUID = 1L;


	private int code;

	public BizException(String msg) {
		super(msg);
	}

	public BizException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public BizException(int code, String msg, Exception e) {
		super(msg, e);
		this.code = code;
	}

	/**
	 * 
	 * setters && getters
	 */
	public int getCode() {
		return code;
	}
}