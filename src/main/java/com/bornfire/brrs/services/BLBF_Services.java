package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	private final ExecutorService executorService = Executors.newFixedThreadPool(5);

	// This method returns IMMEDIATELY
	public void initializeJobStatus(String jobId, MultipartFile file, String userid, String username) {
		jobStatusStorage.put(jobId, "PROCESSING");

		// Submit task to background thread - RETURNS IMMEDIATELY
		executorService.submit(() -> {
			startBLBFUploadAsync(jobId, file, userid, username);
		});
	}

	// This runs in background thread
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

		logger.info("Came to main method for Upload for Loan book(BLBF)");

		try (InputStream is = file.getInputStream();
				Workbook workbook = WorkbookFactory.create(is);
				Connection conn = dataSource.getConnection()) {

			conn.setAutoCommit(false);
			Sheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

			// Query to get max version for L_BOOK
			String getMaxVersionLBook = "SELECT COALESCE(MAX(VERSION), 0) FROM L_BOOK WHERE ACCOUNT_NO = ? AND REPORT_DATE = ? AND DEL_FLG = 'N'";
			PreparedStatement stmtGetMaxVersionLBook = conn.prepareStatement(getMaxVersionLBook);

			// Query to get max version for GENERAL_MASTER_TABLE
			String getMaxVersionMaster = "SELECT COALESCE(MAX(VERSION), 0) FROM GENERAL_MASTER_TABLE WHERE ACCOUNT_NO = ? AND REPORT_DATE = ? AND DEL_FLG = 'N' AND REPORT_CODE='LOANB'";
			PreparedStatement stmtGetMaxVersionMaster = conn.prepareStatement(getMaxVersionMaster);

			// Query to get max version for GENERAL_MASTER_SRC
			String getMaxVersionSrc = "SELECT COALESCE(MAX(VERSION), 0) FROM GENERAL_MASTER_SRC WHERE ACCOUNT_NO = ? AND REPORT_DATE = ? AND DEL_FLG = 'N' AND REPORT_CODE='LOANB'";
			PreparedStatement stmtGetMaxVersionSrc = conn.prepareStatement(getMaxVersionSrc);

			// Soft delete previous versions in L_BOOK
			String softDeleteLBook = "UPDATE L_BOOK SET DEL_FLG = 'Y' WHERE ACCOUNT_NO = ? AND REPORT_DATE = ? AND DEL_FLG = 'N'";
			PreparedStatement stmtSoftDeleteLBook = conn.prepareStatement(softDeleteLBook);

			// Soft delete previous versions in GENERAL_MASTER_TABLE
			String softDeleteMaster = "UPDATE GENERAL_MASTER_TABLE SET DEL_FLG = 'Y' WHERE ACCOUNT_NO = ? AND REPORT_DATE = ? AND DEL_FLG = 'N' AND REPORT_CODE='LOANB'";
			PreparedStatement stmtSoftDeleteMaster = conn.prepareStatement(softDeleteMaster);

			// Soft delete previous versions in GENERAL_MASTER_SRC
			String softDeleteSrc = "UPDATE GENERAL_MASTER_SRC SET DEL_FLG = 'Y' WHERE ACCOUNT_NO = ? AND REPORT_DATE = ? AND DEL_FLG = 'N' AND REPORT_CODE='LOANB'";
			PreparedStatement stmtSoftDeleteSrc = conn.prepareStatement(softDeleteSrc);

			// INSERT query for GENERAL_MASTER_TABLE
			String insertMaster = "INSERT INTO GENERAL_MASTER_TABLE (SOL_ID, CUSTOMER_ID, CUSTOMER_NAME, SCHM_CODE, SCHM_DESC, "
					+ "ACCT_OPEN_DATE, APPROVED_LIMIT, SANCTION_LIMIT, DISBURSED_AMT, BALANCE_AS_ON, CURRENCY, BAL_EQUI_TO_BWP, "
					+ "HUNDRED, ACCRUED_INT_AMT, MONTHLY_INTEREST, LAST_INTEREST_DEBIT_DATE, ACCT_CLS_FLG, ACCT_CLOSE_DATE, "
					+ "GENDER, CLASSIFICATION_CODE, CONSTITUTION_CODE, MATURITY_DATE, GL_SUB_HEAD_CODE, GL_SUB_HEAD_DESC, "
					+ "TENOR_MONTH, EMI, SEGMENT, FACILITY, PAST_DUE, PAST_DUE_DAYS, ASSET, PROVISION, UNSECURED, "
					+ "INT_BUCKET, STAFF, SMME, LABOD, NEW_AC, UNDRAWN, SECTOR, PERIOD, EFFECTIVE_INTEREST_RATE, "
					+ "STAGE, ECL_PROVISION, MAT_BUCKET, " + "ENTRY_TIME, MODIFY_TIME, VERIFY_TIME, UPLOAD_DATE, "
					+ "ENTRY_USER, MODIFY_USER, VERIFY_USER, DEL_USER, "
					+ "ENTRY_FLG, MODIFY_FLG, VERIFY_FLG, DEL_FLG, "
					+ "ACCOUNT_NO, REPORT_DATE, REPORT_CODE, VERSION, BLBF_FLG) " + "VALUES ("
					+ String.join(",", Collections.nCopies(62, "?")) + ")";
			PreparedStatement stmtInsertMaster = conn.prepareStatement(insertMaster);

			// INSERT query for GENERAL_MASTER_SRC (same structure)
			String insertSrc = "INSERT INTO GENERAL_MASTER_SRC (SOL_ID, CUSTOMER_ID, CUSTOMER_NAME, SCHM_CODE, SCHM_DESC, "
					+ "ACCT_OPEN_DATE, APPROVED_LIMIT, SANCTION_LIMIT, DISBURSED_AMT, BALANCE_AS_ON, CURRENCY, BAL_EQUI_TO_BWP, "
					+ "HUNDRED, ACCRUED_INT_AMT, MONTHLY_INTEREST, LAST_INTEREST_DEBIT_DATE, ACCT_CLS_FLG, ACCT_CLOSE_DATE, "
					+ "GENDER, CLASSIFICATION_CODE, CONSTITUTION_CODE, MATURITY_DATE, GL_SUB_HEAD_CODE, GL_SUB_HEAD_DESC, "
					+ "TENOR_MONTH, EMI, SEGMENT, FACILITY, PAST_DUE, PAST_DUE_DAYS, ASSET, PROVISION, UNSECURED, "
					+ "INT_BUCKET, STAFF, SMME, LABOD, NEW_AC, UNDRAWN, SECTOR, PERIOD, EFFECTIVE_INTEREST_RATE, "
					+ "STAGE, ECL_PROVISION, MAT_BUCKET, " + "ENTRY_TIME, MODIFY_TIME, VERIFY_TIME, UPLOAD_DATE, "
					+ "ENTRY_USER, MODIFY_USER, VERIFY_USER, DEL_USER, "
					+ "ENTRY_FLG, MODIFY_FLG, VERIFY_FLG, DEL_FLG, "
					+ "ACCOUNT_NO, REPORT_DATE, REPORT_CODE, VERSION, BLBF_FLG) " + "VALUES ("
					+ String.join(",", Collections.nCopies(62, "?")) + ")";
			PreparedStatement stmtInsertSrc = conn.prepareStatement(insertSrc);

			// INSERT query for L_BOOK
			String insertBLBF = "INSERT INTO L_BOOK (SOL_ID, CUSTOMER_ID, ACCOUNT_NO, CUSTOMER_NAME, SCHM_CODE, SCHM_DESC, "
					+ "ACCT_OPEN_DATE, APPROVED_LIMIT, SANCTION_LIMIT, DISBURSED_AMT, BALANCE_AS_ON, CURRENCY, BAL_EQUI_TO_BWP, "
					+ "RATE_OF_INTEREST, HUNDRED, ACCRUED_INT_AMT, MONTHLY_INTEREST, LAST_INTEREST_DEBIT_DATE, ACCT_CLS_FLG, "
					+ "ACCT_CLOSE_DATE, GENDER, CLASSIFICATION_CODE, CONSTITUTION_CODE, MATURITY_DATE, GL_SUB_HEAD_CODE, "
					+ "GL_SUB_HEAD_DESC, TENOR_MONTH, EMI, SEGMENT, FACILITY, PAST_DUE, PAST_DUE_DAYS, ASSET, PROVISION, UNSECURED, "
					+ "INT_BUCKET, STAFF, SMME, LABOD, NEW_AC, UNDRAWN, SECTOR, PERIOD, EFFECTIVE_INTEREST_RATE, STAGE, ECL_PROVISION, "
					+ "REPORT_DATE, MAT_BUCKET, ENTRY_TIME, MODIFY_TIME, VERIFY_TIME, UPLOAD_DATE, ENTRY_USER, MODIFY_USER, VERIFY_USER, "
					+ "ENTRY_FLG, MODIFY_FLG, VERIFY_FLG, DEL_FLG, REPORT_CODE, VERSION) " + "VALUES ("
					+ String.join(",", Collections.nCopies(61, "?")) + ")";
			PreparedStatement stmtBLBF = conn.prepareStatement(insertBLBF);

			int count = 0;
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				boolean emptyRow = true;
				for (int cn = 0; cn < row.getLastCellNum(); cn++) {
					Cell cell = row.getCell(cn);
					if (cell != null && !formatter.formatCellValue(cell, evaluator).trim().isEmpty()) {
						emptyRow = false;
						break;
					}
				}
				if (emptyRow)
					continue;

				try {
					String accountNo = getCellStringSafe(row, 2, formatter, evaluator);
					java.sql.Date reportDate = getCellDateSafe(row, 46, formatter, evaluator);

					// Get current max version for L_BOOK
					stmtGetMaxVersionLBook.setString(1, accountNo);
					stmtGetMaxVersionLBook.setDate(2, reportDate);
					ResultSet rsLBook = stmtGetMaxVersionLBook.executeQuery();
					int currentMaxVersionLBook = 0;
					if (rsLBook.next()) {
						currentMaxVersionLBook = rsLBook.getInt(1);
					}
					rsLBook.close();
					int newVersionLBook = currentMaxVersionLBook + 1;

					// Get current max version for GENERAL_MASTER_TABLE
					stmtGetMaxVersionMaster.setString(1, accountNo);
					stmtGetMaxVersionMaster.setDate(2, reportDate);
					ResultSet rsMaster = stmtGetMaxVersionMaster.executeQuery();
					int currentMaxVersionMaster = 0;
					if (rsMaster.next()) {
						currentMaxVersionMaster = rsMaster.getInt(1);
					}
					rsMaster.close();
					int newVersionMaster = currentMaxVersionMaster + 1;

					// Get current max version for GENERAL_MASTER_SRC
					stmtGetMaxVersionSrc.setString(1, accountNo);
					stmtGetMaxVersionSrc.setDate(2, reportDate);
					ResultSet rsSrc = stmtGetMaxVersionSrc.executeQuery();
					int currentMaxVersionSrc = 0;
					if (rsSrc.next()) {
						currentMaxVersionSrc = rsSrc.getInt(1);
					}
					rsSrc.close();
					int newVersionSrc = currentMaxVersionSrc + 1;

					// Soft delete previous versions in L_BOOK
					if (currentMaxVersionLBook > 0) {
						stmtSoftDeleteLBook.setString(1, accountNo);
						stmtSoftDeleteLBook.setDate(2, reportDate);
						stmtSoftDeleteLBook.executeUpdate();
					}

					// Soft delete previous versions in GENERAL_MASTER_TABLE
					if (currentMaxVersionMaster > 0) {
						stmtSoftDeleteMaster.setString(1, accountNo);
						stmtSoftDeleteMaster.setDate(2, reportDate);
						stmtSoftDeleteMaster.executeUpdate();
					}

					// Soft delete previous versions in GENERAL_MASTER_SRC
					if (currentMaxVersionSrc > 0) {
						stmtSoftDeleteSrc.setString(1, accountNo);
						stmtSoftDeleteSrc.setDate(2, reportDate);
						stmtSoftDeleteSrc.executeUpdate();
					}

					// Insert into L_BOOK with new version
					int col = 0;
					stmtBLBF.setString(++col, getCellStringSafe(row, 0, formatter, evaluator)); // SOL_ID
					stmtBLBF.setString(++col, getCellStringSafe(row, 1, formatter, evaluator)); // CUSTOMER_ID
					stmtBLBF.setString(++col, accountNo);
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
					stmtBLBF.setDate(++col, reportDate);
					stmtBLBF.setString(++col, getCellStringSafe(row, 47, formatter, evaluator)); // MAT_BUCKET
					stmtBLBF.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // ENTRY_TIME
					stmtBLBF.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // MODIFY_TIME
					stmtBLBF.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // VERIFY_TIME
					stmtBLBF.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // UPLOAD_DATE
					stmtBLBF.setString(++col, userid); // ENTRY_USER
					stmtBLBF.setString(++col, userid); // MODIFY_USER
					stmtBLBF.setString(++col, userid); // VERIFY_USER
					stmtBLBF.setString(++col, "Y"); // ENTRY_FLG
					stmtBLBF.setString(++col, "N"); // MODIFY_FLG
					stmtBLBF.setString(++col, "N"); // VERIFY_FLG
					stmtBLBF.setString(++col, "N"); // DEL_FLG
					stmtBLBF.setString(++col, "LOANB"); // REPORT_CODE
					stmtBLBF.setInt(++col, newVersionLBook); // VERSION
					stmtBLBF.addBatch();

					// Insert into GENERAL_MASTER_TABLE with new version
					col = 0;
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 0, formatter, evaluator)); // SOL_ID
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 1, formatter, evaluator)); // CUSTOMER_ID
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 3, formatter, evaluator)); // CUSTOMER_NAME
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 4, formatter, evaluator)); // SCHM_CODE
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 5, formatter, evaluator)); // SCHM_DESC
					stmtInsertMaster.setDate(++col, getCellDateSafe(row, 6, formatter, evaluator)); // ACCT_OPEN_DATE
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 7, formatter, evaluator)); // APPROVED_LIMIT
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 8, formatter, evaluator)); // SANCTION_LIMIT
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator)); // DISBURSED_AMT
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 10, formatter, evaluator)); // BALANCE_AS_ON
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 11, formatter, evaluator)); // CURRENCY
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator)); // BAL_EQUI_TO_BWP
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 14, formatter, evaluator)); // HUNDRED
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 15, formatter, evaluator)); // ACCRUED_INT_AMT
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 16, formatter, evaluator)); // MONTHLY_INTEREST
					stmtInsertMaster.setDate(++col, getCellDateSafe(row, 17, formatter, evaluator)); // LAST_INTEREST_DEBIT_DATE
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 18, formatter, evaluator)); // ACCT_CLS_FLG
					stmtInsertMaster.setDate(++col, getCellDateSafe(row, 19, formatter, evaluator)); // ACCT_CLOSE_DATE
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 20, formatter, evaluator)); // GENDER
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 21, formatter, evaluator)); // CLASSIFICATION_CODE
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 22, formatter, evaluator)); // CONSTITUTION_CODE
					stmtInsertMaster.setDate(++col, getCellDateSafe(row, 23, formatter, evaluator)); // MATURITY_DATE
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 24, formatter, evaluator)); // GL_SUB_HEAD_CODE
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 25, formatter, evaluator)); // GL_SUB_HEAD_DESC
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 26, formatter, evaluator)); // TENOR_MONTH
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 27, formatter, evaluator)); // EMI
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 28, formatter, evaluator)); // SEGMENT
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 29, formatter, evaluator)); // FACILITY
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 30, formatter, evaluator)); // PAST_DUE
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 31, formatter, evaluator)); // PAST_DUE_DAYS
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 32, formatter, evaluator)); // ASSET
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 33, formatter, evaluator)); // PROVISION
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 34, formatter, evaluator)); // UNSECURED
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 35, formatter, evaluator)); // INT_BUCKET
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 36, formatter, evaluator)); // STAFF
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 37, formatter, evaluator)); // SMME
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 38, formatter, evaluator)); // LABOD
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 39, formatter, evaluator)); // NEW_AC
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 40, formatter, evaluator)); // UNDRAWN
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 41, formatter, evaluator)); // SECTOR
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 42, formatter, evaluator)); // PERIOD
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 43, formatter, evaluator)); // EFFECTIVE_INTEREST_RATE
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 44, formatter, evaluator)); // STAGE
					stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 45, formatter, evaluator)); // ECL_PROVISION
					stmtInsertMaster.setString(++col, getCellStringSafe(row, 47, formatter, evaluator)); // MAT_BUCKET
					stmtInsertMaster.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // ENTRY_TIME
					stmtInsertMaster.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // MODIFY_TIME
					stmtInsertMaster.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // VERIFY_TIME
					stmtInsertMaster.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // UPLOAD_DATE
					stmtInsertMaster.setString(++col, userid); // ENTRY_USER
					stmtInsertMaster.setString(++col, userid); // MODIFY_USER
					stmtInsertMaster.setString(++col, userid); // VERIFY_USER
					stmtInsertMaster.setString(++col, "N"); // DEL_USER
					stmtInsertMaster.setString(++col, "Y"); // ENTRY_FLG
					stmtInsertMaster.setString(++col, "N"); // MODIFY_FLG
					stmtInsertMaster.setString(++col, "Y"); // VERIFY_FLG
					stmtInsertMaster.setString(++col, "N"); // DEL_FLG
					stmtInsertMaster.setString(++col, accountNo); // ACCOUNT_NO
					stmtInsertMaster.setDate(++col, reportDate); // REPORT_DATE
					stmtInsertMaster.setString(++col, "LOANB"); // REPORT_CODE
					stmtInsertMaster.setInt(++col, newVersionMaster); // VERSION
					stmtInsertMaster.setString(++col, "Y"); // BLBF_FLG
					stmtInsertMaster.addBatch();

					// Insert into GENERAL_MASTER_SRC with new version (SAME DATA AS
					// GENERAL_MASTER_TABLE)
					col = 0;
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 0, formatter, evaluator)); // SOL_ID
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 1, formatter, evaluator)); // CUSTOMER_ID
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 3, formatter, evaluator)); // CUSTOMER_NAME
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 4, formatter, evaluator)); // SCHM_CODE
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 5, formatter, evaluator)); // SCHM_DESC
					stmtInsertSrc.setDate(++col, getCellDateSafe(row, 6, formatter, evaluator)); // ACCT_OPEN_DATE
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 7, formatter, evaluator)); // APPROVED_LIMIT
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 8, formatter, evaluator)); // SANCTION_LIMIT
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator)); // DISBURSED_AMT
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 10, formatter, evaluator)); // BALANCE_AS_ON
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 11, formatter, evaluator)); // CURRENCY
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator)); // BAL_EQUI_TO_BWP
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 14, formatter, evaluator)); // HUNDRED
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 15, formatter, evaluator)); // ACCRUED_INT_AMT
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 16, formatter, evaluator)); // MONTHLY_INTEREST
					stmtInsertSrc.setDate(++col, getCellDateSafe(row, 17, formatter, evaluator)); // LAST_INTEREST_DEBIT_DATE
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 18, formatter, evaluator)); // ACCT_CLS_FLG
					stmtInsertSrc.setDate(++col, getCellDateSafe(row, 19, formatter, evaluator)); // ACCT_CLOSE_DATE
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 20, formatter, evaluator)); // GENDER
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 21, formatter, evaluator)); // CLASSIFICATION_CODE
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 22, formatter, evaluator)); // CONSTITUTION_CODE
					stmtInsertSrc.setDate(++col, getCellDateSafe(row, 23, formatter, evaluator)); // MATURITY_DATE
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 24, formatter, evaluator)); // GL_SUB_HEAD_CODE
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 25, formatter, evaluator)); // GL_SUB_HEAD_DESC
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 26, formatter, evaluator)); // TENOR_MONTH
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 27, formatter, evaluator)); // EMI
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 28, formatter, evaluator)); // SEGMENT
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 29, formatter, evaluator)); // FACILITY
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 30, formatter, evaluator)); // PAST_DUE
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 31, formatter, evaluator)); // PAST_DUE_DAYS
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 32, formatter, evaluator)); // ASSET
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 33, formatter, evaluator)); // PROVISION
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 34, formatter, evaluator)); // UNSECURED
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 35, formatter, evaluator)); // INT_BUCKET
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 36, formatter, evaluator)); // STAFF
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 37, formatter, evaluator)); // SMME
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 38, formatter, evaluator)); // LABOD
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 39, formatter, evaluator)); // NEW_AC
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 40, formatter, evaluator)); // UNDRAWN
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 41, formatter, evaluator)); // SECTOR
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 42, formatter, evaluator)); // PERIOD
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 43, formatter, evaluator)); // EFFECTIVE_INTEREST_RATE
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 44, formatter, evaluator)); // STAGE
					stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 45, formatter, evaluator)); // ECL_PROVISION
					stmtInsertSrc.setString(++col, getCellStringSafe(row, 47, formatter, evaluator)); // MAT_BUCKET
					stmtInsertSrc.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // ENTRY_TIME
					stmtInsertSrc.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // MODIFY_TIME
					stmtInsertSrc.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // VERIFY_TIME
					stmtInsertSrc.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // UPLOAD_DATE
					stmtInsertSrc.setString(++col, userid); // ENTRY_USER
					stmtInsertSrc.setString(++col, userid); // MODIFY_USER
					stmtInsertSrc.setString(++col, userid); // VERIFY_USER
					stmtInsertSrc.setString(++col, "N"); // DEL_USER
					stmtInsertSrc.setString(++col, "Y"); // ENTRY_FLG
					stmtInsertSrc.setString(++col, "N"); // MODIFY_FLG
					stmtInsertSrc.setString(++col, "Y"); // VERIFY_FLG
					stmtInsertSrc.setString(++col, "N"); // DEL_FLG
					stmtInsertSrc.setString(++col, accountNo); // ACCOUNT_NO
					stmtInsertSrc.setDate(++col, reportDate); // REPORT_DATE
					stmtInsertSrc.setString(++col, "LOANB"); // REPORT_CODE
					stmtInsertSrc.setInt(++col, newVersionSrc); // VERSION
					stmtInsertSrc.setString(++col, "Y"); // BLBF_FLG
					stmtInsertSrc.addBatch();

					savedCount++;
					count++;

					if (count % batchSize == 0) {
						stmtBLBF.executeBatch();
						stmtInsertMaster.executeBatch();
						stmtInsertSrc.executeBatch();
						conn.commit();
						evaluator.clearAllCachedResultValues();
					}

				} catch (Exception ex) {
					skippedCount++;
					logger.error("Skipping row {} due to error: {}", i, ex.getMessage(), ex);
				}
			}

			stmtBLBF.executeBatch();
			stmtInsertMaster.executeBatch();
			stmtInsertSrc.executeBatch();
			conn.commit();

			long duration = System.currentTimeMillis() - startTime;
			/*return "✅ BLBF Upload complete. Saved: " + savedCount + ", Skipped: " + skippedCount + ". Time: " + duration
					+ " ms";*/
			return "Inserted : " + savedCount + " Records";
		} catch (Exception e) {
			logger.error("❌ Error while processing BLBF Excel: {}", e.getMessage(), e);
			return "Error while reading Excel: " + e.getMessage();
		}
	}

	// ===== Helper methods =====
	private String getCellValueSafe(Cell cell, DataFormatter formatter, FormulaEvaluator evaluator) {
	    if (cell == null) return "";
	    
	    try {
	        // Try to format the cell value
	        return formatter.formatCellValue(cell, evaluator);
	    } catch (Exception e) {
	        // If formula evaluation fails (e.g., DATEDIF not supported), try to get cached value
	        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
	            try {
	                int cachedType = cell.getCachedFormulaResultType();
	                switch (cachedType) {
	                    case Cell.CELL_TYPE_NUMERIC:
	                        if (DateUtil.isCellDateFormatted(cell)) {
	                            return formatter.formatCellValue(cell);
	                        }
	                        return String.valueOf(cell.getNumericCellValue());
	                    case Cell.CELL_TYPE_STRING:
	                        return cell.getRichStringCellValue().getString();
	                    case Cell.CELL_TYPE_BOOLEAN:
	                        return String.valueOf(cell.getBooleanCellValue());
	                    default:
	                        return "";
	                }
	            } catch (Exception ex) {
	                logger.warn("Could not get cached formula value for cell: {}", ex.getMessage());
	                return "";
	            }
	        }
	        return "";
	    }
	}
	
	private String getCellStringSafe(Row row, int index, DataFormatter formatter, FormulaEvaluator evaluator) {
	    Cell cell = row.getCell(index);
	    if (cell == null) return null;
	    
	    String value = getCellValueSafe(cell, formatter, evaluator).trim();
	    return value.isEmpty() ? null : value;
	}

	private java.sql.Date getCellDateSafe(Row row, int colIndex, DataFormatter formatter, FormulaEvaluator evaluator) {
	    try {
	        Cell cell = row.getCell(colIndex);
	        if (cell == null) return null;
	        
	        // First try: if it's a numeric date cell
	        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
	            return new java.sql.Date(cell.getDateCellValue().getTime());
	        }
	        
	        // Second try: if it's a formula that returns a date
	        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
	            try {
	                int cachedType = cell.getCachedFormulaResultType();
	                if (cachedType == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
	                    return new java.sql.Date(cell.getDateCellValue().getTime());
	                }
	            } catch (Exception e) {
	                // Fall through to text parsing
	            }
	        }
	        
	        // Third try: parse as text in dd-MM-yyyy format
	        String text = getCellValueSafe(cell, formatter, evaluator).trim();
	        if (text.isEmpty()) return null;
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	        sdf.setLenient(false);
	        return new java.sql.Date(sdf.parse(text).getTime());
	        
	    } catch (Exception e) {
	        logger.warn("Could not parse date at column {}: {}", colIndex, e.getMessage());
	        return null;
	    }
	}

	private BigDecimal getCellDecimalSafe(Row row, int index, DataFormatter formatter, FormulaEvaluator evaluator) {
	    Cell cell = row.getCell(index);
	    if (cell == null) return null;
	    
	    try {
	        // For numeric cells, get the value directly
	        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
	            return BigDecimal.valueOf(cell.getNumericCellValue());
	        }
	        
	        // For formula cells, try to get cached numeric value
	        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
	            try {
	                int cachedType = cell.getCachedFormulaResultType();
	                if (cachedType == Cell.CELL_TYPE_NUMERIC) {
	                    return BigDecimal.valueOf(cell.getNumericCellValue());
	                }
	            } catch (Exception e) {
	                // Fall through to text parsing
	            }
	        }
	        
	        // Otherwise, format and parse as text
	        String text = getCellValueSafe(cell, formatter, evaluator).replaceAll(",", "").trim();
	        if (text.isEmpty()) return null;
	        
	        return new BigDecimal(text);
	        
	    } catch (Exception e) {
	        logger.warn("Could not parse decimal at column {}: {}", index, e.getMessage());
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
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell))
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
			List<GeneralMasterEntity> dataList = GeneralMasterRepos.findLoanBRecordsByReportDate(todate);

			if (dataList != null && !dataList.isEmpty()) {
				int rowIndex = 1;
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

				for (GeneralMasterEntity rec : dataList) {
					XSSFRow row = sheet.createRow(rowIndex++);
					int col = 0;

					// ======== Text / Date Cells ========
					createTextCell(row, col++, rec.getCustomerId(), dataCellStyle);
					createTextCell(row, col++, rec.getSolId(), dataCellStyle);
					createTextCell(row, col++, rec.getAccountNo(), dataCellStyle);
					createTextCell(row, col++, rec.getCustomerName(), dataCellStyle);
					createTextCell(row, col++, rec.getSchmCode(), dataCellStyle);
					createTextCell(row, col++, rec.getSchmDesc(), dataCellStyle);
					createTextCell(row, col++, rec.getAcctOpenDate() != null ? sdf.format(rec.getAcctOpenDate()) : "",
							dataCellStyle);

					// ======== Numeric Cells ========
					createNumericCell(row, col++, rec.getApprovedLimit(), numericStyle);
					createNumericCell(row, col++, rec.getSanctionLimit(), numericStyle);
					createNumericCell(row, col++, rec.getDisbursedAmt(), numericStyle);
					createNumericCell(row, col++, rec.getBalanceAsOn(), numericStyle);

					createTextCell(row, col++, rec.getCurrency(), dataCellStyle);
					createNumericCell(row, col++, rec.getBalEquiToBwp(), numericStyle);
					createNumericCell(row, col++, rec.getRateOfInterest(), numericStyle);
					createNumericCell(row, col++, rec.getAccruedIntAmt(), numericStyle);
					createNumericCell(row, col++, rec.getMonthlyInterest(), numericStyle);
					createTextCell(row, col++,
							rec.getLastInterestDebitDate() != null ? sdf.format(rec.getLastInterestDebitDate()) : "",
							dataCellStyle);
					createTextCell(row, col++, rec.getAcctClsFlg(), dataCellStyle);
					createTextCell(row, col++, rec.getAcctOpenDate() != null ? sdf.format(rec.getAcctOpenDate()) : "",
							dataCellStyle);
					createTextCell(row, col++, rec.getGender(), dataCellStyle);
					createTextCell(row, col++, rec.getClassificationCode(), dataCellStyle);
					createTextCell(row, col++, rec.getConstitutionCode(), dataCellStyle);
					createTextCell(row, col++, rec.getMaturityDate() != null ? sdf.format(rec.getMaturityDate()) : "",
							dataCellStyle);
					createTextCell(row, col++, rec.getGlSubHeadCode(), dataCellStyle);
					createTextCell(row, col++, rec.getGlSubHeadDesc(), dataCellStyle);

					// ======== Mixed Numeric & Text ========
					createNumericCell(row, col++, rec.getTenorMonth(), numericStyle);
					createNumericCell(row, col++, rec.getEmi(), numericStyle);
					createTextCell(row, col++, rec.getSegment(), dataCellStyle);
					createTextCell(row, col++, rec.getFacility(), dataCellStyle);
					createTextCell(row, col++, rec.getPastDue(), dataCellStyle);
					createNumericCell(row, col++, rec.getPastDueDays(), numericStyle);
					createTextCell(row, col++, rec.getAsset(), dataCellStyle);
					createNumericCell(row, col++, rec.getProvision(), numericStyle);
					createTextCell(row, col++, rec.getUnsecured(), dataCellStyle);
					createTextCell(row, col++, rec.getIntBucket(), dataCellStyle);
					createTextCell(row, col++, rec.getStaff(), dataCellStyle);
					createTextCell(row, col++, rec.getSmme(), dataCellStyle);
					createTextCell(row, col++, rec.getLabod(), dataCellStyle);
					createTextCell(row, col++, rec.getNewAc(), dataCellStyle);
					createNumericCell(row, col++, rec.getUndrawn(), numericStyle);
					createTextCell(row, col++, rec.getSector(), dataCellStyle);
					createTextCell(row, col++, rec.getPeriod(), dataCellStyle);
					createNumericCell(row, col++, rec.getEffectiveInterestRate(), numericStyle);
					createTextCell(row, col++, rec.getStage(), dataCellStyle);
					createNumericCell(row, col++, rec.getEclProvision(), numericStyle);
					createTextCell(row, col++, rec.getBranchName(), dataCellStyle);
					createTextCell(row, col++, rec.getBranchCode(), dataCellStyle);
					createTextCell(row, col++, rec.getReportDate() != null ? sdf.format(rec.getReportDate()) : "",
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