package com.seanzx.service.impl;

import com.seanzx.common.response.Response;
import com.seanzx.enums.ResponseCode;
import com.seanzx.mapper.DatabaseOperateMapper;
import com.seanzx.po.DatabaseOperatePO;
import com.seanzx.service.DatabaseOperateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据库操作日志服务
 * @author zhaoxin
 * @date 2020/10/21
 */
@Service
public class DatabaseOperateServiceImpl implements DatabaseOperateService {

    @Autowired
    private DatabaseOperateMapper databaseOperateMapper;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseOperateServiceImpl.class);

    @Override
    public Response<Integer> add(DatabaseOperatePO databaseOperatePO) {
        int rows = databaseOperateMapper.insertSelective(databaseOperatePO);
        if (rows != 1) {
            logger.error("保存异常，操作行数为:{}, customerVO:{}", rows, databaseOperatePO.toString());
            return Response.ofError(ResponseCode.UNEXPECTED_ERROR, "保存失败");
        }
        return Response.ofSuccess(databaseOperatePO.getId());
    }
}
