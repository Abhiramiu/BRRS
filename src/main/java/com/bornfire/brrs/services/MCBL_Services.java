package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.brrs.config.SequenceGenerator;
import com.bornfire.brrs.entities.MCBL_Detail_Rep;

import com.bornfire.brrs.entities.MCBL_Entity;
import com.bornfire.brrs.entities.MCBL_Main_Entity;

import com.bornfire.brrs.entities.MCBL_Main_Rep;
import com.bornfire.brrs.entities.MCBL_Rep;

@Service
@Transactional
public class MCBL_Services {

    @Autowired
    SequenceGenerator sequence;

    @Autowired
    private MCBL_Main_Rep MCBL_Main_Reps;

    @Autowired
    private MCBL_Detail_Rep MCBL_Detail_Reps;

    @Autowired
	private MCBL_Rep mcblRep;
    
    @Autowired
    private DataSource dataSource;

    private static final Logger logger = LoggerFactory.getLogger(MCBL_Services.class);

    @Transactional
    public String addMCBL(MultipartFile file, String userid, String username, String reportDate) {
        long startTime = System.currentTimeMillis();
        List<String> validationErrors = new ArrayList<>();

        if (file == null || file.isEmpty()) {
            return "Error: Uploaded file is empty!";
        }
        
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is);
             Connection conn = dataSource.getConnection()) {
        	
        	if (workbook.getNumberOfSheets() == 0) {
                return "Error: Excel file has no sheets!";
            }

            conn.setAutoCommit(false);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(reportDate);
            java.sql.Date sqlReportDate = new java.sql.Date(parsedDate.getTime());

            Sheet sheet = workbook.getSheet("MCBL");

            if (sheet == null) {
                return "Error: First sheet is missing! Sheet Name should be MCBL";
            }

            // Prepare delete statements
            String deleteSql = "DELETE FROM BRRS_MCBL " +
                    "WHERE GL_CODE = ? AND GL_SUB_CODE = ? AND HEAD_ACC_NO = ? " +
                    "AND CURRENCY = ? AND REPORT_DATE = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);

            String deleteGeneralSql = "DELETE FROM BRRS_GENERAL_MASTER_TABLE " +
                    "WHERE GL_CODE = ? AND GL_SUB_CODE = ? AND HEAD_ACC_NO = ? " +
                    "AND CURRENCY = ? AND REPORT_DATE = ? AND FILE_TYPE = 'MCBL'";
            PreparedStatement deleteGeneralStmt = conn.prepareStatement(deleteGeneralSql);

            // Prepare insert statements
            String insertSql = "INSERT INTO BRRS_MCBL (GL_CODE, GL_SUB_CODE, HEAD_ACC_NO, DESCRIPTION, CURRENCY, " +
                    "DEBIT_BALANCE, CREDIT_BALANCE, DEBIT_EQUIVALENT, CREDIT_EQUIVALENT, ENTRY_USER, ENTRY_DATE, REPORT_DATE) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);

            String insertGeneralSql = "INSERT INTO BRRS_GENERAL_MASTER_TABLE (" +
            	    "ID, FILE_TYPE, GL_CODE, GL_SUB_CODE, HEAD_ACC_NO, DESCRIPTION, CURRENCY, " +
            	    "DEBIT_BALANCE, CREDIT_BALANCE, DEBIT_EQUIVALENT, CREDIT_EQUIVALENT, " +
            	    "ENTRY_USER, ENTRY_DATE, REPORT_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertGeneralStmt = conn.prepareStatement(insertGeneralSql);

            DataFormatter formatter = new DataFormatter();
            int batchSize = 500;
            int count = 0;
            int skippedCount = 0;

            Set<String> processedKeys = new HashSet<>();

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    System.out.println("Skipping empty row: " + i);
                    continue;
                }
                
                if (row == null) continue;

                String glCode = formatter.formatCellValue(row.getCell(0)).trim();
                String glSubCode = formatter.formatCellValue(row.getCell(1)).trim();
                String headAccNo = formatter.formatCellValue(row.getCell(2)).trim();
                String description = formatter.formatCellValue(row.getCell(3)).trim();
                String currency = formatter.formatCellValue(row.getCell(5)).trim();

                if (!glCode.isEmpty() && !glSubCode.isEmpty() && !headAccNo.isEmpty() && !currency.isEmpty()) {

                    if (isNumeric(headAccNo) || isString(headAccNo)) {
                        String lookupKey = normalizeKey(glCode, glSubCode, headAccNo, currency);
                        String uniqueKey = lookupKey + "|" + sqlReportDate;

                        if (!processedKeys.add(uniqueKey)) {
                            skippedCount++;
                            continue;
                        }

                        // Delete old records
                        deleteStmt.setString(1, glCode);
                        deleteStmt.setString(2, glSubCode);
                        deleteStmt.setString(3, headAccNo);
                        deleteStmt.setString(4, currency);
                        deleteStmt.setDate(5, sqlReportDate);
                        deleteStmt.executeUpdate();

                        deleteGeneralStmt.setString(1, glCode);
                        deleteGeneralStmt.setString(2, glSubCode);
                        deleteGeneralStmt.setString(3, headAccNo);
                        deleteGeneralStmt.setString(4, currency);
                        deleteGeneralStmt.setDate(5, sqlReportDate);
                        deleteGeneralStmt.executeUpdate();

                        // Insert new MCBL record
                        insertStmt.setString(1, glCode);
                        insertStmt.setString(2, glSubCode);
                        insertStmt.setString(3, headAccNo);
                        insertStmt.setString(4, description);
                        insertStmt.setString(5, currency);
                        insertStmt.setBigDecimal(6, getCellDecimal(row.getCell(6)));
                        insertStmt.setBigDecimal(7, getCellDecimal(row.getCell(7)));
                        insertStmt.setBigDecimal(8, getCellDecimal(row.getCell(8)));
                        insertStmt.setBigDecimal(9, getCellDecimal(row.getCell(9)));
                        insertStmt.setString(10, userid);
                        insertStmt.setDate(11, new java.sql.Date(System.currentTimeMillis()));
                        insertStmt.setDate(12, sqlReportDate);
                        insertStmt.addBatch();

                        // Insert into General Master
                        insertGeneralStmt.setString(1, sequence.generateRequestUUId()); // ID
                        insertGeneralStmt.setString(2, "MCBL");                         // FILE_TYPE
                        insertGeneralStmt.setString(3, glCode);                         // GL_CODE
                        insertGeneralStmt.setString(4, glSubCode);                      // GL_SUB_CODE
                        insertGeneralStmt.setString(5, headAccNo);                      // HEAD_ACC_NO
                        insertGeneralStmt.setString(6, description);                    // DESCRIPTION
                        insertGeneralStmt.setString(7, currency);                       // CURRENCY
                        insertGeneralStmt.setBigDecimal(8, getCellDecimal(row.getCell(6))); // DEBIT_BALANCE
                        insertGeneralStmt.setBigDecimal(9, getCellDecimal(row.getCell(7))); // CREDIT_BALANCE
                        insertGeneralStmt.setBigDecimal(10, getCellDecimal(row.getCell(8))); // DEBIT_EQUIVALENT
                        insertGeneralStmt.setBigDecimal(11, getCellDecimal(row.getCell(9))); // CREDIT_EQUIVALENT
                        insertGeneralStmt.setString(12, userid);                           // ENTRY_USER
                        insertGeneralStmt.setDate(13, new java.sql.Date(System.currentTimeMillis())); // ENTRY_DATE
                        insertGeneralStmt.setDate(14, sqlReportDate);                     // REPORT_DATE

                        insertGeneralStmt.addBatch();


                        count++;

                        if (count % batchSize == 0) {
                            insertStmt.executeBatch();
                            insertGeneralStmt.executeBatch();
                            conn.commit();
                        }

                    } else {
                        skippedCount++;
                    }

                } else {
                    skippedCount++;
                }
            }

            // Execute remaining batches
            insertStmt.executeBatch();
            insertGeneralStmt.executeBatch();
            conn.commit();

            long duration = System.currentTimeMillis() - startTime;
            String result = "MCBL Added successfully. Saved: " + count + ", Skipped: " + skippedCount +
                    ". Time taken: " + duration + " ms";

            if (!validationErrors.isEmpty()) {
                result += "\n\nAccounts not found: " + validationErrors.size() + "\n"
                        + String.join("\n", validationErrors);
            }

            return result;

        } catch (Exception e) {
            logger.error("Error while processing MCBL Excel: {}", e.getMessage(), e);
            return "Error Occurred while reading Excel: " + e.getMessage();
        }
    }

    private boolean isString(String value) {
        return value != null && value.matches("[a-zA-Z]+");
    }

    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }

    private String normalizeKey(String glCode, String glSubCode, String headAccNo, String currency) {
        return safeTrim(glCode) + "|" + safeTrim(glSubCode) + "|" + safeTrim(headAccNo) + "|" + safeTrim(currency);
    }

    private String safeTrim(String value) {
        if (value == null) return "NULL";
        value = value.replace(" ", "");
        if (value.endsWith(".0")) value = value.substring(0, value.length() - 2);
        return value.toUpperCase();
    }

    private BigDecimal getCellDecimal(Cell cell) {
        if (cell == null) return BigDecimal.ZERO;
        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING:
                try {
                    return new BigDecimal(cell.getStringCellValue().trim());
                } catch (Exception e) {
                    return BigDecimal.ZERO;
                }
            default:
                return BigDecimal.ZERO;
        }
    }


    
  
    
    
    
    private final ConcurrentHashMap<String, byte[]> jobStorage = new ConcurrentHashMap<>();

    @Async
    public void generateReportAsync(String jobId, String filename, String todate) {
        System.out.println("Starting report generation for: " + filename);
        byte[] fileData = generateMCBLExcel(filename, todate);
        jobStorage.put(jobId, fileData != null ? fileData : null);
        System.out.println("Report generation completed for: " + filename);
    }

    public byte[] getReport(String jobId) {
        return jobStorage.get(jobId);
    }

    public byte[] generateMCBLExcel(String filename, String todate) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("MCBL_Report");

            // ================= Header Style =================
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // ================= Balance / Amount Style =================
            CellStyle balanceStyle = workbook.createCellStyle();
            balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
            balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
            balanceStyle.setBorderTop(BorderStyle.THIN);
            balanceStyle.setBorderBottom(BorderStyle.THIN);
            balanceStyle.setBorderLeft(BorderStyle.THIN);
            balanceStyle.setBorderRight(BorderStyle.THIN);

            // ================= General Data Style =================
            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setBorderTop(BorderStyle.THIN);
            dataCellStyle.setBorderBottom(BorderStyle.THIN);
            dataCellStyle.setBorderLeft(BorderStyle.THIN);
            dataCellStyle.setBorderRight(BorderStyle.THIN);

            // ================= Headers =================
            String[] headers = {"GL Code","GL Sub Code","Head Acc No","Description","Currency",
                                "Debit Balance","Credit Balance","Debit Equivalent","Credit Equivalent","Report Date"};
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 5000);
            }

            // ================= Fetch data from DB =================
            List<MCBL_Entity> dataList = mcblRep.findRecordsByReportDate(todate);

            if (dataList != null && !dataList.isEmpty()) {
                int rowIndex = 1;
                for (MCBL_Entity rec : dataList) {
                    XSSFRow row = sheet.createRow(rowIndex++);

                    // Text / Date cells
                    Cell cell0 = row.createCell(0);
                    cell0.setCellValue(rec.getGl_code());
                    cell0.setCellStyle(dataCellStyle);

                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(rec.getGl_sub_code());
                    cell1.setCellStyle(dataCellStyle);

                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue(rec.getHead_acc_no());
                    cell2.setCellStyle(dataCellStyle);

                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(rec.getDescription());
                    cell3.setCellStyle(dataCellStyle);

                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue(rec.getCurrency());
                    cell4.setCellStyle(dataCellStyle);

                    // Numeric / Amount cells
                    Cell debitCell = row.createCell(5);
                    debitCell.setCellValue(rec.getDebit_balance() != null ? rec.getDebit_balance().doubleValue() : 0);
                    debitCell.setCellStyle(balanceStyle);

                    Cell creditCell = row.createCell(6);
                    creditCell.setCellValue(rec.getCredit_balance() != null ? rec.getCredit_balance().doubleValue() : 0);
                    creditCell.setCellStyle(balanceStyle);

                    Cell debitEqCell = row.createCell(7);
                    debitEqCell.setCellValue(rec.getDebit_equivalent() != null ? rec.getDebit_equivalent().doubleValue() : 0);
                    debitEqCell.setCellStyle(balanceStyle);

                    Cell creditEqCell = row.createCell(8);
                    creditEqCell.setCellValue(rec.getCredit_equivalent() != null ? rec.getCredit_equivalent().doubleValue() : 0);
                    creditEqCell.setCellStyle(balanceStyle);

                    // Report Date cell
                    Cell cell9 = row.createCell(9);
                    cell9.setCellValue(
                        rec.getReport_date() != null ? new SimpleDateFormat("dd-MM-yyyy").format(rec.getReport_date()) : ""
                    );
                    cell9.setCellStyle(dataCellStyle);
                }
            }

            // ================= Write to ByteArray =================
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();
            return bos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

}
