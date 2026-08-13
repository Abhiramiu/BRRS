package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;

@Service
@Transactional
public class BRRS_BORR_UFCE_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_BORR_UFCE_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ------------------------------
	// HELPER METHOD TO PARSE DATE STRINGS
	// ------------------------------
	private Date parseDate(String dateStr) {
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return null;
		}
		String[] patterns = { "dd-MMM-yyyy", "dd/MM/yyyy", "dd-MM-yyyy", "yyyy-MM-dd", "dd-MMM-yy" };
		for (String pattern : patterns) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(pattern, java.util.Locale.ENGLISH);
				sdf.setLenient(false);
				return sdf.parse(dateStr.trim());
			} catch (ParseException ignored) {
			}
		}
		try {
			return new SimpleDateFormat("dd-MMM-yyyy").parse(dateStr.trim());
		} catch (ParseException e) {
			logger.error("Failed to parse date string: {}", dateStr, e);
			return null;
		}
	}

	// ------------------------------
	// GET SUMMARY VIEW FOR BORR_UFCE REPORT
	// ------------------------------
	public ModelAndView getBORR_UFCEView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();

		if ("ARCHIVAL".equals(type) && version != null) {
			List<BORR_UFCE_Archival_Summary_Entity> T1Master = new ArrayList<>();
			Date parsedDate = parseDate(todate);
			if (parsedDate != null) {
				String sql = "select * from BRRS_BORR_UFCE_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?";
				T1Master = jdbcTemplate.query(sql, new Object[] { parsedDate, version },
						BeanPropertyRowMapper.newInstance(BORR_UFCE_Archival_Summary_Entity.class));
			}
			mv.addObject("reportsummary", T1Master);

		} else {
			List<BORR_UFCE_Summary_Entity> T1Master = new ArrayList<>();
			Date parsedDate = parseDate(todate);
			if (parsedDate != null) {
				String sql = "SELECT * FROM BRRS_BORR_UFCE_SUMMARYTABLE WHERE REPORT_DATE = ?";
				T1Master = jdbcTemplate.query(sql, new Object[] { parsedDate },
						BeanPropertyRowMapper.newInstance(BORR_UFCE_Summary_Entity.class));
			}
			mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/BORR_UFCE");
		mv.addObject("displaymode", "summary");
		return mv;
	}

	// ------------------------------
	// GET DETAILS VIEW FOR BORR_UFCE REPORT
	// ------------------------------
	public ModelAndView getBORR_UFCEcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();

		try {
			Date parsedDate = parseDate(todate);

			String rowId = null;
			String columnId = null;

			if (Filter != null && Filter.contains(",")) {
				String[] parts = Filter.split(",");
				if (parts.length >= 2) {
					rowId = parts[0];
					columnId = parts[1];
				}
			}

			if ("ARCHIVAL".equals(type) && version != null) {
				List<BORR_UFCE_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					String sql = "select * from BRRS_BORR_UFCE_ARCHIVALTABLE_DETAIL where REPORT_LABLE =? and REPORT_ADDL_CRITERIA_1=? AND REPORT_DATE=? AND DATA_ENTRY_VERSION=?";
					T1Dt1 = jdbcTemplate.query(sql, new Object[] { rowId, columnId, parsedDate, version },
							BeanPropertyRowMapper.newInstance(BORR_UFCE_Archival_Detail_Entity.class));
				} else {
					String sql = "select * from BRRS_BORR_UFCE_ARCHIVALTABLE_DETAIL where REPORT_DATE=? AND DATA_ENTRY_VERSION=?";
					T1Dt1 = jdbcTemplate.query(sql, new Object[] { parsedDate, version },
							BeanPropertyRowMapper.newInstance(BORR_UFCE_Archival_Detail_Entity.class));
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				List<BORR_UFCE_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					String sql = "select * from BRRS_BORR_UFCE_DETAILTABLE where REPORT_LABLE =? and REPORT_ADDL_CRITERIA_1=? AND REPORT_DATE=?";
					T1Dt1 = jdbcTemplate.query(sql, new Object[] { rowId, columnId, parsedDate },
							BeanPropertyRowMapper.newInstance(BORR_UFCE_Detail_Entity.class));
				} else {
					String sql = "select * from BRRS_BORR_UFCE_DETAILTABLE where REPORT_DATE = ?";
					T1Dt1 = jdbcTemplate.query(sql, new Object[] { parsedDate },
							BeanPropertyRowMapper.newInstance(BORR_UFCE_Detail_Entity.class));
					String countSql = "select count(*) from BRRS_BORR_UFCE_DETAILTABLE where REPORT_DATE = ?";
					try {
						totalPages = jdbcTemplate.queryForObject(countSql, new Object[] { parsedDate }, Integer.class);
					} catch (Exception ex) {
						totalPages = 0;
					}
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}

		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

		mv.setViewName("BRRS/BORR_UFCE");
		mv.addObject("displaymode", "Details");
		return mv;
	}

	// ------------------------------
	// GENERATE EXCEL FOR DETAIL REPORT
	// ------------------------------
	public byte[] getBORR_UFCEDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BORR_UFCE Details...");
			System.out.println("came to Detail download service");

			if ("ARCHIVAL".equals(type) && version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BORR_UFCEDetail");

			BorderStyle border = BorderStyle.THIN;

			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 10);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.LEFT);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderTop(border);
			headerStyle.setBorderBottom(border);
			headerStyle.setBorderLeft(border);
			headerStyle.setBorderRight(border);

			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			Date parsedToDate = parseDate(todate);
			String sql = "select * from BRRS_BORR_UFCE_DETAILTABLE where REPORT_DATE = ?";
			List<BORR_UFCE_Detail_Entity> reportData = jdbcTemplate.query(sql, new Object[] { parsedToDate },
					BeanPropertyRowMapper.newInstance(BORR_UFCE_Detail_Entity.class));

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (BORR_UFCE_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(4).setCellValue(item.getSchmCode());
					row.createCell(5).setCellValue(item.getSchmDesc());
					row.createCell(6)
							.setCellValue(item.getAcctOpnDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getAcctOpnDate())
									: "");
					row.createCell(7).setCellValue(item.getCcy());
					Cell sanctionCell = row.createCell(8);
					if (item.getSanctionAmount() != null) {
						sanctionCell.setCellValue(item.getSanctionAmount().doubleValue());
					} else {
						sanctionCell.setCellValue(0);
					}
					Cell intRateCell = row.createCell(9);
					if (item.getIntRate() != null) {
						intRateCell.setCellValue(item.getIntRate().doubleValue());
					} else {
						intRateCell.setCellValue(0);
					}

					row.createCell(10).setCellValue(item.getReportLable());
					row.createCell(11).setCellValue(item.getReportAddlCriteria1());
					row.createCell(12)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					for (int j = 0; j < 13; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BORR_UFCE — only header will be written.");
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BORR_UFCE Excel", e);
			return new byte[0];
		}
	}

	// ------------------------------
	// GENERATE EXCEL FOR SUMMARY REPORT
	// ------------------------------
	public byte[] getBORR_UFCEExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

		Date reportDate = parseDate(todate);

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelBORR_UFCEARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		} else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating RESUB report for version {}", version);
			String sql = "select * from BRRS_BORR_UFCE_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?";
			List<BORR_UFCE_Archival_Summary_Entity> T1Master = jdbcTemplate.query(sql,
					new Object[] { reportDate, version },
					BeanPropertyRowMapper.newInstance(BORR_UFCE_Archival_Summary_Entity.class));
		}

		String sqlLive = "SELECT * FROM BRRS_BORR_UFCE_SUMMARYTABLE WHERE REPORT_DATE=?";
		List<BORR_UFCE_Summary_Entity> dataList1 = jdbcTemplate.query(sqlLive, new Object[] { reportDate },
				BeanPropertyRowMapper.newInstance(BORR_UFCE_Summary_Entity.class));

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			logger.error("Service: Template file NOT found at: {}", templatePath.toAbsolutePath());
			throw new FileNotFoundException("Template file not found at " + templatePath.toAbsolutePath());
		}

		Workbook workbook;
		try (InputStream templateInputStream = Files.newInputStream(templatePath)) {
			workbook = WorkbookFactory.create(templateInputStream);
		}

		logger.info("Service: Template loaded successfully. Processing sheets...");

		if (!dataList1.isEmpty()) {
			BORR_UFCE_Summary_Entity data = dataList1.get(0);
			Sheet sheet = workbook.getSheetAt(0);

			for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (row == null)
					continue;

				for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
					Cell cell = row.getCell(colIndex);
					if (cell == null)
						continue;

					String cellValue = getCellValueAsString(cell).trim();

					if (cellValue.matches("R\\d+_VAL_DIVIDE_AMT_IN_INR")
							|| cellValue.matches("R\\d+_VAL_MULTIPLY_AMT_IN_INR")) {

						try {
							Method getter = BORR_UFCE_Summary_Entity.class.getMethod("get" + cellValue.toUpperCase());
							Object val = getter.invoke(data);

							if (val instanceof BigDecimal) {
								cell.setCellValue(((BigDecimal) val).doubleValue());
							} else if (val != null) {
								cell.setCellValue(val.toString());
							} else {
								cell.setCellValue(0.0);
							}
						} catch (NoSuchMethodException e) {
							logger.warn("No getter found for field: {}", cellValue);
						} catch (Exception e) {
							logger.error("Error setting value for field {}: {}", cellValue, e.getMessage());
						}
					}
				}
			}

			logger.info("Service: Dynamic field mapping completed.");
		} else {
			logger.warn("Service: dataList1 is empty. Template will be returned unchanged.");
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		workbook.close();

		logger.info("Service: Excel generation completed successfully. Returning byte array.");
		return out.toByteArray();
	}

	// ------------------------------
	// HELPER METHOD TO CONVERT CELL VALUE TO STRING
	// ------------------------------
	private String getCellValueAsString(Cell cell) {
		if (cell == null)
			return "";
		org.apache.poi.ss.usermodel.DataFormatter formatter = new org.apache.poi.ss.usermodel.DataFormatter();
		return formatter.formatCellValue(cell);
	}

	// ------------------------------
	// FETCH ARCHIVAL SUMMARY LIST WITH VERSION
	// ------------------------------
	public List<BORR_UFCE_Archival_Summary_Entity> getdatabydateListWithVersion() {
		String sql = "SELECT * FROM BRRS_BORR_UFCE_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(BORR_UFCE_Archival_Summary_Entity.class));
	}

	// ------------------------------
	// FETCH ARCHIVAL SUMMARY DATA LIST FOR REPORT
	// ------------------------------
	public List<Object[]> getBORR_UFCEArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<BORR_UFCE_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (BORR_UFCE_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching BORR_UFCE Archival data: " + e.getMessage());
			e.printStackTrace();
		}
		return archivalList;
	}

	// ------------------------------
	// GENERATE EXCEL FOR ARCHIVAL DETAIL REPORT
	// ------------------------------
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_BORR_UFCE ARCHIVAL Details...");
			System.out.println("came to Detail download service");

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BORR_UFCEDetail");

			BorderStyle border = BorderStyle.THIN;

			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 10);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.LEFT);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderTop(border);
			headerStyle.setBorderBottom(border);
			headerStyle.setBorderLeft(border);
			headerStyle.setBorderRight(border);

			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###.000"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			Date parsedToDate = parseDate(todate);
			String sql = "select * from BRRS_BORR_UFCE_ARCHIVALTABLE_DETAIL where REPORT_DATE=? AND DATA_ENTRY_VERSION=?";
			List<BORR_UFCE_Archival_Detail_Entity> reportData = jdbcTemplate.query(sql,
					new Object[] { parsedToDate, version },
					BeanPropertyRowMapper.newInstance(BORR_UFCE_Archival_Detail_Entity.class));

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (BORR_UFCE_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(4).setCellValue(item.getSchmCode());
					row.createCell(5).setCellValue(item.getSchmDesc());
					row.createCell(6)
							.setCellValue(item.getAcctOpnDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getAcctOpnDate())
									: "");
					row.createCell(7).setCellValue(item.getCcy());
					Cell sanctionCell = row.createCell(8);
					if (item.getSanctionAmount() != null) {
						sanctionCell.setCellValue(item.getSanctionAmount().doubleValue());
					} else {
						sanctionCell.setCellValue(0);
					}
					Cell intRateCell = row.createCell(9);
					if (item.getIntRate() != null) {
						intRateCell.setCellValue(item.getIntRate().doubleValue());
					} else {
						intRateCell.setCellValue(0);
					}

					row.createCell(10).setCellValue(item.getReportLable());
					row.createCell(11).setCellValue(item.getReportAddlCriteria1());
					row.createCell(12)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					for (int j = 0; j < 13; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_BORR_UFCE ARCHIVAL — only header will be written.");
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_BORR_UFCE ARCHIVAL Excel", e);
			return new byte[0];
		}
	}

	// ------------------------------
	// GENERATE EXCEL FOR ARCHIVAL SUMMARY REPORT
	// ------------------------------
	public byte[] getExcelBORR_UFCEARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		Date parsedDate = parseDate(todate);
		String sql = "select * from BRRS_BORR_UFCE_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?";
		List<BORR_UFCE_Archival_Summary_Entity> dataList = jdbcTemplate.query(sql, new Object[] { parsedDate, version },
				BeanPropertyRowMapper.newInstance(BORR_UFCE_Archival_Summary_Entity.class));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BORR_UFCE Archival report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		Path templatePath = Paths.get(templateDir, templateFileName);

		if (!Files.exists(templatePath)) {
			logger.error("Service: Template file NOT found at: {}", templatePath.toAbsolutePath());
			throw new FileNotFoundException("Template file not found at " + templatePath.toAbsolutePath());
		}

		Workbook workbook;
		try (InputStream templateInputStream = Files.newInputStream(templatePath)) {
			workbook = WorkbookFactory.create(templateInputStream);
		}

		BORR_UFCE_Archival_Summary_Entity data = dataList.get(0);
		Sheet sheet = workbook.getSheetAt(0);

		for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if (row == null)
				continue;

			for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
				Cell cell = row.getCell(colIndex);
				if (cell == null)
					continue;

				String cellValue = getCellValueAsString(cell).trim();

				if (cellValue.matches("R\\d+_VAL_DIVIDE_AMT_IN_INR")
						|| cellValue.matches("R\\d+_VAL_MULTIPLY_AMT_IN_INR")) {

					try {
						Method getter = BORR_UFCE_Archival_Summary_Entity.class
								.getMethod("get" + cellValue.toUpperCase());
						Object val = getter.invoke(data);

						if (val instanceof BigDecimal) {
							cell.setCellValue(((BigDecimal) val).doubleValue());
						} else if (val != null) {
							cell.setCellValue(val.toString());
						} else {
							cell.setCellValue(0.0);
						}
					} catch (NoSuchMethodException e) {
						logger.warn("No getter found for field: {}", cellValue);
					} catch (Exception e) {
						logger.error("Error setting value for field {}: {}", cellValue, e.getMessage());
					}
				}
			}
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		workbook.close();

		return out.toByteArray();
	}

	// ------------------------------
	// FIND RECORD BY ACCOUNT NUMBER
	// ------------------------------
	public BORR_UFCE_Detail_Entity findByAcctnumber(String acctNo) {
		String sql = "SELECT * FROM BRRS_BORR_UFCE_DETAILTABLE WHERE ACCT_NUMBER = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { acctNo },
					BeanPropertyRowMapper.newInstance(BORR_UFCE_Detail_Entity.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	// ------------------------------
	// GET VIEW OR EDIT PAGE FOR ACCOUNT DETAIL
	// ------------------------------
	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/BORR_UFCE");
		if (acctNo != null) {
			BORR_UFCE_Detail_Entity BORR_UFCEEntity = findByAcctnumber(acctNo);
			if (BORR_UFCEEntity != null && BORR_UFCEEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(BORR_UFCEEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", BORR_UFCEEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	// ------------------------------
	// PREPARE MODEL AND VIEW FOR DETAIL EDIT
	// ------------------------------
	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/BORR_UFCE");

		if (acctNo != null) {
			BORR_UFCE_Detail_Entity la1Entity = findByAcctnumber(acctNo);
			if (la1Entity != null && la1Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", la1Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	// ------------------------------
	// UPDATE DETAIL RECORD AND EXECUTE SUMMARY PROCEDURE
	// ------------------------------
	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("acctBalanceInpula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			BORR_UFCE_Detail_Entity existing = findByAcctnumber(acctNo);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			if (acctName != null && !acctName.isEmpty()) {
				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {
					existing.setAcctName(acctName);
					isChanged = true;
					logger.info("Account name updated to {}", acctName);
				}
			}

			if (provisionStr != null && !provisionStr.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr);
				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newProvision) != 0) {
					existing.setAcctBalanceInpula(newProvision);
					isChanged = true;
					logger.info("Balance updated to {}", newProvision);
				}
			}

			if (isChanged) {
				String updateSql = "UPDATE BRRS_BORR_UFCE_DETAILTABLE SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ? WHERE ACCT_NUMBER = ?";
				jdbcTemplate.update(updateSql, existing.getAcctName(), existing.getAcctBalanceInpula(), acctNo);
				logger.info("Record updated successfully for account {}", acctNo);

				Date parsedReportDate = parseDate(reportDateStr);
				String formattedDate = parsedReportDate != null
						? new SimpleDateFormat("dd-MM-yyyy").format(parsedReportDate)
						: reportDateStr;

				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_BORR_UFCE_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_BORR_UFCE_SUMMARY_PROCEDURE(?); END;", formattedDate);
							logger.info("Procedure executed successfully after commit.");
						} catch (Exception e) {
							logger.error("Error executing procedure after commit", e);
						}
					}
				});

				return ResponseEntity.ok("Record updated successfully!");
			} else {
				logger.info("No changes detected for ACCT_NO: {}", acctNo);
				return ResponseEntity.ok("No changes were made.");
			}

		} catch (Exception e) {
			logger.error("Error updating BORR_UFCE record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	// =========================================================================
	// INNER ENTITY CLASSES FOR BORR_UFCE REPORT
	// =========================================================================

	// ------------------------------
	// BORR_UFCE SUMMARY ENTITY CLASS
	// ------------------------------
	@Entity
	@Table(name = "BRRS_BORR_UFCE_SUMMARYTABLE")
	public static class BORR_UFCE_Summary_Entity {

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;
		private String report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		private String R4_CUST_ID;
		private BigDecimal R4_ACCT_NO;
		private String R4_ACCT_NAME;
		private String R4_SCHM_CODE;
		private String R4_SCHM_DESC;
		private Date R4_ACCT_OPN_DATE;
		private String R4_CCY;
		private BigDecimal R4_BAL_EQUI_TO_BWP;
		private BigDecimal R4_SANCTION_AMT_BWP;
		private BigDecimal R4_INT_RATE;
		private BigDecimal R4_AMT_IN_INR;
		private BigDecimal R4_VALUE_1;
		private BigDecimal R4_VALUE_2;

		private String R5_CUST_ID;
		private BigDecimal R5_ACCT_NO;
		private String R5_ACCT_NAME;
		private String R5_SCHM_CODE;
		private String R5_SCHM_DESC;
		private Date R5_ACCT_OPN_DATE;
		private String R5_CCY;
		private BigDecimal R5_BAL_EQUI_TO_BWP;
		private BigDecimal R5_SANCTION_AMT_BWP;
		private BigDecimal R5_INT_RATE;
		private BigDecimal R5_AMT_IN_INR;
		private BigDecimal R5_VALUE_1;
		private BigDecimal R5_VALUE_2;

		private String R6_CUST_ID;
		private BigDecimal R6_ACCT_NO;
		private String R6_ACCT_NAME;
		private String R6_SCHM_CODE;
		private String R6_SCHM_DESC;
		private Date R6_ACCT_OPN_DATE;
		private String R6_CCY;
		private BigDecimal R6_BAL_EQUI_TO_BWP;
		private BigDecimal R6_SANCTION_AMT_BWP;
		private BigDecimal R6_INT_RATE;
		private BigDecimal R6_AMT_IN_INR;
		private BigDecimal R6_VALUE_1;
		private BigDecimal R6_VALUE_2;

		private String R7_CUST_ID;
		private BigDecimal R7_ACCT_NO;
		private String R7_ACCT_NAME;
		private String R7_SCHM_CODE;
		private String R7_SCHM_DESC;
		private Date R7_ACCT_OPN_DATE;
		private String R7_CCY;
		private BigDecimal R7_BAL_EQUI_TO_BWP;
		private BigDecimal R7_SANCTION_AMT_BWP;
		private BigDecimal R7_INT_RATE;
		private BigDecimal R7_AMT_IN_INR;
		private BigDecimal R7_VALUE_1;
		private BigDecimal R7_VALUE_2;

		private String R8_CUST_ID;
		private BigDecimal R8_ACCT_NO;
		private String R8_ACCT_NAME;
		private String R8_SCHM_CODE;
		private String R8_SCHM_DESC;
		private Date R8_ACCT_OPN_DATE;
		private String R8_CCY;
		private BigDecimal R8_BAL_EQUI_TO_BWP;
		private BigDecimal R8_SANCTION_AMT_BWP;
		private BigDecimal R8_INT_RATE;
		private BigDecimal R8_AMT_IN_INR;
		private BigDecimal R8_VALUE_1;
		private BigDecimal R8_VALUE_2;

		private String R9_CUST_ID;
		private BigDecimal R9_ACCT_NO;
		private String R9_ACCT_NAME;
		private String R9_SCHM_CODE;
		private String R9_SCHM_DESC;
		private Date R9_ACCT_OPN_DATE;
		private String R9_CCY;
		private BigDecimal R9_BAL_EQUI_TO_BWP;
		private BigDecimal R9_SANCTION_AMT_BWP;
		private BigDecimal R9_INT_RATE;
		private BigDecimal R9_AMT_IN_INR;
		private BigDecimal R9_VALUE_1;
		private BigDecimal R9_VALUE_2;

		private String R10_CUST_ID;
		private BigDecimal R10_ACCT_NO;
		private String R10_ACCT_NAME;
		private String R10_SCHM_CODE;
		private String R10_SCHM_DESC;
		private Date R10_ACCT_OPN_DATE;
		private String R10_CCY;
		private BigDecimal R10_BAL_EQUI_TO_BWP;
		private BigDecimal R10_SANCTION_AMT_BWP;
		private BigDecimal R10_INT_RATE;
		private BigDecimal R10_AMT_IN_INR;
		private BigDecimal R10_VALUE_1;
		private BigDecimal R10_VALUE_2;

		private String R11_CUST_ID;
		private BigDecimal R11_ACCT_NO;
		private String R11_ACCT_NAME;
		private String R11_SCHM_CODE;
		private String R11_SCHM_DESC;
		private Date R11_ACCT_OPN_DATE;
		private String R11_CCY;
		private BigDecimal R11_BAL_EQUI_TO_BWP;
		private BigDecimal R11_SANCTION_AMT_BWP;
		private BigDecimal R11_INT_RATE;
		private BigDecimal R11_AMT_IN_INR;
		private BigDecimal R11_VALUE_1;
		private BigDecimal R11_VALUE_2;

		private String R12_CUST_ID;
		private BigDecimal R12_ACCT_NO;
		private String R12_ACCT_NAME;
		private String R12_SCHM_CODE;
		private String R12_SCHM_DESC;
		private Date R12_ACCT_OPN_DATE;
		private String R12_CCY;
		private BigDecimal R12_BAL_EQUI_TO_BWP;
		private BigDecimal R12_SANCTION_AMT_BWP;
		private BigDecimal R12_INT_RATE;
		private BigDecimal R12_AMT_IN_INR;
		private BigDecimal R12_VALUE_1;
		private BigDecimal R12_VALUE_2;

		private BigDecimal R3_VAL_MULTIPLY_AMT_IN_INR;
		private BigDecimal R3_VAL_DIVIDE_AMT_IN_INR;
		private BigDecimal R14_AMT_IN_INR;
		private BigDecimal R14_VALUE_2;

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public String getReport_version() {
			return report_version;
		}

		public void setReport_version(String report_version) {
			this.report_version = report_version;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String report_frequency) {
			this.report_frequency = report_frequency;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String report_code) {
			this.report_code = report_code;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String report_desc) {
			this.report_desc = report_desc;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String entity_flg) {
			this.entity_flg = entity_flg;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String modify_flg) {
			this.modify_flg = modify_flg;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String del_flg) {
			this.del_flg = del_flg;
		}

		public String getR4_CUST_ID() {
			return R4_CUST_ID;
		}

		public void setR4_CUST_ID(String r4_CUST_ID) {
			R4_CUST_ID = r4_CUST_ID;
		}

		public BigDecimal getR4_ACCT_NO() {
			return R4_ACCT_NO;
		}

		public void setR4_ACCT_NO(BigDecimal r4_ACCT_NO) {
			R4_ACCT_NO = r4_ACCT_NO;
		}

		public String getR4_ACCT_NAME() {
			return R4_ACCT_NAME;
		}

		public void setR4_ACCT_NAME(String r4_ACCT_NAME) {
			R4_ACCT_NAME = r4_ACCT_NAME;
		}

		public String getR4_SCHM_CODE() {
			return R4_SCHM_CODE;
		}

		public void setR4_SCHM_CODE(String r4_SCHM_CODE) {
			R4_SCHM_CODE = r4_SCHM_CODE;
		}

		public String getR4_SCHM_DESC() {
			return R4_SCHM_DESC;
		}

		public void setR4_SCHM_DESC(String r4_SCHM_DESC) {
			R4_SCHM_DESC = r4_SCHM_DESC;
		}

		public Date getR4_ACCT_OPN_DATE() {
			return R4_ACCT_OPN_DATE;
		}

		public void setR4_ACCT_OPN_DATE(Date r4_ACCT_OPN_DATE) {
			R4_ACCT_OPN_DATE = r4_ACCT_OPN_DATE;
		}

		public String getR4_CCY() {
			return R4_CCY;
		}

		public void setR4_CCY(String r4_CCY) {
			R4_CCY = r4_CCY;
		}

		public BigDecimal getR4_BAL_EQUI_TO_BWP() {
			return R4_BAL_EQUI_TO_BWP;
		}

		public void setR4_BAL_EQUI_TO_BWP(BigDecimal r4_BAL_EQUI_TO_BWP) {
			R4_BAL_EQUI_TO_BWP = r4_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR4_SANCTION_AMT_BWP() {
			return R4_SANCTION_AMT_BWP;
		}

		public void setR4_SANCTION_AMT_BWP(BigDecimal r4_SANCTION_AMT_BWP) {
			R4_SANCTION_AMT_BWP = r4_SANCTION_AMT_BWP;
		}

		public BigDecimal getR4_INT_RATE() {
			return R4_INT_RATE;
		}

		public void setR4_INT_RATE(BigDecimal r4_INT_RATE) {
			R4_INT_RATE = r4_INT_RATE;
		}

		public BigDecimal getR4_AMT_IN_INR() {
			return R4_AMT_IN_INR;
		}

		public void setR4_AMT_IN_INR(BigDecimal r4_AMT_IN_INR) {
			R4_AMT_IN_INR = r4_AMT_IN_INR;
		}

		public BigDecimal getR4_VALUE_1() {
			return R4_VALUE_1;
		}

		public void setR4_VALUE_1(BigDecimal r4_VALUE_1) {
			R4_VALUE_1 = r4_VALUE_1;
		}

		public BigDecimal getR4_VALUE_2() {
			return R4_VALUE_2;
		}

		public void setR4_VALUE_2(BigDecimal r4_VALUE_2) {
			R4_VALUE_2 = r4_VALUE_2;
		}

		public String getR5_CUST_ID() {
			return R5_CUST_ID;
		}

		public void setR5_CUST_ID(String r5_CUST_ID) {
			R5_CUST_ID = r5_CUST_ID;
		}

		public BigDecimal getR5_ACCT_NO() {
			return R5_ACCT_NO;
		}

		public void setR5_ACCT_NO(BigDecimal r5_ACCT_NO) {
			R5_ACCT_NO = r5_ACCT_NO;
		}

		public String getR5_ACCT_NAME() {
			return R5_ACCT_NAME;
		}

		public void setR5_ACCT_NAME(String r5_ACCT_NAME) {
			R5_ACCT_NAME = r5_ACCT_NAME;
		}

		public String getR5_SCHM_CODE() {
			return R5_SCHM_CODE;
		}

		public void setR5_SCHM_CODE(String r5_SCHM_CODE) {
			R5_SCHM_CODE = r5_SCHM_CODE;
		}

		public String getR5_SCHM_DESC() {
			return R5_SCHM_DESC;
		}

		public void setR5_SCHM_DESC(String r5_SCHM_DESC) {
			R5_SCHM_DESC = r5_SCHM_DESC;
		}

		public Date getR5_ACCT_OPN_DATE() {
			return R5_ACCT_OPN_DATE;
		}

		public void setR5_ACCT_OPN_DATE(Date r5_ACCT_OPN_DATE) {
			R5_ACCT_OPN_DATE = r5_ACCT_OPN_DATE;
		}

		public String getR5_CCY() {
			return R5_CCY;
		}

		public void setR5_CCY(String r5_CCY) {
			R5_CCY = r5_CCY;
		}

		public BigDecimal getR5_BAL_EQUI_TO_BWP() {
			return R5_BAL_EQUI_TO_BWP;
		}

		public void setR5_BAL_EQUI_TO_BWP(BigDecimal r5_BAL_EQUI_TO_BWP) {
			R5_BAL_EQUI_TO_BWP = r5_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR5_SANCTION_AMT_BWP() {
			return R5_SANCTION_AMT_BWP;
		}

		public void setR5_SANCTION_AMT_BWP(BigDecimal r5_SANCTION_AMT_BWP) {
			R5_SANCTION_AMT_BWP = r5_SANCTION_AMT_BWP;
		}

		public BigDecimal getR5_INT_RATE() {
			return R5_INT_RATE;
		}

		public void setR5_INT_RATE(BigDecimal r5_INT_RATE) {
			R5_INT_RATE = r5_INT_RATE;
		}

		public BigDecimal getR5_AMT_IN_INR() {
			return R5_AMT_IN_INR;
		}

		public void setR5_AMT_IN_INR(BigDecimal r5_AMT_IN_INR) {
			R5_AMT_IN_INR = r5_AMT_IN_INR;
		}

		public BigDecimal getR5_VALUE_1() {
			return R5_VALUE_1;
		}

		public void setR5_VALUE_1(BigDecimal r5_VALUE_1) {
			R5_VALUE_1 = r5_VALUE_1;
		}

		public BigDecimal getR5_VALUE_2() {
			return R5_VALUE_2;
		}

		public void setR5_VALUE_2(BigDecimal r5_VALUE_2) {
			R5_VALUE_2 = r5_VALUE_2;
		}

		public String getR6_CUST_ID() {
			return R6_CUST_ID;
		}

		public void setR6_CUST_ID(String r6_CUST_ID) {
			R6_CUST_ID = r6_CUST_ID;
		}

		public BigDecimal getR6_ACCT_NO() {
			return R6_ACCT_NO;
		}

		public void setR6_ACCT_NO(BigDecimal r6_ACCT_NO) {
			R6_ACCT_NO = r6_ACCT_NO;
		}

		public String getR6_ACCT_NAME() {
			return R6_ACCT_NAME;
		}

		public void setR6_ACCT_NAME(String r6_ACCT_NAME) {
			R6_ACCT_NAME = r6_ACCT_NAME;
		}

		public String getR6_SCHM_CODE() {
			return R6_SCHM_CODE;
		}

		public void setR6_SCHM_CODE(String r6_SCHM_CODE) {
			R6_SCHM_CODE = r6_SCHM_CODE;
		}

		public String getR6_SCHM_DESC() {
			return R6_SCHM_DESC;
		}

		public void setR6_SCHM_DESC(String r6_SCHM_DESC) {
			R6_SCHM_DESC = r6_SCHM_DESC;
		}

		public Date getR6_ACCT_OPN_DATE() {
			return R6_ACCT_OPN_DATE;
		}

		public void setR6_ACCT_OPN_DATE(Date r6_ACCT_OPN_DATE) {
			R6_ACCT_OPN_DATE = r6_ACCT_OPN_DATE;
		}

		public String getR6_CCY() {
			return R6_CCY;
		}

		public void setR6_CCY(String r6_CCY) {
			R6_CCY = r6_CCY;
		}

		public BigDecimal getR6_BAL_EQUI_TO_BWP() {
			return R6_BAL_EQUI_TO_BWP;
		}

		public void setR6_BAL_EQUI_TO_BWP(BigDecimal r6_BAL_EQUI_TO_BWP) {
			R6_BAL_EQUI_TO_BWP = r6_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR6_SANCTION_AMT_BWP() {
			return R6_SANCTION_AMT_BWP;
		}

		public void setR6_SANCTION_AMT_BWP(BigDecimal r6_SANCTION_AMT_BWP) {
			R6_SANCTION_AMT_BWP = r6_SANCTION_AMT_BWP;
		}

		public BigDecimal getR6_INT_RATE() {
			return R6_INT_RATE;
		}

		public void setR6_INT_RATE(BigDecimal r6_INT_RATE) {
			R6_INT_RATE = r6_INT_RATE;
		}

		public BigDecimal getR6_AMT_IN_INR() {
			return R6_AMT_IN_INR;
		}

		public void setR6_AMT_IN_INR(BigDecimal r6_AMT_IN_INR) {
			R6_AMT_IN_INR = r6_AMT_IN_INR;
		}

		public BigDecimal getR6_VALUE_1() {
			return R6_VALUE_1;
		}

		public void setR6_VALUE_1(BigDecimal r6_VALUE_1) {
			R6_VALUE_1 = r6_VALUE_1;
		}

		public BigDecimal getR6_VALUE_2() {
			return R6_VALUE_2;
		}

		public void setR6_VALUE_2(BigDecimal r6_VALUE_2) {
			R6_VALUE_2 = r6_VALUE_2;
		}

		public String getR7_CUST_ID() {
			return R7_CUST_ID;
		}

		public void setR7_CUST_ID(String r7_CUST_ID) {
			R7_CUST_ID = r7_CUST_ID;
		}

		public BigDecimal getR7_ACCT_NO() {
			return R7_ACCT_NO;
		}

		public void setR7_ACCT_NO(BigDecimal r7_ACCT_NO) {
			R7_ACCT_NO = r7_ACCT_NO;
		}

		public String getR7_ACCT_NAME() {
			return R7_ACCT_NAME;
		}

		public void setR7_ACCT_NAME(String r7_ACCT_NAME) {
			R7_ACCT_NAME = r7_ACCT_NAME;
		}

		public String getR7_SCHM_CODE() {
			return R7_SCHM_CODE;
		}

		public void setR7_SCHM_CODE(String r7_SCHM_CODE) {
			R7_SCHM_CODE = r7_SCHM_CODE;
		}

		public String getR7_SCHM_DESC() {
			return R7_SCHM_DESC;
		}

		public void setR7_SCHM_DESC(String r7_SCHM_DESC) {
			R7_SCHM_DESC = r7_SCHM_DESC;
		}

		public Date getR7_ACCT_OPN_DATE() {
			return R7_ACCT_OPN_DATE;
		}

		public void setR7_ACCT_OPN_DATE(Date r7_ACCT_OPN_DATE) {
			R7_ACCT_OPN_DATE = r7_ACCT_OPN_DATE;
		}

		public String getR7_CCY() {
			return R7_CCY;
		}

		public void setR7_CCY(String r7_CCY) {
			R7_CCY = r7_CCY;
		}

		public BigDecimal getR7_BAL_EQUI_TO_BWP() {
			return R7_BAL_EQUI_TO_BWP;
		}

		public void setR7_BAL_EQUI_TO_BWP(BigDecimal r7_BAL_EQUI_TO_BWP) {
			R7_BAL_EQUI_TO_BWP = r7_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR7_SANCTION_AMT_BWP() {
			return R7_SANCTION_AMT_BWP;
		}

		public void setR7_SANCTION_AMT_BWP(BigDecimal r7_SANCTION_AMT_BWP) {
			R7_SANCTION_AMT_BWP = r7_SANCTION_AMT_BWP;
		}

		public BigDecimal getR7_INT_RATE() {
			return R7_INT_RATE;
		}

		public void setR7_INT_RATE(BigDecimal r7_INT_RATE) {
			R7_INT_RATE = r7_INT_RATE;
		}

		public BigDecimal getR7_AMT_IN_INR() {
			return R7_AMT_IN_INR;
		}

		public void setR7_AMT_IN_INR(BigDecimal r7_AMT_IN_INR) {
			R7_AMT_IN_INR = r7_AMT_IN_INR;
		}

		public BigDecimal getR7_VALUE_1() {
			return R7_VALUE_1;
		}

		public void setR7_VALUE_1(BigDecimal r7_VALUE_1) {
			R7_VALUE_1 = r7_VALUE_1;
		}

		public BigDecimal getR7_VALUE_2() {
			return R7_VALUE_2;
		}

		public void setR7_VALUE_2(BigDecimal r7_VALUE_2) {
			R7_VALUE_2 = r7_VALUE_2;
		}

		public String getR8_CUST_ID() {
			return R8_CUST_ID;
		}

		public void setR8_CUST_ID(String r8_CUST_ID) {
			R8_CUST_ID = r8_CUST_ID;
		}

		public BigDecimal getR8_ACCT_NO() {
			return R8_ACCT_NO;
		}

		public void setR8_ACCT_NO(BigDecimal r8_ACCT_NO) {
			R8_ACCT_NO = r8_ACCT_NO;
		}

		public String getR8_ACCT_NAME() {
			return R8_ACCT_NAME;
		}

		public void setR8_ACCT_NAME(String r8_ACCT_NAME) {
			R8_ACCT_NAME = r8_ACCT_NAME;
		}

		public String getR8_SCHM_CODE() {
			return R8_SCHM_CODE;
		}

		public void setR8_SCHM_CODE(String r8_SCHM_CODE) {
			R8_SCHM_CODE = r8_SCHM_CODE;
		}

		public String getR8_SCHM_DESC() {
			return R8_SCHM_DESC;
		}

		public void setR8_SCHM_DESC(String r8_SCHM_DESC) {
			R8_SCHM_DESC = r8_SCHM_DESC;
		}

		public Date getR8_ACCT_OPN_DATE() {
			return R8_ACCT_OPN_DATE;
		}

		public void setR8_ACCT_OPN_DATE(Date r8_ACCT_OPN_DATE) {
			R8_ACCT_OPN_DATE = r8_ACCT_OPN_DATE;
		}

		public String getR8_CCY() {
			return R8_CCY;
		}

		public void setR8_CCY(String r8_CCY) {
			R8_CCY = r8_CCY;
		}

		public BigDecimal getR8_BAL_EQUI_TO_BWP() {
			return R8_BAL_EQUI_TO_BWP;
		}

		public void setR8_BAL_EQUI_TO_BWP(BigDecimal r8_BAL_EQUI_TO_BWP) {
			R8_BAL_EQUI_TO_BWP = r8_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR8_SANCTION_AMT_BWP() {
			return R8_SANCTION_AMT_BWP;
		}

		public void setR8_SANCTION_AMT_BWP(BigDecimal r8_SANCTION_AMT_BWP) {
			R8_SANCTION_AMT_BWP = r8_SANCTION_AMT_BWP;
		}

		public BigDecimal getR8_INT_RATE() {
			return R8_INT_RATE;
		}

		public void setR8_INT_RATE(BigDecimal r8_INT_RATE) {
			R8_INT_RATE = r8_INT_RATE;
		}

		public BigDecimal getR8_AMT_IN_INR() {
			return R8_AMT_IN_INR;
		}

		public void setR8_AMT_IN_INR(BigDecimal r8_AMT_IN_INR) {
			R8_AMT_IN_INR = r8_AMT_IN_INR;
		}

		public BigDecimal getR8_VALUE_1() {
			return R8_VALUE_1;
		}

		public void setR8_VALUE_1(BigDecimal r8_VALUE_1) {
			R8_VALUE_1 = r8_VALUE_1;
		}

		public BigDecimal getR8_VALUE_2() {
			return R8_VALUE_2;
		}

		public void setR8_VALUE_2(BigDecimal r8_VALUE_2) {
			R8_VALUE_2 = r8_VALUE_2;
		}

		public String getR9_CUST_ID() {
			return R9_CUST_ID;
		}

		public void setR9_CUST_ID(String r9_CUST_ID) {
			R9_CUST_ID = r9_CUST_ID;
		}

		public BigDecimal getR9_ACCT_NO() {
			return R9_ACCT_NO;
		}

		public void setR9_ACCT_NO(BigDecimal r9_ACCT_NO) {
			R9_ACCT_NO = r9_ACCT_NO;
		}

		public String getR9_ACCT_NAME() {
			return R9_ACCT_NAME;
		}

		public void setR9_ACCT_NAME(String r9_ACCT_NAME) {
			R9_ACCT_NAME = r9_ACCT_NAME;
		}

		public String getR9_SCHM_CODE() {
			return R9_SCHM_CODE;
		}

		public void setR9_SCHM_CODE(String r9_SCHM_CODE) {
			R9_SCHM_CODE = r9_SCHM_CODE;
		}

		public String getR9_SCHM_DESC() {
			return R9_SCHM_DESC;
		}

		public void setR9_SCHM_DESC(String r9_SCHM_DESC) {
			R9_SCHM_DESC = r9_SCHM_DESC;
		}

		public Date getR9_ACCT_OPN_DATE() {
			return R9_ACCT_OPN_DATE;
		}

		public void setR9_ACCT_OPN_DATE(Date r9_ACCT_OPN_DATE) {
			R9_ACCT_OPN_DATE = r9_ACCT_OPN_DATE;
		}

		public String getR9_CCY() {
			return R9_CCY;
		}

		public void setR9_CCY(String r9_CCY) {
			R9_CCY = r9_CCY;
		}

		public BigDecimal getR9_BAL_EQUI_TO_BWP() {
			return R9_BAL_EQUI_TO_BWP;
		}

		public void setR9_BAL_EQUI_TO_BWP(BigDecimal r9_BAL_EQUI_TO_BWP) {
			R9_BAL_EQUI_TO_BWP = r9_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR9_SANCTION_AMT_BWP() {
			return R9_SANCTION_AMT_BWP;
		}

		public void setR9_SANCTION_AMT_BWP(BigDecimal r9_SANCTION_AMT_BWP) {
			R9_SANCTION_AMT_BWP = r9_SANCTION_AMT_BWP;
		}

		public BigDecimal getR9_INT_RATE() {
			return R9_INT_RATE;
		}

		public void setR9_INT_RATE(BigDecimal r9_INT_RATE) {
			R9_INT_RATE = r9_INT_RATE;
		}

		public BigDecimal getR9_AMT_IN_INR() {
			return R9_AMT_IN_INR;
		}

		public void setR9_AMT_IN_INR(BigDecimal r9_AMT_IN_INR) {
			R9_AMT_IN_INR = r9_AMT_IN_INR;
		}

		public BigDecimal getR9_VALUE_1() {
			return R9_VALUE_1;
		}

		public void setR9_VALUE_1(BigDecimal r9_VALUE_1) {
			R9_VALUE_1 = r9_VALUE_1;
		}

		public BigDecimal getR9_VALUE_2() {
			return R9_VALUE_2;
		}

		public void setR9_VALUE_2(BigDecimal r9_VALUE_2) {
			R9_VALUE_2 = r9_VALUE_2;
		}

		public String getR10_CUST_ID() {
			return R10_CUST_ID;
		}

		public void setR10_CUST_ID(String r10_CUST_ID) {
			R10_CUST_ID = r10_CUST_ID;
		}

		public BigDecimal getR10_ACCT_NO() {
			return R10_ACCT_NO;
		}

		public void setR10_ACCT_NO(BigDecimal r10_ACCT_NO) {
			R10_ACCT_NO = r10_ACCT_NO;
		}

		public String getR10_ACCT_NAME() {
			return R10_ACCT_NAME;
		}

		public void setR10_ACCT_NAME(String r10_ACCT_NAME) {
			R10_ACCT_NAME = r10_ACCT_NAME;
		}

		public String getR10_SCHM_CODE() {
			return R10_SCHM_CODE;
		}

		public void setR10_SCHM_CODE(String r10_SCHM_CODE) {
			R10_SCHM_CODE = r10_SCHM_CODE;
		}

		public String getR10_SCHM_DESC() {
			return R10_SCHM_DESC;
		}

		public void setR10_SCHM_DESC(String r10_SCHM_DESC) {
			R10_SCHM_DESC = r10_SCHM_DESC;
		}

		public Date getR10_ACCT_OPN_DATE() {
			return R10_ACCT_OPN_DATE;
		}

		public void setR10_ACCT_OPN_DATE(Date r10_ACCT_OPN_DATE) {
			R10_ACCT_OPN_DATE = r10_ACCT_OPN_DATE;
		}

		public String getR10_CCY() {
			return R10_CCY;
		}

		public void setR10_CCY(String r10_CCY) {
			R10_CCY = r10_CCY;
		}

		public BigDecimal getR10_BAL_EQUI_TO_BWP() {
			return R10_BAL_EQUI_TO_BWP;
		}

		public void setR10_BAL_EQUI_TO_BWP(BigDecimal r10_BAL_EQUI_TO_BWP) {
			R10_BAL_EQUI_TO_BWP = r10_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR10_SANCTION_AMT_BWP() {
			return R10_SANCTION_AMT_BWP;
		}

		public void setR10_SANCTION_AMT_BWP(BigDecimal r10_SANCTION_AMT_BWP) {
			R10_SANCTION_AMT_BWP = r10_SANCTION_AMT_BWP;
		}

		public BigDecimal getR10_INT_RATE() {
			return R10_INT_RATE;
		}

		public void setR10_INT_RATE(BigDecimal r10_INT_RATE) {
			R10_INT_RATE = r10_INT_RATE;
		}

		public BigDecimal getR10_AMT_IN_INR() {
			return R10_AMT_IN_INR;
		}

		public void setR10_AMT_IN_INR(BigDecimal r10_AMT_IN_INR) {
			R10_AMT_IN_INR = r10_AMT_IN_INR;
		}

		public BigDecimal getR10_VALUE_1() {
			return R10_VALUE_1;
		}

		public void setR10_VALUE_1(BigDecimal r10_VALUE_1) {
			R10_VALUE_1 = r10_VALUE_1;
		}

		public BigDecimal getR10_VALUE_2() {
			return R10_VALUE_2;
		}

		public void setR10_VALUE_2(BigDecimal r10_VALUE_2) {
			R10_VALUE_2 = r10_VALUE_2;
		}

		public String getR11_CUST_ID() {
			return R11_CUST_ID;
		}

		public void setR11_CUST_ID(String r11_CUST_ID) {
			R11_CUST_ID = r11_CUST_ID;
		}

		public BigDecimal getR11_ACCT_NO() {
			return R11_ACCT_NO;
		}

		public void setR11_ACCT_NO(BigDecimal r11_ACCT_NO) {
			R11_ACCT_NO = r11_ACCT_NO;
		}

		public String getR11_ACCT_NAME() {
			return R11_ACCT_NAME;
		}

		public void setR11_ACCT_NAME(String r11_ACCT_NAME) {
			R11_ACCT_NAME = r11_ACCT_NAME;
		}

		public String getR11_SCHM_CODE() {
			return R11_SCHM_CODE;
		}

		public void setR11_SCHM_CODE(String r11_SCHM_CODE) {
			R11_SCHM_CODE = r11_SCHM_CODE;
		}

		public String getR11_SCHM_DESC() {
			return R11_SCHM_DESC;
		}

		public void setR11_SCHM_DESC(String r11_SCHM_DESC) {
			R11_SCHM_DESC = r11_SCHM_DESC;
		}

		public Date getR11_ACCT_OPN_DATE() {
			return R11_ACCT_OPN_DATE;
		}

		public void setR11_ACCT_OPN_DATE(Date r11_ACCT_OPN_DATE) {
			R11_ACCT_OPN_DATE = r11_ACCT_OPN_DATE;
		}

		public String getR11_CCY() {
			return R11_CCY;
		}

		public void setR11_CCY(String r11_CCY) {
			R11_CCY = r11_CCY;
		}

		public BigDecimal getR11_BAL_EQUI_TO_BWP() {
			return R11_BAL_EQUI_TO_BWP;
		}

		public void setR11_BAL_EQUI_TO_BWP(BigDecimal r11_BAL_EQUI_TO_BWP) {
			R11_BAL_EQUI_TO_BWP = r11_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR11_SANCTION_AMT_BWP() {
			return R11_SANCTION_AMT_BWP;
		}

		public void setR11_SANCTION_AMT_BWP(BigDecimal r11_SANCTION_AMT_BWP) {
			R11_SANCTION_AMT_BWP = r11_SANCTION_AMT_BWP;
		}

		public BigDecimal getR11_INT_RATE() {
			return R11_INT_RATE;
		}

		public void setR11_INT_RATE(BigDecimal r11_INT_RATE) {
			R11_INT_RATE = r11_INT_RATE;
		}

		public BigDecimal getR11_AMT_IN_INR() {
			return R11_AMT_IN_INR;
		}

		public void setR11_AMT_IN_INR(BigDecimal r11_AMT_IN_INR) {
			R11_AMT_IN_INR = r11_AMT_IN_INR;
		}

		public BigDecimal getR11_VALUE_1() {
			return R11_VALUE_1;
		}

		public void setR11_VALUE_1(BigDecimal r11_VALUE_1) {
			R11_VALUE_1 = r11_VALUE_1;
		}

		public BigDecimal getR11_VALUE_2() {
			return R11_VALUE_2;
		}

		public void setR11_VALUE_2(BigDecimal r11_VALUE_2) {
			R11_VALUE_2 = r11_VALUE_2;
		}

		public String getR12_CUST_ID() {
			return R12_CUST_ID;
		}

		public void setR12_CUST_ID(String r12_CUST_ID) {
			R12_CUST_ID = r12_CUST_ID;
		}

		public BigDecimal getR12_ACCT_NO() {
			return R12_ACCT_NO;
		}

		public void setR12_ACCT_NO(BigDecimal r12_ACCT_NO) {
			R12_ACCT_NO = r12_ACCT_NO;
		}

		public String getR12_ACCT_NAME() {
			return R12_ACCT_NAME;
		}

		public void setR12_ACCT_NAME(String r12_ACCT_NAME) {
			R12_ACCT_NAME = r12_ACCT_NAME;
		}

		public String getR12_SCHM_CODE() {
			return R12_SCHM_CODE;
		}

		public void setR12_SCHM_CODE(String r12_SCHM_CODE) {
			R12_SCHM_CODE = r12_SCHM_CODE;
		}

		public String getR12_SCHM_DESC() {
			return R12_SCHM_DESC;
		}

		public void setR12_SCHM_DESC(String r12_SCHM_DESC) {
			R12_SCHM_DESC = r12_SCHM_DESC;
		}

		public Date getR12_ACCT_OPN_DATE() {
			return R12_ACCT_OPN_DATE;
		}

		public void setR12_ACCT_OPN_DATE(Date r12_ACCT_OPN_DATE) {
			R12_ACCT_OPN_DATE = r12_ACCT_OPN_DATE;
		}

		public String getR12_CCY() {
			return R12_CCY;
		}

		public void setR12_CCY(String r12_CCY) {
			R12_CCY = r12_CCY;
		}

		public BigDecimal getR12_BAL_EQUI_TO_BWP() {
			return R12_BAL_EQUI_TO_BWP;
		}

		public void setR12_BAL_EQUI_TO_BWP(BigDecimal r12_BAL_EQUI_TO_BWP) {
			R12_BAL_EQUI_TO_BWP = r12_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR12_SANCTION_AMT_BWP() {
			return R12_SANCTION_AMT_BWP;
		}

		public void setR12_SANCTION_AMT_BWP(BigDecimal r12_SANCTION_AMT_BWP) {
			R12_SANCTION_AMT_BWP = r12_SANCTION_AMT_BWP;
		}

		public BigDecimal getR12_INT_RATE() {
			return R12_INT_RATE;
		}

		public void setR12_INT_RATE(BigDecimal r12_INT_RATE) {
			R12_INT_RATE = r12_INT_RATE;
		}

		public BigDecimal getR12_AMT_IN_INR() {
			return R12_AMT_IN_INR;
		}

		public void setR12_AMT_IN_INR(BigDecimal r12_AMT_IN_INR) {
			R12_AMT_IN_INR = r12_AMT_IN_INR;
		}

		public BigDecimal getR12_VALUE_1() {
			return R12_VALUE_1;
		}

		public void setR12_VALUE_1(BigDecimal r12_VALUE_1) {
			R12_VALUE_1 = r12_VALUE_1;
		}

		public BigDecimal getR12_VALUE_2() {
			return R12_VALUE_2;
		}

		public void setR12_VALUE_2(BigDecimal r12_VALUE_2) {
			R12_VALUE_2 = r12_VALUE_2;
		}

		public BigDecimal getR3_VAL_MULTIPLY_AMT_IN_INR() {
			return R3_VAL_MULTIPLY_AMT_IN_INR;
		}

		public void setR3_VAL_MULTIPLY_AMT_IN_INR(BigDecimal r3_VAL_MULTIPLY_AMT_IN_INR) {
			R3_VAL_MULTIPLY_AMT_IN_INR = r3_VAL_MULTIPLY_AMT_IN_INR;
		}

		public BigDecimal getR3_VAL_DIVIDE_AMT_IN_INR() {
			return R3_VAL_DIVIDE_AMT_IN_INR;
		}

		public void setR3_VAL_DIVIDE_AMT_IN_INR(BigDecimal r3_VAL_DIVIDE_AMT_IN_INR) {
			R3_VAL_DIVIDE_AMT_IN_INR = r3_VAL_DIVIDE_AMT_IN_INR;
		}

		public BigDecimal getR14_AMT_IN_INR() {
			return R14_AMT_IN_INR;
		}

		public void setR14_AMT_IN_INR(BigDecimal r14_AMT_IN_INR) {
			R14_AMT_IN_INR = r14_AMT_IN_INR;
		}

		public BigDecimal getR14_VALUE_2() {
			return R14_VALUE_2;
		}

		public void setR14_VALUE_2(BigDecimal r14_VALUE_2) {
			R14_VALUE_2 = r14_VALUE_2;
		}

		public BORR_UFCE_Summary_Entity() {
			super();
		}
	}

	// ------------------------------
	// BORR_UFCE ARCHIVAL SUMMARY ENTITY CLASS
	// ------------------------------
	@Entity
	@Table(name = "BRRS_BORR_UFCE_ARCHIVALTABLE_SUMMARY")
	public static class BORR_UFCE_Archival_Summary_Entity {

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;
		private String report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;
		private Date REPORT_RESUBDATE;

		private String R4_CUST_ID;
		private BigDecimal R4_ACCT_NO;
		private String R4_ACCT_NAME;
		private String R4_SCHM_CODE;
		private String R4_SCHM_DESC;
		private Date R4_ACCT_OPN_DATE;
		private String R4_CCY;
		private BigDecimal R4_BAL_EQUI_TO_BWP;
		private BigDecimal R4_SANCTION_AMT_BWP;
		private BigDecimal R4_INT_RATE;
		private BigDecimal R4_AMT_IN_INR;
		private BigDecimal R4_VALUE_1;
		private BigDecimal R4_VALUE_2;

		private String R5_CUST_ID;
		private BigDecimal R5_ACCT_NO;
		private String R5_ACCT_NAME;
		private String R5_SCHM_CODE;
		private String R5_SCHM_DESC;
		private Date R5_ACCT_OPN_DATE;
		private String R5_CCY;
		private BigDecimal R5_BAL_EQUI_TO_BWP;
		private BigDecimal R5_SANCTION_AMT_BWP;
		private BigDecimal R5_INT_RATE;
		private BigDecimal R5_AMT_IN_INR;
		private BigDecimal R5_VALUE_1;
		private BigDecimal R5_VALUE_2;

		private String R6_CUST_ID;
		private BigDecimal R6_ACCT_NO;
		private String R6_ACCT_NAME;
		private String R6_SCHM_CODE;
		private String R6_SCHM_DESC;
		private Date R6_ACCT_OPN_DATE;
		private String R6_CCY;
		private BigDecimal R6_BAL_EQUI_TO_BWP;
		private BigDecimal R6_SANCTION_AMT_BWP;
		private BigDecimal R6_INT_RATE;
		private BigDecimal R6_AMT_IN_INR;
		private BigDecimal R6_VALUE_1;
		private BigDecimal R6_VALUE_2;

		private String R7_CUST_ID;
		private BigDecimal R7_ACCT_NO;
		private String R7_ACCT_NAME;
		private String R7_SCHM_CODE;
		private String R7_SCHM_DESC;
		private Date R7_ACCT_OPN_DATE;
		private String R7_CCY;
		private BigDecimal R7_BAL_EQUI_TO_BWP;
		private BigDecimal R7_SANCTION_AMT_BWP;
		private BigDecimal R7_INT_RATE;
		private BigDecimal R7_AMT_IN_INR;
		private BigDecimal R7_VALUE_1;
		private BigDecimal R7_VALUE_2;

		private String R8_CUST_ID;
		private BigDecimal R8_ACCT_NO;
		private String R8_ACCT_NAME;
		private String R8_SCHM_CODE;
		private String R8_SCHM_DESC;
		private Date R8_ACCT_OPN_DATE;
		private String R8_CCY;
		private BigDecimal R8_BAL_EQUI_TO_BWP;
		private BigDecimal R8_SANCTION_AMT_BWP;
		private BigDecimal R8_INT_RATE;
		private BigDecimal R8_AMT_IN_INR;
		private BigDecimal R8_VALUE_1;
		private BigDecimal R8_VALUE_2;

		private String R9_CUST_ID;
		private BigDecimal R9_ACCT_NO;
		private String R9_ACCT_NAME;
		private String R9_SCHM_CODE;
		private String R9_SCHM_DESC;
		private Date R9_ACCT_OPN_DATE;
		private String R9_CCY;
		private BigDecimal R9_BAL_EQUI_TO_BWP;
		private BigDecimal R9_SANCTION_AMT_BWP;
		private BigDecimal R9_INT_RATE;
		private BigDecimal R9_AMT_IN_INR;
		private BigDecimal R9_VALUE_1;
		private BigDecimal R9_VALUE_2;

		private String R10_CUST_ID;
		private BigDecimal R10_ACCT_NO;
		private String R10_ACCT_NAME;
		private String R10_SCHM_CODE;
		private String R10_SCHM_DESC;
		private Date R10_ACCT_OPN_DATE;
		private String R10_CCY;
		private BigDecimal R10_BAL_EQUI_TO_BWP;
		private BigDecimal R10_SANCTION_AMT_BWP;
		private BigDecimal R10_INT_RATE;
		private BigDecimal R10_AMT_IN_INR;
		private BigDecimal R10_VALUE_1;
		private BigDecimal R10_VALUE_2;

		private String R11_CUST_ID;
		private BigDecimal R11_ACCT_NO;
		private String R11_ACCT_NAME;
		private String R11_SCHM_CODE;
		private String R11_SCHM_DESC;
		private Date R11_ACCT_OPN_DATE;
		private String R11_CCY;
		private BigDecimal R11_BAL_EQUI_TO_BWP;
		private BigDecimal R11_SANCTION_AMT_BWP;
		private BigDecimal R11_INT_RATE;
		private BigDecimal R11_AMT_IN_INR;
		private BigDecimal R11_VALUE_1;
		private BigDecimal R11_VALUE_2;

		private String R12_CUST_ID;
		private BigDecimal R12_ACCT_NO;
		private String R12_ACCT_NAME;
		private String R12_SCHM_CODE;
		private String R12_SCHM_DESC;
		private Date R12_ACCT_OPN_DATE;
		private String R12_CCY;
		private BigDecimal R12_BAL_EQUI_TO_BWP;
		private BigDecimal R12_SANCTION_AMT_BWP;
		private BigDecimal R12_INT_RATE;
		private BigDecimal R12_AMT_IN_INR;
		private BigDecimal R12_VALUE_1;
		private BigDecimal R12_VALUE_2;

		private BigDecimal R3_VAL_MULTIPLY_AMT_IN_INR;
		private BigDecimal R3_VAL_DIVIDE_AMT_IN_INR;
		private BigDecimal R14_AMT_IN_INR;
		private BigDecimal R14_VALUE_2;

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public String getReport_version() {
			return report_version;
		}

		public void setReport_version(String report_version) {
			this.report_version = report_version;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String report_frequency) {
			this.report_frequency = report_frequency;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String report_code) {
			this.report_code = report_code;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String report_desc) {
			this.report_desc = report_desc;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String entity_flg) {
			this.entity_flg = entity_flg;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String modify_flg) {
			this.modify_flg = modify_flg;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String del_flg) {
			this.del_flg = del_flg;
		}

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
			REPORT_RESUBDATE = rEPORT_RESUBDATE;
		}

		public String getR4_CUST_ID() {
			return R4_CUST_ID;
		}

		public void setR4_CUST_ID(String r4_CUST_ID) {
			R4_CUST_ID = r4_CUST_ID;
		}

		public BigDecimal getR4_ACCT_NO() {
			return R4_ACCT_NO;
		}

		public void setR4_ACCT_NO(BigDecimal r4_ACCT_NO) {
			R4_ACCT_NO = r4_ACCT_NO;
		}

		public String getR4_ACCT_NAME() {
			return R4_ACCT_NAME;
		}

		public void setR4_ACCT_NAME(String r4_ACCT_NAME) {
			R4_ACCT_NAME = r4_ACCT_NAME;
		}

		public String getR4_SCHM_CODE() {
			return R4_SCHM_CODE;
		}

		public void setR4_SCHM_CODE(String r4_SCHM_CODE) {
			R4_SCHM_CODE = r4_SCHM_CODE;
		}

		public String getR4_SCHM_DESC() {
			return R4_SCHM_DESC;
		}

		public void setR4_SCHM_DESC(String r4_SCHM_DESC) {
			R4_SCHM_DESC = r4_SCHM_DESC;
		}

		public Date getR4_ACCT_OPN_DATE() {
			return R4_ACCT_OPN_DATE;
		}

		public void setR4_ACCT_OPN_DATE(Date r4_ACCT_OPN_DATE) {
			R4_ACCT_OPN_DATE = r4_ACCT_OPN_DATE;
		}

		public String getR4_CCY() {
			return R4_CCY;
		}

		public void setR4_CCY(String r4_CCY) {
			R4_CCY = r4_CCY;
		}

		public BigDecimal getR4_BAL_EQUI_TO_BWP() {
			return R4_BAL_EQUI_TO_BWP;
		}

		public void setR4_BAL_EQUI_TO_BWP(BigDecimal r4_BAL_EQUI_TO_BWP) {
			R4_BAL_EQUI_TO_BWP = r4_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR4_SANCTION_AMT_BWP() {
			return R4_SANCTION_AMT_BWP;
		}

		public void setR4_SANCTION_AMT_BWP(BigDecimal r4_SANCTION_AMT_BWP) {
			R4_SANCTION_AMT_BWP = r4_SANCTION_AMT_BWP;
		}

		public BigDecimal getR4_INT_RATE() {
			return R4_INT_RATE;
		}

		public void setR4_INT_RATE(BigDecimal r4_INT_RATE) {
			R4_INT_RATE = r4_INT_RATE;
		}

		public BigDecimal getR4_AMT_IN_INR() {
			return R4_AMT_IN_INR;
		}

		public void setR4_AMT_IN_INR(BigDecimal r4_AMT_IN_INR) {
			R4_AMT_IN_INR = r4_AMT_IN_INR;
		}

		public BigDecimal getR4_VALUE_1() {
			return R4_VALUE_1;
		}

		public void setR4_VALUE_1(BigDecimal r4_VALUE_1) {
			R4_VALUE_1 = r4_VALUE_1;
		}

		public BigDecimal getR4_VALUE_2() {
			return R4_VALUE_2;
		}

		public void setR4_VALUE_2(BigDecimal r4_VALUE_2) {
			R4_VALUE_2 = r4_VALUE_2;
		}

		public String getR5_CUST_ID() {
			return R5_CUST_ID;
		}

		public void setR5_CUST_ID(String r5_CUST_ID) {
			R5_CUST_ID = r5_CUST_ID;
		}

		public BigDecimal getR5_ACCT_NO() {
			return R5_ACCT_NO;
		}

		public void setR5_ACCT_NO(BigDecimal r5_ACCT_NO) {
			R5_ACCT_NO = r5_ACCT_NO;
		}

		public String getR5_ACCT_NAME() {
			return R5_ACCT_NAME;
		}

		public void setR5_ACCT_NAME(String r5_ACCT_NAME) {
			R5_ACCT_NAME = r5_ACCT_NAME;
		}

		public String getR5_SCHM_CODE() {
			return R5_SCHM_CODE;
		}

		public void setR5_SCHM_CODE(String r5_SCHM_CODE) {
			R5_SCHM_CODE = r5_SCHM_CODE;
		}

		public String getR5_SCHM_DESC() {
			return R5_SCHM_DESC;
		}

		public void setR5_SCHM_DESC(String r5_SCHM_DESC) {
			R5_SCHM_DESC = r5_SCHM_DESC;
		}

		public Date getR5_ACCT_OPN_DATE() {
			return R5_ACCT_OPN_DATE;
		}

		public void setR5_ACCT_OPN_DATE(Date r5_ACCT_OPN_DATE) {
			R5_ACCT_OPN_DATE = r5_ACCT_OPN_DATE;
		}

		public String getR5_CCY() {
			return R5_CCY;
		}

		public void setR5_CCY(String r5_CCY) {
			R5_CCY = r5_CCY;
		}

		public BigDecimal getR5_BAL_EQUI_TO_BWP() {
			return R5_BAL_EQUI_TO_BWP;
		}

		public void setR5_BAL_EQUI_TO_BWP(BigDecimal r5_BAL_EQUI_TO_BWP) {
			R5_BAL_EQUI_TO_BWP = r5_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR5_SANCTION_AMT_BWP() {
			return R5_SANCTION_AMT_BWP;
		}

		public void setR5_SANCTION_AMT_BWP(BigDecimal r5_SANCTION_AMT_BWP) {
			R5_SANCTION_AMT_BWP = r5_SANCTION_AMT_BWP;
		}

		public BigDecimal getR5_INT_RATE() {
			return R5_INT_RATE;
		}

		public void setR5_INT_RATE(BigDecimal r5_INT_RATE) {
			R5_INT_RATE = r5_INT_RATE;
		}

		public BigDecimal getR5_AMT_IN_INR() {
			return R5_AMT_IN_INR;
		}

		public void setR5_AMT_IN_INR(BigDecimal r5_AMT_IN_INR) {
			R5_AMT_IN_INR = r5_AMT_IN_INR;
		}

		public BigDecimal getR5_VALUE_1() {
			return R5_VALUE_1;
		}

		public void setR5_VALUE_1(BigDecimal r5_VALUE_1) {
			R5_VALUE_1 = r5_VALUE_1;
		}

		public BigDecimal getR5_VALUE_2() {
			return R5_VALUE_2;
		}

		public void setR5_VALUE_2(BigDecimal r5_VALUE_2) {
			R5_VALUE_2 = r5_VALUE_2;
		}

		public String getR6_CUST_ID() {
			return R6_CUST_ID;
		}

		public void setR6_CUST_ID(String r6_CUST_ID) {
			R6_CUST_ID = r6_CUST_ID;
		}

		public BigDecimal getR6_ACCT_NO() {
			return R6_ACCT_NO;
		}

		public void setR6_ACCT_NO(BigDecimal r6_ACCT_NO) {
			R6_ACCT_NO = r6_ACCT_NO;
		}

		public String getR6_ACCT_NAME() {
			return R6_ACCT_NAME;
		}

		public void setR6_ACCT_NAME(String r6_ACCT_NAME) {
			R6_ACCT_NAME = r6_ACCT_NAME;
		}

		public String getR6_SCHM_CODE() {
			return R6_SCHM_CODE;
		}

		public void setR6_SCHM_CODE(String r6_SCHM_CODE) {
			R6_SCHM_CODE = r6_SCHM_CODE;
		}

		public String getR6_SCHM_DESC() {
			return R6_SCHM_DESC;
		}

		public void setR6_SCHM_DESC(String r6_SCHM_DESC) {
			R6_SCHM_DESC = r6_SCHM_DESC;
		}

		public Date getR6_ACCT_OPN_DATE() {
			return R6_ACCT_OPN_DATE;
		}

		public void setR6_ACCT_OPN_DATE(Date r6_ACCT_OPN_DATE) {
			R6_ACCT_OPN_DATE = r6_ACCT_OPN_DATE;
		}

		public String getR6_CCY() {
			return R6_CCY;
		}

		public void setR6_CCY(String r6_CCY) {
			R6_CCY = r6_CCY;
		}

		public BigDecimal getR6_BAL_EQUI_TO_BWP() {
			return R6_BAL_EQUI_TO_BWP;
		}

		public void setR6_BAL_EQUI_TO_BWP(BigDecimal r6_BAL_EQUI_TO_BWP) {
			R6_BAL_EQUI_TO_BWP = r6_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR6_SANCTION_AMT_BWP() {
			return R6_SANCTION_AMT_BWP;
		}

		public void setR6_SANCTION_AMT_BWP(BigDecimal r6_SANCTION_AMT_BWP) {
			R6_SANCTION_AMT_BWP = r6_SANCTION_AMT_BWP;
		}

		public BigDecimal getR6_INT_RATE() {
			return R6_INT_RATE;
		}

		public void setR6_INT_RATE(BigDecimal r6_INT_RATE) {
			R6_INT_RATE = r6_INT_RATE;
		}

		public BigDecimal getR6_AMT_IN_INR() {
			return R6_AMT_IN_INR;
		}

		public void setR6_AMT_IN_INR(BigDecimal r6_AMT_IN_INR) {
			R6_AMT_IN_INR = r6_AMT_IN_INR;
		}

		public BigDecimal getR6_VALUE_1() {
			return R6_VALUE_1;
		}

		public void setR6_VALUE_1(BigDecimal r6_VALUE_1) {
			R6_VALUE_1 = r6_VALUE_1;
		}

		public BigDecimal getR6_VALUE_2() {
			return R6_VALUE_2;
		}

		public void setR6_VALUE_2(BigDecimal r6_VALUE_2) {
			R6_VALUE_2 = r6_VALUE_2;
		}

		public String getR7_CUST_ID() {
			return R7_CUST_ID;
		}

		public void setR7_CUST_ID(String r7_CUST_ID) {
			R7_CUST_ID = r7_CUST_ID;
		}

		public BigDecimal getR7_ACCT_NO() {
			return R7_ACCT_NO;
		}

		public void setR7_ACCT_NO(BigDecimal r7_ACCT_NO) {
			R7_ACCT_NO = r7_ACCT_NO;
		}

		public String getR7_ACCT_NAME() {
			return R7_ACCT_NAME;
		}

		public void setR7_ACCT_NAME(String r7_ACCT_NAME) {
			R7_ACCT_NAME = r7_ACCT_NAME;
		}

		public String getR7_SCHM_CODE() {
			return R7_SCHM_CODE;
		}

		public void setR7_SCHM_CODE(String r7_SCHM_CODE) {
			R7_SCHM_CODE = r7_SCHM_CODE;
		}

		public String getR7_SCHM_DESC() {
			return R7_SCHM_DESC;
		}

		public void setR7_SCHM_DESC(String r7_SCHM_DESC) {
			R7_SCHM_DESC = r7_SCHM_DESC;
		}

		public Date getR7_ACCT_OPN_DATE() {
			return R7_ACCT_OPN_DATE;
		}

		public void setR7_ACCT_OPN_DATE(Date r7_ACCT_OPN_DATE) {
			R7_ACCT_OPN_DATE = r7_ACCT_OPN_DATE;
		}

		public String getR7_CCY() {
			return R7_CCY;
		}

		public void setR7_CCY(String r7_CCY) {
			R7_CCY = r7_CCY;
		}

		public BigDecimal getR7_BAL_EQUI_TO_BWP() {
			return R7_BAL_EQUI_TO_BWP;
		}

		public void setR7_BAL_EQUI_TO_BWP(BigDecimal r7_BAL_EQUI_TO_BWP) {
			R7_BAL_EQUI_TO_BWP = r7_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR7_SANCTION_AMT_BWP() {
			return R7_SANCTION_AMT_BWP;
		}

		public void setR7_SANCTION_AMT_BWP(BigDecimal r7_SANCTION_AMT_BWP) {
			R7_SANCTION_AMT_BWP = r7_SANCTION_AMT_BWP;
		}

		public BigDecimal getR7_INT_RATE() {
			return R7_INT_RATE;
		}

		public void setR7_INT_RATE(BigDecimal r7_INT_RATE) {
			R7_INT_RATE = r7_INT_RATE;
		}

		public BigDecimal getR7_AMT_IN_INR() {
			return R7_AMT_IN_INR;
		}

		public void setR7_AMT_IN_INR(BigDecimal r7_AMT_IN_INR) {
			R7_AMT_IN_INR = r7_AMT_IN_INR;
		}

		public BigDecimal getR7_VALUE_1() {
			return R7_VALUE_1;
		}

		public void setR7_VALUE_1(BigDecimal r7_VALUE_1) {
			R7_VALUE_1 = r7_VALUE_1;
		}

		public BigDecimal getR7_VALUE_2() {
			return R7_VALUE_2;
		}

		public void setR7_VALUE_2(BigDecimal r7_VALUE_2) {
			R7_VALUE_2 = r7_VALUE_2;
		}

		public String getR8_CUST_ID() {
			return R8_CUST_ID;
		}

		public void setR8_CUST_ID(String r8_CUST_ID) {
			R8_CUST_ID = r8_CUST_ID;
		}

		public BigDecimal getR8_ACCT_NO() {
			return R8_ACCT_NO;
		}

		public void setR8_ACCT_NO(BigDecimal r8_ACCT_NO) {
			R8_ACCT_NO = r8_ACCT_NO;
		}

		public String getR8_ACCT_NAME() {
			return R8_ACCT_NAME;
		}

		public void setR8_ACCT_NAME(String r8_ACCT_NAME) {
			R8_ACCT_NAME = r8_ACCT_NAME;
		}

		public String getR8_SCHM_CODE() {
			return R8_SCHM_CODE;
		}

		public void setR8_SCHM_CODE(String r8_SCHM_CODE) {
			R8_SCHM_CODE = r8_SCHM_CODE;
		}

		public String getR8_SCHM_DESC() {
			return R8_SCHM_DESC;
		}

		public void setR8_SCHM_DESC(String r8_SCHM_DESC) {
			R8_SCHM_DESC = r8_SCHM_DESC;
		}

		public Date getR8_ACCT_OPN_DATE() {
			return R8_ACCT_OPN_DATE;
		}

		public void setR8_ACCT_OPN_DATE(Date r8_ACCT_OPN_DATE) {
			R8_ACCT_OPN_DATE = r8_ACCT_OPN_DATE;
		}

		public String getR8_CCY() {
			return R8_CCY;
		}

		public void setR8_CCY(String r8_CCY) {
			R8_CCY = r8_CCY;
		}

		public BigDecimal getR8_BAL_EQUI_TO_BWP() {
			return R8_BAL_EQUI_TO_BWP;
		}

		public void setR8_BAL_EQUI_TO_BWP(BigDecimal r8_BAL_EQUI_TO_BWP) {
			R8_BAL_EQUI_TO_BWP = r8_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR8_SANCTION_AMT_BWP() {
			return R8_SANCTION_AMT_BWP;
		}

		public void setR8_SANCTION_AMT_BWP(BigDecimal r8_SANCTION_AMT_BWP) {
			R8_SANCTION_AMT_BWP = r8_SANCTION_AMT_BWP;
		}

		public BigDecimal getR8_INT_RATE() {
			return R8_INT_RATE;
		}

		public void setR8_INT_RATE(BigDecimal r8_INT_RATE) {
			R8_INT_RATE = r8_INT_RATE;
		}

		public BigDecimal getR8_AMT_IN_INR() {
			return R8_AMT_IN_INR;
		}

		public void setR8_AMT_IN_INR(BigDecimal r8_AMT_IN_INR) {
			R8_AMT_IN_INR = r8_AMT_IN_INR;
		}

		public BigDecimal getR8_VALUE_1() {
			return R8_VALUE_1;
		}

		public void setR8_VALUE_1(BigDecimal r8_VALUE_1) {
			R8_VALUE_1 = r8_VALUE_1;
		}

		public BigDecimal getR8_VALUE_2() {
			return R8_VALUE_2;
		}

		public void setR8_VALUE_2(BigDecimal r8_VALUE_2) {
			R8_VALUE_2 = r8_VALUE_2;
		}

		public String getR9_CUST_ID() {
			return R9_CUST_ID;
		}

		public void setR9_CUST_ID(String r9_CUST_ID) {
			R9_CUST_ID = r9_CUST_ID;
		}

		public BigDecimal getR9_ACCT_NO() {
			return R9_ACCT_NO;
		}

		public void setR9_ACCT_NO(BigDecimal r9_ACCT_NO) {
			R9_ACCT_NO = r9_ACCT_NO;
		}

		public String getR9_ACCT_NAME() {
			return R9_ACCT_NAME;
		}

		public void setR9_ACCT_NAME(String r9_ACCT_NAME) {
			R9_ACCT_NAME = r9_ACCT_NAME;
		}

		public String getR9_SCHM_CODE() {
			return R9_SCHM_CODE;
		}

		public void setR9_SCHM_CODE(String r9_SCHM_CODE) {
			R9_SCHM_CODE = r9_SCHM_CODE;
		}

		public String getR9_SCHM_DESC() {
			return R9_SCHM_DESC;
		}

		public void setR9_SCHM_DESC(String r9_SCHM_DESC) {
			R9_SCHM_DESC = r9_SCHM_DESC;
		}

		public Date getR9_ACCT_OPN_DATE() {
			return R9_ACCT_OPN_DATE;
		}

		public void setR9_ACCT_OPN_DATE(Date r9_ACCT_OPN_DATE) {
			R9_ACCT_OPN_DATE = r9_ACCT_OPN_DATE;
		}

		public String getR9_CCY() {
			return R9_CCY;
		}

		public void setR9_CCY(String r9_CCY) {
			R9_CCY = r9_CCY;
		}

		public BigDecimal getR9_BAL_EQUI_TO_BWP() {
			return R9_BAL_EQUI_TO_BWP;
		}

		public void setR9_BAL_EQUI_TO_BWP(BigDecimal r9_BAL_EQUI_TO_BWP) {
			R9_BAL_EQUI_TO_BWP = r9_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR9_SANCTION_AMT_BWP() {
			return R9_SANCTION_AMT_BWP;
		}

		public void setR9_SANCTION_AMT_BWP(BigDecimal r9_SANCTION_AMT_BWP) {
			R9_SANCTION_AMT_BWP = r9_SANCTION_AMT_BWP;
		}

		public BigDecimal getR9_INT_RATE() {
			return R9_INT_RATE;
		}

		public void setR9_INT_RATE(BigDecimal r9_INT_RATE) {
			R9_INT_RATE = r9_INT_RATE;
		}

		public BigDecimal getR9_AMT_IN_INR() {
			return R9_AMT_IN_INR;
		}

		public void setR9_AMT_IN_INR(BigDecimal r9_AMT_IN_INR) {
			R9_AMT_IN_INR = r9_AMT_IN_INR;
		}

		public BigDecimal getR9_VALUE_1() {
			return R9_VALUE_1;
		}

		public void setR9_VALUE_1(BigDecimal r9_VALUE_1) {
			R9_VALUE_1 = r9_VALUE_1;
		}

		public BigDecimal getR9_VALUE_2() {
			return R9_VALUE_2;
		}

		public void setR9_VALUE_2(BigDecimal r9_VALUE_2) {
			R9_VALUE_2 = r9_VALUE_2;
		}

		public String getR10_CUST_ID() {
			return R10_CUST_ID;
		}

		public void setR10_CUST_ID(String r10_CUST_ID) {
			R10_CUST_ID = r10_CUST_ID;
		}

		public BigDecimal getR10_ACCT_NO() {
			return R10_ACCT_NO;
		}

		public void setR10_ACCT_NO(BigDecimal r10_ACCT_NO) {
			R10_ACCT_NO = r10_ACCT_NO;
		}

		public String getR10_ACCT_NAME() {
			return R10_ACCT_NAME;
		}

		public void setR10_ACCT_NAME(String r10_ACCT_NAME) {
			R10_ACCT_NAME = r10_ACCT_NAME;
		}

		public String getR10_SCHM_CODE() {
			return R10_SCHM_CODE;
		}

		public void setR10_SCHM_CODE(String r10_SCHM_CODE) {
			R10_SCHM_CODE = r10_SCHM_CODE;
		}

		public String getR10_SCHM_DESC() {
			return R10_SCHM_DESC;
		}

		public void setR10_SCHM_DESC(String r10_SCHM_DESC) {
			R10_SCHM_DESC = r10_SCHM_DESC;
		}

		public Date getR10_ACCT_OPN_DATE() {
			return R10_ACCT_OPN_DATE;
		}

		public void setR10_ACCT_OPN_DATE(Date r10_ACCT_OPN_DATE) {
			R10_ACCT_OPN_DATE = r10_ACCT_OPN_DATE;
		}

		public String getR10_CCY() {
			return R10_CCY;
		}

		public void setR10_CCY(String r10_CCY) {
			R10_CCY = r10_CCY;
		}

		public BigDecimal getR10_BAL_EQUI_TO_BWP() {
			return R10_BAL_EQUI_TO_BWP;
		}

		public void setR10_BAL_EQUI_TO_BWP(BigDecimal r10_BAL_EQUI_TO_BWP) {
			R10_BAL_EQUI_TO_BWP = r10_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR10_SANCTION_AMT_BWP() {
			return R10_SANCTION_AMT_BWP;
		}

		public void setR10_SANCTION_AMT_BWP(BigDecimal r10_SANCTION_AMT_BWP) {
			R10_SANCTION_AMT_BWP = r10_SANCTION_AMT_BWP;
		}

		public BigDecimal getR10_INT_RATE() {
			return R10_INT_RATE;
		}

		public void setR10_INT_RATE(BigDecimal r10_INT_RATE) {
			R10_INT_RATE = r10_INT_RATE;
		}

		public BigDecimal getR10_AMT_IN_INR() {
			return R10_AMT_IN_INR;
		}

		public void setR10_AMT_IN_INR(BigDecimal r10_AMT_IN_INR) {
			R10_AMT_IN_INR = r10_AMT_IN_INR;
		}

		public BigDecimal getR10_VALUE_1() {
			return R10_VALUE_1;
		}

		public void setR10_VALUE_1(BigDecimal r10_VALUE_1) {
			R10_VALUE_1 = r10_VALUE_1;
		}

		public BigDecimal getR10_VALUE_2() {
			return R10_VALUE_2;
		}

		public void setR10_VALUE_2(BigDecimal r10_VALUE_2) {
			R10_VALUE_2 = r10_VALUE_2;
		}

		public String getR11_CUST_ID() {
			return R11_CUST_ID;
		}

		public void setR11_CUST_ID(String r11_CUST_ID) {
			R11_CUST_ID = r11_CUST_ID;
		}

		public BigDecimal getR11_ACCT_NO() {
			return R11_ACCT_NO;
		}

		public void setR11_ACCT_NO(BigDecimal r11_ACCT_NO) {
			R11_ACCT_NO = r11_ACCT_NO;
		}

		public String getR11_ACCT_NAME() {
			return R11_ACCT_NAME;
		}

		public void setR11_ACCT_NAME(String r11_ACCT_NAME) {
			R11_ACCT_NAME = r11_ACCT_NAME;
		}

		public String getR11_SCHM_CODE() {
			return R11_SCHM_CODE;
		}

		public void setR11_SCHM_CODE(String r11_SCHM_CODE) {
			R11_SCHM_CODE = r11_SCHM_CODE;
		}

		public String getR11_SCHM_DESC() {
			return R11_SCHM_DESC;
		}

		public void setR11_SCHM_DESC(String r11_SCHM_DESC) {
			R11_SCHM_DESC = r11_SCHM_DESC;
		}

		public Date getR11_ACCT_OPN_DATE() {
			return R11_ACCT_OPN_DATE;
		}

		public void setR11_ACCT_OPN_DATE(Date r11_ACCT_OPN_DATE) {
			R11_ACCT_OPN_DATE = r11_ACCT_OPN_DATE;
		}

		public String getR11_CCY() {
			return R11_CCY;
		}

		public void setR11_CCY(String r11_CCY) {
			R11_CCY = r11_CCY;
		}

		public BigDecimal getR11_BAL_EQUI_TO_BWP() {
			return R11_BAL_EQUI_TO_BWP;
		}

		public void setR11_BAL_EQUI_TO_BWP(BigDecimal r11_BAL_EQUI_TO_BWP) {
			R11_BAL_EQUI_TO_BWP = r11_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR11_SANCTION_AMT_BWP() {
			return R11_SANCTION_AMT_BWP;
		}

		public void setR11_SANCTION_AMT_BWP(BigDecimal r11_SANCTION_AMT_BWP) {
			R11_SANCTION_AMT_BWP = r11_SANCTION_AMT_BWP;
		}

		public BigDecimal getR11_INT_RATE() {
			return R11_INT_RATE;
		}

		public void setR11_INT_RATE(BigDecimal r11_INT_RATE) {
			R11_INT_RATE = r11_INT_RATE;
		}

		public BigDecimal getR11_AMT_IN_INR() {
			return R11_AMT_IN_INR;
		}

		public void setR11_AMT_IN_INR(BigDecimal r11_AMT_IN_INR) {
			R11_AMT_IN_INR = r11_AMT_IN_INR;
		}

		public BigDecimal getR11_VALUE_1() {
			return R11_VALUE_1;
		}

		public void setR11_VALUE_1(BigDecimal r11_VALUE_1) {
			R11_VALUE_1 = r11_VALUE_1;
		}

		public BigDecimal getR11_VALUE_2() {
			return R11_VALUE_2;
		}

		public void setR11_VALUE_2(BigDecimal r11_VALUE_2) {
			R11_VALUE_2 = r11_VALUE_2;
		}

		public String getR12_CUST_ID() {
			return R12_CUST_ID;
		}

		public void setR12_CUST_ID(String r12_CUST_ID) {
			R12_CUST_ID = r12_CUST_ID;
		}

		public BigDecimal getR12_ACCT_NO() {
			return R12_ACCT_NO;
		}

		public void setR12_ACCT_NO(BigDecimal r12_ACCT_NO) {
			R12_ACCT_NO = r12_ACCT_NO;
		}

		public String getR12_ACCT_NAME() {
			return R12_ACCT_NAME;
		}

		public void setR12_ACCT_NAME(String r12_ACCT_NAME) {
			R12_ACCT_NAME = r12_ACCT_NAME;
		}

		public String getR12_SCHM_CODE() {
			return R12_SCHM_CODE;
		}

		public void setR12_SCHM_CODE(String r12_SCHM_CODE) {
			R12_SCHM_CODE = r12_SCHM_CODE;
		}

		public String getR12_SCHM_DESC() {
			return R12_SCHM_DESC;
		}

		public void setR12_SCHM_DESC(String r12_SCHM_DESC) {
			R12_SCHM_DESC = r12_SCHM_DESC;
		}

		public Date getR12_ACCT_OPN_DATE() {
			return R12_ACCT_OPN_DATE;
		}

		public void setR12_ACCT_OPN_DATE(Date r12_ACCT_OPN_DATE) {
			R12_ACCT_OPN_DATE = r12_ACCT_OPN_DATE;
		}

		public String getR12_CCY() {
			return R12_CCY;
		}

		public void setR12_CCY(String r12_CCY) {
			R12_CCY = r12_CCY;
		}

		public BigDecimal getR12_BAL_EQUI_TO_BWP() {
			return R12_BAL_EQUI_TO_BWP;
		}

		public void setR12_BAL_EQUI_TO_BWP(BigDecimal r12_BAL_EQUI_TO_BWP) {
			R12_BAL_EQUI_TO_BWP = r12_BAL_EQUI_TO_BWP;
		}

		public BigDecimal getR12_SANCTION_AMT_BWP() {
			return R12_SANCTION_AMT_BWP;
		}

		public void setR12_SANCTION_AMT_BWP(BigDecimal r12_SANCTION_AMT_BWP) {
			R12_SANCTION_AMT_BWP = r12_SANCTION_AMT_BWP;
		}

		public BigDecimal getR12_INT_RATE() {
			return R12_INT_RATE;
		}

		public void setR12_INT_RATE(BigDecimal r12_INT_RATE) {
			R12_INT_RATE = r12_INT_RATE;
		}

		public BigDecimal getR12_AMT_IN_INR() {
			return R12_AMT_IN_INR;
		}

		public void setR12_AMT_IN_INR(BigDecimal r12_AMT_IN_INR) {
			R12_AMT_IN_INR = r12_AMT_IN_INR;
		}

		public BigDecimal getR12_VALUE_1() {
			return R12_VALUE_1;
		}

		public void setR12_VALUE_1(BigDecimal r12_VALUE_1) {
			R12_VALUE_1 = r12_VALUE_1;
		}

		public BigDecimal getR12_VALUE_2() {
			return R12_VALUE_2;
		}

		public void setR12_VALUE_2(BigDecimal r12_VALUE_2) {
			R12_VALUE_2 = r12_VALUE_2;
		}

		public BigDecimal getR3_VAL_MULTIPLY_AMT_IN_INR() {
			return R3_VAL_MULTIPLY_AMT_IN_INR;
		}

		public void setR3_VAL_MULTIPLY_AMT_IN_INR(BigDecimal r3_VAL_MULTIPLY_AMT_IN_INR) {
			R3_VAL_MULTIPLY_AMT_IN_INR = r3_VAL_MULTIPLY_AMT_IN_INR;
		}

		public BigDecimal getR3_VAL_DIVIDE_AMT_IN_INR() {
			return R3_VAL_DIVIDE_AMT_IN_INR;
		}

		public void setR3_VAL_DIVIDE_AMT_IN_INR(BigDecimal r3_VAL_DIVIDE_AMT_IN_INR) {
			R3_VAL_DIVIDE_AMT_IN_INR = r3_VAL_DIVIDE_AMT_IN_INR;
		}

		public BigDecimal getR14_AMT_IN_INR() {
			return R14_AMT_IN_INR;
		}

		public void setR14_AMT_IN_INR(BigDecimal r14_AMT_IN_INR) {
			R14_AMT_IN_INR = r14_AMT_IN_INR;
		}

		public BigDecimal getR14_VALUE_2() {
			return R14_VALUE_2;
		}

		public void setR14_VALUE_2(BigDecimal r14_VALUE_2) {
			R14_VALUE_2 = r14_VALUE_2;
		}

		public BORR_UFCE_Archival_Summary_Entity() {
			super();
		}
	}

	// ------------------------------
	// BORR_UFCE DETAIL ENTITY CLASS
	// ------------------------------
	@Entity
	@Table(name = "BRRS_BORR_UFCE_DETAILTABLE")
	public static class BORR_UFCE_Detail_Entity {

		@Column(name = "CUST_ID")
		private String custId;

		@Id
		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_LABLE")
		private String reportLable;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 2)
		private BigDecimal acctBalanceInpula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;

		@Column(name = "VERIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG", length = 1)
		private String entityFlg;

		@Column(name = "MODIFY_FLG", length = 1)
		private String modifyFlg;

		@Column(name = "DEL_FLG", length = 1)
		private String delFlg;

		@Column(name = "SCHM_CODE", length = 1)
		private String schmCode;

		@Column(name = "SCHM_DESC", length = 1)
		private String schmDesc;

		@Column(name = "CCY", length = 1)
		private String ccy;

		@Column(name = "SANCTION_AMOUNT", precision = 24, scale = 2)
		private BigDecimal sanctionAmount;

		@Column(name = "ACCT_OPN_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date acctOpnDate;

		@Column(name = "INT_RATE", precision = 24, scale = 2)
		private BigDecimal intRate;

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
		}

		public String getReportLable() {
			return reportLable;
		}

		public void setReportLable(String reportLable) {
			this.reportLable = reportLable;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public BigDecimal getAcctBalanceInpula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) {
			this.acctBalanceInpula = acctBalanceInpula;
		}

		public BigDecimal getAcctBalanceInPula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInPula(BigDecimal acctBalanceInPula) {
			this.acctBalanceInpula = acctBalanceInPula;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public String getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(String entityFlg) {
			this.entityFlg = entityFlg;
		}

		public String getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(String modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public String getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(String delFlg) {
			this.delFlg = delFlg;
		}

		public String getSchmCode() {
			return schmCode;
		}

		public void setSchmCode(String schmCode) {
			this.schmCode = schmCode;
		}

		public String getSchmDesc() {
			return schmDesc;
		}

		public void setSchmDesc(String schmDesc) {
			this.schmDesc = schmDesc;
		}

		public String getCcy() {
			return ccy;
		}

		public void setCcy(String ccy) {
			this.ccy = ccy;
		}

		public BigDecimal getSanctionAmount() {
			return sanctionAmount;
		}

		public void setSanctionAmount(BigDecimal sanctionAmount) {
			this.sanctionAmount = sanctionAmount;
		}

		public Date getAcctOpnDate() {
			return acctOpnDate;
		}

		public void setAcctOpnDate(Date acctOpnDate) {
			this.acctOpnDate = acctOpnDate;
		}

		public BigDecimal getIntRate() {
			return intRate;
		}

		public void setIntRate(BigDecimal intRate) {
			this.intRate = intRate;
		}

		public BORR_UFCE_Detail_Entity() {
			super();
		}
	}

	// ------------------------------
	// BORR_UFCE ARCHIVAL DETAIL ENTITY CLASS
	// ------------------------------
	@Entity
	@Table(name = "BRRS_BORR_UFCE_ARCHIVALTABLE_DETAIL")
	public static class BORR_UFCE_Archival_Detail_Entity {

		@Column(name = "CUST_ID")
		private String custId;

		@Id
		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_LABLE")
		private String reportLable;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 2)
		private BigDecimal acctBalanceInpula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;

		@Column(name = "VERIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG", length = 1)
		private String entityFlg;

		@Column(name = "MODIFY_FLG", length = 1)
		private String modifyFlg;

		@Column(name = "DEL_FLG", length = 1)
		private String delFlg;

		@Column(name = "SCHM_CODE", length = 1)
		private String schmCode;

		@Column(name = "SCHM_DESC", length = 1)
		private String schmDesc;

		@Column(name = "CCY", length = 1)
		private String ccy;

		@Column(name = "SANCTION_AMOUNT", precision = 24, scale = 2)
		private BigDecimal sanctionAmount;

		@Column(name = "ACCT_OPN_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date acctOpnDate;

		@Column(name = "INT_RATE", precision = 24, scale = 2)
		private BigDecimal intRate;

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
		}

		public String getReportLable() {
			return reportLable;
		}

		public void setReportLable(String reportLable) {
			this.reportLable = reportLable;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public BigDecimal getAcctBalanceInpula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) {
			this.acctBalanceInpula = acctBalanceInpula;
		}

		public BigDecimal getAcctBalanceInPula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInPula(BigDecimal acctBalanceInpula) {
			this.acctBalanceInpula = acctBalanceInpula;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public String getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(String entityFlg) {
			this.entityFlg = entityFlg;
		}

		public String getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(String modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public String getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(String delFlg) {
			this.delFlg = delFlg;
		}

		public String getSchmCode() {
			return schmCode;
		}

		public void setSchmCode(String schmCode) {
			this.schmCode = schmCode;
		}

		public String getSchmDesc() {
			return schmDesc;
		}

		public void setSchmDesc(String schmDesc) {
			this.schmDesc = schmDesc;
		}

		public String getCcy() {
			return ccy;
		}

		public void setCcy(String ccy) {
			this.ccy = ccy;
		}

		public BigDecimal getSanctionAmount() {
			return sanctionAmount;
		}

		public void setSanctionAmount(BigDecimal sanctionAmount) {
			this.sanctionAmount = sanctionAmount;
		}

		public Date getAcctOpnDate() {
			return acctOpnDate;
		}

		public void setAcctOpnDate(Date acctOpnDate) {
			this.acctOpnDate = acctOpnDate;
		}

		public BigDecimal getIntRate() {
			return intRate;
		}

		public void setIntRate(BigDecimal intRate) {
			this.intRate = intRate;
		}

		public BORR_UFCE_Archival_Detail_Entity() {
			super();
		}
	}
}