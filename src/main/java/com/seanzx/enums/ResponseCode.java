package com.seanzx.enums;

/**
 * 返回代码枚举
 * @author zhaoxin
 * @date 2020/10/19
 */
public enum ResponseCode {

    /**
     * 成功
     */
    SUCCESS("0000"),
    /**
     * 参数错误
     */
    ILLEGAL_ARGUMENTS("0001"),
    /**
     * 缺少登录信息
     */
    NEED_LOGIN("0002"),
    /**
     * 未知异常
     */
    UNEXPECTED_ERROR("0003");

    private String code;

    ResponseCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
