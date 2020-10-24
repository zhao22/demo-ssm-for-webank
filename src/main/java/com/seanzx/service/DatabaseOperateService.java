package com.seanzx.service;

import com.seanzx.common.response.Response;
import com.seanzx.po.DatabaseOperatePO;

/**
 * 数据库操作日志服务
 * @author zhaoxin
 * @date 2020/10/21
 */
public interface DatabaseOperateService {

    /**
     * 新增数据库操作日志
     * @param databaseOperate
     * @return
     */
    Response<Integer> add(DatabaseOperatePO databaseOperate);

}
