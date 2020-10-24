package com.seanzx.service.impl;

import com.seanzx.util.ExcelUtil;
import com.seanzx.common.response.Page;
import com.seanzx.common.response.Response;
import com.seanzx.enums.CustomerExcelColumns;
import com.seanzx.enums.ResponseCode;
import com.seanzx.service.CustomerExcelService;
import com.seanzx.service.CustomerService;
import com.seanzx.vo.CustomerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 客户信息导入导出服务
 * @author zhaoxin
 * @date 2020/10/22
 */
@Service
public class CustomerExcelServiceImpl implements CustomerExcelService {

    @Autowired
    private CustomerService customerService;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public void writeTemplateFileIntoResponse() {
        ExcelUtil.writeIntoResponse("customer_template.xls",
                (sheet) -> ExcelUtil.createTitleRow(sheet, CustomerExcelColumns.values()));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public Response<?> saveCustomerInfoByFile(MultipartFile multipartFile) {
        // 1. 将Excel中数据读为 CustomerVO
        CustomerExcelColumns[] columns = CustomerExcelColumns.values();
        List<CustomerVO> customerVOList = ExcelUtil.readExcelIntoObjects(multipartFile, (row) -> {
            CustomerVO customerVO = new CustomerVO();
            ExcelUtil.setValueByRow(customerVO, columns, row);
            return customerVO;
        });
        // 2. 保存客户信息数组，如果有记录报错，将错误信息收集返回
        StringBuilder responseMessage = new StringBuilder();
        for (int i = 0; i < customerVOList.size(); i++) {
            CustomerVO customerVO = customerVOList.get(i);
            Response<Integer> response = customerService.addCustomer(customerVO);
            if (!response.isSuccess()) {
                responseMessage.append("第").append(i).append("行异常:").append(response.getMessage()).append("\n");
            }
        }
        if (responseMessage.length() > 0) {
            return Response.ofError(ResponseCode.ILLEGAL_ARGUMENTS, responseMessage.toString());
        }
        return Response.ofSuccess();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public void exportFileIntoResponse(int pageNum,
                                       int size,
                                       CustomerVO customerVO) {
        Response<Page<CustomerVO>> result = customerService.findCustomerByPage(pageNum, size, customerVO);
        List<CustomerVO> customers = Optional.ofNullable(result.getData().getList()).orElse(new ArrayList<>());
        ExcelUtil.writeIntoResponse("客户信息.xls",
                (sheet) -> {
                    CustomerExcelColumns[] columns = CustomerExcelColumns.values();
                    ExcelUtil.createTitleRow(sheet, columns);
                    ExcelUtil.createRows(sheet, columns, customers);
                });
    }
}
