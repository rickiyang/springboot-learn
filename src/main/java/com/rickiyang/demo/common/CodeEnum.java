package com.rickiyang.demo.common;

import com.google.common.collect.Maps;

import java.util.Map;

public enum CodeEnum {

    SYSTEM_ERROR(-1, "系统异常"),
    SUCCESS(0, "成功"),
    PARAM_ERROR(1, "参数错误"),
    REPEAT_OPERATOR(2, "重复操作"),
    NOT_OPARETOR(3, "非法操作"),
    RESPONSE_NULL(4, "返回值异常");


    public static Map<Integer, CodeEnum> codeEnumMap = Maps.newHashMap();

    static {
        for (CodeEnum codeEnum : values()) {
            codeEnumMap.put(codeEnum.getCode(), codeEnum);
        }
    }

    private int code;
    private String message;

    CodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
