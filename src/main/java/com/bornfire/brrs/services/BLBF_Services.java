package com.bornfire.brrs.services;


import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.brrs.entities.BLBF_Entity;
import com.bornfire.brrs.entities.BLBF_Rep;

@Service
@Transactional
public class BLBF_Services {

    @Autowired
    private BLBF_Rep BLBF_Reps;

    @Autowired
    private DataSource dataSource; // Inject DataSource for JDBC
   
    
    
    private static final Logger logger = LoggerFactory.getLogger(BLBF_Services.class);

    @Transactional
    public String addBLBF(MultipartFile file, String userid, String username) {
        long startTime = System.currentTimeMillis();
        int savedCount = 0, skippedCount = 0;
        int batchSize = 500;

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is);
             Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            String insertSql = "INSERT INTO BRRS_BLBF (" +
                    "CUST_ID, SOL_ID, ACCOUNT_NO, ACCT_NAME, SCHM_CODE, SCHM_DESC, ACCT_OPN_DATE, APPROVED_LIMIT, " +
                    "SANCTION_LIMIT, DISBURSED_AMT, BALANCE_AS_ON, CCY, BAL_EQUI_TO_BWP, INT_RATE, HUNDRED, ACCRUED_INT_AMT, " +
                    "INT_OF_AUG_25, LAST_INTEREST_DEBIT_DATE, ACCT_CLS_FLG, CLOSE_DATE, GENDER, CLASSIFICATION_CODE, " +
                    "CONSTITUTION_CODE, MATURITY_DATE, GL_SUB_HEAD_CODE, GL_SUB_HEAD_DESC, TENOR_MONTH, EMI, SEGMENT, FACILITY, " +
                    "PAST_DUE, PAST_DUE_DAYS, ASSET, PROVISION, UNSECURED, INT_BUCKET, STAFF, SMME, LABOD, NEW_AC, UNDRAWN, " +
                    "SECTOR, PERIOD, EFFECTIVE_INTEREST_RATE, STAGE, ECL_PROVISION,REPORT_DATE, BRANCH_NAME, BRANCH_CODE, " +
                    "ENTRY_DATE, ENTRY_USER, ENTRY_FLG, DEL_FLG" +
                    ") VALUES (" + String.join(",", Collections.nCopies(53, "?")) + ")";

            PreparedStatement stmt = conn.prepareStatement(insertSql);
            int count = 0;

