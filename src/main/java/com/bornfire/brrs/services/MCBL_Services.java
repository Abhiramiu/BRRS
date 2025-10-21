package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
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
import com.bornfire.brrs.entities.MCBL_Detail_Rep;

import com.bornfire.brrs.entities.MCBL_Entity;
import com.bornfire.brrs.entities.MCBL_Main_Entity;

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
	private MCBL_Detail_Rep MCBL_Detail_Reps;

	@Autowired
	private MCBL_Rep mcblRep;

	@Autowired
	private DataSource dataSource;

	private static final Logger logger = LoggerFactory.getLogger(MCBL_Services.class);

	@Transactional
	public String addMCBL(MultipartFile file, String userid, String username, String reportDate) {
	    long startTime = System.currentTimeMillis();
	    List<String> validationErrors = new ArrayList<>();

	    if (file == null || file.isEmpty()) {
	        return "Error: Uploaded file is empty!";
	    }

	    try (InputStream is = file.getInputStream();
	         //Workbook workbook = new XSSFWorkbook(is);
	    	Workbook workbook = new HSSFWorkbook(is);
	    	Connection conn = dataSource.getConnection()) {

	        conn.setAutoCommit(false);

	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        java.util.Date parsedDate = sdf.parse(reportDate);
	        java.sql.Date sqlReportDate = new java.sql.Date(parsedDate.getTime());

	        // 1️⃣ Last month report date
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(parsedDate);
	        cal.set(Calendar.DAY_OF_MONTH, 1); // first day of current month
	        cal.add(Calendar.DATE, -1);        // last day of previous month
	        java.sql.Date lastMonthDate = new java.sql.Date(cal.getTimeInMillis());

	        // 2️⃣ Fetch last month head account numbers
	        String lastMonthQuery = "SELECT MCBL_HEAD_ACC_NO FROM BRRS_MCBL WHERE REPORT_DATE = ?";
	        PreparedStatement lastMonthStmt = conn.prepareStatement(lastMonthQuery);
	        lastMonthStmt.setDate(1, lastMonthDate);
	        ResultSet rs = lastMonthStmt.executeQuery();
	        Set<String> lastMonthAccounts = new HashSet<>();
	        while (rs.next()) {
	            lastMonthAccounts.add(rs.getString("MCBL_HEAD_ACC_NO"));
	        }

	        // 3️⃣ Read current Excel head accounts
	        Sheet sheet = workbook.getSheet("MCBL");
	        if (sheet == null) {
	            return "Error: Sheet 'MCBL' not found!";
	        }
	        DataFormatter formatter = new DataFormatter();
	     // 3️⃣ Read current Excel head accounts (only valid rows)
	        Set<String> currentAccounts = new HashSet<>();
	        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            String glCode = formatter.formatCellValue(row.getCell(0)).trim();
	            String glSubCode = formatter.formatCellValue(row.getCell(1)).trim();
	            String headAccNo = formatter.formatCellValue(row.getCell(2)).trim();
	            String currency = formatter.formatCellValue(row.getCell(5)).trim();

	            // Only include valid rows
	            if (!glCode.isEmpty() && !glSubCode.isEmpty() && !headAccNo.isEmpty() && !currency.isEmpty() && isNumeric(headAccNo)) {
	                currentAccounts.add(headAccNo);  // ✅ only valid rows
	            }
	        }

	        // 4️⃣ Compare accounts
	        Set<String> newAccounts = new HashSet<>(currentAccounts);
	        newAccounts.removeAll(lastMonthAccounts);

	        Set<String> missingAccounts = new HashSet<>(lastMonthAccounts);
	        missingAccounts.removeAll(currentAccounts);


	        // 5️⃣ Prepare SQL statements
	        String deleteSql = "DELETE FROM BRRS_MCBL WHERE MCBL_GL_CODE = ? AND MCBL_GL_SUB_CODE = ? AND MCBL_HEAD_ACC_NO = ? AND MCBL_CURRENCY = ? AND REPORT_DATE = ?";
	        PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);

	        String deleteGeneralSql = "DELETE FROM GENERAL_MASTER_TABLE WHERE MCBL_GL_CODE = ? AND MCBL_GL_SUB_CODE = ? AND MCBL_HEAD_ACC_NO = ? AND MCBL_CURRENCY = ? AND REPORT_DATE = ?";
	        PreparedStatement deleteGeneralStmt = conn.prepareStatement(deleteGeneralSql);

	        String insertSql = "INSERT INTO BRRS_MCBL (MCBL_GL_CODE, MCBL_GL_SUB_CODE, MCBL_HEAD_ACC_NO, MCBL_DESCRIPTION, MCBL_CURRENCY, " +
	                "MCBL_DEBIT_BALANCE, MCBL_CREDIT_BALANCE, MCBL_DEBIT_EQUIVALENT, MCBL_CREDIT_EQUIVALENT, ENTRY_USER, ENTRY_DATE, REPORT_DATE, ID, ENTRY_FLG, MODIFY_USER, MODIFY_FLG, DELETE_USER, DELETE_FLG) " +
	                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Y', NULL, NULL, NULL, NULL)";
	        PreparedStatement insertStmt = conn.prepareStatement(insertSql);

	        String insertGeneralSql = "INSERT INTO GENERAL_MASTER_TABLE (ID, MCBL_GL_CODE, MCBL_GL_SUB_CODE, MCBL_HEAD_ACC_NO, MCBL_DESCRIPTION, MCBL_CURRENCY, " +
	                "MCBL_DEBIT_BALANCE, MCBL_CREDIT_BALANCE, MCBL_DEBIT_EQUIVALENT, MCBL_CREDIT_EQUIVALENT, ENTRY_USER, ENTRY_DATE, REPORT_DATE,MCBL_FLG) " +
	                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
	        PreparedStatement insertGeneralStmt = conn.prepareStatement(insertGeneralSql);

	        int batchSize = 500;
	        int count = 0;
	        int skippedCount = 0;
	        Set<String> processedKeys = new HashSet<>();

	        
	        // 6️⃣ Process Excel rows
	        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            String glCode = formatter.formatCellValue(row.getCell(0)).trim();
	            String glSubCode = formatter.formatCellValue(row.getCell(1)).trim();
	            String headAccNo = formatter.formatCellValue(row.getCell(2)).trim();
	            String description = formatter.formatCellValue(row.getCell(3)).trim();
	            String currency = formatter.formatCellValue(row.getCell(5)).trim();

	            if (!glCode.isEmpty() && !glSubCode.isEmpty() && !headAccNo.isEmpty() && !currency.isEmpty() && isNumeric(headAccNo) && isNumeric(glCode) && isNumeric(glSubCode)) {
	                String uniqueKey = glCode + "|" + glSubCode + "|" + headAccNo + "|" + currency + "|" + sqlReportDate;
	                if (!processedKeys.add(uniqueKey)) {
	                    skippedCount++;
	                    continue;
	                }

	                // Delete old records
	                deleteStmt.setString(1, glCode);
	                deleteStmt.setString(2, glSubCode);
	                deleteStmt.setString(3, headAccNo);
	                deleteStmt.setString(4, currency);
	                deleteStmt.setDate(5, sqlReportDate);
	                deleteStmt.executeUpdate();

	                deleteGeneralStmt.setString(1, glCode);
	                deleteGeneralStmt.setString(2, glSubCode);
	                deleteGeneralStmt.setString(3, headAccNo);
	                deleteGeneralStmt.setString(4, currency);
	                deleteGeneralStmt.setDate(5, sqlReportDate);
	                deleteGeneralStmt.executeUpdate();

	                // Insert into BRRS_MCBL
	                insertStmt.setString(1, glCode);
	                insertStmt.setString(2, glSubCode);
	                insertStmt.setString(3, headAccNo);
	                insertStmt.setString(4, description);
	                insertStmt.setString(5, currency);
	                insertStmt.setBigDecimal(6, getCellDecimal(row.getCell(6)));
	                insertStmt.setBigDecimal(7, getCellDecimal(row.getCell(7)));
	                insertStmt.setBigDecimal(8, getCellDecimal(row.getCell(8)));
	                insertStmt.setBigDecimal(9, getCellDecimal(row.getCell(9)));
	                insertStmt.setString(10, userid);
	                insertStmt.setDate(11, new java.sql.Date(System.currentTimeMillis()));
	                insertStmt.setDate(12, sqlReportDate);
	                insertStmt.setString(13, sequence.generateRequestUUId());
	                insertStmt.addBatch();

	                // Insert into GENERAL_MASTER_TABLE
	                insertGeneralStmt.setString(1, sequence.generateRequestUUId());
	                insertGeneralStmt.setString(2, glCode);
	                insertGeneralStmt.setString(3, glSubCode);
	                insertGeneralStmt.setString(4, headAccNo);
	                insertGeneralStmt.setString(5, description);
	                insertGeneralStmt.setString(6, currency);
	                insertGeneralStmt.setBigDecimal(7, getCellDecimal(row.getCell(6)));
	                insertGeneralStmt.setBigDecimal(8, getCellDecimal(row.getCell(7)));
	                insertGeneralStmt.setBigDecimal(9, getCellDecimal(row.getCell(8)));
	                insertGeneralStmt.setBigDecimal(10, getCellDecimal(row.getCell(9)));
	                insertGeneralStmt.setString(11, userid);
	                insertGeneralStmt.setDate(12, new java.sql.Date(System.currentTimeMillis()));
	                insertGeneralStmt.setDate(13, sqlReportDate);
	                insertGeneralStmt.setString(14, "Y");               
	                insertGeneralStmt.addBatch();

	                count++;
	                if (count % batchSize == 0) {
	                    insertStmt.executeBatch();
	                    insertGeneralStmt.executeBatch();
	                    conn.commit();
	                }

	            } else {
	                skippedCount++;
	            }
	        }

	        // Final batch commit
	        insertStmt.executeBatch();
	        insertGeneralStmt.executeBatch();
	        conn.commit();

	        long duration = System.currentTimeMillis() - startTime;

	        // 7️⃣ Prepare popup message
	        String popupMessage = "MCBL processed successfully.<br>" +
	                "Saved rows: " + count + "<br>" +
	                "Skipped rows: " + skippedCount + "<br>" +
	                "New Accounts (" + newAccounts.size() + "): " + String.join(", ", newAccounts) + "<br>" +
	                "Missing Accounts (" + missingAccounts.size() + "): " + String.join(", ", missingAccounts) + "<br>" +
	                "Time taken: " + duration + " ms";

	        return popupMessage;

	    } catch (Exception e) {
	        logger.error("Error while processing MCBL Excel: {}", e.getMessage(), e);
	        return "Error occurred while reading Excel: " + e.getMessage();
	    }
	}



	
	private boolean isString(String value) {
		return value != null && value.matches("[a-zA-Z]+");
	}

	private boolean isNumeric(String str) {
		return str != null && str.matches("\\d+");
	}

	private String normalizeKey(String glCode, String glSubCode, String headAccNo, String currency) {
		return safeTrim(glCode) + "|" + safeTrim(glSubCode) + "|" + safeTrim(headAccNo) + "|" + safeTrim(currency);
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
			List<MCBL_Entity> dataList = mcblRep.findRecordsByReportDate(todate);

			if (dataList != null && !dataList.isEmpty()) {
				int rowIndex = 1;
				for (MCBL_Entity rec : dataList) {
					XSSFRow row = sheet.createRow(rowIndex++);

					// Text / Date cells
					Cell cell0 = row.createCell(0);
					cell0.setCellValue(rec.getMcbl_gl_code());
					cell0.setCellStyle(dataCellStyle);

					Cell cell1 = row.createCell(1);
					cell1.setCellValue(rec.getMcbl_gl_sub_code());
					cell1.setCellStyle(dataCellStyle);

					Cell cell2 = row.createCell(2);
					cell2.setCellValue(rec.getMcbl_head_acc_no());
					cell2.setCellStyle(dataCellStyle);

					Cell cell3 = row.createCell(3);
					cell3.setCellValue(rec.getMcbl_description());
					cell3.setCellStyle(dataCellStyle);

					Cell cell4 = row.createCell(4);
					cell4.setCellValue(rec.getMcbl_currency());
					cell4.setCellStyle(dataCellStyle);

					// Numeric / Amount cells
					Cell debitCell = row.createCell(5);
					debitCell.setCellValue(rec.getMcbl_debit_balance() != null ? rec.getMcbl_debit_balance().doubleValue() : 0);
					debitCell.setCellStyle(balanceStyle);

					Cell creditCell = row.createCell(6);
					creditCell
							.setCellValue(rec.getMcbl_credit_balance() != null ? rec.getMcbl_credit_balance().doubleValue() : 0);
					creditCell.setCellStyle(balanceStyle);

					Cell debitEqCell = row.createCell(7);
					debitEqCell.setCellValue(
							rec.getMcbl_debit_equivalent() != null ? rec.getMcbl_debit_equivalent().doubleValue() : 0);
					debitEqCell.setCellStyle(balanceStyle);

					Cell creditEqCell = row.createCell(8);
					creditEqCell.setCellValue(
							rec.getMcbl_credit_equivalent() != null ? rec.getMcbl_credit_equivalent().doubleValue() : 0);
					creditEqCell.setCellStyle(balanceStyle);

					// Report Date cell
					Cell cell9 = row.createCell(9);
					cell9.setCellValue(rec.getReport_date() != null
							? new SimpleDateFormat("dd-MM-yyyy").format(rec.getReport_date())
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
