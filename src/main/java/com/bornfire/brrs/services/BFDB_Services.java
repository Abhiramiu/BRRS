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

import com.bornfire.brrs.config.SequenceGenerator;
import com.bornfire.brrs.entities.BDGF_Rep;
import com.bornfire.brrs.entities.BFDB_Entity;
import com.bornfire.brrs.entities.BFDB_Rep;
import com.bornfire.brrs.entities.BrrsGeneralMasterEntity;
import com.bornfire.brrs.entities.BrrsGeneralMasterRepo;


@Service
@Transactional
public class BFDB_Services {

    @Autowired
    SequenceGenerator sequence;

    @Autowired
    BrrsGeneralMasterRepo BrrsGeneralMasterRepos;
    @Autowired
    private BFDB_Rep BFDB_Reps;

    @Autowired
    private DataSource dataSource; // Inject DataSource for JDBC
   
    
    private static final Logger logger = LoggerFactory.getLogger(BFDB_Services.class);

   
    @Transactional
    public String addBFDB(MultipartFile file, String userid, String username) {
        long startTime = System.currentTimeMillis();
        int batchSize = 500;
        int count = 0;
        int skippedCount = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is);
             Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            // JDBC Insert SQL
            String insertSql = "INSERT INTO BRRS_BFDB (" +
                    "CUST_ID, GENDER, ACCOUNT_NO, ACCT_NAME, SCHM_CODE, SCHM_DESC, " +
                    "ACCT_OPN_DATE, ACCT_CLS_DATE, BALANCE_AS_ON, CCY, BAL_EQUI_TO_BWP, " +
                    "INT_RATE, HUNDRED, STATUS, MATURITY_DATE, GL_SUB_HEAD_CODE, GL_SUB_HEAD_DESC, " +
                    "TYPE_OF_ACCOUNTS, SEGMENT, PERIOD, EFFECTIVE_INT_RATE, " +
                    "REPORT_DATE, ENTRY_DATE, ENTRY_USER, DEL_FLG, ENTRY_FLG) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStmt = conn.prepareStatement(insertSql);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Skip empty row
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
                    // --- Read Excel cells ---
                    String custId = getCellString(row.getCell(0), formatter, evaluator);
                    if (custId == null || custId.isEmpty()) {
                        skippedCount++;
                        continue;
                    }

                    int col = 0;
                    insertStmt.setString(++col, custId); // CUST_ID
                    insertStmt.setString(++col, getCellString(row.getCell(1), formatter, evaluator)); // GENDER
                    insertStmt.setString(++col, getCellString(row.getCell(2), formatter, evaluator)); // ACCOUNT_NO
                    insertStmt.setString(++col, getCellString(row.getCell(3), formatter, evaluator)); // ACCT_NAME
                    insertStmt.setString(++col, getCellString(row.getCell(4), formatter, evaluator)); // SCHM_CODE
                    insertStmt.setString(++col, getCellString(row.getCell(5), formatter, evaluator)); // SCHM_DESC
                    insertStmt.setDate(++col, getCellDate(row.getCell(6), formatter, evaluator)); // ACCT_OPN_DATE
                    insertStmt.setDate(++col, getCellDate(row.getCell(7), formatter, evaluator)); // ACCT_CLS_DATE
                    insertStmt.setBigDecimal(++col, getCellDecimal(row.getCell(8), formatter, evaluator)); // BALANCE_AS_ON
                    insertStmt.setString(++col, getCellString(row.getCell(9), formatter, evaluator)); // CCY
                    insertStmt.setBigDecimal(++col, getCellDecimal(row.getCell(10), formatter, evaluator)); // BAL_EQUI_TO_BWP
                    insertStmt.setBigDecimal(++col, getCellDecimal(row.getCell(11), formatter, evaluator)); // INT_RATE
                    insertStmt.setBigDecimal(++col, getCellDecimal(row.getCell(12), formatter, evaluator)); // 100
                    insertStmt.setString(++col, getCellString(row.getCell(13), formatter, evaluator)); // STATUS
                    insertStmt.setDate(++col, getCellDate(row.getCell(14), formatter, evaluator)); // MATURITY_DATE
                    insertStmt.setString(++col, getCellString(row.getCell(15), formatter, evaluator)); // GL_SUB_HEAD_CODE
                    insertStmt.setString(++col, getCellString(row.getCell(16), formatter, evaluator)); // GL_SUB_HEAD_DESC
                    insertStmt.setString(++col, getCellString(row.getCell(17), formatter, evaluator)); // TYPE
                    insertStmt.setString(++col, getCellString(row.getCell(18), formatter, evaluator)); // SEGMENT
                    insertStmt.setString(++col, getCellString(row.getCell(19), formatter, evaluator)); // PERIOD
                    insertStmt.setBigDecimal(++col, getCellDecimal(row.getCell(20), formatter, evaluator)); // EFFECTIVE_INTEREST_RATE

