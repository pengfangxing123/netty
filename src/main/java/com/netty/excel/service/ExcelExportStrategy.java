package com.netty.excel.service;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * 导出处理 策略接口
 * @author 86136
 */
public interface ExcelExportStrategy {
    SXSSFWorkbook getSXSSFWorkbook();
}
