package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
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
import com.bornfire.brrs.entities.BFDB_Rep;
import com.bornfire.brrs.entities.GeneralMasterEntity;
import com.bornfire.brrs.entities.GeneralMasterRepo;

@Service
@Transactional
public class BFDB_Services {

	@Autowired
	SequenceGenerator sequence;

	@Autowired
	GeneralMasterRepo GeneralMasterRepos;
	
	@Autowired
	private BFDB_Rep BFDB_Reps;

	@Autowired
	private DataSource dataSource; // Inject DataSource for JDBC

	private static Logger logger = LoggerFactory.getLogger(BFDB_Services.class);

	private final ConcurrentHashMap<String, String> jobStatusStorage = new ConcurrentHashMap<>();

	@Async
	public void initializeJobStatus(String jobId, MultipartFile file, String userid, String username) {
	    jobStatusStorage.put(jobId, "PROCESSING");
	    startBFDBUploadAsync(jobId, file, userid, username);
	}

	public String startBFDBUploadAsync(String jobId, MultipartFile file, String userid, String username) {
	    logger.info("Starting BFDB upload job: {}", jobId);

	    try {
	        String resultMsg = processBFDBFile(file, userid, username);
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


    // ==========================================================
    // CORE EXCEL PROCESSING LOGIC (Renamed from the original @Transactional method)
    // The original logic is placed here, but without the Spring @Transactional annotation
    // since manual JDBC transaction management is already in place.
    // ==========================================================
    //@Transactional(propagation = Propagation.NOT_SUPPORTED) // Removed for manual control
	private String processBFDBFile(MultipartFile file, String userid, String username) throws Exception {
	    long startTime = System.currentTimeMillis();
	    int savedCount = 0, skippedCount = 0, insertedCount = 0, updatedCount = 0;
	    int batchSize = 500;
	    int commitInterval = 5000;

	    Connection conn = null;
	    PreparedStatement stmtBFDB = null;
	    PreparedStatement stmtMaster = null;
	    PreparedStatement stmtMasterSrc = null; // NEW: For GENERAL_MASTER_SRC
	    PreparedStatement stmtSelectDepBookVersion = null;
	    PreparedStatement stmtSoftDeleteDepBook = null;
	    PreparedStatement stmtSelectMasterVersion = null;
	    PreparedStatement stmtSoftDeleteMaster = null;
	    PreparedStatement stmtSelectMasterSrcVersion = null; // NEW
	    PreparedStatement stmtSoftDeleteMasterSrc = null; // NEW
	    InputStream is = null;
	    Workbook workbook = null;

	    try {
	        is = file.getInputStream();
	        workbook = WorkbookFactory.create(is);
	        conn = dataSource.getConnection();
	        conn.setAutoCommit(false);

	        if (!conn.isClosed()) {
	            conn.setNetworkTimeout(Executors.newSingleThreadExecutor(), 600000); // 10 min timeout
	        }

	        Sheet sheet = workbook.getSheetAt(0);
	        DataFormatter formatter = new DataFormatter();
	        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

	        // Version + soft delete helpers for DEP_BOOK
	        String selectDepBookVersionSql = "SELECT NVL(MAX(VERSION),0) FROM DEP_BOOK WHERE ACCOUNT_NO = ? AND REPORT_DATE = ?";
	        stmtSelectDepBookVersion = conn.prepareStatement(selectDepBookVersionSql);

	        String softDeleteDepBookSql = "UPDATE DEP_BOOK SET DEL_FLG = 'Y' " +
	                "WHERE ACCOUNT_NO = ? AND REPORT_DATE = ? AND DEL_FLG = 'N'";
	        stmtSoftDeleteDepBook = conn.prepareStatement(softDeleteDepBookSql);

	        // Version + soft delete helpers for GENERAL_MASTER_TABLE
	        String selectMasterVersionSql = "SELECT NVL(MAX(VERSION),0) FROM GENERAL_MASTER_TABLE WHERE ACCOUNT_NO = ? AND REPORT_DATE = ? AND REPORT_CODE='DEPB' ";
	        stmtSelectMasterVersion = conn.prepareStatement(selectMasterVersionSql);

	        String softDeleteMasterSql = "UPDATE GENERAL_MASTER_TABLE SET DEL_FLG = 'Y' " +
	                "WHERE ACCOUNT_NO = ? AND REPORT_DATE = ? AND DEL_FLG = 'N' AND REPORT_CODE='DEPB' ";
	        stmtSoftDeleteMaster = conn.prepareStatement(softDeleteMasterSql);

	        // NEW: Version + soft delete helpers for GENERAL_MASTER_SRC
	        String selectMasterSrcVersionSql = "SELECT NVL(MAX(VERSION),0) FROM GENERAL_MASTER_SRC WHERE ACCOUNT_NO = ? AND REPORT_DATE = ? AND REPORT_CODE='DEPB' ";
	        stmtSelectMasterSrcVersion = conn.prepareStatement(selectMasterSrcVersionSql);

	        String softDeleteMasterSrcSql = "UPDATE GENERAL_MASTER_SRC SET DEL_FLG = 'Y' " +
	                "WHERE ACCOUNT_NO = ? AND REPORT_DATE = ? AND DEL_FLG = 'N' AND REPORT_CODE='DEPB' ";
	        stmtSoftDeleteMasterSrc = conn.prepareStatement(softDeleteMasterSrcSql);

	        // DEP_BOOK insert query
	        String insertBFDB = "INSERT INTO DEP_BOOK (" +
	                "SOL_ID, CUSTOMER_ID, GENDER, ACCOUNT_NO, CUSTOMER_NAME, SCHM_CODE, SCHM_DESC, " +
	                "ACCT_OPEN_DATE, ACCT_CLOSE_DATE, BALANCE_AS_ON, CURRENCY, BAL_EQUI_TO_BWP, " +
	                "RATE_OF_INTEREST, HUNDRED, STATUS, MATURITY_DATE, GL_SUB_HEAD_CODE, GL_SUB_HEAD_DESC, " +
	                "TYPE_OF_ACCOUNTS, SEGMENT, PERIOD, EFFECTIVE_INTEREST_RATE, " +
	                "REPORT_DATE, ENTRY_TIME, MODIFY_TIME, VERIFY_TIME, ENTRY_USER, ENTRY_FLG, MODIFY_USER, VERIFY_USER, " +
	                "MODIFY_FLG, VERIFY_FLG, DEL_FLG, DEL_USER, UPLOAD_DATE, REPORT_CODE, VERSION" +
	                ") VALUES (" + String.join(",", Collections.nCopies(37, "?")) + ")";
	        stmtBFDB = conn.prepareStatement(insertBFDB);

	        // GENERAL_MASTER_TABLE insert query
	        String insertMaster = "INSERT INTO GENERAL_MASTER_TABLE (" +
	                "SOL_ID, CUSTOMER_ID, CUSTOMER_NAME, ACCOUNT_NO, GENDER, SCHM_CODE, SCHM_DESC, " +
	                "ACCT_OPEN_DATE, ACCT_CLOSE_DATE, BALANCE_AS_ON, CURRENCY, BAL_EQUI_TO_BWP, " +
	                "RATE_OF_INTEREST, HUNDRED, STATUS, MATURITY_DATE, GL_SUB_HEAD_CODE, " +
	                "GL_SUB_HEAD_DESC, TYPE_OF_ACCOUNTS, SEGMENT, PERIOD, EFFECTIVE_INTEREST_RATE, " +
	                "REPORT_DATE, ENTRY_TIME, MODIFY_TIME, VERIFY_TIME, UPLOAD_DATE, ENTRY_USER, MODIFY_USER, VERIFY_USER, " +
	                "ENTRY_FLG, MODIFY_FLG, VERIFY_FLG, DEL_FLG, BFDB_FLG, REPORT_CODE, VERSION" +
	                ") VALUES (" + String.join(",", Collections.nCopies(37, "?")) + ")";
	        stmtMaster = conn.prepareStatement(insertMaster);

	        // NEW: GENERAL_MASTER_SRC insert query (same structure as GENERAL_MASTER_TABLE)
	        String insertMasterSrc = "INSERT INTO GENERAL_MASTER_SRC (" +
	                "SOL_ID, CUSTOMER_ID, CUSTOMER_NAME, ACCOUNT_NO, GENDER, SCHM_CODE, SCHM_DESC, " +
	                "ACCT_OPEN_DATE, ACCT_CLOSE_DATE, BALANCE_AS_ON, CURRENCY, BAL_EQUI_TO_BWP, " +
	                "RATE_OF_INTEREST, HUNDRED, STATUS, MATURITY_DATE, GL_SUB_HEAD_CODE, " +
	                "GL_SUB_HEAD_DESC, TYPE_OF_ACCOUNTS, SEGMENT, PERIOD, EFFECTIVE_INTEREST_RATE, " +
	                "REPORT_DATE, ENTRY_TIME, MODIFY_TIME, VERIFY_TIME, UPLOAD_DATE, ENTRY_USER, MODIFY_USER, VERIFY_USER, " +
	                "ENTRY_FLG, MODIFY_FLG, VERIFY_FLG, DEL_FLG, BFDB_FLG, REPORT_CODE, VERSION" +
	                ") VALUES (" + String.join(",", Collections.nCopies(37, "?")) + ")";
	        stmtMasterSrc = conn.prepareStatement(insertMasterSrc);

	        int count = 0;
	        int totalProcessed = 0;

	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null || row.getLastCellNum() <= 0) continue;

	            // Skip blank rows
	            boolean emptyRow = true;
	            for (int cn = 0; cn < row.getLastCellNum(); cn++) {
	                Cell cell = row.getCell(cn);
	                if (cell != null && !formatter.formatCellValue(cell, evaluator).trim().isEmpty()) {
	                    emptyRow = false;
	                    break;
	                }
	            }
	            if (emptyRow) {
	                skippedCount++;
	                continue;
	            }

	            try {
	                // Key fields
	                String accountNo = getCellStringSafe(row, 3, formatter, evaluator);
	                java.sql.Date reportDate = getCellDateSafe(row, 22, formatter, evaluator);

	                if (accountNo == null || accountNo.trim().isEmpty() || reportDate == null) {
	                    skippedCount++;
	                    continue;
	                }

	                // Set current timestamp once for all time fields
	                java.sql.Date currentTime = new java.sql.Date(System.currentTimeMillis());

	                // 1. Handle DEP_BOOK versioning + soft delete
	                int depBookVersion = 1;
	                stmtSelectDepBookVersion.setString(1, accountNo);
	                stmtSelectDepBookVersion.setDate(2, reportDate);
	                try (ResultSet rs = stmtSelectDepBookVersion.executeQuery()) {
	                    if (rs.next()) {
	                        int maxVersion = rs.getInt(1);
	                        if (maxVersion > 0) {
	                            stmtSoftDeleteDepBook.setString(1, accountNo);
	                            stmtSoftDeleteDepBook.setDate(2, reportDate);
	                            stmtSoftDeleteDepBook.executeUpdate();
	                            depBookVersion = maxVersion + 1;
	                        }
	                    }
	                }

	                // 2. Handle GENERAL_MASTER_TABLE versioning + soft delete
	                int masterVersion = 1;
	                stmtSelectMasterVersion.setString(1, accountNo);
	                stmtSelectMasterVersion.setDate(2, reportDate);
	                try (ResultSet rs = stmtSelectMasterVersion.executeQuery()) {
	                    if (rs.next()) {
	                        int maxVersion = rs.getInt(1);
	                        if (maxVersion > 0) {
	                            stmtSoftDeleteMaster.setString(1, accountNo);
	                            stmtSoftDeleteMaster.setDate(2, reportDate);
	                            stmtSoftDeleteMaster.executeUpdate();
	                            masterVersion = maxVersion + 1;
	                        }
	                    }
	                }

	                // NEW: 3. Handle GENERAL_MASTER_SRC versioning + soft delete
	                int masterSrcVersion = 1;
	                stmtSelectMasterSrcVersion.setString(1, accountNo);
	                stmtSelectMasterSrcVersion.setDate(2, reportDate);
	                try (ResultSet rs = stmtSelectMasterSrcVersion.executeQuery()) {
	                    if (rs.next()) {
	                        int maxVersion = rs.getInt(1);
	                        if (maxVersion > 0) {
	                            stmtSoftDeleteMasterSrc.setString(1, accountNo);
	                            stmtSoftDeleteMasterSrc.setDate(2, reportDate);
	                            stmtSoftDeleteMasterSrc.executeUpdate();
	                            masterSrcVersion = maxVersion + 1;
	                        }
	                    }
	                }

	                // 4. DEP_BOOK INSERT
	                int col = 0;
	                stmtBFDB.setString(++col, getCellStringSafe(row, 0, formatter, evaluator)); // SOL_ID
	                stmtBFDB.setString(++col, getCellStringSafe(row, 1, formatter, evaluator)); // CUSTOMER_ID
	                stmtBFDB.setString(++col, getCellStringSafe(row, 2, formatter, evaluator)); // GENDER
	                stmtBFDB.setString(++col, accountNo);                                        // ACCOUNT_NO
	                stmtBFDB.setString(++col, getCellStringSafe(row, 4, formatter, evaluator)); // CUSTOMER_NAME
	                stmtBFDB.setString(++col, getCellStringSafe(row, 5, formatter, evaluator)); // SCHM_CODE
	                stmtBFDB.setString(++col, getCellStringSafe(row, 6, formatter, evaluator)); // SCHM_DESC
	                stmtBFDB.setDate(++col, getCellDateSafe(row, 7, formatter, evaluator));     // ACCT_OPEN_DATE
	                stmtBFDB.setDate(++col, getCellDateSafe(row, 8, formatter, evaluator));     // ACCT_CLOSE_DATE
	                stmtBFDB.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator));  // BALANCE_AS_ON
	                stmtBFDB.setString(++col, getCellStringSafe(row, 10, formatter, evaluator));      // CURRENCY
	                stmtBFDB.setBigDecimal(++col, getCellDecimalSafe(row, 11, formatter, evaluator)); // BAL_EQUI_TO_BWP
	                stmtBFDB.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator)); // RATE_OF_INTEREST
	                stmtBFDB.setBigDecimal(++col, getCellDecimalSafe(row, 13, formatter, evaluator)); // HUNDRED
	                stmtBFDB.setString(++col, getCellStringSafe(row, 14, formatter, evaluator));      // STATUS
	                stmtBFDB.setDate(++col, getCellDateSafe(row, 15, formatter, evaluator));          // MATURITY_DATE
	                stmtBFDB.setString(++col, getCellStringSafe(row, 16, formatter, evaluator));      // GL_SUB_HEAD_CODE
	                stmtBFDB.setString(++col, getCellStringSafe(row, 17, formatter, evaluator));      // GL_SUB_HEAD_DESC
	                stmtBFDB.setString(++col, getCellStringSafe(row, 18, formatter, evaluator));      // TYPE_OF_ACCOUNTS
	                stmtBFDB.setString(++col, getCellStringSafe(row, 19, formatter, evaluator));      // SEGMENT
	                stmtBFDB.setString(++col, getCellStringSafe(row, 20, formatter, evaluator));      // PERIOD
	                stmtBFDB.setBigDecimal(++col, getCellDecimalSafe(row, 21, formatter, evaluator)); // EFFECTIVE_INTEREST_RATE
	                stmtBFDB.setDate(++col, reportDate);                                              // REPORT_DATE
	                stmtBFDB.setDate(++col, currentTime);                                             // ENTRY_TIME
	                stmtBFDB.setDate(++col, currentTime);                                             // MODIFY_TIME
	                stmtBFDB.setDate(++col, currentTime);                                             // VERIFY_TIME
	                stmtBFDB.setString(++col, userid);                                                // ENTRY_USER
	                stmtBFDB.setString(++col, "Y");                                                   // ENTRY_FLG
	                stmtBFDB.setString(++col, userid);                                                // MODIFY_USER
	                stmtBFDB.setString(++col, userid);                                                // VERIFY_USER
	                stmtBFDB.setString(++col, "N");                                                   // MODIFY_FLG
	                stmtBFDB.setString(++col, "N");                                                   // VERIFY_FLG
	                stmtBFDB.setString(++col, "N");                                                   // DEL_FLG
	                stmtBFDB.setString(++col, "N");                                                   // DEL_USER
	                stmtBFDB.setDate(++col, currentTime);                                             // UPLOAD_DATE
	                stmtBFDB.setString(++col, "DEPB");                                                // REPORT_CODE
	                stmtBFDB.setInt(++col, depBookVersion);                                           // VERSION
	                stmtBFDB.addBatch();

