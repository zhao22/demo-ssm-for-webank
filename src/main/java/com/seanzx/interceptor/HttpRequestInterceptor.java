package com.seanzx.interceptor;

import com.seanzx.util.RequestIpUtil;
import com.seanzx.common.SystemLogQueue;
import com.seanzx.po.HttpRequestPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

/**
 * http 请求记录拦截器
 * 记录 系统运行过程中所有http 请求信息
 * @author zhaoxin
 * @date 2020/10/24
 */
@Component
public class HttpRequestInterceptor extends HandlerInterceptorAdapter {

    private static final ThreadLocal<HttpRequestPO> HTTP_REQUEST_THREAD_LOCAL = new ThreadLocal<>();

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpRequestPO httpRequest = new HttpRequestPO();
        HTTP_REQUEST_THREAD_LOCAL.set(httpRequest);

        httpRequest.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        httpRequest.setStartTime(new Date());
        httpRequest.setUri(request.getRequestURI());
        httpRequest.setRequestMethod(request.getMethod());
        httpRequest.setRequestIp(RequestIpUtil.getIp(request));
        logger.info("Request[ip={}, uri={}]", httpRequest.getRequestIp(), httpRequest.getUri());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        HttpRequestPO httpRequest =HTTP_REQUEST_THREAD_LOCAL.get();
        if (httpRequest != null) {
            httpRequest.setDuration(System.currentTimeMillis() - httpRequest.getStartTime().getTime());
            if (null != ex) {
                httpRequest.setExceptionMessage(ex.getMessage());
            }
            SystemLogQueue.put(httpRequest);
        }
        HTTP_REQUEST_THREAD_LOCAL.remove();
    }

    public HttpRequestPO getThreadLocalHttpRequest() {
        return HTTP_REQUEST_THREAD_LOCAL.get();
    }
}
