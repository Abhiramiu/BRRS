package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.brrs.config.SequenceGenerator;
import com.bornfire.brrs.entities.BFDB_Entity;
import com.bornfire.brrs.entities.BFDB_Rep;
import com.bornfire.brrs.entities.BrrsGeneralMasterEntity;
import com.bornfire.brrs.entities.BrrsGeneralMasterRepo;
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
	    PreparedStatement stmtUpdateMaster = null;
	    PreparedStatement stmtDeleteBFDB = null;
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

	        // 🟩 DELETE old data in BFDB only
	        String deleteBFDB = "DELETE FROM BRRS_BFDB WHERE ACCOUNT_NO = ? AND REPORT_DATE = ?";
	        stmtDeleteBFDB = conn.prepareStatement(deleteBFDB);

	        // 🟩 BFDB insert query
	        String insertBFDB = "INSERT INTO BRRS_BFDB (SOL_ID, CUSTOMER_ID, GENDER, ACCOUNT_NO, CUSTOMER_NAME, SCHM_CODE, SCHM_DESC, "
	                + "ACCT_OPEN_DATE, ACCT_CLOSE_DATE, BALANCE_AS_ON, CURRENCY, BAL_EQUI_TO_BWP, RATE_OF_INTEREST, HUNDRED, STATUS, "
	                + "MATURITY_DATE, GL_SUB_HEAD_CODE, GL_SUB_HEAD_DESC, TYPE_OF_ACCOUNTS, SEGMENT, PERIOD, EFFECTIVE_INTEREST_RATE, "
	                + "REPORT_DATE, ENTRY_DATE, ENTRY_USER, ENTRY_FLG, DEL_FLG) "
	                + "VALUES (" + String.join(",", Collections.nCopies(27, "?")) + ")";
	        stmtBFDB = conn.prepareStatement(insertBFDB);

	        // 🟩 MASTER update query
	        String updateMaster = "UPDATE GENERAL_MASTER_TABLE SET "
	                + "SOL_ID=?, CUSTOMER_ID=?, CUSTOMER_NAME=?, GENDER=?, SCHM_CODE=?, SCHM_DESC=?, "
	                + "ACCT_OPEN_DATE=?, ACCT_CLOSE_DATE=?, BALANCE_AS_ON=?, CURRENCY=?, BAL_EQUI_TO_BWP=?, "
	                + "RATE_OF_INTEREST=?, HUNDRED=?, STATUS=?, MATURITY_DATE=?, GL_SUB_HEAD_CODE=?, "
	                + "GL_SUB_HEAD_DESC=?, TYPE_OF_ACCOUNTS=?, SEGMENT=?, PERIOD=?, EFFECTIVE_INTEREST_RATE=?, "
	                + "ENTRY_DATE=?, ENTRY_USER=?, ENTRY_FLG=?, BFDB_FLG=? "
	                + "WHERE ACCOUNT_NO=? AND REPORT_DATE=?";
	        stmtUpdateMaster = conn.prepareStatement(updateMaster);

	        // 🟢 MASTER insert query (when record doesn't exist)
	        String insertMaster = "INSERT INTO GENERAL_MASTER_TABLE (ID, SOL_ID, CUSTOMER_ID, CUSTOMER_NAME, ACCOUNT_NO, GENDER, SCHM_CODE, SCHM_DESC, "
	                + "ACCT_OPEN_DATE, ACCT_CLOSE_DATE, BALANCE_AS_ON, CURRENCY, BAL_EQUI_TO_BWP, RATE_OF_INTEREST, HUNDRED, STATUS, "
	                + "MATURITY_DATE, GL_SUB_HEAD_CODE, GL_SUB_HEAD_DESC, TYPE_OF_ACCOUNTS, SEGMENT, PERIOD, EFFECTIVE_INTEREST_RATE, "
	                + "REPORT_DATE, ENTRY_DATE, ENTRY_USER, ENTRY_FLG, DEL_FLG, BFDB_FLG) "
	                + "VALUES (" + String.join(",", Collections.nCopies(29, "?")) + ")";
	        stmtMaster = conn.prepareStatement(insertMaster);

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

	                // Delete duplicate from BFDB only
	                stmtDeleteBFDB.setString(1, accountNo);
	                stmtDeleteBFDB.setDate(2, reportDate);
	                stmtDeleteBFDB.executeUpdate();

	                // --- BFDB insert ---
	                int col = 0;
	                stmtBFDB.setString(++col, getCellStringSafe(row, 0, formatter, evaluator)); // SOL_ID
	                stmtBFDB.setString(++col, getCellStringSafe(row, 1, formatter, evaluator)); // CUSTOMER_ID
	                stmtBFDB.setString(++col, getCellStringSafe(row, 2, formatter, evaluator)); // GENDER
	                stmtBFDB.setString(++col, accountNo);
	                stmtBFDB.setString(++col, getCellStringSafe(row, 4, formatter, evaluator)); // CUSTOMER_NAME
	                stmtBFDB.setString(++col, getCellStringSafe(row, 5, formatter, evaluator)); // SCHM_CODE
	                stmtBFDB.setString(++col, getCellStringSafe(row, 6, formatter, evaluator)); // SCHM_DESC
	                stmtBFDB.setDate(++col, getCellDateSafe(row, 7, formatter, evaluator)); // ACCT_OPEN_DATE
	                stmtBFDB.setDate(++col, getCellDateSafe(row, 8, formatter, evaluator)); // ACCT_CLOSE_DATE
	                stmtBFDB.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator)); // BALANCE_AS_ON
	                stmtBFDB.setString(++col, getCellStringSafe(row, 10, formatter, evaluator)); // CURRENCY
	                stmtBFDB.setBigDecimal(++col, getCellDecimalSafe(row, 11, formatter, evaluator)); // BAL_EQUI_TO_BWP
	                stmtBFDB.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator)); // RATE_OF_INTEREST
	                stmtBFDB.setBigDecimal(++col, getCellDecimalSafe(row, 13, formatter, evaluator)); // HUNDRED
	                stmtBFDB.setString(++col, getCellStringSafe(row, 14, formatter, evaluator)); // STATUS
	                stmtBFDB.setDate(++col, getCellDateSafe(row, 15, formatter, evaluator)); // MATURITY_DATE
	                stmtBFDB.setString(++col, getCellStringSafe(row, 16, formatter, evaluator)); // GL_SUB_HEAD_CODE
	                stmtBFDB.setString(++col, getCellStringSafe(row, 17, formatter, evaluator)); // GL_SUB_HEAD_DESC
	                stmtBFDB.setString(++col, getCellStringSafe(row, 18, formatter, evaluator)); // TYPE_OF_ACCOUNTS
	                stmtBFDB.setString(++col, getCellStringSafe(row, 19, formatter, evaluator)); // SEGMENT
	                stmtBFDB.setString(++col, getCellStringSafe(row, 20, formatter, evaluator)); // PERIOD
	                stmtBFDB.setBigDecimal(++col, getCellDecimalSafe(row, 21, formatter, evaluator)); // EFFECTIVE_INTEREST_RATE
	                stmtBFDB.setDate(++col, reportDate);
	                stmtBFDB.setDate(++col, new java.sql.Date(System.currentTimeMillis()));
	                stmtBFDB.setString(++col, userid);
	                stmtBFDB.setString(++col, "Y");
	                stmtBFDB.setString(++col, "N");
	                stmtBFDB.addBatch();

	                // 🟢 Try UPDATE first
	                col = 0;
	                stmtUpdateMaster.setString(++col, getCellStringSafe(row, 0, formatter, evaluator)); // SOL_ID
	                stmtUpdateMaster.setString(++col, getCellStringSafe(row, 1, formatter, evaluator)); // CUSTOMER_ID
	                stmtUpdateMaster.setString(++col, getCellStringSafe(row, 4, formatter, evaluator)); // CUSTOMER_NAME
	                stmtUpdateMaster.setString(++col, getCellStringSafe(row, 2, formatter, evaluator)); // GENDER
	                stmtUpdateMaster.setString(++col, getCellStringSafe(row, 5, formatter, evaluator)); // SCHM_CODE
	                stmtUpdateMaster.setString(++col, getCellStringSafe(row, 6, formatter, evaluator)); // SCHM_DESC
	                stmtUpdateMaster.setDate(++col, getCellDateSafe(row, 7, formatter, evaluator)); // ACCT_OPEN_DATE
	                stmtUpdateMaster.setDate(++col, getCellDateSafe(row, 8, formatter, evaluator)); // ACCT_CLOSE_DATE
	                stmtUpdateMaster.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator)); // BALANCE_AS_ON
	                stmtUpdateMaster.setString(++col, getCellStringSafe(row, 10, formatter, evaluator)); // CURRENCY
	                stmtUpdateMaster.setBigDecimal(++col, getCellDecimalSafe(row, 11, formatter, evaluator)); // BAL_EQUI_TO_BWP
	                stmtUpdateMaster.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator)); // RATE_OF_INTEREST
	                stmtUpdateMaster.setBigDecimal(++col, getCellDecimalSafe(row, 13, formatter, evaluator)); // HUNDRED
	                stmtUpdateMaster.setString(++col, getCellStringSafe(row, 14, formatter, evaluator)); // STATUS
	                stmtUpdateMaster.setDate(++col, getCellDateSafe(row, 15, formatter, evaluator)); // MATURITY_DATE
	                stmtUpdateMaster.setString(++col, getCellStringSafe(row, 16, formatter, evaluator)); // GL_SUB_HEAD_CODE
	                stmtUpdateMaster.setString(++col, getCellStringSafe(row, 17, formatter, evaluator)); // GL_SUB_HEAD_DESC
	                stmtUpdateMaster.setString(++col, getCellStringSafe(row, 18, formatter, evaluator)); // TYPE_OF_ACCOUNTS
	                stmtUpdateMaster.setString(++col, getCellStringSafe(row, 19, formatter, evaluator)); // SEGMENT
	                stmtUpdateMaster.setString(++col, getCellStringSafe(row, 20, formatter, evaluator)); // PERIOD
	                stmtUpdateMaster.setBigDecimal(++col, getCellDecimalSafe(row, 21, formatter, evaluator)); // EFFECTIVE_INTEREST_RATE
	                stmtUpdateMaster.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // ENTRY_DATE
	                stmtUpdateMaster.setString(++col, userid); // ENTRY_USER
	                stmtUpdateMaster.setString(++col, "Y"); // ENTRY_FLG
	                stmtUpdateMaster.setString(++col, "Y"); // BFDB_FLG
	                stmtUpdateMaster.setString(++col, accountNo); // WHERE ACCOUNT_NO
	                stmtUpdateMaster.setDate(++col, reportDate); // WHERE REPORT_DATE

	                int updatedRows = stmtUpdateMaster.executeUpdate();

	                // 🟢 If UPDATE didn't affect any rows, INSERT new record
	                if (updatedRows == 0) {
	                    col = 0;
	                    stmtMaster.setString(++col, sequence.generateRequestUUId()); // ID
	                    stmtMaster.setString(++col, getCellStringSafe(row, 0, formatter, evaluator)); // SOL_ID
	                    stmtMaster.setString(++col, getCellStringSafe(row, 1, formatter, evaluator)); // CUSTOMER_ID
	                    stmtMaster.setString(++col, getCellStringSafe(row, 4, formatter, evaluator)); // CUSTOMER_NAME
	                    stmtMaster.setString(++col, accountNo); // ACCOUNT_NO
	                    stmtMaster.setString(++col, getCellStringSafe(row, 2, formatter, evaluator)); // GENDER
	                    stmtMaster.setString(++col, getCellStringSafe(row, 5, formatter, evaluator)); // SCHM_CODE
	                    stmtMaster.setString(++col, getCellStringSafe(row, 6, formatter, evaluator)); // SCHM_DESC
	                    stmtMaster.setDate(++col, getCellDateSafe(row, 7, formatter, evaluator)); // ACCT_OPEN_DATE
	                    stmtMaster.setDate(++col, getCellDateSafe(row, 8, formatter, evaluator)); // ACCT_CLOSE_DATE
	                    stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator)); // BALANCE_AS_ON
	                    stmtMaster.setString(++col, getCellStringSafe(row, 10, formatter, evaluator)); // CURRENCY
	                    stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 11, formatter, evaluator)); // BAL_EQUI_TO_BWP
	                    stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator)); // RATE_OF_INTEREST
	                    stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 13, formatter, evaluator)); // HUNDRED
	                    stmtMaster.setString(++col, getCellStringSafe(row, 14, formatter, evaluator)); // STATUS
	                    stmtMaster.setDate(++col, getCellDateSafe(row, 15, formatter, evaluator)); // MATURITY_DATE
	                    stmtMaster.setString(++col, getCellStringSafe(row, 16, formatter, evaluator)); // GL_SUB_HEAD_CODE
	                    stmtMaster.setString(++col, getCellStringSafe(row, 17, formatter, evaluator)); // GL_SUB_HEAD_DESC
	                    stmtMaster.setString(++col, getCellStringSafe(row, 18, formatter, evaluator)); // TYPE_OF_ACCOUNTS
	                    stmtMaster.setString(++col, getCellStringSafe(row, 19, formatter, evaluator)); // SEGMENT
	                    stmtMaster.setString(++col, getCellStringSafe(row, 20, formatter, evaluator)); // PERIOD
	                    stmtMaster.setBigDecimal(++col, getCellDecimalSafe(row, 21, formatter, evaluator)); // EFFECTIVE_INTEREST_RATE
	                    stmtMaster.setDate(++col, reportDate); // REPORT_DATE
	                    stmtMaster.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // ENTRY_DATE
	                    stmtMaster.setString(++col, userid); // ENTRY_USER
	                    stmtMaster.setString(++col, "Y"); // ENTRY_FLG
	                    stmtMaster.setString(++col, "N"); // DEL_FLG
	                    stmtMaster.setString(++col, "Y"); // BFDB_FLG
	                    stmtMaster.addBatch();
	                    insertedCount++;
	                } else {
	                    updatedCount++;
	                }

	                savedCount++;
	                count++;
	                totalProcessed++;

	                if (count % batchSize == 0) {
	                    stmtBFDB.executeBatch();
	                    stmtMaster.executeBatch();
	                    conn.commit();
	                    evaluator.clearAllCachedResultValues();
	                }

	                if (totalProcessed % commitInterval == 0) {
	                    conn.commit();
	                    logger.info("Progress: {} rows processed (Updated: {}, Inserted: {})", totalProcessed, updatedCount, insertedCount);
	                }

	            } catch (Exception ex) {
	                skippedCount++;
	                logger.error("Skipping row {} due to error: {}", i, ex.getMessage(), ex);
	            }
	        }

	        stmtBFDB.executeBatch();
	        stmtMaster.executeBatch();
	        conn.commit();

	        long duration = System.currentTimeMillis() - startTime;
	        return String.format("✅ BFDB upload completed. Saved: %d (Updated: %d, Inserted: %d), Skipped: %d. Time: %d ms", 
	                savedCount, updatedCount, insertedCount, skippedCount, duration);

	    } catch (Exception e) {
	        if (conn != null) conn.rollback();
	        logger.error("❌ Error while processing BFDB Excel: {}", e.getMessage(), e);
	        throw e;
	    } finally {
	        if (stmtBFDB != null) stmtBFDB.close();
	        if (stmtMaster != null) stmtMaster.close();
	        if (stmtUpdateMaster != null) stmtUpdateMaster.close();
	        if (stmtDeleteBFDB != null) stmtDeleteBFDB.close();
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
	        List<BFDB_Entity> dataList = BFDB_Reps.findRecordsByReportDate(todate);

	        if (dataList != null && !dataList.isEmpty()) {
	            int rowIndex = 1;
	            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	            for (BFDB_Entity rec : dataList) {
	                XSSFRow row = sheet.createRow(rowIndex++);
	                int col = 0;

	                // ======= All cells with borders =======
	                createDataCell(row, col++, rec.getCustomer_id(), dataCellStyle);
	                createDataCell(row, col++, rec.getSol_id(), dataCellStyle);
	                createDataCell(row, col++, rec.getGender(), dataCellStyle);
	                createDataCell(row, col++, rec.getAccount_no(), dataCellStyle);
	                createDataCell(row, col++, rec.getCustomer_name(), dataCellStyle);
	                createDataCell(row, col++, rec.getSchm_code(), dataCellStyle);
	                createDataCell(row, col++, rec.getSchm_desc(), dataCellStyle);
	                createDataCell(row, col++, rec.getAcct_open_date() != null ? sdf.format(rec.getAcct_open_date()) : "", dataCellStyle);
	                createDataCell(row, col++, rec.getAcct_close_date() != null ? sdf.format(rec.getAcct_close_date()) : "", dataCellStyle);

	                createNumericCell(row, col++, rec.getBalance_as_on(), numericStyle);
	                createDataCell(row, col++, rec.getCurrency(), dataCellStyle);
	                createNumericCell(row, col++, rec.getBal_equi_to_bwp(), numericStyle);
	                createNumericCell(row, col++, rec.getRate_of_interest(), numericStyle);
	                createNumericCell(row, col++, rec.getHundred(), numericStyle);
	                createDataCell(row, col++, rec.getStatus(), dataCellStyle);
	                createDataCell(row, col++, rec.getMaturity_date() != null ? sdf.format(rec.getMaturity_date()) : "", dataCellStyle);
	                createDataCell(row, col++, rec.getGl_sub_head_code(), dataCellStyle);
	                createDataCell(row, col++, rec.getGl_sub_head_desc(), dataCellStyle);
	                createDataCell(row, col++, rec.getType_of_accounts(), dataCellStyle);
	                createDataCell(row, col++, rec.getSegment(), dataCellStyle);
	                createDataCell(row, col++, rec.getPeriod(), dataCellStyle);
	                createNumericCell(row, col++, rec.getEffective_interest_rate(), numericStyle);
	                createDataCell(row, col++, rec.getBranch_name(), dataCellStyle);
	                createDataCell(row, col++, rec.getBranch_code(), dataCellStyle);
	                createDataCell(row, col++, rec.getReport_date() != null ? sdf.format(rec.getReport_date()) : "", dataCellStyle);
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
