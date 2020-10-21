package com.seanzx.mapper;

import com.seanzx.po.HttpRequestPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface HttpRequestMapper extends BaseMapper<HttpRequestPO>{

    /**
     * 查询区间内发起的 http 请求, 响应时间超过 minDuration 的记录
     * @param start 开始区间
     * @param end 结果区间
     * @param minDuration
     * @return
     */
    List<HttpRequestPO> findHttpRequests(@Param(value = "start") Date start,
                                         @Param(value = "end") Date end,
                                         @Param(value = "minDuration") Long minDuration);
}
