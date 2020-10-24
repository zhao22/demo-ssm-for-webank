package com.seanzx.task;

import com.seanzx.common.response.Response;
import com.seanzx.util.TimeUtil;
import com.seanzx.service.EmailService;
import com.seanzx.service.HttpRequestService;
import com.seanzx.vo.HttpRequestVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 检查慢请求任务
 * @author zhaoxin
 * @date 2020/10/24
 */
@Component
@EnableScheduling
public class SlowRequestCheckTask {

    private static final Logger logger = LoggerFactory.getLogger(SlowRequestCheckTask.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private HttpRequestService httpRequestService;

    @Value("${webank.slow-request.waterline:2000}")
    private long slowRequestWaterline;
    /**
     * 定时检查较慢的HTTP请求，
     * 发送邮件通知管理人
     */
    @Scheduled(cron = "${webank.slow-request.crontab:0 0 7 * * ?}")
    public void run() {
        try {
            logger.info("慢查询检查开始");
            LocalDate now = LocalDate.now();
            Date startOfToday = TimeUtil.toDate(now.atStartOfDay());
            Date startOfYesterday = TimeUtil.toDate(now.minusDays(1).atStartOfDay());
            Response<List<HttpRequestVO>> response =
                    httpRequestService.findHttpRequests(startOfYesterday, startOfToday, slowRequestWaterline);
            List<HttpRequestVO> requestVOList = response.getData();

            if (requestVOList.isEmpty()) {
                return;
            }
            logger.info("发送慢查询日志邮件");
            Response<String> sendResponse = emailService.sendSlowRequestEmail(requestVOList);
            logger.info("发送成功，返回结果为" + sendResponse.getData());
        } catch (Exception e) {
            logger.error("慢查询检查异常", e);
        }
    }
}
