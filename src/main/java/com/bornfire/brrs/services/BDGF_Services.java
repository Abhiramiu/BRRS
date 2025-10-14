package com.bornfire.brrs.services;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.brrs.config.SequenceGenerator;
import com.bornfire.brrs.entities.BDGF_Entity;
import com.bornfire.brrs.entities.BDGF_Rep;
import com.bornfire.brrs.entities.BrrsGeneralMasterEntity;
import com.bornfire.brrs.entities.BrrsGeneralMasterRepo;

@Service
@Transactional
public class BDGF_Services {

    @Autowired
    SequenceGenerator sequence;

    @Autowired
    private BDGF_Rep BDGF_Reps;
    @Autowired
    BrrsGeneralMasterRepo BrrsGeneralMasterRepos;

    @Autowired
    private DataSource dataSource; // Inject DataSource for JDBC
   
    
    
    private static final Logger logger = LoggerFactory.getLogger(BDGF_Services.class);

    @Transactional
    public String addBDGF(MultipartFile file, String userid, String username) {
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

            String insertSql = "INSERT INTO BRRS_BDGF (" +
            	       "SOL_ID,S_NO, ACC_NO, CUSTOMER_ID, CUSTOMER_NAME, OPEN_DATE, AMOUNT_DEPOSITED, CURRENCY, PERIOD, " +
            	       "RATE_OF_INTEREST, HUNDRED, BAL_EQUI_TO_BWP, OUTSTANDING_BALANCE, OUSTNDNG_BAL_UGX, " +
            	       "MATURITY_DATE, MATURITY_AMOUNT, SCHEME, CR_PREF_INT_RATE, SEGMENT, REFERENCE_DATE, " +
            	       "DIFFERENCE, DAYS, PERIOD_DAYS, EFFECTIVE_INT_RATE, REPORT_DATE, ENTRY_DATE, " +
            	       "ENTRY_USER, ENTRY_FLG, DEL_FLG" +
            	       ") VALUES (" + String.join(",", Collections.nCopies(29, "?")) + ")";

            PreparedStatement stmt = conn.prepareStatement(insertSql);
            int count = 0;

            // === Loop through rows ===
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Skip blank rows
                boolean emptyRow = true;
                for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                    if (!formatter.formatCellValue(row.getCell(cn), evaluator).trim().isEmpty()) {
                        emptyRow = false;
                        break;
                    }
                }
                if (emptyRow) continue;

