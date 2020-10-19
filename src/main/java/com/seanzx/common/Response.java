package com.seanzx.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.PageInfo;
import com.seanzx.enums.ResponseCode;

import java.util.List;

/**
 * 返回结果封装
 * @author zhaoxin
 * @date 2019/12/16
 */
public class Response<T> {

    /**
     * 结果代码
     */
    private final String code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 结果数据
     */
    private T data;

    private Response(String code) {
        this.code = code;
    }

    private Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private Response(String code, T data) {
        this.code = code;
        this.data = data;
    }

    public Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Response ofSuccess() {
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    public static <T>Response<T> ofSuccess(T data) {
        return new Response<>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static Response ofError(ResponseCode responseCode, String message) {
        return new Response<>(responseCode.getCode(), message);
    }

    public static <T> Response<T> ofError(ResponseCode responseCode, String message, T data) {
        return new Response<>(responseCode.getCode(), message, data);
    }


    public static <T>Response<Page<T>> ofPage(PageInfo<T> pageInfo) {
        return new Response<>(ResponseCode.SUCCESS.getCode(), new Page<T>(pageInfo.getTotal(), pageInfo.getList()));
    }

    public static <T> Response<Page<T>> ofPage(long total, List<T> data) {
        return new Response<>(ResponseCode.SUCCESS.getCode(), new Page<>(total, data));
    }



    @JsonIgnore
    public boolean isSuccess() {
        return ResponseCode.SUCCESS.getCode().equals(this.code);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
