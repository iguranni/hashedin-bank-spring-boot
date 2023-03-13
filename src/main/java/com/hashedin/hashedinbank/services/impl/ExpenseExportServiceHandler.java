package com.hashedin.hashedinbank.services.impl;

import com.hashedin.hashedinbank.entities.Expense;
import com.hashedin.hashedinbank.services.ExportToExcelHandler;
import com.hashedin.hashedinbank.utils.ExportToExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Component
public class ExpenseExportServiceHandler implements ExportToExcelHandler {
    private static final String HEADER_ROW_TITLES = "EXPENSE_ID,BILL_ID,USER_ID,EXPENSE_CATEGORY_ID,TOTAL_EXPENDITURE,RECEIPT_DATE,COMMENTS";
    private static final String WORKBOOK_FILENAME = ExportToExcelUtil.getWorkBookFileName("MonthlyExpenseExport");
    private static final String EXCEL_SHEET_NAME = ExportToExcelUtil.getWorkBookSheetName("MonthlyExpenseExport");
    private static final String EXPORT_NAME = "Hashedin Bank Expense Details";

    @Autowired
    public ExpenseExportServiceHandler() {
    }

    /**
     * @return
     */
    @Override
    public String getExcelSheetName() {
        return EXCEL_SHEET_NAME;
    }

    /**
     * @return
     */
    @Override
    public String getExportName() {
        return EXPORT_NAME;
    }

    /**
     * @return
     */
    @Override
    public String getWorkBookFileName() {
        return WORKBOOK_FILENAME;
    }

    /**
     * @return
     */
    @Override
    public List<String> getExcelHeaderRowTitles() {
        return List.of(HEADER_ROW_TITLES.split(","));
    }

    /**
     * @param sheet
     * @param style
     * @param rowNum
     */
    @Override
    public void writeDataToExcel(SXSSFWorkbook workbook, SXSSFSheet sheet, XSSFCellStyle style, AtomicInteger rowNum, List<Expense> expenses) {
        //Iterate over data and write to sheet
        for (Expense expenseResult : expenses) {
            SXSSFRow row = sheet.createRow(rowNum.getAndIncrement());

            row.createCell(0).setCellValue(expenseResult.getExpenseId() == null ? "" : String.valueOf(expenseResult.getExpenseId()));
            sheet.setColumnWidth(0, 4000);
            row.getCell(0).setCellStyle(style);

            row.createCell(1).setCellValue(expenseResult.getBillId() == null ? "" : String.valueOf(expenseResult.getBillId()));
            sheet.setColumnWidth(1, 10000);
            row.getCell(1).setCellStyle(style);

            row.createCell(2).setCellValue(expenseResult.getUserId().getUserId() == null ? "" : String.valueOf(expenseResult.getUserId().getUserId()));
            sheet.setColumnWidth(2, 10000);
            row.getCell(2).setCellStyle(style);

            row.createCell(3).setCellValue(expenseResult.getExpenseCategoryId() == null ? "" : String.valueOf(expenseResult.getExpenseCategoryId().getExpenseCategoryId()));
            sheet.setColumnWidth(3, 10000);
            row.getCell(3).setCellStyle(style);

            row.createCell(4).setCellValue(Optional.of(expenseResult.getTotalExpenditure().doubleValue())
                    .orElse((double) 0));
            sheet.setColumnWidth(4, 8000);
            row.getCell(4).setCellStyle(style);

            row.createCell(5).setCellValue(Optional.ofNullable(expenseResult.getReceiptDate())
                    .map(LocalDate::toString).orElse(""));
            sheet.setColumnWidth(5, 10000);
            row.getCell(5).setCellStyle(style);

            row.createCell(6).setCellValue(Optional.ofNullable(expenseResult.getComments())
                    .orElse(""));
            sheet.setColumnWidth(6, 8000);
            row.getCell(6).setCellStyle(style);
        }
    }

    @Override
    public void setAndCalculateGrandTotal(SXSSFWorkbook workbook, SXSSFSheet sheet, XSSFCellStyle style) {
        int cellNo = sheet.getRow(8).getLastCellNum();
        SXSSFRow row = sheet.createRow(4);
        row.createCell(1).setCellValue("Grand Total : ");
        sheet.setColumnWidth(1, 4000);
        row.getCell(1).setCellStyle(style);

        Cell cell = row.createCell(2);
        sheet.setColumnWidth(2, 4000);
        cell.setCellStyle(style);

        cell.setCellFormula("SUM(E9:E"+cellNo+")");
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        evaluator.evaluateFormulaCell(cell);
    }
}
