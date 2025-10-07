package com.bornfire.brrs.services;


import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.brrs.entities.BDGF_Entity;
import com.bornfire.brrs.entities.BDGF_Rep;

@Service
@Transactional
public class BDGF_Services {

    @Autowired
    private BDGF_Rep BDGF_Reps;

    private static final Logger logger = LoggerFactory.getLogger(BDGF_Services.class);

    public String addBDGF(MultipartFile file, String userid, String username) {
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            int savedCount = 0, skippedCount = 0;

            // Skip header (row 0)
         // Skip header (row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // 🔸 Check if the row is completely empty
                if (isRowEmpty(row)) {
                    break; // stop processing further
                }

                try {
                    BDGF_Entity detail = new BDGF_Entity();

                    detail.setSol_id(getString(row.getCell(0)));
                    detail.setAcc_no(getString(row.getCell(2)));
                    detail.setCustomer_id(getString(row.getCell(3)));
                    detail.setCustomer_name(getString(row.getCell(4)));
                    detail.setOpen_date(getDate(row.getCell(5)));
                    detail.setAmount_deposited(getBigDecimal(row.getCell(6)));
                    detail.setCurrency(getString(row.getCell(7)));
                    detail.setPeriod(getString(row.getCell(8)));
                    detail.setRate_of_interest(getBigDecimal(row.getCell(9)));
                    detail.setHundred(getBigDecimal(row.getCell(10)));
                    detail.setBal_equi_to_bwp(getBigDecimal(row.getCell(11)));
                    detail.setOutstanding_balance(getBigDecimal(row.getCell(12)));
                    detail.setOustndng_bal_ugx(getBigDecimal(row.getCell(13)));
                    detail.setMaturity_date(getDate(row.getCell(14)));
                    detail.setMaturity_amount(getBigDecimal(row.getCell(15)));
                    detail.setScheme(getString(row.getCell(16)));
                    detail.setCr_pref_int_rate(getBigDecimal(row.getCell(17)));
                    detail.setSegment(getString(row.getCell(18)));
                    detail.setReference_date(getDate(row.getCell(19)));
                    detail.setDifference(getBigDecimal(row.getCell(20)));
                    detail.setDays(getBigDecimal(row.getCell(21)));
                    detail.setPeriod_days(getBigDecimal(row.getCell(22)));
                    detail.setEffective_int_rate(getBigDecimal(row.getCell(23)));

                    // ✅ Read report date from Excel
                    Date reportDateFromExcel = getDate(row.getCell(24));

                    // 🔹 Audit fields
                    detail.setReport_date(reportDateFromExcel);
                    detail.setEntry_date(new Date());
                    detail.setEntry_user(userid);
                    detail.setDel_flg("N");
                    detail.setEntry_flg("Y");

                    BDGF_Reps.save(detail);
                    savedCount++;

                } catch (Exception rowEx) {
                    logger.error("Skipping row {} due to error: {}", i, rowEx.getMessage());
                    skippedCount++;
                }
            }


            return "BDGF Added successfully. Saved: " + savedCount + ", Skipped: " + skippedCount;

        } catch (Exception e) {
            logger.error("Error while processing BDGF Excel: {}", e.getMessage(), e);
            return "Error Occurred while reading Excel: " + e.getMessage();
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
