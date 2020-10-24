package com.seanzx.service.impl;

import com.seanzx.common.email.EmailSenderDecorator;
import com.seanzx.common.response.Response;
import com.seanzx.service.EmailService;
import com.seanzx.vo.HttpRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 邮件发送服务
 * @author zhaoxin
 * @date 2020/10/24
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailSenderDecorator emailSenderDecorator;

    @Value("${webank.email.manager.emailbox}")
    private String managerEmailBox;
    /**
     * 发送慢查询邮件
     * @param httpRequestVOList
     * @return
     */
    @Override
    public Response<String> sendSlowRequestEmail(List<HttpRequestVO> httpRequestVOList) {
        if (StringUtils.isEmpty(managerEmailBox)) {
            return Response.ofSuccess("没有需要发送的邮箱");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("检查到系统昨日有慢查询请求:\n");
        for (HttpRequestVO httpRequestVO : httpRequestVOList) {
            builder.append(httpRequestVO.toString()).append("\n");
        }

        String received = emailSenderDecorator.sendEmail(managerEmailBox, "慢查询检查", builder.toString());
        return Response.ofSuccess(received);
    }

}
