package com.wfmyzyz.user.enums;

/**
 * @author admin
 */
public enum  ResponseEnum {
    /**
     * 成功
     */
    SUCCESS(200,"成功"),
    /**
     * 失败
     */
    FAIL(0,"失败"),
    /**
     * 需要登录
     */
    LOGIN(206,"请登录"),
    /**
     * 没有权限
     */
    POWER(208,"没有权限");

    private final int code;
    private final String msg;

    ResponseEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return this.code;
    }

    public String getMsg(){
        return this.msg;
    }
}