	                // 5. GENERAL_MASTER_TABLE INSERT
	                col = 0;
	                stmtMaster.setString(++col, getCellStringSafe(row, 0, formatter, evaluator));    // SOL_ID
	                stmtMaster.setString(++col, getCellStringSafe(row, 1, formatter, evaluator));    // CUSTOMER_ID
	                stmtMaster.setString(++col, getCellStringSafe(row, 4, formatter, evaluator));    // CUSTOMER_NAME
	                stmtMaster.setString(++col, accountNo);                                           // ACCOUNT_NO
	                stmtMaster.setString(++col, getCellStringSafe(row, 2, formatter, evaluator));    // GENDER
	                stmtMaster.setString(++col, getCellStringSafe(row, 5, formatter, evaluator));    // SCHM_CODE
	                stmtMaster.setString(++col, getCellStringSafe(row, 6, formatter, evaluator));    // SCHM_DESC
	                stmtMaster.setDate(++col, getCellDateSafe(row, 7, formatter, evaluator));        // ACCT_OPEN_DATE
	                stmtMaster.setDate(++col, getCellDateSafe(row, 8, formatter, evaluator));        // ACCT_CLOSE_DATE
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator)); // BALANCE_AS_ON
	                stmtMaster.setString(++col, getCellStringSafe(row, 10, formatter, evaluator));   // CURRENCY
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 11, formatter, evaluator)); // BAL_EQUI_TO_BWP
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator)); // RATE_OF_INTEREST
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 13, formatter, evaluator)); // HUNDRED
	                stmtMaster.setString(++col, getCellStringSafe(row, 14, formatter, evaluator));   // STATUS
	                stmtMaster.setDate(++col, getCellDateSafe(row, 15, formatter, evaluator));       // MATURITY_DATE
	                stmtMaster.setString(++col, getCellStringSafe(row, 16, formatter, evaluator));   // GL_SUB_HEAD_CODE
	                stmtMaster.setString(++col, getCellStringSafe(row, 17, formatter, evaluator));   // GL_SUB_HEAD_DESC
	                stmtMaster.setString(++col, getCellStringSafe(row, 18, formatter, evaluator));   // TYPE_OF_ACCOUNTS
	                stmtMaster.setString(++col, getCellStringSafe(row, 19, formatter, evaluator));   // SEGMENT
	                stmtMaster.setString(++col, getCellStringSafe(row, 20, formatter, evaluator));   // PERIOD
	                stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 21, formatter, evaluator)); // EFFECTIVE_INTEREST_RATE
	                stmtMaster.setDate(++col, reportDate);                                            // REPORT_DATE
	                stmtMaster.setDate(++col, currentTime);                                           // ENTRY_TIME
	                stmtMaster.setDate(++col, currentTime);                                           // MODIFY_TIME
	                stmtMaster.setDate(++col, currentTime);                                           // VERIFY_TIME
	                stmtMaster.setDate(++col, currentTime);                                           // UPLOAD_DATE
	                stmtMaster.setString(++col, userid);                                              // ENTRY_USER
	                stmtMaster.setString(++col, userid);                                              // MODIFY_USER
	                stmtMaster.setString(++col, userid);                                              // VERIFY_USER
	                stmtMaster.setString(++col, "Y");                                                 // ENTRY_FLG
	                stmtMaster.setString(++col, "N");                                                 // MODIFY_FLG
	                stmtMaster.setString(++col, "Y");                                                 // VERIFY_FLG
	                stmtMaster.setString(++col, "N");                                                 // DEL_FLG
	                stmtMaster.setString(++col, "Y");                                                 // BFDB_FLG
	                stmtMaster.setString(++col, "DEPB");                                              // REPORT_CODE
	                stmtMaster.setInt(++col, masterVersion);                                          // VERSION
	                stmtMaster.addBatch();

	                // NEW: 6. GENERAL_MASTER_SRC INSERT (same data as GENERAL_MASTER_TABLE)
	                col = 0;
	                stmtMasterSrc.setString(++col, getCellStringSafe(row, 0, formatter, evaluator));    // SOL_ID
	                stmtMasterSrc.setString(++col, getCellStringSafe(row, 1, formatter, evaluator));    // CUSTOMER_ID
	                stmtMasterSrc.setString(++col, getCellStringSafe(row, 4, formatter, evaluator));    // CUSTOMER_NAME
	                stmtMasterSrc.setString(++col, accountNo);                                           // ACCOUNT_NO
	                stmtMasterSrc.setString(++col, getCellStringSafe(row, 2, formatter, evaluator));    // GENDER
	                stmtMasterSrc.setString(++col, getCellStringSafe(row, 5, formatter, evaluator));    // SCHM_CODE
	                stmtMasterSrc.setString(++col, getCellStringSafe(row, 6, formatter, evaluator));    // SCHM_DESC
	                stmtMasterSrc.setDate(++col, getCellDateSafe(row, 7, formatter, evaluator));        // ACCT_OPEN_DATE
	                stmtMasterSrc.setDate(++col, getCellDateSafe(row, 8, formatter, evaluator));        // ACCT_CLOSE_DATE
	                stmtMasterSrc.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator)); // BALANCE_AS_ON
	                stmtMasterSrc.setString(++col, getCellStringSafe(row, 10, formatter, evaluator));   // CURRENCY
	                stmtMasterSrc.setBigDecimal(++col, getCellDecimalSafe(row, 11, formatter, evaluator)); // BAL_EQUI_TO_BWP
	                stmtMasterSrc.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator)); // RATE_OF_INTEREST
	                stmtMasterSrc.setBigDecimal(++col, getCellDecimalSafe(row, 13, formatter, evaluator)); // HUNDRED
	                stmtMasterSrc.setString(++col, getCellStringSafe(row, 14, formatter, evaluator));   // STATUS
	                stmtMasterSrc.setDate(++col, getCellDateSafe(row, 15, formatter, evaluator));       // MATURITY_DATE
	                stmtMasterSrc.setString(++col, getCellStringSafe(row, 16, formatter, evaluator));   // GL_SUB_HEAD_CODE
	                stmtMasterSrc.setString(++col, getCellStringSafe(row, 17, formatter, evaluator));   // GL_SUB_HEAD_DESC
	                stmtMasterSrc.setString(++col, getCellStringSafe(row, 18, formatter, evaluator));   // TYPE_OF_ACCOUNTS
	                stmtMasterSrc.setString(++col, getCellStringSafe(row, 19, formatter, evaluator));   // SEGMENT
	                stmtMasterSrc.setString(++col, getCellStringSafe(row, 20, formatter, evaluator));   // PERIOD
	                stmtMasterSrc.setBigDecimal(++col, getCellDecimalSafe(row, 21, formatter, evaluator)); // EFFECTIVE_INTEREST_RATE
	                stmtMasterSrc.setDate(++col, reportDate);                                            // REPORT_DATE
	                stmtMasterSrc.setDate(++col, currentTime);                                           // ENTRY_TIME
	                stmtMasterSrc.setDate(++col, currentTime);                                           // MODIFY_TIME
	                stmtMasterSrc.setDate(++col, currentTime);                                           // VERIFY_TIME
	                stmtMasterSrc.setDate(++col, currentTime);                                           // UPLOAD_DATE
	                stmtMasterSrc.setString(++col, userid);                                              // ENTRY_USER
	                stmtMasterSrc.setString(++col, userid);                                              // MODIFY_USER
	                stmtMasterSrc.setString(++col, userid);                                              // VERIFY_USER
	                stmtMasterSrc.setString(++col, "Y");                                                 // ENTRY_FLG
	                stmtMasterSrc.setString(++col, "N");                                                 // MODIFY_FLG
	                stmtMasterSrc.setString(++col, "Y");                                                 // VERIFY_FLG
	                stmtMasterSrc.setString(++col, "N");                                                 // DEL_FLG
	                stmtMasterSrc.setString(++col, "Y");                                                 // BFDB_FLG
	                stmtMasterSrc.setString(++col, "DEPB");                                              // REPORT_CODE
	                stmtMasterSrc.setInt(++col, masterSrcVersion);                                       // VERSION
	                stmtMasterSrc.addBatch();

	                insertedCount++;
	                savedCount++;
	                count++;
	                totalProcessed++;

	                if (count % batchSize == 0) {
	                    stmtBFDB.executeBatch();
	                    stmtMaster.executeBatch();
	                    stmtMasterSrc.executeBatch(); // NEW: Execute batch for GENERAL_MASTER_SRC
	                    conn.commit();
	                    evaluator.clearAllCachedResultValues();
	                }

	                if (totalProcessed % commitInterval == 0) {
	                    conn.commit();
	                    logger.info("Progress: {} rows processed (Inserted: {})", totalProcessed, insertedCount);
	                }

	            } catch (Exception ex) {
	                skippedCount++;
	                logger.error("Skipping row {} due to error: {}", i, ex.getMessage(), ex);
	            }
	        }

	        // Final batch execution
	        stmtBFDB.executeBatch();
	        stmtMaster.executeBatch();
	        stmtMasterSrc.executeBatch(); // NEW: Execute final batch for GENERAL_MASTER_SRC
	        conn.commit();

	        long duration = System.currentTimeMillis() - startTime;
	        return String.format(
	                "✅ BFDB upload completed. Saved: %d (Inserted: %d), Skipped: %d. Time: %d ms",
	                savedCount, insertedCount, skippedCount, duration);

	    } catch (Exception e) {
	        if (conn != null) conn.rollback();
	        logger.error("❌ Error while processing BFDB Excel: {}", e.getMessage(), e);
	        throw e;
	    } finally {
	        if (stmtBFDB != null) stmtBFDB.close();
	        if (stmtMaster != null) stmtMaster.close();
	        if (stmtMasterSrc != null) stmtMasterSrc.close(); // NEW: Close statement
	        if (stmtSelectDepBookVersion != null) stmtSelectDepBookVersion.close();
	        if (stmtSoftDeleteDepBook != null) stmtSoftDeleteDepBook.close();
	        if (stmtSelectMasterVersion != null) stmtSelectMasterVersion.close();
	        if (stmtSoftDeleteMaster != null) stmtSoftDeleteMaster.close();
	        if (stmtSelectMasterSrcVersion != null) stmtSelectMasterSrcVersion.close(); // NEW: Close statement
	        if (stmtSoftDeleteMasterSrc != null) stmtSoftDeleteMasterSrc.close(); // NEW: Close statement
	        if (conn != null) conn.close();
	        if (workbook != null) workbook.close();
	        if (is != null) is.close();
	    }
	}
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

	/* ---------------- helper methods ---------------- */

	private String getCellString(Cell cell, DataFormatter formatter, FormulaEvaluator evaluator) {
		if (cell == null)
			return null;
		String value = formatter.formatCellValue(cell, evaluator).trim();
		return value.isEmpty() ? null : value;
	}

	private BigDecimal getCellDecimal(Cell cell, DataFormatter formatter, FormulaEvaluator evaluator) {
		if (cell == null)
			return null;
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
			} catch (NumberFormatException ignored) {
			}
		}
		// fallback to numeric evaluation
		try {
			CellValue cv = evaluator.evaluate(cell);
			if (cv != null && cv.getCellType() == CellType.NUMERIC) {
				return BigDecimal.valueOf(cv.getNumberValue());
			} else if (cell.getCellType() == CellType.NUMERIC) {
				return BigDecimal.valueOf(cell.getNumericCellValue());
			}
		} catch (Exception ignored) {
		}
		return null;
	}

	private java.sql.Date getCellDate(Cell cell, DataFormatter formatter, FormulaEvaluator evaluator) {
		if (cell == null)
			return null;

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
				List<String> patterns = Arrays.asList("dd-MM-yyyy", "d-M-yyyy", "yyyy-MM-dd", "dd/MM/yyyy", "M/d/yyyy",
						"dd-MMM-yyyy");
				for (String p : patterns) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat(p);
						sdf.setLenient(false);
						java.util.Date parsed = sdf.parse(formatted);
						if (parsed != null && parsed.getYear() + 1900 >= 1900) {
							return new java.sql.Date(parsed.getTime());
						}
					} catch (Exception ignored) {
					}
				}
			}

		} catch (Exception e) {
			logger.debug("date parse/eval error for cell '{}': {}", cell, e.getMessage());
		}

		// Return null if all parsing fails
		return null;
	}

	private final ConcurrentHashMap<String, byte[]> jobStorage = new ConcurrentHashMap<>();

	@Async
	public void generateDepositBookReportAsync(String jobId, String filename, String todate) {
		System.out.println("Starting Deposit Book report generation: " + filename);
		byte[] fileData = generateBFDBExcel(filename, todate);
		jobStorage.put(jobId, fileData != null ? fileData : null);
		System.out.println("Deposit Book report generation completed: " + filename);
	}

	public byte[] getReport(String jobId) {
		return jobStorage.get(jobId);
	}

	public byte[] generateBFDBExcel(String filename, String todate) {
	    try {
	        XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("Deposit_Book_Report");

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

	        // ================= Numeric / Amount Style =================
	        CellStyle numericStyle = workbook.createCellStyle();
	        numericStyle.setAlignment(HorizontalAlignment.RIGHT);
	        numericStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
	        numericStyle.setBorderTop(BorderStyle.THIN);
	        numericStyle.setBorderBottom(BorderStyle.THIN);
	        numericStyle.setBorderLeft(BorderStyle.THIN);
	        numericStyle.setBorderRight(BorderStyle.THIN);

	        // ================= General Data Style =================
	        CellStyle dataCellStyle = workbook.createCellStyle();
	        dataCellStyle.setBorderTop(BorderStyle.THIN);
	        dataCellStyle.setBorderBottom(BorderStyle.THIN);
	        dataCellStyle.setBorderLeft(BorderStyle.THIN);
	        dataCellStyle.setBorderRight(BorderStyle.THIN);

	        // ================= Header Row =================
	        String[] headers = { "Cust ID", "SOL ID", "Gender", "Account No", "Account Name", "Scheme Code",
	                "Scheme Desc", "Account Open Date", "Account Close Date", "Balance As On", "Currency",
	                "Bal Equiv to BWP", "Interest Rate", "100%", "Status", "Maturity Date", "GL Sub Head Code",
	                "GL Sub Head Desc", "Type of Accounts", "Segment", "Period", "Effective Int Rate", "Branch Name",
	                "Branch Code", "Report Date" };

	        XSSFRow headerRow = sheet.createRow(0);
	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);
	            cell.setCellStyle(headerStyle);
	            sheet.setColumnWidth(i, 5000);
	        }

	        // ================= Fetch data from DB =================
	        List<GeneralMasterEntity> dataList = GeneralMasterRepos.findDepBRecordsByReportDate(todate);

	        if (dataList != null && !dataList.isEmpty()) {
	            int rowIndex = 1;
	            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	            for (GeneralMasterEntity rec : dataList) {
	                XSSFRow row = sheet.createRow(rowIndex++);
	                int col = 0;

	                // ======= All cells with borders =======
	                createDataCell(row, col++, rec.getCustomerId(), dataCellStyle);
	                createDataCell(row, col++, rec.getSolId(), dataCellStyle);
	                createDataCell(row, col++, rec.getGender(), dataCellStyle);
	                createDataCell(row, col++, rec.getAccountNo(), dataCellStyle);
	                createDataCell(row, col++, rec.getCustomerName(), dataCellStyle);
	                createDataCell(row, col++, rec.getSchmCode(), dataCellStyle);
	                createDataCell(row, col++, rec.getSchmDesc(), dataCellStyle);
	                createDataCell(row, col++, rec.getAcctOpenDate() != null ? sdf.format(rec.getAcctOpenDate()) : "", dataCellStyle);
	                createDataCell(row, col++, rec.getAcctCloseDate() != null ? sdf.format(rec.getAcctCloseDate()) : "", dataCellStyle);

	                createNumericCell(row, col++, rec.getBalanceAsOn(), numericStyle);
	                createDataCell(row, col++, rec.getCurrency(), dataCellStyle);
	                createNumericCell(row, col++, rec.getBalEquiToBwp(), numericStyle);
	                createNumericCell(row, col++, rec.getRateOfInterest(), numericStyle);
	                createNumericCell(row, col++, rec.getHundred(), numericStyle);
	                createDataCell(row, col++, rec.getStatus(), dataCellStyle);
	                createDataCell(row, col++, rec.getMaturityDate() != null ? sdf.format(rec.getMaturityDate()) : "", dataCellStyle);
	                createDataCell(row, col++, rec.getGlSubHeadCode(), dataCellStyle);
	                createDataCell(row, col++, rec.getGlSubHeadDesc(), dataCellStyle);
	                createDataCell(row, col++, rec.getTypeOfAccounts(), dataCellStyle);
	                createDataCell(row, col++, rec.getSegment(), dataCellStyle);
	                createDataCell(row, col++, rec.getPeriod(), dataCellStyle);
	                createNumericCell(row, col++, rec.getEffectiveInterestRate(), numericStyle);
	                createDataCell(row, col++, rec.getBranchName(), dataCellStyle);
	                createDataCell(row, col++, rec.getBranchCode(), dataCellStyle);
	                createDataCell(row, col++, rec.getReportDate() != null ? sdf.format(rec.getReportDate()) : "", dataCellStyle);
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

	// ======= Helper method for text/date cells =======
	private void createDataCell(XSSFRow row, int index, String value, CellStyle style) {
	    Cell cell = row.createCell(index);
	    cell.setCellValue(value != null ? value : "");
	    cell.setCellStyle(style);
	}
	private void createNumericCell(XSSFRow row, int index, BigDecimal value, CellStyle style) {
	    Cell cell = row.createCell(index);
	    cell.setCellValue(value != null ? value.doubleValue() : 0);
	    cell.setCellStyle(style);
	}



}
