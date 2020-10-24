package com.seanzx.controller;

import com.seanzx.common.response.Page;
import com.seanzx.common.response.Response;
import com.seanzx.service.CustomerService;
import com.seanzx.vo.CustomerVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhaoxin
 * @date 2020/10/19
 */
@Api
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @ApiOperation(value = "新增客户")
    @PostMapping
    public Response<Integer> addCustomer(@ApiParam("新增客户信息") @RequestBody CustomerVO customerVO) {
        return customerService.addCustomer(customerVO);
    }

    @ApiOperation(value = "根据客户id查询客户")
    @GetMapping("/{id}")
    public Response<CustomerVO> findCustomer(@ApiParam(value = "客户id")@PathVariable Integer id) {
        return customerService.findCustomer(id);
    }

    @ApiOperation(value = "分页查询客户")
    @GetMapping("/page/{pageNum}")
    public Response<Page<CustomerVO>> findCustomerByPage(@ApiParam(value = "页码") @PathVariable Integer pageNum,
                                                         @ApiParam(value = "分页大小")
                                                         @RequestParam(required = false, defaultValue = "10") Integer size,
                                                         @ApiParam(value = "查询参数(姓名为模糊查询)")CustomerVO customerVO) {
        return customerService.findCustomerByPage(pageNum, size, customerVO);
    }


    @ApiOperation(value = "修改客户信息")
    @PutMapping("/{id}")
    public Response<?> updateCustomerInfo(@ApiParam(value = "客户id") @PathVariable Integer id,
                                       @ApiParam(value = "修改客户信息, 仅传需要修改的字段")
                                       @RequestBody CustomerVO customerVO) {
        return customerService.updateCustomerInfo(id, customerVO);
    }

    @ApiOperation(value = "删除客户", notes = "对客户进行逻辑删除")
    @DeleteMapping("/{id}")
    public Response<?> deleteCustomer(@ApiParam(value = "客户id") @PathVariable Integer id) {
        return customerService.deleteCustomer(id);
    }

}
