package com.seanzx.mapper;

import com.seanzx.po.CustomerPO;
import com.seanzx.vo.CustomerVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 客户信息Mapper
 * @author zhaoxin
 * @date 2020/10/19
 */
@Mapper
public interface CustomerMapper extends BaseMapper<CustomerPO> {

    /**
     * 通过id 查询客户信息
     * @param id 客户id
     * @return 查询到的客户信息
     */
    CustomerPO findCustomer(Integer id);

    /**
     * 根据参数查询客户，姓名为模糊查询
     * @param customerVO 查询参数
     * @return 查询到的客户信息集合
     */
    List<CustomerPO> findCustomers(CustomerVO customerVO);

}
