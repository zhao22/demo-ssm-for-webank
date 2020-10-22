package com.seanzx.common;

/**
 * 公用异常
 * @author zhaoxin
 * @date 2020/10/22
 */
public class ApplicationException extends RuntimeException{

    public static final String UNEXPECT_ERROR_MESSAGE = "系统异常，请联系管理员";

    public ApplicationException() {
        super(UNEXPECT_ERROR_MESSAGE);
    }

    public ApplicationException(String message) {
        super(message);
    }
}
