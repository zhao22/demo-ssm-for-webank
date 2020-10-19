package com.seanzx.controller;

import com.seanzx.common.Page;
import com.seanzx.common.Response;
import com.seanzx.service.CustomerService;
import com.seanzx.vo.CustomerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 客户信息控制器
 * @author zhaoxin
 * @date 2020/10/19
 */
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/customer")
    public Response<Integer> addCustomer(@RequestBody CustomerVO customerVO) {
        return customerService.addCustomer(customerVO);
    }

    @GetMapping("/customer/{id}")
    public Response<CustomerVO> findCustomer(@PathVariable Integer id) {
        return customerService.findCustomer(id);
    }

    @GetMapping("/customer/page/{pageNum}")
    public Response<Page<CustomerVO>> findCustomerByPage(@PathVariable Integer pageNum,
                                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        return customerService.findCustomerByPage(pageNum, size);
    }


    @PutMapping("/customer/{id}")
    public Response updateCustomerInfo(@PathVariable Integer id, @RequestBody CustomerVO customerVO) {
        return customerService.updateCustomerInfo(id, customerVO);
    }

    @DeleteMapping("/customer/{id}")
    public Response deleteCustomer(@PathVariable Integer id) {
        return customerService.deleteCustomer(id);
    }

}
