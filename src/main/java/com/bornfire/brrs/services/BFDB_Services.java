package com.bornfire.brrs.services;


import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.brrs.entities.BFDB_Entity;
import com.bornfire.brrs.entities.BFDB_Rep;


@Service
@Transactional
public class BFDB_Services {

    @Autowired
    private BFDB_Rep BFDB_Reps;

    @Autowired
    private DataSource dataSource; // Inject DataSource for JDBC
   
    
    private static final Logger logger = LoggerFactory.getLogger(BFDB_Services.class);

   
    @Transactional
    public String addBFDB(MultipartFile file, String userid, String username) {
        long startTime = System.currentTimeMillis();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is);
             Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            String insertSql = "INSERT INTO BRRS_BFDB (" +
                    "CUST_ID, SOL_ID, GENDER, ACCOUNT_NO, ACCT_NAME, SCHM_CODE, SCHM_DESC, " +
                    "ACCT_OPN_DATE, ACCT_CLS_DATE, BALANCE_AS_ON, CCY, BAL_EQUI_TO_BWP, INT_RATE, HUNDRED, " +
                    "STATUS, MATURITY_DATE, GL_SUB_HEAD_CODE, GL_SUB_HEAD_DESC, TYPE_OF_ACCOUNTS, SEGMENT, PERIOD, " +
                    "EFFECTIVE_INT_RATE, BRANCH_NAME, BRANCH_CODE, REPORT_DATE, ENTRY_DATE, ENTRY_USER, " +
                    "DEL_FLG, ENTRY_FLG) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStmt = conn.prepareStatement(insertSql);

            int batchSize = 500;
            int count = 0;
            int skippedCount = 0;

