package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import com.bornfire.brrs.entities.GeneralMasterEntity;
import com.bornfire.brrs.entities.GeneralMasterRepo;
import com.bornfire.brrs.entities.MCBL_Detail_Rep;
import com.bornfire.brrs.entities.MCBL_Entity;
import com.bornfire.brrs.entities.MCBL_Main_Rep;
import com.bornfire.brrs.entities.MCBL_Rep;

@Service
@Transactional
public class MCBL_Services {

	@Autowired
	SequenceGenerator sequence;

	@Autowired
	private MCBL_Main_Rep MCBL_Main_Reps;

	@Autowired
	GeneralMasterRepo GeneralMasterRepos;
	@Autowired
	private MCBL_Detail_Rep MCBL_Detail_Reps;

	@Autowired
	private MCBL_Rep mcblRep;

	@Autowired
	private DataSource dataSource;

	private static final Logger logger = LoggerFactory.getLogger(MCBL_Services.class);

	private final ConcurrentHashMap<String, String> jobStatusStorage = new ConcurrentHashMap<>();

	@Async
	public void initializeJobStatus(String jobId, MultipartFile file, String userid, String username,
			String reportDate) {

		logger.info("Initializing job. jobId={}, userId={}, reportDate={}", jobId, userid, reportDate);

		jobStatusStorage.put(jobId, "PROCESSING");

		startMCBLUploadAsync(jobId, file, userid, username, reportDate);
	}

	public String startMCBLUploadAsync(String jobId, MultipartFile file, String userid, String username,
			String reportDate) {

		logger.info("MCBL upload started. jobId={}", jobId);

		try {
			String resultMsg = addMCBL(file, userid, username, reportDate);

			jobStatusStorage.put(jobId, "COMPLETED:" + resultMsg);
			logger.info("MCBL upload completed. jobId={}, result={}", jobId, resultMsg);

			return resultMsg;

		} catch (Exception e) {
			logger.error("MCBL upload failed. jobId={}", jobId, e);
			jobStatusStorage.put(jobId, "ERROR:" + e.getMessage());
			return "ERROR:" + e.getMessage();
		}
	}

	public String getJobStatus(String jobId) {
		return jobStatusStorage.getOrDefault(jobId, "NOT_FOUND");
	}

