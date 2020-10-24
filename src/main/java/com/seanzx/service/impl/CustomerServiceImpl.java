package com.seanzx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seanzx.common.*;
import com.seanzx.common.response.Page;
import com.seanzx.common.response.Response;
import com.seanzx.common.response.ResponseBuilder;
import com.seanzx.enums.DataStatus;
import com.seanzx.enums.ResponseCode;
import com.seanzx.mapper.CustomerMapper;
import com.seanzx.po.CustomerPO;
import com.seanzx.service.CustomerService;
import com.seanzx.util.ClassUtil;
import com.seanzx.vo.CustomerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客户信息 service
 * @author zhaoxin
 * @date 2020/10/19
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    @Transactional
    public Response<Integer> addCustomer(CustomerVO customerVO) {
        // 1. 参数校验
        // 用户名称不能为空; 手机号若不为空，需满足正则; 邮箱若不为空，需满足正则
        Response response = new ResponseBuilder().assertNotBlank(customerVO.getCustomerName(), "客户名称不能为空")
                .assertNullOrMatch(customerVO.getMobile(), Constants.Regex.MOBILE, "手机号格式不正确")
                .assertNullOrMatch(customerVO.getEmail(), Constants.Regex.EMAIL, "邮箱格式不正确")
                .response();
        if (!response.isSuccess()) {
            return response;
        }
        // 2. 转换为客户信息 PO
        CustomerPO po = ClassUtil.copy(customerVO, CustomerPO.class);
        if (po == null) {
            return Response.ofError(ResponseCode.UNEXPECTED_ERROR, "保存失败");
        }
        po.setId(null);
        po.setDataStatus(DataStatus.VALID.ordinal());
        // 3. 插入客户信息数据
        int rows = customerMapper.insertSelective(po);
        if (rows != 1) {
            logger.error("保存异常，操作行数为:{}, customerVO:{}", rows, customerVO.toString());
            return Response.ofError(ResponseCode.UNEXPECTED_ERROR, "保存失败");
        }
        return Response.ofSuccess(po.getId());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Response<CustomerVO> findCustomer(Integer id) {
        CustomerPO po = customerMapper.findCustomer(id);
        return Response.ofSuccess(ClassUtil.copy(po, CustomerVO.class));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Response<Page<CustomerVO>> findCustomerByPage(Integer pageNum, Integer size, CustomerVO customerVO) {
        // 1. 得到分页的 customerPO 集合
        PageHelper.startPage(pageNum, size);
        List<CustomerPO> pos = customerMapper.findCustomers(customerVO);
        PageInfo<CustomerPO> pageInfo = new PageInfo<>(pos);
        // 2. 转换为 customerVO 集合返回
        return Response.ofPage(pageInfo.getTotal(), ClassUtil.copy(pageInfo.getList(), CustomerVO.class));
    }

    @Override
    @Transactional
    public Response<?> updateCustomerInfo(Integer id, CustomerVO customerVO) {
        // 1. 参数校验
        Response<?> response = new ResponseBuilder()
                .assertNullOrMatch(customerVO.getMobile(), Constants.Regex.MOBILE, "手机号格式不正确")
                .assertNullOrMatch(customerVO.getEmail(), Constants.Regex.EMAIL, "邮箱格式不正确")
                .response();
        if (!response.isSuccess()) {
            return response;
        }
        // 2. 不允许更新失效数据
        CustomerPO localData = customerMapper.selectByPrimaryKey(id);
        if (localData != null) {
            boolean dataIsInvalid = localData.getDataStatus() == null ||
                    localData.getDataStatus() == DataStatus.INVALID.ordinal();
            if (dataIsInvalid) {
                return Response.ofSuccess();
            }
        }
        // 3. 数据更新
        CustomerPO customerPO = ClassUtil.copy(customerVO, CustomerPO.class);
        if (customerPO == null) {
            return Response.ofError(ResponseCode.UNEXPECTED_ERROR, "更新失败");
        }
        customerPO.setId(id);
        customerMapper.updateByPrimaryKeySelective(customerPO);
        return Response.ofSuccess();
    }

    @Override
    @Transactional
    public Response<?> deleteCustomer(Integer id) {
        CustomerPO customerPO = new CustomerPO();
        customerPO.setId(id);
        customerPO.setDataStatus(DataStatus.INVALID.ordinal());
        customerMapper.updateByPrimaryKeySelective(customerPO);
        return Response.ofSuccess();
    }
}
