
package com.adobe.aem.guides.wknd.core.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExcelReader {

    public static String extractExcelData(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return extractExcelData(fis);
        }
    }

    public static String extractExcelData(InputStream inputStream) throws IOException {
        StringBuilder data = new StringBuilder();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            for (Sheet sheet : workbook) {
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        data.append(cell.toString()).append("\t");
                    }
                    data.append("\n");
                }
            }
        }
        return data.toString();
    }
}

