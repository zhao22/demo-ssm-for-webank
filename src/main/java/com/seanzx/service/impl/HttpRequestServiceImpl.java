package com.seanzx.service.impl;

import com.seanzx.util.ClassUtil;
import com.seanzx.common.response.Response;
import com.seanzx.enums.ResponseCode;
import com.seanzx.mapper.HttpRequestMapper;
import com.seanzx.po.HttpRequestPO;
import com.seanzx.service.HttpRequestService;
import com.seanzx.vo.HttpRequestVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * http请求服务
 * @author zhaoxin
 * @date 2020/10/21
 */
@Service
public class HttpRequestServiceImpl implements HttpRequestService {

    private final static Logger logger = LoggerFactory.getLogger(HttpRequestServiceImpl.class);

    @Autowired
    private HttpRequestMapper httpRequestMapper;

    @Override
    public Response<String> add(HttpRequestPO httpRequestPO) {
        int rows = httpRequestMapper.insertSelective(httpRequestPO);
        if (rows != 1) {
            logger.error("保存异常，操作行数为:{}, customerVO:{}", rows, httpRequestPO.toString());
            return Response.ofError(ResponseCode.UNEXPECTED_ERROR, "保存失败");
        }
        return Response.ofSuccess(httpRequestPO.getId());
    }

    @Override
    public Response<List<HttpRequestVO>> findHttpRequests(Date startDate, Date endDate, Long minDuration) {
        List<HttpRequestPO> httpRequestPOs = httpRequestMapper.findHttpRequests(startDate, endDate, minDuration);
        return Response.ofSuccess(ClassUtil.copy(httpRequestPOs, HttpRequestVO.class));
    }
}
