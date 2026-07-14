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

@Component
@Service

public class BRRS_M_PLL_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_PLL_ReportService.class);

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
	// Retrieves the M_PLL report summary view
	// ------------------------------
	public ModelAndView getM_PLLView(String reportId, String fromdate, String todate, String currency, String dtltype,
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

			List<M_PLL_Archival_Summary_Entity> T1Master = new ArrayList<M_PLL_Archival_Summary_Entity>();
			if (version != null && parsedDate != null) {
				System.out.println(type);
				System.out.println(version);
				T1Master = jdbcTemplate.query("SELECT * FROM BRRS_M_PLL_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?", new Object[] { parsedDate, version }, new M_PLLArchivalSummaryRowMapper());
			}
			mv.addObject("reportsummary", T1Master);
		} else {
			List<M_PLL_Summary_Entity> T1Master = new ArrayList<M_PLL_Summary_Entity>();
			if (parsedDate != null) {
				T1Master = jdbcTemplate.query("SELECT * FROM BRRS_M_PLL_SUMMARYTABLE WHERE REPORT_DATE = ?", new Object[] { parsedDate }, new M_PLLSummaryRowMapper());
			}
			mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/M_PLL");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());

		return mv;
	}

	// ------------------------------
	// Retrieves the M_PLL report details view
	// ------------------------------
	public ModelAndView getM_PLLcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
				List<M_PLL_Archival_Detail_Entity> T1Dt1 = new ArrayList<>();
				if (version != null && parsedDate != null) {
					if (rowId != null && columnId != null) {
						T1Dt1 = jdbcTemplate.query("SELECT * FROM BRRS_M_PLL_ARCHIVALTABLE_DETAIL WHERE ROW_ID = ? AND COLUMN_ID = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?", new Object[] { rowId, columnId, parsedDate, version }, new M_PLLArchivalDetailRowMapper());
					} else {
						T1Dt1 = jdbcTemplate.query("SELECT * FROM BRRS_M_PLL_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?", new Object[] { parsedDate, version }, new M_PLLArchivalDetailRowMapper());
					}
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_PLL_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = jdbcTemplate.query("SELECT * FROM BRRS_M_PLL_DETAILTABLE WHERE ROW_ID = ? AND COLUMN_ID = ? AND REPORT_DATE = ?", new Object[] { rowId, columnId, parsedDate }, new M_PLLDetailRowMapper());
				} else {
					T1Dt1 = jdbcTemplate.query("SELECT * FROM BRRS_M_PLL_DETAILTABLE WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY", new Object[] { parsedDate, currentPage, pageSize }, new M_PLLDetailRowMapper());
					totalPages = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM BRRS_M_PLL_DETAILTABLE WHERE REPORT_DATE = ?", new Object[] { parsedDate }, Integer.class);
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
		mv.setViewName("BRRS/M_PLL");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	// ------------------------------
	// Generates Excel report for M_PLL summary
	// ------------------------------
	public byte[] getM_PLLExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_PLLARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<M_PLL_Summary_Entity> dataList = jdbcTemplate.query("SELECT * FROM BRRS_M_PLL_SUMMARYTABLE WHERE REPORT_DATE = ?", new Object[] { parseDateRobustly(todate) }, new M_PLLSummaryRowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_PLL report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}

		if (!Files.isReadable(templatePath)) {
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
			font.setFontHeightInPoints((short) 8); // size 8
			font.setFontName("Arial");
			CellStyle numberStyle = workbook.createCellStyle();
			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_PLL_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R12
					// Column B
					Cell cell6 = row.createCell(1);
					if (record.getR12_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR12_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					// R13
					// Column B
					cell6 = row.createCell(1);
					if (record.getR13_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR13_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(13);

					// R14
					// Column B
					cell6 = row.createCell(1);
					if (record.getR14_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR14_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(15);

					// R16
					// Column B
					cell6 = row.createCell(1);
					if (record.getR16_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR16_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(16);

					// R17
					// Column B
					cell6 = row.createCell(1);
					if (record.getR17_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR17_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(17);

					// R18
					// Column B
					cell6 = row.createCell(1);
					if (record.getR18_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR18_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(18);

					// R19
					// Column B
					cell6 = row.createCell(1);
					if (record.getR19_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR19_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(19);

					// R20
					// Column B
					cell6 = row.createCell(1);
					if (record.getR20_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR20_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(20);

					// R21
					// Column B
					cell6 = row.createCell(1);
					if (record.getR21_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR21_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(21);

					// R22
					// Column B
					cell6 = row.createCell(1);
					if (record.getR22_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR22_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(22);

					// R23
					// Column B
					cell6 = row.createCell(1);
					if (record.getR23_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR23_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(23);

					// R24
					// Column B
					cell6 = row.createCell(1);
					if (record.getR24_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR24_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(24);

					// R25
					// Column B
					cell6 = row.createCell(1);
					if (record.getR25_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR25_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(25);

					// R26
					// Column B
					cell6 = row.createCell(1);
					if (record.getR26_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR26_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(26);

					// R27
					// Column B
					cell6 = row.createCell(1);
					if (record.getR27_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR27_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(27);

					// R28
					// Column B
					cell6 = row.createCell(1);
					if (record.getR28_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR28_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(29);

					// R30
					// Column B
					cell6 = row.createCell(1);
					if (record.getR30_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR30_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(30);

					// R31
					// Column B
					cell6 = row.createCell(1);
					if (record.getR31_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR31_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(31);

					// R32
					// Column B
					cell6 = row.createCell(1);
					if (record.getR32_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR32_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(32);

					// R33
					// Column B
					cell6 = row.createCell(1);
					if (record.getR33_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR33_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(33);

					// R34
					// Column B
					cell6 = row.createCell(1);
					if (record.getR34_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR34_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(34);

					// R35
					// Column B
					cell6 = row.createCell(1);
					if (record.getR35_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR35_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(35);

					// R36
					// Column B
					cell6 = row.createCell(1);
					if (record.getR36_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR36_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(36);

					// R37
					// Column B
					cell6 = row.createCell(1);
					if (record.getR37_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR37_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(38);

					// R39
					// Column B
					cell6 = row.createCell(1);
					if (record.getR39_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR39_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(39);

					// R40
					// Column B
					cell6 = row.createCell(1);
					if (record.getR40_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR40_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(41);

					// R42
					// Column B
					cell6 = row.createCell(1);
					if (record.getR42_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR42_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(42);

					// R43
					// Column B
					cell6 = row.createCell(1);
					if (record.getR43_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR43_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(44);

					// R45
					// Column B
					cell6 = row.createCell(1);
					if (record.getR45_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR45_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(45);

					// R46
					// Column B
					cell6 = row.createCell(1);
					if (record.getR46_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR46_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(46);

					// R47
					// Column B
					cell6 = row.createCell(1);
					if (record.getR47_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR47_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(47);

					// R48
					// Column B
					cell6 = row.createCell(1);
					if (record.getR48_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR48_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(49);

					// R50
					// Column B
					cell6 = row.createCell(1);
					if (record.getR50_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR50_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(50);

					// R51
					// Column B
					cell6 = row.createCell(1);
					if (record.getR51_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR51_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(52);

					// R53
					// Column B
					cell6 = row.createCell(1);
					if (record.getR53_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR53_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(53);

					// R54
					// Column B
					cell6 = row.createCell(1);
					if (record.getR54_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR54_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(54);

					// R55
					// Column B
					cell6 = row.createCell(1);
					if (record.getR55_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR55_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(55);

					// R56
					// Column B
					cell6 = row.createCell(1);
					if (record.getR56_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR56_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(56);

					// R57
					// Column B
					cell6 = row.createCell(1);
					if (record.getR57_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR57_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(58);

					// R59
					// Column B
					cell6 = row.createCell(1);
					if (record.getR59_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR59_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(59);

					// R60
					// Column B
					cell6 = row.createCell(1);
					if (record.getR60_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR60_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(60);

					// R61
					// Column B
					cell6 = row.createCell(1);
					if (record.getR61_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR61_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(61);

					// R62
					// Column B
					cell6 = row.createCell(1);
					if (record.getR62_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR62_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(62);

					// R63
					// Column B
					cell6 = row.createCell(1);
					if (record.getR63_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR63_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_PLL SUMMARY", null, "BRRS_M_PLL_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	// ------------------------------
	// Generates Excel report for M_PLL details
	// ------------------------------
	public byte[] getM_PLLDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_PLL Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_PLLDetails");

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
			List<M_PLL_Detail_Entity> reportData = jdbcTemplate.query("SELECT * FROM BRRS_M_PLL_DETAILTABLE WHERE REPORT_DATE = ?", new Object[] { parsedToDate }, new M_PLLDetailRowMapper());

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_PLL_Detail_Entity item : reportData) {
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
				logger.info("No data found for M_PLL — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_PLL Excel", e);
			return new byte[0];
		}
	}

	// ------------------------------
	// Fetches M_PLL archival list
	// ------------------------------
	public List<Object> getM_PLLArchival() {
		List<Object> M_PLLArchivallist = new ArrayList<>();
		try {
			M_PLLArchivallist = jdbcTemplate.query("select REPORT_DATE, REPORT_VERSION from BRRS_M_PLL_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION DESC", (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION"), null });
			System.out.println("countser" + M_PLLArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_PLL Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_PLLArchivallist;
	}

	// ------------------------------
	// Finds the maximum version for a given report date
	// ------------------------------
	public BigDecimal findMaxVersion(Date reportDate) {
		try {
			String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_PLL_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
			return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
		} catch (Exception e) {
			logger.warn("No version found for date {}: {}", reportDate, e.getMessage());
			return null;
		}
	}

	// ------------------------------
	// Generates Excel report for M_PLL archival summary
	// ------------------------------
	public byte[] getExcelM_PLLARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_PLL_Archival_Summary_Entity> dataList = jdbcTemplate.query("SELECT * FROM BRRS_M_PLL_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?", new Object[] { parseDateRobustly(todate), version }, new M_PLLArchivalSummaryRowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_PLL report. Returning empty result.");
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
			font.setFontHeightInPoints((short) 8); // size 8
			font.setFontName("Arial");

			CellStyle numberStyle = workbook.createCellStyle();
			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_PLL_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R12
					// Column B
					Cell cell6 = row.createCell(1);
					if (record.getR12_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR12_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					// R13
					// Column B
					cell6 = row.createCell(1);
					if (record.getR13_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR13_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(13);

					// R14
					// Column B
					cell6 = row.createCell(1);
					if (record.getR14_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR14_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(15);

					// R16
					// Column B
					cell6 = row.createCell(1);
					if (record.getR16_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR16_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(16);

					// R17
					// Column B
					cell6 = row.createCell(1);
					if (record.getR17_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR17_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(17);

					// R18
					// Column B
					cell6 = row.createCell(1);
					if (record.getR18_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR18_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(18);

					// R19
					// Column B
					cell6 = row.createCell(1);
					if (record.getR19_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR19_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(19);

					// R20
					// Column B
					cell6 = row.createCell(1);
					if (record.getR20_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR20_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(20);

					// R21
					// Column B
					cell6 = row.createCell(1);
					if (record.getR21_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR21_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(21);

					// R22
					// Column B
					cell6 = row.createCell(1);
					if (record.getR22_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR22_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(22);

					// R23
					// Column B
					cell6 = row.createCell(1);
					if (record.getR23_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR23_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(23);

					// R24
					// Column B
					cell6 = row.createCell(1);
					if (record.getR24_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR24_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(24);

					// R25
					// Column B
					cell6 = row.createCell(1);
					if (record.getR25_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR25_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(25);

					// R26
					// Column B
					cell6 = row.createCell(1);
					if (record.getR26_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR26_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(26);

					// R27
					// Column B
					cell6 = row.createCell(1);
					if (record.getR27_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR27_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(27);

					// R28
					// Column B
					cell6 = row.createCell(1);
					if (record.getR28_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR28_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(29);

					// R30
					// Column B
					cell6 = row.createCell(1);
					if (record.getR30_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR30_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(30);

					// R31
					// Column B
					cell6 = row.createCell(1);
					if (record.getR31_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR31_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(31);

					// R32
					// Column B
					cell6 = row.createCell(1);
					if (record.getR32_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR32_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(32);

					// R33
					// Column B
					cell6 = row.createCell(1);
					if (record.getR33_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR33_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(33);

					// R34
					// Column B
					cell6 = row.createCell(1);
					if (record.getR34_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR34_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(34);

					// R35
					// Column B
					cell6 = row.createCell(1);
					if (record.getR35_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR35_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(35);

					// R36
					// Column B
					cell6 = row.createCell(1);
					if (record.getR36_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR36_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(36);

					// R37
					// Column B
					cell6 = row.createCell(1);
					if (record.getR37_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR37_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(38);

					// R39
					// Column B
					cell6 = row.createCell(1);
					if (record.getR39_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR39_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(39);

					// R40
					// Column B
					cell6 = row.createCell(1);
					if (record.getR40_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR40_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(41);

					// R42
					// Column B
					cell6 = row.createCell(1);
					if (record.getR42_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR42_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(42);

					// R43
					// Column B
					cell6 = row.createCell(1);
					if (record.getR43_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR43_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(44);

					// R45
					// Column B
					cell6 = row.createCell(1);
					if (record.getR45_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR45_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(45);

					// R46
					// Column B
					cell6 = row.createCell(1);
					if (record.getR46_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR46_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(46);

					// R47
					// Column B
					cell6 = row.createCell(1);
					if (record.getR47_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR47_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(47);

					// R48
					// Column B
					cell6 = row.createCell(1);
					if (record.getR48_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR48_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(49);

					// R50
					// Column B
					cell6 = row.createCell(1);
					if (record.getR50_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR50_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(50);

					// R51
					// Column B
					cell6 = row.createCell(1);
					if (record.getR51_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR51_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(52);

					// R53
					// Column B
					cell6 = row.createCell(1);
					if (record.getR53_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR53_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(53);

					// R54
					// Column B
					cell6 = row.createCell(1);
					if (record.getR54_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR54_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(54);

					// R55
					// Column B
					cell6 = row.createCell(1);
					if (record.getR55_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR55_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(55);

					// R56
					// Column B
					cell6 = row.createCell(1);
					if (record.getR56_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR56_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(56);

					// R57
					// Column B
					cell6 = row.createCell(1);
					if (record.getR57_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR57_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(58);

					// R59
					// Column B
					cell6 = row.createCell(1);
					if (record.getR59_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR59_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(59);

					// R60
					// Column B
					cell6 = row.createCell(1);
					if (record.getR60_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR60_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(60);

					// R61
					// Column B
					cell6 = row.createCell(1);
					if (record.getR61_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR61_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(61);

					// R62
					// Column B
					cell6 = row.createCell(1);
					if (record.getR62_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR62_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(62);

					// R63
					// Column B
					cell6 = row.createCell(1);
					if (record.getR63_provi_loan_loss() != null) {
						cell6.setCellValue(record.getR63_provi_loan_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_PLL ARCHIVAL SUMMARY", null, "BRRS_M_PLL_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// ------------------------------
	// Generates Excel report for M_PLL archival details
	// ------------------------------
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_PLL ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_PLLDetails");

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
			
			List<M_PLL_Archival_Detail_Entity> reportData = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_PLL_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
					new Object[] { parsedToDate, version },
					new M_PLLArchivalDetailRowMapper()
			);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_PLL_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for M_PLL — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_PLL Excel", e);
			return new byte[0];
		}
	}

//public boolean updateProvision(M_PLL_Detail_Entity mpllData) {
//    try {
//        M_PLL_Detail_Entity existing = BRRS_M_PLL_Detail_Repo.findByAcctNumber(mpllData.getAcctNumber());
//        
//        System.out.println("came to services");
//        if (existing != null) {
//            existing.setProvision(mpllData.getProvision());
//            existing.setAcctName(mpllData.getAcctName());
//            
//            
//            BRRS_jdbcTemplate.update("UPDATE BRRS_M_PLL_DETAILTABLE SET ACCT_NAME = ?, PROVISION = ? WHERE ACCT_NUMBER = ?", existing.getAcctName(), existing.getProvision(), existing.getAcctNumber());
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
		ModelAndView mv = new ModelAndView("BRRS/M_PLL"); 

		if (acctNo != null) {
			List<M_PLL_Detail_Entity> list = jdbcTemplate.query("SELECT * FROM BRRS_M_PLL_DETAILTABLE WHERE ACCT_NUMBER = ?", new Object[] { acctNo }, new M_PLLDetailRowMapper());
			M_PLL_Detail_Entity mpllEntity = list.isEmpty() ? null : list.get(0);
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
	// Updates the M_PLL detail record and triggers summary procedure
	// ------------------------------
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("provision");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			List<M_PLL_Detail_Entity> list = jdbcTemplate.query("SELECT * FROM BRRS_M_PLL_DETAILTABLE WHERE ACCT_NUMBER = ?", new Object[] { acctNo }, new M_PLLDetailRowMapper());
			M_PLL_Detail_Entity existing = list.isEmpty() ? null : list.get(0);
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
				jdbcTemplate.update("UPDATE BRRS_M_PLL_DETAILTABLE SET ACCT_NAME = ?, PROVISION = ? WHERE ACCT_NUMBER = ?", existing.getAcctName(), existing.getProvision(), existing.getAcctNumber());
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_M_PLL_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_M_PLL_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating M_PLL record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	// ------------------------------
	// Entity class for M_PLL Summary Report
	// ------------------------------
	public static class M_PLL_Summary_Entity {	

	private String r11_provi_ins_sec;
	private BigDecimal r11_provi_loan_loss;
	private String r12_provi_ins_sec;
	private BigDecimal r12_provi_loan_loss;
	private String r13_provi_ins_sec;
	private BigDecimal r13_provi_loan_loss;
	private String r14_provi_ins_sec;
	private BigDecimal r14_provi_loan_loss;
	private String r15_provi_ins_sec;
	private BigDecimal r15_provi_loan_loss;
	private String r16_provi_ins_sec;
	private BigDecimal r16_provi_loan_loss;
	private String r17_provi_ins_sec;
	private BigDecimal r17_provi_loan_loss;
	private String r18_provi_ins_sec;
	private BigDecimal r18_provi_loan_loss;
	private String r19_provi_ins_sec;
	private BigDecimal r19_provi_loan_loss;
	private String r20_provi_ins_sec;
	private BigDecimal r20_provi_loan_loss;
	private String r21_provi_ins_sec;
	private BigDecimal r21_provi_loan_loss;
	private String r22_provi_ins_sec;
	private BigDecimal r22_provi_loan_loss;
	private String r23_provi_ins_sec;
	private BigDecimal r23_provi_loan_loss;
	private String r24_provi_ins_sec;
	private BigDecimal r24_provi_loan_loss;
	private String r25_provi_ins_sec;
	private BigDecimal r25_provi_loan_loss;
	private String r26_provi_ins_sec;
	private BigDecimal r26_provi_loan_loss;
	private String r27_provi_ins_sec;
	private BigDecimal r27_provi_loan_loss;
	private String r28_provi_ins_sec;
	private BigDecimal r28_provi_loan_loss;
	private String r29_provi_ins_sec;
	private BigDecimal r29_provi_loan_loss;
	private String r30_provi_ins_sec;
	private BigDecimal r30_provi_loan_loss;
	private String r31_provi_ins_sec;
	private BigDecimal r31_provi_loan_loss;
	private String r32_provi_ins_sec;
	private BigDecimal r32_provi_loan_loss;
	private String r33_provi_ins_sec;
	private BigDecimal r33_provi_loan_loss;
	private String r34_provi_ins_sec;
	private BigDecimal r34_provi_loan_loss;
	private String r35_provi_ins_sec;
	private BigDecimal r35_provi_loan_loss;
	private String r36_provi_ins_sec;
	private BigDecimal r36_provi_loan_loss;
	private String r37_provi_ins_sec;
	private BigDecimal r37_provi_loan_loss;
	private String r38_provi_ins_sec;
	private BigDecimal r38_provi_loan_loss;
	private String r39_provi_ins_sec;
	private BigDecimal r39_provi_loan_loss;
	private String r40_provi_ins_sec;
	private BigDecimal r40_provi_loan_loss;
	private String r41_provi_ins_sec;
	private BigDecimal r41_provi_loan_loss;
	private String r42_provi_ins_sec;
	private BigDecimal r42_provi_loan_loss;
	private String r43_provi_ins_sec;
	private BigDecimal r43_provi_loan_loss;
	private String r44_provi_ins_sec;
	private BigDecimal r44_provi_loan_loss;
	private String r45_provi_ins_sec;
	private BigDecimal r45_provi_loan_loss;
	private String r46_provi_ins_sec;
	private BigDecimal r46_provi_loan_loss;
	private String r47_provi_ins_sec;
	private BigDecimal r47_provi_loan_loss;
	private String r48_provi_ins_sec;
	private BigDecimal r48_provi_loan_loss;
	private String r49_provi_ins_sec;
	private BigDecimal r49_provi_loan_loss;
	private String r50_provi_ins_sec;
	private BigDecimal r50_provi_loan_loss;
	private String r51_provi_ins_sec;
	private BigDecimal r51_provi_loan_loss;
	private String r52_provi_ins_sec;
	private BigDecimal r52_provi_loan_loss;
	private String r53_provi_ins_sec;
	private BigDecimal r53_provi_loan_loss;
	private String r54_provi_ins_sec;
	private BigDecimal r54_provi_loan_loss;
	private String r55_provi_ins_sec;
	private BigDecimal r55_provi_loan_loss;
	private String r56_provi_ins_sec;
	private BigDecimal r56_provi_loan_loss;
	private String r57_provi_ins_sec;
	private BigDecimal r57_provi_loan_loss;
	private String r58_provi_ins_sec;
	private BigDecimal r58_provi_loan_loss;
	private String r59_provi_ins_sec;
	private BigDecimal r59_provi_loan_loss;
	private String r60_provi_ins_sec;
	private BigDecimal r60_provi_loan_loss;
	private String r61_provi_ins_sec;
	private BigDecimal r61_provi_loan_loss;
	private String r62_provi_ins_sec;
	private BigDecimal r62_provi_loan_loss;
	private String r63_provi_ins_sec;
	private BigDecimal r63_provi_loan_loss;
	private String r64_provi_ins_sec;
	private BigDecimal r64_provi_loan_loss;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR11_provi_ins_sec() {
			return r11_provi_ins_sec;
		}

		public void setR11_provi_ins_sec(String r11_provi_ins_sec) {
			this.r11_provi_ins_sec = r11_provi_ins_sec;
		}

		public BigDecimal getR11_provi_loan_loss() {
			return r11_provi_loan_loss;
		}

		public void setR11_provi_loan_loss(BigDecimal r11_provi_loan_loss) {
			this.r11_provi_loan_loss = r11_provi_loan_loss;
		}

		public String getR12_provi_ins_sec() {
			return r12_provi_ins_sec;
		}

		public void setR12_provi_ins_sec(String r12_provi_ins_sec) {
			this.r12_provi_ins_sec = r12_provi_ins_sec;
		}

		public BigDecimal getR12_provi_loan_loss() {
			return r12_provi_loan_loss;
		}

		public void setR12_provi_loan_loss(BigDecimal r12_provi_loan_loss) {
			this.r12_provi_loan_loss = r12_provi_loan_loss;
		}

		public String getR13_provi_ins_sec() {
			return r13_provi_ins_sec;
		}

		public void setR13_provi_ins_sec(String r13_provi_ins_sec) {
			this.r13_provi_ins_sec = r13_provi_ins_sec;
		}

		public BigDecimal getR13_provi_loan_loss() {
			return r13_provi_loan_loss;
		}

		public void setR13_provi_loan_loss(BigDecimal r13_provi_loan_loss) {
			this.r13_provi_loan_loss = r13_provi_loan_loss;
		}

		public String getR14_provi_ins_sec() {
			return r14_provi_ins_sec;
		}

		public void setR14_provi_ins_sec(String r14_provi_ins_sec) {
			this.r14_provi_ins_sec = r14_provi_ins_sec;
		}

		public BigDecimal getR14_provi_loan_loss() {
			return r14_provi_loan_loss;
		}

		public void setR14_provi_loan_loss(BigDecimal r14_provi_loan_loss) {
			this.r14_provi_loan_loss = r14_provi_loan_loss;
		}

		public String getR15_provi_ins_sec() {
			return r15_provi_ins_sec;
		}

		public void setR15_provi_ins_sec(String r15_provi_ins_sec) {
			this.r15_provi_ins_sec = r15_provi_ins_sec;
		}

		public BigDecimal getR15_provi_loan_loss() {
			return r15_provi_loan_loss;
		}

		public void setR15_provi_loan_loss(BigDecimal r15_provi_loan_loss) {
			this.r15_provi_loan_loss = r15_provi_loan_loss;
		}

		public String getR16_provi_ins_sec() {
			return r16_provi_ins_sec;
		}

		public void setR16_provi_ins_sec(String r16_provi_ins_sec) {
			this.r16_provi_ins_sec = r16_provi_ins_sec;
		}

		public BigDecimal getR16_provi_loan_loss() {
			return r16_provi_loan_loss;
		}

		public void setR16_provi_loan_loss(BigDecimal r16_provi_loan_loss) {
			this.r16_provi_loan_loss = r16_provi_loan_loss;
		}

		public String getR17_provi_ins_sec() {
			return r17_provi_ins_sec;
		}

		public void setR17_provi_ins_sec(String r17_provi_ins_sec) {
			this.r17_provi_ins_sec = r17_provi_ins_sec;
		}

		public BigDecimal getR17_provi_loan_loss() {
			return r17_provi_loan_loss;
		}

		public void setR17_provi_loan_loss(BigDecimal r17_provi_loan_loss) {
			this.r17_provi_loan_loss = r17_provi_loan_loss;
		}

		public String getR18_provi_ins_sec() {
			return r18_provi_ins_sec;
		}

		public void setR18_provi_ins_sec(String r18_provi_ins_sec) {
			this.r18_provi_ins_sec = r18_provi_ins_sec;
		}

		public BigDecimal getR18_provi_loan_loss() {
			return r18_provi_loan_loss;
		}

		public void setR18_provi_loan_loss(BigDecimal r18_provi_loan_loss) {
			this.r18_provi_loan_loss = r18_provi_loan_loss;
		}

		public String getR19_provi_ins_sec() {
			return r19_provi_ins_sec;
		}

		public void setR19_provi_ins_sec(String r19_provi_ins_sec) {
			this.r19_provi_ins_sec = r19_provi_ins_sec;
		}

		public BigDecimal getR19_provi_loan_loss() {
			return r19_provi_loan_loss;
		}

		public void setR19_provi_loan_loss(BigDecimal r19_provi_loan_loss) {
			this.r19_provi_loan_loss = r19_provi_loan_loss;
		}

		public String getR20_provi_ins_sec() {
			return r20_provi_ins_sec;
		}

		public void setR20_provi_ins_sec(String r20_provi_ins_sec) {
			this.r20_provi_ins_sec = r20_provi_ins_sec;
		}

		public BigDecimal getR20_provi_loan_loss() {
			return r20_provi_loan_loss;
		}

		public void setR20_provi_loan_loss(BigDecimal r20_provi_loan_loss) {
			this.r20_provi_loan_loss = r20_provi_loan_loss;
		}

		public String getR21_provi_ins_sec() {
			return r21_provi_ins_sec;
		}

		public void setR21_provi_ins_sec(String r21_provi_ins_sec) {
			this.r21_provi_ins_sec = r21_provi_ins_sec;
		}

		public BigDecimal getR21_provi_loan_loss() {
			return r21_provi_loan_loss;
		}

		public void setR21_provi_loan_loss(BigDecimal r21_provi_loan_loss) {
			this.r21_provi_loan_loss = r21_provi_loan_loss;
		}

		public String getR22_provi_ins_sec() {
			return r22_provi_ins_sec;
		}

		public void setR22_provi_ins_sec(String r22_provi_ins_sec) {
			this.r22_provi_ins_sec = r22_provi_ins_sec;
		}

		public BigDecimal getR22_provi_loan_loss() {
			return r22_provi_loan_loss;
		}

		public void setR22_provi_loan_loss(BigDecimal r22_provi_loan_loss) {
			this.r22_provi_loan_loss = r22_provi_loan_loss;
		}

		public String getR23_provi_ins_sec() {
			return r23_provi_ins_sec;
		}

		public void setR23_provi_ins_sec(String r23_provi_ins_sec) {
			this.r23_provi_ins_sec = r23_provi_ins_sec;
		}

		public BigDecimal getR23_provi_loan_loss() {
			return r23_provi_loan_loss;
		}

		public void setR23_provi_loan_loss(BigDecimal r23_provi_loan_loss) {
			this.r23_provi_loan_loss = r23_provi_loan_loss;
		}

		public String getR24_provi_ins_sec() {
			return r24_provi_ins_sec;
		}

		public void setR24_provi_ins_sec(String r24_provi_ins_sec) {
			this.r24_provi_ins_sec = r24_provi_ins_sec;
		}

		public BigDecimal getR24_provi_loan_loss() {
			return r24_provi_loan_loss;
		}

		public void setR24_provi_loan_loss(BigDecimal r24_provi_loan_loss) {
			this.r24_provi_loan_loss = r24_provi_loan_loss;
		}

		public String getR25_provi_ins_sec() {
			return r25_provi_ins_sec;
		}

		public void setR25_provi_ins_sec(String r25_provi_ins_sec) {
			this.r25_provi_ins_sec = r25_provi_ins_sec;
		}

		public BigDecimal getR25_provi_loan_loss() {
			return r25_provi_loan_loss;
		}

		public void setR25_provi_loan_loss(BigDecimal r25_provi_loan_loss) {
			this.r25_provi_loan_loss = r25_provi_loan_loss;
		}

		public String getR26_provi_ins_sec() {
			return r26_provi_ins_sec;
		}

		public void setR26_provi_ins_sec(String r26_provi_ins_sec) {
			this.r26_provi_ins_sec = r26_provi_ins_sec;
		}

		public BigDecimal getR26_provi_loan_loss() {
			return r26_provi_loan_loss;
		}

		public void setR26_provi_loan_loss(BigDecimal r26_provi_loan_loss) {
			this.r26_provi_loan_loss = r26_provi_loan_loss;
		}

		public String getR27_provi_ins_sec() {
			return r27_provi_ins_sec;
		}

		public void setR27_provi_ins_sec(String r27_provi_ins_sec) {
			this.r27_provi_ins_sec = r27_provi_ins_sec;
		}

		public BigDecimal getR27_provi_loan_loss() {
			return r27_provi_loan_loss;
		}

		public void setR27_provi_loan_loss(BigDecimal r27_provi_loan_loss) {
			this.r27_provi_loan_loss = r27_provi_loan_loss;
		}

		public String getR28_provi_ins_sec() {
			return r28_provi_ins_sec;
		}

		public void setR28_provi_ins_sec(String r28_provi_ins_sec) {
			this.r28_provi_ins_sec = r28_provi_ins_sec;
		}

		public BigDecimal getR28_provi_loan_loss() {
			return r28_provi_loan_loss;
		}

		public void setR28_provi_loan_loss(BigDecimal r28_provi_loan_loss) {
			this.r28_provi_loan_loss = r28_provi_loan_loss;
		}

		public String getR29_provi_ins_sec() {
			return r29_provi_ins_sec;
		}

		public void setR29_provi_ins_sec(String r29_provi_ins_sec) {
			this.r29_provi_ins_sec = r29_provi_ins_sec;
		}

		public BigDecimal getR29_provi_loan_loss() {
			return r29_provi_loan_loss;
		}

		public void setR29_provi_loan_loss(BigDecimal r29_provi_loan_loss) {
			this.r29_provi_loan_loss = r29_provi_loan_loss;
		}

		public String getR30_provi_ins_sec() {
			return r30_provi_ins_sec;
		}

		public void setR30_provi_ins_sec(String r30_provi_ins_sec) {
			this.r30_provi_ins_sec = r30_provi_ins_sec;
		}

		public BigDecimal getR30_provi_loan_loss() {
			return r30_provi_loan_loss;
		}

		public void setR30_provi_loan_loss(BigDecimal r30_provi_loan_loss) {
			this.r30_provi_loan_loss = r30_provi_loan_loss;
		}

		public String getR31_provi_ins_sec() {
			return r31_provi_ins_sec;
		}

		public void setR31_provi_ins_sec(String r31_provi_ins_sec) {
			this.r31_provi_ins_sec = r31_provi_ins_sec;
		}

		public BigDecimal getR31_provi_loan_loss() {
			return r31_provi_loan_loss;
		}

		public void setR31_provi_loan_loss(BigDecimal r31_provi_loan_loss) {
			this.r31_provi_loan_loss = r31_provi_loan_loss;
		}

		public String getR32_provi_ins_sec() {
			return r32_provi_ins_sec;
		}

		public void setR32_provi_ins_sec(String r32_provi_ins_sec) {
			this.r32_provi_ins_sec = r32_provi_ins_sec;
		}

		public BigDecimal getR32_provi_loan_loss() {
			return r32_provi_loan_loss;
		}

		public void setR32_provi_loan_loss(BigDecimal r32_provi_loan_loss) {
			this.r32_provi_loan_loss = r32_provi_loan_loss;
		}

		public String getR33_provi_ins_sec() {
			return r33_provi_ins_sec;
		}

		public void setR33_provi_ins_sec(String r33_provi_ins_sec) {
			this.r33_provi_ins_sec = r33_provi_ins_sec;
		}

		public BigDecimal getR33_provi_loan_loss() {
			return r33_provi_loan_loss;
		}

		public void setR33_provi_loan_loss(BigDecimal r33_provi_loan_loss) {
			this.r33_provi_loan_loss = r33_provi_loan_loss;
		}

		public String getR34_provi_ins_sec() {
			return r34_provi_ins_sec;
		}

		public void setR34_provi_ins_sec(String r34_provi_ins_sec) {
			this.r34_provi_ins_sec = r34_provi_ins_sec;
		}

		public BigDecimal getR34_provi_loan_loss() {
			return r34_provi_loan_loss;
		}

		public void setR34_provi_loan_loss(BigDecimal r34_provi_loan_loss) {
			this.r34_provi_loan_loss = r34_provi_loan_loss;
		}

		public String getR35_provi_ins_sec() {
			return r35_provi_ins_sec;
		}

		public void setR35_provi_ins_sec(String r35_provi_ins_sec) {
			this.r35_provi_ins_sec = r35_provi_ins_sec;
		}

		public BigDecimal getR35_provi_loan_loss() {
			return r35_provi_loan_loss;
		}

		public void setR35_provi_loan_loss(BigDecimal r35_provi_loan_loss) {
			this.r35_provi_loan_loss = r35_provi_loan_loss;
		}

		public String getR36_provi_ins_sec() {
			return r36_provi_ins_sec;
		}

		public void setR36_provi_ins_sec(String r36_provi_ins_sec) {
			this.r36_provi_ins_sec = r36_provi_ins_sec;
		}

		public BigDecimal getR36_provi_loan_loss() {
			return r36_provi_loan_loss;
		}

		public void setR36_provi_loan_loss(BigDecimal r36_provi_loan_loss) {
			this.r36_provi_loan_loss = r36_provi_loan_loss;
		}

		public String getR37_provi_ins_sec() {
			return r37_provi_ins_sec;
		}

		public void setR37_provi_ins_sec(String r37_provi_ins_sec) {
			this.r37_provi_ins_sec = r37_provi_ins_sec;
		}

		public BigDecimal getR37_provi_loan_loss() {
			return r37_provi_loan_loss;
		}

		public void setR37_provi_loan_loss(BigDecimal r37_provi_loan_loss) {
			this.r37_provi_loan_loss = r37_provi_loan_loss;
		}

		public String getR38_provi_ins_sec() {
			return r38_provi_ins_sec;
		}

		public void setR38_provi_ins_sec(String r38_provi_ins_sec) {
			this.r38_provi_ins_sec = r38_provi_ins_sec;
		}

		public BigDecimal getR38_provi_loan_loss() {
			return r38_provi_loan_loss;
		}

		public void setR38_provi_loan_loss(BigDecimal r38_provi_loan_loss) {
			this.r38_provi_loan_loss = r38_provi_loan_loss;
		}

		public String getR39_provi_ins_sec() {
			return r39_provi_ins_sec;
		}

		public void setR39_provi_ins_sec(String r39_provi_ins_sec) {
			this.r39_provi_ins_sec = r39_provi_ins_sec;
		}

		public BigDecimal getR39_provi_loan_loss() {
			return r39_provi_loan_loss;
		}

		public void setR39_provi_loan_loss(BigDecimal r39_provi_loan_loss) {
			this.r39_provi_loan_loss = r39_provi_loan_loss;
		}

		public String getR40_provi_ins_sec() {
			return r40_provi_ins_sec;
		}

		public void setR40_provi_ins_sec(String r40_provi_ins_sec) {
			this.r40_provi_ins_sec = r40_provi_ins_sec;
		}

		public BigDecimal getR40_provi_loan_loss() {
			return r40_provi_loan_loss;
		}

		public void setR40_provi_loan_loss(BigDecimal r40_provi_loan_loss) {
			this.r40_provi_loan_loss = r40_provi_loan_loss;
		}

		public String getR41_provi_ins_sec() {
			return r41_provi_ins_sec;
		}

		public void setR41_provi_ins_sec(String r41_provi_ins_sec) {
			this.r41_provi_ins_sec = r41_provi_ins_sec;
		}

		public BigDecimal getR41_provi_loan_loss() {
			return r41_provi_loan_loss;
		}

		public void setR41_provi_loan_loss(BigDecimal r41_provi_loan_loss) {
			this.r41_provi_loan_loss = r41_provi_loan_loss;
		}

		public String getR42_provi_ins_sec() {
			return r42_provi_ins_sec;
		}

		public void setR42_provi_ins_sec(String r42_provi_ins_sec) {
			this.r42_provi_ins_sec = r42_provi_ins_sec;
		}

		public BigDecimal getR42_provi_loan_loss() {
			return r42_provi_loan_loss;
		}

		public void setR42_provi_loan_loss(BigDecimal r42_provi_loan_loss) {
			this.r42_provi_loan_loss = r42_provi_loan_loss;
		}

		public String getR43_provi_ins_sec() {
			return r43_provi_ins_sec;
		}

		public void setR43_provi_ins_sec(String r43_provi_ins_sec) {
			this.r43_provi_ins_sec = r43_provi_ins_sec;
		}

		public BigDecimal getR43_provi_loan_loss() {
			return r43_provi_loan_loss;
		}

		public void setR43_provi_loan_loss(BigDecimal r43_provi_loan_loss) {
			this.r43_provi_loan_loss = r43_provi_loan_loss;
		}

		public String getR44_provi_ins_sec() {
			return r44_provi_ins_sec;
		}

		public void setR44_provi_ins_sec(String r44_provi_ins_sec) {
			this.r44_provi_ins_sec = r44_provi_ins_sec;
		}

		public BigDecimal getR44_provi_loan_loss() {
			return r44_provi_loan_loss;
		}

		public void setR44_provi_loan_loss(BigDecimal r44_provi_loan_loss) {
			this.r44_provi_loan_loss = r44_provi_loan_loss;
		}

		public String getR45_provi_ins_sec() {
			return r45_provi_ins_sec;
		}

		public void setR45_provi_ins_sec(String r45_provi_ins_sec) {
			this.r45_provi_ins_sec = r45_provi_ins_sec;
		}

		public BigDecimal getR45_provi_loan_loss() {
			return r45_provi_loan_loss;
		}

		public void setR45_provi_loan_loss(BigDecimal r45_provi_loan_loss) {
			this.r45_provi_loan_loss = r45_provi_loan_loss;
		}

		public String getR46_provi_ins_sec() {
			return r46_provi_ins_sec;
		}

		public void setR46_provi_ins_sec(String r46_provi_ins_sec) {
			this.r46_provi_ins_sec = r46_provi_ins_sec;
		}

		public BigDecimal getR46_provi_loan_loss() {
			return r46_provi_loan_loss;
		}

		public void setR46_provi_loan_loss(BigDecimal r46_provi_loan_loss) {
			this.r46_provi_loan_loss = r46_provi_loan_loss;
		}

		public String getR47_provi_ins_sec() {
			return r47_provi_ins_sec;
		}

		public void setR47_provi_ins_sec(String r47_provi_ins_sec) {
			this.r47_provi_ins_sec = r47_provi_ins_sec;
		}

		public BigDecimal getR47_provi_loan_loss() {
			return r47_provi_loan_loss;
		}

		public void setR47_provi_loan_loss(BigDecimal r47_provi_loan_loss) {
			this.r47_provi_loan_loss = r47_provi_loan_loss;
		}

		public String getR48_provi_ins_sec() {
			return r48_provi_ins_sec;
		}

		public void setR48_provi_ins_sec(String r48_provi_ins_sec) {
			this.r48_provi_ins_sec = r48_provi_ins_sec;
		}

		public BigDecimal getR48_provi_loan_loss() {
			return r48_provi_loan_loss;
		}

		public void setR48_provi_loan_loss(BigDecimal r48_provi_loan_loss) {
			this.r48_provi_loan_loss = r48_provi_loan_loss;
		}

		public String getR49_provi_ins_sec() {
			return r49_provi_ins_sec;
		}

		public void setR49_provi_ins_sec(String r49_provi_ins_sec) {
			this.r49_provi_ins_sec = r49_provi_ins_sec;
		}

		public BigDecimal getR49_provi_loan_loss() {
			return r49_provi_loan_loss;
		}

		public void setR49_provi_loan_loss(BigDecimal r49_provi_loan_loss) {
			this.r49_provi_loan_loss = r49_provi_loan_loss;
		}

		public String getR50_provi_ins_sec() {
			return r50_provi_ins_sec;
		}

		public void setR50_provi_ins_sec(String r50_provi_ins_sec) {
			this.r50_provi_ins_sec = r50_provi_ins_sec;
		}

		public BigDecimal getR50_provi_loan_loss() {
			return r50_provi_loan_loss;
		}

		public void setR50_provi_loan_loss(BigDecimal r50_provi_loan_loss) {
			this.r50_provi_loan_loss = r50_provi_loan_loss;
		}

		public String getR51_provi_ins_sec() {
			return r51_provi_ins_sec;
		}

		public void setR51_provi_ins_sec(String r51_provi_ins_sec) {
			this.r51_provi_ins_sec = r51_provi_ins_sec;
		}

		public BigDecimal getR51_provi_loan_loss() {
			return r51_provi_loan_loss;
		}

		public void setR51_provi_loan_loss(BigDecimal r51_provi_loan_loss) {
			this.r51_provi_loan_loss = r51_provi_loan_loss;
		}

		public String getR52_provi_ins_sec() {
			return r52_provi_ins_sec;
		}

		public void setR52_provi_ins_sec(String r52_provi_ins_sec) {
			this.r52_provi_ins_sec = r52_provi_ins_sec;
		}

		public BigDecimal getR52_provi_loan_loss() {
			return r52_provi_loan_loss;
		}

		public void setR52_provi_loan_loss(BigDecimal r52_provi_loan_loss) {
			this.r52_provi_loan_loss = r52_provi_loan_loss;
		}

		public String getR53_provi_ins_sec() {
			return r53_provi_ins_sec;
		}

		public void setR53_provi_ins_sec(String r53_provi_ins_sec) {
			this.r53_provi_ins_sec = r53_provi_ins_sec;
		}

		public BigDecimal getR53_provi_loan_loss() {
			return r53_provi_loan_loss;
		}

		public void setR53_provi_loan_loss(BigDecimal r53_provi_loan_loss) {
			this.r53_provi_loan_loss = r53_provi_loan_loss;
		}

		public String getR54_provi_ins_sec() {
			return r54_provi_ins_sec;
		}

		public void setR54_provi_ins_sec(String r54_provi_ins_sec) {
			this.r54_provi_ins_sec = r54_provi_ins_sec;
		}

		public BigDecimal getR54_provi_loan_loss() {
			return r54_provi_loan_loss;
		}

		public void setR54_provi_loan_loss(BigDecimal r54_provi_loan_loss) {
			this.r54_provi_loan_loss = r54_provi_loan_loss;
		}

		public String getR55_provi_ins_sec() {
			return r55_provi_ins_sec;
		}

		public void setR55_provi_ins_sec(String r55_provi_ins_sec) {
			this.r55_provi_ins_sec = r55_provi_ins_sec;
		}

		public BigDecimal getR55_provi_loan_loss() {
			return r55_provi_loan_loss;
		}

		public void setR55_provi_loan_loss(BigDecimal r55_provi_loan_loss) {
			this.r55_provi_loan_loss = r55_provi_loan_loss;
		}

		public String getR56_provi_ins_sec() {
			return r56_provi_ins_sec;
		}

		public void setR56_provi_ins_sec(String r56_provi_ins_sec) {
			this.r56_provi_ins_sec = r56_provi_ins_sec;
		}

		public BigDecimal getR56_provi_loan_loss() {
			return r56_provi_loan_loss;
		}

		public void setR56_provi_loan_loss(BigDecimal r56_provi_loan_loss) {
			this.r56_provi_loan_loss = r56_provi_loan_loss;
		}

		public String getR57_provi_ins_sec() {
			return r57_provi_ins_sec;
		}

		public void setR57_provi_ins_sec(String r57_provi_ins_sec) {
			this.r57_provi_ins_sec = r57_provi_ins_sec;
		}

		public BigDecimal getR57_provi_loan_loss() {
			return r57_provi_loan_loss;
		}

		public void setR57_provi_loan_loss(BigDecimal r57_provi_loan_loss) {
			this.r57_provi_loan_loss = r57_provi_loan_loss;
		}

		public String getR58_provi_ins_sec() {
			return r58_provi_ins_sec;
		}

		public void setR58_provi_ins_sec(String r58_provi_ins_sec) {
			this.r58_provi_ins_sec = r58_provi_ins_sec;
		}

		public BigDecimal getR58_provi_loan_loss() {
			return r58_provi_loan_loss;
		}

		public void setR58_provi_loan_loss(BigDecimal r58_provi_loan_loss) {
			this.r58_provi_loan_loss = r58_provi_loan_loss;
		}

		public String getR59_provi_ins_sec() {
			return r59_provi_ins_sec;
		}

		public void setR59_provi_ins_sec(String r59_provi_ins_sec) {
			this.r59_provi_ins_sec = r59_provi_ins_sec;
		}

		public BigDecimal getR59_provi_loan_loss() {
			return r59_provi_loan_loss;
		}

		public void setR59_provi_loan_loss(BigDecimal r59_provi_loan_loss) {
			this.r59_provi_loan_loss = r59_provi_loan_loss;
		}

		public String getR60_provi_ins_sec() {
			return r60_provi_ins_sec;
		}

		public void setR60_provi_ins_sec(String r60_provi_ins_sec) {
			this.r60_provi_ins_sec = r60_provi_ins_sec;
		}

		public BigDecimal getR60_provi_loan_loss() {
			return r60_provi_loan_loss;
		}

		public void setR60_provi_loan_loss(BigDecimal r60_provi_loan_loss) {
			this.r60_provi_loan_loss = r60_provi_loan_loss;
		}

		public String getR61_provi_ins_sec() {
			return r61_provi_ins_sec;
		}

		public void setR61_provi_ins_sec(String r61_provi_ins_sec) {
			this.r61_provi_ins_sec = r61_provi_ins_sec;
		}

		public BigDecimal getR61_provi_loan_loss() {
			return r61_provi_loan_loss;
		}

		public void setR61_provi_loan_loss(BigDecimal r61_provi_loan_loss) {
			this.r61_provi_loan_loss = r61_provi_loan_loss;
		}

		public String getR62_provi_ins_sec() {
			return r62_provi_ins_sec;
		}

		public void setR62_provi_ins_sec(String r62_provi_ins_sec) {
			this.r62_provi_ins_sec = r62_provi_ins_sec;
		}

		public BigDecimal getR62_provi_loan_loss() {
			return r62_provi_loan_loss;
		}

		public void setR62_provi_loan_loss(BigDecimal r62_provi_loan_loss) {
			this.r62_provi_loan_loss = r62_provi_loan_loss;
		}

		public String getR63_provi_ins_sec() {
			return r63_provi_ins_sec;
		}

		public void setR63_provi_ins_sec(String r63_provi_ins_sec) {
			this.r63_provi_ins_sec = r63_provi_ins_sec;
		}

		public BigDecimal getR63_provi_loan_loss() {
			return r63_provi_loan_loss;
		}

		public void setR63_provi_loan_loss(BigDecimal r63_provi_loan_loss) {
			this.r63_provi_loan_loss = r63_provi_loan_loss;
		}

		public String getR64_provi_ins_sec() {
			return r64_provi_ins_sec;
		}

		public void setR64_provi_ins_sec(String r64_provi_ins_sec) {
			this.r64_provi_ins_sec = r64_provi_ins_sec;
		}

		public BigDecimal getR64_provi_loan_loss() {
			return r64_provi_loan_loss;
		}

		public void setR64_provi_loan_loss(BigDecimal r64_provi_loan_loss) {
			this.r64_provi_loan_loss = r64_provi_loan_loss;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public BigDecimal getReport_version() {
			return report_version;
		}

		public void setReport_version(BigDecimal report_version) {
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

		public M_PLL_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	}

	// ------------------------------
	// Entity class for M_PLL Detail Report
	// ------------------------------
	public static class M_PLL_Detail_Entity {

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
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
	
		public M_PLL_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	// ------------------------------
	// Entity class for M_PLL Archival Summary Report
	// ------------------------------
	public static class M_PLL_Archival_Summary_Entity {	

	private String r11_provi_ins_sec;
	private BigDecimal r11_provi_loan_loss;
	private String r12_provi_ins_sec;
	private BigDecimal r12_provi_loan_loss;
	private String r13_provi_ins_sec;
	private BigDecimal r13_provi_loan_loss;
	private String r14_provi_ins_sec;
	private BigDecimal r14_provi_loan_loss;
	private String r15_provi_ins_sec;
	private BigDecimal r15_provi_loan_loss;
	private String r16_provi_ins_sec;
	private BigDecimal r16_provi_loan_loss;
	private String r17_provi_ins_sec;
	private BigDecimal r17_provi_loan_loss;
	private String r18_provi_ins_sec;
	private BigDecimal r18_provi_loan_loss;
	private String r19_provi_ins_sec;
	private BigDecimal r19_provi_loan_loss;
	private String r20_provi_ins_sec;
	private BigDecimal r20_provi_loan_loss;
	private String r21_provi_ins_sec;
	private BigDecimal r21_provi_loan_loss;
	private String r22_provi_ins_sec;
	private BigDecimal r22_provi_loan_loss;
	private String r23_provi_ins_sec;
	private BigDecimal r23_provi_loan_loss;
	private String r24_provi_ins_sec;
	private BigDecimal r24_provi_loan_loss;
	private String r25_provi_ins_sec;
	private BigDecimal r25_provi_loan_loss;
	private String r26_provi_ins_sec;
	private BigDecimal r26_provi_loan_loss;
	private String r27_provi_ins_sec;
	private BigDecimal r27_provi_loan_loss;
	private String r28_provi_ins_sec;
	private BigDecimal r28_provi_loan_loss;
	private String r29_provi_ins_sec;
	private BigDecimal r29_provi_loan_loss;
	private String r30_provi_ins_sec;
	private BigDecimal r30_provi_loan_loss;
	private String r31_provi_ins_sec;
	private BigDecimal r31_provi_loan_loss;
	private String r32_provi_ins_sec;
	private BigDecimal r32_provi_loan_loss;
	private String r33_provi_ins_sec;
	private BigDecimal r33_provi_loan_loss;
	private String r34_provi_ins_sec;
	private BigDecimal r34_provi_loan_loss;
	private String r35_provi_ins_sec;
	private BigDecimal r35_provi_loan_loss;
	private String r36_provi_ins_sec;
	private BigDecimal r36_provi_loan_loss;
	private String r37_provi_ins_sec;
	private BigDecimal r37_provi_loan_loss;
	private String r38_provi_ins_sec;
	private BigDecimal r38_provi_loan_loss;
	private String r39_provi_ins_sec;
	private BigDecimal r39_provi_loan_loss;
	private String r40_provi_ins_sec;
	private BigDecimal r40_provi_loan_loss;
	private String r41_provi_ins_sec;
	private BigDecimal r41_provi_loan_loss;
	private String r42_provi_ins_sec;
	private BigDecimal r42_provi_loan_loss;
	private String r43_provi_ins_sec;
	private BigDecimal r43_provi_loan_loss;
	private String r44_provi_ins_sec;
	private BigDecimal r44_provi_loan_loss;
	private String r45_provi_ins_sec;
	private BigDecimal r45_provi_loan_loss;
	private String r46_provi_ins_sec;
	private BigDecimal r46_provi_loan_loss;
	private String r47_provi_ins_sec;
	private BigDecimal r47_provi_loan_loss;
	private String r48_provi_ins_sec;
	private BigDecimal r48_provi_loan_loss;
	private String r49_provi_ins_sec;
	private BigDecimal r49_provi_loan_loss;
	private String r50_provi_ins_sec;
	private BigDecimal r50_provi_loan_loss;
	private String r51_provi_ins_sec;
	private BigDecimal r51_provi_loan_loss;
	private String r52_provi_ins_sec;
	private BigDecimal r52_provi_loan_loss;
	private String r53_provi_ins_sec;
	private BigDecimal r53_provi_loan_loss;
	private String r54_provi_ins_sec;
	private BigDecimal r54_provi_loan_loss;
	private String r55_provi_ins_sec;
	private BigDecimal r55_provi_loan_loss;
	private String r56_provi_ins_sec;
	private BigDecimal r56_provi_loan_loss;
	private String r57_provi_ins_sec;
	private BigDecimal r57_provi_loan_loss;
	private String r58_provi_ins_sec;
	private BigDecimal r58_provi_loan_loss;
	private String r59_provi_ins_sec;
	private BigDecimal r59_provi_loan_loss;
	private String r60_provi_ins_sec;
	private BigDecimal r60_provi_loan_loss;
	private String r61_provi_ins_sec;
	private BigDecimal r61_provi_loan_loss;
	private String r62_provi_ins_sec;
	private BigDecimal r62_provi_loan_loss;
	private String r63_provi_ins_sec;
	private BigDecimal r63_provi_loan_loss;
	private String r64_provi_ins_sec;
	private BigDecimal r64_provi_loan_loss;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR11_provi_ins_sec() {
			return r11_provi_ins_sec;
		}

		public void setR11_provi_ins_sec(String r11_provi_ins_sec) {
			this.r11_provi_ins_sec = r11_provi_ins_sec;
		}

		public BigDecimal getR11_provi_loan_loss() {
			return r11_provi_loan_loss;
		}

		public void setR11_provi_loan_loss(BigDecimal r11_provi_loan_loss) {
			this.r11_provi_loan_loss = r11_provi_loan_loss;
		}

		public String getR12_provi_ins_sec() {
			return r12_provi_ins_sec;
		}

		public void setR12_provi_ins_sec(String r12_provi_ins_sec) {
			this.r12_provi_ins_sec = r12_provi_ins_sec;
		}

		public BigDecimal getR12_provi_loan_loss() {
			return r12_provi_loan_loss;
		}

		public void setR12_provi_loan_loss(BigDecimal r12_provi_loan_loss) {
			this.r12_provi_loan_loss = r12_provi_loan_loss;
		}

		public String getR13_provi_ins_sec() {
			return r13_provi_ins_sec;
		}

		public void setR13_provi_ins_sec(String r13_provi_ins_sec) {
			this.r13_provi_ins_sec = r13_provi_ins_sec;
		}

		public BigDecimal getR13_provi_loan_loss() {
			return r13_provi_loan_loss;
		}

		public void setR13_provi_loan_loss(BigDecimal r13_provi_loan_loss) {
			this.r13_provi_loan_loss = r13_provi_loan_loss;
		}

		public String getR14_provi_ins_sec() {
			return r14_provi_ins_sec;
		}

		public void setR14_provi_ins_sec(String r14_provi_ins_sec) {
			this.r14_provi_ins_sec = r14_provi_ins_sec;
		}

		public BigDecimal getR14_provi_loan_loss() {
			return r14_provi_loan_loss;
		}

		public void setR14_provi_loan_loss(BigDecimal r14_provi_loan_loss) {
			this.r14_provi_loan_loss = r14_provi_loan_loss;
		}

		public String getR15_provi_ins_sec() {
			return r15_provi_ins_sec;
		}

		public void setR15_provi_ins_sec(String r15_provi_ins_sec) {
			this.r15_provi_ins_sec = r15_provi_ins_sec;
		}

		public BigDecimal getR15_provi_loan_loss() {
			return r15_provi_loan_loss;
		}

		public void setR15_provi_loan_loss(BigDecimal r15_provi_loan_loss) {
			this.r15_provi_loan_loss = r15_provi_loan_loss;
		}

		public String getR16_provi_ins_sec() {
			return r16_provi_ins_sec;
		}

		public void setR16_provi_ins_sec(String r16_provi_ins_sec) {
			this.r16_provi_ins_sec = r16_provi_ins_sec;
		}

		public BigDecimal getR16_provi_loan_loss() {
			return r16_provi_loan_loss;
		}

		public void setR16_provi_loan_loss(BigDecimal r16_provi_loan_loss) {
			this.r16_provi_loan_loss = r16_provi_loan_loss;
		}

		public String getR17_provi_ins_sec() {
			return r17_provi_ins_sec;
		}

		public void setR17_provi_ins_sec(String r17_provi_ins_sec) {
			this.r17_provi_ins_sec = r17_provi_ins_sec;
		}

		public BigDecimal getR17_provi_loan_loss() {
			return r17_provi_loan_loss;
		}

		public void setR17_provi_loan_loss(BigDecimal r17_provi_loan_loss) {
			this.r17_provi_loan_loss = r17_provi_loan_loss;
		}

		public String getR18_provi_ins_sec() {
			return r18_provi_ins_sec;
		}

		public void setR18_provi_ins_sec(String r18_provi_ins_sec) {
			this.r18_provi_ins_sec = r18_provi_ins_sec;
		}

		public BigDecimal getR18_provi_loan_loss() {
			return r18_provi_loan_loss;
		}

		public void setR18_provi_loan_loss(BigDecimal r18_provi_loan_loss) {
			this.r18_provi_loan_loss = r18_provi_loan_loss;
		}

		public String getR19_provi_ins_sec() {
			return r19_provi_ins_sec;
		}

		public void setR19_provi_ins_sec(String r19_provi_ins_sec) {
			this.r19_provi_ins_sec = r19_provi_ins_sec;
		}

		public BigDecimal getR19_provi_loan_loss() {
			return r19_provi_loan_loss;
		}

		public void setR19_provi_loan_loss(BigDecimal r19_provi_loan_loss) {
			this.r19_provi_loan_loss = r19_provi_loan_loss;
		}

		public String getR20_provi_ins_sec() {
			return r20_provi_ins_sec;
		}

		public void setR20_provi_ins_sec(String r20_provi_ins_sec) {
			this.r20_provi_ins_sec = r20_provi_ins_sec;
		}

		public BigDecimal getR20_provi_loan_loss() {
			return r20_provi_loan_loss;
		}

		public void setR20_provi_loan_loss(BigDecimal r20_provi_loan_loss) {
			this.r20_provi_loan_loss = r20_provi_loan_loss;
		}

		public String getR21_provi_ins_sec() {
			return r21_provi_ins_sec;
		}

		public void setR21_provi_ins_sec(String r21_provi_ins_sec) {
			this.r21_provi_ins_sec = r21_provi_ins_sec;
		}

		public BigDecimal getR21_provi_loan_loss() {
			return r21_provi_loan_loss;
		}

		public void setR21_provi_loan_loss(BigDecimal r21_provi_loan_loss) {
			this.r21_provi_loan_loss = r21_provi_loan_loss;
		}

		public String getR22_provi_ins_sec() {
			return r22_provi_ins_sec;
		}

		public void setR22_provi_ins_sec(String r22_provi_ins_sec) {
			this.r22_provi_ins_sec = r22_provi_ins_sec;
		}

		public BigDecimal getR22_provi_loan_loss() {
			return r22_provi_loan_loss;
		}

		public void setR22_provi_loan_loss(BigDecimal r22_provi_loan_loss) {
			this.r22_provi_loan_loss = r22_provi_loan_loss;
		}

		public String getR23_provi_ins_sec() {
			return r23_provi_ins_sec;
		}

		public void setR23_provi_ins_sec(String r23_provi_ins_sec) {
			this.r23_provi_ins_sec = r23_provi_ins_sec;
		}

		public BigDecimal getR23_provi_loan_loss() {
			return r23_provi_loan_loss;
		}

		public void setR23_provi_loan_loss(BigDecimal r23_provi_loan_loss) {
			this.r23_provi_loan_loss = r23_provi_loan_loss;
		}

		public String getR24_provi_ins_sec() {
			return r24_provi_ins_sec;
		}

		public void setR24_provi_ins_sec(String r24_provi_ins_sec) {
			this.r24_provi_ins_sec = r24_provi_ins_sec;
		}

		public BigDecimal getR24_provi_loan_loss() {
			return r24_provi_loan_loss;
		}

		public void setR24_provi_loan_loss(BigDecimal r24_provi_loan_loss) {
			this.r24_provi_loan_loss = r24_provi_loan_loss;
		}

		public String getR25_provi_ins_sec() {
			return r25_provi_ins_sec;
		}

		public void setR25_provi_ins_sec(String r25_provi_ins_sec) {
			this.r25_provi_ins_sec = r25_provi_ins_sec;
		}

		public BigDecimal getR25_provi_loan_loss() {
			return r25_provi_loan_loss;
		}

		public void setR25_provi_loan_loss(BigDecimal r25_provi_loan_loss) {
			this.r25_provi_loan_loss = r25_provi_loan_loss;
		}

		public String getR26_provi_ins_sec() {
			return r26_provi_ins_sec;
		}

		public void setR26_provi_ins_sec(String r26_provi_ins_sec) {
			this.r26_provi_ins_sec = r26_provi_ins_sec;
		}

		public BigDecimal getR26_provi_loan_loss() {
			return r26_provi_loan_loss;
		}

		public void setR26_provi_loan_loss(BigDecimal r26_provi_loan_loss) {
			this.r26_provi_loan_loss = r26_provi_loan_loss;
		}

		public String getR27_provi_ins_sec() {
			return r27_provi_ins_sec;
		}

		public void setR27_provi_ins_sec(String r27_provi_ins_sec) {
			this.r27_provi_ins_sec = r27_provi_ins_sec;
		}

		public BigDecimal getR27_provi_loan_loss() {
			return r27_provi_loan_loss;
		}

		public void setR27_provi_loan_loss(BigDecimal r27_provi_loan_loss) {
			this.r27_provi_loan_loss = r27_provi_loan_loss;
		}

		public String getR28_provi_ins_sec() {
			return r28_provi_ins_sec;
		}

		public void setR28_provi_ins_sec(String r28_provi_ins_sec) {
			this.r28_provi_ins_sec = r28_provi_ins_sec;
		}

		public BigDecimal getR28_provi_loan_loss() {
			return r28_provi_loan_loss;
		}

		public void setR28_provi_loan_loss(BigDecimal r28_provi_loan_loss) {
			this.r28_provi_loan_loss = r28_provi_loan_loss;
		}

		public String getR29_provi_ins_sec() {
			return r29_provi_ins_sec;
		}

		public void setR29_provi_ins_sec(String r29_provi_ins_sec) {
			this.r29_provi_ins_sec = r29_provi_ins_sec;
		}

		public BigDecimal getR29_provi_loan_loss() {
			return r29_provi_loan_loss;
		}

		public void setR29_provi_loan_loss(BigDecimal r29_provi_loan_loss) {
			this.r29_provi_loan_loss = r29_provi_loan_loss;
		}

		public String getR30_provi_ins_sec() {
			return r30_provi_ins_sec;
		}

		public void setR30_provi_ins_sec(String r30_provi_ins_sec) {
			this.r30_provi_ins_sec = r30_provi_ins_sec;
		}

		public BigDecimal getR30_provi_loan_loss() {
			return r30_provi_loan_loss;
		}

		public void setR30_provi_loan_loss(BigDecimal r30_provi_loan_loss) {
			this.r30_provi_loan_loss = r30_provi_loan_loss;
		}

		public String getR31_provi_ins_sec() {
			return r31_provi_ins_sec;
		}

		public void setR31_provi_ins_sec(String r31_provi_ins_sec) {
			this.r31_provi_ins_sec = r31_provi_ins_sec;
		}

		public BigDecimal getR31_provi_loan_loss() {
			return r31_provi_loan_loss;
		}

		public void setR31_provi_loan_loss(BigDecimal r31_provi_loan_loss) {
			this.r31_provi_loan_loss = r31_provi_loan_loss;
		}

		public String getR32_provi_ins_sec() {
			return r32_provi_ins_sec;
		}

		public void setR32_provi_ins_sec(String r32_provi_ins_sec) {
			this.r32_provi_ins_sec = r32_provi_ins_sec;
		}

		public BigDecimal getR32_provi_loan_loss() {
			return r32_provi_loan_loss;
		}

		public void setR32_provi_loan_loss(BigDecimal r32_provi_loan_loss) {
			this.r32_provi_loan_loss = r32_provi_loan_loss;
		}

		public String getR33_provi_ins_sec() {
			return r33_provi_ins_sec;
		}

		public void setR33_provi_ins_sec(String r33_provi_ins_sec) {
			this.r33_provi_ins_sec = r33_provi_ins_sec;
		}

		public BigDecimal getR33_provi_loan_loss() {
			return r33_provi_loan_loss;
		}

		public void setR33_provi_loan_loss(BigDecimal r33_provi_loan_loss) {
			this.r33_provi_loan_loss = r33_provi_loan_loss;
		}

		public String getR34_provi_ins_sec() {
			return r34_provi_ins_sec;
		}

		public void setR34_provi_ins_sec(String r34_provi_ins_sec) {
			this.r34_provi_ins_sec = r34_provi_ins_sec;
		}

		public BigDecimal getR34_provi_loan_loss() {
			return r34_provi_loan_loss;
		}

		public void setR34_provi_loan_loss(BigDecimal r34_provi_loan_loss) {
			this.r34_provi_loan_loss = r34_provi_loan_loss;
		}

		public String getR35_provi_ins_sec() {
			return r35_provi_ins_sec;
		}

		public void setR35_provi_ins_sec(String r35_provi_ins_sec) {
			this.r35_provi_ins_sec = r35_provi_ins_sec;
		}

		public BigDecimal getR35_provi_loan_loss() {
			return r35_provi_loan_loss;
		}

		public void setR35_provi_loan_loss(BigDecimal r35_provi_loan_loss) {
			this.r35_provi_loan_loss = r35_provi_loan_loss;
		}

		public String getR36_provi_ins_sec() {
			return r36_provi_ins_sec;
		}

		public void setR36_provi_ins_sec(String r36_provi_ins_sec) {
			this.r36_provi_ins_sec = r36_provi_ins_sec;
		}

		public BigDecimal getR36_provi_loan_loss() {
			return r36_provi_loan_loss;
		}

		public void setR36_provi_loan_loss(BigDecimal r36_provi_loan_loss) {
			this.r36_provi_loan_loss = r36_provi_loan_loss;
		}

		public String getR37_provi_ins_sec() {
			return r37_provi_ins_sec;
		}

		public void setR37_provi_ins_sec(String r37_provi_ins_sec) {
			this.r37_provi_ins_sec = r37_provi_ins_sec;
		}

		public BigDecimal getR37_provi_loan_loss() {
			return r37_provi_loan_loss;
		}

		public void setR37_provi_loan_loss(BigDecimal r37_provi_loan_loss) {
			this.r37_provi_loan_loss = r37_provi_loan_loss;
		}

		public String getR38_provi_ins_sec() {
			return r38_provi_ins_sec;
		}

		public void setR38_provi_ins_sec(String r38_provi_ins_sec) {
			this.r38_provi_ins_sec = r38_provi_ins_sec;
		}

		public BigDecimal getR38_provi_loan_loss() {
			return r38_provi_loan_loss;
		}

		public void setR38_provi_loan_loss(BigDecimal r38_provi_loan_loss) {
			this.r38_provi_loan_loss = r38_provi_loan_loss;
		}

		public String getR39_provi_ins_sec() {
			return r39_provi_ins_sec;
		}

		public void setR39_provi_ins_sec(String r39_provi_ins_sec) {
			this.r39_provi_ins_sec = r39_provi_ins_sec;
		}

		public BigDecimal getR39_provi_loan_loss() {
			return r39_provi_loan_loss;
		}

		public void setR39_provi_loan_loss(BigDecimal r39_provi_loan_loss) {
			this.r39_provi_loan_loss = r39_provi_loan_loss;
		}

		public String getR40_provi_ins_sec() {
			return r40_provi_ins_sec;
		}

		public void setR40_provi_ins_sec(String r40_provi_ins_sec) {
			this.r40_provi_ins_sec = r40_provi_ins_sec;
		}

		public BigDecimal getR40_provi_loan_loss() {
			return r40_provi_loan_loss;
		}

		public void setR40_provi_loan_loss(BigDecimal r40_provi_loan_loss) {
			this.r40_provi_loan_loss = r40_provi_loan_loss;
		}

		public String getR41_provi_ins_sec() {
			return r41_provi_ins_sec;
		}

		public void setR41_provi_ins_sec(String r41_provi_ins_sec) {
			this.r41_provi_ins_sec = r41_provi_ins_sec;
		}

		public BigDecimal getR41_provi_loan_loss() {
			return r41_provi_loan_loss;
		}

		public void setR41_provi_loan_loss(BigDecimal r41_provi_loan_loss) {
			this.r41_provi_loan_loss = r41_provi_loan_loss;
		}

		public String getR42_provi_ins_sec() {
			return r42_provi_ins_sec;
		}

		public void setR42_provi_ins_sec(String r42_provi_ins_sec) {
			this.r42_provi_ins_sec = r42_provi_ins_sec;
		}

		public BigDecimal getR42_provi_loan_loss() {
			return r42_provi_loan_loss;
		}

		public void setR42_provi_loan_loss(BigDecimal r42_provi_loan_loss) {
			this.r42_provi_loan_loss = r42_provi_loan_loss;
		}

		public String getR43_provi_ins_sec() {
			return r43_provi_ins_sec;
		}

		public void setR43_provi_ins_sec(String r43_provi_ins_sec) {
			this.r43_provi_ins_sec = r43_provi_ins_sec;
		}

		public BigDecimal getR43_provi_loan_loss() {
			return r43_provi_loan_loss;
		}

		public void setR43_provi_loan_loss(BigDecimal r43_provi_loan_loss) {
			this.r43_provi_loan_loss = r43_provi_loan_loss;
		}

		public String getR44_provi_ins_sec() {
			return r44_provi_ins_sec;
		}

		public void setR44_provi_ins_sec(String r44_provi_ins_sec) {
			this.r44_provi_ins_sec = r44_provi_ins_sec;
		}

		public BigDecimal getR44_provi_loan_loss() {
			return r44_provi_loan_loss;
		}

		public void setR44_provi_loan_loss(BigDecimal r44_provi_loan_loss) {
			this.r44_provi_loan_loss = r44_provi_loan_loss;
		}

		public String getR45_provi_ins_sec() {
			return r45_provi_ins_sec;
		}

		public void setR45_provi_ins_sec(String r45_provi_ins_sec) {
			this.r45_provi_ins_sec = r45_provi_ins_sec;
		}

		public BigDecimal getR45_provi_loan_loss() {
			return r45_provi_loan_loss;
		}

		public void setR45_provi_loan_loss(BigDecimal r45_provi_loan_loss) {
			this.r45_provi_loan_loss = r45_provi_loan_loss;
		}

		public String getR46_provi_ins_sec() {
			return r46_provi_ins_sec;
		}

		public void setR46_provi_ins_sec(String r46_provi_ins_sec) {
			this.r46_provi_ins_sec = r46_provi_ins_sec;
		}

		public BigDecimal getR46_provi_loan_loss() {
			return r46_provi_loan_loss;
		}

		public void setR46_provi_loan_loss(BigDecimal r46_provi_loan_loss) {
			this.r46_provi_loan_loss = r46_provi_loan_loss;
		}

		public String getR47_provi_ins_sec() {
			return r47_provi_ins_sec;
		}

		public void setR47_provi_ins_sec(String r47_provi_ins_sec) {
			this.r47_provi_ins_sec = r47_provi_ins_sec;
		}

		public BigDecimal getR47_provi_loan_loss() {
			return r47_provi_loan_loss;
		}

		public void setR47_provi_loan_loss(BigDecimal r47_provi_loan_loss) {
			this.r47_provi_loan_loss = r47_provi_loan_loss;
		}

		public String getR48_provi_ins_sec() {
			return r48_provi_ins_sec;
		}

		public void setR48_provi_ins_sec(String r48_provi_ins_sec) {
			this.r48_provi_ins_sec = r48_provi_ins_sec;
		}

		public BigDecimal getR48_provi_loan_loss() {
			return r48_provi_loan_loss;
		}

		public void setR48_provi_loan_loss(BigDecimal r48_provi_loan_loss) {
			this.r48_provi_loan_loss = r48_provi_loan_loss;
		}

		public String getR49_provi_ins_sec() {
			return r49_provi_ins_sec;
		}

		public void setR49_provi_ins_sec(String r49_provi_ins_sec) {
			this.r49_provi_ins_sec = r49_provi_ins_sec;
		}

		public BigDecimal getR49_provi_loan_loss() {
			return r49_provi_loan_loss;
		}

		public void setR49_provi_loan_loss(BigDecimal r49_provi_loan_loss) {
			this.r49_provi_loan_loss = r49_provi_loan_loss;
		}

		public String getR50_provi_ins_sec() {
			return r50_provi_ins_sec;
		}

		public void setR50_provi_ins_sec(String r50_provi_ins_sec) {
			this.r50_provi_ins_sec = r50_provi_ins_sec;
		}

		public BigDecimal getR50_provi_loan_loss() {
			return r50_provi_loan_loss;
		}

		public void setR50_provi_loan_loss(BigDecimal r50_provi_loan_loss) {
			this.r50_provi_loan_loss = r50_provi_loan_loss;
		}

		public String getR51_provi_ins_sec() {
			return r51_provi_ins_sec;
		}

		public void setR51_provi_ins_sec(String r51_provi_ins_sec) {
			this.r51_provi_ins_sec = r51_provi_ins_sec;
		}

		public BigDecimal getR51_provi_loan_loss() {
			return r51_provi_loan_loss;
		}

		public void setR51_provi_loan_loss(BigDecimal r51_provi_loan_loss) {
			this.r51_provi_loan_loss = r51_provi_loan_loss;
		}

		public String getR52_provi_ins_sec() {
			return r52_provi_ins_sec;
		}

		public void setR52_provi_ins_sec(String r52_provi_ins_sec) {
			this.r52_provi_ins_sec = r52_provi_ins_sec;
		}

		public BigDecimal getR52_provi_loan_loss() {
			return r52_provi_loan_loss;
		}

		public void setR52_provi_loan_loss(BigDecimal r52_provi_loan_loss) {
			this.r52_provi_loan_loss = r52_provi_loan_loss;
		}

		public String getR53_provi_ins_sec() {
			return r53_provi_ins_sec;
		}

		public void setR53_provi_ins_sec(String r53_provi_ins_sec) {
			this.r53_provi_ins_sec = r53_provi_ins_sec;
		}

		public BigDecimal getR53_provi_loan_loss() {
			return r53_provi_loan_loss;
		}

		public void setR53_provi_loan_loss(BigDecimal r53_provi_loan_loss) {
			this.r53_provi_loan_loss = r53_provi_loan_loss;
		}

		public String getR54_provi_ins_sec() {
			return r54_provi_ins_sec;
		}

		public void setR54_provi_ins_sec(String r54_provi_ins_sec) {
			this.r54_provi_ins_sec = r54_provi_ins_sec;
		}

		public BigDecimal getR54_provi_loan_loss() {
			return r54_provi_loan_loss;
		}

		public void setR54_provi_loan_loss(BigDecimal r54_provi_loan_loss) {
			this.r54_provi_loan_loss = r54_provi_loan_loss;
		}

		public String getR55_provi_ins_sec() {
			return r55_provi_ins_sec;
		}

		public void setR55_provi_ins_sec(String r55_provi_ins_sec) {
			this.r55_provi_ins_sec = r55_provi_ins_sec;
		}

		public BigDecimal getR55_provi_loan_loss() {
			return r55_provi_loan_loss;
		}

		public void setR55_provi_loan_loss(BigDecimal r55_provi_loan_loss) {
			this.r55_provi_loan_loss = r55_provi_loan_loss;
		}

		public String getR56_provi_ins_sec() {
			return r56_provi_ins_sec;
		}

		public void setR56_provi_ins_sec(String r56_provi_ins_sec) {
			this.r56_provi_ins_sec = r56_provi_ins_sec;
		}

		public BigDecimal getR56_provi_loan_loss() {
			return r56_provi_loan_loss;
		}

		public void setR56_provi_loan_loss(BigDecimal r56_provi_loan_loss) {
			this.r56_provi_loan_loss = r56_provi_loan_loss;
		}

		public String getR57_provi_ins_sec() {
			return r57_provi_ins_sec;
		}

		public void setR57_provi_ins_sec(String r57_provi_ins_sec) {
			this.r57_provi_ins_sec = r57_provi_ins_sec;
		}

		public BigDecimal getR57_provi_loan_loss() {
			return r57_provi_loan_loss;
		}

		public void setR57_provi_loan_loss(BigDecimal r57_provi_loan_loss) {
			this.r57_provi_loan_loss = r57_provi_loan_loss;
		}

		public String getR58_provi_ins_sec() {
			return r58_provi_ins_sec;
		}

		public void setR58_provi_ins_sec(String r58_provi_ins_sec) {
			this.r58_provi_ins_sec = r58_provi_ins_sec;
		}

		public BigDecimal getR58_provi_loan_loss() {
			return r58_provi_loan_loss;
		}

		public void setR58_provi_loan_loss(BigDecimal r58_provi_loan_loss) {
			this.r58_provi_loan_loss = r58_provi_loan_loss;
		}

		public String getR59_provi_ins_sec() {
			return r59_provi_ins_sec;
		}

		public void setR59_provi_ins_sec(String r59_provi_ins_sec) {
			this.r59_provi_ins_sec = r59_provi_ins_sec;
		}

		public BigDecimal getR59_provi_loan_loss() {
			return r59_provi_loan_loss;
		}

		public void setR59_provi_loan_loss(BigDecimal r59_provi_loan_loss) {
			this.r59_provi_loan_loss = r59_provi_loan_loss;
		}

		public String getR60_provi_ins_sec() {
			return r60_provi_ins_sec;
		}

		public void setR60_provi_ins_sec(String r60_provi_ins_sec) {
			this.r60_provi_ins_sec = r60_provi_ins_sec;
		}

		public BigDecimal getR60_provi_loan_loss() {
			return r60_provi_loan_loss;
		}

		public void setR60_provi_loan_loss(BigDecimal r60_provi_loan_loss) {
			this.r60_provi_loan_loss = r60_provi_loan_loss;
		}

		public String getR61_provi_ins_sec() {
			return r61_provi_ins_sec;
		}

		public void setR61_provi_ins_sec(String r61_provi_ins_sec) {
			this.r61_provi_ins_sec = r61_provi_ins_sec;
		}

		public BigDecimal getR61_provi_loan_loss() {
			return r61_provi_loan_loss;
		}

		public void setR61_provi_loan_loss(BigDecimal r61_provi_loan_loss) {
			this.r61_provi_loan_loss = r61_provi_loan_loss;
		}

		public String getR62_provi_ins_sec() {
			return r62_provi_ins_sec;
		}

		public void setR62_provi_ins_sec(String r62_provi_ins_sec) {
			this.r62_provi_ins_sec = r62_provi_ins_sec;
		}

		public BigDecimal getR62_provi_loan_loss() {
			return r62_provi_loan_loss;
		}

		public void setR62_provi_loan_loss(BigDecimal r62_provi_loan_loss) {
			this.r62_provi_loan_loss = r62_provi_loan_loss;
		}

		public String getR63_provi_ins_sec() {
			return r63_provi_ins_sec;
		}

		public void setR63_provi_ins_sec(String r63_provi_ins_sec) {
			this.r63_provi_ins_sec = r63_provi_ins_sec;
		}

		public BigDecimal getR63_provi_loan_loss() {
			return r63_provi_loan_loss;
		}

		public void setR63_provi_loan_loss(BigDecimal r63_provi_loan_loss) {
			this.r63_provi_loan_loss = r63_provi_loan_loss;
		}

		public String getR64_provi_ins_sec() {
			return r64_provi_ins_sec;
		}

		public void setR64_provi_ins_sec(String r64_provi_ins_sec) {
			this.r64_provi_ins_sec = r64_provi_ins_sec;
		}

		public BigDecimal getR64_provi_loan_loss() {
			return r64_provi_loan_loss;
		}

		public void setR64_provi_loan_loss(BigDecimal r64_provi_loan_loss) {
			this.r64_provi_loan_loss = r64_provi_loan_loss;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public BigDecimal getReport_version() {
			return report_version;
		}

		public void setReport_version(BigDecimal report_version) {
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

		public M_PLL_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	}

	// ------------------------------
	// Entity class for M_PLL Archival Detail Report
	// ------------------------------
	public static class M_PLL_Archival_Detail_Entity {
		
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
	
			public M_PLL_Archival_Detail_Entity() {
				super();
				// TODO Auto-generated constructor stub
			}

	}

	// ------------------------------
	// Row Mapper for M_PLL_Summary_Entity
	// ------------------------------
	class M_PLLSummaryRowMapper implements RowMapper<M_PLL_Summary_Entity> {
		@Override
		public M_PLL_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_PLL_Summary_Entity obj = new M_PLL_Summary_Entity();
			obj.setR11_provi_ins_sec(rs.getString("r11_provi_ins_sec"));
			obj.setR11_provi_loan_loss(rs.getBigDecimal("r11_provi_loan_loss"));
			obj.setR12_provi_ins_sec(rs.getString("r12_provi_ins_sec"));
			obj.setR12_provi_loan_loss(rs.getBigDecimal("r12_provi_loan_loss"));
			obj.setR13_provi_ins_sec(rs.getString("r13_provi_ins_sec"));
			obj.setR13_provi_loan_loss(rs.getBigDecimal("r13_provi_loan_loss"));
			obj.setR14_provi_ins_sec(rs.getString("r14_provi_ins_sec"));
			obj.setR14_provi_loan_loss(rs.getBigDecimal("r14_provi_loan_loss"));
			obj.setR15_provi_ins_sec(rs.getString("r15_provi_ins_sec"));
			obj.setR15_provi_loan_loss(rs.getBigDecimal("r15_provi_loan_loss"));
			obj.setR16_provi_ins_sec(rs.getString("r16_provi_ins_sec"));
			obj.setR16_provi_loan_loss(rs.getBigDecimal("r16_provi_loan_loss"));
			obj.setR17_provi_ins_sec(rs.getString("r17_provi_ins_sec"));
			obj.setR17_provi_loan_loss(rs.getBigDecimal("r17_provi_loan_loss"));
			obj.setR18_provi_ins_sec(rs.getString("r18_provi_ins_sec"));
			obj.setR18_provi_loan_loss(rs.getBigDecimal("r18_provi_loan_loss"));
			obj.setR19_provi_ins_sec(rs.getString("r19_provi_ins_sec"));
			obj.setR19_provi_loan_loss(rs.getBigDecimal("r19_provi_loan_loss"));
			obj.setR20_provi_ins_sec(rs.getString("r20_provi_ins_sec"));
			obj.setR20_provi_loan_loss(rs.getBigDecimal("r20_provi_loan_loss"));
			obj.setR21_provi_ins_sec(rs.getString("r21_provi_ins_sec"));
			obj.setR21_provi_loan_loss(rs.getBigDecimal("r21_provi_loan_loss"));
			obj.setR22_provi_ins_sec(rs.getString("r22_provi_ins_sec"));
			obj.setR22_provi_loan_loss(rs.getBigDecimal("r22_provi_loan_loss"));
			obj.setR23_provi_ins_sec(rs.getString("r23_provi_ins_sec"));
			obj.setR23_provi_loan_loss(rs.getBigDecimal("r23_provi_loan_loss"));
			obj.setR24_provi_ins_sec(rs.getString("r24_provi_ins_sec"));
			obj.setR24_provi_loan_loss(rs.getBigDecimal("r24_provi_loan_loss"));
			obj.setR25_provi_ins_sec(rs.getString("r25_provi_ins_sec"));
			obj.setR25_provi_loan_loss(rs.getBigDecimal("r25_provi_loan_loss"));
			obj.setR26_provi_ins_sec(rs.getString("r26_provi_ins_sec"));
			obj.setR26_provi_loan_loss(rs.getBigDecimal("r26_provi_loan_loss"));
			obj.setR27_provi_ins_sec(rs.getString("r27_provi_ins_sec"));
			obj.setR27_provi_loan_loss(rs.getBigDecimal("r27_provi_loan_loss"));
			obj.setR28_provi_ins_sec(rs.getString("r28_provi_ins_sec"));
			obj.setR28_provi_loan_loss(rs.getBigDecimal("r28_provi_loan_loss"));
			obj.setR29_provi_ins_sec(rs.getString("r29_provi_ins_sec"));
			obj.setR29_provi_loan_loss(rs.getBigDecimal("r29_provi_loan_loss"));
			obj.setR30_provi_ins_sec(rs.getString("r30_provi_ins_sec"));
			obj.setR30_provi_loan_loss(rs.getBigDecimal("r30_provi_loan_loss"));
			obj.setR31_provi_ins_sec(rs.getString("r31_provi_ins_sec"));
			obj.setR31_provi_loan_loss(rs.getBigDecimal("r31_provi_loan_loss"));
			obj.setR32_provi_ins_sec(rs.getString("r32_provi_ins_sec"));
			obj.setR32_provi_loan_loss(rs.getBigDecimal("r32_provi_loan_loss"));
			obj.setR33_provi_ins_sec(rs.getString("r33_provi_ins_sec"));
			obj.setR33_provi_loan_loss(rs.getBigDecimal("r33_provi_loan_loss"));
			obj.setR34_provi_ins_sec(rs.getString("r34_provi_ins_sec"));
			obj.setR34_provi_loan_loss(rs.getBigDecimal("r34_provi_loan_loss"));
			obj.setR35_provi_ins_sec(rs.getString("r35_provi_ins_sec"));
			obj.setR35_provi_loan_loss(rs.getBigDecimal("r35_provi_loan_loss"));
			obj.setR36_provi_ins_sec(rs.getString("r36_provi_ins_sec"));
			obj.setR36_provi_loan_loss(rs.getBigDecimal("r36_provi_loan_loss"));
			obj.setR37_provi_ins_sec(rs.getString("r37_provi_ins_sec"));
			obj.setR37_provi_loan_loss(rs.getBigDecimal("r37_provi_loan_loss"));
			obj.setR38_provi_ins_sec(rs.getString("r38_provi_ins_sec"));
			obj.setR38_provi_loan_loss(rs.getBigDecimal("r38_provi_loan_loss"));
			obj.setR39_provi_ins_sec(rs.getString("r39_provi_ins_sec"));
			obj.setR39_provi_loan_loss(rs.getBigDecimal("r39_provi_loan_loss"));
			obj.setR40_provi_ins_sec(rs.getString("r40_provi_ins_sec"));
			obj.setR40_provi_loan_loss(rs.getBigDecimal("r40_provi_loan_loss"));
			obj.setR41_provi_ins_sec(rs.getString("r41_provi_ins_sec"));
			obj.setR41_provi_loan_loss(rs.getBigDecimal("r41_provi_loan_loss"));
			obj.setR42_provi_ins_sec(rs.getString("r42_provi_ins_sec"));
			obj.setR42_provi_loan_loss(rs.getBigDecimal("r42_provi_loan_loss"));
			obj.setR43_provi_ins_sec(rs.getString("r43_provi_ins_sec"));
			obj.setR43_provi_loan_loss(rs.getBigDecimal("r43_provi_loan_loss"));
			obj.setR44_provi_ins_sec(rs.getString("r44_provi_ins_sec"));
			obj.setR44_provi_loan_loss(rs.getBigDecimal("r44_provi_loan_loss"));
			obj.setR45_provi_ins_sec(rs.getString("r45_provi_ins_sec"));
			obj.setR45_provi_loan_loss(rs.getBigDecimal("r45_provi_loan_loss"));
			obj.setR46_provi_ins_sec(rs.getString("r46_provi_ins_sec"));
			obj.setR46_provi_loan_loss(rs.getBigDecimal("r46_provi_loan_loss"));
			obj.setR47_provi_ins_sec(rs.getString("r47_provi_ins_sec"));
			obj.setR47_provi_loan_loss(rs.getBigDecimal("r47_provi_loan_loss"));
			obj.setR48_provi_ins_sec(rs.getString("r48_provi_ins_sec"));
			obj.setR48_provi_loan_loss(rs.getBigDecimal("r48_provi_loan_loss"));
			obj.setR49_provi_ins_sec(rs.getString("r49_provi_ins_sec"));
			obj.setR49_provi_loan_loss(rs.getBigDecimal("r49_provi_loan_loss"));
			obj.setR50_provi_ins_sec(rs.getString("r50_provi_ins_sec"));
			obj.setR50_provi_loan_loss(rs.getBigDecimal("r50_provi_loan_loss"));
			obj.setR51_provi_ins_sec(rs.getString("r51_provi_ins_sec"));
			obj.setR51_provi_loan_loss(rs.getBigDecimal("r51_provi_loan_loss"));
			obj.setR52_provi_ins_sec(rs.getString("r52_provi_ins_sec"));
			obj.setR52_provi_loan_loss(rs.getBigDecimal("r52_provi_loan_loss"));
			obj.setR53_provi_ins_sec(rs.getString("r53_provi_ins_sec"));
			obj.setR53_provi_loan_loss(rs.getBigDecimal("r53_provi_loan_loss"));
			obj.setR54_provi_ins_sec(rs.getString("r54_provi_ins_sec"));
			obj.setR54_provi_loan_loss(rs.getBigDecimal("r54_provi_loan_loss"));
			obj.setR55_provi_ins_sec(rs.getString("r55_provi_ins_sec"));
			obj.setR55_provi_loan_loss(rs.getBigDecimal("r55_provi_loan_loss"));
			obj.setR56_provi_ins_sec(rs.getString("r56_provi_ins_sec"));
			obj.setR56_provi_loan_loss(rs.getBigDecimal("r56_provi_loan_loss"));
			obj.setR57_provi_ins_sec(rs.getString("r57_provi_ins_sec"));
			obj.setR57_provi_loan_loss(rs.getBigDecimal("r57_provi_loan_loss"));
			obj.setR58_provi_ins_sec(rs.getString("r58_provi_ins_sec"));
			obj.setR58_provi_loan_loss(rs.getBigDecimal("r58_provi_loan_loss"));
			obj.setR59_provi_ins_sec(rs.getString("r59_provi_ins_sec"));
			obj.setR59_provi_loan_loss(rs.getBigDecimal("r59_provi_loan_loss"));
			obj.setR60_provi_ins_sec(rs.getString("r60_provi_ins_sec"));
			obj.setR60_provi_loan_loss(rs.getBigDecimal("r60_provi_loan_loss"));
			obj.setR61_provi_ins_sec(rs.getString("r61_provi_ins_sec"));
			obj.setR61_provi_loan_loss(rs.getBigDecimal("r61_provi_loan_loss"));
			obj.setR62_provi_ins_sec(rs.getString("r62_provi_ins_sec"));
			obj.setR62_provi_loan_loss(rs.getBigDecimal("r62_provi_loan_loss"));
			obj.setR63_provi_ins_sec(rs.getString("r63_provi_ins_sec"));
			obj.setR63_provi_loan_loss(rs.getBigDecimal("r63_provi_loan_loss"));
			obj.setR64_provi_ins_sec(rs.getString("r64_provi_ins_sec"));
			obj.setR64_provi_loan_loss(rs.getBigDecimal("r64_provi_loan_loss"));
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));
			return obj;
		}
	}

	// ------------------------------
	// Row Mapper for M_PLL_Detail_Entity
	// ------------------------------
	class M_PLLDetailRowMapper implements RowMapper<M_PLL_Detail_Entity> {
		@Override
		public M_PLL_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_PLL_Detail_Entity obj = new M_PLL_Detail_Entity();
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
	// Row Mapper for M_PLL_Archival_Summary_Entity
	// ------------------------------
	class M_PLLArchivalSummaryRowMapper implements RowMapper<M_PLL_Archival_Summary_Entity> {
		@Override
		public M_PLL_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_PLL_Archival_Summary_Entity obj = new M_PLL_Archival_Summary_Entity();
			obj.setR11_provi_ins_sec(rs.getString("r11_provi_ins_sec"));
			obj.setR11_provi_loan_loss(rs.getBigDecimal("r11_provi_loan_loss"));
			obj.setR12_provi_ins_sec(rs.getString("r12_provi_ins_sec"));
			obj.setR12_provi_loan_loss(rs.getBigDecimal("r12_provi_loan_loss"));
			obj.setR13_provi_ins_sec(rs.getString("r13_provi_ins_sec"));
			obj.setR13_provi_loan_loss(rs.getBigDecimal("r13_provi_loan_loss"));
			obj.setR14_provi_ins_sec(rs.getString("r14_provi_ins_sec"));
			obj.setR14_provi_loan_loss(rs.getBigDecimal("r14_provi_loan_loss"));
			obj.setR15_provi_ins_sec(rs.getString("r15_provi_ins_sec"));
			obj.setR15_provi_loan_loss(rs.getBigDecimal("r15_provi_loan_loss"));
			obj.setR16_provi_ins_sec(rs.getString("r16_provi_ins_sec"));
			obj.setR16_provi_loan_loss(rs.getBigDecimal("r16_provi_loan_loss"));
			obj.setR17_provi_ins_sec(rs.getString("r17_provi_ins_sec"));
			obj.setR17_provi_loan_loss(rs.getBigDecimal("r17_provi_loan_loss"));
			obj.setR18_provi_ins_sec(rs.getString("r18_provi_ins_sec"));
			obj.setR18_provi_loan_loss(rs.getBigDecimal("r18_provi_loan_loss"));
			obj.setR19_provi_ins_sec(rs.getString("r19_provi_ins_sec"));
			obj.setR19_provi_loan_loss(rs.getBigDecimal("r19_provi_loan_loss"));
			obj.setR20_provi_ins_sec(rs.getString("r20_provi_ins_sec"));
			obj.setR20_provi_loan_loss(rs.getBigDecimal("r20_provi_loan_loss"));
			obj.setR21_provi_ins_sec(rs.getString("r21_provi_ins_sec"));
			obj.setR21_provi_loan_loss(rs.getBigDecimal("r21_provi_loan_loss"));
			obj.setR22_provi_ins_sec(rs.getString("r22_provi_ins_sec"));
			obj.setR22_provi_loan_loss(rs.getBigDecimal("r22_provi_loan_loss"));
			obj.setR23_provi_ins_sec(rs.getString("r23_provi_ins_sec"));
			obj.setR23_provi_loan_loss(rs.getBigDecimal("r23_provi_loan_loss"));
			obj.setR24_provi_ins_sec(rs.getString("r24_provi_ins_sec"));
			obj.setR24_provi_loan_loss(rs.getBigDecimal("r24_provi_loan_loss"));
			obj.setR25_provi_ins_sec(rs.getString("r25_provi_ins_sec"));
			obj.setR25_provi_loan_loss(rs.getBigDecimal("r25_provi_loan_loss"));
			obj.setR26_provi_ins_sec(rs.getString("r26_provi_ins_sec"));
			obj.setR26_provi_loan_loss(rs.getBigDecimal("r26_provi_loan_loss"));
			obj.setR27_provi_ins_sec(rs.getString("r27_provi_ins_sec"));
			obj.setR27_provi_loan_loss(rs.getBigDecimal("r27_provi_loan_loss"));
			obj.setR28_provi_ins_sec(rs.getString("r28_provi_ins_sec"));
			obj.setR28_provi_loan_loss(rs.getBigDecimal("r28_provi_loan_loss"));
			obj.setR29_provi_ins_sec(rs.getString("r29_provi_ins_sec"));
			obj.setR29_provi_loan_loss(rs.getBigDecimal("r29_provi_loan_loss"));
			obj.setR30_provi_ins_sec(rs.getString("r30_provi_ins_sec"));
			obj.setR30_provi_loan_loss(rs.getBigDecimal("r30_provi_loan_loss"));
			obj.setR31_provi_ins_sec(rs.getString("r31_provi_ins_sec"));
			obj.setR31_provi_loan_loss(rs.getBigDecimal("r31_provi_loan_loss"));
			obj.setR32_provi_ins_sec(rs.getString("r32_provi_ins_sec"));
			obj.setR32_provi_loan_loss(rs.getBigDecimal("r32_provi_loan_loss"));
			obj.setR33_provi_ins_sec(rs.getString("r33_provi_ins_sec"));
			obj.setR33_provi_loan_loss(rs.getBigDecimal("r33_provi_loan_loss"));
			obj.setR34_provi_ins_sec(rs.getString("r34_provi_ins_sec"));
			obj.setR34_provi_loan_loss(rs.getBigDecimal("r34_provi_loan_loss"));
			obj.setR35_provi_ins_sec(rs.getString("r35_provi_ins_sec"));
			obj.setR35_provi_loan_loss(rs.getBigDecimal("r35_provi_loan_loss"));
			obj.setR36_provi_ins_sec(rs.getString("r36_provi_ins_sec"));
			obj.setR36_provi_loan_loss(rs.getBigDecimal("r36_provi_loan_loss"));
			obj.setR37_provi_ins_sec(rs.getString("r37_provi_ins_sec"));
			obj.setR37_provi_loan_loss(rs.getBigDecimal("r37_provi_loan_loss"));
			obj.setR38_provi_ins_sec(rs.getString("r38_provi_ins_sec"));
			obj.setR38_provi_loan_loss(rs.getBigDecimal("r38_provi_loan_loss"));
			obj.setR39_provi_ins_sec(rs.getString("r39_provi_ins_sec"));
			obj.setR39_provi_loan_loss(rs.getBigDecimal("r39_provi_loan_loss"));
			obj.setR40_provi_ins_sec(rs.getString("r40_provi_ins_sec"));
			obj.setR40_provi_loan_loss(rs.getBigDecimal("r40_provi_loan_loss"));
			obj.setR41_provi_ins_sec(rs.getString("r41_provi_ins_sec"));
			obj.setR41_provi_loan_loss(rs.getBigDecimal("r41_provi_loan_loss"));
			obj.setR42_provi_ins_sec(rs.getString("r42_provi_ins_sec"));
			obj.setR42_provi_loan_loss(rs.getBigDecimal("r42_provi_loan_loss"));
			obj.setR43_provi_ins_sec(rs.getString("r43_provi_ins_sec"));
			obj.setR43_provi_loan_loss(rs.getBigDecimal("r43_provi_loan_loss"));
			obj.setR44_provi_ins_sec(rs.getString("r44_provi_ins_sec"));
			obj.setR44_provi_loan_loss(rs.getBigDecimal("r44_provi_loan_loss"));
			obj.setR45_provi_ins_sec(rs.getString("r45_provi_ins_sec"));
			obj.setR45_provi_loan_loss(rs.getBigDecimal("r45_provi_loan_loss"));
			obj.setR46_provi_ins_sec(rs.getString("r46_provi_ins_sec"));
			obj.setR46_provi_loan_loss(rs.getBigDecimal("r46_provi_loan_loss"));
			obj.setR47_provi_ins_sec(rs.getString("r47_provi_ins_sec"));
			obj.setR47_provi_loan_loss(rs.getBigDecimal("r47_provi_loan_loss"));
			obj.setR48_provi_ins_sec(rs.getString("r48_provi_ins_sec"));
			obj.setR48_provi_loan_loss(rs.getBigDecimal("r48_provi_loan_loss"));
			obj.setR49_provi_ins_sec(rs.getString("r49_provi_ins_sec"));
			obj.setR49_provi_loan_loss(rs.getBigDecimal("r49_provi_loan_loss"));
			obj.setR50_provi_ins_sec(rs.getString("r50_provi_ins_sec"));
			obj.setR50_provi_loan_loss(rs.getBigDecimal("r50_provi_loan_loss"));
			obj.setR51_provi_ins_sec(rs.getString("r51_provi_ins_sec"));
			obj.setR51_provi_loan_loss(rs.getBigDecimal("r51_provi_loan_loss"));
			obj.setR52_provi_ins_sec(rs.getString("r52_provi_ins_sec"));
			obj.setR52_provi_loan_loss(rs.getBigDecimal("r52_provi_loan_loss"));
			obj.setR53_provi_ins_sec(rs.getString("r53_provi_ins_sec"));
			obj.setR53_provi_loan_loss(rs.getBigDecimal("r53_provi_loan_loss"));
			obj.setR54_provi_ins_sec(rs.getString("r54_provi_ins_sec"));
			obj.setR54_provi_loan_loss(rs.getBigDecimal("r54_provi_loan_loss"));
			obj.setR55_provi_ins_sec(rs.getString("r55_provi_ins_sec"));
			obj.setR55_provi_loan_loss(rs.getBigDecimal("r55_provi_loan_loss"));
			obj.setR56_provi_ins_sec(rs.getString("r56_provi_ins_sec"));
			obj.setR56_provi_loan_loss(rs.getBigDecimal("r56_provi_loan_loss"));
			obj.setR57_provi_ins_sec(rs.getString("r57_provi_ins_sec"));
			obj.setR57_provi_loan_loss(rs.getBigDecimal("r57_provi_loan_loss"));
			obj.setR58_provi_ins_sec(rs.getString("r58_provi_ins_sec"));
			obj.setR58_provi_loan_loss(rs.getBigDecimal("r58_provi_loan_loss"));
			obj.setR59_provi_ins_sec(rs.getString("r59_provi_ins_sec"));
			obj.setR59_provi_loan_loss(rs.getBigDecimal("r59_provi_loan_loss"));
			obj.setR60_provi_ins_sec(rs.getString("r60_provi_ins_sec"));
			obj.setR60_provi_loan_loss(rs.getBigDecimal("r60_provi_loan_loss"));
			obj.setR61_provi_ins_sec(rs.getString("r61_provi_ins_sec"));
			obj.setR61_provi_loan_loss(rs.getBigDecimal("r61_provi_loan_loss"));
			obj.setR62_provi_ins_sec(rs.getString("r62_provi_ins_sec"));
			obj.setR62_provi_loan_loss(rs.getBigDecimal("r62_provi_loan_loss"));
			obj.setR63_provi_ins_sec(rs.getString("r63_provi_ins_sec"));
			obj.setR63_provi_loan_loss(rs.getBigDecimal("r63_provi_loan_loss"));
			obj.setR64_provi_ins_sec(rs.getString("r64_provi_ins_sec"));
			obj.setR64_provi_loan_loss(rs.getBigDecimal("r64_provi_loan_loss"));
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));
			return obj;
		}
	}

	// ------------------------------
	// Row Mapper for M_PLL_Archival_Detail_Entity
	// ------------------------------
	class M_PLLArchivalDetailRowMapper implements RowMapper<M_PLL_Archival_Detail_Entity> {
		@Override
		public M_PLL_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_PLL_Archival_Detail_Entity obj = new M_PLL_Archival_Detail_Entity();
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

}