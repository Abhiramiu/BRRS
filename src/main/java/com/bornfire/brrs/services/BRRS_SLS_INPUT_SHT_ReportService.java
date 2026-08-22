package com.bornfire.brrs.services;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Column;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;


import com.bornfire.brrs.entities.UserProfileRep;
import com.bornfire.brrs.services.BRRS_M_PLL_ReportService.M_PLL_Archival_Detail_Entity;
import com.bornfire.brrs.services.BRRS_M_PLL_ReportService.M_PLL_Detail_Entity;

@Component
@Service
public class BRRS_SLS_INPUT_SHT_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_SLS_INPUT_SHT_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ------------------------------
	// Parses a date string robustly supporting multiple formats
	// ------------------------------
	private Date parseDateRobustly(String dateStr) {
		if (dateStr == null || dateStr.isEmpty()) {
			return null;
		}
		try {
			return dateformat.parse(dateStr);
		} catch (ParseException e) {
			try {
				return new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
			} catch (ParseException ex) {
				try {
					return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
				} catch (ParseException ex2) {
					logger.error("Failed to parse date: {}", dateStr);
					return null;
				}
			}
		}
	}


	// ------------------------------
	// Retrieves the SLS_INPUT_SHT report summary view
	// ------------------------------
	public ModelAndView getSLSView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version,HttpServletRequest req1,Model md) {
		ModelAndView mv = new ModelAndView();
		
		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		System.out.println("testing");
		System.out.println(version);

		Date parsedDate = parseDateRobustly(todate);

		if (type.equals("ARCHIVAL")) {
			if (version == null && parsedDate != null) {
				version = findMaxVersion(parsedDate);
				System.out.println("✅ Auto-detected version inside service: " + version);
			}

			List<SLS_INPUT_SHT_Archival_Summary_Entity> T1Master = new ArrayList<SLS_INPUT_SHT_Archival_Summary_Entity>();
			if (version != null && parsedDate != null) {
				System.out.println(type);
				System.out.println(version);
				T1Master = jdbcTemplate.query("SELECT * FROM BRRS_SLS_INPUT_SHT_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?", new Object[] { parsedDate, version }, new SLSArchivalSummaryRowMapper());
			}
			mv.addObject("reportsummary", T1Master);
		} else {
			List<SLS_INPUT_SHT_Summary_Entity> T1Master = new ArrayList<SLS_INPUT_SHT_Summary_Entity>();
			if (parsedDate != null) {
				T1Master = jdbcTemplate.query("SELECT * FROM BRRS_SLS_INPUT_SHT_SUMMARYTABLE WHERE REPORT_DATE = ?", new Object[] { parsedDate }, new SLSSummaryRowMapper());
			}
			mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/SLS_REPORT");
		mv.addObject("displaymode", "summary");
		mv.addObject("reportdate", todate);
		// ✅ FIX: these attributes are required by SLS_REPORT.html (buttons, links, download data-attrs)
		// but were never added to the model, so ${reportid}/${asondate}/etc. resolved to null/"null"
		// which broke the Details button link (404) and the hidden filter fields.
		mv.addObject("reportid", reportId);
		mv.addObject("asondate", todate);
		mv.addObject("fromdate", fromdate);
		mv.addObject("todate", todate);
		mv.addObject("currency", currency);
		mv.addObject("dtltype", dtltype);
		mv.addObject("type", type);
		mv.addObject("version", version);
		System.out.println("scv" + mv.getViewName());

		return mv;
	}

	// ------------------------------
	// Retrieves the SLS_INPUT_SHT report details view
	// ------------------------------
	public ModelAndView getSLScurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version,HttpServletRequest req1,Model md) {
		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();
		
		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = parseDateRobustly(todate);

			String rowId = null;
			String columnId = null;

			// ✅ Split filter string into rowId & columnId
			if (Filter != null && Filter.contains(",")) {
				String[] parts = Filter.split(",");
				if (parts.length >= 2) {
					rowId = parts[0];
					columnId = parts[1];
				}
			}
			System.out.println(type);
			if ("ARCHIVAL".equals(type)) {
				if ((version == null || version.isEmpty() || "null".equalsIgnoreCase(version)) && parsedDate != null) {
					BigDecimal maxVer = findMaxVersion(parsedDate);
					if (maxVer != null) {
						version = maxVer.toString();
					}
				}
				System.out.println(type);
				// 🔹 Archival branch
				List<SLS_INPUT_SHT_Archival_Detail_Entity > T1Dt1 = new ArrayList<>();
				if (version != null && parsedDate != null) {
					if (rowId != null && columnId != null) {
						T1Dt1 = jdbcTemplate.query("SELECT * FROM BRRS_SLS_INPUT_SHT_ARCHIVALTABLE_DETAILL WHERE ROW_ID = ? AND COLUMN_ID = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?", new Object[] { rowId, columnId, parsedDate, version }, new SLSArchivalDetailRowMapper());
					} else {
						T1Dt1 = jdbcTemplate.query("SELECT * FROM BRRS_SLS_INPUT_SHT_ARCHIVALTABLE_DETAILL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?", new Object[] { parsedDate, version }, new SLSArchivalDetailRowMapper());
					}
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<SLS_INPUT_SHT_Detail_Entity > T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = jdbcTemplate.query("SELECT * FROM BRRS_SLS_INPUT_SHT_DETAILTABLE WHERE ROW_ID = ? AND COLUMN_ID = ? AND REPORT_DATE = ?", new Object[] { rowId, columnId, parsedDate }, new SLSDetailRowMapper());
				} else {
					T1Dt1 = jdbcTemplate.query("SELECT * FROM BRRS_SLS_INPUT_SHT_DETAILTABLE WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY", new Object[] { parsedDate, currentPage, pageSize }, new SLSDetailRowMapper());
					totalPages = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM BRRS_SLS_INPUT_SHT_DETAILTABLE WHERE REPORT_DATE = ?", new Object[] { parsedDate }, Integer.class);
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

		// ✅ Common attributes
		mv.setViewName("BRRS/SLS_REPORT");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}
	// ------------------------------
	// Generates Excel report for SLS_INPUT_SHT summary
	// ------------------------------
	public byte[] getSLSExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		

		List<SLS_INPUT_SHT_Summary_Entity> dataList = jdbcTemplate.query("SELECT * FROM BRRS_SLS_INPUT_SHT_SUMMARYTABLE WHERE REPORT_DATE = ?", new Object[] { parseDateRobustly(todate) }, new SLSSummaryRowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for sls report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			// This specific exception will be caught by the controller.
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			// A specific exception for permission errors.
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		// This try-with-resources block is perfect. It guarantees all resources are
		// closed automatically.
		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			// --- Style Definitions ---
			CreationHelper createHelper = workbook.getCreationHelper();

			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
			dateStyle.setBorderBottom(BorderStyle.THIN);
			dateStyle.setBorderTop(BorderStyle.THIN);
			dateStyle.setBorderLeft(BorderStyle.THIN);
			dateStyle.setBorderRight(BorderStyle.THIN);

			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setBorderBottom(BorderStyle.THIN);
			textStyle.setBorderTop(BorderStyle.THIN);
			textStyle.setBorderLeft(BorderStyle.THIN);
			textStyle.setBorderRight(BorderStyle.THIN);

			// Create the font
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 11); // size 8
			font.setFontName("Calibri");

			CellStyle numberStyle = workbook.createCellStyle();
			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

				
			
			  Row daterow=sheet.getRow(4);
			  Cell datecell =daterow.getCell(3);
			  datecell.setCellValue(parseDateRobustly(todate));
			 
			
			
			int startRow = 10;

			if (!dataList.isEmpty()) {
				
				String [] fieldSuffixes= {"DAY1","DAY2_7","DAY8_14","DAY15_30","DAY31_TO_2M","MORE2M_TO_3M",
											"OVER3M_TO_6M","OVER6M_TO_1Y","OVER1Y_TO_3Y","OVER3Y_TO_5Y","OVER5Y"};
				
				
				
				for (int rowIndex = 10; rowIndex < 85; rowIndex++) {
					
					SLS_INPUT_SHT_Summary_Entity record = dataList.get(0);
					
					System.out.println(rowIndex);
					if(rowIndex==40 || rowIndex==41 || rowIndex==42 || rowIndex==43 || rowIndex==44 ||rowIndex==45 || rowIndex==74 
							|| rowIndex==75 || rowIndex==76 ||rowIndex==77|| rowIndex==78 ||rowIndex==79 || rowIndex==80 || rowIndex==82 ||rowIndex==85 ) {
						continue;
					}

					
					for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
						
						
					String fieldName = "R"+(rowIndex+1)+"_" + fieldSuffixes[colIndex];
					Row row = sheet.getRow(rowIndex);
					Cell cell = row.getCell(colIndex+3);
					
					 try {
				          Field field = SLS_INPUT_SHT_Summary_Entity.class.getDeclaredField(fieldName);
				          field.setAccessible(true);
				          Object value = field.get(record);
				          System.out.println("fieldname"+value);
				          if (value instanceof BigDecimal) {
				              cell.setCellValue(((BigDecimal) value).doubleValue());
				              //cell.setCellStyle(numberStyle);
				          } else {
				              cell.setCellValue(0.00);
				              //cell.setCellStyle(textStyle);
				          }
				      } catch (NoSuchFieldException | IllegalAccessException e) {
				          cell.setCellValue("");
				          cell.setCellStyle(textStyle);
				          LoggerFactory.getLogger(getClass()).warn("Field not found or inaccessible: {}", fieldName);
				      }
						
					}
					



				}

				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}
	// ------------------------------
	// Generates Excel report for SLS_INPUT_SHT details
	// ------------------------------
	public byte[] getSLSDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for SLS Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("SLSDetails");

			// Common border style
			BorderStyle border = BorderStyle.THIN;

			// Header style (left aligned)
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

			// Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

			// Default data style (left aligned)
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

			// ACCT BALANCE style (right aligned with 3 decimals)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "PROVISION AMOUNT", "REPORT LABEL", "REPORT ADDL CRITERIA",
					"REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = parseDateRobustly(todate);
			List<SLS_INPUT_SHT_Detail_Entity> reportData = jdbcTemplate.query("SELECT * FROM BRRS_SLS_INPUT_SHT_DETAILTABLE WHERE REPORT_DATE = ?", new Object[] { parsedToDate }, new SLSDetailRowMapper());

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (SLS_INPUT_SHT_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getProvision() != null) {
						balanceCell.setCellValue(item.getProvision().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getRowId());
					row.createCell(5).setCellValue(item.getColumnId());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for SLS — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating SLS_INPUT_SHT Excel", e);
			return new byte[0];
		}
	}

	// ------------------------------
	// Fetches SLS_INPUT_SHT archival list
	// ------------------------------
	public List<Object> getSLSArchival() {
		List<Object> SLSArchivallist = new ArrayList<>();
		try {
			SLSArchivallist = jdbcTemplate.query("select REPORT_DATE, REPORT_VERSION from BRRS_SLS_INPUT_SHT_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION DESC", (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION"), null });
			System.out.println("countser" + SLSArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching SLS Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return SLSArchivallist;
	}

	// ------------------------------
	// Finds the maximum version for a given report date
	// ------------------------------
	public BigDecimal findMaxVersion(Date reportDate) {
		try {
			String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_SLS_INPUT_SHT_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
			return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
		} catch (Exception e) {
			logger.warn("No version found for date {}: {}", reportDate, e.getMessage());
			return null;
		}
	}

	// ------------------------------
	// Generates Excel report for SLS_INPUT_SHT archival summary
	// ------------------------------
	public byte[] getExcelSLSARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<SLS_INPUT_SHT_Archival_Summary_Entity> dataList = jdbcTemplate.query("SELECT * FROM BRRS_SLS_INPUT_SHT_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?", new Object[] { parseDateRobustly(todate), version }, new SLSArchivalSummaryRowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for SLS_INPUT_SHT report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			// This specific exception will be caught by the controller.
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			// A specific exception for permission errors.
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		// This try-with-resources block is perfect. It guarantees all resources are
		// closed automatically.
		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			// --- Style Definitions ---
						CreationHelper createHelper = workbook.getCreationHelper();

						CellStyle dateStyle = workbook.createCellStyle();
						dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
						dateStyle.setBorderBottom(BorderStyle.THIN);
						dateStyle.setBorderTop(BorderStyle.THIN);
						dateStyle.setBorderLeft(BorderStyle.THIN);
						dateStyle.setBorderRight(BorderStyle.THIN);

						CellStyle textStyle = workbook.createCellStyle();
						textStyle.setBorderBottom(BorderStyle.THIN);
						textStyle.setBorderTop(BorderStyle.THIN);
						textStyle.setBorderLeft(BorderStyle.THIN);
						textStyle.setBorderRight(BorderStyle.THIN);

						// Create the font
						Font font = workbook.createFont();
						font.setFontHeightInPoints((short) 11); // size 8
						font.setFontName("Calibri");

						CellStyle numberStyle = workbook.createCellStyle();
						// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
						numberStyle.setBorderBottom(BorderStyle.THIN);
						numberStyle.setBorderTop(BorderStyle.THIN);
						numberStyle.setBorderLeft(BorderStyle.THIN);
						numberStyle.setBorderRight(BorderStyle.THIN);
						numberStyle.setFont(font);
						// --- End of Style Definitions ---

							
						
						  Row daterow=sheet.getRow(4);
						  Cell datecell =daterow.getCell(3);
						  datecell.setCellValue(parseDateRobustly(todate));
						 
						
						
						int startRow = 10;

						if (!dataList.isEmpty()) {
							
							String [] fieldSuffixes= {"DAY1","DAY2_7","DAY8_14","DAY15_30","DAY31_TO_2M","MORE2M_TO_3M",
														"OVER3M_TO_6M","OVER6M_TO_1Y","OVER1Y_TO_3Y","OVER3Y_TO_5Y","OVER5Y"};
							
							
							
							for (int rowIndex = 10; rowIndex < 85; rowIndex++) {
								
								SLS_INPUT_SHT_Archival_Summary_Entity record = dataList.get(0);
								
								System.out.println(rowIndex);
								if(rowIndex==40 || rowIndex==41 || rowIndex==42 || rowIndex==43 || rowIndex==44 ||rowIndex==45 || rowIndex==74 
										|| rowIndex==75 || rowIndex==76 ||rowIndex==77|| rowIndex==78 ||rowIndex==79 || rowIndex==80 || rowIndex==82 ||rowIndex==85 ) {
									continue;
								}

								
								for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
									
									
								String fieldName = "R"+(rowIndex+1)+"_" + fieldSuffixes[colIndex];
								Row row = sheet.getRow(rowIndex);
								Cell cell = row.getCell(colIndex+3);
								
								 try {
							          Field field = SLS_INPUT_SHT_Archival_Summary_Entity.class.getDeclaredField(fieldName);
							          field.setAccessible(true);
							          Object value = field.get(record);
							          System.out.println("fieldname"+value);
							          if (value instanceof BigDecimal) {
							              cell.setCellValue(((BigDecimal) value).doubleValue());
							              //cell.setCellStyle(numberStyle);
							          } else {
							              cell.setCellValue(0.00);
							              //cell.setCellStyle(textStyle);
							          }
							      } catch (NoSuchFieldException | IllegalAccessException e) {
							          cell.setCellValue("");
							          cell.setCellStyle(textStyle);
							          LoggerFactory.getLogger(getClass()).warn("Field not found or inaccessible: {}", fieldName);
							      }
									
								}
								



							}

							workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
						} else {

						}

						// Write the final workbook content to the in-memory stream.
						workbook.write(out);

						logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

						return out.toByteArray();
					}
				}

	// ------------------------------
	// Generates Excel report for SLS_INPUT_SHT archival details
	// ------------------------------
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for SLS ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("SLSDetails");

			// Common border style
			BorderStyle border = BorderStyle.THIN;

			// Header style (left aligned)
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

			// Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

			// Default data style (left aligned)
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

			// ACCT BALANCE style (right aligned with 3 decimals)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "PROVISION AMOUNT", "REPORT LABEL", "REPORT ADDL CRITERIA",
					"REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}
			
			Date parsedToDate = parseDateRobustly(todate);
			
			List<SLS_INPUT_SHT_Archival_Detail_Entity> reportData = jdbcTemplate.query(
					"SELECT * FROM BRRS_SLS_INPUT_SHT_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
					new Object[] { parsedToDate, version },
					new SLSArchivalDetailRowMapper()
			);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (SLS_INPUT_SHT_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getProvision() != null) {
						balanceCell.setCellValue(item.getProvision().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getRowId());
					row.createCell(5).setCellValue(item.getColumnId());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for SLS_INPUT_SHT — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating SLS_INPUT_SHT Excel", e);
			return new byte[0];
		}
	}

//public boolean updateProvision(SLS_INPUT_SHT_Detail_Entity mpllData) {
//    try {
//        SLS_INPUT_SHT_Detail_Entity existing = BRRS_SLS_INPUT_SHT_Detail_Repo.findByAcctNumber(mpllData.getAcctNumber());
//        
//        System.out.println("came to services");
//        if (existing != null) {
//            existing.setProvision(mpllData.getProvision());
//            existing.setAcctName(mpllData.getAcctName());
//            
//            
//            BRRS_jdbcTemplate.update("UPDATE BRRS_SLS_INPUT_SHT_DETAILTABLE SET ACCT_NAME = ?, PROVISION = ? WHERE ACCT_NUMBER = ?", existing.getAcctName(), existing.getProvision(), existing.getAcctNumber());
//            
//            return true;
//        } else {
//            System.out.println("Record not found for Account No: " + mpllData.getAcctNumber());
//            return false;
//        }
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        return false;
//    }
//}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// ------------------------------
	// Returns the view or edit page for a specific account
	// ------------------------------
	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/SLS"); 

		if (acctNo != null) {
			List<SLS_INPUT_SHT_Detail_Entity> list = jdbcTemplate.query("SELECT * FROM BRRS_SLS_INPUT_SHT_DETAILTABLE WHERE ACCT_NUMBER = ?", new Object[] { acctNo }, new SLSDetailRowMapper());
			SLS_INPUT_SHT_Detail_Entity mpllEntity = list.isEmpty() ? null : list.get(0);
			if (mpllEntity != null && mpllEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(mpllEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("mpllData", mpllEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	// ------------------------------
	// Updates the SLS_INPUT_SHT detail record and triggers summary procedure
	// ------------------------------
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("provision");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			List<SLS_INPUT_SHT_Detail_Entity> list = jdbcTemplate.query("SELECT * FROM BRRS_SLS_INPUT_SHT_DETAILTABLE WHERE ACCT_NUMBER = ?", new Object[] { acctNo }, new SLSDetailRowMapper());
			SLS_INPUT_SHT_Detail_Entity existing = list.isEmpty() ? null : list.get(0);
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
				if (existing.getProvision() == null || existing.getProvision().compareTo(newProvision) != 0) {
					existing.setProvision(newProvision);
					isChanged = true;
					logger.info("Provision updated to {}", newProvision);
				}
			}
			if (isChanged) {
				jdbcTemplate.update("UPDATE BRRS_SLS_INPUT_SHT_DETAILTABLE SET ACCT_NAME = ?, PROVISION = ? WHERE ACCT_NUMBER = ?", existing.getAcctName(), existing.getProvision(), existing.getAcctNumber());
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_SLS_INPUT_SHT_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_SLS_INPUT_SHT_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating SLS record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	// ==============================
	// Row Mappers
	// ==============================

	// ------------------------------
	// Row Mapper for  SLS_INPUT_SHT_Summary_Entity
	// ------------------------------
	class SLSSummaryRowMapper implements RowMapper<SLS_INPUT_SHT_Summary_Entity> {
		@Override
		public SLS_INPUT_SHT_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			SLS_INPUT_SHT_Summary_Entity obj = new SLS_INPUT_SHT_Summary_Entity();
			obj.setSRL_NO(rs.getString("SRL_NO"));
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_DAY1(rs.getBigDecimal("R11_DAY1"));
			obj.setR11_DAY2_7(rs.getBigDecimal("R11_DAY2_7"));
			obj.setR11_DAY8_14(rs.getBigDecimal("R11_DAY8_14"));
			obj.setR11_DAY15_30(rs.getBigDecimal("R11_DAY15_30"));
			obj.setR11_DAY31_TO_2M(rs.getBigDecimal("R11_DAY31_TO_2M"));
			obj.setR11_MORE2M_TO_3M(rs.getBigDecimal("R11_MORE2M_TO_3M"));
			obj.setR11_OVER3M_TO_6M(rs.getBigDecimal("R11_OVER3M_TO_6M"));
			obj.setR11_OVER6M_TO_1Y(rs.getBigDecimal("R11_OVER6M_TO_1Y"));
			obj.setR11_OVER1Y_TO_3Y(rs.getBigDecimal("R11_OVER1Y_TO_3Y"));
			obj.setR11_OVER3Y_TO_5Y(rs.getBigDecimal("R11_OVER3Y_TO_5Y"));
			obj.setR11_OVER5Y(rs.getBigDecimal("R11_OVER5Y"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_DAY1(rs.getBigDecimal("R12_DAY1"));
			obj.setR12_DAY2_7(rs.getBigDecimal("R12_DAY2_7"));
			obj.setR12_DAY8_14(rs.getBigDecimal("R12_DAY8_14"));
			obj.setR12_DAY15_30(rs.getBigDecimal("R12_DAY15_30"));
			obj.setR12_DAY31_TO_2M(rs.getBigDecimal("R12_DAY31_TO_2M"));
			obj.setR12_MORE2M_TO_3M(rs.getBigDecimal("R12_MORE2M_TO_3M"));
			obj.setR12_OVER3M_TO_6M(rs.getBigDecimal("R12_OVER3M_TO_6M"));
			obj.setR12_OVER6M_TO_1Y(rs.getBigDecimal("R12_OVER6M_TO_1Y"));
			obj.setR12_OVER1Y_TO_3Y(rs.getBigDecimal("R12_OVER1Y_TO_3Y"));
			obj.setR12_OVER3Y_TO_5Y(rs.getBigDecimal("R12_OVER3Y_TO_5Y"));
			obj.setR12_OVER5Y(rs.getBigDecimal("R12_OVER5Y"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_DAY1(rs.getBigDecimal("R13_DAY1"));
			obj.setR13_DAY2_7(rs.getBigDecimal("R13_DAY2_7"));
			obj.setR13_DAY8_14(rs.getBigDecimal("R13_DAY8_14"));
			obj.setR13_DAY15_30(rs.getBigDecimal("R13_DAY15_30"));
			obj.setR13_DAY31_TO_2M(rs.getBigDecimal("R13_DAY31_TO_2M"));
			obj.setR13_MORE2M_TO_3M(rs.getBigDecimal("R13_MORE2M_TO_3M"));
			obj.setR13_OVER3M_TO_6M(rs.getBigDecimal("R13_OVER3M_TO_6M"));
			obj.setR13_OVER6M_TO_1Y(rs.getBigDecimal("R13_OVER6M_TO_1Y"));
			obj.setR13_OVER1Y_TO_3Y(rs.getBigDecimal("R13_OVER1Y_TO_3Y"));
			obj.setR13_OVER3Y_TO_5Y(rs.getBigDecimal("R13_OVER3Y_TO_5Y"));
			obj.setR13_OVER5Y(rs.getBigDecimal("R13_OVER5Y"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_DAY1(rs.getBigDecimal("R14_DAY1"));
			obj.setR14_DAY2_7(rs.getBigDecimal("R14_DAY2_7"));
			obj.setR14_DAY8_14(rs.getBigDecimal("R14_DAY8_14"));
			obj.setR14_DAY15_30(rs.getBigDecimal("R14_DAY15_30"));
			obj.setR14_DAY31_TO_2M(rs.getBigDecimal("R14_DAY31_TO_2M"));
			obj.setR14_MORE2M_TO_3M(rs.getBigDecimal("R14_MORE2M_TO_3M"));
			obj.setR14_OVER3M_TO_6M(rs.getBigDecimal("R14_OVER3M_TO_6M"));
			obj.setR14_OVER6M_TO_1Y(rs.getBigDecimal("R14_OVER6M_TO_1Y"));
			obj.setR14_OVER1Y_TO_3Y(rs.getBigDecimal("R14_OVER1Y_TO_3Y"));
			obj.setR14_OVER3Y_TO_5Y(rs.getBigDecimal("R14_OVER3Y_TO_5Y"));
			obj.setR14_OVER5Y(rs.getBigDecimal("R14_OVER5Y"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_DAY1(rs.getBigDecimal("R15_DAY1"));
			obj.setR15_DAY2_7(rs.getBigDecimal("R15_DAY2_7"));
			obj.setR15_DAY8_14(rs.getBigDecimal("R15_DAY8_14"));
			obj.setR15_DAY15_30(rs.getBigDecimal("R15_DAY15_30"));
			obj.setR15_DAY31_TO_2M(rs.getBigDecimal("R15_DAY31_TO_2M"));
			obj.setR15_MORE2M_TO_3M(rs.getBigDecimal("R15_MORE2M_TO_3M"));
			obj.setR15_OVER3M_TO_6M(rs.getBigDecimal("R15_OVER3M_TO_6M"));
			obj.setR15_OVER6M_TO_1Y(rs.getBigDecimal("R15_OVER6M_TO_1Y"));
			obj.setR15_OVER1Y_TO_3Y(rs.getBigDecimal("R15_OVER1Y_TO_3Y"));
			obj.setR15_OVER3Y_TO_5Y(rs.getBigDecimal("R15_OVER3Y_TO_5Y"));
			obj.setR15_OVER5Y(rs.getBigDecimal("R15_OVER5Y"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_DAY1(rs.getBigDecimal("R16_DAY1"));
			obj.setR16_DAY2_7(rs.getBigDecimal("R16_DAY2_7"));
			obj.setR16_DAY8_14(rs.getBigDecimal("R16_DAY8_14"));
			obj.setR16_DAY15_30(rs.getBigDecimal("R16_DAY15_30"));
			obj.setR16_DAY31_TO_2M(rs.getBigDecimal("R16_DAY31_TO_2M"));
			obj.setR16_MORE2M_TO_3M(rs.getBigDecimal("R16_MORE2M_TO_3M"));
			obj.setR16_OVER3M_TO_6M(rs.getBigDecimal("R16_OVER3M_TO_6M"));
			obj.setR16_OVER6M_TO_1Y(rs.getBigDecimal("R16_OVER6M_TO_1Y"));
			obj.setR16_OVER1Y_TO_3Y(rs.getBigDecimal("R16_OVER1Y_TO_3Y"));
			obj.setR16_OVER3Y_TO_5Y(rs.getBigDecimal("R16_OVER3Y_TO_5Y"));
			obj.setR16_OVER5Y(rs.getBigDecimal("R16_OVER5Y"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_DAY1(rs.getBigDecimal("R17_DAY1"));
			obj.setR17_DAY2_7(rs.getBigDecimal("R17_DAY2_7"));
			obj.setR17_DAY8_14(rs.getBigDecimal("R17_DAY8_14"));
			obj.setR17_DAY15_30(rs.getBigDecimal("R17_DAY15_30"));
			obj.setR17_DAY31_TO_2M(rs.getBigDecimal("R17_DAY31_TO_2M"));
			obj.setR17_MORE2M_TO_3M(rs.getBigDecimal("R17_MORE2M_TO_3M"));
			obj.setR17_OVER3M_TO_6M(rs.getBigDecimal("R17_OVER3M_TO_6M"));
			obj.setR17_OVER6M_TO_1Y(rs.getBigDecimal("R17_OVER6M_TO_1Y"));
			obj.setR17_OVER1Y_TO_3Y(rs.getBigDecimal("R17_OVER1Y_TO_3Y"));
			obj.setR17_OVER3Y_TO_5Y(rs.getBigDecimal("R17_OVER3Y_TO_5Y"));
			obj.setR17_OVER5Y(rs.getBigDecimal("R17_OVER5Y"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_DAY1(rs.getBigDecimal("R18_DAY1"));
			obj.setR18_DAY2_7(rs.getBigDecimal("R18_DAY2_7"));
			obj.setR18_DAY8_14(rs.getBigDecimal("R18_DAY8_14"));
			obj.setR18_DAY15_30(rs.getBigDecimal("R18_DAY15_30"));
			obj.setR18_DAY31_TO_2M(rs.getBigDecimal("R18_DAY31_TO_2M"));
			obj.setR18_MORE2M_TO_3M(rs.getBigDecimal("R18_MORE2M_TO_3M"));
			obj.setR18_OVER3M_TO_6M(rs.getBigDecimal("R18_OVER3M_TO_6M"));
			obj.setR18_OVER6M_TO_1Y(rs.getBigDecimal("R18_OVER6M_TO_1Y"));
			obj.setR18_OVER1Y_TO_3Y(rs.getBigDecimal("R18_OVER1Y_TO_3Y"));
			obj.setR18_OVER3Y_TO_5Y(rs.getBigDecimal("R18_OVER3Y_TO_5Y"));
			obj.setR18_OVER5Y(rs.getBigDecimal("R18_OVER5Y"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_DAY1(rs.getBigDecimal("R19_DAY1"));
			obj.setR19_DAY2_7(rs.getBigDecimal("R19_DAY2_7"));
			obj.setR19_DAY8_14(rs.getBigDecimal("R19_DAY8_14"));
			obj.setR19_DAY15_30(rs.getBigDecimal("R19_DAY15_30"));
			obj.setR19_DAY31_TO_2M(rs.getBigDecimal("R19_DAY31_TO_2M"));
			obj.setR19_MORE2M_TO_3M(rs.getBigDecimal("R19_MORE2M_TO_3M"));
			obj.setR19_OVER3M_TO_6M(rs.getBigDecimal("R19_OVER3M_TO_6M"));
			obj.setR19_OVER6M_TO_1Y(rs.getBigDecimal("R19_OVER6M_TO_1Y"));
			obj.setR19_OVER1Y_TO_3Y(rs.getBigDecimal("R19_OVER1Y_TO_3Y"));
			obj.setR19_OVER3Y_TO_5Y(rs.getBigDecimal("R19_OVER3Y_TO_5Y"));
			obj.setR19_OVER5Y(rs.getBigDecimal("R19_OVER5Y"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
			obj.setR20_DAY1(rs.getBigDecimal("R20_DAY1"));
			obj.setR20_DAY2_7(rs.getBigDecimal("R20_DAY2_7"));
			obj.setR20_DAY8_14(rs.getBigDecimal("R20_DAY8_14"));
			obj.setR20_DAY15_30(rs.getBigDecimal("R20_DAY15_30"));
			obj.setR20_DAY31_TO_2M(rs.getBigDecimal("R20_DAY31_TO_2M"));
			obj.setR20_MORE2M_TO_3M(rs.getBigDecimal("R20_MORE2M_TO_3M"));
			obj.setR20_OVER3M_TO_6M(rs.getBigDecimal("R20_OVER3M_TO_6M"));
			obj.setR20_OVER6M_TO_1Y(rs.getBigDecimal("R20_OVER6M_TO_1Y"));
			obj.setR20_OVER1Y_TO_3Y(rs.getBigDecimal("R20_OVER1Y_TO_3Y"));
			obj.setR20_OVER3Y_TO_5Y(rs.getBigDecimal("R20_OVER3Y_TO_5Y"));
			obj.setR20_OVER5Y(rs.getBigDecimal("R20_OVER5Y"));
			obj.setR20_TOTAL(rs.getBigDecimal("R20_TOTAL"));
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_DAY1(rs.getBigDecimal("R21_DAY1"));
			obj.setR21_DAY2_7(rs.getBigDecimal("R21_DAY2_7"));
			obj.setR21_DAY8_14(rs.getBigDecimal("R21_DAY8_14"));
			obj.setR21_DAY15_30(rs.getBigDecimal("R21_DAY15_30"));
			obj.setR21_DAY31_TO_2M(rs.getBigDecimal("R21_DAY31_TO_2M"));
			obj.setR21_MORE2M_TO_3M(rs.getBigDecimal("R21_MORE2M_TO_3M"));
			obj.setR21_OVER3M_TO_6M(rs.getBigDecimal("R21_OVER3M_TO_6M"));
			obj.setR21_OVER6M_TO_1Y(rs.getBigDecimal("R21_OVER6M_TO_1Y"));
			obj.setR21_OVER1Y_TO_3Y(rs.getBigDecimal("R21_OVER1Y_TO_3Y"));
			obj.setR21_OVER3Y_TO_5Y(rs.getBigDecimal("R21_OVER3Y_TO_5Y"));
			obj.setR21_OVER5Y(rs.getBigDecimal("R21_OVER5Y"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));
			obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
			obj.setR22_DAY1(rs.getBigDecimal("R22_DAY1"));
			obj.setR22_DAY2_7(rs.getBigDecimal("R22_DAY2_7"));
			obj.setR22_DAY8_14(rs.getBigDecimal("R22_DAY8_14"));
			obj.setR22_DAY15_30(rs.getBigDecimal("R22_DAY15_30"));
			obj.setR22_DAY31_TO_2M(rs.getBigDecimal("R22_DAY31_TO_2M"));
			obj.setR22_MORE2M_TO_3M(rs.getBigDecimal("R22_MORE2M_TO_3M"));
			obj.setR22_OVER3M_TO_6M(rs.getBigDecimal("R22_OVER3M_TO_6M"));
			obj.setR22_OVER6M_TO_1Y(rs.getBigDecimal("R22_OVER6M_TO_1Y"));
			obj.setR22_OVER1Y_TO_3Y(rs.getBigDecimal("R22_OVER1Y_TO_3Y"));
			obj.setR22_OVER3Y_TO_5Y(rs.getBigDecimal("R22_OVER3Y_TO_5Y"));
			obj.setR22_OVER5Y(rs.getBigDecimal("R22_OVER5Y"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));
			obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
			obj.setR23_DAY1(rs.getBigDecimal("R23_DAY1"));
			obj.setR23_DAY2_7(rs.getBigDecimal("R23_DAY2_7"));
			obj.setR23_DAY8_14(rs.getBigDecimal("R23_DAY8_14"));
			obj.setR23_DAY15_30(rs.getBigDecimal("R23_DAY15_30"));
			obj.setR23_DAY31_TO_2M(rs.getBigDecimal("R23_DAY31_TO_2M"));
			obj.setR23_MORE2M_TO_3M(rs.getBigDecimal("R23_MORE2M_TO_3M"));
			obj.setR23_OVER3M_TO_6M(rs.getBigDecimal("R23_OVER3M_TO_6M"));
			obj.setR23_OVER6M_TO_1Y(rs.getBigDecimal("R23_OVER6M_TO_1Y"));
			obj.setR23_OVER1Y_TO_3Y(rs.getBigDecimal("R23_OVER1Y_TO_3Y"));
			obj.setR23_OVER3Y_TO_5Y(rs.getBigDecimal("R23_OVER3Y_TO_5Y"));
			obj.setR23_OVER5Y(rs.getBigDecimal("R23_OVER5Y"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
			obj.setR24_DAY1(rs.getBigDecimal("R24_DAY1"));
			obj.setR24_DAY2_7(rs.getBigDecimal("R24_DAY2_7"));
			obj.setR24_DAY8_14(rs.getBigDecimal("R24_DAY8_14"));
			obj.setR24_DAY15_30(rs.getBigDecimal("R24_DAY15_30"));
			obj.setR24_DAY31_TO_2M(rs.getBigDecimal("R24_DAY31_TO_2M"));
			obj.setR24_MORE2M_TO_3M(rs.getBigDecimal("R24_MORE2M_TO_3M"));
			obj.setR24_OVER3M_TO_6M(rs.getBigDecimal("R24_OVER3M_TO_6M"));
			obj.setR24_OVER6M_TO_1Y(rs.getBigDecimal("R24_OVER6M_TO_1Y"));
			obj.setR24_OVER1Y_TO_3Y(rs.getBigDecimal("R24_OVER1Y_TO_3Y"));
			obj.setR24_OVER3Y_TO_5Y(rs.getBigDecimal("R24_OVER3Y_TO_5Y"));
			obj.setR24_OVER5Y(rs.getBigDecimal("R24_OVER5Y"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
			obj.setR25_DAY1(rs.getBigDecimal("R25_DAY1"));
			obj.setR25_DAY2_7(rs.getBigDecimal("R25_DAY2_7"));
			obj.setR25_DAY8_14(rs.getBigDecimal("R25_DAY8_14"));
			obj.setR25_DAY15_30(rs.getBigDecimal("R25_DAY15_30"));
			obj.setR25_DAY31_TO_2M(rs.getBigDecimal("R25_DAY31_TO_2M"));
			obj.setR25_MORE2M_TO_3M(rs.getBigDecimal("R25_MORE2M_TO_3M"));
			obj.setR25_OVER3M_TO_6M(rs.getBigDecimal("R25_OVER3M_TO_6M"));
			obj.setR25_OVER6M_TO_1Y(rs.getBigDecimal("R25_OVER6M_TO_1Y"));
			obj.setR25_OVER1Y_TO_3Y(rs.getBigDecimal("R25_OVER1Y_TO_3Y"));
			obj.setR25_OVER3Y_TO_5Y(rs.getBigDecimal("R25_OVER3Y_TO_5Y"));
			obj.setR25_OVER5Y(rs.getBigDecimal("R25_OVER5Y"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_DAY1(rs.getBigDecimal("R26_DAY1"));
			obj.setR26_DAY2_7(rs.getBigDecimal("R26_DAY2_7"));
			obj.setR26_DAY8_14(rs.getBigDecimal("R26_DAY8_14"));
			obj.setR26_DAY15_30(rs.getBigDecimal("R26_DAY15_30"));
			obj.setR26_DAY31_TO_2M(rs.getBigDecimal("R26_DAY31_TO_2M"));
			obj.setR26_MORE2M_TO_3M(rs.getBigDecimal("R26_MORE2M_TO_3M"));
			obj.setR26_OVER3M_TO_6M(rs.getBigDecimal("R26_OVER3M_TO_6M"));
			obj.setR26_OVER6M_TO_1Y(rs.getBigDecimal("R26_OVER6M_TO_1Y"));
			obj.setR26_OVER1Y_TO_3Y(rs.getBigDecimal("R26_OVER1Y_TO_3Y"));
			obj.setR26_OVER3Y_TO_5Y(rs.getBigDecimal("R26_OVER3Y_TO_5Y"));
			obj.setR26_OVER5Y(rs.getBigDecimal("R26_OVER5Y"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_DAY1(rs.getBigDecimal("R27_DAY1"));
			obj.setR27_DAY2_7(rs.getBigDecimal("R27_DAY2_7"));
			obj.setR27_DAY8_14(rs.getBigDecimal("R27_DAY8_14"));
			obj.setR27_DAY15_30(rs.getBigDecimal("R27_DAY15_30"));
			obj.setR27_DAY31_TO_2M(rs.getBigDecimal("R27_DAY31_TO_2M"));
			obj.setR27_MORE2M_TO_3M(rs.getBigDecimal("R27_MORE2M_TO_3M"));
			obj.setR27_OVER3M_TO_6M(rs.getBigDecimal("R27_OVER3M_TO_6M"));
			obj.setR27_OVER6M_TO_1Y(rs.getBigDecimal("R27_OVER6M_TO_1Y"));
			obj.setR27_OVER1Y_TO_3Y(rs.getBigDecimal("R27_OVER1Y_TO_3Y"));
			obj.setR27_OVER3Y_TO_5Y(rs.getBigDecimal("R27_OVER3Y_TO_5Y"));
			obj.setR27_OVER5Y(rs.getBigDecimal("R27_OVER5Y"));
			obj.setR27_TOTAL(rs.getBigDecimal("R27_TOTAL"));
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_DAY1(rs.getBigDecimal("R28_DAY1"));
			obj.setR28_DAY2_7(rs.getBigDecimal("R28_DAY2_7"));
			obj.setR28_DAY8_14(rs.getBigDecimal("R28_DAY8_14"));
			obj.setR28_DAY15_30(rs.getBigDecimal("R28_DAY15_30"));
			obj.setR28_DAY31_TO_2M(rs.getBigDecimal("R28_DAY31_TO_2M"));
			obj.setR28_MORE2M_TO_3M(rs.getBigDecimal("R28_MORE2M_TO_3M"));
			obj.setR28_OVER3M_TO_6M(rs.getBigDecimal("R28_OVER3M_TO_6M"));
			obj.setR28_OVER6M_TO_1Y(rs.getBigDecimal("R28_OVER6M_TO_1Y"));
			obj.setR28_OVER1Y_TO_3Y(rs.getBigDecimal("R28_OVER1Y_TO_3Y"));
			obj.setR28_OVER3Y_TO_5Y(rs.getBigDecimal("R28_OVER3Y_TO_5Y"));
			obj.setR28_OVER5Y(rs.getBigDecimal("R28_OVER5Y"));
			obj.setR28_TOTAL(rs.getBigDecimal("R28_TOTAL"));
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_DAY1(rs.getBigDecimal("R29_DAY1"));
			obj.setR29_DAY2_7(rs.getBigDecimal("R29_DAY2_7"));
			obj.setR29_DAY8_14(rs.getBigDecimal("R29_DAY8_14"));
			obj.setR29_DAY15_30(rs.getBigDecimal("R29_DAY15_30"));
			obj.setR29_DAY31_TO_2M(rs.getBigDecimal("R29_DAY31_TO_2M"));
			obj.setR29_MORE2M_TO_3M(rs.getBigDecimal("R29_MORE2M_TO_3M"));
			obj.setR29_OVER3M_TO_6M(rs.getBigDecimal("R29_OVER3M_TO_6M"));
			obj.setR29_OVER6M_TO_1Y(rs.getBigDecimal("R29_OVER6M_TO_1Y"));
			obj.setR29_OVER1Y_TO_3Y(rs.getBigDecimal("R29_OVER1Y_TO_3Y"));
			obj.setR29_OVER3Y_TO_5Y(rs.getBigDecimal("R29_OVER3Y_TO_5Y"));
			obj.setR29_OVER5Y(rs.getBigDecimal("R29_OVER5Y"));
			obj.setR29_TOTAL(rs.getBigDecimal("R29_TOTAL"));
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_DAY1(rs.getBigDecimal("R30_DAY1"));
			obj.setR30_DAY2_7(rs.getBigDecimal("R30_DAY2_7"));
			obj.setR30_DAY8_14(rs.getBigDecimal("R30_DAY8_14"));
			obj.setR30_DAY15_30(rs.getBigDecimal("R30_DAY15_30"));
			obj.setR30_DAY31_TO_2M(rs.getBigDecimal("R30_DAY31_TO_2M"));
			obj.setR30_MORE2M_TO_3M(rs.getBigDecimal("R30_MORE2M_TO_3M"));
			obj.setR30_OVER3M_TO_6M(rs.getBigDecimal("R30_OVER3M_TO_6M"));
			obj.setR30_OVER6M_TO_1Y(rs.getBigDecimal("R30_OVER6M_TO_1Y"));
			obj.setR30_OVER1Y_TO_3Y(rs.getBigDecimal("R30_OVER1Y_TO_3Y"));
			obj.setR30_OVER3Y_TO_5Y(rs.getBigDecimal("R30_OVER3Y_TO_5Y"));
			obj.setR30_OVER5Y(rs.getBigDecimal("R30_OVER5Y"));
			obj.setR30_TOTAL(rs.getBigDecimal("R30_TOTAL"));
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_DAY1(rs.getBigDecimal("R31_DAY1"));
			obj.setR31_DAY2_7(rs.getBigDecimal("R31_DAY2_7"));
			obj.setR31_DAY8_14(rs.getBigDecimal("R31_DAY8_14"));
			obj.setR31_DAY15_30(rs.getBigDecimal("R31_DAY15_30"));
			obj.setR31_DAY31_TO_2M(rs.getBigDecimal("R31_DAY31_TO_2M"));
			obj.setR31_MORE2M_TO_3M(rs.getBigDecimal("R31_MORE2M_TO_3M"));
			obj.setR31_OVER3M_TO_6M(rs.getBigDecimal("R31_OVER3M_TO_6M"));
			obj.setR31_OVER6M_TO_1Y(rs.getBigDecimal("R31_OVER6M_TO_1Y"));
			obj.setR31_OVER1Y_TO_3Y(rs.getBigDecimal("R31_OVER1Y_TO_3Y"));
			obj.setR31_OVER3Y_TO_5Y(rs.getBigDecimal("R31_OVER3Y_TO_5Y"));
			obj.setR31_OVER5Y(rs.getBigDecimal("R31_OVER5Y"));
			obj.setR31_TOTAL(rs.getBigDecimal("R31_TOTAL"));
			obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
			obj.setR32_DAY1(rs.getBigDecimal("R32_DAY1"));
			obj.setR32_DAY2_7(rs.getBigDecimal("R32_DAY2_7"));
			obj.setR32_DAY8_14(rs.getBigDecimal("R32_DAY8_14"));
			obj.setR32_DAY15_30(rs.getBigDecimal("R32_DAY15_30"));
			obj.setR32_DAY31_TO_2M(rs.getBigDecimal("R32_DAY31_TO_2M"));
			obj.setR32_MORE2M_TO_3M(rs.getBigDecimal("R32_MORE2M_TO_3M"));
			obj.setR32_OVER3M_TO_6M(rs.getBigDecimal("R32_OVER3M_TO_6M"));
			obj.setR32_OVER6M_TO_1Y(rs.getBigDecimal("R32_OVER6M_TO_1Y"));
			obj.setR32_OVER1Y_TO_3Y(rs.getBigDecimal("R32_OVER1Y_TO_3Y"));
			obj.setR32_OVER3Y_TO_5Y(rs.getBigDecimal("R32_OVER3Y_TO_5Y"));
			obj.setR32_OVER5Y(rs.getBigDecimal("R32_OVER5Y"));
			obj.setR32_TOTAL(rs.getBigDecimal("R32_TOTAL"));
			obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
			obj.setR33_DAY1(rs.getBigDecimal("R33_DAY1"));
			obj.setR33_DAY2_7(rs.getBigDecimal("R33_DAY2_7"));
			obj.setR33_DAY8_14(rs.getBigDecimal("R33_DAY8_14"));
			obj.setR33_DAY15_30(rs.getBigDecimal("R33_DAY15_30"));
			obj.setR33_DAY31_TO_2M(rs.getBigDecimal("R33_DAY31_TO_2M"));
			obj.setR33_MORE2M_TO_3M(rs.getBigDecimal("R33_MORE2M_TO_3M"));
			obj.setR33_OVER3M_TO_6M(rs.getBigDecimal("R33_OVER3M_TO_6M"));
			obj.setR33_OVER6M_TO_1Y(rs.getBigDecimal("R33_OVER6M_TO_1Y"));
			obj.setR33_OVER1Y_TO_3Y(rs.getBigDecimal("R33_OVER1Y_TO_3Y"));
			obj.setR33_OVER3Y_TO_5Y(rs.getBigDecimal("R33_OVER3Y_TO_5Y"));
			obj.setR33_OVER5Y(rs.getBigDecimal("R33_OVER5Y"));
			obj.setR33_TOTAL(rs.getBigDecimal("R33_TOTAL"));
			obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
			obj.setR34_DAY1(rs.getBigDecimal("R34_DAY1"));
			obj.setR34_DAY2_7(rs.getBigDecimal("R34_DAY2_7"));
			obj.setR34_DAY8_14(rs.getBigDecimal("R34_DAY8_14"));
			obj.setR34_DAY15_30(rs.getBigDecimal("R34_DAY15_30"));
			obj.setR34_DAY31_TO_2M(rs.getBigDecimal("R34_DAY31_TO_2M"));
			obj.setR34_MORE2M_TO_3M(rs.getBigDecimal("R34_MORE2M_TO_3M"));
			obj.setR34_OVER3M_TO_6M(rs.getBigDecimal("R34_OVER3M_TO_6M"));
			obj.setR34_OVER6M_TO_1Y(rs.getBigDecimal("R34_OVER6M_TO_1Y"));
			obj.setR34_OVER1Y_TO_3Y(rs.getBigDecimal("R34_OVER1Y_TO_3Y"));
			obj.setR34_OVER3Y_TO_5Y(rs.getBigDecimal("R34_OVER3Y_TO_5Y"));
			obj.setR34_OVER5Y(rs.getBigDecimal("R34_OVER5Y"));
			obj.setR34_TOTAL(rs.getBigDecimal("R34_TOTAL"));
			obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
			obj.setR35_DAY1(rs.getBigDecimal("R35_DAY1"));
			obj.setR35_DAY2_7(rs.getBigDecimal("R35_DAY2_7"));
			obj.setR35_DAY8_14(rs.getBigDecimal("R35_DAY8_14"));
			obj.setR35_DAY15_30(rs.getBigDecimal("R35_DAY15_30"));
			obj.setR35_DAY31_TO_2M(rs.getBigDecimal("R35_DAY31_TO_2M"));
			obj.setR35_MORE2M_TO_3M(rs.getBigDecimal("R35_MORE2M_TO_3M"));
			obj.setR35_OVER3M_TO_6M(rs.getBigDecimal("R35_OVER3M_TO_6M"));
			obj.setR35_OVER6M_TO_1Y(rs.getBigDecimal("R35_OVER6M_TO_1Y"));
			obj.setR35_OVER1Y_TO_3Y(rs.getBigDecimal("R35_OVER1Y_TO_3Y"));
			obj.setR35_OVER3Y_TO_5Y(rs.getBigDecimal("R35_OVER3Y_TO_5Y"));
			obj.setR35_OVER5Y(rs.getBigDecimal("R35_OVER5Y"));
			obj.setR35_TOTAL(rs.getBigDecimal("R35_TOTAL"));
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_DAY1(rs.getBigDecimal("R36_DAY1"));
			obj.setR36_DAY2_7(rs.getBigDecimal("R36_DAY2_7"));
			obj.setR36_DAY8_14(rs.getBigDecimal("R36_DAY8_14"));
			obj.setR36_DAY15_30(rs.getBigDecimal("R36_DAY15_30"));
			obj.setR36_DAY31_TO_2M(rs.getBigDecimal("R36_DAY31_TO_2M"));
			obj.setR36_MORE2M_TO_3M(rs.getBigDecimal("R36_MORE2M_TO_3M"));
			obj.setR36_OVER3M_TO_6M(rs.getBigDecimal("R36_OVER3M_TO_6M"));
			obj.setR36_OVER6M_TO_1Y(rs.getBigDecimal("R36_OVER6M_TO_1Y"));
			obj.setR36_OVER1Y_TO_3Y(rs.getBigDecimal("R36_OVER1Y_TO_3Y"));
			obj.setR36_OVER3Y_TO_5Y(rs.getBigDecimal("R36_OVER3Y_TO_5Y"));
			obj.setR36_OVER5Y(rs.getBigDecimal("R36_OVER5Y"));
			obj.setR36_TOTAL(rs.getBigDecimal("R36_TOTAL"));
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
			obj.setR37_DAY1(rs.getBigDecimal("R37_DAY1"));
			obj.setR37_DAY2_7(rs.getBigDecimal("R37_DAY2_7"));
			obj.setR37_DAY8_14(rs.getBigDecimal("R37_DAY8_14"));
			obj.setR37_DAY15_30(rs.getBigDecimal("R37_DAY15_30"));
			obj.setR37_DAY31_TO_2M(rs.getBigDecimal("R37_DAY31_TO_2M"));
			obj.setR37_MORE2M_TO_3M(rs.getBigDecimal("R37_MORE2M_TO_3M"));
			obj.setR37_OVER3M_TO_6M(rs.getBigDecimal("R37_OVER3M_TO_6M"));
			obj.setR37_OVER6M_TO_1Y(rs.getBigDecimal("R37_OVER6M_TO_1Y"));
			obj.setR37_OVER1Y_TO_3Y(rs.getBigDecimal("R37_OVER1Y_TO_3Y"));
			obj.setR37_OVER3Y_TO_5Y(rs.getBigDecimal("R37_OVER3Y_TO_5Y"));
			obj.setR37_OVER5Y(rs.getBigDecimal("R37_OVER5Y"));
			obj.setR37_TOTAL(rs.getBigDecimal("R37_TOTAL"));
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
			obj.setR38_DAY1(rs.getBigDecimal("R38_DAY1"));
			obj.setR38_DAY2_7(rs.getBigDecimal("R38_DAY2_7"));
			obj.setR38_DAY8_14(rs.getBigDecimal("R38_DAY8_14"));
			obj.setR38_DAY15_30(rs.getBigDecimal("R38_DAY15_30"));
			obj.setR38_DAY31_TO_2M(rs.getBigDecimal("R38_DAY31_TO_2M"));
			obj.setR38_MORE2M_TO_3M(rs.getBigDecimal("R38_MORE2M_TO_3M"));
			obj.setR38_OVER3M_TO_6M(rs.getBigDecimal("R38_OVER3M_TO_6M"));
			obj.setR38_OVER6M_TO_1Y(rs.getBigDecimal("R38_OVER6M_TO_1Y"));
			obj.setR38_OVER1Y_TO_3Y(rs.getBigDecimal("R38_OVER1Y_TO_3Y"));
			obj.setR38_OVER3Y_TO_5Y(rs.getBigDecimal("R38_OVER3Y_TO_5Y"));
			obj.setR38_OVER5Y(rs.getBigDecimal("R38_OVER5Y"));
			obj.setR38_TOTAL(rs.getBigDecimal("R38_TOTAL"));
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
			obj.setR39_DAY1(rs.getBigDecimal("R39_DAY1"));
			obj.setR39_DAY2_7(rs.getBigDecimal("R39_DAY2_7"));
			obj.setR39_DAY8_14(rs.getBigDecimal("R39_DAY8_14"));
			obj.setR39_DAY15_30(rs.getBigDecimal("R39_DAY15_30"));
			obj.setR39_DAY31_TO_2M(rs.getBigDecimal("R39_DAY31_TO_2M"));
			obj.setR39_MORE2M_TO_3M(rs.getBigDecimal("R39_MORE2M_TO_3M"));
			obj.setR39_OVER3M_TO_6M(rs.getBigDecimal("R39_OVER3M_TO_6M"));
			obj.setR39_OVER6M_TO_1Y(rs.getBigDecimal("R39_OVER6M_TO_1Y"));
			obj.setR39_OVER1Y_TO_3Y(rs.getBigDecimal("R39_OVER1Y_TO_3Y"));
			obj.setR39_OVER3Y_TO_5Y(rs.getBigDecimal("R39_OVER3Y_TO_5Y"));
			obj.setR39_OVER5Y(rs.getBigDecimal("R39_OVER5Y"));
			obj.setR39_TOTAL(rs.getBigDecimal("R39_TOTAL"));
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
			obj.setR40_DAY1(rs.getBigDecimal("R40_DAY1"));
			obj.setR40_DAY2_7(rs.getBigDecimal("R40_DAY2_7"));
			obj.setR40_DAY8_14(rs.getBigDecimal("R40_DAY8_14"));
			obj.setR40_DAY15_30(rs.getBigDecimal("R40_DAY15_30"));
			obj.setR40_DAY31_TO_2M(rs.getBigDecimal("R40_DAY31_TO_2M"));
			obj.setR40_MORE2M_TO_3M(rs.getBigDecimal("R40_MORE2M_TO_3M"));
			obj.setR40_OVER3M_TO_6M(rs.getBigDecimal("R40_OVER3M_TO_6M"));
			obj.setR40_OVER6M_TO_1Y(rs.getBigDecimal("R40_OVER6M_TO_1Y"));
			obj.setR40_OVER1Y_TO_3Y(rs.getBigDecimal("R40_OVER1Y_TO_3Y"));
			obj.setR40_OVER3Y_TO_5Y(rs.getBigDecimal("R40_OVER3Y_TO_5Y"));
			obj.setR40_OVER5Y(rs.getBigDecimal("R40_OVER5Y"));
			obj.setR40_TOTAL(rs.getBigDecimal("R40_TOTAL"));
//			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
//			obj.setR41_DAY1(rs.getBigDecimal("R41_DAY1"));
//			obj.setR41_DAY2_7(rs.getBigDecimal("R41_DAY2_7"));
//			obj.setR41_DAY8_14(rs.getBigDecimal("R41_DAY8_14"));
//			obj.setR41_DAY15_30(rs.getBigDecimal("R41_DAY15_30"));
//			obj.setR41_DAY31_TO_2M(rs.getBigDecimal("R41_DAY31_TO_2M"));
//			obj.setR41_MORE2M_TO_3M(rs.getBigDecimal("R41_MORE2M_TO_3M"));
//			obj.setR41_OVER3M_TO_6M(rs.getBigDecimal("R41_OVER3M_TO_6M"));
//			obj.setR41_OVER6M_TO_1Y(rs.getBigDecimal("R41_OVER6M_TO_1Y"));
//			obj.setR41_OVER1Y_TO_3Y(rs.getBigDecimal("R41_OVER1Y_TO_3Y"));
//			obj.setR41_OVER3Y_TO_5Y(rs.getBigDecimal("R41_OVER3Y_TO_5Y"));
//			obj.setR41_OVER5Y(rs.getBigDecimal("R41_OVER5Y"));
//			obj.setR41_TOTAL(rs.getBigDecimal("R41_TOTAL"));
//			obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
//			obj.setR42_DAY1(rs.getBigDecimal("R42_DAY1"));
//			obj.setR42_DAY2_7(rs.getBigDecimal("R42_DAY2_7"));
//			obj.setR42_DAY8_14(rs.getBigDecimal("R42_DAY8_14"));
//			obj.setR42_DAY15_30(rs.getBigDecimal("R42_DAY15_30"));
//			obj.setR42_DAY31_TO_2M(rs.getBigDecimal("R42_DAY31_TO_2M"));
//			obj.setR42_MORE2M_TO_3M(rs.getBigDecimal("R42_MORE2M_TO_3M"));
//			obj.setR42_OVER3M_TO_6M(rs.getBigDecimal("R42_OVER3M_TO_6M"));
//			obj.setR42_OVER6M_TO_1Y(rs.getBigDecimal("R42_OVER6M_TO_1Y"));
//			obj.setR42_OVER1Y_TO_3Y(rs.getBigDecimal("R42_OVER1Y_TO_3Y"));
//			obj.setR42_OVER3Y_TO_5Y(rs.getBigDecimal("R42_OVER3Y_TO_5Y"));
//			obj.setR42_OVER5Y(rs.getBigDecimal("R42_OVER5Y"));
//			obj.setR42_TOTAL(rs.getBigDecimal("R42_TOTAL"));
//			obj.setR43_PRODUCT(rs.getString("R43_PRODUCT"));
//			obj.setR43_DAY1(rs.getBigDecimal("R43_DAY1"));
//			obj.setR43_DAY2_7(rs.getBigDecimal("R43_DAY2_7"));
//			obj.setR43_DAY8_14(rs.getBigDecimal("R43_DAY8_14"));
//			obj.setR43_DAY15_30(rs.getBigDecimal("R43_DAY15_30"));
//			obj.setR43_DAY31_TO_2M(rs.getBigDecimal("R43_DAY31_TO_2M"));
//			obj.setR43_MORE2M_TO_3M(rs.getBigDecimal("R43_MORE2M_TO_3M"));
//			obj.setR43_OVER3M_TO_6M(rs.getBigDecimal("R43_OVER3M_TO_6M"));
//			obj.setR43_OVER6M_TO_1Y(rs.getBigDecimal("R43_OVER6M_TO_1Y"));
//			obj.setR43_OVER1Y_TO_3Y(rs.getBigDecimal("R43_OVER1Y_TO_3Y"));
//			obj.setR43_OVER3Y_TO_5Y(rs.getBigDecimal("R43_OVER3Y_TO_5Y"));
//			obj.setR43_OVER5Y(rs.getBigDecimal("R43_OVER5Y"));
//			obj.setR43_TOTAL(rs.getBigDecimal("R43_TOTAL"));
//			obj.setR44_PRODUCT(rs.getString("R44_PRODUCT"));
//			obj.setR44_DAY1(rs.getBigDecimal("R44_DAY1"));
//			obj.setR44_DAY2_7(rs.getBigDecimal("R44_DAY2_7"));
//			obj.setR44_DAY8_14(rs.getBigDecimal("R44_DAY8_14"));
//			obj.setR44_DAY15_30(rs.getBigDecimal("R44_DAY15_30"));
//			obj.setR44_DAY31_TO_2M(rs.getBigDecimal("R44_DAY31_TO_2M"));
//			obj.setR44_MORE2M_TO_3M(rs.getBigDecimal("R44_MORE2M_TO_3M"));
//			obj.setR44_OVER3M_TO_6M(rs.getBigDecimal("R44_OVER3M_TO_6M"));
//			obj.setR44_OVER6M_TO_1Y(rs.getBigDecimal("R44_OVER6M_TO_1Y"));
//			obj.setR44_OVER1Y_TO_3Y(rs.getBigDecimal("R44_OVER1Y_TO_3Y"));
//			obj.setR44_OVER3Y_TO_5Y(rs.getBigDecimal("R44_OVER3Y_TO_5Y"));
//			obj.setR44_OVER5Y(rs.getBigDecimal("R44_OVER5Y"));
//			obj.setR44_TOTAL(rs.getBigDecimal("R44_TOTAL"));
//			obj.setR45_PRODUCT(rs.getString("R45_PRODUCT"));
//			obj.setR45_DAY1(rs.getBigDecimal("R45_DAY1"));
//			obj.setR45_DAY2_7(rs.getBigDecimal("R45_DAY2_7"));
//			obj.setR45_DAY8_14(rs.getBigDecimal("R45_DAY8_14"));
//			obj.setR45_DAY15_30(rs.getBigDecimal("R45_DAY15_30"));
//			obj.setR45_DAY31_TO_2M(rs.getBigDecimal("R45_DAY31_TO_2M"));
//			obj.setR45_MORE2M_TO_3M(rs.getBigDecimal("R45_MORE2M_TO_3M"));
//			obj.setR45_OVER3M_TO_6M(rs.getBigDecimal("R45_OVER3M_TO_6M"));
//			obj.setR45_OVER6M_TO_1Y(rs.getBigDecimal("R45_OVER6M_TO_1Y"));
//			obj.setR45_OVER1Y_TO_3Y(rs.getBigDecimal("R45_OVER1Y_TO_3Y"));
//			obj.setR45_OVER3Y_TO_5Y(rs.getBigDecimal("R45_OVER3Y_TO_5Y"));
//			obj.setR45_OVER5Y(rs.getBigDecimal("R45_OVER5Y"));
//			obj.setR45_TOTAL(rs.getBigDecimal("R45_TOTAL"));
//			obj.setR46_PRODUCT(rs.getString("R46_PRODUCT"));
//			obj.setR46_DAY1(rs.getBigDecimal("R46_DAY1"));
//			obj.setR46_DAY2_7(rs.getBigDecimal("R46_DAY2_7"));
//			obj.setR46_DAY8_14(rs.getBigDecimal("R46_DAY8_14"));
//			obj.setR46_DAY15_30(rs.getBigDecimal("R46_DAY15_30"));
//			obj.setR46_DAY31_TO_2M(rs.getBigDecimal("R46_DAY31_TO_2M"));
//			obj.setR46_MORE2M_TO_3M(rs.getBigDecimal("R46_MORE2M_TO_3M"));
//			obj.setR46_OVER3M_TO_6M(rs.getBigDecimal("R46_OVER3M_TO_6M"));
//			obj.setR46_OVER6M_TO_1Y(rs.getBigDecimal("R46_OVER6M_TO_1Y"));
//			obj.setR46_OVER1Y_TO_3Y(rs.getBigDecimal("R46_OVER1Y_TO_3Y"));
//			obj.setR46_OVER3Y_TO_5Y(rs.getBigDecimal("R46_OVER3Y_TO_5Y"));
//			obj.setR46_OVER5Y(rs.getBigDecimal("R46_OVER5Y"));
//			obj.setR46_TOTAL(rs.getBigDecimal("R46_TOTAL"));
			obj.setR47_PRODUCT(rs.getString("R47_PRODUCT"));
			obj.setR47_DAY1(rs.getBigDecimal("R47_DAY1"));
			obj.setR47_DAY2_7(rs.getBigDecimal("R47_DAY2_7"));
			obj.setR47_DAY8_14(rs.getBigDecimal("R47_DAY8_14"));
			obj.setR47_DAY15_30(rs.getBigDecimal("R47_DAY15_30"));
			obj.setR47_DAY31_TO_2M(rs.getBigDecimal("R47_DAY31_TO_2M"));
			obj.setR47_MORE2M_TO_3M(rs.getBigDecimal("R47_MORE2M_TO_3M"));
			obj.setR47_OVER3M_TO_6M(rs.getBigDecimal("R47_OVER3M_TO_6M"));
			obj.setR47_OVER6M_TO_1Y(rs.getBigDecimal("R47_OVER6M_TO_1Y"));
			obj.setR47_OVER1Y_TO_3Y(rs.getBigDecimal("R47_OVER1Y_TO_3Y"));
			obj.setR47_OVER3Y_TO_5Y(rs.getBigDecimal("R47_OVER3Y_TO_5Y"));
			obj.setR47_OVER5Y(rs.getBigDecimal("R47_OVER5Y"));
			obj.setR47_TOTAL(rs.getBigDecimal("R47_TOTAL"));
			obj.setR48_PRODUCT(rs.getString("R48_PRODUCT"));
			obj.setR48_DAY1(rs.getBigDecimal("R48_DAY1"));
			obj.setR48_DAY2_7(rs.getBigDecimal("R48_DAY2_7"));
			obj.setR48_DAY8_14(rs.getBigDecimal("R48_DAY8_14"));
			obj.setR48_DAY15_30(rs.getBigDecimal("R48_DAY15_30"));
			obj.setR48_DAY31_TO_2M(rs.getBigDecimal("R48_DAY31_TO_2M"));
			obj.setR48_MORE2M_TO_3M(rs.getBigDecimal("R48_MORE2M_TO_3M"));
			obj.setR48_OVER3M_TO_6M(rs.getBigDecimal("R48_OVER3M_TO_6M"));
			obj.setR48_OVER6M_TO_1Y(rs.getBigDecimal("R48_OVER6M_TO_1Y"));
			obj.setR48_OVER1Y_TO_3Y(rs.getBigDecimal("R48_OVER1Y_TO_3Y"));
			obj.setR48_OVER3Y_TO_5Y(rs.getBigDecimal("R48_OVER3Y_TO_5Y"));
			obj.setR48_OVER5Y(rs.getBigDecimal("R48_OVER5Y"));
			obj.setR48_TOTAL(rs.getBigDecimal("R48_TOTAL"));
			obj.setR49_PRODUCT(rs.getString("R49_PRODUCT"));
			obj.setR49_DAY1(rs.getBigDecimal("R49_DAY1"));
			obj.setR49_DAY2_7(rs.getBigDecimal("R49_DAY2_7"));
			obj.setR49_DAY8_14(rs.getBigDecimal("R49_DAY8_14"));
			obj.setR49_DAY15_30(rs.getBigDecimal("R49_DAY15_30"));
			obj.setR49_DAY31_TO_2M(rs.getBigDecimal("R49_DAY31_TO_2M"));
			obj.setR49_MORE2M_TO_3M(rs.getBigDecimal("R49_MORE2M_TO_3M"));
			obj.setR49_OVER3M_TO_6M(rs.getBigDecimal("R49_OVER3M_TO_6M"));
			obj.setR49_OVER6M_TO_1Y(rs.getBigDecimal("R49_OVER6M_TO_1Y"));
			obj.setR49_OVER1Y_TO_3Y(rs.getBigDecimal("R49_OVER1Y_TO_3Y"));
			obj.setR49_OVER3Y_TO_5Y(rs.getBigDecimal("R49_OVER3Y_TO_5Y"));
			obj.setR49_OVER5Y(rs.getBigDecimal("R49_OVER5Y"));
			obj.setR49_TOTAL(rs.getBigDecimal("R49_TOTAL"));
			obj.setR50_PRODUCT(rs.getString("R50_PRODUCT"));
			obj.setR50_DAY1(rs.getBigDecimal("R50_DAY1"));
			obj.setR50_DAY2_7(rs.getBigDecimal("R50_DAY2_7"));
			obj.setR50_DAY8_14(rs.getBigDecimal("R50_DAY8_14"));
			obj.setR50_DAY15_30(rs.getBigDecimal("R50_DAY15_30"));
			obj.setR50_DAY31_TO_2M(rs.getBigDecimal("R50_DAY31_TO_2M"));
			obj.setR50_MORE2M_TO_3M(rs.getBigDecimal("R50_MORE2M_TO_3M"));
			obj.setR50_OVER3M_TO_6M(rs.getBigDecimal("R50_OVER3M_TO_6M"));
			obj.setR50_OVER6M_TO_1Y(rs.getBigDecimal("R50_OVER6M_TO_1Y"));
			obj.setR50_OVER1Y_TO_3Y(rs.getBigDecimal("R50_OVER1Y_TO_3Y"));
			obj.setR50_OVER3Y_TO_5Y(rs.getBigDecimal("R50_OVER3Y_TO_5Y"));
			obj.setR50_OVER5Y(rs.getBigDecimal("R50_OVER5Y"));
			obj.setR50_TOTAL(rs.getBigDecimal("R50_TOTAL"));
			obj.setR51_PRODUCT(rs.getString("R51_PRODUCT"));
			obj.setR51_DAY1(rs.getBigDecimal("R51_DAY1"));
			obj.setR51_DAY2_7(rs.getBigDecimal("R51_DAY2_7"));
			obj.setR51_DAY8_14(rs.getBigDecimal("R51_DAY8_14"));
			obj.setR51_DAY15_30(rs.getBigDecimal("R51_DAY15_30"));
			obj.setR51_DAY31_TO_2M(rs.getBigDecimal("R51_DAY31_TO_2M"));
			obj.setR51_MORE2M_TO_3M(rs.getBigDecimal("R51_MORE2M_TO_3M"));
			obj.setR51_OVER3M_TO_6M(rs.getBigDecimal("R51_OVER3M_TO_6M"));
			obj.setR51_OVER6M_TO_1Y(rs.getBigDecimal("R51_OVER6M_TO_1Y"));
			obj.setR51_OVER1Y_TO_3Y(rs.getBigDecimal("R51_OVER1Y_TO_3Y"));
			obj.setR51_OVER3Y_TO_5Y(rs.getBigDecimal("R51_OVER3Y_TO_5Y"));
			obj.setR51_OVER5Y(rs.getBigDecimal("R51_OVER5Y"));
			obj.setR51_TOTAL(rs.getBigDecimal("R51_TOTAL"));
			obj.setR52_PRODUCT(rs.getString("R52_PRODUCT"));
			obj.setR52_DAY1(rs.getBigDecimal("R52_DAY1"));
			obj.setR52_DAY2_7(rs.getBigDecimal("R52_DAY2_7"));
			obj.setR52_DAY8_14(rs.getBigDecimal("R52_DAY8_14"));
			obj.setR52_DAY15_30(rs.getBigDecimal("R52_DAY15_30"));
			obj.setR52_DAY31_TO_2M(rs.getBigDecimal("R52_DAY31_TO_2M"));
			obj.setR52_MORE2M_TO_3M(rs.getBigDecimal("R52_MORE2M_TO_3M"));
			obj.setR52_OVER3M_TO_6M(rs.getBigDecimal("R52_OVER3M_TO_6M"));
			obj.setR52_OVER6M_TO_1Y(rs.getBigDecimal("R52_OVER6M_TO_1Y"));
			obj.setR52_OVER1Y_TO_3Y(rs.getBigDecimal("R52_OVER1Y_TO_3Y"));
			obj.setR52_OVER3Y_TO_5Y(rs.getBigDecimal("R52_OVER3Y_TO_5Y"));
			obj.setR52_OVER5Y(rs.getBigDecimal("R52_OVER5Y"));
			obj.setR52_TOTAL(rs.getBigDecimal("R52_TOTAL"));
			obj.setR53_PRODUCT(rs.getString("R53_PRODUCT"));
			obj.setR53_DAY1(rs.getBigDecimal("R53_DAY1"));
			obj.setR53_DAY2_7(rs.getBigDecimal("R53_DAY2_7"));
			obj.setR53_DAY8_14(rs.getBigDecimal("R53_DAY8_14"));
			obj.setR53_DAY15_30(rs.getBigDecimal("R53_DAY15_30"));
			obj.setR53_DAY31_TO_2M(rs.getBigDecimal("R53_DAY31_TO_2M"));
			obj.setR53_MORE2M_TO_3M(rs.getBigDecimal("R53_MORE2M_TO_3M"));
			obj.setR53_OVER3M_TO_6M(rs.getBigDecimal("R53_OVER3M_TO_6M"));
			obj.setR53_OVER6M_TO_1Y(rs.getBigDecimal("R53_OVER6M_TO_1Y"));
			obj.setR53_OVER1Y_TO_3Y(rs.getBigDecimal("R53_OVER1Y_TO_3Y"));
			obj.setR53_OVER3Y_TO_5Y(rs.getBigDecimal("R53_OVER3Y_TO_5Y"));
			obj.setR53_OVER5Y(rs.getBigDecimal("R53_OVER5Y"));
			obj.setR53_TOTAL(rs.getBigDecimal("R53_TOTAL"));
			obj.setR54_PRODUCT(rs.getString("R54_PRODUCT"));
			obj.setR54_DAY1(rs.getBigDecimal("R54_DAY1"));
			obj.setR54_DAY2_7(rs.getBigDecimal("R54_DAY2_7"));
			obj.setR54_DAY8_14(rs.getBigDecimal("R54_DAY8_14"));
			obj.setR54_DAY15_30(rs.getBigDecimal("R54_DAY15_30"));
			obj.setR54_DAY31_TO_2M(rs.getBigDecimal("R54_DAY31_TO_2M"));
			obj.setR54_MORE2M_TO_3M(rs.getBigDecimal("R54_MORE2M_TO_3M"));
			obj.setR54_OVER3M_TO_6M(rs.getBigDecimal("R54_OVER3M_TO_6M"));
			obj.setR54_OVER6M_TO_1Y(rs.getBigDecimal("R54_OVER6M_TO_1Y"));
			obj.setR54_OVER1Y_TO_3Y(rs.getBigDecimal("R54_OVER1Y_TO_3Y"));
			obj.setR54_OVER3Y_TO_5Y(rs.getBigDecimal("R54_OVER3Y_TO_5Y"));
			obj.setR54_OVER5Y(rs.getBigDecimal("R54_OVER5Y"));
			obj.setR54_TOTAL(rs.getBigDecimal("R54_TOTAL"));
			obj.setR55_PRODUCT(rs.getString("R55_PRODUCT"));
			obj.setR55_DAY1(rs.getBigDecimal("R55_DAY1"));
			obj.setR55_DAY2_7(rs.getBigDecimal("R55_DAY2_7"));
			obj.setR55_DAY8_14(rs.getBigDecimal("R55_DAY8_14"));
			obj.setR55_DAY15_30(rs.getBigDecimal("R55_DAY15_30"));
			obj.setR55_DAY31_TO_2M(rs.getBigDecimal("R55_DAY31_TO_2M"));
			obj.setR55_MORE2M_TO_3M(rs.getBigDecimal("R55_MORE2M_TO_3M"));
			obj.setR55_OVER3M_TO_6M(rs.getBigDecimal("R55_OVER3M_TO_6M"));
			obj.setR55_OVER6M_TO_1Y(rs.getBigDecimal("R55_OVER6M_TO_1Y"));
			obj.setR55_OVER1Y_TO_3Y(rs.getBigDecimal("R55_OVER1Y_TO_3Y"));
			obj.setR55_OVER3Y_TO_5Y(rs.getBigDecimal("R55_OVER3Y_TO_5Y"));
			obj.setR55_OVER5Y(rs.getBigDecimal("R55_OVER5Y"));
			obj.setR55_TOTAL(rs.getBigDecimal("R55_TOTAL"));
			obj.setR56_PRODUCT(rs.getString("R56_PRODUCT"));
			obj.setR56_DAY1(rs.getBigDecimal("R56_DAY1"));
			obj.setR56_DAY2_7(rs.getBigDecimal("R56_DAY2_7"));
			obj.setR56_DAY8_14(rs.getBigDecimal("R56_DAY8_14"));
			obj.setR56_DAY15_30(rs.getBigDecimal("R56_DAY15_30"));
			obj.setR56_DAY31_TO_2M(rs.getBigDecimal("R56_DAY31_TO_2M"));
			obj.setR56_MORE2M_TO_3M(rs.getBigDecimal("R56_MORE2M_TO_3M"));
			obj.setR56_OVER3M_TO_6M(rs.getBigDecimal("R56_OVER3M_TO_6M"));
			obj.setR56_OVER6M_TO_1Y(rs.getBigDecimal("R56_OVER6M_TO_1Y"));
			obj.setR56_OVER1Y_TO_3Y(rs.getBigDecimal("R56_OVER1Y_TO_3Y"));
			obj.setR56_OVER3Y_TO_5Y(rs.getBigDecimal("R56_OVER3Y_TO_5Y"));
			obj.setR56_OVER5Y(rs.getBigDecimal("R56_OVER5Y"));
			obj.setR56_TOTAL(rs.getBigDecimal("R56_TOTAL"));
			obj.setR57_PRODUCT(rs.getString("R57_PRODUCT"));
			obj.setR57_DAY1(rs.getBigDecimal("R57_DAY1"));
			obj.setR57_DAY2_7(rs.getBigDecimal("R57_DAY2_7"));
			obj.setR57_DAY8_14(rs.getBigDecimal("R57_DAY8_14"));
			obj.setR57_DAY15_30(rs.getBigDecimal("R57_DAY15_30"));
			obj.setR57_DAY31_TO_2M(rs.getBigDecimal("R57_DAY31_TO_2M"));
			obj.setR57_MORE2M_TO_3M(rs.getBigDecimal("R57_MORE2M_TO_3M"));
			obj.setR57_OVER3M_TO_6M(rs.getBigDecimal("R57_OVER3M_TO_6M"));
			obj.setR57_OVER6M_TO_1Y(rs.getBigDecimal("R57_OVER6M_TO_1Y"));
			obj.setR57_OVER1Y_TO_3Y(rs.getBigDecimal("R57_OVER1Y_TO_3Y"));
			obj.setR57_OVER3Y_TO_5Y(rs.getBigDecimal("R57_OVER3Y_TO_5Y"));
			obj.setR57_OVER5Y(rs.getBigDecimal("R57_OVER5Y"));
			obj.setR57_TOTAL(rs.getBigDecimal("R57_TOTAL"));
			obj.setR58_PRODUCT(rs.getString("R58_PRODUCT"));
			obj.setR58_DAY1(rs.getBigDecimal("R58_DAY1"));
			obj.setR58_DAY2_7(rs.getBigDecimal("R58_DAY2_7"));
			obj.setR58_DAY8_14(rs.getBigDecimal("R58_DAY8_14"));
			obj.setR58_DAY15_30(rs.getBigDecimal("R58_DAY15_30"));
			obj.setR58_DAY31_TO_2M(rs.getBigDecimal("R58_DAY31_TO_2M"));
			obj.setR58_MORE2M_TO_3M(rs.getBigDecimal("R58_MORE2M_TO_3M"));
			obj.setR58_OVER3M_TO_6M(rs.getBigDecimal("R58_OVER3M_TO_6M"));
			obj.setR58_OVER6M_TO_1Y(rs.getBigDecimal("R58_OVER6M_TO_1Y"));
			obj.setR58_OVER1Y_TO_3Y(rs.getBigDecimal("R58_OVER1Y_TO_3Y"));
			obj.setR58_OVER3Y_TO_5Y(rs.getBigDecimal("R58_OVER3Y_TO_5Y"));
			obj.setR58_OVER5Y(rs.getBigDecimal("R58_OVER5Y"));
			obj.setR58_TOTAL(rs.getBigDecimal("R58_TOTAL"));
			obj.setR59_PRODUCT(rs.getString("R59_PRODUCT"));
			obj.setR59_DAY1(rs.getBigDecimal("R59_DAY1"));
			obj.setR59_DAY2_7(rs.getBigDecimal("R59_DAY2_7"));
			obj.setR59_DAY8_14(rs.getBigDecimal("R59_DAY8_14"));
			obj.setR59_DAY15_30(rs.getBigDecimal("R59_DAY15_30"));
			obj.setR59_DAY31_TO_2M(rs.getBigDecimal("R59_DAY31_TO_2M"));
			obj.setR59_MORE2M_TO_3M(rs.getBigDecimal("R59_MORE2M_TO_3M"));
			obj.setR59_OVER3M_TO_6M(rs.getBigDecimal("R59_OVER3M_TO_6M"));
			obj.setR59_OVER6M_TO_1Y(rs.getBigDecimal("R59_OVER6M_TO_1Y"));
			obj.setR59_OVER1Y_TO_3Y(rs.getBigDecimal("R59_OVER1Y_TO_3Y"));
			obj.setR59_OVER3Y_TO_5Y(rs.getBigDecimal("R59_OVER3Y_TO_5Y"));
			obj.setR59_OVER5Y(rs.getBigDecimal("R59_OVER5Y"));
			obj.setR59_TOTAL(rs.getBigDecimal("R59_TOTAL"));
			obj.setR60_PRODUCT(rs.getString("R60_PRODUCT"));
			obj.setR60_DAY1(rs.getBigDecimal("R60_DAY1"));
			obj.setR60_DAY2_7(rs.getBigDecimal("R60_DAY2_7"));
			obj.setR60_DAY8_14(rs.getBigDecimal("R60_DAY8_14"));
			obj.setR60_DAY15_30(rs.getBigDecimal("R60_DAY15_30"));
			obj.setR60_DAY31_TO_2M(rs.getBigDecimal("R60_DAY31_TO_2M"));
			obj.setR60_MORE2M_TO_3M(rs.getBigDecimal("R60_MORE2M_TO_3M"));
			obj.setR60_OVER3M_TO_6M(rs.getBigDecimal("R60_OVER3M_TO_6M"));
			obj.setR60_OVER6M_TO_1Y(rs.getBigDecimal("R60_OVER6M_TO_1Y"));
			obj.setR60_OVER1Y_TO_3Y(rs.getBigDecimal("R60_OVER1Y_TO_3Y"));
			obj.setR60_OVER3Y_TO_5Y(rs.getBigDecimal("R60_OVER3Y_TO_5Y"));
			obj.setR60_OVER5Y(rs.getBigDecimal("R60_OVER5Y"));
			obj.setR60_TOTAL(rs.getBigDecimal("R60_TOTAL"));
			obj.setR61_PRODUCT(rs.getString("R61_PRODUCT"));
			obj.setR61_DAY1(rs.getBigDecimal("R61_DAY1"));
			obj.setR61_DAY2_7(rs.getBigDecimal("R61_DAY2_7"));
			obj.setR61_DAY8_14(rs.getBigDecimal("R61_DAY8_14"));
			obj.setR61_DAY15_30(rs.getBigDecimal("R61_DAY15_30"));
			obj.setR61_DAY31_TO_2M(rs.getBigDecimal("R61_DAY31_TO_2M"));
			obj.setR61_MORE2M_TO_3M(rs.getBigDecimal("R61_MORE2M_TO_3M"));
			obj.setR61_OVER3M_TO_6M(rs.getBigDecimal("R61_OVER3M_TO_6M"));
			obj.setR61_OVER6M_TO_1Y(rs.getBigDecimal("R61_OVER6M_TO_1Y"));
			obj.setR61_OVER1Y_TO_3Y(rs.getBigDecimal("R61_OVER1Y_TO_3Y"));
			obj.setR61_OVER3Y_TO_5Y(rs.getBigDecimal("R61_OVER3Y_TO_5Y"));
			obj.setR61_OVER5Y(rs.getBigDecimal("R61_OVER5Y"));
			obj.setR61_TOTAL(rs.getBigDecimal("R61_TOTAL"));
			obj.setR62_PRODUCT(rs.getString("R62_PRODUCT"));
			obj.setR62_DAY1(rs.getBigDecimal("R62_DAY1"));
			obj.setR62_DAY2_7(rs.getBigDecimal("R62_DAY2_7"));
			obj.setR62_DAY8_14(rs.getBigDecimal("R62_DAY8_14"));
			obj.setR62_DAY15_30(rs.getBigDecimal("R62_DAY15_30"));
			obj.setR62_DAY31_TO_2M(rs.getBigDecimal("R62_DAY31_TO_2M"));
			obj.setR62_MORE2M_TO_3M(rs.getBigDecimal("R62_MORE2M_TO_3M"));
			obj.setR62_OVER3M_TO_6M(rs.getBigDecimal("R62_OVER3M_TO_6M"));
			obj.setR62_OVER6M_TO_1Y(rs.getBigDecimal("R62_OVER6M_TO_1Y"));
			obj.setR62_OVER1Y_TO_3Y(rs.getBigDecimal("R62_OVER1Y_TO_3Y"));
			obj.setR62_OVER3Y_TO_5Y(rs.getBigDecimal("R62_OVER3Y_TO_5Y"));
			obj.setR62_OVER5Y(rs.getBigDecimal("R62_OVER5Y"));
			obj.setR62_TOTAL(rs.getBigDecimal("R62_TOTAL"));
			obj.setR63_PRODUCT(rs.getString("R63_PRODUCT"));
			obj.setR63_DAY1(rs.getBigDecimal("R63_DAY1"));
			obj.setR63_DAY2_7(rs.getBigDecimal("R63_DAY2_7"));
			obj.setR63_DAY8_14(rs.getBigDecimal("R63_DAY8_14"));
			obj.setR63_DAY15_30(rs.getBigDecimal("R63_DAY15_30"));
			obj.setR63_DAY31_TO_2M(rs.getBigDecimal("R63_DAY31_TO_2M"));
			obj.setR63_MORE2M_TO_3M(rs.getBigDecimal("R63_MORE2M_TO_3M"));
			obj.setR63_OVER3M_TO_6M(rs.getBigDecimal("R63_OVER3M_TO_6M"));
			obj.setR63_OVER6M_TO_1Y(rs.getBigDecimal("R63_OVER6M_TO_1Y"));
			obj.setR63_OVER1Y_TO_3Y(rs.getBigDecimal("R63_OVER1Y_TO_3Y"));
			obj.setR63_OVER3Y_TO_5Y(rs.getBigDecimal("R63_OVER3Y_TO_5Y"));
			obj.setR63_OVER5Y(rs.getBigDecimal("R63_OVER5Y"));
			obj.setR63_TOTAL(rs.getBigDecimal("R63_TOTAL"));
			obj.setR64_PRODUCT(rs.getString("R64_PRODUCT"));
			obj.setR64_DAY1(rs.getBigDecimal("R64_DAY1"));
			obj.setR64_DAY2_7(rs.getBigDecimal("R64_DAY2_7"));
			obj.setR64_DAY8_14(rs.getBigDecimal("R64_DAY8_14"));
			obj.setR64_DAY15_30(rs.getBigDecimal("R64_DAY15_30"));
			obj.setR64_DAY31_TO_2M(rs.getBigDecimal("R64_DAY31_TO_2M"));
			obj.setR64_MORE2M_TO_3M(rs.getBigDecimal("R64_MORE2M_TO_3M"));
			obj.setR64_OVER3M_TO_6M(rs.getBigDecimal("R64_OVER3M_TO_6M"));
			obj.setR64_OVER6M_TO_1Y(rs.getBigDecimal("R64_OVER6M_TO_1Y"));
			obj.setR64_OVER1Y_TO_3Y(rs.getBigDecimal("R64_OVER1Y_TO_3Y"));
			obj.setR64_OVER3Y_TO_5Y(rs.getBigDecimal("R64_OVER3Y_TO_5Y"));
			obj.setR64_OVER5Y(rs.getBigDecimal("R64_OVER5Y"));
			obj.setR64_TOTAL(rs.getBigDecimal("R64_TOTAL"));
			obj.setR65_PRODUCT(rs.getString("R65_PRODUCT"));
			obj.setR65_DAY1(rs.getBigDecimal("R65_DAY1"));
			obj.setR65_DAY2_7(rs.getBigDecimal("R65_DAY2_7"));
			obj.setR65_DAY8_14(rs.getBigDecimal("R65_DAY8_14"));
			obj.setR65_DAY15_30(rs.getBigDecimal("R65_DAY15_30"));
			obj.setR65_DAY31_TO_2M(rs.getBigDecimal("R65_DAY31_TO_2M"));
			obj.setR65_MORE2M_TO_3M(rs.getBigDecimal("R65_MORE2M_TO_3M"));
			obj.setR65_OVER3M_TO_6M(rs.getBigDecimal("R65_OVER3M_TO_6M"));
			obj.setR65_OVER6M_TO_1Y(rs.getBigDecimal("R65_OVER6M_TO_1Y"));
			obj.setR65_OVER1Y_TO_3Y(rs.getBigDecimal("R65_OVER1Y_TO_3Y"));
			obj.setR65_OVER3Y_TO_5Y(rs.getBigDecimal("R65_OVER3Y_TO_5Y"));
			obj.setR65_OVER5Y(rs.getBigDecimal("R65_OVER5Y"));
			obj.setR65_TOTAL(rs.getBigDecimal("R65_TOTAL"));
			obj.setR66_PRODUCT(rs.getString("R66_PRODUCT"));
			obj.setR66_DAY1(rs.getBigDecimal("R66_DAY1"));
			obj.setR66_DAY2_7(rs.getBigDecimal("R66_DAY2_7"));
			obj.setR66_DAY8_14(rs.getBigDecimal("R66_DAY8_14"));
			obj.setR66_DAY15_30(rs.getBigDecimal("R66_DAY15_30"));
			obj.setR66_DAY31_TO_2M(rs.getBigDecimal("R66_DAY31_TO_2M"));
			obj.setR66_MORE2M_TO_3M(rs.getBigDecimal("R66_MORE2M_TO_3M"));
			obj.setR66_OVER3M_TO_6M(rs.getBigDecimal("R66_OVER3M_TO_6M"));
			obj.setR66_OVER6M_TO_1Y(rs.getBigDecimal("R66_OVER6M_TO_1Y"));
			obj.setR66_OVER1Y_TO_3Y(rs.getBigDecimal("R66_OVER1Y_TO_3Y"));
			obj.setR66_OVER3Y_TO_5Y(rs.getBigDecimal("R66_OVER3Y_TO_5Y"));
			obj.setR66_OVER5Y(rs.getBigDecimal("R66_OVER5Y"));
			obj.setR66_TOTAL(rs.getBigDecimal("R66_TOTAL"));
			obj.setR67_PRODUCT(rs.getString("R67_PRODUCT"));
			obj.setR67_DAY1(rs.getBigDecimal("R67_DAY1"));
			obj.setR67_DAY2_7(rs.getBigDecimal("R67_DAY2_7"));
			obj.setR67_DAY8_14(rs.getBigDecimal("R67_DAY8_14"));
			obj.setR67_DAY15_30(rs.getBigDecimal("R67_DAY15_30"));
			obj.setR67_DAY31_TO_2M(rs.getBigDecimal("R67_DAY31_TO_2M"));
			obj.setR67_MORE2M_TO_3M(rs.getBigDecimal("R67_MORE2M_TO_3M"));
			obj.setR67_OVER3M_TO_6M(rs.getBigDecimal("R67_OVER3M_TO_6M"));
			obj.setR67_OVER6M_TO_1Y(rs.getBigDecimal("R67_OVER6M_TO_1Y"));
			obj.setR67_OVER1Y_TO_3Y(rs.getBigDecimal("R67_OVER1Y_TO_3Y"));
			obj.setR67_OVER3Y_TO_5Y(rs.getBigDecimal("R67_OVER3Y_TO_5Y"));
			obj.setR67_OVER5Y(rs.getBigDecimal("R67_OVER5Y"));
			obj.setR67_TOTAL(rs.getBigDecimal("R67_TOTAL"));
			obj.setR68_PRODUCT(rs.getString("R68_PRODUCT"));
			obj.setR68_DAY1(rs.getBigDecimal("R68_DAY1"));
			obj.setR68_DAY2_7(rs.getBigDecimal("R68_DAY2_7"));
			obj.setR68_DAY8_14(rs.getBigDecimal("R68_DAY8_14"));
			obj.setR68_DAY15_30(rs.getBigDecimal("R68_DAY15_30"));
			obj.setR68_DAY31_TO_2M(rs.getBigDecimal("R68_DAY31_TO_2M"));
			obj.setR68_MORE2M_TO_3M(rs.getBigDecimal("R68_MORE2M_TO_3M"));
			obj.setR68_OVER3M_TO_6M(rs.getBigDecimal("R68_OVER3M_TO_6M"));
			obj.setR68_OVER6M_TO_1Y(rs.getBigDecimal("R68_OVER6M_TO_1Y"));
			obj.setR68_OVER1Y_TO_3Y(rs.getBigDecimal("R68_OVER1Y_TO_3Y"));
			obj.setR68_OVER3Y_TO_5Y(rs.getBigDecimal("R68_OVER3Y_TO_5Y"));
			obj.setR68_OVER5Y(rs.getBigDecimal("R68_OVER5Y"));
			obj.setR68_TOTAL(rs.getBigDecimal("R68_TOTAL"));
			obj.setR69_PRODUCT(rs.getString("R69_PRODUCT"));
			obj.setR69_DAY1(rs.getBigDecimal("R69_DAY1"));
			obj.setR69_DAY2_7(rs.getBigDecimal("R69_DAY2_7"));
			obj.setR69_DAY8_14(rs.getBigDecimal("R69_DAY8_14"));
			obj.setR69_DAY15_30(rs.getBigDecimal("R69_DAY15_30"));
			obj.setR69_DAY31_TO_2M(rs.getBigDecimal("R69_DAY31_TO_2M"));
			obj.setR69_MORE2M_TO_3M(rs.getBigDecimal("R69_MORE2M_TO_3M"));
			obj.setR69_OVER3M_TO_6M(rs.getBigDecimal("R69_OVER3M_TO_6M"));
			obj.setR69_OVER6M_TO_1Y(rs.getBigDecimal("R69_OVER6M_TO_1Y"));
			obj.setR69_OVER1Y_TO_3Y(rs.getBigDecimal("R69_OVER1Y_TO_3Y"));
			obj.setR69_OVER3Y_TO_5Y(rs.getBigDecimal("R69_OVER3Y_TO_5Y"));
			obj.setR69_OVER5Y(rs.getBigDecimal("R69_OVER5Y"));
			obj.setR69_TOTAL(rs.getBigDecimal("R69_TOTAL"));
			obj.setR70_PRODUCT(rs.getString("R70_PRODUCT"));
			obj.setR70_DAY1(rs.getBigDecimal("R70_DAY1"));
			obj.setR70_DAY2_7(rs.getBigDecimal("R70_DAY2_7"));
			obj.setR70_DAY8_14(rs.getBigDecimal("R70_DAY8_14"));
			obj.setR70_DAY15_30(rs.getBigDecimal("R70_DAY15_30"));
			obj.setR70_DAY31_TO_2M(rs.getBigDecimal("R70_DAY31_TO_2M"));
			obj.setR70_MORE2M_TO_3M(rs.getBigDecimal("R70_MORE2M_TO_3M"));
			obj.setR70_OVER3M_TO_6M(rs.getBigDecimal("R70_OVER3M_TO_6M"));
			obj.setR70_OVER6M_TO_1Y(rs.getBigDecimal("R70_OVER6M_TO_1Y"));
			obj.setR70_OVER1Y_TO_3Y(rs.getBigDecimal("R70_OVER1Y_TO_3Y"));
			obj.setR70_OVER3Y_TO_5Y(rs.getBigDecimal("R70_OVER3Y_TO_5Y"));
			obj.setR70_OVER5Y(rs.getBigDecimal("R70_OVER5Y"));
			obj.setR70_TOTAL(rs.getBigDecimal("R70_TOTAL"));
			obj.setR71_PRODUCT(rs.getString("R71_PRODUCT"));
			obj.setR71_DAY1(rs.getBigDecimal("R71_DAY1"));
			obj.setR71_DAY2_7(rs.getBigDecimal("R71_DAY2_7"));
			obj.setR71_DAY8_14(rs.getBigDecimal("R71_DAY8_14"));
			obj.setR71_DAY15_30(rs.getBigDecimal("R71_DAY15_30"));
			obj.setR71_DAY31_TO_2M(rs.getBigDecimal("R71_DAY31_TO_2M"));
			obj.setR71_MORE2M_TO_3M(rs.getBigDecimal("R71_MORE2M_TO_3M"));
			obj.setR71_OVER3M_TO_6M(rs.getBigDecimal("R71_OVER3M_TO_6M"));
			obj.setR71_OVER6M_TO_1Y(rs.getBigDecimal("R71_OVER6M_TO_1Y"));
			obj.setR71_OVER1Y_TO_3Y(rs.getBigDecimal("R71_OVER1Y_TO_3Y"));
			obj.setR71_OVER3Y_TO_5Y(rs.getBigDecimal("R71_OVER3Y_TO_5Y"));
			obj.setR71_OVER5Y(rs.getBigDecimal("R71_OVER5Y"));
			obj.setR71_TOTAL(rs.getBigDecimal("R71_TOTAL"));
			obj.setR72_PRODUCT(rs.getString("R72_PRODUCT"));
			obj.setR72_DAY1(rs.getBigDecimal("R72_DAY1"));
			obj.setR72_DAY2_7(rs.getBigDecimal("R72_DAY2_7"));
			obj.setR72_DAY8_14(rs.getBigDecimal("R72_DAY8_14"));
			obj.setR72_DAY15_30(rs.getBigDecimal("R72_DAY15_30"));
			obj.setR72_DAY31_TO_2M(rs.getBigDecimal("R72_DAY31_TO_2M"));
			obj.setR72_MORE2M_TO_3M(rs.getBigDecimal("R72_MORE2M_TO_3M"));
			obj.setR72_OVER3M_TO_6M(rs.getBigDecimal("R72_OVER3M_TO_6M"));
			obj.setR72_OVER6M_TO_1Y(rs.getBigDecimal("R72_OVER6M_TO_1Y"));
			obj.setR72_OVER1Y_TO_3Y(rs.getBigDecimal("R72_OVER1Y_TO_3Y"));
			obj.setR72_OVER3Y_TO_5Y(rs.getBigDecimal("R72_OVER3Y_TO_5Y"));
			obj.setR72_OVER5Y(rs.getBigDecimal("R72_OVER5Y"));
			obj.setR72_TOTAL(rs.getBigDecimal("R72_TOTAL"));
			obj.setR73_PRODUCT(rs.getString("R73_PRODUCT"));
			obj.setR73_DAY1(rs.getBigDecimal("R73_DAY1"));
			obj.setR73_DAY2_7(rs.getBigDecimal("R73_DAY2_7"));
			obj.setR73_DAY8_14(rs.getBigDecimal("R73_DAY8_14"));
			obj.setR73_DAY15_30(rs.getBigDecimal("R73_DAY15_30"));
			obj.setR73_DAY31_TO_2M(rs.getBigDecimal("R73_DAY31_TO_2M"));
			obj.setR73_MORE2M_TO_3M(rs.getBigDecimal("R73_MORE2M_TO_3M"));
			obj.setR73_OVER3M_TO_6M(rs.getBigDecimal("R73_OVER3M_TO_6M"));
			obj.setR73_OVER6M_TO_1Y(rs.getBigDecimal("R73_OVER6M_TO_1Y"));
			obj.setR73_OVER1Y_TO_3Y(rs.getBigDecimal("R73_OVER1Y_TO_3Y"));
			obj.setR73_OVER3Y_TO_5Y(rs.getBigDecimal("R73_OVER3Y_TO_5Y"));
			obj.setR73_OVER5Y(rs.getBigDecimal("R73_OVER5Y"));
			obj.setR73_TOTAL(rs.getBigDecimal("R73_TOTAL"));
			obj.setR74_PRODUCT(rs.getString("R74_PRODUCT"));
			obj.setR74_DAY1(rs.getBigDecimal("R74_DAY1"));
			obj.setR74_DAY2_7(rs.getBigDecimal("R74_DAY2_7"));
			obj.setR74_DAY8_14(rs.getBigDecimal("R74_DAY8_14"));
			obj.setR74_DAY15_30(rs.getBigDecimal("R74_DAY15_30"));
			obj.setR74_DAY31_TO_2M(rs.getBigDecimal("R74_DAY31_TO_2M"));
			obj.setR74_MORE2M_TO_3M(rs.getBigDecimal("R74_MORE2M_TO_3M"));
			obj.setR74_OVER3M_TO_6M(rs.getBigDecimal("R74_OVER3M_TO_6M"));
			obj.setR74_OVER6M_TO_1Y(rs.getBigDecimal("R74_OVER6M_TO_1Y"));
			obj.setR74_OVER1Y_TO_3Y(rs.getBigDecimal("R74_OVER1Y_TO_3Y"));
			obj.setR74_OVER3Y_TO_5Y(rs.getBigDecimal("R74_OVER3Y_TO_5Y"));
			obj.setR74_OVER5Y(rs.getBigDecimal("R74_OVER5Y"));
			obj.setR74_TOTAL(rs.getBigDecimal("R74_TOTAL"));
			obj.setR75_PRODUCT(rs.getString("R75_PRODUCT"));
			obj.setR75_DAY1(rs.getBigDecimal("R75_DAY1"));
			obj.setR75_DAY2_7(rs.getBigDecimal("R75_DAY2_7"));
			obj.setR75_DAY8_14(rs.getBigDecimal("R75_DAY8_14"));
			obj.setR75_DAY15_30(rs.getBigDecimal("R75_DAY15_30"));
			obj.setR75_DAY31_TO_2M(rs.getBigDecimal("R75_DAY31_TO_2M"));
			obj.setR75_MORE2M_TO_3M(rs.getBigDecimal("R75_MORE2M_TO_3M"));
			obj.setR75_OVER3M_TO_6M(rs.getBigDecimal("R75_OVER3M_TO_6M"));
			obj.setR75_OVER6M_TO_1Y(rs.getBigDecimal("R75_OVER6M_TO_1Y"));
			obj.setR75_OVER1Y_TO_3Y(rs.getBigDecimal("R75_OVER1Y_TO_3Y"));
			obj.setR75_OVER3Y_TO_5Y(rs.getBigDecimal("R75_OVER3Y_TO_5Y"));
			obj.setR75_OVER5Y(rs.getBigDecimal("R75_OVER5Y"));
			obj.setR75_TOTAL(rs.getBigDecimal("R75_TOTAL"));
			obj.setR76_PRODUCT(rs.getString("R76_PRODUCT"));
			obj.setR76_DAY1(rs.getBigDecimal("R76_DAY1"));
			obj.setR76_DAY2_7(rs.getBigDecimal("R76_DAY2_7"));
			obj.setR76_DAY8_14(rs.getBigDecimal("R76_DAY8_14"));
			obj.setR76_DAY15_30(rs.getBigDecimal("R76_DAY15_30"));
			obj.setR76_DAY31_TO_2M(rs.getBigDecimal("R76_DAY31_TO_2M"));
			obj.setR76_MORE2M_TO_3M(rs.getBigDecimal("R76_MORE2M_TO_3M"));
			obj.setR76_OVER3M_TO_6M(rs.getBigDecimal("R76_OVER3M_TO_6M"));
			obj.setR76_OVER6M_TO_1Y(rs.getBigDecimal("R76_OVER6M_TO_1Y"));
			obj.setR76_OVER1Y_TO_3Y(rs.getBigDecimal("R76_OVER1Y_TO_3Y"));
			obj.setR76_OVER3Y_TO_5Y(rs.getBigDecimal("R76_OVER3Y_TO_5Y"));
			obj.setR76_OVER5Y(rs.getBigDecimal("R76_OVER5Y"));
			obj.setR76_TOTAL(rs.getBigDecimal("R76_TOTAL"));
//			obj.setR77_PRODUCT(rs.getString("R77_PRODUCT"));
//			obj.setR77_DAY1(rs.getBigDecimal("R77_DAY1"));
//			obj.setR77_DAY2_7(rs.getBigDecimal("R77_DAY2_7"));
//			obj.setR77_DAY8_14(rs.getBigDecimal("R77_DAY8_14"));
//			obj.setR77_DAY15_30(rs.getBigDecimal("R77_DAY15_30"));
//			obj.setR77_DAY31_TO_2M(rs.getBigDecimal("R77_DAY31_TO_2M"));
//			obj.setR77_MORE2M_TO_3M(rs.getBigDecimal("R77_MORE2M_TO_3M"));
//			obj.setR77_OVER3M_TO_6M(rs.getBigDecimal("R77_OVER3M_TO_6M"));
//			obj.setR77_OVER6M_TO_1Y(rs.getBigDecimal("R77_OVER6M_TO_1Y"));
//			obj.setR77_OVER1Y_TO_3Y(rs.getBigDecimal("R77_OVER1Y_TO_3Y"));
//			obj.setR77_OVER3Y_TO_5Y(rs.getBigDecimal("R77_OVER3Y_TO_5Y"));
//			obj.setR77_OVER5Y(rs.getBigDecimal("R77_OVER5Y"));
//			obj.setR77_TOTAL(rs.getBigDecimal("R77_TOTAL"));
//			obj.setR78_PRODUCT(rs.getString("R78_PRODUCT"));
//			obj.setR78_DAY1(rs.getBigDecimal("R78_DAY1"));
//			obj.setR78_DAY2_7(rs.getBigDecimal("R78_DAY2_7"));
//			obj.setR78_DAY8_14(rs.getBigDecimal("R78_DAY8_14"));
//			obj.setR78_DAY15_30(rs.getBigDecimal("R78_DAY15_30"));
//			obj.setR78_DAY31_TO_2M(rs.getBigDecimal("R78_DAY31_TO_2M"));
//			obj.setR78_MORE2M_TO_3M(rs.getBigDecimal("R78_MORE2M_TO_3M"));
//			obj.setR78_OVER3M_TO_6M(rs.getBigDecimal("R78_OVER3M_TO_6M"));
//			obj.setR78_OVER6M_TO_1Y(rs.getBigDecimal("R78_OVER6M_TO_1Y"));
//			obj.setR78_OVER1Y_TO_3Y(rs.getBigDecimal("R78_OVER1Y_TO_3Y"));
//			obj.setR78_OVER3Y_TO_5Y(rs.getBigDecimal("R78_OVER3Y_TO_5Y"));
//			obj.setR78_OVER5Y(rs.getBigDecimal("R78_OVER5Y"));
//			obj.setR78_TOTAL(rs.getBigDecimal("R78_TOTAL"));
//			obj.setR79_PRODUCT(rs.getString("R79_PRODUCT"));
//			obj.setR79_DAY1(rs.getBigDecimal("R79_DAY1"));
//			obj.setR79_DAY2_7(rs.getBigDecimal("R79_DAY2_7"));
//			obj.setR79_DAY8_14(rs.getBigDecimal("R79_DAY8_14"));
//			obj.setR79_DAY15_30(rs.getBigDecimal("R79_DAY15_30"));
//			obj.setR79_DAY31_TO_2M(rs.getBigDecimal("R79_DAY31_TO_2M"));
//			obj.setR79_MORE2M_TO_3M(rs.getBigDecimal("R79_MORE2M_TO_3M"));
//			obj.setR79_OVER3M_TO_6M(rs.getBigDecimal("R79_OVER3M_TO_6M"));
//			obj.setR79_OVER6M_TO_1Y(rs.getBigDecimal("R79_OVER6M_TO_1Y"));
//			obj.setR79_OVER1Y_TO_3Y(rs.getBigDecimal("R79_OVER1Y_TO_3Y"));
//			obj.setR79_OVER3Y_TO_5Y(rs.getBigDecimal("R79_OVER3Y_TO_5Y"));
//			obj.setR79_OVER5Y(rs.getBigDecimal("R79_OVER5Y"));
//			obj.setR79_TOTAL(rs.getBigDecimal("R79_TOTAL"));
//			obj.setR80_PRODUCT(rs.getString("R80_PRODUCT"));
//			obj.setR80_DAY1(rs.getBigDecimal("R80_DAY1"));
//			obj.setR80_DAY2_7(rs.getBigDecimal("R80_DAY2_7"));
//			obj.setR80_DAY8_14(rs.getBigDecimal("R80_DAY8_14"));
//			obj.setR80_DAY15_30(rs.getBigDecimal("R80_DAY15_30"));
//			obj.setR80_DAY31_TO_2M(rs.getBigDecimal("R80_DAY31_TO_2M"));
//			obj.setR80_MORE2M_TO_3M(rs.getBigDecimal("R80_MORE2M_TO_3M"));
//			obj.setR80_OVER3M_TO_6M(rs.getBigDecimal("R80_OVER3M_TO_6M"));
//			obj.setR80_OVER6M_TO_1Y(rs.getBigDecimal("R80_OVER6M_TO_1Y"));
//			obj.setR80_OVER1Y_TO_3Y(rs.getBigDecimal("R80_OVER1Y_TO_3Y"));
//			obj.setR80_OVER3Y_TO_5Y(rs.getBigDecimal("R80_OVER3Y_TO_5Y"));
//			obj.setR80_OVER5Y(rs.getBigDecimal("R80_OVER5Y"));
//			obj.setR80_TOTAL(rs.getBigDecimal("R80_TOTAL"));
//			obj.setR81_PRODUCT(rs.getString("R81_PRODUCT"));
//			obj.setR81_DAY1(rs.getBigDecimal("R81_DAY1"));
//			obj.setR81_DAY2_7(rs.getBigDecimal("R81_DAY2_7"));
//			obj.setR81_DAY8_14(rs.getBigDecimal("R81_DAY8_14"));
//			obj.setR81_DAY15_30(rs.getBigDecimal("R81_DAY15_30"));
//			obj.setR81_DAY31_TO_2M(rs.getBigDecimal("R81_DAY31_TO_2M"));
//			obj.setR81_MORE2M_TO_3M(rs.getBigDecimal("R81_MORE2M_TO_3M"));
//			obj.setR81_OVER3M_TO_6M(rs.getBigDecimal("R81_OVER3M_TO_6M"));
//			obj.setR81_OVER6M_TO_1Y(rs.getBigDecimal("R81_OVER6M_TO_1Y"));
//			obj.setR81_OVER1Y_TO_3Y(rs.getBigDecimal("R81_OVER1Y_TO_3Y"));
//			obj.setR81_OVER3Y_TO_5Y(rs.getBigDecimal("R81_OVER3Y_TO_5Y"));
//			obj.setR81_OVER5Y(rs.getBigDecimal("R81_OVER5Y"));
//			obj.setR81_TOTAL(rs.getBigDecimal("R81_TOTAL"));
//			obj.setR82_PRODUCT(rs.getString("R82_PRODUCT"));
//			obj.setR82_DAY1(rs.getBigDecimal("R82_DAY1"));
//			obj.setR82_DAY2_7(rs.getBigDecimal("R82_DAY2_7"));
//			obj.setR82_DAY8_14(rs.getBigDecimal("R82_DAY8_14"));
//			obj.setR82_DAY15_30(rs.getBigDecimal("R82_DAY15_30"));
//			obj.setR82_DAY31_TO_2M(rs.getBigDecimal("R82_DAY31_TO_2M"));
//			obj.setR82_MORE2M_TO_3M(rs.getBigDecimal("R82_MORE2M_TO_3M"));
//			obj.setR82_OVER3M_TO_6M(rs.getBigDecimal("R82_OVER3M_TO_6M"));
//			obj.setR82_OVER6M_TO_1Y(rs.getBigDecimal("R82_OVER6M_TO_1Y"));
//			obj.setR82_OVER1Y_TO_3Y(rs.getBigDecimal("R82_OVER1Y_TO_3Y"));
//			obj.setR82_OVER3Y_TO_5Y(rs.getBigDecimal("R82_OVER3Y_TO_5Y"));
//			obj.setR82_OVER5Y(rs.getBigDecimal("R82_OVER5Y"));
//			obj.setR82_TOTAL(rs.getBigDecimal("R82_TOTAL"));
//			obj.setR83_PRODUCT(rs.getString("R83_PRODUCT"));
//			obj.setR83_DAY1(rs.getBigDecimal("R83_DAY1"));
//			obj.setR83_DAY2_7(rs.getBigDecimal("R83_DAY2_7"));
//			obj.setR83_DAY8_14(rs.getBigDecimal("R83_DAY8_14"));
//			obj.setR83_DAY15_30(rs.getBigDecimal("R83_DAY15_30"));
//			obj.setR83_DAY31_TO_2M(rs.getBigDecimal("R83_DAY31_TO_2M"));
//			obj.setR83_MORE2M_TO_3M(rs.getBigDecimal("R83_MORE2M_TO_3M"));
//			obj.setR83_OVER3M_TO_6M(rs.getBigDecimal("R83_OVER3M_TO_6M"));
//			obj.setR83_OVER6M_TO_1Y(rs.getBigDecimal("R83_OVER6M_TO_1Y"));
//			obj.setR83_OVER1Y_TO_3Y(rs.getBigDecimal("R83_OVER1Y_TO_3Y"));
//			obj.setR83_OVER3Y_TO_5Y(rs.getBigDecimal("R83_OVER3Y_TO_5Y"));
//			obj.setR83_OVER5Y(rs.getBigDecimal("R83_OVER5Y"));
//			obj.setR83_TOTAL(rs.getBigDecimal("R83_TOTAL"));
			obj.setR84_PRODUCT(rs.getString("R84_PRODUCT"));
			obj.setR84_DAY1(rs.getBigDecimal("R84_DAY1"));
			obj.setR84_DAY2_7(rs.getBigDecimal("R84_DAY2_7"));
			obj.setR84_DAY8_14(rs.getBigDecimal("R84_DAY8_14"));
			obj.setR84_DAY15_30(rs.getBigDecimal("R84_DAY15_30"));
			obj.setR84_DAY31_TO_2M(rs.getBigDecimal("R84_DAY31_TO_2M"));
			obj.setR84_MORE2M_TO_3M(rs.getBigDecimal("R84_MORE2M_TO_3M"));
			obj.setR84_OVER3M_TO_6M(rs.getBigDecimal("R84_OVER3M_TO_6M"));
			obj.setR84_OVER6M_TO_1Y(rs.getBigDecimal("R84_OVER6M_TO_1Y"));
			obj.setR84_OVER1Y_TO_3Y(rs.getBigDecimal("R84_OVER1Y_TO_3Y"));
			obj.setR84_OVER3Y_TO_5Y(rs.getBigDecimal("R84_OVER3Y_TO_5Y"));
			obj.setR84_OVER5Y(rs.getBigDecimal("R84_OVER5Y"));
			obj.setR84_TOTAL(rs.getBigDecimal("R84_TOTAL"));
			obj.setR85_PRODUCT(rs.getString("R85_PRODUCT"));
			obj.setR85_DAY1(rs.getBigDecimal("R85_DAY1"));
			obj.setR85_DAY2_7(rs.getBigDecimal("R85_DAY2_7"));
			obj.setR85_DAY8_14(rs.getBigDecimal("R85_DAY8_14"));
			obj.setR85_DAY15_30(rs.getBigDecimal("R85_DAY15_30"));
			obj.setR85_DAY31_TO_2M(rs.getBigDecimal("R85_DAY31_TO_2M"));
			obj.setR85_MORE2M_TO_3M(rs.getBigDecimal("R85_MORE2M_TO_3M"));
			obj.setR85_OVER3M_TO_6M(rs.getBigDecimal("R85_OVER3M_TO_6M"));
			obj.setR85_OVER6M_TO_1Y(rs.getBigDecimal("R85_OVER6M_TO_1Y"));
			obj.setR85_OVER1Y_TO_3Y(rs.getBigDecimal("R85_OVER1Y_TO_3Y"));
			obj.setR85_OVER3Y_TO_5Y(rs.getBigDecimal("R85_OVER3Y_TO_5Y"));
			obj.setR85_OVER5Y(rs.getBigDecimal("R85_OVER5Y"));
			obj.setR85_TOTAL(rs.getBigDecimal("R85_TOTAL"));
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getString("REPORT_VERSION"));
			obj.setREPORT_CURRENCY(rs.getString("REPORT_CURRENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// ------------------------------
	// Row Mapper for SLS_INPUT_SHT_Detail_Entity
	// ------------------------------
	class SLSDetailRowMapper implements RowMapper<SLS_INPUT_SHT_Detail_Entity> {
		@Override
		public SLS_INPUT_SHT_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			SLS_INPUT_SHT_Detail_Entity obj = new SLS_INPUT_SHT_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setRowId(rs.getString("ROW_ID"));
			obj.setColumnId(rs.getString("COLUMN_ID"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInPula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setProvision(rs.getBigDecimal("PROVISION"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// ------------------------------
	// Row Mapper for SLS_INPUT_SHT_Archival_Summary_Entity
	// ------------------------------
	class SLSArchivalSummaryRowMapper implements RowMapper<SLS_INPUT_SHT_Archival_Summary_Entity> {
		@Override
		public SLS_INPUT_SHT_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			SLS_INPUT_SHT_Archival_Summary_Entity obj = new SLS_INPUT_SHT_Archival_Summary_Entity();
			obj.setSRL_NO(rs.getString("SRL_NO"));
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_DAY1(rs.getBigDecimal("R11_DAY1"));
			obj.setR11_DAY2_7(rs.getBigDecimal("R11_DAY2_7"));
			obj.setR11_DAY8_14(rs.getBigDecimal("R11_DAY8_14"));
			obj.setR11_DAY15_30(rs.getBigDecimal("R11_DAY15_30"));
			obj.setR11_DAY31_TO_2M(rs.getBigDecimal("R11_DAY31_TO_2M"));
			obj.setR11_MORE2M_TO_3M(rs.getBigDecimal("R11_MORE2M_TO_3M"));
			obj.setR11_OVER3M_TO_6M(rs.getBigDecimal("R11_OVER3M_TO_6M"));
			obj.setR11_OVER6M_TO_1Y(rs.getBigDecimal("R11_OVER6M_TO_1Y"));
			obj.setR11_OVER1Y_TO_3Y(rs.getBigDecimal("R11_OVER1Y_TO_3Y"));
			obj.setR11_OVER3Y_TO_5Y(rs.getBigDecimal("R11_OVER3Y_TO_5Y"));
			obj.setR11_OVER5Y(rs.getBigDecimal("R11_OVER5Y"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_DAY1(rs.getBigDecimal("R12_DAY1"));
			obj.setR12_DAY2_7(rs.getBigDecimal("R12_DAY2_7"));
			obj.setR12_DAY8_14(rs.getBigDecimal("R12_DAY8_14"));
			obj.setR12_DAY15_30(rs.getBigDecimal("R12_DAY15_30"));
			obj.setR12_DAY31_TO_2M(rs.getBigDecimal("R12_DAY31_TO_2M"));
			obj.setR12_MORE2M_TO_3M(rs.getBigDecimal("R12_MORE2M_TO_3M"));
			obj.setR12_OVER3M_TO_6M(rs.getBigDecimal("R12_OVER3M_TO_6M"));
			obj.setR12_OVER6M_TO_1Y(rs.getBigDecimal("R12_OVER6M_TO_1Y"));
			obj.setR12_OVER1Y_TO_3Y(rs.getBigDecimal("R12_OVER1Y_TO_3Y"));
			obj.setR12_OVER3Y_TO_5Y(rs.getBigDecimal("R12_OVER3Y_TO_5Y"));
			obj.setR12_OVER5Y(rs.getBigDecimal("R12_OVER5Y"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_DAY1(rs.getBigDecimal("R13_DAY1"));
			obj.setR13_DAY2_7(rs.getBigDecimal("R13_DAY2_7"));
			obj.setR13_DAY8_14(rs.getBigDecimal("R13_DAY8_14"));
			obj.setR13_DAY15_30(rs.getBigDecimal("R13_DAY15_30"));
			obj.setR13_DAY31_TO_2M(rs.getBigDecimal("R13_DAY31_TO_2M"));
			obj.setR13_MORE2M_TO_3M(rs.getBigDecimal("R13_MORE2M_TO_3M"));
			obj.setR13_OVER3M_TO_6M(rs.getBigDecimal("R13_OVER3M_TO_6M"));
			obj.setR13_OVER6M_TO_1Y(rs.getBigDecimal("R13_OVER6M_TO_1Y"));
			obj.setR13_OVER1Y_TO_3Y(rs.getBigDecimal("R13_OVER1Y_TO_3Y"));
			obj.setR13_OVER3Y_TO_5Y(rs.getBigDecimal("R13_OVER3Y_TO_5Y"));
			obj.setR13_OVER5Y(rs.getBigDecimal("R13_OVER5Y"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_DAY1(rs.getBigDecimal("R14_DAY1"));
			obj.setR14_DAY2_7(rs.getBigDecimal("R14_DAY2_7"));
			obj.setR14_DAY8_14(rs.getBigDecimal("R14_DAY8_14"));
			obj.setR14_DAY15_30(rs.getBigDecimal("R14_DAY15_30"));
			obj.setR14_DAY31_TO_2M(rs.getBigDecimal("R14_DAY31_TO_2M"));
			obj.setR14_MORE2M_TO_3M(rs.getBigDecimal("R14_MORE2M_TO_3M"));
			obj.setR14_OVER3M_TO_6M(rs.getBigDecimal("R14_OVER3M_TO_6M"));
			obj.setR14_OVER6M_TO_1Y(rs.getBigDecimal("R14_OVER6M_TO_1Y"));
			obj.setR14_OVER1Y_TO_3Y(rs.getBigDecimal("R14_OVER1Y_TO_3Y"));
			obj.setR14_OVER3Y_TO_5Y(rs.getBigDecimal("R14_OVER3Y_TO_5Y"));
			obj.setR14_OVER5Y(rs.getBigDecimal("R14_OVER5Y"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_DAY1(rs.getBigDecimal("R15_DAY1"));
			obj.setR15_DAY2_7(rs.getBigDecimal("R15_DAY2_7"));
			obj.setR15_DAY8_14(rs.getBigDecimal("R15_DAY8_14"));
			obj.setR15_DAY15_30(rs.getBigDecimal("R15_DAY15_30"));
			obj.setR15_DAY31_TO_2M(rs.getBigDecimal("R15_DAY31_TO_2M"));
			obj.setR15_MORE2M_TO_3M(rs.getBigDecimal("R15_MORE2M_TO_3M"));
			obj.setR15_OVER3M_TO_6M(rs.getBigDecimal("R15_OVER3M_TO_6M"));
			obj.setR15_OVER6M_TO_1Y(rs.getBigDecimal("R15_OVER6M_TO_1Y"));
			obj.setR15_OVER1Y_TO_3Y(rs.getBigDecimal("R15_OVER1Y_TO_3Y"));
			obj.setR15_OVER3Y_TO_5Y(rs.getBigDecimal("R15_OVER3Y_TO_5Y"));
			obj.setR15_OVER5Y(rs.getBigDecimal("R15_OVER5Y"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_DAY1(rs.getBigDecimal("R16_DAY1"));
			obj.setR16_DAY2_7(rs.getBigDecimal("R16_DAY2_7"));
			obj.setR16_DAY8_14(rs.getBigDecimal("R16_DAY8_14"));
			obj.setR16_DAY15_30(rs.getBigDecimal("R16_DAY15_30"));
			obj.setR16_DAY31_TO_2M(rs.getBigDecimal("R16_DAY31_TO_2M"));
			obj.setR16_MORE2M_TO_3M(rs.getBigDecimal("R16_MORE2M_TO_3M"));
			obj.setR16_OVER3M_TO_6M(rs.getBigDecimal("R16_OVER3M_TO_6M"));
			obj.setR16_OVER6M_TO_1Y(rs.getBigDecimal("R16_OVER6M_TO_1Y"));
			obj.setR16_OVER1Y_TO_3Y(rs.getBigDecimal("R16_OVER1Y_TO_3Y"));
			obj.setR16_OVER3Y_TO_5Y(rs.getBigDecimal("R16_OVER3Y_TO_5Y"));
			obj.setR16_OVER5Y(rs.getBigDecimal("R16_OVER5Y"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_DAY1(rs.getBigDecimal("R17_DAY1"));
			obj.setR17_DAY2_7(rs.getBigDecimal("R17_DAY2_7"));
			obj.setR17_DAY8_14(rs.getBigDecimal("R17_DAY8_14"));
			obj.setR17_DAY15_30(rs.getBigDecimal("R17_DAY15_30"));
			obj.setR17_DAY31_TO_2M(rs.getBigDecimal("R17_DAY31_TO_2M"));
			obj.setR17_MORE2M_TO_3M(rs.getBigDecimal("R17_MORE2M_TO_3M"));
			obj.setR17_OVER3M_TO_6M(rs.getBigDecimal("R17_OVER3M_TO_6M"));
			obj.setR17_OVER6M_TO_1Y(rs.getBigDecimal("R17_OVER6M_TO_1Y"));
			obj.setR17_OVER1Y_TO_3Y(rs.getBigDecimal("R17_OVER1Y_TO_3Y"));
			obj.setR17_OVER3Y_TO_5Y(rs.getBigDecimal("R17_OVER3Y_TO_5Y"));
			obj.setR17_OVER5Y(rs.getBigDecimal("R17_OVER5Y"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_DAY1(rs.getBigDecimal("R18_DAY1"));
			obj.setR18_DAY2_7(rs.getBigDecimal("R18_DAY2_7"));
			obj.setR18_DAY8_14(rs.getBigDecimal("R18_DAY8_14"));
			obj.setR18_DAY15_30(rs.getBigDecimal("R18_DAY15_30"));
			obj.setR18_DAY31_TO_2M(rs.getBigDecimal("R18_DAY31_TO_2M"));
			obj.setR18_MORE2M_TO_3M(rs.getBigDecimal("R18_MORE2M_TO_3M"));
			obj.setR18_OVER3M_TO_6M(rs.getBigDecimal("R18_OVER3M_TO_6M"));
			obj.setR18_OVER6M_TO_1Y(rs.getBigDecimal("R18_OVER6M_TO_1Y"));
			obj.setR18_OVER1Y_TO_3Y(rs.getBigDecimal("R18_OVER1Y_TO_3Y"));
			obj.setR18_OVER3Y_TO_5Y(rs.getBigDecimal("R18_OVER3Y_TO_5Y"));
			obj.setR18_OVER5Y(rs.getBigDecimal("R18_OVER5Y"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_DAY1(rs.getBigDecimal("R19_DAY1"));
			obj.setR19_DAY2_7(rs.getBigDecimal("R19_DAY2_7"));
			obj.setR19_DAY8_14(rs.getBigDecimal("R19_DAY8_14"));
			obj.setR19_DAY15_30(rs.getBigDecimal("R19_DAY15_30"));
			obj.setR19_DAY31_TO_2M(rs.getBigDecimal("R19_DAY31_TO_2M"));
			obj.setR19_MORE2M_TO_3M(rs.getBigDecimal("R19_MORE2M_TO_3M"));
			obj.setR19_OVER3M_TO_6M(rs.getBigDecimal("R19_OVER3M_TO_6M"));
			obj.setR19_OVER6M_TO_1Y(rs.getBigDecimal("R19_OVER6M_TO_1Y"));
			obj.setR19_OVER1Y_TO_3Y(rs.getBigDecimal("R19_OVER1Y_TO_3Y"));
			obj.setR19_OVER3Y_TO_5Y(rs.getBigDecimal("R19_OVER3Y_TO_5Y"));
			obj.setR19_OVER5Y(rs.getBigDecimal("R19_OVER5Y"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
			obj.setR20_DAY1(rs.getBigDecimal("R20_DAY1"));
			obj.setR20_DAY2_7(rs.getBigDecimal("R20_DAY2_7"));
			obj.setR20_DAY8_14(rs.getBigDecimal("R20_DAY8_14"));
			obj.setR20_DAY15_30(rs.getBigDecimal("R20_DAY15_30"));
			obj.setR20_DAY31_TO_2M(rs.getBigDecimal("R20_DAY31_TO_2M"));
			obj.setR20_MORE2M_TO_3M(rs.getBigDecimal("R20_MORE2M_TO_3M"));
			obj.setR20_OVER3M_TO_6M(rs.getBigDecimal("R20_OVER3M_TO_6M"));
			obj.setR20_OVER6M_TO_1Y(rs.getBigDecimal("R20_OVER6M_TO_1Y"));
			obj.setR20_OVER1Y_TO_3Y(rs.getBigDecimal("R20_OVER1Y_TO_3Y"));
			obj.setR20_OVER3Y_TO_5Y(rs.getBigDecimal("R20_OVER3Y_TO_5Y"));
			obj.setR20_OVER5Y(rs.getBigDecimal("R20_OVER5Y"));
			obj.setR20_TOTAL(rs.getBigDecimal("R20_TOTAL"));
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_DAY1(rs.getBigDecimal("R21_DAY1"));
			obj.setR21_DAY2_7(rs.getBigDecimal("R21_DAY2_7"));
			obj.setR21_DAY8_14(rs.getBigDecimal("R21_DAY8_14"));
			obj.setR21_DAY15_30(rs.getBigDecimal("R21_DAY15_30"));
			obj.setR21_DAY31_TO_2M(rs.getBigDecimal("R21_DAY31_TO_2M"));
			obj.setR21_MORE2M_TO_3M(rs.getBigDecimal("R21_MORE2M_TO_3M"));
			obj.setR21_OVER3M_TO_6M(rs.getBigDecimal("R21_OVER3M_TO_6M"));
			obj.setR21_OVER6M_TO_1Y(rs.getBigDecimal("R21_OVER6M_TO_1Y"));
			obj.setR21_OVER1Y_TO_3Y(rs.getBigDecimal("R21_OVER1Y_TO_3Y"));
			obj.setR21_OVER3Y_TO_5Y(rs.getBigDecimal("R21_OVER3Y_TO_5Y"));
			obj.setR21_OVER5Y(rs.getBigDecimal("R21_OVER5Y"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));
			obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
			obj.setR22_DAY1(rs.getBigDecimal("R22_DAY1"));
			obj.setR22_DAY2_7(rs.getBigDecimal("R22_DAY2_7"));
			obj.setR22_DAY8_14(rs.getBigDecimal("R22_DAY8_14"));
			obj.setR22_DAY15_30(rs.getBigDecimal("R22_DAY15_30"));
			obj.setR22_DAY31_TO_2M(rs.getBigDecimal("R22_DAY31_TO_2M"));
			obj.setR22_MORE2M_TO_3M(rs.getBigDecimal("R22_MORE2M_TO_3M"));
			obj.setR22_OVER3M_TO_6M(rs.getBigDecimal("R22_OVER3M_TO_6M"));
			obj.setR22_OVER6M_TO_1Y(rs.getBigDecimal("R22_OVER6M_TO_1Y"));
			obj.setR22_OVER1Y_TO_3Y(rs.getBigDecimal("R22_OVER1Y_TO_3Y"));
			obj.setR22_OVER3Y_TO_5Y(rs.getBigDecimal("R22_OVER3Y_TO_5Y"));
			obj.setR22_OVER5Y(rs.getBigDecimal("R22_OVER5Y"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));
			obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
			obj.setR23_DAY1(rs.getBigDecimal("R23_DAY1"));
			obj.setR23_DAY2_7(rs.getBigDecimal("R23_DAY2_7"));
			obj.setR23_DAY8_14(rs.getBigDecimal("R23_DAY8_14"));
			obj.setR23_DAY15_30(rs.getBigDecimal("R23_DAY15_30"));
			obj.setR23_DAY31_TO_2M(rs.getBigDecimal("R23_DAY31_TO_2M"));
			obj.setR23_MORE2M_TO_3M(rs.getBigDecimal("R23_MORE2M_TO_3M"));
			obj.setR23_OVER3M_TO_6M(rs.getBigDecimal("R23_OVER3M_TO_6M"));
			obj.setR23_OVER6M_TO_1Y(rs.getBigDecimal("R23_OVER6M_TO_1Y"));
			obj.setR23_OVER1Y_TO_3Y(rs.getBigDecimal("R23_OVER1Y_TO_3Y"));
			obj.setR23_OVER3Y_TO_5Y(rs.getBigDecimal("R23_OVER3Y_TO_5Y"));
			obj.setR23_OVER5Y(rs.getBigDecimal("R23_OVER5Y"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
			obj.setR24_DAY1(rs.getBigDecimal("R24_DAY1"));
			obj.setR24_DAY2_7(rs.getBigDecimal("R24_DAY2_7"));
			obj.setR24_DAY8_14(rs.getBigDecimal("R24_DAY8_14"));
			obj.setR24_DAY15_30(rs.getBigDecimal("R24_DAY15_30"));
			obj.setR24_DAY31_TO_2M(rs.getBigDecimal("R24_DAY31_TO_2M"));
			obj.setR24_MORE2M_TO_3M(rs.getBigDecimal("R24_MORE2M_TO_3M"));
			obj.setR24_OVER3M_TO_6M(rs.getBigDecimal("R24_OVER3M_TO_6M"));
			obj.setR24_OVER6M_TO_1Y(rs.getBigDecimal("R24_OVER6M_TO_1Y"));
			obj.setR24_OVER1Y_TO_3Y(rs.getBigDecimal("R24_OVER1Y_TO_3Y"));
			obj.setR24_OVER3Y_TO_5Y(rs.getBigDecimal("R24_OVER3Y_TO_5Y"));
			obj.setR24_OVER5Y(rs.getBigDecimal("R24_OVER5Y"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
			obj.setR25_DAY1(rs.getBigDecimal("R25_DAY1"));
			obj.setR25_DAY2_7(rs.getBigDecimal("R25_DAY2_7"));
			obj.setR25_DAY8_14(rs.getBigDecimal("R25_DAY8_14"));
			obj.setR25_DAY15_30(rs.getBigDecimal("R25_DAY15_30"));
			obj.setR25_DAY31_TO_2M(rs.getBigDecimal("R25_DAY31_TO_2M"));
			obj.setR25_MORE2M_TO_3M(rs.getBigDecimal("R25_MORE2M_TO_3M"));
			obj.setR25_OVER3M_TO_6M(rs.getBigDecimal("R25_OVER3M_TO_6M"));
			obj.setR25_OVER6M_TO_1Y(rs.getBigDecimal("R25_OVER6M_TO_1Y"));
			obj.setR25_OVER1Y_TO_3Y(rs.getBigDecimal("R25_OVER1Y_TO_3Y"));
			obj.setR25_OVER3Y_TO_5Y(rs.getBigDecimal("R25_OVER3Y_TO_5Y"));
			obj.setR25_OVER5Y(rs.getBigDecimal("R25_OVER5Y"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_DAY1(rs.getBigDecimal("R26_DAY1"));
			obj.setR26_DAY2_7(rs.getBigDecimal("R26_DAY2_7"));
			obj.setR26_DAY8_14(rs.getBigDecimal("R26_DAY8_14"));
			obj.setR26_DAY15_30(rs.getBigDecimal("R26_DAY15_30"));
			obj.setR26_DAY31_TO_2M(rs.getBigDecimal("R26_DAY31_TO_2M"));
			obj.setR26_MORE2M_TO_3M(rs.getBigDecimal("R26_MORE2M_TO_3M"));
			obj.setR26_OVER3M_TO_6M(rs.getBigDecimal("R26_OVER3M_TO_6M"));
			obj.setR26_OVER6M_TO_1Y(rs.getBigDecimal("R26_OVER6M_TO_1Y"));
			obj.setR26_OVER1Y_TO_3Y(rs.getBigDecimal("R26_OVER1Y_TO_3Y"));
			obj.setR26_OVER3Y_TO_5Y(rs.getBigDecimal("R26_OVER3Y_TO_5Y"));
			obj.setR26_OVER5Y(rs.getBigDecimal("R26_OVER5Y"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_DAY1(rs.getBigDecimal("R27_DAY1"));
			obj.setR27_DAY2_7(rs.getBigDecimal("R27_DAY2_7"));
			obj.setR27_DAY8_14(rs.getBigDecimal("R27_DAY8_14"));
			obj.setR27_DAY15_30(rs.getBigDecimal("R27_DAY15_30"));
			obj.setR27_DAY31_TO_2M(rs.getBigDecimal("R27_DAY31_TO_2M"));
			obj.setR27_MORE2M_TO_3M(rs.getBigDecimal("R27_MORE2M_TO_3M"));
			obj.setR27_OVER3M_TO_6M(rs.getBigDecimal("R27_OVER3M_TO_6M"));
			obj.setR27_OVER6M_TO_1Y(rs.getBigDecimal("R27_OVER6M_TO_1Y"));
			obj.setR27_OVER1Y_TO_3Y(rs.getBigDecimal("R27_OVER1Y_TO_3Y"));
			obj.setR27_OVER3Y_TO_5Y(rs.getBigDecimal("R27_OVER3Y_TO_5Y"));
			obj.setR27_OVER5Y(rs.getBigDecimal("R27_OVER5Y"));
			obj.setR27_TOTAL(rs.getBigDecimal("R27_TOTAL"));
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_DAY1(rs.getBigDecimal("R28_DAY1"));
			obj.setR28_DAY2_7(rs.getBigDecimal("R28_DAY2_7"));
			obj.setR28_DAY8_14(rs.getBigDecimal("R28_DAY8_14"));
			obj.setR28_DAY15_30(rs.getBigDecimal("R28_DAY15_30"));
			obj.setR28_DAY31_TO_2M(rs.getBigDecimal("R28_DAY31_TO_2M"));
			obj.setR28_MORE2M_TO_3M(rs.getBigDecimal("R28_MORE2M_TO_3M"));
			obj.setR28_OVER3M_TO_6M(rs.getBigDecimal("R28_OVER3M_TO_6M"));
			obj.setR28_OVER6M_TO_1Y(rs.getBigDecimal("R28_OVER6M_TO_1Y"));
			obj.setR28_OVER1Y_TO_3Y(rs.getBigDecimal("R28_OVER1Y_TO_3Y"));
			obj.setR28_OVER3Y_TO_5Y(rs.getBigDecimal("R28_OVER3Y_TO_5Y"));
			obj.setR28_OVER5Y(rs.getBigDecimal("R28_OVER5Y"));
			obj.setR28_TOTAL(rs.getBigDecimal("R28_TOTAL"));
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_DAY1(rs.getBigDecimal("R29_DAY1"));
			obj.setR29_DAY2_7(rs.getBigDecimal("R29_DAY2_7"));
			obj.setR29_DAY8_14(rs.getBigDecimal("R29_DAY8_14"));
			obj.setR29_DAY15_30(rs.getBigDecimal("R29_DAY15_30"));
			obj.setR29_DAY31_TO_2M(rs.getBigDecimal("R29_DAY31_TO_2M"));
			obj.setR29_MORE2M_TO_3M(rs.getBigDecimal("R29_MORE2M_TO_3M"));
			obj.setR29_OVER3M_TO_6M(rs.getBigDecimal("R29_OVER3M_TO_6M"));
			obj.setR29_OVER6M_TO_1Y(rs.getBigDecimal("R29_OVER6M_TO_1Y"));
			obj.setR29_OVER1Y_TO_3Y(rs.getBigDecimal("R29_OVER1Y_TO_3Y"));
			obj.setR29_OVER3Y_TO_5Y(rs.getBigDecimal("R29_OVER3Y_TO_5Y"));
			obj.setR29_OVER5Y(rs.getBigDecimal("R29_OVER5Y"));
			obj.setR29_TOTAL(rs.getBigDecimal("R29_TOTAL"));
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_DAY1(rs.getBigDecimal("R30_DAY1"));
			obj.setR30_DAY2_7(rs.getBigDecimal("R30_DAY2_7"));
			obj.setR30_DAY8_14(rs.getBigDecimal("R30_DAY8_14"));
			obj.setR30_DAY15_30(rs.getBigDecimal("R30_DAY15_30"));
			obj.setR30_DAY31_TO_2M(rs.getBigDecimal("R30_DAY31_TO_2M"));
			obj.setR30_MORE2M_TO_3M(rs.getBigDecimal("R30_MORE2M_TO_3M"));
			obj.setR30_OVER3M_TO_6M(rs.getBigDecimal("R30_OVER3M_TO_6M"));
			obj.setR30_OVER6M_TO_1Y(rs.getBigDecimal("R30_OVER6M_TO_1Y"));
			obj.setR30_OVER1Y_TO_3Y(rs.getBigDecimal("R30_OVER1Y_TO_3Y"));
			obj.setR30_OVER3Y_TO_5Y(rs.getBigDecimal("R30_OVER3Y_TO_5Y"));
			obj.setR30_OVER5Y(rs.getBigDecimal("R30_OVER5Y"));
			obj.setR30_TOTAL(rs.getBigDecimal("R30_TOTAL"));
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_DAY1(rs.getBigDecimal("R31_DAY1"));
			obj.setR31_DAY2_7(rs.getBigDecimal("R31_DAY2_7"));
			obj.setR31_DAY8_14(rs.getBigDecimal("R31_DAY8_14"));
			obj.setR31_DAY15_30(rs.getBigDecimal("R31_DAY15_30"));
			obj.setR31_DAY31_TO_2M(rs.getBigDecimal("R31_DAY31_TO_2M"));
			obj.setR31_MORE2M_TO_3M(rs.getBigDecimal("R31_MORE2M_TO_3M"));
			obj.setR31_OVER3M_TO_6M(rs.getBigDecimal("R31_OVER3M_TO_6M"));
			obj.setR31_OVER6M_TO_1Y(rs.getBigDecimal("R31_OVER6M_TO_1Y"));
			obj.setR31_OVER1Y_TO_3Y(rs.getBigDecimal("R31_OVER1Y_TO_3Y"));
			obj.setR31_OVER3Y_TO_5Y(rs.getBigDecimal("R31_OVER3Y_TO_5Y"));
			obj.setR31_OVER5Y(rs.getBigDecimal("R31_OVER5Y"));
			obj.setR31_TOTAL(rs.getBigDecimal("R31_TOTAL"));
			obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
			obj.setR32_DAY1(rs.getBigDecimal("R32_DAY1"));
			obj.setR32_DAY2_7(rs.getBigDecimal("R32_DAY2_7"));
			obj.setR32_DAY8_14(rs.getBigDecimal("R32_DAY8_14"));
			obj.setR32_DAY15_30(rs.getBigDecimal("R32_DAY15_30"));
			obj.setR32_DAY31_TO_2M(rs.getBigDecimal("R32_DAY31_TO_2M"));
			obj.setR32_MORE2M_TO_3M(rs.getBigDecimal("R32_MORE2M_TO_3M"));
			obj.setR32_OVER3M_TO_6M(rs.getBigDecimal("R32_OVER3M_TO_6M"));
			obj.setR32_OVER6M_TO_1Y(rs.getBigDecimal("R32_OVER6M_TO_1Y"));
			obj.setR32_OVER1Y_TO_3Y(rs.getBigDecimal("R32_OVER1Y_TO_3Y"));
			obj.setR32_OVER3Y_TO_5Y(rs.getBigDecimal("R32_OVER3Y_TO_5Y"));
			obj.setR32_OVER5Y(rs.getBigDecimal("R32_OVER5Y"));
			obj.setR32_TOTAL(rs.getBigDecimal("R32_TOTAL"));
			obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
			obj.setR33_DAY1(rs.getBigDecimal("R33_DAY1"));
			obj.setR33_DAY2_7(rs.getBigDecimal("R33_DAY2_7"));
			obj.setR33_DAY8_14(rs.getBigDecimal("R33_DAY8_14"));
			obj.setR33_DAY15_30(rs.getBigDecimal("R33_DAY15_30"));
			obj.setR33_DAY31_TO_2M(rs.getBigDecimal("R33_DAY31_TO_2M"));
			obj.setR33_MORE2M_TO_3M(rs.getBigDecimal("R33_MORE2M_TO_3M"));
			obj.setR33_OVER3M_TO_6M(rs.getBigDecimal("R33_OVER3M_TO_6M"));
			obj.setR33_OVER6M_TO_1Y(rs.getBigDecimal("R33_OVER6M_TO_1Y"));
			obj.setR33_OVER1Y_TO_3Y(rs.getBigDecimal("R33_OVER1Y_TO_3Y"));
			obj.setR33_OVER3Y_TO_5Y(rs.getBigDecimal("R33_OVER3Y_TO_5Y"));
			obj.setR33_OVER5Y(rs.getBigDecimal("R33_OVER5Y"));
			obj.setR33_TOTAL(rs.getBigDecimal("R33_TOTAL"));
			obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
			obj.setR34_DAY1(rs.getBigDecimal("R34_DAY1"));
			obj.setR34_DAY2_7(rs.getBigDecimal("R34_DAY2_7"));
			obj.setR34_DAY8_14(rs.getBigDecimal("R34_DAY8_14"));
			obj.setR34_DAY15_30(rs.getBigDecimal("R34_DAY15_30"));
			obj.setR34_DAY31_TO_2M(rs.getBigDecimal("R34_DAY31_TO_2M"));
			obj.setR34_MORE2M_TO_3M(rs.getBigDecimal("R34_MORE2M_TO_3M"));
			obj.setR34_OVER3M_TO_6M(rs.getBigDecimal("R34_OVER3M_TO_6M"));
			obj.setR34_OVER6M_TO_1Y(rs.getBigDecimal("R34_OVER6M_TO_1Y"));
			obj.setR34_OVER1Y_TO_3Y(rs.getBigDecimal("R34_OVER1Y_TO_3Y"));
			obj.setR34_OVER3Y_TO_5Y(rs.getBigDecimal("R34_OVER3Y_TO_5Y"));
			obj.setR34_OVER5Y(rs.getBigDecimal("R34_OVER5Y"));
			obj.setR34_TOTAL(rs.getBigDecimal("R34_TOTAL"));
			obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
			obj.setR35_DAY1(rs.getBigDecimal("R35_DAY1"));
			obj.setR35_DAY2_7(rs.getBigDecimal("R35_DAY2_7"));
			obj.setR35_DAY8_14(rs.getBigDecimal("R35_DAY8_14"));
			obj.setR35_DAY15_30(rs.getBigDecimal("R35_DAY15_30"));
			obj.setR35_DAY31_TO_2M(rs.getBigDecimal("R35_DAY31_TO_2M"));
			obj.setR35_MORE2M_TO_3M(rs.getBigDecimal("R35_MORE2M_TO_3M"));
			obj.setR35_OVER3M_TO_6M(rs.getBigDecimal("R35_OVER3M_TO_6M"));
			obj.setR35_OVER6M_TO_1Y(rs.getBigDecimal("R35_OVER6M_TO_1Y"));
			obj.setR35_OVER1Y_TO_3Y(rs.getBigDecimal("R35_OVER1Y_TO_3Y"));
			obj.setR35_OVER3Y_TO_5Y(rs.getBigDecimal("R35_OVER3Y_TO_5Y"));
			obj.setR35_OVER5Y(rs.getBigDecimal("R35_OVER5Y"));
			obj.setR35_TOTAL(rs.getBigDecimal("R35_TOTAL"));
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_DAY1(rs.getBigDecimal("R36_DAY1"));
			obj.setR36_DAY2_7(rs.getBigDecimal("R36_DAY2_7"));
			obj.setR36_DAY8_14(rs.getBigDecimal("R36_DAY8_14"));
			obj.setR36_DAY15_30(rs.getBigDecimal("R36_DAY15_30"));
			obj.setR36_DAY31_TO_2M(rs.getBigDecimal("R36_DAY31_TO_2M"));
			obj.setR36_MORE2M_TO_3M(rs.getBigDecimal("R36_MORE2M_TO_3M"));
			obj.setR36_OVER3M_TO_6M(rs.getBigDecimal("R36_OVER3M_TO_6M"));
			obj.setR36_OVER6M_TO_1Y(rs.getBigDecimal("R36_OVER6M_TO_1Y"));
			obj.setR36_OVER1Y_TO_3Y(rs.getBigDecimal("R36_OVER1Y_TO_3Y"));
			obj.setR36_OVER3Y_TO_5Y(rs.getBigDecimal("R36_OVER3Y_TO_5Y"));
			obj.setR36_OVER5Y(rs.getBigDecimal("R36_OVER5Y"));
			obj.setR36_TOTAL(rs.getBigDecimal("R36_TOTAL"));
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
			obj.setR37_DAY1(rs.getBigDecimal("R37_DAY1"));
			obj.setR37_DAY2_7(rs.getBigDecimal("R37_DAY2_7"));
			obj.setR37_DAY8_14(rs.getBigDecimal("R37_DAY8_14"));
			obj.setR37_DAY15_30(rs.getBigDecimal("R37_DAY15_30"));
			obj.setR37_DAY31_TO_2M(rs.getBigDecimal("R37_DAY31_TO_2M"));
			obj.setR37_MORE2M_TO_3M(rs.getBigDecimal("R37_MORE2M_TO_3M"));
			obj.setR37_OVER3M_TO_6M(rs.getBigDecimal("R37_OVER3M_TO_6M"));
			obj.setR37_OVER6M_TO_1Y(rs.getBigDecimal("R37_OVER6M_TO_1Y"));
			obj.setR37_OVER1Y_TO_3Y(rs.getBigDecimal("R37_OVER1Y_TO_3Y"));
			obj.setR37_OVER3Y_TO_5Y(rs.getBigDecimal("R37_OVER3Y_TO_5Y"));
			obj.setR37_OVER5Y(rs.getBigDecimal("R37_OVER5Y"));
			obj.setR37_TOTAL(rs.getBigDecimal("R37_TOTAL"));
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
			obj.setR38_DAY1(rs.getBigDecimal("R38_DAY1"));
			obj.setR38_DAY2_7(rs.getBigDecimal("R38_DAY2_7"));
			obj.setR38_DAY8_14(rs.getBigDecimal("R38_DAY8_14"));
			obj.setR38_DAY15_30(rs.getBigDecimal("R38_DAY15_30"));
			obj.setR38_DAY31_TO_2M(rs.getBigDecimal("R38_DAY31_TO_2M"));
			obj.setR38_MORE2M_TO_3M(rs.getBigDecimal("R38_MORE2M_TO_3M"));
			obj.setR38_OVER3M_TO_6M(rs.getBigDecimal("R38_OVER3M_TO_6M"));
			obj.setR38_OVER6M_TO_1Y(rs.getBigDecimal("R38_OVER6M_TO_1Y"));
			obj.setR38_OVER1Y_TO_3Y(rs.getBigDecimal("R38_OVER1Y_TO_3Y"));
			obj.setR38_OVER3Y_TO_5Y(rs.getBigDecimal("R38_OVER3Y_TO_5Y"));
			obj.setR38_OVER5Y(rs.getBigDecimal("R38_OVER5Y"));
			obj.setR38_TOTAL(rs.getBigDecimal("R38_TOTAL"));
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
			obj.setR39_DAY1(rs.getBigDecimal("R39_DAY1"));
			obj.setR39_DAY2_7(rs.getBigDecimal("R39_DAY2_7"));
			obj.setR39_DAY8_14(rs.getBigDecimal("R39_DAY8_14"));
			obj.setR39_DAY15_30(rs.getBigDecimal("R39_DAY15_30"));
			obj.setR39_DAY31_TO_2M(rs.getBigDecimal("R39_DAY31_TO_2M"));
			obj.setR39_MORE2M_TO_3M(rs.getBigDecimal("R39_MORE2M_TO_3M"));
			obj.setR39_OVER3M_TO_6M(rs.getBigDecimal("R39_OVER3M_TO_6M"));
			obj.setR39_OVER6M_TO_1Y(rs.getBigDecimal("R39_OVER6M_TO_1Y"));
			obj.setR39_OVER1Y_TO_3Y(rs.getBigDecimal("R39_OVER1Y_TO_3Y"));
			obj.setR39_OVER3Y_TO_5Y(rs.getBigDecimal("R39_OVER3Y_TO_5Y"));
			obj.setR39_OVER5Y(rs.getBigDecimal("R39_OVER5Y"));
			obj.setR39_TOTAL(rs.getBigDecimal("R39_TOTAL"));
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
			obj.setR40_DAY1(rs.getBigDecimal("R40_DAY1"));
			obj.setR40_DAY2_7(rs.getBigDecimal("R40_DAY2_7"));
			obj.setR40_DAY8_14(rs.getBigDecimal("R40_DAY8_14"));
			obj.setR40_DAY15_30(rs.getBigDecimal("R40_DAY15_30"));
			obj.setR40_DAY31_TO_2M(rs.getBigDecimal("R40_DAY31_TO_2M"));
			obj.setR40_MORE2M_TO_3M(rs.getBigDecimal("R40_MORE2M_TO_3M"));
			obj.setR40_OVER3M_TO_6M(rs.getBigDecimal("R40_OVER3M_TO_6M"));
			obj.setR40_OVER6M_TO_1Y(rs.getBigDecimal("R40_OVER6M_TO_1Y"));
			obj.setR40_OVER1Y_TO_3Y(rs.getBigDecimal("R40_OVER1Y_TO_3Y"));
			obj.setR40_OVER3Y_TO_5Y(rs.getBigDecimal("R40_OVER3Y_TO_5Y"));
			obj.setR40_OVER5Y(rs.getBigDecimal("R40_OVER5Y"));
			obj.setR40_TOTAL(rs.getBigDecimal("R40_TOTAL"));
//			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
//			obj.setR41_DAY1(rs.getBigDecimal("R41_DAY1"));
//			obj.setR41_DAY2_7(rs.getBigDecimal("R41_DAY2_7"));
//			obj.setR41_DAY8_14(rs.getBigDecimal("R41_DAY8_14"));
//			obj.setR41_DAY15_30(rs.getBigDecimal("R41_DAY15_30"));
//			obj.setR41_DAY31_TO_2M(rs.getBigDecimal("R41_DAY31_TO_2M"));
//			obj.setR41_MORE2M_TO_3M(rs.getBigDecimal("R41_MORE2M_TO_3M"));
//			obj.setR41_OVER3M_TO_6M(rs.getBigDecimal("R41_OVER3M_TO_6M"));
//			obj.setR41_OVER6M_TO_1Y(rs.getBigDecimal("R41_OVER6M_TO_1Y"));
//			obj.setR41_OVER1Y_TO_3Y(rs.getBigDecimal("R41_OVER1Y_TO_3Y"));
//			obj.setR41_OVER3Y_TO_5Y(rs.getBigDecimal("R41_OVER3Y_TO_5Y"));
//			obj.setR41_OVER5Y(rs.getBigDecimal("R41_OVER5Y"));
//			obj.setR41_TOTAL(rs.getBigDecimal("R41_TOTAL"));
//			obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
//			obj.setR42_DAY1(rs.getBigDecimal("R42_DAY1"));
//			obj.setR42_DAY2_7(rs.getBigDecimal("R42_DAY2_7"));
//			obj.setR42_DAY8_14(rs.getBigDecimal("R42_DAY8_14"));
//			obj.setR42_DAY15_30(rs.getBigDecimal("R42_DAY15_30"));
//			obj.setR42_DAY31_TO_2M(rs.getBigDecimal("R42_DAY31_TO_2M"));
//			obj.setR42_MORE2M_TO_3M(rs.getBigDecimal("R42_MORE2M_TO_3M"));
//			obj.setR42_OVER3M_TO_6M(rs.getBigDecimal("R42_OVER3M_TO_6M"));
//			obj.setR42_OVER6M_TO_1Y(rs.getBigDecimal("R42_OVER6M_TO_1Y"));
//			obj.setR42_OVER1Y_TO_3Y(rs.getBigDecimal("R42_OVER1Y_TO_3Y"));
//			obj.setR42_OVER3Y_TO_5Y(rs.getBigDecimal("R42_OVER3Y_TO_5Y"));
//			obj.setR42_OVER5Y(rs.getBigDecimal("R42_OVER5Y"));
//			obj.setR42_TOTAL(rs.getBigDecimal("R42_TOTAL"));
//			obj.setR43_PRODUCT(rs.getString("R43_PRODUCT"));
//			obj.setR43_DAY1(rs.getBigDecimal("R43_DAY1"));
//			obj.setR43_DAY2_7(rs.getBigDecimal("R43_DAY2_7"));
//			obj.setR43_DAY8_14(rs.getBigDecimal("R43_DAY8_14"));
//			obj.setR43_DAY15_30(rs.getBigDecimal("R43_DAY15_30"));
//			obj.setR43_DAY31_TO_2M(rs.getBigDecimal("R43_DAY31_TO_2M"));
//			obj.setR43_MORE2M_TO_3M(rs.getBigDecimal("R43_MORE2M_TO_3M"));
//			obj.setR43_OVER3M_TO_6M(rs.getBigDecimal("R43_OVER3M_TO_6M"));
//			obj.setR43_OVER6M_TO_1Y(rs.getBigDecimal("R43_OVER6M_TO_1Y"));
//			obj.setR43_OVER1Y_TO_3Y(rs.getBigDecimal("R43_OVER1Y_TO_3Y"));
//			obj.setR43_OVER3Y_TO_5Y(rs.getBigDecimal("R43_OVER3Y_TO_5Y"));
//			obj.setR43_OVER5Y(rs.getBigDecimal("R43_OVER5Y"));
//			obj.setR43_TOTAL(rs.getBigDecimal("R43_TOTAL"));
//			obj.setR44_PRODUCT(rs.getString("R44_PRODUCT"));
//			obj.setR44_DAY1(rs.getBigDecimal("R44_DAY1"));
//			obj.setR44_DAY2_7(rs.getBigDecimal("R44_DAY2_7"));
//			obj.setR44_DAY8_14(rs.getBigDecimal("R44_DAY8_14"));
//			obj.setR44_DAY15_30(rs.getBigDecimal("R44_DAY15_30"));
//			obj.setR44_DAY31_TO_2M(rs.getBigDecimal("R44_DAY31_TO_2M"));
//			obj.setR44_MORE2M_TO_3M(rs.getBigDecimal("R44_MORE2M_TO_3M"));
//			obj.setR44_OVER3M_TO_6M(rs.getBigDecimal("R44_OVER3M_TO_6M"));
//			obj.setR44_OVER6M_TO_1Y(rs.getBigDecimal("R44_OVER6M_TO_1Y"));
//			obj.setR44_OVER1Y_TO_3Y(rs.getBigDecimal("R44_OVER1Y_TO_3Y"));
//			obj.setR44_OVER3Y_TO_5Y(rs.getBigDecimal("R44_OVER3Y_TO_5Y"));
//			obj.setR44_OVER5Y(rs.getBigDecimal("R44_OVER5Y"));
//			obj.setR44_TOTAL(rs.getBigDecimal("R44_TOTAL"));
//			obj.setR45_PRODUCT(rs.getString("R45_PRODUCT"));
//			obj.setR45_DAY1(rs.getBigDecimal("R45_DAY1"));
//			obj.setR45_DAY2_7(rs.getBigDecimal("R45_DAY2_7"));
//			obj.setR45_DAY8_14(rs.getBigDecimal("R45_DAY8_14"));
//			obj.setR45_DAY15_30(rs.getBigDecimal("R45_DAY15_30"));
//			obj.setR45_DAY31_TO_2M(rs.getBigDecimal("R45_DAY31_TO_2M"));
//			obj.setR45_MORE2M_TO_3M(rs.getBigDecimal("R45_MORE2M_TO_3M"));
//			obj.setR45_OVER3M_TO_6M(rs.getBigDecimal("R45_OVER3M_TO_6M"));
//			obj.setR45_OVER6M_TO_1Y(rs.getBigDecimal("R45_OVER6M_TO_1Y"));
//			obj.setR45_OVER1Y_TO_3Y(rs.getBigDecimal("R45_OVER1Y_TO_3Y"));
//			obj.setR45_OVER3Y_TO_5Y(rs.getBigDecimal("R45_OVER3Y_TO_5Y"));
//			obj.setR45_OVER5Y(rs.getBigDecimal("R45_OVER5Y"));
//			obj.setR45_TOTAL(rs.getBigDecimal("R45_TOTAL"));
//			obj.setR46_PRODUCT(rs.getString("R46_PRODUCT"));
//			obj.setR46_DAY1(rs.getBigDecimal("R46_DAY1"));
//			obj.setR46_DAY2_7(rs.getBigDecimal("R46_DAY2_7"));
//			obj.setR46_DAY8_14(rs.getBigDecimal("R46_DAY8_14"));
//			obj.setR46_DAY15_30(rs.getBigDecimal("R46_DAY15_30"));
//			obj.setR46_DAY31_TO_2M(rs.getBigDecimal("R46_DAY31_TO_2M"));
//			obj.setR46_MORE2M_TO_3M(rs.getBigDecimal("R46_MORE2M_TO_3M"));
//			obj.setR46_OVER3M_TO_6M(rs.getBigDecimal("R46_OVER3M_TO_6M"));
//			obj.setR46_OVER6M_TO_1Y(rs.getBigDecimal("R46_OVER6M_TO_1Y"));
//			obj.setR46_OVER1Y_TO_3Y(rs.getBigDecimal("R46_OVER1Y_TO_3Y"));
//			obj.setR46_OVER3Y_TO_5Y(rs.getBigDecimal("R46_OVER3Y_TO_5Y"));
//			obj.setR46_OVER5Y(rs.getBigDecimal("R46_OVER5Y"));
//			obj.setR46_TOTAL(rs.getBigDecimal("R46_TOTAL"));
			obj.setR47_PRODUCT(rs.getString("R47_PRODUCT"));
			obj.setR47_DAY1(rs.getBigDecimal("R47_DAY1"));
			obj.setR47_DAY2_7(rs.getBigDecimal("R47_DAY2_7"));
			obj.setR47_DAY8_14(rs.getBigDecimal("R47_DAY8_14"));
			obj.setR47_DAY15_30(rs.getBigDecimal("R47_DAY15_30"));
			obj.setR47_DAY31_TO_2M(rs.getBigDecimal("R47_DAY31_TO_2M"));
			obj.setR47_MORE2M_TO_3M(rs.getBigDecimal("R47_MORE2M_TO_3M"));
			obj.setR47_OVER3M_TO_6M(rs.getBigDecimal("R47_OVER3M_TO_6M"));
			obj.setR47_OVER6M_TO_1Y(rs.getBigDecimal("R47_OVER6M_TO_1Y"));
			obj.setR47_OVER1Y_TO_3Y(rs.getBigDecimal("R47_OVER1Y_TO_3Y"));
			obj.setR47_OVER3Y_TO_5Y(rs.getBigDecimal("R47_OVER3Y_TO_5Y"));
			obj.setR47_OVER5Y(rs.getBigDecimal("R47_OVER5Y"));
			obj.setR47_TOTAL(rs.getBigDecimal("R47_TOTAL"));
			obj.setR48_PRODUCT(rs.getString("R48_PRODUCT"));
			obj.setR48_DAY1(rs.getBigDecimal("R48_DAY1"));
			obj.setR48_DAY2_7(rs.getBigDecimal("R48_DAY2_7"));
			obj.setR48_DAY8_14(rs.getBigDecimal("R48_DAY8_14"));
			obj.setR48_DAY15_30(rs.getBigDecimal("R48_DAY15_30"));
			obj.setR48_DAY31_TO_2M(rs.getBigDecimal("R48_DAY31_TO_2M"));
			obj.setR48_MORE2M_TO_3M(rs.getBigDecimal("R48_MORE2M_TO_3M"));
			obj.setR48_OVER3M_TO_6M(rs.getBigDecimal("R48_OVER3M_TO_6M"));
			obj.setR48_OVER6M_TO_1Y(rs.getBigDecimal("R48_OVER6M_TO_1Y"));
			obj.setR48_OVER1Y_TO_3Y(rs.getBigDecimal("R48_OVER1Y_TO_3Y"));
			obj.setR48_OVER3Y_TO_5Y(rs.getBigDecimal("R48_OVER3Y_TO_5Y"));
			obj.setR48_OVER5Y(rs.getBigDecimal("R48_OVER5Y"));
			obj.setR48_TOTAL(rs.getBigDecimal("R48_TOTAL"));
			obj.setR49_PRODUCT(rs.getString("R49_PRODUCT"));
			obj.setR49_DAY1(rs.getBigDecimal("R49_DAY1"));
			obj.setR49_DAY2_7(rs.getBigDecimal("R49_DAY2_7"));
			obj.setR49_DAY8_14(rs.getBigDecimal("R49_DAY8_14"));
			obj.setR49_DAY15_30(rs.getBigDecimal("R49_DAY15_30"));
			obj.setR49_DAY31_TO_2M(rs.getBigDecimal("R49_DAY31_TO_2M"));
			obj.setR49_MORE2M_TO_3M(rs.getBigDecimal("R49_MORE2M_TO_3M"));
			obj.setR49_OVER3M_TO_6M(rs.getBigDecimal("R49_OVER3M_TO_6M"));
			obj.setR49_OVER6M_TO_1Y(rs.getBigDecimal("R49_OVER6M_TO_1Y"));
			obj.setR49_OVER1Y_TO_3Y(rs.getBigDecimal("R49_OVER1Y_TO_3Y"));
			obj.setR49_OVER3Y_TO_5Y(rs.getBigDecimal("R49_OVER3Y_TO_5Y"));
			obj.setR49_OVER5Y(rs.getBigDecimal("R49_OVER5Y"));
			obj.setR49_TOTAL(rs.getBigDecimal("R49_TOTAL"));
			obj.setR50_PRODUCT(rs.getString("R50_PRODUCT"));
			obj.setR50_DAY1(rs.getBigDecimal("R50_DAY1"));
			obj.setR50_DAY2_7(rs.getBigDecimal("R50_DAY2_7"));
			obj.setR50_DAY8_14(rs.getBigDecimal("R50_DAY8_14"));
			obj.setR50_DAY15_30(rs.getBigDecimal("R50_DAY15_30"));
			obj.setR50_DAY31_TO_2M(rs.getBigDecimal("R50_DAY31_TO_2M"));
			obj.setR50_MORE2M_TO_3M(rs.getBigDecimal("R50_MORE2M_TO_3M"));
			obj.setR50_OVER3M_TO_6M(rs.getBigDecimal("R50_OVER3M_TO_6M"));
			obj.setR50_OVER6M_TO_1Y(rs.getBigDecimal("R50_OVER6M_TO_1Y"));
			obj.setR50_OVER1Y_TO_3Y(rs.getBigDecimal("R50_OVER1Y_TO_3Y"));
			obj.setR50_OVER3Y_TO_5Y(rs.getBigDecimal("R50_OVER3Y_TO_5Y"));
			obj.setR50_OVER5Y(rs.getBigDecimal("R50_OVER5Y"));
			obj.setR50_TOTAL(rs.getBigDecimal("R50_TOTAL"));
			obj.setR51_PRODUCT(rs.getString("R51_PRODUCT"));
			obj.setR51_DAY1(rs.getBigDecimal("R51_DAY1"));
			obj.setR51_DAY2_7(rs.getBigDecimal("R51_DAY2_7"));
			obj.setR51_DAY8_14(rs.getBigDecimal("R51_DAY8_14"));
			obj.setR51_DAY15_30(rs.getBigDecimal("R51_DAY15_30"));
			obj.setR51_DAY31_TO_2M(rs.getBigDecimal("R51_DAY31_TO_2M"));
			obj.setR51_MORE2M_TO_3M(rs.getBigDecimal("R51_MORE2M_TO_3M"));
			obj.setR51_OVER3M_TO_6M(rs.getBigDecimal("R51_OVER3M_TO_6M"));
			obj.setR51_OVER6M_TO_1Y(rs.getBigDecimal("R51_OVER6M_TO_1Y"));
			obj.setR51_OVER1Y_TO_3Y(rs.getBigDecimal("R51_OVER1Y_TO_3Y"));
			obj.setR51_OVER3Y_TO_5Y(rs.getBigDecimal("R51_OVER3Y_TO_5Y"));
			obj.setR51_OVER5Y(rs.getBigDecimal("R51_OVER5Y"));
			obj.setR51_TOTAL(rs.getBigDecimal("R51_TOTAL"));
			obj.setR52_PRODUCT(rs.getString("R52_PRODUCT"));
			obj.setR52_DAY1(rs.getBigDecimal("R52_DAY1"));
			obj.setR52_DAY2_7(rs.getBigDecimal("R52_DAY2_7"));
			obj.setR52_DAY8_14(rs.getBigDecimal("R52_DAY8_14"));
			obj.setR52_DAY15_30(rs.getBigDecimal("R52_DAY15_30"));
			obj.setR52_DAY31_TO_2M(rs.getBigDecimal("R52_DAY31_TO_2M"));
			obj.setR52_MORE2M_TO_3M(rs.getBigDecimal("R52_MORE2M_TO_3M"));
			obj.setR52_OVER3M_TO_6M(rs.getBigDecimal("R52_OVER3M_TO_6M"));
			obj.setR52_OVER6M_TO_1Y(rs.getBigDecimal("R52_OVER6M_TO_1Y"));
			obj.setR52_OVER1Y_TO_3Y(rs.getBigDecimal("R52_OVER1Y_TO_3Y"));
			obj.setR52_OVER3Y_TO_5Y(rs.getBigDecimal("R52_OVER3Y_TO_5Y"));
			obj.setR52_OVER5Y(rs.getBigDecimal("R52_OVER5Y"));
			obj.setR52_TOTAL(rs.getBigDecimal("R52_TOTAL"));
			obj.setR53_PRODUCT(rs.getString("R53_PRODUCT"));
			obj.setR53_DAY1(rs.getBigDecimal("R53_DAY1"));
			obj.setR53_DAY2_7(rs.getBigDecimal("R53_DAY2_7"));
			obj.setR53_DAY8_14(rs.getBigDecimal("R53_DAY8_14"));
			obj.setR53_DAY15_30(rs.getBigDecimal("R53_DAY15_30"));
			obj.setR53_DAY31_TO_2M(rs.getBigDecimal("R53_DAY31_TO_2M"));
			obj.setR53_MORE2M_TO_3M(rs.getBigDecimal("R53_MORE2M_TO_3M"));
			obj.setR53_OVER3M_TO_6M(rs.getBigDecimal("R53_OVER3M_TO_6M"));
			obj.setR53_OVER6M_TO_1Y(rs.getBigDecimal("R53_OVER6M_TO_1Y"));
			obj.setR53_OVER1Y_TO_3Y(rs.getBigDecimal("R53_OVER1Y_TO_3Y"));
			obj.setR53_OVER3Y_TO_5Y(rs.getBigDecimal("R53_OVER3Y_TO_5Y"));
			obj.setR53_OVER5Y(rs.getBigDecimal("R53_OVER5Y"));
			obj.setR53_TOTAL(rs.getBigDecimal("R53_TOTAL"));
			obj.setR54_PRODUCT(rs.getString("R54_PRODUCT"));
			obj.setR54_DAY1(rs.getBigDecimal("R54_DAY1"));
			obj.setR54_DAY2_7(rs.getBigDecimal("R54_DAY2_7"));
			obj.setR54_DAY8_14(rs.getBigDecimal("R54_DAY8_14"));
			obj.setR54_DAY15_30(rs.getBigDecimal("R54_DAY15_30"));
			obj.setR54_DAY31_TO_2M(rs.getBigDecimal("R54_DAY31_TO_2M"));
			obj.setR54_MORE2M_TO_3M(rs.getBigDecimal("R54_MORE2M_TO_3M"));
			obj.setR54_OVER3M_TO_6M(rs.getBigDecimal("R54_OVER3M_TO_6M"));
			obj.setR54_OVER6M_TO_1Y(rs.getBigDecimal("R54_OVER6M_TO_1Y"));
			obj.setR54_OVER1Y_TO_3Y(rs.getBigDecimal("R54_OVER1Y_TO_3Y"));
			obj.setR54_OVER3Y_TO_5Y(rs.getBigDecimal("R54_OVER3Y_TO_5Y"));
			obj.setR54_OVER5Y(rs.getBigDecimal("R54_OVER5Y"));
			obj.setR54_TOTAL(rs.getBigDecimal("R54_TOTAL"));
			obj.setR55_PRODUCT(rs.getString("R55_PRODUCT"));
			obj.setR55_DAY1(rs.getBigDecimal("R55_DAY1"));
			obj.setR55_DAY2_7(rs.getBigDecimal("R55_DAY2_7"));
			obj.setR55_DAY8_14(rs.getBigDecimal("R55_DAY8_14"));
			obj.setR55_DAY15_30(rs.getBigDecimal("R55_DAY15_30"));
			obj.setR55_DAY31_TO_2M(rs.getBigDecimal("R55_DAY31_TO_2M"));
			obj.setR55_MORE2M_TO_3M(rs.getBigDecimal("R55_MORE2M_TO_3M"));
			obj.setR55_OVER3M_TO_6M(rs.getBigDecimal("R55_OVER3M_TO_6M"));
			obj.setR55_OVER6M_TO_1Y(rs.getBigDecimal("R55_OVER6M_TO_1Y"));
			obj.setR55_OVER1Y_TO_3Y(rs.getBigDecimal("R55_OVER1Y_TO_3Y"));
			obj.setR55_OVER3Y_TO_5Y(rs.getBigDecimal("R55_OVER3Y_TO_5Y"));
			obj.setR55_OVER5Y(rs.getBigDecimal("R55_OVER5Y"));
			obj.setR55_TOTAL(rs.getBigDecimal("R55_TOTAL"));
			obj.setR56_PRODUCT(rs.getString("R56_PRODUCT"));
			obj.setR56_DAY1(rs.getBigDecimal("R56_DAY1"));
			obj.setR56_DAY2_7(rs.getBigDecimal("R56_DAY2_7"));
			obj.setR56_DAY8_14(rs.getBigDecimal("R56_DAY8_14"));
			obj.setR56_DAY15_30(rs.getBigDecimal("R56_DAY15_30"));
			obj.setR56_DAY31_TO_2M(rs.getBigDecimal("R56_DAY31_TO_2M"));
			obj.setR56_MORE2M_TO_3M(rs.getBigDecimal("R56_MORE2M_TO_3M"));
			obj.setR56_OVER3M_TO_6M(rs.getBigDecimal("R56_OVER3M_TO_6M"));
			obj.setR56_OVER6M_TO_1Y(rs.getBigDecimal("R56_OVER6M_TO_1Y"));
			obj.setR56_OVER1Y_TO_3Y(rs.getBigDecimal("R56_OVER1Y_TO_3Y"));
			obj.setR56_OVER3Y_TO_5Y(rs.getBigDecimal("R56_OVER3Y_TO_5Y"));
			obj.setR56_OVER5Y(rs.getBigDecimal("R56_OVER5Y"));
			obj.setR56_TOTAL(rs.getBigDecimal("R56_TOTAL"));
			obj.setR57_PRODUCT(rs.getString("R57_PRODUCT"));
			obj.setR57_DAY1(rs.getBigDecimal("R57_DAY1"));
			obj.setR57_DAY2_7(rs.getBigDecimal("R57_DAY2_7"));
			obj.setR57_DAY8_14(rs.getBigDecimal("R57_DAY8_14"));
			obj.setR57_DAY15_30(rs.getBigDecimal("R57_DAY15_30"));
			obj.setR57_DAY31_TO_2M(rs.getBigDecimal("R57_DAY31_TO_2M"));
			obj.setR57_MORE2M_TO_3M(rs.getBigDecimal("R57_MORE2M_TO_3M"));
			obj.setR57_OVER3M_TO_6M(rs.getBigDecimal("R57_OVER3M_TO_6M"));
			obj.setR57_OVER6M_TO_1Y(rs.getBigDecimal("R57_OVER6M_TO_1Y"));
			obj.setR57_OVER1Y_TO_3Y(rs.getBigDecimal("R57_OVER1Y_TO_3Y"));
			obj.setR57_OVER3Y_TO_5Y(rs.getBigDecimal("R57_OVER3Y_TO_5Y"));
			obj.setR57_OVER5Y(rs.getBigDecimal("R57_OVER5Y"));
			obj.setR57_TOTAL(rs.getBigDecimal("R57_TOTAL"));
			obj.setR58_PRODUCT(rs.getString("R58_PRODUCT"));
			obj.setR58_DAY1(rs.getBigDecimal("R58_DAY1"));
			obj.setR58_DAY2_7(rs.getBigDecimal("R58_DAY2_7"));
			obj.setR58_DAY8_14(rs.getBigDecimal("R58_DAY8_14"));
			obj.setR58_DAY15_30(rs.getBigDecimal("R58_DAY15_30"));
			obj.setR58_DAY31_TO_2M(rs.getBigDecimal("R58_DAY31_TO_2M"));
			obj.setR58_MORE2M_TO_3M(rs.getBigDecimal("R58_MORE2M_TO_3M"));
			obj.setR58_OVER3M_TO_6M(rs.getBigDecimal("R58_OVER3M_TO_6M"));
			obj.setR58_OVER6M_TO_1Y(rs.getBigDecimal("R58_OVER6M_TO_1Y"));
			obj.setR58_OVER1Y_TO_3Y(rs.getBigDecimal("R58_OVER1Y_TO_3Y"));
			obj.setR58_OVER3Y_TO_5Y(rs.getBigDecimal("R58_OVER3Y_TO_5Y"));
			obj.setR58_OVER5Y(rs.getBigDecimal("R58_OVER5Y"));
			obj.setR58_TOTAL(rs.getBigDecimal("R58_TOTAL"));
			obj.setR59_PRODUCT(rs.getString("R59_PRODUCT"));
			obj.setR59_DAY1(rs.getBigDecimal("R59_DAY1"));
			obj.setR59_DAY2_7(rs.getBigDecimal("R59_DAY2_7"));
			obj.setR59_DAY8_14(rs.getBigDecimal("R59_DAY8_14"));
			obj.setR59_DAY15_30(rs.getBigDecimal("R59_DAY15_30"));
			obj.setR59_DAY31_TO_2M(rs.getBigDecimal("R59_DAY31_TO_2M"));
			obj.setR59_MORE2M_TO_3M(rs.getBigDecimal("R59_MORE2M_TO_3M"));
			obj.setR59_OVER3M_TO_6M(rs.getBigDecimal("R59_OVER3M_TO_6M"));
			obj.setR59_OVER6M_TO_1Y(rs.getBigDecimal("R59_OVER6M_TO_1Y"));
			obj.setR59_OVER1Y_TO_3Y(rs.getBigDecimal("R59_OVER1Y_TO_3Y"));
			obj.setR59_OVER3Y_TO_5Y(rs.getBigDecimal("R59_OVER3Y_TO_5Y"));
			obj.setR59_OVER5Y(rs.getBigDecimal("R59_OVER5Y"));
			obj.setR59_TOTAL(rs.getBigDecimal("R59_TOTAL"));
			obj.setR60_PRODUCT(rs.getString("R60_PRODUCT"));
			obj.setR60_DAY1(rs.getBigDecimal("R60_DAY1"));
			obj.setR60_DAY2_7(rs.getBigDecimal("R60_DAY2_7"));
			obj.setR60_DAY8_14(rs.getBigDecimal("R60_DAY8_14"));
			obj.setR60_DAY15_30(rs.getBigDecimal("R60_DAY15_30"));
			obj.setR60_DAY31_TO_2M(rs.getBigDecimal("R60_DAY31_TO_2M"));
			obj.setR60_MORE2M_TO_3M(rs.getBigDecimal("R60_MORE2M_TO_3M"));
			obj.setR60_OVER3M_TO_6M(rs.getBigDecimal("R60_OVER3M_TO_6M"));
			obj.setR60_OVER6M_TO_1Y(rs.getBigDecimal("R60_OVER6M_TO_1Y"));
			obj.setR60_OVER1Y_TO_3Y(rs.getBigDecimal("R60_OVER1Y_TO_3Y"));
			obj.setR60_OVER3Y_TO_5Y(rs.getBigDecimal("R60_OVER3Y_TO_5Y"));
			obj.setR60_OVER5Y(rs.getBigDecimal("R60_OVER5Y"));
			obj.setR60_TOTAL(rs.getBigDecimal("R60_TOTAL"));
			obj.setR61_PRODUCT(rs.getString("R61_PRODUCT"));
			obj.setR61_DAY1(rs.getBigDecimal("R61_DAY1"));
			obj.setR61_DAY2_7(rs.getBigDecimal("R61_DAY2_7"));
			obj.setR61_DAY8_14(rs.getBigDecimal("R61_DAY8_14"));
			obj.setR61_DAY15_30(rs.getBigDecimal("R61_DAY15_30"));
			obj.setR61_DAY31_TO_2M(rs.getBigDecimal("R61_DAY31_TO_2M"));
			obj.setR61_MORE2M_TO_3M(rs.getBigDecimal("R61_MORE2M_TO_3M"));
			obj.setR61_OVER3M_TO_6M(rs.getBigDecimal("R61_OVER3M_TO_6M"));
			obj.setR61_OVER6M_TO_1Y(rs.getBigDecimal("R61_OVER6M_TO_1Y"));
			obj.setR61_OVER1Y_TO_3Y(rs.getBigDecimal("R61_OVER1Y_TO_3Y"));
			obj.setR61_OVER3Y_TO_5Y(rs.getBigDecimal("R61_OVER3Y_TO_5Y"));
			obj.setR61_OVER5Y(rs.getBigDecimal("R61_OVER5Y"));
			obj.setR61_TOTAL(rs.getBigDecimal("R61_TOTAL"));
			obj.setR62_PRODUCT(rs.getString("R62_PRODUCT"));
			obj.setR62_DAY1(rs.getBigDecimal("R62_DAY1"));
			obj.setR62_DAY2_7(rs.getBigDecimal("R62_DAY2_7"));
			obj.setR62_DAY8_14(rs.getBigDecimal("R62_DAY8_14"));
			obj.setR62_DAY15_30(rs.getBigDecimal("R62_DAY15_30"));
			obj.setR62_DAY31_TO_2M(rs.getBigDecimal("R62_DAY31_TO_2M"));
			obj.setR62_MORE2M_TO_3M(rs.getBigDecimal("R62_MORE2M_TO_3M"));
			obj.setR62_OVER3M_TO_6M(rs.getBigDecimal("R62_OVER3M_TO_6M"));
			obj.setR62_OVER6M_TO_1Y(rs.getBigDecimal("R62_OVER6M_TO_1Y"));
			obj.setR62_OVER1Y_TO_3Y(rs.getBigDecimal("R62_OVER1Y_TO_3Y"));
			obj.setR62_OVER3Y_TO_5Y(rs.getBigDecimal("R62_OVER3Y_TO_5Y"));
			obj.setR62_OVER5Y(rs.getBigDecimal("R62_OVER5Y"));
			obj.setR62_TOTAL(rs.getBigDecimal("R62_TOTAL"));
			obj.setR63_PRODUCT(rs.getString("R63_PRODUCT"));
			obj.setR63_DAY1(rs.getBigDecimal("R63_DAY1"));
			obj.setR63_DAY2_7(rs.getBigDecimal("R63_DAY2_7"));
			obj.setR63_DAY8_14(rs.getBigDecimal("R63_DAY8_14"));
			obj.setR63_DAY15_30(rs.getBigDecimal("R63_DAY15_30"));
			obj.setR63_DAY31_TO_2M(rs.getBigDecimal("R63_DAY31_TO_2M"));
			obj.setR63_MORE2M_TO_3M(rs.getBigDecimal("R63_MORE2M_TO_3M"));
			obj.setR63_OVER3M_TO_6M(rs.getBigDecimal("R63_OVER3M_TO_6M"));
			obj.setR63_OVER6M_TO_1Y(rs.getBigDecimal("R63_OVER6M_TO_1Y"));
			obj.setR63_OVER1Y_TO_3Y(rs.getBigDecimal("R63_OVER1Y_TO_3Y"));
			obj.setR63_OVER3Y_TO_5Y(rs.getBigDecimal("R63_OVER3Y_TO_5Y"));
			obj.setR63_OVER5Y(rs.getBigDecimal("R63_OVER5Y"));
			obj.setR63_TOTAL(rs.getBigDecimal("R63_TOTAL"));
			obj.setR64_PRODUCT(rs.getString("R64_PRODUCT"));
			obj.setR64_DAY1(rs.getBigDecimal("R64_DAY1"));
			obj.setR64_DAY2_7(rs.getBigDecimal("R64_DAY2_7"));
			obj.setR64_DAY8_14(rs.getBigDecimal("R64_DAY8_14"));
			obj.setR64_DAY15_30(rs.getBigDecimal("R64_DAY15_30"));
			obj.setR64_DAY31_TO_2M(rs.getBigDecimal("R64_DAY31_TO_2M"));
			obj.setR64_MORE2M_TO_3M(rs.getBigDecimal("R64_MORE2M_TO_3M"));
			obj.setR64_OVER3M_TO_6M(rs.getBigDecimal("R64_OVER3M_TO_6M"));
			obj.setR64_OVER6M_TO_1Y(rs.getBigDecimal("R64_OVER6M_TO_1Y"));
			obj.setR64_OVER1Y_TO_3Y(rs.getBigDecimal("R64_OVER1Y_TO_3Y"));
			obj.setR64_OVER3Y_TO_5Y(rs.getBigDecimal("R64_OVER3Y_TO_5Y"));
			obj.setR64_OVER5Y(rs.getBigDecimal("R64_OVER5Y"));
			obj.setR64_TOTAL(rs.getBigDecimal("R64_TOTAL"));
			obj.setR65_PRODUCT(rs.getString("R65_PRODUCT"));
			obj.setR65_DAY1(rs.getBigDecimal("R65_DAY1"));
			obj.setR65_DAY2_7(rs.getBigDecimal("R65_DAY2_7"));
			obj.setR65_DAY8_14(rs.getBigDecimal("R65_DAY8_14"));
			obj.setR65_DAY15_30(rs.getBigDecimal("R65_DAY15_30"));
			obj.setR65_DAY31_TO_2M(rs.getBigDecimal("R65_DAY31_TO_2M"));
			obj.setR65_MORE2M_TO_3M(rs.getBigDecimal("R65_MORE2M_TO_3M"));
			obj.setR65_OVER3M_TO_6M(rs.getBigDecimal("R65_OVER3M_TO_6M"));
			obj.setR65_OVER6M_TO_1Y(rs.getBigDecimal("R65_OVER6M_TO_1Y"));
			obj.setR65_OVER1Y_TO_3Y(rs.getBigDecimal("R65_OVER1Y_TO_3Y"));
			obj.setR65_OVER3Y_TO_5Y(rs.getBigDecimal("R65_OVER3Y_TO_5Y"));
			obj.setR65_OVER5Y(rs.getBigDecimal("R65_OVER5Y"));
			obj.setR65_TOTAL(rs.getBigDecimal("R65_TOTAL"));
			obj.setR66_PRODUCT(rs.getString("R66_PRODUCT"));
			obj.setR66_DAY1(rs.getBigDecimal("R66_DAY1"));
			obj.setR66_DAY2_7(rs.getBigDecimal("R66_DAY2_7"));
			obj.setR66_DAY8_14(rs.getBigDecimal("R66_DAY8_14"));
			obj.setR66_DAY15_30(rs.getBigDecimal("R66_DAY15_30"));
			obj.setR66_DAY31_TO_2M(rs.getBigDecimal("R66_DAY31_TO_2M"));
			obj.setR66_MORE2M_TO_3M(rs.getBigDecimal("R66_MORE2M_TO_3M"));
			obj.setR66_OVER3M_TO_6M(rs.getBigDecimal("R66_OVER3M_TO_6M"));
			obj.setR66_OVER6M_TO_1Y(rs.getBigDecimal("R66_OVER6M_TO_1Y"));
			obj.setR66_OVER1Y_TO_3Y(rs.getBigDecimal("R66_OVER1Y_TO_3Y"));
			obj.setR66_OVER3Y_TO_5Y(rs.getBigDecimal("R66_OVER3Y_TO_5Y"));
			obj.setR66_OVER5Y(rs.getBigDecimal("R66_OVER5Y"));
			obj.setR66_TOTAL(rs.getBigDecimal("R66_TOTAL"));
			obj.setR67_PRODUCT(rs.getString("R67_PRODUCT"));
			obj.setR67_DAY1(rs.getBigDecimal("R67_DAY1"));
			obj.setR67_DAY2_7(rs.getBigDecimal("R67_DAY2_7"));
			obj.setR67_DAY8_14(rs.getBigDecimal("R67_DAY8_14"));
			obj.setR67_DAY15_30(rs.getBigDecimal("R67_DAY15_30"));
			obj.setR67_DAY31_TO_2M(rs.getBigDecimal("R67_DAY31_TO_2M"));
			obj.setR67_MORE2M_TO_3M(rs.getBigDecimal("R67_MORE2M_TO_3M"));
			obj.setR67_OVER3M_TO_6M(rs.getBigDecimal("R67_OVER3M_TO_6M"));
			obj.setR67_OVER6M_TO_1Y(rs.getBigDecimal("R67_OVER6M_TO_1Y"));
			obj.setR67_OVER1Y_TO_3Y(rs.getBigDecimal("R67_OVER1Y_TO_3Y"));
			obj.setR67_OVER3Y_TO_5Y(rs.getBigDecimal("R67_OVER3Y_TO_5Y"));
			obj.setR67_OVER5Y(rs.getBigDecimal("R67_OVER5Y"));
			obj.setR67_TOTAL(rs.getBigDecimal("R67_TOTAL"));
			obj.setR68_PRODUCT(rs.getString("R68_PRODUCT"));
			obj.setR68_DAY1(rs.getBigDecimal("R68_DAY1"));
			obj.setR68_DAY2_7(rs.getBigDecimal("R68_DAY2_7"));
			obj.setR68_DAY8_14(rs.getBigDecimal("R68_DAY8_14"));
			obj.setR68_DAY15_30(rs.getBigDecimal("R68_DAY15_30"));
			obj.setR68_DAY31_TO_2M(rs.getBigDecimal("R68_DAY31_TO_2M"));
			obj.setR68_MORE2M_TO_3M(rs.getBigDecimal("R68_MORE2M_TO_3M"));
			obj.setR68_OVER3M_TO_6M(rs.getBigDecimal("R68_OVER3M_TO_6M"));
			obj.setR68_OVER6M_TO_1Y(rs.getBigDecimal("R68_OVER6M_TO_1Y"));
			obj.setR68_OVER1Y_TO_3Y(rs.getBigDecimal("R68_OVER1Y_TO_3Y"));
			obj.setR68_OVER3Y_TO_5Y(rs.getBigDecimal("R68_OVER3Y_TO_5Y"));
			obj.setR68_OVER5Y(rs.getBigDecimal("R68_OVER5Y"));
			obj.setR68_TOTAL(rs.getBigDecimal("R68_TOTAL"));
			obj.setR69_PRODUCT(rs.getString("R69_PRODUCT"));
			obj.setR69_DAY1(rs.getBigDecimal("R69_DAY1"));
			obj.setR69_DAY2_7(rs.getBigDecimal("R69_DAY2_7"));
			obj.setR69_DAY8_14(rs.getBigDecimal("R69_DAY8_14"));
			obj.setR69_DAY15_30(rs.getBigDecimal("R69_DAY15_30"));
			obj.setR69_DAY31_TO_2M(rs.getBigDecimal("R69_DAY31_TO_2M"));
			obj.setR69_MORE2M_TO_3M(rs.getBigDecimal("R69_MORE2M_TO_3M"));
			obj.setR69_OVER3M_TO_6M(rs.getBigDecimal("R69_OVER3M_TO_6M"));
			obj.setR69_OVER6M_TO_1Y(rs.getBigDecimal("R69_OVER6M_TO_1Y"));
			obj.setR69_OVER1Y_TO_3Y(rs.getBigDecimal("R69_OVER1Y_TO_3Y"));
			obj.setR69_OVER3Y_TO_5Y(rs.getBigDecimal("R69_OVER3Y_TO_5Y"));
			obj.setR69_OVER5Y(rs.getBigDecimal("R69_OVER5Y"));
			obj.setR69_TOTAL(rs.getBigDecimal("R69_TOTAL"));
			obj.setR70_PRODUCT(rs.getString("R70_PRODUCT"));
			obj.setR70_DAY1(rs.getBigDecimal("R70_DAY1"));
			obj.setR70_DAY2_7(rs.getBigDecimal("R70_DAY2_7"));
			obj.setR70_DAY8_14(rs.getBigDecimal("R70_DAY8_14"));
			obj.setR70_DAY15_30(rs.getBigDecimal("R70_DAY15_30"));
			obj.setR70_DAY31_TO_2M(rs.getBigDecimal("R70_DAY31_TO_2M"));
			obj.setR70_MORE2M_TO_3M(rs.getBigDecimal("R70_MORE2M_TO_3M"));
			obj.setR70_OVER3M_TO_6M(rs.getBigDecimal("R70_OVER3M_TO_6M"));
			obj.setR70_OVER6M_TO_1Y(rs.getBigDecimal("R70_OVER6M_TO_1Y"));
			obj.setR70_OVER1Y_TO_3Y(rs.getBigDecimal("R70_OVER1Y_TO_3Y"));
			obj.setR70_OVER3Y_TO_5Y(rs.getBigDecimal("R70_OVER3Y_TO_5Y"));
			obj.setR70_OVER5Y(rs.getBigDecimal("R70_OVER5Y"));
			obj.setR70_TOTAL(rs.getBigDecimal("R70_TOTAL"));
			obj.setR71_PRODUCT(rs.getString("R71_PRODUCT"));
			obj.setR71_DAY1(rs.getBigDecimal("R71_DAY1"));
			obj.setR71_DAY2_7(rs.getBigDecimal("R71_DAY2_7"));
			obj.setR71_DAY8_14(rs.getBigDecimal("R71_DAY8_14"));
			obj.setR71_DAY15_30(rs.getBigDecimal("R71_DAY15_30"));
			obj.setR71_DAY31_TO_2M(rs.getBigDecimal("R71_DAY31_TO_2M"));
			obj.setR71_MORE2M_TO_3M(rs.getBigDecimal("R71_MORE2M_TO_3M"));
			obj.setR71_OVER3M_TO_6M(rs.getBigDecimal("R71_OVER3M_TO_6M"));
			obj.setR71_OVER6M_TO_1Y(rs.getBigDecimal("R71_OVER6M_TO_1Y"));
			obj.setR71_OVER1Y_TO_3Y(rs.getBigDecimal("R71_OVER1Y_TO_3Y"));
			obj.setR71_OVER3Y_TO_5Y(rs.getBigDecimal("R71_OVER3Y_TO_5Y"));
			obj.setR71_OVER5Y(rs.getBigDecimal("R71_OVER5Y"));
			obj.setR71_TOTAL(rs.getBigDecimal("R71_TOTAL"));
			obj.setR72_PRODUCT(rs.getString("R72_PRODUCT"));
			obj.setR72_DAY1(rs.getBigDecimal("R72_DAY1"));
			obj.setR72_DAY2_7(rs.getBigDecimal("R72_DAY2_7"));
			obj.setR72_DAY8_14(rs.getBigDecimal("R72_DAY8_14"));
			obj.setR72_DAY15_30(rs.getBigDecimal("R72_DAY15_30"));
			obj.setR72_DAY31_TO_2M(rs.getBigDecimal("R72_DAY31_TO_2M"));
			obj.setR72_MORE2M_TO_3M(rs.getBigDecimal("R72_MORE2M_TO_3M"));
			obj.setR72_OVER3M_TO_6M(rs.getBigDecimal("R72_OVER3M_TO_6M"));
			obj.setR72_OVER6M_TO_1Y(rs.getBigDecimal("R72_OVER6M_TO_1Y"));
			obj.setR72_OVER1Y_TO_3Y(rs.getBigDecimal("R72_OVER1Y_TO_3Y"));
			obj.setR72_OVER3Y_TO_5Y(rs.getBigDecimal("R72_OVER3Y_TO_5Y"));
			obj.setR72_OVER5Y(rs.getBigDecimal("R72_OVER5Y"));
			obj.setR72_TOTAL(rs.getBigDecimal("R72_TOTAL"));
			obj.setR73_PRODUCT(rs.getString("R73_PRODUCT"));
			obj.setR73_DAY1(rs.getBigDecimal("R73_DAY1"));
			obj.setR73_DAY2_7(rs.getBigDecimal("R73_DAY2_7"));
			obj.setR73_DAY8_14(rs.getBigDecimal("R73_DAY8_14"));
			obj.setR73_DAY15_30(rs.getBigDecimal("R73_DAY15_30"));
			obj.setR73_DAY31_TO_2M(rs.getBigDecimal("R73_DAY31_TO_2M"));
			obj.setR73_MORE2M_TO_3M(rs.getBigDecimal("R73_MORE2M_TO_3M"));
			obj.setR73_OVER3M_TO_6M(rs.getBigDecimal("R73_OVER3M_TO_6M"));
			obj.setR73_OVER6M_TO_1Y(rs.getBigDecimal("R73_OVER6M_TO_1Y"));
			obj.setR73_OVER1Y_TO_3Y(rs.getBigDecimal("R73_OVER1Y_TO_3Y"));
			obj.setR73_OVER3Y_TO_5Y(rs.getBigDecimal("R73_OVER3Y_TO_5Y"));
			obj.setR73_OVER5Y(rs.getBigDecimal("R73_OVER5Y"));
			obj.setR73_TOTAL(rs.getBigDecimal("R73_TOTAL"));
			obj.setR74_PRODUCT(rs.getString("R74_PRODUCT"));
			obj.setR74_DAY1(rs.getBigDecimal("R74_DAY1"));
			obj.setR74_DAY2_7(rs.getBigDecimal("R74_DAY2_7"));
			obj.setR74_DAY8_14(rs.getBigDecimal("R74_DAY8_14"));
			obj.setR74_DAY15_30(rs.getBigDecimal("R74_DAY15_30"));
			obj.setR74_DAY31_TO_2M(rs.getBigDecimal("R74_DAY31_TO_2M"));
			obj.setR74_MORE2M_TO_3M(rs.getBigDecimal("R74_MORE2M_TO_3M"));
			obj.setR74_OVER3M_TO_6M(rs.getBigDecimal("R74_OVER3M_TO_6M"));
			obj.setR74_OVER6M_TO_1Y(rs.getBigDecimal("R74_OVER6M_TO_1Y"));
			obj.setR74_OVER1Y_TO_3Y(rs.getBigDecimal("R74_OVER1Y_TO_3Y"));
			obj.setR74_OVER3Y_TO_5Y(rs.getBigDecimal("R74_OVER3Y_TO_5Y"));
			obj.setR74_OVER5Y(rs.getBigDecimal("R74_OVER5Y"));
			obj.setR74_TOTAL(rs.getBigDecimal("R74_TOTAL"));
			obj.setR75_PRODUCT(rs.getString("R75_PRODUCT"));
			obj.setR75_DAY1(rs.getBigDecimal("R75_DAY1"));
			obj.setR75_DAY2_7(rs.getBigDecimal("R75_DAY2_7"));
			obj.setR75_DAY8_14(rs.getBigDecimal("R75_DAY8_14"));
			obj.setR75_DAY15_30(rs.getBigDecimal("R75_DAY15_30"));
			obj.setR75_DAY31_TO_2M(rs.getBigDecimal("R75_DAY31_TO_2M"));
			obj.setR75_MORE2M_TO_3M(rs.getBigDecimal("R75_MORE2M_TO_3M"));
			obj.setR75_OVER3M_TO_6M(rs.getBigDecimal("R75_OVER3M_TO_6M"));
			obj.setR75_OVER6M_TO_1Y(rs.getBigDecimal("R75_OVER6M_TO_1Y"));
			obj.setR75_OVER1Y_TO_3Y(rs.getBigDecimal("R75_OVER1Y_TO_3Y"));
			obj.setR75_OVER3Y_TO_5Y(rs.getBigDecimal("R75_OVER3Y_TO_5Y"));
			obj.setR75_OVER5Y(rs.getBigDecimal("R75_OVER5Y"));
			obj.setR75_TOTAL(rs.getBigDecimal("R75_TOTAL"));
			obj.setR76_PRODUCT(rs.getString("R76_PRODUCT"));
			obj.setR76_DAY1(rs.getBigDecimal("R76_DAY1"));
			obj.setR76_DAY2_7(rs.getBigDecimal("R76_DAY2_7"));
			obj.setR76_DAY8_14(rs.getBigDecimal("R76_DAY8_14"));
			obj.setR76_DAY15_30(rs.getBigDecimal("R76_DAY15_30"));
			obj.setR76_DAY31_TO_2M(rs.getBigDecimal("R76_DAY31_TO_2M"));
			obj.setR76_MORE2M_TO_3M(rs.getBigDecimal("R76_MORE2M_TO_3M"));
			obj.setR76_OVER3M_TO_6M(rs.getBigDecimal("R76_OVER3M_TO_6M"));
			obj.setR76_OVER6M_TO_1Y(rs.getBigDecimal("R76_OVER6M_TO_1Y"));
			obj.setR76_OVER1Y_TO_3Y(rs.getBigDecimal("R76_OVER1Y_TO_3Y"));
			obj.setR76_OVER3Y_TO_5Y(rs.getBigDecimal("R76_OVER3Y_TO_5Y"));
			obj.setR76_OVER5Y(rs.getBigDecimal("R76_OVER5Y"));
			obj.setR76_TOTAL(rs.getBigDecimal("R76_TOTAL"));
//			obj.setR77_PRODUCT(rs.getString("R77_PRODUCT"));
//			obj.setR77_DAY1(rs.getBigDecimal("R77_DAY1"));
//			obj.setR77_DAY2_7(rs.getBigDecimal("R77_DAY2_7"));
//			obj.setR77_DAY8_14(rs.getBigDecimal("R77_DAY8_14"));
//			obj.setR77_DAY15_30(rs.getBigDecimal("R77_DAY15_30"));
//			obj.setR77_DAY31_TO_2M(rs.getBigDecimal("R77_DAY31_TO_2M"));
//			obj.setR77_MORE2M_TO_3M(rs.getBigDecimal("R77_MORE2M_TO_3M"));
//			obj.setR77_OVER3M_TO_6M(rs.getBigDecimal("R77_OVER3M_TO_6M"));
//			obj.setR77_OVER6M_TO_1Y(rs.getBigDecimal("R77_OVER6M_TO_1Y"));
//			obj.setR77_OVER1Y_TO_3Y(rs.getBigDecimal("R77_OVER1Y_TO_3Y"));
//			obj.setR77_OVER3Y_TO_5Y(rs.getBigDecimal("R77_OVER3Y_TO_5Y"));
//			obj.setR77_OVER5Y(rs.getBigDecimal("R77_OVER5Y"));
//			obj.setR77_TOTAL(rs.getBigDecimal("R77_TOTAL"));
//			obj.setR78_PRODUCT(rs.getString("R78_PRODUCT"));
//			obj.setR78_DAY1(rs.getBigDecimal("R78_DAY1"));
//			obj.setR78_DAY2_7(rs.getBigDecimal("R78_DAY2_7"));
//			obj.setR78_DAY8_14(rs.getBigDecimal("R78_DAY8_14"));
//			obj.setR78_DAY15_30(rs.getBigDecimal("R78_DAY15_30"));
//			obj.setR78_DAY31_TO_2M(rs.getBigDecimal("R78_DAY31_TO_2M"));
//			obj.setR78_MORE2M_TO_3M(rs.getBigDecimal("R78_MORE2M_TO_3M"));
//			obj.setR78_OVER3M_TO_6M(rs.getBigDecimal("R78_OVER3M_TO_6M"));
//			obj.setR78_OVER6M_TO_1Y(rs.getBigDecimal("R78_OVER6M_TO_1Y"));
//			obj.setR78_OVER1Y_TO_3Y(rs.getBigDecimal("R78_OVER1Y_TO_3Y"));
//			obj.setR78_OVER3Y_TO_5Y(rs.getBigDecimal("R78_OVER3Y_TO_5Y"));
//			obj.setR78_OVER5Y(rs.getBigDecimal("R78_OVER5Y"));
//			obj.setR78_TOTAL(rs.getBigDecimal("R78_TOTAL"));
//			obj.setR79_PRODUCT(rs.getString("R79_PRODUCT"));
//			obj.setR79_DAY1(rs.getBigDecimal("R79_DAY1"));
//			obj.setR79_DAY2_7(rs.getBigDecimal("R79_DAY2_7"));
//			obj.setR79_DAY8_14(rs.getBigDecimal("R79_DAY8_14"));
//			obj.setR79_DAY15_30(rs.getBigDecimal("R79_DAY15_30"));
//			obj.setR79_DAY31_TO_2M(rs.getBigDecimal("R79_DAY31_TO_2M"));
//			obj.setR79_MORE2M_TO_3M(rs.getBigDecimal("R79_MORE2M_TO_3M"));
//			obj.setR79_OVER3M_TO_6M(rs.getBigDecimal("R79_OVER3M_TO_6M"));
//			obj.setR79_OVER6M_TO_1Y(rs.getBigDecimal("R79_OVER6M_TO_1Y"));
//			obj.setR79_OVER1Y_TO_3Y(rs.getBigDecimal("R79_OVER1Y_TO_3Y"));
//			obj.setR79_OVER3Y_TO_5Y(rs.getBigDecimal("R79_OVER3Y_TO_5Y"));
//			obj.setR79_OVER5Y(rs.getBigDecimal("R79_OVER5Y"));
//			obj.setR79_TOTAL(rs.getBigDecimal("R79_TOTAL"));
//			obj.setR80_PRODUCT(rs.getString("R80_PRODUCT"));
//			obj.setR80_DAY1(rs.getBigDecimal("R80_DAY1"));
//			obj.setR80_DAY2_7(rs.getBigDecimal("R80_DAY2_7"));
//			obj.setR80_DAY8_14(rs.getBigDecimal("R80_DAY8_14"));
//			obj.setR80_DAY15_30(rs.getBigDecimal("R80_DAY15_30"));
//			obj.setR80_DAY31_TO_2M(rs.getBigDecimal("R80_DAY31_TO_2M"));
//			obj.setR80_MORE2M_TO_3M(rs.getBigDecimal("R80_MORE2M_TO_3M"));
//			obj.setR80_OVER3M_TO_6M(rs.getBigDecimal("R80_OVER3M_TO_6M"));
//			obj.setR80_OVER6M_TO_1Y(rs.getBigDecimal("R80_OVER6M_TO_1Y"));
//			obj.setR80_OVER1Y_TO_3Y(rs.getBigDecimal("R80_OVER1Y_TO_3Y"));
//			obj.setR80_OVER3Y_TO_5Y(rs.getBigDecimal("R80_OVER3Y_TO_5Y"));
//			obj.setR80_OVER5Y(rs.getBigDecimal("R80_OVER5Y"));
//			obj.setR80_TOTAL(rs.getBigDecimal("R80_TOTAL"));
//			obj.setR81_PRODUCT(rs.getString("R81_PRODUCT"));
//			obj.setR81_DAY1(rs.getBigDecimal("R81_DAY1"));
//			obj.setR81_DAY2_7(rs.getBigDecimal("R81_DAY2_7"));
//			obj.setR81_DAY8_14(rs.getBigDecimal("R81_DAY8_14"));
//			obj.setR81_DAY15_30(rs.getBigDecimal("R81_DAY15_30"));
//			obj.setR81_DAY31_TO_2M(rs.getBigDecimal("R81_DAY31_TO_2M"));
//			obj.setR81_MORE2M_TO_3M(rs.getBigDecimal("R81_MORE2M_TO_3M"));
//			obj.setR81_OVER3M_TO_6M(rs.getBigDecimal("R81_OVER3M_TO_6M"));
//			obj.setR81_OVER6M_TO_1Y(rs.getBigDecimal("R81_OVER6M_TO_1Y"));
//			obj.setR81_OVER1Y_TO_3Y(rs.getBigDecimal("R81_OVER1Y_TO_3Y"));
//			obj.setR81_OVER3Y_TO_5Y(rs.getBigDecimal("R81_OVER3Y_TO_5Y"));
//			obj.setR81_OVER5Y(rs.getBigDecimal("R81_OVER5Y"));
//			obj.setR81_TOTAL(rs.getBigDecimal("R81_TOTAL"));
//			obj.setR82_PRODUCT(rs.getString("R82_PRODUCT"));
//			obj.setR82_DAY1(rs.getBigDecimal("R82_DAY1"));
//			obj.setR82_DAY2_7(rs.getBigDecimal("R82_DAY2_7"));
//			obj.setR82_DAY8_14(rs.getBigDecimal("R82_DAY8_14"));
//			obj.setR82_DAY15_30(rs.getBigDecimal("R82_DAY15_30"));
//			obj.setR82_DAY31_TO_2M(rs.getBigDecimal("R82_DAY31_TO_2M"));
//			obj.setR82_MORE2M_TO_3M(rs.getBigDecimal("R82_MORE2M_TO_3M"));
//			obj.setR82_OVER3M_TO_6M(rs.getBigDecimal("R82_OVER3M_TO_6M"));
//			obj.setR82_OVER6M_TO_1Y(rs.getBigDecimal("R82_OVER6M_TO_1Y"));
//			obj.setR82_OVER1Y_TO_3Y(rs.getBigDecimal("R82_OVER1Y_TO_3Y"));
//			obj.setR82_OVER3Y_TO_5Y(rs.getBigDecimal("R82_OVER3Y_TO_5Y"));
//			obj.setR82_OVER5Y(rs.getBigDecimal("R82_OVER5Y"));
//			obj.setR82_TOTAL(rs.getBigDecimal("R82_TOTAL"));
//			obj.setR83_PRODUCT(rs.getString("R83_PRODUCT"));
//			obj.setR83_DAY1(rs.getBigDecimal("R83_DAY1"));
//			obj.setR83_DAY2_7(rs.getBigDecimal("R83_DAY2_7"));
//			obj.setR83_DAY8_14(rs.getBigDecimal("R83_DAY8_14"));
//			obj.setR83_DAY15_30(rs.getBigDecimal("R83_DAY15_30"));
//			obj.setR83_DAY31_TO_2M(rs.getBigDecimal("R83_DAY31_TO_2M"));
//			obj.setR83_MORE2M_TO_3M(rs.getBigDecimal("R83_MORE2M_TO_3M"));
//			obj.setR83_OVER3M_TO_6M(rs.getBigDecimal("R83_OVER3M_TO_6M"));
//			obj.setR83_OVER6M_TO_1Y(rs.getBigDecimal("R83_OVER6M_TO_1Y"));
//			obj.setR83_OVER1Y_TO_3Y(rs.getBigDecimal("R83_OVER1Y_TO_3Y"));
//			obj.setR83_OVER3Y_TO_5Y(rs.getBigDecimal("R83_OVER3Y_TO_5Y"));
//			obj.setR83_OVER5Y(rs.getBigDecimal("R83_OVER5Y"));
//			obj.setR83_TOTAL(rs.getBigDecimal("R83_TOTAL"));
			obj.setR84_PRODUCT(rs.getString("R84_PRODUCT"));
			obj.setR84_DAY1(rs.getBigDecimal("R84_DAY1"));
			obj.setR84_DAY2_7(rs.getBigDecimal("R84_DAY2_7"));
			obj.setR84_DAY8_14(rs.getBigDecimal("R84_DAY8_14"));
			obj.setR84_DAY15_30(rs.getBigDecimal("R84_DAY15_30"));
			obj.setR84_DAY31_TO_2M(rs.getBigDecimal("R84_DAY31_TO_2M"));
			obj.setR84_MORE2M_TO_3M(rs.getBigDecimal("R84_MORE2M_TO_3M"));
			obj.setR84_OVER3M_TO_6M(rs.getBigDecimal("R84_OVER3M_TO_6M"));
			obj.setR84_OVER6M_TO_1Y(rs.getBigDecimal("R84_OVER6M_TO_1Y"));
			obj.setR84_OVER1Y_TO_3Y(rs.getBigDecimal("R84_OVER1Y_TO_3Y"));
			obj.setR84_OVER3Y_TO_5Y(rs.getBigDecimal("R84_OVER3Y_TO_5Y"));
			obj.setR84_OVER5Y(rs.getBigDecimal("R84_OVER5Y"));
			obj.setR84_TOTAL(rs.getBigDecimal("R84_TOTAL"));
			obj.setR85_PRODUCT(rs.getString("R85_PRODUCT"));
			obj.setR85_DAY1(rs.getBigDecimal("R85_DAY1"));
			obj.setR85_DAY2_7(rs.getBigDecimal("R85_DAY2_7"));
			obj.setR85_DAY8_14(rs.getBigDecimal("R85_DAY8_14"));
			obj.setR85_DAY15_30(rs.getBigDecimal("R85_DAY15_30"));
			obj.setR85_DAY31_TO_2M(rs.getBigDecimal("R85_DAY31_TO_2M"));
			obj.setR85_MORE2M_TO_3M(rs.getBigDecimal("R85_MORE2M_TO_3M"));
			obj.setR85_OVER3M_TO_6M(rs.getBigDecimal("R85_OVER3M_TO_6M"));
			obj.setR85_OVER6M_TO_1Y(rs.getBigDecimal("R85_OVER6M_TO_1Y"));
			obj.setR85_OVER1Y_TO_3Y(rs.getBigDecimal("R85_OVER1Y_TO_3Y"));
			obj.setR85_OVER3Y_TO_5Y(rs.getBigDecimal("R85_OVER3Y_TO_5Y"));
			obj.setR85_OVER5Y(rs.getBigDecimal("R85_OVER5Y"));
			obj.setR85_TOTAL(rs.getBigDecimal("R85_TOTAL"));
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getString("REPORT_VERSION"));
			obj.setREPORT_CURRENCY(rs.getString("REPORT_CURRENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// ------------------------------
	// Row Mapper for SLS_INPUT_SHT_Archival_Detail_Entity
	// ------------------------------
	class SLSArchivalDetailRowMapper implements RowMapper<SLS_INPUT_SHT_Archival_Detail_Entity> {
		@Override
		public SLS_INPUT_SHT_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			SLS_INPUT_SHT_Archival_Detail_Entity obj = new SLS_INPUT_SHT_Archival_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setRowId(rs.getString("ROW_ID"));
			obj.setColumnId(rs.getString("COLUMN_ID"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInPula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setProvision(rs.getBigDecimal("PROVISION"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			return obj;
		}
	}
	

//// SLS_INPUT_SHT_Summary_Entity

	// ==============================
	// Entity classes
	// ==============================

	// ------------------------------
	// Entity class for SLS_INPUT_SHT Summary Report
	// ------------------------------
	public static class SLS_INPUT_SHT_Summary_Entity {
	@Id
	private String SRL_NO;
	private String R11_PRODUCT;
	private BigDecimal R11_DAY1;
	private BigDecimal R11_DAY2_7;
	private BigDecimal R11_DAY8_14;
	private BigDecimal R11_DAY15_30;
	private BigDecimal R11_DAY31_TO_2M;
	private BigDecimal R11_MORE2M_TO_3M;
	private BigDecimal R11_OVER3M_TO_6M;
	private BigDecimal R11_OVER6M_TO_1Y;
	private BigDecimal R11_OVER1Y_TO_3Y;
	private BigDecimal R11_OVER3Y_TO_5Y;
	private BigDecimal R11_OVER5Y;
	private BigDecimal R11_TOTAL;
	private String R12_PRODUCT;
	private BigDecimal R12_DAY1;
	private BigDecimal R12_DAY2_7;
	private BigDecimal R12_DAY8_14;
	private BigDecimal R12_DAY15_30;
	private BigDecimal R12_DAY31_TO_2M;
	private BigDecimal R12_MORE2M_TO_3M;
	private BigDecimal R12_OVER3M_TO_6M;
	private BigDecimal R12_OVER6M_TO_1Y;
	private BigDecimal R12_OVER1Y_TO_3Y;
	private BigDecimal R12_OVER3Y_TO_5Y;
	private BigDecimal R12_OVER5Y;
	private BigDecimal R12_TOTAL;
	private String R13_PRODUCT;
	private BigDecimal R13_DAY1;
	private BigDecimal R13_DAY2_7;
	private BigDecimal R13_DAY8_14;
	private BigDecimal R13_DAY15_30;
	private BigDecimal R13_DAY31_TO_2M;
	private BigDecimal R13_MORE2M_TO_3M;
	private BigDecimal R13_OVER3M_TO_6M;
	private BigDecimal R13_OVER6M_TO_1Y;
	private BigDecimal R13_OVER1Y_TO_3Y;
	private BigDecimal R13_OVER3Y_TO_5Y;
	private BigDecimal R13_OVER5Y;
	private BigDecimal R13_TOTAL;
	private String R14_PRODUCT;
	private BigDecimal R14_DAY1;
	private BigDecimal R14_DAY2_7;
	private BigDecimal R14_DAY8_14;
	private BigDecimal R14_DAY15_30;
	private BigDecimal R14_DAY31_TO_2M;
	private BigDecimal R14_MORE2M_TO_3M;
	private BigDecimal R14_OVER3M_TO_6M;
	private BigDecimal R14_OVER6M_TO_1Y;
	private BigDecimal R14_OVER1Y_TO_3Y;
	private BigDecimal R14_OVER3Y_TO_5Y;
	private BigDecimal R14_OVER5Y;
	private BigDecimal R14_TOTAL;
	private String R15_PRODUCT;
	private BigDecimal R15_DAY1;
	private BigDecimal R15_DAY2_7;
	private BigDecimal R15_DAY8_14;
	private BigDecimal R15_DAY15_30;
	private BigDecimal R15_DAY31_TO_2M;
	private BigDecimal R15_MORE2M_TO_3M;
	private BigDecimal R15_OVER3M_TO_6M;
	private BigDecimal R15_OVER6M_TO_1Y;
	private BigDecimal R15_OVER1Y_TO_3Y;
	private BigDecimal R15_OVER3Y_TO_5Y;
	private BigDecimal R15_OVER5Y;
	private BigDecimal R15_TOTAL;
	private String R16_PRODUCT;
	private BigDecimal R16_DAY1;
	private BigDecimal R16_DAY2_7;
	private BigDecimal R16_DAY8_14;
	private BigDecimal R16_DAY15_30;
	private BigDecimal R16_DAY31_TO_2M;
	private BigDecimal R16_MORE2M_TO_3M;
	private BigDecimal R16_OVER3M_TO_6M;
	private BigDecimal R16_OVER6M_TO_1Y;
	private BigDecimal R16_OVER1Y_TO_3Y;
	private BigDecimal R16_OVER3Y_TO_5Y;
	private BigDecimal R16_OVER5Y;
	private BigDecimal R16_TOTAL;
	private String R17_PRODUCT;
	private BigDecimal R17_DAY1;
	private BigDecimal R17_DAY2_7;
	private BigDecimal R17_DAY8_14;
	private BigDecimal R17_DAY15_30;
	private BigDecimal R17_DAY31_TO_2M;
	private BigDecimal R17_MORE2M_TO_3M;
	private BigDecimal R17_OVER3M_TO_6M;
	private BigDecimal R17_OVER6M_TO_1Y;
	private BigDecimal R17_OVER1Y_TO_3Y;
	private BigDecimal R17_OVER3Y_TO_5Y;
	private BigDecimal R17_OVER5Y;
	private BigDecimal R17_TOTAL;
	private String R18_PRODUCT;
	private BigDecimal R18_DAY1;
	private BigDecimal R18_DAY2_7;
	private BigDecimal R18_DAY8_14;
	private BigDecimal R18_DAY15_30;
	private BigDecimal R18_DAY31_TO_2M;
	private BigDecimal R18_MORE2M_TO_3M;
	private BigDecimal R18_OVER3M_TO_6M;
	private BigDecimal R18_OVER6M_TO_1Y;
	private BigDecimal R18_OVER1Y_TO_3Y;
	private BigDecimal R18_OVER3Y_TO_5Y;
	private BigDecimal R18_OVER5Y;
	private BigDecimal R18_TOTAL;
	private String R19_PRODUCT;
	private BigDecimal R19_DAY1;
	private BigDecimal R19_DAY2_7;
	private BigDecimal R19_DAY8_14;
	private BigDecimal R19_DAY15_30;
	private BigDecimal R19_DAY31_TO_2M;
	private BigDecimal R19_MORE2M_TO_3M;
	private BigDecimal R19_OVER3M_TO_6M;
	private BigDecimal R19_OVER6M_TO_1Y;
	private BigDecimal R19_OVER1Y_TO_3Y;
	private BigDecimal R19_OVER3Y_TO_5Y;
	private BigDecimal R19_OVER5Y;
	private BigDecimal R19_TOTAL;
	private String R20_PRODUCT;
	private BigDecimal R20_DAY1;
	private BigDecimal R20_DAY2_7;
	private BigDecimal R20_DAY8_14;
	private BigDecimal R20_DAY15_30;
	private BigDecimal R20_DAY31_TO_2M;
	private BigDecimal R20_MORE2M_TO_3M;
	private BigDecimal R20_OVER3M_TO_6M;
	private BigDecimal R20_OVER6M_TO_1Y;
	private BigDecimal R20_OVER1Y_TO_3Y;
	private BigDecimal R20_OVER3Y_TO_5Y;
	private BigDecimal R20_OVER5Y;
	private BigDecimal R20_TOTAL;
	private String R21_PRODUCT;
	private BigDecimal R21_DAY1;
	private BigDecimal R21_DAY2_7;
	private BigDecimal R21_DAY8_14;
	private BigDecimal R21_DAY15_30;
	private BigDecimal R21_DAY31_TO_2M;
	private BigDecimal R21_MORE2M_TO_3M;
	private BigDecimal R21_OVER3M_TO_6M;
	private BigDecimal R21_OVER6M_TO_1Y;
	private BigDecimal R21_OVER1Y_TO_3Y;
	private BigDecimal R21_OVER3Y_TO_5Y;
	private BigDecimal R21_OVER5Y;
	private BigDecimal R21_TOTAL;
	private String R22_PRODUCT;
	private BigDecimal R22_DAY1;
	private BigDecimal R22_DAY2_7;
	private BigDecimal R22_DAY8_14;
	private BigDecimal R22_DAY15_30;
	private BigDecimal R22_DAY31_TO_2M;
	private BigDecimal R22_MORE2M_TO_3M;
	private BigDecimal R22_OVER3M_TO_6M;
	private BigDecimal R22_OVER6M_TO_1Y;
	private BigDecimal R22_OVER1Y_TO_3Y;
	private BigDecimal R22_OVER3Y_TO_5Y;
	private BigDecimal R22_OVER5Y;
	private BigDecimal R22_TOTAL;
	private String R23_PRODUCT;
	private BigDecimal R23_DAY1;
	private BigDecimal R23_DAY2_7;
	private BigDecimal R23_DAY8_14;
	private BigDecimal R23_DAY15_30;
	private BigDecimal R23_DAY31_TO_2M;
	private BigDecimal R23_MORE2M_TO_3M;
	private BigDecimal R23_OVER3M_TO_6M;
	private BigDecimal R23_OVER6M_TO_1Y;
	private BigDecimal R23_OVER1Y_TO_3Y;
	private BigDecimal R23_OVER3Y_TO_5Y;
	private BigDecimal R23_OVER5Y;
	private BigDecimal R23_TOTAL;
	private String R24_PRODUCT;
	private BigDecimal R24_DAY1;
	private BigDecimal R24_DAY2_7;
	private BigDecimal R24_DAY8_14;
	private BigDecimal R24_DAY15_30;
	private BigDecimal R24_DAY31_TO_2M;
	private BigDecimal R24_MORE2M_TO_3M;
	private BigDecimal R24_OVER3M_TO_6M;
	private BigDecimal R24_OVER6M_TO_1Y;
	private BigDecimal R24_OVER1Y_TO_3Y;
	private BigDecimal R24_OVER3Y_TO_5Y;
	private BigDecimal R24_OVER5Y;
	private BigDecimal R24_TOTAL;
	private String R25_PRODUCT;
	private BigDecimal R25_DAY1;
	private BigDecimal R25_DAY2_7;
	private BigDecimal R25_DAY8_14;
	private BigDecimal R25_DAY15_30;
	private BigDecimal R25_DAY31_TO_2M;
	private BigDecimal R25_MORE2M_TO_3M;
	private BigDecimal R25_OVER3M_TO_6M;
	private BigDecimal R25_OVER6M_TO_1Y;
	private BigDecimal R25_OVER1Y_TO_3Y;
	private BigDecimal R25_OVER3Y_TO_5Y;
	private BigDecimal R25_OVER5Y;
	private BigDecimal R25_TOTAL;
	private String R26_PRODUCT;
	private BigDecimal R26_DAY1;
	private BigDecimal R26_DAY2_7;
	private BigDecimal R26_DAY8_14;
	private BigDecimal R26_DAY15_30;
	private BigDecimal R26_DAY31_TO_2M;
	private BigDecimal R26_MORE2M_TO_3M;
	private BigDecimal R26_OVER3M_TO_6M;
	private BigDecimal R26_OVER6M_TO_1Y;
	private BigDecimal R26_OVER1Y_TO_3Y;
	private BigDecimal R26_OVER3Y_TO_5Y;
	private BigDecimal R26_OVER5Y;
	private BigDecimal R26_TOTAL;
	private String R27_PRODUCT;
	private BigDecimal R27_DAY1;
	private BigDecimal R27_DAY2_7;
	private BigDecimal R27_DAY8_14;
	private BigDecimal R27_DAY15_30;
	private BigDecimal R27_DAY31_TO_2M;
	private BigDecimal R27_MORE2M_TO_3M;
	private BigDecimal R27_OVER3M_TO_6M;
	private BigDecimal R27_OVER6M_TO_1Y;
	private BigDecimal R27_OVER1Y_TO_3Y;
	private BigDecimal R27_OVER3Y_TO_5Y;
	private BigDecimal R27_OVER5Y;
	private BigDecimal R27_TOTAL;
	private String R28_PRODUCT;
	private BigDecimal R28_DAY1;
	private BigDecimal R28_DAY2_7;
	private BigDecimal R28_DAY8_14;
	private BigDecimal R28_DAY15_30;
	private BigDecimal R28_DAY31_TO_2M;
	private BigDecimal R28_MORE2M_TO_3M;
	private BigDecimal R28_OVER3M_TO_6M;
	private BigDecimal R28_OVER6M_TO_1Y;
	private BigDecimal R28_OVER1Y_TO_3Y;
	private BigDecimal R28_OVER3Y_TO_5Y;
	private BigDecimal R28_OVER5Y;
	private BigDecimal R28_TOTAL;
	private String R29_PRODUCT;
	private BigDecimal R29_DAY1;
	private BigDecimal R29_DAY2_7;
	private BigDecimal R29_DAY8_14;
	private BigDecimal R29_DAY15_30;
	private BigDecimal R29_DAY31_TO_2M;
	private BigDecimal R29_MORE2M_TO_3M;
	private BigDecimal R29_OVER3M_TO_6M;
	private BigDecimal R29_OVER6M_TO_1Y;
	private BigDecimal R29_OVER1Y_TO_3Y;
	private BigDecimal R29_OVER3Y_TO_5Y;
	private BigDecimal R29_OVER5Y;
	private BigDecimal R29_TOTAL;
	private String R30_PRODUCT;
	private BigDecimal R30_DAY1;
	private BigDecimal R30_DAY2_7;
	private BigDecimal R30_DAY8_14;
	private BigDecimal R30_DAY15_30;
	private BigDecimal R30_DAY31_TO_2M;
	private BigDecimal R30_MORE2M_TO_3M;
	private BigDecimal R30_OVER3M_TO_6M;
	private BigDecimal R30_OVER6M_TO_1Y;
	private BigDecimal R30_OVER1Y_TO_3Y;
	private BigDecimal R30_OVER3Y_TO_5Y;
	private BigDecimal R30_OVER5Y;
	private BigDecimal R30_TOTAL;
	private String R31_PRODUCT;
	private BigDecimal R31_DAY1;
	private BigDecimal R31_DAY2_7;
	private BigDecimal R31_DAY8_14;
	private BigDecimal R31_DAY15_30;
	private BigDecimal R31_DAY31_TO_2M;
	private BigDecimal R31_MORE2M_TO_3M;
	private BigDecimal R31_OVER3M_TO_6M;
	private BigDecimal R31_OVER6M_TO_1Y;
	private BigDecimal R31_OVER1Y_TO_3Y;
	private BigDecimal R31_OVER3Y_TO_5Y;
	private BigDecimal R31_OVER5Y;
	private BigDecimal R31_TOTAL;
	private String R32_PRODUCT;
	private BigDecimal R32_DAY1;
	private BigDecimal R32_DAY2_7;
	private BigDecimal R32_DAY8_14;
	private BigDecimal R32_DAY15_30;
	private BigDecimal R32_DAY31_TO_2M;
	private BigDecimal R32_MORE2M_TO_3M;
	private BigDecimal R32_OVER3M_TO_6M;
	private BigDecimal R32_OVER6M_TO_1Y;
	private BigDecimal R32_OVER1Y_TO_3Y;
	private BigDecimal R32_OVER3Y_TO_5Y;
	private BigDecimal R32_OVER5Y;
	private BigDecimal R32_TOTAL;
	private String R33_PRODUCT;
	private BigDecimal R33_DAY1;
	private BigDecimal R33_DAY2_7;
	private BigDecimal R33_DAY8_14;
	private BigDecimal R33_DAY15_30;
	private BigDecimal R33_DAY31_TO_2M;
	private BigDecimal R33_MORE2M_TO_3M;
	private BigDecimal R33_OVER3M_TO_6M;
	private BigDecimal R33_OVER6M_TO_1Y;
	private BigDecimal R33_OVER1Y_TO_3Y;
	private BigDecimal R33_OVER3Y_TO_5Y;
	private BigDecimal R33_OVER5Y;
	private BigDecimal R33_TOTAL;
	private String R34_PRODUCT;
	private BigDecimal R34_DAY1;
	private BigDecimal R34_DAY2_7;
	private BigDecimal R34_DAY8_14;
	private BigDecimal R34_DAY15_30;
	private BigDecimal R34_DAY31_TO_2M;
	private BigDecimal R34_MORE2M_TO_3M;
	private BigDecimal R34_OVER3M_TO_6M;
	private BigDecimal R34_OVER6M_TO_1Y;
	private BigDecimal R34_OVER1Y_TO_3Y;
	private BigDecimal R34_OVER3Y_TO_5Y;
	private BigDecimal R34_OVER5Y;
	private BigDecimal R34_TOTAL;
	private String R35_PRODUCT;
	private BigDecimal R35_DAY1;
	private BigDecimal R35_DAY2_7;
	private BigDecimal R35_DAY8_14;
	private BigDecimal R35_DAY15_30;
	private BigDecimal R35_DAY31_TO_2M;
	private BigDecimal R35_MORE2M_TO_3M;
	private BigDecimal R35_OVER3M_TO_6M;
	private BigDecimal R35_OVER6M_TO_1Y;
	private BigDecimal R35_OVER1Y_TO_3Y;
	private BigDecimal R35_OVER3Y_TO_5Y;
	private BigDecimal R35_OVER5Y;
	private BigDecimal R35_TOTAL;
	private String R36_PRODUCT;
	private BigDecimal R36_DAY1;
	private BigDecimal R36_DAY2_7;
	private BigDecimal R36_DAY8_14;
	private BigDecimal R36_DAY15_30;
	private BigDecimal R36_DAY31_TO_2M;
	private BigDecimal R36_MORE2M_TO_3M;
	private BigDecimal R36_OVER3M_TO_6M;
	private BigDecimal R36_OVER6M_TO_1Y;
	private BigDecimal R36_OVER1Y_TO_3Y;
	private BigDecimal R36_OVER3Y_TO_5Y;
	private BigDecimal R36_OVER5Y;
	private BigDecimal R36_TOTAL;
	private String R37_PRODUCT;
	private BigDecimal R37_DAY1;
	private BigDecimal R37_DAY2_7;
	private BigDecimal R37_DAY8_14;
	private BigDecimal R37_DAY15_30;
	private BigDecimal R37_DAY31_TO_2M;
	private BigDecimal R37_MORE2M_TO_3M;
	private BigDecimal R37_OVER3M_TO_6M;
	private BigDecimal R37_OVER6M_TO_1Y;
	private BigDecimal R37_OVER1Y_TO_3Y;
	private BigDecimal R37_OVER3Y_TO_5Y;
	private BigDecimal R37_OVER5Y;
	private BigDecimal R37_TOTAL;
	private String R38_PRODUCT;
	private BigDecimal R38_DAY1;
	private BigDecimal R38_DAY2_7;
	private BigDecimal R38_DAY8_14;
	private BigDecimal R38_DAY15_30;
	private BigDecimal R38_DAY31_TO_2M;
	private BigDecimal R38_MORE2M_TO_3M;
	private BigDecimal R38_OVER3M_TO_6M;
	private BigDecimal R38_OVER6M_TO_1Y;
	private BigDecimal R38_OVER1Y_TO_3Y;
	private BigDecimal R38_OVER3Y_TO_5Y;
	private BigDecimal R38_OVER5Y;
	private BigDecimal R38_TOTAL;
	private String R39_PRODUCT;
	private BigDecimal R39_DAY1;
	private BigDecimal R39_DAY2_7;
	private BigDecimal R39_DAY8_14;
	private BigDecimal R39_DAY15_30;
	private BigDecimal R39_DAY31_TO_2M;
	private BigDecimal R39_MORE2M_TO_3M;
	private BigDecimal R39_OVER3M_TO_6M;
	private BigDecimal R39_OVER6M_TO_1Y;
	private BigDecimal R39_OVER1Y_TO_3Y;
	private BigDecimal R39_OVER3Y_TO_5Y;
	private BigDecimal R39_OVER5Y;
	private BigDecimal R39_TOTAL;
	private String R40_PRODUCT;
	private BigDecimal R40_DAY1;
	private BigDecimal R40_DAY2_7;
	private BigDecimal R40_DAY8_14;
	private BigDecimal R40_DAY15_30;
	private BigDecimal R40_DAY31_TO_2M;
	private BigDecimal R40_MORE2M_TO_3M;
	private BigDecimal R40_OVER3M_TO_6M;
	private BigDecimal R40_OVER6M_TO_1Y;
	private BigDecimal R40_OVER1Y_TO_3Y;
	private BigDecimal R40_OVER3Y_TO_5Y;
	private BigDecimal R40_OVER5Y;
	private BigDecimal R40_TOTAL;
//	private String R41_PRODUCT;
//	private BigDecimal R41_DAY1;
//	private BigDecimal R41_DAY2_7;
//	private BigDecimal R41_DAY8_14;
//	private BigDecimal R41_DAY15_30;
//	private BigDecimal R41_DAY31_TO_2M;
//	private BigDecimal R41_MORE2M_TO_3M;
//	private BigDecimal R41_OVER3M_TO_6M;
//	private BigDecimal R41_OVER6M_TO_1Y;
//	private BigDecimal R41_OVER1Y_TO_3Y;
//	private BigDecimal R41_OVER3Y_TO_5Y;
//	private BigDecimal R41_OVER5Y;
//	private BigDecimal R41_TOTAL;
//	private String R42_PRODUCT;
//	private BigDecimal R42_DAY1;
//	private BigDecimal R42_DAY2_7;
//	private BigDecimal R42_DAY8_14;
//	private BigDecimal R42_DAY15_30;
//	private BigDecimal R42_DAY31_TO_2M;
//	private BigDecimal R42_MORE2M_TO_3M;
//	private BigDecimal R42_OVER3M_TO_6M;
//	private BigDecimal R42_OVER6M_TO_1Y;
//	private BigDecimal R42_OVER1Y_TO_3Y;
//	private BigDecimal R42_OVER3Y_TO_5Y;
//	private BigDecimal R42_OVER5Y;
//	private BigDecimal R42_TOTAL;
//	private String R43_PRODUCT;
//	private BigDecimal R43_DAY1;
//	private BigDecimal R43_DAY2_7;
//	private BigDecimal R43_DAY8_14;
//	private BigDecimal R43_DAY15_30;
//	private BigDecimal R43_DAY31_TO_2M;
//	private BigDecimal R43_MORE2M_TO_3M;
//	private BigDecimal R43_OVER3M_TO_6M;
//	private BigDecimal R43_OVER6M_TO_1Y;
//	private BigDecimal R43_OVER1Y_TO_3Y;
//	private BigDecimal R43_OVER3Y_TO_5Y;
//	private BigDecimal R43_OVER5Y;
//	private BigDecimal R43_TOTAL;
//	private String R44_PRODUCT;
//	private BigDecimal R44_DAY1;
//	private BigDecimal R44_DAY2_7;
//	private BigDecimal R44_DAY8_14;
//	private BigDecimal R44_DAY15_30;
//	private BigDecimal R44_DAY31_TO_2M;
//	private BigDecimal R44_MORE2M_TO_3M;
//	private BigDecimal R44_OVER3M_TO_6M;
//	private BigDecimal R44_OVER6M_TO_1Y;
//	private BigDecimal R44_OVER1Y_TO_3Y;
//	private BigDecimal R44_OVER3Y_TO_5Y;
//	private BigDecimal R44_OVER5Y;
//	private BigDecimal R44_TOTAL;
//	private String R45_PRODUCT;
//	private BigDecimal R45_DAY1;
//	private BigDecimal R45_DAY2_7;
//	private BigDecimal R45_DAY8_14;
//	private BigDecimal R45_DAY15_30;
//	private BigDecimal R45_DAY31_TO_2M;
//	private BigDecimal R45_MORE2M_TO_3M;
//	private BigDecimal R45_OVER3M_TO_6M;
//	private BigDecimal R45_OVER6M_TO_1Y;
//	private BigDecimal R45_OVER1Y_TO_3Y;
//	private BigDecimal R45_OVER3Y_TO_5Y;
//	private BigDecimal R45_OVER5Y;
//	private BigDecimal R45_TOTAL;
//	private String R46_PRODUCT;
//	private BigDecimal R46_DAY1;
//	private BigDecimal R46_DAY2_7;
//	private BigDecimal R46_DAY8_14;
//	private BigDecimal R46_DAY15_30;
//	private BigDecimal R46_DAY31_TO_2M;
//	private BigDecimal R46_MORE2M_TO_3M;
//	private BigDecimal R46_OVER3M_TO_6M;
//	private BigDecimal R46_OVER6M_TO_1Y;
//	private BigDecimal R46_OVER1Y_TO_3Y;
//	private BigDecimal R46_OVER3Y_TO_5Y;
//	private BigDecimal R46_OVER5Y;
//	private BigDecimal R46_TOTAL;
	private String R47_PRODUCT;
	private BigDecimal R47_DAY1;
	private BigDecimal R47_DAY2_7;
	private BigDecimal R47_DAY8_14;
	private BigDecimal R47_DAY15_30;
	private BigDecimal R47_DAY31_TO_2M;
	private BigDecimal R47_MORE2M_TO_3M;
	private BigDecimal R47_OVER3M_TO_6M;
	private BigDecimal R47_OVER6M_TO_1Y;
	private BigDecimal R47_OVER1Y_TO_3Y;
	private BigDecimal R47_OVER3Y_TO_5Y;
	private BigDecimal R47_OVER5Y;
	private BigDecimal R47_TOTAL;
	private String R48_PRODUCT;
	private BigDecimal R48_DAY1;
	private BigDecimal R48_DAY2_7;
	private BigDecimal R48_DAY8_14;
	private BigDecimal R48_DAY15_30;
	private BigDecimal R48_DAY31_TO_2M;
	private BigDecimal R48_MORE2M_TO_3M;
	private BigDecimal R48_OVER3M_TO_6M;
	private BigDecimal R48_OVER6M_TO_1Y;
	private BigDecimal R48_OVER1Y_TO_3Y;
	private BigDecimal R48_OVER3Y_TO_5Y;
	private BigDecimal R48_OVER5Y;
	private BigDecimal R48_TOTAL;
	private String R49_PRODUCT;
	private BigDecimal R49_DAY1;
	private BigDecimal R49_DAY2_7;
	private BigDecimal R49_DAY8_14;
	private BigDecimal R49_DAY15_30;
	private BigDecimal R49_DAY31_TO_2M;
	private BigDecimal R49_MORE2M_TO_3M;
	private BigDecimal R49_OVER3M_TO_6M;
	private BigDecimal R49_OVER6M_TO_1Y;
	private BigDecimal R49_OVER1Y_TO_3Y;
	private BigDecimal R49_OVER3Y_TO_5Y;
	private BigDecimal R49_OVER5Y;
	private BigDecimal R49_TOTAL;
	private String R50_PRODUCT;
	private BigDecimal R50_DAY1;
	private BigDecimal R50_DAY2_7;
	private BigDecimal R50_DAY8_14;
	private BigDecimal R50_DAY15_30;
	private BigDecimal R50_DAY31_TO_2M;
	private BigDecimal R50_MORE2M_TO_3M;
	private BigDecimal R50_OVER3M_TO_6M;
	private BigDecimal R50_OVER6M_TO_1Y;
	private BigDecimal R50_OVER1Y_TO_3Y;
	private BigDecimal R50_OVER3Y_TO_5Y;
	private BigDecimal R50_OVER5Y;
	private BigDecimal R50_TOTAL;
	private String R51_PRODUCT;
	private BigDecimal R51_DAY1;
	private BigDecimal R51_DAY2_7;
	private BigDecimal R51_DAY8_14;
	private BigDecimal R51_DAY15_30;
	private BigDecimal R51_DAY31_TO_2M;
	private BigDecimal R51_MORE2M_TO_3M;
	private BigDecimal R51_OVER3M_TO_6M;
	private BigDecimal R51_OVER6M_TO_1Y;
	private BigDecimal R51_OVER1Y_TO_3Y;
	private BigDecimal R51_OVER3Y_TO_5Y;
	private BigDecimal R51_OVER5Y;
	private BigDecimal R51_TOTAL;
	private String R52_PRODUCT;
	private BigDecimal R52_DAY1;
	private BigDecimal R52_DAY2_7;
	private BigDecimal R52_DAY8_14;
	private BigDecimal R52_DAY15_30;
	private BigDecimal R52_DAY31_TO_2M;
	private BigDecimal R52_MORE2M_TO_3M;
	private BigDecimal R52_OVER3M_TO_6M;
	private BigDecimal R52_OVER6M_TO_1Y;
	private BigDecimal R52_OVER1Y_TO_3Y;
	private BigDecimal R52_OVER3Y_TO_5Y;
	private BigDecimal R52_OVER5Y;
	private BigDecimal R52_TOTAL;
	private String R53_PRODUCT;
	private BigDecimal R53_DAY1;
	private BigDecimal R53_DAY2_7;
	private BigDecimal R53_DAY8_14;
	private BigDecimal R53_DAY15_30;
	private BigDecimal R53_DAY31_TO_2M;
	private BigDecimal R53_MORE2M_TO_3M;
	private BigDecimal R53_OVER3M_TO_6M;
	private BigDecimal R53_OVER6M_TO_1Y;
	private BigDecimal R53_OVER1Y_TO_3Y;
	private BigDecimal R53_OVER3Y_TO_5Y;
	private BigDecimal R53_OVER5Y;
	private BigDecimal R53_TOTAL;
	private String R54_PRODUCT;
	private BigDecimal R54_DAY1;
	private BigDecimal R54_DAY2_7;
	private BigDecimal R54_DAY8_14;
	private BigDecimal R54_DAY15_30;
	private BigDecimal R54_DAY31_TO_2M;
	private BigDecimal R54_MORE2M_TO_3M;
	private BigDecimal R54_OVER3M_TO_6M;
	private BigDecimal R54_OVER6M_TO_1Y;
	private BigDecimal R54_OVER1Y_TO_3Y;
	private BigDecimal R54_OVER3Y_TO_5Y;
	private BigDecimal R54_OVER5Y;
	private BigDecimal R54_TOTAL;
	private String R55_PRODUCT;
	private BigDecimal R55_DAY1;
	private BigDecimal R55_DAY2_7;
	private BigDecimal R55_DAY8_14;
	private BigDecimal R55_DAY15_30;
	private BigDecimal R55_DAY31_TO_2M;
	private BigDecimal R55_MORE2M_TO_3M;
	private BigDecimal R55_OVER3M_TO_6M;
	private BigDecimal R55_OVER6M_TO_1Y;
	private BigDecimal R55_OVER1Y_TO_3Y;
	private BigDecimal R55_OVER3Y_TO_5Y;
	private BigDecimal R55_OVER5Y;
	private BigDecimal R55_TOTAL;
	private String R56_PRODUCT;
	private BigDecimal R56_DAY1;
	private BigDecimal R56_DAY2_7;
	private BigDecimal R56_DAY8_14;
	private BigDecimal R56_DAY15_30;
	private BigDecimal R56_DAY31_TO_2M;
	private BigDecimal R56_MORE2M_TO_3M;
	private BigDecimal R56_OVER3M_TO_6M;
	private BigDecimal R56_OVER6M_TO_1Y;
	private BigDecimal R56_OVER1Y_TO_3Y;
	private BigDecimal R56_OVER3Y_TO_5Y;
	private BigDecimal R56_OVER5Y;
	private BigDecimal R56_TOTAL;
	private String R57_PRODUCT;
	private BigDecimal R57_DAY1;
	private BigDecimal R57_DAY2_7;
	private BigDecimal R57_DAY8_14;
	private BigDecimal R57_DAY15_30;
	private BigDecimal R57_DAY31_TO_2M;
	private BigDecimal R57_MORE2M_TO_3M;
	private BigDecimal R57_OVER3M_TO_6M;
	private BigDecimal R57_OVER6M_TO_1Y;
	private BigDecimal R57_OVER1Y_TO_3Y;
	private BigDecimal R57_OVER3Y_TO_5Y;
	private BigDecimal R57_OVER5Y;
	private BigDecimal R57_TOTAL;
	private String R58_PRODUCT;
	private BigDecimal R58_DAY1;
	private BigDecimal R58_DAY2_7;
	private BigDecimal R58_DAY8_14;
	private BigDecimal R58_DAY15_30;
	private BigDecimal R58_DAY31_TO_2M;
	private BigDecimal R58_MORE2M_TO_3M;
	private BigDecimal R58_OVER3M_TO_6M;
	private BigDecimal R58_OVER6M_TO_1Y;
	private BigDecimal R58_OVER1Y_TO_3Y;
	private BigDecimal R58_OVER3Y_TO_5Y;
	private BigDecimal R58_OVER5Y;
	private BigDecimal R58_TOTAL;
	private String R59_PRODUCT;
	private BigDecimal R59_DAY1;
	private BigDecimal R59_DAY2_7;
	private BigDecimal R59_DAY8_14;
	private BigDecimal R59_DAY15_30;
	private BigDecimal R59_DAY31_TO_2M;
	private BigDecimal R59_MORE2M_TO_3M;
	private BigDecimal R59_OVER3M_TO_6M;
	private BigDecimal R59_OVER6M_TO_1Y;
	private BigDecimal R59_OVER1Y_TO_3Y;
	private BigDecimal R59_OVER3Y_TO_5Y;
	private BigDecimal R59_OVER5Y;
	private BigDecimal R59_TOTAL;
	private String R60_PRODUCT;
	private BigDecimal R60_DAY1;
	private BigDecimal R60_DAY2_7;
	private BigDecimal R60_DAY8_14;
	private BigDecimal R60_DAY15_30;
	private BigDecimal R60_DAY31_TO_2M;
	private BigDecimal R60_MORE2M_TO_3M;
	private BigDecimal R60_OVER3M_TO_6M;
	private BigDecimal R60_OVER6M_TO_1Y;
	private BigDecimal R60_OVER1Y_TO_3Y;
	private BigDecimal R60_OVER3Y_TO_5Y;
	private BigDecimal R60_OVER5Y;
	private BigDecimal R60_TOTAL;
	private String R61_PRODUCT;
	private BigDecimal R61_DAY1;
	private BigDecimal R61_DAY2_7;
	private BigDecimal R61_DAY8_14;
	private BigDecimal R61_DAY15_30;
	private BigDecimal R61_DAY31_TO_2M;
	private BigDecimal R61_MORE2M_TO_3M;
	private BigDecimal R61_OVER3M_TO_6M;
	private BigDecimal R61_OVER6M_TO_1Y;
	private BigDecimal R61_OVER1Y_TO_3Y;
	private BigDecimal R61_OVER3Y_TO_5Y;
	private BigDecimal R61_OVER5Y;
	private BigDecimal R61_TOTAL;
	private String R62_PRODUCT;
	private BigDecimal R62_DAY1;
	private BigDecimal R62_DAY2_7;
	private BigDecimal R62_DAY8_14;
	private BigDecimal R62_DAY15_30;
	private BigDecimal R62_DAY31_TO_2M;
	private BigDecimal R62_MORE2M_TO_3M;
	private BigDecimal R62_OVER3M_TO_6M;
	private BigDecimal R62_OVER6M_TO_1Y;
	private BigDecimal R62_OVER1Y_TO_3Y;
	private BigDecimal R62_OVER3Y_TO_5Y;
	private BigDecimal R62_OVER5Y;
	private BigDecimal R62_TOTAL;
	private String R63_PRODUCT;
	private BigDecimal R63_DAY1;
	private BigDecimal R63_DAY2_7;
	private BigDecimal R63_DAY8_14;
	private BigDecimal R63_DAY15_30;
	private BigDecimal R63_DAY31_TO_2M;
	private BigDecimal R63_MORE2M_TO_3M;
	private BigDecimal R63_OVER3M_TO_6M;
	private BigDecimal R63_OVER6M_TO_1Y;
	private BigDecimal R63_OVER1Y_TO_3Y;
	private BigDecimal R63_OVER3Y_TO_5Y;
	private BigDecimal R63_OVER5Y;
	private BigDecimal R63_TOTAL;
	private String R64_PRODUCT;
	private BigDecimal R64_DAY1;
	private BigDecimal R64_DAY2_7;
	private BigDecimal R64_DAY8_14;
	private BigDecimal R64_DAY15_30;
	private BigDecimal R64_DAY31_TO_2M;
	private BigDecimal R64_MORE2M_TO_3M;
	private BigDecimal R64_OVER3M_TO_6M;
	private BigDecimal R64_OVER6M_TO_1Y;
	private BigDecimal R64_OVER1Y_TO_3Y;
	private BigDecimal R64_OVER3Y_TO_5Y;
	private BigDecimal R64_OVER5Y;
	private BigDecimal R64_TOTAL;
	private String R65_PRODUCT;
	private BigDecimal R65_DAY1;
	private BigDecimal R65_DAY2_7;
	private BigDecimal R65_DAY8_14;
	private BigDecimal R65_DAY15_30;
	private BigDecimal R65_DAY31_TO_2M;
	private BigDecimal R65_MORE2M_TO_3M;
	private BigDecimal R65_OVER3M_TO_6M;
	private BigDecimal R65_OVER6M_TO_1Y;
	private BigDecimal R65_OVER1Y_TO_3Y;
	private BigDecimal R65_OVER3Y_TO_5Y;
	private BigDecimal R65_OVER5Y;
	private BigDecimal R65_TOTAL;
	private String R66_PRODUCT;
	private BigDecimal R66_DAY1;
	private BigDecimal R66_DAY2_7;
	private BigDecimal R66_DAY8_14;
	private BigDecimal R66_DAY15_30;
	private BigDecimal R66_DAY31_TO_2M;
	private BigDecimal R66_MORE2M_TO_3M;
	private BigDecimal R66_OVER3M_TO_6M;
	private BigDecimal R66_OVER6M_TO_1Y;
	private BigDecimal R66_OVER1Y_TO_3Y;
	private BigDecimal R66_OVER3Y_TO_5Y;
	private BigDecimal R66_OVER5Y;
	private BigDecimal R66_TOTAL;
	private String R67_PRODUCT;
	private BigDecimal R67_DAY1;
	private BigDecimal R67_DAY2_7;
	private BigDecimal R67_DAY8_14;
	private BigDecimal R67_DAY15_30;
	private BigDecimal R67_DAY31_TO_2M;
	private BigDecimal R67_MORE2M_TO_3M;
	private BigDecimal R67_OVER3M_TO_6M;
	private BigDecimal R67_OVER6M_TO_1Y;
	private BigDecimal R67_OVER1Y_TO_3Y;
	private BigDecimal R67_OVER3Y_TO_5Y;
	private BigDecimal R67_OVER5Y;
	private BigDecimal R67_TOTAL;
	private String R68_PRODUCT;
	private BigDecimal R68_DAY1;
	private BigDecimal R68_DAY2_7;
	private BigDecimal R68_DAY8_14;
	private BigDecimal R68_DAY15_30;
	private BigDecimal R68_DAY31_TO_2M;
	private BigDecimal R68_MORE2M_TO_3M;
	private BigDecimal R68_OVER3M_TO_6M;
	private BigDecimal R68_OVER6M_TO_1Y;
	private BigDecimal R68_OVER1Y_TO_3Y;
	private BigDecimal R68_OVER3Y_TO_5Y;
	private BigDecimal R68_OVER5Y;
	private BigDecimal R68_TOTAL;
	private String R69_PRODUCT;
	private BigDecimal R69_DAY1;
	private BigDecimal R69_DAY2_7;
	private BigDecimal R69_DAY8_14;
	private BigDecimal R69_DAY15_30;
	private BigDecimal R69_DAY31_TO_2M;
	private BigDecimal R69_MORE2M_TO_3M;
	private BigDecimal R69_OVER3M_TO_6M;
	private BigDecimal R69_OVER6M_TO_1Y;
	private BigDecimal R69_OVER1Y_TO_3Y;
	private BigDecimal R69_OVER3Y_TO_5Y;
	private BigDecimal R69_OVER5Y;
	private BigDecimal R69_TOTAL;
	private String R70_PRODUCT;
	private BigDecimal R70_DAY1;
	private BigDecimal R70_DAY2_7;
	private BigDecimal R70_DAY8_14;
	private BigDecimal R70_DAY15_30;
	private BigDecimal R70_DAY31_TO_2M;
	private BigDecimal R70_MORE2M_TO_3M;
	private BigDecimal R70_OVER3M_TO_6M;
	private BigDecimal R70_OVER6M_TO_1Y;
	private BigDecimal R70_OVER1Y_TO_3Y;
	private BigDecimal R70_OVER3Y_TO_5Y;
	private BigDecimal R70_OVER5Y;
	private BigDecimal R70_TOTAL;
	private String R71_PRODUCT;
	private BigDecimal R71_DAY1;
	private BigDecimal R71_DAY2_7;
	private BigDecimal R71_DAY8_14;
	private BigDecimal R71_DAY15_30;
	private BigDecimal R71_DAY31_TO_2M;
	private BigDecimal R71_MORE2M_TO_3M;
	private BigDecimal R71_OVER3M_TO_6M;
	private BigDecimal R71_OVER6M_TO_1Y;
	private BigDecimal R71_OVER1Y_TO_3Y;
	private BigDecimal R71_OVER3Y_TO_5Y;
	private BigDecimal R71_OVER5Y;
	private BigDecimal R71_TOTAL;
	private String R72_PRODUCT;
	private BigDecimal R72_DAY1;
	private BigDecimal R72_DAY2_7;
	private BigDecimal R72_DAY8_14;
	private BigDecimal R72_DAY15_30;
	private BigDecimal R72_DAY31_TO_2M;
	private BigDecimal R72_MORE2M_TO_3M;
	private BigDecimal R72_OVER3M_TO_6M;
	private BigDecimal R72_OVER6M_TO_1Y;
	private BigDecimal R72_OVER1Y_TO_3Y;
	private BigDecimal R72_OVER3Y_TO_5Y;
	private BigDecimal R72_OVER5Y;
	private BigDecimal R72_TOTAL;
	private String R73_PRODUCT;
	private BigDecimal R73_DAY1;
	private BigDecimal R73_DAY2_7;
	private BigDecimal R73_DAY8_14;
	private BigDecimal R73_DAY15_30;
	private BigDecimal R73_DAY31_TO_2M;
	private BigDecimal R73_MORE2M_TO_3M;
	private BigDecimal R73_OVER3M_TO_6M;
	private BigDecimal R73_OVER6M_TO_1Y;
	private BigDecimal R73_OVER1Y_TO_3Y;
	private BigDecimal R73_OVER3Y_TO_5Y;
	private BigDecimal R73_OVER5Y;
	private BigDecimal R73_TOTAL;
	private String R74_PRODUCT;
	private BigDecimal R74_DAY1;
	private BigDecimal R74_DAY2_7;
	private BigDecimal R74_DAY8_14;
	private BigDecimal R74_DAY15_30;
	private BigDecimal R74_DAY31_TO_2M;
	private BigDecimal R74_MORE2M_TO_3M;
	private BigDecimal R74_OVER3M_TO_6M;
	private BigDecimal R74_OVER6M_TO_1Y;
	private BigDecimal R74_OVER1Y_TO_3Y;
	private BigDecimal R74_OVER3Y_TO_5Y;
	private BigDecimal R74_OVER5Y;
	private BigDecimal R74_TOTAL;
	private String R75_PRODUCT;
	private BigDecimal R75_DAY1;
	private BigDecimal R75_DAY2_7;
	private BigDecimal R75_DAY8_14;
	private BigDecimal R75_DAY15_30;
	private BigDecimal R75_DAY31_TO_2M;
	private BigDecimal R75_MORE2M_TO_3M;
	private BigDecimal R75_OVER3M_TO_6M;
	private BigDecimal R75_OVER6M_TO_1Y;
	private BigDecimal R75_OVER1Y_TO_3Y;
	private BigDecimal R75_OVER3Y_TO_5Y;
	private BigDecimal R75_OVER5Y;
	private BigDecimal R75_TOTAL;
	private String R76_PRODUCT;
	private BigDecimal R76_DAY1;
	private BigDecimal R76_DAY2_7;
	private BigDecimal R76_DAY8_14;
	private BigDecimal R76_DAY15_30;
	private BigDecimal R76_DAY31_TO_2M;
	private BigDecimal R76_MORE2M_TO_3M;
	private BigDecimal R76_OVER3M_TO_6M;
	private BigDecimal R76_OVER6M_TO_1Y;
	private BigDecimal R76_OVER1Y_TO_3Y;
	private BigDecimal R76_OVER3Y_TO_5Y;
	private BigDecimal R76_OVER5Y;
	private BigDecimal R76_TOTAL;
//	private String R77_PRODUCT;
//	private BigDecimal R77_DAY1;
//	private BigDecimal R77_DAY2_7;
//	private BigDecimal R77_DAY8_14;
//	private BigDecimal R77_DAY15_30;
//	private BigDecimal R77_DAY31_TO_2M;
//	private BigDecimal R77_MORE2M_TO_3M;
//	private BigDecimal R77_OVER3M_TO_6M;
//	private BigDecimal R77_OVER6M_TO_1Y;
//	private BigDecimal R77_OVER1Y_TO_3Y;
//	private BigDecimal R77_OVER3Y_TO_5Y;
//	private BigDecimal R77_OVER5Y;
//	private BigDecimal R77_TOTAL;
//	private String R78_PRODUCT;
//	private BigDecimal R78_DAY1;
//	private BigDecimal R78_DAY2_7;
//	private BigDecimal R78_DAY8_14;
//	private BigDecimal R78_DAY15_30;
//	private BigDecimal R78_DAY31_TO_2M;
//	private BigDecimal R78_MORE2M_TO_3M;
//	private BigDecimal R78_OVER3M_TO_6M;
//	private BigDecimal R78_OVER6M_TO_1Y;
//	private BigDecimal R78_OVER1Y_TO_3Y;
//	private BigDecimal R78_OVER3Y_TO_5Y;
//	private BigDecimal R78_OVER5Y;
//	private BigDecimal R78_TOTAL;
//	private String R79_PRODUCT;
//	private BigDecimal R79_DAY1;
//	private BigDecimal R79_DAY2_7;
//	private BigDecimal R79_DAY8_14;
//	private BigDecimal R79_DAY15_30;
//	private BigDecimal R79_DAY31_TO_2M;
//	private BigDecimal R79_MORE2M_TO_3M;
//	private BigDecimal R79_OVER3M_TO_6M;
//	private BigDecimal R79_OVER6M_TO_1Y;
//	private BigDecimal R79_OVER1Y_TO_3Y;
//	private BigDecimal R79_OVER3Y_TO_5Y;
//	private BigDecimal R79_OVER5Y;
//	private BigDecimal R79_TOTAL;
//	private String R80_PRODUCT;
//	private BigDecimal R80_DAY1;
//	private BigDecimal R80_DAY2_7;
//	private BigDecimal R80_DAY8_14;
//	private BigDecimal R80_DAY15_30;
//	private BigDecimal R80_DAY31_TO_2M;
//	private BigDecimal R80_MORE2M_TO_3M;
//	private BigDecimal R80_OVER3M_TO_6M;
//	private BigDecimal R80_OVER6M_TO_1Y;
//	private BigDecimal R80_OVER1Y_TO_3Y;
//	private BigDecimal R80_OVER3Y_TO_5Y;
//	private BigDecimal R80_OVER5Y;
//	private BigDecimal R80_TOTAL;
//	private String R81_PRODUCT;
//	private BigDecimal R81_DAY1;
//	private BigDecimal R81_DAY2_7;
//	private BigDecimal R81_DAY8_14;
//	private BigDecimal R81_DAY15_30;
//	private BigDecimal R81_DAY31_TO_2M;
//	private BigDecimal R81_MORE2M_TO_3M;
//	private BigDecimal R81_OVER3M_TO_6M;
//	private BigDecimal R81_OVER6M_TO_1Y;
//	private BigDecimal R81_OVER1Y_TO_3Y;
//	private BigDecimal R81_OVER3Y_TO_5Y;
//	private BigDecimal R81_OVER5Y;
//	private BigDecimal R81_TOTAL;
//	private String R82_PRODUCT;
//	private BigDecimal R82_DAY1;
//	private BigDecimal R82_DAY2_7;
//	private BigDecimal R82_DAY8_14;
//	private BigDecimal R82_DAY15_30;
//	private BigDecimal R82_DAY31_TO_2M;
//	private BigDecimal R82_MORE2M_TO_3M;
//	private BigDecimal R82_OVER3M_TO_6M;
//	private BigDecimal R82_OVER6M_TO_1Y;
//	private BigDecimal R82_OVER1Y_TO_3Y;
//	private BigDecimal R82_OVER3Y_TO_5Y;
//	private BigDecimal R82_OVER5Y;
//	private BigDecimal R82_TOTAL;
//	private String R83_PRODUCT;
//	private BigDecimal R83_DAY1;
//	private BigDecimal R83_DAY2_7;
//	private BigDecimal R83_DAY8_14;
//	private BigDecimal R83_DAY15_30;
//	private BigDecimal R83_DAY31_TO_2M;
//	private BigDecimal R83_MORE2M_TO_3M;
//	private BigDecimal R83_OVER3M_TO_6M;
//	private BigDecimal R83_OVER6M_TO_1Y;
//	private BigDecimal R83_OVER1Y_TO_3Y;
//	private BigDecimal R83_OVER3Y_TO_5Y;
//	private BigDecimal R83_OVER5Y;
//	private BigDecimal R83_TOTAL;
	private String R84_PRODUCT;
	private BigDecimal R84_DAY1;
	private BigDecimal R84_DAY2_7;
	private BigDecimal R84_DAY8_14;
	private BigDecimal R84_DAY15_30;
	private BigDecimal R84_DAY31_TO_2M;
	private BigDecimal R84_MORE2M_TO_3M;
	private BigDecimal R84_OVER3M_TO_6M;
	private BigDecimal R84_OVER6M_TO_1Y;
	private BigDecimal R84_OVER1Y_TO_3Y;
	private BigDecimal R84_OVER3Y_TO_5Y;
	private BigDecimal R84_OVER5Y;
	private BigDecimal R84_TOTAL;
	private String R85_PRODUCT;
	private BigDecimal R85_DAY1;
	private BigDecimal R85_DAY2_7;
	private BigDecimal R85_DAY8_14;
	private BigDecimal R85_DAY15_30;
	private BigDecimal R85_DAY31_TO_2M;
	private BigDecimal R85_MORE2M_TO_3M;
	private BigDecimal R85_OVER3M_TO_6M;
	private BigDecimal R85_OVER6M_TO_1Y;
	private BigDecimal R85_OVER1Y_TO_3Y;
	private BigDecimal R85_OVER3Y_TO_5Y;
	private BigDecimal R85_OVER5Y;
	private BigDecimal R85_TOTAL;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date REPORT_DATE;
	private String REPORT_VERSION;
	private String REPORT_CURRENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;
	public String getSRL_NO() {
		return SRL_NO;
	}
	public void setSRL_NO(String sRL_NO) {
		SRL_NO = sRL_NO;
	}
	public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}
	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}
	public BigDecimal getR11_DAY1() {
		return R11_DAY1;
	}
	public void setR11_DAY1(BigDecimal r11_DAY1) {
		R11_DAY1 = r11_DAY1;
	}
	public BigDecimal getR11_DAY2_7() {
		return R11_DAY2_7;
	}
	public void setR11_DAY2_7(BigDecimal r11_DAY2_7) {
		R11_DAY2_7 = r11_DAY2_7;
	}
	public BigDecimal getR11_DAY8_14() {
		return R11_DAY8_14;
	}
	public void setR11_DAY8_14(BigDecimal r11_DAY8_14) {
		R11_DAY8_14 = r11_DAY8_14;
	}
	public BigDecimal getR11_DAY15_30() {
		return R11_DAY15_30;
	}
	public void setR11_DAY15_30(BigDecimal r11_DAY15_30) {
		R11_DAY15_30 = r11_DAY15_30;
	}
	public BigDecimal getR11_DAY31_TO_2M() {
		return R11_DAY31_TO_2M;
	}
	public void setR11_DAY31_TO_2M(BigDecimal r11_DAY31_TO_2M) {
		R11_DAY31_TO_2M = r11_DAY31_TO_2M;
	}
	public BigDecimal getR11_MORE2M_TO_3M() {
		return R11_MORE2M_TO_3M;
	}
	public void setR11_MORE2M_TO_3M(BigDecimal r11_MORE2M_TO_3M) {
		R11_MORE2M_TO_3M = r11_MORE2M_TO_3M;
	}
	public BigDecimal getR11_OVER3M_TO_6M() {
		return R11_OVER3M_TO_6M;
	}
	public void setR11_OVER3M_TO_6M(BigDecimal r11_OVER3M_TO_6M) {
		R11_OVER3M_TO_6M = r11_OVER3M_TO_6M;
	}
	public BigDecimal getR11_OVER6M_TO_1Y() {
		return R11_OVER6M_TO_1Y;
	}
	public void setR11_OVER6M_TO_1Y(BigDecimal r11_OVER6M_TO_1Y) {
		R11_OVER6M_TO_1Y = r11_OVER6M_TO_1Y;
	}
	public BigDecimal getR11_OVER1Y_TO_3Y() {
		return R11_OVER1Y_TO_3Y;
	}
	public void setR11_OVER1Y_TO_3Y(BigDecimal r11_OVER1Y_TO_3Y) {
		R11_OVER1Y_TO_3Y = r11_OVER1Y_TO_3Y;
	}
	public BigDecimal getR11_OVER3Y_TO_5Y() {
		return R11_OVER3Y_TO_5Y;
	}
	public void setR11_OVER3Y_TO_5Y(BigDecimal r11_OVER3Y_TO_5Y) {
		R11_OVER3Y_TO_5Y = r11_OVER3Y_TO_5Y;
	}
	public BigDecimal getR11_OVER5Y() {
		return R11_OVER5Y;
	}
	public void setR11_OVER5Y(BigDecimal r11_OVER5Y) {
		R11_OVER5Y = r11_OVER5Y;
	}
	public BigDecimal getR11_TOTAL() {
		return R11_TOTAL;
	}
	public void setR11_TOTAL(BigDecimal r11_TOTAL) {
		R11_TOTAL = r11_TOTAL;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_DAY1() {
		return R12_DAY1;
	}
	public void setR12_DAY1(BigDecimal r12_DAY1) {
		R12_DAY1 = r12_DAY1;
	}
	public BigDecimal getR12_DAY2_7() {
		return R12_DAY2_7;
	}
	public void setR12_DAY2_7(BigDecimal r12_DAY2_7) {
		R12_DAY2_7 = r12_DAY2_7;
	}
	public BigDecimal getR12_DAY8_14() {
		return R12_DAY8_14;
	}
	public void setR12_DAY8_14(BigDecimal r12_DAY8_14) {
		R12_DAY8_14 = r12_DAY8_14;
	}
	public BigDecimal getR12_DAY15_30() {
		return R12_DAY15_30;
	}
	public void setR12_DAY15_30(BigDecimal r12_DAY15_30) {
		R12_DAY15_30 = r12_DAY15_30;
	}
	public BigDecimal getR12_DAY31_TO_2M() {
		return R12_DAY31_TO_2M;
	}
	public void setR12_DAY31_TO_2M(BigDecimal r12_DAY31_TO_2M) {
		R12_DAY31_TO_2M = r12_DAY31_TO_2M;
	}
	public BigDecimal getR12_MORE2M_TO_3M() {
		return R12_MORE2M_TO_3M;
	}
	public void setR12_MORE2M_TO_3M(BigDecimal r12_MORE2M_TO_3M) {
		R12_MORE2M_TO_3M = r12_MORE2M_TO_3M;
	}
	public BigDecimal getR12_OVER3M_TO_6M() {
		return R12_OVER3M_TO_6M;
	}
	public void setR12_OVER3M_TO_6M(BigDecimal r12_OVER3M_TO_6M) {
		R12_OVER3M_TO_6M = r12_OVER3M_TO_6M;
	}
	public BigDecimal getR12_OVER6M_TO_1Y() {
		return R12_OVER6M_TO_1Y;
	}
	public void setR12_OVER6M_TO_1Y(BigDecimal r12_OVER6M_TO_1Y) {
		R12_OVER6M_TO_1Y = r12_OVER6M_TO_1Y;
	}
	public BigDecimal getR12_OVER1Y_TO_3Y() {
		return R12_OVER1Y_TO_3Y;
	}
	public void setR12_OVER1Y_TO_3Y(BigDecimal r12_OVER1Y_TO_3Y) {
		R12_OVER1Y_TO_3Y = r12_OVER1Y_TO_3Y;
	}
	public BigDecimal getR12_OVER3Y_TO_5Y() {
		return R12_OVER3Y_TO_5Y;
	}
	public void setR12_OVER3Y_TO_5Y(BigDecimal r12_OVER3Y_TO_5Y) {
		R12_OVER3Y_TO_5Y = r12_OVER3Y_TO_5Y;
	}
	public BigDecimal getR12_OVER5Y() {
		return R12_OVER5Y;
	}
	public void setR12_OVER5Y(BigDecimal r12_OVER5Y) {
		R12_OVER5Y = r12_OVER5Y;
	}
	public BigDecimal getR12_TOTAL() {
		return R12_TOTAL;
	}
	public void setR12_TOTAL(BigDecimal r12_TOTAL) {
		R12_TOTAL = r12_TOTAL;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_DAY1() {
		return R13_DAY1;
	}
	public void setR13_DAY1(BigDecimal r13_DAY1) {
		R13_DAY1 = r13_DAY1;
	}
	public BigDecimal getR13_DAY2_7() {
		return R13_DAY2_7;
	}
	public void setR13_DAY2_7(BigDecimal r13_DAY2_7) {
		R13_DAY2_7 = r13_DAY2_7;
	}
	public BigDecimal getR13_DAY8_14() {
		return R13_DAY8_14;
	}
	public void setR13_DAY8_14(BigDecimal r13_DAY8_14) {
		R13_DAY8_14 = r13_DAY8_14;
	}
	public BigDecimal getR13_DAY15_30() {
		return R13_DAY15_30;
	}
	public void setR13_DAY15_30(BigDecimal r13_DAY15_30) {
		R13_DAY15_30 = r13_DAY15_30;
	}
	public BigDecimal getR13_DAY31_TO_2M() {
		return R13_DAY31_TO_2M;
	}
	public void setR13_DAY31_TO_2M(BigDecimal r13_DAY31_TO_2M) {
		R13_DAY31_TO_2M = r13_DAY31_TO_2M;
	}
	public BigDecimal getR13_MORE2M_TO_3M() {
		return R13_MORE2M_TO_3M;
	}
	public void setR13_MORE2M_TO_3M(BigDecimal r13_MORE2M_TO_3M) {
		R13_MORE2M_TO_3M = r13_MORE2M_TO_3M;
	}
	public BigDecimal getR13_OVER3M_TO_6M() {
		return R13_OVER3M_TO_6M;
	}
	public void setR13_OVER3M_TO_6M(BigDecimal r13_OVER3M_TO_6M) {
		R13_OVER3M_TO_6M = r13_OVER3M_TO_6M;
	}
	public BigDecimal getR13_OVER6M_TO_1Y() {
		return R13_OVER6M_TO_1Y;
	}
	public void setR13_OVER6M_TO_1Y(BigDecimal r13_OVER6M_TO_1Y) {
		R13_OVER6M_TO_1Y = r13_OVER6M_TO_1Y;
	}
	public BigDecimal getR13_OVER1Y_TO_3Y() {
		return R13_OVER1Y_TO_3Y;
	}
	public void setR13_OVER1Y_TO_3Y(BigDecimal r13_OVER1Y_TO_3Y) {
		R13_OVER1Y_TO_3Y = r13_OVER1Y_TO_3Y;
	}
	public BigDecimal getR13_OVER3Y_TO_5Y() {
		return R13_OVER3Y_TO_5Y;
	}
	public void setR13_OVER3Y_TO_5Y(BigDecimal r13_OVER3Y_TO_5Y) {
		R13_OVER3Y_TO_5Y = r13_OVER3Y_TO_5Y;
	}
	public BigDecimal getR13_OVER5Y() {
		return R13_OVER5Y;
	}
	public void setR13_OVER5Y(BigDecimal r13_OVER5Y) {
		R13_OVER5Y = r13_OVER5Y;
	}
	public BigDecimal getR13_TOTAL() {
		return R13_TOTAL;
	}
	public void setR13_TOTAL(BigDecimal r13_TOTAL) {
		R13_TOTAL = r13_TOTAL;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_DAY1() {
		return R14_DAY1;
	}
	public void setR14_DAY1(BigDecimal r14_DAY1) {
		R14_DAY1 = r14_DAY1;
	}
	public BigDecimal getR14_DAY2_7() {
		return R14_DAY2_7;
	}
	public void setR14_DAY2_7(BigDecimal r14_DAY2_7) {
		R14_DAY2_7 = r14_DAY2_7;
	}
	public BigDecimal getR14_DAY8_14() {
		return R14_DAY8_14;
	}
	public void setR14_DAY8_14(BigDecimal r14_DAY8_14) {
		R14_DAY8_14 = r14_DAY8_14;
	}
	public BigDecimal getR14_DAY15_30() {
		return R14_DAY15_30;
	}
	public void setR14_DAY15_30(BigDecimal r14_DAY15_30) {
		R14_DAY15_30 = r14_DAY15_30;
	}
	public BigDecimal getR14_DAY31_TO_2M() {
		return R14_DAY31_TO_2M;
	}
	public void setR14_DAY31_TO_2M(BigDecimal r14_DAY31_TO_2M) {
		R14_DAY31_TO_2M = r14_DAY31_TO_2M;
	}
	public BigDecimal getR14_MORE2M_TO_3M() {
		return R14_MORE2M_TO_3M;
	}
	public void setR14_MORE2M_TO_3M(BigDecimal r14_MORE2M_TO_3M) {
		R14_MORE2M_TO_3M = r14_MORE2M_TO_3M;
	}
	public BigDecimal getR14_OVER3M_TO_6M() {
		return R14_OVER3M_TO_6M;
	}
	public void setR14_OVER3M_TO_6M(BigDecimal r14_OVER3M_TO_6M) {
		R14_OVER3M_TO_6M = r14_OVER3M_TO_6M;
	}
	public BigDecimal getR14_OVER6M_TO_1Y() {
		return R14_OVER6M_TO_1Y;
	}
	public void setR14_OVER6M_TO_1Y(BigDecimal r14_OVER6M_TO_1Y) {
		R14_OVER6M_TO_1Y = r14_OVER6M_TO_1Y;
	}
	public BigDecimal getR14_OVER1Y_TO_3Y() {
		return R14_OVER1Y_TO_3Y;
	}
	public void setR14_OVER1Y_TO_3Y(BigDecimal r14_OVER1Y_TO_3Y) {
		R14_OVER1Y_TO_3Y = r14_OVER1Y_TO_3Y;
	}
	public BigDecimal getR14_OVER3Y_TO_5Y() {
		return R14_OVER3Y_TO_5Y;
	}
	public void setR14_OVER3Y_TO_5Y(BigDecimal r14_OVER3Y_TO_5Y) {
		R14_OVER3Y_TO_5Y = r14_OVER3Y_TO_5Y;
	}
	public BigDecimal getR14_OVER5Y() {
		return R14_OVER5Y;
	}
	public void setR14_OVER5Y(BigDecimal r14_OVER5Y) {
		R14_OVER5Y = r14_OVER5Y;
	}
	public BigDecimal getR14_TOTAL() {
		return R14_TOTAL;
	}
	public void setR14_TOTAL(BigDecimal r14_TOTAL) {
		R14_TOTAL = r14_TOTAL;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_DAY1() {
		return R15_DAY1;
	}
	public void setR15_DAY1(BigDecimal r15_DAY1) {
		R15_DAY1 = r15_DAY1;
	}
	public BigDecimal getR15_DAY2_7() {
		return R15_DAY2_7;
	}
	public void setR15_DAY2_7(BigDecimal r15_DAY2_7) {
		R15_DAY2_7 = r15_DAY2_7;
	}
	public BigDecimal getR15_DAY8_14() {
		return R15_DAY8_14;
	}
	public void setR15_DAY8_14(BigDecimal r15_DAY8_14) {
		R15_DAY8_14 = r15_DAY8_14;
	}
	public BigDecimal getR15_DAY15_30() {
		return R15_DAY15_30;
	}
	public void setR15_DAY15_30(BigDecimal r15_DAY15_30) {
		R15_DAY15_30 = r15_DAY15_30;
	}
	public BigDecimal getR15_DAY31_TO_2M() {
		return R15_DAY31_TO_2M;
	}
	public void setR15_DAY31_TO_2M(BigDecimal r15_DAY31_TO_2M) {
		R15_DAY31_TO_2M = r15_DAY31_TO_2M;
	}
	public BigDecimal getR15_MORE2M_TO_3M() {
		return R15_MORE2M_TO_3M;
	}
	public void setR15_MORE2M_TO_3M(BigDecimal r15_MORE2M_TO_3M) {
		R15_MORE2M_TO_3M = r15_MORE2M_TO_3M;
	}
	public BigDecimal getR15_OVER3M_TO_6M() {
		return R15_OVER3M_TO_6M;
	}
	public void setR15_OVER3M_TO_6M(BigDecimal r15_OVER3M_TO_6M) {
		R15_OVER3M_TO_6M = r15_OVER3M_TO_6M;
	}
	public BigDecimal getR15_OVER6M_TO_1Y() {
		return R15_OVER6M_TO_1Y;
	}
	public void setR15_OVER6M_TO_1Y(BigDecimal r15_OVER6M_TO_1Y) {
		R15_OVER6M_TO_1Y = r15_OVER6M_TO_1Y;
	}
	public BigDecimal getR15_OVER1Y_TO_3Y() {
		return R15_OVER1Y_TO_3Y;
	}
	public void setR15_OVER1Y_TO_3Y(BigDecimal r15_OVER1Y_TO_3Y) {
		R15_OVER1Y_TO_3Y = r15_OVER1Y_TO_3Y;
	}
	public BigDecimal getR15_OVER3Y_TO_5Y() {
		return R15_OVER3Y_TO_5Y;
	}
	public void setR15_OVER3Y_TO_5Y(BigDecimal r15_OVER3Y_TO_5Y) {
		R15_OVER3Y_TO_5Y = r15_OVER3Y_TO_5Y;
	}
	public BigDecimal getR15_OVER5Y() {
		return R15_OVER5Y;
	}
	public void setR15_OVER5Y(BigDecimal r15_OVER5Y) {
		R15_OVER5Y = r15_OVER5Y;
	}
	public BigDecimal getR15_TOTAL() {
		return R15_TOTAL;
	}
	public void setR15_TOTAL(BigDecimal r15_TOTAL) {
		R15_TOTAL = r15_TOTAL;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_DAY1() {
		return R16_DAY1;
	}
	public void setR16_DAY1(BigDecimal r16_DAY1) {
		R16_DAY1 = r16_DAY1;
	}
	public BigDecimal getR16_DAY2_7() {
		return R16_DAY2_7;
	}
	public void setR16_DAY2_7(BigDecimal r16_DAY2_7) {
		R16_DAY2_7 = r16_DAY2_7;
	}
	public BigDecimal getR16_DAY8_14() {
		return R16_DAY8_14;
	}
	public void setR16_DAY8_14(BigDecimal r16_DAY8_14) {
		R16_DAY8_14 = r16_DAY8_14;
	}
	public BigDecimal getR16_DAY15_30() {
		return R16_DAY15_30;
	}
	public void setR16_DAY15_30(BigDecimal r16_DAY15_30) {
		R16_DAY15_30 = r16_DAY15_30;
	}
	public BigDecimal getR16_DAY31_TO_2M() {
		return R16_DAY31_TO_2M;
	}
	public void setR16_DAY31_TO_2M(BigDecimal r16_DAY31_TO_2M) {
		R16_DAY31_TO_2M = r16_DAY31_TO_2M;
	}
	public BigDecimal getR16_MORE2M_TO_3M() {
		return R16_MORE2M_TO_3M;
	}
	public void setR16_MORE2M_TO_3M(BigDecimal r16_MORE2M_TO_3M) {
		R16_MORE2M_TO_3M = r16_MORE2M_TO_3M;
	}
	public BigDecimal getR16_OVER3M_TO_6M() {
		return R16_OVER3M_TO_6M;
	}
	public void setR16_OVER3M_TO_6M(BigDecimal r16_OVER3M_TO_6M) {
		R16_OVER3M_TO_6M = r16_OVER3M_TO_6M;
	}
	public BigDecimal getR16_OVER6M_TO_1Y() {
		return R16_OVER6M_TO_1Y;
	}
	public void setR16_OVER6M_TO_1Y(BigDecimal r16_OVER6M_TO_1Y) {
		R16_OVER6M_TO_1Y = r16_OVER6M_TO_1Y;
	}
	public BigDecimal getR16_OVER1Y_TO_3Y() {
		return R16_OVER1Y_TO_3Y;
	}
	public void setR16_OVER1Y_TO_3Y(BigDecimal r16_OVER1Y_TO_3Y) {
		R16_OVER1Y_TO_3Y = r16_OVER1Y_TO_3Y;
	}
	public BigDecimal getR16_OVER3Y_TO_5Y() {
		return R16_OVER3Y_TO_5Y;
	}
	public void setR16_OVER3Y_TO_5Y(BigDecimal r16_OVER3Y_TO_5Y) {
		R16_OVER3Y_TO_5Y = r16_OVER3Y_TO_5Y;
	}
	public BigDecimal getR16_OVER5Y() {
		return R16_OVER5Y;
	}
	public void setR16_OVER5Y(BigDecimal r16_OVER5Y) {
		R16_OVER5Y = r16_OVER5Y;
	}
	public BigDecimal getR16_TOTAL() {
		return R16_TOTAL;
	}
	public void setR16_TOTAL(BigDecimal r16_TOTAL) {
		R16_TOTAL = r16_TOTAL;
	}
	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}
	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}
	public BigDecimal getR17_DAY1() {
		return R17_DAY1;
	}
	public void setR17_DAY1(BigDecimal r17_DAY1) {
		R17_DAY1 = r17_DAY1;
	}
	public BigDecimal getR17_DAY2_7() {
		return R17_DAY2_7;
	}
	public void setR17_DAY2_7(BigDecimal r17_DAY2_7) {
		R17_DAY2_7 = r17_DAY2_7;
	}
	public BigDecimal getR17_DAY8_14() {
		return R17_DAY8_14;
	}
	public void setR17_DAY8_14(BigDecimal r17_DAY8_14) {
		R17_DAY8_14 = r17_DAY8_14;
	}
	public BigDecimal getR17_DAY15_30() {
		return R17_DAY15_30;
	}
	public void setR17_DAY15_30(BigDecimal r17_DAY15_30) {
		R17_DAY15_30 = r17_DAY15_30;
	}
	public BigDecimal getR17_DAY31_TO_2M() {
		return R17_DAY31_TO_2M;
	}
	public void setR17_DAY31_TO_2M(BigDecimal r17_DAY31_TO_2M) {
		R17_DAY31_TO_2M = r17_DAY31_TO_2M;
	}
	public BigDecimal getR17_MORE2M_TO_3M() {
		return R17_MORE2M_TO_3M;
	}
	public void setR17_MORE2M_TO_3M(BigDecimal r17_MORE2M_TO_3M) {
		R17_MORE2M_TO_3M = r17_MORE2M_TO_3M;
	}
	public BigDecimal getR17_OVER3M_TO_6M() {
		return R17_OVER3M_TO_6M;
	}
	public void setR17_OVER3M_TO_6M(BigDecimal r17_OVER3M_TO_6M) {
		R17_OVER3M_TO_6M = r17_OVER3M_TO_6M;
	}
	public BigDecimal getR17_OVER6M_TO_1Y() {
		return R17_OVER6M_TO_1Y;
	}
	public void setR17_OVER6M_TO_1Y(BigDecimal r17_OVER6M_TO_1Y) {
		R17_OVER6M_TO_1Y = r17_OVER6M_TO_1Y;
	}
	public BigDecimal getR17_OVER1Y_TO_3Y() {
		return R17_OVER1Y_TO_3Y;
	}
	public void setR17_OVER1Y_TO_3Y(BigDecimal r17_OVER1Y_TO_3Y) {
		R17_OVER1Y_TO_3Y = r17_OVER1Y_TO_3Y;
	}
	public BigDecimal getR17_OVER3Y_TO_5Y() {
		return R17_OVER3Y_TO_5Y;
	}
	public void setR17_OVER3Y_TO_5Y(BigDecimal r17_OVER3Y_TO_5Y) {
		R17_OVER3Y_TO_5Y = r17_OVER3Y_TO_5Y;
	}
	public BigDecimal getR17_OVER5Y() {
		return R17_OVER5Y;
	}
	public void setR17_OVER5Y(BigDecimal r17_OVER5Y) {
		R17_OVER5Y = r17_OVER5Y;
	}
	public BigDecimal getR17_TOTAL() {
		return R17_TOTAL;
	}
	public void setR17_TOTAL(BigDecimal r17_TOTAL) {
		R17_TOTAL = r17_TOTAL;
	}
	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}
	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}
	public BigDecimal getR18_DAY1() {
		return R18_DAY1;
	}
	public void setR18_DAY1(BigDecimal r18_DAY1) {
		R18_DAY1 = r18_DAY1;
	}
	public BigDecimal getR18_DAY2_7() {
		return R18_DAY2_7;
	}
	public void setR18_DAY2_7(BigDecimal r18_DAY2_7) {
		R18_DAY2_7 = r18_DAY2_7;
	}
	public BigDecimal getR18_DAY8_14() {
		return R18_DAY8_14;
	}
	public void setR18_DAY8_14(BigDecimal r18_DAY8_14) {
		R18_DAY8_14 = r18_DAY8_14;
	}
	public BigDecimal getR18_DAY15_30() {
		return R18_DAY15_30;
	}
	public void setR18_DAY15_30(BigDecimal r18_DAY15_30) {
		R18_DAY15_30 = r18_DAY15_30;
	}
	public BigDecimal getR18_DAY31_TO_2M() {
		return R18_DAY31_TO_2M;
	}
	public void setR18_DAY31_TO_2M(BigDecimal r18_DAY31_TO_2M) {
		R18_DAY31_TO_2M = r18_DAY31_TO_2M;
	}
	public BigDecimal getR18_MORE2M_TO_3M() {
		return R18_MORE2M_TO_3M;
	}
	public void setR18_MORE2M_TO_3M(BigDecimal r18_MORE2M_TO_3M) {
		R18_MORE2M_TO_3M = r18_MORE2M_TO_3M;
	}
	public BigDecimal getR18_OVER3M_TO_6M() {
		return R18_OVER3M_TO_6M;
	}
	public void setR18_OVER3M_TO_6M(BigDecimal r18_OVER3M_TO_6M) {
		R18_OVER3M_TO_6M = r18_OVER3M_TO_6M;
	}
	public BigDecimal getR18_OVER6M_TO_1Y() {
		return R18_OVER6M_TO_1Y;
	}
	public void setR18_OVER6M_TO_1Y(BigDecimal r18_OVER6M_TO_1Y) {
		R18_OVER6M_TO_1Y = r18_OVER6M_TO_1Y;
	}
	public BigDecimal getR18_OVER1Y_TO_3Y() {
		return R18_OVER1Y_TO_3Y;
	}
	public void setR18_OVER1Y_TO_3Y(BigDecimal r18_OVER1Y_TO_3Y) {
		R18_OVER1Y_TO_3Y = r18_OVER1Y_TO_3Y;
	}
	public BigDecimal getR18_OVER3Y_TO_5Y() {
		return R18_OVER3Y_TO_5Y;
	}
	public void setR18_OVER3Y_TO_5Y(BigDecimal r18_OVER3Y_TO_5Y) {
		R18_OVER3Y_TO_5Y = r18_OVER3Y_TO_5Y;
	}
	public BigDecimal getR18_OVER5Y() {
		return R18_OVER5Y;
	}
	public void setR18_OVER5Y(BigDecimal r18_OVER5Y) {
		R18_OVER5Y = r18_OVER5Y;
	}
	public BigDecimal getR18_TOTAL() {
		return R18_TOTAL;
	}
	public void setR18_TOTAL(BigDecimal r18_TOTAL) {
		R18_TOTAL = r18_TOTAL;
	}
	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}
	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}
	public BigDecimal getR19_DAY1() {
		return R19_DAY1;
	}
	public void setR19_DAY1(BigDecimal r19_DAY1) {
		R19_DAY1 = r19_DAY1;
	}
	public BigDecimal getR19_DAY2_7() {
		return R19_DAY2_7;
	}
	public void setR19_DAY2_7(BigDecimal r19_DAY2_7) {
		R19_DAY2_7 = r19_DAY2_7;
	}
	public BigDecimal getR19_DAY8_14() {
		return R19_DAY8_14;
	}
	public void setR19_DAY8_14(BigDecimal r19_DAY8_14) {
		R19_DAY8_14 = r19_DAY8_14;
	}
	public BigDecimal getR19_DAY15_30() {
		return R19_DAY15_30;
	}
	public void setR19_DAY15_30(BigDecimal r19_DAY15_30) {
		R19_DAY15_30 = r19_DAY15_30;
	}
	public BigDecimal getR19_DAY31_TO_2M() {
		return R19_DAY31_TO_2M;
	}
	public void setR19_DAY31_TO_2M(BigDecimal r19_DAY31_TO_2M) {
		R19_DAY31_TO_2M = r19_DAY31_TO_2M;
	}
	public BigDecimal getR19_MORE2M_TO_3M() {
		return R19_MORE2M_TO_3M;
	}
	public void setR19_MORE2M_TO_3M(BigDecimal r19_MORE2M_TO_3M) {
		R19_MORE2M_TO_3M = r19_MORE2M_TO_3M;
	}
	public BigDecimal getR19_OVER3M_TO_6M() {
		return R19_OVER3M_TO_6M;
	}
	public void setR19_OVER3M_TO_6M(BigDecimal r19_OVER3M_TO_6M) {
		R19_OVER3M_TO_6M = r19_OVER3M_TO_6M;
	}
	public BigDecimal getR19_OVER6M_TO_1Y() {
		return R19_OVER6M_TO_1Y;
	}
	public void setR19_OVER6M_TO_1Y(BigDecimal r19_OVER6M_TO_1Y) {
		R19_OVER6M_TO_1Y = r19_OVER6M_TO_1Y;
	}
	public BigDecimal getR19_OVER1Y_TO_3Y() {
		return R19_OVER1Y_TO_3Y;
	}
	public void setR19_OVER1Y_TO_3Y(BigDecimal r19_OVER1Y_TO_3Y) {
		R19_OVER1Y_TO_3Y = r19_OVER1Y_TO_3Y;
	}
	public BigDecimal getR19_OVER3Y_TO_5Y() {
		return R19_OVER3Y_TO_5Y;
	}
	public void setR19_OVER3Y_TO_5Y(BigDecimal r19_OVER3Y_TO_5Y) {
		R19_OVER3Y_TO_5Y = r19_OVER3Y_TO_5Y;
	}
	public BigDecimal getR19_OVER5Y() {
		return R19_OVER5Y;
	}
	public void setR19_OVER5Y(BigDecimal r19_OVER5Y) {
		R19_OVER5Y = r19_OVER5Y;
	}
	public BigDecimal getR19_TOTAL() {
		return R19_TOTAL;
	}
	public void setR19_TOTAL(BigDecimal r19_TOTAL) {
		R19_TOTAL = r19_TOTAL;
	}
	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}
	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}
	public BigDecimal getR20_DAY1() {
		return R20_DAY1;
	}
	public void setR20_DAY1(BigDecimal r20_DAY1) {
		R20_DAY1 = r20_DAY1;
	}
	public BigDecimal getR20_DAY2_7() {
		return R20_DAY2_7;
	}
	public void setR20_DAY2_7(BigDecimal r20_DAY2_7) {
		R20_DAY2_7 = r20_DAY2_7;
	}
	public BigDecimal getR20_DAY8_14() {
		return R20_DAY8_14;
	}
	public void setR20_DAY8_14(BigDecimal r20_DAY8_14) {
		R20_DAY8_14 = r20_DAY8_14;
	}
	public BigDecimal getR20_DAY15_30() {
		return R20_DAY15_30;
	}
	public void setR20_DAY15_30(BigDecimal r20_DAY15_30) {
		R20_DAY15_30 = r20_DAY15_30;
	}
	public BigDecimal getR20_DAY31_TO_2M() {
		return R20_DAY31_TO_2M;
	}
	public void setR20_DAY31_TO_2M(BigDecimal r20_DAY31_TO_2M) {
		R20_DAY31_TO_2M = r20_DAY31_TO_2M;
	}
	public BigDecimal getR20_MORE2M_TO_3M() {
		return R20_MORE2M_TO_3M;
	}
	public void setR20_MORE2M_TO_3M(BigDecimal r20_MORE2M_TO_3M) {
		R20_MORE2M_TO_3M = r20_MORE2M_TO_3M;
	}
	public BigDecimal getR20_OVER3M_TO_6M() {
		return R20_OVER3M_TO_6M;
	}
	public void setR20_OVER3M_TO_6M(BigDecimal r20_OVER3M_TO_6M) {
		R20_OVER3M_TO_6M = r20_OVER3M_TO_6M;
	}
	public BigDecimal getR20_OVER6M_TO_1Y() {
		return R20_OVER6M_TO_1Y;
	}
	public void setR20_OVER6M_TO_1Y(BigDecimal r20_OVER6M_TO_1Y) {
		R20_OVER6M_TO_1Y = r20_OVER6M_TO_1Y;
	}
	public BigDecimal getR20_OVER1Y_TO_3Y() {
		return R20_OVER1Y_TO_3Y;
	}
	public void setR20_OVER1Y_TO_3Y(BigDecimal r20_OVER1Y_TO_3Y) {
		R20_OVER1Y_TO_3Y = r20_OVER1Y_TO_3Y;
	}
	public BigDecimal getR20_OVER3Y_TO_5Y() {
		return R20_OVER3Y_TO_5Y;
	}
	public void setR20_OVER3Y_TO_5Y(BigDecimal r20_OVER3Y_TO_5Y) {
		R20_OVER3Y_TO_5Y = r20_OVER3Y_TO_5Y;
	}
	public BigDecimal getR20_OVER5Y() {
		return R20_OVER5Y;
	}
	public void setR20_OVER5Y(BigDecimal r20_OVER5Y) {
		R20_OVER5Y = r20_OVER5Y;
	}
	public BigDecimal getR20_TOTAL() {
		return R20_TOTAL;
	}
	public void setR20_TOTAL(BigDecimal r20_TOTAL) {
		R20_TOTAL = r20_TOTAL;
	}
	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}
	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}
	public BigDecimal getR21_DAY1() {
		return R21_DAY1;
	}
	public void setR21_DAY1(BigDecimal r21_DAY1) {
		R21_DAY1 = r21_DAY1;
	}
	public BigDecimal getR21_DAY2_7() {
		return R21_DAY2_7;
	}
	public void setR21_DAY2_7(BigDecimal r21_DAY2_7) {
		R21_DAY2_7 = r21_DAY2_7;
	}
	public BigDecimal getR21_DAY8_14() {
		return R21_DAY8_14;
	}
	public void setR21_DAY8_14(BigDecimal r21_DAY8_14) {
		R21_DAY8_14 = r21_DAY8_14;
	}
	public BigDecimal getR21_DAY15_30() {
		return R21_DAY15_30;
	}
	public void setR21_DAY15_30(BigDecimal r21_DAY15_30) {
		R21_DAY15_30 = r21_DAY15_30;
	}
	public BigDecimal getR21_DAY31_TO_2M() {
		return R21_DAY31_TO_2M;
	}
	public void setR21_DAY31_TO_2M(BigDecimal r21_DAY31_TO_2M) {
		R21_DAY31_TO_2M = r21_DAY31_TO_2M;
	}
	public BigDecimal getR21_MORE2M_TO_3M() {
		return R21_MORE2M_TO_3M;
	}
	public void setR21_MORE2M_TO_3M(BigDecimal r21_MORE2M_TO_3M) {
		R21_MORE2M_TO_3M = r21_MORE2M_TO_3M;
	}
	public BigDecimal getR21_OVER3M_TO_6M() {
		return R21_OVER3M_TO_6M;
	}
	public void setR21_OVER3M_TO_6M(BigDecimal r21_OVER3M_TO_6M) {
		R21_OVER3M_TO_6M = r21_OVER3M_TO_6M;
	}
	public BigDecimal getR21_OVER6M_TO_1Y() {
		return R21_OVER6M_TO_1Y;
	}
	public void setR21_OVER6M_TO_1Y(BigDecimal r21_OVER6M_TO_1Y) {
		R21_OVER6M_TO_1Y = r21_OVER6M_TO_1Y;
	}
	public BigDecimal getR21_OVER1Y_TO_3Y() {
		return R21_OVER1Y_TO_3Y;
	}
	public void setR21_OVER1Y_TO_3Y(BigDecimal r21_OVER1Y_TO_3Y) {
		R21_OVER1Y_TO_3Y = r21_OVER1Y_TO_3Y;
	}
	public BigDecimal getR21_OVER3Y_TO_5Y() {
		return R21_OVER3Y_TO_5Y;
	}
	public void setR21_OVER3Y_TO_5Y(BigDecimal r21_OVER3Y_TO_5Y) {
		R21_OVER3Y_TO_5Y = r21_OVER3Y_TO_5Y;
	}
	public BigDecimal getR21_OVER5Y() {
		return R21_OVER5Y;
	}
	public void setR21_OVER5Y(BigDecimal r21_OVER5Y) {
		R21_OVER5Y = r21_OVER5Y;
	}
	public BigDecimal getR21_TOTAL() {
		return R21_TOTAL;
	}
	public void setR21_TOTAL(BigDecimal r21_TOTAL) {
		R21_TOTAL = r21_TOTAL;
	}
	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}
	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}
	public BigDecimal getR22_DAY1() {
		return R22_DAY1;
	}
	public void setR22_DAY1(BigDecimal r22_DAY1) {
		R22_DAY1 = r22_DAY1;
	}
	public BigDecimal getR22_DAY2_7() {
		return R22_DAY2_7;
	}
	public void setR22_DAY2_7(BigDecimal r22_DAY2_7) {
		R22_DAY2_7 = r22_DAY2_7;
	}
	public BigDecimal getR22_DAY8_14() {
		return R22_DAY8_14;
	}
	public void setR22_DAY8_14(BigDecimal r22_DAY8_14) {
		R22_DAY8_14 = r22_DAY8_14;
	}
	public BigDecimal getR22_DAY15_30() {
		return R22_DAY15_30;
	}
	public void setR22_DAY15_30(BigDecimal r22_DAY15_30) {
		R22_DAY15_30 = r22_DAY15_30;
	}
	public BigDecimal getR22_DAY31_TO_2M() {
		return R22_DAY31_TO_2M;
	}
	public void setR22_DAY31_TO_2M(BigDecimal r22_DAY31_TO_2M) {
		R22_DAY31_TO_2M = r22_DAY31_TO_2M;
	}
	public BigDecimal getR22_MORE2M_TO_3M() {
		return R22_MORE2M_TO_3M;
	}
	public void setR22_MORE2M_TO_3M(BigDecimal r22_MORE2M_TO_3M) {
		R22_MORE2M_TO_3M = r22_MORE2M_TO_3M;
	}
	public BigDecimal getR22_OVER3M_TO_6M() {
		return R22_OVER3M_TO_6M;
	}
	public void setR22_OVER3M_TO_6M(BigDecimal r22_OVER3M_TO_6M) {
		R22_OVER3M_TO_6M = r22_OVER3M_TO_6M;
	}
	public BigDecimal getR22_OVER6M_TO_1Y() {
		return R22_OVER6M_TO_1Y;
	}
	public void setR22_OVER6M_TO_1Y(BigDecimal r22_OVER6M_TO_1Y) {
		R22_OVER6M_TO_1Y = r22_OVER6M_TO_1Y;
	}
	public BigDecimal getR22_OVER1Y_TO_3Y() {
		return R22_OVER1Y_TO_3Y;
	}
	public void setR22_OVER1Y_TO_3Y(BigDecimal r22_OVER1Y_TO_3Y) {
		R22_OVER1Y_TO_3Y = r22_OVER1Y_TO_3Y;
	}
	public BigDecimal getR22_OVER3Y_TO_5Y() {
		return R22_OVER3Y_TO_5Y;
	}
	public void setR22_OVER3Y_TO_5Y(BigDecimal r22_OVER3Y_TO_5Y) {
		R22_OVER3Y_TO_5Y = r22_OVER3Y_TO_5Y;
	}
	public BigDecimal getR22_OVER5Y() {
		return R22_OVER5Y;
	}
	public void setR22_OVER5Y(BigDecimal r22_OVER5Y) {
		R22_OVER5Y = r22_OVER5Y;
	}
	public BigDecimal getR22_TOTAL() {
		return R22_TOTAL;
	}
	public void setR22_TOTAL(BigDecimal r22_TOTAL) {
		R22_TOTAL = r22_TOTAL;
	}
	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}
	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}
	public BigDecimal getR23_DAY1() {
		return R23_DAY1;
	}
	public void setR23_DAY1(BigDecimal r23_DAY1) {
		R23_DAY1 = r23_DAY1;
	}
	public BigDecimal getR23_DAY2_7() {
		return R23_DAY2_7;
	}
	public void setR23_DAY2_7(BigDecimal r23_DAY2_7) {
		R23_DAY2_7 = r23_DAY2_7;
	}
	public BigDecimal getR23_DAY8_14() {
		return R23_DAY8_14;
	}
	public void setR23_DAY8_14(BigDecimal r23_DAY8_14) {
		R23_DAY8_14 = r23_DAY8_14;
	}
	public BigDecimal getR23_DAY15_30() {
		return R23_DAY15_30;
	}
	public void setR23_DAY15_30(BigDecimal r23_DAY15_30) {
		R23_DAY15_30 = r23_DAY15_30;
	}
	public BigDecimal getR23_DAY31_TO_2M() {
		return R23_DAY31_TO_2M;
	}
	public void setR23_DAY31_TO_2M(BigDecimal r23_DAY31_TO_2M) {
		R23_DAY31_TO_2M = r23_DAY31_TO_2M;
	}
	public BigDecimal getR23_MORE2M_TO_3M() {
		return R23_MORE2M_TO_3M;
	}
	public void setR23_MORE2M_TO_3M(BigDecimal r23_MORE2M_TO_3M) {
		R23_MORE2M_TO_3M = r23_MORE2M_TO_3M;
	}
	public BigDecimal getR23_OVER3M_TO_6M() {
		return R23_OVER3M_TO_6M;
	}
	public void setR23_OVER3M_TO_6M(BigDecimal r23_OVER3M_TO_6M) {
		R23_OVER3M_TO_6M = r23_OVER3M_TO_6M;
	}
	public BigDecimal getR23_OVER6M_TO_1Y() {
		return R23_OVER6M_TO_1Y;
	}
	public void setR23_OVER6M_TO_1Y(BigDecimal r23_OVER6M_TO_1Y) {
		R23_OVER6M_TO_1Y = r23_OVER6M_TO_1Y;
	}
	public BigDecimal getR23_OVER1Y_TO_3Y() {
		return R23_OVER1Y_TO_3Y;
	}
	public void setR23_OVER1Y_TO_3Y(BigDecimal r23_OVER1Y_TO_3Y) {
		R23_OVER1Y_TO_3Y = r23_OVER1Y_TO_3Y;
	}
	public BigDecimal getR23_OVER3Y_TO_5Y() {
		return R23_OVER3Y_TO_5Y;
	}
	public void setR23_OVER3Y_TO_5Y(BigDecimal r23_OVER3Y_TO_5Y) {
		R23_OVER3Y_TO_5Y = r23_OVER3Y_TO_5Y;
	}
	public BigDecimal getR23_OVER5Y() {
		return R23_OVER5Y;
	}
	public void setR23_OVER5Y(BigDecimal r23_OVER5Y) {
		R23_OVER5Y = r23_OVER5Y;
	}
	public BigDecimal getR23_TOTAL() {
		return R23_TOTAL;
	}
	public void setR23_TOTAL(BigDecimal r23_TOTAL) {
		R23_TOTAL = r23_TOTAL;
	}
	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}
	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}
	public BigDecimal getR24_DAY1() {
		return R24_DAY1;
	}
	public void setR24_DAY1(BigDecimal r24_DAY1) {
		R24_DAY1 = r24_DAY1;
	}
	public BigDecimal getR24_DAY2_7() {
		return R24_DAY2_7;
	}
	public void setR24_DAY2_7(BigDecimal r24_DAY2_7) {
		R24_DAY2_7 = r24_DAY2_7;
	}
	public BigDecimal getR24_DAY8_14() {
		return R24_DAY8_14;
	}
	public void setR24_DAY8_14(BigDecimal r24_DAY8_14) {
		R24_DAY8_14 = r24_DAY8_14;
	}
	public BigDecimal getR24_DAY15_30() {
		return R24_DAY15_30;
	}
	public void setR24_DAY15_30(BigDecimal r24_DAY15_30) {
		R24_DAY15_30 = r24_DAY15_30;
	}
	public BigDecimal getR24_DAY31_TO_2M() {
		return R24_DAY31_TO_2M;
	}
	public void setR24_DAY31_TO_2M(BigDecimal r24_DAY31_TO_2M) {
		R24_DAY31_TO_2M = r24_DAY31_TO_2M;
	}
	public BigDecimal getR24_MORE2M_TO_3M() {
		return R24_MORE2M_TO_3M;
	}
	public void setR24_MORE2M_TO_3M(BigDecimal r24_MORE2M_TO_3M) {
		R24_MORE2M_TO_3M = r24_MORE2M_TO_3M;
	}
	public BigDecimal getR24_OVER3M_TO_6M() {
		return R24_OVER3M_TO_6M;
	}
	public void setR24_OVER3M_TO_6M(BigDecimal r24_OVER3M_TO_6M) {
		R24_OVER3M_TO_6M = r24_OVER3M_TO_6M;
	}
	public BigDecimal getR24_OVER6M_TO_1Y() {
		return R24_OVER6M_TO_1Y;
	}
	public void setR24_OVER6M_TO_1Y(BigDecimal r24_OVER6M_TO_1Y) {
		R24_OVER6M_TO_1Y = r24_OVER6M_TO_1Y;
	}
	public BigDecimal getR24_OVER1Y_TO_3Y() {
		return R24_OVER1Y_TO_3Y;
	}
	public void setR24_OVER1Y_TO_3Y(BigDecimal r24_OVER1Y_TO_3Y) {
		R24_OVER1Y_TO_3Y = r24_OVER1Y_TO_3Y;
	}
	public BigDecimal getR24_OVER3Y_TO_5Y() {
		return R24_OVER3Y_TO_5Y;
	}
	public void setR24_OVER3Y_TO_5Y(BigDecimal r24_OVER3Y_TO_5Y) {
		R24_OVER3Y_TO_5Y = r24_OVER3Y_TO_5Y;
	}
	public BigDecimal getR24_OVER5Y() {
		return R24_OVER5Y;
	}
	public void setR24_OVER5Y(BigDecimal r24_OVER5Y) {
		R24_OVER5Y = r24_OVER5Y;
	}
	public BigDecimal getR24_TOTAL() {
		return R24_TOTAL;
	}
	public void setR24_TOTAL(BigDecimal r24_TOTAL) {
		R24_TOTAL = r24_TOTAL;
	}
	public String getR25_PRODUCT() {
		return R25_PRODUCT;
	}
	public void setR25_PRODUCT(String r25_PRODUCT) {
		R25_PRODUCT = r25_PRODUCT;
	}
	public BigDecimal getR25_DAY1() {
		return R25_DAY1;
	}
	public void setR25_DAY1(BigDecimal r25_DAY1) {
		R25_DAY1 = r25_DAY1;
	}
	public BigDecimal getR25_DAY2_7() {
		return R25_DAY2_7;
	}
	public void setR25_DAY2_7(BigDecimal r25_DAY2_7) {
		R25_DAY2_7 = r25_DAY2_7;
	}
	public BigDecimal getR25_DAY8_14() {
		return R25_DAY8_14;
	}
	public void setR25_DAY8_14(BigDecimal r25_DAY8_14) {
		R25_DAY8_14 = r25_DAY8_14;
	}
	public BigDecimal getR25_DAY15_30() {
		return R25_DAY15_30;
	}
	public void setR25_DAY15_30(BigDecimal r25_DAY15_30) {
		R25_DAY15_30 = r25_DAY15_30;
	}
	public BigDecimal getR25_DAY31_TO_2M() {
		return R25_DAY31_TO_2M;
	}
	public void setR25_DAY31_TO_2M(BigDecimal r25_DAY31_TO_2M) {
		R25_DAY31_TO_2M = r25_DAY31_TO_2M;
	}
	public BigDecimal getR25_MORE2M_TO_3M() {
		return R25_MORE2M_TO_3M;
	}
	public void setR25_MORE2M_TO_3M(BigDecimal r25_MORE2M_TO_3M) {
		R25_MORE2M_TO_3M = r25_MORE2M_TO_3M;
	}
	public BigDecimal getR25_OVER3M_TO_6M() {
		return R25_OVER3M_TO_6M;
	}
	public void setR25_OVER3M_TO_6M(BigDecimal r25_OVER3M_TO_6M) {
		R25_OVER3M_TO_6M = r25_OVER3M_TO_6M;
	}
	public BigDecimal getR25_OVER6M_TO_1Y() {
		return R25_OVER6M_TO_1Y;
	}
	public void setR25_OVER6M_TO_1Y(BigDecimal r25_OVER6M_TO_1Y) {
		R25_OVER6M_TO_1Y = r25_OVER6M_TO_1Y;
	}
	public BigDecimal getR25_OVER1Y_TO_3Y() {
		return R25_OVER1Y_TO_3Y;
	}
	public void setR25_OVER1Y_TO_3Y(BigDecimal r25_OVER1Y_TO_3Y) {
		R25_OVER1Y_TO_3Y = r25_OVER1Y_TO_3Y;
	}
	public BigDecimal getR25_OVER3Y_TO_5Y() {
		return R25_OVER3Y_TO_5Y;
	}
	public void setR25_OVER3Y_TO_5Y(BigDecimal r25_OVER3Y_TO_5Y) {
		R25_OVER3Y_TO_5Y = r25_OVER3Y_TO_5Y;
	}
	public BigDecimal getR25_OVER5Y() {
		return R25_OVER5Y;
	}
	public void setR25_OVER5Y(BigDecimal r25_OVER5Y) {
		R25_OVER5Y = r25_OVER5Y;
	}
	public BigDecimal getR25_TOTAL() {
		return R25_TOTAL;
	}
	public void setR25_TOTAL(BigDecimal r25_TOTAL) {
		R25_TOTAL = r25_TOTAL;
	}
	public String getR26_PRODUCT() {
		return R26_PRODUCT;
	}
	public void setR26_PRODUCT(String r26_PRODUCT) {
		R26_PRODUCT = r26_PRODUCT;
	}
	public BigDecimal getR26_DAY1() {
		return R26_DAY1;
	}
	public void setR26_DAY1(BigDecimal r26_DAY1) {
		R26_DAY1 = r26_DAY1;
	}
	public BigDecimal getR26_DAY2_7() {
		return R26_DAY2_7;
	}
	public void setR26_DAY2_7(BigDecimal r26_DAY2_7) {
		R26_DAY2_7 = r26_DAY2_7;
	}
	public BigDecimal getR26_DAY8_14() {
		return R26_DAY8_14;
	}
	public void setR26_DAY8_14(BigDecimal r26_DAY8_14) {
		R26_DAY8_14 = r26_DAY8_14;
	}
	public BigDecimal getR26_DAY15_30() {
		return R26_DAY15_30;
	}
	public void setR26_DAY15_30(BigDecimal r26_DAY15_30) {
		R26_DAY15_30 = r26_DAY15_30;
	}
	public BigDecimal getR26_DAY31_TO_2M() {
		return R26_DAY31_TO_2M;
	}
	public void setR26_DAY31_TO_2M(BigDecimal r26_DAY31_TO_2M) {
		R26_DAY31_TO_2M = r26_DAY31_TO_2M;
	}
	public BigDecimal getR26_MORE2M_TO_3M() {
		return R26_MORE2M_TO_3M;
	}
	public void setR26_MORE2M_TO_3M(BigDecimal r26_MORE2M_TO_3M) {
		R26_MORE2M_TO_3M = r26_MORE2M_TO_3M;
	}
	public BigDecimal getR26_OVER3M_TO_6M() {
		return R26_OVER3M_TO_6M;
	}
	public void setR26_OVER3M_TO_6M(BigDecimal r26_OVER3M_TO_6M) {
		R26_OVER3M_TO_6M = r26_OVER3M_TO_6M;
	}
	public BigDecimal getR26_OVER6M_TO_1Y() {
		return R26_OVER6M_TO_1Y;
	}
	public void setR26_OVER6M_TO_1Y(BigDecimal r26_OVER6M_TO_1Y) {
		R26_OVER6M_TO_1Y = r26_OVER6M_TO_1Y;
	}
	public BigDecimal getR26_OVER1Y_TO_3Y() {
		return R26_OVER1Y_TO_3Y;
	}
	public void setR26_OVER1Y_TO_3Y(BigDecimal r26_OVER1Y_TO_3Y) {
		R26_OVER1Y_TO_3Y = r26_OVER1Y_TO_3Y;
	}
	public BigDecimal getR26_OVER3Y_TO_5Y() {
		return R26_OVER3Y_TO_5Y;
	}
	public void setR26_OVER3Y_TO_5Y(BigDecimal r26_OVER3Y_TO_5Y) {
		R26_OVER3Y_TO_5Y = r26_OVER3Y_TO_5Y;
	}
	public BigDecimal getR26_OVER5Y() {
		return R26_OVER5Y;
	}
	public void setR26_OVER5Y(BigDecimal r26_OVER5Y) {
		R26_OVER5Y = r26_OVER5Y;
	}
	public BigDecimal getR26_TOTAL() {
		return R26_TOTAL;
	}
	public void setR26_TOTAL(BigDecimal r26_TOTAL) {
		R26_TOTAL = r26_TOTAL;
	}
	public String getR27_PRODUCT() {
		return R27_PRODUCT;
	}
	public void setR27_PRODUCT(String r27_PRODUCT) {
		R27_PRODUCT = r27_PRODUCT;
	}
	public BigDecimal getR27_DAY1() {
		return R27_DAY1;
	}
	public void setR27_DAY1(BigDecimal r27_DAY1) {
		R27_DAY1 = r27_DAY1;
	}
	public BigDecimal getR27_DAY2_7() {
		return R27_DAY2_7;
	}
	public void setR27_DAY2_7(BigDecimal r27_DAY2_7) {
		R27_DAY2_7 = r27_DAY2_7;
	}
	public BigDecimal getR27_DAY8_14() {
		return R27_DAY8_14;
	}
	public void setR27_DAY8_14(BigDecimal r27_DAY8_14) {
		R27_DAY8_14 = r27_DAY8_14;
	}
	public BigDecimal getR27_DAY15_30() {
		return R27_DAY15_30;
	}
	public void setR27_DAY15_30(BigDecimal r27_DAY15_30) {
		R27_DAY15_30 = r27_DAY15_30;
	}
	public BigDecimal getR27_DAY31_TO_2M() {
		return R27_DAY31_TO_2M;
	}
	public void setR27_DAY31_TO_2M(BigDecimal r27_DAY31_TO_2M) {
		R27_DAY31_TO_2M = r27_DAY31_TO_2M;
	}
	public BigDecimal getR27_MORE2M_TO_3M() {
		return R27_MORE2M_TO_3M;
	}
	public void setR27_MORE2M_TO_3M(BigDecimal r27_MORE2M_TO_3M) {
		R27_MORE2M_TO_3M = r27_MORE2M_TO_3M;
	}
	public BigDecimal getR27_OVER3M_TO_6M() {
		return R27_OVER3M_TO_6M;
	}
	public void setR27_OVER3M_TO_6M(BigDecimal r27_OVER3M_TO_6M) {
		R27_OVER3M_TO_6M = r27_OVER3M_TO_6M;
	}
	public BigDecimal getR27_OVER6M_TO_1Y() {
		return R27_OVER6M_TO_1Y;
	}
	public void setR27_OVER6M_TO_1Y(BigDecimal r27_OVER6M_TO_1Y) {
		R27_OVER6M_TO_1Y = r27_OVER6M_TO_1Y;
	}
	public BigDecimal getR27_OVER1Y_TO_3Y() {
		return R27_OVER1Y_TO_3Y;
	}
	public void setR27_OVER1Y_TO_3Y(BigDecimal r27_OVER1Y_TO_3Y) {
		R27_OVER1Y_TO_3Y = r27_OVER1Y_TO_3Y;
	}
	public BigDecimal getR27_OVER3Y_TO_5Y() {
		return R27_OVER3Y_TO_5Y;
	}
	public void setR27_OVER3Y_TO_5Y(BigDecimal r27_OVER3Y_TO_5Y) {
		R27_OVER3Y_TO_5Y = r27_OVER3Y_TO_5Y;
	}
	public BigDecimal getR27_OVER5Y() {
		return R27_OVER5Y;
	}
	public void setR27_OVER5Y(BigDecimal r27_OVER5Y) {
		R27_OVER5Y = r27_OVER5Y;
	}
	public BigDecimal getR27_TOTAL() {
		return R27_TOTAL;
	}
	public void setR27_TOTAL(BigDecimal r27_TOTAL) {
		R27_TOTAL = r27_TOTAL;
	}
	public String getR28_PRODUCT() {
		return R28_PRODUCT;
	}
	public void setR28_PRODUCT(String r28_PRODUCT) {
		R28_PRODUCT = r28_PRODUCT;
	}
	public BigDecimal getR28_DAY1() {
		return R28_DAY1;
	}
	public void setR28_DAY1(BigDecimal r28_DAY1) {
		R28_DAY1 = r28_DAY1;
	}
	public BigDecimal getR28_DAY2_7() {
		return R28_DAY2_7;
	}
	public void setR28_DAY2_7(BigDecimal r28_DAY2_7) {
		R28_DAY2_7 = r28_DAY2_7;
	}
	public BigDecimal getR28_DAY8_14() {
		return R28_DAY8_14;
	}
	public void setR28_DAY8_14(BigDecimal r28_DAY8_14) {
		R28_DAY8_14 = r28_DAY8_14;
	}
	public BigDecimal getR28_DAY15_30() {
		return R28_DAY15_30;
	}
	public void setR28_DAY15_30(BigDecimal r28_DAY15_30) {
		R28_DAY15_30 = r28_DAY15_30;
	}
	public BigDecimal getR28_DAY31_TO_2M() {
		return R28_DAY31_TO_2M;
	}
	public void setR28_DAY31_TO_2M(BigDecimal r28_DAY31_TO_2M) {
		R28_DAY31_TO_2M = r28_DAY31_TO_2M;
	}
	public BigDecimal getR28_MORE2M_TO_3M() {
		return R28_MORE2M_TO_3M;
	}
	public void setR28_MORE2M_TO_3M(BigDecimal r28_MORE2M_TO_3M) {
		R28_MORE2M_TO_3M = r28_MORE2M_TO_3M;
	}
	public BigDecimal getR28_OVER3M_TO_6M() {
		return R28_OVER3M_TO_6M;
	}
	public void setR28_OVER3M_TO_6M(BigDecimal r28_OVER3M_TO_6M) {
		R28_OVER3M_TO_6M = r28_OVER3M_TO_6M;
	}
	public BigDecimal getR28_OVER6M_TO_1Y() {
		return R28_OVER6M_TO_1Y;
	}
	public void setR28_OVER6M_TO_1Y(BigDecimal r28_OVER6M_TO_1Y) {
		R28_OVER6M_TO_1Y = r28_OVER6M_TO_1Y;
	}
	public BigDecimal getR28_OVER1Y_TO_3Y() {
		return R28_OVER1Y_TO_3Y;
	}
	public void setR28_OVER1Y_TO_3Y(BigDecimal r28_OVER1Y_TO_3Y) {
		R28_OVER1Y_TO_3Y = r28_OVER1Y_TO_3Y;
	}
	public BigDecimal getR28_OVER3Y_TO_5Y() {
		return R28_OVER3Y_TO_5Y;
	}
	public void setR28_OVER3Y_TO_5Y(BigDecimal r28_OVER3Y_TO_5Y) {
		R28_OVER3Y_TO_5Y = r28_OVER3Y_TO_5Y;
	}
	public BigDecimal getR28_OVER5Y() {
		return R28_OVER5Y;
	}
	public void setR28_OVER5Y(BigDecimal r28_OVER5Y) {
		R28_OVER5Y = r28_OVER5Y;
	}
	public BigDecimal getR28_TOTAL() {
		return R28_TOTAL;
	}
	public void setR28_TOTAL(BigDecimal r28_TOTAL) {
		R28_TOTAL = r28_TOTAL;
	}
	public String getR29_PRODUCT() {
		return R29_PRODUCT;
	}
	public void setR29_PRODUCT(String r29_PRODUCT) {
		R29_PRODUCT = r29_PRODUCT;
	}
	public BigDecimal getR29_DAY1() {
		return R29_DAY1;
	}
	public void setR29_DAY1(BigDecimal r29_DAY1) {
		R29_DAY1 = r29_DAY1;
	}
	public BigDecimal getR29_DAY2_7() {
		return R29_DAY2_7;
	}
	public void setR29_DAY2_7(BigDecimal r29_DAY2_7) {
		R29_DAY2_7 = r29_DAY2_7;
	}
	public BigDecimal getR29_DAY8_14() {
		return R29_DAY8_14;
	}
	public void setR29_DAY8_14(BigDecimal r29_DAY8_14) {
		R29_DAY8_14 = r29_DAY8_14;
	}
	public BigDecimal getR29_DAY15_30() {
		return R29_DAY15_30;
	}
	public void setR29_DAY15_30(BigDecimal r29_DAY15_30) {
		R29_DAY15_30 = r29_DAY15_30;
	}
	public BigDecimal getR29_DAY31_TO_2M() {
		return R29_DAY31_TO_2M;
	}
	public void setR29_DAY31_TO_2M(BigDecimal r29_DAY31_TO_2M) {
		R29_DAY31_TO_2M = r29_DAY31_TO_2M;
	}
	public BigDecimal getR29_MORE2M_TO_3M() {
		return R29_MORE2M_TO_3M;
	}
	public void setR29_MORE2M_TO_3M(BigDecimal r29_MORE2M_TO_3M) {
		R29_MORE2M_TO_3M = r29_MORE2M_TO_3M;
	}
	public BigDecimal getR29_OVER3M_TO_6M() {
		return R29_OVER3M_TO_6M;
	}
	public void setR29_OVER3M_TO_6M(BigDecimal r29_OVER3M_TO_6M) {
		R29_OVER3M_TO_6M = r29_OVER3M_TO_6M;
	}
	public BigDecimal getR29_OVER6M_TO_1Y() {
		return R29_OVER6M_TO_1Y;
	}
	public void setR29_OVER6M_TO_1Y(BigDecimal r29_OVER6M_TO_1Y) {
		R29_OVER6M_TO_1Y = r29_OVER6M_TO_1Y;
	}
	public BigDecimal getR29_OVER1Y_TO_3Y() {
		return R29_OVER1Y_TO_3Y;
	}
	public void setR29_OVER1Y_TO_3Y(BigDecimal r29_OVER1Y_TO_3Y) {
		R29_OVER1Y_TO_3Y = r29_OVER1Y_TO_3Y;
	}
	public BigDecimal getR29_OVER3Y_TO_5Y() {
		return R29_OVER3Y_TO_5Y;
	}
	public void setR29_OVER3Y_TO_5Y(BigDecimal r29_OVER3Y_TO_5Y) {
		R29_OVER3Y_TO_5Y = r29_OVER3Y_TO_5Y;
	}
	public BigDecimal getR29_OVER5Y() {
		return R29_OVER5Y;
	}
	public void setR29_OVER5Y(BigDecimal r29_OVER5Y) {
		R29_OVER5Y = r29_OVER5Y;
	}
	public BigDecimal getR29_TOTAL() {
		return R29_TOTAL;
	}
	public void setR29_TOTAL(BigDecimal r29_TOTAL) {
		R29_TOTAL = r29_TOTAL;
	}
	public String getR30_PRODUCT() {
		return R30_PRODUCT;
	}
	public void setR30_PRODUCT(String r30_PRODUCT) {
		R30_PRODUCT = r30_PRODUCT;
	}
	public BigDecimal getR30_DAY1() {
		return R30_DAY1;
	}
	public void setR30_DAY1(BigDecimal r30_DAY1) {
		R30_DAY1 = r30_DAY1;
	}
	public BigDecimal getR30_DAY2_7() {
		return R30_DAY2_7;
	}
	public void setR30_DAY2_7(BigDecimal r30_DAY2_7) {
		R30_DAY2_7 = r30_DAY2_7;
	}
	public BigDecimal getR30_DAY8_14() {
		return R30_DAY8_14;
	}
	public void setR30_DAY8_14(BigDecimal r30_DAY8_14) {
		R30_DAY8_14 = r30_DAY8_14;
	}
	public BigDecimal getR30_DAY15_30() {
		return R30_DAY15_30;
	}
	public void setR30_DAY15_30(BigDecimal r30_DAY15_30) {
		R30_DAY15_30 = r30_DAY15_30;
	}
	public BigDecimal getR30_DAY31_TO_2M() {
		return R30_DAY31_TO_2M;
	}
	public void setR30_DAY31_TO_2M(BigDecimal r30_DAY31_TO_2M) {
		R30_DAY31_TO_2M = r30_DAY31_TO_2M;
	}
	public BigDecimal getR30_MORE2M_TO_3M() {
		return R30_MORE2M_TO_3M;
	}
	public void setR30_MORE2M_TO_3M(BigDecimal r30_MORE2M_TO_3M) {
		R30_MORE2M_TO_3M = r30_MORE2M_TO_3M;
	}
	public BigDecimal getR30_OVER3M_TO_6M() {
		return R30_OVER3M_TO_6M;
	}
	public void setR30_OVER3M_TO_6M(BigDecimal r30_OVER3M_TO_6M) {
		R30_OVER3M_TO_6M = r30_OVER3M_TO_6M;
	}
	public BigDecimal getR30_OVER6M_TO_1Y() {
		return R30_OVER6M_TO_1Y;
	}
	public void setR30_OVER6M_TO_1Y(BigDecimal r30_OVER6M_TO_1Y) {
		R30_OVER6M_TO_1Y = r30_OVER6M_TO_1Y;
	}
	public BigDecimal getR30_OVER1Y_TO_3Y() {
		return R30_OVER1Y_TO_3Y;
	}
	public void setR30_OVER1Y_TO_3Y(BigDecimal r30_OVER1Y_TO_3Y) {
		R30_OVER1Y_TO_3Y = r30_OVER1Y_TO_3Y;
	}
	public BigDecimal getR30_OVER3Y_TO_5Y() {
		return R30_OVER3Y_TO_5Y;
	}
	public void setR30_OVER3Y_TO_5Y(BigDecimal r30_OVER3Y_TO_5Y) {
		R30_OVER3Y_TO_5Y = r30_OVER3Y_TO_5Y;
	}
	public BigDecimal getR30_OVER5Y() {
		return R30_OVER5Y;
	}
	public void setR30_OVER5Y(BigDecimal r30_OVER5Y) {
		R30_OVER5Y = r30_OVER5Y;
	}
	public BigDecimal getR30_TOTAL() {
		return R30_TOTAL;
	}
	public void setR30_TOTAL(BigDecimal r30_TOTAL) {
		R30_TOTAL = r30_TOTAL;
	}
	public String getR31_PRODUCT() {
		return R31_PRODUCT;
	}
	public void setR31_PRODUCT(String r31_PRODUCT) {
		R31_PRODUCT = r31_PRODUCT;
	}
	public BigDecimal getR31_DAY1() {
		return R31_DAY1;
	}
	public void setR31_DAY1(BigDecimal r31_DAY1) {
		R31_DAY1 = r31_DAY1;
	}
	public BigDecimal getR31_DAY2_7() {
		return R31_DAY2_7;
	}
	public void setR31_DAY2_7(BigDecimal r31_DAY2_7) {
		R31_DAY2_7 = r31_DAY2_7;
	}
	public BigDecimal getR31_DAY8_14() {
		return R31_DAY8_14;
	}
	public void setR31_DAY8_14(BigDecimal r31_DAY8_14) {
		R31_DAY8_14 = r31_DAY8_14;
	}
	public BigDecimal getR31_DAY15_30() {
		return R31_DAY15_30;
	}
	public void setR31_DAY15_30(BigDecimal r31_DAY15_30) {
		R31_DAY15_30 = r31_DAY15_30;
	}
	public BigDecimal getR31_DAY31_TO_2M() {
		return R31_DAY31_TO_2M;
	}
	public void setR31_DAY31_TO_2M(BigDecimal r31_DAY31_TO_2M) {
		R31_DAY31_TO_2M = r31_DAY31_TO_2M;
	}
	public BigDecimal getR31_MORE2M_TO_3M() {
		return R31_MORE2M_TO_3M;
	}
	public void setR31_MORE2M_TO_3M(BigDecimal r31_MORE2M_TO_3M) {
		R31_MORE2M_TO_3M = r31_MORE2M_TO_3M;
	}
	public BigDecimal getR31_OVER3M_TO_6M() {
		return R31_OVER3M_TO_6M;
	}
	public void setR31_OVER3M_TO_6M(BigDecimal r31_OVER3M_TO_6M) {
		R31_OVER3M_TO_6M = r31_OVER3M_TO_6M;
	}
	public BigDecimal getR31_OVER6M_TO_1Y() {
		return R31_OVER6M_TO_1Y;
	}
	public void setR31_OVER6M_TO_1Y(BigDecimal r31_OVER6M_TO_1Y) {
		R31_OVER6M_TO_1Y = r31_OVER6M_TO_1Y;
	}
	public BigDecimal getR31_OVER1Y_TO_3Y() {
		return R31_OVER1Y_TO_3Y;
	}
	public void setR31_OVER1Y_TO_3Y(BigDecimal r31_OVER1Y_TO_3Y) {
		R31_OVER1Y_TO_3Y = r31_OVER1Y_TO_3Y;
	}
	public BigDecimal getR31_OVER3Y_TO_5Y() {
		return R31_OVER3Y_TO_5Y;
	}
	public void setR31_OVER3Y_TO_5Y(BigDecimal r31_OVER3Y_TO_5Y) {
		R31_OVER3Y_TO_5Y = r31_OVER3Y_TO_5Y;
	}
	public BigDecimal getR31_OVER5Y() {
		return R31_OVER5Y;
	}
	public void setR31_OVER5Y(BigDecimal r31_OVER5Y) {
		R31_OVER5Y = r31_OVER5Y;
	}
	public BigDecimal getR31_TOTAL() {
		return R31_TOTAL;
	}
	public void setR31_TOTAL(BigDecimal r31_TOTAL) {
		R31_TOTAL = r31_TOTAL;
	}
	public String getR32_PRODUCT() {
		return R32_PRODUCT;
	}
	public void setR32_PRODUCT(String r32_PRODUCT) {
		R32_PRODUCT = r32_PRODUCT;
	}
	public BigDecimal getR32_DAY1() {
		return R32_DAY1;
	}
	public void setR32_DAY1(BigDecimal r32_DAY1) {
		R32_DAY1 = r32_DAY1;
	}
	public BigDecimal getR32_DAY2_7() {
		return R32_DAY2_7;
	}
	public void setR32_DAY2_7(BigDecimal r32_DAY2_7) {
		R32_DAY2_7 = r32_DAY2_7;
	}
	public BigDecimal getR32_DAY8_14() {
		return R32_DAY8_14;
	}
	public void setR32_DAY8_14(BigDecimal r32_DAY8_14) {
		R32_DAY8_14 = r32_DAY8_14;
	}
	public BigDecimal getR32_DAY15_30() {
		return R32_DAY15_30;
	}
	public void setR32_DAY15_30(BigDecimal r32_DAY15_30) {
		R32_DAY15_30 = r32_DAY15_30;
	}
	public BigDecimal getR32_DAY31_TO_2M() {
		return R32_DAY31_TO_2M;
	}
	public void setR32_DAY31_TO_2M(BigDecimal r32_DAY31_TO_2M) {
		R32_DAY31_TO_2M = r32_DAY31_TO_2M;
	}
	public BigDecimal getR32_MORE2M_TO_3M() {
		return R32_MORE2M_TO_3M;
	}
	public void setR32_MORE2M_TO_3M(BigDecimal r32_MORE2M_TO_3M) {
		R32_MORE2M_TO_3M = r32_MORE2M_TO_3M;
	}
	public BigDecimal getR32_OVER3M_TO_6M() {
		return R32_OVER3M_TO_6M;
	}
	public void setR32_OVER3M_TO_6M(BigDecimal r32_OVER3M_TO_6M) {
		R32_OVER3M_TO_6M = r32_OVER3M_TO_6M;
	}
	public BigDecimal getR32_OVER6M_TO_1Y() {
		return R32_OVER6M_TO_1Y;
	}
	public void setR32_OVER6M_TO_1Y(BigDecimal r32_OVER6M_TO_1Y) {
		R32_OVER6M_TO_1Y = r32_OVER6M_TO_1Y;
	}
	public BigDecimal getR32_OVER1Y_TO_3Y() {
		return R32_OVER1Y_TO_3Y;
	}
	public void setR32_OVER1Y_TO_3Y(BigDecimal r32_OVER1Y_TO_3Y) {
		R32_OVER1Y_TO_3Y = r32_OVER1Y_TO_3Y;
	}
	public BigDecimal getR32_OVER3Y_TO_5Y() {
		return R32_OVER3Y_TO_5Y;
	}
	public void setR32_OVER3Y_TO_5Y(BigDecimal r32_OVER3Y_TO_5Y) {
		R32_OVER3Y_TO_5Y = r32_OVER3Y_TO_5Y;
	}
	public BigDecimal getR32_OVER5Y() {
		return R32_OVER5Y;
	}
	public void setR32_OVER5Y(BigDecimal r32_OVER5Y) {
		R32_OVER5Y = r32_OVER5Y;
	}
	public BigDecimal getR32_TOTAL() {
		return R32_TOTAL;
	}
	public void setR32_TOTAL(BigDecimal r32_TOTAL) {
		R32_TOTAL = r32_TOTAL;
	}
	public String getR33_PRODUCT() {
		return R33_PRODUCT;
	}
	public void setR33_PRODUCT(String r33_PRODUCT) {
		R33_PRODUCT = r33_PRODUCT;
	}
	public BigDecimal getR33_DAY1() {
		return R33_DAY1;
	}
	public void setR33_DAY1(BigDecimal r33_DAY1) {
		R33_DAY1 = r33_DAY1;
	}
	public BigDecimal getR33_DAY2_7() {
		return R33_DAY2_7;
	}
	public void setR33_DAY2_7(BigDecimal r33_DAY2_7) {
		R33_DAY2_7 = r33_DAY2_7;
	}
	public BigDecimal getR33_DAY8_14() {
		return R33_DAY8_14;
	}
	public void setR33_DAY8_14(BigDecimal r33_DAY8_14) {
		R33_DAY8_14 = r33_DAY8_14;
	}
	public BigDecimal getR33_DAY15_30() {
		return R33_DAY15_30;
	}
	public void setR33_DAY15_30(BigDecimal r33_DAY15_30) {
		R33_DAY15_30 = r33_DAY15_30;
	}
	public BigDecimal getR33_DAY31_TO_2M() {
		return R33_DAY31_TO_2M;
	}
	public void setR33_DAY31_TO_2M(BigDecimal r33_DAY31_TO_2M) {
		R33_DAY31_TO_2M = r33_DAY31_TO_2M;
	}
	public BigDecimal getR33_MORE2M_TO_3M() {
		return R33_MORE2M_TO_3M;
	}
	public void setR33_MORE2M_TO_3M(BigDecimal r33_MORE2M_TO_3M) {
		R33_MORE2M_TO_3M = r33_MORE2M_TO_3M;
	}
	public BigDecimal getR33_OVER3M_TO_6M() {
		return R33_OVER3M_TO_6M;
	}
	public void setR33_OVER3M_TO_6M(BigDecimal r33_OVER3M_TO_6M) {
		R33_OVER3M_TO_6M = r33_OVER3M_TO_6M;
	}
	public BigDecimal getR33_OVER6M_TO_1Y() {
		return R33_OVER6M_TO_1Y;
	}
	public void setR33_OVER6M_TO_1Y(BigDecimal r33_OVER6M_TO_1Y) {
		R33_OVER6M_TO_1Y = r33_OVER6M_TO_1Y;
	}
	public BigDecimal getR33_OVER1Y_TO_3Y() {
		return R33_OVER1Y_TO_3Y;
	}
	public void setR33_OVER1Y_TO_3Y(BigDecimal r33_OVER1Y_TO_3Y) {
		R33_OVER1Y_TO_3Y = r33_OVER1Y_TO_3Y;
	}
	public BigDecimal getR33_OVER3Y_TO_5Y() {
		return R33_OVER3Y_TO_5Y;
	}
	public void setR33_OVER3Y_TO_5Y(BigDecimal r33_OVER3Y_TO_5Y) {
		R33_OVER3Y_TO_5Y = r33_OVER3Y_TO_5Y;
	}
	public BigDecimal getR33_OVER5Y() {
		return R33_OVER5Y;
	}
	public void setR33_OVER5Y(BigDecimal r33_OVER5Y) {
		R33_OVER5Y = r33_OVER5Y;
	}
	public BigDecimal getR33_TOTAL() {
		return R33_TOTAL;
	}
	public void setR33_TOTAL(BigDecimal r33_TOTAL) {
		R33_TOTAL = r33_TOTAL;
	}
	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}
	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}
	public BigDecimal getR34_DAY1() {
		return R34_DAY1;
	}
	public void setR34_DAY1(BigDecimal r34_DAY1) {
		R34_DAY1 = r34_DAY1;
	}
	public BigDecimal getR34_DAY2_7() {
		return R34_DAY2_7;
	}
	public void setR34_DAY2_7(BigDecimal r34_DAY2_7) {
		R34_DAY2_7 = r34_DAY2_7;
	}
	public BigDecimal getR34_DAY8_14() {
		return R34_DAY8_14;
	}
	public void setR34_DAY8_14(BigDecimal r34_DAY8_14) {
		R34_DAY8_14 = r34_DAY8_14;
	}
	public BigDecimal getR34_DAY15_30() {
		return R34_DAY15_30;
	}
	public void setR34_DAY15_30(BigDecimal r34_DAY15_30) {
		R34_DAY15_30 = r34_DAY15_30;
	}
	public BigDecimal getR34_DAY31_TO_2M() {
		return R34_DAY31_TO_2M;
	}
	public void setR34_DAY31_TO_2M(BigDecimal r34_DAY31_TO_2M) {
		R34_DAY31_TO_2M = r34_DAY31_TO_2M;
	}
	public BigDecimal getR34_MORE2M_TO_3M() {
		return R34_MORE2M_TO_3M;
	}
	public void setR34_MORE2M_TO_3M(BigDecimal r34_MORE2M_TO_3M) {
		R34_MORE2M_TO_3M = r34_MORE2M_TO_3M;
	}
	public BigDecimal getR34_OVER3M_TO_6M() {
		return R34_OVER3M_TO_6M;
	}
	public void setR34_OVER3M_TO_6M(BigDecimal r34_OVER3M_TO_6M) {
		R34_OVER3M_TO_6M = r34_OVER3M_TO_6M;
	}
	public BigDecimal getR34_OVER6M_TO_1Y() {
		return R34_OVER6M_TO_1Y;
	}
	public void setR34_OVER6M_TO_1Y(BigDecimal r34_OVER6M_TO_1Y) {
		R34_OVER6M_TO_1Y = r34_OVER6M_TO_1Y;
	}
	public BigDecimal getR34_OVER1Y_TO_3Y() {
		return R34_OVER1Y_TO_3Y;
	}
	public void setR34_OVER1Y_TO_3Y(BigDecimal r34_OVER1Y_TO_3Y) {
		R34_OVER1Y_TO_3Y = r34_OVER1Y_TO_3Y;
	}
	public BigDecimal getR34_OVER3Y_TO_5Y() {
		return R34_OVER3Y_TO_5Y;
	}
	public void setR34_OVER3Y_TO_5Y(BigDecimal r34_OVER3Y_TO_5Y) {
		R34_OVER3Y_TO_5Y = r34_OVER3Y_TO_5Y;
	}
	public BigDecimal getR34_OVER5Y() {
		return R34_OVER5Y;
	}
	public void setR34_OVER5Y(BigDecimal r34_OVER5Y) {
		R34_OVER5Y = r34_OVER5Y;
	}
	public BigDecimal getR34_TOTAL() {
		return R34_TOTAL;
	}
	public void setR34_TOTAL(BigDecimal r34_TOTAL) {
		R34_TOTAL = r34_TOTAL;
	}
	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}
	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}
	public BigDecimal getR35_DAY1() {
		return R35_DAY1;
	}
	public void setR35_DAY1(BigDecimal r35_DAY1) {
		R35_DAY1 = r35_DAY1;
	}
	public BigDecimal getR35_DAY2_7() {
		return R35_DAY2_7;
	}
	public void setR35_DAY2_7(BigDecimal r35_DAY2_7) {
		R35_DAY2_7 = r35_DAY2_7;
	}
	public BigDecimal getR35_DAY8_14() {
		return R35_DAY8_14;
	}
	public void setR35_DAY8_14(BigDecimal r35_DAY8_14) {
		R35_DAY8_14 = r35_DAY8_14;
	}
	public BigDecimal getR35_DAY15_30() {
		return R35_DAY15_30;
	}
	public void setR35_DAY15_30(BigDecimal r35_DAY15_30) {
		R35_DAY15_30 = r35_DAY15_30;
	}
	public BigDecimal getR35_DAY31_TO_2M() {
		return R35_DAY31_TO_2M;
	}
	public void setR35_DAY31_TO_2M(BigDecimal r35_DAY31_TO_2M) {
		R35_DAY31_TO_2M = r35_DAY31_TO_2M;
	}
	public BigDecimal getR35_MORE2M_TO_3M() {
		return R35_MORE2M_TO_3M;
	}
	public void setR35_MORE2M_TO_3M(BigDecimal r35_MORE2M_TO_3M) {
		R35_MORE2M_TO_3M = r35_MORE2M_TO_3M;
	}
	public BigDecimal getR35_OVER3M_TO_6M() {
		return R35_OVER3M_TO_6M;
	}
	public void setR35_OVER3M_TO_6M(BigDecimal r35_OVER3M_TO_6M) {
		R35_OVER3M_TO_6M = r35_OVER3M_TO_6M;
	}
	public BigDecimal getR35_OVER6M_TO_1Y() {
		return R35_OVER6M_TO_1Y;
	}
	public void setR35_OVER6M_TO_1Y(BigDecimal r35_OVER6M_TO_1Y) {
		R35_OVER6M_TO_1Y = r35_OVER6M_TO_1Y;
	}
	public BigDecimal getR35_OVER1Y_TO_3Y() {
		return R35_OVER1Y_TO_3Y;
	}
	public void setR35_OVER1Y_TO_3Y(BigDecimal r35_OVER1Y_TO_3Y) {
		R35_OVER1Y_TO_3Y = r35_OVER1Y_TO_3Y;
	}
	public BigDecimal getR35_OVER3Y_TO_5Y() {
		return R35_OVER3Y_TO_5Y;
	}
	public void setR35_OVER3Y_TO_5Y(BigDecimal r35_OVER3Y_TO_5Y) {
		R35_OVER3Y_TO_5Y = r35_OVER3Y_TO_5Y;
	}
	public BigDecimal getR35_OVER5Y() {
		return R35_OVER5Y;
	}
	public void setR35_OVER5Y(BigDecimal r35_OVER5Y) {
		R35_OVER5Y = r35_OVER5Y;
	}
	public BigDecimal getR35_TOTAL() {
		return R35_TOTAL;
	}
	public void setR35_TOTAL(BigDecimal r35_TOTAL) {
		R35_TOTAL = r35_TOTAL;
	}
	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}
	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}
	public BigDecimal getR36_DAY1() {
		return R36_DAY1;
	}
	public void setR36_DAY1(BigDecimal r36_DAY1) {
		R36_DAY1 = r36_DAY1;
	}
	public BigDecimal getR36_DAY2_7() {
		return R36_DAY2_7;
	}
	public void setR36_DAY2_7(BigDecimal r36_DAY2_7) {
		R36_DAY2_7 = r36_DAY2_7;
	}
	public BigDecimal getR36_DAY8_14() {
		return R36_DAY8_14;
	}
	public void setR36_DAY8_14(BigDecimal r36_DAY8_14) {
		R36_DAY8_14 = r36_DAY8_14;
	}
	public BigDecimal getR36_DAY15_30() {
		return R36_DAY15_30;
	}
	public void setR36_DAY15_30(BigDecimal r36_DAY15_30) {
		R36_DAY15_30 = r36_DAY15_30;
	}
	public BigDecimal getR36_DAY31_TO_2M() {
		return R36_DAY31_TO_2M;
	}
	public void setR36_DAY31_TO_2M(BigDecimal r36_DAY31_TO_2M) {
		R36_DAY31_TO_2M = r36_DAY31_TO_2M;
	}
	public BigDecimal getR36_MORE2M_TO_3M() {
		return R36_MORE2M_TO_3M;
	}
	public void setR36_MORE2M_TO_3M(BigDecimal r36_MORE2M_TO_3M) {
		R36_MORE2M_TO_3M = r36_MORE2M_TO_3M;
	}
	public BigDecimal getR36_OVER3M_TO_6M() {
		return R36_OVER3M_TO_6M;
	}
	public void setR36_OVER3M_TO_6M(BigDecimal r36_OVER3M_TO_6M) {
		R36_OVER3M_TO_6M = r36_OVER3M_TO_6M;
	}
	public BigDecimal getR36_OVER6M_TO_1Y() {
		return R36_OVER6M_TO_1Y;
	}
	public void setR36_OVER6M_TO_1Y(BigDecimal r36_OVER6M_TO_1Y) {
		R36_OVER6M_TO_1Y = r36_OVER6M_TO_1Y;
	}
	public BigDecimal getR36_OVER1Y_TO_3Y() {
		return R36_OVER1Y_TO_3Y;
	}
	public void setR36_OVER1Y_TO_3Y(BigDecimal r36_OVER1Y_TO_3Y) {
		R36_OVER1Y_TO_3Y = r36_OVER1Y_TO_3Y;
	}
	public BigDecimal getR36_OVER3Y_TO_5Y() {
		return R36_OVER3Y_TO_5Y;
	}
	public void setR36_OVER3Y_TO_5Y(BigDecimal r36_OVER3Y_TO_5Y) {
		R36_OVER3Y_TO_5Y = r36_OVER3Y_TO_5Y;
	}
	public BigDecimal getR36_OVER5Y() {
		return R36_OVER5Y;
	}
	public void setR36_OVER5Y(BigDecimal r36_OVER5Y) {
		R36_OVER5Y = r36_OVER5Y;
	}
	public BigDecimal getR36_TOTAL() {
		return R36_TOTAL;
	}
	public void setR36_TOTAL(BigDecimal r36_TOTAL) {
		R36_TOTAL = r36_TOTAL;
	}
	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}
	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}
	public BigDecimal getR37_DAY1() {
		return R37_DAY1;
	}
	public void setR37_DAY1(BigDecimal r37_DAY1) {
		R37_DAY1 = r37_DAY1;
	}
	public BigDecimal getR37_DAY2_7() {
		return R37_DAY2_7;
	}
	public void setR37_DAY2_7(BigDecimal r37_DAY2_7) {
		R37_DAY2_7 = r37_DAY2_7;
	}
	public BigDecimal getR37_DAY8_14() {
		return R37_DAY8_14;
	}
	public void setR37_DAY8_14(BigDecimal r37_DAY8_14) {
		R37_DAY8_14 = r37_DAY8_14;
	}
	public BigDecimal getR37_DAY15_30() {
		return R37_DAY15_30;
	}
	public void setR37_DAY15_30(BigDecimal r37_DAY15_30) {
		R37_DAY15_30 = r37_DAY15_30;
	}
	public BigDecimal getR37_DAY31_TO_2M() {
		return R37_DAY31_TO_2M;
	}
	public void setR37_DAY31_TO_2M(BigDecimal r37_DAY31_TO_2M) {
		R37_DAY31_TO_2M = r37_DAY31_TO_2M;
	}
	public BigDecimal getR37_MORE2M_TO_3M() {
		return R37_MORE2M_TO_3M;
	}
	public void setR37_MORE2M_TO_3M(BigDecimal r37_MORE2M_TO_3M) {
		R37_MORE2M_TO_3M = r37_MORE2M_TO_3M;
	}
	public BigDecimal getR37_OVER3M_TO_6M() {
		return R37_OVER3M_TO_6M;
	}
	public void setR37_OVER3M_TO_6M(BigDecimal r37_OVER3M_TO_6M) {
		R37_OVER3M_TO_6M = r37_OVER3M_TO_6M;
	}
	public BigDecimal getR37_OVER6M_TO_1Y() {
		return R37_OVER6M_TO_1Y;
	}
	public void setR37_OVER6M_TO_1Y(BigDecimal r37_OVER6M_TO_1Y) {
		R37_OVER6M_TO_1Y = r37_OVER6M_TO_1Y;
	}
	public BigDecimal getR37_OVER1Y_TO_3Y() {
		return R37_OVER1Y_TO_3Y;
	}
	public void setR37_OVER1Y_TO_3Y(BigDecimal r37_OVER1Y_TO_3Y) {
		R37_OVER1Y_TO_3Y = r37_OVER1Y_TO_3Y;
	}
	public BigDecimal getR37_OVER3Y_TO_5Y() {
		return R37_OVER3Y_TO_5Y;
	}
	public void setR37_OVER3Y_TO_5Y(BigDecimal r37_OVER3Y_TO_5Y) {
		R37_OVER3Y_TO_5Y = r37_OVER3Y_TO_5Y;
	}
	public BigDecimal getR37_OVER5Y() {
		return R37_OVER5Y;
	}
	public void setR37_OVER5Y(BigDecimal r37_OVER5Y) {
		R37_OVER5Y = r37_OVER5Y;
	}
	public BigDecimal getR37_TOTAL() {
		return R37_TOTAL;
	}
	public void setR37_TOTAL(BigDecimal r37_TOTAL) {
		R37_TOTAL = r37_TOTAL;
	}
	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}
	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}
	public BigDecimal getR38_DAY1() {
		return R38_DAY1;
	}
	public void setR38_DAY1(BigDecimal r38_DAY1) {
		R38_DAY1 = r38_DAY1;
	}
	public BigDecimal getR38_DAY2_7() {
		return R38_DAY2_7;
	}
	public void setR38_DAY2_7(BigDecimal r38_DAY2_7) {
		R38_DAY2_7 = r38_DAY2_7;
	}
	public BigDecimal getR38_DAY8_14() {
		return R38_DAY8_14;
	}
	public void setR38_DAY8_14(BigDecimal r38_DAY8_14) {
		R38_DAY8_14 = r38_DAY8_14;
	}
	public BigDecimal getR38_DAY15_30() {
		return R38_DAY15_30;
	}
	public void setR38_DAY15_30(BigDecimal r38_DAY15_30) {
		R38_DAY15_30 = r38_DAY15_30;
	}
	public BigDecimal getR38_DAY31_TO_2M() {
		return R38_DAY31_TO_2M;
	}
	public void setR38_DAY31_TO_2M(BigDecimal r38_DAY31_TO_2M) {
		R38_DAY31_TO_2M = r38_DAY31_TO_2M;
	}
	public BigDecimal getR38_MORE2M_TO_3M() {
		return R38_MORE2M_TO_3M;
	}
	public void setR38_MORE2M_TO_3M(BigDecimal r38_MORE2M_TO_3M) {
		R38_MORE2M_TO_3M = r38_MORE2M_TO_3M;
	}
	public BigDecimal getR38_OVER3M_TO_6M() {
		return R38_OVER3M_TO_6M;
	}
	public void setR38_OVER3M_TO_6M(BigDecimal r38_OVER3M_TO_6M) {
		R38_OVER3M_TO_6M = r38_OVER3M_TO_6M;
	}
	public BigDecimal getR38_OVER6M_TO_1Y() {
		return R38_OVER6M_TO_1Y;
	}
	public void setR38_OVER6M_TO_1Y(BigDecimal r38_OVER6M_TO_1Y) {
		R38_OVER6M_TO_1Y = r38_OVER6M_TO_1Y;
	}
	public BigDecimal getR38_OVER1Y_TO_3Y() {
		return R38_OVER1Y_TO_3Y;
	}
	public void setR38_OVER1Y_TO_3Y(BigDecimal r38_OVER1Y_TO_3Y) {
		R38_OVER1Y_TO_3Y = r38_OVER1Y_TO_3Y;
	}
	public BigDecimal getR38_OVER3Y_TO_5Y() {
		return R38_OVER3Y_TO_5Y;
	}
	public void setR38_OVER3Y_TO_5Y(BigDecimal r38_OVER3Y_TO_5Y) {
		R38_OVER3Y_TO_5Y = r38_OVER3Y_TO_5Y;
	}
	public BigDecimal getR38_OVER5Y() {
		return R38_OVER5Y;
	}
	public void setR38_OVER5Y(BigDecimal r38_OVER5Y) {
		R38_OVER5Y = r38_OVER5Y;
	}
	public BigDecimal getR38_TOTAL() {
		return R38_TOTAL;
	}
	public void setR38_TOTAL(BigDecimal r38_TOTAL) {
		R38_TOTAL = r38_TOTAL;
	}
	public String getR39_PRODUCT() {
		return R39_PRODUCT;
	}
	public void setR39_PRODUCT(String r39_PRODUCT) {
		R39_PRODUCT = r39_PRODUCT;
	}
	public BigDecimal getR39_DAY1() {
		return R39_DAY1;
	}
	public void setR39_DAY1(BigDecimal r39_DAY1) {
		R39_DAY1 = r39_DAY1;
	}
	public BigDecimal getR39_DAY2_7() {
		return R39_DAY2_7;
	}
	public void setR39_DAY2_7(BigDecimal r39_DAY2_7) {
		R39_DAY2_7 = r39_DAY2_7;
	}
	public BigDecimal getR39_DAY8_14() {
		return R39_DAY8_14;
	}
	public void setR39_DAY8_14(BigDecimal r39_DAY8_14) {
		R39_DAY8_14 = r39_DAY8_14;
	}
	public BigDecimal getR39_DAY15_30() {
		return R39_DAY15_30;
	}
	public void setR39_DAY15_30(BigDecimal r39_DAY15_30) {
		R39_DAY15_30 = r39_DAY15_30;
	}
	public BigDecimal getR39_DAY31_TO_2M() {
		return R39_DAY31_TO_2M;
	}
	public void setR39_DAY31_TO_2M(BigDecimal r39_DAY31_TO_2M) {
		R39_DAY31_TO_2M = r39_DAY31_TO_2M;
	}
	public BigDecimal getR39_MORE2M_TO_3M() {
		return R39_MORE2M_TO_3M;
	}
	public void setR39_MORE2M_TO_3M(BigDecimal r39_MORE2M_TO_3M) {
		R39_MORE2M_TO_3M = r39_MORE2M_TO_3M;
	}
	public BigDecimal getR39_OVER3M_TO_6M() {
		return R39_OVER3M_TO_6M;
	}
	public void setR39_OVER3M_TO_6M(BigDecimal r39_OVER3M_TO_6M) {
		R39_OVER3M_TO_6M = r39_OVER3M_TO_6M;
	}
	public BigDecimal getR39_OVER6M_TO_1Y() {
		return R39_OVER6M_TO_1Y;
	}
	public void setR39_OVER6M_TO_1Y(BigDecimal r39_OVER6M_TO_1Y) {
		R39_OVER6M_TO_1Y = r39_OVER6M_TO_1Y;
	}
	public BigDecimal getR39_OVER1Y_TO_3Y() {
		return R39_OVER1Y_TO_3Y;
	}
	public void setR39_OVER1Y_TO_3Y(BigDecimal r39_OVER1Y_TO_3Y) {
		R39_OVER1Y_TO_3Y = r39_OVER1Y_TO_3Y;
	}
	public BigDecimal getR39_OVER3Y_TO_5Y() {
		return R39_OVER3Y_TO_5Y;
	}
	public void setR39_OVER3Y_TO_5Y(BigDecimal r39_OVER3Y_TO_5Y) {
		R39_OVER3Y_TO_5Y = r39_OVER3Y_TO_5Y;
	}
	public BigDecimal getR39_OVER5Y() {
		return R39_OVER5Y;
	}
	public void setR39_OVER5Y(BigDecimal r39_OVER5Y) {
		R39_OVER5Y = r39_OVER5Y;
	}
	public BigDecimal getR39_TOTAL() {
		return R39_TOTAL;
	}
	public void setR39_TOTAL(BigDecimal r39_TOTAL) {
		R39_TOTAL = r39_TOTAL;
	}
	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}
	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}
	public BigDecimal getR40_DAY1() {
		return R40_DAY1;
	}
	public void setR40_DAY1(BigDecimal r40_DAY1) {
		R40_DAY1 = r40_DAY1;
	}
	public BigDecimal getR40_DAY2_7() {
		return R40_DAY2_7;
	}
	public void setR40_DAY2_7(BigDecimal r40_DAY2_7) {
		R40_DAY2_7 = r40_DAY2_7;
	}
	public BigDecimal getR40_DAY8_14() {
		return R40_DAY8_14;
	}
	public void setR40_DAY8_14(BigDecimal r40_DAY8_14) {
		R40_DAY8_14 = r40_DAY8_14;
	}
	public BigDecimal getR40_DAY15_30() {
		return R40_DAY15_30;
	}
	public void setR40_DAY15_30(BigDecimal r40_DAY15_30) {
		R40_DAY15_30 = r40_DAY15_30;
	}
	public BigDecimal getR40_DAY31_TO_2M() {
		return R40_DAY31_TO_2M;
	}
	public void setR40_DAY31_TO_2M(BigDecimal r40_DAY31_TO_2M) {
		R40_DAY31_TO_2M = r40_DAY31_TO_2M;
	}
	public BigDecimal getR40_MORE2M_TO_3M() {
		return R40_MORE2M_TO_3M;
	}
	public void setR40_MORE2M_TO_3M(BigDecimal r40_MORE2M_TO_3M) {
		R40_MORE2M_TO_3M = r40_MORE2M_TO_3M;
	}
	public BigDecimal getR40_OVER3M_TO_6M() {
		return R40_OVER3M_TO_6M;
	}
	public void setR40_OVER3M_TO_6M(BigDecimal r40_OVER3M_TO_6M) {
		R40_OVER3M_TO_6M = r40_OVER3M_TO_6M;
	}
	public BigDecimal getR40_OVER6M_TO_1Y() {
		return R40_OVER6M_TO_1Y;
	}
	public void setR40_OVER6M_TO_1Y(BigDecimal r40_OVER6M_TO_1Y) {
		R40_OVER6M_TO_1Y = r40_OVER6M_TO_1Y;
	}
	public BigDecimal getR40_OVER1Y_TO_3Y() {
		return R40_OVER1Y_TO_3Y;
	}
	public void setR40_OVER1Y_TO_3Y(BigDecimal r40_OVER1Y_TO_3Y) {
		R40_OVER1Y_TO_3Y = r40_OVER1Y_TO_3Y;
	}
	public BigDecimal getR40_OVER3Y_TO_5Y() {
		return R40_OVER3Y_TO_5Y;
	}
	public void setR40_OVER3Y_TO_5Y(BigDecimal r40_OVER3Y_TO_5Y) {
		R40_OVER3Y_TO_5Y = r40_OVER3Y_TO_5Y;
	}
	public BigDecimal getR40_OVER5Y() {
		return R40_OVER5Y;
	}
	public void setR40_OVER5Y(BigDecimal r40_OVER5Y) {
		R40_OVER5Y = r40_OVER5Y;
	}
	public BigDecimal getR40_TOTAL() {
		return R40_TOTAL;
	}
	public void setR40_TOTAL(BigDecimal r40_TOTAL) {
		R40_TOTAL = r40_TOTAL;
	}
//	public String getR41_PRODUCT() {
//		return R41_PRODUCT;
//	}
//	public void setR41_PRODUCT(String r41_PRODUCT) {
//		R41_PRODUCT = r41_PRODUCT;
//	}
//	public BigDecimal getR41_DAY1() {
//		return R41_DAY1;
//	}
//	public void setR41_DAY1(BigDecimal r41_DAY1) {
//		R41_DAY1 = r41_DAY1;
//	}
//	public BigDecimal getR41_DAY2_7() {
//		return R41_DAY2_7;
//	}
//	public void setR41_DAY2_7(BigDecimal r41_DAY2_7) {
//		R41_DAY2_7 = r41_DAY2_7;
//	}
//	public BigDecimal getR41_DAY8_14() {
//		return R41_DAY8_14;
//	}
//	public void setR41_DAY8_14(BigDecimal r41_DAY8_14) {
//		R41_DAY8_14 = r41_DAY8_14;
//	}
//	public BigDecimal getR41_DAY15_30() {
//		return R41_DAY15_30;
//	}
//	public void setR41_DAY15_30(BigDecimal r41_DAY15_30) {
//		R41_DAY15_30 = r41_DAY15_30;
//	}
//	public BigDecimal getR41_DAY31_TO_2M() {
//		return R41_DAY31_TO_2M;
//	}
//	public void setR41_DAY31_TO_2M(BigDecimal r41_DAY31_TO_2M) {
//		R41_DAY31_TO_2M = r41_DAY31_TO_2M;
//	}
//	public BigDecimal getR41_MORE2M_TO_3M() {
//		return R41_MORE2M_TO_3M;
//	}
//	public void setR41_MORE2M_TO_3M(BigDecimal r41_MORE2M_TO_3M) {
//		R41_MORE2M_TO_3M = r41_MORE2M_TO_3M;
//	}
//	public BigDecimal getR41_OVER3M_TO_6M() {
//		return R41_OVER3M_TO_6M;
//	}
//	public void setR41_OVER3M_TO_6M(BigDecimal r41_OVER3M_TO_6M) {
//		R41_OVER3M_TO_6M = r41_OVER3M_TO_6M;
//	}
//	public BigDecimal getR41_OVER6M_TO_1Y() {
//		return R41_OVER6M_TO_1Y;
//	}
//	public void setR41_OVER6M_TO_1Y(BigDecimal r41_OVER6M_TO_1Y) {
//		R41_OVER6M_TO_1Y = r41_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR41_OVER1Y_TO_3Y() {
//		return R41_OVER1Y_TO_3Y;
//	}
//	public void setR41_OVER1Y_TO_3Y(BigDecimal r41_OVER1Y_TO_3Y) {
//		R41_OVER1Y_TO_3Y = r41_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR41_OVER3Y_TO_5Y() {
//		return R41_OVER3Y_TO_5Y;
//	}
//	public void setR41_OVER3Y_TO_5Y(BigDecimal r41_OVER3Y_TO_5Y) {
//		R41_OVER3Y_TO_5Y = r41_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR41_OVER5Y() {
//		return R41_OVER5Y;
//	}
//	public void setR41_OVER5Y(BigDecimal r41_OVER5Y) {
//		R41_OVER5Y = r41_OVER5Y;
//	}
//	public BigDecimal getR41_TOTAL() {
//		return R41_TOTAL;
//	}
//	public void setR41_TOTAL(BigDecimal r41_TOTAL) {
//		R41_TOTAL = r41_TOTAL;
//	}
//	public String getR42_PRODUCT() {
//		return R42_PRODUCT;
//	}
//	public void setR42_PRODUCT(String r42_PRODUCT) {
//		R42_PRODUCT = r42_PRODUCT;
//	}
//	public BigDecimal getR42_DAY1() {
//		return R42_DAY1;
//	}
//	public void setR42_DAY1(BigDecimal r42_DAY1) {
//		R42_DAY1 = r42_DAY1;
//	}
//	public BigDecimal getR42_DAY2_7() {
//		return R42_DAY2_7;
//	}
//	public void setR42_DAY2_7(BigDecimal r42_DAY2_7) {
//		R42_DAY2_7 = r42_DAY2_7;
//	}
//	public BigDecimal getR42_DAY8_14() {
//		return R42_DAY8_14;
//	}
//	public void setR42_DAY8_14(BigDecimal r42_DAY8_14) {
//		R42_DAY8_14 = r42_DAY8_14;
//	}
//	public BigDecimal getR42_DAY15_30() {
//		return R42_DAY15_30;
//	}
//	public void setR42_DAY15_30(BigDecimal r42_DAY15_30) {
//		R42_DAY15_30 = r42_DAY15_30;
//	}
//	public BigDecimal getR42_DAY31_TO_2M() {
//		return R42_DAY31_TO_2M;
//	}
//	public void setR42_DAY31_TO_2M(BigDecimal r42_DAY31_TO_2M) {
//		R42_DAY31_TO_2M = r42_DAY31_TO_2M;
//	}
//	public BigDecimal getR42_MORE2M_TO_3M() {
//		return R42_MORE2M_TO_3M;
//	}
//	public void setR42_MORE2M_TO_3M(BigDecimal r42_MORE2M_TO_3M) {
//		R42_MORE2M_TO_3M = r42_MORE2M_TO_3M;
//	}
//	public BigDecimal getR42_OVER3M_TO_6M() {
//		return R42_OVER3M_TO_6M;
//	}
//	public void setR42_OVER3M_TO_6M(BigDecimal r42_OVER3M_TO_6M) {
//		R42_OVER3M_TO_6M = r42_OVER3M_TO_6M;
//	}
//	public BigDecimal getR42_OVER6M_TO_1Y() {
//		return R42_OVER6M_TO_1Y;
//	}
//	public void setR42_OVER6M_TO_1Y(BigDecimal r42_OVER6M_TO_1Y) {
//		R42_OVER6M_TO_1Y = r42_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR42_OVER1Y_TO_3Y() {
//		return R42_OVER1Y_TO_3Y;
//	}
//	public void setR42_OVER1Y_TO_3Y(BigDecimal r42_OVER1Y_TO_3Y) {
//		R42_OVER1Y_TO_3Y = r42_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR42_OVER3Y_TO_5Y() {
//		return R42_OVER3Y_TO_5Y;
//	}
//	public void setR42_OVER3Y_TO_5Y(BigDecimal r42_OVER3Y_TO_5Y) {
//		R42_OVER3Y_TO_5Y = r42_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR42_OVER5Y() {
//		return R42_OVER5Y;
//	}
//	public void setR42_OVER5Y(BigDecimal r42_OVER5Y) {
//		R42_OVER5Y = r42_OVER5Y;
//	}
//	public BigDecimal getR42_TOTAL() {
//		return R42_TOTAL;
//	}
//	public void setR42_TOTAL(BigDecimal r42_TOTAL) {
//		R42_TOTAL = r42_TOTAL;
//	}
//	public String getR43_PRODUCT() {
//		return R43_PRODUCT;
//	}
//	public void setR43_PRODUCT(String r43_PRODUCT) {
//		R43_PRODUCT = r43_PRODUCT;
//	}
//	public BigDecimal getR43_DAY1() {
//		return R43_DAY1;
//	}
//	public void setR43_DAY1(BigDecimal r43_DAY1) {
//		R43_DAY1 = r43_DAY1;
//	}
//	public BigDecimal getR43_DAY2_7() {
//		return R43_DAY2_7;
//	}
//	public void setR43_DAY2_7(BigDecimal r43_DAY2_7) {
//		R43_DAY2_7 = r43_DAY2_7;
//	}
//	public BigDecimal getR43_DAY8_14() {
//		return R43_DAY8_14;
//	}
//	public void setR43_DAY8_14(BigDecimal r43_DAY8_14) {
//		R43_DAY8_14 = r43_DAY8_14;
//	}
//	public BigDecimal getR43_DAY15_30() {
//		return R43_DAY15_30;
//	}
//	public void setR43_DAY15_30(BigDecimal r43_DAY15_30) {
//		R43_DAY15_30 = r43_DAY15_30;
//	}
//	public BigDecimal getR43_DAY31_TO_2M() {
//		return R43_DAY31_TO_2M;
//	}
//	public void setR43_DAY31_TO_2M(BigDecimal r43_DAY31_TO_2M) {
//		R43_DAY31_TO_2M = r43_DAY31_TO_2M;
//	}
//	public BigDecimal getR43_MORE2M_TO_3M() {
//		return R43_MORE2M_TO_3M;
//	}
//	public void setR43_MORE2M_TO_3M(BigDecimal r43_MORE2M_TO_3M) {
//		R43_MORE2M_TO_3M = r43_MORE2M_TO_3M;
//	}
//	public BigDecimal getR43_OVER3M_TO_6M() {
//		return R43_OVER3M_TO_6M;
//	}
//	public void setR43_OVER3M_TO_6M(BigDecimal r43_OVER3M_TO_6M) {
//		R43_OVER3M_TO_6M = r43_OVER3M_TO_6M;
//	}
//	public BigDecimal getR43_OVER6M_TO_1Y() {
//		return R43_OVER6M_TO_1Y;
//	}
//	public void setR43_OVER6M_TO_1Y(BigDecimal r43_OVER6M_TO_1Y) {
//		R43_OVER6M_TO_1Y = r43_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR43_OVER1Y_TO_3Y() {
//		return R43_OVER1Y_TO_3Y;
//	}
//	public void setR43_OVER1Y_TO_3Y(BigDecimal r43_OVER1Y_TO_3Y) {
//		R43_OVER1Y_TO_3Y = r43_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR43_OVER3Y_TO_5Y() {
//		return R43_OVER3Y_TO_5Y;
//	}
//	public void setR43_OVER3Y_TO_5Y(BigDecimal r43_OVER3Y_TO_5Y) {
//		R43_OVER3Y_TO_5Y = r43_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR43_OVER5Y() {
//		return R43_OVER5Y;
//	}
//	public void setR43_OVER5Y(BigDecimal r43_OVER5Y) {
//		R43_OVER5Y = r43_OVER5Y;
//	}
//	public BigDecimal getR43_TOTAL() {
//		return R43_TOTAL;
//	}
//	public void setR43_TOTAL(BigDecimal r43_TOTAL) {
//		R43_TOTAL = r43_TOTAL;
//	}
//	public String getR44_PRODUCT() {
//		return R44_PRODUCT;
//	}
//	public void setR44_PRODUCT(String r44_PRODUCT) {
//		R44_PRODUCT = r44_PRODUCT;
//	}
//	public BigDecimal getR44_DAY1() {
//		return R44_DAY1;
//	}
//	public void setR44_DAY1(BigDecimal r44_DAY1) {
//		R44_DAY1 = r44_DAY1;
//	}
//	public BigDecimal getR44_DAY2_7() {
//		return R44_DAY2_7;
//	}
//	public void setR44_DAY2_7(BigDecimal r44_DAY2_7) {
//		R44_DAY2_7 = r44_DAY2_7;
//	}
//	public BigDecimal getR44_DAY8_14() {
//		return R44_DAY8_14;
//	}
//	public void setR44_DAY8_14(BigDecimal r44_DAY8_14) {
//		R44_DAY8_14 = r44_DAY8_14;
//	}
//	public BigDecimal getR44_DAY15_30() {
//		return R44_DAY15_30;
//	}
//	public void setR44_DAY15_30(BigDecimal r44_DAY15_30) {
//		R44_DAY15_30 = r44_DAY15_30;
//	}
//	public BigDecimal getR44_DAY31_TO_2M() {
//		return R44_DAY31_TO_2M;
//	}
//	public void setR44_DAY31_TO_2M(BigDecimal r44_DAY31_TO_2M) {
//		R44_DAY31_TO_2M = r44_DAY31_TO_2M;
//	}
//	public BigDecimal getR44_MORE2M_TO_3M() {
//		return R44_MORE2M_TO_3M;
//	}
//	public void setR44_MORE2M_TO_3M(BigDecimal r44_MORE2M_TO_3M) {
//		R44_MORE2M_TO_3M = r44_MORE2M_TO_3M;
//	}
//	public BigDecimal getR44_OVER3M_TO_6M() {
//		return R44_OVER3M_TO_6M;
//	}
//	public void setR44_OVER3M_TO_6M(BigDecimal r44_OVER3M_TO_6M) {
//		R44_OVER3M_TO_6M = r44_OVER3M_TO_6M;
//	}
//	public BigDecimal getR44_OVER6M_TO_1Y() {
//		return R44_OVER6M_TO_1Y;
//	}
//	public void setR44_OVER6M_TO_1Y(BigDecimal r44_OVER6M_TO_1Y) {
//		R44_OVER6M_TO_1Y = r44_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR44_OVER1Y_TO_3Y() {
//		return R44_OVER1Y_TO_3Y;
//	}
//	public void setR44_OVER1Y_TO_3Y(BigDecimal r44_OVER1Y_TO_3Y) {
//		R44_OVER1Y_TO_3Y = r44_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR44_OVER3Y_TO_5Y() {
//		return R44_OVER3Y_TO_5Y;
//	}
//	public void setR44_OVER3Y_TO_5Y(BigDecimal r44_OVER3Y_TO_5Y) {
//		R44_OVER3Y_TO_5Y = r44_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR44_OVER5Y() {
//		return R44_OVER5Y;
//	}
//	public void setR44_OVER5Y(BigDecimal r44_OVER5Y) {
//		R44_OVER5Y = r44_OVER5Y;
//	}
//	public BigDecimal getR44_TOTAL() {
//		return R44_TOTAL;
//	}
//	public void setR44_TOTAL(BigDecimal r44_TOTAL) {
//		R44_TOTAL = r44_TOTAL;
//	}
//	public String getR45_PRODUCT() {
//		return R45_PRODUCT;
//	}
//	public void setR45_PRODUCT(String r45_PRODUCT) {
//		R45_PRODUCT = r45_PRODUCT;
//	}
//	public BigDecimal getR45_DAY1() {
//		return R45_DAY1;
//	}
//	public void setR45_DAY1(BigDecimal r45_DAY1) {
//		R45_DAY1 = r45_DAY1;
//	}
//	public BigDecimal getR45_DAY2_7() {
//		return R45_DAY2_7;
//	}
//	public void setR45_DAY2_7(BigDecimal r45_DAY2_7) {
//		R45_DAY2_7 = r45_DAY2_7;
//	}
//	public BigDecimal getR45_DAY8_14() {
//		return R45_DAY8_14;
//	}
//	public void setR45_DAY8_14(BigDecimal r45_DAY8_14) {
//		R45_DAY8_14 = r45_DAY8_14;
//	}
//	public BigDecimal getR45_DAY15_30() {
//		return R45_DAY15_30;
//	}
//	public void setR45_DAY15_30(BigDecimal r45_DAY15_30) {
//		R45_DAY15_30 = r45_DAY15_30;
//	}
//	public BigDecimal getR45_DAY31_TO_2M() {
//		return R45_DAY31_TO_2M;
//	}
//	public void setR45_DAY31_TO_2M(BigDecimal r45_DAY31_TO_2M) {
//		R45_DAY31_TO_2M = r45_DAY31_TO_2M;
//	}
//	public BigDecimal getR45_MORE2M_TO_3M() {
//		return R45_MORE2M_TO_3M;
//	}
//	public void setR45_MORE2M_TO_3M(BigDecimal r45_MORE2M_TO_3M) {
//		R45_MORE2M_TO_3M = r45_MORE2M_TO_3M;
//	}
//	public BigDecimal getR45_OVER3M_TO_6M() {
//		return R45_OVER3M_TO_6M;
//	}
//	public void setR45_OVER3M_TO_6M(BigDecimal r45_OVER3M_TO_6M) {
//		R45_OVER3M_TO_6M = r45_OVER3M_TO_6M;
//	}
//	public BigDecimal getR45_OVER6M_TO_1Y() {
//		return R45_OVER6M_TO_1Y;
//	}
//	public void setR45_OVER6M_TO_1Y(BigDecimal r45_OVER6M_TO_1Y) {
//		R45_OVER6M_TO_1Y = r45_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR45_OVER1Y_TO_3Y() {
//		return R45_OVER1Y_TO_3Y;
//	}
//	public void setR45_OVER1Y_TO_3Y(BigDecimal r45_OVER1Y_TO_3Y) {
//		R45_OVER1Y_TO_3Y = r45_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR45_OVER3Y_TO_5Y() {
//		return R45_OVER3Y_TO_5Y;
//	}
//	public void setR45_OVER3Y_TO_5Y(BigDecimal r45_OVER3Y_TO_5Y) {
//		R45_OVER3Y_TO_5Y = r45_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR45_OVER5Y() {
//		return R45_OVER5Y;
//	}
//	public void setR45_OVER5Y(BigDecimal r45_OVER5Y) {
//		R45_OVER5Y = r45_OVER5Y;
//	}
//	public BigDecimal getR45_TOTAL() {
//		return R45_TOTAL;
//	}
//	public void setR45_TOTAL(BigDecimal r45_TOTAL) {
//		R45_TOTAL = r45_TOTAL;
//	}
//	public String getR46_PRODUCT() {
//		return R46_PRODUCT;
//	}
//	public void setR46_PRODUCT(String r46_PRODUCT) {
//		R46_PRODUCT = r46_PRODUCT;
//	}
//	public BigDecimal getR46_DAY1() {
//		return R46_DAY1;
//	}
//	public void setR46_DAY1(BigDecimal r46_DAY1) {
//		R46_DAY1 = r46_DAY1;
//	}
//	public BigDecimal getR46_DAY2_7() {
//		return R46_DAY2_7;
//	}
//	public void setR46_DAY2_7(BigDecimal r46_DAY2_7) {
//		R46_DAY2_7 = r46_DAY2_7;
//	}
//	public BigDecimal getR46_DAY8_14() {
//		return R46_DAY8_14;
//	}
//	public void setR46_DAY8_14(BigDecimal r46_DAY8_14) {
//		R46_DAY8_14 = r46_DAY8_14;
//	}
//	public BigDecimal getR46_DAY15_30() {
//		return R46_DAY15_30;
//	}
//	public void setR46_DAY15_30(BigDecimal r46_DAY15_30) {
//		R46_DAY15_30 = r46_DAY15_30;
//	}
//	public BigDecimal getR46_DAY31_TO_2M() {
//		return R46_DAY31_TO_2M;
//	}
//	public void setR46_DAY31_TO_2M(BigDecimal r46_DAY31_TO_2M) {
//		R46_DAY31_TO_2M = r46_DAY31_TO_2M;
//	}
//	public BigDecimal getR46_MORE2M_TO_3M() {
//		return R46_MORE2M_TO_3M;
//	}
//	public void setR46_MORE2M_TO_3M(BigDecimal r46_MORE2M_TO_3M) {
//		R46_MORE2M_TO_3M = r46_MORE2M_TO_3M;
//	}
//	public BigDecimal getR46_OVER3M_TO_6M() {
//		return R46_OVER3M_TO_6M;
//	}
//	public void setR46_OVER3M_TO_6M(BigDecimal r46_OVER3M_TO_6M) {
//		R46_OVER3M_TO_6M = r46_OVER3M_TO_6M;
//	}
//	public BigDecimal getR46_OVER6M_TO_1Y() {
//		return R46_OVER6M_TO_1Y;
//	}
//	public void setR46_OVER6M_TO_1Y(BigDecimal r46_OVER6M_TO_1Y) {
//		R46_OVER6M_TO_1Y = r46_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR46_OVER1Y_TO_3Y() {
//		return R46_OVER1Y_TO_3Y;
//	}
//	public void setR46_OVER1Y_TO_3Y(BigDecimal r46_OVER1Y_TO_3Y) {
//		R46_OVER1Y_TO_3Y = r46_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR46_OVER3Y_TO_5Y() {
//		return R46_OVER3Y_TO_5Y;
//	}
//	public void setR46_OVER3Y_TO_5Y(BigDecimal r46_OVER3Y_TO_5Y) {
//		R46_OVER3Y_TO_5Y = r46_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR46_OVER5Y() {
//		return R46_OVER5Y;
//	}
//	public void setR46_OVER5Y(BigDecimal r46_OVER5Y) {
//		R46_OVER5Y = r46_OVER5Y;
//	}
//	public BigDecimal getR46_TOTAL() {
//		return R46_TOTAL;
//	}
//	public void setR46_TOTAL(BigDecimal r46_TOTAL) {
//		R46_TOTAL = r46_TOTAL;
//	}
	public String getR47_PRODUCT() {
		return R47_PRODUCT;
	}
	public void setR47_PRODUCT(String r47_PRODUCT) {
		R47_PRODUCT = r47_PRODUCT;
	}
	public BigDecimal getR47_DAY1() {
		return R47_DAY1;
	}
	public void setR47_DAY1(BigDecimal r47_DAY1) {
		R47_DAY1 = r47_DAY1;
	}
	public BigDecimal getR47_DAY2_7() {
		return R47_DAY2_7;
	}
	public void setR47_DAY2_7(BigDecimal r47_DAY2_7) {
		R47_DAY2_7 = r47_DAY2_7;
	}
	public BigDecimal getR47_DAY8_14() {
		return R47_DAY8_14;
	}
	public void setR47_DAY8_14(BigDecimal r47_DAY8_14) {
		R47_DAY8_14 = r47_DAY8_14;
	}
	public BigDecimal getR47_DAY15_30() {
		return R47_DAY15_30;
	}
	public void setR47_DAY15_30(BigDecimal r47_DAY15_30) {
		R47_DAY15_30 = r47_DAY15_30;
	}
	public BigDecimal getR47_DAY31_TO_2M() {
		return R47_DAY31_TO_2M;
	}
	public void setR47_DAY31_TO_2M(BigDecimal r47_DAY31_TO_2M) {
		R47_DAY31_TO_2M = r47_DAY31_TO_2M;
	}
	public BigDecimal getR47_MORE2M_TO_3M() {
		return R47_MORE2M_TO_3M;
	}
	public void setR47_MORE2M_TO_3M(BigDecimal r47_MORE2M_TO_3M) {
		R47_MORE2M_TO_3M = r47_MORE2M_TO_3M;
	}
	public BigDecimal getR47_OVER3M_TO_6M() {
		return R47_OVER3M_TO_6M;
	}
	public void setR47_OVER3M_TO_6M(BigDecimal r47_OVER3M_TO_6M) {
		R47_OVER3M_TO_6M = r47_OVER3M_TO_6M;
	}
	public BigDecimal getR47_OVER6M_TO_1Y() {
		return R47_OVER6M_TO_1Y;
	}
	public void setR47_OVER6M_TO_1Y(BigDecimal r47_OVER6M_TO_1Y) {
		R47_OVER6M_TO_1Y = r47_OVER6M_TO_1Y;
	}
	public BigDecimal getR47_OVER1Y_TO_3Y() {
		return R47_OVER1Y_TO_3Y;
	}
	public void setR47_OVER1Y_TO_3Y(BigDecimal r47_OVER1Y_TO_3Y) {
		R47_OVER1Y_TO_3Y = r47_OVER1Y_TO_3Y;
	}
	public BigDecimal getR47_OVER3Y_TO_5Y() {
		return R47_OVER3Y_TO_5Y;
	}
	public void setR47_OVER3Y_TO_5Y(BigDecimal r47_OVER3Y_TO_5Y) {
		R47_OVER3Y_TO_5Y = r47_OVER3Y_TO_5Y;
	}
	public BigDecimal getR47_OVER5Y() {
		return R47_OVER5Y;
	}
	public void setR47_OVER5Y(BigDecimal r47_OVER5Y) {
		R47_OVER5Y = r47_OVER5Y;
	}
	public BigDecimal getR47_TOTAL() {
		return R47_TOTAL;
	}
	public void setR47_TOTAL(BigDecimal r47_TOTAL) {
		R47_TOTAL = r47_TOTAL;
	}
	public String getR48_PRODUCT() {
		return R48_PRODUCT;
	}
	public void setR48_PRODUCT(String r48_PRODUCT) {
		R48_PRODUCT = r48_PRODUCT;
	}
	public BigDecimal getR48_DAY1() {
		return R48_DAY1;
	}
	public void setR48_DAY1(BigDecimal r48_DAY1) {
		R48_DAY1 = r48_DAY1;
	}
	public BigDecimal getR48_DAY2_7() {
		return R48_DAY2_7;
	}
	public void setR48_DAY2_7(BigDecimal r48_DAY2_7) {
		R48_DAY2_7 = r48_DAY2_7;
	}
	public BigDecimal getR48_DAY8_14() {
		return R48_DAY8_14;
	}
	public void setR48_DAY8_14(BigDecimal r48_DAY8_14) {
		R48_DAY8_14 = r48_DAY8_14;
	}
	public BigDecimal getR48_DAY15_30() {
		return R48_DAY15_30;
	}
	public void setR48_DAY15_30(BigDecimal r48_DAY15_30) {
		R48_DAY15_30 = r48_DAY15_30;
	}
	public BigDecimal getR48_DAY31_TO_2M() {
		return R48_DAY31_TO_2M;
	}
	public void setR48_DAY31_TO_2M(BigDecimal r48_DAY31_TO_2M) {
		R48_DAY31_TO_2M = r48_DAY31_TO_2M;
	}
	public BigDecimal getR48_MORE2M_TO_3M() {
		return R48_MORE2M_TO_3M;
	}
	public void setR48_MORE2M_TO_3M(BigDecimal r48_MORE2M_TO_3M) {
		R48_MORE2M_TO_3M = r48_MORE2M_TO_3M;
	}
	public BigDecimal getR48_OVER3M_TO_6M() {
		return R48_OVER3M_TO_6M;
	}
	public void setR48_OVER3M_TO_6M(BigDecimal r48_OVER3M_TO_6M) {
		R48_OVER3M_TO_6M = r48_OVER3M_TO_6M;
	}
	public BigDecimal getR48_OVER6M_TO_1Y() {
		return R48_OVER6M_TO_1Y;
	}
	public void setR48_OVER6M_TO_1Y(BigDecimal r48_OVER6M_TO_1Y) {
		R48_OVER6M_TO_1Y = r48_OVER6M_TO_1Y;
	}
	public BigDecimal getR48_OVER1Y_TO_3Y() {
		return R48_OVER1Y_TO_3Y;
	}
	public void setR48_OVER1Y_TO_3Y(BigDecimal r48_OVER1Y_TO_3Y) {
		R48_OVER1Y_TO_3Y = r48_OVER1Y_TO_3Y;
	}
	public BigDecimal getR48_OVER3Y_TO_5Y() {
		return R48_OVER3Y_TO_5Y;
	}
	public void setR48_OVER3Y_TO_5Y(BigDecimal r48_OVER3Y_TO_5Y) {
		R48_OVER3Y_TO_5Y = r48_OVER3Y_TO_5Y;
	}
	public BigDecimal getR48_OVER5Y() {
		return R48_OVER5Y;
	}
	public void setR48_OVER5Y(BigDecimal r48_OVER5Y) {
		R48_OVER5Y = r48_OVER5Y;
	}
	public BigDecimal getR48_TOTAL() {
		return R48_TOTAL;
	}
	public void setR48_TOTAL(BigDecimal r48_TOTAL) {
		R48_TOTAL = r48_TOTAL;
	}
	public String getR49_PRODUCT() {
		return R49_PRODUCT;
	}
	public void setR49_PRODUCT(String r49_PRODUCT) {
		R49_PRODUCT = r49_PRODUCT;
	}
	public BigDecimal getR49_DAY1() {
		return R49_DAY1;
	}
	public void setR49_DAY1(BigDecimal r49_DAY1) {
		R49_DAY1 = r49_DAY1;
	}
	public BigDecimal getR49_DAY2_7() {
		return R49_DAY2_7;
	}
	public void setR49_DAY2_7(BigDecimal r49_DAY2_7) {
		R49_DAY2_7 = r49_DAY2_7;
	}
	public BigDecimal getR49_DAY8_14() {
		return R49_DAY8_14;
	}
	public void setR49_DAY8_14(BigDecimal r49_DAY8_14) {
		R49_DAY8_14 = r49_DAY8_14;
	}
	public BigDecimal getR49_DAY15_30() {
		return R49_DAY15_30;
	}
	public void setR49_DAY15_30(BigDecimal r49_DAY15_30) {
		R49_DAY15_30 = r49_DAY15_30;
	}
	public BigDecimal getR49_DAY31_TO_2M() {
		return R49_DAY31_TO_2M;
	}
	public void setR49_DAY31_TO_2M(BigDecimal r49_DAY31_TO_2M) {
		R49_DAY31_TO_2M = r49_DAY31_TO_2M;
	}
	public BigDecimal getR49_MORE2M_TO_3M() {
		return R49_MORE2M_TO_3M;
	}
	public void setR49_MORE2M_TO_3M(BigDecimal r49_MORE2M_TO_3M) {
		R49_MORE2M_TO_3M = r49_MORE2M_TO_3M;
	}
	public BigDecimal getR49_OVER3M_TO_6M() {
		return R49_OVER3M_TO_6M;
	}
	public void setR49_OVER3M_TO_6M(BigDecimal r49_OVER3M_TO_6M) {
		R49_OVER3M_TO_6M = r49_OVER3M_TO_6M;
	}
	public BigDecimal getR49_OVER6M_TO_1Y() {
		return R49_OVER6M_TO_1Y;
	}
	public void setR49_OVER6M_TO_1Y(BigDecimal r49_OVER6M_TO_1Y) {
		R49_OVER6M_TO_1Y = r49_OVER6M_TO_1Y;
	}
	public BigDecimal getR49_OVER1Y_TO_3Y() {
		return R49_OVER1Y_TO_3Y;
	}
	public void setR49_OVER1Y_TO_3Y(BigDecimal r49_OVER1Y_TO_3Y) {
		R49_OVER1Y_TO_3Y = r49_OVER1Y_TO_3Y;
	}
	public BigDecimal getR49_OVER3Y_TO_5Y() {
		return R49_OVER3Y_TO_5Y;
	}
	public void setR49_OVER3Y_TO_5Y(BigDecimal r49_OVER3Y_TO_5Y) {
		R49_OVER3Y_TO_5Y = r49_OVER3Y_TO_5Y;
	}
	public BigDecimal getR49_OVER5Y() {
		return R49_OVER5Y;
	}
	public void setR49_OVER5Y(BigDecimal r49_OVER5Y) {
		R49_OVER5Y = r49_OVER5Y;
	}
	public BigDecimal getR49_TOTAL() {
		return R49_TOTAL;
	}
	public void setR49_TOTAL(BigDecimal r49_TOTAL) {
		R49_TOTAL = r49_TOTAL;
	}
	public String getR50_PRODUCT() {
		return R50_PRODUCT;
	}
	public void setR50_PRODUCT(String r50_PRODUCT) {
		R50_PRODUCT = r50_PRODUCT;
	}
	public BigDecimal getR50_DAY1() {
		return R50_DAY1;
	}
	public void setR50_DAY1(BigDecimal r50_DAY1) {
		R50_DAY1 = r50_DAY1;
	}
	public BigDecimal getR50_DAY2_7() {
		return R50_DAY2_7;
	}
	public void setR50_DAY2_7(BigDecimal r50_DAY2_7) {
		R50_DAY2_7 = r50_DAY2_7;
	}
	public BigDecimal getR50_DAY8_14() {
		return R50_DAY8_14;
	}
	public void setR50_DAY8_14(BigDecimal r50_DAY8_14) {
		R50_DAY8_14 = r50_DAY8_14;
	}
	public BigDecimal getR50_DAY15_30() {
		return R50_DAY15_30;
	}
	public void setR50_DAY15_30(BigDecimal r50_DAY15_30) {
		R50_DAY15_30 = r50_DAY15_30;
	}
	public BigDecimal getR50_DAY31_TO_2M() {
		return R50_DAY31_TO_2M;
	}
	public void setR50_DAY31_TO_2M(BigDecimal r50_DAY31_TO_2M) {
		R50_DAY31_TO_2M = r50_DAY31_TO_2M;
	}
	public BigDecimal getR50_MORE2M_TO_3M() {
		return R50_MORE2M_TO_3M;
	}
	public void setR50_MORE2M_TO_3M(BigDecimal r50_MORE2M_TO_3M) {
		R50_MORE2M_TO_3M = r50_MORE2M_TO_3M;
	}
	public BigDecimal getR50_OVER3M_TO_6M() {
		return R50_OVER3M_TO_6M;
	}
	public void setR50_OVER3M_TO_6M(BigDecimal r50_OVER3M_TO_6M) {
		R50_OVER3M_TO_6M = r50_OVER3M_TO_6M;
	}
	public BigDecimal getR50_OVER6M_TO_1Y() {
		return R50_OVER6M_TO_1Y;
	}
	public void setR50_OVER6M_TO_1Y(BigDecimal r50_OVER6M_TO_1Y) {
		R50_OVER6M_TO_1Y = r50_OVER6M_TO_1Y;
	}
	public BigDecimal getR50_OVER1Y_TO_3Y() {
		return R50_OVER1Y_TO_3Y;
	}
	public void setR50_OVER1Y_TO_3Y(BigDecimal r50_OVER1Y_TO_3Y) {
		R50_OVER1Y_TO_3Y = r50_OVER1Y_TO_3Y;
	}
	public BigDecimal getR50_OVER3Y_TO_5Y() {
		return R50_OVER3Y_TO_5Y;
	}
	public void setR50_OVER3Y_TO_5Y(BigDecimal r50_OVER3Y_TO_5Y) {
		R50_OVER3Y_TO_5Y = r50_OVER3Y_TO_5Y;
	}
	public BigDecimal getR50_OVER5Y() {
		return R50_OVER5Y;
	}
	public void setR50_OVER5Y(BigDecimal r50_OVER5Y) {
		R50_OVER5Y = r50_OVER5Y;
	}
	public BigDecimal getR50_TOTAL() {
		return R50_TOTAL;
	}
	public void setR50_TOTAL(BigDecimal r50_TOTAL) {
		R50_TOTAL = r50_TOTAL;
	}
	public String getR51_PRODUCT() {
		return R51_PRODUCT;
	}
	public void setR51_PRODUCT(String r51_PRODUCT) {
		R51_PRODUCT = r51_PRODUCT;
	}
	public BigDecimal getR51_DAY1() {
		return R51_DAY1;
	}
	public void setR51_DAY1(BigDecimal r51_DAY1) {
		R51_DAY1 = r51_DAY1;
	}
	public BigDecimal getR51_DAY2_7() {
		return R51_DAY2_7;
	}
	public void setR51_DAY2_7(BigDecimal r51_DAY2_7) {
		R51_DAY2_7 = r51_DAY2_7;
	}
	public BigDecimal getR51_DAY8_14() {
		return R51_DAY8_14;
	}
	public void setR51_DAY8_14(BigDecimal r51_DAY8_14) {
		R51_DAY8_14 = r51_DAY8_14;
	}
	public BigDecimal getR51_DAY15_30() {
		return R51_DAY15_30;
	}
	public void setR51_DAY15_30(BigDecimal r51_DAY15_30) {
		R51_DAY15_30 = r51_DAY15_30;
	}
	public BigDecimal getR51_DAY31_TO_2M() {
		return R51_DAY31_TO_2M;
	}
	public void setR51_DAY31_TO_2M(BigDecimal r51_DAY31_TO_2M) {
		R51_DAY31_TO_2M = r51_DAY31_TO_2M;
	}
	public BigDecimal getR51_MORE2M_TO_3M() {
		return R51_MORE2M_TO_3M;
	}
	public void setR51_MORE2M_TO_3M(BigDecimal r51_MORE2M_TO_3M) {
		R51_MORE2M_TO_3M = r51_MORE2M_TO_3M;
	}
	public BigDecimal getR51_OVER3M_TO_6M() {
		return R51_OVER3M_TO_6M;
	}
	public void setR51_OVER3M_TO_6M(BigDecimal r51_OVER3M_TO_6M) {
		R51_OVER3M_TO_6M = r51_OVER3M_TO_6M;
	}
	public BigDecimal getR51_OVER6M_TO_1Y() {
		return R51_OVER6M_TO_1Y;
	}
	public void setR51_OVER6M_TO_1Y(BigDecimal r51_OVER6M_TO_1Y) {
		R51_OVER6M_TO_1Y = r51_OVER6M_TO_1Y;
	}
	public BigDecimal getR51_OVER1Y_TO_3Y() {
		return R51_OVER1Y_TO_3Y;
	}
	public void setR51_OVER1Y_TO_3Y(BigDecimal r51_OVER1Y_TO_3Y) {
		R51_OVER1Y_TO_3Y = r51_OVER1Y_TO_3Y;
	}
	public BigDecimal getR51_OVER3Y_TO_5Y() {
		return R51_OVER3Y_TO_5Y;
	}
	public void setR51_OVER3Y_TO_5Y(BigDecimal r51_OVER3Y_TO_5Y) {
		R51_OVER3Y_TO_5Y = r51_OVER3Y_TO_5Y;
	}
	public BigDecimal getR51_OVER5Y() {
		return R51_OVER5Y;
	}
	public void setR51_OVER5Y(BigDecimal r51_OVER5Y) {
		R51_OVER5Y = r51_OVER5Y;
	}
	public BigDecimal getR51_TOTAL() {
		return R51_TOTAL;
	}
	public void setR51_TOTAL(BigDecimal r51_TOTAL) {
		R51_TOTAL = r51_TOTAL;
	}
	public String getR52_PRODUCT() {
		return R52_PRODUCT;
	}
	public void setR52_PRODUCT(String r52_PRODUCT) {
		R52_PRODUCT = r52_PRODUCT;
	}
	public BigDecimal getR52_DAY1() {
		return R52_DAY1;
	}
	public void setR52_DAY1(BigDecimal r52_DAY1) {
		R52_DAY1 = r52_DAY1;
	}
	public BigDecimal getR52_DAY2_7() {
		return R52_DAY2_7;
	}
	public void setR52_DAY2_7(BigDecimal r52_DAY2_7) {
		R52_DAY2_7 = r52_DAY2_7;
	}
	public BigDecimal getR52_DAY8_14() {
		return R52_DAY8_14;
	}
	public void setR52_DAY8_14(BigDecimal r52_DAY8_14) {
		R52_DAY8_14 = r52_DAY8_14;
	}
	public BigDecimal getR52_DAY15_30() {
		return R52_DAY15_30;
	}
	public void setR52_DAY15_30(BigDecimal r52_DAY15_30) {
		R52_DAY15_30 = r52_DAY15_30;
	}
	public BigDecimal getR52_DAY31_TO_2M() {
		return R52_DAY31_TO_2M;
	}
	public void setR52_DAY31_TO_2M(BigDecimal r52_DAY31_TO_2M) {
		R52_DAY31_TO_2M = r52_DAY31_TO_2M;
	}
	public BigDecimal getR52_MORE2M_TO_3M() {
		return R52_MORE2M_TO_3M;
	}
	public void setR52_MORE2M_TO_3M(BigDecimal r52_MORE2M_TO_3M) {
		R52_MORE2M_TO_3M = r52_MORE2M_TO_3M;
	}
	public BigDecimal getR52_OVER3M_TO_6M() {
		return R52_OVER3M_TO_6M;
	}
	public void setR52_OVER3M_TO_6M(BigDecimal r52_OVER3M_TO_6M) {
		R52_OVER3M_TO_6M = r52_OVER3M_TO_6M;
	}
	public BigDecimal getR52_OVER6M_TO_1Y() {
		return R52_OVER6M_TO_1Y;
	}
	public void setR52_OVER6M_TO_1Y(BigDecimal r52_OVER6M_TO_1Y) {
		R52_OVER6M_TO_1Y = r52_OVER6M_TO_1Y;
	}
	public BigDecimal getR52_OVER1Y_TO_3Y() {
		return R52_OVER1Y_TO_3Y;
	}
	public void setR52_OVER1Y_TO_3Y(BigDecimal r52_OVER1Y_TO_3Y) {
		R52_OVER1Y_TO_3Y = r52_OVER1Y_TO_3Y;
	}
	public BigDecimal getR52_OVER3Y_TO_5Y() {
		return R52_OVER3Y_TO_5Y;
	}
	public void setR52_OVER3Y_TO_5Y(BigDecimal r52_OVER3Y_TO_5Y) {
		R52_OVER3Y_TO_5Y = r52_OVER3Y_TO_5Y;
	}
	public BigDecimal getR52_OVER5Y() {
		return R52_OVER5Y;
	}
	public void setR52_OVER5Y(BigDecimal r52_OVER5Y) {
		R52_OVER5Y = r52_OVER5Y;
	}
	public BigDecimal getR52_TOTAL() {
		return R52_TOTAL;
	}
	public void setR52_TOTAL(BigDecimal r52_TOTAL) {
		R52_TOTAL = r52_TOTAL;
	}
	public String getR53_PRODUCT() {
		return R53_PRODUCT;
	}
	public void setR53_PRODUCT(String r53_PRODUCT) {
		R53_PRODUCT = r53_PRODUCT;
	}
	public BigDecimal getR53_DAY1() {
		return R53_DAY1;
	}
	public void setR53_DAY1(BigDecimal r53_DAY1) {
		R53_DAY1 = r53_DAY1;
	}
	public BigDecimal getR53_DAY2_7() {
		return R53_DAY2_7;
	}
	public void setR53_DAY2_7(BigDecimal r53_DAY2_7) {
		R53_DAY2_7 = r53_DAY2_7;
	}
	public BigDecimal getR53_DAY8_14() {
		return R53_DAY8_14;
	}
	public void setR53_DAY8_14(BigDecimal r53_DAY8_14) {
		R53_DAY8_14 = r53_DAY8_14;
	}
	public BigDecimal getR53_DAY15_30() {
		return R53_DAY15_30;
	}
	public void setR53_DAY15_30(BigDecimal r53_DAY15_30) {
		R53_DAY15_30 = r53_DAY15_30;
	}
	public BigDecimal getR53_DAY31_TO_2M() {
		return R53_DAY31_TO_2M;
	}
	public void setR53_DAY31_TO_2M(BigDecimal r53_DAY31_TO_2M) {
		R53_DAY31_TO_2M = r53_DAY31_TO_2M;
	}
	public BigDecimal getR53_MORE2M_TO_3M() {
		return R53_MORE2M_TO_3M;
	}
	public void setR53_MORE2M_TO_3M(BigDecimal r53_MORE2M_TO_3M) {
		R53_MORE2M_TO_3M = r53_MORE2M_TO_3M;
	}
	public BigDecimal getR53_OVER3M_TO_6M() {
		return R53_OVER3M_TO_6M;
	}
	public void setR53_OVER3M_TO_6M(BigDecimal r53_OVER3M_TO_6M) {
		R53_OVER3M_TO_6M = r53_OVER3M_TO_6M;
	}
	public BigDecimal getR53_OVER6M_TO_1Y() {
		return R53_OVER6M_TO_1Y;
	}
	public void setR53_OVER6M_TO_1Y(BigDecimal r53_OVER6M_TO_1Y) {
		R53_OVER6M_TO_1Y = r53_OVER6M_TO_1Y;
	}
	public BigDecimal getR53_OVER1Y_TO_3Y() {
		return R53_OVER1Y_TO_3Y;
	}
	public void setR53_OVER1Y_TO_3Y(BigDecimal r53_OVER1Y_TO_3Y) {
		R53_OVER1Y_TO_3Y = r53_OVER1Y_TO_3Y;
	}
	public BigDecimal getR53_OVER3Y_TO_5Y() {
		return R53_OVER3Y_TO_5Y;
	}
	public void setR53_OVER3Y_TO_5Y(BigDecimal r53_OVER3Y_TO_5Y) {
		R53_OVER3Y_TO_5Y = r53_OVER3Y_TO_5Y;
	}
	public BigDecimal getR53_OVER5Y() {
		return R53_OVER5Y;
	}
	public void setR53_OVER5Y(BigDecimal r53_OVER5Y) {
		R53_OVER5Y = r53_OVER5Y;
	}
	public BigDecimal getR53_TOTAL() {
		return R53_TOTAL;
	}
	public void setR53_TOTAL(BigDecimal r53_TOTAL) {
		R53_TOTAL = r53_TOTAL;
	}
	public String getR54_PRODUCT() {
		return R54_PRODUCT;
	}
	public void setR54_PRODUCT(String r54_PRODUCT) {
		R54_PRODUCT = r54_PRODUCT;
	}
	public BigDecimal getR54_DAY1() {
		return R54_DAY1;
	}
	public void setR54_DAY1(BigDecimal r54_DAY1) {
		R54_DAY1 = r54_DAY1;
	}
	public BigDecimal getR54_DAY2_7() {
		return R54_DAY2_7;
	}
	public void setR54_DAY2_7(BigDecimal r54_DAY2_7) {
		R54_DAY2_7 = r54_DAY2_7;
	}
	public BigDecimal getR54_DAY8_14() {
		return R54_DAY8_14;
	}
	public void setR54_DAY8_14(BigDecimal r54_DAY8_14) {
		R54_DAY8_14 = r54_DAY8_14;
	}
	public BigDecimal getR54_DAY15_30() {
		return R54_DAY15_30;
	}
	public void setR54_DAY15_30(BigDecimal r54_DAY15_30) {
		R54_DAY15_30 = r54_DAY15_30;
	}
	public BigDecimal getR54_DAY31_TO_2M() {
		return R54_DAY31_TO_2M;
	}
	public void setR54_DAY31_TO_2M(BigDecimal r54_DAY31_TO_2M) {
		R54_DAY31_TO_2M = r54_DAY31_TO_2M;
	}
	public BigDecimal getR54_MORE2M_TO_3M() {
		return R54_MORE2M_TO_3M;
	}
	public void setR54_MORE2M_TO_3M(BigDecimal r54_MORE2M_TO_3M) {
		R54_MORE2M_TO_3M = r54_MORE2M_TO_3M;
	}
	public BigDecimal getR54_OVER3M_TO_6M() {
		return R54_OVER3M_TO_6M;
	}
	public void setR54_OVER3M_TO_6M(BigDecimal r54_OVER3M_TO_6M) {
		R54_OVER3M_TO_6M = r54_OVER3M_TO_6M;
	}
	public BigDecimal getR54_OVER6M_TO_1Y() {
		return R54_OVER6M_TO_1Y;
	}
	public void setR54_OVER6M_TO_1Y(BigDecimal r54_OVER6M_TO_1Y) {
		R54_OVER6M_TO_1Y = r54_OVER6M_TO_1Y;
	}
	public BigDecimal getR54_OVER1Y_TO_3Y() {
		return R54_OVER1Y_TO_3Y;
	}
	public void setR54_OVER1Y_TO_3Y(BigDecimal r54_OVER1Y_TO_3Y) {
		R54_OVER1Y_TO_3Y = r54_OVER1Y_TO_3Y;
	}
	public BigDecimal getR54_OVER3Y_TO_5Y() {
		return R54_OVER3Y_TO_5Y;
	}
	public void setR54_OVER3Y_TO_5Y(BigDecimal r54_OVER3Y_TO_5Y) {
		R54_OVER3Y_TO_5Y = r54_OVER3Y_TO_5Y;
	}
	public BigDecimal getR54_OVER5Y() {
		return R54_OVER5Y;
	}
	public void setR54_OVER5Y(BigDecimal r54_OVER5Y) {
		R54_OVER5Y = r54_OVER5Y;
	}
	public BigDecimal getR54_TOTAL() {
		return R54_TOTAL;
	}
	public void setR54_TOTAL(BigDecimal r54_TOTAL) {
		R54_TOTAL = r54_TOTAL;
	}
	public String getR55_PRODUCT() {
		return R55_PRODUCT;
	}
	public void setR55_PRODUCT(String r55_PRODUCT) {
		R55_PRODUCT = r55_PRODUCT;
	}
	public BigDecimal getR55_DAY1() {
		return R55_DAY1;
	}
	public void setR55_DAY1(BigDecimal r55_DAY1) {
		R55_DAY1 = r55_DAY1;
	}
	public BigDecimal getR55_DAY2_7() {
		return R55_DAY2_7;
	}
	public void setR55_DAY2_7(BigDecimal r55_DAY2_7) {
		R55_DAY2_7 = r55_DAY2_7;
	}
	public BigDecimal getR55_DAY8_14() {
		return R55_DAY8_14;
	}
	public void setR55_DAY8_14(BigDecimal r55_DAY8_14) {
		R55_DAY8_14 = r55_DAY8_14;
	}
	public BigDecimal getR55_DAY15_30() {
		return R55_DAY15_30;
	}
	public void setR55_DAY15_30(BigDecimal r55_DAY15_30) {
		R55_DAY15_30 = r55_DAY15_30;
	}
	public BigDecimal getR55_DAY31_TO_2M() {
		return R55_DAY31_TO_2M;
	}
	public void setR55_DAY31_TO_2M(BigDecimal r55_DAY31_TO_2M) {
		R55_DAY31_TO_2M = r55_DAY31_TO_2M;
	}
	public BigDecimal getR55_MORE2M_TO_3M() {
		return R55_MORE2M_TO_3M;
	}
	public void setR55_MORE2M_TO_3M(BigDecimal r55_MORE2M_TO_3M) {
		R55_MORE2M_TO_3M = r55_MORE2M_TO_3M;
	}
	public BigDecimal getR55_OVER3M_TO_6M() {
		return R55_OVER3M_TO_6M;
	}
	public void setR55_OVER3M_TO_6M(BigDecimal r55_OVER3M_TO_6M) {
		R55_OVER3M_TO_6M = r55_OVER3M_TO_6M;
	}
	public BigDecimal getR55_OVER6M_TO_1Y() {
		return R55_OVER6M_TO_1Y;
	}
	public void setR55_OVER6M_TO_1Y(BigDecimal r55_OVER6M_TO_1Y) {
		R55_OVER6M_TO_1Y = r55_OVER6M_TO_1Y;
	}
	public BigDecimal getR55_OVER1Y_TO_3Y() {
		return R55_OVER1Y_TO_3Y;
	}
	public void setR55_OVER1Y_TO_3Y(BigDecimal r55_OVER1Y_TO_3Y) {
		R55_OVER1Y_TO_3Y = r55_OVER1Y_TO_3Y;
	}
	public BigDecimal getR55_OVER3Y_TO_5Y() {
		return R55_OVER3Y_TO_5Y;
	}
	public void setR55_OVER3Y_TO_5Y(BigDecimal r55_OVER3Y_TO_5Y) {
		R55_OVER3Y_TO_5Y = r55_OVER3Y_TO_5Y;
	}
	public BigDecimal getR55_OVER5Y() {
		return R55_OVER5Y;
	}
	public void setR55_OVER5Y(BigDecimal r55_OVER5Y) {
		R55_OVER5Y = r55_OVER5Y;
	}
	public BigDecimal getR55_TOTAL() {
		return R55_TOTAL;
	}
	public void setR55_TOTAL(BigDecimal r55_TOTAL) {
		R55_TOTAL = r55_TOTAL;
	}
	public String getR56_PRODUCT() {
		return R56_PRODUCT;
	}
	public void setR56_PRODUCT(String r56_PRODUCT) {
		R56_PRODUCT = r56_PRODUCT;
	}
	public BigDecimal getR56_DAY1() {
		return R56_DAY1;
	}
	public void setR56_DAY1(BigDecimal r56_DAY1) {
		R56_DAY1 = r56_DAY1;
	}
	public BigDecimal getR56_DAY2_7() {
		return R56_DAY2_7;
	}
	public void setR56_DAY2_7(BigDecimal r56_DAY2_7) {
		R56_DAY2_7 = r56_DAY2_7;
	}
	public BigDecimal getR56_DAY8_14() {
		return R56_DAY8_14;
	}
	public void setR56_DAY8_14(BigDecimal r56_DAY8_14) {
		R56_DAY8_14 = r56_DAY8_14;
	}
	public BigDecimal getR56_DAY15_30() {
		return R56_DAY15_30;
	}
	public void setR56_DAY15_30(BigDecimal r56_DAY15_30) {
		R56_DAY15_30 = r56_DAY15_30;
	}
	public BigDecimal getR56_DAY31_TO_2M() {
		return R56_DAY31_TO_2M;
	}
	public void setR56_DAY31_TO_2M(BigDecimal r56_DAY31_TO_2M) {
		R56_DAY31_TO_2M = r56_DAY31_TO_2M;
	}
	public BigDecimal getR56_MORE2M_TO_3M() {
		return R56_MORE2M_TO_3M;
	}
	public void setR56_MORE2M_TO_3M(BigDecimal r56_MORE2M_TO_3M) {
		R56_MORE2M_TO_3M = r56_MORE2M_TO_3M;
	}
	public BigDecimal getR56_OVER3M_TO_6M() {
		return R56_OVER3M_TO_6M;
	}
	public void setR56_OVER3M_TO_6M(BigDecimal r56_OVER3M_TO_6M) {
		R56_OVER3M_TO_6M = r56_OVER3M_TO_6M;
	}
	public BigDecimal getR56_OVER6M_TO_1Y() {
		return R56_OVER6M_TO_1Y;
	}
	public void setR56_OVER6M_TO_1Y(BigDecimal r56_OVER6M_TO_1Y) {
		R56_OVER6M_TO_1Y = r56_OVER6M_TO_1Y;
	}
	public BigDecimal getR56_OVER1Y_TO_3Y() {
		return R56_OVER1Y_TO_3Y;
	}
	public void setR56_OVER1Y_TO_3Y(BigDecimal r56_OVER1Y_TO_3Y) {
		R56_OVER1Y_TO_3Y = r56_OVER1Y_TO_3Y;
	}
	public BigDecimal getR56_OVER3Y_TO_5Y() {
		return R56_OVER3Y_TO_5Y;
	}
	public void setR56_OVER3Y_TO_5Y(BigDecimal r56_OVER3Y_TO_5Y) {
		R56_OVER3Y_TO_5Y = r56_OVER3Y_TO_5Y;
	}
	public BigDecimal getR56_OVER5Y() {
		return R56_OVER5Y;
	}
	public void setR56_OVER5Y(BigDecimal r56_OVER5Y) {
		R56_OVER5Y = r56_OVER5Y;
	}
	public BigDecimal getR56_TOTAL() {
		return R56_TOTAL;
	}
	public void setR56_TOTAL(BigDecimal r56_TOTAL) {
		R56_TOTAL = r56_TOTAL;
	}
	public String getR57_PRODUCT() {
		return R57_PRODUCT;
	}
	public void setR57_PRODUCT(String r57_PRODUCT) {
		R57_PRODUCT = r57_PRODUCT;
	}
	public BigDecimal getR57_DAY1() {
		return R57_DAY1;
	}
	public void setR57_DAY1(BigDecimal r57_DAY1) {
		R57_DAY1 = r57_DAY1;
	}
	public BigDecimal getR57_DAY2_7() {
		return R57_DAY2_7;
	}
	public void setR57_DAY2_7(BigDecimal r57_DAY2_7) {
		R57_DAY2_7 = r57_DAY2_7;
	}
	public BigDecimal getR57_DAY8_14() {
		return R57_DAY8_14;
	}
	public void setR57_DAY8_14(BigDecimal r57_DAY8_14) {
		R57_DAY8_14 = r57_DAY8_14;
	}
	public BigDecimal getR57_DAY15_30() {
		return R57_DAY15_30;
	}
	public void setR57_DAY15_30(BigDecimal r57_DAY15_30) {
		R57_DAY15_30 = r57_DAY15_30;
	}
	public BigDecimal getR57_DAY31_TO_2M() {
		return R57_DAY31_TO_2M;
	}
	public void setR57_DAY31_TO_2M(BigDecimal r57_DAY31_TO_2M) {
		R57_DAY31_TO_2M = r57_DAY31_TO_2M;
	}
	public BigDecimal getR57_MORE2M_TO_3M() {
		return R57_MORE2M_TO_3M;
	}
	public void setR57_MORE2M_TO_3M(BigDecimal r57_MORE2M_TO_3M) {
		R57_MORE2M_TO_3M = r57_MORE2M_TO_3M;
	}
	public BigDecimal getR57_OVER3M_TO_6M() {
		return R57_OVER3M_TO_6M;
	}
	public void setR57_OVER3M_TO_6M(BigDecimal r57_OVER3M_TO_6M) {
		R57_OVER3M_TO_6M = r57_OVER3M_TO_6M;
	}
	public BigDecimal getR57_OVER6M_TO_1Y() {
		return R57_OVER6M_TO_1Y;
	}
	public void setR57_OVER6M_TO_1Y(BigDecimal r57_OVER6M_TO_1Y) {
		R57_OVER6M_TO_1Y = r57_OVER6M_TO_1Y;
	}
	public BigDecimal getR57_OVER1Y_TO_3Y() {
		return R57_OVER1Y_TO_3Y;
	}
	public void setR57_OVER1Y_TO_3Y(BigDecimal r57_OVER1Y_TO_3Y) {
		R57_OVER1Y_TO_3Y = r57_OVER1Y_TO_3Y;
	}
	public BigDecimal getR57_OVER3Y_TO_5Y() {
		return R57_OVER3Y_TO_5Y;
	}
	public void setR57_OVER3Y_TO_5Y(BigDecimal r57_OVER3Y_TO_5Y) {
		R57_OVER3Y_TO_5Y = r57_OVER3Y_TO_5Y;
	}
	public BigDecimal getR57_OVER5Y() {
		return R57_OVER5Y;
	}
	public void setR57_OVER5Y(BigDecimal r57_OVER5Y) {
		R57_OVER5Y = r57_OVER5Y;
	}
	public BigDecimal getR57_TOTAL() {
		return R57_TOTAL;
	}
	public void setR57_TOTAL(BigDecimal r57_TOTAL) {
		R57_TOTAL = r57_TOTAL;
	}
	public String getR58_PRODUCT() {
		return R58_PRODUCT;
	}
	public void setR58_PRODUCT(String r58_PRODUCT) {
		R58_PRODUCT = r58_PRODUCT;
	}
	public BigDecimal getR58_DAY1() {
		return R58_DAY1;
	}
	public void setR58_DAY1(BigDecimal r58_DAY1) {
		R58_DAY1 = r58_DAY1;
	}
	public BigDecimal getR58_DAY2_7() {
		return R58_DAY2_7;
	}
	public void setR58_DAY2_7(BigDecimal r58_DAY2_7) {
		R58_DAY2_7 = r58_DAY2_7;
	}
	public BigDecimal getR58_DAY8_14() {
		return R58_DAY8_14;
	}
	public void setR58_DAY8_14(BigDecimal r58_DAY8_14) {
		R58_DAY8_14 = r58_DAY8_14;
	}
	public BigDecimal getR58_DAY15_30() {
		return R58_DAY15_30;
	}
	public void setR58_DAY15_30(BigDecimal r58_DAY15_30) {
		R58_DAY15_30 = r58_DAY15_30;
	}
	public BigDecimal getR58_DAY31_TO_2M() {
		return R58_DAY31_TO_2M;
	}
	public void setR58_DAY31_TO_2M(BigDecimal r58_DAY31_TO_2M) {
		R58_DAY31_TO_2M = r58_DAY31_TO_2M;
	}
	public BigDecimal getR58_MORE2M_TO_3M() {
		return R58_MORE2M_TO_3M;
	}
	public void setR58_MORE2M_TO_3M(BigDecimal r58_MORE2M_TO_3M) {
		R58_MORE2M_TO_3M = r58_MORE2M_TO_3M;
	}
	public BigDecimal getR58_OVER3M_TO_6M() {
		return R58_OVER3M_TO_6M;
	}
	public void setR58_OVER3M_TO_6M(BigDecimal r58_OVER3M_TO_6M) {
		R58_OVER3M_TO_6M = r58_OVER3M_TO_6M;
	}
	public BigDecimal getR58_OVER6M_TO_1Y() {
		return R58_OVER6M_TO_1Y;
	}
	public void setR58_OVER6M_TO_1Y(BigDecimal r58_OVER6M_TO_1Y) {
		R58_OVER6M_TO_1Y = r58_OVER6M_TO_1Y;
	}
	public BigDecimal getR58_OVER1Y_TO_3Y() {
		return R58_OVER1Y_TO_3Y;
	}
	public void setR58_OVER1Y_TO_3Y(BigDecimal r58_OVER1Y_TO_3Y) {
		R58_OVER1Y_TO_3Y = r58_OVER1Y_TO_3Y;
	}
	public BigDecimal getR58_OVER3Y_TO_5Y() {
		return R58_OVER3Y_TO_5Y;
	}
	public void setR58_OVER3Y_TO_5Y(BigDecimal r58_OVER3Y_TO_5Y) {
		R58_OVER3Y_TO_5Y = r58_OVER3Y_TO_5Y;
	}
	public BigDecimal getR58_OVER5Y() {
		return R58_OVER5Y;
	}
	public void setR58_OVER5Y(BigDecimal r58_OVER5Y) {
		R58_OVER5Y = r58_OVER5Y;
	}
	public BigDecimal getR58_TOTAL() {
		return R58_TOTAL;
	}
	public void setR58_TOTAL(BigDecimal r58_TOTAL) {
		R58_TOTAL = r58_TOTAL;
	}
	public String getR59_PRODUCT() {
		return R59_PRODUCT;
	}
	public void setR59_PRODUCT(String r59_PRODUCT) {
		R59_PRODUCT = r59_PRODUCT;
	}
	public BigDecimal getR59_DAY1() {
		return R59_DAY1;
	}
	public void setR59_DAY1(BigDecimal r59_DAY1) {
		R59_DAY1 = r59_DAY1;
	}
	public BigDecimal getR59_DAY2_7() {
		return R59_DAY2_7;
	}
	public void setR59_DAY2_7(BigDecimal r59_DAY2_7) {
		R59_DAY2_7 = r59_DAY2_7;
	}
	public BigDecimal getR59_DAY8_14() {
		return R59_DAY8_14;
	}
	public void setR59_DAY8_14(BigDecimal r59_DAY8_14) {
		R59_DAY8_14 = r59_DAY8_14;
	}
	public BigDecimal getR59_DAY15_30() {
		return R59_DAY15_30;
	}
	public void setR59_DAY15_30(BigDecimal r59_DAY15_30) {
		R59_DAY15_30 = r59_DAY15_30;
	}
	public BigDecimal getR59_DAY31_TO_2M() {
		return R59_DAY31_TO_2M;
	}
	public void setR59_DAY31_TO_2M(BigDecimal r59_DAY31_TO_2M) {
		R59_DAY31_TO_2M = r59_DAY31_TO_2M;
	}
	public BigDecimal getR59_MORE2M_TO_3M() {
		return R59_MORE2M_TO_3M;
	}
	public void setR59_MORE2M_TO_3M(BigDecimal r59_MORE2M_TO_3M) {
		R59_MORE2M_TO_3M = r59_MORE2M_TO_3M;
	}
	public BigDecimal getR59_OVER3M_TO_6M() {
		return R59_OVER3M_TO_6M;
	}
	public void setR59_OVER3M_TO_6M(BigDecimal r59_OVER3M_TO_6M) {
		R59_OVER3M_TO_6M = r59_OVER3M_TO_6M;
	}
	public BigDecimal getR59_OVER6M_TO_1Y() {
		return R59_OVER6M_TO_1Y;
	}
	public void setR59_OVER6M_TO_1Y(BigDecimal r59_OVER6M_TO_1Y) {
		R59_OVER6M_TO_1Y = r59_OVER6M_TO_1Y;
	}
	public BigDecimal getR59_OVER1Y_TO_3Y() {
		return R59_OVER1Y_TO_3Y;
	}
	public void setR59_OVER1Y_TO_3Y(BigDecimal r59_OVER1Y_TO_3Y) {
		R59_OVER1Y_TO_3Y = r59_OVER1Y_TO_3Y;
	}
	public BigDecimal getR59_OVER3Y_TO_5Y() {
		return R59_OVER3Y_TO_5Y;
	}
	public void setR59_OVER3Y_TO_5Y(BigDecimal r59_OVER3Y_TO_5Y) {
		R59_OVER3Y_TO_5Y = r59_OVER3Y_TO_5Y;
	}
	public BigDecimal getR59_OVER5Y() {
		return R59_OVER5Y;
	}
	public void setR59_OVER5Y(BigDecimal r59_OVER5Y) {
		R59_OVER5Y = r59_OVER5Y;
	}
	public BigDecimal getR59_TOTAL() {
		return R59_TOTAL;
	}
	public void setR59_TOTAL(BigDecimal r59_TOTAL) {
		R59_TOTAL = r59_TOTAL;
	}
	public String getR60_PRODUCT() {
		return R60_PRODUCT;
	}
	public void setR60_PRODUCT(String r60_PRODUCT) {
		R60_PRODUCT = r60_PRODUCT;
	}
	public BigDecimal getR60_DAY1() {
		return R60_DAY1;
	}
	public void setR60_DAY1(BigDecimal r60_DAY1) {
		R60_DAY1 = r60_DAY1;
	}
	public BigDecimal getR60_DAY2_7() {
		return R60_DAY2_7;
	}
	public void setR60_DAY2_7(BigDecimal r60_DAY2_7) {
		R60_DAY2_7 = r60_DAY2_7;
	}
	public BigDecimal getR60_DAY8_14() {
		return R60_DAY8_14;
	}
	public void setR60_DAY8_14(BigDecimal r60_DAY8_14) {
		R60_DAY8_14 = r60_DAY8_14;
	}
	public BigDecimal getR60_DAY15_30() {
		return R60_DAY15_30;
	}
	public void setR60_DAY15_30(BigDecimal r60_DAY15_30) {
		R60_DAY15_30 = r60_DAY15_30;
	}
	public BigDecimal getR60_DAY31_TO_2M() {
		return R60_DAY31_TO_2M;
	}
	public void setR60_DAY31_TO_2M(BigDecimal r60_DAY31_TO_2M) {
		R60_DAY31_TO_2M = r60_DAY31_TO_2M;
	}
	public BigDecimal getR60_MORE2M_TO_3M() {
		return R60_MORE2M_TO_3M;
	}
	public void setR60_MORE2M_TO_3M(BigDecimal r60_MORE2M_TO_3M) {
		R60_MORE2M_TO_3M = r60_MORE2M_TO_3M;
	}
	public BigDecimal getR60_OVER3M_TO_6M() {
		return R60_OVER3M_TO_6M;
	}
	public void setR60_OVER3M_TO_6M(BigDecimal r60_OVER3M_TO_6M) {
		R60_OVER3M_TO_6M = r60_OVER3M_TO_6M;
	}
	public BigDecimal getR60_OVER6M_TO_1Y() {
		return R60_OVER6M_TO_1Y;
	}
	public void setR60_OVER6M_TO_1Y(BigDecimal r60_OVER6M_TO_1Y) {
		R60_OVER6M_TO_1Y = r60_OVER6M_TO_1Y;
	}
	public BigDecimal getR60_OVER1Y_TO_3Y() {
		return R60_OVER1Y_TO_3Y;
	}
	public void setR60_OVER1Y_TO_3Y(BigDecimal r60_OVER1Y_TO_3Y) {
		R60_OVER1Y_TO_3Y = r60_OVER1Y_TO_3Y;
	}
	public BigDecimal getR60_OVER3Y_TO_5Y() {
		return R60_OVER3Y_TO_5Y;
	}
	public void setR60_OVER3Y_TO_5Y(BigDecimal r60_OVER3Y_TO_5Y) {
		R60_OVER3Y_TO_5Y = r60_OVER3Y_TO_5Y;
	}
	public BigDecimal getR60_OVER5Y() {
		return R60_OVER5Y;
	}
	public void setR60_OVER5Y(BigDecimal r60_OVER5Y) {
		R60_OVER5Y = r60_OVER5Y;
	}
	public BigDecimal getR60_TOTAL() {
		return R60_TOTAL;
	}
	public void setR60_TOTAL(BigDecimal r60_TOTAL) {
		R60_TOTAL = r60_TOTAL;
	}
	public String getR61_PRODUCT() {
		return R61_PRODUCT;
	}
	public void setR61_PRODUCT(String r61_PRODUCT) {
		R61_PRODUCT = r61_PRODUCT;
	}
	public BigDecimal getR61_DAY1() {
		return R61_DAY1;
	}
	public void setR61_DAY1(BigDecimal r61_DAY1) {
		R61_DAY1 = r61_DAY1;
	}
	public BigDecimal getR61_DAY2_7() {
		return R61_DAY2_7;
	}
	public void setR61_DAY2_7(BigDecimal r61_DAY2_7) {
		R61_DAY2_7 = r61_DAY2_7;
	}
	public BigDecimal getR61_DAY8_14() {
		return R61_DAY8_14;
	}
	public void setR61_DAY8_14(BigDecimal r61_DAY8_14) {
		R61_DAY8_14 = r61_DAY8_14;
	}
	public BigDecimal getR61_DAY15_30() {
		return R61_DAY15_30;
	}
	public void setR61_DAY15_30(BigDecimal r61_DAY15_30) {
		R61_DAY15_30 = r61_DAY15_30;
	}
	public BigDecimal getR61_DAY31_TO_2M() {
		return R61_DAY31_TO_2M;
	}
	public void setR61_DAY31_TO_2M(BigDecimal r61_DAY31_TO_2M) {
		R61_DAY31_TO_2M = r61_DAY31_TO_2M;
	}
	public BigDecimal getR61_MORE2M_TO_3M() {
		return R61_MORE2M_TO_3M;
	}
	public void setR61_MORE2M_TO_3M(BigDecimal r61_MORE2M_TO_3M) {
		R61_MORE2M_TO_3M = r61_MORE2M_TO_3M;
	}
	public BigDecimal getR61_OVER3M_TO_6M() {
		return R61_OVER3M_TO_6M;
	}
	public void setR61_OVER3M_TO_6M(BigDecimal r61_OVER3M_TO_6M) {
		R61_OVER3M_TO_6M = r61_OVER3M_TO_6M;
	}
	public BigDecimal getR61_OVER6M_TO_1Y() {
		return R61_OVER6M_TO_1Y;
	}
	public void setR61_OVER6M_TO_1Y(BigDecimal r61_OVER6M_TO_1Y) {
		R61_OVER6M_TO_1Y = r61_OVER6M_TO_1Y;
	}
	public BigDecimal getR61_OVER1Y_TO_3Y() {
		return R61_OVER1Y_TO_3Y;
	}
	public void setR61_OVER1Y_TO_3Y(BigDecimal r61_OVER1Y_TO_3Y) {
		R61_OVER1Y_TO_3Y = r61_OVER1Y_TO_3Y;
	}
	public BigDecimal getR61_OVER3Y_TO_5Y() {
		return R61_OVER3Y_TO_5Y;
	}
	public void setR61_OVER3Y_TO_5Y(BigDecimal r61_OVER3Y_TO_5Y) {
		R61_OVER3Y_TO_5Y = r61_OVER3Y_TO_5Y;
	}
	public BigDecimal getR61_OVER5Y() {
		return R61_OVER5Y;
	}
	public void setR61_OVER5Y(BigDecimal r61_OVER5Y) {
		R61_OVER5Y = r61_OVER5Y;
	}
	public BigDecimal getR61_TOTAL() {
		return R61_TOTAL;
	}
	public void setR61_TOTAL(BigDecimal r61_TOTAL) {
		R61_TOTAL = r61_TOTAL;
	}
	public String getR62_PRODUCT() {
		return R62_PRODUCT;
	}
	public void setR62_PRODUCT(String r62_PRODUCT) {
		R62_PRODUCT = r62_PRODUCT;
	}
	public BigDecimal getR62_DAY1() {
		return R62_DAY1;
	}
	public void setR62_DAY1(BigDecimal r62_DAY1) {
		R62_DAY1 = r62_DAY1;
	}
	public BigDecimal getR62_DAY2_7() {
		return R62_DAY2_7;
	}
	public void setR62_DAY2_7(BigDecimal r62_DAY2_7) {
		R62_DAY2_7 = r62_DAY2_7;
	}
	public BigDecimal getR62_DAY8_14() {
		return R62_DAY8_14;
	}
	public void setR62_DAY8_14(BigDecimal r62_DAY8_14) {
		R62_DAY8_14 = r62_DAY8_14;
	}
	public BigDecimal getR62_DAY15_30() {
		return R62_DAY15_30;
	}
	public void setR62_DAY15_30(BigDecimal r62_DAY15_30) {
		R62_DAY15_30 = r62_DAY15_30;
	}
	public BigDecimal getR62_DAY31_TO_2M() {
		return R62_DAY31_TO_2M;
	}
	public void setR62_DAY31_TO_2M(BigDecimal r62_DAY31_TO_2M) {
		R62_DAY31_TO_2M = r62_DAY31_TO_2M;
	}
	public BigDecimal getR62_MORE2M_TO_3M() {
		return R62_MORE2M_TO_3M;
	}
	public void setR62_MORE2M_TO_3M(BigDecimal r62_MORE2M_TO_3M) {
		R62_MORE2M_TO_3M = r62_MORE2M_TO_3M;
	}
	public BigDecimal getR62_OVER3M_TO_6M() {
		return R62_OVER3M_TO_6M;
	}
	public void setR62_OVER3M_TO_6M(BigDecimal r62_OVER3M_TO_6M) {
		R62_OVER3M_TO_6M = r62_OVER3M_TO_6M;
	}
	public BigDecimal getR62_OVER6M_TO_1Y() {
		return R62_OVER6M_TO_1Y;
	}
	public void setR62_OVER6M_TO_1Y(BigDecimal r62_OVER6M_TO_1Y) {
		R62_OVER6M_TO_1Y = r62_OVER6M_TO_1Y;
	}
	public BigDecimal getR62_OVER1Y_TO_3Y() {
		return R62_OVER1Y_TO_3Y;
	}
	public void setR62_OVER1Y_TO_3Y(BigDecimal r62_OVER1Y_TO_3Y) {
		R62_OVER1Y_TO_3Y = r62_OVER1Y_TO_3Y;
	}
	public BigDecimal getR62_OVER3Y_TO_5Y() {
		return R62_OVER3Y_TO_5Y;
	}
	public void setR62_OVER3Y_TO_5Y(BigDecimal r62_OVER3Y_TO_5Y) {
		R62_OVER3Y_TO_5Y = r62_OVER3Y_TO_5Y;
	}
	public BigDecimal getR62_OVER5Y() {
		return R62_OVER5Y;
	}
	public void setR62_OVER5Y(BigDecimal r62_OVER5Y) {
		R62_OVER5Y = r62_OVER5Y;
	}
	public BigDecimal getR62_TOTAL() {
		return R62_TOTAL;
	}
	public void setR62_TOTAL(BigDecimal r62_TOTAL) {
		R62_TOTAL = r62_TOTAL;
	}
	public String getR63_PRODUCT() {
		return R63_PRODUCT;
	}
	public void setR63_PRODUCT(String r63_PRODUCT) {
		R63_PRODUCT = r63_PRODUCT;
	}
	public BigDecimal getR63_DAY1() {
		return R63_DAY1;
	}
	public void setR63_DAY1(BigDecimal r63_DAY1) {
		R63_DAY1 = r63_DAY1;
	}
	public BigDecimal getR63_DAY2_7() {
		return R63_DAY2_7;
	}
	public void setR63_DAY2_7(BigDecimal r63_DAY2_7) {
		R63_DAY2_7 = r63_DAY2_7;
	}
	public BigDecimal getR63_DAY8_14() {
		return R63_DAY8_14;
	}
	public void setR63_DAY8_14(BigDecimal r63_DAY8_14) {
		R63_DAY8_14 = r63_DAY8_14;
	}
	public BigDecimal getR63_DAY15_30() {
		return R63_DAY15_30;
	}
	public void setR63_DAY15_30(BigDecimal r63_DAY15_30) {
		R63_DAY15_30 = r63_DAY15_30;
	}
	public BigDecimal getR63_DAY31_TO_2M() {
		return R63_DAY31_TO_2M;
	}
	public void setR63_DAY31_TO_2M(BigDecimal r63_DAY31_TO_2M) {
		R63_DAY31_TO_2M = r63_DAY31_TO_2M;
	}
	public BigDecimal getR63_MORE2M_TO_3M() {
		return R63_MORE2M_TO_3M;
	}
	public void setR63_MORE2M_TO_3M(BigDecimal r63_MORE2M_TO_3M) {
		R63_MORE2M_TO_3M = r63_MORE2M_TO_3M;
	}
	public BigDecimal getR63_OVER3M_TO_6M() {
		return R63_OVER3M_TO_6M;
	}
	public void setR63_OVER3M_TO_6M(BigDecimal r63_OVER3M_TO_6M) {
		R63_OVER3M_TO_6M = r63_OVER3M_TO_6M;
	}
	public BigDecimal getR63_OVER6M_TO_1Y() {
		return R63_OVER6M_TO_1Y;
	}
	public void setR63_OVER6M_TO_1Y(BigDecimal r63_OVER6M_TO_1Y) {
		R63_OVER6M_TO_1Y = r63_OVER6M_TO_1Y;
	}
	public BigDecimal getR63_OVER1Y_TO_3Y() {
		return R63_OVER1Y_TO_3Y;
	}
	public void setR63_OVER1Y_TO_3Y(BigDecimal r63_OVER1Y_TO_3Y) {
		R63_OVER1Y_TO_3Y = r63_OVER1Y_TO_3Y;
	}
	public BigDecimal getR63_OVER3Y_TO_5Y() {
		return R63_OVER3Y_TO_5Y;
	}
	public void setR63_OVER3Y_TO_5Y(BigDecimal r63_OVER3Y_TO_5Y) {
		R63_OVER3Y_TO_5Y = r63_OVER3Y_TO_5Y;
	}
	public BigDecimal getR63_OVER5Y() {
		return R63_OVER5Y;
	}
	public void setR63_OVER5Y(BigDecimal r63_OVER5Y) {
		R63_OVER5Y = r63_OVER5Y;
	}
	public BigDecimal getR63_TOTAL() {
		return R63_TOTAL;
	}
	public void setR63_TOTAL(BigDecimal r63_TOTAL) {
		R63_TOTAL = r63_TOTAL;
	}
	public String getR64_PRODUCT() {
		return R64_PRODUCT;
	}
	public void setR64_PRODUCT(String r64_PRODUCT) {
		R64_PRODUCT = r64_PRODUCT;
	}
	public BigDecimal getR64_DAY1() {
		return R64_DAY1;
	}
	public void setR64_DAY1(BigDecimal r64_DAY1) {
		R64_DAY1 = r64_DAY1;
	}
	public BigDecimal getR64_DAY2_7() {
		return R64_DAY2_7;
	}
	public void setR64_DAY2_7(BigDecimal r64_DAY2_7) {
		R64_DAY2_7 = r64_DAY2_7;
	}
	public BigDecimal getR64_DAY8_14() {
		return R64_DAY8_14;
	}
	public void setR64_DAY8_14(BigDecimal r64_DAY8_14) {
		R64_DAY8_14 = r64_DAY8_14;
	}
	public BigDecimal getR64_DAY15_30() {
		return R64_DAY15_30;
	}
	public void setR64_DAY15_30(BigDecimal r64_DAY15_30) {
		R64_DAY15_30 = r64_DAY15_30;
	}
	public BigDecimal getR64_DAY31_TO_2M() {
		return R64_DAY31_TO_2M;
	}
	public void setR64_DAY31_TO_2M(BigDecimal r64_DAY31_TO_2M) {
		R64_DAY31_TO_2M = r64_DAY31_TO_2M;
	}
	public BigDecimal getR64_MORE2M_TO_3M() {
		return R64_MORE2M_TO_3M;
	}
	public void setR64_MORE2M_TO_3M(BigDecimal r64_MORE2M_TO_3M) {
		R64_MORE2M_TO_3M = r64_MORE2M_TO_3M;
	}
	public BigDecimal getR64_OVER3M_TO_6M() {
		return R64_OVER3M_TO_6M;
	}
	public void setR64_OVER3M_TO_6M(BigDecimal r64_OVER3M_TO_6M) {
		R64_OVER3M_TO_6M = r64_OVER3M_TO_6M;
	}
	public BigDecimal getR64_OVER6M_TO_1Y() {
		return R64_OVER6M_TO_1Y;
	}
	public void setR64_OVER6M_TO_1Y(BigDecimal r64_OVER6M_TO_1Y) {
		R64_OVER6M_TO_1Y = r64_OVER6M_TO_1Y;
	}
	public BigDecimal getR64_OVER1Y_TO_3Y() {
		return R64_OVER1Y_TO_3Y;
	}
	public void setR64_OVER1Y_TO_3Y(BigDecimal r64_OVER1Y_TO_3Y) {
		R64_OVER1Y_TO_3Y = r64_OVER1Y_TO_3Y;
	}
	public BigDecimal getR64_OVER3Y_TO_5Y() {
		return R64_OVER3Y_TO_5Y;
	}
	public void setR64_OVER3Y_TO_5Y(BigDecimal r64_OVER3Y_TO_5Y) {
		R64_OVER3Y_TO_5Y = r64_OVER3Y_TO_5Y;
	}
	public BigDecimal getR64_OVER5Y() {
		return R64_OVER5Y;
	}
	public void setR64_OVER5Y(BigDecimal r64_OVER5Y) {
		R64_OVER5Y = r64_OVER5Y;
	}
	public BigDecimal getR64_TOTAL() {
		return R64_TOTAL;
	}
	public void setR64_TOTAL(BigDecimal r64_TOTAL) {
		R64_TOTAL = r64_TOTAL;
	}
	public String getR65_PRODUCT() {
		return R65_PRODUCT;
	}
	public void setR65_PRODUCT(String r65_PRODUCT) {
		R65_PRODUCT = r65_PRODUCT;
	}
	public BigDecimal getR65_DAY1() {
		return R65_DAY1;
	}
	public void setR65_DAY1(BigDecimal r65_DAY1) {
		R65_DAY1 = r65_DAY1;
	}
	public BigDecimal getR65_DAY2_7() {
		return R65_DAY2_7;
	}
	public void setR65_DAY2_7(BigDecimal r65_DAY2_7) {
		R65_DAY2_7 = r65_DAY2_7;
	}
	public BigDecimal getR65_DAY8_14() {
		return R65_DAY8_14;
	}
	public void setR65_DAY8_14(BigDecimal r65_DAY8_14) {
		R65_DAY8_14 = r65_DAY8_14;
	}
	public BigDecimal getR65_DAY15_30() {
		return R65_DAY15_30;
	}
	public void setR65_DAY15_30(BigDecimal r65_DAY15_30) {
		R65_DAY15_30 = r65_DAY15_30;
	}
	public BigDecimal getR65_DAY31_TO_2M() {
		return R65_DAY31_TO_2M;
	}
	public void setR65_DAY31_TO_2M(BigDecimal r65_DAY31_TO_2M) {
		R65_DAY31_TO_2M = r65_DAY31_TO_2M;
	}
	public BigDecimal getR65_MORE2M_TO_3M() {
		return R65_MORE2M_TO_3M;
	}
	public void setR65_MORE2M_TO_3M(BigDecimal r65_MORE2M_TO_3M) {
		R65_MORE2M_TO_3M = r65_MORE2M_TO_3M;
	}
	public BigDecimal getR65_OVER3M_TO_6M() {
		return R65_OVER3M_TO_6M;
	}
	public void setR65_OVER3M_TO_6M(BigDecimal r65_OVER3M_TO_6M) {
		R65_OVER3M_TO_6M = r65_OVER3M_TO_6M;
	}
	public BigDecimal getR65_OVER6M_TO_1Y() {
		return R65_OVER6M_TO_1Y;
	}
	public void setR65_OVER6M_TO_1Y(BigDecimal r65_OVER6M_TO_1Y) {
		R65_OVER6M_TO_1Y = r65_OVER6M_TO_1Y;
	}
	public BigDecimal getR65_OVER1Y_TO_3Y() {
		return R65_OVER1Y_TO_3Y;
	}
	public void setR65_OVER1Y_TO_3Y(BigDecimal r65_OVER1Y_TO_3Y) {
		R65_OVER1Y_TO_3Y = r65_OVER1Y_TO_3Y;
	}
	public BigDecimal getR65_OVER3Y_TO_5Y() {
		return R65_OVER3Y_TO_5Y;
	}
	public void setR65_OVER3Y_TO_5Y(BigDecimal r65_OVER3Y_TO_5Y) {
		R65_OVER3Y_TO_5Y = r65_OVER3Y_TO_5Y;
	}
	public BigDecimal getR65_OVER5Y() {
		return R65_OVER5Y;
	}
	public void setR65_OVER5Y(BigDecimal r65_OVER5Y) {
		R65_OVER5Y = r65_OVER5Y;
	}
	public BigDecimal getR65_TOTAL() {
		return R65_TOTAL;
	}
	public void setR65_TOTAL(BigDecimal r65_TOTAL) {
		R65_TOTAL = r65_TOTAL;
	}
	public String getR66_PRODUCT() {
		return R66_PRODUCT;
	}
	public void setR66_PRODUCT(String r66_PRODUCT) {
		R66_PRODUCT = r66_PRODUCT;
	}
	public BigDecimal getR66_DAY1() {
		return R66_DAY1;
	}
	public void setR66_DAY1(BigDecimal r66_DAY1) {
		R66_DAY1 = r66_DAY1;
	}
	public BigDecimal getR66_DAY2_7() {
		return R66_DAY2_7;
	}
	public void setR66_DAY2_7(BigDecimal r66_DAY2_7) {
		R66_DAY2_7 = r66_DAY2_7;
	}
	public BigDecimal getR66_DAY8_14() {
		return R66_DAY8_14;
	}
	public void setR66_DAY8_14(BigDecimal r66_DAY8_14) {
		R66_DAY8_14 = r66_DAY8_14;
	}
	public BigDecimal getR66_DAY15_30() {
		return R66_DAY15_30;
	}
	public void setR66_DAY15_30(BigDecimal r66_DAY15_30) {
		R66_DAY15_30 = r66_DAY15_30;
	}
	public BigDecimal getR66_DAY31_TO_2M() {
		return R66_DAY31_TO_2M;
	}
	public void setR66_DAY31_TO_2M(BigDecimal r66_DAY31_TO_2M) {
		R66_DAY31_TO_2M = r66_DAY31_TO_2M;
	}
	public BigDecimal getR66_MORE2M_TO_3M() {
		return R66_MORE2M_TO_3M;
	}
	public void setR66_MORE2M_TO_3M(BigDecimal r66_MORE2M_TO_3M) {
		R66_MORE2M_TO_3M = r66_MORE2M_TO_3M;
	}
	public BigDecimal getR66_OVER3M_TO_6M() {
		return R66_OVER3M_TO_6M;
	}
	public void setR66_OVER3M_TO_6M(BigDecimal r66_OVER3M_TO_6M) {
		R66_OVER3M_TO_6M = r66_OVER3M_TO_6M;
	}
	public BigDecimal getR66_OVER6M_TO_1Y() {
		return R66_OVER6M_TO_1Y;
	}
	public void setR66_OVER6M_TO_1Y(BigDecimal r66_OVER6M_TO_1Y) {
		R66_OVER6M_TO_1Y = r66_OVER6M_TO_1Y;
	}
	public BigDecimal getR66_OVER1Y_TO_3Y() {
		return R66_OVER1Y_TO_3Y;
	}
	public void setR66_OVER1Y_TO_3Y(BigDecimal r66_OVER1Y_TO_3Y) {
		R66_OVER1Y_TO_3Y = r66_OVER1Y_TO_3Y;
	}
	public BigDecimal getR66_OVER3Y_TO_5Y() {
		return R66_OVER3Y_TO_5Y;
	}
	public void setR66_OVER3Y_TO_5Y(BigDecimal r66_OVER3Y_TO_5Y) {
		R66_OVER3Y_TO_5Y = r66_OVER3Y_TO_5Y;
	}
	public BigDecimal getR66_OVER5Y() {
		return R66_OVER5Y;
	}
	public void setR66_OVER5Y(BigDecimal r66_OVER5Y) {
		R66_OVER5Y = r66_OVER5Y;
	}
	public BigDecimal getR66_TOTAL() {
		return R66_TOTAL;
	}
	public void setR66_TOTAL(BigDecimal r66_TOTAL) {
		R66_TOTAL = r66_TOTAL;
	}
	public String getR67_PRODUCT() {
		return R67_PRODUCT;
	}
	public void setR67_PRODUCT(String r67_PRODUCT) {
		R67_PRODUCT = r67_PRODUCT;
	}
	public BigDecimal getR67_DAY1() {
		return R67_DAY1;
	}
	public void setR67_DAY1(BigDecimal r67_DAY1) {
		R67_DAY1 = r67_DAY1;
	}
	public BigDecimal getR67_DAY2_7() {
		return R67_DAY2_7;
	}
	public void setR67_DAY2_7(BigDecimal r67_DAY2_7) {
		R67_DAY2_7 = r67_DAY2_7;
	}
	public BigDecimal getR67_DAY8_14() {
		return R67_DAY8_14;
	}
	public void setR67_DAY8_14(BigDecimal r67_DAY8_14) {
		R67_DAY8_14 = r67_DAY8_14;
	}
	public BigDecimal getR67_DAY15_30() {
		return R67_DAY15_30;
	}
	public void setR67_DAY15_30(BigDecimal r67_DAY15_30) {
		R67_DAY15_30 = r67_DAY15_30;
	}
	public BigDecimal getR67_DAY31_TO_2M() {
		return R67_DAY31_TO_2M;
	}
	public void setR67_DAY31_TO_2M(BigDecimal r67_DAY31_TO_2M) {
		R67_DAY31_TO_2M = r67_DAY31_TO_2M;
	}
	public BigDecimal getR67_MORE2M_TO_3M() {
		return R67_MORE2M_TO_3M;
	}
	public void setR67_MORE2M_TO_3M(BigDecimal r67_MORE2M_TO_3M) {
		R67_MORE2M_TO_3M = r67_MORE2M_TO_3M;
	}
	public BigDecimal getR67_OVER3M_TO_6M() {
		return R67_OVER3M_TO_6M;
	}
	public void setR67_OVER3M_TO_6M(BigDecimal r67_OVER3M_TO_6M) {
		R67_OVER3M_TO_6M = r67_OVER3M_TO_6M;
	}
	public BigDecimal getR67_OVER6M_TO_1Y() {
		return R67_OVER6M_TO_1Y;
	}
	public void setR67_OVER6M_TO_1Y(BigDecimal r67_OVER6M_TO_1Y) {
		R67_OVER6M_TO_1Y = r67_OVER6M_TO_1Y;
	}
	public BigDecimal getR67_OVER1Y_TO_3Y() {
		return R67_OVER1Y_TO_3Y;
	}
	public void setR67_OVER1Y_TO_3Y(BigDecimal r67_OVER1Y_TO_3Y) {
		R67_OVER1Y_TO_3Y = r67_OVER1Y_TO_3Y;
	}
	public BigDecimal getR67_OVER3Y_TO_5Y() {
		return R67_OVER3Y_TO_5Y;
	}
	public void setR67_OVER3Y_TO_5Y(BigDecimal r67_OVER3Y_TO_5Y) {
		R67_OVER3Y_TO_5Y = r67_OVER3Y_TO_5Y;
	}
	public BigDecimal getR67_OVER5Y() {
		return R67_OVER5Y;
	}
	public void setR67_OVER5Y(BigDecimal r67_OVER5Y) {
		R67_OVER5Y = r67_OVER5Y;
	}
	public BigDecimal getR67_TOTAL() {
		return R67_TOTAL;
	}
	public void setR67_TOTAL(BigDecimal r67_TOTAL) {
		R67_TOTAL = r67_TOTAL;
	}
	public String getR68_PRODUCT() {
		return R68_PRODUCT;
	}
	public void setR68_PRODUCT(String r68_PRODUCT) {
		R68_PRODUCT = r68_PRODUCT;
	}
	public BigDecimal getR68_DAY1() {
		return R68_DAY1;
	}
	public void setR68_DAY1(BigDecimal r68_DAY1) {
		R68_DAY1 = r68_DAY1;
	}
	public BigDecimal getR68_DAY2_7() {
		return R68_DAY2_7;
	}
	public void setR68_DAY2_7(BigDecimal r68_DAY2_7) {
		R68_DAY2_7 = r68_DAY2_7;
	}
	public BigDecimal getR68_DAY8_14() {
		return R68_DAY8_14;
	}
	public void setR68_DAY8_14(BigDecimal r68_DAY8_14) {
		R68_DAY8_14 = r68_DAY8_14;
	}
	public BigDecimal getR68_DAY15_30() {
		return R68_DAY15_30;
	}
	public void setR68_DAY15_30(BigDecimal r68_DAY15_30) {
		R68_DAY15_30 = r68_DAY15_30;
	}
	public BigDecimal getR68_DAY31_TO_2M() {
		return R68_DAY31_TO_2M;
	}
	public void setR68_DAY31_TO_2M(BigDecimal r68_DAY31_TO_2M) {
		R68_DAY31_TO_2M = r68_DAY31_TO_2M;
	}
	public BigDecimal getR68_MORE2M_TO_3M() {
		return R68_MORE2M_TO_3M;
	}
	public void setR68_MORE2M_TO_3M(BigDecimal r68_MORE2M_TO_3M) {
		R68_MORE2M_TO_3M = r68_MORE2M_TO_3M;
	}
	public BigDecimal getR68_OVER3M_TO_6M() {
		return R68_OVER3M_TO_6M;
	}
	public void setR68_OVER3M_TO_6M(BigDecimal r68_OVER3M_TO_6M) {
		R68_OVER3M_TO_6M = r68_OVER3M_TO_6M;
	}
	public BigDecimal getR68_OVER6M_TO_1Y() {
		return R68_OVER6M_TO_1Y;
	}
	public void setR68_OVER6M_TO_1Y(BigDecimal r68_OVER6M_TO_1Y) {
		R68_OVER6M_TO_1Y = r68_OVER6M_TO_1Y;
	}
	public BigDecimal getR68_OVER1Y_TO_3Y() {
		return R68_OVER1Y_TO_3Y;
	}
	public void setR68_OVER1Y_TO_3Y(BigDecimal r68_OVER1Y_TO_3Y) {
		R68_OVER1Y_TO_3Y = r68_OVER1Y_TO_3Y;
	}
	public BigDecimal getR68_OVER3Y_TO_5Y() {
		return R68_OVER3Y_TO_5Y;
	}
	public void setR68_OVER3Y_TO_5Y(BigDecimal r68_OVER3Y_TO_5Y) {
		R68_OVER3Y_TO_5Y = r68_OVER3Y_TO_5Y;
	}
	public BigDecimal getR68_OVER5Y() {
		return R68_OVER5Y;
	}
	public void setR68_OVER5Y(BigDecimal r68_OVER5Y) {
		R68_OVER5Y = r68_OVER5Y;
	}
	public BigDecimal getR68_TOTAL() {
		return R68_TOTAL;
	}
	public void setR68_TOTAL(BigDecimal r68_TOTAL) {
		R68_TOTAL = r68_TOTAL;
	}
	public String getR69_PRODUCT() {
		return R69_PRODUCT;
	}
	public void setR69_PRODUCT(String r69_PRODUCT) {
		R69_PRODUCT = r69_PRODUCT;
	}
	public BigDecimal getR69_DAY1() {
		return R69_DAY1;
	}
	public void setR69_DAY1(BigDecimal r69_DAY1) {
		R69_DAY1 = r69_DAY1;
	}
	public BigDecimal getR69_DAY2_7() {
		return R69_DAY2_7;
	}
	public void setR69_DAY2_7(BigDecimal r69_DAY2_7) {
		R69_DAY2_7 = r69_DAY2_7;
	}
	public BigDecimal getR69_DAY8_14() {
		return R69_DAY8_14;
	}
	public void setR69_DAY8_14(BigDecimal r69_DAY8_14) {
		R69_DAY8_14 = r69_DAY8_14;
	}
	public BigDecimal getR69_DAY15_30() {
		return R69_DAY15_30;
	}
	public void setR69_DAY15_30(BigDecimal r69_DAY15_30) {
		R69_DAY15_30 = r69_DAY15_30;
	}
	public BigDecimal getR69_DAY31_TO_2M() {
		return R69_DAY31_TO_2M;
	}
	public void setR69_DAY31_TO_2M(BigDecimal r69_DAY31_TO_2M) {
		R69_DAY31_TO_2M = r69_DAY31_TO_2M;
	}
	public BigDecimal getR69_MORE2M_TO_3M() {
		return R69_MORE2M_TO_3M;
	}
	public void setR69_MORE2M_TO_3M(BigDecimal r69_MORE2M_TO_3M) {
		R69_MORE2M_TO_3M = r69_MORE2M_TO_3M;
	}
	public BigDecimal getR69_OVER3M_TO_6M() {
		return R69_OVER3M_TO_6M;
	}
	public void setR69_OVER3M_TO_6M(BigDecimal r69_OVER3M_TO_6M) {
		R69_OVER3M_TO_6M = r69_OVER3M_TO_6M;
	}
	public BigDecimal getR69_OVER6M_TO_1Y() {
		return R69_OVER6M_TO_1Y;
	}
	public void setR69_OVER6M_TO_1Y(BigDecimal r69_OVER6M_TO_1Y) {
		R69_OVER6M_TO_1Y = r69_OVER6M_TO_1Y;
	}
	public BigDecimal getR69_OVER1Y_TO_3Y() {
		return R69_OVER1Y_TO_3Y;
	}
	public void setR69_OVER1Y_TO_3Y(BigDecimal r69_OVER1Y_TO_3Y) {
		R69_OVER1Y_TO_3Y = r69_OVER1Y_TO_3Y;
	}
	public BigDecimal getR69_OVER3Y_TO_5Y() {
		return R69_OVER3Y_TO_5Y;
	}
	public void setR69_OVER3Y_TO_5Y(BigDecimal r69_OVER3Y_TO_5Y) {
		R69_OVER3Y_TO_5Y = r69_OVER3Y_TO_5Y;
	}
	public BigDecimal getR69_OVER5Y() {
		return R69_OVER5Y;
	}
	public void setR69_OVER5Y(BigDecimal r69_OVER5Y) {
		R69_OVER5Y = r69_OVER5Y;
	}
	public BigDecimal getR69_TOTAL() {
		return R69_TOTAL;
	}
	public void setR69_TOTAL(BigDecimal r69_TOTAL) {
		R69_TOTAL = r69_TOTAL;
	}
	public String getR70_PRODUCT() {
		return R70_PRODUCT;
	}
	public void setR70_PRODUCT(String r70_PRODUCT) {
		R70_PRODUCT = r70_PRODUCT;
	}
	public BigDecimal getR70_DAY1() {
		return R70_DAY1;
	}
	public void setR70_DAY1(BigDecimal r70_DAY1) {
		R70_DAY1 = r70_DAY1;
	}
	public BigDecimal getR70_DAY2_7() {
		return R70_DAY2_7;
	}
	public void setR70_DAY2_7(BigDecimal r70_DAY2_7) {
		R70_DAY2_7 = r70_DAY2_7;
	}
	public BigDecimal getR70_DAY8_14() {
		return R70_DAY8_14;
	}
	public void setR70_DAY8_14(BigDecimal r70_DAY8_14) {
		R70_DAY8_14 = r70_DAY8_14;
	}
	public BigDecimal getR70_DAY15_30() {
		return R70_DAY15_30;
	}
	public void setR70_DAY15_30(BigDecimal r70_DAY15_30) {
		R70_DAY15_30 = r70_DAY15_30;
	}
	public BigDecimal getR70_DAY31_TO_2M() {
		return R70_DAY31_TO_2M;
	}
	public void setR70_DAY31_TO_2M(BigDecimal r70_DAY31_TO_2M) {
		R70_DAY31_TO_2M = r70_DAY31_TO_2M;
	}
	public BigDecimal getR70_MORE2M_TO_3M() {
		return R70_MORE2M_TO_3M;
	}
	public void setR70_MORE2M_TO_3M(BigDecimal r70_MORE2M_TO_3M) {
		R70_MORE2M_TO_3M = r70_MORE2M_TO_3M;
	}
	public BigDecimal getR70_OVER3M_TO_6M() {
		return R70_OVER3M_TO_6M;
	}
	public void setR70_OVER3M_TO_6M(BigDecimal r70_OVER3M_TO_6M) {
		R70_OVER3M_TO_6M = r70_OVER3M_TO_6M;
	}
	public BigDecimal getR70_OVER6M_TO_1Y() {
		return R70_OVER6M_TO_1Y;
	}
	public void setR70_OVER6M_TO_1Y(BigDecimal r70_OVER6M_TO_1Y) {
		R70_OVER6M_TO_1Y = r70_OVER6M_TO_1Y;
	}
	public BigDecimal getR70_OVER1Y_TO_3Y() {
		return R70_OVER1Y_TO_3Y;
	}
	public void setR70_OVER1Y_TO_3Y(BigDecimal r70_OVER1Y_TO_3Y) {
		R70_OVER1Y_TO_3Y = r70_OVER1Y_TO_3Y;
	}
	public BigDecimal getR70_OVER3Y_TO_5Y() {
		return R70_OVER3Y_TO_5Y;
	}
	public void setR70_OVER3Y_TO_5Y(BigDecimal r70_OVER3Y_TO_5Y) {
		R70_OVER3Y_TO_5Y = r70_OVER3Y_TO_5Y;
	}
	public BigDecimal getR70_OVER5Y() {
		return R70_OVER5Y;
	}
	public void setR70_OVER5Y(BigDecimal r70_OVER5Y) {
		R70_OVER5Y = r70_OVER5Y;
	}
	public BigDecimal getR70_TOTAL() {
		return R70_TOTAL;
	}
	public void setR70_TOTAL(BigDecimal r70_TOTAL) {
		R70_TOTAL = r70_TOTAL;
	}
	public String getR71_PRODUCT() {
		return R71_PRODUCT;
	}
	public void setR71_PRODUCT(String r71_PRODUCT) {
		R71_PRODUCT = r71_PRODUCT;
	}
	public BigDecimal getR71_DAY1() {
		return R71_DAY1;
	}
	public void setR71_DAY1(BigDecimal r71_DAY1) {
		R71_DAY1 = r71_DAY1;
	}
	public BigDecimal getR71_DAY2_7() {
		return R71_DAY2_7;
	}
	public void setR71_DAY2_7(BigDecimal r71_DAY2_7) {
		R71_DAY2_7 = r71_DAY2_7;
	}
	public BigDecimal getR71_DAY8_14() {
		return R71_DAY8_14;
	}
	public void setR71_DAY8_14(BigDecimal r71_DAY8_14) {
		R71_DAY8_14 = r71_DAY8_14;
	}
	public BigDecimal getR71_DAY15_30() {
		return R71_DAY15_30;
	}
	public void setR71_DAY15_30(BigDecimal r71_DAY15_30) {
		R71_DAY15_30 = r71_DAY15_30;
	}
	public BigDecimal getR71_DAY31_TO_2M() {
		return R71_DAY31_TO_2M;
	}
	public void setR71_DAY31_TO_2M(BigDecimal r71_DAY31_TO_2M) {
		R71_DAY31_TO_2M = r71_DAY31_TO_2M;
	}
	public BigDecimal getR71_MORE2M_TO_3M() {
		return R71_MORE2M_TO_3M;
	}
	public void setR71_MORE2M_TO_3M(BigDecimal r71_MORE2M_TO_3M) {
		R71_MORE2M_TO_3M = r71_MORE2M_TO_3M;
	}
	public BigDecimal getR71_OVER3M_TO_6M() {
		return R71_OVER3M_TO_6M;
	}
	public void setR71_OVER3M_TO_6M(BigDecimal r71_OVER3M_TO_6M) {
		R71_OVER3M_TO_6M = r71_OVER3M_TO_6M;
	}
	public BigDecimal getR71_OVER6M_TO_1Y() {
		return R71_OVER6M_TO_1Y;
	}
	public void setR71_OVER6M_TO_1Y(BigDecimal r71_OVER6M_TO_1Y) {
		R71_OVER6M_TO_1Y = r71_OVER6M_TO_1Y;
	}
	public BigDecimal getR71_OVER1Y_TO_3Y() {
		return R71_OVER1Y_TO_3Y;
	}
	public void setR71_OVER1Y_TO_3Y(BigDecimal r71_OVER1Y_TO_3Y) {
		R71_OVER1Y_TO_3Y = r71_OVER1Y_TO_3Y;
	}
	public BigDecimal getR71_OVER3Y_TO_5Y() {
		return R71_OVER3Y_TO_5Y;
	}
	public void setR71_OVER3Y_TO_5Y(BigDecimal r71_OVER3Y_TO_5Y) {
		R71_OVER3Y_TO_5Y = r71_OVER3Y_TO_5Y;
	}
	public BigDecimal getR71_OVER5Y() {
		return R71_OVER5Y;
	}
	public void setR71_OVER5Y(BigDecimal r71_OVER5Y) {
		R71_OVER5Y = r71_OVER5Y;
	}
	public BigDecimal getR71_TOTAL() {
		return R71_TOTAL;
	}
	public void setR71_TOTAL(BigDecimal r71_TOTAL) {
		R71_TOTAL = r71_TOTAL;
	}
	public String getR72_PRODUCT() {
		return R72_PRODUCT;
	}
	public void setR72_PRODUCT(String r72_PRODUCT) {
		R72_PRODUCT = r72_PRODUCT;
	}
	public BigDecimal getR72_DAY1() {
		return R72_DAY1;
	}
	public void setR72_DAY1(BigDecimal r72_DAY1) {
		R72_DAY1 = r72_DAY1;
	}
	public BigDecimal getR72_DAY2_7() {
		return R72_DAY2_7;
	}
	public void setR72_DAY2_7(BigDecimal r72_DAY2_7) {
		R72_DAY2_7 = r72_DAY2_7;
	}
	public BigDecimal getR72_DAY8_14() {
		return R72_DAY8_14;
	}
	public void setR72_DAY8_14(BigDecimal r72_DAY8_14) {
		R72_DAY8_14 = r72_DAY8_14;
	}
	public BigDecimal getR72_DAY15_30() {
		return R72_DAY15_30;
	}
	public void setR72_DAY15_30(BigDecimal r72_DAY15_30) {
		R72_DAY15_30 = r72_DAY15_30;
	}
	public BigDecimal getR72_DAY31_TO_2M() {
		return R72_DAY31_TO_2M;
	}
	public void setR72_DAY31_TO_2M(BigDecimal r72_DAY31_TO_2M) {
		R72_DAY31_TO_2M = r72_DAY31_TO_2M;
	}
	public BigDecimal getR72_MORE2M_TO_3M() {
		return R72_MORE2M_TO_3M;
	}
	public void setR72_MORE2M_TO_3M(BigDecimal r72_MORE2M_TO_3M) {
		R72_MORE2M_TO_3M = r72_MORE2M_TO_3M;
	}
	public BigDecimal getR72_OVER3M_TO_6M() {
		return R72_OVER3M_TO_6M;
	}
	public void setR72_OVER3M_TO_6M(BigDecimal r72_OVER3M_TO_6M) {
		R72_OVER3M_TO_6M = r72_OVER3M_TO_6M;
	}
	public BigDecimal getR72_OVER6M_TO_1Y() {
		return R72_OVER6M_TO_1Y;
	}
	public void setR72_OVER6M_TO_1Y(BigDecimal r72_OVER6M_TO_1Y) {
		R72_OVER6M_TO_1Y = r72_OVER6M_TO_1Y;
	}
	public BigDecimal getR72_OVER1Y_TO_3Y() {
		return R72_OVER1Y_TO_3Y;
	}
	public void setR72_OVER1Y_TO_3Y(BigDecimal r72_OVER1Y_TO_3Y) {
		R72_OVER1Y_TO_3Y = r72_OVER1Y_TO_3Y;
	}
	public BigDecimal getR72_OVER3Y_TO_5Y() {
		return R72_OVER3Y_TO_5Y;
	}
	public void setR72_OVER3Y_TO_5Y(BigDecimal r72_OVER3Y_TO_5Y) {
		R72_OVER3Y_TO_5Y = r72_OVER3Y_TO_5Y;
	}
	public BigDecimal getR72_OVER5Y() {
		return R72_OVER5Y;
	}
	public void setR72_OVER5Y(BigDecimal r72_OVER5Y) {
		R72_OVER5Y = r72_OVER5Y;
	}
	public BigDecimal getR72_TOTAL() {
		return R72_TOTAL;
	}
	public void setR72_TOTAL(BigDecimal r72_TOTAL) {
		R72_TOTAL = r72_TOTAL;
	}
	public String getR73_PRODUCT() {
		return R73_PRODUCT;
	}
	public void setR73_PRODUCT(String r73_PRODUCT) {
		R73_PRODUCT = r73_PRODUCT;
	}
	public BigDecimal getR73_DAY1() {
		return R73_DAY1;
	}
	public void setR73_DAY1(BigDecimal r73_DAY1) {
		R73_DAY1 = r73_DAY1;
	}
	public BigDecimal getR73_DAY2_7() {
		return R73_DAY2_7;
	}
	public void setR73_DAY2_7(BigDecimal r73_DAY2_7) {
		R73_DAY2_7 = r73_DAY2_7;
	}
	public BigDecimal getR73_DAY8_14() {
		return R73_DAY8_14;
	}
	public void setR73_DAY8_14(BigDecimal r73_DAY8_14) {
		R73_DAY8_14 = r73_DAY8_14;
	}
	public BigDecimal getR73_DAY15_30() {
		return R73_DAY15_30;
	}
	public void setR73_DAY15_30(BigDecimal r73_DAY15_30) {
		R73_DAY15_30 = r73_DAY15_30;
	}
	public BigDecimal getR73_DAY31_TO_2M() {
		return R73_DAY31_TO_2M;
	}
	public void setR73_DAY31_TO_2M(BigDecimal r73_DAY31_TO_2M) {
		R73_DAY31_TO_2M = r73_DAY31_TO_2M;
	}
	public BigDecimal getR73_MORE2M_TO_3M() {
		return R73_MORE2M_TO_3M;
	}
	public void setR73_MORE2M_TO_3M(BigDecimal r73_MORE2M_TO_3M) {
		R73_MORE2M_TO_3M = r73_MORE2M_TO_3M;
	}
	public BigDecimal getR73_OVER3M_TO_6M() {
		return R73_OVER3M_TO_6M;
	}
	public void setR73_OVER3M_TO_6M(BigDecimal r73_OVER3M_TO_6M) {
		R73_OVER3M_TO_6M = r73_OVER3M_TO_6M;
	}
	public BigDecimal getR73_OVER6M_TO_1Y() {
		return R73_OVER6M_TO_1Y;
	}
	public void setR73_OVER6M_TO_1Y(BigDecimal r73_OVER6M_TO_1Y) {
		R73_OVER6M_TO_1Y = r73_OVER6M_TO_1Y;
	}
	public BigDecimal getR73_OVER1Y_TO_3Y() {
		return R73_OVER1Y_TO_3Y;
	}
	public void setR73_OVER1Y_TO_3Y(BigDecimal r73_OVER1Y_TO_3Y) {
		R73_OVER1Y_TO_3Y = r73_OVER1Y_TO_3Y;
	}
	public BigDecimal getR73_OVER3Y_TO_5Y() {
		return R73_OVER3Y_TO_5Y;
	}
	public void setR73_OVER3Y_TO_5Y(BigDecimal r73_OVER3Y_TO_5Y) {
		R73_OVER3Y_TO_5Y = r73_OVER3Y_TO_5Y;
	}
	public BigDecimal getR73_OVER5Y() {
		return R73_OVER5Y;
	}
	public void setR73_OVER5Y(BigDecimal r73_OVER5Y) {
		R73_OVER5Y = r73_OVER5Y;
	}
	public BigDecimal getR73_TOTAL() {
		return R73_TOTAL;
	}
	public void setR73_TOTAL(BigDecimal r73_TOTAL) {
		R73_TOTAL = r73_TOTAL;
	}
	public String getR74_PRODUCT() {
		return R74_PRODUCT;
	}
	public void setR74_PRODUCT(String r74_PRODUCT) {
		R74_PRODUCT = r74_PRODUCT;
	}
	public BigDecimal getR74_DAY1() {
		return R74_DAY1;
	}
	public void setR74_DAY1(BigDecimal r74_DAY1) {
		R74_DAY1 = r74_DAY1;
	}
	public BigDecimal getR74_DAY2_7() {
		return R74_DAY2_7;
	}
	public void setR74_DAY2_7(BigDecimal r74_DAY2_7) {
		R74_DAY2_7 = r74_DAY2_7;
	}
	public BigDecimal getR74_DAY8_14() {
		return R74_DAY8_14;
	}
	public void setR74_DAY8_14(BigDecimal r74_DAY8_14) {
		R74_DAY8_14 = r74_DAY8_14;
	}
	public BigDecimal getR74_DAY15_30() {
		return R74_DAY15_30;
	}
	public void setR74_DAY15_30(BigDecimal r74_DAY15_30) {
		R74_DAY15_30 = r74_DAY15_30;
	}
	public BigDecimal getR74_DAY31_TO_2M() {
		return R74_DAY31_TO_2M;
	}
	public void setR74_DAY31_TO_2M(BigDecimal r74_DAY31_TO_2M) {
		R74_DAY31_TO_2M = r74_DAY31_TO_2M;
	}
	public BigDecimal getR74_MORE2M_TO_3M() {
		return R74_MORE2M_TO_3M;
	}
	public void setR74_MORE2M_TO_3M(BigDecimal r74_MORE2M_TO_3M) {
		R74_MORE2M_TO_3M = r74_MORE2M_TO_3M;
	}
	public BigDecimal getR74_OVER3M_TO_6M() {
		return R74_OVER3M_TO_6M;
	}
	public void setR74_OVER3M_TO_6M(BigDecimal r74_OVER3M_TO_6M) {
		R74_OVER3M_TO_6M = r74_OVER3M_TO_6M;
	}
	public BigDecimal getR74_OVER6M_TO_1Y() {
		return R74_OVER6M_TO_1Y;
	}
	public void setR74_OVER6M_TO_1Y(BigDecimal r74_OVER6M_TO_1Y) {
		R74_OVER6M_TO_1Y = r74_OVER6M_TO_1Y;
	}
	public BigDecimal getR74_OVER1Y_TO_3Y() {
		return R74_OVER1Y_TO_3Y;
	}
	public void setR74_OVER1Y_TO_3Y(BigDecimal r74_OVER1Y_TO_3Y) {
		R74_OVER1Y_TO_3Y = r74_OVER1Y_TO_3Y;
	}
	public BigDecimal getR74_OVER3Y_TO_5Y() {
		return R74_OVER3Y_TO_5Y;
	}
	public void setR74_OVER3Y_TO_5Y(BigDecimal r74_OVER3Y_TO_5Y) {
		R74_OVER3Y_TO_5Y = r74_OVER3Y_TO_5Y;
	}
	public BigDecimal getR74_OVER5Y() {
		return R74_OVER5Y;
	}
	public void setR74_OVER5Y(BigDecimal r74_OVER5Y) {
		R74_OVER5Y = r74_OVER5Y;
	}
	public BigDecimal getR74_TOTAL() {
		return R74_TOTAL;
	}
	public void setR74_TOTAL(BigDecimal r74_TOTAL) {
		R74_TOTAL = r74_TOTAL;
	}
	public String getR75_PRODUCT() {
		return R75_PRODUCT;
	}
	public void setR75_PRODUCT(String r75_PRODUCT) {
		R75_PRODUCT = r75_PRODUCT;
	}
	public BigDecimal getR75_DAY1() {
		return R75_DAY1;
	}
	public void setR75_DAY1(BigDecimal r75_DAY1) {
		R75_DAY1 = r75_DAY1;
	}
	public BigDecimal getR75_DAY2_7() {
		return R75_DAY2_7;
	}
	public void setR75_DAY2_7(BigDecimal r75_DAY2_7) {
		R75_DAY2_7 = r75_DAY2_7;
	}
	public BigDecimal getR75_DAY8_14() {
		return R75_DAY8_14;
	}
	public void setR75_DAY8_14(BigDecimal r75_DAY8_14) {
		R75_DAY8_14 = r75_DAY8_14;
	}
	public BigDecimal getR75_DAY15_30() {
		return R75_DAY15_30;
	}
	public void setR75_DAY15_30(BigDecimal r75_DAY15_30) {
		R75_DAY15_30 = r75_DAY15_30;
	}
	public BigDecimal getR75_DAY31_TO_2M() {
		return R75_DAY31_TO_2M;
	}
	public void setR75_DAY31_TO_2M(BigDecimal r75_DAY31_TO_2M) {
		R75_DAY31_TO_2M = r75_DAY31_TO_2M;
	}
	public BigDecimal getR75_MORE2M_TO_3M() {
		return R75_MORE2M_TO_3M;
	}
	public void setR75_MORE2M_TO_3M(BigDecimal r75_MORE2M_TO_3M) {
		R75_MORE2M_TO_3M = r75_MORE2M_TO_3M;
	}
	public BigDecimal getR75_OVER3M_TO_6M() {
		return R75_OVER3M_TO_6M;
	}
	public void setR75_OVER3M_TO_6M(BigDecimal r75_OVER3M_TO_6M) {
		R75_OVER3M_TO_6M = r75_OVER3M_TO_6M;
	}
	public BigDecimal getR75_OVER6M_TO_1Y() {
		return R75_OVER6M_TO_1Y;
	}
	public void setR75_OVER6M_TO_1Y(BigDecimal r75_OVER6M_TO_1Y) {
		R75_OVER6M_TO_1Y = r75_OVER6M_TO_1Y;
	}
	public BigDecimal getR75_OVER1Y_TO_3Y() {
		return R75_OVER1Y_TO_3Y;
	}
	public void setR75_OVER1Y_TO_3Y(BigDecimal r75_OVER1Y_TO_3Y) {
		R75_OVER1Y_TO_3Y = r75_OVER1Y_TO_3Y;
	}
	public BigDecimal getR75_OVER3Y_TO_5Y() {
		return R75_OVER3Y_TO_5Y;
	}
	public void setR75_OVER3Y_TO_5Y(BigDecimal r75_OVER3Y_TO_5Y) {
		R75_OVER3Y_TO_5Y = r75_OVER3Y_TO_5Y;
	}
	public BigDecimal getR75_OVER5Y() {
		return R75_OVER5Y;
	}
	public void setR75_OVER5Y(BigDecimal r75_OVER5Y) {
		R75_OVER5Y = r75_OVER5Y;
	}
	public BigDecimal getR75_TOTAL() {
		return R75_TOTAL;
	}
	public void setR75_TOTAL(BigDecimal r75_TOTAL) {
		R75_TOTAL = r75_TOTAL;
	}
	public String getR76_PRODUCT() {
		return R76_PRODUCT;
	}
	public void setR76_PRODUCT(String r76_PRODUCT) {
		R76_PRODUCT = r76_PRODUCT;
	}
	public BigDecimal getR76_DAY1() {
		return R76_DAY1;
	}
	public void setR76_DAY1(BigDecimal r76_DAY1) {
		R76_DAY1 = r76_DAY1;
	}
	public BigDecimal getR76_DAY2_7() {
		return R76_DAY2_7;
	}
	public void setR76_DAY2_7(BigDecimal r76_DAY2_7) {
		R76_DAY2_7 = r76_DAY2_7;
	}
	public BigDecimal getR76_DAY8_14() {
		return R76_DAY8_14;
	}
	public void setR76_DAY8_14(BigDecimal r76_DAY8_14) {
		R76_DAY8_14 = r76_DAY8_14;
	}
	public BigDecimal getR76_DAY15_30() {
		return R76_DAY15_30;
	}
	public void setR76_DAY15_30(BigDecimal r76_DAY15_30) {
		R76_DAY15_30 = r76_DAY15_30;
	}
	public BigDecimal getR76_DAY31_TO_2M() {
		return R76_DAY31_TO_2M;
	}
	public void setR76_DAY31_TO_2M(BigDecimal r76_DAY31_TO_2M) {
		R76_DAY31_TO_2M = r76_DAY31_TO_2M;
	}
	public BigDecimal getR76_MORE2M_TO_3M() {
		return R76_MORE2M_TO_3M;
	}
	public void setR76_MORE2M_TO_3M(BigDecimal r76_MORE2M_TO_3M) {
		R76_MORE2M_TO_3M = r76_MORE2M_TO_3M;
	}
	public BigDecimal getR76_OVER3M_TO_6M() {
		return R76_OVER3M_TO_6M;
	}
	public void setR76_OVER3M_TO_6M(BigDecimal r76_OVER3M_TO_6M) {
		R76_OVER3M_TO_6M = r76_OVER3M_TO_6M;
	}
	public BigDecimal getR76_OVER6M_TO_1Y() {
		return R76_OVER6M_TO_1Y;
	}
	public void setR76_OVER6M_TO_1Y(BigDecimal r76_OVER6M_TO_1Y) {
		R76_OVER6M_TO_1Y = r76_OVER6M_TO_1Y;
	}
	public BigDecimal getR76_OVER1Y_TO_3Y() {
		return R76_OVER1Y_TO_3Y;
	}
	public void setR76_OVER1Y_TO_3Y(BigDecimal r76_OVER1Y_TO_3Y) {
		R76_OVER1Y_TO_3Y = r76_OVER1Y_TO_3Y;
	}
	public BigDecimal getR76_OVER3Y_TO_5Y() {
		return R76_OVER3Y_TO_5Y;
	}
	public void setR76_OVER3Y_TO_5Y(BigDecimal r76_OVER3Y_TO_5Y) {
		R76_OVER3Y_TO_5Y = r76_OVER3Y_TO_5Y;
	}
	public BigDecimal getR76_OVER5Y() {
		return R76_OVER5Y;
	}
	public void setR76_OVER5Y(BigDecimal r76_OVER5Y) {
		R76_OVER5Y = r76_OVER5Y;
	}
	public BigDecimal getR76_TOTAL() {
		return R76_TOTAL;
	}
	public void setR76_TOTAL(BigDecimal r76_TOTAL) {
		R76_TOTAL = r76_TOTAL;
	}
//	public String getR77_PRODUCT() {
//		return R77_PRODUCT;
//	}
//	public void setR77_PRODUCT(String r77_PRODUCT) {
//		R77_PRODUCT = r77_PRODUCT;
//	}
//	public BigDecimal getR77_DAY1() {
//		return R77_DAY1;
//	}
//	public void setR77_DAY1(BigDecimal r77_DAY1) {
//		R77_DAY1 = r77_DAY1;
//	}
//	public BigDecimal getR77_DAY2_7() {
//		return R77_DAY2_7;
//	}
//	public void setR77_DAY2_7(BigDecimal r77_DAY2_7) {
//		R77_DAY2_7 = r77_DAY2_7;
//	}
//	public BigDecimal getR77_DAY8_14() {
//		return R77_DAY8_14;
//	}
//	public void setR77_DAY8_14(BigDecimal r77_DAY8_14) {
//		R77_DAY8_14 = r77_DAY8_14;
//	}
//	public BigDecimal getR77_DAY15_30() {
//		return R77_DAY15_30;
//	}
//	public void setR77_DAY15_30(BigDecimal r77_DAY15_30) {
//		R77_DAY15_30 = r77_DAY15_30;
//	}
//	public BigDecimal getR77_DAY31_TO_2M() {
//		return R77_DAY31_TO_2M;
//	}
//	public void setR77_DAY31_TO_2M(BigDecimal r77_DAY31_TO_2M) {
//		R77_DAY31_TO_2M = r77_DAY31_TO_2M;
//	}
//	public BigDecimal getR77_MORE2M_TO_3M() {
//		return R77_MORE2M_TO_3M;
//	}
//	public void setR77_MORE2M_TO_3M(BigDecimal r77_MORE2M_TO_3M) {
//		R77_MORE2M_TO_3M = r77_MORE2M_TO_3M;
//	}
//	public BigDecimal getR77_OVER3M_TO_6M() {
//		return R77_OVER3M_TO_6M;
//	}
//	public void setR77_OVER3M_TO_6M(BigDecimal r77_OVER3M_TO_6M) {
//		R77_OVER3M_TO_6M = r77_OVER3M_TO_6M;
//	}
//	public BigDecimal getR77_OVER6M_TO_1Y() {
//		return R77_OVER6M_TO_1Y;
//	}
//	public void setR77_OVER6M_TO_1Y(BigDecimal r77_OVER6M_TO_1Y) {
//		R77_OVER6M_TO_1Y = r77_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR77_OVER1Y_TO_3Y() {
//		return R77_OVER1Y_TO_3Y;
//	}
//	public void setR77_OVER1Y_TO_3Y(BigDecimal r77_OVER1Y_TO_3Y) {
//		R77_OVER1Y_TO_3Y = r77_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR77_OVER3Y_TO_5Y() {
//		return R77_OVER3Y_TO_5Y;
//	}
//	public void setR77_OVER3Y_TO_5Y(BigDecimal r77_OVER3Y_TO_5Y) {
//		R77_OVER3Y_TO_5Y = r77_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR77_OVER5Y() {
//		return R77_OVER5Y;
//	}
//	public void setR77_OVER5Y(BigDecimal r77_OVER5Y) {
//		R77_OVER5Y = r77_OVER5Y;
//	}
//	public BigDecimal getR77_TOTAL() {
//		return R77_TOTAL;
//	}
//	public void setR77_TOTAL(BigDecimal r77_TOTAL) {
//		R77_TOTAL = r77_TOTAL;
//	}
//	public String getR78_PRODUCT() {
//		return R78_PRODUCT;
//	}
//	public void setR78_PRODUCT(String r78_PRODUCT) {
//		R78_PRODUCT = r78_PRODUCT;
//	}
//	public BigDecimal getR78_DAY1() {
//		return R78_DAY1;
//	}
//	public void setR78_DAY1(BigDecimal r78_DAY1) {
//		R78_DAY1 = r78_DAY1;
//	}
//	public BigDecimal getR78_DAY2_7() {
//		return R78_DAY2_7;
//	}
//	public void setR78_DAY2_7(BigDecimal r78_DAY2_7) {
//		R78_DAY2_7 = r78_DAY2_7;
//	}
//	public BigDecimal getR78_DAY8_14() {
//		return R78_DAY8_14;
//	}
//	public void setR78_DAY8_14(BigDecimal r78_DAY8_14) {
//		R78_DAY8_14 = r78_DAY8_14;
//	}
//	public BigDecimal getR78_DAY15_30() {
//		return R78_DAY15_30;
//	}
//	public void setR78_DAY15_30(BigDecimal r78_DAY15_30) {
//		R78_DAY15_30 = r78_DAY15_30;
//	}
//	public BigDecimal getR78_DAY31_TO_2M() {
//		return R78_DAY31_TO_2M;
//	}
//	public void setR78_DAY31_TO_2M(BigDecimal r78_DAY31_TO_2M) {
//		R78_DAY31_TO_2M = r78_DAY31_TO_2M;
//	}
//	public BigDecimal getR78_MORE2M_TO_3M() {
//		return R78_MORE2M_TO_3M;
//	}
//	public void setR78_MORE2M_TO_3M(BigDecimal r78_MORE2M_TO_3M) {
//		R78_MORE2M_TO_3M = r78_MORE2M_TO_3M;
//	}
//	public BigDecimal getR78_OVER3M_TO_6M() {
//		return R78_OVER3M_TO_6M;
//	}
//	public void setR78_OVER3M_TO_6M(BigDecimal r78_OVER3M_TO_6M) {
//		R78_OVER3M_TO_6M = r78_OVER3M_TO_6M;
//	}
//	public BigDecimal getR78_OVER6M_TO_1Y() {
//		return R78_OVER6M_TO_1Y;
//	}
//	public void setR78_OVER6M_TO_1Y(BigDecimal r78_OVER6M_TO_1Y) {
//		R78_OVER6M_TO_1Y = r78_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR78_OVER1Y_TO_3Y() {
//		return R78_OVER1Y_TO_3Y;
//	}
//	public void setR78_OVER1Y_TO_3Y(BigDecimal r78_OVER1Y_TO_3Y) {
//		R78_OVER1Y_TO_3Y = r78_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR78_OVER3Y_TO_5Y() {
//		return R78_OVER3Y_TO_5Y;
//	}
//	public void setR78_OVER3Y_TO_5Y(BigDecimal r78_OVER3Y_TO_5Y) {
//		R78_OVER3Y_TO_5Y = r78_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR78_OVER5Y() {
//		return R78_OVER5Y;
//	}
//	public void setR78_OVER5Y(BigDecimal r78_OVER5Y) {
//		R78_OVER5Y = r78_OVER5Y;
//	}
//	public BigDecimal getR78_TOTAL() {
//		return R78_TOTAL;
//	}
//	public void setR78_TOTAL(BigDecimal r78_TOTAL) {
//		R78_TOTAL = r78_TOTAL;
//	}
//	public String getR79_PRODUCT() {
//		return R79_PRODUCT;
//	}
//	public void setR79_PRODUCT(String r79_PRODUCT) {
//		R79_PRODUCT = r79_PRODUCT;
//	}
//	public BigDecimal getR79_DAY1() {
//		return R79_DAY1;
//	}
//	public void setR79_DAY1(BigDecimal r79_DAY1) {
//		R79_DAY1 = r79_DAY1;
//	}
//	public BigDecimal getR79_DAY2_7() {
//		return R79_DAY2_7;
//	}
//	public void setR79_DAY2_7(BigDecimal r79_DAY2_7) {
//		R79_DAY2_7 = r79_DAY2_7;
//	}
//	public BigDecimal getR79_DAY8_14() {
//		return R79_DAY8_14;
//	}
//	public void setR79_DAY8_14(BigDecimal r79_DAY8_14) {
//		R79_DAY8_14 = r79_DAY8_14;
//	}
//	public BigDecimal getR79_DAY15_30() {
//		return R79_DAY15_30;
//	}
//	public void setR79_DAY15_30(BigDecimal r79_DAY15_30) {
//		R79_DAY15_30 = r79_DAY15_30;
//	}
//	public BigDecimal getR79_DAY31_TO_2M() {
//		return R79_DAY31_TO_2M;
//	}
//	public void setR79_DAY31_TO_2M(BigDecimal r79_DAY31_TO_2M) {
//		R79_DAY31_TO_2M = r79_DAY31_TO_2M;
//	}
//	public BigDecimal getR79_MORE2M_TO_3M() {
//		return R79_MORE2M_TO_3M;
//	}
//	public void setR79_MORE2M_TO_3M(BigDecimal r79_MORE2M_TO_3M) {
//		R79_MORE2M_TO_3M = r79_MORE2M_TO_3M;
//	}
//	public BigDecimal getR79_OVER3M_TO_6M() {
//		return R79_OVER3M_TO_6M;
//	}
//	public void setR79_OVER3M_TO_6M(BigDecimal r79_OVER3M_TO_6M) {
//		R79_OVER3M_TO_6M = r79_OVER3M_TO_6M;
//	}
//	public BigDecimal getR79_OVER6M_TO_1Y() {
//		return R79_OVER6M_TO_1Y;
//	}
//	public void setR79_OVER6M_TO_1Y(BigDecimal r79_OVER6M_TO_1Y) {
//		R79_OVER6M_TO_1Y = r79_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR79_OVER1Y_TO_3Y() {
//		return R79_OVER1Y_TO_3Y;
//	}
//	public void setR79_OVER1Y_TO_3Y(BigDecimal r79_OVER1Y_TO_3Y) {
//		R79_OVER1Y_TO_3Y = r79_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR79_OVER3Y_TO_5Y() {
//		return R79_OVER3Y_TO_5Y;
//	}
//	public void setR79_OVER3Y_TO_5Y(BigDecimal r79_OVER3Y_TO_5Y) {
//		R79_OVER3Y_TO_5Y = r79_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR79_OVER5Y() {
//		return R79_OVER5Y;
//	}
//	public void setR79_OVER5Y(BigDecimal r79_OVER5Y) {
//		R79_OVER5Y = r79_OVER5Y;
//	}
//	public BigDecimal getR79_TOTAL() {
//		return R79_TOTAL;
//	}
//	public void setR79_TOTAL(BigDecimal r79_TOTAL) {
//		R79_TOTAL = r79_TOTAL;
//	}
//	public String getR80_PRODUCT() {
//		return R80_PRODUCT;
//	}
//	public void setR80_PRODUCT(String r80_PRODUCT) {
//		R80_PRODUCT = r80_PRODUCT;
//	}
//	public BigDecimal getR80_DAY1() {
//		return R80_DAY1;
//	}
//	public void setR80_DAY1(BigDecimal r80_DAY1) {
//		R80_DAY1 = r80_DAY1;
//	}
//	public BigDecimal getR80_DAY2_7() {
//		return R80_DAY2_7;
//	}
//	public void setR80_DAY2_7(BigDecimal r80_DAY2_7) {
//		R80_DAY2_7 = r80_DAY2_7;
//	}
//	public BigDecimal getR80_DAY8_14() {
//		return R80_DAY8_14;
//	}
//	public void setR80_DAY8_14(BigDecimal r80_DAY8_14) {
//		R80_DAY8_14 = r80_DAY8_14;
//	}
//	public BigDecimal getR80_DAY15_30() {
//		return R80_DAY15_30;
//	}
//	public void setR80_DAY15_30(BigDecimal r80_DAY15_30) {
//		R80_DAY15_30 = r80_DAY15_30;
//	}
//	public BigDecimal getR80_DAY31_TO_2M() {
//		return R80_DAY31_TO_2M;
//	}
//	public void setR80_DAY31_TO_2M(BigDecimal r80_DAY31_TO_2M) {
//		R80_DAY31_TO_2M = r80_DAY31_TO_2M;
//	}
//	public BigDecimal getR80_MORE2M_TO_3M() {
//		return R80_MORE2M_TO_3M;
//	}
//	public void setR80_MORE2M_TO_3M(BigDecimal r80_MORE2M_TO_3M) {
//		R80_MORE2M_TO_3M = r80_MORE2M_TO_3M;
//	}
//	public BigDecimal getR80_OVER3M_TO_6M() {
//		return R80_OVER3M_TO_6M;
//	}
//	public void setR80_OVER3M_TO_6M(BigDecimal r80_OVER3M_TO_6M) {
//		R80_OVER3M_TO_6M = r80_OVER3M_TO_6M;
//	}
//	public BigDecimal getR80_OVER6M_TO_1Y() {
//		return R80_OVER6M_TO_1Y;
//	}
//	public void setR80_OVER6M_TO_1Y(BigDecimal r80_OVER6M_TO_1Y) {
//		R80_OVER6M_TO_1Y = r80_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR80_OVER1Y_TO_3Y() {
//		return R80_OVER1Y_TO_3Y;
//	}
//	public void setR80_OVER1Y_TO_3Y(BigDecimal r80_OVER1Y_TO_3Y) {
//		R80_OVER1Y_TO_3Y = r80_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR80_OVER3Y_TO_5Y() {
//		return R80_OVER3Y_TO_5Y;
//	}
//	public void setR80_OVER3Y_TO_5Y(BigDecimal r80_OVER3Y_TO_5Y) {
//		R80_OVER3Y_TO_5Y = r80_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR80_OVER5Y() {
//		return R80_OVER5Y;
//	}
//	public void setR80_OVER5Y(BigDecimal r80_OVER5Y) {
//		R80_OVER5Y = r80_OVER5Y;
//	}
//	public BigDecimal getR80_TOTAL() {
//		return R80_TOTAL;
//	}
//	public void setR80_TOTAL(BigDecimal r80_TOTAL) {
//		R80_TOTAL = r80_TOTAL;
//	}
//	public String getR81_PRODUCT() {
//		return R81_PRODUCT;
//	}
//	public void setR81_PRODUCT(String r81_PRODUCT) {
//		R81_PRODUCT = r81_PRODUCT;
//	}
//	public BigDecimal getR81_DAY1() {
//		return R81_DAY1;
//	}
//	public void setR81_DAY1(BigDecimal r81_DAY1) {
//		R81_DAY1 = r81_DAY1;
//	}
//	public BigDecimal getR81_DAY2_7() {
//		return R81_DAY2_7;
//	}
//	public void setR81_DAY2_7(BigDecimal r81_DAY2_7) {
//		R81_DAY2_7 = r81_DAY2_7;
//	}
//	public BigDecimal getR81_DAY8_14() {
//		return R81_DAY8_14;
//	}
//	public void setR81_DAY8_14(BigDecimal r81_DAY8_14) {
//		R81_DAY8_14 = r81_DAY8_14;
//	}
//	public BigDecimal getR81_DAY15_30() {
//		return R81_DAY15_30;
//	}
//	public void setR81_DAY15_30(BigDecimal r81_DAY15_30) {
//		R81_DAY15_30 = r81_DAY15_30;
//	}
//	public BigDecimal getR81_DAY31_TO_2M() {
//		return R81_DAY31_TO_2M;
//	}
//	public void setR81_DAY31_TO_2M(BigDecimal r81_DAY31_TO_2M) {
//		R81_DAY31_TO_2M = r81_DAY31_TO_2M;
//	}
//	public BigDecimal getR81_MORE2M_TO_3M() {
//		return R81_MORE2M_TO_3M;
//	}
//	public void setR81_MORE2M_TO_3M(BigDecimal r81_MORE2M_TO_3M) {
//		R81_MORE2M_TO_3M = r81_MORE2M_TO_3M;
//	}
//	public BigDecimal getR81_OVER3M_TO_6M() {
//		return R81_OVER3M_TO_6M;
//	}
//	public void setR81_OVER3M_TO_6M(BigDecimal r81_OVER3M_TO_6M) {
//		R81_OVER3M_TO_6M = r81_OVER3M_TO_6M;
//	}
//	public BigDecimal getR81_OVER6M_TO_1Y() {
//		return R81_OVER6M_TO_1Y;
//	}
//	public void setR81_OVER6M_TO_1Y(BigDecimal r81_OVER6M_TO_1Y) {
//		R81_OVER6M_TO_1Y = r81_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR81_OVER1Y_TO_3Y() {
//		return R81_OVER1Y_TO_3Y;
//	}
//	public void setR81_OVER1Y_TO_3Y(BigDecimal r81_OVER1Y_TO_3Y) {
//		R81_OVER1Y_TO_3Y = r81_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR81_OVER3Y_TO_5Y() {
//		return R81_OVER3Y_TO_5Y;
//	}
//	public void setR81_OVER3Y_TO_5Y(BigDecimal r81_OVER3Y_TO_5Y) {
//		R81_OVER3Y_TO_5Y = r81_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR81_OVER5Y() {
//		return R81_OVER5Y;
//	}
//	public void setR81_OVER5Y(BigDecimal r81_OVER5Y) {
//		R81_OVER5Y = r81_OVER5Y;
//	}
//	public BigDecimal getR81_TOTAL() {
//		return R81_TOTAL;
//	}
//	public void setR81_TOTAL(BigDecimal r81_TOTAL) {
//		R81_TOTAL = r81_TOTAL;
//	}
//	public String getR82_PRODUCT() {
//		return R82_PRODUCT;
//	}
//	public void setR82_PRODUCT(String r82_PRODUCT) {
//		R82_PRODUCT = r82_PRODUCT;
//	}
//	public BigDecimal getR82_DAY1() {
//		return R82_DAY1;
//	}
//	public void setR82_DAY1(BigDecimal r82_DAY1) {
//		R82_DAY1 = r82_DAY1;
//	}
//	public BigDecimal getR82_DAY2_7() {
//		return R82_DAY2_7;
//	}
//	public void setR82_DAY2_7(BigDecimal r82_DAY2_7) {
//		R82_DAY2_7 = r82_DAY2_7;
//	}
//	public BigDecimal getR82_DAY8_14() {
//		return R82_DAY8_14;
//	}
//	public void setR82_DAY8_14(BigDecimal r82_DAY8_14) {
//		R82_DAY8_14 = r82_DAY8_14;
//	}
//	public BigDecimal getR82_DAY15_30() {
//		return R82_DAY15_30;
//	}
//	public void setR82_DAY15_30(BigDecimal r82_DAY15_30) {
//		R82_DAY15_30 = r82_DAY15_30;
//	}
//	public BigDecimal getR82_DAY31_TO_2M() {
//		return R82_DAY31_TO_2M;
//	}
//	public void setR82_DAY31_TO_2M(BigDecimal r82_DAY31_TO_2M) {
//		R82_DAY31_TO_2M = r82_DAY31_TO_2M;
//	}
//	public BigDecimal getR82_MORE2M_TO_3M() {
//		return R82_MORE2M_TO_3M;
//	}
//	public void setR82_MORE2M_TO_3M(BigDecimal r82_MORE2M_TO_3M) {
//		R82_MORE2M_TO_3M = r82_MORE2M_TO_3M;
//	}
//	public BigDecimal getR82_OVER3M_TO_6M() {
//		return R82_OVER3M_TO_6M;
//	}
//	public void setR82_OVER3M_TO_6M(BigDecimal r82_OVER3M_TO_6M) {
//		R82_OVER3M_TO_6M = r82_OVER3M_TO_6M;
//	}
//	public BigDecimal getR82_OVER6M_TO_1Y() {
//		return R82_OVER6M_TO_1Y;
//	}
//	public void setR82_OVER6M_TO_1Y(BigDecimal r82_OVER6M_TO_1Y) {
//		R82_OVER6M_TO_1Y = r82_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR82_OVER1Y_TO_3Y() {
//		return R82_OVER1Y_TO_3Y;
//	}
//	public void setR82_OVER1Y_TO_3Y(BigDecimal r82_OVER1Y_TO_3Y) {
//		R82_OVER1Y_TO_3Y = r82_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR82_OVER3Y_TO_5Y() {
//		return R82_OVER3Y_TO_5Y;
//	}
//	public void setR82_OVER3Y_TO_5Y(BigDecimal r82_OVER3Y_TO_5Y) {
//		R82_OVER3Y_TO_5Y = r82_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR82_OVER5Y() {
//		return R82_OVER5Y;
//	}
//	public void setR82_OVER5Y(BigDecimal r82_OVER5Y) {
//		R82_OVER5Y = r82_OVER5Y;
//	}
//	public BigDecimal getR82_TOTAL() {
//		return R82_TOTAL;
//	}
//	public void setR82_TOTAL(BigDecimal r82_TOTAL) {
//		R82_TOTAL = r82_TOTAL;
//	}
//	public String getR83_PRODUCT() {
//		return R83_PRODUCT;
//	}
//	public void setR83_PRODUCT(String r83_PRODUCT) {
//		R83_PRODUCT = r83_PRODUCT;
//	}
//	public BigDecimal getR83_DAY1() {
//		return R83_DAY1;
//	}
//	public void setR83_DAY1(BigDecimal r83_DAY1) {
//		R83_DAY1 = r83_DAY1;
//	}
//	public BigDecimal getR83_DAY2_7() {
//		return R83_DAY2_7;
//	}
//	public void setR83_DAY2_7(BigDecimal r83_DAY2_7) {
//		R83_DAY2_7 = r83_DAY2_7;
//	}
//	public BigDecimal getR83_DAY8_14() {
//		return R83_DAY8_14;
//	}
//	public void setR83_DAY8_14(BigDecimal r83_DAY8_14) {
//		R83_DAY8_14 = r83_DAY8_14;
//	}
//	public BigDecimal getR83_DAY15_30() {
//		return R83_DAY15_30;
//	}
//	public void setR83_DAY15_30(BigDecimal r83_DAY15_30) {
//		R83_DAY15_30 = r83_DAY15_30;
//	}
//	public BigDecimal getR83_DAY31_TO_2M() {
//		return R83_DAY31_TO_2M;
//	}
//	public void setR83_DAY31_TO_2M(BigDecimal r83_DAY31_TO_2M) {
//		R83_DAY31_TO_2M = r83_DAY31_TO_2M;
//	}
//	public BigDecimal getR83_MORE2M_TO_3M() {
//		return R83_MORE2M_TO_3M;
//	}
//	public void setR83_MORE2M_TO_3M(BigDecimal r83_MORE2M_TO_3M) {
//		R83_MORE2M_TO_3M = r83_MORE2M_TO_3M;
//	}
//	public BigDecimal getR83_OVER3M_TO_6M() {
//		return R83_OVER3M_TO_6M;
//	}
//	public void setR83_OVER3M_TO_6M(BigDecimal r83_OVER3M_TO_6M) {
//		R83_OVER3M_TO_6M = r83_OVER3M_TO_6M;
//	}
//	public BigDecimal getR83_OVER6M_TO_1Y() {
//		return R83_OVER6M_TO_1Y;
//	}
//	public void setR83_OVER6M_TO_1Y(BigDecimal r83_OVER6M_TO_1Y) {
//		R83_OVER6M_TO_1Y = r83_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR83_OVER1Y_TO_3Y() {
//		return R83_OVER1Y_TO_3Y;
//	}
//	public void setR83_OVER1Y_TO_3Y(BigDecimal r83_OVER1Y_TO_3Y) {
//		R83_OVER1Y_TO_3Y = r83_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR83_OVER3Y_TO_5Y() {
//		return R83_OVER3Y_TO_5Y;
//	}
//	public void setR83_OVER3Y_TO_5Y(BigDecimal r83_OVER3Y_TO_5Y) {
//		R83_OVER3Y_TO_5Y = r83_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR83_OVER5Y() {
//		return R83_OVER5Y;
//	}
//	public void setR83_OVER5Y(BigDecimal r83_OVER5Y) {
//		R83_OVER5Y = r83_OVER5Y;
//	}
//	public BigDecimal getR83_TOTAL() {
//		return R83_TOTAL;
//	}
//	public void setR83_TOTAL(BigDecimal r83_TOTAL) {
//		R83_TOTAL = r83_TOTAL;
//	}
	public String getR84_PRODUCT() {
		return R84_PRODUCT;
	}
	public void setR84_PRODUCT(String r84_PRODUCT) {
		R84_PRODUCT = r84_PRODUCT;
	}
	public BigDecimal getR84_DAY1() {
		return R84_DAY1;
	}
	public void setR84_DAY1(BigDecimal r84_DAY1) {
		R84_DAY1 = r84_DAY1;
	}
	public BigDecimal getR84_DAY2_7() {
		return R84_DAY2_7;
	}
	public void setR84_DAY2_7(BigDecimal r84_DAY2_7) {
		R84_DAY2_7 = r84_DAY2_7;
	}
	public BigDecimal getR84_DAY8_14() {
		return R84_DAY8_14;
	}
	public void setR84_DAY8_14(BigDecimal r84_DAY8_14) {
		R84_DAY8_14 = r84_DAY8_14;
	}
	public BigDecimal getR84_DAY15_30() {
		return R84_DAY15_30;
	}
	public void setR84_DAY15_30(BigDecimal r84_DAY15_30) {
		R84_DAY15_30 = r84_DAY15_30;
	}
	public BigDecimal getR84_DAY31_TO_2M() {
		return R84_DAY31_TO_2M;
	}
	public void setR84_DAY31_TO_2M(BigDecimal r84_DAY31_TO_2M) {
		R84_DAY31_TO_2M = r84_DAY31_TO_2M;
	}
	public BigDecimal getR84_MORE2M_TO_3M() {
		return R84_MORE2M_TO_3M;
	}
	public void setR84_MORE2M_TO_3M(BigDecimal r84_MORE2M_TO_3M) {
		R84_MORE2M_TO_3M = r84_MORE2M_TO_3M;
	}
	public BigDecimal getR84_OVER3M_TO_6M() {
		return R84_OVER3M_TO_6M;
	}
	public void setR84_OVER3M_TO_6M(BigDecimal r84_OVER3M_TO_6M) {
		R84_OVER3M_TO_6M = r84_OVER3M_TO_6M;
	}
	public BigDecimal getR84_OVER6M_TO_1Y() {
		return R84_OVER6M_TO_1Y;
	}
	public void setR84_OVER6M_TO_1Y(BigDecimal r84_OVER6M_TO_1Y) {
		R84_OVER6M_TO_1Y = r84_OVER6M_TO_1Y;
	}
	public BigDecimal getR84_OVER1Y_TO_3Y() {
		return R84_OVER1Y_TO_3Y;
	}
	public void setR84_OVER1Y_TO_3Y(BigDecimal r84_OVER1Y_TO_3Y) {
		R84_OVER1Y_TO_3Y = r84_OVER1Y_TO_3Y;
	}
	public BigDecimal getR84_OVER3Y_TO_5Y() {
		return R84_OVER3Y_TO_5Y;
	}
	public void setR84_OVER3Y_TO_5Y(BigDecimal r84_OVER3Y_TO_5Y) {
		R84_OVER3Y_TO_5Y = r84_OVER3Y_TO_5Y;
	}
	public BigDecimal getR84_OVER5Y() {
		return R84_OVER5Y;
	}
	public void setR84_OVER5Y(BigDecimal r84_OVER5Y) {
		R84_OVER5Y = r84_OVER5Y;
	}
	public BigDecimal getR84_TOTAL() {
		return R84_TOTAL;
	}
	public void setR84_TOTAL(BigDecimal r84_TOTAL) {
		R84_TOTAL = r84_TOTAL;
	}
	public String getR85_PRODUCT() {
		return R85_PRODUCT;
	}
	public void setR85_PRODUCT(String r85_PRODUCT) {
		R85_PRODUCT = r85_PRODUCT;
	}
	public BigDecimal getR85_DAY1() {
		return R85_DAY1;
	}
	public void setR85_DAY1(BigDecimal r85_DAY1) {
		R85_DAY1 = r85_DAY1;
	}
	public BigDecimal getR85_DAY2_7() {
		return R85_DAY2_7;
	}
	public void setR85_DAY2_7(BigDecimal r85_DAY2_7) {
		R85_DAY2_7 = r85_DAY2_7;
	}
	public BigDecimal getR85_DAY8_14() {
		return R85_DAY8_14;
	}
	public void setR85_DAY8_14(BigDecimal r85_DAY8_14) {
		R85_DAY8_14 = r85_DAY8_14;
	}
	public BigDecimal getR85_DAY15_30() {
		return R85_DAY15_30;
	}
	public void setR85_DAY15_30(BigDecimal r85_DAY15_30) {
		R85_DAY15_30 = r85_DAY15_30;
	}
	public BigDecimal getR85_DAY31_TO_2M() {
		return R85_DAY31_TO_2M;
	}
	public void setR85_DAY31_TO_2M(BigDecimal r85_DAY31_TO_2M) {
		R85_DAY31_TO_2M = r85_DAY31_TO_2M;
	}
	public BigDecimal getR85_MORE2M_TO_3M() {
		return R85_MORE2M_TO_3M;
	}
	public void setR85_MORE2M_TO_3M(BigDecimal r85_MORE2M_TO_3M) {
		R85_MORE2M_TO_3M = r85_MORE2M_TO_3M;
	}
	public BigDecimal getR85_OVER3M_TO_6M() {
		return R85_OVER3M_TO_6M;
	}
	public void setR85_OVER3M_TO_6M(BigDecimal r85_OVER3M_TO_6M) {
		R85_OVER3M_TO_6M = r85_OVER3M_TO_6M;
	}
	public BigDecimal getR85_OVER6M_TO_1Y() {
		return R85_OVER6M_TO_1Y;
	}
	public void setR85_OVER6M_TO_1Y(BigDecimal r85_OVER6M_TO_1Y) {
		R85_OVER6M_TO_1Y = r85_OVER6M_TO_1Y;
	}
	public BigDecimal getR85_OVER1Y_TO_3Y() {
		return R85_OVER1Y_TO_3Y;
	}
	public void setR85_OVER1Y_TO_3Y(BigDecimal r85_OVER1Y_TO_3Y) {
		R85_OVER1Y_TO_3Y = r85_OVER1Y_TO_3Y;
	}
	public BigDecimal getR85_OVER3Y_TO_5Y() {
		return R85_OVER3Y_TO_5Y;
	}
	public void setR85_OVER3Y_TO_5Y(BigDecimal r85_OVER3Y_TO_5Y) {
		R85_OVER3Y_TO_5Y = r85_OVER3Y_TO_5Y;
	}
	public BigDecimal getR85_OVER5Y() {
		return R85_OVER5Y;
	}
	public void setR85_OVER5Y(BigDecimal r85_OVER5Y) {
		R85_OVER5Y = r85_OVER5Y;
	}
	public BigDecimal getR85_TOTAL() {
		return R85_TOTAL;
	}
	public void setR85_TOTAL(BigDecimal r85_TOTAL) {
		R85_TOTAL = r85_TOTAL;
	}
	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}
	public void setREPORT_DATE(Date rEPORT_DATE) {
		REPORT_DATE = rEPORT_DATE;
	}
	public String getREPORT_VERSION() {
		return REPORT_VERSION;
	}
	public void setREPORT_VERSION(String rEPORT_VERSION) {
		REPORT_VERSION = rEPORT_VERSION;
	}
	public String getREPORT_CURRENCY() {
		return REPORT_CURRENCY;
	}
	public void setREPORT_CURRENCY(String rEPORT_CURRENCY) {
		REPORT_CURRENCY = rEPORT_CURRENCY;
	}
	public String getREPORT_CODE() {
		return REPORT_CODE;
	}
	public void setREPORT_CODE(String rEPORT_CODE) {
		REPORT_CODE = rEPORT_CODE;
	}
	public String getREPORT_DESC() {
		return REPORT_DESC;
	}
	public void setREPORT_DESC(String rEPORT_DESC) {
		REPORT_DESC = rEPORT_DESC;
	}
	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}
	public void setENTITY_FLG(String eNTITY_FLG) {
		ENTITY_FLG = eNTITY_FLG;
	}
	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}
	public void setMODIFY_FLG(String mODIFY_FLG) {
		MODIFY_FLG = mODIFY_FLG;
	}
	public String getDEL_FLG() {
		return DEL_FLG;
	}
	public void setDEL_FLG(String dEL_FLG) {
		DEL_FLG = dEL_FLG;
	}
	public SLS_INPUT_SHT_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	}

	// ------------------------------
	// Entity class for SLS_INPUT_SHT Detail Report
	// ------------------------------
	public static class SLS_INPUT_SHT_Detail_Entity {

	  @Column(name = "CUST_ID", length = 100)
	    private String custId;

	  @Id
	    @Column(name = "ACCT_NUMBER", length = 100)
	    private String acctNumber;

	    @Column(name = "ACCT_NAME", length = 100)
	    private String acctName;

	    @Column(name = "DATA_TYPE", length = 100)
	    private String dataType;
    
	    @Column(name = "ROW_ID", length = 100)
	    private String rowId;
    
	    @Column(name = "COLUMN_ID", length = 100)
	    private String columnId;

	    @Column(name = "REPORT_REMARKS", length = 100)
	    private String reportRemarks;

	    @Column(name = "MODIFICATION_REMARKS", length = 100)
	    private String modificationRemarks;

	    @Column(name = "DATA_ENTRY_VERSION", length = 100)
	    private String dataEntryVersion;

	    @Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
	    private BigDecimal acctBalanceInPula;
    
	    @Column(name = "PROVISION", precision = 32, scale = 2)
	    private BigDecimal provision;

	    @Column(name = "REPORT_DATE")
	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    private Date reportDate;

	    @Column(name = "REPORT_NAME", length = 100)
	    private String reportName;

	    @Column(name = "CREATE_USER", length = 50)
	    private String createUser;

	    @Column(name = "CREATE_TIME")
	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    private Date createTime;

	    @Column(name = "MODIFY_USER", length = 50)
	    private String modifyUser;

	    @Column(name = "MODIFY_TIME")
	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    private Date modifyTime;

	    @Column(name = "VERIFY_USER", length = 50)
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

		public String getRowId() {
			return rowId;
		}

		public void setRowId(String rowId) {
			this.rowId = rowId;
		}

		public String getColumnId() {
			return columnId;
		}

		public void setColumnId(String columnId) {
			this.columnId = columnId;
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

		public BigDecimal getAcctBalanceInPula() {
			return acctBalanceInPula;
		}

		public void setAcctBalanceInPula(BigDecimal acctBalanceInPula) {
			this.acctBalanceInPula = acctBalanceInPula;
		}

		public BigDecimal getProvision() {
			return provision;
		}

		public void setProvision(BigDecimal provision) {
			this.provision = provision;
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

		public SLS_INPUT_SHT_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	// ------------------------------
	// Entity class for SLS_INPUT_SHT Archival Summary Report
	// ------------------------------
	public static class SLS_INPUT_SHT_Archival_Summary_Entity {
	@Id
	private String SRL_NO;
	private String R11_PRODUCT;
	private BigDecimal R11_DAY1;
	private BigDecimal R11_DAY2_7;
	private BigDecimal R11_DAY8_14;
	private BigDecimal R11_DAY15_30;
	private BigDecimal R11_DAY31_TO_2M;
	private BigDecimal R11_MORE2M_TO_3M;
	private BigDecimal R11_OVER3M_TO_6M;
	private BigDecimal R11_OVER6M_TO_1Y;
	private BigDecimal R11_OVER1Y_TO_3Y;
	private BigDecimal R11_OVER3Y_TO_5Y;
	private BigDecimal R11_OVER5Y;
	private BigDecimal R11_TOTAL;
	private String R12_PRODUCT;
	private BigDecimal R12_DAY1;
	private BigDecimal R12_DAY2_7;
	private BigDecimal R12_DAY8_14;
	private BigDecimal R12_DAY15_30;
	private BigDecimal R12_DAY31_TO_2M;
	private BigDecimal R12_MORE2M_TO_3M;
	private BigDecimal R12_OVER3M_TO_6M;
	private BigDecimal R12_OVER6M_TO_1Y;
	private BigDecimal R12_OVER1Y_TO_3Y;
	private BigDecimal R12_OVER3Y_TO_5Y;
	private BigDecimal R12_OVER5Y;
	private BigDecimal R12_TOTAL;
	private String R13_PRODUCT;
	private BigDecimal R13_DAY1;
	private BigDecimal R13_DAY2_7;
	private BigDecimal R13_DAY8_14;
	private BigDecimal R13_DAY15_30;
	private BigDecimal R13_DAY31_TO_2M;
	private BigDecimal R13_MORE2M_TO_3M;
	private BigDecimal R13_OVER3M_TO_6M;
	private BigDecimal R13_OVER6M_TO_1Y;
	private BigDecimal R13_OVER1Y_TO_3Y;
	private BigDecimal R13_OVER3Y_TO_5Y;
	private BigDecimal R13_OVER5Y;
	private BigDecimal R13_TOTAL;
	private String R14_PRODUCT;
	private BigDecimal R14_DAY1;
	private BigDecimal R14_DAY2_7;
	private BigDecimal R14_DAY8_14;
	private BigDecimal R14_DAY15_30;
	private BigDecimal R14_DAY31_TO_2M;
	private BigDecimal R14_MORE2M_TO_3M;
	private BigDecimal R14_OVER3M_TO_6M;
	private BigDecimal R14_OVER6M_TO_1Y;
	private BigDecimal R14_OVER1Y_TO_3Y;
	private BigDecimal R14_OVER3Y_TO_5Y;
	private BigDecimal R14_OVER5Y;
	private BigDecimal R14_TOTAL;
	private String R15_PRODUCT;
	private BigDecimal R15_DAY1;
	private BigDecimal R15_DAY2_7;
	private BigDecimal R15_DAY8_14;
	private BigDecimal R15_DAY15_30;
	private BigDecimal R15_DAY31_TO_2M;
	private BigDecimal R15_MORE2M_TO_3M;
	private BigDecimal R15_OVER3M_TO_6M;
	private BigDecimal R15_OVER6M_TO_1Y;
	private BigDecimal R15_OVER1Y_TO_3Y;
	private BigDecimal R15_OVER3Y_TO_5Y;
	private BigDecimal R15_OVER5Y;
	private BigDecimal R15_TOTAL;
	private String R16_PRODUCT;
	private BigDecimal R16_DAY1;
	private BigDecimal R16_DAY2_7;
	private BigDecimal R16_DAY8_14;
	private BigDecimal R16_DAY15_30;
	private BigDecimal R16_DAY31_TO_2M;
	private BigDecimal R16_MORE2M_TO_3M;
	private BigDecimal R16_OVER3M_TO_6M;
	private BigDecimal R16_OVER6M_TO_1Y;
	private BigDecimal R16_OVER1Y_TO_3Y;
	private BigDecimal R16_OVER3Y_TO_5Y;
	private BigDecimal R16_OVER5Y;
	private BigDecimal R16_TOTAL;
	private String R17_PRODUCT;
	private BigDecimal R17_DAY1;
	private BigDecimal R17_DAY2_7;
	private BigDecimal R17_DAY8_14;
	private BigDecimal R17_DAY15_30;
	private BigDecimal R17_DAY31_TO_2M;
	private BigDecimal R17_MORE2M_TO_3M;
	private BigDecimal R17_OVER3M_TO_6M;
	private BigDecimal R17_OVER6M_TO_1Y;
	private BigDecimal R17_OVER1Y_TO_3Y;
	private BigDecimal R17_OVER3Y_TO_5Y;
	private BigDecimal R17_OVER5Y;
	private BigDecimal R17_TOTAL;
	private String R18_PRODUCT;
	private BigDecimal R18_DAY1;
	private BigDecimal R18_DAY2_7;
	private BigDecimal R18_DAY8_14;
	private BigDecimal R18_DAY15_30;
	private BigDecimal R18_DAY31_TO_2M;
	private BigDecimal R18_MORE2M_TO_3M;
	private BigDecimal R18_OVER3M_TO_6M;
	private BigDecimal R18_OVER6M_TO_1Y;
	private BigDecimal R18_OVER1Y_TO_3Y;
	private BigDecimal R18_OVER3Y_TO_5Y;
	private BigDecimal R18_OVER5Y;
	private BigDecimal R18_TOTAL;
	private String R19_PRODUCT;
	private BigDecimal R19_DAY1;
	private BigDecimal R19_DAY2_7;
	private BigDecimal R19_DAY8_14;
	private BigDecimal R19_DAY15_30;
	private BigDecimal R19_DAY31_TO_2M;
	private BigDecimal R19_MORE2M_TO_3M;
	private BigDecimal R19_OVER3M_TO_6M;
	private BigDecimal R19_OVER6M_TO_1Y;
	private BigDecimal R19_OVER1Y_TO_3Y;
	private BigDecimal R19_OVER3Y_TO_5Y;
	private BigDecimal R19_OVER5Y;
	private BigDecimal R19_TOTAL;
	private String R20_PRODUCT;
	private BigDecimal R20_DAY1;
	private BigDecimal R20_DAY2_7;
	private BigDecimal R20_DAY8_14;
	private BigDecimal R20_DAY15_30;
	private BigDecimal R20_DAY31_TO_2M;
	private BigDecimal R20_MORE2M_TO_3M;
	private BigDecimal R20_OVER3M_TO_6M;
	private BigDecimal R20_OVER6M_TO_1Y;
	private BigDecimal R20_OVER1Y_TO_3Y;
	private BigDecimal R20_OVER3Y_TO_5Y;
	private BigDecimal R20_OVER5Y;
	private BigDecimal R20_TOTAL;
	private String R21_PRODUCT;
	private BigDecimal R21_DAY1;
	private BigDecimal R21_DAY2_7;
	private BigDecimal R21_DAY8_14;
	private BigDecimal R21_DAY15_30;
	private BigDecimal R21_DAY31_TO_2M;
	private BigDecimal R21_MORE2M_TO_3M;
	private BigDecimal R21_OVER3M_TO_6M;
	private BigDecimal R21_OVER6M_TO_1Y;
	private BigDecimal R21_OVER1Y_TO_3Y;
	private BigDecimal R21_OVER3Y_TO_5Y;
	private BigDecimal R21_OVER5Y;
	private BigDecimal R21_TOTAL;
	private String R22_PRODUCT;
	private BigDecimal R22_DAY1;
	private BigDecimal R22_DAY2_7;
	private BigDecimal R22_DAY8_14;
	private BigDecimal R22_DAY15_30;
	private BigDecimal R22_DAY31_TO_2M;
	private BigDecimal R22_MORE2M_TO_3M;
	private BigDecimal R22_OVER3M_TO_6M;
	private BigDecimal R22_OVER6M_TO_1Y;
	private BigDecimal R22_OVER1Y_TO_3Y;
	private BigDecimal R22_OVER3Y_TO_5Y;
	private BigDecimal R22_OVER5Y;
	private BigDecimal R22_TOTAL;
	private String R23_PRODUCT;
	private BigDecimal R23_DAY1;
	private BigDecimal R23_DAY2_7;
	private BigDecimal R23_DAY8_14;
	private BigDecimal R23_DAY15_30;
	private BigDecimal R23_DAY31_TO_2M;
	private BigDecimal R23_MORE2M_TO_3M;
	private BigDecimal R23_OVER3M_TO_6M;
	private BigDecimal R23_OVER6M_TO_1Y;
	private BigDecimal R23_OVER1Y_TO_3Y;
	private BigDecimal R23_OVER3Y_TO_5Y;
	private BigDecimal R23_OVER5Y;
	private BigDecimal R23_TOTAL;
	private String R24_PRODUCT;
	private BigDecimal R24_DAY1;
	private BigDecimal R24_DAY2_7;
	private BigDecimal R24_DAY8_14;
	private BigDecimal R24_DAY15_30;
	private BigDecimal R24_DAY31_TO_2M;
	private BigDecimal R24_MORE2M_TO_3M;
	private BigDecimal R24_OVER3M_TO_6M;
	private BigDecimal R24_OVER6M_TO_1Y;
	private BigDecimal R24_OVER1Y_TO_3Y;
	private BigDecimal R24_OVER3Y_TO_5Y;
	private BigDecimal R24_OVER5Y;
	private BigDecimal R24_TOTAL;
	private String R25_PRODUCT;
	private BigDecimal R25_DAY1;
	private BigDecimal R25_DAY2_7;
	private BigDecimal R25_DAY8_14;
	private BigDecimal R25_DAY15_30;
	private BigDecimal R25_DAY31_TO_2M;
	private BigDecimal R25_MORE2M_TO_3M;
	private BigDecimal R25_OVER3M_TO_6M;
	private BigDecimal R25_OVER6M_TO_1Y;
	private BigDecimal R25_OVER1Y_TO_3Y;
	private BigDecimal R25_OVER3Y_TO_5Y;
	private BigDecimal R25_OVER5Y;
	private BigDecimal R25_TOTAL;
	private String R26_PRODUCT;
	private BigDecimal R26_DAY1;
	private BigDecimal R26_DAY2_7;
	private BigDecimal R26_DAY8_14;
	private BigDecimal R26_DAY15_30;
	private BigDecimal R26_DAY31_TO_2M;
	private BigDecimal R26_MORE2M_TO_3M;
	private BigDecimal R26_OVER3M_TO_6M;
	private BigDecimal R26_OVER6M_TO_1Y;
	private BigDecimal R26_OVER1Y_TO_3Y;
	private BigDecimal R26_OVER3Y_TO_5Y;
	private BigDecimal R26_OVER5Y;
	private BigDecimal R26_TOTAL;
	private String R27_PRODUCT;
	private BigDecimal R27_DAY1;
	private BigDecimal R27_DAY2_7;
	private BigDecimal R27_DAY8_14;
	private BigDecimal R27_DAY15_30;
	private BigDecimal R27_DAY31_TO_2M;
	private BigDecimal R27_MORE2M_TO_3M;
	private BigDecimal R27_OVER3M_TO_6M;
	private BigDecimal R27_OVER6M_TO_1Y;
	private BigDecimal R27_OVER1Y_TO_3Y;
	private BigDecimal R27_OVER3Y_TO_5Y;
	private BigDecimal R27_OVER5Y;
	private BigDecimal R27_TOTAL;
	private String R28_PRODUCT;
	private BigDecimal R28_DAY1;
	private BigDecimal R28_DAY2_7;
	private BigDecimal R28_DAY8_14;
	private BigDecimal R28_DAY15_30;
	private BigDecimal R28_DAY31_TO_2M;
	private BigDecimal R28_MORE2M_TO_3M;
	private BigDecimal R28_OVER3M_TO_6M;
	private BigDecimal R28_OVER6M_TO_1Y;
	private BigDecimal R28_OVER1Y_TO_3Y;
	private BigDecimal R28_OVER3Y_TO_5Y;
	private BigDecimal R28_OVER5Y;
	private BigDecimal R28_TOTAL;
	private String R29_PRODUCT;
	private BigDecimal R29_DAY1;
	private BigDecimal R29_DAY2_7;
	private BigDecimal R29_DAY8_14;
	private BigDecimal R29_DAY15_30;
	private BigDecimal R29_DAY31_TO_2M;
	private BigDecimal R29_MORE2M_TO_3M;
	private BigDecimal R29_OVER3M_TO_6M;
	private BigDecimal R29_OVER6M_TO_1Y;
	private BigDecimal R29_OVER1Y_TO_3Y;
	private BigDecimal R29_OVER3Y_TO_5Y;
	private BigDecimal R29_OVER5Y;
	private BigDecimal R29_TOTAL;
	private String R30_PRODUCT;
	private BigDecimal R30_DAY1;
	private BigDecimal R30_DAY2_7;
	private BigDecimal R30_DAY8_14;
	private BigDecimal R30_DAY15_30;
	private BigDecimal R30_DAY31_TO_2M;
	private BigDecimal R30_MORE2M_TO_3M;
	private BigDecimal R30_OVER3M_TO_6M;
	private BigDecimal R30_OVER6M_TO_1Y;
	private BigDecimal R30_OVER1Y_TO_3Y;
	private BigDecimal R30_OVER3Y_TO_5Y;
	private BigDecimal R30_OVER5Y;
	private BigDecimal R30_TOTAL;
	private String R31_PRODUCT;
	private BigDecimal R31_DAY1;
	private BigDecimal R31_DAY2_7;
	private BigDecimal R31_DAY8_14;
	private BigDecimal R31_DAY15_30;
	private BigDecimal R31_DAY31_TO_2M;
	private BigDecimal R31_MORE2M_TO_3M;
	private BigDecimal R31_OVER3M_TO_6M;
	private BigDecimal R31_OVER6M_TO_1Y;
	private BigDecimal R31_OVER1Y_TO_3Y;
	private BigDecimal R31_OVER3Y_TO_5Y;
	private BigDecimal R31_OVER5Y;
	private BigDecimal R31_TOTAL;
	private String R32_PRODUCT;
	private BigDecimal R32_DAY1;
	private BigDecimal R32_DAY2_7;
	private BigDecimal R32_DAY8_14;
	private BigDecimal R32_DAY15_30;
	private BigDecimal R32_DAY31_TO_2M;
	private BigDecimal R32_MORE2M_TO_3M;
	private BigDecimal R32_OVER3M_TO_6M;
	private BigDecimal R32_OVER6M_TO_1Y;
	private BigDecimal R32_OVER1Y_TO_3Y;
	private BigDecimal R32_OVER3Y_TO_5Y;
	private BigDecimal R32_OVER5Y;
	private BigDecimal R32_TOTAL;
	private String R33_PRODUCT;
	private BigDecimal R33_DAY1;
	private BigDecimal R33_DAY2_7;
	private BigDecimal R33_DAY8_14;
	private BigDecimal R33_DAY15_30;
	private BigDecimal R33_DAY31_TO_2M;
	private BigDecimal R33_MORE2M_TO_3M;
	private BigDecimal R33_OVER3M_TO_6M;
	private BigDecimal R33_OVER6M_TO_1Y;
	private BigDecimal R33_OVER1Y_TO_3Y;
	private BigDecimal R33_OVER3Y_TO_5Y;
	private BigDecimal R33_OVER5Y;
	private BigDecimal R33_TOTAL;
	private String R34_PRODUCT;
	private BigDecimal R34_DAY1;
	private BigDecimal R34_DAY2_7;
	private BigDecimal R34_DAY8_14;
	private BigDecimal R34_DAY15_30;
	private BigDecimal R34_DAY31_TO_2M;
	private BigDecimal R34_MORE2M_TO_3M;
	private BigDecimal R34_OVER3M_TO_6M;
	private BigDecimal R34_OVER6M_TO_1Y;
	private BigDecimal R34_OVER1Y_TO_3Y;
	private BigDecimal R34_OVER3Y_TO_5Y;
	private BigDecimal R34_OVER5Y;
	private BigDecimal R34_TOTAL;
	private String R35_PRODUCT;
	private BigDecimal R35_DAY1;
	private BigDecimal R35_DAY2_7;
	private BigDecimal R35_DAY8_14;
	private BigDecimal R35_DAY15_30;
	private BigDecimal R35_DAY31_TO_2M;
	private BigDecimal R35_MORE2M_TO_3M;
	private BigDecimal R35_OVER3M_TO_6M;
	private BigDecimal R35_OVER6M_TO_1Y;
	private BigDecimal R35_OVER1Y_TO_3Y;
	private BigDecimal R35_OVER3Y_TO_5Y;
	private BigDecimal R35_OVER5Y;
	private BigDecimal R35_TOTAL;
	private String R36_PRODUCT;
	private BigDecimal R36_DAY1;
	private BigDecimal R36_DAY2_7;
	private BigDecimal R36_DAY8_14;
	private BigDecimal R36_DAY15_30;
	private BigDecimal R36_DAY31_TO_2M;
	private BigDecimal R36_MORE2M_TO_3M;
	private BigDecimal R36_OVER3M_TO_6M;
	private BigDecimal R36_OVER6M_TO_1Y;
	private BigDecimal R36_OVER1Y_TO_3Y;
	private BigDecimal R36_OVER3Y_TO_5Y;
	private BigDecimal R36_OVER5Y;
	private BigDecimal R36_TOTAL;
	private String R37_PRODUCT;
	private BigDecimal R37_DAY1;
	private BigDecimal R37_DAY2_7;
	private BigDecimal R37_DAY8_14;
	private BigDecimal R37_DAY15_30;
	private BigDecimal R37_DAY31_TO_2M;
	private BigDecimal R37_MORE2M_TO_3M;
	private BigDecimal R37_OVER3M_TO_6M;
	private BigDecimal R37_OVER6M_TO_1Y;
	private BigDecimal R37_OVER1Y_TO_3Y;
	private BigDecimal R37_OVER3Y_TO_5Y;
	private BigDecimal R37_OVER5Y;
	private BigDecimal R37_TOTAL;
	private String R38_PRODUCT;
	private BigDecimal R38_DAY1;
	private BigDecimal R38_DAY2_7;
	private BigDecimal R38_DAY8_14;
	private BigDecimal R38_DAY15_30;
	private BigDecimal R38_DAY31_TO_2M;
	private BigDecimal R38_MORE2M_TO_3M;
	private BigDecimal R38_OVER3M_TO_6M;
	private BigDecimal R38_OVER6M_TO_1Y;
	private BigDecimal R38_OVER1Y_TO_3Y;
	private BigDecimal R38_OVER3Y_TO_5Y;
	private BigDecimal R38_OVER5Y;
	private BigDecimal R38_TOTAL;
	private String R39_PRODUCT;
	private BigDecimal R39_DAY1;
	private BigDecimal R39_DAY2_7;
	private BigDecimal R39_DAY8_14;
	private BigDecimal R39_DAY15_30;
	private BigDecimal R39_DAY31_TO_2M;
	private BigDecimal R39_MORE2M_TO_3M;
	private BigDecimal R39_OVER3M_TO_6M;
	private BigDecimal R39_OVER6M_TO_1Y;
	private BigDecimal R39_OVER1Y_TO_3Y;
	private BigDecimal R39_OVER3Y_TO_5Y;
	private BigDecimal R39_OVER5Y;
	private BigDecimal R39_TOTAL;
	private String R40_PRODUCT;
	private BigDecimal R40_DAY1;
	private BigDecimal R40_DAY2_7;
	private BigDecimal R40_DAY8_14;
	private BigDecimal R40_DAY15_30;
	private BigDecimal R40_DAY31_TO_2M;
	private BigDecimal R40_MORE2M_TO_3M;
	private BigDecimal R40_OVER3M_TO_6M;
	private BigDecimal R40_OVER6M_TO_1Y;
	private BigDecimal R40_OVER1Y_TO_3Y;
	private BigDecimal R40_OVER3Y_TO_5Y;
	private BigDecimal R40_OVER5Y;
	private BigDecimal R40_TOTAL;
//	private String R41_PRODUCT;
//	private BigDecimal R41_DAY1;
//	private BigDecimal R41_DAY2_7;
//	private BigDecimal R41_DAY8_14;
//	private BigDecimal R41_DAY15_30;
//	private BigDecimal R41_DAY31_TO_2M;
//	private BigDecimal R41_MORE2M_TO_3M;
//	private BigDecimal R41_OVER3M_TO_6M;
//	private BigDecimal R41_OVER6M_TO_1Y;
//	private BigDecimal R41_OVER1Y_TO_3Y;
//	private BigDecimal R41_OVER3Y_TO_5Y;
//	private BigDecimal R41_OVER5Y;
//	private BigDecimal R41_TOTAL;
//	private String R42_PRODUCT;
//	private BigDecimal R42_DAY1;
//	private BigDecimal R42_DAY2_7;
//	private BigDecimal R42_DAY8_14;
//	private BigDecimal R42_DAY15_30;
//	private BigDecimal R42_DAY31_TO_2M;
//	private BigDecimal R42_MORE2M_TO_3M;
//	private BigDecimal R42_OVER3M_TO_6M;
//	private BigDecimal R42_OVER6M_TO_1Y;
//	private BigDecimal R42_OVER1Y_TO_3Y;
//	private BigDecimal R42_OVER3Y_TO_5Y;
//	private BigDecimal R42_OVER5Y;
//	private BigDecimal R42_TOTAL;
//	private String R43_PRODUCT;
//	private BigDecimal R43_DAY1;
//	private BigDecimal R43_DAY2_7;
//	private BigDecimal R43_DAY8_14;
//	private BigDecimal R43_DAY15_30;
//	private BigDecimal R43_DAY31_TO_2M;
//	private BigDecimal R43_MORE2M_TO_3M;
//	private BigDecimal R43_OVER3M_TO_6M;
//	private BigDecimal R43_OVER6M_TO_1Y;
//	private BigDecimal R43_OVER1Y_TO_3Y;
//	private BigDecimal R43_OVER3Y_TO_5Y;
//	private BigDecimal R43_OVER5Y;
//	private BigDecimal R43_TOTAL;
//	private String R44_PRODUCT;
//	private BigDecimal R44_DAY1;
//	private BigDecimal R44_DAY2_7;
//	private BigDecimal R44_DAY8_14;
//	private BigDecimal R44_DAY15_30;
//	private BigDecimal R44_DAY31_TO_2M;
//	private BigDecimal R44_MORE2M_TO_3M;
//	private BigDecimal R44_OVER3M_TO_6M;
//	private BigDecimal R44_OVER6M_TO_1Y;
//	private BigDecimal R44_OVER1Y_TO_3Y;
//	private BigDecimal R44_OVER3Y_TO_5Y;
//	private BigDecimal R44_OVER5Y;
//	private BigDecimal R44_TOTAL;
//	private String R45_PRODUCT;
//	private BigDecimal R45_DAY1;
//	private BigDecimal R45_DAY2_7;
//	private BigDecimal R45_DAY8_14;
//	private BigDecimal R45_DAY15_30;
//	private BigDecimal R45_DAY31_TO_2M;
//	private BigDecimal R45_MORE2M_TO_3M;
//	private BigDecimal R45_OVER3M_TO_6M;
//	private BigDecimal R45_OVER6M_TO_1Y;
//	private BigDecimal R45_OVER1Y_TO_3Y;
//	private BigDecimal R45_OVER3Y_TO_5Y;
//	private BigDecimal R45_OVER5Y;
//	private BigDecimal R45_TOTAL;
//	private String R46_PRODUCT;
//	private BigDecimal R46_DAY1;
//	private BigDecimal R46_DAY2_7;
//	private BigDecimal R46_DAY8_14;
//	private BigDecimal R46_DAY15_30;
//	private BigDecimal R46_DAY31_TO_2M;
//	private BigDecimal R46_MORE2M_TO_3M;
//	private BigDecimal R46_OVER3M_TO_6M;
//	private BigDecimal R46_OVER6M_TO_1Y;
//	private BigDecimal R46_OVER1Y_TO_3Y;
//	private BigDecimal R46_OVER3Y_TO_5Y;
//	private BigDecimal R46_OVER5Y;
//	private BigDecimal R46_TOTAL;
	private String R47_PRODUCT;
	private BigDecimal R47_DAY1;
	private BigDecimal R47_DAY2_7;
	private BigDecimal R47_DAY8_14;
	private BigDecimal R47_DAY15_30;
	private BigDecimal R47_DAY31_TO_2M;
	private BigDecimal R47_MORE2M_TO_3M;
	private BigDecimal R47_OVER3M_TO_6M;
	private BigDecimal R47_OVER6M_TO_1Y;
	private BigDecimal R47_OVER1Y_TO_3Y;
	private BigDecimal R47_OVER3Y_TO_5Y;
	private BigDecimal R47_OVER5Y;
	private BigDecimal R47_TOTAL;
	private String R48_PRODUCT;
	private BigDecimal R48_DAY1;
	private BigDecimal R48_DAY2_7;
	private BigDecimal R48_DAY8_14;
	private BigDecimal R48_DAY15_30;
	private BigDecimal R48_DAY31_TO_2M;
	private BigDecimal R48_MORE2M_TO_3M;
	private BigDecimal R48_OVER3M_TO_6M;
	private BigDecimal R48_OVER6M_TO_1Y;
	private BigDecimal R48_OVER1Y_TO_3Y;
	private BigDecimal R48_OVER3Y_TO_5Y;
	private BigDecimal R48_OVER5Y;
	private BigDecimal R48_TOTAL;
	private String R49_PRODUCT;
	private BigDecimal R49_DAY1;
	private BigDecimal R49_DAY2_7;
	private BigDecimal R49_DAY8_14;
	private BigDecimal R49_DAY15_30;
	private BigDecimal R49_DAY31_TO_2M;
	private BigDecimal R49_MORE2M_TO_3M;
	private BigDecimal R49_OVER3M_TO_6M;
	private BigDecimal R49_OVER6M_TO_1Y;
	private BigDecimal R49_OVER1Y_TO_3Y;
	private BigDecimal R49_OVER3Y_TO_5Y;
	private BigDecimal R49_OVER5Y;
	private BigDecimal R49_TOTAL;
	private String R50_PRODUCT;
	private BigDecimal R50_DAY1;
	private BigDecimal R50_DAY2_7;
	private BigDecimal R50_DAY8_14;
	private BigDecimal R50_DAY15_30;
	private BigDecimal R50_DAY31_TO_2M;
	private BigDecimal R50_MORE2M_TO_3M;
	private BigDecimal R50_OVER3M_TO_6M;
	private BigDecimal R50_OVER6M_TO_1Y;
	private BigDecimal R50_OVER1Y_TO_3Y;
	private BigDecimal R50_OVER3Y_TO_5Y;
	private BigDecimal R50_OVER5Y;
	private BigDecimal R50_TOTAL;
	private String R51_PRODUCT;
	private BigDecimal R51_DAY1;
	private BigDecimal R51_DAY2_7;
	private BigDecimal R51_DAY8_14;
	private BigDecimal R51_DAY15_30;
	private BigDecimal R51_DAY31_TO_2M;
	private BigDecimal R51_MORE2M_TO_3M;
	private BigDecimal R51_OVER3M_TO_6M;
	private BigDecimal R51_OVER6M_TO_1Y;
	private BigDecimal R51_OVER1Y_TO_3Y;
	private BigDecimal R51_OVER3Y_TO_5Y;
	private BigDecimal R51_OVER5Y;
	private BigDecimal R51_TOTAL;
	private String R52_PRODUCT;
	private BigDecimal R52_DAY1;
	private BigDecimal R52_DAY2_7;
	private BigDecimal R52_DAY8_14;
	private BigDecimal R52_DAY15_30;
	private BigDecimal R52_DAY31_TO_2M;
	private BigDecimal R52_MORE2M_TO_3M;
	private BigDecimal R52_OVER3M_TO_6M;
	private BigDecimal R52_OVER6M_TO_1Y;
	private BigDecimal R52_OVER1Y_TO_3Y;
	private BigDecimal R52_OVER3Y_TO_5Y;
	private BigDecimal R52_OVER5Y;
	private BigDecimal R52_TOTAL;
	private String R53_PRODUCT;
	private BigDecimal R53_DAY1;
	private BigDecimal R53_DAY2_7;
	private BigDecimal R53_DAY8_14;
	private BigDecimal R53_DAY15_30;
	private BigDecimal R53_DAY31_TO_2M;
	private BigDecimal R53_MORE2M_TO_3M;
	private BigDecimal R53_OVER3M_TO_6M;
	private BigDecimal R53_OVER6M_TO_1Y;
	private BigDecimal R53_OVER1Y_TO_3Y;
	private BigDecimal R53_OVER3Y_TO_5Y;
	private BigDecimal R53_OVER5Y;
	private BigDecimal R53_TOTAL;
	private String R54_PRODUCT;
	private BigDecimal R54_DAY1;
	private BigDecimal R54_DAY2_7;
	private BigDecimal R54_DAY8_14;
	private BigDecimal R54_DAY15_30;
	private BigDecimal R54_DAY31_TO_2M;
	private BigDecimal R54_MORE2M_TO_3M;
	private BigDecimal R54_OVER3M_TO_6M;
	private BigDecimal R54_OVER6M_TO_1Y;
	private BigDecimal R54_OVER1Y_TO_3Y;
	private BigDecimal R54_OVER3Y_TO_5Y;
	private BigDecimal R54_OVER5Y;
	private BigDecimal R54_TOTAL;
	private String R55_PRODUCT;
	private BigDecimal R55_DAY1;
	private BigDecimal R55_DAY2_7;
	private BigDecimal R55_DAY8_14;
	private BigDecimal R55_DAY15_30;
	private BigDecimal R55_DAY31_TO_2M;
	private BigDecimal R55_MORE2M_TO_3M;
	private BigDecimal R55_OVER3M_TO_6M;
	private BigDecimal R55_OVER6M_TO_1Y;
	private BigDecimal R55_OVER1Y_TO_3Y;
	private BigDecimal R55_OVER3Y_TO_5Y;
	private BigDecimal R55_OVER5Y;
	private BigDecimal R55_TOTAL;
	private String R56_PRODUCT;
	private BigDecimal R56_DAY1;
	private BigDecimal R56_DAY2_7;
	private BigDecimal R56_DAY8_14;
	private BigDecimal R56_DAY15_30;
	private BigDecimal R56_DAY31_TO_2M;
	private BigDecimal R56_MORE2M_TO_3M;
	private BigDecimal R56_OVER3M_TO_6M;
	private BigDecimal R56_OVER6M_TO_1Y;
	private BigDecimal R56_OVER1Y_TO_3Y;
	private BigDecimal R56_OVER3Y_TO_5Y;
	private BigDecimal R56_OVER5Y;
	private BigDecimal R56_TOTAL;
	private String R57_PRODUCT;
	private BigDecimal R57_DAY1;
	private BigDecimal R57_DAY2_7;
	private BigDecimal R57_DAY8_14;
	private BigDecimal R57_DAY15_30;
	private BigDecimal R57_DAY31_TO_2M;
	private BigDecimal R57_MORE2M_TO_3M;
	private BigDecimal R57_OVER3M_TO_6M;
	private BigDecimal R57_OVER6M_TO_1Y;
	private BigDecimal R57_OVER1Y_TO_3Y;
	private BigDecimal R57_OVER3Y_TO_5Y;
	private BigDecimal R57_OVER5Y;
	private BigDecimal R57_TOTAL;
	private String R58_PRODUCT;
	private BigDecimal R58_DAY1;
	private BigDecimal R58_DAY2_7;
	private BigDecimal R58_DAY8_14;
	private BigDecimal R58_DAY15_30;
	private BigDecimal R58_DAY31_TO_2M;
	private BigDecimal R58_MORE2M_TO_3M;
	private BigDecimal R58_OVER3M_TO_6M;
	private BigDecimal R58_OVER6M_TO_1Y;
	private BigDecimal R58_OVER1Y_TO_3Y;
	private BigDecimal R58_OVER3Y_TO_5Y;
	private BigDecimal R58_OVER5Y;
	private BigDecimal R58_TOTAL;
	private String R59_PRODUCT;
	private BigDecimal R59_DAY1;
	private BigDecimal R59_DAY2_7;
	private BigDecimal R59_DAY8_14;
	private BigDecimal R59_DAY15_30;
	private BigDecimal R59_DAY31_TO_2M;
	private BigDecimal R59_MORE2M_TO_3M;
	private BigDecimal R59_OVER3M_TO_6M;
	private BigDecimal R59_OVER6M_TO_1Y;
	private BigDecimal R59_OVER1Y_TO_3Y;
	private BigDecimal R59_OVER3Y_TO_5Y;
	private BigDecimal R59_OVER5Y;
	private BigDecimal R59_TOTAL;
	private String R60_PRODUCT;
	private BigDecimal R60_DAY1;
	private BigDecimal R60_DAY2_7;
	private BigDecimal R60_DAY8_14;
	private BigDecimal R60_DAY15_30;
	private BigDecimal R60_DAY31_TO_2M;
	private BigDecimal R60_MORE2M_TO_3M;
	private BigDecimal R60_OVER3M_TO_6M;
	private BigDecimal R60_OVER6M_TO_1Y;
	private BigDecimal R60_OVER1Y_TO_3Y;
	private BigDecimal R60_OVER3Y_TO_5Y;
	private BigDecimal R60_OVER5Y;
	private BigDecimal R60_TOTAL;
	private String R61_PRODUCT;
	private BigDecimal R61_DAY1;
	private BigDecimal R61_DAY2_7;
	private BigDecimal R61_DAY8_14;
	private BigDecimal R61_DAY15_30;
	private BigDecimal R61_DAY31_TO_2M;
	private BigDecimal R61_MORE2M_TO_3M;
	private BigDecimal R61_OVER3M_TO_6M;
	private BigDecimal R61_OVER6M_TO_1Y;
	private BigDecimal R61_OVER1Y_TO_3Y;
	private BigDecimal R61_OVER3Y_TO_5Y;
	private BigDecimal R61_OVER5Y;
	private BigDecimal R61_TOTAL;
	private String R62_PRODUCT;
	private BigDecimal R62_DAY1;
	private BigDecimal R62_DAY2_7;
	private BigDecimal R62_DAY8_14;
	private BigDecimal R62_DAY15_30;
	private BigDecimal R62_DAY31_TO_2M;
	private BigDecimal R62_MORE2M_TO_3M;
	private BigDecimal R62_OVER3M_TO_6M;
	private BigDecimal R62_OVER6M_TO_1Y;
	private BigDecimal R62_OVER1Y_TO_3Y;
	private BigDecimal R62_OVER3Y_TO_5Y;
	private BigDecimal R62_OVER5Y;
	private BigDecimal R62_TOTAL;
	private String R63_PRODUCT;
	private BigDecimal R63_DAY1;
	private BigDecimal R63_DAY2_7;
	private BigDecimal R63_DAY8_14;
	private BigDecimal R63_DAY15_30;
	private BigDecimal R63_DAY31_TO_2M;
	private BigDecimal R63_MORE2M_TO_3M;
	private BigDecimal R63_OVER3M_TO_6M;
	private BigDecimal R63_OVER6M_TO_1Y;
	private BigDecimal R63_OVER1Y_TO_3Y;
	private BigDecimal R63_OVER3Y_TO_5Y;
	private BigDecimal R63_OVER5Y;
	private BigDecimal R63_TOTAL;
	private String R64_PRODUCT;
	private BigDecimal R64_DAY1;
	private BigDecimal R64_DAY2_7;
	private BigDecimal R64_DAY8_14;
	private BigDecimal R64_DAY15_30;
	private BigDecimal R64_DAY31_TO_2M;
	private BigDecimal R64_MORE2M_TO_3M;
	private BigDecimal R64_OVER3M_TO_6M;
	private BigDecimal R64_OVER6M_TO_1Y;
	private BigDecimal R64_OVER1Y_TO_3Y;
	private BigDecimal R64_OVER3Y_TO_5Y;
	private BigDecimal R64_OVER5Y;
	private BigDecimal R64_TOTAL;
	private String R65_PRODUCT;
	private BigDecimal R65_DAY1;
	private BigDecimal R65_DAY2_7;
	private BigDecimal R65_DAY8_14;
	private BigDecimal R65_DAY15_30;
	private BigDecimal R65_DAY31_TO_2M;
	private BigDecimal R65_MORE2M_TO_3M;
	private BigDecimal R65_OVER3M_TO_6M;
	private BigDecimal R65_OVER6M_TO_1Y;
	private BigDecimal R65_OVER1Y_TO_3Y;
	private BigDecimal R65_OVER3Y_TO_5Y;
	private BigDecimal R65_OVER5Y;
	private BigDecimal R65_TOTAL;
	private String R66_PRODUCT;
	private BigDecimal R66_DAY1;
	private BigDecimal R66_DAY2_7;
	private BigDecimal R66_DAY8_14;
	private BigDecimal R66_DAY15_30;
	private BigDecimal R66_DAY31_TO_2M;
	private BigDecimal R66_MORE2M_TO_3M;
	private BigDecimal R66_OVER3M_TO_6M;
	private BigDecimal R66_OVER6M_TO_1Y;
	private BigDecimal R66_OVER1Y_TO_3Y;
	private BigDecimal R66_OVER3Y_TO_5Y;
	private BigDecimal R66_OVER5Y;
	private BigDecimal R66_TOTAL;
	private String R67_PRODUCT;
	private BigDecimal R67_DAY1;
	private BigDecimal R67_DAY2_7;
	private BigDecimal R67_DAY8_14;
	private BigDecimal R67_DAY15_30;
	private BigDecimal R67_DAY31_TO_2M;
	private BigDecimal R67_MORE2M_TO_3M;
	private BigDecimal R67_OVER3M_TO_6M;
	private BigDecimal R67_OVER6M_TO_1Y;
	private BigDecimal R67_OVER1Y_TO_3Y;
	private BigDecimal R67_OVER3Y_TO_5Y;
	private BigDecimal R67_OVER5Y;
	private BigDecimal R67_TOTAL;
	private String R68_PRODUCT;
	private BigDecimal R68_DAY1;
	private BigDecimal R68_DAY2_7;
	private BigDecimal R68_DAY8_14;
	private BigDecimal R68_DAY15_30;
	private BigDecimal R68_DAY31_TO_2M;
	private BigDecimal R68_MORE2M_TO_3M;
	private BigDecimal R68_OVER3M_TO_6M;
	private BigDecimal R68_OVER6M_TO_1Y;
	private BigDecimal R68_OVER1Y_TO_3Y;
	private BigDecimal R68_OVER3Y_TO_5Y;
	private BigDecimal R68_OVER5Y;
	private BigDecimal R68_TOTAL;
	private String R69_PRODUCT;
	private BigDecimal R69_DAY1;
	private BigDecimal R69_DAY2_7;
	private BigDecimal R69_DAY8_14;
	private BigDecimal R69_DAY15_30;
	private BigDecimal R69_DAY31_TO_2M;
	private BigDecimal R69_MORE2M_TO_3M;
	private BigDecimal R69_OVER3M_TO_6M;
	private BigDecimal R69_OVER6M_TO_1Y;
	private BigDecimal R69_OVER1Y_TO_3Y;
	private BigDecimal R69_OVER3Y_TO_5Y;
	private BigDecimal R69_OVER5Y;
	private BigDecimal R69_TOTAL;
	private String R70_PRODUCT;
	private BigDecimal R70_DAY1;
	private BigDecimal R70_DAY2_7;
	private BigDecimal R70_DAY8_14;
	private BigDecimal R70_DAY15_30;
	private BigDecimal R70_DAY31_TO_2M;
	private BigDecimal R70_MORE2M_TO_3M;
	private BigDecimal R70_OVER3M_TO_6M;
	private BigDecimal R70_OVER6M_TO_1Y;
	private BigDecimal R70_OVER1Y_TO_3Y;
	private BigDecimal R70_OVER3Y_TO_5Y;
	private BigDecimal R70_OVER5Y;
	private BigDecimal R70_TOTAL;
	private String R71_PRODUCT;
	private BigDecimal R71_DAY1;
	private BigDecimal R71_DAY2_7;
	private BigDecimal R71_DAY8_14;
	private BigDecimal R71_DAY15_30;
	private BigDecimal R71_DAY31_TO_2M;
	private BigDecimal R71_MORE2M_TO_3M;
	private BigDecimal R71_OVER3M_TO_6M;
	private BigDecimal R71_OVER6M_TO_1Y;
	private BigDecimal R71_OVER1Y_TO_3Y;
	private BigDecimal R71_OVER3Y_TO_5Y;
	private BigDecimal R71_OVER5Y;
	private BigDecimal R71_TOTAL;
	private String R72_PRODUCT;
	private BigDecimal R72_DAY1;
	private BigDecimal R72_DAY2_7;
	private BigDecimal R72_DAY8_14;
	private BigDecimal R72_DAY15_30;
	private BigDecimal R72_DAY31_TO_2M;
	private BigDecimal R72_MORE2M_TO_3M;
	private BigDecimal R72_OVER3M_TO_6M;
	private BigDecimal R72_OVER6M_TO_1Y;
	private BigDecimal R72_OVER1Y_TO_3Y;
	private BigDecimal R72_OVER3Y_TO_5Y;
	private BigDecimal R72_OVER5Y;
	private BigDecimal R72_TOTAL;
	private String R73_PRODUCT;
	private BigDecimal R73_DAY1;
	private BigDecimal R73_DAY2_7;
	private BigDecimal R73_DAY8_14;
	private BigDecimal R73_DAY15_30;
	private BigDecimal R73_DAY31_TO_2M;
	private BigDecimal R73_MORE2M_TO_3M;
	private BigDecimal R73_OVER3M_TO_6M;
	private BigDecimal R73_OVER6M_TO_1Y;
	private BigDecimal R73_OVER1Y_TO_3Y;
	private BigDecimal R73_OVER3Y_TO_5Y;
	private BigDecimal R73_OVER5Y;
	private BigDecimal R73_TOTAL;
	private String R74_PRODUCT;
	private BigDecimal R74_DAY1;
	private BigDecimal R74_DAY2_7;
	private BigDecimal R74_DAY8_14;
	private BigDecimal R74_DAY15_30;
	private BigDecimal R74_DAY31_TO_2M;
	private BigDecimal R74_MORE2M_TO_3M;
	private BigDecimal R74_OVER3M_TO_6M;
	private BigDecimal R74_OVER6M_TO_1Y;
	private BigDecimal R74_OVER1Y_TO_3Y;
	private BigDecimal R74_OVER3Y_TO_5Y;
	private BigDecimal R74_OVER5Y;
	private BigDecimal R74_TOTAL;
	private String R75_PRODUCT;
	private BigDecimal R75_DAY1;
	private BigDecimal R75_DAY2_7;
	private BigDecimal R75_DAY8_14;
	private BigDecimal R75_DAY15_30;
	private BigDecimal R75_DAY31_TO_2M;
	private BigDecimal R75_MORE2M_TO_3M;
	private BigDecimal R75_OVER3M_TO_6M;
	private BigDecimal R75_OVER6M_TO_1Y;
	private BigDecimal R75_OVER1Y_TO_3Y;
	private BigDecimal R75_OVER3Y_TO_5Y;
	private BigDecimal R75_OVER5Y;
	private BigDecimal R75_TOTAL;
	private String R76_PRODUCT;
	private BigDecimal R76_DAY1;
	private BigDecimal R76_DAY2_7;
	private BigDecimal R76_DAY8_14;
	private BigDecimal R76_DAY15_30;
	private BigDecimal R76_DAY31_TO_2M;
	private BigDecimal R76_MORE2M_TO_3M;
	private BigDecimal R76_OVER3M_TO_6M;
	private BigDecimal R76_OVER6M_TO_1Y;
	private BigDecimal R76_OVER1Y_TO_3Y;
	private BigDecimal R76_OVER3Y_TO_5Y;
	private BigDecimal R76_OVER5Y;
	private BigDecimal R76_TOTAL;
//	private String R77_PRODUCT;
//	private BigDecimal R77_DAY1;
//	private BigDecimal R77_DAY2_7;
//	private BigDecimal R77_DAY8_14;
//	private BigDecimal R77_DAY15_30;
//	private BigDecimal R77_DAY31_TO_2M;
//	private BigDecimal R77_MORE2M_TO_3M;
//	private BigDecimal R77_OVER3M_TO_6M;
//	private BigDecimal R77_OVER6M_TO_1Y;
//	private BigDecimal R77_OVER1Y_TO_3Y;
//	private BigDecimal R77_OVER3Y_TO_5Y;
//	private BigDecimal R77_OVER5Y;
//	private BigDecimal R77_TOTAL;
//	private String R78_PRODUCT;
//	private BigDecimal R78_DAY1;
//	private BigDecimal R78_DAY2_7;
//	private BigDecimal R78_DAY8_14;
//	private BigDecimal R78_DAY15_30;
//	private BigDecimal R78_DAY31_TO_2M;
//	private BigDecimal R78_MORE2M_TO_3M;
//	private BigDecimal R78_OVER3M_TO_6M;
//	private BigDecimal R78_OVER6M_TO_1Y;
//	private BigDecimal R78_OVER1Y_TO_3Y;
//	private BigDecimal R78_OVER3Y_TO_5Y;
//	private BigDecimal R78_OVER5Y;
//	private BigDecimal R78_TOTAL;
//	private String R79_PRODUCT;
//	private BigDecimal R79_DAY1;
//	private BigDecimal R79_DAY2_7;
//	private BigDecimal R79_DAY8_14;
//	private BigDecimal R79_DAY15_30;
//	private BigDecimal R79_DAY31_TO_2M;
//	private BigDecimal R79_MORE2M_TO_3M;
//	private BigDecimal R79_OVER3M_TO_6M;
//	private BigDecimal R79_OVER6M_TO_1Y;
//	private BigDecimal R79_OVER1Y_TO_3Y;
//	private BigDecimal R79_OVER3Y_TO_5Y;
//	private BigDecimal R79_OVER5Y;
//	private BigDecimal R79_TOTAL;
//	private String R80_PRODUCT;
//	private BigDecimal R80_DAY1;
//	private BigDecimal R80_DAY2_7;
//	private BigDecimal R80_DAY8_14;
//	private BigDecimal R80_DAY15_30;
//	private BigDecimal R80_DAY31_TO_2M;
//	private BigDecimal R80_MORE2M_TO_3M;
//	private BigDecimal R80_OVER3M_TO_6M;
//	private BigDecimal R80_OVER6M_TO_1Y;
//	private BigDecimal R80_OVER1Y_TO_3Y;
//	private BigDecimal R80_OVER3Y_TO_5Y;
//	private BigDecimal R80_OVER5Y;
//	private BigDecimal R80_TOTAL;
//	private String R81_PRODUCT;
//	private BigDecimal R81_DAY1;
//	private BigDecimal R81_DAY2_7;
//	private BigDecimal R81_DAY8_14;
//	private BigDecimal R81_DAY15_30;
//	private BigDecimal R81_DAY31_TO_2M;
//	private BigDecimal R81_MORE2M_TO_3M;
//	private BigDecimal R81_OVER3M_TO_6M;
//	private BigDecimal R81_OVER6M_TO_1Y;
//	private BigDecimal R81_OVER1Y_TO_3Y;
//	private BigDecimal R81_OVER3Y_TO_5Y;
//	private BigDecimal R81_OVER5Y;
//	private BigDecimal R81_TOTAL;
//	private String R82_PRODUCT;
//	private BigDecimal R82_DAY1;
//	private BigDecimal R82_DAY2_7;
//	private BigDecimal R82_DAY8_14;
//	private BigDecimal R82_DAY15_30;
//	private BigDecimal R82_DAY31_TO_2M;
//	private BigDecimal R82_MORE2M_TO_3M;
//	private BigDecimal R82_OVER3M_TO_6M;
//	private BigDecimal R82_OVER6M_TO_1Y;
//	private BigDecimal R82_OVER1Y_TO_3Y;
//	private BigDecimal R82_OVER3Y_TO_5Y;
//	private BigDecimal R82_OVER5Y;
//	private BigDecimal R82_TOTAL;
//	private String R83_PRODUCT;
//	private BigDecimal R83_DAY1;
//	private BigDecimal R83_DAY2_7;
//	private BigDecimal R83_DAY8_14;
//	private BigDecimal R83_DAY15_30;
//	private BigDecimal R83_DAY31_TO_2M;
//	private BigDecimal R83_MORE2M_TO_3M;
//	private BigDecimal R83_OVER3M_TO_6M;
//	private BigDecimal R83_OVER6M_TO_1Y;
//	private BigDecimal R83_OVER1Y_TO_3Y;
//	private BigDecimal R83_OVER3Y_TO_5Y;
//	private BigDecimal R83_OVER5Y;
//	private BigDecimal R83_TOTAL;
	private String R84_PRODUCT;
	private BigDecimal R84_DAY1;
	private BigDecimal R84_DAY2_7;
	private BigDecimal R84_DAY8_14;
	private BigDecimal R84_DAY15_30;
	private BigDecimal R84_DAY31_TO_2M;
	private BigDecimal R84_MORE2M_TO_3M;
	private BigDecimal R84_OVER3M_TO_6M;
	private BigDecimal R84_OVER6M_TO_1Y;
	private BigDecimal R84_OVER1Y_TO_3Y;
	private BigDecimal R84_OVER3Y_TO_5Y;
	private BigDecimal R84_OVER5Y;
	private BigDecimal R84_TOTAL;
	private String R85_PRODUCT;
	private BigDecimal R85_DAY1;
	private BigDecimal R85_DAY2_7;
	private BigDecimal R85_DAY8_14;
	private BigDecimal R85_DAY15_30;
	private BigDecimal R85_DAY31_TO_2M;
	private BigDecimal R85_MORE2M_TO_3M;
	private BigDecimal R85_OVER3M_TO_6M;
	private BigDecimal R85_OVER6M_TO_1Y;
	private BigDecimal R85_OVER1Y_TO_3Y;
	private BigDecimal R85_OVER3Y_TO_5Y;
	private BigDecimal R85_OVER5Y;
	private BigDecimal R85_TOTAL;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date REPORT_DATE;
	private String REPORT_VERSION;
	private String REPORT_CURRENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;
	public String getSRL_NO() {
		return SRL_NO;
	}
	public void setSRL_NO(String sRL_NO) {
		SRL_NO = sRL_NO;
	}
	public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}
	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}
	public BigDecimal getR11_DAY1() {
		return R11_DAY1;
	}
	public void setR11_DAY1(BigDecimal r11_DAY1) {
		R11_DAY1 = r11_DAY1;
	}
	public BigDecimal getR11_DAY2_7() {
		return R11_DAY2_7;
	}
	public void setR11_DAY2_7(BigDecimal r11_DAY2_7) {
		R11_DAY2_7 = r11_DAY2_7;
	}
	public BigDecimal getR11_DAY8_14() {
		return R11_DAY8_14;
	}
	public void setR11_DAY8_14(BigDecimal r11_DAY8_14) {
		R11_DAY8_14 = r11_DAY8_14;
	}
	public BigDecimal getR11_DAY15_30() {
		return R11_DAY15_30;
	}
	public void setR11_DAY15_30(BigDecimal r11_DAY15_30) {
		R11_DAY15_30 = r11_DAY15_30;
	}
	public BigDecimal getR11_DAY31_TO_2M() {
		return R11_DAY31_TO_2M;
	}
	public void setR11_DAY31_TO_2M(BigDecimal r11_DAY31_TO_2M) {
		R11_DAY31_TO_2M = r11_DAY31_TO_2M;
	}
	public BigDecimal getR11_MORE2M_TO_3M() {
		return R11_MORE2M_TO_3M;
	}
	public void setR11_MORE2M_TO_3M(BigDecimal r11_MORE2M_TO_3M) {
		R11_MORE2M_TO_3M = r11_MORE2M_TO_3M;
	}
	public BigDecimal getR11_OVER3M_TO_6M() {
		return R11_OVER3M_TO_6M;
	}
	public void setR11_OVER3M_TO_6M(BigDecimal r11_OVER3M_TO_6M) {
		R11_OVER3M_TO_6M = r11_OVER3M_TO_6M;
	}
	public BigDecimal getR11_OVER6M_TO_1Y() {
		return R11_OVER6M_TO_1Y;
	}
	public void setR11_OVER6M_TO_1Y(BigDecimal r11_OVER6M_TO_1Y) {
		R11_OVER6M_TO_1Y = r11_OVER6M_TO_1Y;
	}
	public BigDecimal getR11_OVER1Y_TO_3Y() {
		return R11_OVER1Y_TO_3Y;
	}
	public void setR11_OVER1Y_TO_3Y(BigDecimal r11_OVER1Y_TO_3Y) {
		R11_OVER1Y_TO_3Y = r11_OVER1Y_TO_3Y;
	}
	public BigDecimal getR11_OVER3Y_TO_5Y() {
		return R11_OVER3Y_TO_5Y;
	}
	public void setR11_OVER3Y_TO_5Y(BigDecimal r11_OVER3Y_TO_5Y) {
		R11_OVER3Y_TO_5Y = r11_OVER3Y_TO_5Y;
	}
	public BigDecimal getR11_OVER5Y() {
		return R11_OVER5Y;
	}
	public void setR11_OVER5Y(BigDecimal r11_OVER5Y) {
		R11_OVER5Y = r11_OVER5Y;
	}
	public BigDecimal getR11_TOTAL() {
		return R11_TOTAL;
	}
	public void setR11_TOTAL(BigDecimal r11_TOTAL) {
		R11_TOTAL = r11_TOTAL;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_DAY1() {
		return R12_DAY1;
	}
	public void setR12_DAY1(BigDecimal r12_DAY1) {
		R12_DAY1 = r12_DAY1;
	}
	public BigDecimal getR12_DAY2_7() {
		return R12_DAY2_7;
	}
	public void setR12_DAY2_7(BigDecimal r12_DAY2_7) {
		R12_DAY2_7 = r12_DAY2_7;
	}
	public BigDecimal getR12_DAY8_14() {
		return R12_DAY8_14;
	}
	public void setR12_DAY8_14(BigDecimal r12_DAY8_14) {
		R12_DAY8_14 = r12_DAY8_14;
	}
	public BigDecimal getR12_DAY15_30() {
		return R12_DAY15_30;
	}
	public void setR12_DAY15_30(BigDecimal r12_DAY15_30) {
		R12_DAY15_30 = r12_DAY15_30;
	}
	public BigDecimal getR12_DAY31_TO_2M() {
		return R12_DAY31_TO_2M;
	}
	public void setR12_DAY31_TO_2M(BigDecimal r12_DAY31_TO_2M) {
		R12_DAY31_TO_2M = r12_DAY31_TO_2M;
	}
	public BigDecimal getR12_MORE2M_TO_3M() {
		return R12_MORE2M_TO_3M;
	}
	public void setR12_MORE2M_TO_3M(BigDecimal r12_MORE2M_TO_3M) {
		R12_MORE2M_TO_3M = r12_MORE2M_TO_3M;
	}
	public BigDecimal getR12_OVER3M_TO_6M() {
		return R12_OVER3M_TO_6M;
	}
	public void setR12_OVER3M_TO_6M(BigDecimal r12_OVER3M_TO_6M) {
		R12_OVER3M_TO_6M = r12_OVER3M_TO_6M;
	}
	public BigDecimal getR12_OVER6M_TO_1Y() {
		return R12_OVER6M_TO_1Y;
	}
	public void setR12_OVER6M_TO_1Y(BigDecimal r12_OVER6M_TO_1Y) {
		R12_OVER6M_TO_1Y = r12_OVER6M_TO_1Y;
	}
	public BigDecimal getR12_OVER1Y_TO_3Y() {
		return R12_OVER1Y_TO_3Y;
	}
	public void setR12_OVER1Y_TO_3Y(BigDecimal r12_OVER1Y_TO_3Y) {
		R12_OVER1Y_TO_3Y = r12_OVER1Y_TO_3Y;
	}
	public BigDecimal getR12_OVER3Y_TO_5Y() {
		return R12_OVER3Y_TO_5Y;
	}
	public void setR12_OVER3Y_TO_5Y(BigDecimal r12_OVER3Y_TO_5Y) {
		R12_OVER3Y_TO_5Y = r12_OVER3Y_TO_5Y;
	}
	public BigDecimal getR12_OVER5Y() {
		return R12_OVER5Y;
	}
	public void setR12_OVER5Y(BigDecimal r12_OVER5Y) {
		R12_OVER5Y = r12_OVER5Y;
	}
	public BigDecimal getR12_TOTAL() {
		return R12_TOTAL;
	}
	public void setR12_TOTAL(BigDecimal r12_TOTAL) {
		R12_TOTAL = r12_TOTAL;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_DAY1() {
		return R13_DAY1;
	}
	public void setR13_DAY1(BigDecimal r13_DAY1) {
		R13_DAY1 = r13_DAY1;
	}
	public BigDecimal getR13_DAY2_7() {
		return R13_DAY2_7;
	}
	public void setR13_DAY2_7(BigDecimal r13_DAY2_7) {
		R13_DAY2_7 = r13_DAY2_7;
	}
	public BigDecimal getR13_DAY8_14() {
		return R13_DAY8_14;
	}
	public void setR13_DAY8_14(BigDecimal r13_DAY8_14) {
		R13_DAY8_14 = r13_DAY8_14;
	}
	public BigDecimal getR13_DAY15_30() {
		return R13_DAY15_30;
	}
	public void setR13_DAY15_30(BigDecimal r13_DAY15_30) {
		R13_DAY15_30 = r13_DAY15_30;
	}
	public BigDecimal getR13_DAY31_TO_2M() {
		return R13_DAY31_TO_2M;
	}
	public void setR13_DAY31_TO_2M(BigDecimal r13_DAY31_TO_2M) {
		R13_DAY31_TO_2M = r13_DAY31_TO_2M;
	}
	public BigDecimal getR13_MORE2M_TO_3M() {
		return R13_MORE2M_TO_3M;
	}
	public void setR13_MORE2M_TO_3M(BigDecimal r13_MORE2M_TO_3M) {
		R13_MORE2M_TO_3M = r13_MORE2M_TO_3M;
	}
	public BigDecimal getR13_OVER3M_TO_6M() {
		return R13_OVER3M_TO_6M;
	}
	public void setR13_OVER3M_TO_6M(BigDecimal r13_OVER3M_TO_6M) {
		R13_OVER3M_TO_6M = r13_OVER3M_TO_6M;
	}
	public BigDecimal getR13_OVER6M_TO_1Y() {
		return R13_OVER6M_TO_1Y;
	}
	public void setR13_OVER6M_TO_1Y(BigDecimal r13_OVER6M_TO_1Y) {
		R13_OVER6M_TO_1Y = r13_OVER6M_TO_1Y;
	}
	public BigDecimal getR13_OVER1Y_TO_3Y() {
		return R13_OVER1Y_TO_3Y;
	}
	public void setR13_OVER1Y_TO_3Y(BigDecimal r13_OVER1Y_TO_3Y) {
		R13_OVER1Y_TO_3Y = r13_OVER1Y_TO_3Y;
	}
	public BigDecimal getR13_OVER3Y_TO_5Y() {
		return R13_OVER3Y_TO_5Y;
	}
	public void setR13_OVER3Y_TO_5Y(BigDecimal r13_OVER3Y_TO_5Y) {
		R13_OVER3Y_TO_5Y = r13_OVER3Y_TO_5Y;
	}
	public BigDecimal getR13_OVER5Y() {
		return R13_OVER5Y;
	}
	public void setR13_OVER5Y(BigDecimal r13_OVER5Y) {
		R13_OVER5Y = r13_OVER5Y;
	}
	public BigDecimal getR13_TOTAL() {
		return R13_TOTAL;
	}
	public void setR13_TOTAL(BigDecimal r13_TOTAL) {
		R13_TOTAL = r13_TOTAL;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_DAY1() {
		return R14_DAY1;
	}
	public void setR14_DAY1(BigDecimal r14_DAY1) {
		R14_DAY1 = r14_DAY1;
	}
	public BigDecimal getR14_DAY2_7() {
		return R14_DAY2_7;
	}
	public void setR14_DAY2_7(BigDecimal r14_DAY2_7) {
		R14_DAY2_7 = r14_DAY2_7;
	}
	public BigDecimal getR14_DAY8_14() {
		return R14_DAY8_14;
	}
	public void setR14_DAY8_14(BigDecimal r14_DAY8_14) {
		R14_DAY8_14 = r14_DAY8_14;
	}
	public BigDecimal getR14_DAY15_30() {
		return R14_DAY15_30;
	}
	public void setR14_DAY15_30(BigDecimal r14_DAY15_30) {
		R14_DAY15_30 = r14_DAY15_30;
	}
	public BigDecimal getR14_DAY31_TO_2M() {
		return R14_DAY31_TO_2M;
	}
	public void setR14_DAY31_TO_2M(BigDecimal r14_DAY31_TO_2M) {
		R14_DAY31_TO_2M = r14_DAY31_TO_2M;
	}
	public BigDecimal getR14_MORE2M_TO_3M() {
		return R14_MORE2M_TO_3M;
	}
	public void setR14_MORE2M_TO_3M(BigDecimal r14_MORE2M_TO_3M) {
		R14_MORE2M_TO_3M = r14_MORE2M_TO_3M;
	}
	public BigDecimal getR14_OVER3M_TO_6M() {
		return R14_OVER3M_TO_6M;
	}
	public void setR14_OVER3M_TO_6M(BigDecimal r14_OVER3M_TO_6M) {
		R14_OVER3M_TO_6M = r14_OVER3M_TO_6M;
	}
	public BigDecimal getR14_OVER6M_TO_1Y() {
		return R14_OVER6M_TO_1Y;
	}
	public void setR14_OVER6M_TO_1Y(BigDecimal r14_OVER6M_TO_1Y) {
		R14_OVER6M_TO_1Y = r14_OVER6M_TO_1Y;
	}
	public BigDecimal getR14_OVER1Y_TO_3Y() {
		return R14_OVER1Y_TO_3Y;
	}
	public void setR14_OVER1Y_TO_3Y(BigDecimal r14_OVER1Y_TO_3Y) {
		R14_OVER1Y_TO_3Y = r14_OVER1Y_TO_3Y;
	}
	public BigDecimal getR14_OVER3Y_TO_5Y() {
		return R14_OVER3Y_TO_5Y;
	}
	public void setR14_OVER3Y_TO_5Y(BigDecimal r14_OVER3Y_TO_5Y) {
		R14_OVER3Y_TO_5Y = r14_OVER3Y_TO_5Y;
	}
	public BigDecimal getR14_OVER5Y() {
		return R14_OVER5Y;
	}
	public void setR14_OVER5Y(BigDecimal r14_OVER5Y) {
		R14_OVER5Y = r14_OVER5Y;
	}
	public BigDecimal getR14_TOTAL() {
		return R14_TOTAL;
	}
	public void setR14_TOTAL(BigDecimal r14_TOTAL) {
		R14_TOTAL = r14_TOTAL;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_DAY1() {
		return R15_DAY1;
	}
	public void setR15_DAY1(BigDecimal r15_DAY1) {
		R15_DAY1 = r15_DAY1;
	}
	public BigDecimal getR15_DAY2_7() {
		return R15_DAY2_7;
	}
	public void setR15_DAY2_7(BigDecimal r15_DAY2_7) {
		R15_DAY2_7 = r15_DAY2_7;
	}
	public BigDecimal getR15_DAY8_14() {
		return R15_DAY8_14;
	}
	public void setR15_DAY8_14(BigDecimal r15_DAY8_14) {
		R15_DAY8_14 = r15_DAY8_14;
	}
	public BigDecimal getR15_DAY15_30() {
		return R15_DAY15_30;
	}
	public void setR15_DAY15_30(BigDecimal r15_DAY15_30) {
		R15_DAY15_30 = r15_DAY15_30;
	}
	public BigDecimal getR15_DAY31_TO_2M() {
		return R15_DAY31_TO_2M;
	}
	public void setR15_DAY31_TO_2M(BigDecimal r15_DAY31_TO_2M) {
		R15_DAY31_TO_2M = r15_DAY31_TO_2M;
	}
	public BigDecimal getR15_MORE2M_TO_3M() {
		return R15_MORE2M_TO_3M;
	}
	public void setR15_MORE2M_TO_3M(BigDecimal r15_MORE2M_TO_3M) {
		R15_MORE2M_TO_3M = r15_MORE2M_TO_3M;
	}
	public BigDecimal getR15_OVER3M_TO_6M() {
		return R15_OVER3M_TO_6M;
	}
	public void setR15_OVER3M_TO_6M(BigDecimal r15_OVER3M_TO_6M) {
		R15_OVER3M_TO_6M = r15_OVER3M_TO_6M;
	}
	public BigDecimal getR15_OVER6M_TO_1Y() {
		return R15_OVER6M_TO_1Y;
	}
	public void setR15_OVER6M_TO_1Y(BigDecimal r15_OVER6M_TO_1Y) {
		R15_OVER6M_TO_1Y = r15_OVER6M_TO_1Y;
	}
	public BigDecimal getR15_OVER1Y_TO_3Y() {
		return R15_OVER1Y_TO_3Y;
	}
	public void setR15_OVER1Y_TO_3Y(BigDecimal r15_OVER1Y_TO_3Y) {
		R15_OVER1Y_TO_3Y = r15_OVER1Y_TO_3Y;
	}
	public BigDecimal getR15_OVER3Y_TO_5Y() {
		return R15_OVER3Y_TO_5Y;
	}
	public void setR15_OVER3Y_TO_5Y(BigDecimal r15_OVER3Y_TO_5Y) {
		R15_OVER3Y_TO_5Y = r15_OVER3Y_TO_5Y;
	}
	public BigDecimal getR15_OVER5Y() {
		return R15_OVER5Y;
	}
	public void setR15_OVER5Y(BigDecimal r15_OVER5Y) {
		R15_OVER5Y = r15_OVER5Y;
	}
	public BigDecimal getR15_TOTAL() {
		return R15_TOTAL;
	}
	public void setR15_TOTAL(BigDecimal r15_TOTAL) {
		R15_TOTAL = r15_TOTAL;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_DAY1() {
		return R16_DAY1;
	}
	public void setR16_DAY1(BigDecimal r16_DAY1) {
		R16_DAY1 = r16_DAY1;
	}
	public BigDecimal getR16_DAY2_7() {
		return R16_DAY2_7;
	}
	public void setR16_DAY2_7(BigDecimal r16_DAY2_7) {
		R16_DAY2_7 = r16_DAY2_7;
	}
	public BigDecimal getR16_DAY8_14() {
		return R16_DAY8_14;
	}
	public void setR16_DAY8_14(BigDecimal r16_DAY8_14) {
		R16_DAY8_14 = r16_DAY8_14;
	}
	public BigDecimal getR16_DAY15_30() {
		return R16_DAY15_30;
	}
	public void setR16_DAY15_30(BigDecimal r16_DAY15_30) {
		R16_DAY15_30 = r16_DAY15_30;
	}
	public BigDecimal getR16_DAY31_TO_2M() {
		return R16_DAY31_TO_2M;
	}
	public void setR16_DAY31_TO_2M(BigDecimal r16_DAY31_TO_2M) {
		R16_DAY31_TO_2M = r16_DAY31_TO_2M;
	}
	public BigDecimal getR16_MORE2M_TO_3M() {
		return R16_MORE2M_TO_3M;
	}
	public void setR16_MORE2M_TO_3M(BigDecimal r16_MORE2M_TO_3M) {
		R16_MORE2M_TO_3M = r16_MORE2M_TO_3M;
	}
	public BigDecimal getR16_OVER3M_TO_6M() {
		return R16_OVER3M_TO_6M;
	}
	public void setR16_OVER3M_TO_6M(BigDecimal r16_OVER3M_TO_6M) {
		R16_OVER3M_TO_6M = r16_OVER3M_TO_6M;
	}
	public BigDecimal getR16_OVER6M_TO_1Y() {
		return R16_OVER6M_TO_1Y;
	}
	public void setR16_OVER6M_TO_1Y(BigDecimal r16_OVER6M_TO_1Y) {
		R16_OVER6M_TO_1Y = r16_OVER6M_TO_1Y;
	}
	public BigDecimal getR16_OVER1Y_TO_3Y() {
		return R16_OVER1Y_TO_3Y;
	}
	public void setR16_OVER1Y_TO_3Y(BigDecimal r16_OVER1Y_TO_3Y) {
		R16_OVER1Y_TO_3Y = r16_OVER1Y_TO_3Y;
	}
	public BigDecimal getR16_OVER3Y_TO_5Y() {
		return R16_OVER3Y_TO_5Y;
	}
	public void setR16_OVER3Y_TO_5Y(BigDecimal r16_OVER3Y_TO_5Y) {
		R16_OVER3Y_TO_5Y = r16_OVER3Y_TO_5Y;
	}
	public BigDecimal getR16_OVER5Y() {
		return R16_OVER5Y;
	}
	public void setR16_OVER5Y(BigDecimal r16_OVER5Y) {
		R16_OVER5Y = r16_OVER5Y;
	}
	public BigDecimal getR16_TOTAL() {
		return R16_TOTAL;
	}
	public void setR16_TOTAL(BigDecimal r16_TOTAL) {
		R16_TOTAL = r16_TOTAL;
	}
	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}
	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}
	public BigDecimal getR17_DAY1() {
		return R17_DAY1;
	}
	public void setR17_DAY1(BigDecimal r17_DAY1) {
		R17_DAY1 = r17_DAY1;
	}
	public BigDecimal getR17_DAY2_7() {
		return R17_DAY2_7;
	}
	public void setR17_DAY2_7(BigDecimal r17_DAY2_7) {
		R17_DAY2_7 = r17_DAY2_7;
	}
	public BigDecimal getR17_DAY8_14() {
		return R17_DAY8_14;
	}
	public void setR17_DAY8_14(BigDecimal r17_DAY8_14) {
		R17_DAY8_14 = r17_DAY8_14;
	}
	public BigDecimal getR17_DAY15_30() {
		return R17_DAY15_30;
	}
	public void setR17_DAY15_30(BigDecimal r17_DAY15_30) {
		R17_DAY15_30 = r17_DAY15_30;
	}
	public BigDecimal getR17_DAY31_TO_2M() {
		return R17_DAY31_TO_2M;
	}
	public void setR17_DAY31_TO_2M(BigDecimal r17_DAY31_TO_2M) {
		R17_DAY31_TO_2M = r17_DAY31_TO_2M;
	}
	public BigDecimal getR17_MORE2M_TO_3M() {
		return R17_MORE2M_TO_3M;
	}
	public void setR17_MORE2M_TO_3M(BigDecimal r17_MORE2M_TO_3M) {
		R17_MORE2M_TO_3M = r17_MORE2M_TO_3M;
	}
	public BigDecimal getR17_OVER3M_TO_6M() {
		return R17_OVER3M_TO_6M;
	}
	public void setR17_OVER3M_TO_6M(BigDecimal r17_OVER3M_TO_6M) {
		R17_OVER3M_TO_6M = r17_OVER3M_TO_6M;
	}
	public BigDecimal getR17_OVER6M_TO_1Y() {
		return R17_OVER6M_TO_1Y;
	}
	public void setR17_OVER6M_TO_1Y(BigDecimal r17_OVER6M_TO_1Y) {
		R17_OVER6M_TO_1Y = r17_OVER6M_TO_1Y;
	}
	public BigDecimal getR17_OVER1Y_TO_3Y() {
		return R17_OVER1Y_TO_3Y;
	}
	public void setR17_OVER1Y_TO_3Y(BigDecimal r17_OVER1Y_TO_3Y) {
		R17_OVER1Y_TO_3Y = r17_OVER1Y_TO_3Y;
	}
	public BigDecimal getR17_OVER3Y_TO_5Y() {
		return R17_OVER3Y_TO_5Y;
	}
	public void setR17_OVER3Y_TO_5Y(BigDecimal r17_OVER3Y_TO_5Y) {
		R17_OVER3Y_TO_5Y = r17_OVER3Y_TO_5Y;
	}
	public BigDecimal getR17_OVER5Y() {
		return R17_OVER5Y;
	}
	public void setR17_OVER5Y(BigDecimal r17_OVER5Y) {
		R17_OVER5Y = r17_OVER5Y;
	}
	public BigDecimal getR17_TOTAL() {
		return R17_TOTAL;
	}
	public void setR17_TOTAL(BigDecimal r17_TOTAL) {
		R17_TOTAL = r17_TOTAL;
	}
	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}
	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}
	public BigDecimal getR18_DAY1() {
		return R18_DAY1;
	}
	public void setR18_DAY1(BigDecimal r18_DAY1) {
		R18_DAY1 = r18_DAY1;
	}
	public BigDecimal getR18_DAY2_7() {
		return R18_DAY2_7;
	}
	public void setR18_DAY2_7(BigDecimal r18_DAY2_7) {
		R18_DAY2_7 = r18_DAY2_7;
	}
	public BigDecimal getR18_DAY8_14() {
		return R18_DAY8_14;
	}
	public void setR18_DAY8_14(BigDecimal r18_DAY8_14) {
		R18_DAY8_14 = r18_DAY8_14;
	}
	public BigDecimal getR18_DAY15_30() {
		return R18_DAY15_30;
	}
	public void setR18_DAY15_30(BigDecimal r18_DAY15_30) {
		R18_DAY15_30 = r18_DAY15_30;
	}
	public BigDecimal getR18_DAY31_TO_2M() {
		return R18_DAY31_TO_2M;
	}
	public void setR18_DAY31_TO_2M(BigDecimal r18_DAY31_TO_2M) {
		R18_DAY31_TO_2M = r18_DAY31_TO_2M;
	}
	public BigDecimal getR18_MORE2M_TO_3M() {
		return R18_MORE2M_TO_3M;
	}
	public void setR18_MORE2M_TO_3M(BigDecimal r18_MORE2M_TO_3M) {
		R18_MORE2M_TO_3M = r18_MORE2M_TO_3M;
	}
	public BigDecimal getR18_OVER3M_TO_6M() {
		return R18_OVER3M_TO_6M;
	}
	public void setR18_OVER3M_TO_6M(BigDecimal r18_OVER3M_TO_6M) {
		R18_OVER3M_TO_6M = r18_OVER3M_TO_6M;
	}
	public BigDecimal getR18_OVER6M_TO_1Y() {
		return R18_OVER6M_TO_1Y;
	}
	public void setR18_OVER6M_TO_1Y(BigDecimal r18_OVER6M_TO_1Y) {
		R18_OVER6M_TO_1Y = r18_OVER6M_TO_1Y;
	}
	public BigDecimal getR18_OVER1Y_TO_3Y() {
		return R18_OVER1Y_TO_3Y;
	}
	public void setR18_OVER1Y_TO_3Y(BigDecimal r18_OVER1Y_TO_3Y) {
		R18_OVER1Y_TO_3Y = r18_OVER1Y_TO_3Y;
	}
	public BigDecimal getR18_OVER3Y_TO_5Y() {
		return R18_OVER3Y_TO_5Y;
	}
	public void setR18_OVER3Y_TO_5Y(BigDecimal r18_OVER3Y_TO_5Y) {
		R18_OVER3Y_TO_5Y = r18_OVER3Y_TO_5Y;
	}
	public BigDecimal getR18_OVER5Y() {
		return R18_OVER5Y;
	}
	public void setR18_OVER5Y(BigDecimal r18_OVER5Y) {
		R18_OVER5Y = r18_OVER5Y;
	}
	public BigDecimal getR18_TOTAL() {
		return R18_TOTAL;
	}
	public void setR18_TOTAL(BigDecimal r18_TOTAL) {
		R18_TOTAL = r18_TOTAL;
	}
	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}
	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}
	public BigDecimal getR19_DAY1() {
		return R19_DAY1;
	}
	public void setR19_DAY1(BigDecimal r19_DAY1) {
		R19_DAY1 = r19_DAY1;
	}
	public BigDecimal getR19_DAY2_7() {
		return R19_DAY2_7;
	}
	public void setR19_DAY2_7(BigDecimal r19_DAY2_7) {
		R19_DAY2_7 = r19_DAY2_7;
	}
	public BigDecimal getR19_DAY8_14() {
		return R19_DAY8_14;
	}
	public void setR19_DAY8_14(BigDecimal r19_DAY8_14) {
		R19_DAY8_14 = r19_DAY8_14;
	}
	public BigDecimal getR19_DAY15_30() {
		return R19_DAY15_30;
	}
	public void setR19_DAY15_30(BigDecimal r19_DAY15_30) {
		R19_DAY15_30 = r19_DAY15_30;
	}
	public BigDecimal getR19_DAY31_TO_2M() {
		return R19_DAY31_TO_2M;
	}
	public void setR19_DAY31_TO_2M(BigDecimal r19_DAY31_TO_2M) {
		R19_DAY31_TO_2M = r19_DAY31_TO_2M;
	}
	public BigDecimal getR19_MORE2M_TO_3M() {
		return R19_MORE2M_TO_3M;
	}
	public void setR19_MORE2M_TO_3M(BigDecimal r19_MORE2M_TO_3M) {
		R19_MORE2M_TO_3M = r19_MORE2M_TO_3M;
	}
	public BigDecimal getR19_OVER3M_TO_6M() {
		return R19_OVER3M_TO_6M;
	}
	public void setR19_OVER3M_TO_6M(BigDecimal r19_OVER3M_TO_6M) {
		R19_OVER3M_TO_6M = r19_OVER3M_TO_6M;
	}
	public BigDecimal getR19_OVER6M_TO_1Y() {
		return R19_OVER6M_TO_1Y;
	}
	public void setR19_OVER6M_TO_1Y(BigDecimal r19_OVER6M_TO_1Y) {
		R19_OVER6M_TO_1Y = r19_OVER6M_TO_1Y;
	}
	public BigDecimal getR19_OVER1Y_TO_3Y() {
		return R19_OVER1Y_TO_3Y;
	}
	public void setR19_OVER1Y_TO_3Y(BigDecimal r19_OVER1Y_TO_3Y) {
		R19_OVER1Y_TO_3Y = r19_OVER1Y_TO_3Y;
	}
	public BigDecimal getR19_OVER3Y_TO_5Y() {
		return R19_OVER3Y_TO_5Y;
	}
	public void setR19_OVER3Y_TO_5Y(BigDecimal r19_OVER3Y_TO_5Y) {
		R19_OVER3Y_TO_5Y = r19_OVER3Y_TO_5Y;
	}
	public BigDecimal getR19_OVER5Y() {
		return R19_OVER5Y;
	}
	public void setR19_OVER5Y(BigDecimal r19_OVER5Y) {
		R19_OVER5Y = r19_OVER5Y;
	}
	public BigDecimal getR19_TOTAL() {
		return R19_TOTAL;
	}
	public void setR19_TOTAL(BigDecimal r19_TOTAL) {
		R19_TOTAL = r19_TOTAL;
	}
	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}
	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}
	public BigDecimal getR20_DAY1() {
		return R20_DAY1;
	}
	public void setR20_DAY1(BigDecimal r20_DAY1) {
		R20_DAY1 = r20_DAY1;
	}
	public BigDecimal getR20_DAY2_7() {
		return R20_DAY2_7;
	}
	public void setR20_DAY2_7(BigDecimal r20_DAY2_7) {
		R20_DAY2_7 = r20_DAY2_7;
	}
	public BigDecimal getR20_DAY8_14() {
		return R20_DAY8_14;
	}
	public void setR20_DAY8_14(BigDecimal r20_DAY8_14) {
		R20_DAY8_14 = r20_DAY8_14;
	}
	public BigDecimal getR20_DAY15_30() {
		return R20_DAY15_30;
	}
	public void setR20_DAY15_30(BigDecimal r20_DAY15_30) {
		R20_DAY15_30 = r20_DAY15_30;
	}
	public BigDecimal getR20_DAY31_TO_2M() {
		return R20_DAY31_TO_2M;
	}
	public void setR20_DAY31_TO_2M(BigDecimal r20_DAY31_TO_2M) {
		R20_DAY31_TO_2M = r20_DAY31_TO_2M;
	}
	public BigDecimal getR20_MORE2M_TO_3M() {
		return R20_MORE2M_TO_3M;
	}
	public void setR20_MORE2M_TO_3M(BigDecimal r20_MORE2M_TO_3M) {
		R20_MORE2M_TO_3M = r20_MORE2M_TO_3M;
	}
	public BigDecimal getR20_OVER3M_TO_6M() {
		return R20_OVER3M_TO_6M;
	}
	public void setR20_OVER3M_TO_6M(BigDecimal r20_OVER3M_TO_6M) {
		R20_OVER3M_TO_6M = r20_OVER3M_TO_6M;
	}
	public BigDecimal getR20_OVER6M_TO_1Y() {
		return R20_OVER6M_TO_1Y;
	}
	public void setR20_OVER6M_TO_1Y(BigDecimal r20_OVER6M_TO_1Y) {
		R20_OVER6M_TO_1Y = r20_OVER6M_TO_1Y;
	}
	public BigDecimal getR20_OVER1Y_TO_3Y() {
		return R20_OVER1Y_TO_3Y;
	}
	public void setR20_OVER1Y_TO_3Y(BigDecimal r20_OVER1Y_TO_3Y) {
		R20_OVER1Y_TO_3Y = r20_OVER1Y_TO_3Y;
	}
	public BigDecimal getR20_OVER3Y_TO_5Y() {
		return R20_OVER3Y_TO_5Y;
	}
	public void setR20_OVER3Y_TO_5Y(BigDecimal r20_OVER3Y_TO_5Y) {
		R20_OVER3Y_TO_5Y = r20_OVER3Y_TO_5Y;
	}
	public BigDecimal getR20_OVER5Y() {
		return R20_OVER5Y;
	}
	public void setR20_OVER5Y(BigDecimal r20_OVER5Y) {
		R20_OVER5Y = r20_OVER5Y;
	}
	public BigDecimal getR20_TOTAL() {
		return R20_TOTAL;
	}
	public void setR20_TOTAL(BigDecimal r20_TOTAL) {
		R20_TOTAL = r20_TOTAL;
	}
	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}
	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}
	public BigDecimal getR21_DAY1() {
		return R21_DAY1;
	}
	public void setR21_DAY1(BigDecimal r21_DAY1) {
		R21_DAY1 = r21_DAY1;
	}
	public BigDecimal getR21_DAY2_7() {
		return R21_DAY2_7;
	}
	public void setR21_DAY2_7(BigDecimal r21_DAY2_7) {
		R21_DAY2_7 = r21_DAY2_7;
	}
	public BigDecimal getR21_DAY8_14() {
		return R21_DAY8_14;
	}
	public void setR21_DAY8_14(BigDecimal r21_DAY8_14) {
		R21_DAY8_14 = r21_DAY8_14;
	}
	public BigDecimal getR21_DAY15_30() {
		return R21_DAY15_30;
	}
	public void setR21_DAY15_30(BigDecimal r21_DAY15_30) {
		R21_DAY15_30 = r21_DAY15_30;
	}
	public BigDecimal getR21_DAY31_TO_2M() {
		return R21_DAY31_TO_2M;
	}
	public void setR21_DAY31_TO_2M(BigDecimal r21_DAY31_TO_2M) {
		R21_DAY31_TO_2M = r21_DAY31_TO_2M;
	}
	public BigDecimal getR21_MORE2M_TO_3M() {
		return R21_MORE2M_TO_3M;
	}
	public void setR21_MORE2M_TO_3M(BigDecimal r21_MORE2M_TO_3M) {
		R21_MORE2M_TO_3M = r21_MORE2M_TO_3M;
	}
	public BigDecimal getR21_OVER3M_TO_6M() {
		return R21_OVER3M_TO_6M;
	}
	public void setR21_OVER3M_TO_6M(BigDecimal r21_OVER3M_TO_6M) {
		R21_OVER3M_TO_6M = r21_OVER3M_TO_6M;
	}
	public BigDecimal getR21_OVER6M_TO_1Y() {
		return R21_OVER6M_TO_1Y;
	}
	public void setR21_OVER6M_TO_1Y(BigDecimal r21_OVER6M_TO_1Y) {
		R21_OVER6M_TO_1Y = r21_OVER6M_TO_1Y;
	}
	public BigDecimal getR21_OVER1Y_TO_3Y() {
		return R21_OVER1Y_TO_3Y;
	}
	public void setR21_OVER1Y_TO_3Y(BigDecimal r21_OVER1Y_TO_3Y) {
		R21_OVER1Y_TO_3Y = r21_OVER1Y_TO_3Y;
	}
	public BigDecimal getR21_OVER3Y_TO_5Y() {
		return R21_OVER3Y_TO_5Y;
	}
	public void setR21_OVER3Y_TO_5Y(BigDecimal r21_OVER3Y_TO_5Y) {
		R21_OVER3Y_TO_5Y = r21_OVER3Y_TO_5Y;
	}
	public BigDecimal getR21_OVER5Y() {
		return R21_OVER5Y;
	}
	public void setR21_OVER5Y(BigDecimal r21_OVER5Y) {
		R21_OVER5Y = r21_OVER5Y;
	}
	public BigDecimal getR21_TOTAL() {
		return R21_TOTAL;
	}
	public void setR21_TOTAL(BigDecimal r21_TOTAL) {
		R21_TOTAL = r21_TOTAL;
	}
	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}
	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}
	public BigDecimal getR22_DAY1() {
		return R22_DAY1;
	}
	public void setR22_DAY1(BigDecimal r22_DAY1) {
		R22_DAY1 = r22_DAY1;
	}
	public BigDecimal getR22_DAY2_7() {
		return R22_DAY2_7;
	}
	public void setR22_DAY2_7(BigDecimal r22_DAY2_7) {
		R22_DAY2_7 = r22_DAY2_7;
	}
	public BigDecimal getR22_DAY8_14() {
		return R22_DAY8_14;
	}
	public void setR22_DAY8_14(BigDecimal r22_DAY8_14) {
		R22_DAY8_14 = r22_DAY8_14;
	}
	public BigDecimal getR22_DAY15_30() {
		return R22_DAY15_30;
	}
	public void setR22_DAY15_30(BigDecimal r22_DAY15_30) {
		R22_DAY15_30 = r22_DAY15_30;
	}
	public BigDecimal getR22_DAY31_TO_2M() {
		return R22_DAY31_TO_2M;
	}
	public void setR22_DAY31_TO_2M(BigDecimal r22_DAY31_TO_2M) {
		R22_DAY31_TO_2M = r22_DAY31_TO_2M;
	}
	public BigDecimal getR22_MORE2M_TO_3M() {
		return R22_MORE2M_TO_3M;
	}
	public void setR22_MORE2M_TO_3M(BigDecimal r22_MORE2M_TO_3M) {
		R22_MORE2M_TO_3M = r22_MORE2M_TO_3M;
	}
	public BigDecimal getR22_OVER3M_TO_6M() {
		return R22_OVER3M_TO_6M;
	}
	public void setR22_OVER3M_TO_6M(BigDecimal r22_OVER3M_TO_6M) {
		R22_OVER3M_TO_6M = r22_OVER3M_TO_6M;
	}
	public BigDecimal getR22_OVER6M_TO_1Y() {
		return R22_OVER6M_TO_1Y;
	}
	public void setR22_OVER6M_TO_1Y(BigDecimal r22_OVER6M_TO_1Y) {
		R22_OVER6M_TO_1Y = r22_OVER6M_TO_1Y;
	}
	public BigDecimal getR22_OVER1Y_TO_3Y() {
		return R22_OVER1Y_TO_3Y;
	}
	public void setR22_OVER1Y_TO_3Y(BigDecimal r22_OVER1Y_TO_3Y) {
		R22_OVER1Y_TO_3Y = r22_OVER1Y_TO_3Y;
	}
	public BigDecimal getR22_OVER3Y_TO_5Y() {
		return R22_OVER3Y_TO_5Y;
	}
	public void setR22_OVER3Y_TO_5Y(BigDecimal r22_OVER3Y_TO_5Y) {
		R22_OVER3Y_TO_5Y = r22_OVER3Y_TO_5Y;
	}
	public BigDecimal getR22_OVER5Y() {
		return R22_OVER5Y;
	}
	public void setR22_OVER5Y(BigDecimal r22_OVER5Y) {
		R22_OVER5Y = r22_OVER5Y;
	}
	public BigDecimal getR22_TOTAL() {
		return R22_TOTAL;
	}
	public void setR22_TOTAL(BigDecimal r22_TOTAL) {
		R22_TOTAL = r22_TOTAL;
	}
	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}
	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}
	public BigDecimal getR23_DAY1() {
		return R23_DAY1;
	}
	public void setR23_DAY1(BigDecimal r23_DAY1) {
		R23_DAY1 = r23_DAY1;
	}
	public BigDecimal getR23_DAY2_7() {
		return R23_DAY2_7;
	}
	public void setR23_DAY2_7(BigDecimal r23_DAY2_7) {
		R23_DAY2_7 = r23_DAY2_7;
	}
	public BigDecimal getR23_DAY8_14() {
		return R23_DAY8_14;
	}
	public void setR23_DAY8_14(BigDecimal r23_DAY8_14) {
		R23_DAY8_14 = r23_DAY8_14;
	}
	public BigDecimal getR23_DAY15_30() {
		return R23_DAY15_30;
	}
	public void setR23_DAY15_30(BigDecimal r23_DAY15_30) {
		R23_DAY15_30 = r23_DAY15_30;
	}
	public BigDecimal getR23_DAY31_TO_2M() {
		return R23_DAY31_TO_2M;
	}
	public void setR23_DAY31_TO_2M(BigDecimal r23_DAY31_TO_2M) {
		R23_DAY31_TO_2M = r23_DAY31_TO_2M;
	}
	public BigDecimal getR23_MORE2M_TO_3M() {
		return R23_MORE2M_TO_3M;
	}
	public void setR23_MORE2M_TO_3M(BigDecimal r23_MORE2M_TO_3M) {
		R23_MORE2M_TO_3M = r23_MORE2M_TO_3M;
	}
	public BigDecimal getR23_OVER3M_TO_6M() {
		return R23_OVER3M_TO_6M;
	}
	public void setR23_OVER3M_TO_6M(BigDecimal r23_OVER3M_TO_6M) {
		R23_OVER3M_TO_6M = r23_OVER3M_TO_6M;
	}
	public BigDecimal getR23_OVER6M_TO_1Y() {
		return R23_OVER6M_TO_1Y;
	}
	public void setR23_OVER6M_TO_1Y(BigDecimal r23_OVER6M_TO_1Y) {
		R23_OVER6M_TO_1Y = r23_OVER6M_TO_1Y;
	}
	public BigDecimal getR23_OVER1Y_TO_3Y() {
		return R23_OVER1Y_TO_3Y;
	}
	public void setR23_OVER1Y_TO_3Y(BigDecimal r23_OVER1Y_TO_3Y) {
		R23_OVER1Y_TO_3Y = r23_OVER1Y_TO_3Y;
	}
	public BigDecimal getR23_OVER3Y_TO_5Y() {
		return R23_OVER3Y_TO_5Y;
	}
	public void setR23_OVER3Y_TO_5Y(BigDecimal r23_OVER3Y_TO_5Y) {
		R23_OVER3Y_TO_5Y = r23_OVER3Y_TO_5Y;
	}
	public BigDecimal getR23_OVER5Y() {
		return R23_OVER5Y;
	}
	public void setR23_OVER5Y(BigDecimal r23_OVER5Y) {
		R23_OVER5Y = r23_OVER5Y;
	}
	public BigDecimal getR23_TOTAL() {
		return R23_TOTAL;
	}
	public void setR23_TOTAL(BigDecimal r23_TOTAL) {
		R23_TOTAL = r23_TOTAL;
	}
	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}
	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}
	public BigDecimal getR24_DAY1() {
		return R24_DAY1;
	}
	public void setR24_DAY1(BigDecimal r24_DAY1) {
		R24_DAY1 = r24_DAY1;
	}
	public BigDecimal getR24_DAY2_7() {
		return R24_DAY2_7;
	}
	public void setR24_DAY2_7(BigDecimal r24_DAY2_7) {
		R24_DAY2_7 = r24_DAY2_7;
	}
	public BigDecimal getR24_DAY8_14() {
		return R24_DAY8_14;
	}
	public void setR24_DAY8_14(BigDecimal r24_DAY8_14) {
		R24_DAY8_14 = r24_DAY8_14;
	}
	public BigDecimal getR24_DAY15_30() {
		return R24_DAY15_30;
	}
	public void setR24_DAY15_30(BigDecimal r24_DAY15_30) {
		R24_DAY15_30 = r24_DAY15_30;
	}
	public BigDecimal getR24_DAY31_TO_2M() {
		return R24_DAY31_TO_2M;
	}
	public void setR24_DAY31_TO_2M(BigDecimal r24_DAY31_TO_2M) {
		R24_DAY31_TO_2M = r24_DAY31_TO_2M;
	}
	public BigDecimal getR24_MORE2M_TO_3M() {
		return R24_MORE2M_TO_3M;
	}
	public void setR24_MORE2M_TO_3M(BigDecimal r24_MORE2M_TO_3M) {
		R24_MORE2M_TO_3M = r24_MORE2M_TO_3M;
	}
	public BigDecimal getR24_OVER3M_TO_6M() {
		return R24_OVER3M_TO_6M;
	}
	public void setR24_OVER3M_TO_6M(BigDecimal r24_OVER3M_TO_6M) {
		R24_OVER3M_TO_6M = r24_OVER3M_TO_6M;
	}
	public BigDecimal getR24_OVER6M_TO_1Y() {
		return R24_OVER6M_TO_1Y;
	}
	public void setR24_OVER6M_TO_1Y(BigDecimal r24_OVER6M_TO_1Y) {
		R24_OVER6M_TO_1Y = r24_OVER6M_TO_1Y;
	}
	public BigDecimal getR24_OVER1Y_TO_3Y() {
		return R24_OVER1Y_TO_3Y;
	}
	public void setR24_OVER1Y_TO_3Y(BigDecimal r24_OVER1Y_TO_3Y) {
		R24_OVER1Y_TO_3Y = r24_OVER1Y_TO_3Y;
	}
	public BigDecimal getR24_OVER3Y_TO_5Y() {
		return R24_OVER3Y_TO_5Y;
	}
	public void setR24_OVER3Y_TO_5Y(BigDecimal r24_OVER3Y_TO_5Y) {
		R24_OVER3Y_TO_5Y = r24_OVER3Y_TO_5Y;
	}
	public BigDecimal getR24_OVER5Y() {
		return R24_OVER5Y;
	}
	public void setR24_OVER5Y(BigDecimal r24_OVER5Y) {
		R24_OVER5Y = r24_OVER5Y;
	}
	public BigDecimal getR24_TOTAL() {
		return R24_TOTAL;
	}
	public void setR24_TOTAL(BigDecimal r24_TOTAL) {
		R24_TOTAL = r24_TOTAL;
	}
	public String getR25_PRODUCT() {
		return R25_PRODUCT;
	}
	public void setR25_PRODUCT(String r25_PRODUCT) {
		R25_PRODUCT = r25_PRODUCT;
	}
	public BigDecimal getR25_DAY1() {
		return R25_DAY1;
	}
	public void setR25_DAY1(BigDecimal r25_DAY1) {
		R25_DAY1 = r25_DAY1;
	}
	public BigDecimal getR25_DAY2_7() {
		return R25_DAY2_7;
	}
	public void setR25_DAY2_7(BigDecimal r25_DAY2_7) {
		R25_DAY2_7 = r25_DAY2_7;
	}
	public BigDecimal getR25_DAY8_14() {
		return R25_DAY8_14;
	}
	public void setR25_DAY8_14(BigDecimal r25_DAY8_14) {
		R25_DAY8_14 = r25_DAY8_14;
	}
	public BigDecimal getR25_DAY15_30() {
		return R25_DAY15_30;
	}
	public void setR25_DAY15_30(BigDecimal r25_DAY15_30) {
		R25_DAY15_30 = r25_DAY15_30;
	}
	public BigDecimal getR25_DAY31_TO_2M() {
		return R25_DAY31_TO_2M;
	}
	public void setR25_DAY31_TO_2M(BigDecimal r25_DAY31_TO_2M) {
		R25_DAY31_TO_2M = r25_DAY31_TO_2M;
	}
	public BigDecimal getR25_MORE2M_TO_3M() {
		return R25_MORE2M_TO_3M;
	}
	public void setR25_MORE2M_TO_3M(BigDecimal r25_MORE2M_TO_3M) {
		R25_MORE2M_TO_3M = r25_MORE2M_TO_3M;
	}
	public BigDecimal getR25_OVER3M_TO_6M() {
		return R25_OVER3M_TO_6M;
	}
	public void setR25_OVER3M_TO_6M(BigDecimal r25_OVER3M_TO_6M) {
		R25_OVER3M_TO_6M = r25_OVER3M_TO_6M;
	}
	public BigDecimal getR25_OVER6M_TO_1Y() {
		return R25_OVER6M_TO_1Y;
	}
	public void setR25_OVER6M_TO_1Y(BigDecimal r25_OVER6M_TO_1Y) {
		R25_OVER6M_TO_1Y = r25_OVER6M_TO_1Y;
	}
	public BigDecimal getR25_OVER1Y_TO_3Y() {
		return R25_OVER1Y_TO_3Y;
	}
	public void setR25_OVER1Y_TO_3Y(BigDecimal r25_OVER1Y_TO_3Y) {
		R25_OVER1Y_TO_3Y = r25_OVER1Y_TO_3Y;
	}
	public BigDecimal getR25_OVER3Y_TO_5Y() {
		return R25_OVER3Y_TO_5Y;
	}
	public void setR25_OVER3Y_TO_5Y(BigDecimal r25_OVER3Y_TO_5Y) {
		R25_OVER3Y_TO_5Y = r25_OVER3Y_TO_5Y;
	}
	public BigDecimal getR25_OVER5Y() {
		return R25_OVER5Y;
	}
	public void setR25_OVER5Y(BigDecimal r25_OVER5Y) {
		R25_OVER5Y = r25_OVER5Y;
	}
	public BigDecimal getR25_TOTAL() {
		return R25_TOTAL;
	}
	public void setR25_TOTAL(BigDecimal r25_TOTAL) {
		R25_TOTAL = r25_TOTAL;
	}
	public String getR26_PRODUCT() {
		return R26_PRODUCT;
	}
	public void setR26_PRODUCT(String r26_PRODUCT) {
		R26_PRODUCT = r26_PRODUCT;
	}
	public BigDecimal getR26_DAY1() {
		return R26_DAY1;
	}
	public void setR26_DAY1(BigDecimal r26_DAY1) {
		R26_DAY1 = r26_DAY1;
	}
	public BigDecimal getR26_DAY2_7() {
		return R26_DAY2_7;
	}
	public void setR26_DAY2_7(BigDecimal r26_DAY2_7) {
		R26_DAY2_7 = r26_DAY2_7;
	}
	public BigDecimal getR26_DAY8_14() {
		return R26_DAY8_14;
	}
	public void setR26_DAY8_14(BigDecimal r26_DAY8_14) {
		R26_DAY8_14 = r26_DAY8_14;
	}
	public BigDecimal getR26_DAY15_30() {
		return R26_DAY15_30;
	}
	public void setR26_DAY15_30(BigDecimal r26_DAY15_30) {
		R26_DAY15_30 = r26_DAY15_30;
	}
	public BigDecimal getR26_DAY31_TO_2M() {
		return R26_DAY31_TO_2M;
	}
	public void setR26_DAY31_TO_2M(BigDecimal r26_DAY31_TO_2M) {
		R26_DAY31_TO_2M = r26_DAY31_TO_2M;
	}
	public BigDecimal getR26_MORE2M_TO_3M() {
		return R26_MORE2M_TO_3M;
	}
	public void setR26_MORE2M_TO_3M(BigDecimal r26_MORE2M_TO_3M) {
		R26_MORE2M_TO_3M = r26_MORE2M_TO_3M;
	}
	public BigDecimal getR26_OVER3M_TO_6M() {
		return R26_OVER3M_TO_6M;
	}
	public void setR26_OVER3M_TO_6M(BigDecimal r26_OVER3M_TO_6M) {
		R26_OVER3M_TO_6M = r26_OVER3M_TO_6M;
	}
	public BigDecimal getR26_OVER6M_TO_1Y() {
		return R26_OVER6M_TO_1Y;
	}
	public void setR26_OVER6M_TO_1Y(BigDecimal r26_OVER6M_TO_1Y) {
		R26_OVER6M_TO_1Y = r26_OVER6M_TO_1Y;
	}
	public BigDecimal getR26_OVER1Y_TO_3Y() {
		return R26_OVER1Y_TO_3Y;
	}
	public void setR26_OVER1Y_TO_3Y(BigDecimal r26_OVER1Y_TO_3Y) {
		R26_OVER1Y_TO_3Y = r26_OVER1Y_TO_3Y;
	}
	public BigDecimal getR26_OVER3Y_TO_5Y() {
		return R26_OVER3Y_TO_5Y;
	}
	public void setR26_OVER3Y_TO_5Y(BigDecimal r26_OVER3Y_TO_5Y) {
		R26_OVER3Y_TO_5Y = r26_OVER3Y_TO_5Y;
	}
	public BigDecimal getR26_OVER5Y() {
		return R26_OVER5Y;
	}
	public void setR26_OVER5Y(BigDecimal r26_OVER5Y) {
		R26_OVER5Y = r26_OVER5Y;
	}
	public BigDecimal getR26_TOTAL() {
		return R26_TOTAL;
	}
	public void setR26_TOTAL(BigDecimal r26_TOTAL) {
		R26_TOTAL = r26_TOTAL;
	}
	public String getR27_PRODUCT() {
		return R27_PRODUCT;
	}
	public void setR27_PRODUCT(String r27_PRODUCT) {
		R27_PRODUCT = r27_PRODUCT;
	}
	public BigDecimal getR27_DAY1() {
		return R27_DAY1;
	}
	public void setR27_DAY1(BigDecimal r27_DAY1) {
		R27_DAY1 = r27_DAY1;
	}
	public BigDecimal getR27_DAY2_7() {
		return R27_DAY2_7;
	}
	public void setR27_DAY2_7(BigDecimal r27_DAY2_7) {
		R27_DAY2_7 = r27_DAY2_7;
	}
	public BigDecimal getR27_DAY8_14() {
		return R27_DAY8_14;
	}
	public void setR27_DAY8_14(BigDecimal r27_DAY8_14) {
		R27_DAY8_14 = r27_DAY8_14;
	}
	public BigDecimal getR27_DAY15_30() {
		return R27_DAY15_30;
	}
	public void setR27_DAY15_30(BigDecimal r27_DAY15_30) {
		R27_DAY15_30 = r27_DAY15_30;
	}
	public BigDecimal getR27_DAY31_TO_2M() {
		return R27_DAY31_TO_2M;
	}
	public void setR27_DAY31_TO_2M(BigDecimal r27_DAY31_TO_2M) {
		R27_DAY31_TO_2M = r27_DAY31_TO_2M;
	}
	public BigDecimal getR27_MORE2M_TO_3M() {
		return R27_MORE2M_TO_3M;
	}
	public void setR27_MORE2M_TO_3M(BigDecimal r27_MORE2M_TO_3M) {
		R27_MORE2M_TO_3M = r27_MORE2M_TO_3M;
	}
	public BigDecimal getR27_OVER3M_TO_6M() {
		return R27_OVER3M_TO_6M;
	}
	public void setR27_OVER3M_TO_6M(BigDecimal r27_OVER3M_TO_6M) {
		R27_OVER3M_TO_6M = r27_OVER3M_TO_6M;
	}
	public BigDecimal getR27_OVER6M_TO_1Y() {
		return R27_OVER6M_TO_1Y;
	}
	public void setR27_OVER6M_TO_1Y(BigDecimal r27_OVER6M_TO_1Y) {
		R27_OVER6M_TO_1Y = r27_OVER6M_TO_1Y;
	}
	public BigDecimal getR27_OVER1Y_TO_3Y() {
		return R27_OVER1Y_TO_3Y;
	}
	public void setR27_OVER1Y_TO_3Y(BigDecimal r27_OVER1Y_TO_3Y) {
		R27_OVER1Y_TO_3Y = r27_OVER1Y_TO_3Y;
	}
	public BigDecimal getR27_OVER3Y_TO_5Y() {
		return R27_OVER3Y_TO_5Y;
	}
	public void setR27_OVER3Y_TO_5Y(BigDecimal r27_OVER3Y_TO_5Y) {
		R27_OVER3Y_TO_5Y = r27_OVER3Y_TO_5Y;
	}
	public BigDecimal getR27_OVER5Y() {
		return R27_OVER5Y;
	}
	public void setR27_OVER5Y(BigDecimal r27_OVER5Y) {
		R27_OVER5Y = r27_OVER5Y;
	}
	public BigDecimal getR27_TOTAL() {
		return R27_TOTAL;
	}
	public void setR27_TOTAL(BigDecimal r27_TOTAL) {
		R27_TOTAL = r27_TOTAL;
	}
	public String getR28_PRODUCT() {
		return R28_PRODUCT;
	}
	public void setR28_PRODUCT(String r28_PRODUCT) {
		R28_PRODUCT = r28_PRODUCT;
	}
	public BigDecimal getR28_DAY1() {
		return R28_DAY1;
	}
	public void setR28_DAY1(BigDecimal r28_DAY1) {
		R28_DAY1 = r28_DAY1;
	}
	public BigDecimal getR28_DAY2_7() {
		return R28_DAY2_7;
	}
	public void setR28_DAY2_7(BigDecimal r28_DAY2_7) {
		R28_DAY2_7 = r28_DAY2_7;
	}
	public BigDecimal getR28_DAY8_14() {
		return R28_DAY8_14;
	}
	public void setR28_DAY8_14(BigDecimal r28_DAY8_14) {
		R28_DAY8_14 = r28_DAY8_14;
	}
	public BigDecimal getR28_DAY15_30() {
		return R28_DAY15_30;
	}
	public void setR28_DAY15_30(BigDecimal r28_DAY15_30) {
		R28_DAY15_30 = r28_DAY15_30;
	}
	public BigDecimal getR28_DAY31_TO_2M() {
		return R28_DAY31_TO_2M;
	}
	public void setR28_DAY31_TO_2M(BigDecimal r28_DAY31_TO_2M) {
		R28_DAY31_TO_2M = r28_DAY31_TO_2M;
	}
	public BigDecimal getR28_MORE2M_TO_3M() {
		return R28_MORE2M_TO_3M;
	}
	public void setR28_MORE2M_TO_3M(BigDecimal r28_MORE2M_TO_3M) {
		R28_MORE2M_TO_3M = r28_MORE2M_TO_3M;
	}
	public BigDecimal getR28_OVER3M_TO_6M() {
		return R28_OVER3M_TO_6M;
	}
	public void setR28_OVER3M_TO_6M(BigDecimal r28_OVER3M_TO_6M) {
		R28_OVER3M_TO_6M = r28_OVER3M_TO_6M;
	}
	public BigDecimal getR28_OVER6M_TO_1Y() {
		return R28_OVER6M_TO_1Y;
	}
	public void setR28_OVER6M_TO_1Y(BigDecimal r28_OVER6M_TO_1Y) {
		R28_OVER6M_TO_1Y = r28_OVER6M_TO_1Y;
	}
	public BigDecimal getR28_OVER1Y_TO_3Y() {
		return R28_OVER1Y_TO_3Y;
	}
	public void setR28_OVER1Y_TO_3Y(BigDecimal r28_OVER1Y_TO_3Y) {
		R28_OVER1Y_TO_3Y = r28_OVER1Y_TO_3Y;
	}
	public BigDecimal getR28_OVER3Y_TO_5Y() {
		return R28_OVER3Y_TO_5Y;
	}
	public void setR28_OVER3Y_TO_5Y(BigDecimal r28_OVER3Y_TO_5Y) {
		R28_OVER3Y_TO_5Y = r28_OVER3Y_TO_5Y;
	}
	public BigDecimal getR28_OVER5Y() {
		return R28_OVER5Y;
	}
	public void setR28_OVER5Y(BigDecimal r28_OVER5Y) {
		R28_OVER5Y = r28_OVER5Y;
	}
	public BigDecimal getR28_TOTAL() {
		return R28_TOTAL;
	}
	public void setR28_TOTAL(BigDecimal r28_TOTAL) {
		R28_TOTAL = r28_TOTAL;
	}
	public String getR29_PRODUCT() {
		return R29_PRODUCT;
	}
	public void setR29_PRODUCT(String r29_PRODUCT) {
		R29_PRODUCT = r29_PRODUCT;
	}
	public BigDecimal getR29_DAY1() {
		return R29_DAY1;
	}
	public void setR29_DAY1(BigDecimal r29_DAY1) {
		R29_DAY1 = r29_DAY1;
	}
	public BigDecimal getR29_DAY2_7() {
		return R29_DAY2_7;
	}
	public void setR29_DAY2_7(BigDecimal r29_DAY2_7) {
		R29_DAY2_7 = r29_DAY2_7;
	}
	public BigDecimal getR29_DAY8_14() {
		return R29_DAY8_14;
	}
	public void setR29_DAY8_14(BigDecimal r29_DAY8_14) {
		R29_DAY8_14 = r29_DAY8_14;
	}
	public BigDecimal getR29_DAY15_30() {
		return R29_DAY15_30;
	}
	public void setR29_DAY15_30(BigDecimal r29_DAY15_30) {
		R29_DAY15_30 = r29_DAY15_30;
	}
	public BigDecimal getR29_DAY31_TO_2M() {
		return R29_DAY31_TO_2M;
	}
	public void setR29_DAY31_TO_2M(BigDecimal r29_DAY31_TO_2M) {
		R29_DAY31_TO_2M = r29_DAY31_TO_2M;
	}
	public BigDecimal getR29_MORE2M_TO_3M() {
		return R29_MORE2M_TO_3M;
	}
	public void setR29_MORE2M_TO_3M(BigDecimal r29_MORE2M_TO_3M) {
		R29_MORE2M_TO_3M = r29_MORE2M_TO_3M;
	}
	public BigDecimal getR29_OVER3M_TO_6M() {
		return R29_OVER3M_TO_6M;
	}
	public void setR29_OVER3M_TO_6M(BigDecimal r29_OVER3M_TO_6M) {
		R29_OVER3M_TO_6M = r29_OVER3M_TO_6M;
	}
	public BigDecimal getR29_OVER6M_TO_1Y() {
		return R29_OVER6M_TO_1Y;
	}
	public void setR29_OVER6M_TO_1Y(BigDecimal r29_OVER6M_TO_1Y) {
		R29_OVER6M_TO_1Y = r29_OVER6M_TO_1Y;
	}
	public BigDecimal getR29_OVER1Y_TO_3Y() {
		return R29_OVER1Y_TO_3Y;
	}
	public void setR29_OVER1Y_TO_3Y(BigDecimal r29_OVER1Y_TO_3Y) {
		R29_OVER1Y_TO_3Y = r29_OVER1Y_TO_3Y;
	}
	public BigDecimal getR29_OVER3Y_TO_5Y() {
		return R29_OVER3Y_TO_5Y;
	}
	public void setR29_OVER3Y_TO_5Y(BigDecimal r29_OVER3Y_TO_5Y) {
		R29_OVER3Y_TO_5Y = r29_OVER3Y_TO_5Y;
	}
	public BigDecimal getR29_OVER5Y() {
		return R29_OVER5Y;
	}
	public void setR29_OVER5Y(BigDecimal r29_OVER5Y) {
		R29_OVER5Y = r29_OVER5Y;
	}
	public BigDecimal getR29_TOTAL() {
		return R29_TOTAL;
	}
	public void setR29_TOTAL(BigDecimal r29_TOTAL) {
		R29_TOTAL = r29_TOTAL;
	}
	public String getR30_PRODUCT() {
		return R30_PRODUCT;
	}
	public void setR30_PRODUCT(String r30_PRODUCT) {
		R30_PRODUCT = r30_PRODUCT;
	}
	public BigDecimal getR30_DAY1() {
		return R30_DAY1;
	}
	public void setR30_DAY1(BigDecimal r30_DAY1) {
		R30_DAY1 = r30_DAY1;
	}
	public BigDecimal getR30_DAY2_7() {
		return R30_DAY2_7;
	}
	public void setR30_DAY2_7(BigDecimal r30_DAY2_7) {
		R30_DAY2_7 = r30_DAY2_7;
	}
	public BigDecimal getR30_DAY8_14() {
		return R30_DAY8_14;
	}
	public void setR30_DAY8_14(BigDecimal r30_DAY8_14) {
		R30_DAY8_14 = r30_DAY8_14;
	}
	public BigDecimal getR30_DAY15_30() {
		return R30_DAY15_30;
	}
	public void setR30_DAY15_30(BigDecimal r30_DAY15_30) {
		R30_DAY15_30 = r30_DAY15_30;
	}
	public BigDecimal getR30_DAY31_TO_2M() {
		return R30_DAY31_TO_2M;
	}
	public void setR30_DAY31_TO_2M(BigDecimal r30_DAY31_TO_2M) {
		R30_DAY31_TO_2M = r30_DAY31_TO_2M;
	}
	public BigDecimal getR30_MORE2M_TO_3M() {
		return R30_MORE2M_TO_3M;
	}
	public void setR30_MORE2M_TO_3M(BigDecimal r30_MORE2M_TO_3M) {
		R30_MORE2M_TO_3M = r30_MORE2M_TO_3M;
	}
	public BigDecimal getR30_OVER3M_TO_6M() {
		return R30_OVER3M_TO_6M;
	}
	public void setR30_OVER3M_TO_6M(BigDecimal r30_OVER3M_TO_6M) {
		R30_OVER3M_TO_6M = r30_OVER3M_TO_6M;
	}
	public BigDecimal getR30_OVER6M_TO_1Y() {
		return R30_OVER6M_TO_1Y;
	}
	public void setR30_OVER6M_TO_1Y(BigDecimal r30_OVER6M_TO_1Y) {
		R30_OVER6M_TO_1Y = r30_OVER6M_TO_1Y;
	}
	public BigDecimal getR30_OVER1Y_TO_3Y() {
		return R30_OVER1Y_TO_3Y;
	}
	public void setR30_OVER1Y_TO_3Y(BigDecimal r30_OVER1Y_TO_3Y) {
		R30_OVER1Y_TO_3Y = r30_OVER1Y_TO_3Y;
	}
	public BigDecimal getR30_OVER3Y_TO_5Y() {
		return R30_OVER3Y_TO_5Y;
	}
	public void setR30_OVER3Y_TO_5Y(BigDecimal r30_OVER3Y_TO_5Y) {
		R30_OVER3Y_TO_5Y = r30_OVER3Y_TO_5Y;
	}
	public BigDecimal getR30_OVER5Y() {
		return R30_OVER5Y;
	}
	public void setR30_OVER5Y(BigDecimal r30_OVER5Y) {
		R30_OVER5Y = r30_OVER5Y;
	}
	public BigDecimal getR30_TOTAL() {
		return R30_TOTAL;
	}
	public void setR30_TOTAL(BigDecimal r30_TOTAL) {
		R30_TOTAL = r30_TOTAL;
	}
	public String getR31_PRODUCT() {
		return R31_PRODUCT;
	}
	public void setR31_PRODUCT(String r31_PRODUCT) {
		R31_PRODUCT = r31_PRODUCT;
	}
	public BigDecimal getR31_DAY1() {
		return R31_DAY1;
	}
	public void setR31_DAY1(BigDecimal r31_DAY1) {
		R31_DAY1 = r31_DAY1;
	}
	public BigDecimal getR31_DAY2_7() {
		return R31_DAY2_7;
	}
	public void setR31_DAY2_7(BigDecimal r31_DAY2_7) {
		R31_DAY2_7 = r31_DAY2_7;
	}
	public BigDecimal getR31_DAY8_14() {
		return R31_DAY8_14;
	}
	public void setR31_DAY8_14(BigDecimal r31_DAY8_14) {
		R31_DAY8_14 = r31_DAY8_14;
	}
	public BigDecimal getR31_DAY15_30() {
		return R31_DAY15_30;
	}
	public void setR31_DAY15_30(BigDecimal r31_DAY15_30) {
		R31_DAY15_30 = r31_DAY15_30;
	}
	public BigDecimal getR31_DAY31_TO_2M() {
		return R31_DAY31_TO_2M;
	}
	public void setR31_DAY31_TO_2M(BigDecimal r31_DAY31_TO_2M) {
		R31_DAY31_TO_2M = r31_DAY31_TO_2M;
	}
	public BigDecimal getR31_MORE2M_TO_3M() {
		return R31_MORE2M_TO_3M;
	}
	public void setR31_MORE2M_TO_3M(BigDecimal r31_MORE2M_TO_3M) {
		R31_MORE2M_TO_3M = r31_MORE2M_TO_3M;
	}
	public BigDecimal getR31_OVER3M_TO_6M() {
		return R31_OVER3M_TO_6M;
	}
	public void setR31_OVER3M_TO_6M(BigDecimal r31_OVER3M_TO_6M) {
		R31_OVER3M_TO_6M = r31_OVER3M_TO_6M;
	}
	public BigDecimal getR31_OVER6M_TO_1Y() {
		return R31_OVER6M_TO_1Y;
	}
	public void setR31_OVER6M_TO_1Y(BigDecimal r31_OVER6M_TO_1Y) {
		R31_OVER6M_TO_1Y = r31_OVER6M_TO_1Y;
	}
	public BigDecimal getR31_OVER1Y_TO_3Y() {
		return R31_OVER1Y_TO_3Y;
	}
	public void setR31_OVER1Y_TO_3Y(BigDecimal r31_OVER1Y_TO_3Y) {
		R31_OVER1Y_TO_3Y = r31_OVER1Y_TO_3Y;
	}
	public BigDecimal getR31_OVER3Y_TO_5Y() {
		return R31_OVER3Y_TO_5Y;
	}
	public void setR31_OVER3Y_TO_5Y(BigDecimal r31_OVER3Y_TO_5Y) {
		R31_OVER3Y_TO_5Y = r31_OVER3Y_TO_5Y;
	}
	public BigDecimal getR31_OVER5Y() {
		return R31_OVER5Y;
	}
	public void setR31_OVER5Y(BigDecimal r31_OVER5Y) {
		R31_OVER5Y = r31_OVER5Y;
	}
	public BigDecimal getR31_TOTAL() {
		return R31_TOTAL;
	}
	public void setR31_TOTAL(BigDecimal r31_TOTAL) {
		R31_TOTAL = r31_TOTAL;
	}
	public String getR32_PRODUCT() {
		return R32_PRODUCT;
	}
	public void setR32_PRODUCT(String r32_PRODUCT) {
		R32_PRODUCT = r32_PRODUCT;
	}
	public BigDecimal getR32_DAY1() {
		return R32_DAY1;
	}
	public void setR32_DAY1(BigDecimal r32_DAY1) {
		R32_DAY1 = r32_DAY1;
	}
	public BigDecimal getR32_DAY2_7() {
		return R32_DAY2_7;
	}
	public void setR32_DAY2_7(BigDecimal r32_DAY2_7) {
		R32_DAY2_7 = r32_DAY2_7;
	}
	public BigDecimal getR32_DAY8_14() {
		return R32_DAY8_14;
	}
	public void setR32_DAY8_14(BigDecimal r32_DAY8_14) {
		R32_DAY8_14 = r32_DAY8_14;
	}
	public BigDecimal getR32_DAY15_30() {
		return R32_DAY15_30;
	}
	public void setR32_DAY15_30(BigDecimal r32_DAY15_30) {
		R32_DAY15_30 = r32_DAY15_30;
	}
	public BigDecimal getR32_DAY31_TO_2M() {
		return R32_DAY31_TO_2M;
	}
	public void setR32_DAY31_TO_2M(BigDecimal r32_DAY31_TO_2M) {
		R32_DAY31_TO_2M = r32_DAY31_TO_2M;
	}
	public BigDecimal getR32_MORE2M_TO_3M() {
		return R32_MORE2M_TO_3M;
	}
	public void setR32_MORE2M_TO_3M(BigDecimal r32_MORE2M_TO_3M) {
		R32_MORE2M_TO_3M = r32_MORE2M_TO_3M;
	}
	public BigDecimal getR32_OVER3M_TO_6M() {
		return R32_OVER3M_TO_6M;
	}
	public void setR32_OVER3M_TO_6M(BigDecimal r32_OVER3M_TO_6M) {
		R32_OVER3M_TO_6M = r32_OVER3M_TO_6M;
	}
	public BigDecimal getR32_OVER6M_TO_1Y() {
		return R32_OVER6M_TO_1Y;
	}
	public void setR32_OVER6M_TO_1Y(BigDecimal r32_OVER6M_TO_1Y) {
		R32_OVER6M_TO_1Y = r32_OVER6M_TO_1Y;
	}
	public BigDecimal getR32_OVER1Y_TO_3Y() {
		return R32_OVER1Y_TO_3Y;
	}
	public void setR32_OVER1Y_TO_3Y(BigDecimal r32_OVER1Y_TO_3Y) {
		R32_OVER1Y_TO_3Y = r32_OVER1Y_TO_3Y;
	}
	public BigDecimal getR32_OVER3Y_TO_5Y() {
		return R32_OVER3Y_TO_5Y;
	}
	public void setR32_OVER3Y_TO_5Y(BigDecimal r32_OVER3Y_TO_5Y) {
		R32_OVER3Y_TO_5Y = r32_OVER3Y_TO_5Y;
	}
	public BigDecimal getR32_OVER5Y() {
		return R32_OVER5Y;
	}
	public void setR32_OVER5Y(BigDecimal r32_OVER5Y) {
		R32_OVER5Y = r32_OVER5Y;
	}
	public BigDecimal getR32_TOTAL() {
		return R32_TOTAL;
	}
	public void setR32_TOTAL(BigDecimal r32_TOTAL) {
		R32_TOTAL = r32_TOTAL;
	}
	public String getR33_PRODUCT() {
		return R33_PRODUCT;
	}
	public void setR33_PRODUCT(String r33_PRODUCT) {
		R33_PRODUCT = r33_PRODUCT;
	}
	public BigDecimal getR33_DAY1() {
		return R33_DAY1;
	}
	public void setR33_DAY1(BigDecimal r33_DAY1) {
		R33_DAY1 = r33_DAY1;
	}
	public BigDecimal getR33_DAY2_7() {
		return R33_DAY2_7;
	}
	public void setR33_DAY2_7(BigDecimal r33_DAY2_7) {
		R33_DAY2_7 = r33_DAY2_7;
	}
	public BigDecimal getR33_DAY8_14() {
		return R33_DAY8_14;
	}
	public void setR33_DAY8_14(BigDecimal r33_DAY8_14) {
		R33_DAY8_14 = r33_DAY8_14;
	}
	public BigDecimal getR33_DAY15_30() {
		return R33_DAY15_30;
	}
	public void setR33_DAY15_30(BigDecimal r33_DAY15_30) {
		R33_DAY15_30 = r33_DAY15_30;
	}
	public BigDecimal getR33_DAY31_TO_2M() {
		return R33_DAY31_TO_2M;
	}
	public void setR33_DAY31_TO_2M(BigDecimal r33_DAY31_TO_2M) {
		R33_DAY31_TO_2M = r33_DAY31_TO_2M;
	}
	public BigDecimal getR33_MORE2M_TO_3M() {
		return R33_MORE2M_TO_3M;
	}
	public void setR33_MORE2M_TO_3M(BigDecimal r33_MORE2M_TO_3M) {
		R33_MORE2M_TO_3M = r33_MORE2M_TO_3M;
	}
	public BigDecimal getR33_OVER3M_TO_6M() {
		return R33_OVER3M_TO_6M;
	}
	public void setR33_OVER3M_TO_6M(BigDecimal r33_OVER3M_TO_6M) {
		R33_OVER3M_TO_6M = r33_OVER3M_TO_6M;
	}
	public BigDecimal getR33_OVER6M_TO_1Y() {
		return R33_OVER6M_TO_1Y;
	}
	public void setR33_OVER6M_TO_1Y(BigDecimal r33_OVER6M_TO_1Y) {
		R33_OVER6M_TO_1Y = r33_OVER6M_TO_1Y;
	}
	public BigDecimal getR33_OVER1Y_TO_3Y() {
		return R33_OVER1Y_TO_3Y;
	}
	public void setR33_OVER1Y_TO_3Y(BigDecimal r33_OVER1Y_TO_3Y) {
		R33_OVER1Y_TO_3Y = r33_OVER1Y_TO_3Y;
	}
	public BigDecimal getR33_OVER3Y_TO_5Y() {
		return R33_OVER3Y_TO_5Y;
	}
	public void setR33_OVER3Y_TO_5Y(BigDecimal r33_OVER3Y_TO_5Y) {
		R33_OVER3Y_TO_5Y = r33_OVER3Y_TO_5Y;
	}
	public BigDecimal getR33_OVER5Y() {
		return R33_OVER5Y;
	}
	public void setR33_OVER5Y(BigDecimal r33_OVER5Y) {
		R33_OVER5Y = r33_OVER5Y;
	}
	public BigDecimal getR33_TOTAL() {
		return R33_TOTAL;
	}
	public void setR33_TOTAL(BigDecimal r33_TOTAL) {
		R33_TOTAL = r33_TOTAL;
	}
	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}
	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}
	public BigDecimal getR34_DAY1() {
		return R34_DAY1;
	}
	public void setR34_DAY1(BigDecimal r34_DAY1) {
		R34_DAY1 = r34_DAY1;
	}
	public BigDecimal getR34_DAY2_7() {
		return R34_DAY2_7;
	}
	public void setR34_DAY2_7(BigDecimal r34_DAY2_7) {
		R34_DAY2_7 = r34_DAY2_7;
	}
	public BigDecimal getR34_DAY8_14() {
		return R34_DAY8_14;
	}
	public void setR34_DAY8_14(BigDecimal r34_DAY8_14) {
		R34_DAY8_14 = r34_DAY8_14;
	}
	public BigDecimal getR34_DAY15_30() {
		return R34_DAY15_30;
	}
	public void setR34_DAY15_30(BigDecimal r34_DAY15_30) {
		R34_DAY15_30 = r34_DAY15_30;
	}
	public BigDecimal getR34_DAY31_TO_2M() {
		return R34_DAY31_TO_2M;
	}
	public void setR34_DAY31_TO_2M(BigDecimal r34_DAY31_TO_2M) {
		R34_DAY31_TO_2M = r34_DAY31_TO_2M;
	}
	public BigDecimal getR34_MORE2M_TO_3M() {
		return R34_MORE2M_TO_3M;
	}
	public void setR34_MORE2M_TO_3M(BigDecimal r34_MORE2M_TO_3M) {
		R34_MORE2M_TO_3M = r34_MORE2M_TO_3M;
	}
	public BigDecimal getR34_OVER3M_TO_6M() {
		return R34_OVER3M_TO_6M;
	}
	public void setR34_OVER3M_TO_6M(BigDecimal r34_OVER3M_TO_6M) {
		R34_OVER3M_TO_6M = r34_OVER3M_TO_6M;
	}
	public BigDecimal getR34_OVER6M_TO_1Y() {
		return R34_OVER6M_TO_1Y;
	}
	public void setR34_OVER6M_TO_1Y(BigDecimal r34_OVER6M_TO_1Y) {
		R34_OVER6M_TO_1Y = r34_OVER6M_TO_1Y;
	}
	public BigDecimal getR34_OVER1Y_TO_3Y() {
		return R34_OVER1Y_TO_3Y;
	}
	public void setR34_OVER1Y_TO_3Y(BigDecimal r34_OVER1Y_TO_3Y) {
		R34_OVER1Y_TO_3Y = r34_OVER1Y_TO_3Y;
	}
	public BigDecimal getR34_OVER3Y_TO_5Y() {
		return R34_OVER3Y_TO_5Y;
	}
	public void setR34_OVER3Y_TO_5Y(BigDecimal r34_OVER3Y_TO_5Y) {
		R34_OVER3Y_TO_5Y = r34_OVER3Y_TO_5Y;
	}
	public BigDecimal getR34_OVER5Y() {
		return R34_OVER5Y;
	}
	public void setR34_OVER5Y(BigDecimal r34_OVER5Y) {
		R34_OVER5Y = r34_OVER5Y;
	}
	public BigDecimal getR34_TOTAL() {
		return R34_TOTAL;
	}
	public void setR34_TOTAL(BigDecimal r34_TOTAL) {
		R34_TOTAL = r34_TOTAL;
	}
	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}
	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}
	public BigDecimal getR35_DAY1() {
		return R35_DAY1;
	}
	public void setR35_DAY1(BigDecimal r35_DAY1) {
		R35_DAY1 = r35_DAY1;
	}
	public BigDecimal getR35_DAY2_7() {
		return R35_DAY2_7;
	}
	public void setR35_DAY2_7(BigDecimal r35_DAY2_7) {
		R35_DAY2_7 = r35_DAY2_7;
	}
	public BigDecimal getR35_DAY8_14() {
		return R35_DAY8_14;
	}
	public void setR35_DAY8_14(BigDecimal r35_DAY8_14) {
		R35_DAY8_14 = r35_DAY8_14;
	}
	public BigDecimal getR35_DAY15_30() {
		return R35_DAY15_30;
	}
	public void setR35_DAY15_30(BigDecimal r35_DAY15_30) {
		R35_DAY15_30 = r35_DAY15_30;
	}
	public BigDecimal getR35_DAY31_TO_2M() {
		return R35_DAY31_TO_2M;
	}
	public void setR35_DAY31_TO_2M(BigDecimal r35_DAY31_TO_2M) {
		R35_DAY31_TO_2M = r35_DAY31_TO_2M;
	}
	public BigDecimal getR35_MORE2M_TO_3M() {
		return R35_MORE2M_TO_3M;
	}
	public void setR35_MORE2M_TO_3M(BigDecimal r35_MORE2M_TO_3M) {
		R35_MORE2M_TO_3M = r35_MORE2M_TO_3M;
	}
	public BigDecimal getR35_OVER3M_TO_6M() {
		return R35_OVER3M_TO_6M;
	}
	public void setR35_OVER3M_TO_6M(BigDecimal r35_OVER3M_TO_6M) {
		R35_OVER3M_TO_6M = r35_OVER3M_TO_6M;
	}
	public BigDecimal getR35_OVER6M_TO_1Y() {
		return R35_OVER6M_TO_1Y;
	}
	public void setR35_OVER6M_TO_1Y(BigDecimal r35_OVER6M_TO_1Y) {
		R35_OVER6M_TO_1Y = r35_OVER6M_TO_1Y;
	}
	public BigDecimal getR35_OVER1Y_TO_3Y() {
		return R35_OVER1Y_TO_3Y;
	}
	public void setR35_OVER1Y_TO_3Y(BigDecimal r35_OVER1Y_TO_3Y) {
		R35_OVER1Y_TO_3Y = r35_OVER1Y_TO_3Y;
	}
	public BigDecimal getR35_OVER3Y_TO_5Y() {
		return R35_OVER3Y_TO_5Y;
	}
	public void setR35_OVER3Y_TO_5Y(BigDecimal r35_OVER3Y_TO_5Y) {
		R35_OVER3Y_TO_5Y = r35_OVER3Y_TO_5Y;
	}
	public BigDecimal getR35_OVER5Y() {
		return R35_OVER5Y;
	}
	public void setR35_OVER5Y(BigDecimal r35_OVER5Y) {
		R35_OVER5Y = r35_OVER5Y;
	}
	public BigDecimal getR35_TOTAL() {
		return R35_TOTAL;
	}
	public void setR35_TOTAL(BigDecimal r35_TOTAL) {
		R35_TOTAL = r35_TOTAL;
	}
	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}
	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}
	public BigDecimal getR36_DAY1() {
		return R36_DAY1;
	}
	public void setR36_DAY1(BigDecimal r36_DAY1) {
		R36_DAY1 = r36_DAY1;
	}
	public BigDecimal getR36_DAY2_7() {
		return R36_DAY2_7;
	}
	public void setR36_DAY2_7(BigDecimal r36_DAY2_7) {
		R36_DAY2_7 = r36_DAY2_7;
	}
	public BigDecimal getR36_DAY8_14() {
		return R36_DAY8_14;
	}
	public void setR36_DAY8_14(BigDecimal r36_DAY8_14) {
		R36_DAY8_14 = r36_DAY8_14;
	}
	public BigDecimal getR36_DAY15_30() {
		return R36_DAY15_30;
	}
	public void setR36_DAY15_30(BigDecimal r36_DAY15_30) {
		R36_DAY15_30 = r36_DAY15_30;
	}
	public BigDecimal getR36_DAY31_TO_2M() {
		return R36_DAY31_TO_2M;
	}
	public void setR36_DAY31_TO_2M(BigDecimal r36_DAY31_TO_2M) {
		R36_DAY31_TO_2M = r36_DAY31_TO_2M;
	}
	public BigDecimal getR36_MORE2M_TO_3M() {
		return R36_MORE2M_TO_3M;
	}
	public void setR36_MORE2M_TO_3M(BigDecimal r36_MORE2M_TO_3M) {
		R36_MORE2M_TO_3M = r36_MORE2M_TO_3M;
	}
	public BigDecimal getR36_OVER3M_TO_6M() {
		return R36_OVER3M_TO_6M;
	}
	public void setR36_OVER3M_TO_6M(BigDecimal r36_OVER3M_TO_6M) {
		R36_OVER3M_TO_6M = r36_OVER3M_TO_6M;
	}
	public BigDecimal getR36_OVER6M_TO_1Y() {
		return R36_OVER6M_TO_1Y;
	}
	public void setR36_OVER6M_TO_1Y(BigDecimal r36_OVER6M_TO_1Y) {
		R36_OVER6M_TO_1Y = r36_OVER6M_TO_1Y;
	}
	public BigDecimal getR36_OVER1Y_TO_3Y() {
		return R36_OVER1Y_TO_3Y;
	}
	public void setR36_OVER1Y_TO_3Y(BigDecimal r36_OVER1Y_TO_3Y) {
		R36_OVER1Y_TO_3Y = r36_OVER1Y_TO_3Y;
	}
	public BigDecimal getR36_OVER3Y_TO_5Y() {
		return R36_OVER3Y_TO_5Y;
	}
	public void setR36_OVER3Y_TO_5Y(BigDecimal r36_OVER3Y_TO_5Y) {
		R36_OVER3Y_TO_5Y = r36_OVER3Y_TO_5Y;
	}
	public BigDecimal getR36_OVER5Y() {
		return R36_OVER5Y;
	}
	public void setR36_OVER5Y(BigDecimal r36_OVER5Y) {
		R36_OVER5Y = r36_OVER5Y;
	}
	public BigDecimal getR36_TOTAL() {
		return R36_TOTAL;
	}
	public void setR36_TOTAL(BigDecimal r36_TOTAL) {
		R36_TOTAL = r36_TOTAL;
	}
	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}
	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}
	public BigDecimal getR37_DAY1() {
		return R37_DAY1;
	}
	public void setR37_DAY1(BigDecimal r37_DAY1) {
		R37_DAY1 = r37_DAY1;
	}
	public BigDecimal getR37_DAY2_7() {
		return R37_DAY2_7;
	}
	public void setR37_DAY2_7(BigDecimal r37_DAY2_7) {
		R37_DAY2_7 = r37_DAY2_7;
	}
	public BigDecimal getR37_DAY8_14() {
		return R37_DAY8_14;
	}
	public void setR37_DAY8_14(BigDecimal r37_DAY8_14) {
		R37_DAY8_14 = r37_DAY8_14;
	}
	public BigDecimal getR37_DAY15_30() {
		return R37_DAY15_30;
	}
	public void setR37_DAY15_30(BigDecimal r37_DAY15_30) {
		R37_DAY15_30 = r37_DAY15_30;
	}
	public BigDecimal getR37_DAY31_TO_2M() {
		return R37_DAY31_TO_2M;
	}
	public void setR37_DAY31_TO_2M(BigDecimal r37_DAY31_TO_2M) {
		R37_DAY31_TO_2M = r37_DAY31_TO_2M;
	}
	public BigDecimal getR37_MORE2M_TO_3M() {
		return R37_MORE2M_TO_3M;
	}
	public void setR37_MORE2M_TO_3M(BigDecimal r37_MORE2M_TO_3M) {
		R37_MORE2M_TO_3M = r37_MORE2M_TO_3M;
	}
	public BigDecimal getR37_OVER3M_TO_6M() {
		return R37_OVER3M_TO_6M;
	}
	public void setR37_OVER3M_TO_6M(BigDecimal r37_OVER3M_TO_6M) {
		R37_OVER3M_TO_6M = r37_OVER3M_TO_6M;
	}
	public BigDecimal getR37_OVER6M_TO_1Y() {
		return R37_OVER6M_TO_1Y;
	}
	public void setR37_OVER6M_TO_1Y(BigDecimal r37_OVER6M_TO_1Y) {
		R37_OVER6M_TO_1Y = r37_OVER6M_TO_1Y;
	}
	public BigDecimal getR37_OVER1Y_TO_3Y() {
		return R37_OVER1Y_TO_3Y;
	}
	public void setR37_OVER1Y_TO_3Y(BigDecimal r37_OVER1Y_TO_3Y) {
		R37_OVER1Y_TO_3Y = r37_OVER1Y_TO_3Y;
	}
	public BigDecimal getR37_OVER3Y_TO_5Y() {
		return R37_OVER3Y_TO_5Y;
	}
	public void setR37_OVER3Y_TO_5Y(BigDecimal r37_OVER3Y_TO_5Y) {
		R37_OVER3Y_TO_5Y = r37_OVER3Y_TO_5Y;
	}
	public BigDecimal getR37_OVER5Y() {
		return R37_OVER5Y;
	}
	public void setR37_OVER5Y(BigDecimal r37_OVER5Y) {
		R37_OVER5Y = r37_OVER5Y;
	}
	public BigDecimal getR37_TOTAL() {
		return R37_TOTAL;
	}
	public void setR37_TOTAL(BigDecimal r37_TOTAL) {
		R37_TOTAL = r37_TOTAL;
	}
	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}
	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}
	public BigDecimal getR38_DAY1() {
		return R38_DAY1;
	}
	public void setR38_DAY1(BigDecimal r38_DAY1) {
		R38_DAY1 = r38_DAY1;
	}
	public BigDecimal getR38_DAY2_7() {
		return R38_DAY2_7;
	}
	public void setR38_DAY2_7(BigDecimal r38_DAY2_7) {
		R38_DAY2_7 = r38_DAY2_7;
	}
	public BigDecimal getR38_DAY8_14() {
		return R38_DAY8_14;
	}
	public void setR38_DAY8_14(BigDecimal r38_DAY8_14) {
		R38_DAY8_14 = r38_DAY8_14;
	}
	public BigDecimal getR38_DAY15_30() {
		return R38_DAY15_30;
	}
	public void setR38_DAY15_30(BigDecimal r38_DAY15_30) {
		R38_DAY15_30 = r38_DAY15_30;
	}
	public BigDecimal getR38_DAY31_TO_2M() {
		return R38_DAY31_TO_2M;
	}
	public void setR38_DAY31_TO_2M(BigDecimal r38_DAY31_TO_2M) {
		R38_DAY31_TO_2M = r38_DAY31_TO_2M;
	}
	public BigDecimal getR38_MORE2M_TO_3M() {
		return R38_MORE2M_TO_3M;
	}
	public void setR38_MORE2M_TO_3M(BigDecimal r38_MORE2M_TO_3M) {
		R38_MORE2M_TO_3M = r38_MORE2M_TO_3M;
	}
	public BigDecimal getR38_OVER3M_TO_6M() {
		return R38_OVER3M_TO_6M;
	}
	public void setR38_OVER3M_TO_6M(BigDecimal r38_OVER3M_TO_6M) {
		R38_OVER3M_TO_6M = r38_OVER3M_TO_6M;
	}
	public BigDecimal getR38_OVER6M_TO_1Y() {
		return R38_OVER6M_TO_1Y;
	}
	public void setR38_OVER6M_TO_1Y(BigDecimal r38_OVER6M_TO_1Y) {
		R38_OVER6M_TO_1Y = r38_OVER6M_TO_1Y;
	}
	public BigDecimal getR38_OVER1Y_TO_3Y() {
		return R38_OVER1Y_TO_3Y;
	}
	public void setR38_OVER1Y_TO_3Y(BigDecimal r38_OVER1Y_TO_3Y) {
		R38_OVER1Y_TO_3Y = r38_OVER1Y_TO_3Y;
	}
	public BigDecimal getR38_OVER3Y_TO_5Y() {
		return R38_OVER3Y_TO_5Y;
	}
	public void setR38_OVER3Y_TO_5Y(BigDecimal r38_OVER3Y_TO_5Y) {
		R38_OVER3Y_TO_5Y = r38_OVER3Y_TO_5Y;
	}
	public BigDecimal getR38_OVER5Y() {
		return R38_OVER5Y;
	}
	public void setR38_OVER5Y(BigDecimal r38_OVER5Y) {
		R38_OVER5Y = r38_OVER5Y;
	}
	public BigDecimal getR38_TOTAL() {
		return R38_TOTAL;
	}
	public void setR38_TOTAL(BigDecimal r38_TOTAL) {
		R38_TOTAL = r38_TOTAL;
	}
	public String getR39_PRODUCT() {
		return R39_PRODUCT;
	}
	public void setR39_PRODUCT(String r39_PRODUCT) {
		R39_PRODUCT = r39_PRODUCT;
	}
	public BigDecimal getR39_DAY1() {
		return R39_DAY1;
	}
	public void setR39_DAY1(BigDecimal r39_DAY1) {
		R39_DAY1 = r39_DAY1;
	}
	public BigDecimal getR39_DAY2_7() {
		return R39_DAY2_7;
	}
	public void setR39_DAY2_7(BigDecimal r39_DAY2_7) {
		R39_DAY2_7 = r39_DAY2_7;
	}
	public BigDecimal getR39_DAY8_14() {
		return R39_DAY8_14;
	}
	public void setR39_DAY8_14(BigDecimal r39_DAY8_14) {
		R39_DAY8_14 = r39_DAY8_14;
	}
	public BigDecimal getR39_DAY15_30() {
		return R39_DAY15_30;
	}
	public void setR39_DAY15_30(BigDecimal r39_DAY15_30) {
		R39_DAY15_30 = r39_DAY15_30;
	}
	public BigDecimal getR39_DAY31_TO_2M() {
		return R39_DAY31_TO_2M;
	}
	public void setR39_DAY31_TO_2M(BigDecimal r39_DAY31_TO_2M) {
		R39_DAY31_TO_2M = r39_DAY31_TO_2M;
	}
	public BigDecimal getR39_MORE2M_TO_3M() {
		return R39_MORE2M_TO_3M;
	}
	public void setR39_MORE2M_TO_3M(BigDecimal r39_MORE2M_TO_3M) {
		R39_MORE2M_TO_3M = r39_MORE2M_TO_3M;
	}
	public BigDecimal getR39_OVER3M_TO_6M() {
		return R39_OVER3M_TO_6M;
	}
	public void setR39_OVER3M_TO_6M(BigDecimal r39_OVER3M_TO_6M) {
		R39_OVER3M_TO_6M = r39_OVER3M_TO_6M;
	}
	public BigDecimal getR39_OVER6M_TO_1Y() {
		return R39_OVER6M_TO_1Y;
	}
	public void setR39_OVER6M_TO_1Y(BigDecimal r39_OVER6M_TO_1Y) {
		R39_OVER6M_TO_1Y = r39_OVER6M_TO_1Y;
	}
	public BigDecimal getR39_OVER1Y_TO_3Y() {
		return R39_OVER1Y_TO_3Y;
	}
	public void setR39_OVER1Y_TO_3Y(BigDecimal r39_OVER1Y_TO_3Y) {
		R39_OVER1Y_TO_3Y = r39_OVER1Y_TO_3Y;
	}
	public BigDecimal getR39_OVER3Y_TO_5Y() {
		return R39_OVER3Y_TO_5Y;
	}
	public void setR39_OVER3Y_TO_5Y(BigDecimal r39_OVER3Y_TO_5Y) {
		R39_OVER3Y_TO_5Y = r39_OVER3Y_TO_5Y;
	}
	public BigDecimal getR39_OVER5Y() {
		return R39_OVER5Y;
	}
	public void setR39_OVER5Y(BigDecimal r39_OVER5Y) {
		R39_OVER5Y = r39_OVER5Y;
	}
	public BigDecimal getR39_TOTAL() {
		return R39_TOTAL;
	}
	public void setR39_TOTAL(BigDecimal r39_TOTAL) {
		R39_TOTAL = r39_TOTAL;
	}
	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}
	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}
	public BigDecimal getR40_DAY1() {
		return R40_DAY1;
	}
	public void setR40_DAY1(BigDecimal r40_DAY1) {
		R40_DAY1 = r40_DAY1;
	}
	public BigDecimal getR40_DAY2_7() {
		return R40_DAY2_7;
	}
	public void setR40_DAY2_7(BigDecimal r40_DAY2_7) {
		R40_DAY2_7 = r40_DAY2_7;
	}
	public BigDecimal getR40_DAY8_14() {
		return R40_DAY8_14;
	}
	public void setR40_DAY8_14(BigDecimal r40_DAY8_14) {
		R40_DAY8_14 = r40_DAY8_14;
	}
	public BigDecimal getR40_DAY15_30() {
		return R40_DAY15_30;
	}
	public void setR40_DAY15_30(BigDecimal r40_DAY15_30) {
		R40_DAY15_30 = r40_DAY15_30;
	}
	public BigDecimal getR40_DAY31_TO_2M() {
		return R40_DAY31_TO_2M;
	}
	public void setR40_DAY31_TO_2M(BigDecimal r40_DAY31_TO_2M) {
		R40_DAY31_TO_2M = r40_DAY31_TO_2M;
	}
	public BigDecimal getR40_MORE2M_TO_3M() {
		return R40_MORE2M_TO_3M;
	}
	public void setR40_MORE2M_TO_3M(BigDecimal r40_MORE2M_TO_3M) {
		R40_MORE2M_TO_3M = r40_MORE2M_TO_3M;
	}
	public BigDecimal getR40_OVER3M_TO_6M() {
		return R40_OVER3M_TO_6M;
	}
	public void setR40_OVER3M_TO_6M(BigDecimal r40_OVER3M_TO_6M) {
		R40_OVER3M_TO_6M = r40_OVER3M_TO_6M;
	}
	public BigDecimal getR40_OVER6M_TO_1Y() {
		return R40_OVER6M_TO_1Y;
	}
	public void setR40_OVER6M_TO_1Y(BigDecimal r40_OVER6M_TO_1Y) {
		R40_OVER6M_TO_1Y = r40_OVER6M_TO_1Y;
	}
	public BigDecimal getR40_OVER1Y_TO_3Y() {
		return R40_OVER1Y_TO_3Y;
	}
	public void setR40_OVER1Y_TO_3Y(BigDecimal r40_OVER1Y_TO_3Y) {
		R40_OVER1Y_TO_3Y = r40_OVER1Y_TO_3Y;
	}
	public BigDecimal getR40_OVER3Y_TO_5Y() {
		return R40_OVER3Y_TO_5Y;
	}
	public void setR40_OVER3Y_TO_5Y(BigDecimal r40_OVER3Y_TO_5Y) {
		R40_OVER3Y_TO_5Y = r40_OVER3Y_TO_5Y;
	}
	public BigDecimal getR40_OVER5Y() {
		return R40_OVER5Y;
	}
	public void setR40_OVER5Y(BigDecimal r40_OVER5Y) {
		R40_OVER5Y = r40_OVER5Y;
	}
	public BigDecimal getR40_TOTAL() {
		return R40_TOTAL;
	}
	public void setR40_TOTAL(BigDecimal r40_TOTAL) {
		R40_TOTAL = r40_TOTAL;
	}
//	public String getR41_PRODUCT() {
//		return R41_PRODUCT;
//	}
//	public void setR41_PRODUCT(String r41_PRODUCT) {
//		R41_PRODUCT = r41_PRODUCT;
//	}
//	public BigDecimal getR41_DAY1() {
//		return R41_DAY1;
//	}
//	public void setR41_DAY1(BigDecimal r41_DAY1) {
//		R41_DAY1 = r41_DAY1;
//	}
//	public BigDecimal getR41_DAY2_7() {
//		return R41_DAY2_7;
//	}
//	public void setR41_DAY2_7(BigDecimal r41_DAY2_7) {
//		R41_DAY2_7 = r41_DAY2_7;
//	}
//	public BigDecimal getR41_DAY8_14() {
//		return R41_DAY8_14;
//	}
//	public void setR41_DAY8_14(BigDecimal r41_DAY8_14) {
//		R41_DAY8_14 = r41_DAY8_14;
//	}
//	public BigDecimal getR41_DAY15_30() {
//		return R41_DAY15_30;
//	}
//	public void setR41_DAY15_30(BigDecimal r41_DAY15_30) {
//		R41_DAY15_30 = r41_DAY15_30;
//	}
//	public BigDecimal getR41_DAY31_TO_2M() {
//		return R41_DAY31_TO_2M;
//	}
//	public void setR41_DAY31_TO_2M(BigDecimal r41_DAY31_TO_2M) {
//		R41_DAY31_TO_2M = r41_DAY31_TO_2M;
//	}
//	public BigDecimal getR41_MORE2M_TO_3M() {
//		return R41_MORE2M_TO_3M;
//	}
//	public void setR41_MORE2M_TO_3M(BigDecimal r41_MORE2M_TO_3M) {
//		R41_MORE2M_TO_3M = r41_MORE2M_TO_3M;
//	}
//	public BigDecimal getR41_OVER3M_TO_6M() {
//		return R41_OVER3M_TO_6M;
//	}
//	public void setR41_OVER3M_TO_6M(BigDecimal r41_OVER3M_TO_6M) {
//		R41_OVER3M_TO_6M = r41_OVER3M_TO_6M;
//	}
//	public BigDecimal getR41_OVER6M_TO_1Y() {
//		return R41_OVER6M_TO_1Y;
//	}
//	public void setR41_OVER6M_TO_1Y(BigDecimal r41_OVER6M_TO_1Y) {
//		R41_OVER6M_TO_1Y = r41_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR41_OVER1Y_TO_3Y() {
//		return R41_OVER1Y_TO_3Y;
//	}
//	public void setR41_OVER1Y_TO_3Y(BigDecimal r41_OVER1Y_TO_3Y) {
//		R41_OVER1Y_TO_3Y = r41_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR41_OVER3Y_TO_5Y() {
//		return R41_OVER3Y_TO_5Y;
//	}
//	public void setR41_OVER3Y_TO_5Y(BigDecimal r41_OVER3Y_TO_5Y) {
//		R41_OVER3Y_TO_5Y = r41_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR41_OVER5Y() {
//		return R41_OVER5Y;
//	}
//	public void setR41_OVER5Y(BigDecimal r41_OVER5Y) {
//		R41_OVER5Y = r41_OVER5Y;
//	}
//	public BigDecimal getR41_TOTAL() {
//		return R41_TOTAL;
//	}
//	public void setR41_TOTAL(BigDecimal r41_TOTAL) {
//		R41_TOTAL = r41_TOTAL;
//	}
//	public String getR42_PRODUCT() {
//		return R42_PRODUCT;
//	}
//	public void setR42_PRODUCT(String r42_PRODUCT) {
//		R42_PRODUCT = r42_PRODUCT;
//	}
//	public BigDecimal getR42_DAY1() {
//		return R42_DAY1;
//	}
//	public void setR42_DAY1(BigDecimal r42_DAY1) {
//		R42_DAY1 = r42_DAY1;
//	}
//	public BigDecimal getR42_DAY2_7() {
//		return R42_DAY2_7;
//	}
//	public void setR42_DAY2_7(BigDecimal r42_DAY2_7) {
//		R42_DAY2_7 = r42_DAY2_7;
//	}
//	public BigDecimal getR42_DAY8_14() {
//		return R42_DAY8_14;
//	}
//	public void setR42_DAY8_14(BigDecimal r42_DAY8_14) {
//		R42_DAY8_14 = r42_DAY8_14;
//	}
//	public BigDecimal getR42_DAY15_30() {
//		return R42_DAY15_30;
//	}
//	public void setR42_DAY15_30(BigDecimal r42_DAY15_30) {
//		R42_DAY15_30 = r42_DAY15_30;
//	}
//	public BigDecimal getR42_DAY31_TO_2M() {
//		return R42_DAY31_TO_2M;
//	}
//	public void setR42_DAY31_TO_2M(BigDecimal r42_DAY31_TO_2M) {
//		R42_DAY31_TO_2M = r42_DAY31_TO_2M;
//	}
//	public BigDecimal getR42_MORE2M_TO_3M() {
//		return R42_MORE2M_TO_3M;
//	}
//	public void setR42_MORE2M_TO_3M(BigDecimal r42_MORE2M_TO_3M) {
//		R42_MORE2M_TO_3M = r42_MORE2M_TO_3M;
//	}
//	public BigDecimal getR42_OVER3M_TO_6M() {
//		return R42_OVER3M_TO_6M;
//	}
//	public void setR42_OVER3M_TO_6M(BigDecimal r42_OVER3M_TO_6M) {
//		R42_OVER3M_TO_6M = r42_OVER3M_TO_6M;
//	}
//	public BigDecimal getR42_OVER6M_TO_1Y() {
//		return R42_OVER6M_TO_1Y;
//	}
//	public void setR42_OVER6M_TO_1Y(BigDecimal r42_OVER6M_TO_1Y) {
//		R42_OVER6M_TO_1Y = r42_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR42_OVER1Y_TO_3Y() {
//		return R42_OVER1Y_TO_3Y;
//	}
//	public void setR42_OVER1Y_TO_3Y(BigDecimal r42_OVER1Y_TO_3Y) {
//		R42_OVER1Y_TO_3Y = r42_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR42_OVER3Y_TO_5Y() {
//		return R42_OVER3Y_TO_5Y;
//	}
//	public void setR42_OVER3Y_TO_5Y(BigDecimal r42_OVER3Y_TO_5Y) {
//		R42_OVER3Y_TO_5Y = r42_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR42_OVER5Y() {
//		return R42_OVER5Y;
//	}
//	public void setR42_OVER5Y(BigDecimal r42_OVER5Y) {
//		R42_OVER5Y = r42_OVER5Y;
//	}
//	public BigDecimal getR42_TOTAL() {
//		return R42_TOTAL;
//	}
//	public void setR42_TOTAL(BigDecimal r42_TOTAL) {
//		R42_TOTAL = r42_TOTAL;
//	}
//	public String getR43_PRODUCT() {
//		return R43_PRODUCT;
//	}
//	public void setR43_PRODUCT(String r43_PRODUCT) {
//		R43_PRODUCT = r43_PRODUCT;
//	}
//	public BigDecimal getR43_DAY1() {
//		return R43_DAY1;
//	}
//	public void setR43_DAY1(BigDecimal r43_DAY1) {
//		R43_DAY1 = r43_DAY1;
//	}
//	public BigDecimal getR43_DAY2_7() {
//		return R43_DAY2_7;
//	}
//	public void setR43_DAY2_7(BigDecimal r43_DAY2_7) {
//		R43_DAY2_7 = r43_DAY2_7;
//	}
//	public BigDecimal getR43_DAY8_14() {
//		return R43_DAY8_14;
//	}
//	public void setR43_DAY8_14(BigDecimal r43_DAY8_14) {
//		R43_DAY8_14 = r43_DAY8_14;
//	}
//	public BigDecimal getR43_DAY15_30() {
//		return R43_DAY15_30;
//	}
//	public void setR43_DAY15_30(BigDecimal r43_DAY15_30) {
//		R43_DAY15_30 = r43_DAY15_30;
//	}
//	public BigDecimal getR43_DAY31_TO_2M() {
//		return R43_DAY31_TO_2M;
//	}
//	public void setR43_DAY31_TO_2M(BigDecimal r43_DAY31_TO_2M) {
//		R43_DAY31_TO_2M = r43_DAY31_TO_2M;
//	}
//	public BigDecimal getR43_MORE2M_TO_3M() {
//		return R43_MORE2M_TO_3M;
//	}
//	public void setR43_MORE2M_TO_3M(BigDecimal r43_MORE2M_TO_3M) {
//		R43_MORE2M_TO_3M = r43_MORE2M_TO_3M;
//	}
//	public BigDecimal getR43_OVER3M_TO_6M() {
//		return R43_OVER3M_TO_6M;
//	}
//	public void setR43_OVER3M_TO_6M(BigDecimal r43_OVER3M_TO_6M) {
//		R43_OVER3M_TO_6M = r43_OVER3M_TO_6M;
//	}
//	public BigDecimal getR43_OVER6M_TO_1Y() {
//		return R43_OVER6M_TO_1Y;
//	}
//	public void setR43_OVER6M_TO_1Y(BigDecimal r43_OVER6M_TO_1Y) {
//		R43_OVER6M_TO_1Y = r43_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR43_OVER1Y_TO_3Y() {
//		return R43_OVER1Y_TO_3Y;
//	}
//	public void setR43_OVER1Y_TO_3Y(BigDecimal r43_OVER1Y_TO_3Y) {
//		R43_OVER1Y_TO_3Y = r43_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR43_OVER3Y_TO_5Y() {
//		return R43_OVER3Y_TO_5Y;
//	}
//	public void setR43_OVER3Y_TO_5Y(BigDecimal r43_OVER3Y_TO_5Y) {
//		R43_OVER3Y_TO_5Y = r43_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR43_OVER5Y() {
//		return R43_OVER5Y;
//	}
//	public void setR43_OVER5Y(BigDecimal r43_OVER5Y) {
//		R43_OVER5Y = r43_OVER5Y;
//	}
//	public BigDecimal getR43_TOTAL() {
//		return R43_TOTAL;
//	}
//	public void setR43_TOTAL(BigDecimal r43_TOTAL) {
//		R43_TOTAL = r43_TOTAL;
//	}
//	public String getR44_PRODUCT() {
//		return R44_PRODUCT;
//	}
//	public void setR44_PRODUCT(String r44_PRODUCT) {
//		R44_PRODUCT = r44_PRODUCT;
//	}
//	public BigDecimal getR44_DAY1() {
//		return R44_DAY1;
//	}
//	public void setR44_DAY1(BigDecimal r44_DAY1) {
//		R44_DAY1 = r44_DAY1;
//	}
//	public BigDecimal getR44_DAY2_7() {
//		return R44_DAY2_7;
//	}
//	public void setR44_DAY2_7(BigDecimal r44_DAY2_7) {
//		R44_DAY2_7 = r44_DAY2_7;
//	}
//	public BigDecimal getR44_DAY8_14() {
//		return R44_DAY8_14;
//	}
//	public void setR44_DAY8_14(BigDecimal r44_DAY8_14) {
//		R44_DAY8_14 = r44_DAY8_14;
//	}
//	public BigDecimal getR44_DAY15_30() {
//		return R44_DAY15_30;
//	}
//	public void setR44_DAY15_30(BigDecimal r44_DAY15_30) {
//		R44_DAY15_30 = r44_DAY15_30;
//	}
//	public BigDecimal getR44_DAY31_TO_2M() {
//		return R44_DAY31_TO_2M;
//	}
//	public void setR44_DAY31_TO_2M(BigDecimal r44_DAY31_TO_2M) {
//		R44_DAY31_TO_2M = r44_DAY31_TO_2M;
//	}
//	public BigDecimal getR44_MORE2M_TO_3M() {
//		return R44_MORE2M_TO_3M;
//	}
//	public void setR44_MORE2M_TO_3M(BigDecimal r44_MORE2M_TO_3M) {
//		R44_MORE2M_TO_3M = r44_MORE2M_TO_3M;
//	}
//	public BigDecimal getR44_OVER3M_TO_6M() {
//		return R44_OVER3M_TO_6M;
//	}
//	public void setR44_OVER3M_TO_6M(BigDecimal r44_OVER3M_TO_6M) {
//		R44_OVER3M_TO_6M = r44_OVER3M_TO_6M;
//	}
//	public BigDecimal getR44_OVER6M_TO_1Y() {
//		return R44_OVER6M_TO_1Y;
//	}
//	public void setR44_OVER6M_TO_1Y(BigDecimal r44_OVER6M_TO_1Y) {
//		R44_OVER6M_TO_1Y = r44_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR44_OVER1Y_TO_3Y() {
//		return R44_OVER1Y_TO_3Y;
//	}
//	public void setR44_OVER1Y_TO_3Y(BigDecimal r44_OVER1Y_TO_3Y) {
//		R44_OVER1Y_TO_3Y = r44_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR44_OVER3Y_TO_5Y() {
//		return R44_OVER3Y_TO_5Y;
//	}
//	public void setR44_OVER3Y_TO_5Y(BigDecimal r44_OVER3Y_TO_5Y) {
//		R44_OVER3Y_TO_5Y = r44_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR44_OVER5Y() {
//		return R44_OVER5Y;
//	}
//	public void setR44_OVER5Y(BigDecimal r44_OVER5Y) {
//		R44_OVER5Y = r44_OVER5Y;
//	}
//	public BigDecimal getR44_TOTAL() {
//		return R44_TOTAL;
//	}
//	public void setR44_TOTAL(BigDecimal r44_TOTAL) {
//		R44_TOTAL = r44_TOTAL;
//	}
//	public String getR45_PRODUCT() {
//		return R45_PRODUCT;
//	}
//	public void setR45_PRODUCT(String r45_PRODUCT) {
//		R45_PRODUCT = r45_PRODUCT;
//	}
//	public BigDecimal getR45_DAY1() {
//		return R45_DAY1;
//	}
//	public void setR45_DAY1(BigDecimal r45_DAY1) {
//		R45_DAY1 = r45_DAY1;
//	}
//	public BigDecimal getR45_DAY2_7() {
//		return R45_DAY2_7;
//	}
//	public void setR45_DAY2_7(BigDecimal r45_DAY2_7) {
//		R45_DAY2_7 = r45_DAY2_7;
//	}
//	public BigDecimal getR45_DAY8_14() {
//		return R45_DAY8_14;
//	}
//	public void setR45_DAY8_14(BigDecimal r45_DAY8_14) {
//		R45_DAY8_14 = r45_DAY8_14;
//	}
//	public BigDecimal getR45_DAY15_30() {
//		return R45_DAY15_30;
//	}
//	public void setR45_DAY15_30(BigDecimal r45_DAY15_30) {
//		R45_DAY15_30 = r45_DAY15_30;
//	}
//	public BigDecimal getR45_DAY31_TO_2M() {
//		return R45_DAY31_TO_2M;
//	}
//	public void setR45_DAY31_TO_2M(BigDecimal r45_DAY31_TO_2M) {
//		R45_DAY31_TO_2M = r45_DAY31_TO_2M;
//	}
//	public BigDecimal getR45_MORE2M_TO_3M() {
//		return R45_MORE2M_TO_3M;
//	}
//	public void setR45_MORE2M_TO_3M(BigDecimal r45_MORE2M_TO_3M) {
//		R45_MORE2M_TO_3M = r45_MORE2M_TO_3M;
//	}
//	public BigDecimal getR45_OVER3M_TO_6M() {
//		return R45_OVER3M_TO_6M;
//	}
//	public void setR45_OVER3M_TO_6M(BigDecimal r45_OVER3M_TO_6M) {
//		R45_OVER3M_TO_6M = r45_OVER3M_TO_6M;
//	}
//	public BigDecimal getR45_OVER6M_TO_1Y() {
//		return R45_OVER6M_TO_1Y;
//	}
//	public void setR45_OVER6M_TO_1Y(BigDecimal r45_OVER6M_TO_1Y) {
//		R45_OVER6M_TO_1Y = r45_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR45_OVER1Y_TO_3Y() {
//		return R45_OVER1Y_TO_3Y;
//	}
//	public void setR45_OVER1Y_TO_3Y(BigDecimal r45_OVER1Y_TO_3Y) {
//		R45_OVER1Y_TO_3Y = r45_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR45_OVER3Y_TO_5Y() {
//		return R45_OVER3Y_TO_5Y;
//	}
//	public void setR45_OVER3Y_TO_5Y(BigDecimal r45_OVER3Y_TO_5Y) {
//		R45_OVER3Y_TO_5Y = r45_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR45_OVER5Y() {
//		return R45_OVER5Y;
//	}
//	public void setR45_OVER5Y(BigDecimal r45_OVER5Y) {
//		R45_OVER5Y = r45_OVER5Y;
//	}
//	public BigDecimal getR45_TOTAL() {
//		return R45_TOTAL;
//	}
//	public void setR45_TOTAL(BigDecimal r45_TOTAL) {
//		R45_TOTAL = r45_TOTAL;
//	}
//	public String getR46_PRODUCT() {
//		return R46_PRODUCT;
//	}
//	public void setR46_PRODUCT(String r46_PRODUCT) {
//		R46_PRODUCT = r46_PRODUCT;
//	}
//	public BigDecimal getR46_DAY1() {
//		return R46_DAY1;
//	}
//	public void setR46_DAY1(BigDecimal r46_DAY1) {
//		R46_DAY1 = r46_DAY1;
//	}
//	public BigDecimal getR46_DAY2_7() {
//		return R46_DAY2_7;
//	}
//	public void setR46_DAY2_7(BigDecimal r46_DAY2_7) {
//		R46_DAY2_7 = r46_DAY2_7;
//	}
//	public BigDecimal getR46_DAY8_14() {
//		return R46_DAY8_14;
//	}
//	public void setR46_DAY8_14(BigDecimal r46_DAY8_14) {
//		R46_DAY8_14 = r46_DAY8_14;
//	}
//	public BigDecimal getR46_DAY15_30() {
//		return R46_DAY15_30;
//	}
//	public void setR46_DAY15_30(BigDecimal r46_DAY15_30) {
//		R46_DAY15_30 = r46_DAY15_30;
//	}
//	public BigDecimal getR46_DAY31_TO_2M() {
//		return R46_DAY31_TO_2M;
//	}
//	public void setR46_DAY31_TO_2M(BigDecimal r46_DAY31_TO_2M) {
//		R46_DAY31_TO_2M = r46_DAY31_TO_2M;
//	}
//	public BigDecimal getR46_MORE2M_TO_3M() {
//		return R46_MORE2M_TO_3M;
//	}
//	public void setR46_MORE2M_TO_3M(BigDecimal r46_MORE2M_TO_3M) {
//		R46_MORE2M_TO_3M = r46_MORE2M_TO_3M;
//	}
//	public BigDecimal getR46_OVER3M_TO_6M() {
//		return R46_OVER3M_TO_6M;
//	}
//	public void setR46_OVER3M_TO_6M(BigDecimal r46_OVER3M_TO_6M) {
//		R46_OVER3M_TO_6M = r46_OVER3M_TO_6M;
//	}
//	public BigDecimal getR46_OVER6M_TO_1Y() {
//		return R46_OVER6M_TO_1Y;
//	}
//	public void setR46_OVER6M_TO_1Y(BigDecimal r46_OVER6M_TO_1Y) {
//		R46_OVER6M_TO_1Y = r46_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR46_OVER1Y_TO_3Y() {
//		return R46_OVER1Y_TO_3Y;
//	}
//	public void setR46_OVER1Y_TO_3Y(BigDecimal r46_OVER1Y_TO_3Y) {
//		R46_OVER1Y_TO_3Y = r46_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR46_OVER3Y_TO_5Y() {
//		return R46_OVER3Y_TO_5Y;
//	}
//	public void setR46_OVER3Y_TO_5Y(BigDecimal r46_OVER3Y_TO_5Y) {
//		R46_OVER3Y_TO_5Y = r46_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR46_OVER5Y() {
//		return R46_OVER5Y;
//	}
//	public void setR46_OVER5Y(BigDecimal r46_OVER5Y) {
//		R46_OVER5Y = r46_OVER5Y;
//	}
//	public BigDecimal getR46_TOTAL() {
//		return R46_TOTAL;
//	}
//	public void setR46_TOTAL(BigDecimal r46_TOTAL) {
//		R46_TOTAL = r46_TOTAL;
//	}
	public String getR47_PRODUCT() {
		return R47_PRODUCT;
	}
	public void setR47_PRODUCT(String r47_PRODUCT) {
		R47_PRODUCT = r47_PRODUCT;
	}
	public BigDecimal getR47_DAY1() {
		return R47_DAY1;
	}
	public void setR47_DAY1(BigDecimal r47_DAY1) {
		R47_DAY1 = r47_DAY1;
	}
	public BigDecimal getR47_DAY2_7() {
		return R47_DAY2_7;
	}
	public void setR47_DAY2_7(BigDecimal r47_DAY2_7) {
		R47_DAY2_7 = r47_DAY2_7;
	}
	public BigDecimal getR47_DAY8_14() {
		return R47_DAY8_14;
	}
	public void setR47_DAY8_14(BigDecimal r47_DAY8_14) {
		R47_DAY8_14 = r47_DAY8_14;
	}
	public BigDecimal getR47_DAY15_30() {
		return R47_DAY15_30;
	}
	public void setR47_DAY15_30(BigDecimal r47_DAY15_30) {
		R47_DAY15_30 = r47_DAY15_30;
	}
	public BigDecimal getR47_DAY31_TO_2M() {
		return R47_DAY31_TO_2M;
	}
	public void setR47_DAY31_TO_2M(BigDecimal r47_DAY31_TO_2M) {
		R47_DAY31_TO_2M = r47_DAY31_TO_2M;
	}
	public BigDecimal getR47_MORE2M_TO_3M() {
		return R47_MORE2M_TO_3M;
	}
	public void setR47_MORE2M_TO_3M(BigDecimal r47_MORE2M_TO_3M) {
		R47_MORE2M_TO_3M = r47_MORE2M_TO_3M;
	}
	public BigDecimal getR47_OVER3M_TO_6M() {
		return R47_OVER3M_TO_6M;
	}
	public void setR47_OVER3M_TO_6M(BigDecimal r47_OVER3M_TO_6M) {
		R47_OVER3M_TO_6M = r47_OVER3M_TO_6M;
	}
	public BigDecimal getR47_OVER6M_TO_1Y() {
		return R47_OVER6M_TO_1Y;
	}
	public void setR47_OVER6M_TO_1Y(BigDecimal r47_OVER6M_TO_1Y) {
		R47_OVER6M_TO_1Y = r47_OVER6M_TO_1Y;
	}
	public BigDecimal getR47_OVER1Y_TO_3Y() {
		return R47_OVER1Y_TO_3Y;
	}
	public void setR47_OVER1Y_TO_3Y(BigDecimal r47_OVER1Y_TO_3Y) {
		R47_OVER1Y_TO_3Y = r47_OVER1Y_TO_3Y;
	}
	public BigDecimal getR47_OVER3Y_TO_5Y() {
		return R47_OVER3Y_TO_5Y;
	}
	public void setR47_OVER3Y_TO_5Y(BigDecimal r47_OVER3Y_TO_5Y) {
		R47_OVER3Y_TO_5Y = r47_OVER3Y_TO_5Y;
	}
	public BigDecimal getR47_OVER5Y() {
		return R47_OVER5Y;
	}
	public void setR47_OVER5Y(BigDecimal r47_OVER5Y) {
		R47_OVER5Y = r47_OVER5Y;
	}
	public BigDecimal getR47_TOTAL() {
		return R47_TOTAL;
	}
	public void setR47_TOTAL(BigDecimal r47_TOTAL) {
		R47_TOTAL = r47_TOTAL;
	}
	public String getR48_PRODUCT() {
		return R48_PRODUCT;
	}
	public void setR48_PRODUCT(String r48_PRODUCT) {
		R48_PRODUCT = r48_PRODUCT;
	}
	public BigDecimal getR48_DAY1() {
		return R48_DAY1;
	}
	public void setR48_DAY1(BigDecimal r48_DAY1) {
		R48_DAY1 = r48_DAY1;
	}
	public BigDecimal getR48_DAY2_7() {
		return R48_DAY2_7;
	}
	public void setR48_DAY2_7(BigDecimal r48_DAY2_7) {
		R48_DAY2_7 = r48_DAY2_7;
	}
	public BigDecimal getR48_DAY8_14() {
		return R48_DAY8_14;
	}
	public void setR48_DAY8_14(BigDecimal r48_DAY8_14) {
		R48_DAY8_14 = r48_DAY8_14;
	}
	public BigDecimal getR48_DAY15_30() {
		return R48_DAY15_30;
	}
	public void setR48_DAY15_30(BigDecimal r48_DAY15_30) {
		R48_DAY15_30 = r48_DAY15_30;
	}
	public BigDecimal getR48_DAY31_TO_2M() {
		return R48_DAY31_TO_2M;
	}
	public void setR48_DAY31_TO_2M(BigDecimal r48_DAY31_TO_2M) {
		R48_DAY31_TO_2M = r48_DAY31_TO_2M;
	}
	public BigDecimal getR48_MORE2M_TO_3M() {
		return R48_MORE2M_TO_3M;
	}
	public void setR48_MORE2M_TO_3M(BigDecimal r48_MORE2M_TO_3M) {
		R48_MORE2M_TO_3M = r48_MORE2M_TO_3M;
	}
	public BigDecimal getR48_OVER3M_TO_6M() {
		return R48_OVER3M_TO_6M;
	}
	public void setR48_OVER3M_TO_6M(BigDecimal r48_OVER3M_TO_6M) {
		R48_OVER3M_TO_6M = r48_OVER3M_TO_6M;
	}
	public BigDecimal getR48_OVER6M_TO_1Y() {
		return R48_OVER6M_TO_1Y;
	}
	public void setR48_OVER6M_TO_1Y(BigDecimal r48_OVER6M_TO_1Y) {
		R48_OVER6M_TO_1Y = r48_OVER6M_TO_1Y;
	}
	public BigDecimal getR48_OVER1Y_TO_3Y() {
		return R48_OVER1Y_TO_3Y;
	}
	public void setR48_OVER1Y_TO_3Y(BigDecimal r48_OVER1Y_TO_3Y) {
		R48_OVER1Y_TO_3Y = r48_OVER1Y_TO_3Y;
	}
	public BigDecimal getR48_OVER3Y_TO_5Y() {
		return R48_OVER3Y_TO_5Y;
	}
	public void setR48_OVER3Y_TO_5Y(BigDecimal r48_OVER3Y_TO_5Y) {
		R48_OVER3Y_TO_5Y = r48_OVER3Y_TO_5Y;
	}
	public BigDecimal getR48_OVER5Y() {
		return R48_OVER5Y;
	}
	public void setR48_OVER5Y(BigDecimal r48_OVER5Y) {
		R48_OVER5Y = r48_OVER5Y;
	}
	public BigDecimal getR48_TOTAL() {
		return R48_TOTAL;
	}
	public void setR48_TOTAL(BigDecimal r48_TOTAL) {
		R48_TOTAL = r48_TOTAL;
	}
	public String getR49_PRODUCT() {
		return R49_PRODUCT;
	}
	public void setR49_PRODUCT(String r49_PRODUCT) {
		R49_PRODUCT = r49_PRODUCT;
	}
	public BigDecimal getR49_DAY1() {
		return R49_DAY1;
	}
	public void setR49_DAY1(BigDecimal r49_DAY1) {
		R49_DAY1 = r49_DAY1;
	}
	public BigDecimal getR49_DAY2_7() {
		return R49_DAY2_7;
	}
	public void setR49_DAY2_7(BigDecimal r49_DAY2_7) {
		R49_DAY2_7 = r49_DAY2_7;
	}
	public BigDecimal getR49_DAY8_14() {
		return R49_DAY8_14;
	}
	public void setR49_DAY8_14(BigDecimal r49_DAY8_14) {
		R49_DAY8_14 = r49_DAY8_14;
	}
	public BigDecimal getR49_DAY15_30() {
		return R49_DAY15_30;
	}
	public void setR49_DAY15_30(BigDecimal r49_DAY15_30) {
		R49_DAY15_30 = r49_DAY15_30;
	}
	public BigDecimal getR49_DAY31_TO_2M() {
		return R49_DAY31_TO_2M;
	}
	public void setR49_DAY31_TO_2M(BigDecimal r49_DAY31_TO_2M) {
		R49_DAY31_TO_2M = r49_DAY31_TO_2M;
	}
	public BigDecimal getR49_MORE2M_TO_3M() {
		return R49_MORE2M_TO_3M;
	}
	public void setR49_MORE2M_TO_3M(BigDecimal r49_MORE2M_TO_3M) {
		R49_MORE2M_TO_3M = r49_MORE2M_TO_3M;
	}
	public BigDecimal getR49_OVER3M_TO_6M() {
		return R49_OVER3M_TO_6M;
	}
	public void setR49_OVER3M_TO_6M(BigDecimal r49_OVER3M_TO_6M) {
		R49_OVER3M_TO_6M = r49_OVER3M_TO_6M;
	}
	public BigDecimal getR49_OVER6M_TO_1Y() {
		return R49_OVER6M_TO_1Y;
	}
	public void setR49_OVER6M_TO_1Y(BigDecimal r49_OVER6M_TO_1Y) {
		R49_OVER6M_TO_1Y = r49_OVER6M_TO_1Y;
	}
	public BigDecimal getR49_OVER1Y_TO_3Y() {
		return R49_OVER1Y_TO_3Y;
	}
	public void setR49_OVER1Y_TO_3Y(BigDecimal r49_OVER1Y_TO_3Y) {
		R49_OVER1Y_TO_3Y = r49_OVER1Y_TO_3Y;
	}
	public BigDecimal getR49_OVER3Y_TO_5Y() {
		return R49_OVER3Y_TO_5Y;
	}
	public void setR49_OVER3Y_TO_5Y(BigDecimal r49_OVER3Y_TO_5Y) {
		R49_OVER3Y_TO_5Y = r49_OVER3Y_TO_5Y;
	}
	public BigDecimal getR49_OVER5Y() {
		return R49_OVER5Y;
	}
	public void setR49_OVER5Y(BigDecimal r49_OVER5Y) {
		R49_OVER5Y = r49_OVER5Y;
	}
	public BigDecimal getR49_TOTAL() {
		return R49_TOTAL;
	}
	public void setR49_TOTAL(BigDecimal r49_TOTAL) {
		R49_TOTAL = r49_TOTAL;
	}
	public String getR50_PRODUCT() {
		return R50_PRODUCT;
	}
	public void setR50_PRODUCT(String r50_PRODUCT) {
		R50_PRODUCT = r50_PRODUCT;
	}
	public BigDecimal getR50_DAY1() {
		return R50_DAY1;
	}
	public void setR50_DAY1(BigDecimal r50_DAY1) {
		R50_DAY1 = r50_DAY1;
	}
	public BigDecimal getR50_DAY2_7() {
		return R50_DAY2_7;
	}
	public void setR50_DAY2_7(BigDecimal r50_DAY2_7) {
		R50_DAY2_7 = r50_DAY2_7;
	}
	public BigDecimal getR50_DAY8_14() {
		return R50_DAY8_14;
	}
	public void setR50_DAY8_14(BigDecimal r50_DAY8_14) {
		R50_DAY8_14 = r50_DAY8_14;
	}
	public BigDecimal getR50_DAY15_30() {
		return R50_DAY15_30;
	}
	public void setR50_DAY15_30(BigDecimal r50_DAY15_30) {
		R50_DAY15_30 = r50_DAY15_30;
	}
	public BigDecimal getR50_DAY31_TO_2M() {
		return R50_DAY31_TO_2M;
	}
	public void setR50_DAY31_TO_2M(BigDecimal r50_DAY31_TO_2M) {
		R50_DAY31_TO_2M = r50_DAY31_TO_2M;
	}
	public BigDecimal getR50_MORE2M_TO_3M() {
		return R50_MORE2M_TO_3M;
	}
	public void setR50_MORE2M_TO_3M(BigDecimal r50_MORE2M_TO_3M) {
		R50_MORE2M_TO_3M = r50_MORE2M_TO_3M;
	}
	public BigDecimal getR50_OVER3M_TO_6M() {
		return R50_OVER3M_TO_6M;
	}
	public void setR50_OVER3M_TO_6M(BigDecimal r50_OVER3M_TO_6M) {
		R50_OVER3M_TO_6M = r50_OVER3M_TO_6M;
	}
	public BigDecimal getR50_OVER6M_TO_1Y() {
		return R50_OVER6M_TO_1Y;
	}
	public void setR50_OVER6M_TO_1Y(BigDecimal r50_OVER6M_TO_1Y) {
		R50_OVER6M_TO_1Y = r50_OVER6M_TO_1Y;
	}
	public BigDecimal getR50_OVER1Y_TO_3Y() {
		return R50_OVER1Y_TO_3Y;
	}
	public void setR50_OVER1Y_TO_3Y(BigDecimal r50_OVER1Y_TO_3Y) {
		R50_OVER1Y_TO_3Y = r50_OVER1Y_TO_3Y;
	}
	public BigDecimal getR50_OVER3Y_TO_5Y() {
		return R50_OVER3Y_TO_5Y;
	}
	public void setR50_OVER3Y_TO_5Y(BigDecimal r50_OVER3Y_TO_5Y) {
		R50_OVER3Y_TO_5Y = r50_OVER3Y_TO_5Y;
	}
	public BigDecimal getR50_OVER5Y() {
		return R50_OVER5Y;
	}
	public void setR50_OVER5Y(BigDecimal r50_OVER5Y) {
		R50_OVER5Y = r50_OVER5Y;
	}
	public BigDecimal getR50_TOTAL() {
		return R50_TOTAL;
	}
	public void setR50_TOTAL(BigDecimal r50_TOTAL) {
		R50_TOTAL = r50_TOTAL;
	}
	public String getR51_PRODUCT() {
		return R51_PRODUCT;
	}
	public void setR51_PRODUCT(String r51_PRODUCT) {
		R51_PRODUCT = r51_PRODUCT;
	}
	public BigDecimal getR51_DAY1() {
		return R51_DAY1;
	}
	public void setR51_DAY1(BigDecimal r51_DAY1) {
		R51_DAY1 = r51_DAY1;
	}
	public BigDecimal getR51_DAY2_7() {
		return R51_DAY2_7;
	}
	public void setR51_DAY2_7(BigDecimal r51_DAY2_7) {
		R51_DAY2_7 = r51_DAY2_7;
	}
	public BigDecimal getR51_DAY8_14() {
		return R51_DAY8_14;
	}
	public void setR51_DAY8_14(BigDecimal r51_DAY8_14) {
		R51_DAY8_14 = r51_DAY8_14;
	}
	public BigDecimal getR51_DAY15_30() {
		return R51_DAY15_30;
	}
	public void setR51_DAY15_30(BigDecimal r51_DAY15_30) {
		R51_DAY15_30 = r51_DAY15_30;
	}
	public BigDecimal getR51_DAY31_TO_2M() {
		return R51_DAY31_TO_2M;
	}
	public void setR51_DAY31_TO_2M(BigDecimal r51_DAY31_TO_2M) {
		R51_DAY31_TO_2M = r51_DAY31_TO_2M;
	}
	public BigDecimal getR51_MORE2M_TO_3M() {
		return R51_MORE2M_TO_3M;
	}
	public void setR51_MORE2M_TO_3M(BigDecimal r51_MORE2M_TO_3M) {
		R51_MORE2M_TO_3M = r51_MORE2M_TO_3M;
	}
	public BigDecimal getR51_OVER3M_TO_6M() {
		return R51_OVER3M_TO_6M;
	}
	public void setR51_OVER3M_TO_6M(BigDecimal r51_OVER3M_TO_6M) {
		R51_OVER3M_TO_6M = r51_OVER3M_TO_6M;
	}
	public BigDecimal getR51_OVER6M_TO_1Y() {
		return R51_OVER6M_TO_1Y;
	}
	public void setR51_OVER6M_TO_1Y(BigDecimal r51_OVER6M_TO_1Y) {
		R51_OVER6M_TO_1Y = r51_OVER6M_TO_1Y;
	}
	public BigDecimal getR51_OVER1Y_TO_3Y() {
		return R51_OVER1Y_TO_3Y;
	}
	public void setR51_OVER1Y_TO_3Y(BigDecimal r51_OVER1Y_TO_3Y) {
		R51_OVER1Y_TO_3Y = r51_OVER1Y_TO_3Y;
	}
	public BigDecimal getR51_OVER3Y_TO_5Y() {
		return R51_OVER3Y_TO_5Y;
	}
	public void setR51_OVER3Y_TO_5Y(BigDecimal r51_OVER3Y_TO_5Y) {
		R51_OVER3Y_TO_5Y = r51_OVER3Y_TO_5Y;
	}
	public BigDecimal getR51_OVER5Y() {
		return R51_OVER5Y;
	}
	public void setR51_OVER5Y(BigDecimal r51_OVER5Y) {
		R51_OVER5Y = r51_OVER5Y;
	}
	public BigDecimal getR51_TOTAL() {
		return R51_TOTAL;
	}
	public void setR51_TOTAL(BigDecimal r51_TOTAL) {
		R51_TOTAL = r51_TOTAL;
	}
	public String getR52_PRODUCT() {
		return R52_PRODUCT;
	}
	public void setR52_PRODUCT(String r52_PRODUCT) {
		R52_PRODUCT = r52_PRODUCT;
	}
	public BigDecimal getR52_DAY1() {
		return R52_DAY1;
	}
	public void setR52_DAY1(BigDecimal r52_DAY1) {
		R52_DAY1 = r52_DAY1;
	}
	public BigDecimal getR52_DAY2_7() {
		return R52_DAY2_7;
	}
	public void setR52_DAY2_7(BigDecimal r52_DAY2_7) {
		R52_DAY2_7 = r52_DAY2_7;
	}
	public BigDecimal getR52_DAY8_14() {
		return R52_DAY8_14;
	}
	public void setR52_DAY8_14(BigDecimal r52_DAY8_14) {
		R52_DAY8_14 = r52_DAY8_14;
	}
	public BigDecimal getR52_DAY15_30() {
		return R52_DAY15_30;
	}
	public void setR52_DAY15_30(BigDecimal r52_DAY15_30) {
		R52_DAY15_30 = r52_DAY15_30;
	}
	public BigDecimal getR52_DAY31_TO_2M() {
		return R52_DAY31_TO_2M;
	}
	public void setR52_DAY31_TO_2M(BigDecimal r52_DAY31_TO_2M) {
		R52_DAY31_TO_2M = r52_DAY31_TO_2M;
	}
	public BigDecimal getR52_MORE2M_TO_3M() {
		return R52_MORE2M_TO_3M;
	}
	public void setR52_MORE2M_TO_3M(BigDecimal r52_MORE2M_TO_3M) {
		R52_MORE2M_TO_3M = r52_MORE2M_TO_3M;
	}
	public BigDecimal getR52_OVER3M_TO_6M() {
		return R52_OVER3M_TO_6M;
	}
	public void setR52_OVER3M_TO_6M(BigDecimal r52_OVER3M_TO_6M) {
		R52_OVER3M_TO_6M = r52_OVER3M_TO_6M;
	}
	public BigDecimal getR52_OVER6M_TO_1Y() {
		return R52_OVER6M_TO_1Y;
	}
	public void setR52_OVER6M_TO_1Y(BigDecimal r52_OVER6M_TO_1Y) {
		R52_OVER6M_TO_1Y = r52_OVER6M_TO_1Y;
	}
	public BigDecimal getR52_OVER1Y_TO_3Y() {
		return R52_OVER1Y_TO_3Y;
	}
	public void setR52_OVER1Y_TO_3Y(BigDecimal r52_OVER1Y_TO_3Y) {
		R52_OVER1Y_TO_3Y = r52_OVER1Y_TO_3Y;
	}
	public BigDecimal getR52_OVER3Y_TO_5Y() {
		return R52_OVER3Y_TO_5Y;
	}
	public void setR52_OVER3Y_TO_5Y(BigDecimal r52_OVER3Y_TO_5Y) {
		R52_OVER3Y_TO_5Y = r52_OVER3Y_TO_5Y;
	}
	public BigDecimal getR52_OVER5Y() {
		return R52_OVER5Y;
	}
	public void setR52_OVER5Y(BigDecimal r52_OVER5Y) {
		R52_OVER5Y = r52_OVER5Y;
	}
	public BigDecimal getR52_TOTAL() {
		return R52_TOTAL;
	}
	public void setR52_TOTAL(BigDecimal r52_TOTAL) {
		R52_TOTAL = r52_TOTAL;
	}
	public String getR53_PRODUCT() {
		return R53_PRODUCT;
	}
	public void setR53_PRODUCT(String r53_PRODUCT) {
		R53_PRODUCT = r53_PRODUCT;
	}
	public BigDecimal getR53_DAY1() {
		return R53_DAY1;
	}
	public void setR53_DAY1(BigDecimal r53_DAY1) {
		R53_DAY1 = r53_DAY1;
	}
	public BigDecimal getR53_DAY2_7() {
		return R53_DAY2_7;
	}
	public void setR53_DAY2_7(BigDecimal r53_DAY2_7) {
		R53_DAY2_7 = r53_DAY2_7;
	}
	public BigDecimal getR53_DAY8_14() {
		return R53_DAY8_14;
	}
	public void setR53_DAY8_14(BigDecimal r53_DAY8_14) {
		R53_DAY8_14 = r53_DAY8_14;
	}
	public BigDecimal getR53_DAY15_30() {
		return R53_DAY15_30;
	}
	public void setR53_DAY15_30(BigDecimal r53_DAY15_30) {
		R53_DAY15_30 = r53_DAY15_30;
	}
	public BigDecimal getR53_DAY31_TO_2M() {
		return R53_DAY31_TO_2M;
	}
	public void setR53_DAY31_TO_2M(BigDecimal r53_DAY31_TO_2M) {
		R53_DAY31_TO_2M = r53_DAY31_TO_2M;
	}
	public BigDecimal getR53_MORE2M_TO_3M() {
		return R53_MORE2M_TO_3M;
	}
	public void setR53_MORE2M_TO_3M(BigDecimal r53_MORE2M_TO_3M) {
		R53_MORE2M_TO_3M = r53_MORE2M_TO_3M;
	}
	public BigDecimal getR53_OVER3M_TO_6M() {
		return R53_OVER3M_TO_6M;
	}
	public void setR53_OVER3M_TO_6M(BigDecimal r53_OVER3M_TO_6M) {
		R53_OVER3M_TO_6M = r53_OVER3M_TO_6M;
	}
	public BigDecimal getR53_OVER6M_TO_1Y() {
		return R53_OVER6M_TO_1Y;
	}
	public void setR53_OVER6M_TO_1Y(BigDecimal r53_OVER6M_TO_1Y) {
		R53_OVER6M_TO_1Y = r53_OVER6M_TO_1Y;
	}
	public BigDecimal getR53_OVER1Y_TO_3Y() {
		return R53_OVER1Y_TO_3Y;
	}
	public void setR53_OVER1Y_TO_3Y(BigDecimal r53_OVER1Y_TO_3Y) {
		R53_OVER1Y_TO_3Y = r53_OVER1Y_TO_3Y;
	}
	public BigDecimal getR53_OVER3Y_TO_5Y() {
		return R53_OVER3Y_TO_5Y;
	}
	public void setR53_OVER3Y_TO_5Y(BigDecimal r53_OVER3Y_TO_5Y) {
		R53_OVER3Y_TO_5Y = r53_OVER3Y_TO_5Y;
	}
	public BigDecimal getR53_OVER5Y() {
		return R53_OVER5Y;
	}
	public void setR53_OVER5Y(BigDecimal r53_OVER5Y) {
		R53_OVER5Y = r53_OVER5Y;
	}
	public BigDecimal getR53_TOTAL() {
		return R53_TOTAL;
	}
	public void setR53_TOTAL(BigDecimal r53_TOTAL) {
		R53_TOTAL = r53_TOTAL;
	}
	public String getR54_PRODUCT() {
		return R54_PRODUCT;
	}
	public void setR54_PRODUCT(String r54_PRODUCT) {
		R54_PRODUCT = r54_PRODUCT;
	}
	public BigDecimal getR54_DAY1() {
		return R54_DAY1;
	}
	public void setR54_DAY1(BigDecimal r54_DAY1) {
		R54_DAY1 = r54_DAY1;
	}
	public BigDecimal getR54_DAY2_7() {
		return R54_DAY2_7;
	}
	public void setR54_DAY2_7(BigDecimal r54_DAY2_7) {
		R54_DAY2_7 = r54_DAY2_7;
	}
	public BigDecimal getR54_DAY8_14() {
		return R54_DAY8_14;
	}
	public void setR54_DAY8_14(BigDecimal r54_DAY8_14) {
		R54_DAY8_14 = r54_DAY8_14;
	}
	public BigDecimal getR54_DAY15_30() {
		return R54_DAY15_30;
	}
	public void setR54_DAY15_30(BigDecimal r54_DAY15_30) {
		R54_DAY15_30 = r54_DAY15_30;
	}
	public BigDecimal getR54_DAY31_TO_2M() {
		return R54_DAY31_TO_2M;
	}
	public void setR54_DAY31_TO_2M(BigDecimal r54_DAY31_TO_2M) {
		R54_DAY31_TO_2M = r54_DAY31_TO_2M;
	}
	public BigDecimal getR54_MORE2M_TO_3M() {
		return R54_MORE2M_TO_3M;
	}
	public void setR54_MORE2M_TO_3M(BigDecimal r54_MORE2M_TO_3M) {
		R54_MORE2M_TO_3M = r54_MORE2M_TO_3M;
	}
	public BigDecimal getR54_OVER3M_TO_6M() {
		return R54_OVER3M_TO_6M;
	}
	public void setR54_OVER3M_TO_6M(BigDecimal r54_OVER3M_TO_6M) {
		R54_OVER3M_TO_6M = r54_OVER3M_TO_6M;
	}
	public BigDecimal getR54_OVER6M_TO_1Y() {
		return R54_OVER6M_TO_1Y;
	}
	public void setR54_OVER6M_TO_1Y(BigDecimal r54_OVER6M_TO_1Y) {
		R54_OVER6M_TO_1Y = r54_OVER6M_TO_1Y;
	}
	public BigDecimal getR54_OVER1Y_TO_3Y() {
		return R54_OVER1Y_TO_3Y;
	}
	public void setR54_OVER1Y_TO_3Y(BigDecimal r54_OVER1Y_TO_3Y) {
		R54_OVER1Y_TO_3Y = r54_OVER1Y_TO_3Y;
	}
	public BigDecimal getR54_OVER3Y_TO_5Y() {
		return R54_OVER3Y_TO_5Y;
	}
	public void setR54_OVER3Y_TO_5Y(BigDecimal r54_OVER3Y_TO_5Y) {
		R54_OVER3Y_TO_5Y = r54_OVER3Y_TO_5Y;
	}
	public BigDecimal getR54_OVER5Y() {
		return R54_OVER5Y;
	}
	public void setR54_OVER5Y(BigDecimal r54_OVER5Y) {
		R54_OVER5Y = r54_OVER5Y;
	}
	public BigDecimal getR54_TOTAL() {
		return R54_TOTAL;
	}
	public void setR54_TOTAL(BigDecimal r54_TOTAL) {
		R54_TOTAL = r54_TOTAL;
	}
	public String getR55_PRODUCT() {
		return R55_PRODUCT;
	}
	public void setR55_PRODUCT(String r55_PRODUCT) {
		R55_PRODUCT = r55_PRODUCT;
	}
	public BigDecimal getR55_DAY1() {
		return R55_DAY1;
	}
	public void setR55_DAY1(BigDecimal r55_DAY1) {
		R55_DAY1 = r55_DAY1;
	}
	public BigDecimal getR55_DAY2_7() {
		return R55_DAY2_7;
	}
	public void setR55_DAY2_7(BigDecimal r55_DAY2_7) {
		R55_DAY2_7 = r55_DAY2_7;
	}
	public BigDecimal getR55_DAY8_14() {
		return R55_DAY8_14;
	}
	public void setR55_DAY8_14(BigDecimal r55_DAY8_14) {
		R55_DAY8_14 = r55_DAY8_14;
	}
	public BigDecimal getR55_DAY15_30() {
		return R55_DAY15_30;
	}
	public void setR55_DAY15_30(BigDecimal r55_DAY15_30) {
		R55_DAY15_30 = r55_DAY15_30;
	}
	public BigDecimal getR55_DAY31_TO_2M() {
		return R55_DAY31_TO_2M;
	}
	public void setR55_DAY31_TO_2M(BigDecimal r55_DAY31_TO_2M) {
		R55_DAY31_TO_2M = r55_DAY31_TO_2M;
	}
	public BigDecimal getR55_MORE2M_TO_3M() {
		return R55_MORE2M_TO_3M;
	}
	public void setR55_MORE2M_TO_3M(BigDecimal r55_MORE2M_TO_3M) {
		R55_MORE2M_TO_3M = r55_MORE2M_TO_3M;
	}
	public BigDecimal getR55_OVER3M_TO_6M() {
		return R55_OVER3M_TO_6M;
	}
	public void setR55_OVER3M_TO_6M(BigDecimal r55_OVER3M_TO_6M) {
		R55_OVER3M_TO_6M = r55_OVER3M_TO_6M;
	}
	public BigDecimal getR55_OVER6M_TO_1Y() {
		return R55_OVER6M_TO_1Y;
	}
	public void setR55_OVER6M_TO_1Y(BigDecimal r55_OVER6M_TO_1Y) {
		R55_OVER6M_TO_1Y = r55_OVER6M_TO_1Y;
	}
	public BigDecimal getR55_OVER1Y_TO_3Y() {
		return R55_OVER1Y_TO_3Y;
	}
	public void setR55_OVER1Y_TO_3Y(BigDecimal r55_OVER1Y_TO_3Y) {
		R55_OVER1Y_TO_3Y = r55_OVER1Y_TO_3Y;
	}
	public BigDecimal getR55_OVER3Y_TO_5Y() {
		return R55_OVER3Y_TO_5Y;
	}
	public void setR55_OVER3Y_TO_5Y(BigDecimal r55_OVER3Y_TO_5Y) {
		R55_OVER3Y_TO_5Y = r55_OVER3Y_TO_5Y;
	}
	public BigDecimal getR55_OVER5Y() {
		return R55_OVER5Y;
	}
	public void setR55_OVER5Y(BigDecimal r55_OVER5Y) {
		R55_OVER5Y = r55_OVER5Y;
	}
	public BigDecimal getR55_TOTAL() {
		return R55_TOTAL;
	}
	public void setR55_TOTAL(BigDecimal r55_TOTAL) {
		R55_TOTAL = r55_TOTAL;
	}
	public String getR56_PRODUCT() {
		return R56_PRODUCT;
	}
	public void setR56_PRODUCT(String r56_PRODUCT) {
		R56_PRODUCT = r56_PRODUCT;
	}
	public BigDecimal getR56_DAY1() {
		return R56_DAY1;
	}
	public void setR56_DAY1(BigDecimal r56_DAY1) {
		R56_DAY1 = r56_DAY1;
	}
	public BigDecimal getR56_DAY2_7() {
		return R56_DAY2_7;
	}
	public void setR56_DAY2_7(BigDecimal r56_DAY2_7) {
		R56_DAY2_7 = r56_DAY2_7;
	}
	public BigDecimal getR56_DAY8_14() {
		return R56_DAY8_14;
	}
	public void setR56_DAY8_14(BigDecimal r56_DAY8_14) {
		R56_DAY8_14 = r56_DAY8_14;
	}
	public BigDecimal getR56_DAY15_30() {
		return R56_DAY15_30;
	}
	public void setR56_DAY15_30(BigDecimal r56_DAY15_30) {
		R56_DAY15_30 = r56_DAY15_30;
	}
	public BigDecimal getR56_DAY31_TO_2M() {
		return R56_DAY31_TO_2M;
	}
	public void setR56_DAY31_TO_2M(BigDecimal r56_DAY31_TO_2M) {
		R56_DAY31_TO_2M = r56_DAY31_TO_2M;
	}
	public BigDecimal getR56_MORE2M_TO_3M() {
		return R56_MORE2M_TO_3M;
	}
	public void setR56_MORE2M_TO_3M(BigDecimal r56_MORE2M_TO_3M) {
		R56_MORE2M_TO_3M = r56_MORE2M_TO_3M;
	}
	public BigDecimal getR56_OVER3M_TO_6M() {
		return R56_OVER3M_TO_6M;
	}
	public void setR56_OVER3M_TO_6M(BigDecimal r56_OVER3M_TO_6M) {
		R56_OVER3M_TO_6M = r56_OVER3M_TO_6M;
	}
	public BigDecimal getR56_OVER6M_TO_1Y() {
		return R56_OVER6M_TO_1Y;
	}
	public void setR56_OVER6M_TO_1Y(BigDecimal r56_OVER6M_TO_1Y) {
		R56_OVER6M_TO_1Y = r56_OVER6M_TO_1Y;
	}
	public BigDecimal getR56_OVER1Y_TO_3Y() {
		return R56_OVER1Y_TO_3Y;
	}
	public void setR56_OVER1Y_TO_3Y(BigDecimal r56_OVER1Y_TO_3Y) {
		R56_OVER1Y_TO_3Y = r56_OVER1Y_TO_3Y;
	}
	public BigDecimal getR56_OVER3Y_TO_5Y() {
		return R56_OVER3Y_TO_5Y;
	}
	public void setR56_OVER3Y_TO_5Y(BigDecimal r56_OVER3Y_TO_5Y) {
		R56_OVER3Y_TO_5Y = r56_OVER3Y_TO_5Y;
	}
	public BigDecimal getR56_OVER5Y() {
		return R56_OVER5Y;
	}
	public void setR56_OVER5Y(BigDecimal r56_OVER5Y) {
		R56_OVER5Y = r56_OVER5Y;
	}
	public BigDecimal getR56_TOTAL() {
		return R56_TOTAL;
	}
	public void setR56_TOTAL(BigDecimal r56_TOTAL) {
		R56_TOTAL = r56_TOTAL;
	}
	public String getR57_PRODUCT() {
		return R57_PRODUCT;
	}
	public void setR57_PRODUCT(String r57_PRODUCT) {
		R57_PRODUCT = r57_PRODUCT;
	}
	public BigDecimal getR57_DAY1() {
		return R57_DAY1;
	}
	public void setR57_DAY1(BigDecimal r57_DAY1) {
		R57_DAY1 = r57_DAY1;
	}
	public BigDecimal getR57_DAY2_7() {
		return R57_DAY2_7;
	}
	public void setR57_DAY2_7(BigDecimal r57_DAY2_7) {
		R57_DAY2_7 = r57_DAY2_7;
	}
	public BigDecimal getR57_DAY8_14() {
		return R57_DAY8_14;
	}
	public void setR57_DAY8_14(BigDecimal r57_DAY8_14) {
		R57_DAY8_14 = r57_DAY8_14;
	}
	public BigDecimal getR57_DAY15_30() {
		return R57_DAY15_30;
	}
	public void setR57_DAY15_30(BigDecimal r57_DAY15_30) {
		R57_DAY15_30 = r57_DAY15_30;
	}
	public BigDecimal getR57_DAY31_TO_2M() {
		return R57_DAY31_TO_2M;
	}
	public void setR57_DAY31_TO_2M(BigDecimal r57_DAY31_TO_2M) {
		R57_DAY31_TO_2M = r57_DAY31_TO_2M;
	}
	public BigDecimal getR57_MORE2M_TO_3M() {
		return R57_MORE2M_TO_3M;
	}
	public void setR57_MORE2M_TO_3M(BigDecimal r57_MORE2M_TO_3M) {
		R57_MORE2M_TO_3M = r57_MORE2M_TO_3M;
	}
	public BigDecimal getR57_OVER3M_TO_6M() {
		return R57_OVER3M_TO_6M;
	}
	public void setR57_OVER3M_TO_6M(BigDecimal r57_OVER3M_TO_6M) {
		R57_OVER3M_TO_6M = r57_OVER3M_TO_6M;
	}
	public BigDecimal getR57_OVER6M_TO_1Y() {
		return R57_OVER6M_TO_1Y;
	}
	public void setR57_OVER6M_TO_1Y(BigDecimal r57_OVER6M_TO_1Y) {
		R57_OVER6M_TO_1Y = r57_OVER6M_TO_1Y;
	}
	public BigDecimal getR57_OVER1Y_TO_3Y() {
		return R57_OVER1Y_TO_3Y;
	}
	public void setR57_OVER1Y_TO_3Y(BigDecimal r57_OVER1Y_TO_3Y) {
		R57_OVER1Y_TO_3Y = r57_OVER1Y_TO_3Y;
	}
	public BigDecimal getR57_OVER3Y_TO_5Y() {
		return R57_OVER3Y_TO_5Y;
	}
	public void setR57_OVER3Y_TO_5Y(BigDecimal r57_OVER3Y_TO_5Y) {
		R57_OVER3Y_TO_5Y = r57_OVER3Y_TO_5Y;
	}
	public BigDecimal getR57_OVER5Y() {
		return R57_OVER5Y;
	}
	public void setR57_OVER5Y(BigDecimal r57_OVER5Y) {
		R57_OVER5Y = r57_OVER5Y;
	}
	public BigDecimal getR57_TOTAL() {
		return R57_TOTAL;
	}
	public void setR57_TOTAL(BigDecimal r57_TOTAL) {
		R57_TOTAL = r57_TOTAL;
	}
	public String getR58_PRODUCT() {
		return R58_PRODUCT;
	}
	public void setR58_PRODUCT(String r58_PRODUCT) {
		R58_PRODUCT = r58_PRODUCT;
	}
	public BigDecimal getR58_DAY1() {
		return R58_DAY1;
	}
	public void setR58_DAY1(BigDecimal r58_DAY1) {
		R58_DAY1 = r58_DAY1;
	}
	public BigDecimal getR58_DAY2_7() {
		return R58_DAY2_7;
	}
	public void setR58_DAY2_7(BigDecimal r58_DAY2_7) {
		R58_DAY2_7 = r58_DAY2_7;
	}
	public BigDecimal getR58_DAY8_14() {
		return R58_DAY8_14;
	}
	public void setR58_DAY8_14(BigDecimal r58_DAY8_14) {
		R58_DAY8_14 = r58_DAY8_14;
	}
	public BigDecimal getR58_DAY15_30() {
		return R58_DAY15_30;
	}
	public void setR58_DAY15_30(BigDecimal r58_DAY15_30) {
		R58_DAY15_30 = r58_DAY15_30;
	}
	public BigDecimal getR58_DAY31_TO_2M() {
		return R58_DAY31_TO_2M;
	}
	public void setR58_DAY31_TO_2M(BigDecimal r58_DAY31_TO_2M) {
		R58_DAY31_TO_2M = r58_DAY31_TO_2M;
	}
	public BigDecimal getR58_MORE2M_TO_3M() {
		return R58_MORE2M_TO_3M;
	}
	public void setR58_MORE2M_TO_3M(BigDecimal r58_MORE2M_TO_3M) {
		R58_MORE2M_TO_3M = r58_MORE2M_TO_3M;
	}
	public BigDecimal getR58_OVER3M_TO_6M() {
		return R58_OVER3M_TO_6M;
	}
	public void setR58_OVER3M_TO_6M(BigDecimal r58_OVER3M_TO_6M) {
		R58_OVER3M_TO_6M = r58_OVER3M_TO_6M;
	}
	public BigDecimal getR58_OVER6M_TO_1Y() {
		return R58_OVER6M_TO_1Y;
	}
	public void setR58_OVER6M_TO_1Y(BigDecimal r58_OVER6M_TO_1Y) {
		R58_OVER6M_TO_1Y = r58_OVER6M_TO_1Y;
	}
	public BigDecimal getR58_OVER1Y_TO_3Y() {
		return R58_OVER1Y_TO_3Y;
	}
	public void setR58_OVER1Y_TO_3Y(BigDecimal r58_OVER1Y_TO_3Y) {
		R58_OVER1Y_TO_3Y = r58_OVER1Y_TO_3Y;
	}
	public BigDecimal getR58_OVER3Y_TO_5Y() {
		return R58_OVER3Y_TO_5Y;
	}
	public void setR58_OVER3Y_TO_5Y(BigDecimal r58_OVER3Y_TO_5Y) {
		R58_OVER3Y_TO_5Y = r58_OVER3Y_TO_5Y;
	}
	public BigDecimal getR58_OVER5Y() {
		return R58_OVER5Y;
	}
	public void setR58_OVER5Y(BigDecimal r58_OVER5Y) {
		R58_OVER5Y = r58_OVER5Y;
	}
	public BigDecimal getR58_TOTAL() {
		return R58_TOTAL;
	}
	public void setR58_TOTAL(BigDecimal r58_TOTAL) {
		R58_TOTAL = r58_TOTAL;
	}
	public String getR59_PRODUCT() {
		return R59_PRODUCT;
	}
	public void setR59_PRODUCT(String r59_PRODUCT) {
		R59_PRODUCT = r59_PRODUCT;
	}
	public BigDecimal getR59_DAY1() {
		return R59_DAY1;
	}
	public void setR59_DAY1(BigDecimal r59_DAY1) {
		R59_DAY1 = r59_DAY1;
	}
	public BigDecimal getR59_DAY2_7() {
		return R59_DAY2_7;
	}
	public void setR59_DAY2_7(BigDecimal r59_DAY2_7) {
		R59_DAY2_7 = r59_DAY2_7;
	}
	public BigDecimal getR59_DAY8_14() {
		return R59_DAY8_14;
	}
	public void setR59_DAY8_14(BigDecimal r59_DAY8_14) {
		R59_DAY8_14 = r59_DAY8_14;
	}
	public BigDecimal getR59_DAY15_30() {
		return R59_DAY15_30;
	}
	public void setR59_DAY15_30(BigDecimal r59_DAY15_30) {
		R59_DAY15_30 = r59_DAY15_30;
	}
	public BigDecimal getR59_DAY31_TO_2M() {
		return R59_DAY31_TO_2M;
	}
	public void setR59_DAY31_TO_2M(BigDecimal r59_DAY31_TO_2M) {
		R59_DAY31_TO_2M = r59_DAY31_TO_2M;
	}
	public BigDecimal getR59_MORE2M_TO_3M() {
		return R59_MORE2M_TO_3M;
	}
	public void setR59_MORE2M_TO_3M(BigDecimal r59_MORE2M_TO_3M) {
		R59_MORE2M_TO_3M = r59_MORE2M_TO_3M;
	}
	public BigDecimal getR59_OVER3M_TO_6M() {
		return R59_OVER3M_TO_6M;
	}
	public void setR59_OVER3M_TO_6M(BigDecimal r59_OVER3M_TO_6M) {
		R59_OVER3M_TO_6M = r59_OVER3M_TO_6M;
	}
	public BigDecimal getR59_OVER6M_TO_1Y() {
		return R59_OVER6M_TO_1Y;
	}
	public void setR59_OVER6M_TO_1Y(BigDecimal r59_OVER6M_TO_1Y) {
		R59_OVER6M_TO_1Y = r59_OVER6M_TO_1Y;
	}
	public BigDecimal getR59_OVER1Y_TO_3Y() {
		return R59_OVER1Y_TO_3Y;
	}
	public void setR59_OVER1Y_TO_3Y(BigDecimal r59_OVER1Y_TO_3Y) {
		R59_OVER1Y_TO_3Y = r59_OVER1Y_TO_3Y;
	}
	public BigDecimal getR59_OVER3Y_TO_5Y() {
		return R59_OVER3Y_TO_5Y;
	}
	public void setR59_OVER3Y_TO_5Y(BigDecimal r59_OVER3Y_TO_5Y) {
		R59_OVER3Y_TO_5Y = r59_OVER3Y_TO_5Y;
	}
	public BigDecimal getR59_OVER5Y() {
		return R59_OVER5Y;
	}
	public void setR59_OVER5Y(BigDecimal r59_OVER5Y) {
		R59_OVER5Y = r59_OVER5Y;
	}
	public BigDecimal getR59_TOTAL() {
		return R59_TOTAL;
	}
	public void setR59_TOTAL(BigDecimal r59_TOTAL) {
		R59_TOTAL = r59_TOTAL;
	}
	public String getR60_PRODUCT() {
		return R60_PRODUCT;
	}
	public void setR60_PRODUCT(String r60_PRODUCT) {
		R60_PRODUCT = r60_PRODUCT;
	}
	public BigDecimal getR60_DAY1() {
		return R60_DAY1;
	}
	public void setR60_DAY1(BigDecimal r60_DAY1) {
		R60_DAY1 = r60_DAY1;
	}
	public BigDecimal getR60_DAY2_7() {
		return R60_DAY2_7;
	}
	public void setR60_DAY2_7(BigDecimal r60_DAY2_7) {
		R60_DAY2_7 = r60_DAY2_7;
	}
	public BigDecimal getR60_DAY8_14() {
		return R60_DAY8_14;
	}
	public void setR60_DAY8_14(BigDecimal r60_DAY8_14) {
		R60_DAY8_14 = r60_DAY8_14;
	}
	public BigDecimal getR60_DAY15_30() {
		return R60_DAY15_30;
	}
	public void setR60_DAY15_30(BigDecimal r60_DAY15_30) {
		R60_DAY15_30 = r60_DAY15_30;
	}
	public BigDecimal getR60_DAY31_TO_2M() {
		return R60_DAY31_TO_2M;
	}
	public void setR60_DAY31_TO_2M(BigDecimal r60_DAY31_TO_2M) {
		R60_DAY31_TO_2M = r60_DAY31_TO_2M;
	}
	public BigDecimal getR60_MORE2M_TO_3M() {
		return R60_MORE2M_TO_3M;
	}
	public void setR60_MORE2M_TO_3M(BigDecimal r60_MORE2M_TO_3M) {
		R60_MORE2M_TO_3M = r60_MORE2M_TO_3M;
	}
	public BigDecimal getR60_OVER3M_TO_6M() {
		return R60_OVER3M_TO_6M;
	}
	public void setR60_OVER3M_TO_6M(BigDecimal r60_OVER3M_TO_6M) {
		R60_OVER3M_TO_6M = r60_OVER3M_TO_6M;
	}
	public BigDecimal getR60_OVER6M_TO_1Y() {
		return R60_OVER6M_TO_1Y;
	}
	public void setR60_OVER6M_TO_1Y(BigDecimal r60_OVER6M_TO_1Y) {
		R60_OVER6M_TO_1Y = r60_OVER6M_TO_1Y;
	}
	public BigDecimal getR60_OVER1Y_TO_3Y() {
		return R60_OVER1Y_TO_3Y;
	}
	public void setR60_OVER1Y_TO_3Y(BigDecimal r60_OVER1Y_TO_3Y) {
		R60_OVER1Y_TO_3Y = r60_OVER1Y_TO_3Y;
	}
	public BigDecimal getR60_OVER3Y_TO_5Y() {
		return R60_OVER3Y_TO_5Y;
	}
	public void setR60_OVER3Y_TO_5Y(BigDecimal r60_OVER3Y_TO_5Y) {
		R60_OVER3Y_TO_5Y = r60_OVER3Y_TO_5Y;
	}
	public BigDecimal getR60_OVER5Y() {
		return R60_OVER5Y;
	}
	public void setR60_OVER5Y(BigDecimal r60_OVER5Y) {
		R60_OVER5Y = r60_OVER5Y;
	}
	public BigDecimal getR60_TOTAL() {
		return R60_TOTAL;
	}
	public void setR60_TOTAL(BigDecimal r60_TOTAL) {
		R60_TOTAL = r60_TOTAL;
	}
	public String getR61_PRODUCT() {
		return R61_PRODUCT;
	}
	public void setR61_PRODUCT(String r61_PRODUCT) {
		R61_PRODUCT = r61_PRODUCT;
	}
	public BigDecimal getR61_DAY1() {
		return R61_DAY1;
	}
	public void setR61_DAY1(BigDecimal r61_DAY1) {
		R61_DAY1 = r61_DAY1;
	}
	public BigDecimal getR61_DAY2_7() {
		return R61_DAY2_7;
	}
	public void setR61_DAY2_7(BigDecimal r61_DAY2_7) {
		R61_DAY2_7 = r61_DAY2_7;
	}
	public BigDecimal getR61_DAY8_14() {
		return R61_DAY8_14;
	}
	public void setR61_DAY8_14(BigDecimal r61_DAY8_14) {
		R61_DAY8_14 = r61_DAY8_14;
	}
	public BigDecimal getR61_DAY15_30() {
		return R61_DAY15_30;
	}
	public void setR61_DAY15_30(BigDecimal r61_DAY15_30) {
		R61_DAY15_30 = r61_DAY15_30;
	}
	public BigDecimal getR61_DAY31_TO_2M() {
		return R61_DAY31_TO_2M;
	}
	public void setR61_DAY31_TO_2M(BigDecimal r61_DAY31_TO_2M) {
		R61_DAY31_TO_2M = r61_DAY31_TO_2M;
	}
	public BigDecimal getR61_MORE2M_TO_3M() {
		return R61_MORE2M_TO_3M;
	}
	public void setR61_MORE2M_TO_3M(BigDecimal r61_MORE2M_TO_3M) {
		R61_MORE2M_TO_3M = r61_MORE2M_TO_3M;
	}
	public BigDecimal getR61_OVER3M_TO_6M() {
		return R61_OVER3M_TO_6M;
	}
	public void setR61_OVER3M_TO_6M(BigDecimal r61_OVER3M_TO_6M) {
		R61_OVER3M_TO_6M = r61_OVER3M_TO_6M;
	}
	public BigDecimal getR61_OVER6M_TO_1Y() {
		return R61_OVER6M_TO_1Y;
	}
	public void setR61_OVER6M_TO_1Y(BigDecimal r61_OVER6M_TO_1Y) {
		R61_OVER6M_TO_1Y = r61_OVER6M_TO_1Y;
	}
	public BigDecimal getR61_OVER1Y_TO_3Y() {
		return R61_OVER1Y_TO_3Y;
	}
	public void setR61_OVER1Y_TO_3Y(BigDecimal r61_OVER1Y_TO_3Y) {
		R61_OVER1Y_TO_3Y = r61_OVER1Y_TO_3Y;
	}
	public BigDecimal getR61_OVER3Y_TO_5Y() {
		return R61_OVER3Y_TO_5Y;
	}
	public void setR61_OVER3Y_TO_5Y(BigDecimal r61_OVER3Y_TO_5Y) {
		R61_OVER3Y_TO_5Y = r61_OVER3Y_TO_5Y;
	}
	public BigDecimal getR61_OVER5Y() {
		return R61_OVER5Y;
	}
	public void setR61_OVER5Y(BigDecimal r61_OVER5Y) {
		R61_OVER5Y = r61_OVER5Y;
	}
	public BigDecimal getR61_TOTAL() {
		return R61_TOTAL;
	}
	public void setR61_TOTAL(BigDecimal r61_TOTAL) {
		R61_TOTAL = r61_TOTAL;
	}
	public String getR62_PRODUCT() {
		return R62_PRODUCT;
	}
	public void setR62_PRODUCT(String r62_PRODUCT) {
		R62_PRODUCT = r62_PRODUCT;
	}
	public BigDecimal getR62_DAY1() {
		return R62_DAY1;
	}
	public void setR62_DAY1(BigDecimal r62_DAY1) {
		R62_DAY1 = r62_DAY1;
	}
	public BigDecimal getR62_DAY2_7() {
		return R62_DAY2_7;
	}
	public void setR62_DAY2_7(BigDecimal r62_DAY2_7) {
		R62_DAY2_7 = r62_DAY2_7;
	}
	public BigDecimal getR62_DAY8_14() {
		return R62_DAY8_14;
	}
	public void setR62_DAY8_14(BigDecimal r62_DAY8_14) {
		R62_DAY8_14 = r62_DAY8_14;
	}
	public BigDecimal getR62_DAY15_30() {
		return R62_DAY15_30;
	}
	public void setR62_DAY15_30(BigDecimal r62_DAY15_30) {
		R62_DAY15_30 = r62_DAY15_30;
	}
	public BigDecimal getR62_DAY31_TO_2M() {
		return R62_DAY31_TO_2M;
	}
	public void setR62_DAY31_TO_2M(BigDecimal r62_DAY31_TO_2M) {
		R62_DAY31_TO_2M = r62_DAY31_TO_2M;
	}
	public BigDecimal getR62_MORE2M_TO_3M() {
		return R62_MORE2M_TO_3M;
	}
	public void setR62_MORE2M_TO_3M(BigDecimal r62_MORE2M_TO_3M) {
		R62_MORE2M_TO_3M = r62_MORE2M_TO_3M;
	}
	public BigDecimal getR62_OVER3M_TO_6M() {
		return R62_OVER3M_TO_6M;
	}
	public void setR62_OVER3M_TO_6M(BigDecimal r62_OVER3M_TO_6M) {
		R62_OVER3M_TO_6M = r62_OVER3M_TO_6M;
	}
	public BigDecimal getR62_OVER6M_TO_1Y() {
		return R62_OVER6M_TO_1Y;
	}
	public void setR62_OVER6M_TO_1Y(BigDecimal r62_OVER6M_TO_1Y) {
		R62_OVER6M_TO_1Y = r62_OVER6M_TO_1Y;
	}
	public BigDecimal getR62_OVER1Y_TO_3Y() {
		return R62_OVER1Y_TO_3Y;
	}
	public void setR62_OVER1Y_TO_3Y(BigDecimal r62_OVER1Y_TO_3Y) {
		R62_OVER1Y_TO_3Y = r62_OVER1Y_TO_3Y;
	}
	public BigDecimal getR62_OVER3Y_TO_5Y() {
		return R62_OVER3Y_TO_5Y;
	}
	public void setR62_OVER3Y_TO_5Y(BigDecimal r62_OVER3Y_TO_5Y) {
		R62_OVER3Y_TO_5Y = r62_OVER3Y_TO_5Y;
	}
	public BigDecimal getR62_OVER5Y() {
		return R62_OVER5Y;
	}
	public void setR62_OVER5Y(BigDecimal r62_OVER5Y) {
		R62_OVER5Y = r62_OVER5Y;
	}
	public BigDecimal getR62_TOTAL() {
		return R62_TOTAL;
	}
	public void setR62_TOTAL(BigDecimal r62_TOTAL) {
		R62_TOTAL = r62_TOTAL;
	}
	public String getR63_PRODUCT() {
		return R63_PRODUCT;
	}
	public void setR63_PRODUCT(String r63_PRODUCT) {
		R63_PRODUCT = r63_PRODUCT;
	}
	public BigDecimal getR63_DAY1() {
		return R63_DAY1;
	}
	public void setR63_DAY1(BigDecimal r63_DAY1) {
		R63_DAY1 = r63_DAY1;
	}
	public BigDecimal getR63_DAY2_7() {
		return R63_DAY2_7;
	}
	public void setR63_DAY2_7(BigDecimal r63_DAY2_7) {
		R63_DAY2_7 = r63_DAY2_7;
	}
	public BigDecimal getR63_DAY8_14() {
		return R63_DAY8_14;
	}
	public void setR63_DAY8_14(BigDecimal r63_DAY8_14) {
		R63_DAY8_14 = r63_DAY8_14;
	}
	public BigDecimal getR63_DAY15_30() {
		return R63_DAY15_30;
	}
	public void setR63_DAY15_30(BigDecimal r63_DAY15_30) {
		R63_DAY15_30 = r63_DAY15_30;
	}
	public BigDecimal getR63_DAY31_TO_2M() {
		return R63_DAY31_TO_2M;
	}
	public void setR63_DAY31_TO_2M(BigDecimal r63_DAY31_TO_2M) {
		R63_DAY31_TO_2M = r63_DAY31_TO_2M;
	}
	public BigDecimal getR63_MORE2M_TO_3M() {
		return R63_MORE2M_TO_3M;
	}
	public void setR63_MORE2M_TO_3M(BigDecimal r63_MORE2M_TO_3M) {
		R63_MORE2M_TO_3M = r63_MORE2M_TO_3M;
	}
	public BigDecimal getR63_OVER3M_TO_6M() {
		return R63_OVER3M_TO_6M;
	}
	public void setR63_OVER3M_TO_6M(BigDecimal r63_OVER3M_TO_6M) {
		R63_OVER3M_TO_6M = r63_OVER3M_TO_6M;
	}
	public BigDecimal getR63_OVER6M_TO_1Y() {
		return R63_OVER6M_TO_1Y;
	}
	public void setR63_OVER6M_TO_1Y(BigDecimal r63_OVER6M_TO_1Y) {
		R63_OVER6M_TO_1Y = r63_OVER6M_TO_1Y;
	}
	public BigDecimal getR63_OVER1Y_TO_3Y() {
		return R63_OVER1Y_TO_3Y;
	}
	public void setR63_OVER1Y_TO_3Y(BigDecimal r63_OVER1Y_TO_3Y) {
		R63_OVER1Y_TO_3Y = r63_OVER1Y_TO_3Y;
	}
	public BigDecimal getR63_OVER3Y_TO_5Y() {
		return R63_OVER3Y_TO_5Y;
	}
	public void setR63_OVER3Y_TO_5Y(BigDecimal r63_OVER3Y_TO_5Y) {
		R63_OVER3Y_TO_5Y = r63_OVER3Y_TO_5Y;
	}
	public BigDecimal getR63_OVER5Y() {
		return R63_OVER5Y;
	}
	public void setR63_OVER5Y(BigDecimal r63_OVER5Y) {
		R63_OVER5Y = r63_OVER5Y;
	}
	public BigDecimal getR63_TOTAL() {
		return R63_TOTAL;
	}
	public void setR63_TOTAL(BigDecimal r63_TOTAL) {
		R63_TOTAL = r63_TOTAL;
	}
	public String getR64_PRODUCT() {
		return R64_PRODUCT;
	}
	public void setR64_PRODUCT(String r64_PRODUCT) {
		R64_PRODUCT = r64_PRODUCT;
	}
	public BigDecimal getR64_DAY1() {
		return R64_DAY1;
	}
	public void setR64_DAY1(BigDecimal r64_DAY1) {
		R64_DAY1 = r64_DAY1;
	}
	public BigDecimal getR64_DAY2_7() {
		return R64_DAY2_7;
	}
	public void setR64_DAY2_7(BigDecimal r64_DAY2_7) {
		R64_DAY2_7 = r64_DAY2_7;
	}
	public BigDecimal getR64_DAY8_14() {
		return R64_DAY8_14;
	}
	public void setR64_DAY8_14(BigDecimal r64_DAY8_14) {
		R64_DAY8_14 = r64_DAY8_14;
	}
	public BigDecimal getR64_DAY15_30() {
		return R64_DAY15_30;
	}
	public void setR64_DAY15_30(BigDecimal r64_DAY15_30) {
		R64_DAY15_30 = r64_DAY15_30;
	}
	public BigDecimal getR64_DAY31_TO_2M() {
		return R64_DAY31_TO_2M;
	}
	public void setR64_DAY31_TO_2M(BigDecimal r64_DAY31_TO_2M) {
		R64_DAY31_TO_2M = r64_DAY31_TO_2M;
	}
	public BigDecimal getR64_MORE2M_TO_3M() {
		return R64_MORE2M_TO_3M;
	}
	public void setR64_MORE2M_TO_3M(BigDecimal r64_MORE2M_TO_3M) {
		R64_MORE2M_TO_3M = r64_MORE2M_TO_3M;
	}
	public BigDecimal getR64_OVER3M_TO_6M() {
		return R64_OVER3M_TO_6M;
	}
	public void setR64_OVER3M_TO_6M(BigDecimal r64_OVER3M_TO_6M) {
		R64_OVER3M_TO_6M = r64_OVER3M_TO_6M;
	}
	public BigDecimal getR64_OVER6M_TO_1Y() {
		return R64_OVER6M_TO_1Y;
	}
	public void setR64_OVER6M_TO_1Y(BigDecimal r64_OVER6M_TO_1Y) {
		R64_OVER6M_TO_1Y = r64_OVER6M_TO_1Y;
	}
	public BigDecimal getR64_OVER1Y_TO_3Y() {
		return R64_OVER1Y_TO_3Y;
	}
	public void setR64_OVER1Y_TO_3Y(BigDecimal r64_OVER1Y_TO_3Y) {
		R64_OVER1Y_TO_3Y = r64_OVER1Y_TO_3Y;
	}
	public BigDecimal getR64_OVER3Y_TO_5Y() {
		return R64_OVER3Y_TO_5Y;
	}
	public void setR64_OVER3Y_TO_5Y(BigDecimal r64_OVER3Y_TO_5Y) {
		R64_OVER3Y_TO_5Y = r64_OVER3Y_TO_5Y;
	}
	public BigDecimal getR64_OVER5Y() {
		return R64_OVER5Y;
	}
	public void setR64_OVER5Y(BigDecimal r64_OVER5Y) {
		R64_OVER5Y = r64_OVER5Y;
	}
	public BigDecimal getR64_TOTAL() {
		return R64_TOTAL;
	}
	public void setR64_TOTAL(BigDecimal r64_TOTAL) {
		R64_TOTAL = r64_TOTAL;
	}
	public String getR65_PRODUCT() {
		return R65_PRODUCT;
	}
	public void setR65_PRODUCT(String r65_PRODUCT) {
		R65_PRODUCT = r65_PRODUCT;
	}
	public BigDecimal getR65_DAY1() {
		return R65_DAY1;
	}
	public void setR65_DAY1(BigDecimal r65_DAY1) {
		R65_DAY1 = r65_DAY1;
	}
	public BigDecimal getR65_DAY2_7() {
		return R65_DAY2_7;
	}
	public void setR65_DAY2_7(BigDecimal r65_DAY2_7) {
		R65_DAY2_7 = r65_DAY2_7;
	}
	public BigDecimal getR65_DAY8_14() {
		return R65_DAY8_14;
	}
	public void setR65_DAY8_14(BigDecimal r65_DAY8_14) {
		R65_DAY8_14 = r65_DAY8_14;
	}
	public BigDecimal getR65_DAY15_30() {
		return R65_DAY15_30;
	}
	public void setR65_DAY15_30(BigDecimal r65_DAY15_30) {
		R65_DAY15_30 = r65_DAY15_30;
	}
	public BigDecimal getR65_DAY31_TO_2M() {
		return R65_DAY31_TO_2M;
	}
	public void setR65_DAY31_TO_2M(BigDecimal r65_DAY31_TO_2M) {
		R65_DAY31_TO_2M = r65_DAY31_TO_2M;
	}
	public BigDecimal getR65_MORE2M_TO_3M() {
		return R65_MORE2M_TO_3M;
	}
	public void setR65_MORE2M_TO_3M(BigDecimal r65_MORE2M_TO_3M) {
		R65_MORE2M_TO_3M = r65_MORE2M_TO_3M;
	}
	public BigDecimal getR65_OVER3M_TO_6M() {
		return R65_OVER3M_TO_6M;
	}
	public void setR65_OVER3M_TO_6M(BigDecimal r65_OVER3M_TO_6M) {
		R65_OVER3M_TO_6M = r65_OVER3M_TO_6M;
	}
	public BigDecimal getR65_OVER6M_TO_1Y() {
		return R65_OVER6M_TO_1Y;
	}
	public void setR65_OVER6M_TO_1Y(BigDecimal r65_OVER6M_TO_1Y) {
		R65_OVER6M_TO_1Y = r65_OVER6M_TO_1Y;
	}
	public BigDecimal getR65_OVER1Y_TO_3Y() {
		return R65_OVER1Y_TO_3Y;
	}
	public void setR65_OVER1Y_TO_3Y(BigDecimal r65_OVER1Y_TO_3Y) {
		R65_OVER1Y_TO_3Y = r65_OVER1Y_TO_3Y;
	}
	public BigDecimal getR65_OVER3Y_TO_5Y() {
		return R65_OVER3Y_TO_5Y;
	}
	public void setR65_OVER3Y_TO_5Y(BigDecimal r65_OVER3Y_TO_5Y) {
		R65_OVER3Y_TO_5Y = r65_OVER3Y_TO_5Y;
	}
	public BigDecimal getR65_OVER5Y() {
		return R65_OVER5Y;
	}
	public void setR65_OVER5Y(BigDecimal r65_OVER5Y) {
		R65_OVER5Y = r65_OVER5Y;
	}
	public BigDecimal getR65_TOTAL() {
		return R65_TOTAL;
	}
	public void setR65_TOTAL(BigDecimal r65_TOTAL) {
		R65_TOTAL = r65_TOTAL;
	}
	public String getR66_PRODUCT() {
		return R66_PRODUCT;
	}
	public void setR66_PRODUCT(String r66_PRODUCT) {
		R66_PRODUCT = r66_PRODUCT;
	}
	public BigDecimal getR66_DAY1() {
		return R66_DAY1;
	}
	public void setR66_DAY1(BigDecimal r66_DAY1) {
		R66_DAY1 = r66_DAY1;
	}
	public BigDecimal getR66_DAY2_7() {
		return R66_DAY2_7;
	}
	public void setR66_DAY2_7(BigDecimal r66_DAY2_7) {
		R66_DAY2_7 = r66_DAY2_7;
	}
	public BigDecimal getR66_DAY8_14() {
		return R66_DAY8_14;
	}
	public void setR66_DAY8_14(BigDecimal r66_DAY8_14) {
		R66_DAY8_14 = r66_DAY8_14;
	}
	public BigDecimal getR66_DAY15_30() {
		return R66_DAY15_30;
	}
	public void setR66_DAY15_30(BigDecimal r66_DAY15_30) {
		R66_DAY15_30 = r66_DAY15_30;
	}
	public BigDecimal getR66_DAY31_TO_2M() {
		return R66_DAY31_TO_2M;
	}
	public void setR66_DAY31_TO_2M(BigDecimal r66_DAY31_TO_2M) {
		R66_DAY31_TO_2M = r66_DAY31_TO_2M;
	}
	public BigDecimal getR66_MORE2M_TO_3M() {
		return R66_MORE2M_TO_3M;
	}
	public void setR66_MORE2M_TO_3M(BigDecimal r66_MORE2M_TO_3M) {
		R66_MORE2M_TO_3M = r66_MORE2M_TO_3M;
	}
	public BigDecimal getR66_OVER3M_TO_6M() {
		return R66_OVER3M_TO_6M;
	}
	public void setR66_OVER3M_TO_6M(BigDecimal r66_OVER3M_TO_6M) {
		R66_OVER3M_TO_6M = r66_OVER3M_TO_6M;
	}
	public BigDecimal getR66_OVER6M_TO_1Y() {
		return R66_OVER6M_TO_1Y;
	}
	public void setR66_OVER6M_TO_1Y(BigDecimal r66_OVER6M_TO_1Y) {
		R66_OVER6M_TO_1Y = r66_OVER6M_TO_1Y;
	}
	public BigDecimal getR66_OVER1Y_TO_3Y() {
		return R66_OVER1Y_TO_3Y;
	}
	public void setR66_OVER1Y_TO_3Y(BigDecimal r66_OVER1Y_TO_3Y) {
		R66_OVER1Y_TO_3Y = r66_OVER1Y_TO_3Y;
	}
	public BigDecimal getR66_OVER3Y_TO_5Y() {
		return R66_OVER3Y_TO_5Y;
	}
	public void setR66_OVER3Y_TO_5Y(BigDecimal r66_OVER3Y_TO_5Y) {
		R66_OVER3Y_TO_5Y = r66_OVER3Y_TO_5Y;
	}
	public BigDecimal getR66_OVER5Y() {
		return R66_OVER5Y;
	}
	public void setR66_OVER5Y(BigDecimal r66_OVER5Y) {
		R66_OVER5Y = r66_OVER5Y;
	}
	public BigDecimal getR66_TOTAL() {
		return R66_TOTAL;
	}
	public void setR66_TOTAL(BigDecimal r66_TOTAL) {
		R66_TOTAL = r66_TOTAL;
	}
	public String getR67_PRODUCT() {
		return R67_PRODUCT;
	}
	public void setR67_PRODUCT(String r67_PRODUCT) {
		R67_PRODUCT = r67_PRODUCT;
	}
	public BigDecimal getR67_DAY1() {
		return R67_DAY1;
	}
	public void setR67_DAY1(BigDecimal r67_DAY1) {
		R67_DAY1 = r67_DAY1;
	}
	public BigDecimal getR67_DAY2_7() {
		return R67_DAY2_7;
	}
	public void setR67_DAY2_7(BigDecimal r67_DAY2_7) {
		R67_DAY2_7 = r67_DAY2_7;
	}
	public BigDecimal getR67_DAY8_14() {
		return R67_DAY8_14;
	}
	public void setR67_DAY8_14(BigDecimal r67_DAY8_14) {
		R67_DAY8_14 = r67_DAY8_14;
	}
	public BigDecimal getR67_DAY15_30() {
		return R67_DAY15_30;
	}
	public void setR67_DAY15_30(BigDecimal r67_DAY15_30) {
		R67_DAY15_30 = r67_DAY15_30;
	}
	public BigDecimal getR67_DAY31_TO_2M() {
		return R67_DAY31_TO_2M;
	}
	public void setR67_DAY31_TO_2M(BigDecimal r67_DAY31_TO_2M) {
		R67_DAY31_TO_2M = r67_DAY31_TO_2M;
	}
	public BigDecimal getR67_MORE2M_TO_3M() {
		return R67_MORE2M_TO_3M;
	}
	public void setR67_MORE2M_TO_3M(BigDecimal r67_MORE2M_TO_3M) {
		R67_MORE2M_TO_3M = r67_MORE2M_TO_3M;
	}
	public BigDecimal getR67_OVER3M_TO_6M() {
		return R67_OVER3M_TO_6M;
	}
	public void setR67_OVER3M_TO_6M(BigDecimal r67_OVER3M_TO_6M) {
		R67_OVER3M_TO_6M = r67_OVER3M_TO_6M;
	}
	public BigDecimal getR67_OVER6M_TO_1Y() {
		return R67_OVER6M_TO_1Y;
	}
	public void setR67_OVER6M_TO_1Y(BigDecimal r67_OVER6M_TO_1Y) {
		R67_OVER6M_TO_1Y = r67_OVER6M_TO_1Y;
	}
	public BigDecimal getR67_OVER1Y_TO_3Y() {
		return R67_OVER1Y_TO_3Y;
	}
	public void setR67_OVER1Y_TO_3Y(BigDecimal r67_OVER1Y_TO_3Y) {
		R67_OVER1Y_TO_3Y = r67_OVER1Y_TO_3Y;
	}
	public BigDecimal getR67_OVER3Y_TO_5Y() {
		return R67_OVER3Y_TO_5Y;
	}
	public void setR67_OVER3Y_TO_5Y(BigDecimal r67_OVER3Y_TO_5Y) {
		R67_OVER3Y_TO_5Y = r67_OVER3Y_TO_5Y;
	}
	public BigDecimal getR67_OVER5Y() {
		return R67_OVER5Y;
	}
	public void setR67_OVER5Y(BigDecimal r67_OVER5Y) {
		R67_OVER5Y = r67_OVER5Y;
	}
	public BigDecimal getR67_TOTAL() {
		return R67_TOTAL;
	}
	public void setR67_TOTAL(BigDecimal r67_TOTAL) {
		R67_TOTAL = r67_TOTAL;
	}
	public String getR68_PRODUCT() {
		return R68_PRODUCT;
	}
	public void setR68_PRODUCT(String r68_PRODUCT) {
		R68_PRODUCT = r68_PRODUCT;
	}
	public BigDecimal getR68_DAY1() {
		return R68_DAY1;
	}
	public void setR68_DAY1(BigDecimal r68_DAY1) {
		R68_DAY1 = r68_DAY1;
	}
	public BigDecimal getR68_DAY2_7() {
		return R68_DAY2_7;
	}
	public void setR68_DAY2_7(BigDecimal r68_DAY2_7) {
		R68_DAY2_7 = r68_DAY2_7;
	}
	public BigDecimal getR68_DAY8_14() {
		return R68_DAY8_14;
	}
	public void setR68_DAY8_14(BigDecimal r68_DAY8_14) {
		R68_DAY8_14 = r68_DAY8_14;
	}
	public BigDecimal getR68_DAY15_30() {
		return R68_DAY15_30;
	}
	public void setR68_DAY15_30(BigDecimal r68_DAY15_30) {
		R68_DAY15_30 = r68_DAY15_30;
	}
	public BigDecimal getR68_DAY31_TO_2M() {
		return R68_DAY31_TO_2M;
	}
	public void setR68_DAY31_TO_2M(BigDecimal r68_DAY31_TO_2M) {
		R68_DAY31_TO_2M = r68_DAY31_TO_2M;
	}
	public BigDecimal getR68_MORE2M_TO_3M() {
		return R68_MORE2M_TO_3M;
	}
	public void setR68_MORE2M_TO_3M(BigDecimal r68_MORE2M_TO_3M) {
		R68_MORE2M_TO_3M = r68_MORE2M_TO_3M;
	}
	public BigDecimal getR68_OVER3M_TO_6M() {
		return R68_OVER3M_TO_6M;
	}
	public void setR68_OVER3M_TO_6M(BigDecimal r68_OVER3M_TO_6M) {
		R68_OVER3M_TO_6M = r68_OVER3M_TO_6M;
	}
	public BigDecimal getR68_OVER6M_TO_1Y() {
		return R68_OVER6M_TO_1Y;
	}
	public void setR68_OVER6M_TO_1Y(BigDecimal r68_OVER6M_TO_1Y) {
		R68_OVER6M_TO_1Y = r68_OVER6M_TO_1Y;
	}
	public BigDecimal getR68_OVER1Y_TO_3Y() {
		return R68_OVER1Y_TO_3Y;
	}
	public void setR68_OVER1Y_TO_3Y(BigDecimal r68_OVER1Y_TO_3Y) {
		R68_OVER1Y_TO_3Y = r68_OVER1Y_TO_3Y;
	}
	public BigDecimal getR68_OVER3Y_TO_5Y() {
		return R68_OVER3Y_TO_5Y;
	}
	public void setR68_OVER3Y_TO_5Y(BigDecimal r68_OVER3Y_TO_5Y) {
		R68_OVER3Y_TO_5Y = r68_OVER3Y_TO_5Y;
	}
	public BigDecimal getR68_OVER5Y() {
		return R68_OVER5Y;
	}
	public void setR68_OVER5Y(BigDecimal r68_OVER5Y) {
		R68_OVER5Y = r68_OVER5Y;
	}
	public BigDecimal getR68_TOTAL() {
		return R68_TOTAL;
	}
	public void setR68_TOTAL(BigDecimal r68_TOTAL) {
		R68_TOTAL = r68_TOTAL;
	}
	public String getR69_PRODUCT() {
		return R69_PRODUCT;
	}
	public void setR69_PRODUCT(String r69_PRODUCT) {
		R69_PRODUCT = r69_PRODUCT;
	}
	public BigDecimal getR69_DAY1() {
		return R69_DAY1;
	}
	public void setR69_DAY1(BigDecimal r69_DAY1) {
		R69_DAY1 = r69_DAY1;
	}
	public BigDecimal getR69_DAY2_7() {
		return R69_DAY2_7;
	}
	public void setR69_DAY2_7(BigDecimal r69_DAY2_7) {
		R69_DAY2_7 = r69_DAY2_7;
	}
	public BigDecimal getR69_DAY8_14() {
		return R69_DAY8_14;
	}
	public void setR69_DAY8_14(BigDecimal r69_DAY8_14) {
		R69_DAY8_14 = r69_DAY8_14;
	}
	public BigDecimal getR69_DAY15_30() {
		return R69_DAY15_30;
	}
	public void setR69_DAY15_30(BigDecimal r69_DAY15_30) {
		R69_DAY15_30 = r69_DAY15_30;
	}
	public BigDecimal getR69_DAY31_TO_2M() {
		return R69_DAY31_TO_2M;
	}
	public void setR69_DAY31_TO_2M(BigDecimal r69_DAY31_TO_2M) {
		R69_DAY31_TO_2M = r69_DAY31_TO_2M;
	}
	public BigDecimal getR69_MORE2M_TO_3M() {
		return R69_MORE2M_TO_3M;
	}
	public void setR69_MORE2M_TO_3M(BigDecimal r69_MORE2M_TO_3M) {
		R69_MORE2M_TO_3M = r69_MORE2M_TO_3M;
	}
	public BigDecimal getR69_OVER3M_TO_6M() {
		return R69_OVER3M_TO_6M;
	}
	public void setR69_OVER3M_TO_6M(BigDecimal r69_OVER3M_TO_6M) {
		R69_OVER3M_TO_6M = r69_OVER3M_TO_6M;
	}
	public BigDecimal getR69_OVER6M_TO_1Y() {
		return R69_OVER6M_TO_1Y;
	}
	public void setR69_OVER6M_TO_1Y(BigDecimal r69_OVER6M_TO_1Y) {
		R69_OVER6M_TO_1Y = r69_OVER6M_TO_1Y;
	}
	public BigDecimal getR69_OVER1Y_TO_3Y() {
		return R69_OVER1Y_TO_3Y;
	}
	public void setR69_OVER1Y_TO_3Y(BigDecimal r69_OVER1Y_TO_3Y) {
		R69_OVER1Y_TO_3Y = r69_OVER1Y_TO_3Y;
	}
	public BigDecimal getR69_OVER3Y_TO_5Y() {
		return R69_OVER3Y_TO_5Y;
	}
	public void setR69_OVER3Y_TO_5Y(BigDecimal r69_OVER3Y_TO_5Y) {
		R69_OVER3Y_TO_5Y = r69_OVER3Y_TO_5Y;
	}
	public BigDecimal getR69_OVER5Y() {
		return R69_OVER5Y;
	}
	public void setR69_OVER5Y(BigDecimal r69_OVER5Y) {
		R69_OVER5Y = r69_OVER5Y;
	}
	public BigDecimal getR69_TOTAL() {
		return R69_TOTAL;
	}
	public void setR69_TOTAL(BigDecimal r69_TOTAL) {
		R69_TOTAL = r69_TOTAL;
	}
	public String getR70_PRODUCT() {
		return R70_PRODUCT;
	}
	public void setR70_PRODUCT(String r70_PRODUCT) {
		R70_PRODUCT = r70_PRODUCT;
	}
	public BigDecimal getR70_DAY1() {
		return R70_DAY1;
	}
	public void setR70_DAY1(BigDecimal r70_DAY1) {
		R70_DAY1 = r70_DAY1;
	}
	public BigDecimal getR70_DAY2_7() {
		return R70_DAY2_7;
	}
	public void setR70_DAY2_7(BigDecimal r70_DAY2_7) {
		R70_DAY2_7 = r70_DAY2_7;
	}
	public BigDecimal getR70_DAY8_14() {
		return R70_DAY8_14;
	}
	public void setR70_DAY8_14(BigDecimal r70_DAY8_14) {
		R70_DAY8_14 = r70_DAY8_14;
	}
	public BigDecimal getR70_DAY15_30() {
		return R70_DAY15_30;
	}
	public void setR70_DAY15_30(BigDecimal r70_DAY15_30) {
		R70_DAY15_30 = r70_DAY15_30;
	}
	public BigDecimal getR70_DAY31_TO_2M() {
		return R70_DAY31_TO_2M;
	}
	public void setR70_DAY31_TO_2M(BigDecimal r70_DAY31_TO_2M) {
		R70_DAY31_TO_2M = r70_DAY31_TO_2M;
	}
	public BigDecimal getR70_MORE2M_TO_3M() {
		return R70_MORE2M_TO_3M;
	}
	public void setR70_MORE2M_TO_3M(BigDecimal r70_MORE2M_TO_3M) {
		R70_MORE2M_TO_3M = r70_MORE2M_TO_3M;
	}
	public BigDecimal getR70_OVER3M_TO_6M() {
		return R70_OVER3M_TO_6M;
	}
	public void setR70_OVER3M_TO_6M(BigDecimal r70_OVER3M_TO_6M) {
		R70_OVER3M_TO_6M = r70_OVER3M_TO_6M;
	}
	public BigDecimal getR70_OVER6M_TO_1Y() {
		return R70_OVER6M_TO_1Y;
	}
	public void setR70_OVER6M_TO_1Y(BigDecimal r70_OVER6M_TO_1Y) {
		R70_OVER6M_TO_1Y = r70_OVER6M_TO_1Y;
	}
	public BigDecimal getR70_OVER1Y_TO_3Y() {
		return R70_OVER1Y_TO_3Y;
	}
	public void setR70_OVER1Y_TO_3Y(BigDecimal r70_OVER1Y_TO_3Y) {
		R70_OVER1Y_TO_3Y = r70_OVER1Y_TO_3Y;
	}
	public BigDecimal getR70_OVER3Y_TO_5Y() {
		return R70_OVER3Y_TO_5Y;
	}
	public void setR70_OVER3Y_TO_5Y(BigDecimal r70_OVER3Y_TO_5Y) {
		R70_OVER3Y_TO_5Y = r70_OVER3Y_TO_5Y;
	}
	public BigDecimal getR70_OVER5Y() {
		return R70_OVER5Y;
	}
	public void setR70_OVER5Y(BigDecimal r70_OVER5Y) {
		R70_OVER5Y = r70_OVER5Y;
	}
	public BigDecimal getR70_TOTAL() {
		return R70_TOTAL;
	}
	public void setR70_TOTAL(BigDecimal r70_TOTAL) {
		R70_TOTAL = r70_TOTAL;
	}
	public String getR71_PRODUCT() {
		return R71_PRODUCT;
	}
	public void setR71_PRODUCT(String r71_PRODUCT) {
		R71_PRODUCT = r71_PRODUCT;
	}
	public BigDecimal getR71_DAY1() {
		return R71_DAY1;
	}
	public void setR71_DAY1(BigDecimal r71_DAY1) {
		R71_DAY1 = r71_DAY1;
	}
	public BigDecimal getR71_DAY2_7() {
		return R71_DAY2_7;
	}
	public void setR71_DAY2_7(BigDecimal r71_DAY2_7) {
		R71_DAY2_7 = r71_DAY2_7;
	}
	public BigDecimal getR71_DAY8_14() {
		return R71_DAY8_14;
	}
	public void setR71_DAY8_14(BigDecimal r71_DAY8_14) {
		R71_DAY8_14 = r71_DAY8_14;
	}
	public BigDecimal getR71_DAY15_30() {
		return R71_DAY15_30;
	}
	public void setR71_DAY15_30(BigDecimal r71_DAY15_30) {
		R71_DAY15_30 = r71_DAY15_30;
	}
	public BigDecimal getR71_DAY31_TO_2M() {
		return R71_DAY31_TO_2M;
	}
	public void setR71_DAY31_TO_2M(BigDecimal r71_DAY31_TO_2M) {
		R71_DAY31_TO_2M = r71_DAY31_TO_2M;
	}
	public BigDecimal getR71_MORE2M_TO_3M() {
		return R71_MORE2M_TO_3M;
	}
	public void setR71_MORE2M_TO_3M(BigDecimal r71_MORE2M_TO_3M) {
		R71_MORE2M_TO_3M = r71_MORE2M_TO_3M;
	}
	public BigDecimal getR71_OVER3M_TO_6M() {
		return R71_OVER3M_TO_6M;
	}
	public void setR71_OVER3M_TO_6M(BigDecimal r71_OVER3M_TO_6M) {
		R71_OVER3M_TO_6M = r71_OVER3M_TO_6M;
	}
	public BigDecimal getR71_OVER6M_TO_1Y() {
		return R71_OVER6M_TO_1Y;
	}
	public void setR71_OVER6M_TO_1Y(BigDecimal r71_OVER6M_TO_1Y) {
		R71_OVER6M_TO_1Y = r71_OVER6M_TO_1Y;
	}
	public BigDecimal getR71_OVER1Y_TO_3Y() {
		return R71_OVER1Y_TO_3Y;
	}
	public void setR71_OVER1Y_TO_3Y(BigDecimal r71_OVER1Y_TO_3Y) {
		R71_OVER1Y_TO_3Y = r71_OVER1Y_TO_3Y;
	}
	public BigDecimal getR71_OVER3Y_TO_5Y() {
		return R71_OVER3Y_TO_5Y;
	}
	public void setR71_OVER3Y_TO_5Y(BigDecimal r71_OVER3Y_TO_5Y) {
		R71_OVER3Y_TO_5Y = r71_OVER3Y_TO_5Y;
	}
	public BigDecimal getR71_OVER5Y() {
		return R71_OVER5Y;
	}
	public void setR71_OVER5Y(BigDecimal r71_OVER5Y) {
		R71_OVER5Y = r71_OVER5Y;
	}
	public BigDecimal getR71_TOTAL() {
		return R71_TOTAL;
	}
	public void setR71_TOTAL(BigDecimal r71_TOTAL) {
		R71_TOTAL = r71_TOTAL;
	}
	public String getR72_PRODUCT() {
		return R72_PRODUCT;
	}
	public void setR72_PRODUCT(String r72_PRODUCT) {
		R72_PRODUCT = r72_PRODUCT;
	}
	public BigDecimal getR72_DAY1() {
		return R72_DAY1;
	}
	public void setR72_DAY1(BigDecimal r72_DAY1) {
		R72_DAY1 = r72_DAY1;
	}
	public BigDecimal getR72_DAY2_7() {
		return R72_DAY2_7;
	}
	public void setR72_DAY2_7(BigDecimal r72_DAY2_7) {
		R72_DAY2_7 = r72_DAY2_7;
	}
	public BigDecimal getR72_DAY8_14() {
		return R72_DAY8_14;
	}
	public void setR72_DAY8_14(BigDecimal r72_DAY8_14) {
		R72_DAY8_14 = r72_DAY8_14;
	}
	public BigDecimal getR72_DAY15_30() {
		return R72_DAY15_30;
	}
	public void setR72_DAY15_30(BigDecimal r72_DAY15_30) {
		R72_DAY15_30 = r72_DAY15_30;
	}
	public BigDecimal getR72_DAY31_TO_2M() {
		return R72_DAY31_TO_2M;
	}
	public void setR72_DAY31_TO_2M(BigDecimal r72_DAY31_TO_2M) {
		R72_DAY31_TO_2M = r72_DAY31_TO_2M;
	}
	public BigDecimal getR72_MORE2M_TO_3M() {
		return R72_MORE2M_TO_3M;
	}
	public void setR72_MORE2M_TO_3M(BigDecimal r72_MORE2M_TO_3M) {
		R72_MORE2M_TO_3M = r72_MORE2M_TO_3M;
	}
	public BigDecimal getR72_OVER3M_TO_6M() {
		return R72_OVER3M_TO_6M;
	}
	public void setR72_OVER3M_TO_6M(BigDecimal r72_OVER3M_TO_6M) {
		R72_OVER3M_TO_6M = r72_OVER3M_TO_6M;
	}
	public BigDecimal getR72_OVER6M_TO_1Y() {
		return R72_OVER6M_TO_1Y;
	}
	public void setR72_OVER6M_TO_1Y(BigDecimal r72_OVER6M_TO_1Y) {
		R72_OVER6M_TO_1Y = r72_OVER6M_TO_1Y;
	}
	public BigDecimal getR72_OVER1Y_TO_3Y() {
		return R72_OVER1Y_TO_3Y;
	}
	public void setR72_OVER1Y_TO_3Y(BigDecimal r72_OVER1Y_TO_3Y) {
		R72_OVER1Y_TO_3Y = r72_OVER1Y_TO_3Y;
	}
	public BigDecimal getR72_OVER3Y_TO_5Y() {
		return R72_OVER3Y_TO_5Y;
	}
	public void setR72_OVER3Y_TO_5Y(BigDecimal r72_OVER3Y_TO_5Y) {
		R72_OVER3Y_TO_5Y = r72_OVER3Y_TO_5Y;
	}
	public BigDecimal getR72_OVER5Y() {
		return R72_OVER5Y;
	}
	public void setR72_OVER5Y(BigDecimal r72_OVER5Y) {
		R72_OVER5Y = r72_OVER5Y;
	}
	public BigDecimal getR72_TOTAL() {
		return R72_TOTAL;
	}
	public void setR72_TOTAL(BigDecimal r72_TOTAL) {
		R72_TOTAL = r72_TOTAL;
	}
	public String getR73_PRODUCT() {
		return R73_PRODUCT;
	}
	public void setR73_PRODUCT(String r73_PRODUCT) {
		R73_PRODUCT = r73_PRODUCT;
	}
	public BigDecimal getR73_DAY1() {
		return R73_DAY1;
	}
	public void setR73_DAY1(BigDecimal r73_DAY1) {
		R73_DAY1 = r73_DAY1;
	}
	public BigDecimal getR73_DAY2_7() {
		return R73_DAY2_7;
	}
	public void setR73_DAY2_7(BigDecimal r73_DAY2_7) {
		R73_DAY2_7 = r73_DAY2_7;
	}
	public BigDecimal getR73_DAY8_14() {
		return R73_DAY8_14;
	}
	public void setR73_DAY8_14(BigDecimal r73_DAY8_14) {
		R73_DAY8_14 = r73_DAY8_14;
	}
	public BigDecimal getR73_DAY15_30() {
		return R73_DAY15_30;
	}
	public void setR73_DAY15_30(BigDecimal r73_DAY15_30) {
		R73_DAY15_30 = r73_DAY15_30;
	}
	public BigDecimal getR73_DAY31_TO_2M() {
		return R73_DAY31_TO_2M;
	}
	public void setR73_DAY31_TO_2M(BigDecimal r73_DAY31_TO_2M) {
		R73_DAY31_TO_2M = r73_DAY31_TO_2M;
	}
	public BigDecimal getR73_MORE2M_TO_3M() {
		return R73_MORE2M_TO_3M;
	}
	public void setR73_MORE2M_TO_3M(BigDecimal r73_MORE2M_TO_3M) {
		R73_MORE2M_TO_3M = r73_MORE2M_TO_3M;
	}
	public BigDecimal getR73_OVER3M_TO_6M() {
		return R73_OVER3M_TO_6M;
	}
	public void setR73_OVER3M_TO_6M(BigDecimal r73_OVER3M_TO_6M) {
		R73_OVER3M_TO_6M = r73_OVER3M_TO_6M;
	}
	public BigDecimal getR73_OVER6M_TO_1Y() {
		return R73_OVER6M_TO_1Y;
	}
	public void setR73_OVER6M_TO_1Y(BigDecimal r73_OVER6M_TO_1Y) {
		R73_OVER6M_TO_1Y = r73_OVER6M_TO_1Y;
	}
	public BigDecimal getR73_OVER1Y_TO_3Y() {
		return R73_OVER1Y_TO_3Y;
	}
	public void setR73_OVER1Y_TO_3Y(BigDecimal r73_OVER1Y_TO_3Y) {
		R73_OVER1Y_TO_3Y = r73_OVER1Y_TO_3Y;
	}
	public BigDecimal getR73_OVER3Y_TO_5Y() {
		return R73_OVER3Y_TO_5Y;
	}
	public void setR73_OVER3Y_TO_5Y(BigDecimal r73_OVER3Y_TO_5Y) {
		R73_OVER3Y_TO_5Y = r73_OVER3Y_TO_5Y;
	}
	public BigDecimal getR73_OVER5Y() {
		return R73_OVER5Y;
	}
	public void setR73_OVER5Y(BigDecimal r73_OVER5Y) {
		R73_OVER5Y = r73_OVER5Y;
	}
	public BigDecimal getR73_TOTAL() {
		return R73_TOTAL;
	}
	public void setR73_TOTAL(BigDecimal r73_TOTAL) {
		R73_TOTAL = r73_TOTAL;
	}
	public String getR74_PRODUCT() {
		return R74_PRODUCT;
	}
	public void setR74_PRODUCT(String r74_PRODUCT) {
		R74_PRODUCT = r74_PRODUCT;
	}
	public BigDecimal getR74_DAY1() {
		return R74_DAY1;
	}
	public void setR74_DAY1(BigDecimal r74_DAY1) {
		R74_DAY1 = r74_DAY1;
	}
	public BigDecimal getR74_DAY2_7() {
		return R74_DAY2_7;
	}
	public void setR74_DAY2_7(BigDecimal r74_DAY2_7) {
		R74_DAY2_7 = r74_DAY2_7;
	}
	public BigDecimal getR74_DAY8_14() {
		return R74_DAY8_14;
	}
	public void setR74_DAY8_14(BigDecimal r74_DAY8_14) {
		R74_DAY8_14 = r74_DAY8_14;
	}
	public BigDecimal getR74_DAY15_30() {
		return R74_DAY15_30;
	}
	public void setR74_DAY15_30(BigDecimal r74_DAY15_30) {
		R74_DAY15_30 = r74_DAY15_30;
	}
	public BigDecimal getR74_DAY31_TO_2M() {
		return R74_DAY31_TO_2M;
	}
	public void setR74_DAY31_TO_2M(BigDecimal r74_DAY31_TO_2M) {
		R74_DAY31_TO_2M = r74_DAY31_TO_2M;
	}
	public BigDecimal getR74_MORE2M_TO_3M() {
		return R74_MORE2M_TO_3M;
	}
	public void setR74_MORE2M_TO_3M(BigDecimal r74_MORE2M_TO_3M) {
		R74_MORE2M_TO_3M = r74_MORE2M_TO_3M;
	}
	public BigDecimal getR74_OVER3M_TO_6M() {
		return R74_OVER3M_TO_6M;
	}
	public void setR74_OVER3M_TO_6M(BigDecimal r74_OVER3M_TO_6M) {
		R74_OVER3M_TO_6M = r74_OVER3M_TO_6M;
	}
	public BigDecimal getR74_OVER6M_TO_1Y() {
		return R74_OVER6M_TO_1Y;
	}
	public void setR74_OVER6M_TO_1Y(BigDecimal r74_OVER6M_TO_1Y) {
		R74_OVER6M_TO_1Y = r74_OVER6M_TO_1Y;
	}
	public BigDecimal getR74_OVER1Y_TO_3Y() {
		return R74_OVER1Y_TO_3Y;
	}
	public void setR74_OVER1Y_TO_3Y(BigDecimal r74_OVER1Y_TO_3Y) {
		R74_OVER1Y_TO_3Y = r74_OVER1Y_TO_3Y;
	}
	public BigDecimal getR74_OVER3Y_TO_5Y() {
		return R74_OVER3Y_TO_5Y;
	}
	public void setR74_OVER3Y_TO_5Y(BigDecimal r74_OVER3Y_TO_5Y) {
		R74_OVER3Y_TO_5Y = r74_OVER3Y_TO_5Y;
	}
	public BigDecimal getR74_OVER5Y() {
		return R74_OVER5Y;
	}
	public void setR74_OVER5Y(BigDecimal r74_OVER5Y) {
		R74_OVER5Y = r74_OVER5Y;
	}
	public BigDecimal getR74_TOTAL() {
		return R74_TOTAL;
	}
	public void setR74_TOTAL(BigDecimal r74_TOTAL) {
		R74_TOTAL = r74_TOTAL;
	}
	public String getR75_PRODUCT() {
		return R75_PRODUCT;
	}
	public void setR75_PRODUCT(String r75_PRODUCT) {
		R75_PRODUCT = r75_PRODUCT;
	}
	public BigDecimal getR75_DAY1() {
		return R75_DAY1;
	}
	public void setR75_DAY1(BigDecimal r75_DAY1) {
		R75_DAY1 = r75_DAY1;
	}
	public BigDecimal getR75_DAY2_7() {
		return R75_DAY2_7;
	}
	public void setR75_DAY2_7(BigDecimal r75_DAY2_7) {
		R75_DAY2_7 = r75_DAY2_7;
	}
	public BigDecimal getR75_DAY8_14() {
		return R75_DAY8_14;
	}
	public void setR75_DAY8_14(BigDecimal r75_DAY8_14) {
		R75_DAY8_14 = r75_DAY8_14;
	}
	public BigDecimal getR75_DAY15_30() {
		return R75_DAY15_30;
	}
	public void setR75_DAY15_30(BigDecimal r75_DAY15_30) {
		R75_DAY15_30 = r75_DAY15_30;
	}
	public BigDecimal getR75_DAY31_TO_2M() {
		return R75_DAY31_TO_2M;
	}
	public void setR75_DAY31_TO_2M(BigDecimal r75_DAY31_TO_2M) {
		R75_DAY31_TO_2M = r75_DAY31_TO_2M;
	}
	public BigDecimal getR75_MORE2M_TO_3M() {
		return R75_MORE2M_TO_3M;
	}
	public void setR75_MORE2M_TO_3M(BigDecimal r75_MORE2M_TO_3M) {
		R75_MORE2M_TO_3M = r75_MORE2M_TO_3M;
	}
	public BigDecimal getR75_OVER3M_TO_6M() {
		return R75_OVER3M_TO_6M;
	}
	public void setR75_OVER3M_TO_6M(BigDecimal r75_OVER3M_TO_6M) {
		R75_OVER3M_TO_6M = r75_OVER3M_TO_6M;
	}
	public BigDecimal getR75_OVER6M_TO_1Y() {
		return R75_OVER6M_TO_1Y;
	}
	public void setR75_OVER6M_TO_1Y(BigDecimal r75_OVER6M_TO_1Y) {
		R75_OVER6M_TO_1Y = r75_OVER6M_TO_1Y;
	}
	public BigDecimal getR75_OVER1Y_TO_3Y() {
		return R75_OVER1Y_TO_3Y;
	}
	public void setR75_OVER1Y_TO_3Y(BigDecimal r75_OVER1Y_TO_3Y) {
		R75_OVER1Y_TO_3Y = r75_OVER1Y_TO_3Y;
	}
	public BigDecimal getR75_OVER3Y_TO_5Y() {
		return R75_OVER3Y_TO_5Y;
	}
	public void setR75_OVER3Y_TO_5Y(BigDecimal r75_OVER3Y_TO_5Y) {
		R75_OVER3Y_TO_5Y = r75_OVER3Y_TO_5Y;
	}
	public BigDecimal getR75_OVER5Y() {
		return R75_OVER5Y;
	}
	public void setR75_OVER5Y(BigDecimal r75_OVER5Y) {
		R75_OVER5Y = r75_OVER5Y;
	}
	public BigDecimal getR75_TOTAL() {
		return R75_TOTAL;
	}
	public void setR75_TOTAL(BigDecimal r75_TOTAL) {
		R75_TOTAL = r75_TOTAL;
	}
	public String getR76_PRODUCT() {
		return R76_PRODUCT;
	}
	public void setR76_PRODUCT(String r76_PRODUCT) {
		R76_PRODUCT = r76_PRODUCT;
	}
	public BigDecimal getR76_DAY1() {
		return R76_DAY1;
	}
	public void setR76_DAY1(BigDecimal r76_DAY1) {
		R76_DAY1 = r76_DAY1;
	}
	public BigDecimal getR76_DAY2_7() {
		return R76_DAY2_7;
	}
	public void setR76_DAY2_7(BigDecimal r76_DAY2_7) {
		R76_DAY2_7 = r76_DAY2_7;
	}
	public BigDecimal getR76_DAY8_14() {
		return R76_DAY8_14;
	}
	public void setR76_DAY8_14(BigDecimal r76_DAY8_14) {
		R76_DAY8_14 = r76_DAY8_14;
	}
	public BigDecimal getR76_DAY15_30() {
		return R76_DAY15_30;
	}
	public void setR76_DAY15_30(BigDecimal r76_DAY15_30) {
		R76_DAY15_30 = r76_DAY15_30;
	}
	public BigDecimal getR76_DAY31_TO_2M() {
		return R76_DAY31_TO_2M;
	}
	public void setR76_DAY31_TO_2M(BigDecimal r76_DAY31_TO_2M) {
		R76_DAY31_TO_2M = r76_DAY31_TO_2M;
	}
	public BigDecimal getR76_MORE2M_TO_3M() {
		return R76_MORE2M_TO_3M;
	}
	public void setR76_MORE2M_TO_3M(BigDecimal r76_MORE2M_TO_3M) {
		R76_MORE2M_TO_3M = r76_MORE2M_TO_3M;
	}
	public BigDecimal getR76_OVER3M_TO_6M() {
		return R76_OVER3M_TO_6M;
	}
	public void setR76_OVER3M_TO_6M(BigDecimal r76_OVER3M_TO_6M) {
		R76_OVER3M_TO_6M = r76_OVER3M_TO_6M;
	}
	public BigDecimal getR76_OVER6M_TO_1Y() {
		return R76_OVER6M_TO_1Y;
	}
	public void setR76_OVER6M_TO_1Y(BigDecimal r76_OVER6M_TO_1Y) {
		R76_OVER6M_TO_1Y = r76_OVER6M_TO_1Y;
	}
	public BigDecimal getR76_OVER1Y_TO_3Y() {
		return R76_OVER1Y_TO_3Y;
	}
	public void setR76_OVER1Y_TO_3Y(BigDecimal r76_OVER1Y_TO_3Y) {
		R76_OVER1Y_TO_3Y = r76_OVER1Y_TO_3Y;
	}
	public BigDecimal getR76_OVER3Y_TO_5Y() {
		return R76_OVER3Y_TO_5Y;
	}
	public void setR76_OVER3Y_TO_5Y(BigDecimal r76_OVER3Y_TO_5Y) {
		R76_OVER3Y_TO_5Y = r76_OVER3Y_TO_5Y;
	}
	public BigDecimal getR76_OVER5Y() {
		return R76_OVER5Y;
	}
	public void setR76_OVER5Y(BigDecimal r76_OVER5Y) {
		R76_OVER5Y = r76_OVER5Y;
	}
	public BigDecimal getR76_TOTAL() {
		return R76_TOTAL;
	}
	public void setR76_TOTAL(BigDecimal r76_TOTAL) {
		R76_TOTAL = r76_TOTAL;
	}
//	public String getR77_PRODUCT() {
//		return R77_PRODUCT;
//	}
//	public void setR77_PRODUCT(String r77_PRODUCT) {
//		R77_PRODUCT = r77_PRODUCT;
//	}
//	public BigDecimal getR77_DAY1() {
//		return R77_DAY1;
//	}
//	public void setR77_DAY1(BigDecimal r77_DAY1) {
//		R77_DAY1 = r77_DAY1;
//	}
//	public BigDecimal getR77_DAY2_7() {
//		return R77_DAY2_7;
//	}
//	public void setR77_DAY2_7(BigDecimal r77_DAY2_7) {
//		R77_DAY2_7 = r77_DAY2_7;
//	}
//	public BigDecimal getR77_DAY8_14() {
//		return R77_DAY8_14;
//	}
//	public void setR77_DAY8_14(BigDecimal r77_DAY8_14) {
//		R77_DAY8_14 = r77_DAY8_14;
//	}
//	public BigDecimal getR77_DAY15_30() {
//		return R77_DAY15_30;
//	}
//	public void setR77_DAY15_30(BigDecimal r77_DAY15_30) {
//		R77_DAY15_30 = r77_DAY15_30;
//	}
//	public BigDecimal getR77_DAY31_TO_2M() {
//		return R77_DAY31_TO_2M;
//	}
//	public void setR77_DAY31_TO_2M(BigDecimal r77_DAY31_TO_2M) {
//		R77_DAY31_TO_2M = r77_DAY31_TO_2M;
//	}
//	public BigDecimal getR77_MORE2M_TO_3M() {
//		return R77_MORE2M_TO_3M;
//	}
//	public void setR77_MORE2M_TO_3M(BigDecimal r77_MORE2M_TO_3M) {
//		R77_MORE2M_TO_3M = r77_MORE2M_TO_3M;
//	}
//	public BigDecimal getR77_OVER3M_TO_6M() {
//		return R77_OVER3M_TO_6M;
//	}
//	public void setR77_OVER3M_TO_6M(BigDecimal r77_OVER3M_TO_6M) {
//		R77_OVER3M_TO_6M = r77_OVER3M_TO_6M;
//	}
//	public BigDecimal getR77_OVER6M_TO_1Y() {
//		return R77_OVER6M_TO_1Y;
//	}
//	public void setR77_OVER6M_TO_1Y(BigDecimal r77_OVER6M_TO_1Y) {
//		R77_OVER6M_TO_1Y = r77_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR77_OVER1Y_TO_3Y() {
//		return R77_OVER1Y_TO_3Y;
//	}
//	public void setR77_OVER1Y_TO_3Y(BigDecimal r77_OVER1Y_TO_3Y) {
//		R77_OVER1Y_TO_3Y = r77_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR77_OVER3Y_TO_5Y() {
//		return R77_OVER3Y_TO_5Y;
//	}
//	public void setR77_OVER3Y_TO_5Y(BigDecimal r77_OVER3Y_TO_5Y) {
//		R77_OVER3Y_TO_5Y = r77_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR77_OVER5Y() {
//		return R77_OVER5Y;
//	}
//	public void setR77_OVER5Y(BigDecimal r77_OVER5Y) {
//		R77_OVER5Y = r77_OVER5Y;
//	}
//	public BigDecimal getR77_TOTAL() {
//		return R77_TOTAL;
//	}
//	public void setR77_TOTAL(BigDecimal r77_TOTAL) {
//		R77_TOTAL = r77_TOTAL;
//	}
//	public String getR78_PRODUCT() {
//		return R78_PRODUCT;
//	}
//	public void setR78_PRODUCT(String r78_PRODUCT) {
//		R78_PRODUCT = r78_PRODUCT;
//	}
//	public BigDecimal getR78_DAY1() {
//		return R78_DAY1;
//	}
//	public void setR78_DAY1(BigDecimal r78_DAY1) {
//		R78_DAY1 = r78_DAY1;
//	}
//	public BigDecimal getR78_DAY2_7() {
//		return R78_DAY2_7;
//	}
//	public void setR78_DAY2_7(BigDecimal r78_DAY2_7) {
//		R78_DAY2_7 = r78_DAY2_7;
//	}
//	public BigDecimal getR78_DAY8_14() {
//		return R78_DAY8_14;
//	}
//	public void setR78_DAY8_14(BigDecimal r78_DAY8_14) {
//		R78_DAY8_14 = r78_DAY8_14;
//	}
//	public BigDecimal getR78_DAY15_30() {
//		return R78_DAY15_30;
//	}
//	public void setR78_DAY15_30(BigDecimal r78_DAY15_30) {
//		R78_DAY15_30 = r78_DAY15_30;
//	}
//	public BigDecimal getR78_DAY31_TO_2M() {
//		return R78_DAY31_TO_2M;
//	}
//	public void setR78_DAY31_TO_2M(BigDecimal r78_DAY31_TO_2M) {
//		R78_DAY31_TO_2M = r78_DAY31_TO_2M;
//	}
//	public BigDecimal getR78_MORE2M_TO_3M() {
//		return R78_MORE2M_TO_3M;
//	}
//	public void setR78_MORE2M_TO_3M(BigDecimal r78_MORE2M_TO_3M) {
//		R78_MORE2M_TO_3M = r78_MORE2M_TO_3M;
//	}
//	public BigDecimal getR78_OVER3M_TO_6M() {
//		return R78_OVER3M_TO_6M;
//	}
//	public void setR78_OVER3M_TO_6M(BigDecimal r78_OVER3M_TO_6M) {
//		R78_OVER3M_TO_6M = r78_OVER3M_TO_6M;
//	}
//	public BigDecimal getR78_OVER6M_TO_1Y() {
//		return R78_OVER6M_TO_1Y;
//	}
//	public void setR78_OVER6M_TO_1Y(BigDecimal r78_OVER6M_TO_1Y) {
//		R78_OVER6M_TO_1Y = r78_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR78_OVER1Y_TO_3Y() {
//		return R78_OVER1Y_TO_3Y;
//	}
//	public void setR78_OVER1Y_TO_3Y(BigDecimal r78_OVER1Y_TO_3Y) {
//		R78_OVER1Y_TO_3Y = r78_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR78_OVER3Y_TO_5Y() {
//		return R78_OVER3Y_TO_5Y;
//	}
//	public void setR78_OVER3Y_TO_5Y(BigDecimal r78_OVER3Y_TO_5Y) {
//		R78_OVER3Y_TO_5Y = r78_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR78_OVER5Y() {
//		return R78_OVER5Y;
//	}
//	public void setR78_OVER5Y(BigDecimal r78_OVER5Y) {
//		R78_OVER5Y = r78_OVER5Y;
//	}
//	public BigDecimal getR78_TOTAL() {
//		return R78_TOTAL;
//	}
//	public void setR78_TOTAL(BigDecimal r78_TOTAL) {
//		R78_TOTAL = r78_TOTAL;
//	}
//	public String getR79_PRODUCT() {
//		return R79_PRODUCT;
//	}
//	public void setR79_PRODUCT(String r79_PRODUCT) {
//		R79_PRODUCT = r79_PRODUCT;
//	}
//	public BigDecimal getR79_DAY1() {
//		return R79_DAY1;
//	}
//	public void setR79_DAY1(BigDecimal r79_DAY1) {
//		R79_DAY1 = r79_DAY1;
//	}
//	public BigDecimal getR79_DAY2_7() {
//		return R79_DAY2_7;
//	}
//	public void setR79_DAY2_7(BigDecimal r79_DAY2_7) {
//		R79_DAY2_7 = r79_DAY2_7;
//	}
//	public BigDecimal getR79_DAY8_14() {
//		return R79_DAY8_14;
//	}
//	public void setR79_DAY8_14(BigDecimal r79_DAY8_14) {
//		R79_DAY8_14 = r79_DAY8_14;
//	}
//	public BigDecimal getR79_DAY15_30() {
//		return R79_DAY15_30;
//	}
//	public void setR79_DAY15_30(BigDecimal r79_DAY15_30) {
//		R79_DAY15_30 = r79_DAY15_30;
//	}
//	public BigDecimal getR79_DAY31_TO_2M() {
//		return R79_DAY31_TO_2M;
//	}
//	public void setR79_DAY31_TO_2M(BigDecimal r79_DAY31_TO_2M) {
//		R79_DAY31_TO_2M = r79_DAY31_TO_2M;
//	}
//	public BigDecimal getR79_MORE2M_TO_3M() {
//		return R79_MORE2M_TO_3M;
//	}
//	public void setR79_MORE2M_TO_3M(BigDecimal r79_MORE2M_TO_3M) {
//		R79_MORE2M_TO_3M = r79_MORE2M_TO_3M;
//	}
//	public BigDecimal getR79_OVER3M_TO_6M() {
//		return R79_OVER3M_TO_6M;
//	}
//	public void setR79_OVER3M_TO_6M(BigDecimal r79_OVER3M_TO_6M) {
//		R79_OVER3M_TO_6M = r79_OVER3M_TO_6M;
//	}
//	public BigDecimal getR79_OVER6M_TO_1Y() {
//		return R79_OVER6M_TO_1Y;
//	}
//	public void setR79_OVER6M_TO_1Y(BigDecimal r79_OVER6M_TO_1Y) {
//		R79_OVER6M_TO_1Y = r79_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR79_OVER1Y_TO_3Y() {
//		return R79_OVER1Y_TO_3Y;
//	}
//	public void setR79_OVER1Y_TO_3Y(BigDecimal r79_OVER1Y_TO_3Y) {
//		R79_OVER1Y_TO_3Y = r79_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR79_OVER3Y_TO_5Y() {
//		return R79_OVER3Y_TO_5Y;
//	}
//	public void setR79_OVER3Y_TO_5Y(BigDecimal r79_OVER3Y_TO_5Y) {
//		R79_OVER3Y_TO_5Y = r79_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR79_OVER5Y() {
//		return R79_OVER5Y;
//	}
//	public void setR79_OVER5Y(BigDecimal r79_OVER5Y) {
//		R79_OVER5Y = r79_OVER5Y;
//	}
//	public BigDecimal getR79_TOTAL() {
//		return R79_TOTAL;
//	}
//	public void setR79_TOTAL(BigDecimal r79_TOTAL) {
//		R79_TOTAL = r79_TOTAL;
//	}
//	public String getR80_PRODUCT() {
//		return R80_PRODUCT;
//	}
//	public void setR80_PRODUCT(String r80_PRODUCT) {
//		R80_PRODUCT = r80_PRODUCT;
//	}
//	public BigDecimal getR80_DAY1() {
//		return R80_DAY1;
//	}
//	public void setR80_DAY1(BigDecimal r80_DAY1) {
//		R80_DAY1 = r80_DAY1;
//	}
//	public BigDecimal getR80_DAY2_7() {
//		return R80_DAY2_7;
//	}
//	public void setR80_DAY2_7(BigDecimal r80_DAY2_7) {
//		R80_DAY2_7 = r80_DAY2_7;
//	}
//	public BigDecimal getR80_DAY8_14() {
//		return R80_DAY8_14;
//	}
//	public void setR80_DAY8_14(BigDecimal r80_DAY8_14) {
//		R80_DAY8_14 = r80_DAY8_14;
//	}
//	public BigDecimal getR80_DAY15_30() {
//		return R80_DAY15_30;
//	}
//	public void setR80_DAY15_30(BigDecimal r80_DAY15_30) {
//		R80_DAY15_30 = r80_DAY15_30;
//	}
//	public BigDecimal getR80_DAY31_TO_2M() {
//		return R80_DAY31_TO_2M;
//	}
//	public void setR80_DAY31_TO_2M(BigDecimal r80_DAY31_TO_2M) {
//		R80_DAY31_TO_2M = r80_DAY31_TO_2M;
//	}
//	public BigDecimal getR80_MORE2M_TO_3M() {
//		return R80_MORE2M_TO_3M;
//	}
//	public void setR80_MORE2M_TO_3M(BigDecimal r80_MORE2M_TO_3M) {
//		R80_MORE2M_TO_3M = r80_MORE2M_TO_3M;
//	}
//	public BigDecimal getR80_OVER3M_TO_6M() {
//		return R80_OVER3M_TO_6M;
//	}
//	public void setR80_OVER3M_TO_6M(BigDecimal r80_OVER3M_TO_6M) {
//		R80_OVER3M_TO_6M = r80_OVER3M_TO_6M;
//	}
//	public BigDecimal getR80_OVER6M_TO_1Y() {
//		return R80_OVER6M_TO_1Y;
//	}
//	public void setR80_OVER6M_TO_1Y(BigDecimal r80_OVER6M_TO_1Y) {
//		R80_OVER6M_TO_1Y = r80_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR80_OVER1Y_TO_3Y() {
//		return R80_OVER1Y_TO_3Y;
//	}
//	public void setR80_OVER1Y_TO_3Y(BigDecimal r80_OVER1Y_TO_3Y) {
//		R80_OVER1Y_TO_3Y = r80_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR80_OVER3Y_TO_5Y() {
//		return R80_OVER3Y_TO_5Y;
//	}
//	public void setR80_OVER3Y_TO_5Y(BigDecimal r80_OVER3Y_TO_5Y) {
//		R80_OVER3Y_TO_5Y = r80_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR80_OVER5Y() {
//		return R80_OVER5Y;
//	}
//	public void setR80_OVER5Y(BigDecimal r80_OVER5Y) {
//		R80_OVER5Y = r80_OVER5Y;
//	}
//	public BigDecimal getR80_TOTAL() {
//		return R80_TOTAL;
//	}
//	public void setR80_TOTAL(BigDecimal r80_TOTAL) {
//		R80_TOTAL = r80_TOTAL;
//	}
//	public String getR81_PRODUCT() {
//		return R81_PRODUCT;
//	}
//	public void setR81_PRODUCT(String r81_PRODUCT) {
//		R81_PRODUCT = r81_PRODUCT;
//	}
//	public BigDecimal getR81_DAY1() {
//		return R81_DAY1;
//	}
//	public void setR81_DAY1(BigDecimal r81_DAY1) {
//		R81_DAY1 = r81_DAY1;
//	}
//	public BigDecimal getR81_DAY2_7() {
//		return R81_DAY2_7;
//	}
//	public void setR81_DAY2_7(BigDecimal r81_DAY2_7) {
//		R81_DAY2_7 = r81_DAY2_7;
//	}
//	public BigDecimal getR81_DAY8_14() {
//		return R81_DAY8_14;
//	}
//	public void setR81_DAY8_14(BigDecimal r81_DAY8_14) {
//		R81_DAY8_14 = r81_DAY8_14;
//	}
//	public BigDecimal getR81_DAY15_30() {
//		return R81_DAY15_30;
//	}
//	public void setR81_DAY15_30(BigDecimal r81_DAY15_30) {
//		R81_DAY15_30 = r81_DAY15_30;
//	}
//	public BigDecimal getR81_DAY31_TO_2M() {
//		return R81_DAY31_TO_2M;
//	}
//	public void setR81_DAY31_TO_2M(BigDecimal r81_DAY31_TO_2M) {
//		R81_DAY31_TO_2M = r81_DAY31_TO_2M;
//	}
//	public BigDecimal getR81_MORE2M_TO_3M() {
//		return R81_MORE2M_TO_3M;
//	}
//	public void setR81_MORE2M_TO_3M(BigDecimal r81_MORE2M_TO_3M) {
//		R81_MORE2M_TO_3M = r81_MORE2M_TO_3M;
//	}
//	public BigDecimal getR81_OVER3M_TO_6M() {
//		return R81_OVER3M_TO_6M;
//	}
//	public void setR81_OVER3M_TO_6M(BigDecimal r81_OVER3M_TO_6M) {
//		R81_OVER3M_TO_6M = r81_OVER3M_TO_6M;
//	}
//	public BigDecimal getR81_OVER6M_TO_1Y() {
//		return R81_OVER6M_TO_1Y;
//	}
//	public void setR81_OVER6M_TO_1Y(BigDecimal r81_OVER6M_TO_1Y) {
//		R81_OVER6M_TO_1Y = r81_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR81_OVER1Y_TO_3Y() {
//		return R81_OVER1Y_TO_3Y;
//	}
//	public void setR81_OVER1Y_TO_3Y(BigDecimal r81_OVER1Y_TO_3Y) {
//		R81_OVER1Y_TO_3Y = r81_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR81_OVER3Y_TO_5Y() {
//		return R81_OVER3Y_TO_5Y;
//	}
//	public void setR81_OVER3Y_TO_5Y(BigDecimal r81_OVER3Y_TO_5Y) {
//		R81_OVER3Y_TO_5Y = r81_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR81_OVER5Y() {
//		return R81_OVER5Y;
//	}
//	public void setR81_OVER5Y(BigDecimal r81_OVER5Y) {
//		R81_OVER5Y = r81_OVER5Y;
//	}
//	public BigDecimal getR81_TOTAL() {
//		return R81_TOTAL;
//	}
//	public void setR81_TOTAL(BigDecimal r81_TOTAL) {
//		R81_TOTAL = r81_TOTAL;
//	}
//	public String getR82_PRODUCT() {
//		return R82_PRODUCT;
//	}
//	public void setR82_PRODUCT(String r82_PRODUCT) {
//		R82_PRODUCT = r82_PRODUCT;
//	}
//	public BigDecimal getR82_DAY1() {
//		return R82_DAY1;
//	}
//	public void setR82_DAY1(BigDecimal r82_DAY1) {
//		R82_DAY1 = r82_DAY1;
//	}
//	public BigDecimal getR82_DAY2_7() {
//		return R82_DAY2_7;
//	}
//	public void setR82_DAY2_7(BigDecimal r82_DAY2_7) {
//		R82_DAY2_7 = r82_DAY2_7;
//	}
//	public BigDecimal getR82_DAY8_14() {
//		return R82_DAY8_14;
//	}
//	public void setR82_DAY8_14(BigDecimal r82_DAY8_14) {
//		R82_DAY8_14 = r82_DAY8_14;
//	}
//	public BigDecimal getR82_DAY15_30() {
//		return R82_DAY15_30;
//	}
//	public void setR82_DAY15_30(BigDecimal r82_DAY15_30) {
//		R82_DAY15_30 = r82_DAY15_30;
//	}
//	public BigDecimal getR82_DAY31_TO_2M() {
//		return R82_DAY31_TO_2M;
//	}
//	public void setR82_DAY31_TO_2M(BigDecimal r82_DAY31_TO_2M) {
//		R82_DAY31_TO_2M = r82_DAY31_TO_2M;
//	}
//	public BigDecimal getR82_MORE2M_TO_3M() {
//		return R82_MORE2M_TO_3M;
//	}
//	public void setR82_MORE2M_TO_3M(BigDecimal r82_MORE2M_TO_3M) {
//		R82_MORE2M_TO_3M = r82_MORE2M_TO_3M;
//	}
//	public BigDecimal getR82_OVER3M_TO_6M() {
//		return R82_OVER3M_TO_6M;
//	}
//	public void setR82_OVER3M_TO_6M(BigDecimal r82_OVER3M_TO_6M) {
//		R82_OVER3M_TO_6M = r82_OVER3M_TO_6M;
//	}
//	public BigDecimal getR82_OVER6M_TO_1Y() {
//		return R82_OVER6M_TO_1Y;
//	}
//	public void setR82_OVER6M_TO_1Y(BigDecimal r82_OVER6M_TO_1Y) {
//		R82_OVER6M_TO_1Y = r82_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR82_OVER1Y_TO_3Y() {
//		return R82_OVER1Y_TO_3Y;
//	}
//	public void setR82_OVER1Y_TO_3Y(BigDecimal r82_OVER1Y_TO_3Y) {
//		R82_OVER1Y_TO_3Y = r82_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR82_OVER3Y_TO_5Y() {
//		return R82_OVER3Y_TO_5Y;
//	}
//	public void setR82_OVER3Y_TO_5Y(BigDecimal r82_OVER3Y_TO_5Y) {
//		R82_OVER3Y_TO_5Y = r82_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR82_OVER5Y() {
//		return R82_OVER5Y;
//	}
//	public void setR82_OVER5Y(BigDecimal r82_OVER5Y) {
//		R82_OVER5Y = r82_OVER5Y;
//	}
//	public BigDecimal getR82_TOTAL() {
//		return R82_TOTAL;
//	}
//	public void setR82_TOTAL(BigDecimal r82_TOTAL) {
//		R82_TOTAL = r82_TOTAL;
//	}
//	public String getR83_PRODUCT() {
//		return R83_PRODUCT;
//	}
//	public void setR83_PRODUCT(String r83_PRODUCT) {
//		R83_PRODUCT = r83_PRODUCT;
//	}
//	public BigDecimal getR83_DAY1() {
//		return R83_DAY1;
//	}
//	public void setR83_DAY1(BigDecimal r83_DAY1) {
//		R83_DAY1 = r83_DAY1;
//	}
//	public BigDecimal getR83_DAY2_7() {
//		return R83_DAY2_7;
//	}
//	public void setR83_DAY2_7(BigDecimal r83_DAY2_7) {
//		R83_DAY2_7 = r83_DAY2_7;
//	}
//	public BigDecimal getR83_DAY8_14() {
//		return R83_DAY8_14;
//	}
//	public void setR83_DAY8_14(BigDecimal r83_DAY8_14) {
//		R83_DAY8_14 = r83_DAY8_14;
//	}
//	public BigDecimal getR83_DAY15_30() {
//		return R83_DAY15_30;
//	}
//	public void setR83_DAY15_30(BigDecimal r83_DAY15_30) {
//		R83_DAY15_30 = r83_DAY15_30;
//	}
//	public BigDecimal getR83_DAY31_TO_2M() {
//		return R83_DAY31_TO_2M;
//	}
//	public void setR83_DAY31_TO_2M(BigDecimal r83_DAY31_TO_2M) {
//		R83_DAY31_TO_2M = r83_DAY31_TO_2M;
//	}
//	public BigDecimal getR83_MORE2M_TO_3M() {
//		return R83_MORE2M_TO_3M;
//	}
//	public void setR83_MORE2M_TO_3M(BigDecimal r83_MORE2M_TO_3M) {
//		R83_MORE2M_TO_3M = r83_MORE2M_TO_3M;
//	}
//	public BigDecimal getR83_OVER3M_TO_6M() {
//		return R83_OVER3M_TO_6M;
//	}
//	public void setR83_OVER3M_TO_6M(BigDecimal r83_OVER3M_TO_6M) {
//		R83_OVER3M_TO_6M = r83_OVER3M_TO_6M;
//	}
//	public BigDecimal getR83_OVER6M_TO_1Y() {
//		return R83_OVER6M_TO_1Y;
//	}
//	public void setR83_OVER6M_TO_1Y(BigDecimal r83_OVER6M_TO_1Y) {
//		R83_OVER6M_TO_1Y = r83_OVER6M_TO_1Y;
//	}
//	public BigDecimal getR83_OVER1Y_TO_3Y() {
//		return R83_OVER1Y_TO_3Y;
//	}
//	public void setR83_OVER1Y_TO_3Y(BigDecimal r83_OVER1Y_TO_3Y) {
//		R83_OVER1Y_TO_3Y = r83_OVER1Y_TO_3Y;
//	}
//	public BigDecimal getR83_OVER3Y_TO_5Y() {
//		return R83_OVER3Y_TO_5Y;
//	}
//	public void setR83_OVER3Y_TO_5Y(BigDecimal r83_OVER3Y_TO_5Y) {
//		R83_OVER3Y_TO_5Y = r83_OVER3Y_TO_5Y;
//	}
//	public BigDecimal getR83_OVER5Y() {
//		return R83_OVER5Y;
//	}
//	public void setR83_OVER5Y(BigDecimal r83_OVER5Y) {
//		R83_OVER5Y = r83_OVER5Y;
//	}
//	public BigDecimal getR83_TOTAL() {
//		return R83_TOTAL;
//	}
//	public void setR83_TOTAL(BigDecimal r83_TOTAL) {
//		R83_TOTAL = r83_TOTAL;
//	}
	public String getR84_PRODUCT() {
		return R84_PRODUCT;
	}
	public void setR84_PRODUCT(String r84_PRODUCT) {
		R84_PRODUCT = r84_PRODUCT;
	}
	public BigDecimal getR84_DAY1() {
		return R84_DAY1;
	}
	public void setR84_DAY1(BigDecimal r84_DAY1) {
		R84_DAY1 = r84_DAY1;
	}
	public BigDecimal getR84_DAY2_7() {
		return R84_DAY2_7;
	}
	public void setR84_DAY2_7(BigDecimal r84_DAY2_7) {
		R84_DAY2_7 = r84_DAY2_7;
	}
	public BigDecimal getR84_DAY8_14() {
		return R84_DAY8_14;
	}
	public void setR84_DAY8_14(BigDecimal r84_DAY8_14) {
		R84_DAY8_14 = r84_DAY8_14;
	}
	public BigDecimal getR84_DAY15_30() {
		return R84_DAY15_30;
	}
	public void setR84_DAY15_30(BigDecimal r84_DAY15_30) {
		R84_DAY15_30 = r84_DAY15_30;
	}
	public BigDecimal getR84_DAY31_TO_2M() {
		return R84_DAY31_TO_2M;
	}
	public void setR84_DAY31_TO_2M(BigDecimal r84_DAY31_TO_2M) {
		R84_DAY31_TO_2M = r84_DAY31_TO_2M;
	}
	public BigDecimal getR84_MORE2M_TO_3M() {
		return R84_MORE2M_TO_3M;
	}
	public void setR84_MORE2M_TO_3M(BigDecimal r84_MORE2M_TO_3M) {
		R84_MORE2M_TO_3M = r84_MORE2M_TO_3M;
	}
	public BigDecimal getR84_OVER3M_TO_6M() {
		return R84_OVER3M_TO_6M;
	}
	public void setR84_OVER3M_TO_6M(BigDecimal r84_OVER3M_TO_6M) {
		R84_OVER3M_TO_6M = r84_OVER3M_TO_6M;
	}
	public BigDecimal getR84_OVER6M_TO_1Y() {
		return R84_OVER6M_TO_1Y;
	}
	public void setR84_OVER6M_TO_1Y(BigDecimal r84_OVER6M_TO_1Y) {
		R84_OVER6M_TO_1Y = r84_OVER6M_TO_1Y;
	}
	public BigDecimal getR84_OVER1Y_TO_3Y() {
		return R84_OVER1Y_TO_3Y;
	}
	public void setR84_OVER1Y_TO_3Y(BigDecimal r84_OVER1Y_TO_3Y) {
		R84_OVER1Y_TO_3Y = r84_OVER1Y_TO_3Y;
	}
	public BigDecimal getR84_OVER3Y_TO_5Y() {
		return R84_OVER3Y_TO_5Y;
	}
	public void setR84_OVER3Y_TO_5Y(BigDecimal r84_OVER3Y_TO_5Y) {
		R84_OVER3Y_TO_5Y = r84_OVER3Y_TO_5Y;
	}
	public BigDecimal getR84_OVER5Y() {
		return R84_OVER5Y;
	}
	public void setR84_OVER5Y(BigDecimal r84_OVER5Y) {
		R84_OVER5Y = r84_OVER5Y;
	}
	public BigDecimal getR84_TOTAL() {
		return R84_TOTAL;
	}
	public void setR84_TOTAL(BigDecimal r84_TOTAL) {
		R84_TOTAL = r84_TOTAL;
	}
	public String getR85_PRODUCT() {
		return R85_PRODUCT;
	}
	public void setR85_PRODUCT(String r85_PRODUCT) {
		R85_PRODUCT = r85_PRODUCT;
	}
	public BigDecimal getR85_DAY1() {
		return R85_DAY1;
	}
	public void setR85_DAY1(BigDecimal r85_DAY1) {
		R85_DAY1 = r85_DAY1;
	}
	public BigDecimal getR85_DAY2_7() {
		return R85_DAY2_7;
	}
	public void setR85_DAY2_7(BigDecimal r85_DAY2_7) {
		R85_DAY2_7 = r85_DAY2_7;
	}
	public BigDecimal getR85_DAY8_14() {
		return R85_DAY8_14;
	}
	public void setR85_DAY8_14(BigDecimal r85_DAY8_14) {
		R85_DAY8_14 = r85_DAY8_14;
	}
	public BigDecimal getR85_DAY15_30() {
		return R85_DAY15_30;
	}
	public void setR85_DAY15_30(BigDecimal r85_DAY15_30) {
		R85_DAY15_30 = r85_DAY15_30;
	}
	public BigDecimal getR85_DAY31_TO_2M() {
		return R85_DAY31_TO_2M;
	}
	public void setR85_DAY31_TO_2M(BigDecimal r85_DAY31_TO_2M) {
		R85_DAY31_TO_2M = r85_DAY31_TO_2M;
	}
	public BigDecimal getR85_MORE2M_TO_3M() {
		return R85_MORE2M_TO_3M;
	}
	public void setR85_MORE2M_TO_3M(BigDecimal r85_MORE2M_TO_3M) {
		R85_MORE2M_TO_3M = r85_MORE2M_TO_3M;
	}
	public BigDecimal getR85_OVER3M_TO_6M() {
		return R85_OVER3M_TO_6M;
	}
	public void setR85_OVER3M_TO_6M(BigDecimal r85_OVER3M_TO_6M) {
		R85_OVER3M_TO_6M = r85_OVER3M_TO_6M;
	}
	public BigDecimal getR85_OVER6M_TO_1Y() {
		return R85_OVER6M_TO_1Y;
	}
	public void setR85_OVER6M_TO_1Y(BigDecimal r85_OVER6M_TO_1Y) {
		R85_OVER6M_TO_1Y = r85_OVER6M_TO_1Y;
	}
	public BigDecimal getR85_OVER1Y_TO_3Y() {
		return R85_OVER1Y_TO_3Y;
	}
	public void setR85_OVER1Y_TO_3Y(BigDecimal r85_OVER1Y_TO_3Y) {
		R85_OVER1Y_TO_3Y = r85_OVER1Y_TO_3Y;
	}
	public BigDecimal getR85_OVER3Y_TO_5Y() {
		return R85_OVER3Y_TO_5Y;
	}
	public void setR85_OVER3Y_TO_5Y(BigDecimal r85_OVER3Y_TO_5Y) {
		R85_OVER3Y_TO_5Y = r85_OVER3Y_TO_5Y;
	}
	public BigDecimal getR85_OVER5Y() {
		return R85_OVER5Y;
	}
	public void setR85_OVER5Y(BigDecimal r85_OVER5Y) {
		R85_OVER5Y = r85_OVER5Y;
	}
	public BigDecimal getR85_TOTAL() {
		return R85_TOTAL;
	}
	public void setR85_TOTAL(BigDecimal r85_TOTAL) {
		R85_TOTAL = r85_TOTAL;
	}
	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}
	public void setREPORT_DATE(Date rEPORT_DATE) {
		REPORT_DATE = rEPORT_DATE;
	}
	public String getREPORT_VERSION() {
		return REPORT_VERSION;
	}
	public void setREPORT_VERSION(String rEPORT_VERSION) {
		REPORT_VERSION = rEPORT_VERSION;
	}
	public String getREPORT_CURRENCY() {
		return REPORT_CURRENCY;
	}
	public void setREPORT_CURRENCY(String rEPORT_CURRENCY) {
		REPORT_CURRENCY = rEPORT_CURRENCY;
	}
	public String getREPORT_CODE() {
		return REPORT_CODE;
	}
	public void setREPORT_CODE(String rEPORT_CODE) {
		REPORT_CODE = rEPORT_CODE;
	}
	public String getREPORT_DESC() {
		return REPORT_DESC;
	}
	public void setREPORT_DESC(String rEPORT_DESC) {
		REPORT_DESC = rEPORT_DESC;
	}
	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}
	public void setENTITY_FLG(String eNTITY_FLG) {
		ENTITY_FLG = eNTITY_FLG;
	}
	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}
	public void setMODIFY_FLG(String mODIFY_FLG) {
		MODIFY_FLG = mODIFY_FLG;
	}
	public String getDEL_FLG() {
		return DEL_FLG;
	}
	public void setDEL_FLG(String dEL_FLG) {
		DEL_FLG = dEL_FLG;
	}
	public SLS_INPUT_SHT_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}



	}

	// ------------------------------
	// Entity class for SLS_INPUT_SHT Archival Detail Report
	// ------------------------------
	public static class SLS_INPUT_SHT_Archival_Detail_Entity {

	  @Column(name = "CUST_ID", length = 100)
	    private String custId;

	  @Id
	    @Column(name = "ACCT_NUMBER", length = 100)
	    private String acctNumber;

	    @Column(name = "ACCT_NAME", length = 100)
	    private String acctName;

	    @Column(name = "DATA_TYPE", length = 100)
	    private String dataType;
    
	    @Column(name = "ROW_ID", length = 100)
	    private String rowId;
    
	    @Column(name = "COLUMN_ID", length = 100)
	    private String columnId;

	    @Column(name = "REPORT_REMARKS", length = 100)
	    private String reportRemarks;

	    @Column(name = "MODIFICATION_REMARKS", length = 100)
	    private String modificationRemarks;

	    @Column(name = "DATA_ENTRY_VERSION", length = 100)
	    private String dataEntryVersion;

	    @Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
	    private BigDecimal acctBalanceInPula;
    
	    @Column(name = "PROVISION", precision = 32, scale = 2)
	    private BigDecimal provision;

	    @Column(name = "REPORT_DATE")
	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    private Date reportDate;

	    @Column(name = "REPORT_NAME", length = 100)
	    private String reportName;

	    @Column(name = "CREATE_USER", length = 50)
	    private String createUser;

	    @Column(name = "CREATE_TIME")
	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    private Date createTime;

	    @Column(name = "MODIFY_USER", length = 50)
	    private String modifyUser;

	    @Column(name = "MODIFY_TIME")
	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    private Date modifyTime;

	    @Column(name = "VERIFY_USER", length = 50)
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

		public String getRowId() {
			return rowId;
		}

		public void setRowId(String rowId) {
			this.rowId = rowId;
		}

		public String getColumnId() {
			return columnId;
		}

		public void setColumnId(String columnId) {
			this.columnId = columnId;
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

		public BigDecimal getAcctBalanceInPula() {
			return acctBalanceInPula;
		}

		public void setAcctBalanceInPula(BigDecimal acctBalanceInPula) {
			this.acctBalanceInPula = acctBalanceInPula;
		}

		public BigDecimal getProvision() {
			return provision;
		}

		public void setProvision(BigDecimal provision) {
			this.provision = provision;
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

		public SLS_INPUT_SHT_Archival_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

}
