package com.seanzx.enums;

import com.seanzx.util.ExcelUtil;
import com.seanzx.vo.CustomerVO;
import org.apache.poi.ss.usermodel.Cell;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 客户信息导出枚举类
 * @author zhaoxin
 * @date 2020/10/22
 */
public enum CustomerExcelColumns implements ExcelUtil.IColumn<CustomerVO> {

    /**
     * id 不通过Excel设值
     */
    ID("id",
            CustomerVO::getId,
            null),
    /**
     * 客户名称
     */
    CUSTOMER_NAME("客户名称",
            CustomerVO::getCustomerName,
            (vo, cell) -> vo.setCustomerName(cell.getStringCellValue())),
    /**
     * 性别
     */
    GENDER("性别",
            CustomerVO::getGender,
            (vo, cell) -> vo.setGender(Integer.valueOf(cell.getStringCellValue()))),
    /**
     * 年龄
     */
    AGE("年龄",
            CustomerVO::getAge,
            (vo, cell) -> vo.setAge(Integer.valueOf(cell.getStringCellValue()))),
    /**
     * 手机号
     */
    MOBILE("手机号",
            CustomerVO::getMobile,
            (vo, cell) -> vo.setMobile(cell.getStringCellValue())),
    /**
     * 邮箱
     */
    EMAIL("邮箱",
            CustomerVO::getEmail,
            (vo, cell) -> vo.setEmail(cell.getStringCellValue())),
    /**
     * 地址
     */
    ADDRESS("地址",
            CustomerVO::getAddress,
            (vo, cell) -> vo.setAddress(cell.getStringCellValue()));


    private String columnName;

    private Function<CustomerVO, Object> getter;

    private BiConsumer<CustomerVO, Cell> setter;

    CustomerExcelColumns(String columnName,
                         Function<CustomerVO, Object> getter,
                         BiConsumer<CustomerVO, Cell> setter) {
        this.columnName = columnName;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public Object getValue(CustomerVO vo) {
        return this.getter.apply(vo);
    }

    @Override
    public void setValue(CustomerVO vo, Cell cell) {
        if (this.setter != null) {
            this.setter.accept(vo, cell);
        }
    }
}