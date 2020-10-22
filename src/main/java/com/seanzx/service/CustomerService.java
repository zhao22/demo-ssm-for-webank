package com.seanzx.service;

import com.seanzx.common.Page;
import com.seanzx.common.Response;
import com.seanzx.vo.CustomerVO;

/**
 * 客户信息Service
 * @author zhaoxin
 * @date 2020/10/19
 */
public interface CustomerService {

    /**
     * 新增客户
     * @param customerVO 新增的客户信息
     * @return 新增的客户id
     */
    Response<Integer> addCustomer(CustomerVO customerVO);

    /**
     * 通过 id 查找客户信息
     * @param id 客户 id
     * @return 查找到的客户信息，没有则为空
     */
    Response<CustomerVO> findCustomer(Integer id);

    /**
     * 分页查询客户信息
     * @param pageNum 页码
     * @param size 一页的大小
     * @param customerVO 其它查询参数(姓名为模糊查询)
     * @return 该页客户数据
     */
    Response<Page<CustomerVO>> findCustomerByPage(Integer pageNum, Integer size, CustomerVO customerVO);

    /**
     * 更新客户信息
     * @param id 客户 id
     * @param customerVO 需要更新的用户信息，不需要更新的字段可为空
     * @return 更新结果
     */
    Response<?> updateCustomerInfo(Integer id, CustomerVO customerVO);

    /**
     * 删除客户信息（逻辑删除，置 data_status = 0）
     * @param id 客户 id
     * @return 删除结果
     */
    Response<?> deleteCustomer(Integer id);
}
