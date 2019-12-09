package com.netty.excel.demo;

import com.google.common.collect.Lists;
import com.netty.utils.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 86136
 */
public class ExportExcelByPoiUtil {

    public static String createExcel(HttpServletRequest request, HttpServletResponse response, String[] title,
                                     String titleHead , int[] widthAttr, Map<String/*sheet名*/,
            List<Map<String/*对应title的值*/, String>>> maps, int[] mergeIndex){
        if (title.length==0){
            return null;
        }
        /*初始化excel模板*/
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = null;
        int n = 0;
        /*循环sheet页*/
        for(Map.Entry<String, List<Map<String/*对应title的值*/, String>>> entry : maps.entrySet()){
            /*实例化sheet对象并且设置sheet名称，book对象*/
            try {
                sheet = workbook.createSheet();
                workbook.setSheetName(n, entry.getKey());
                workbook.setSelectedTab(0);
            }catch (Exception e){
                e.printStackTrace();
            }
            // 设置样式 头 cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            // 水平方向的对齐方式
            CellStyle cellStyle_head = style(0, workbook);
            // 导出时间
            CellStyle cellStyle_export = style(3, workbook);
            // 标题
            CellStyle cellStyle_title = style(1, workbook);
            // 正文
            CellStyle cellStyle = style(2, workbook);
            // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
            CellRangeAddress c1 = new CellRangeAddress(0, 0, 0, title.length-1);
            sheet.addMergedRegion(c1);
            CellRangeAddress c2 = new CellRangeAddress(1, 1, 0, title.length-1);
            sheet.addMergedRegion(c2);
            // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            Row row0 = sheet.createRow(0);
            // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
            Cell cell1 = row0.createCell(0);
            // 设置单元格内容 标题
            cell1.setCellValue("上海明华电力———" + titleHead + "一览表");
            cell1.setCellStyle(cellStyle_head);
            // 设置合并单元格边框
            setRegionStyle(sheet, c1, cellStyle_head);
            setRegionStyle(sheet, c2, cellStyle_export);
            // 设置列宽
            for (int i = 0; i < widthAttr.length; i++) {
                sheet.setColumnWidth((short) i, (short) widthAttr[i] * 200);
            }
            // 在sheet里创建第二行
            Row row1 = sheet.createRow(1);
            // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
            Cell cell2 = row1.createCell(0);
            // 设置单元格内容 标题
            cell2.setCellValue("导出时间：" + DateUtil.getDateTimeFormat(new Date()));
            cell2.setCellStyle(cellStyle_export);
            /*初始化标题，填值标题行（第一行）*/
            Row row2 = sheet.createRow(2);
            for(int i = 0; i<title.length; i++){
                /*创建单元格，指定类型*/
                Cell cell_1 = row2.createCell(i, Cell.CELL_TYPE_STRING);
                //设置标题的值
                cell_1.setCellValue(title[i]);
                //设置标题样式
                cell_1.setCellStyle(cellStyle_title);
            }
            /*得到当前sheet下的数据集合*/
            List<Map<String/*对应title的值*/, String>> list = entry.getValue();
            /*遍历该数据集合*/
            List<PoiModel> poiModels = Lists.newArrayList();
            if(null!=workbook){
                Iterator iterator = list.iterator();
//                int index = 1;/*这里1是从excel的第二行开始，第一行已经塞入标题了*/
                int index = 3;/*这里3是从excel的第四行开始，前面几行已经塞入标题了*/
                while (iterator.hasNext()){
                    Row row = sheet.createRow(index);
                    /*取得当前这行的map，该map中以key，value的形式存着这一行值*/
                    @SuppressWarnings("unchecked")
                    Map<String, String> map = (Map<String, String>)iterator.next();
                    /*循环列数，给当前行塞值*/
                    for(int i = 0; i<title.length; i++){
                        String old = "";
                        /*old存的是上一行统一位置的单元的值，第一行是最上一行了，所以从第二行开始记*/
                        if(index > 3){
                            old = poiModels.get(i)==null ? "":poiModels.get(i).getContent();
                        }
                        /*循环需要合并的列*/
                        for(int j = 0; j < mergeIndex.length; j++){
                            /* 因为标题行前还有2行  所以index从3开始    也就是第四行*/
                            if(index == 3){
                                /*记录第一行的开始行和开始列*/
                                PoiModel poiModel = new PoiModel();
                                poiModel.setOldContent(map.get(title[i]));
                                poiModel.setContent(map.get(title[i]));
                                poiModel.setRowIndex(3);
                                poiModel.setCellIndex(i);
                                poiModels.add(poiModel);
                                break;
                            }else if(i > 0 && mergeIndex[j] == i){
                                /*这边i>0也是因为第一列已经是最前一列了，只能从第二列开始*/
                                /*当前同一列的内容与上一行同一列不同时，把那以上的合并, 或者在当前元素一样的情况下，前一列的元素并不一样，这种情况也合并*/
                                /*如果不需要考虑当前行与上一行内容相同，但是它们的前一列内容不一样则不合并的情况，把下面条件中||poiModels.get(i).getContent().equals(map.get(title[i])) && !poiModels.get(i - 1).getOldContent().equals(map.get(title[i-1]))去掉就行*/
                                if(!poiModels.get(i).getContent().equals(map.get(title[i])) || poiModels.get(i).getContent().equals(map.get(title[i])) && !poiModels.get(i - 1).getOldContent().equals(map.get(title[i-1]))){
                                    /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/
                                    CellRangeAddress cra=new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/, index - 1/*到第几行*/, poiModels.get(i).getCellIndex()/*从某一列开始*/, poiModels.get(i).getCellIndex()/*到第几列*/);
                                    //在sheet里增加合并单元格
                                    sheet.addMergedRegion(cra);
                                    /*重新记录该列的内容为当前内容，行标记改为当前行标记，列标记则为当前列*/
                                    poiModels.get(i).setContent(map.get(title[i]));
                                    poiModels.get(i).setRowIndex(index);
                                    poiModels.get(i).setCellIndex(i);
                                }
                            }
                            /*处理第一列的情况*/
                            if(mergeIndex[j] == i && i == 0 && !poiModels.get(i).getContent().equals(map.get(title[i]))){
                                /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/
                                CellRangeAddress cra=new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/, index - 1/*到第几行*/, poiModels.get(i).getCellIndex()/*从某一列开始*/, poiModels.get(i).getCellIndex()/*到第几列*/);
                                //在sheet里增加合并单元格
                                sheet.addMergedRegion(cra);
                                /*重新记录该列的内容为当前内容，行标记改为当前行标记*/
                                poiModels.get(i).setContent(map.get(title[i]));
                                poiModels.get(i).setRowIndex(index);
                                poiModels.get(i).setCellIndex(i);
                            }

                            /*最后一行没有后续的行与之比较，所有当到最后一行时则直接合并对应列的相同内容  加2是因为标题行前面还有2行*/
                            if(mergeIndex[j] == i && index == list.size()+2){
                                CellRangeAddress cra = new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/, index/*到第几行*/, poiModels.get(i).getCellIndex()/*从某一列开始*/, poiModels.get(i).getCellIndex()/*到第几列*/);
                                //在sheet里增加合并单元格
                                sheet.addMergedRegion(cra);
                            }
                        }
                        Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                        cell.setCellValue(map.get(title[i]));
                        cell.setCellStyle(cellStyle);
                        /*在每一个单元格处理完成后，把这个单元格内容设置为old内容*/
                        poiModels.get(i).setOldContent(old);
                    }
                    index++;
                }
            }
            n++;
        }

        OutputStream out = null;
        String localPath = null;
        try {
            Calendar calendar1 = Calendar.getInstance();
            String cal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar1.getTime());
            out = response.getOutputStream();
            response.reset();//清空输出流
            response.setHeader("Content-disposition", "attachment;filename=" + new String(titleHead.getBytes("gbk"), "iso8859-1") + cal + ".xlsx");// 设定输出文件头
            response.setContentType("application/vnd.ms-excel;charset=GBK");// 定义输出类型
            workbook.write(out);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                out.flush();
                out.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return localPath;
    }

    /**
     *
     * @Title: style
     * @Description: TODO(样式)
     * @author: GMY
     * @date: 2018年5月9日 下午5:16:48
     * @param @return    设定文件  index 0:头 1：标题 2：正文
     * @return HSSFCellStyle    返回类型
     * @throws
     */
    public static CellStyle style(int index, Workbook workbook) {
        CellStyle cellStyle = null;
        if (index == 0) {
            // 设置头部样式
            cellStyle = workbook.createCellStyle();
            // 设置字体大小 位置
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            // 生成一个字体
            Font font = workbook.createFont();
            //设置字体
            font.setFontName("微软雅黑");
            //字体颜色
            font.setColor(HSSFColor.BLACK.index);// HSSFColor.VIOLET.index
            font.setFontHeightInPoints((short) 12);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
            cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);//背景白色
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyle.setFont(font);
        }
        //标题
        if (index == 1) {
            cellStyle = workbook.createCellStyle();
            // 设置字体大小 位置
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            // 生成一个字体
            Font font_title = workbook.createFont();
            //设置字体
            font_title.setFontName("微软雅黑");
            font_title.setColor(HSSFColor.BLACK.index);// HSSFColor.VIOLET.index
            //字体颜色
            font_title.setFontHeightInPoints((short) 10);
            font_title.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
            cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
            cellStyle.setFont(font_title);
        }
        //正文
        if (index == 2) {
            // 设置样式
            cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            // 生成一个字体
            Font font_title = workbook.createFont();
            //设置字体
            font_title.setFontName("微软雅黑");
//            cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
            cellStyle.setWrapText(true); // 自动换行
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);//背景白色
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
        }
        //时间
        if (index == 3) {
            // 设置样式
            cellStyle = workbook.createCellStyle();
            // 居中
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            // 生成一个字体
            Font font_title = workbook.createFont();
            //设置字体
            font_title.setFontName("微软雅黑");
            font_title.setColor(HSSFColor.BLACK.index);// HSSFColor.VIOLET.index
            // //字体颜色
            font_title.setFontHeightInPoints((short) 10);
            font_title.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
            cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setFont(font_title);

        }
        if (index == 4) {
            // 设置样式
            cellStyle = workbook.createCellStyle();
            // 居中
//            cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
        }
        return cellStyle;
    }
    /**
     *
     * @Title: setRegionStyle
     * @Description: TODO(合并单元格后边框不显示问题)
     * @author: GMY
     * @date: 2018年5月10日 上午10:46:00
     * @param @param sheet
     * @param @param region
     * @param @param cs    设定文件
     * @return void    返回类型
     * @throws
     */
    public static void setRegionStyle(Sheet sheet, CellRangeAddress region, CellStyle cs) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            Row row = CellUtil.getRow(i, sheet);
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                Cell cell = CellUtil.getCell(row, (short) j);
                cell.setCellStyle(cs);
            }
        }
    }
}
