package com.bornfire.brrs.services;


import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
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

    private static final Logger logger = LoggerFactory.getLogger(BDGF_Services.class);

    public String addBDGF(MultipartFile file, String userid, String username) {
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            int savedCount = 0, skippedCount = 0;

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

                    detail.setS_no(getBigDecimal(row.getCell(0))); // S No
                    detail.setAcc_no(getString(row.getCell(1))); // A/C No
                    detail.setCustomer_id(getString(row.getCell(2))); // Customer ID
                    detail.setCustomer_name(getString(row.getCell(3))); // Customer Name
                    detail.setOpen_date(getDate(row.getCell(4))); // Open Date
                    detail.setAmount_deposited(getBigDecimal(row.getCell(5))); // Amount Deposited
                    detail.setCurrency(getString(row.getCell(6))); // Currency
                    detail.setPeriod(getString(row.getCell(7))); // Period
                    detail.setRate_of_interest(getBigDecimal(row.getCell(8))); // Rate of Interest
                    detail.setHundred(getBigDecimal(row.getCell(9))); // 100
                    detail.setBal_equi_to_bwp(getBigDecimal(row.getCell(10))); // BAL EQUI TO BWP
                    detail.setOutstanding_balance(getBigDecimal(row.getCell(11))); // Outstanding Balance
                    detail.setOustndng_bal_ugx(getBigDecimal(row.getCell(12))); // Oustndng Bal UGX
                    detail.setMaturity_date(getDate(row.getCell(13))); // Maturity Date
                    detail.setMaturity_amount(getBigDecimal(row.getCell(14))); // Maturity Amount
                    detail.setScheme(getString(row.getCell(15))); // Scheme
                    detail.setCr_pref_int_rate(getBigDecimal(row.getCell(16))); // Cr Pref Int Rate
                    detail.setSegment(getString(row.getCell(17))); // Segment
                    detail.setReference_date(getDate(row.getCell(18))); // REFERENCE DATE
                    detail.setResidual_tenure(getBigDecimal(row.getCell(19))); // Residual Tenure / Difference
                    detail.setSls_bucket(getString(row.getCell(20))); // SLS BUCKET
                    detail.setDays(getBigDecimal(row.getCell(21))); // DAYS
                    detail.setPeriod_days(getBigDecimal(row.getCell(22))); // PERIOD
                    detail.setEffective_int_rate(getBigDecimal(row.getCell(23))); // EFFECTIVE INTEREST RATE

                    // ✅ Read report date from Excel
                    Date reportDateFromExcel = getDate(row.getCell(23));

                    // 🔹 Audit fields
                    detail.setReport_date(reportDateFromExcel);
                    detail.setEntry_date(new Date());
                    detail.setEntry_user(userid);
                    detail.setDel_flg("N");
                    detail.setEntry_flg("Y");

                 // ✅ Create Master Entity
                    BrrsGeneralMasterEntity masterEntity = new BrrsGeneralMasterEntity();

                    // ✅ Set unique ID (since you're using String ID with custom sequence)
                    masterEntity.setId(sequence.generateRequestUUId());

                    // ✅ Map Excel columns to fields safely
                    masterEntity.setFile_type("BDGF");               //TYPE
                    masterEntity.setS_no(getBigDecimal(row.getCell(0)));               // S No
                    masterEntity.setAcc_no(getString(row.getCell(1)));                 // A/C No
                    masterEntity.setCustomer_id(getString(row.getCell(2)));            // Customer ID
                    masterEntity.setCustomer_name(getString(row.getCell(3)));          // Customer Name
                    masterEntity.setOpen_date(getDate(row.getCell(4)));                // Open Date
                    masterEntity.setAmount_deposited(getBigDecimal(row.getCell(5)));   // Amount Deposited
                    masterEntity.setCurrency(getString(row.getCell(6)));               // Currency
                    masterEntity.setPeriod(getString(row.getCell(7)));                 // Period
                    masterEntity.setRate_of_interest(getBigDecimal(row.getCell(8)));   // Rate of Interest
                    masterEntity.setHundred(getBigDecimal(row.getCell(9)));            // 100
                    masterEntity.setBal_equi_to_bwp(getBigDecimal(row.getCell(10)));   // BAL EQUI TO BWP
                    masterEntity.setOutstanding_balance(getBigDecimal(row.getCell(11))); // Outstanding Balance
                    masterEntity.setOustndng_bal_ugx(getBigDecimal(row.getCell(12)));  // Outstanding Bal UGX
                    masterEntity.setMaturity_date(getDate(row.getCell(13)));           // Maturity Date
                    masterEntity.setMaturity_amount(getBigDecimal(row.getCell(14)));   // Maturity Amount
                    masterEntity.setScheme(getString(row.getCell(15)));                // Scheme
                    masterEntity.setCr_pref_int_rate(getBigDecimal(row.getCell(16)));  // Cr Pref Int Rate
                    masterEntity.setSegment(getString(row.getCell(17)));               // Segment
                    masterEntity.setReference_date(getDate(row.getCell(18)));          // Reference Date
                    masterEntity.setResidual_tenure(getBigDecimal(row.getCell(19)));   // Residual Tenure
                    masterEntity.setSls_bucket(getString(row.getCell(20)));            // SLS Bucket
                    masterEntity.setDays(getBigDecimal(row.getCell(21)));              // Days
                    masterEntity.setPeriod_days(getBigDecimal(row.getCell(22)));       // Period Days
                    masterEntity.setEffective_int_rate(getBigDecimal(row.getCell(23)));// Effective Interest Rate

                    // ✅ Audit Fields
                   // masterEntity.setReport_date(reportDateFromExcel);
                    masterEntity.setEntry_date(new Date());
                    masterEntity.setEntry_user(userid);
                    masterEntity.setDel_flg("N");
                    masterEntity.setEntry_flg("Y");


                    BDGF_Reps.save(detail);
                    
                    BrrsGeneralMasterRepos.save(masterEntity);
                    
                    savedCount++;

                } catch (Exception rowEx) {
                    logger.error("Skipping row {} due to error: {}", i, rowEx.getMessage());
                    skippedCount++;
                }
            }


       //     return "BDGF Added successfully. Saved: " + savedCount + ", Skipped: " + skippedCount;
            return "BDGF Added successfully.";

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
