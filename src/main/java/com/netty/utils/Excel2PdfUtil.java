package com.netty.utils;

import com.aspose.cells.License;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author 86136
 */
public class Excel2PdfUtil {
    /**
     * excel 转 pdf
     * @param excelPath 要转换的excel文件路径
     * @param pdfPath   转换完成后输出的pdf文件路径
     */
    public static void excel2pdf(String excelPath,String pdfPath) {
        if (!getLicense()) {
            return;
        }
        try {
            Workbook convertExcel = new Workbook(new FileInputStream(excelPath));
            convertExcel.save(pdfPath, SaveFormat.PDF);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取license
     *
     * @return
     */
    private static boolean getLicense() {
        boolean result = false;
        try {
            // 凭证
            String license =
                    "<License>\n" +
                            "  <Data>\n" +
                            "    <Products>\n" +
                            "      <Product>Aspose.Total for Java</Product>\n" +
                            "      <Product>Aspose.Words for Java</Product>\n" +
                            "    </Products>\n" +
                            "    <EditionType>Enterprise</EditionType>\n" +
                            "    <SubscriptionExpiry>20991231</SubscriptionExpiry>\n" +
                            "    <LicenseExpiry>20991231</LicenseExpiry>\n" +
                            "    <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n" +
                            "  </Data>\n" +
                            "  <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\n" +
                            "</License>";
            InputStream is = new ByteArrayInputStream(license.getBytes("UTF-8"));
            License asposeLic = new License();
            asposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        String excelPath="C:\\Users\\86136\\Desktop\\test.xlsx";
        String pdfPath="C:\\Users\\86136\\Desktop\\test.pdf";
        excel2pdf(excelPath,pdfPath);
    }
}
