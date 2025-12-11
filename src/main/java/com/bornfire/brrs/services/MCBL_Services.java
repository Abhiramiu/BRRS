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
	public void initializeJobStatus(String jobId, MultipartFile file, String userid, String username,String reportDate) {
	    jobStatusStorage.put(jobId, "PROCESSING");
	    startMCBLUploadAsync(jobId, file, userid, username,reportDate);
	}

	public String startMCBLUploadAsync(String jobId, MultipartFile file, String userid, String username,String reportDate) {
	    logger.info("Starting MCBL upload job: {}", jobId);

	    try {
	        String resultMsg = addMCBL(file, userid, username,reportDate);
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
	public String addMCBL(MultipartFile file, String userid, String username, String reportDate) {
	    long startTime = System.currentTimeMillis();
	    
	    if (file == null || file.isEmpty()) {
	        return "Error: Uploaded file is empty!";
	    }
	    
	    try (InputStream is = file.getInputStream(); Connection conn = dataSource.getConnection()) {
	        Workbook workbook;
	        String fileName = file.getOriginalFilename();
	        
	        if (fileName != null && fileName.toLowerCase().endsWith(".xlsx")) {
	            workbook = new XSSFWorkbook(is);
	        } else if (fileName != null && fileName.toLowerCase().endsWith(".xls")) {
	            workbook = new HSSFWorkbook(is);
	        } else {
	            return "Error: Invalid file format! Please upload .xls or .xlsx file.";
	        }
	        
	        conn.setAutoCommit(false);
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        java.util.Date parsedDate = sdf.parse(reportDate);
	        java.sql.Date sqlReportDate = new java.sql.Date(parsedDate.getTime());
	        
	        // ============= MARK PREVIOUS VERSIONS AS DELETED (Before Processing New Upload) =============
	        String markDeletedMCBL = "UPDATE MCBL SET DEL_FLG = 'Y', MODIFY_TIME = SYSTIMESTAMP, MODIFY_USER = ? " +
	            "WHERE REPORT_DATE = ? AND MCBL_HEAD_ACC_NO IN (SELECT DISTINCT MCBL_HEAD_ACC_NO FROM MCBL WHERE REPORT_DATE = ?) " +
	            "AND DEL_FLG = 'N'";
	        
	        String markDeletedGeneral = "UPDATE GENERAL_MASTER_TABLE SET DEL_FLG = 'Y', MODIFY_TIME = SYSTIMESTAMP, MODIFY_USER = ? " +
	            "WHERE REPORT_CODE = 'MCBL' AND REPORT_DATE = ? AND ACCOUNT_NO IN (SELECT DISTINCT ACCOUNT_NO FROM GENERAL_MASTER_TABLE WHERE REPORT_CODE = 'MCBL' AND REPORT_DATE = ?) " +
	            "AND DEL_FLG = 'N'";
	        
	        String markDeletedGeneralSrc = "UPDATE GENERAL_MASTER_SRC SET DEL_FLG = 'Y', MODIFY_TIME = SYSTIMESTAMP, MODIFY_USER = ? " +
	            "WHERE REPORT_CODE = 'MCBL' AND REPORT_DATE = ? AND ACCOUNT_NO IN (SELECT DISTINCT ACCOUNT_NO FROM GENERAL_MASTER_SRC WHERE REPORT_CODE = 'MCBL' AND REPORT_DATE = ?) " +
	            "AND DEL_FLG = 'N'";
	        
	        // ============= COLLECT ACCOUNT NOS FROM EXCEL FIRST =============
	        Sheet sheet = workbook.getSheet("MCBL");
	        if (sheet == null) {
	            return "Error: Sheet 'MCBL' not found!";
	        }
	        
	        DataFormatter formatter = new DataFormatter();
	        Set<String> excelAccountNumbers = new HashSet<>();
	        
	        // Quick scan to collect all valid account numbers
	        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;
	            
	            String headAccNo = formatter.formatCellValue(row.getCell(2)).trim();
	            String glCode = formatter.formatCellValue(row.getCell(0)).trim();
	            String glSubCode = formatter.formatCellValue(row.getCell(1)).trim();
	            String currency = formatter.formatCellValue(row.getCell(5)).trim();
	            
	            if (!headAccNo.isEmpty() && !glCode.isEmpty() && !glSubCode.isEmpty() && !currency.isEmpty() &&
	                glCode.matches("\\d+") && glSubCode.matches("\\d+")) {
	                excelAccountNumbers.add(headAccNo);
	            }
	        }
	        
	        // Mark previous versions of accounts in current upload as deleted
	        try (PreparedStatement ps = conn.prepareStatement(markDeletedMCBL)) {
	            ps.setString(1, userid);
	            ps.setDate(2, sqlReportDate);
	            ps.setDate(3, sqlReportDate);
	            ps.executeUpdate();
	        }
	        
	        try (PreparedStatement ps = conn.prepareStatement(markDeletedGeneral)) {
	            ps.setString(1, userid);
	            ps.setDate(2, sqlReportDate);
	            ps.setDate(3, sqlReportDate);
	            ps.executeUpdate();
	        }
	        
	        try (PreparedStatement ps = conn.prepareStatement(markDeletedGeneralSrc)) {
	            ps.setString(1, userid);
	            ps.setDate(2, sqlReportDate);
	            ps.setDate(3, sqlReportDate);
	            ps.executeUpdate();
	        }
	        
	        // ---------------- LAST MONTH DATA ----------------
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(parsedDate);
	        cal.set(Calendar.DAY_OF_MONTH, 1);
	        cal.add(Calendar.DATE, -1);
	        java.sql.Date lastMonthDate = new java.sql.Date(cal.getTimeInMillis());
	        
	        Set<String> lastMonthAccounts = new HashSet<>();
	        try (PreparedStatement ps = conn.prepareStatement(
	            "SELECT MCBL_HEAD_ACC_NO FROM MCBL WHERE REPORT_DATE = ? AND DEL_FLG = 'N'")) {
	            ps.setDate(1, lastMonthDate);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                lastMonthAccounts.add(rs.getString(1));
	            }
	        }
	        
	        // ---------------- SHEET VALIDATION ----------------
	        Set<String> processedExcelKeys = new HashSet<>();
	        Set<String> currentAccounts = new HashSet<>();
	        
	        // ============= PRE-FETCH EXISTING DB RECORDS =============
	        Set<String> existingMCBLKeys = new HashSet<>();
	        try (PreparedStatement ps = conn.prepareStatement(
	            "SELECT MCBL_GL_CODE, MCBL_GL_SUB_CODE, MCBL_HEAD_ACC_NO, MCBL_CURRENCY, " +
	            "MCBL_DEBIT_BALANCE, MCBL_CREDIT_BALANCE, MCBL_DEBIT_EQUIVALENT, MCBL_CREDIT_EQUIVALENT " +
	            "FROM MCBL WHERE REPORT_DATE = ? AND DEL_FLG = 'N'")) {
	            ps.setDate(1, sqlReportDate);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                String key = rs.getString(1) + "|" + rs.getString(2) + "|" + rs.getString(3) + "|" +
	                    rs.getString(4) + "|" + rs.getBigDecimal(5) + "|" + rs.getBigDecimal(6) + "|" +
	                    rs.getBigDecimal(7) + "|" + rs.getBigDecimal(8);
	                existingMCBLKeys.add(key);
	            }
	        }
	        
	        Set<String> existingGeneralKeys = new HashSet<>();
	        try (PreparedStatement ps = conn.prepareStatement(
	            "SELECT ACCOUNT_NO, REPORT_DATE FROM GENERAL_MASTER_TABLE WHERE REPORT_DATE = ? AND DEL_FLG = 'N'")) {
	            ps.setDate(1, sqlReportDate);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                existingGeneralKeys.add(rs.getString(1) + "|" + rs.getDate(2));
	            }
	        }
	        
	        Set<String> existingGeneralSrcKeys = new HashSet<>();
	        try (PreparedStatement ps = conn.prepareStatement(
	            "SELECT ACCOUNT_NO, REPORT_DATE FROM GENERAL_MASTER_SRC WHERE REPORT_DATE = ? AND DEL_FLG = 'N'")) {
	            ps.setDate(1, sqlReportDate);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                existingGeneralSrcKeys.add(rs.getString(1) + "|" + rs.getDate(2));
	            }
	        }
	        
	        // ============= PRE-FETCH VERSION MAP (By Report Date & Account No) =============
	        Map<String, Integer> versionMap = new HashMap<>();
	        try (PreparedStatement ps = conn.prepareStatement(
	            "SELECT MCBL_HEAD_ACC_NO, MAX(VERSION) as MAX_VERSION FROM MCBL " +
	            "WHERE REPORT_DATE = ? GROUP BY MCBL_HEAD_ACC_NO")) {
	            ps.setDate(1, sqlReportDate);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                String accountNo = rs.getString(1);
	                int maxVersion = rs.getInt(2);
	                versionMap.put(accountNo, maxVersion + 1); // Next version
	            }
	        }
	        
	        // ---------------- SQL PREPARATION ----------------
	        String insertMCBL = "INSERT INTO MCBL (MCBL_GL_CODE, MCBL_GL_SUB_CODE, MCBL_HEAD_ACC_NO, " +
	            "MCBL_DESCRIPTION, MCBL_CURRENCY, MCBL_DEBIT_BALANCE, MCBL_CREDIT_BALANCE, " +
	            "MCBL_DEBIT_EQUIVALENT, MCBL_CREDIT_EQUIVALENT, REPORT_CODE, HOME_CURRENCY, DEL_FLG, VERSION, ENTRY_USER, ENTRY_TIME, UPLOAD_DATE, REPORT_DATE, " +
	            "CUST_FLG, MODIFY_TIME, VERIFY_TIME, MODIFY_USER, VERIFY_USER, ENTRY_FLG, MODIFY_FLG, VERIFY_FLG) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Y','N', 'N')";
	        PreparedStatement insertStmt = conn.prepareStatement(insertMCBL);
	        
	        String insertGeneral = "INSERT INTO GENERAL_MASTER_TABLE (MCBL_GL_CODE, GL_SUB_HEAD_CODE, " +
	            "ACCOUNT_NO, MCBL_DESCRIPTION, CURRENCY, MCBL_DEBIT_BALANCE, MCBL_CREDIT_BALANCE, " +
	            "MCBL_DEBIT_EQUIVALENT, MCBL_CREDIT_EQUIVALENT, REPORT_CODE, DEL_FLG, VERSION, ENTRY_USER, ENTRY_TIME, UPLOAD_DATE, REPORT_DATE, MCBL_FLG) " +
	            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        PreparedStatement insertGeneralStmt = conn.prepareStatement(insertGeneral);
	        
	        String insertGeneralSrc = "INSERT INTO GENERAL_MASTER_SRC (MCBL_GL_CODE, GL_SUB_HEAD_CODE, " +
	            "ACCOUNT_NO, MCBL_DESCRIPTION, CURRENCY, MCBL_DEBIT_BALANCE, MCBL_CREDIT_BALANCE, " +
	            "MCBL_DEBIT_EQUIVALENT, MCBL_CREDIT_EQUIVALENT, REPORT_CODE, DEL_FLG, VERSION, ENTRY_USER, ENTRY_TIME, UPLOAD_DATE, REPORT_DATE, MCBL_FLG) " +
	            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        PreparedStatement insertGeneralSrcStmt = conn.prepareStatement(insertGeneralSrc);
	        
	        String insertTrack = "INSERT INTO BRRS_MCBL_ACCOUNT_TRACK " +
	            "(ID, REPORT_DATE, ACCOUNT_NO, CHANGE_TYPE, ENTRY_USER, ENTRY_TIME, REMARKS) " +
	            "VALUES (?, ?, ?, ?, ?, SYSTIMESTAMP, ?)";
	        PreparedStatement insertTrackStmt = conn.prepareStatement(insertTrack);
	        
	        // ============= ACTUAL EXCEL PROCESSING =============
	        int batchSize = 500;
	        int count = 0;
	        int skippedExcelDupes = 0;
	        int skippedExistingDupes = 0;
	        
	        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;
	            
	            String glCode = formatter.formatCellValue(row.getCell(0)).trim();
	            String glSubCode = formatter.formatCellValue(row.getCell(1)).trim();
	            String headAccNo = formatter.formatCellValue(row.getCell(2)).trim();
	            String description = formatter.formatCellValue(row.getCell(3)).trim();
	            String currency = formatter.formatCellValue(row.getCell(5)).trim();
	            
	            if (glCode.isEmpty() || glSubCode.isEmpty() || headAccNo.isEmpty() || currency.isEmpty())
	                continue;
	            
	            if (!glCode.matches("\\d+") || !glSubCode.matches("\\d+"))
	                continue;
	            
	            BigDecimal debitBal = getCellDecimal(row.getCell(6));
	            BigDecimal creditBal = getCellDecimal(row.getCell(7));
	            BigDecimal debitEq = getCellDecimal(row.getCell(8));
	            BigDecimal creditEq = getCellDecimal(row.getCell(9));
	            
	            String ReportCode = "MCBL";
	            String custFlg = isNumeric(headAccNo) ? "N" : "Y";
	            
	            // Excel duplicate check
	            String excelKey = glCode + "|" + glSubCode + "|" + headAccNo + "|" + currency + "|" +
	                description + "|" + debitBal + "|" + creditBal + "|" + debitEq + "|" + creditEq;
	            if (!processedExcelKeys.add(excelKey)) {
	                skippedExcelDupes++;
	                continue;
	            }
	            
	            // DB duplicate check
	            String dbKey = glCode + "|" + glSubCode + "|" + headAccNo + "|" + currency + "|" +
	                debitBal + "|" + creditBal + "|" + debitEq + "|" + creditEq;
	            if (existingMCBLKeys.contains(dbKey)) {
	                skippedExistingDupes++;
	                continue;
	            }
	            
	            int version = versionMap.getOrDefault(headAccNo, 1);
	            
	            // --- Insert MCBL ---
	            insertStmt.setString(1, glCode);
	            insertStmt.setString(2, glSubCode);
	            insertStmt.setString(3, headAccNo);
	            insertStmt.setString(4, description);
	            insertStmt.setString(5, "BWP");
	            insertStmt.setBigDecimal(6, debitBal);
	            insertStmt.setBigDecimal(7, creditBal);
	            insertStmt.setBigDecimal(8, debitEq);
	            insertStmt.setBigDecimal(9, creditEq);
	            insertStmt.setString(10, ReportCode);
	            insertStmt.setString(11, currency);
	            insertStmt.setString(12, "N");
	            insertStmt.setInt(13, version);
	            insertStmt.setString(14, userid);
	            insertStmt.setDate(15, new java.sql.Date(System.currentTimeMillis()));
	            insertStmt.setDate(16, new java.sql.Date(System.currentTimeMillis()));
	            insertStmt.setDate(17, sqlReportDate);
	            insertStmt.setString(18, custFlg);
	            insertStmt.setDate(19, sqlReportDate);
	            insertStmt.setDate(20, sqlReportDate);
	            insertStmt.setString(21, userid);
	            insertStmt.setString(22, userid);
	            insertStmt.addBatch();
	            
	            String generalKey = headAccNo + "|" + sqlReportDate;
	            
	            // --- Insert GENERAL_MASTER_TABLE ---
	            insertGeneralStmt.setString(1, glCode);
	            insertGeneralStmt.setString(2, glSubCode);
	            insertGeneralStmt.setString(3, headAccNo);
	            insertGeneralStmt.setString(4, description);
	            insertGeneralStmt.setString(5, currency);
	            insertGeneralStmt.setBigDecimal(6, debitBal);
	            insertGeneralStmt.setBigDecimal(7, creditBal);
	            insertGeneralStmt.setBigDecimal(8, debitEq);
	            insertGeneralStmt.setBigDecimal(9, creditEq);
	            insertGeneralStmt.setString(10, "MCBL");
	            insertGeneralStmt.setString(11, "N");
	            insertGeneralStmt.setInt(12, version);
	            insertGeneralStmt.setString(13, userid);
	            insertGeneralStmt.setDate(14, new java.sql.Date(System.currentTimeMillis()));
	            insertGeneralStmt.setDate(15, new java.sql.Date(System.currentTimeMillis()));
	            insertGeneralStmt.setDate(16, sqlReportDate);
	            insertGeneralStmt.setString(17, "Y");
	            insertGeneralStmt.addBatch();
	            
	            existingGeneralKeys.add(generalKey);
	            
	            // --- Insert GENERAL_MASTER_SRC ---
	            insertGeneralSrcStmt.setString(1, glCode);
	            insertGeneralSrcStmt.setString(2, glSubCode);
	            insertGeneralSrcStmt.setString(3, headAccNo);
	            insertGeneralSrcStmt.setString(4, description);
	            insertGeneralSrcStmt.setString(5, currency);
	            insertGeneralSrcStmt.setBigDecimal(6, debitBal);
	            insertGeneralSrcStmt.setBigDecimal(7, creditBal);
	            insertGeneralSrcStmt.setBigDecimal(8, debitEq);
	            insertGeneralSrcStmt.setBigDecimal(9, creditEq);
	            insertGeneralSrcStmt.setString(10, "MCBL");
	            insertGeneralSrcStmt.setString(11, "N");
	            insertGeneralSrcStmt.setInt(12, version);
	            insertGeneralSrcStmt.setString(13, userid);
	            insertGeneralSrcStmt.setDate(14, new java.sql.Date(System.currentTimeMillis()));
	            insertGeneralSrcStmt.setDate(15, new java.sql.Date(System.currentTimeMillis()));
	            insertGeneralSrcStmt.setDate(16, sqlReportDate);
	            insertGeneralSrcStmt.setString(17, "Y");
	            insertGeneralSrcStmt.addBatch();
	            
	            existingGeneralSrcKeys.add(generalKey);
	            currentAccounts.add(headAccNo);
	            
	            count++;
	            if (count % batchSize == 0) {
	                insertStmt.executeBatch();
	                insertGeneralStmt.executeBatch();
	                insertGeneralSrcStmt.executeBatch();
	                conn.commit();
	            }
	        }
	        
	        insertStmt.executeBatch();
	        insertGeneralStmt.executeBatch();
	        insertGeneralSrcStmt.executeBatch();
	        
	        // ============= ACCOUNT TRACKING =============
	        Set<String> newAccounts = new HashSet<>(currentAccounts);
	        newAccounts.removeAll(lastMonthAccounts);
	        
	        Set<String> missingAccounts = new HashSet<>(lastMonthAccounts);
	        missingAccounts.removeAll(currentAccounts);
	        
	        for (String acc : newAccounts) {
	            insertTrackStmt.setString(1, sequence.generateRequestUUId());
	            insertTrackStmt.setDate(2, sqlReportDate);
	            insertTrackStmt.setString(3, acc);
	            insertTrackStmt.setString(4, "ADDED");
	            insertTrackStmt.setString(5, userid);
	            insertTrackStmt.setString(6, "New Account Detected");
	            insertTrackStmt.addBatch();
	        }
	        
	        for (String acc : missingAccounts) {
	            insertTrackStmt.setString(1, sequence.generateRequestUUId());
	            insertTrackStmt.setDate(2, sqlReportDate);
	            insertTrackStmt.setString(3, acc);
	            insertTrackStmt.setString(4, "MISSED");
	            insertTrackStmt.setString(5, userid);
	            insertTrackStmt.setString(6, "Missing Account Detected");
	            insertTrackStmt.addBatch();
	        }
	        
	        insertTrackStmt.executeBatch();
	        conn.commit();
	        
	        long duration = System.currentTimeMillis() - startTime;
	        return "✅ MCBL upload completed in " + duration + " ms. " +
	            "Inserted: " + count + ", Skipped Excel duplicates: " + skippedExcelDupes +
	            ", Skipped existing DB duplicates: " + skippedExistingDupes;
	            
	    } catch (Exception e) {
	        logger.error("Error while processing MCBL Excel: {}", e.getMessage(), e);
	        return "Error occurred while reading Excel: " + e.getMessage();
	    }
	}	private boolean isNumeric(String s) {
	    if (s == null) return false;
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
					debitCell.setCellValue(rec.getMcblDebitBalance() != null ? rec.getMcblDebitBalance().doubleValue() : 0);
					debitCell.setCellStyle(balanceStyle);

					Cell creditCell = row.createCell(6);
					creditCell
							.setCellValue(rec.getMcblCreditBalance() != null ? rec.getMcblCreditBalance().doubleValue() : 0);
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
					cell9.setCellValue(rec.getReportDate() != null
							? new SimpleDateFormat("dd-MM-yyyy").format(rec.getReportDate())
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