                    java.sql.Date reportDateFromExcel = getCellDate(row.getCell(6), formatter, evaluator); // Use ACCT_OPN_DATE as report date
                    insertStmt.setDate(++col, reportDateFromExcel); // REPORT_DATE
                    insertStmt.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // ENTRY_DATE
                    insertStmt.setString(++col, userid); // ENTRY_USER
                    insertStmt.setString(++col, "N"); // DEL_FLG
                    insertStmt.setString(++col, "Y"); // ENTRY_FLG

                    insertStmt.addBatch();

                    // --- Create Master Entity ---
                    BrrsGeneralMasterEntity masterEntity = new BrrsGeneralMasterEntity();
                    masterEntity.setId(sequence.generateRequestUUId());
                    masterEntity.setFile_type("BFDB");
                    masterEntity.setCustomer_id(custId);
                    masterEntity.setGender(getCellString(row.getCell(1), formatter, evaluator));
                    masterEntity.setAcc_no(getCellString(row.getCell(2), formatter, evaluator));
                    masterEntity.setCustomer_name(getCellString(row.getCell(3), formatter, evaluator));
                    masterEntity.setSchm_code(getCellString(row.getCell(4), formatter, evaluator));
                    masterEntity.setSchm_desc(getCellString(row.getCell(5), formatter, evaluator));
                    masterEntity.setOpen_date(getCellDate(row.getCell(6), formatter, evaluator));
                    masterEntity.setAcct_cls_date(getCellDate(row.getCell(7), formatter, evaluator));
                    masterEntity.setAmount_deposited(getCellDecimal(row.getCell(8), formatter, evaluator));
                    masterEntity.setCurrency(getCellString(row.getCell(9), formatter, evaluator));
                    masterEntity.setBal_equi_to_bwp(getCellDecimal(row.getCell(10), formatter, evaluator));
                    masterEntity.setRate_of_interest(getCellDecimal(row.getCell(11), formatter, evaluator));
                    masterEntity.setHundred(getCellDecimal(row.getCell(12), formatter, evaluator));
                    masterEntity.setStatus(getCellString(row.getCell(13), formatter, evaluator));
                    masterEntity.setMaturity_date(getCellDate(row.getCell(14), formatter, evaluator));
                    masterEntity.setGl_sub_head_code(getCellString(row.getCell(15), formatter, evaluator));
                    masterEntity.setGl_sub_head_desc(getCellString(row.getCell(16), formatter, evaluator));
                    masterEntity.setType_of_accounts(getCellString(row.getCell(17), formatter, evaluator));
                    masterEntity.setSegment(getCellString(row.getCell(18), formatter, evaluator));
                    masterEntity.setPeriod(getCellString(row.getCell(19), formatter, evaluator));
                    masterEntity.setEffective_int_rate(getCellDecimal(row.getCell(20), formatter, evaluator));
                    masterEntity.setEntry_date(new Date());
                    masterEntity.setEntry_user(userid);
                    masterEntity.setDel_flg("N");
                    masterEntity.setEntry_flg("Y");

                    BrrsGeneralMasterRepos.save(masterEntity);

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
            // 1) Numeric cell with Excel date
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                java.util.Date d = cell.getDateCellValue();
                if (d != null && d.getYear() + 1900 >= 1900) { // Ensure year >= 1900
                    return new java.sql.Date(d.getTime());
                }
                return null;
            }

            // 2) Formula evaluation
            CellValue cv = evaluator.evaluate(cell);
            if (cv != null && cv.getCellType() == CellType.NUMERIC) {
                double num = cv.getNumberValue();
                if (DateUtil.isValidExcelDate(num)) {
                    java.util.Date d = DateUtil.getJavaDate(num, false);
                    if (d.getYear() + 1900 >= 1900) {
                        return new java.sql.Date(d.getTime());
                    }
                }
            }

            // 3) Parse text
            String formatted = formatter.formatCellValue(cell, evaluator).trim();
            if (!formatted.isEmpty()) {
                List<String> patterns = Arrays.asList(
                        "dd-MM-yyyy", "d-M-yyyy", "yyyy-MM-dd", 
                        "dd/MM/yyyy", "M/d/yyyy", "dd-MMM-yyyy"
                );
                for (String p : patterns) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(p);
                        sdf.setLenient(false);
                        java.util.Date parsed = sdf.parse(formatted);
                        if (parsed != null && parsed.getYear() + 1900 >= 1900) {
                            return new java.sql.Date(parsed.getTime());
                        }
                    } catch (Exception ignored) {}
                }
            }

        } catch (Exception e) {
            logger.debug("date parse/eval error for cell '{}': {}", cell, e.getMessage());
        }

        // Return null if all parsing fails
        return null;
    }


}

