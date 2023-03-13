package com.hashedin.hashedinbank.services;

import com.hashedin.hashedinbank.entities.Expense;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface ExportToExcelHandler {
    String getExcelSheetName();

    String getExportName();

    String getWorkBookFileName();

    List<String> getExcelHeaderRowTitles();

    void writeDataToExcel(SXSSFWorkbook workbook, SXSSFSheet sheet, XSSFCellStyle style, AtomicInteger headerRow, List<Expense> expenses);
    void setAndCalculateGrandTotal(SXSSFWorkbook workbook, SXSSFSheet sheet, XSSFCellStyle style);

}
