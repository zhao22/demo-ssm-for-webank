package com.seanzx.service;

import com.seanzx.common.Response;
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