            // === Loop Rows ===
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // ✅ Skip blank rows
                boolean emptyRow = true;
                for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                    Cell cell = row.getCell(cn);
                    if (cell != null && !formatter.formatCellValue(cell, evaluator).trim().isEmpty()) {
                        emptyRow = false;
                        break;
                    }
                }
                if (emptyRow) continue;

                try {
                    int col = 0;

                    stmt.setString(++col, getCellStringSafe(row, 1, formatter, evaluator)); // CUST_ID
                    stmt.setString(++col, getCellStringSafe(row, 0, formatter, evaluator)); // SOL_ID
                    stmt.setString(++col, getCellStringSafe(row, 2, formatter, evaluator)); // ACCOUNT_NO
                    stmt.setString(++col, getCellStringSafe(row, 3, formatter, evaluator)); // ACCT_NAME
                    stmt.setString(++col, getCellStringSafe(row, 4, formatter, evaluator)); // SCHM_CODE
                    stmt.setString(++col, getCellStringSafe(row, 5, formatter, evaluator)); // SCHM_DESC

                    stmt.setDate(++col, getCellDateSafe(row, 6, formatter, evaluator));  // ACCT_OPN_DATE
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 7, formatter, evaluator)); // APPROVED_LIMIT
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 8, formatter, evaluator)); // SANCTION_LIMIT
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator)); // DISBURSED_AMT
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 10, formatter, evaluator)); // BALANCE_AS_ON
                    stmt.setString(++col, getCellStringSafe(row, 11, formatter, evaluator)); // CCY
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator)); // BAL_EQUI_TO_BWP
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 13, formatter, evaluator)); // INT_RATE
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 14, formatter, evaluator)); // HUNDRED
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 15, formatter, evaluator)); // ACCRUED_INT_AMT
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 16, formatter, evaluator)); // INT_OF_AUG_25
                    stmt.setDate(++col, getCellDateSafe(row, 17, formatter, evaluator));          // LAST_INTEREST_DEBIT_DATE
                    stmt.setString(++col, getCellStringSafe(row, 18, formatter, evaluator));      // ACCT_CLS_FLG
                    stmt.setDate(++col, getCellDateSafe(row, 19, formatter, evaluator));          // CLOSE_DATE
                    stmt.setString(++col, getCellStringSafe(row, 20, formatter, evaluator));      // GENDER
                    stmt.setString(++col, getCellStringSafe(row, 21, formatter, evaluator));      // CLASSIFICATION_CODE
                    stmt.setString(++col, getCellStringSafe(row, 22, formatter, evaluator));      // CONSTITUTION_CODE
                    stmt.setDate(++col, getCellDateSafe(row, 23, formatter, evaluator));          // MATURITY_DATE
                    stmt.setString(++col, getCellStringSafe(row, 24, formatter, evaluator));      // GL_SUB_HEAD_CODE
                    stmt.setString(++col, getCellStringSafe(row, 25, formatter, evaluator));      // GL_SUB_HEAD_DESC
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 26, formatter, evaluator)); // TENOR_MONTH
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 27, formatter, evaluator)); // EMI
                    stmt.setString(++col, getCellStringSafe(row, 28, formatter, evaluator));      // SEGMENT
                    stmt.setString(++col, getCellStringSafe(row, 29, formatter, evaluator));      // FACILITY
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 30, formatter, evaluator)); // PAST_DUE
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 31, formatter, evaluator)); // PAST_DUE_DAYS
                    stmt.setString(++col, getCellStringSafe(row, 32, formatter, evaluator));      // ASSET
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 33, formatter, evaluator)); // PROVISION
                    stmt.setString(++col, getCellStringSafe(row, 34, formatter, evaluator));      // UNSECURED
                    stmt.setString(++col, getCellStringSafe(row, 35, formatter, evaluator));      // INT_BUCKET
                    stmt.setString(++col, getCellStringSafe(row, 36, formatter, evaluator));      // STAFF
                    stmt.setString(++col, getCellStringSafe(row, 37, formatter, evaluator));      // SMME
                    stmt.setString(++col, getCellStringSafe(row, 38, formatter, evaluator));      // LABOD
                    stmt.setString(++col, getCellStringSafe(row, 39, formatter, evaluator));      // NEW_AC
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 40, formatter, evaluator)); // UNDRAWN
                    stmt.setString(++col, getCellStringSafe(row, 41, formatter, evaluator));      // SECTOR
                    stmt.setString(++col, getCellStringSafe(row, 42, formatter, evaluator));      // PERIOD
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 43, formatter, evaluator)); // EFFECTIVE_INTEREST_RATE
                    stmt.setString(++col, getCellStringSafe(row, 44, formatter, evaluator));      // STAGE
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 45, formatter, evaluator)); // ECL_PROVISION

                    // ✅ Branch details & Report Date from Excel
                    stmt.setString(++col, getCellStringSafe(row, 46, formatter, evaluator)); // BRANCH_NAME
                    stmt.setString(++col, getCellStringSafe(row, 47, formatter, evaluator)); // BRANCH_CODE
                    java.sql.Date reportDateFromExcel = getCellDateSafe(row, 48, formatter, evaluator); // REPORT_DATE
                    stmt.setDate(++col, reportDateFromExcel);

                    // ✅ Audit fields
                    stmt.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // ENTRY_DATE
                    stmt.setString(++col, userid);                                      // ENTRY_USER
                    stmt.setString(++col, "Y");                                         // ENTRY_FLG
                    stmt.setString(++col, "N");                                         // DEL_FLG

                    stmt.addBatch();
                    count++;

                    if (count % batchSize == 0) {
                        stmt.executeBatch();
                        conn.commit();
                        evaluator.clearAllCachedResultValues();
                    }

                } catch (Exception ex) {
                    skippedCount++;
                    logger.error("Skipping row {} due to error: {}", i, ex.getMessage(), ex);
                }
            }

            stmt.executeBatch();
            conn.commit();

            long duration = System.currentTimeMillis() - startTime;
           /* return "BLBF Added successfully. Saved: " + count + ", Skipped: " + skippedCount +
                    ". Time taken: " + duration + " ms";
*/
            return "BLBF Added successfully.";

        } catch (Exception e) {
            logger.error("Error while processing BLBF Excel: {}", e.getMessage(), e);
            return "Error Occurred while reading Excel: " + e.getMessage();
        }
    }


    // ===== Helper methods =====
    private String getCellStringSafe(Row row, int index, DataFormatter formatter, FormulaEvaluator evaluator) {
        Cell cell = row.getCell(index);
        if (cell == null) return null;
        return formatter.formatCellValue(cell, evaluator).trim();
    }

    private java.sql.Date getCellDateSafe(Row row, int colIndex, DataFormatter formatter, FormulaEvaluator evaluator) {
        try {
            Cell cell = row.getCell(colIndex);
            if (cell == null) return null;

            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return new java.sql.Date(cell.getDateCellValue().getTime());
            } else {
                // Parse text in dd-MM-yyyy format
                String text = formatter.formatCellValue(cell, evaluator).trim();
                if (text.isEmpty()) return null;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // match Excel format
                return new java.sql.Date(sdf.parse(text).getTime());
            }
        } catch (Exception e) {
            return null;
        }
    }



    private BigDecimal getCellDecimalSafe(Row row, int index, DataFormatter formatter, FormulaEvaluator evaluator) {
        Cell cell = row.getCell(index);
        if (cell == null) return null;
        try {
            return new BigDecimal(formatter.formatCellValue(cell, evaluator).replaceAll(",", "").trim());
        } catch (Exception e) {
            return null;
        }
    }


}