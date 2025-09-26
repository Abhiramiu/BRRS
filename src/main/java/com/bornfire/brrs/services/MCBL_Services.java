package com.bornfire.brrs.services;


import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    @PersistenceContext
    private EntityManager entityManager;

    
    private static final Logger logger = LoggerFactory.getLogger(MCBL_Services.class);

    @Transactional
    public String addMCBL(MultipartFile file, String userid, String username, String reportDate) {
        long startTime = System.currentTimeMillis();  // start timer
        System.out.println("Came to service");

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(reportDate);

            Sheet sheet = workbook.getSheet("MCBL");
            List<MCBL_Main_Entity> mainlist = MCBL_Main_Reps.getall();

            if (mainlist.isEmpty()) {
                logger.warn("No data in Main Table, skipping processing.");
                return "No data in Main Table, skipping processing.";
            }

            // --- Step 1: Build lookup map for main table ---
            Map<String, MCBL_Main_Entity> mainMap = new HashMap<>();
            for (MCBL_Main_Entity mainRow : mainlist) {
                String key = normalizeKey(mainRow.getGl_code(), mainRow.getGl_sub_code(),
                                          mainRow.getHead_acc_no(), mainRow.getCurrency());
                mainMap.put(key, mainRow);
            }

            // --- Step 2: Prepare lists for batch delete & batch insert ---
            List<Object[]> keysToDelete = new ArrayList<>();
            List<MCBL_Detail_Entity> detailsToSave = new ArrayList<>();
            int skippedCount = 0;

            // --- Step 3: Read Excel row by row ---
            DataFormatter formatter = new DataFormatter(); // efficient for cell to string
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String glCode = formatter.formatCellValue(row.getCell(0));
                String glSubCode = formatter.formatCellValue(row.getCell(1));
                String headAccNo = formatter.formatCellValue(row.getCell(2));
                String description = formatter.formatCellValue(row.getCell(3));
                String currency = formatter.formatCellValue(row.getCell(5));

                BigDecimal debitBalance = getCellDecimal(row.getCell(6));
                BigDecimal creditBalance = getCellDecimal(row.getCell(7));
                BigDecimal debitEquivalent = getCellDecimal(row.getCell(8));
                BigDecimal creditEquivalent = getCellDecimal(row.getCell(9));

                String lookupKey = normalizeKey(glCode, glSubCode, headAccNo, currency);
                MCBL_Main_Entity matchedMain = mainMap.get(lookupKey);

                if (matchedMain != null) {
                    keysToDelete.add(new Object[]{glCode, glSubCode, headAccNo, currency});

                    MCBL_Detail_Entity detail = new MCBL_Detail_Entity();
                    detail.setId(sequence.generateRequestUUId());
                    detail.setGl_code(glCode);
                    detail.setGl_sub_code(glSubCode);
                    detail.setHead_acc_no(headAccNo);
                    detail.setDescription(description);
                    detail.setCurrency(currency);
                    detail.setDebit_balance(debitBalance);
                    detail.setCredit_balance(creditBalance);
                    detail.setDebit_equivalent(debitEquivalent);
                    detail.setCredit_equivalent(creditEquivalent);
                    detail.setEntry_user(userid);
                    detail.setEntry_date(new Date());
                    detail.setReport_date(date);

                    detailsToSave.add(detail);
                } else {
                    skippedCount++;
                }
            }

            // --- Step 4: Batch delete ---
            if (!keysToDelete.isEmpty()) {
                batchDeleteByKeys(keysToDelete, date);
            }

            // --- Step 5: Batch insert ---
            if (!detailsToSave.isEmpty()) {
                batchInsertDetails(detailsToSave);
            }

            long duration = System.currentTimeMillis() - startTime;
            System.out.println("MCBL processing time: " + duration + " ms");

            return "MCBL Added successfully. Saved: " + detailsToSave.size() + 
                   ", Skipped: " + skippedCount + ". Time taken: " + duration + " ms";

        } catch (Exception e) {
            logger.error("Error while processing MCBL Excel: {}", e.getMessage(), e);
            return "Error Occurred while reading Excel: " + e.getMessage();
        }
    }

    // ------------------- Helper Methods -------------------

    // Normalize key without expensive regex
    private String normalizeKey(String glCode, String glSubCode, String headAccNo, String currency) {
        return safeTrim(glCode) + "|" + safeTrim(glSubCode) + "|" + safeTrim(headAccNo) + "|" + safeTrim(currency);
    }

    private String safeTrim(String value) {
        if (value == null) return "NULL";
        value = value.replace(" ", ""); // simple replace instead of regex
        if (value.endsWith(".0")) value = value.substring(0, value.length() - 2);
        return value.toUpperCase();
    }

    // Convert numeric cells to BigDecimal
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

    // ------------------- Batch Delete -------------------
    @Transactional
    public void batchDeleteByKeys(List<Object[]> keys, Date reportDate) {
        int batchSize = 1000;
        for (int i = 0; i < keys.size(); i += batchSize) {
            int end = Math.min(i + batchSize, keys.size());
            List<Object[]> batch = keys.subList(i, end);

            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM BRRS_MCBL_DETAIL WHERE REPORT_DATE = ? AND (GL_CODE, GL_SUB_CODE, HEAD_ACC_NO, CURRENCY) IN (");

            StringJoiner joiner = new StringJoiner(",");
            for (int j = 0; j < batch.size(); j++) {
                joiner.add("(?, ?, ?, ?)");
            }
            sql.append(joiner.toString()).append(")");

            Query query = entityManager.createNativeQuery(sql.toString());
            query.setParameter(1, reportDate);

            int paramIndex = 2;
            for (Object[] k : batch) {
                for (Object col : k) {
                    query.setParameter(paramIndex++, col);
                }
            }
            query.executeUpdate();
        }
    }

    // ------------------- Batch Insert -------------------
    @Transactional
    public void batchInsertDetails(List<MCBL_Detail_Entity> list) {
        int batchSize = 500; // tune this
        for (int i = 0; i < list.size(); i++) {
            entityManager.persist(list.get(i));
            if (i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }


    private boolean safeEquals(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;

        // remove all spaces including non-breaking
        a = a.replaceAll("\\s+", "");
        b = b.replaceAll("\\s+", "");

        // remove trailing .0 if Excel numeric
        if (a.endsWith(".0")) a = a.substring(0, a.length() - 2);
        if (b.endsWith(".0")) b = b.substring(0, b.length() - 2);

        return a.equalsIgnoreCase(b);
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

