package com.seanzx.vo;


import com.seanzx.common.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 客户信息VO
 * @author zhaoxin
 * @date 2020/10/19
 */
@ApiModel(value="客户信息模型")
public class CustomerVO {

    @ApiModelProperty(name = "客户id", notes = "自增主键，新增和修改时不传")
    private Integer id;

    @ApiModelProperty(name = "客户名称")
    private String customerName;

    @ApiModelProperty(name = "性别", notes = "0: 女 1: 男")
    private Integer gender;

    @ApiModelProperty(name = "年龄", notes = "0 - 130 的数字")
    private Integer age;

    @ApiModelProperty(name = "手机号", example = "13333333333", notes = "传值时需符合正则[" + Constants.Regex.MOBILE + "]")
    private String mobile;

    @ApiModelProperty(name = "邮箱", example = "seam622@163.com", notes = "传值时需符合正则[" + Constants.Regex.EMAIL + "]")
    private String email;

    @ApiModelProperty(name = "住址", notes = "长度不超过 100 位")
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CustomerVO{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
