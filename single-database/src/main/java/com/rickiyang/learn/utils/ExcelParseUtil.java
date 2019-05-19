package com.rickiyang.learn.utils;


import com.rickiyang.learn.aop.BeanFieldAnnotation;
import com.rickiyang.learn.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;

/**
 * @author: yangyue
 * @date: 2019/2/20
 * @description: 读取excel文件，将每一行 的 每一个单元格解析成string
 */
@Slf4j
@Component
public class ExcelParseUtil<T> {
    private static final String FILE_PARA_ERROR = "文件参数异常";
    private static final String FILE_FORMAT_ERROR = "请选择excel文件进行解析";
    private static final String FILE_SHEET_ERROR = "sheet名称错误：";
    private static final String FILE_SHEET_AT_ERROR = "sheet索引越界：";
    private static final String FILE_CELL_ERROR = "文件内容异常";
    private static final String SYSTEM_ERROR = "系统异常";
    private static final String CONVERT_ERROR = "转换异常";

    private static final String SUFFIX_2003 = ".xls";
    private static final String SUFFIX_2007 = ".xlsx";

    /**
     * excel文件解析
     *
     * @param file
     * @param sheetName
     * @return
     * @throws IOException
     */
    public List<List<String>> parseWorkBook(MultipartFile file, String sheetName, int totalColumnNum) {
        try {
            if (file.isEmpty() || StringUtils.isEmpty(sheetName)) {
                throw new BizException(FILE_PARA_ERROR);
            }
            Workbook workbook = createWorkbook(file);
            return parse(workbook, sheetName, totalColumnNum);
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.error("", e);
            throw new BizException(SYSTEM_ERROR);
        }
    }

    /**
     * 创建Workbook
     *
     * @param file
     * @return
     */
    private Workbook createWorkbook(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            InputStream is = file.getInputStream();
            Workbook workbook = null;
            //根据后缀，得到不同的Workbook子类，即HSSFWorkbook或XSSFWorkbook
            if (fileName.endsWith(SUFFIX_2003)) {
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(SUFFIX_2007)) {
                workbook = new XSSFWorkbook(is);
            } else {
                throw new BizException(FILE_FORMAT_ERROR);
            }
            return workbook;
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.error("", e);
            throw new BizException(SYSTEM_ERROR);
        }
    }

    /**
     * 解析指定sheet
     *
     * @param workbook
     * @param sheetName
     * @return
     */
    private List<List<String>> parse(Workbook workbook, String sheetName, int totalColumnNum) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new BizException(FILE_SHEET_ERROR + sheetName);
        }

        List<List<String>> results = new LinkedList<List<String>>();
        int maxRowNum = sheet.getLastRowNum();

        for (int rowNum = 1; rowNum <= maxRowNum; rowNum++) {
            Row row = sheet.getRow(rowNum);

            List<String> rowResult = new ArrayList<String>(totalColumnNum);
            for (int columnNum = 0; columnNum < totalColumnNum; columnNum++) {
                rowResult.add(getCellValue(row.getCell(columnNum, RETURN_BLANK_AS_NULL)));
            }

            results.add(rowResult);
        }

        log.info("共解析{}行", maxRowNum + 1);

        return results;
    }

    /**
     * 解析单元格内容
     *
     * @param cell
     * @return
     */
    private String getCellValue(Cell cell) {
        String cellValue = "";

        if (cell != null) {
            //把数字当成String来读，避免出现1读成1.0的情况
            if (cell.getCellTypeEnum() == NUMERIC) {
                cell.setCellType(STRING);
            }

            switch (cell.getCellTypeEnum()) {
                case NUMERIC:
                case STRING:
                case FORMULA:
                    cellValue = String.valueOf(cell.getStringCellValue());
                    break;
                case BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    break;
                default:
                    throw new BizException(FILE_CELL_ERROR);
            }
        }

        return cellValue;
    }


    /**
     * 反射解析excel文件成beanList
     *
     * @param clazz
     * @param file
     * @param sheetAt
     * @return
     * @throws Exception
     */
    public List<T> getBeanList(Class<T> clazz, MultipartFile file, int sheetAt) {
        if (file.isEmpty() || sheetAt < 0) {
            throw new BizException(FILE_PARA_ERROR);
        }
        Workbook workbook = createWorkbook(file);
        Sheet sheet = workbook.getSheetAt(sheetAt);
        if (sheet == null) {
            throw new BizException(FILE_SHEET_AT_ERROR + sheetAt);
        }
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length != sheet.getRow(0).getLastCellNum()) {
            throw new BizException(FILE_CELL_ERROR);
        }
        int maxRowNum = sheet.getLastRowNum();
        List<T> beanList = new ArrayList<>();
        for (int rowNum = 1; rowNum <= maxRowNum; rowNum++) {
            Row row = sheet.getRow(rowNum);
            T bean = null;
            try {
                bean = row2Bean(clazz, row);
            } catch (BizException e1) {
                log.error("excel -> bean error", e1);
                throw e1;
            } catch (Exception e2) {
                log.error("excel -> bean error", e2);
                throw new BizException(CONVERT_ERROR);
            }
            beanList.add(bean);
        }
        return beanList;
    }

    /**
     * row->bean
     *
     * @param clazz
     * @param row
     * @return
     * @throws Exception
     */
    private T row2Bean(Class<T> clazz, Row row) throws Exception {
        T bean = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        // 获取fields（按声明时顺序）
        List<Field> fieldList = getOrderedFields(fields);
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (null == cell) {
                continue;
            }
            fieldList.get(i).setAccessible(true);
            switch (cell.getCellTypeEnum()) {
                case NUMERIC:
                    // 处理日期格式格式
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        fields[i].set(bean, date);
                        // 把数字当成String来读，避免出现1读成1.0的情况
                    } else {
                        cell.setCellType(STRING);
                        fields[i].set(bean, cell.getStringCellValue().trim());
                    }
                    break;
                case STRING:
                case FORMULA:
                    fields[i].set(bean, cell.getStringCellValue().trim());
                    break;
                case BOOLEAN:
                    fields[i].setBoolean(bean, cell.getBooleanCellValue());
                    break;
                case BLANK:
                    break;
                default:
                    throw new BizException(FILE_CELL_ERROR);
            }
        }
        return bean;
    }

    /**
     * 获取Field声明时顺序（依赖BeanFieldAnnotation）
     * @param fields
     * @return
     */
    private List<Field> getOrderedFields(Field[] fields) {
        // 用来存放所有的属性域
        List<Field> fieldList = new ArrayList<>();
        // 过滤带有注解的Field
        for (Field f : fields) {
            if (f.getAnnotation(BeanFieldAnnotation.class) != null) {
                fieldList.add(f);
            }
        }
        // 排序
        fieldList.sort(Comparator.comparingInt(
                m -> m.getAnnotation(BeanFieldAnnotation.class).order()
        ));
        return fieldList;
    }

}
