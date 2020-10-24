package com.seanzx.common;

import com.seanzx.common.response.ApplicationException;
import com.seanzx.common.response.Response;
import com.seanzx.enums.ResponseCode;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 全局异常封装
 * @author zhaoxin
 * @date 2020/10/22
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    /**
     * Exception 类捕获 500 异常处理
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public HttpEntity<Response> handlerException(Exception e) {
        String errorMessage;
        if (e instanceof ApplicationException) {
            errorMessage = e.getMessage();
        } else {
            errorMessage = ApplicationException.UNEXPECT_ERROR_MESSAGE;
        }
        return new HttpEntity<>(Response.ofError(ResponseCode.UNEXPECTED_ERROR, errorMessage));
    }
}