	@Transactional
	public String addMCBL(MultipartFile file, String userid, String username, String reportDate) {
		logger.info("======================================================================");
		logger.info("START: addMCBL() - User: {}, Report Date: {}", userid, reportDate);
		logger.info("======================================================================");

		long startTime = System.currentTimeMillis();

		// 1. Initial File Validation
		if (file == null || file.isEmpty()) {
			logger.error("PROCESS TERMINATED: The uploaded file is null or empty.");
			return "Error: Uploaded file is empty!";
		}

		String fileName = file.getOriginalFilename();
		logger.info("File received for processing: {} | Size: {} bytes", fileName, file.getSize());

		try (InputStream is = file.getInputStream(); Connection conn = dataSource.getConnection()) {
			Workbook workbook;

			// 2. Workbook Initialization based on extension
			logger.info("Initializing Workbook for file extension check.");
			if (fileName != null && fileName.toLowerCase().endsWith(".xlsx")) {
				logger.info("Format detected: .xlsx (XSSFWorkbook)");
				workbook = new XSSFWorkbook(is);
			} else if (fileName != null && fileName.toLowerCase().endsWith(".xls")) {
				logger.info("Format detected: .xls (HSSFWorkbook)");
				workbook = new HSSFWorkbook(is);
			} else {
				logger.error("PROCESS TERMINATED: Invalid file format '{}'. Expected .xls or .xlsx.", fileName);
				return "Error: Invalid file format! Please upload .xls or .xlsx file.";
			}

			// 3. Database Transaction Setup
			logger.info("Setting Connection AutoCommit to FALSE for transaction management.");
			conn.setAutoCommit(false);

			// 4. Date Parsing
			logger.info("Parsing reportDate string: {}", reportDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date parsedDate = sdf.parse(reportDate);
			java.sql.Date sqlReportDate = new java.sql.Date(parsedDate.getTime());
			logger.info("Successfully converted to SQL Date: {}", sqlReportDate);

			// 5. Soft-Delete Previous Records
			logger.info("Executing soft-delete (DEL_FLG='Y') for existing records on {}", sqlReportDate);

			String markDeletedMCBL = "UPDATE MCBL SET DEL_FLG = 'Y', MODIFY_TIME = SYSTIMESTAMP, MODIFY_USER = ? "
					+ "WHERE REPORT_DATE = ? AND DEL_FLG = 'N'";

			String markDeletedGeneral = "UPDATE GENERAL_MASTER_TABLE SET DEL_FLG = 'Y', MODIFY_TIME = SYSTIMESTAMP, MODIFY_USER = ? "
					+ "WHERE REPORT_CODE = 'MCBL' AND REPORT_DATE = ? AND DEL_FLG = 'N'";

			String markDeletedGeneralSrc = "UPDATE GENERAL_MASTER_SRC SET DEL_FLG = 'Y', MODIFY_TIME = SYSTIMESTAMP, MODIFY_USER = ? "
					+ "WHERE REPORT_CODE = 'MCBL' AND REPORT_DATE = ? AND DEL_FLG = 'N'";

			try (PreparedStatement ps = conn.prepareStatement(markDeletedMCBL)) {
				ps.setString(1, userid);
				ps.setDate(2, sqlReportDate);
				int count = ps.executeUpdate();
				logger.info("Clean-up: Marked {} records as deleted in MCBL table.", count);
			}

			try (PreparedStatement ps = conn.prepareStatement(markDeletedGeneral)) {
				ps.setString(1, userid);
				ps.setDate(2, sqlReportDate);
				int count = ps.executeUpdate();
				logger.info("Clean-up: Marked {} records as deleted in GENERAL_MASTER_TABLE.", count);
			}

			try (PreparedStatement ps = conn.prepareStatement(markDeletedGeneralSrc)) {
				ps.setString(1, userid);
				ps.setDate(2, sqlReportDate);
				int count = ps.executeUpdate();
				logger.info("Clean-up: Marked {} records as deleted in GENERAL_MASTER_SRC.", count);
			}

			// 6. Access Sheet
			logger.info("Accessing Excel sheet named 'MCBL'.");
			Sheet sheet = workbook.getSheet("MCBL");
			if (sheet == null) {
				logger.error("PROCESS TERMINATED: Sheet 'MCBL' not found in workbook.");
				return "Error: Sheet 'MCBL' not found!";
			}
			int totalRows = sheet.getLastRowNum();
			logger.info("Total rows identified in sheet: {}", totalRows);

			// 7. Last Month Data Retrieval (For tracking changes)
			Calendar cal = Calendar.getInstance();
			cal.setTime(parsedDate);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DATE, -1);
			java.sql.Date lastMonthDate = new java.sql.Date(cal.getTimeInMillis());
			logger.info("Calculated previous month date for tracking: {}", lastMonthDate);

			Set<String> lastMonthAccounts = new HashSet<>();
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT MCBL_HEAD_ACC_NO FROM MCBL WHERE REPORT_DATE = ? AND DEL_FLG = 'N'")) {
				ps.setDate(1, lastMonthDate);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					lastMonthAccounts.add(rs.getString(1));
				}
			}
			logger.info("Found {} active accounts from last month to compare.", lastMonthAccounts.size());

