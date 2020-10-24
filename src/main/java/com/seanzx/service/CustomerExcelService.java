package com.seanzx.service;

import com.seanzx.common.response.Response;
import com.seanzx.vo.CustomerVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 客户信息导入导出服务
 * @author zhaoxin
 * @date 2020/10/22
 */
public interface CustomerExcelService {

    /**
     * 导出客户信息模板
     */
    void writeTemplateFileIntoResponse();

    /**
     * 根据Excel数据保存客户信息
     * @param multipartFile Excel 文件
     * @return 保存结果
     */
    Response<?> saveCustomerInfoByFile(MultipartFile multipartFile);
    /**
     * 导出客户信息
     * @param pageNum 页码
     * @param size 一页的大小
     * @param customerVO 查询参数
     */
    void exportFileIntoResponse(int pageNum,
                                int size,
                                CustomerVO customerVO);
}
