package com.hashedin.hashedinbank.services.impl;

import com.hashedin.hashedinbank.entities.Expense;
import com.hashedin.hashedinbank.services.ExportToExcelHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Component
public class ExportToExcelService {
    public static final String EXCEL_CONTENT_DISPOSITION = "attachment; filename=";
    private final String EXCEL_FONT_NAME = "Calibri";
    private final int EXCEL_FONT_SIZE = 11;
    private final int INITIAL_HEADER_ROW_VALUE = 8;

    /**
     * creating and saving excel file
     */
    public void createExcel(ExportToExcelHandler exportToExcelHandler, List<Expense> expenses, String companyCode) throws IOException {
        if (!expenses.isEmpty()) {

            try (FileOutputStream outputStream = new FileOutputStream("src/main/resources/monthlyExpenseFiles/" + companyCode + "_" + exportToExcelHandler.getWorkBookFileName());
                 SXSSFWorkbook workbook = new SXSSFWorkbook()) {
                log.debug("Creating excel sheet..");

                SXSSFSheet sheet = workbook.createSheet(exportToExcelHandler.getExcelSheetName());

                //Creating Styles
                log.debug("Creating excel style..");
                XSSFCellStyle style = createExcelStyles(workbook);

                //Add Company Logo
                log.debug("Adding company logo to excel sheet..");
                addLogo(workbook, sheet);        //Add Export Details
                log.debug("Writing search export details to excel sheet..");
                expenseExportDetails(sheet, style, exportToExcelHandler, expenses);

                //Create Header Row
                log.debug("Writing header row to excel sheet..");
                createHeaderRow(workbook, sheet, exportToExcelHandler);
                log.debug("Writing search filter Data to excel sheet..");
                exportToExcelHandler.writeDataToExcel(workbook, sheet, style, new AtomicInteger(INITIAL_HEADER_ROW_VALUE), expenses);

                exportToExcelHandler.setAndCalculateGrandTotal(workbook,sheet,style);

                //Write the workbook in output stream
                workbook.write(outputStream);
            } catch (Exception e) {
                log.info(e.getMessage());
                throw e;
            }
        }

    }

    /**
     * to add logo inside excel sheet
     */
    private void addLogo(SXSSFWorkbook workbook, SXSSFSheet sheet) throws IOException {
        InputStream logoImage = getClass().getClassLoader().getResourceAsStream("HashedinBankLogo.png");
        byte[] bytes = IOUtils.toByteArray(Objects.requireNonNull(logoImage));
        logoImage.close();
        SXSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor logoSizeCoverage = new XSSFClientAnchor();
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
        logoSizeCoverage.setCol1(0);
        logoSizeCoverage.setRow1(0);
        logoSizeCoverage.setCol2(1);
        logoSizeCoverage.setRow2(3);
        drawing.createPicture(logoSizeCoverage, workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG));
    }

    /**
     * adding export details columns
     */
    private void expenseExportDetails(SXSSFSheet sheet, XSSFCellStyle style, ExportToExcelHandler
            exportToExcelHandler, List<Expense> expenses) {
        createCell(sheet.createRow(0), 1, "Export Name", style);
        createCell(sheet.getRow(0), 2, exportToExcelHandler.getExportName(), style);
        createCell(sheet.createRow(1), 1, "Export Date", style);
        createCell(sheet.getRow(1), 2, LocalDate.now(), style);
        createCell(sheet.createRow(2), 1, "Number of Rows", style);
        createCell(sheet.getRow(2), 2, expenses.size(), style);
    }

    /**
     * to create cells based on data type
     */
    private void createCell(SXSSFRow row, int column, Object value, XSSFCellStyle style) {
        Cell cell = row.createCell(column);
        String classType = value.getClass().getName();
        switch (classType.substring(classType.lastIndexOf('.') + 1)) {
            case "Integer" -> cell.setCellValue((Integer) value);
            case "String" -> cell.setCellValue(String.valueOf(value));
            case "Boolean" -> cell.setCellValue((Boolean) value);
            case "LocalDate" -> cell.setCellValue(LocalDate.now().toString());
        }
        cell.setCellStyle(style);
    }

    /**
     * creating header row in excel
     */
    private void createHeaderRow(SXSSFWorkbook workbook, SXSSFSheet sheet, ExportToExcelHandler
            exportToExcelHandler) {
        SXSSFRow header = sheet.createRow(7);
        header.setHeight((short) 550);
        XSSFCellStyle headerStyle = createExcelStyles(workbook);
        headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontName(EXCEL_FONT_NAME);
        font.setFontHeightInPoints((short) (EXCEL_FONT_SIZE + 1));
        font.setBold(true);
        headerStyle.setFont(font);
        List<String> headerTitles = exportToExcelHandler.getExcelHeaderRowTitles();
        AtomicInteger cellNum = new AtomicInteger();
        headerTitles.forEach(headerTitle -> {
            SXSSFCell headerCell = header.createCell(cellNum.getAndIncrement());
            headerCell.setCellValue(headerTitle);
            headerCell.setCellStyle(headerStyle);
        });
    }

    /**
     * defining styles to each cell
     */
    private XSSFCellStyle createExcelStyles(SXSSFWorkbook workbook) {
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        style.setWrapText(true);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontName(EXCEL_FONT_NAME);
        font.setFontHeightInPoints((short) EXCEL_FONT_SIZE);
        style.setFont(font);
        return style;
    }
}
