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

    CustomerPO findCustomer(Integer id);

    /**
     * 根据参数查询客户，姓名为模糊查询
     * @return
     */
    List<CustomerPO> findCustomers(CustomerVO customerVO);

}
