package com.seanzx.service;

import com.seanzx.common.response.Response;
import com.seanzx.po.HttpRequestPO;
import com.seanzx.vo.HttpRequestVO;

import java.util.Date;
import java.util.List;

/**
 * http 请求日志服务
 * @author zhaoxin
 * @date 2020/10/21
 */
public interface HttpRequestService {

    /**
     * 新增 http 请求日志
     * @param httpRequest 请求日志 po
     * @return 新增记录 id
     */
    Response<String> add(HttpRequestPO httpRequest);

    /**
     * 根据请求接收时间 (start <= start_time < end)查询响应时间超过指定时间(minDuration)的请求
     * @param start 开始区间(可不传)
     * @param end 结束区间(可不传)
     * @param minDuration 响应时间水平线(可不传)
     * @return
     */
    Response<List<HttpRequestVO>> findHttpRequests(Date start, Date end, Long minDuration);

}
