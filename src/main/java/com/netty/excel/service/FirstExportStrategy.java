package com.netty.excel.service;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 * 导出策略实现类
 * @author 86136
 */
@Service
public class FirstExportStrategy implements ExcelExportStrategy{
    @Override
    public SXSSFWorkbook getSXSSFWorkbook() {
        return null;
    }
}