                try {
                    int col = 0;
                    
                    stmt.setString(++col, getCellStringSafe(row, 0, formatter, evaluator)); // SOL ID
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 1, formatter, evaluator)); // S_NO
                    stmt.setString(++col, getCellStringSafe(row, 2, formatter, evaluator));      // ACC_NO
                    stmt.setString(++col, getCellStringSafe(row, 3, formatter, evaluator));      // CUSTOMER_ID
                    stmt.setString(++col, getCellStringSafe(row, 4, formatter, evaluator));      // CUSTOMER_NAME
                    
                    stmt.setDate(++col, getCellDateSafe(row, 5, formatter, evaluator));          // OPEN_DATE
                   
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 6, formatter, evaluator)); // AMOUNT_DEPOSITED
                    stmt.setString(++col, getCellStringSafe(row, 7, formatter, evaluator));      // CURRENCY
                    stmt.setString(++col, getCellStringSafe(row, 8, formatter, evaluator));      // PERIOD
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator)); // RATE_OF_INTEREST
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 10, formatter, evaluator));// HUNDRED
                  
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 11, formatter, evaluator));// BAL_EQUI_TO_BWP
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator));// OUTSTANDING_BALANCE
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 13, formatter, evaluator));// OUSTNDNG_BAL_UGX
                    
                    stmt.setDate(++col, getCellDateSafe(row, 14, formatter, evaluator));          // MATURITY_DATE
                    
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 15, formatter, evaluator));// MATURITY_AMOUNT
                    stmt.setString(++col, getCellStringSafe(row, 16, formatter, evaluator));      // SCHEME
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 17, formatter, evaluator));// CR_PREF_INT_RATE
                    stmt.setString(++col, getCellStringSafe(row, 18, formatter, evaluator));      // SEGMENT
                    
                    stmt.setDate(++col, getCellDateSafe(row, 19, formatter, evaluator));          // REFERENCE_DATE
                    
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 20, formatter, evaluator));// Difference
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 21, formatter, evaluator));// DAYS
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 22, formatter, evaluator));// PERIOD_DAYS
                    stmt.setBigDecimal(++col, getCellDecimalSafe(row, 23, formatter, evaluator));// EFFECTIVE_INT_RATE
                    
                    stmt.setDate(++col, getCellDateSafe(row, 24, formatter, evaluator));          // REPORT_DATE
                    
                    // Audit fields
                    stmt.setDate(++col, new java.sql.Date(System.currentTimeMillis()));
                    stmt.setString(++col, userid);
                    stmt.setString(++col, "Y");
                    stmt.setString(++col, "N");

                    stmt.addBatch();

                    // === Master Entity ===
                    BrrsGeneralMasterEntity master = new BrrsGeneralMasterEntity();
                    master.setId(sequence.generateRequestUUId());
                    master.setFile_type("BDGF");
                   
                    master.setSol_id(getCellString(row.getCell(0), formatter, evaluator));
                    
                    master.setS_no(getCellDecimal(row.getCell(1), formatter, evaluator));
                    
                    master.setAcc_no(getCellString(row.getCell(2), formatter, evaluator));
                    master.setCustomer_id(getCellString(row.getCell(3), formatter, evaluator));
                    master.setCustomer_name(getCellString(row.getCell(4), formatter, evaluator));
                    master.setOpen_date(getCellDate(row.getCell(5), formatter, evaluator));
                    
                    master.setAmount_deposited(getCellDecimal(row.getCell(6), formatter, evaluator));
                    master.setCurrency(getCellString(row.getCell(7), formatter, evaluator));
                    master.setPeriod(getCellString(row.getCell(8), formatter, evaluator));
                    master.setRate_of_interest(getCellDecimal(row.getCell(9), formatter, evaluator));
                    master.setHundred(getCellDecimal(row.getCell(10), formatter, evaluator));
                    master.setBal_equi_to_bwp(getCellDecimal(row.getCell(11), formatter, evaluator));
                    master.setOutstanding_balance(getCellDecimal(row.getCell(12), formatter, evaluator));
                    master.setOustndng_bal_ugx(getCellDecimal(row.getCell(13), formatter, evaluator));
                    master.setMaturity_date(getCellDate(row.getCell(14), formatter, evaluator));
                    master.setMaturity_amount(getCellDecimal(row.getCell(15), formatter, evaluator));
                    master.setScheme(getCellString(row.getCell(16), formatter, evaluator));
                    master.setCr_pref_int_rate(getCellDecimal(row.getCell(17), formatter, evaluator));
                    master.setSegment(getCellString(row.getCell(18), formatter, evaluator));
                    master.setReference_date(getCellDate(row.getCell(19), formatter, evaluator));
                    
                    master.setDifference(getCellDecimal(row.getCell(20), formatter, evaluator));
                    master.setDays(getCellDecimal(row.getCell(21), formatter, evaluator));
                    master.setPeriod_days(getCellDecimal(row.getCell(22), formatter, evaluator));
                    master.setEffective_int_rate(getCellDecimal(row.getCell(23), formatter, evaluator));
                    master.setReport_date(getCellDate(row.getCell(24), formatter, evaluator));

                    // Audit
                    master.setEntry_date(new Date());
                    master.setEntry_user(userid);
                    master.setDel_flg("N");
                    master.setEntry_flg("Y");

                    BrrsGeneralMasterRepos.save(master);

                    savedCount++;

                    if (++count % batchSize == 0) {
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
            return "BDGF Added successfully. Saved: " + savedCount + ", Skipped: " + skippedCount +
                    ". Time taken: " + duration + " ms";

        } catch (Exception e) {
            logger.error("Error while processing BDGF Excel: {}", e.getMessage(), e);
            return "Error Occurred while reading Excel: " + e.getMessage();
        }
    }

    
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

    
    private String getCellString(Cell cell, DataFormatter f, FormulaEvaluator e) {
        if (cell == null) return null;
        return f.formatCellValue(cell, e).trim();
    }

    private BigDecimal getCellDecimal(Cell cell, DataFormatter f, FormulaEvaluator e) {
        try {
            String val = f.formatCellValue(cell, e).replace(",", "").trim();
            return val.isEmpty() ? null : new BigDecimal(val);
        } catch (Exception ex) {
            return null;
        }
    }

    private java.util.Date getCellDate(Cell cell, DataFormatter f, FormulaEvaluator e) {
        try {
            if (cell == null) return null;
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
                return cell.getDateCellValue();
            String text = f.formatCellValue(cell, e);
            if (text.isEmpty()) return null;
            return new SimpleDateFormat("dd-MM-yyyy").parse(text);
        } catch (Exception ex) {
            return null;
        }
    }



    // 🔹 Helper methods to safely parse Excel cells
    private String getString(Cell cell) {
        if (cell == null) return null;
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    private BigDecimal getBigDecimal(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return BigDecimal.valueOf(cell.getNumericCellValue());
            } else {
                String str = cell.toString().trim();
                return str.isEmpty() ? null : new BigDecimal(str);
            }
        } catch (Exception e) {
            return null;
        }
    }
    private Long getLong(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (long) cell.getNumericCellValue();
            } else {
                String str = cell.toString().trim();
                return str.isEmpty() ? null : Long.parseLong(str);
            }
        } catch (Exception e) {
            return null;
        }
    }


    private Date getDate(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        } else {
            try {
                String str = cell.toString().trim();
                if (str.isEmpty()) return null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.parse(str);
            } catch (Exception e) {
                return null;
            }
        }
    }
    
    
    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = cell.toString().trim();
                if (!value.isEmpty()) return false;
            }
        }
        return true;
    }

}
