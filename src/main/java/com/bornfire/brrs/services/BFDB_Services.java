package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
import com.bornfire.brrs.entities.BFDB_Entity;
import com.bornfire.brrs.entities.BFDB_Rep;
import com.bornfire.brrs.entities.BrrsGeneralMasterEntity;
import com.bornfire.brrs.entities.BrrsGeneralMasterRepo;

@Service
@Transactional
public class BFDB_Services {

	@Autowired
	SequenceGenerator sequence;

	@Autowired
	BrrsGeneralMasterRepo BrrsGeneralMasterRepos;
	@Autowired
	private BFDB_Rep BFDB_Reps;

	@Autowired
	private DataSource dataSource; // Inject DataSource for JDBC

	private static final Logger logger = LoggerFactory.getLogger(BFDB_Services.class);

	@Transactional
	public String addBFDB(MultipartFile file, String userid, String username) {
		long startTime = System.currentTimeMillis();
		int savedCount = 0, skippedCount = 0;
		int batchSize = 500;

		try (InputStream is = file.getInputStream();
				Workbook workbook = new XSSFWorkbook(is);
				Connection conn = dataSource.getConnection()) {

			conn.setAutoCommit(false);
			Sheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

			String insertSql = "INSERT INTO BRRS_BFDB ("
					+ "SOL_ID,CUST_ID, GENDER, ACCOUNT_NO, ACCT_NAME, SCHM_CODE, SCHM_DESC, "
					+ "ACCT_OPN_DATE, ACCT_CLS_DATE, BALANCE_AS_ON, CCY, BAL_EQUI_TO_BWP, "
					+ "INT_RATE, HUNDRED, STATUS, MATURITY_DATE, GL_SUB_HEAD_CODE, GL_SUB_HEAD_DESC, "
					+ "TYPE_OF_ACCOUNTS, SEGMENT, PERIOD, EFFECTIVE_INT_RATE, "
					+ "REPORT_DATE, ENTRY_DATE, ENTRY_USER, ENTRY_FLG, DEL_FLG" + ") VALUES ("
					+ String.join(",", Collections.nCopies(27, "?")) + ")";

			PreparedStatement stmt = conn.prepareStatement(insertSql);
			int count = 0;

			// === Loop through Excel rows ===
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				// Skip empty rows
				boolean emptyRow = true;
				for (int cn = 0; cn < row.getLastCellNum(); cn++) {
					if (!formatter.formatCellValue(row.getCell(cn), evaluator).trim().isEmpty()) {
						emptyRow = false;
						break;
					}
				}
				if (emptyRow)
					continue;

				try {
					int col = 0;

					stmt.setString(++col, getCellStringSafe(row, 0, formatter, evaluator)); // SOL_ID
					stmt.setString(++col, getCellStringSafe(row, 1, formatter, evaluator)); // CUST_ID
					stmt.setString(++col, getCellStringSafe(row, 2, formatter, evaluator)); // GENDER
					stmt.setString(++col, getCellStringSafe(row, 3, formatter, evaluator)); // ACCOUNT_NO
					stmt.setString(++col, getCellStringSafe(row, 4, formatter, evaluator)); // ACCT_NAME
					stmt.setString(++col, getCellStringSafe(row, 5, formatter, evaluator)); // SCHM_CODE
					stmt.setString(++col, getCellStringSafe(row, 6, formatter, evaluator)); // SCHM_DESC

					stmt.setDate(++col, getCellDateSafe(row, 7, formatter, evaluator)); // ACCT_OPN_DATE
					stmt.setDate(++col, getCellDateSafe(row, 8, formatter, evaluator)); // ACCT_CLS_DATE

					stmt.setBigDecimal(++col, getCellDecimalSafe(row, 9, formatter, evaluator)); // BALANCE_AS_ON
					stmt.setString(++col, getCellStringSafe(row, 10, formatter, evaluator)); // CCY
					stmt.setBigDecimal(++col, getCellDecimalSafe(row, 11, formatter, evaluator)); // BAL_EQUI_TO_BWP
					stmt.setBigDecimal(++col, getCellDecimalSafe(row, 12, formatter, evaluator)); // INT_RATE
					stmt.setBigDecimal(++col, getCellDecimalSafe(row, 13, formatter, evaluator)); // HUNDRED
					stmt.setString(++col, getCellStringSafe(row, 14, formatter, evaluator)); // STATUS
					stmt.setDate(++col, getCellDateSafe(row, 15, formatter, evaluator)); // MATURITY_DATE
					stmt.setString(++col, getCellStringSafe(row, 16, formatter, evaluator)); // GL_SUB_HEAD_CODE
					stmt.setString(++col, getCellStringSafe(row, 17, formatter, evaluator)); // GL_SUB_HEAD_DESC
					stmt.setString(++col, getCellStringSafe(row, 18, formatter, evaluator)); // TYPE_OF_ACCOUNTS
					stmt.setString(++col, getCellStringSafe(row, 19, formatter, evaluator)); // SEGMENT
					stmt.setString(++col, getCellStringSafe(row, 20, formatter, evaluator)); // PERIOD
					stmt.setBigDecimal(++col, getCellDecimalSafe(row, 21, formatter, evaluator)); // EFFECTIVE_INT_RATE

					stmt.setDate(++col, getCellDateSafe(row, 22, formatter, evaluator)); // REPORT_DATE

					stmt.setDate(++col, new java.sql.Date(System.currentTimeMillis())); // ENTRY_DATE
					stmt.setString(++col, userid); // ENTRY_USER
					stmt.setString(++col, "Y"); // ENTRY_FLG
					stmt.setString(++col, "N"); // DEL_FLG

					stmt.addBatch();

					// === Master Entity ===
					BrrsGeneralMasterEntity master = new BrrsGeneralMasterEntity();
					master.setId(sequence.generateRequestUUId());
					master.setFile_type("BFDB");

					master.setSol_id(getCellString(row.getCell(0), formatter, evaluator));
					master.setCustomer_id(getCellString(row.getCell(1), formatter, evaluator));
					master.setGender(getCellString(row.getCell(2), formatter, evaluator));
					master.setAcc_no(getCellString(row.getCell(3), formatter, evaluator));
					master.setCustomer_name(getCellString(row.getCell(4), formatter, evaluator));
					master.setSchm_code(getCellString(row.getCell(5), formatter, evaluator));
					master.setSchm_desc(getCellString(row.getCell(6), formatter, evaluator));
					master.setOpen_date(getCellDate(row.getCell(7), formatter, evaluator));
					master.setAcct_cls_date(getCellDate(row.getCell(8), formatter, evaluator));

					master.setBalance_as_on(getCellDecimal(row.getCell(9), formatter, evaluator));
					master.setCurrency(getCellString(row.getCell(10), formatter, evaluator));
					master.setBal_equi_to_bwp(getCellDecimal(row.getCell(11), formatter, evaluator));
					master.setRate_of_interest(getCellDecimal(row.getCell(12), formatter, evaluator));
					master.setHundred(getCellDecimal(row.getCell(13), formatter, evaluator));
					master.setStatus(getCellString(row.getCell(14), formatter, evaluator));
					master.setMaturity_date(getCellDate(row.getCell(15), formatter, evaluator));
					master.setGl_sub_head_code(getCellString(row.getCell(16), formatter, evaluator));
					master.setGl_sub_head_desc(getCellString(row.getCell(17), formatter, evaluator));
					master.setType_of_accounts(getCellString(row.getCell(18), formatter, evaluator));
					master.setSegment(getCellString(row.getCell(19), formatter, evaluator));
					master.setPeriod(getCellString(row.getCell(20), formatter, evaluator));
					master.setEffective_int_rate(getCellDecimal(row.getCell(21), formatter, evaluator));
					master.setReport_date(getCellDate(row.getCell(22), formatter, evaluator));

					master.setEntry_date(new Date());
					master.setEntry_user(userid);
					master.setDel_flg("N");
					master.setEntry_flg("Y");

					BrrsGeneralMasterRepos.save(master);

					savedCount++;

					if (++count % batchSize == 0) {
						stmt.executeBatch();
						conn.commit();
						evaluator.clearAllCachedResultValues();
					}

				} catch (Exception ex) {
					skippedCount++;
					logger.error("Skipping row {} due to error: {}", i, ex.getMessage(), ex);
				}
			}

			stmt.executeBatch();
			conn.commit();

			long duration = System.currentTimeMillis() - startTime;
			return "BFDB Added successfully. Saved: " + savedCount + ", Skipped: " + skippedCount + ". Time taken: "
					+ duration + " ms";

		} catch (Exception e) {
			logger.error("Error while processing BFDB Excel: {}", e.getMessage(), e);
			return "Error Occurred while reading Excel: " + e.getMessage();
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
