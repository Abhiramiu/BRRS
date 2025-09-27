package com.bornfire.brrs.services;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.brrs.config.SequenceGenerator;
import com.bornfire.brrs.entities.MCBL_Detail_Entity;
import com.bornfire.brrs.entities.MCBL_Detail_Rep;
import com.bornfire.brrs.entities.MCBL_Main_Entity;
import com.bornfire.brrs.entities.MCBL_Main_Rep;

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
    private DataSource dataSource; // Inject DataSource for JDBC
    
    private static final Logger logger = LoggerFactory.getLogger(MCBL_Services.class);

    @Transactional
    public String addMCBL(MultipartFile file, String userid, String username, String reportDate) {
        long startTime = System.currentTimeMillis();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is);
             Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false); // batch mode

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(reportDate);
            java.sql.Date sqlReportDate = new java.sql.Date(parsedDate.getTime());  // force sql.Date


            Sheet sheet = workbook.getSheet("MCBL");

            // --- Step 1: Load main table into Map ---
            List<MCBL_Main_Entity> mainlist = MCBL_Main_Reps.getall();
           System.out.println("mail list count is : "+ mainlist.size());
            Map<String, MCBL_Main_Entity> mainMap = new HashMap<>();
           
            for (MCBL_Main_Entity mainRow : mainlist) {
                String key = normalizeKey(mainRow.getGl_code(),
                                          mainRow.getGl_sub_code(),
                                          mainRow.getHead_acc_no(),
                                          mainRow.getCurrency());
                System.out.println("MainMap Key: " + key);
                mainMap.put(key, mainRow);
            }

            if (mainMap.isEmpty()) {
                return "No data in Main Table, skipping processing.";
            }

            // --- Step 2: Prepare statements ---
            String deleteSql = "DELETE FROM BRRS_MCBL_DETAIL " +
                               "WHERE GL_CODE = ? AND GL_SUB_CODE = ? AND HEAD_ACC_NO = ? " +
                               "AND CURRENCY = ? AND REPORT_DATE = ?";

            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);

            String insertSql = "INSERT INTO BRRS_MCBL_DETAIL (ID, GL_CODE, GL_SUB_CODE, HEAD_ACC_NO, DESCRIPTION, CURRENCY, " +
                               "DEBIT_BALANCE, CREDIT_BALANCE, DEBIT_EQUIVALENT, CREDIT_EQUIVALENT, ENTRY_USER, ENTRY_DATE, REPORT_DATE) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStmt = conn.prepareStatement(insertSql);

            DataFormatter formatter = new DataFormatter();
            int batchSize = 500;
            int count = 0;
            int skippedCount = 0;

            // --- Step 3: Loop through Excel rows ---
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String glCode = formatter.formatCellValue(row.getCell(0));
                String glSubCode = formatter.formatCellValue(row.getCell(1));
                String headAccNo = formatter.formatCellValue(row.getCell(2));
                String description = formatter.formatCellValue(row.getCell(3));
                String currency = formatter.formatCellValue(row.getCell(5)); // ⚠️ verify column index!

                String lookupKey = normalizeKey(glCode, glSubCode, headAccNo, currency);

                // Debugging log
                if (!mainMap.containsKey(lookupKey)) {
                    skippedCount++;
                    System.out.println("❌ Skipped Row " + i +
                        " | ExcelKey=" + lookupKey +
                        " | ExcelRaw=[" + glCode + "," + glSubCode + "," + headAccNo + "," + currency + "]");
                    continue;
                } else {
                    System.out.println("✅ Matched Row " + i + " | Key=" + lookupKey);
                }

             // --- Delete old ---
                deleteStmt.setString(1, glCode);
                deleteStmt.setString(2, glSubCode);
                deleteStmt.setString(3, headAccNo);
                deleteStmt.setString(4, currency);
                deleteStmt.setDate(5, sqlReportDate); // ✅ fixed
                deleteStmt.executeUpdate();

                // --- Insert new ---
                insertStmt.setString(1, java.util.UUID.randomUUID().toString());
                insertStmt.setString(2, glCode);
                insertStmt.setString(3, glSubCode);
                insertStmt.setString(4, headAccNo);
                insertStmt.setString(5, description);
                insertStmt.setString(6, currency);
                insertStmt.setBigDecimal(7, getCellDecimal(row.getCell(6)));
                insertStmt.setBigDecimal(8, getCellDecimal(row.getCell(7)));
                insertStmt.setBigDecimal(9, getCellDecimal(row.getCell(8)));
                insertStmt.setBigDecimal(10, getCellDecimal(row.getCell(9)));
                insertStmt.setString(11, userid);
                insertStmt.setDate(12, new java.sql.Date(System.currentTimeMillis())); // ✅ fixed
                insertStmt.setDate(13, sqlReportDate); // ✅ fixed

                insertStmt.addBatch();
                count++;

                if (count % batchSize == 0) {
                    insertStmt.executeBatch();
                    conn.commit();
                }
            }

            insertStmt.executeBatch();
            conn.commit();

            long duration = System.currentTimeMillis() - startTime;
            return "MCBL Added successfully. Saved: " + count + ", Skipped: " + skippedCount +
                   ". Time taken: " + duration + " ms";

        } catch (Exception e) {
            logger.error("Error while processing MCBL Excel: {}", e.getMessage(), e);
            return "Error Occurred while reading Excel: " + e.getMessage();
        }
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

    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        try {
            CellType type = cell.getCellType(); // now returns enum
            switch (type) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                    }
                    return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook()
                            .getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);
                    if (cellValue == null) return "";
                    switch (cellValue.getCellType()) { // also enum
                        case STRING:
                            return cellValue.getStringValue().trim();
                        case NUMERIC:
                            return BigDecimal.valueOf(cellValue.getNumberValue()).toPlainString();
                        case BOOLEAN:
                            return String.valueOf(cellValue.getBooleanValue());
                        default:
                            return "";
                    }
                case BLANK:
                default:
                    return "";
            }
        } catch (Exception e) {
            return "";
        }
    }


    private BigDecimal parseNumber(String val) {
        if (val == null || val.trim().isEmpty())
            return BigDecimal.ZERO;
        val = val.replace(",", "");
        try {
            return new BigDecimal(val);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}