            // Loop through rows (skip header)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // ✅ Skip truly empty rows
                boolean isEmpty = true;
                for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                    Cell cell = row.getCell(cn);
                    if (cell != null && !formatter.formatCellValue(cell, evaluator).trim().isEmpty()) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) continue;

                try {
                    int col = 0;

                    String custId = getCellString(row.getCell(1), formatter, evaluator);
                    if (custId == null || custId.isEmpty()) {
                        skippedCount++;
                        continue;
                    }

                    insertStmt.setString(++col, custId);
                    insertStmt.setString(++col, getCellString(row.getCell(0), formatter, evaluator));
                    insertStmt.setString(++col, getCellString(row.getCell(2), formatter, evaluator));
                    insertStmt.setString(++col, getCellString(row.getCell(3), formatter, evaluator));
                    insertStmt.setString(++col, getCellString(row.getCell(4), formatter, evaluator));
                    insertStmt.setString(++col, getCellString(row.getCell(5), formatter, evaluator));
                    insertStmt.setString(++col, getCellString(row.getCell(6), formatter, evaluator));

                    insertStmt.setDate(++col, getCellDate(row.getCell(7), formatter, evaluator));
                    insertStmt.setDate(++col, getCellDate(row.getCell(8), formatter, evaluator));
                    insertStmt.setBigDecimal(++col, getCellDecimal(row.getCell(9), formatter, evaluator));

                    insertStmt.setString(++col, getCellString(row.getCell(10), formatter, evaluator));
                    insertStmt.setBigDecimal(++col, getCellDecimal(row.getCell(11), formatter, evaluator));
                    insertStmt.setBigDecimal(++col, getCellDecimal(row.getCell(12), formatter, evaluator));
                    insertStmt.setBigDecimal(++col, getCellDecimal(row.getCell(13), formatter, evaluator));
                    insertStmt.setString(++col, getCellString(row.getCell(14), formatter, evaluator));
                    insertStmt.setDate(++col, getCellDate(row.getCell(15), formatter, evaluator));
                    insertStmt.setString(++col, getCellString(row.getCell(16), formatter, evaluator));
                    insertStmt.setString(++col, getCellString(row.getCell(17), formatter, evaluator));
                    insertStmt.setString(++col, getCellString(row.getCell(18), formatter, evaluator));
                    insertStmt.setString(++col, getCellString(row.getCell(19), formatter, evaluator));
                    insertStmt.setString(++col, getCellString(row.getCell(20), formatter, evaluator));
                    insertStmt.setBigDecimal(++col, getCellDecimal(row.getCell(21), formatter, evaluator));
                    insertStmt.setString(++col, getCellString(row.getCell(22), formatter, evaluator));
                    insertStmt.setString(++col, getCellString(row.getCell(23), formatter, evaluator));

                    // ✅ Get report date from Excel (e.g., last column index 24)
                    java.sql.Date reportDateFromExcel = getCellDate(row.getCell(24), formatter, evaluator);
                    insertStmt.setDate(++col, reportDateFromExcel);

                    // audit fields
                    insertStmt.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // ENTRY_DATE
                    insertStmt.setString(++col, userid);
                    insertStmt.setString(++col, "N");
                    insertStmt.setString(++col, "Y");

                    insertStmt.addBatch();
                    count++;

                    if (count % batchSize == 0) {
                        insertStmt.executeBatch();
                        conn.commit();
                        evaluator.clearAllCachedResultValues();
                    }

                } catch (Exception rowEx) {
                    skippedCount++;
                    logger.error("Skipping row {} due to error: {}", i, rowEx.getMessage(), rowEx);
                }
            }

            insertStmt.executeBatch();
            conn.commit();

            long duration = System.currentTimeMillis() - startTime;
            return "BFDB Added successfully. Saved: " + count + ", Skipped: " + skippedCount +
                    ". Time taken: " + duration + " ms";

        } catch (Exception e) {
            logger.error("Error while processing BFDB Excel: {}", e.getMessage(), e);
            return "Error Occurred while reading Excel: " + e.getMessage();
        }
    }


    /* ---------------- helper methods ---------------- */

    private String getCellString(Cell cell, DataFormatter formatter, FormulaEvaluator evaluator) {
        if (cell == null) return null;
        String value = formatter.formatCellValue(cell, evaluator).trim();
        return value.isEmpty() ? null : value;
    }

    private BigDecimal getCellDecimal(Cell cell, DataFormatter formatter, FormulaEvaluator evaluator) {
        if (cell == null) return null;
        // First try formatted text (handles formulas evaluated to text)
        String text = formatter.formatCellValue(cell, evaluator).trim();
        if (!text.isEmpty()) {
            text = text.replaceAll(",", ""); // remove thousands separators
            // handle parentheses as negative
            if (text.startsWith("(") && text.endsWith(")")) {
                text = "-" + text.substring(1, text.length() - 1);
            }
            try {
                return new BigDecimal(text);
            } catch (NumberFormatException ignored) { }
        }
        // fallback to numeric evaluation
        try {
            CellValue cv = evaluator.evaluate(cell);
            if (cv != null && cv.getCellType() == CellType.NUMERIC) {
                return BigDecimal.valueOf(cv.getNumberValue());
            } else if (cell.getCellType() == CellType.NUMERIC) {
                return BigDecimal.valueOf(cell.getNumericCellValue());
            }
        } catch (Exception ignored) { }
        return null;
    }

    private java.sql.Date getCellDate(Cell cell, DataFormatter formatter, FormulaEvaluator evaluator) {
        if (cell == null) return null;

        try {
            // 1) If cell is a numeric date (Excel format)
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                java.util.Date d = cell.getDateCellValue();
                return new java.sql.Date(d.getTime()); // preserves full year
            }

            // 2) Evaluate formulas
            CellValue cv = evaluator.evaluate(cell);
            if (cv != null && cv.getCellType() == CellType.NUMERIC) {
                double num = cv.getNumberValue();
                if (DateUtil.isValidExcelDate(num)) {
                    java.util.Date d = DateUtil.getJavaDate(num, false); // false = 1900-based system
                    return new java.sql.Date(d.getTime());
                }
            }

            // 3) Parse text values
            String formatted = formatter.formatCellValue(cell, evaluator).trim();
            if (!formatted.isEmpty()) {
                List<String> patterns = Arrays.asList(
                        "dd-MM-yyyy",
                        "d-M-yyyy",
                        "yyyy-MM-dd",
                        "dd/MM/yyyy",
                        "M/d/yyyy",
                        "dd-MMM-yyyy"
                );
                for (String p : patterns) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(p);
                        sdf.setLenient(false);
                        java.util.Date parsed = sdf.parse(formatted);
                        if (parsed != null) {
                            return new java.sql.Date(parsed.getTime());
                        }
                    } catch (Exception ignored) { }
                }
            }
        } catch (Exception e) {
            logger.debug("date parse/eval error for cell: {}", e.getMessage());
        }

        return null;
    }


}

