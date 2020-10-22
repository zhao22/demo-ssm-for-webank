package com.seanzx;

import com.seanzx.common.Page;
import com.seanzx.common.Response;
import com.seanzx.enums.Gender;
import com.seanzx.service.CustomerService;
import com.seanzx.vo.CustomerVO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class CustomerTests {

    @Autowired
    private CustomerService customerService;


    @Test
    void testFindOne() {
        Response<CustomerVO> response = customerService.findCustomer(1);
        CustomerVO customerVO = response.getData();
        Assert.assertEquals("张三", customerVO.getCustomerName());
        Assert.assertEquals(1, (int) customerVO.getGender());
        Assert.assertEquals(120, (int) customerVO.getAge());
        Assert.assertEquals("13637855354", customerVO.getMobile());
        Assert.assertEquals("seanzhxi@126.com", customerVO.getEmail());
        Assert.assertEquals("深圳市南山区", customerVO.getAddress());
    }

    @Test
    void testFindPage() {
        // 1.随机插入20组以内数据
        Random random = new Random();
        for (int i = 0; i < random.nextInt(20); i++) {
            customerService.addCustomer(generateTestVO());
        }
        // 2. 验证分页数据量为 total % size。当前页数为 total / size + 1
        int pageNum = 1, size = 10;

        Response<Page<CustomerVO>> response = customerService.findCustomerByPage(pageNum, size, new CustomerVO());
        Page<CustomerVO> pageData = response.getData();
        Assert.assertEquals(pageData.getTotal() % size, pageData.getList().size());
        Assert.assertEquals(pageData.getTotal() / size + 1, pageNum);
        Assert.assertNotNull(pageData.getList().get(0));
    }

    private CustomerVO generateTestVO() {
        CustomerVO customerVO = new CustomerVO();
        customerVO.setCustomerName("李寻欢");
        customerVO.setGender(Gender.MALE.ordinal());
        customerVO.setAge(130);
        customerVO.setMobile("13637855354");
        customerVO.setEmail("seanzhxi@126.com");
        customerVO.setAddress("in story");
        return customerVO;
    }

    @Test
    void testAdd() {
        // 1. 新建客户，设置随机名称
        CustomerVO customerVO = generateTestVO();
        customerVO.setCustomerName(customerVO.getCustomerName() + Math.random());

        Response<Integer> response = customerService.addCustomer(customerVO);
        Integer customerId = response.getData();
        // 2. 从数据库中查询该客户，校验名称与设置名称相同
        Response<CustomerVO> response1 = customerService.findCustomer(customerId);
        CustomerVO addedCustomerVO = response1.getData();
        Assert.assertEquals(customerVO.getCustomerName(), addedCustomerVO.getCustomerName());
    }

    @Test
    void testUpdate() {
        Response<CustomerVO> response = customerService.findCustomer(1);
        CustomerVO customerVO = response.getData();
        Assert.assertEquals("张三", customerVO.getCustomerName());
        customerVO.setCustomerName(customerVO.getCustomerName() + Math.random());

        Response response1 = customerService.updateCustomerInfo(customerVO.getId(), customerVO);
        Assert.assertTrue(response1.isSuccess());

        Response<CustomerVO> response2 = customerService.findCustomer(1);
        Assert.assertEquals(customerVO.getCustomerName(), response2.getData().getCustomerName());
    }

    @Test
    void testAddFailed() {
        CustomerVO customerVO = generateTestVO();
        customerVO.setCustomerName("");
        Response<Integer> response = customerService.addCustomer(customerVO);
        Assert.assertFalse(response.isSuccess());
        customerVO.setCustomerName("some");

        customerVO.setMobile("1363785535");
        Response<Integer> response1 = customerService.addCustomer(customerVO);
        Assert.assertFalse(response.isSuccess());
        customerVO.setMobile("13637855354");
    }

    @Test
    void testDelete() {
        Response<CustomerVO> response = customerService.findCustomer(1);
        Assert.assertNotNull(response.getData());

        Response response1 = customerService.deleteCustomer(1);
        Assert.assertTrue(response.isSuccess());

        Response<CustomerVO> response2 = customerService.findCustomer(1);
        Assert.assertNull(response2.getData());
    }
}
