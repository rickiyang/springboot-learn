package com.rickiyang.demo.common;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class BaseResponse implements Serializable {

    private static final long serialVersionUID = -123L;

    protected Integer code;
    protected String message;
    protected Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static BaseResponse fail(CodeEnum codeEnum) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(codeEnum.getCode());
        baseResponse.setMessage(codeEnum.getMessage());
        return baseResponse;
    }

    public static BaseResponse fail(Integer code, String message) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(code);
        baseResponse.setMessage(message);
        return baseResponse;
    }

    public static BaseResponse fail(Integer code, String message, Object data) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(code);
        baseResponse.setMessage(message);
        baseResponse.setData(data);
        return baseResponse;
    }


    public static BaseResponse success(CodeEnum codeEnum, Object data) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(codeEnum.getCode());
        baseResponse.setMessage(codeEnum.getMessage());
        baseResponse.setData(data);
        return baseResponse;
    }

    public static BaseResponse success(Object data) {
        return success(CodeEnum.SUCCESS, data);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("code", code)
                .append("message", message)
                .append("data", data)
                .toString();
    }
}
