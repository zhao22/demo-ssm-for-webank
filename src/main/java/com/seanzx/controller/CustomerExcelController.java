package com.seanzx.controller;

import com.seanzx.common.response.Response;
import com.seanzx.service.CustomerExcelService;
import com.seanzx.vo.CustomerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 客户信息导入导出接口
 * @author zhaoxin
 * @date 2020/10/22
 */
@Api
@RestController
@RequestMapping("/customer/excel")
public class CustomerExcelController {

    @Autowired
    private CustomerExcelService customerExcelService;

    @ApiOperation(value = "获取客户信息导入模板", produces = "application/octet-stream")
    @GetMapping("/template")
    public void getCustomerExcelTemplate() {
        customerExcelService.writeTemplateFileIntoResponse();
    }

    @ApiOperation(value = "导入客户信息")
    @PostMapping
    public Response importCustomers(MultipartFile multipartFile) {
        return customerExcelService.saveCustomerInfoByFile(multipartFile);
    }

    @ApiOperation(value = "导出客户信息", produces = "application/octet-stream")
    @GetMapping("/{pageNum}")
    public void exportCustomers(@ApiParam(value = "页码") @PathVariable Integer pageNum,
                                @ApiParam(value = "分页大小")
                                @RequestParam(required = false, defaultValue = "10") Integer size,
                                @ApiParam(value = "查询参数(姓名为模糊查询)") CustomerVO customerVO) {
        customerExcelService.exportFileIntoResponse(pageNum, size, customerVO);
    }
}
