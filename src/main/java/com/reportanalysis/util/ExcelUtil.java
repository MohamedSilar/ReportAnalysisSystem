package com.reportanalysis.util;

import com.reportanalysis.model.DailyReport;
import com.reportanalysis.model.ExportError;
import com.reportanalysis.model.ExportSummary;
import com.reportanalysis.model.User;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ExcelUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static String generateUserReportExcel(User user,List<DailyReport> reports,ExportSummary summary) {

        String filePath = "user_report_" + user.getUsername() + ".xlsx";

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(200);
             FileOutputStream fileOut = new FileOutputStream(filePath)) {

            workbook.setCompressTempFiles(true);

            Sheet sheet = workbook.createSheet("User Report");

            CellStyle headerStyle = createHeaderStyle(workbook);

            int rowIndex = 0;
            Row row1 = sheet.createRow(rowIndex++);
            row1.createCell(0).setCellValue("Username:");
            row1.createCell(1).setCellValue(safe(user.getUsername()));

            Row row2 = sheet.createRow(rowIndex++);
            row2.createCell(0).setCellValue("Email:");
            row2.createCell(1).setCellValue(safe(user.getEmail()));

            rowIndex++;

            String[] headers = {"Date", "Chats", "Calls","Meetings", "Webinars"};

            Row headerRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            for (DailyReport report : reports) {

                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(report.getReportDate() == null ? "" : report.getReportDate().toString());

                row.createCell(1).setCellValue(report.getChats());
                row.createCell(2).setCellValue(report.getCalls());
                row.createCell(3).setCellValue(report.getMeetings());
                row.createCell(4).setCellValue(report.getWebinars());
            }

            rowIndex++;

            createSummaryRow(sheet, rowIndex++, "Total Chats", summary.getTotalChats());
            createSummaryRow(sheet, rowIndex++, "Total Calls", summary.getTotalCalls());
            createSummaryRow(sheet, rowIndex++, "Total Meetings", summary.getTotalMeetings());
            createSummaryRow(sheet, rowIndex++, "Total Webinars", summary.getTotalWebinars());

            workbook.write(fileOut);
            workbook.dispose();

        } catch (Exception e) {
            throw new RuntimeException("User Excel generation failed", e);
        }

        return filePath;
    }
    
    public static String generateExcelWithErrorSheet(List<Object[]> successData,List<ExportError> errorData) {

        String filePath = "User Report " + ".xlsx";

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(200);
             FileOutputStream fileOut = new FileOutputStream(filePath)) {

            workbook.setCompressTempFiles(true);
            SXSSFSheet successSheet = (SXSSFSheet) workbook.createSheet("Success Data");
            successSheet.trackAllColumnsForAutoSizing();


            String[] successHeaders = {"Report ID","Username","Email","Country Code","Mobile","Chats","Calls","Meetings","Webinars","Created Time"};

            Row headerRow = successSheet.createRow(0);

            for (int i = 0; i < successHeaders.length; i++) {
                headerRow.createCell(i).setCellValue(successHeaders[i]);
            }

            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper creationHelper = workbook.getCreationHelper();
            short dateFormat = creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss");
            dateStyle.setDataFormat(dateFormat);

            int rowNum = 1;

            for (Object[] rowData : successData) {

                Row row = successSheet.createRow(rowNum++);

                for (int i = 0; i < rowData.length; i++) {

                    Cell cell = row.createCell(i);

                    if (i == 9 && rowData[i] != null) {

                        try {
                            long epoch = Long.parseLong(rowData[i].toString());
                            Date date = new Date(epoch);
                            cell.setCellValue(date);
                            cell.setCellStyle(dateStyle);
                        } catch (Exception e) {
                            cell.setCellValue(rowData[i].toString());
                        }

                    } else {
                        cell.setCellValue(rowData[i] == null ? "" : rowData[i].toString());
                    }
                }
            }
            for (int i = 0; i < successHeaders.length; i++) {
                successSheet.autoSizeColumn(i);
            }
            SXSSFSheet errorSheet = (SXSSFSheet) workbook.createSheet("Error Data");
            errorSheet.trackAllColumnsForAutoSizing();


            String[] errorHeaders = {"User ID","Report ID","Report Date","Exception Type","Exception Message"};

            Row errorHeaderRow = errorSheet.createRow(0);

            for (int i = 0; i < errorHeaders.length; i++) {
                errorHeaderRow.createCell(i).setCellValue(errorHeaders[i]);
            }

            int errorRowNum = 1;

            for (ExportError error : errorData) {

                Row row = errorSheet.createRow(errorRowNum++);

                row.createCell(0).setCellValue(error.getUserId() != null ? error.getUserId() : 0);
                row.createCell(1).setCellValue(error.getReportId() != null ? error.getReportId() : 0);
                row.createCell(2).setCellValue(error.getReportDate() != null ? error.getReportDate() : "");
                row.createCell(3).setCellValue(error.getExceptionType() != null ? error.getExceptionType() : "");
                row.createCell(4).setCellValue(error.getExceptionMessage() != null ? error.getExceptionMessage() : "");
            }
            for (int i = 0; i < errorHeaders.length; i++) {
                errorSheet.autoSizeColumn(i);
            }

            workbook.write(fileOut);
            workbook.dispose();

        } catch (Exception e) {
            throw new RuntimeException("Excel with error sheet generation failed", e);
        }

        return filePath;
    }



    private static String safe(String value) {
        return value == null ? "" : value;
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {

        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        return style;
    }
    private static void createSummaryRow(Sheet sheet,int rowIndex,String label,int value) {

        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label + ":");
        row.createCell(1).setCellValue(value);
    }
}
