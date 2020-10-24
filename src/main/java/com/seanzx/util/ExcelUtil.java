package com.seanzx.util;

import com.seanzx.common.response.ApplicationException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Excel POI 操作简化工具类
 * @author zhaoxin
 * @date 2020/10/22
 */
public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 将Excel 写入 Response 中返回给调用者
     * @param fileName Excel 文件名
     * @param consumer 传递HSSFSheet 给该行为，由该行为将内容写入 Excel中
     */
    public static void writeIntoResponse(String fileName, Consumer<HSSFSheet> consumer) {
        // 1. 得到 HttpServletRequest 和 HttpServletResponse
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new ApplicationException();
        }
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        if (response == null) {
            throw new ApplicationException();
        }
        // 2.在内存中创建一个excel文件
        try (HSSFWorkbook hssfWorkbook = new HSSFWorkbook()) {
            // 3.创建工作簿
            HSSFSheet sheet = hssfWorkbook.createSheet();
            // 4. 执行内置方法
            consumer.accept(sheet);
            // 5.写入response
            ServletOutputStream outputStream = response.getOutputStream();
            setResponseHeader(request, response, fileName);
            hssfWorkbook.write(outputStream);
        } catch(IOException e) {
            logger.error("IO 异常", e);
        }
    }

    /**
     * 将请求流中Excel读入Objects中
     * @param multipartFile 请求流
     * @param function 将 row 转换为 vo 的方法
     * @param <T> IColumn
     * @return 结果 VO 集合
     */
    public static <T> List<T> readExcelIntoObjects(MultipartFile multipartFile, Function<Row, T> function){
        List<T> resultList = new ArrayList<>();
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            logger.error("解析inputStream 异常", e);
            throw new ApplicationException("解析异常");
        }
        try (HSSFWorkbook workbook = new HSSFWorkbook(inputStream)){
            HSSFSheet sheetAt = workbook.getSheetAt(0);
            for (Row row : sheetAt) {
                // 标题行不读取
                if (row.getRowNum() == 0) {
                    continue;
                }
                resultList.add(function.apply(row));
            }
        } catch (IOException e) {
            logger.error("IO 异常", e);
        }
        return resultList;
    }

    /**
     * 创建标题行
     */
    public static void createTitleRow(HSSFSheet sheet, IColumn<?>[] enums) {
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < enums.length; i++) {
            row.createCell(i).setCellValue(enums[i].getColumnName());
        }
    }

    /**
     * 将Excel 行 信息写入 vo 中
     */
    public static <T> void setValueByRow(T vo, IColumn<T>[] enums, Row row) {
        for (int i = 0; i < enums.length; i++) {
            enums[i].setValue(vo, row.getCell(i));
        }
    }

    /**
     * 将VO集合数据写入Excel中
     */
    public static <T> void createRows(HSSFSheet sheet, IColumn<T>[] enums, Collection<T> vos) {
        int i = 1;
        for (T vo : vos) {
            // 1. 每个vo 创建一行
            HSSFRow row = sheet.createRow(i);
            for (int i1 = 0; i1 < enums.length; i1++) {
                // 2. 每个enum 创建一列
                Cell cell = row.createCell(i1);
                // 3. 获取 vo 中 对应列值
                Object value = enums[i1].getValue(vo);
                // 4. 将vo 中对应值写入
                if (value == null) {
                     continue;
                }
                cell.setCellValue(value.toString());
            }
            i++;
        }
    }

    /**
     * 通过浏览器信息,设置文件名及其它返回头信息
     * @param request
     * @param response
     * @param fileName
     * @throws IOException
     */
    public static void setResponseHeader(HttpServletRequest request,
                                          HttpServletResponse response,
                                          String fileName) throws IOException {
        // 1.获取mimeType
        ServletContext servletContext = request.getServletContext();
        String mimeType = servletContext.getMimeType(fileName);
        // 2.获取浏览器信息,对文件名进行重新编码
        fileName = FileUtil.filenameEncoding(fileName, request);

        // 3.设置信息头
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition","attachment;filename="+fileName);
    }

    /**
     * Excel 每一列对应的接口
     * @param <T>
     */
    public interface IColumn<T> {

        /**
         * 获取列名
         * 用于将标题 写入 Excel中
         * @return 列名
         */
        String getColumnName();

        /**
         * 通过vo对象获取列的值
         * 用于将vo的属性值写入Excel对应列
         * @param vo 需要取值的vo对象
         * @return 对象的列值
         */
        Object getValue(T vo);

        /**
         * 将targetValue 写入 vo 对应列中
         * 用于将Excel对应列值写入 vo 的属性值
         * @param vo 需要设值的 vo 对象
         * @param targetValue 需要设置的值
         */
        void setValue(T vo, Cell targetValue);
    }
}
