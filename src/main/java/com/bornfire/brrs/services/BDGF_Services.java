package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.bornfire.brrs.entities.BDGF_Entity;
import com.bornfire.brrs.entities.BDGF_Rep;
import com.bornfire.brrs.entities.GeneralMasterEntity;
import com.bornfire.brrs.entities.GeneralMasterRepo;

@Service
@Transactional
public class BDGF_Services {

	@Autowired
	SequenceGenerator sequence;

	@Autowired
	private BDGF_Rep BDGF_Reps;
	@Autowired
	GeneralMasterRepo GeneralMasterRepos;

	@Autowired
	private DataSource dataSource; // Inject DataSource for JDBC

	private static final Logger logger = LoggerFactory.getLogger(BDGF_Services.class);

	private final ConcurrentHashMap<String, String> jobStatusStorage = new ConcurrentHashMap<>();

	@Async
	public void initializeJobStatus(String jobId, MultipartFile file, String userid, String username) {
	    jobStatusStorage.put(jobId, "PROCESSING");
	    startBDGFUploadAsync(jobId, file, userid, username);
	}

	public String startBDGFUploadAsync(String jobId, MultipartFile file, String userid, String username) {
	    logger.info("Starting BDGF upload job: {}", jobId);

	    try {
	        String resultMsg = addBDGF(file, userid, username);
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
	public String addBDGF(MultipartFile file, String userid, String username) {
	    long startTime = System.currentTimeMillis();
	    int savedCount = 0, skippedCount = 0, insertedCount = 0;
	    int batchSize = 500;
	    
	    logger.info("Came to main method for Upload for Deposit General(BDGF)");
	    
	    try (InputStream is = file.getInputStream();
	         Workbook workbook = WorkbookFactory.create(is);
	         Connection conn = dataSource.getConnection()) {
	        
	        conn.setAutoCommit(false);
	        Sheet sheet = workbook.getSheetAt(0);
	        DataFormatter formatter = new DataFormatter();
	        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	        
	        // 🔄 Soft delete existing records in DEP_GENERAL
	        String softDeleteBDGF = "UPDATE DEP_GENERAL SET DEL_FLG = 'Y', DEL_USER = ?, " +
	                "MODIFY_DATE = ?, MODIFY_USER = ? " +
	                "WHERE ACCOUNT_NO = ? AND REPORT_DATE = ? AND DEL_FLG = 'N'";
	        PreparedStatement stmtSoftDeleteBDGF = conn.prepareStatement(softDeleteBDGF);
	        
	        // 🔄 Soft delete existing records in GENERAL_MASTER_TABLE
	        String softDeleteMaster = "UPDATE GENERAL_MASTER_TABLE SET DEL_FLG = 'Y', DEL_USER = ?, " +
	                "MODIFY_TIME = ?, MODIFY_USER = ? " +
	                "WHERE ACCOUNT_NO = ? AND REPORT_DATE = ? AND DEL_FLG = 'N'";
	        PreparedStatement stmtSoftDeleteMaster = conn.prepareStatement(softDeleteMaster);
	        
	        // 🔄 Soft delete existing records in GENERAL_MASTER_SRC
	        String softDeleteSrc = "UPDATE GENERAL_MASTER_SRC SET DEL_FLG = 'Y', DEL_USER = ?, " +
	                "MODIFY_TIME = ?, MODIFY_USER = ? " +
	                "WHERE ACCOUNT_NO = ? AND REPORT_DATE = ? AND DEL_FLG = 'N'";
	        PreparedStatement stmtSoftDeleteSrc = conn.prepareStatement(softDeleteSrc);
	        
	        // 🔍 Get max version for DEP_GENERAL
	        String getMaxVersionBDGF = "SELECT COALESCE(MAX(VERSION), 0) FROM DEP_GENERAL " +
	                "WHERE ACCOUNT_NO = ? AND REPORT_DATE = ?";
	        PreparedStatement stmtGetMaxVersionBDGF = conn.prepareStatement(getMaxVersionBDGF);
	        
	        // 🔍 Get max version for GENERAL_MASTER_TABLE
	        String getMaxVersionMaster = "SELECT COALESCE(MAX(VERSION), 0) FROM GENERAL_MASTER_TABLE " +
	                "WHERE ACCOUNT_NO = ? AND REPORT_DATE = ?";
	        PreparedStatement stmtGetMaxVersionMaster = conn.prepareStatement(getMaxVersionMaster);
	        
	        // 🔍 Get max version for GENERAL_MASTER_SRC
	        String getMaxVersionSrc = "SELECT COALESCE(MAX(VERSION), 0) FROM GENERAL_MASTER_SRC " +
	                "WHERE ACCOUNT_NO = ? AND REPORT_DATE = ?";
	        PreparedStatement stmtGetMaxVersionSrc = conn.prepareStatement(getMaxVersionSrc);
	        
	        // 🟢 Insert new record in GENERAL_MASTER_TABLE with VERSION
	        String insertMaster = "INSERT INTO GENERAL_MASTER_TABLE (SOL_ID, CUSTOMER_ID, CUSTOMER_NAME, ACCOUNT_NO, " +
	                "ACCT_OPEN_DATE, AMOUNT_DEPOSITED, CURRENCY, PERIOD, RATE_OF_INTEREST, HUNDRED, BAL_EQUI_TO_BWP, " +
	                "OUTSTANDING_BALANCE, OUSTNDNG_BAL_UGX, MATURITY_DATE, MATURITY_AMOUNT, SCHEME, CR_PREF_INT_RATE, " +
	                "SEGMENT, REFERENCE_DATE, DIFFERENCE, LIQGAP_BUCKET, MDEP2A_BUCKET, MDEP_BUCKET, PERIOD_DAYS, " +
	                "EFFECTIVE_INTEREST_RATE, REPORT_CODE, REPORT_DATE, VERSION, " +
	                "ENTRY_TIME, MODIFY_TIME, VERIFY_TIME, UPLOAD_DATE, " +
	                "ENTRY_USER, MODIFY_USER, VERIFY_USER, DEL_USER, " +
	                "ENTRY_FLG, MODIFY_FLG, VERIFY_FLG, DEL_FLG, BDGF_FLG) " +
	                "VALUES (" + String.join(",", Collections.nCopies(41, "?")) + ")";
	        PreparedStatement stmtInsertMaster = conn.prepareStatement(insertMaster);
	        
	        // 🟢 Insert new record in GENERAL_MASTER_SRC with VERSION (same structure)
	        String insertSrc = "INSERT INTO GENERAL_MASTER_SRC (SOL_ID, CUSTOMER_ID, CUSTOMER_NAME, ACCOUNT_NO, " +
	                "ACCT_OPEN_DATE, AMOUNT_DEPOSITED, CURRENCY, PERIOD, RATE_OF_INTEREST, HUNDRED, BAL_EQUI_TO_BWP, " +
	                "OUTSTANDING_BALANCE, OUSTNDNG_BAL_UGX, MATURITY_DATE, MATURITY_AMOUNT, SCHEME, CR_PREF_INT_RATE, " +
	                "SEGMENT, REFERENCE_DATE, DIFFERENCE, LIQGAP_BUCKET, MDEP2A_BUCKET, MDEP_BUCKET, PERIOD_DAYS, " +
	                "EFFECTIVE_INTEREST_RATE, REPORT_CODE, REPORT_DATE, VERSION, " +
	                "ENTRY_TIME, MODIFY_TIME, VERIFY_TIME, UPLOAD_DATE, " +
	                "ENTRY_USER, MODIFY_USER, VERIFY_USER, DEL_USER, " +
	                "ENTRY_FLG, MODIFY_FLG, VERIFY_FLG, DEL_FLG, BDGF_FLG) " +
	                "VALUES (" + String.join(",", Collections.nCopies(41, "?")) + ")";
	        PreparedStatement stmtInsertSrc = conn.prepareStatement(insertSrc);
	        
	        // 🟩 Insert SQL for DEP_GENERAL with VERSION
	        String insertSql = "INSERT INTO DEP_GENERAL (" +
	                "SOL_ID, S_NO, ACCOUNT_NO, CUSTOMER_ID, CUSTOMER_NAME, ACCT_OPEN_DATE, AMOUNT_DEPOSITED, " +
	                "CURRENCY, PERIOD, RATE_OF_INTEREST, HUNDRED, BAL_EQUI_TO_BWP, OUTSTANDING_BALANCE, " +
	                "OUSTNDNG_BAL_UGX, MATURITY_DATE, MATURITY_AMOUNT, SCHEME, CR_PREF_INT_RATE, SEGMENT, " +
	                "REFERENCE_DATE, DIFFERENCE, LIQGAP_BUCKET, MDEP2A_BUCKET, MDEP_BUCKET, PERIOD_DAYS, " +
	                "EFFECTIVE_INTEREST_RATE, REPORT_DATE, REPORT_CODE, VERSION, " +
	                "ENTRY_DATE, MODIFY_DATE, VERIFY_DATE, UPLOAD_DATE, " +
	                "ENTRY_USER, MODIFY_USER, VERIFY_USER, DEL_USER, " +
	                "ENTRY_FLG, MODIFY_FLG, VERIFY_FLG, DEL_FLG) " +
	                "VALUES (" + String.join(",", Collections.nCopies(41, "?")) + ")";
	        PreparedStatement stmtInsertBDGF = conn.prepareStatement(insertSql);
	        
	        int count = 0;
	        
	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;
	            
	            boolean emptyRow = true;
	            for (int cn = 0; cn < row.getLastCellNum(); cn++) {
	                if (!formatter.formatCellValue(row.getCell(cn), evaluator).trim().isEmpty()) {
	                    emptyRow = false;
	                    break;
	                }
	            }
	            if (emptyRow) continue;
	            
	            try {
	                String accountNo = getCellStringSafe(row, 2, formatter, evaluator);
	                java.sql.Date reportDate = getCellDateSafe(row, 26, formatter, evaluator);
	                java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
	                
	                // 🔄 Step 1: Soft delete existing active records in DEP_GENERAL
	                stmtSoftDeleteBDGF.setString(1, userid);
	                stmtSoftDeleteBDGF.setDate(2, currentDate);
	                stmtSoftDeleteBDGF.setString(3, userid);
	                stmtSoftDeleteBDGF.setString(4, accountNo);
	                stmtSoftDeleteBDGF.setDate(5, reportDate);
	                stmtSoftDeleteBDGF.executeUpdate();
	                
	                // 🔄 Step 2: Soft delete existing active records in GENERAL_MASTER_TABLE
	                stmtSoftDeleteMaster.setString(1, userid);
	                stmtSoftDeleteMaster.setDate(2, currentDate);
	                stmtSoftDeleteMaster.setString(3, userid);
	                stmtSoftDeleteMaster.setString(4, accountNo);
	                stmtSoftDeleteMaster.setDate(5, reportDate);
	                stmtSoftDeleteMaster.executeUpdate();
	                
	                // 🔄 Step 3: Soft delete existing active records in GENERAL_MASTER_SRC
	                stmtSoftDeleteSrc.setString(1, userid);
	                stmtSoftDeleteSrc.setDate(2, currentDate);
	                stmtSoftDeleteSrc.setString(3, userid);
	                stmtSoftDeleteSrc.setString(4, accountNo);
	                stmtSoftDeleteSrc.setDate(5, reportDate);
	                stmtSoftDeleteSrc.executeUpdate();
	                
	                // 🔍 Step 4: Get max version for DEP_GENERAL
	                stmtGetMaxVersionBDGF.setString(1, accountNo);
	                stmtGetMaxVersionBDGF.setDate(2, reportDate);
	                int newVersionBDGF = 1;
	                try (ResultSet rs = stmtGetMaxVersionBDGF.executeQuery()) {
	                    if (rs.next()) {
	                        newVersionBDGF = rs.getInt(1) + 1;
	                    }
	                }
	                
	                // 🔍 Step 5: Get max version for GENERAL_MASTER_TABLE
	                stmtGetMaxVersionMaster.setString(1, accountNo);
	                stmtGetMaxVersionMaster.setDate(2, reportDate);
	                int newVersionMaster = 1;
	                try (ResultSet rs = stmtGetMaxVersionMaster.executeQuery()) {
	                    if (rs.next()) {
	                        newVersionMaster = rs.getInt(1) + 1;
	                    }
	                }
	                
	                // 🔍 Step 6: Get max version for GENERAL_MASTER_SRC
	                stmtGetMaxVersionSrc.setString(1, accountNo);
	                stmtGetMaxVersionSrc.setDate(2, reportDate);
	                int newVersionSrc = 1;
	                try (ResultSet rs = stmtGetMaxVersionSrc.executeQuery()) {
	                    if (rs.next()) {
	                        newVersionSrc = rs.getInt(1) + 1;
	                    }
	                }
	                
	                // 🟩 Step 7: Insert new record into DEP_GENERAL
	                int col = 0;
	                stmtInsertBDGF.setString(++col, getCellStringSafe(row, 0, formatter, evaluator));
	                stmtInsertBDGF.setBigDecimal(++col, getCellDecimalSafe(row, 1, formatter, evaluator));
	                stmtInsertBDGF.setString(++col, accountNo);
	                stmtInsertBDGF.setString(++col, getCellStringSafe(row, 3, formatter, evaluator));
	                stmtInsertBDGF.setString(++col, getCellStringSafe(row, 4, formatter, evaluator));
	                stmtInsertBDGF.setDate(++col, getCellDateSafe(row, 5, formatter, evaluator));
	                stmtInsertBDGF.setBigDecimal(++col, getCellDecimalSafe(row, 6, formatter, evaluator));
	                stmtInsertBDGF.setString(++col, getCellStringSafe(row, 7, formatter, evaluator));
	                stmtInsertBDGF.setString(++col, getCellStringSafe(row, 8, formatter, evaluator));
	                stmtInsertBDGF.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator));
	                stmtInsertBDGF.setBigDecimal(++col, getCellDecimalSafe(row, 10, formatter, evaluator));
	                stmtInsertBDGF.setBigDecimal(++col, getCellDecimalSafe(row, 11, formatter, evaluator));
	                stmtInsertBDGF.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator));
	                stmtInsertBDGF.setBigDecimal(++col, getCellDecimalSafe(row, 13, formatter, evaluator));
	                stmtInsertBDGF.setDate(++col, getCellDateSafe(row, 14, formatter, evaluator));
	                stmtInsertBDGF.setBigDecimal(++col, getCellDecimalSafe(row, 15, formatter, evaluator));
	                stmtInsertBDGF.setString(++col, getCellStringSafe(row, 16, formatter, evaluator));
	                stmtInsertBDGF.setBigDecimal(++col, getCellDecimalSafe(row, 17, formatter, evaluator));
	                stmtInsertBDGF.setString(++col, getCellStringSafe(row, 18, formatter, evaluator));
	                stmtInsertBDGF.setDate(++col, getCellDateSafe(row, 19, formatter, evaluator));
	                stmtInsertBDGF.setBigDecimal(++col, getCellDecimalSafe(row, 20, formatter, evaluator));
	                stmtInsertBDGF.setString(++col, getCellStringSafe(row, 21, formatter, evaluator));
	                stmtInsertBDGF.setString(++col, getCellStringSafe(row, 22, formatter, evaluator));
	                stmtInsertBDGF.setString(++col, getCellStringSafe(row, 23, formatter, evaluator));
	                stmtInsertBDGF.setBigDecimal(++col, getCellDecimalSafe(row, 24, formatter, evaluator));
	                stmtInsertBDGF.setBigDecimal(++col, getCellDecimalSafe(row, 25, formatter, evaluator));
	                stmtInsertBDGF.setDate(++col, reportDate);
	                stmtInsertBDGF.setString(++col, "DEPG");
	                stmtInsertBDGF.setInt(++col, newVersionBDGF);
	                stmtInsertBDGF.setDate(++col, currentDate);
	                stmtInsertBDGF.setDate(++col, currentDate);
	                stmtInsertBDGF.setDate(++col, currentDate);
	                stmtInsertBDGF.setDate(++col, currentDate);
	                stmtInsertBDGF.setString(++col, userid);
	                stmtInsertBDGF.setString(++col, userid);
	                stmtInsertBDGF.setString(++col, userid);
	                stmtInsertBDGF.setString(++col, "N");
	                stmtInsertBDGF.setString(++col, "Y");
	                stmtInsertBDGF.setString(++col, "N");
	                stmtInsertBDGF.setString(++col, "Y");
	                stmtInsertBDGF.setString(++col, "N");
	                stmtInsertBDGF.addBatch();
	                
	                // 🟢 Step 8: Insert new record in GENERAL_MASTER_TABLE with VERSION
	                col = 0;
	                stmtInsertMaster.setString(++col, getCellStringSafe(row, 0, formatter, evaluator));
	                stmtInsertMaster.setString(++col, getCellStringSafe(row, 3, formatter, evaluator));
	                stmtInsertMaster.setString(++col, getCellStringSafe(row, 4, formatter, evaluator));
	                stmtInsertMaster.setString(++col, accountNo);
	                stmtInsertMaster.setDate(++col, getCellDateSafe(row, 5, formatter, evaluator));
	                stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 6, formatter, evaluator));
	                stmtInsertMaster.setString(++col, getCellStringSafe(row, 7, formatter, evaluator));
	                stmtInsertMaster.setString(++col, getCellStringSafe(row, 8, formatter, evaluator));
	                stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator));
	                stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 10, formatter, evaluator));
	                stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 11, formatter, evaluator));
	                stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator));
	                stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 13, formatter, evaluator));
	                stmtInsertMaster.setDate(++col, getCellDateSafe(row, 14, formatter, evaluator));
	                stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 15, formatter, evaluator));
	                stmtInsertMaster.setString(++col, getCellStringSafe(row, 16, formatter, evaluator));
	                stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 17, formatter, evaluator));
	                stmtInsertMaster.setString(++col, getCellStringSafe(row, 18, formatter, evaluator));
	                stmtInsertMaster.setDate(++col, getCellDateSafe(row, 19, formatter, evaluator));
	                stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 20, formatter, evaluator));
	                stmtInsertMaster.setString(++col, getCellStringSafe(row, 21, formatter, evaluator));
	                stmtInsertMaster.setString(++col, getCellStringSafe(row, 22, formatter, evaluator));
	                stmtInsertMaster.setString(++col, getCellStringSafe(row, 23, formatter, evaluator));
	                stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 24, formatter, evaluator));
	                stmtInsertMaster.setBigDecimal(++col, getCellDecimalSafe(row, 25, formatter, evaluator));
	                stmtInsertMaster.setString(++col, "DEPG");
	                stmtInsertMaster.setDate(++col, reportDate);
	                stmtInsertMaster.setInt(++col, newVersionMaster);
	                stmtInsertMaster.setDate(++col, currentDate);
	                stmtInsertMaster.setDate(++col, currentDate);
	                stmtInsertMaster.setDate(++col, currentDate);
	                stmtInsertMaster.setDate(++col, currentDate);
	                stmtInsertMaster.setString(++col, userid);
	                stmtInsertMaster.setString(++col, userid);
	                stmtInsertMaster.setString(++col, userid);
	                stmtInsertMaster.setString(++col, "N");
	                stmtInsertMaster.setString(++col, "Y");
	                stmtInsertMaster.setString(++col, "N");
	                stmtInsertMaster.setString(++col, "Y");
	                stmtInsertMaster.setString(++col, "N");
	                stmtInsertMaster.setString(++col, "Y");
	                stmtInsertMaster.addBatch();
	                
	                // 🟢 Step 9: Insert new record in GENERAL_MASTER_SRC with VERSION (SAME DATA)
	                col = 0;
	                stmtInsertSrc.setString(++col, getCellStringSafe(row, 0, formatter, evaluator));
	                stmtInsertSrc.setString(++col, getCellStringSafe(row, 3, formatter, evaluator));
	                stmtInsertSrc.setString(++col, getCellStringSafe(row, 4, formatter, evaluator));
	                stmtInsertSrc.setString(++col, accountNo);
	                stmtInsertSrc.setDate(++col, getCellDateSafe(row, 5, formatter, evaluator));
	                stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 6, formatter, evaluator));
	                stmtInsertSrc.setString(++col, getCellStringSafe(row, 7, formatter, evaluator));
	                stmtInsertSrc.setString(++col, getCellStringSafe(row, 8, formatter, evaluator));
	                stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator));
	                stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 10, formatter, evaluator));
	                stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 11, formatter, evaluator));
	                stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator));
	                stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 13, formatter, evaluator));
	                stmtInsertSrc.setDate(++col, getCellDateSafe(row, 14, formatter, evaluator));
	                stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 15, formatter, evaluator));
	                stmtInsertSrc.setString(++col, getCellStringSafe(row, 16, formatter, evaluator));
	                stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 17, formatter, evaluator));
	                stmtInsertSrc.setString(++col, getCellStringSafe(row, 18, formatter, evaluator));
	                stmtInsertSrc.setDate(++col, getCellDateSafe(row, 19, formatter, evaluator));
	                stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 20, formatter, evaluator));
	                stmtInsertSrc.setString(++col, getCellStringSafe(row, 21, formatter, evaluator));
	                stmtInsertSrc.setString(++col, getCellStringSafe(row, 22, formatter, evaluator));
	                stmtInsertSrc.setString(++col, getCellStringSafe(row, 23, formatter, evaluator));
	                stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 24, formatter, evaluator));
	                stmtInsertSrc.setBigDecimal(++col, getCellDecimalSafe(row, 25, formatter, evaluator));
	                stmtInsertSrc.setString(++col, "DEPG");
	                stmtInsertSrc.setDate(++col, reportDate);
	                stmtInsertSrc.setInt(++col, newVersionSrc);
	                stmtInsertSrc.setDate(++col, currentDate);
	                stmtInsertSrc.setDate(++col, currentDate);
	                stmtInsertSrc.setDate(++col, currentDate);
	                stmtInsertSrc.setDate(++col, currentDate);
	                stmtInsertSrc.setString(++col, userid);
	                stmtInsertSrc.setString(++col, userid);
	                stmtInsertSrc.setString(++col, userid);
	                stmtInsertSrc.setString(++col, "N");
	                stmtInsertSrc.setString(++col, "Y");
	                stmtInsertSrc.setString(++col, "N");
	                stmtInsertSrc.setString(++col, "Y");
	                stmtInsertSrc.setString(++col, "N");
	                stmtInsertSrc.setString(++col, "Y");
	                stmtInsertSrc.addBatch();
	                
	                insertedCount++;
	                savedCount++;
	                count++;
	                
	                if (count % batchSize == 0) {
	                    stmtInsertBDGF.executeBatch();
	                    stmtInsertMaster.executeBatch();
	                    stmtInsertSrc.executeBatch();  // Execute batch for SRC table
	                    conn.commit();
	                    evaluator.clearAllCachedResultValues();
	                }
	                
	            } catch (Exception ex) {
	                skippedCount++;
	                logger.error("Skipping row {} due to error: {}", i, ex.getMessage(), ex);
	            }
	        }
	        
	        // Execute remaining batches
	        stmtInsertBDGF.executeBatch();
	        stmtInsertMaster.executeBatch();
	        stmtInsertSrc.executeBatch();  // Execute final batch for SRC table
	        conn.commit();
	        
	        long duration = System.currentTimeMillis() - startTime;
	        return String.format("✅ BDGF Upload complete. Saved: %d (Inserted: %d), Skipped: %d. Time: %d ms",
	                savedCount, insertedCount, skippedCount, duration);
	                
	    } catch (Exception e) {
	        logger.error("❌ Error while processing BDGF Excel: {}", e.getMessage(), e);
	        return "Error while reading Excel: " + e.getMessage();
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

			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC &&
				    DateUtil.isCellDateFormatted(cell)) {

				    return new java.sql.Date(cell.getDateCellValue().getTime());
				}
 else {
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
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC &&
				    DateUtil.isCellDateFormatted(cell)) 
				return cell.getDateCellValue();
			String text = f.formatCellValue(cell, e);
			if (text.isEmpty())
				return null;
			return new SimpleDateFormat("dd-MM-yyyy").parse(text);
		} catch (Exception ex) {
			return null;
		}
	}

	// 🔹 Helper methods to safely parse Excel cells
	private String getString(Cell cell) {
		if (cell == null)
			return null;
		cell.setCellType(CellType.STRING);
		return cell.getStringCellValue().trim();
	}

	private BigDecimal getBigDecimal(Cell cell) {
		if (cell == null)
			return null;
		try {
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
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
		if (cell == null)
			return null;
		try {
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
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
		if (cell == null)
			return null;
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
			return cell.getDateCellValue();
		} else {
			try {
				String str = cell.toString().trim();
				if (str.isEmpty())
					return null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.parse(str);
			} catch (Exception e) {
				return null;
			}
		}
	}

	private boolean isRowEmpty(Row row) {
		if (row == null)
			return true;
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
				String value = cell.toString().trim();
				if (!value.isEmpty())
					return false;
			}
		}
		return true;
	}

	private final ConcurrentHashMap<String, byte[]> jobStorage = new ConcurrentHashMap<>();

	@Async
	public void generateDepositGeneralReportAsync(String jobId, String filename, String todate) {
		System.out.println("Starting Deposit General report generation: " + filename);
		byte[] fileData = generateBDGFExcel(filename, todate);
		jobStorage.put(jobId, fileData != null ? fileData : null);
		System.out.println("Deposit General report generation completed: " + filename);
	}

	public byte[] getReport(String jobId) {
		return jobStorage.get(jobId);
	}

	public byte[] generateBDGFExcel(String filename, String todate) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Deposit_General_Report");

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
			String[] headers = {"SOL ID", "Account No", "Customer ID", "Customer Name", "Open Date",
					"Amount Deposited", "Currency", "Period", "Rate of Interest", "100%", "Bal Equiv to BWP",
					"Outstanding Balance", "Outstanding Balance UGX", "Maturity Date", "Maturity Amount", "Scheme",
					"CR Pref Int Rate", "Segment", "Reference Date", "Difference", "Days", "Period Days",
					"Effective Int Rate", "Branch Name", "Branch Code", "Report Date" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(headerStyle);
				sheet.setColumnWidth(i, 5000);
			}

			// ================= Fetch data from DB =================
			List<GeneralMasterEntity> dataList = GeneralMasterRepos.findDepGRecordsByReportDate(todate);

			if (dataList != null && !dataList.isEmpty()) {
				int rowIndex = 1;
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

				for (GeneralMasterEntity rec : dataList) {
					XSSFRow row = sheet.createRow(rowIndex++);
					int col = 0;

					// All numeric/text/date cells use either numericStyle or dataCellStyle
					//createNumericCell(row, col++, rec.getS_no(), dataCellStyle);
					createTextCell(row, col++, rec.getSolId(), dataCellStyle);
					createTextCell(row, col++, rec.getAccountNo(), dataCellStyle);
					createTextCell(row, col++, rec.getCustomerId(), dataCellStyle);
					createTextCell(row, col++, rec.getCustomerName(), dataCellStyle);
					createDateCell(row, col++, rec.getAcctOpenDate(), sdf, dataCellStyle);

					createNumericCell(row, col++, rec.getAmountDeposited(), numericStyle);
					createTextCell(row, col++, rec.getCurrency(), dataCellStyle);
					createTextCell(row, col++, rec.getPeriod(), dataCellStyle);
					createNumericCell(row, col++, rec.getRateOfInterest(), numericStyle);
					createNumericCell(row, col++, rec.getHundred(), numericStyle);
					createNumericCell(row, col++, rec.getBalEquiToBwp(), numericStyle);
					createNumericCell(row, col++, rec.getOutstandingBalance(), numericStyle);
					createNumericCell(row, col++, rec.getOustndngBalUgx(), numericStyle);
					createDateCell(row, col++, rec.getMaturityDate(), sdf, dataCellStyle);
					createNumericCell(row, col++, rec.getMaturityAmount(), numericStyle);
					createTextCell(row, col++, rec.getScheme(), dataCellStyle);
					createNumericCell(row, col++, rec.getCrPrefIntRate(), numericStyle);
					createTextCell(row, col++, rec.getSegment(), dataCellStyle);
					createDateCell(row, col++, rec.getReferenceDate(), sdf, dataCellStyle);
					createNumericCell(row, col++, rec.getDifference(), numericStyle);
					createNumericCell(row, col++, rec.getDays(), numericStyle);
					createNumericCell(row, col++, rec.getPeriodDays(), numericStyle);
					createNumericCell(row, col++, rec.getEffectiveInterestRate(), numericStyle);
					createTextCell(row, col++, rec.getBranchName(), dataCellStyle);
					createTextCell(row, col++, rec.getBranchCode(), dataCellStyle);
					createDateCell(row, col++, rec.getReportDate(), sdf, dataCellStyle);
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

	// Numeric cell helper
	private void createNumericCell(XSSFRow row, int index, BigDecimal value, CellStyle style) {
		Cell cell = row.createCell(index);
		cell.setCellValue(value != null ? value.doubleValue() : 0);
		cell.setCellStyle(style);
	}

	// Text cell helper
	private void createTextCell(XSSFRow row, int index, String value, CellStyle style) {
		Cell cell = row.createCell(index);
		cell.setCellValue(value != null ? value : "");
		cell.setCellStyle(style);
	}

	// Date cell helper
	private void createDateCell(XSSFRow row, int index, Date value, SimpleDateFormat sdf, CellStyle style) {
		Cell cell = row.createCell(index);
		cell.setCellValue(value != null ? sdf.format(value) : "");
		cell.setCellStyle(style);
	}

}
