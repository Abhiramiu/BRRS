package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import com.bornfire.brrs.entities.BLBF_Entity;
import com.bornfire.brrs.entities.BLBF_Rep;
import com.bornfire.brrs.entities.BrrsGeneralMasterEntity;
import com.bornfire.brrs.entities.BrrsGeneralMasterRepo;
import com.bornfire.brrs.entities.GeneralMasterEntity;
import com.bornfire.brrs.entities.GeneralMasterRepo;

@Service
@Transactional
public class BLBF_Services {

	@Autowired
	SequenceGenerator sequence;

	@Autowired
	GeneralMasterRepo GeneralMasterRepos;
	@Autowired
	private BLBF_Rep BLBF_Reps;

	@Autowired
	private DataSource dataSource; // Inject DataSource for JDBC

	private static final Logger logger = LoggerFactory.getLogger(BLBF_Services.class);


	private final ConcurrentHashMap<String, String> jobStatusStorage = new ConcurrentHashMap<>();

	@Async
	public void initializeJobStatus(String jobId, MultipartFile file, String userid, String username) {
	    jobStatusStorage.put(jobId, "PROCESSING");
	    startBLBFUploadAsync(jobId, file, userid, username);
	}

	public String startBLBFUploadAsync(String jobId, MultipartFile file, String userid, String username) {
	    logger.info("Starting BLBF upload job: {}", jobId);

	    try {
	        String resultMsg = addBLBF(file, userid, username);
	        jobStatusStorage.put(jobId, "COMPLETED:" + resultMsg);
	        logger.info("Job {} completed successfully.", jobId);
	        return resultMsg;

	    } catch (Exception e) {
	        String errorMessage = "ERROR:" + e.getMessage();
	        jobStatusStorage.put(jobId, errorMessage);
	        logger.error("Job {} failed: {}", jobId, e.getMessage(), e);
	        return errorMessage;
	    }
	}

	public String getJobStatus(String jobId) {
	    return jobStatusStorage.getOrDefault(jobId, "NOT_FOUND");
	}
	
