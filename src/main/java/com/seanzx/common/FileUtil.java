package com.seanzx.common;


import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文件处理工具类
 * @author zhaoxin
 * @date 2020/10/22
 */
public class FileUtil {

    /**
     * 根据请求浏览器对文件名进行相应的编码
     * @param filename 文件名
     * @param request 浏览器请求
     * @return 编码后的文件名
     * @throws IOException
     */
    public static String filenameEncoding(String filename, HttpServletRequest request) throws IOException {
        String agent = request.getHeader("User-Agent"); //获取浏览器
        if (agent.contains("Firefox")) {
            BASE64Encoder base64Encoder = new BASE64Encoder();
            filename = "=?utf-8?B?"
                    + base64Encoder.encode(filename.getBytes(StandardCharsets.UTF_8))
                    + "?=";
        } else if(agent.contains("MSIE")) {
            filename = URLEncoder.encode(filename, "utf-8");
        } else if(agent.contains ("Safari")) {
            filename = new String (filename.getBytes (StandardCharsets.UTF_8),"ISO8859-1");
        } else {
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }
}