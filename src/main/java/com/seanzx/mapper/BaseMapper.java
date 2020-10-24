package com.seanzx.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 集成CKMapper的接口，用以简化SQL
 * @param <T> 对应 po 类
 * @author zhaoxin
 * @date 2020/10/24
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
