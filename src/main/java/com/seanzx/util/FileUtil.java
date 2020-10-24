package com.seanzx.util;



import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 文件处理工具类
 * @author zhaoxin
 * @date 2020/10/22
 */
public class FileUtil {

    public static final String FIREFOX_AGENT = "Firefox";

    public static final String IE_AGENT = "MSIE";

    public static final String SAFARI_AGENT = "Safari";
    /**
     * 根据请求浏览器对文件名进行相应的编码
     * @param filename 文件名
     * @param request 浏览器请求
     * @return 编码后的文件名
     * @throws IOException
     */
    public static String filenameEncoding(String filename, HttpServletRequest request) throws IOException {
        //获取浏览器
        String agent = request.getHeader("User-Agent");
        if (agent.contains(FIREFOX_AGENT)) {
            Base64 base64 = new Base64();
            filename = "=?utf-8?B?"
                    + Arrays.toString(base64.encode(filename.getBytes(StandardCharsets.UTF_8)))
                    + "?=";
        } else if(agent.contains(IE_AGENT)) {
            filename = URLEncoder.encode(filename, "utf-8");
        } else if(agent.contains (SAFARI_AGENT)) {
            filename = new String (filename.getBytes (StandardCharsets.UTF_8),"ISO8859-1");
        } else {
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }
}