			// 8. Version Management
			logger.info("Pre-fetching current version numbers from DB.");
			Map<String, Integer> versionMap = new HashMap<>();
			try (PreparedStatement ps = conn.prepareStatement(
					"SELECT MCBL_HEAD_ACC_NO, MAX(VERSION) as MAX_VERSION FROM MCBL WHERE REPORT_DATE = ? GROUP BY MCBL_HEAD_ACC_NO")) {
				ps.setDate(1, sqlReportDate);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					versionMap.put(rs.getString(1), rs.getInt(2) + 1);
				}
			}
			logger.info("Version mapping complete for {} accounts.", versionMap.size());

			// 9. Prepare Batch Statements
			logger.info("Preparing SQL Batch Statements.");
			String insertMCBL = "INSERT INTO MCBL (MCBL_GL_CODE, MCBL_GL_SUB_CODE, MCBL_HEAD_ACC_NO, MCBL_DESCRIPTION, MCBL_CURRENCY, MCBL_DEBIT_BALANCE, MCBL_CREDIT_BALANCE, MCBL_DEBIT_EQUIVALENT, MCBL_CREDIT_EQUIVALENT, REPORT_CODE, HOME_CURRENCY, DEL_FLG, VERSION, ENTRY_USER, ENTRY_TIME, UPLOAD_DATE, REPORT_DATE, CUST_FLG, MODIFY_TIME, VERIFY_TIME, MODIFY_USER, VERIFY_USER, ENTRY_FLG, MODIFY_FLG, VERIFY_FLG) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Y','N', 'N')";
			String insertGeneral = "INSERT INTO GENERAL_MASTER_TABLE (MCBL_GL_CODE, GL_SUB_HEAD_CODE, ACCOUNT_NO, MCBL_DESCRIPTION, CURRENCY, MCBL_DEBIT_BALANCE, MCBL_CREDIT_BALANCE, MCBL_DEBIT_EQUIVALENT, MCBL_CREDIT_EQUIVALENT, REPORT_CODE, DEL_FLG, VERSION, ENTRY_USER, ENTRY_TIME, UPLOAD_DATE, REPORT_DATE, MCBL_FLG, CUST_FLG) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement mcblStmt = conn.prepareStatement(insertMCBL);
			PreparedStatement genStmt = conn.prepareStatement(insertGeneral);

			// 10. Process Excel Rows
			DataFormatter formatter = new DataFormatter();
			Set<String> processedExcelKeys = new HashSet<>();
			Set<String> currentAccounts = new HashSet<>();
			int count = 0;
			int skippedRows = 0;

			logger.info("Starting Row-by-Row processing.");
			for (int i = 0; i <= totalRows; i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					logger.debug("Row {} is null, skipping.", i);
					continue;
				}

				String glCode = formatter.formatCellValue(row.getCell(0)).trim();
				String glSubCode = formatter.formatCellValue(row.getCell(1)).trim();
				String headAccNo = formatter.formatCellValue(row.getCell(2)).trim();

				if (glCode.isEmpty() || headAccNo.isEmpty()) {
					logger.debug("Row {} skipped: Empty GL Code or Account Number.", i);
					skippedRows++;
					continue;
				}

				// Simple uniqueness check within the Excel itself
				String uniqueKey = glCode + "|" + glSubCode + "|" + headAccNo;
				if (!processedExcelKeys.add(uniqueKey)) {
					logger.warn("Row {}: Duplicate entry for {} in file. Skipping.", i, uniqueKey);
					continue;
				}

				// Extraction
				String desc = formatter.formatCellValue(row.getCell(3)).trim();
				String curr = formatter.formatCellValue(row.getCell(5)).trim();
				BigDecimal debBal = getCellDecimal(row.getCell(6));
				BigDecimal creBal = getCellDecimal(row.getCell(7));
				int version = versionMap.getOrDefault(headAccNo, 1);

				// Populate MCBL Statement
				mcblStmt.setString(1, glCode);
				mcblStmt.setString(2, glSubCode);
				mcblStmt.setString(3, headAccNo);
				mcblStmt.setString(4, desc);
				mcblStmt.setString(5, "BWP"); // As per original logic
				mcblStmt.setBigDecimal(6, debBal);
				mcblStmt.setBigDecimal(7, creBal);
				mcblStmt.setBigDecimal(8, getCellDecimal(row.getCell(8)));
				mcblStmt.setBigDecimal(9, getCellDecimal(row.getCell(9)));
				mcblStmt.setString(10, "MCBL");
				mcblStmt.setString(11, curr);
				mcblStmt.setString(12, "N");
				mcblStmt.setInt(13, version);
				mcblStmt.setString(14, userid);
				mcblStmt.setDate(15, new java.sql.Date(System.currentTimeMillis()));
				mcblStmt.setDate(16, new java.sql.Date(System.currentTimeMillis()));
				mcblStmt.setDate(17, sqlReportDate);
				mcblStmt.setString(18, isNumeric(headAccNo) ? "N" : "Y");
				mcblStmt.setDate(19, sqlReportDate);
				mcblStmt.setDate(20, sqlReportDate);
				mcblStmt.setString(21, userid);
				mcblStmt.setString(22, userid);
				mcblStmt.addBatch();

				// Populate General Statement
				genStmt.setString(1, glCode);
				genStmt.setString(2, glSubCode);
				genStmt.setString(3, headAccNo);
				genStmt.setString(4, desc);
				genStmt.setString(5, curr);
				genStmt.setBigDecimal(6, debBal);
				genStmt.setBigDecimal(7, creBal);
				genStmt.setBigDecimal(8, getCellDecimal(row.getCell(8)));
				genStmt.setBigDecimal(9, getCellDecimal(row.getCell(9)));
				genStmt.setString(10, "MCBL");
				genStmt.setString(11, "N");
				genStmt.setInt(12, version);
				genStmt.setString(13, userid);
				genStmt.setDate(14, new java.sql.Date(System.currentTimeMillis()));
				genStmt.setDate(15, new java.sql.Date(System.currentTimeMillis()));
				genStmt.setDate(16, sqlReportDate);
				genStmt.setString(17, "Y");
				genStmt.setString(18, isNumeric(headAccNo) ? "N" : "Y");
				genStmt.addBatch();

				currentAccounts.add(headAccNo);
				count++;

				// Batch Commit
				if (count % 500 == 0) {
					logger.info("Reaching batch limit (500). Executing intermediate batch update.");
					mcblStmt.executeBatch();
					genStmt.executeBatch();
					conn.commit();
					logger.info("Intermediate commit successful at row {}.", i);
				}
			}

			// Final Batch Execute
			logger.info("Executing final batches for remaining records.");
			mcblStmt.executeBatch();
			genStmt.executeBatch();

			// 11. Account Tracking (Added/Missed)
			logger.info("Calculating New vs Missed accounts for tracking table.");
			Set<String> newAccs = new HashSet<>(currentAccounts);
			newAccs.removeAll(lastMonthAccounts);

			Set<String> missedAccs = new HashSet<>(lastMonthAccounts);
			missedAccs.removeAll(currentAccounts);

			logger.info("Change Summary: New Accounts: {} | Missed Accounts: {}", newAccs.size(), missedAccs.size());

			String insertTrack = "INSERT INTO BRRS_MCBL_ACCOUNT_TRACK (ID, REPORT_DATE, ACCOUNT_NO, CHANGE_TYPE, ENTRY_USER, ENTRY_TIME, REMARKS) VALUES (?, ?, ?, ?, ?, SYSTIMESTAMP, ?)";
			try (PreparedStatement trackStmt = conn.prepareStatement(insertTrack)) {
				for (String acc : newAccs) {
					trackStmt.setString(1, sequence.generateRequestUUId());
					trackStmt.setDate(2, sqlReportDate);
					trackStmt.setString(3, acc);
					trackStmt.setString(4, "ADDED");
					trackStmt.setString(5, userid);
					trackStmt.setString(6, "New Account found in current upload.");
					trackStmt.addBatch();
				}
				for (String acc : missedAccs) {
					trackStmt.setString(1, sequence.generateRequestUUId());
					trackStmt.setDate(2, sqlReportDate);
					trackStmt.setString(3, acc);
					trackStmt.setString(4, "MISSED");
					trackStmt.setString(5, userid);
					trackStmt.setString(6, "Account missing from current month upload.");
					trackStmt.addBatch();
				}
				trackStmt.executeBatch();
				logger.info("Account tracking updates completed.");
			}

			// 12. Final Commit
			logger.info("Finalizing transaction and committing to Database.");
			conn.commit();

			long duration = System.currentTimeMillis() - startTime;
			String finalMsg = String.format("SUCCESS: Processed %d rows (Skipped %d) in %d ms.", count, skippedRows,
					duration);
			logger.info(finalMsg);

			return "✅ MCBL upload completed successfully! Total Records: " + count;

		} catch (Exception e) {
			logger.error("!!! CRITICAL ERROR during MCBL Upload !!!");
			logger.error("Exception Message: {}", e.getMessage());
			logger.error("Stack Trace: ", e);
			return "Error occurred while reading Excel: " + e.getMessage();
		} finally {
			logger.info("======================================================================");
			logger.info("END: addMCBL() process finished.");
			logger.info("======================================================================");
		}
	}

	private boolean isNumeric(String s) {
		if (s == null)
			return false;
		s = s.trim();
		return !s.isEmpty() && s.matches("\\d+");
	}

	private String safeTrim(String value) {
		if (value == null)
			return "NULL";
		value = value.replace(" ", "");
		if (value.endsWith(".0"))
			value = value.substring(0, value.length() - 2);
		return value.toUpperCase();
	}

	private BigDecimal getCellDecimal(Cell cell) {
		if (cell == null)
			return BigDecimal.ZERO;
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

	private final ConcurrentHashMap<String, byte[]> jobStorage = new ConcurrentHashMap<>();

	@Async
	public void generateReportAsync(String jobId, String filename, String todate) {
		System.out.println("Starting report generation for: " + filename);
		byte[] fileData = generateMCBLExcel(filename, todate);
		jobStorage.put(jobId, fileData != null ? fileData : null);
		System.out.println("Report generation completed for: " + filename);
	}

	public byte[] getReport(String jobId) {
		return jobStorage.get(jobId);
	}

	public byte[] generateMCBLExcel(String filename, String todate) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MCBL_Report");

			// ================= Header Style =================
			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerStyle.setFont(headerFont);
			headerStyle.setBorderTop(BorderStyle.THIN);
			headerStyle.setBorderBottom(BorderStyle.THIN);
			headerStyle.setBorderLeft(BorderStyle.THIN);
			headerStyle.setBorderRight(BorderStyle.THIN);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);

			// ================= Balance / Amount Style =================
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			balanceStyle.setBorderTop(BorderStyle.THIN);
			balanceStyle.setBorderBottom(BorderStyle.THIN);
			balanceStyle.setBorderLeft(BorderStyle.THIN);
			balanceStyle.setBorderRight(BorderStyle.THIN);

			// ================= General Data Style =================
			CellStyle dataCellStyle = workbook.createCellStyle();
			dataCellStyle.setBorderTop(BorderStyle.THIN);
			dataCellStyle.setBorderBottom(BorderStyle.THIN);
			dataCellStyle.setBorderLeft(BorderStyle.THIN);
			dataCellStyle.setBorderRight(BorderStyle.THIN);

			// ================= Headers =================
			String[] headers = { "GL Code", "GL Sub Code", "Head Acc No", "Description", "Currency", "Debit Balance",
					"Credit Balance", "Debit Equivalent", "Credit Equivalent", "Report Date" };
			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(headerStyle);
				sheet.setColumnWidth(i, 5000);
			}

			// ================= Fetch data from DB =================
			List<GeneralMasterEntity> dataList = GeneralMasterRepos.findMCBLRecordsByReportDate(todate);

			if (dataList != null && !dataList.isEmpty()) {
				int rowIndex = 1;
				for (GeneralMasterEntity rec : dataList) {
					XSSFRow row = sheet.createRow(rowIndex++);

					// Text / Date cells
					Cell cell0 = row.createCell(0);
					cell0.setCellValue(rec.getMcblGlCode());
					cell0.setCellStyle(dataCellStyle);

					Cell cell1 = row.createCell(1);
					cell1.setCellValue(rec.getGlSubHeadCode());
					cell1.setCellStyle(dataCellStyle);

					Cell cell2 = row.createCell(2);
					cell2.setCellValue(rec.getAccountNo());
					cell2.setCellStyle(dataCellStyle);

					Cell cell3 = row.createCell(3);
					cell3.setCellValue(rec.getMcblDescription());
					cell3.setCellStyle(dataCellStyle);

					Cell cell4 = row.createCell(4);
					cell4.setCellValue(rec.getCurrency());
					cell4.setCellStyle(dataCellStyle);

					// Numeric / Amount cells
					Cell debitCell = row.createCell(5);
					debitCell.setCellValue(
							rec.getMcblDebitBalance() != null ? rec.getMcblDebitBalance().doubleValue() : 0);
					debitCell.setCellStyle(balanceStyle);

					Cell creditCell = row.createCell(6);
					creditCell.setCellValue(
							rec.getMcblCreditBalance() != null ? rec.getMcblCreditBalance().doubleValue() : 0);
					creditCell.setCellStyle(balanceStyle);

					Cell debitEqCell = row.createCell(7);
					debitEqCell.setCellValue(
							rec.getMcblDebitEquivalent() != null ? rec.getMcblDebitEquivalent().doubleValue() : 0);
					debitEqCell.setCellStyle(balanceStyle);

					Cell creditEqCell = row.createCell(8);
					creditEqCell.setCellValue(
							rec.getMcblCreditEquivalent() != null ? rec.getMcblCreditEquivalent().doubleValue() : 0);
					creditEqCell.setCellStyle(balanceStyle);

					// Report Date cell
					Cell cell9 = row.createCell(9);
					cell9.setCellValue(
							rec.getReportDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(rec.getReportDate())
									: "");
					cell9.setCellStyle(dataCellStyle);
				}
			}

			// ================= Write to ByteArray =================
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			return bos.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

}
