package com.netty.excel.content;

import com.netty.excel.service.ExcelExportStrategy;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * 导出上下文
 * @author 86136
 */
public class ExcelExportContent {
    private ExcelExportStrategy excelExportStrategy;

    public SXSSFWorkbook getSXSSFWorkbook(){
       return excelExportStrategy.getSXSSFWorkbook();
    }

    public ExcelExportContent(ExcelExportStrategy excelExportStrategy) {
        this.excelExportStrategy = excelExportStrategy;
    }
}
