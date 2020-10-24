package com.seanzx.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取客户 ip
 * @author zhaoxin
 * @date 2020/10/21
 */
public class RequestIpUtil {

    private static final String[] NAMES = {
            "x-forwarded-for",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };
    /**
     * 获取请求的客户IP
     * 避免获取到Nginx等代理地址
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = null;
        // 获取 ip 地址
        for (String name : NAMES) {
            ip = request.getHeader(name);
            if (isValid(ip)) {
                break;
            }
        }
        if (!isValid(ip)) {
            ip = request.getRemoteAddr();
        }
        // 只取第一个ip
        int index = ip.indexOf(',');
        if (index != -1) {
            ip = ip.substring(0, index);
        }
        return ip;
    }

    private static boolean isValid(String ip) {
        return ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip);
    }
}