	@Transactional
	public String addBLBF(MultipartFile file, String userid, String username) {
	    long startTime = System.currentTimeMillis();
	    int savedCount = 0, skippedCount = 0;
	    int batchSize = 500;

	    try (InputStream is = file.getInputStream();
	         Workbook workbook = WorkbookFactory.create(is);
	         Connection conn = dataSource.getConnection()) {

	        conn.setAutoCommit(false);

	        Sheet sheet = workbook.getSheetAt(0);
	        DataFormatter formatter = new DataFormatter();
	        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

	        // 🟩 Prepare DELETE statements for duplicates
	        String deleteBLBF = "DELETE FROM BRRS_BLBF WHERE ACCOUNT_NO = ? AND REPORT_DATE = ?";
	        String deleteMaster = "DELETE FROM GENERAL_MASTER_TABLE WHERE ACCOUNT_NO = ? AND REPORT_DATE = ?";
	        PreparedStatement stmtDeleteBLBF = conn.prepareStatement(deleteBLBF);
	        PreparedStatement stmtDeleteMaster = conn.prepareStatement(deleteMaster);

	        // 🟩 Insert queries
	        String insertBLBF = "INSERT INTO BRRS_BLBF (SOL_ID, CUSTOMER_ID, ACCOUNT_NO, CUSTOMER_NAME, SCHM_CODE, SCHM_DESC, "
	                + "ACCT_OPEN_DATE, APPROVED_LIMIT, SANCTION_LIMIT, DISBURSED_AMT, BALANCE_AS_ON, CURRENCY, BAL_EQUI_TO_BWP, "
	                + "RATE_OF_INTEREST, HUNDRED, ACCRUED_INT_AMT, MONTHLY_INTEREST, LAST_INTEREST_DEBIT_DATE, ACCT_CLS_FLG, "
	                + "ACCT_CLOSE_DATE, GENDER, CLASSIFICATION_CODE, CONSTITUTION_CODE, MATURITY_DATE, GL_SUB_HEAD_CODE, "
	                + "GL_SUB_HEAD_DESC, TENOR_MONTH, EMI, SEGMENT, FACILITY, PAST_DUE, PAST_DUE_DAYS, ASSET, PROVISION, UNSECURED, "
	                + "INT_BUCKET, STAFF, SMME, LABOD, NEW_AC, UNDRAWN, SECTOR, PERIOD, EFFECTIVE_INTEREST_RATE, STAGE, ECL_PROVISION, "
	                + "REPORT_DATE, MAT_BUCKET, ENTRY_DATE, ENTRY_USER, ENTRY_FLG, DEL_FLG) "
	                + "VALUES (" + String.join(",", Collections.nCopies(52, "?")) + ")";

	        String insertMaster = "INSERT INTO GENERAL_MASTER_TABLE (ID, SOL_ID, CUSTOMER_ID, ACCOUNT_NO, CUSTOMER_NAME, SCHM_CODE, SCHM_DESC, "
	                + "ACCT_OPEN_DATE, APPROVED_LIMIT, SANCTION_LIMIT, DISBURSED_AMT, BALANCE_AS_ON, CURRENCY, BAL_EQUI_TO_BWP, "
	                + "HUNDRED, ACCRUED_INT_AMT, MONTHLY_INTEREST, LAST_INTEREST_DEBIT_DATE, ACCT_CLS_FLG, ACCT_CLOSE_DATE, "
	                + "GENDER, CLASSIFICATION_CODE, CONSTITUTION_CODE, MATURITY_DATE, GL_SUB_HEAD_CODE, GL_SUB_HEAD_DESC, TENOR_MONTH, "
	                + "EMI, SEGMENT, FACILITY, PAST_DUE, PAST_DUE_DAYS, ASSET, PROVISION, UNSECURED, INT_BUCKET, STAFF, SMME, LABOD, "
	                + "NEW_AC, UNDRAWN, SECTOR, PERIOD, EFFECTIVE_INTEREST_RATE, STAGE, ECL_PROVISION, REPORT_DATE, MAT_BUCKET, "
	                + "ENTRY_DATE, ENTRY_USER, ENTRY_FLG, DEL_FLG, BLBF_FLG) "
	                + "VALUES (" + String.join(",", Collections.nCopies(53, "?")) + ")";

	        PreparedStatement stmtBLBF = conn.prepareStatement(insertBLBF);
	        PreparedStatement stmtMaster = conn.prepareStatement(insertMaster);

	        int count = 0;

	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            // Skip empty rows
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
	                // --- Get key columns ---
	                String accountNo = getCellStringSafe(row, 2, formatter, evaluator);
	                java.sql.Date reportDate = getCellDateSafe(row, 46, formatter, evaluator);

	                // 🟩 Delete existing rows before insert
	                stmtDeleteBLBF.setString(1, accountNo);
	                stmtDeleteBLBF.setDate(2, reportDate);
	                stmtDeleteBLBF.executeUpdate();

	                stmtDeleteMaster.setString(1, accountNo);
	                stmtDeleteMaster.setDate(2, reportDate);
	                stmtDeleteMaster.executeUpdate();

	                // --- BRRS_BLBF insert ---
	                int col = 0;
	                stmtBLBF.setString(++col, getCellStringSafe(row, 0, formatter, evaluator)); // SOL_ID
	                stmtBLBF.setString(++col, getCellStringSafe(row, 1, formatter, evaluator)); // CUSTOMER_ID
	                stmtBLBF.setString(++col, accountNo); // ACCOUNT_NO
	                stmtBLBF.setString(++col, getCellStringSafe(row, 3, formatter, evaluator)); // CUSTOMER_NAME
	                stmtBLBF.setString(++col, getCellStringSafe(row, 4, formatter, evaluator)); // SCHM_CODE
	                stmtBLBF.setString(++col, getCellStringSafe(row, 5, formatter, evaluator)); // SCHM_DESC
	                stmtBLBF.setDate(++col, getCellDateSafe(row, 6, formatter, evaluator)); // ACCT_OPEN_DATE
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 7, formatter, evaluator)); // APPROVED_LIMIT
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 8, formatter, evaluator)); // SANCTION_LIMIT
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator)); // DISBURSED_AMT
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 10, formatter, evaluator)); // BALANCE_AS_ON
	                stmtBLBF.setString(++col, getCellStringSafe(row, 11, formatter, evaluator)); // CURRENCY
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator)); // BAL_EQUI_TO_BWP
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 13, formatter, evaluator)); // RATE_OF_INTEREST
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 14, formatter, evaluator)); // HUNDRED
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 15, formatter, evaluator)); // ACCRUED_INT_AMT
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 16, formatter, evaluator)); // MONTHLY_INTEREST
	                stmtBLBF.setDate(++col, getCellDateSafe(row, 17, formatter, evaluator)); // LAST_INTEREST_DEBIT_DATE
	                stmtBLBF.setString(++col, getCellStringSafe(row, 18, formatter, evaluator)); // ACCT_CLS_FLG
	                stmtBLBF.setDate(++col, getCellDateSafe(row, 19, formatter, evaluator)); // ACCT_CLOSE_DATE
	                stmtBLBF.setString(++col, getCellStringSafe(row, 20, formatter, evaluator)); // GENDER
	                stmtBLBF.setString(++col, getCellStringSafe(row, 21, formatter, evaluator)); // CLASSIFICATION_CODE
	                stmtBLBF.setString(++col, getCellStringSafe(row, 22, formatter, evaluator)); // CONSTITUTION_CODE
	                stmtBLBF.setDate(++col, getCellDateSafe(row, 23, formatter, evaluator)); // MATURITY_DATE
	                stmtBLBF.setString(++col, getCellStringSafe(row, 24, formatter, evaluator)); // GL_SUB_HEAD_CODE
	                stmtBLBF.setString(++col, getCellStringSafe(row, 25, formatter, evaluator)); // GL_SUB_HEAD_DESC
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 26, formatter, evaluator)); // TENOR_MONTH
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 27, formatter, evaluator)); // EMI
	                stmtBLBF.setString(++col, getCellStringSafe(row, 28, formatter, evaluator)); // SEGMENT
	                stmtBLBF.setString(++col, getCellStringSafe(row, 29, formatter, evaluator)); // FACILITY
	                stmtBLBF.setString(++col, getCellStringSafe(row, 30, formatter, evaluator)); // PAST_DUE
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 31, formatter, evaluator)); // PAST_DUE_DAYS
	                stmtBLBF.setString(++col, getCellStringSafe(row, 32, formatter, evaluator)); // ASSET
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 33, formatter, evaluator)); // PROVISION
	                stmtBLBF.setString(++col, getCellStringSafe(row, 34, formatter, evaluator)); // UNSECURED
	                stmtBLBF.setString(++col, getCellStringSafe(row, 35, formatter, evaluator)); // INT_BUCKET
	                stmtBLBF.setString(++col, getCellStringSafe(row, 36, formatter, evaluator)); // STAFF
	                stmtBLBF.setString(++col, getCellStringSafe(row, 37, formatter, evaluator)); // SMME
	                stmtBLBF.setString(++col, getCellStringSafe(row, 38, formatter, evaluator)); // LABOD
	                stmtBLBF.setString(++col, getCellStringSafe(row, 39, formatter, evaluator)); // NEW_AC
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 40, formatter, evaluator)); // UNDRAWN
	                stmtBLBF.setString(++col, getCellStringSafe(row, 41, formatter, evaluator)); // SECTOR
	                stmtBLBF.setString(++col, getCellStringSafe(row, 42, formatter, evaluator)); // PERIOD
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 43, formatter, evaluator)); // EFFECTIVE_INTEREST_RATE
	                stmtBLBF.setString(++col, getCellStringSafe(row, 44, formatter, evaluator)); // STAGE
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 45, formatter, evaluator)); // ECL_PROVISION
	                stmtBLBF.setDate(++col, reportDate); // REPORT_DATE
	                stmtBLBF.setBigDecimal(++col, getCellDecimalSafe(row, 47, formatter, evaluator)); // MAT_BUCKET
	                stmtBLBF.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // ENTRY_DATE
	                stmtBLBF.setString(++col, userid);
	                stmtBLBF.setString(++col, "Y"); // ENTRY_FLG
	                stmtBLBF.setString(++col, "N"); // DEL_FLG
	                stmtBLBF.addBatch();

	                // --- GENERAL_MASTER_TABLE insert ---
	                col = 0;
	                stmtMaster.setString(++col, sequence.generateRequestUUId());
	                stmtMaster.setString(++col, getCellStringSafe(row, 0, formatter, evaluator)); // SOL_ID
	                stmtMaster.setString(++col, getCellStringSafe(row, 1, formatter, evaluator)); // CUSTOMER_ID
	                stmtMaster.setString(++col, accountNo);
	                stmtMaster.setString(++col, getCellStringSafe(row, 3, formatter, evaluator)); // CUSTOMER_NAME
	                stmtMaster.setString(++col, getCellStringSafe(row, 4, formatter, evaluator)); // SCHM_CODE
	                stmtMaster.setString(++col, getCellStringSafe(row, 5, formatter, evaluator)); // SCHM_DESC
	                stmtMaster.setDate(++col, getCellDateSafe(row, 6, formatter, evaluator)); // ACCT_OPEN_DATE
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 7, formatter, evaluator)); // APPROVED_LIMIT
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 8, formatter, evaluator)); // SANCTION_LIMIT
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator)); // DISBURSED_AMT
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 10, formatter, evaluator)); // BALANCE_AS_ON
	                stmtMaster.setString(++col, getCellStringSafe(row, 11, formatter, evaluator)); // CURRENCY
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator)); // BAL_EQUI_TO_BWP
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 14, formatter, evaluator)); // HUNDRED
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 15, formatter, evaluator)); // ACCRUED_INT_AMT
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 16, formatter, evaluator)); // MONTHLY_INTEREST
	                stmtMaster.setDate(++col, getCellDateSafe(row, 17, formatter, evaluator)); // LAST_INTEREST_DEBIT_DATE
	                stmtMaster.setString(++col, getCellStringSafe(row, 18, formatter, evaluator)); // ACCT_CLS_FLG
	                stmtMaster.setDate(++col, getCellDateSafe(row, 19, formatter, evaluator)); // ACCT_CLOSE_DATE
	                stmtMaster.setString(++col, getCellStringSafe(row, 20, formatter, evaluator)); // GENDER
	                stmtMaster.setString(++col, getCellStringSafe(row, 21, formatter, evaluator)); // CLASSIFICATION_CODE
	                stmtMaster.setString(++col, getCellStringSafe(row, 22, formatter, evaluator)); // CONSTITUTION_CODE
	                stmtMaster.setDate(++col, getCellDateSafe(row, 23, formatter, evaluator)); // MATURITY_DATE
	                stmtMaster.setString(++col, getCellStringSafe(row, 24, formatter, evaluator)); // GL_SUB_HEAD_CODE
	                stmtMaster.setString(++col, getCellStringSafe(row, 25, formatter, evaluator)); // GL_SUB_HEAD_DESC
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 26, formatter, evaluator)); // TENOR_MONTH
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 27, formatter, evaluator)); // EMI
	                stmtMaster.setString(++col, getCellStringSafe(row, 28, formatter, evaluator)); // SEGMENT
	                stmtMaster.setString(++col, getCellStringSafe(row, 29, formatter, evaluator)); // FACILITY
	                stmtMaster.setString(++col, getCellStringSafe(row, 30, formatter, evaluator)); // PAST_DUE
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 31, formatter, evaluator)); // PAST_DUE_DAYS
	                stmtMaster.setString(++col, getCellStringSafe(row, 32, formatter, evaluator)); // ASSET
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 33, formatter, evaluator)); // PROVISION
	                stmtMaster.setString(++col, getCellStringSafe(row, 34, formatter, evaluator)); // UNSECURED
	                stmtMaster.setString(++col, getCellStringSafe(row, 35, formatter, evaluator)); // INT_BUCKET
	                stmtMaster.setString(++col, getCellStringSafe(row, 36, formatter, evaluator)); // STAFF
	                stmtMaster.setString(++col, getCellStringSafe(row, 37, formatter, evaluator)); // SMME
	                stmtMaster.setString(++col, getCellStringSafe(row, 38, formatter, evaluator)); // LABOD
	                stmtMaster.setString(++col, getCellStringSafe(row, 39, formatter, evaluator)); // NEW_AC
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 40, formatter, evaluator)); // UNDRAWN
	                stmtMaster.setString(++col, getCellStringSafe(row, 41, formatter, evaluator)); // SECTOR
	                stmtMaster.setString(++col, getCellStringSafe(row, 42, formatter, evaluator)); // PERIOD
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 43, formatter, evaluator)); // EFFECTIVE_INTEREST_RATE
	                stmtMaster.setString(++col, getCellStringSafe(row, 44, formatter, evaluator)); // STAGE
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 45, formatter, evaluator)); // ECL_PROVISION
	                stmtMaster.setDate(++col, reportDate);
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 47, formatter, evaluator)); // MAT_BUCKET
	                stmtMaster.setDate(++col, new java.sql.Date(System.currentTimeMillis()));
	                stmtMaster.setString(++col, userid);
	                stmtMaster.setString(++col, "Y");
	                stmtMaster.setString(++col, "N");
	                stmtMaster.setString(++col, "Y"); // BLBF_FLG
	                stmtMaster.addBatch();

	                savedCount++;
	                count++;

	                if (count % batchSize == 0) {
	                    stmtBLBF.executeBatch();
	                    stmtMaster.executeBatch();
	                    conn.commit();
	                    evaluator.clearAllCachedResultValues();
	                }

	            } catch (Exception ex) {
	                skippedCount++;
	                logger.error("Skipping row {} due to error: {}", i, ex.getMessage(), ex);
	            }
	        }

	        stmtBLBF.executeBatch();
	        stmtMaster.executeBatch();
	        conn.commit();

	        long duration = System.currentTimeMillis() - startTime;
	        return "✅ BLBF Added successfully. Saved: " + savedCount + ", Skipped: " + skippedCount + ". Time taken: " + duration + " ms";

	    } catch (Exception e) {
	        logger.error("❌ Error while processing BLBF Excel: {}", e.getMessage(), e);
	        return "Error Occurred while reading Excel: " + e.getMessage();
	    }
	}



	// ===== Helper methods =====
	private String getCellStringSafe(Row row, int index, DataFormatter formatter, FormulaEvaluator evaluator) {
		Cell cell = row.getCell(index);
		if (cell == null)
			return null;
		return formatter.formatCellValue(cell, evaluator).trim();
	}

	private java.sql.Date getCellDateSafe(Row row, int colIndex, DataFormatter formatter, FormulaEvaluator evaluator) {
		try {
			Cell cell = row.getCell(colIndex);
			if (cell == null)
				return null;

			if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
				return new java.sql.Date(cell.getDateCellValue().getTime());
			} else {
				// Parse text in dd-MM-yyyy format
				String text = formatter.formatCellValue(cell, evaluator).trim();
				if (text.isEmpty())
					return null;
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // match Excel format
				return new java.sql.Date(sdf.parse(text).getTime());
			}
		} catch (Exception e) {
			return null;
		}
	}

	private BigDecimal getCellDecimalSafe(Row row, int index, DataFormatter formatter, FormulaEvaluator evaluator) {
		Cell cell = row.getCell(index);
		if (cell == null)
			return null;
		try {
			return new BigDecimal(formatter.formatCellValue(cell, evaluator).replaceAll(",", "").trim());
		} catch (Exception e) {
			return null;
		}
	}

	private String getCellString(Cell cell, DataFormatter f, FormulaEvaluator e) {
		if (cell == null)
			return null;
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
			if (cell == null)
				return null;
			if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
				return cell.getDateCellValue();
			String text = f.formatCellValue(cell, e);
			if (text.isEmpty())
				return null;
			return new SimpleDateFormat("dd-MM-yyyy").parse(text);
		} catch (Exception ex) {
			return null;
		}
	}

	private final ConcurrentHashMap<String, byte[]> jobStorage = new ConcurrentHashMap<>();

	// ================= ASYNC REPORT GENERATION =================
	@Async
	public void generateLoanBookReportAsync(String jobId, String filename, String todate) {
		System.out.println("Starting Loan Book report generation for: " + filename);
		byte[] fileData = generateLoanBookExcel(filename, todate);
		jobStorage.put(jobId, fileData != null ? fileData : null);
		System.out.println("Loan Book report generation completed for: " + filename);
	}

	public byte[] getReport(String jobId) {
		return jobStorage.get(jobId);
	}

	// ================= EXCEL GENERATION =================
	public byte[] generateLoanBookExcel(String filename, String todate) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Loan_Book_Report");

			// ======== Header Style ========
			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setBorderTop(BorderStyle.THIN);
			headerStyle.setBorderBottom(BorderStyle.THIN);
			headerStyle.setBorderLeft(BorderStyle.THIN);
			headerStyle.setBorderRight(BorderStyle.THIN);

			// ======== Numeric / Amount Style ========
			CellStyle numericStyle = workbook.createCellStyle();
			numericStyle.setAlignment(HorizontalAlignment.RIGHT);
			numericStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
			numericStyle.setBorderTop(BorderStyle.THIN);
			numericStyle.setBorderBottom(BorderStyle.THIN);
			numericStyle.setBorderLeft(BorderStyle.THIN);
			numericStyle.setBorderRight(BorderStyle.THIN);

			// ======== General / Text Style ========
			CellStyle dataCellStyle = workbook.createCellStyle();
			dataCellStyle.setBorderTop(BorderStyle.THIN);
			dataCellStyle.setBorderBottom(BorderStyle.THIN);
			dataCellStyle.setBorderLeft(BorderStyle.THIN);
			dataCellStyle.setBorderRight(BorderStyle.THIN);
			dataCellStyle.setAlignment(HorizontalAlignment.CENTER);

			// ======== Header Row ========
			String[] headers = { "Cust ID", "SOL ID", "Account No", "Account Name", "Scheme Code", "Scheme Desc",
					"Account Open Date", "Approved Limit", "Sanction Limit", "Disbursed Amt", "Balance As On",
					"Currency", "Bal Equiv to BWP", "Interest Rate", "Accrued Int Amt", "Int of Aug 25",
					"Last Interest Debit Date", "Account Close Flag", "Close Date", "Gender", "Classification Code",
					"Constitution Code", "Maturity Date", "GL Sub Head Code", "GL Sub Head Desc", "Tenor Month", "EMI",
					"Segment", "Facility", "Past Due", "Past Due Days", "Asset", "Provision", "Unsecured", "Int Bucket",
					"Staff", "SMME", "LABOD", "New AC", "Undrawn", "Sector", "Period", "Effective Int Rate", "Stage",
					"ECL Provision", "Branch Name", "Branch Code", "Report Date" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(headerStyle);
				sheet.setColumnWidth(i, 4500);
			}

			// ======== Fetch Data from DB ========
			List<BLBF_Entity> dataList = BLBF_Reps.findRecordsByReportDate(todate);

			if (dataList != null && !dataList.isEmpty()) {
				int rowIndex = 1;
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

				for (BLBF_Entity rec : dataList) {
					XSSFRow row = sheet.createRow(rowIndex++);
					int col = 0;

					// ======== Text / Date Cells ========
					createTextCell(row, col++, rec.getCustomer_id(), dataCellStyle);
					createTextCell(row, col++, rec.getSol_id(), dataCellStyle);
					createTextCell(row, col++, rec.getAccount_no(), dataCellStyle);
					createTextCell(row, col++, rec.getCustomer_name(), dataCellStyle);
					createTextCell(row, col++, rec.getSchm_code(), dataCellStyle);
					createTextCell(row, col++, rec.getSchm_desc(), dataCellStyle);
					createTextCell(row, col++, rec.getAcct_open_date() != null ? sdf.format(rec.getAcct_open_date()) : "",
							dataCellStyle);

					// ======== Numeric Cells ========
					createNumericCell(row, col++, rec.getApproved_limit(), numericStyle);
					createNumericCell(row, col++, rec.getSanction_limit(), numericStyle);
					createNumericCell(row, col++, rec.getDisbursed_amt(), numericStyle);
					createNumericCell(row, col++, rec.getBalance_as_on(), numericStyle);

					createTextCell(row, col++, rec.getCurrency(), dataCellStyle);
					createNumericCell(row, col++, rec.getBal_equi_to_bwp(), numericStyle);
					createNumericCell(row, col++, rec.getRate_of_interest(), numericStyle);
					createNumericCell(row, col++, rec.getAccrued_int_amt(), numericStyle);
					createNumericCell(row, col++, rec.getMonthly_interest(), numericStyle);
					createTextCell(row, col++,
							rec.getLast_interest_debit_date() != null ? sdf.format(rec.getLast_interest_debit_date())
									: "",
							dataCellStyle);
					createTextCell(row, col++, rec.getAcct_cls_flg(), dataCellStyle);
					createTextCell(row, col++, rec.getAcct_close_date() != null ? sdf.format(rec.getAcct_close_date()) : "",
							dataCellStyle);
					createTextCell(row, col++, rec.getGender(), dataCellStyle);
					createTextCell(row, col++, rec.getClassification_code(), dataCellStyle);
					createTextCell(row, col++, rec.getConstitution_code(), dataCellStyle);
					createTextCell(row, col++, rec.getMaturity_date() != null ? sdf.format(rec.getMaturity_date()) : "",
							dataCellStyle);
					createTextCell(row, col++, rec.getGl_sub_head_code(), dataCellStyle);
					createTextCell(row, col++, rec.getGl_sub_head_desc(), dataCellStyle);

					// ======== Mixed Numeric & Text ========
					createNumericCell(row, col++, rec.getTenor_month(), numericStyle);
					createNumericCell(row, col++, rec.getEmi(), numericStyle);
					createTextCell(row, col++, rec.getSegment(), dataCellStyle);
					createTextCell(row, col++, rec.getFacility(), dataCellStyle);
					createTextCell(row, col++, rec.getPast_due(), dataCellStyle);
					createNumericCell(row, col++, rec.getPast_due_days(), numericStyle);
					createTextCell(row, col++, rec.getAsset(), dataCellStyle);
					createNumericCell(row, col++, rec.getProvision(), numericStyle);
					createTextCell(row, col++, rec.getUnsecured(), dataCellStyle);
					createTextCell(row, col++, rec.getInt_bucket(), dataCellStyle);
					createTextCell(row, col++, rec.getStaff(), dataCellStyle);
					createTextCell(row, col++, rec.getSmme(), dataCellStyle);
					createTextCell(row, col++, rec.getLabod(), dataCellStyle);
					createTextCell(row, col++, rec.getNew_ac(), dataCellStyle);
					createNumericCell(row, col++, rec.getUndrawn(), numericStyle);
					createTextCell(row, col++, rec.getSector(), dataCellStyle);
					createTextCell(row, col++, rec.getPeriod(), dataCellStyle);
					createNumericCell(row, col++, rec.getEffective_interest_rate(), numericStyle);
					createTextCell(row, col++, rec.getStage(), dataCellStyle);
					createNumericCell(row, col++, rec.getEcl_provision(), numericStyle);
					createTextCell(row, col++, rec.getBranch_name(), dataCellStyle);
					createTextCell(row, col++, rec.getBranch_code(), dataCellStyle);
					createTextCell(row, col++, rec.getReport_date() != null ? sdf.format(rec.getReport_date()) : "",
							dataCellStyle);
				}
			}

			// ======== Write to ByteArrayOutputStream ========
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			return bos.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	// ======== Helper Methods ========
	private void createNumericCell(XSSFRow row, int index, java.math.BigDecimal value, CellStyle style) {
		Cell cell = row.createCell(index);
		cell.setCellValue(value != null ? value.doubleValue() : 0);
		cell.setCellStyle(style);
	}

	private void createTextCell(XSSFRow row, int index, String value, CellStyle style) {
		Cell cell = row.createCell(index);
		cell.setCellValue(value != null ? value : "");
		cell.setCellStyle(style);
	}

}