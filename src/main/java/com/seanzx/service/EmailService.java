package com.seanzx.service;

import com.seanzx.common.response.Response;
import com.seanzx.vo.HttpRequestVO;

import java.util.List;

/**
 * 邮件发送服务
 * @author zhaoxin
 * @date 2020/10/24
 */
public interface EmailService {

    /**
     * 发送慢查询邮件
     * @param httpRequestVOList
     * @return
     */
    Response<String> sendSlowRequestEmail(List<HttpRequestVO> httpRequestVOList);
}
