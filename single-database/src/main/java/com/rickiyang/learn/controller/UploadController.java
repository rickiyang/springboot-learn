package com.rickiyang.learn.controller;

import com.rickiyang.learn.common.BaseResponse;
import com.rickiyang.learn.utils.ExcelParseUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author: rickiyang
 * @date: 2019/2/20
 * @description: 上传Excel 文件，获取单元格
 */
@RestController
@RequestMapping(value = "/upload")
public class UploadController {
    private static final String GREY_BLACK_LIST_SHEET_NAME = "Sheet1";
    private static final int GREY_BLACK_LIST_COLUMN_NUM = 7;

    @RequestMapping(value = "/getExcelData", method = RequestMethod.POST)
    public BaseResponse uploadGreyBlackList(@RequestParam("file") MultipartFile file){
        ExcelParseUtil excelParseUtil = new ExcelParseUtil();
        List<List<String>> resultList = excelParseUtil.parseWorkBook(file, GREY_BLACK_LIST_SHEET_NAME, GREY_BLACK_LIST_COLUMN_NUM);

        return BaseResponse.success(resultList);
    }
}
