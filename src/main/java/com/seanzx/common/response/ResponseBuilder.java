package com.seanzx.common.response;

import com.seanzx.enums.ResponseCode;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 参数校验类
 * 链式调用时，
 * 若参数都符合假定条件，将返回 SUCCESS RESPONSE
 * 若参数不符合假定条件，将返回 ILLEGAL ARGUMENTS RESPONSE，其中错误信息为第一个不符条件设置的信息
 * @author zhaoxin
 * @date 2019/12/17
 */
public class ResponseBuilder {

    private Response response;

    /**
     * 假定对象不为NULL
     */
    public ResponseBuilder assertNotNull(Object parameter, String errorMessage) {
        setResponseByAssertion(parameter != null, errorMessage);
        return this;
    }

    /**
     * 假定 String 不为 empty
     */
    public ResponseBuilder assertNotEmpty(String parameter, String errorMessage) {
        setResponseByAssertion(!StringUtils.isEmpty(parameter), errorMessage);
        return this;
    }

    /**
     * 假定 String 不为 Blank
     */
    public ResponseBuilder assertNotBlank(String parameter, String errorMessage) {
        setResponseByAssertion(parameter != null && !StringUtils.isEmpty(parameter.trim()), errorMessage);
        return this;
    }

    /**
     * 假定 集合 不为 empty
     */
    public ResponseBuilder assertNotEmpty(Collection<?> collection, String errorMessage) {
        setResponseByAssertion(CollectionUtils.isEmpty(collection), errorMessage);
        return this;
    }

    /**
     * 假定 parameterOne.equals(parameterTwo)
     */
    public ResponseBuilder assertEquals(String parameterOne, String parameterTwo, String errorMessage) {
        setResponseByAssertion(assertEquals(parameterOne, parameterTwo), errorMessage);
        return this;
    }

    /**
     * 假定 !parameter.equals(parameterTwo)
     */
    public ResponseBuilder assertNotEquals(String parameterOne, String parameterTwo, String errorMessage) {
        setResponseByAssertion(!assertEquals(parameterOne, parameterTwo), errorMessage);
        return this;
    }

    private boolean assertEquals(String parameterOne, String parameterTwo) {
        return (parameterOne == null && parameterTwo == null) ||
                (parameterOne != null && parameterOne.equals(parameterTwo));
    }

    /**
     * 假定 parameter 符合 regex 正则
     */
    public ResponseBuilder assertMatch(String parameter, String regex, String errorMessage) {
        setResponseByAssertion(parameter != null && parameter.matches(regex),errorMessage);
        return this;
    }

    /**
     * 假定 parameter 为空 或 符合 regex 正则
     */
    public ResponseBuilder assertNullOrMatch(String parameter, String regex, String errorMessage) {
        setResponseByAssertion(parameter == null || parameter.matches(regex), errorMessage);
        return this;
    }

    /**
     * 假定结果为true
     */
    public ResponseBuilder assertTrue(Boolean isSuccess, String errorMessage) {
        setResponseByAssertion(isSuccess == null || !isSuccess, errorMessage);
        return this;
    }

    /**
     * 根据断言设置结果
     */
    private void setResponseByAssertion(boolean assertion, String errorMessage) {
        // 1. 如果当前链式调用已经失败，不再继续校验
        if (response != null && !response.isSuccess()) {
            return;
        }
        // 2. 符合断言，返回 SUCCESS RESPONSE, 否则 返回 ILLEGAL ARGUMENTS，设置错误信息
        if (assertion) {
            response = Response.ofSuccess();
        } else {
            response = Response.ofError(ResponseCode.ILLEGAL_ARGUMENTS, errorMessage);
        }
    }

    public Response response() {
        return this.response;
    }
}