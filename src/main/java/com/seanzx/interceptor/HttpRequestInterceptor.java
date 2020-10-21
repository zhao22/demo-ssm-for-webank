package com.seanzx.interceptor;

import com.seanzx.common.RequestIpUtil;
import com.seanzx.common.SystemLogQueue;
import com.seanzx.po.HttpRequestPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
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

    private static final ThreadLocal<HttpRequestPO> httpRequestPOThreadLocal = new ThreadLocal<>();

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpRequestPO httpRequestPO = new HttpRequestPO();
        httpRequestPOThreadLocal.set(httpRequestPO);

        httpRequestPO.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        httpRequestPO.setStartTime(new Date());
        httpRequestPO.setUri(request.getRequestURI());
        httpRequestPO.setRequestMethod(request.getMethod());
        httpRequestPO.setRequestIp(RequestIpUtil.getIP(request));
        logger.info("Request[ip={}, uri={}]", httpRequestPO.getRequestIp(), httpRequestPO.getUri());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        HttpRequestPO httpRequestPO =httpRequestPOThreadLocal.get();
        if (httpRequestPO != null) {
            httpRequestPO.setDuration(new Date().getTime() - httpRequestPO.getStartTime().getTime());
            if (null != ex) {
                httpRequestPO.setExceptionMessage(ex.getMessage());
            }
            SystemLogQueue.put(httpRequestPO);
        }
        httpRequestPOThreadLocal.set(null);
    }

    public HttpRequestPO getThreadLocalHttpRequestPO() {
        return httpRequestPOThreadLocal.get();
    }
}
