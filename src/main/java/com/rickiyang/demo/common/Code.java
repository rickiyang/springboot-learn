package com.rickiyang.demo.common;

/**
 * @ClassName: Code
 * @Description: 返回码枚举
 */
public enum Code {

    /**
     * 成功。
     */
    SUCCESS(0),

    /**
     * 失败。
     */
    FAIL(1),

    /**
     * token无效。
     */
    TOKEN_INVALID(2),

    /**
     * 参数为空。
     */
    PARAMATERSISNULL(3),

    /**
     * 系统异常
     */
    SYSTEMEXCEPTION(4),
    /**
     * 无权限操作
     */
    AUTH_ENABLE(5),
    /**
     * 操作权限校验失败
     */
    AUTH_FAIED(6);

    private int value;

    private Code(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
