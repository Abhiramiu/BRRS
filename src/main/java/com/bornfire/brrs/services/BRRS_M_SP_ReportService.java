package com.bornfire.brrs.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import java.io.Serializable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Id;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

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
import java.util.Objects;
import java.util.stream.Collectors;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;

import com.bornfire.brrs.entities.UserProfileRep;

@Component
@Service

public class BRRS_M_SP_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SP_ReportService.class);

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
	// Method to retrieve and display the M_SP summary view
	// ------------------------------

	public ModelAndView getM_SPView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {
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

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_SP_Archival_Summary_Entity> T1Master = new ArrayList<M_SP_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = jdbcTemplate.query(
						"SELECT * FROM BRRS_M_SP_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
						new Object[] { dateformat.parse(todate), version }, new M_SP_Archival_Summary_RowMapper());

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {

			List<M_SP_Summary_Entity> T1Master = new ArrayList<M_SP_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = jdbcTemplate.query("SELECT * FROM BRRS_M_SP_SUMMARYTABLE WHERE REPORT_DATE = ?",
						new Object[] { dateformat.parse(todate) }, new M_SP_Summary_RowMapper());

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_SP");

		// mv.addObject("reportsummary", T1Master);
		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;
	}

	// ------------------------------
	// Method to retrieve and display the M_SP current detail view
	// ------------------------------

	public ModelAndView getM_SPcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version, HttpServletRequest req1,
			Model md) {

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
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

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

			if ("ARCHIVAL".equals(type) && version != null) {
				// 🔹 Archival branch
				List<M_SP_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = jdbcTemplate.query(
							"SELECT * FROM BRRS_M_SP_ARCHIVALTABLE_DETAIL WHERE REPORT_LABLE = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
							new Object[] { rowId, columnId, parsedDate, version },
							new M_SP_Archival_Detail_RowMapper());
				} else {
					T1Dt1 = jdbcTemplate.query(
							"SELECT * FROM BRRS_M_SP_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
							new Object[] { parsedDate, version }, new M_SP_Archival_Detail_RowMapper());
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_SP_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = jdbcTemplate.query(
							"SELECT * FROM BRRS_M_SP_DETAILTABLE WHERE REPORT_LABLE = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?",
							new Object[] { rowId, columnId, parsedDate }, new M_SP_Detail_RowMapper());
				} else {
					T1Dt1 = jdbcTemplate.query("SELECT * FROM BRRS_M_SP_DETAILTABLE WHERE REPORT_DATE = ?",
							new Object[] { parsedDate }, new M_SP_Detail_RowMapper());
					totalPages = jdbcTemplate.queryForObject(
							"SELECT COUNT(*) FROM BRRS_M_SP_DETAILTABLE WHERE REPORT_DATE = ?",
							new Object[] { parsedDate }, Integer.class);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}

		} catch (ParseException e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Invalid date format: " + todate);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

		// ✅ Common attributes
		mv.setViewName("BRRS/M_SP");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	// ------------------------------
	// Method to generate and download the M_SP Excel summary report
	// ------------------------------

	public byte[] getM_SPExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_SPARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<M_SP_Summary_Entity> dataList = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SP_SUMMARYTABLE WHERE REPORT_DATE = ?", new Object[] { dateformat.parse(todate) },
				new M_SP_Summary_RowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SP report. Returning empty result.");
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

			int startRow = 9;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SP_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R10
					// Column C
					Cell cell6 = row.createCell(1);
					if (record.getR10_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR10_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R10
					// Column D
					Cell cell7 = row.createCell(2);
					if (record.getR10_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR10_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					// R11
					// Column C
					cell6 = row.createCell(1);
					if (record.getR11_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR11_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R11
					// Column D
					cell7 = row.createCell(2);
					if (record.getR11_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR11_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R12	
					row = sheet.getRow(11);
					// R12
					// Column C
					cell6 = row.createCell(1);
					if (record.getR12_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR12_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R12
					// Column D
					cell7 = row.createCell(2);
					if (record.getR12_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR12_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R14		
					row = sheet.getRow(13);
					// R14
					// Column C
					cell6 = row.createCell(1);
					if (record.getR14_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR14_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R14
					// Column D
					cell7 = row.createCell(2);
					if (record.getR14_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR14_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R15			
					row = sheet.getRow(14);

					// R15
					// Column C
					cell6 = row.createCell(1);
					if (record.getR15_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR15_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R15
					// Column D
					cell7 = row.createCell(2);
					if (record.getR15_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR15_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R16			
					row = sheet.getRow(15);
					// R16
					// Column C
					cell6 = row.createCell(1);
					if (record.getR16_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR16_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R16
					// Column D
					cell7 = row.createCell(2);
					if (record.getR16_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR16_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// R17
					row = sheet.getRow(16);

					// R17
					// Column C
					cell6 = row.createCell(1);
					if (record.getR17_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR17_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R17
					// Column D
					cell7 = row.createCell(2);
					if (record.getR17_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR17_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R18				
					row = sheet.getRow(17);

					// R18
					// Column C
					cell6 = row.createCell(1);
					if (record.getR18_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR18_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R18
					// Column D
					cell7 = row.createCell(2);
					if (record.getR18_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR18_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R19				
					row = sheet.getRow(18);
					// R19
					// Column C
					cell6 = row.createCell(1);
					if (record.getR19_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR19_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R19
					// Column D
					cell7 = row.createCell(2);
					if (record.getR19_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR19_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R20				
					row = sheet.getRow(19);
					// R20
					// Column C
					cell6 = row.createCell(1);
					if (record.getR20_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR20_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R20
					// Column D
					cell7 = row.createCell(2);
					if (record.getR20_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR20_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R21				
					row = sheet.getRow(20);
					// R21
					// Column C
					cell6 = row.createCell(1);
					if (record.getR21_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR21_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R21
					// Column D
					cell7 = row.createCell(2);
					if (record.getR21_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR21_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R22				
					row = sheet.getRow(21);

					// R22
					// Column C
					cell6 = row.createCell(1);
					if (record.getR22_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR22_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R22
					// Column D
					cell7 = row.createCell(2);
					if (record.getR22_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR22_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R23				
					row = sheet.getRow(22);

					// R23
					// Column C
					cell6 = row.createCell(1);
					if (record.getR23_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR23_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R23
					// Column D
					cell7 = row.createCell(2);
					if (record.getR23_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR23_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R24				
					row = sheet.getRow(23);
					// R24
					// Column C
					cell6 = row.createCell(1);
					if (record.getR24_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR24_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R20
					// Column D
					cell7 = row.createCell(2);
					if (record.getR24_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR24_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R25				
					row = sheet.getRow(24);

					// R25
					// Column C
					cell6 = row.createCell(1);
					if (record.getR25_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR25_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R25
					// Column D
					cell7 = row.createCell(2);
					if (record.getR25_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR25_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R26				
					row = sheet.getRow(25);
					// R26
					// Column C
					cell6 = row.createCell(1);
					if (record.getR26_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR26_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R26
					// Column D
					cell7 = row.createCell(2);
					if (record.getR26_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR26_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R28				
					row = sheet.getRow(27);

					// R28
					// Column C
					cell6 = row.createCell(1);
					if (record.getR28_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR28_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R28
					// Column D
					cell7 = row.createCell(2);
					if (record.getR28_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR28_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R29				
					row = sheet.getRow(28);

					// R29
					// Column C
					cell6 = row.createCell(1);
					if (record.getR29_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR29_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R29
					// Column D
					cell7 = row.createCell(2);
					if (record.getR29_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR29_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R30				
					row = sheet.getRow(29);

					// R30
					// Column C
					cell6 = row.createCell(1);
					if (record.getR30_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR30_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R30
					// Column D
					cell7 = row.createCell(2);
					if (record.getR30_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR30_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R31			
					row = sheet.getRow(30);
					// R31
					// Column C
					cell6 = row.createCell(1);
					if (record.getR31_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR31_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R31
					// Column D
					cell7 = row.createCell(2);
					if (record.getR31_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR31_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R32				
					row = sheet.getRow(31);

					// R32
					// Column C
					cell6 = row.createCell(1);
					if (record.getR32_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR32_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R32
					// Column D
					cell7 = row.createCell(2);
					if (record.getR32_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR32_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R33				
					row = sheet.getRow(32);

					// R33
					// Column C
					cell6 = row.createCell(1);
					if (record.getR33_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR33_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R33
					// Column D
					cell7 = row.createCell(2);
					if (record.getR33_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR33_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R34				
					row = sheet.getRow(33);

					// R34
					// Column C
					cell6 = row.createCell(1);
					if (record.getR34_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR34_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R34
					// Column D
					cell7 = row.createCell(2);
					if (record.getR34_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR34_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R35				
					row = sheet.getRow(34);

					// R35
					// Column C
					cell6 = row.createCell(1);
					if (record.getR35_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR35_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R35
					// Column D
					cell7 = row.createCell(2);
					if (record.getR35_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR35_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R37				
					row = sheet.getRow(36);

					// R37
					// Column C
					cell6 = row.createCell(1);
					if (record.getR37_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR37_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R37
					// Column D
					cell7 = row.createCell(2);
					if (record.getR37_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR37_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R38				
					row = sheet.getRow(37);

					// R38
					// Column C
					cell6 = row.createCell(1);
					if (record.getR38_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR38_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R38
					// Column D
					cell7 = row.createCell(2);
					if (record.getR38_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR38_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R40				
					row = sheet.getRow(39);

					// R40
					// Column C
					cell6 = row.createCell(1);
					if (record.getR40_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR40_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R41
					// Column D
					cell7 = row.createCell(2);
					if (record.getR40_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR40_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R41				
					row = sheet.getRow(40);

					// R41
					// Column C
					cell6 = row.createCell(1);
					if (record.getR41_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR41_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R41
					// Column D
					cell7 = row.createCell(2);
					if (record.getR41_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR41_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R43				
					row = sheet.getRow(42);

					// R43
					// Column C
					cell6 = row.createCell(1);
					if (record.getR43_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR43_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R43
					// Column D
					cell7 = row.createCell(2);
					if (record.getR43_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR43_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R44				
					row = sheet.getRow(43);

					// R44
					// Column C
					cell6 = row.createCell(1);
					if (record.getR44_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR44_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R43
					// Column D
					cell7 = row.createCell(2);
					if (record.getR44_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR44_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R45				
					row = sheet.getRow(44);

					// R45
					// Column C
					cell6 = row.createCell(1);
					if (record.getR45_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR45_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R45
					// Column D
					cell7 = row.createCell(2);
					if (record.getR45_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR45_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R46				
					row = sheet.getRow(45);

					// R46
					// Column C
					cell6 = row.createCell(1);
					if (record.getR46_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR46_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R46
					// Column D
					cell7 = row.createCell(2);
					if (record.getR46_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR46_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R48				
					row = sheet.getRow(47);

					// R48
					// Column C
					cell6 = row.createCell(1);
					if (record.getR48_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR48_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R48
					// Column D
					cell7 = row.createCell(2);
					if (record.getR48_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR48_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
//R49				

					row = sheet.getRow(48);

					// R49
					// Column C
					cell6 = row.createCell(1);
					if (record.getR49_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR49_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R49
					// Column D
					cell7 = row.createCell(2);
					if (record.getR49_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR49_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R50					
					row = sheet.getRow(49);

					// R50
					// Column C
					cell6 = row.createCell(1);
					if (record.getR50_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR50_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R50
					// Column D
					cell7 = row.createCell(2);
					if (record.getR50_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR50_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R52						
					row = sheet.getRow(51);

					// R52
					// Column C
					cell6 = row.createCell(1);
					if (record.getR52_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR52_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R52
					// Column D
					cell7 = row.createCell(2);
					if (record.getR52_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR52_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R53						
					row = sheet.getRow(52);

					// R53
					// Column C
					cell6 = row.createCell(1);
					if (record.getR53_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR53_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R53
					// Column D
					cell7 = row.createCell(2);
					if (record.getR53_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR53_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R54						
					row = sheet.getRow(53);

					// R54
					// Column C
					cell6 = row.createCell(1);
					if (record.getR54_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR54_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R54
					// Column D
					cell7 = row.createCell(2);
					if (record.getR54_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR54_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R56						
					row = sheet.getRow(55);

					// R56
					// Column C
					cell6 = row.createCell(1);
					if (record.getR56_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR56_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R56
					// Column D
					cell7 = row.createCell(2);
					if (record.getR56_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR56_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R57						
					row = sheet.getRow(56);

					// R57
					// Column C
					cell6 = row.createCell(1);
					if (record.getR57_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR57_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R57
					// Column D
					cell7 = row.createCell(2);
					if (record.getR57_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR57_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R58						
					row = sheet.getRow(57);

					// R58
					// Column C
					cell6 = row.createCell(1);
					if (record.getR58_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR58_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R58
					// Column D
					cell7 = row.createCell(2);
					if (record.getR58_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR58_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R59						
					row = sheet.getRow(58);

					// R59
					// Column C
					cell6 = row.createCell(1);
					if (record.getR59_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR59_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R59
					// Column D
					cell7 = row.createCell(2);
					if (record.getR59_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR59_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R60						
					row = sheet.getRow(59);

					// R60
					// Column C
					cell6 = row.createCell(1);
					if (record.getR60_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR60_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R60
					// Column D
					cell7 = row.createCell(2);
					if (record.getR60_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR60_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R61						
					row = sheet.getRow(60);

					// R61
					// Column C
					cell6 = row.createCell(1);
					if (record.getR61_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR61_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R60
					// Column D
					cell7 = row.createCell(2);
					if (record.getR61_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR61_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SP SUMMARY", null, "BRRS_M_SP_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	// ------------------------------
	// Method to generate and download the M_SP Excel detail report
	// ------------------------------

	public byte[] getM_SPDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_SP Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_SPDetails");

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
			String[] headers = { "CUST ID", "ACCT NUMBER", "ACCT NAME", "ACCT BALANCE IN PULA", "PROVISION",
					"REPORT LABEL", "REPORT ADDL CRITERIA", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_SP_Detail_Entity> reportData = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_SP_DETAILTABLE WHERE REPORT_DATE = ?", new Object[] { parsedToDate },
					new M_SP_Detail_RowMapper());

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SP_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell1 = row.createCell(4);
					if (item.getProvision() != null) {
						balanceCell1.setCellValue(item.getProvision().doubleValue());
					} else {
						balanceCell1.setCellValue(0);
					}
					balanceCell1.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLable());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_SP — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_SP Excel", e);
			return new byte[0];
		}
	}

// ------------------------------
// Method to retrieve list of archival versions for M_SP
// ------------------------------

	public List<Object> getM_SPArchival() {
		List<Object> M_SPArchivallist = new ArrayList<>();
		try {
			List<Object[]> queryList = jdbcTemplate.query(
					"SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_M_SP_ARCHIVALTABLE_SUMMARY ORDER BY REPORT_VERSION",
					(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
			M_SPArchivallist = new ArrayList<>(queryList);
			System.out.println("countser" + M_SPArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_SP Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_SPArchivallist;
	}

	// ------------------------------
	// Method to generate and download archival summary Excel report
	// ------------------------------

	public byte[] getExcelM_SPARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}
		System.out.println("Testing");
		List<M_SP_Archival_Summary_Entity> dataList = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SP_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version }, new M_SP_Archival_Summary_RowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SP report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting1 to load template from path: {}", templatePath.toAbsolutePath());

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
			int startRow = 9;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SP_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R10
					// Column C
					Cell cell6 = row.createCell(1);
					if (record.getR10_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR10_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R10
					// Column D
					Cell cell7 = row.createCell(2);
					if (record.getR10_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR10_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					// R11
					// Column C
					cell6 = row.createCell(1);
					if (record.getR11_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR11_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R11
					// Column D
					cell7 = row.createCell(2);
					if (record.getR11_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR11_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R12	
					row = sheet.getRow(11);
					// R12
					// Column C
					cell6 = row.createCell(1);
					if (record.getR12_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR12_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R12
					// Column D
					cell7 = row.createCell(2);
					if (record.getR12_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR12_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R14		
					row = sheet.getRow(13);
					// R14
					// Column C
					cell6 = row.createCell(1);
					if (record.getR14_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR14_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R14
					// Column D
					cell7 = row.createCell(2);
					if (record.getR14_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR14_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R15			
					row = sheet.getRow(14);

					// R15
					// Column C
					cell6 = row.createCell(1);
					if (record.getR15_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR15_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R15
					// Column D
					cell7 = row.createCell(2);
					if (record.getR15_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR15_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R16			
					row = sheet.getRow(15);
					// R16
					// Column C
					cell6 = row.createCell(1);
					if (record.getR16_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR16_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R16
					// Column D
					cell7 = row.createCell(2);
					if (record.getR16_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR16_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// R17
					row = sheet.getRow(16);

					// R17
					// Column C
					cell6 = row.createCell(1);
					if (record.getR17_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR17_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R17
					// Column D
					cell7 = row.createCell(2);
					if (record.getR17_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR17_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R18				
					row = sheet.getRow(17);

					// R18
					// Column C
					cell6 = row.createCell(1);
					if (record.getR18_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR18_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R18
					// Column D
					cell7 = row.createCell(2);
					if (record.getR18_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR18_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R19				
					row = sheet.getRow(18);
					// R19
					// Column C
					cell6 = row.createCell(1);
					if (record.getR19_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR19_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R19
					// Column D
					cell7 = row.createCell(2);
					if (record.getR19_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR19_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R20				
					row = sheet.getRow(19);
					// R20
					// Column C
					cell6 = row.createCell(1);
					if (record.getR20_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR20_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R20
					// Column D
					cell7 = row.createCell(2);
					if (record.getR20_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR20_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R21				
					row = sheet.getRow(20);
					// R21
					// Column C
					cell6 = row.createCell(1);
					if (record.getR21_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR21_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R21
					// Column D
					cell7 = row.createCell(2);
					if (record.getR21_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR21_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R22				
					row = sheet.getRow(21);

					// R22
					// Column C
					cell6 = row.createCell(1);
					if (record.getR22_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR22_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R22
					// Column D
					cell7 = row.createCell(2);
					if (record.getR22_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR22_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R23				
					row = sheet.getRow(22);

					// R23
					// Column C
					cell6 = row.createCell(1);
					if (record.getR23_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR23_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R23
					// Column D
					cell7 = row.createCell(2);
					if (record.getR23_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR23_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R24				
					row = sheet.getRow(23);
					// R24
					// Column C
					cell6 = row.createCell(1);
					if (record.getR24_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR24_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R20
					// Column D
					cell7 = row.createCell(2);
					if (record.getR24_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR24_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R25				
					row = sheet.getRow(24);

					// R25
					// Column C
					cell6 = row.createCell(1);
					if (record.getR25_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR25_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R25
					// Column D
					cell7 = row.createCell(2);
					if (record.getR25_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR25_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R26				
					row = sheet.getRow(25);
					// R26
					// Column C
					cell6 = row.createCell(1);
					if (record.getR26_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR26_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R26
					// Column D
					cell7 = row.createCell(2);
					if (record.getR26_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR26_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R28				
					row = sheet.getRow(27);

					// R28
					// Column C
					cell6 = row.createCell(1);
					if (record.getR28_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR28_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R28
					// Column D
					cell7 = row.createCell(2);
					if (record.getR28_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR28_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R29				
					row = sheet.getRow(28);

					// R29
					// Column C
					cell6 = row.createCell(1);
					if (record.getR29_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR29_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R29
					// Column D
					cell7 = row.createCell(2);
					if (record.getR29_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR29_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R30				
					row = sheet.getRow(29);

					// R30
					// Column C
					cell6 = row.createCell(1);
					if (record.getR30_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR30_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R30
					// Column D
					cell7 = row.createCell(2);
					if (record.getR30_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR30_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R31			
					row = sheet.getRow(30);
					// R31
					// Column C
					cell6 = row.createCell(1);
					if (record.getR31_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR31_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R31
					// Column D
					cell7 = row.createCell(2);
					if (record.getR31_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR31_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R32				
					row = sheet.getRow(31);

					// R32
					// Column C
					cell6 = row.createCell(1);
					if (record.getR32_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR32_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R32
					// Column D
					cell7 = row.createCell(2);
					if (record.getR32_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR32_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R33				
					row = sheet.getRow(32);

					// R33
					// Column C
					cell6 = row.createCell(1);
					if (record.getR33_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR33_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R33
					// Column D
					cell7 = row.createCell(2);
					if (record.getR33_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR33_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R34				
					row = sheet.getRow(33);

					// R34
					// Column C
					cell6 = row.createCell(1);
					if (record.getR34_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR34_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R34
					// Column D
					cell7 = row.createCell(2);
					if (record.getR34_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR34_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R35				
					row = sheet.getRow(34);

					// R35
					// Column C
					cell6 = row.createCell(1);
					if (record.getR35_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR35_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R35
					// Column D
					cell7 = row.createCell(2);
					if (record.getR35_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR35_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R37				
					row = sheet.getRow(36);

					// R37
					// Column C
					cell6 = row.createCell(1);
					if (record.getR37_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR37_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R37
					// Column D
					cell7 = row.createCell(2);
					if (record.getR37_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR37_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R38				
					row = sheet.getRow(37);

					// R38
					// Column C
					cell6 = row.createCell(1);
					if (record.getR38_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR38_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R38
					// Column D
					cell7 = row.createCell(2);
					if (record.getR38_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR38_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R40				
					row = sheet.getRow(39);

					// R40
					// Column C
					cell6 = row.createCell(1);
					if (record.getR40_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR40_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R41
					// Column D
					cell7 = row.createCell(2);
					if (record.getR40_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR40_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R41				
					row = sheet.getRow(40);

					// R41
					// Column C
					cell6 = row.createCell(1);
					if (record.getR41_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR41_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R41
					// Column D
					cell7 = row.createCell(2);
					if (record.getR41_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR41_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R43				
					row = sheet.getRow(42);

					// R43
					// Column C
					cell6 = row.createCell(1);
					if (record.getR43_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR43_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R43
					// Column D
					cell7 = row.createCell(2);
					if (record.getR43_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR43_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R44				
					row = sheet.getRow(43);

					// R44
					// Column C
					cell6 = row.createCell(1);
					if (record.getR44_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR44_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R43
					// Column D
					cell7 = row.createCell(2);
					if (record.getR44_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR44_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R45				
					row = sheet.getRow(44);

					// R45
					// Column C
					cell6 = row.createCell(1);
					if (record.getR45_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR45_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R45
					// Column D
					cell7 = row.createCell(2);
					if (record.getR45_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR45_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R46				
					row = sheet.getRow(45);

					// R46
					// Column C
					cell6 = row.createCell(1);
					if (record.getR46_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR46_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R46
					// Column D
					cell7 = row.createCell(2);
					if (record.getR46_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR46_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R48				

					row = sheet.getRow(47);

					// R48
					// Column C
					cell6 = row.createCell(1);
					if (record.getR48_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR48_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R48
					// Column D
					cell7 = row.createCell(2);
					if (record.getR48_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR48_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
//R49				

					row = sheet.getRow(48);

					// R49
					// Column C
					cell6 = row.createCell(1);
					if (record.getR49_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR49_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R49
					// Column D
					cell7 = row.createCell(2);
					if (record.getR49_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR49_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R50					
					row = sheet.getRow(49);

					// R50
					// Column C
					cell6 = row.createCell(1);
					if (record.getR50_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR50_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R50
					// Column D
					cell7 = row.createCell(2);
					if (record.getR50_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR50_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R52						
					row = sheet.getRow(51);

					// R52
					// Column C
					cell6 = row.createCell(1);
					if (record.getR52_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR52_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R52
					// Column D
					cell7 = row.createCell(2);
					if (record.getR52_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR52_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R53						
					row = sheet.getRow(52);

					// R53
					// Column C
					cell6 = row.createCell(1);
					if (record.getR53_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR53_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R53
					// Column D
					cell7 = row.createCell(2);
					if (record.getR53_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR53_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R54						
					row = sheet.getRow(53);

					// R54
					// Column C
					cell6 = row.createCell(1);
					if (record.getR54_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR54_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R54
					// Column D
					cell7 = row.createCell(2);
					if (record.getR54_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR54_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R56						
					row = sheet.getRow(55);

					// R56
					// Column C
					cell6 = row.createCell(1);
					if (record.getR56_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR56_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R56
					// Column D
					cell7 = row.createCell(2);
					if (record.getR56_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR56_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R57						
					row = sheet.getRow(56);

					// R57
					// Column C
					cell6 = row.createCell(1);
					if (record.getR57_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR57_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R57
					// Column D
					cell7 = row.createCell(2);
					if (record.getR57_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR57_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R58						
					row = sheet.getRow(57);

					// R58
					// Column C
					cell6 = row.createCell(1);
					if (record.getR58_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR58_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R58
					// Column D
					cell7 = row.createCell(2);
					if (record.getR58_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR58_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R59						
					row = sheet.getRow(58);

					// R59
					// Column C
					cell6 = row.createCell(1);
					if (record.getR59_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR59_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R59
					// Column D
					cell7 = row.createCell(2);
					if (record.getR59_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR59_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R60						
					row = sheet.getRow(59);

					// R60
					// Column C
					cell6 = row.createCell(1);
					if (record.getR60_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR60_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R60
					// Column D
					cell7 = row.createCell(2);
					if (record.getR60_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR60_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//R61						
					row = sheet.getRow(60);

					// R61
					// Column C
					cell6 = row.createCell(1);
					if (record.getR61_qualifi_stage_2_provisions_sp() != null) {
						cell6.setCellValue(record.getR61_qualifi_stage_2_provisions_sp().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R60
					// Column D
					cell7 = row.createCell(2);
					if (record.getR61_stage_3_provisions_sp() != null) {
						cell7.setCellValue(record.getR61_stage_3_provisions_sp().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SP ARCHIVAL SUMMARY", null,
						"BRRS_M_SP_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

// ------------------------------
// Method to generate and download archival detail Excel report
// ------------------------------

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_SP ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_SPDetails");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "PROVISION", "REPORT LABEL",
					"REPORT ADDL CRITERIA", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_SP_Archival_Detail_Entity> reportData = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_SP_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
					new Object[] { parsedToDate, version }, new M_SP_Archival_Detail_RowMapper());

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SP_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell1 = row.createCell(4);
					if (item.getProvision() != null) {
						balanceCell1.setCellValue(item.getProvision().doubleValue());
					} else {
						balanceCell1.setCellValue(0);
					}
					balanceCell1.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLable());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_SP — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_SP Excel", e);
			return new byte[0];
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

// ------------------------------
// Method to retrieve detail edit page for a given account number
// ------------------------------

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_SP"); // ✅ match the report name
		System.out.println("Hello");
		if (acctNo != null) {
			List<M_SP_Detail_Entity> queryList = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_SP_DETAILTABLE WHERE ACCT_NUMBER = ?", new Object[] { acctNo },
					new M_SP_Detail_RowMapper());
			M_SP_Detail_Entity la1Entity = queryList.isEmpty() ? null : queryList.get(0);
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

	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_SP"); // ✅ match the report name

		if (acctNo != null) {
			List<M_SP_Detail_Entity> queryList = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_SP_DETAILTABLE WHERE ACCT_NUMBER = ?", new Object[] { acctNo },
					new M_SP_Detail_RowMapper());
			M_SP_Detail_Entity la1Entity = queryList.isEmpty() ? null : queryList.get(0);
			if (la1Entity != null && la1Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
				System.out.println(formattedDate);
			}
			mv.addObject("Data", la1Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional

// ------------------------------

// Method to process details updates and invoke summary procedure

// ------------------------------

	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("acctBalanceInPula");
			String acctName = request.getParameter("acctName");
			String provision = request.getParameter("provision");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			List<M_SP_Detail_Entity> queryList = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_SP_DETAILTABLE WHERE ACCT_NUMBER = ?", new Object[] { acctNo },
					new M_SP_Detail_RowMapper());
			M_SP_Detail_Entity existing = queryList.isEmpty() ? null : queryList.get(0);
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
				if (existing.getAcctBalanceInPula() == null
						|| existing.getAcctBalanceInPula().compareTo(newProvision) != 0) {
					existing.setAcctBalanceInPula(newProvision);
					isChanged = true;
					logger.info("Balance updated to {}", newProvision);
				}
			}

			if (provision != null && !provision.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provision);
				if (existing.getProvision() == null || existing.getProvision().compareTo(newProvision) != 0) {
					existing.setProvision(newProvision);
					isChanged = true;
					logger.info("Balance updated to {}", newProvision);
				}
			}

			if (isChanged) {
				jdbcTemplate.update(
						"UPDATE BRRS_M_SP_DETAILTABLE SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ?, PROVISION = ? WHERE ACCT_NUMBER = ?",
						existing.getAcctName(), existing.getAcctBalanceInPula(), existing.getProvision(),
						existing.getAcctNumber());
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_M_SP_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_M_SP_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating M_SP record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	// =========================================================================
	// INNER CLASSES AND ROW MAPPERS
	// =========================================================================

	// ------------------------------
	// RowMapper for mapping ResultSet rows to M_SP_Detail_Entity
	// ------------------------------
	class M_SP_Detail_RowMapper implements RowMapper<M_SP_Detail_Entity> {
		@Override
		public M_SP_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SP_Detail_Entity obj = new M_SP_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportLable(rs.getString("REPORT_LABLE"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			obj.setReportAddlCriteria3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			obj.setSanctionLimit(rs.getBigDecimal("SANCTION_LIMIT"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInPula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
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
			obj.setSegment(rs.getString("SEGMENT"));
			obj.setProvision(rs.getBigDecimal("PROVISION"));
			return obj;
		}
	}

	// ------------------------------
	// RowMapper for mapping ResultSet rows to M_SP_Summary_Entity
	// ------------------------------
	class M_SP_Summary_RowMapper implements RowMapper<M_SP_Summary_Entity> {
		@Override
		public M_SP_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SP_Summary_Entity obj = new M_SP_Summary_Entity();
			obj.setR9_product(rs.getString("R9_PRODUCT"));
			obj.setR9_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R9_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR9_stage_3_provisions_sp(rs.getBigDecimal("R9_STAGE_3_PROVISIONS_SP"));
			obj.setR9_total_specific_provisions(rs.getBigDecimal("R9_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R10_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR10_stage_3_provisions_sp(rs.getBigDecimal("R10_STAGE_3_PROVISIONS_SP"));
			obj.setR10_total_specific_provisions(rs.getBigDecimal("R10_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R11_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR11_stage_3_provisions_sp(rs.getBigDecimal("R11_STAGE_3_PROVISIONS_SP"));
			obj.setR11_total_specific_provisions(rs.getBigDecimal("R11_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R12_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR12_stage_3_provisions_sp(rs.getBigDecimal("R12_STAGE_3_PROVISIONS_SP"));
			obj.setR12_total_specific_provisions(rs.getBigDecimal("R12_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R13_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR13_stage_3_provisions_sp(rs.getBigDecimal("R13_STAGE_3_PROVISIONS_SP"));
			obj.setR13_total_specific_provisions(rs.getBigDecimal("R13_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R14_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR14_stage_3_provisions_sp(rs.getBigDecimal("R14_STAGE_3_PROVISIONS_SP"));
			obj.setR14_total_specific_provisions(rs.getBigDecimal("R14_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R15_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR15_stage_3_provisions_sp(rs.getBigDecimal("R15_STAGE_3_PROVISIONS_SP"));
			obj.setR15_total_specific_provisions(rs.getBigDecimal("R15_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R16_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR16_stage_3_provisions_sp(rs.getBigDecimal("R16_STAGE_3_PROVISIONS_SP"));
			obj.setR16_total_specific_provisions(rs.getBigDecimal("R16_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R17_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR17_stage_3_provisions_sp(rs.getBigDecimal("R17_STAGE_3_PROVISIONS_SP"));
			obj.setR17_total_specific_provisions(rs.getBigDecimal("R17_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R18_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR18_stage_3_provisions_sp(rs.getBigDecimal("R18_STAGE_3_PROVISIONS_SP"));
			obj.setR18_total_specific_provisions(rs.getBigDecimal("R18_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R19_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR19_stage_3_provisions_sp(rs.getBigDecimal("R19_STAGE_3_PROVISIONS_SP"));
			obj.setR19_total_specific_provisions(rs.getBigDecimal("R19_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R20_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR20_stage_3_provisions_sp(rs.getBigDecimal("R20_STAGE_3_PROVISIONS_SP"));
			obj.setR20_total_specific_provisions(rs.getBigDecimal("R20_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R21_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR21_stage_3_provisions_sp(rs.getBigDecimal("R21_STAGE_3_PROVISIONS_SP"));
			obj.setR21_total_specific_provisions(rs.getBigDecimal("R21_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R22_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR22_stage_3_provisions_sp(rs.getBigDecimal("R22_STAGE_3_PROVISIONS_SP"));
			obj.setR22_total_specific_provisions(rs.getBigDecimal("R22_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R23_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR23_stage_3_provisions_sp(rs.getBigDecimal("R23_STAGE_3_PROVISIONS_SP"));
			obj.setR23_total_specific_provisions(rs.getBigDecimal("R23_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R24_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR24_stage_3_provisions_sp(rs.getBigDecimal("R24_STAGE_3_PROVISIONS_SP"));
			obj.setR24_total_specific_provisions(rs.getBigDecimal("R24_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R25_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR25_stage_3_provisions_sp(rs.getBigDecimal("R25_STAGE_3_PROVISIONS_SP"));
			obj.setR25_total_specific_provisions(rs.getBigDecimal("R25_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R26_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR26_stage_3_provisions_sp(rs.getBigDecimal("R26_STAGE_3_PROVISIONS_SP"));
			obj.setR26_total_specific_provisions(rs.getBigDecimal("R26_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R27_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR27_stage_3_provisions_sp(rs.getBigDecimal("R27_STAGE_3_PROVISIONS_SP"));
			obj.setR27_total_specific_provisions(rs.getBigDecimal("R27_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R28_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR28_stage_3_provisions_sp(rs.getBigDecimal("R28_STAGE_3_PROVISIONS_SP"));
			obj.setR28_total_specific_provisions(rs.getBigDecimal("R28_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R29_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR29_stage_3_provisions_sp(rs.getBigDecimal("R29_STAGE_3_PROVISIONS_SP"));
			obj.setR29_total_specific_provisions(rs.getBigDecimal("R29_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR30_product(rs.getString("R30_PRODUCT"));
			obj.setR30_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R30_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR30_stage_3_provisions_sp(rs.getBigDecimal("R30_STAGE_3_PROVISIONS_SP"));
			obj.setR30_total_specific_provisions(rs.getBigDecimal("R30_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R31_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR31_stage_3_provisions_sp(rs.getBigDecimal("R31_STAGE_3_PROVISIONS_SP"));
			obj.setR31_total_specific_provisions(rs.getBigDecimal("R31_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R32_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR32_stage_3_provisions_sp(rs.getBigDecimal("R32_STAGE_3_PROVISIONS_SP"));
			obj.setR32_total_specific_provisions(rs.getBigDecimal("R32_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R33_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR33_stage_3_provisions_sp(rs.getBigDecimal("R33_STAGE_3_PROVISIONS_SP"));
			obj.setR33_total_specific_provisions(rs.getBigDecimal("R33_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R34_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR34_stage_3_provisions_sp(rs.getBigDecimal("R34_STAGE_3_PROVISIONS_SP"));
			obj.setR34_total_specific_provisions(rs.getBigDecimal("R34_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R35_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR35_stage_3_provisions_sp(rs.getBigDecimal("R35_STAGE_3_PROVISIONS_SP"));
			obj.setR35_total_specific_provisions(rs.getBigDecimal("R35_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R36_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR36_stage_3_provisions_sp(rs.getBigDecimal("R36_STAGE_3_PROVISIONS_SP"));
			obj.setR36_total_specific_provisions(rs.getBigDecimal("R36_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R37_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR37_stage_3_provisions_sp(rs.getBigDecimal("R37_STAGE_3_PROVISIONS_SP"));
			obj.setR37_total_specific_provisions(rs.getBigDecimal("R37_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R38_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR38_stage_3_provisions_sp(rs.getBigDecimal("R38_STAGE_3_PROVISIONS_SP"));
			obj.setR38_total_specific_provisions(rs.getBigDecimal("R38_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR39_product(rs.getString("R39_PRODUCT"));
			obj.setR39_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R39_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR39_stage_3_provisions_sp(rs.getBigDecimal("R39_STAGE_3_PROVISIONS_SP"));
			obj.setR39_total_specific_provisions(rs.getBigDecimal("R39_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR40_product(rs.getString("R40_PRODUCT"));
			obj.setR40_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R40_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR40_stage_3_provisions_sp(rs.getBigDecimal("R40_STAGE_3_PROVISIONS_SP"));
			obj.setR40_total_specific_provisions(rs.getBigDecimal("R40_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R41_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR41_stage_3_provisions_sp(rs.getBigDecimal("R41_STAGE_3_PROVISIONS_SP"));
			obj.setR41_total_specific_provisions(rs.getBigDecimal("R41_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R42_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR42_stage_3_provisions_sp(rs.getBigDecimal("R42_STAGE_3_PROVISIONS_SP"));
			obj.setR42_total_specific_provisions(rs.getBigDecimal("R42_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R43_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR43_stage_3_provisions_sp(rs.getBigDecimal("R43_STAGE_3_PROVISIONS_SP"));
			obj.setR43_total_specific_provisions(rs.getBigDecimal("R43_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R44_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR44_stage_3_provisions_sp(rs.getBigDecimal("R44_STAGE_3_PROVISIONS_SP"));
			obj.setR44_total_specific_provisions(rs.getBigDecimal("R44_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R45_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR45_stage_3_provisions_sp(rs.getBigDecimal("R45_STAGE_3_PROVISIONS_SP"));
			obj.setR45_total_specific_provisions(rs.getBigDecimal("R45_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R46_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR46_stage_3_provisions_sp(rs.getBigDecimal("R46_STAGE_3_PROVISIONS_SP"));
			obj.setR46_total_specific_provisions(rs.getBigDecimal("R46_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R47_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR47_stage_3_provisions_sp(rs.getBigDecimal("R47_STAGE_3_PROVISIONS_SP"));
			obj.setR47_total_specific_provisions(rs.getBigDecimal("R47_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R48_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR48_stage_3_provisions_sp(rs.getBigDecimal("R48_STAGE_3_PROVISIONS_SP"));
			obj.setR48_total_specific_provisions(rs.getBigDecimal("R48_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R49_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR49_stage_3_provisions_sp(rs.getBigDecimal("R49_STAGE_3_PROVISIONS_SP"));
			obj.setR49_total_specific_provisions(rs.getBigDecimal("R49_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR50_product(rs.getString("R50_PRODUCT"));
			obj.setR50_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R50_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR50_stage_3_provisions_sp(rs.getBigDecimal("R50_STAGE_3_PROVISIONS_SP"));
			obj.setR50_total_specific_provisions(rs.getBigDecimal("R50_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR51_product(rs.getString("R51_PRODUCT"));
			obj.setR51_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R51_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR51_stage_3_provisions_sp(rs.getBigDecimal("R51_STAGE_3_PROVISIONS_SP"));
			obj.setR51_total_specific_provisions(rs.getBigDecimal("R51_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR52_product(rs.getString("R52_PRODUCT"));
			obj.setR52_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R52_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR52_stage_3_provisions_sp(rs.getBigDecimal("R52_STAGE_3_PROVISIONS_SP"));
			obj.setR52_total_specific_provisions(rs.getBigDecimal("R52_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR53_product(rs.getString("R53_PRODUCT"));
			obj.setR53_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R53_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR53_stage_3_provisions_sp(rs.getBigDecimal("R53_STAGE_3_PROVISIONS_SP"));
			obj.setR53_total_specific_provisions(rs.getBigDecimal("R53_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR54_product(rs.getString("R54_PRODUCT"));
			obj.setR54_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R54_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR54_stage_3_provisions_sp(rs.getBigDecimal("R54_STAGE_3_PROVISIONS_SP"));
			obj.setR54_total_specific_provisions(rs.getBigDecimal("R54_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR55_product(rs.getString("R55_PRODUCT"));
			obj.setR55_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R55_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR55_stage_3_provisions_sp(rs.getBigDecimal("R55_STAGE_3_PROVISIONS_SP"));
			obj.setR55_total_specific_provisions(rs.getBigDecimal("R55_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR56_product(rs.getString("R56_PRODUCT"));
			obj.setR56_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R56_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR56_stage_3_provisions_sp(rs.getBigDecimal("R56_STAGE_3_PROVISIONS_SP"));
			obj.setR56_total_specific_provisions(rs.getBigDecimal("R56_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR57_product(rs.getString("R57_PRODUCT"));
			obj.setR57_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R57_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR57_stage_3_provisions_sp(rs.getBigDecimal("R57_STAGE_3_PROVISIONS_SP"));
			obj.setR57_total_specific_provisions(rs.getBigDecimal("R57_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR58_product(rs.getString("R58_PRODUCT"));
			obj.setR58_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R58_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR58_stage_3_provisions_sp(rs.getBigDecimal("R58_STAGE_3_PROVISIONS_SP"));
			obj.setR58_total_specific_provisions(rs.getBigDecimal("R58_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR59_product(rs.getString("R59_PRODUCT"));
			obj.setR59_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R59_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR59_stage_3_provisions_sp(rs.getBigDecimal("R59_STAGE_3_PROVISIONS_SP"));
			obj.setR59_total_specific_provisions(rs.getBigDecimal("R59_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR60_product(rs.getString("R60_PRODUCT"));
			obj.setR60_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R60_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR60_stage_3_provisions_sp(rs.getBigDecimal("R60_STAGE_3_PROVISIONS_SP"));
			obj.setR60_total_specific_provisions(rs.getBigDecimal("R60_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR61_product(rs.getString("R61_PRODUCT"));
			obj.setR61_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R61_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR61_stage_3_provisions_sp(rs.getBigDecimal("R61_STAGE_3_PROVISIONS_SP"));
			obj.setR61_total_specific_provisions(rs.getBigDecimal("R61_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR62_product(rs.getString("R62_PRODUCT"));
			obj.setR62_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R62_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR62_stage_3_provisions_sp(rs.getBigDecimal("R62_STAGE_3_PROVISIONS_SP"));
			obj.setR62_total_specific_provisions(rs.getBigDecimal("R62_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// ------------------------------
	// RowMapper for mapping ResultSet rows to M_SP_Archival_Detail_Entity
	// ------------------------------
	class M_SP_Archival_Detail_RowMapper implements RowMapper<M_SP_Archival_Detail_Entity> {
		@Override
		public M_SP_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SP_Archival_Detail_Entity obj = new M_SP_Archival_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportLable(rs.getString("REPORT_LABLE"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			obj.setReportAddlCriteria3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			obj.setSanctionLimit(rs.getBigDecimal("SANCTION_LIMIT"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInPula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
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
			obj.setSegment(rs.getString("SEGMENT"));
			obj.setProvision(rs.getBigDecimal("PROVISION"));
			return obj;
		}
	}

	// ------------------------------
	// RowMapper for mapping ResultSet rows to M_SP_Archival_Summary_Entity
	// ------------------------------
	class M_SP_Archival_Summary_RowMapper implements RowMapper<M_SP_Archival_Summary_Entity> {
		@Override
		public M_SP_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SP_Archival_Summary_Entity obj = new M_SP_Archival_Summary_Entity();
			obj.setR9_product(rs.getString("R9_PRODUCT"));
			obj.setR9_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R9_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR9_stage_3_provisions_sp(rs.getBigDecimal("R9_STAGE_3_PROVISIONS_SP"));
			obj.setR9_total_specific_provisions(rs.getBigDecimal("R9_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R10_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR10_stage_3_provisions_sp(rs.getBigDecimal("R10_STAGE_3_PROVISIONS_SP"));
			obj.setR10_total_specific_provisions(rs.getBigDecimal("R10_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R11_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR11_stage_3_provisions_sp(rs.getBigDecimal("R11_STAGE_3_PROVISIONS_SP"));
			obj.setR11_total_specific_provisions(rs.getBigDecimal("R11_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R12_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR12_stage_3_provisions_sp(rs.getBigDecimal("R12_STAGE_3_PROVISIONS_SP"));
			obj.setR12_total_specific_provisions(rs.getBigDecimal("R12_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R13_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR13_stage_3_provisions_sp(rs.getBigDecimal("R13_STAGE_3_PROVISIONS_SP"));
			obj.setR13_total_specific_provisions(rs.getBigDecimal("R13_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R14_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR14_stage_3_provisions_sp(rs.getBigDecimal("R14_STAGE_3_PROVISIONS_SP"));
			obj.setR14_total_specific_provisions(rs.getBigDecimal("R14_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R15_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR15_stage_3_provisions_sp(rs.getBigDecimal("R15_STAGE_3_PROVISIONS_SP"));
			obj.setR15_total_specific_provisions(rs.getBigDecimal("R15_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R16_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR16_stage_3_provisions_sp(rs.getBigDecimal("R16_STAGE_3_PROVISIONS_SP"));
			obj.setR16_total_specific_provisions(rs.getBigDecimal("R16_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R17_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR17_stage_3_provisions_sp(rs.getBigDecimal("R17_STAGE_3_PROVISIONS_SP"));
			obj.setR17_total_specific_provisions(rs.getBigDecimal("R17_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R18_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR18_stage_3_provisions_sp(rs.getBigDecimal("R18_STAGE_3_PROVISIONS_SP"));
			obj.setR18_total_specific_provisions(rs.getBigDecimal("R18_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R19_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR19_stage_3_provisions_sp(rs.getBigDecimal("R19_STAGE_3_PROVISIONS_SP"));
			obj.setR19_total_specific_provisions(rs.getBigDecimal("R19_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R20_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR20_stage_3_provisions_sp(rs.getBigDecimal("R20_STAGE_3_PROVISIONS_SP"));
			obj.setR20_total_specific_provisions(rs.getBigDecimal("R20_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R21_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR21_stage_3_provisions_sp(rs.getBigDecimal("R21_STAGE_3_PROVISIONS_SP"));
			obj.setR21_total_specific_provisions(rs.getBigDecimal("R21_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R22_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR22_stage_3_provisions_sp(rs.getBigDecimal("R22_STAGE_3_PROVISIONS_SP"));
			obj.setR22_total_specific_provisions(rs.getBigDecimal("R22_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R23_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR23_stage_3_provisions_sp(rs.getBigDecimal("R23_STAGE_3_PROVISIONS_SP"));
			obj.setR23_total_specific_provisions(rs.getBigDecimal("R23_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R24_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR24_stage_3_provisions_sp(rs.getBigDecimal("R24_STAGE_3_PROVISIONS_SP"));
			obj.setR24_total_specific_provisions(rs.getBigDecimal("R24_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R25_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR25_stage_3_provisions_sp(rs.getBigDecimal("R25_STAGE_3_PROVISIONS_SP"));
			obj.setR25_total_specific_provisions(rs.getBigDecimal("R25_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R26_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR26_stage_3_provisions_sp(rs.getBigDecimal("R26_STAGE_3_PROVISIONS_SP"));
			obj.setR26_total_specific_provisions(rs.getBigDecimal("R26_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R27_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR27_stage_3_provisions_sp(rs.getBigDecimal("R27_STAGE_3_PROVISIONS_SP"));
			obj.setR27_total_specific_provisions(rs.getBigDecimal("R27_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R28_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR28_stage_3_provisions_sp(rs.getBigDecimal("R28_STAGE_3_PROVISIONS_SP"));
			obj.setR28_total_specific_provisions(rs.getBigDecimal("R28_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R29_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR29_stage_3_provisions_sp(rs.getBigDecimal("R29_STAGE_3_PROVISIONS_SP"));
			obj.setR29_total_specific_provisions(rs.getBigDecimal("R29_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR30_product(rs.getString("R30_PRODUCT"));
			obj.setR30_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R30_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR30_stage_3_provisions_sp(rs.getBigDecimal("R30_STAGE_3_PROVISIONS_SP"));
			obj.setR30_total_specific_provisions(rs.getBigDecimal("R30_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R31_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR31_stage_3_provisions_sp(rs.getBigDecimal("R31_STAGE_3_PROVISIONS_SP"));
			obj.setR31_total_specific_provisions(rs.getBigDecimal("R31_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R32_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR32_stage_3_provisions_sp(rs.getBigDecimal("R32_STAGE_3_PROVISIONS_SP"));
			obj.setR32_total_specific_provisions(rs.getBigDecimal("R32_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R33_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR33_stage_3_provisions_sp(rs.getBigDecimal("R33_STAGE_3_PROVISIONS_SP"));
			obj.setR33_total_specific_provisions(rs.getBigDecimal("R33_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R34_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR34_stage_3_provisions_sp(rs.getBigDecimal("R34_STAGE_3_PROVISIONS_SP"));
			obj.setR34_total_specific_provisions(rs.getBigDecimal("R34_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R35_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR35_stage_3_provisions_sp(rs.getBigDecimal("R35_STAGE_3_PROVISIONS_SP"));
			obj.setR35_total_specific_provisions(rs.getBigDecimal("R35_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R36_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR36_stage_3_provisions_sp(rs.getBigDecimal("R36_STAGE_3_PROVISIONS_SP"));
			obj.setR36_total_specific_provisions(rs.getBigDecimal("R36_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R37_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR37_stage_3_provisions_sp(rs.getBigDecimal("R37_STAGE_3_PROVISIONS_SP"));
			obj.setR37_total_specific_provisions(rs.getBigDecimal("R37_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R38_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR38_stage_3_provisions_sp(rs.getBigDecimal("R38_STAGE_3_PROVISIONS_SP"));
			obj.setR38_total_specific_provisions(rs.getBigDecimal("R38_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR39_product(rs.getString("R39_PRODUCT"));
			obj.setR39_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R39_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR39_stage_3_provisions_sp(rs.getBigDecimal("R39_STAGE_3_PROVISIONS_SP"));
			obj.setR39_total_specific_provisions(rs.getBigDecimal("R39_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR40_product(rs.getString("R40_PRODUCT"));
			obj.setR40_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R40_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR40_stage_3_provisions_sp(rs.getBigDecimal("R40_STAGE_3_PROVISIONS_SP"));
			obj.setR40_total_specific_provisions(rs.getBigDecimal("R40_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R41_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR41_stage_3_provisions_sp(rs.getBigDecimal("R41_STAGE_3_PROVISIONS_SP"));
			obj.setR41_total_specific_provisions(rs.getBigDecimal("R41_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R42_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR42_stage_3_provisions_sp(rs.getBigDecimal("R42_STAGE_3_PROVISIONS_SP"));
			obj.setR42_total_specific_provisions(rs.getBigDecimal("R42_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R43_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR43_stage_3_provisions_sp(rs.getBigDecimal("R43_STAGE_3_PROVISIONS_SP"));
			obj.setR43_total_specific_provisions(rs.getBigDecimal("R43_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R44_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR44_stage_3_provisions_sp(rs.getBigDecimal("R44_STAGE_3_PROVISIONS_SP"));
			obj.setR44_total_specific_provisions(rs.getBigDecimal("R44_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R45_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR45_stage_3_provisions_sp(rs.getBigDecimal("R45_STAGE_3_PROVISIONS_SP"));
			obj.setR45_total_specific_provisions(rs.getBigDecimal("R45_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R46_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR46_stage_3_provisions_sp(rs.getBigDecimal("R46_STAGE_3_PROVISIONS_SP"));
			obj.setR46_total_specific_provisions(rs.getBigDecimal("R46_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R47_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR47_stage_3_provisions_sp(rs.getBigDecimal("R47_STAGE_3_PROVISIONS_SP"));
			obj.setR47_total_specific_provisions(rs.getBigDecimal("R47_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R48_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR48_stage_3_provisions_sp(rs.getBigDecimal("R48_STAGE_3_PROVISIONS_SP"));
			obj.setR48_total_specific_provisions(rs.getBigDecimal("R48_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R49_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR49_stage_3_provisions_sp(rs.getBigDecimal("R49_STAGE_3_PROVISIONS_SP"));
			obj.setR49_total_specific_provisions(rs.getBigDecimal("R49_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR50_product(rs.getString("R50_PRODUCT"));
			obj.setR50_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R50_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR50_stage_3_provisions_sp(rs.getBigDecimal("R50_STAGE_3_PROVISIONS_SP"));
			obj.setR50_total_specific_provisions(rs.getBigDecimal("R50_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR51_product(rs.getString("R51_PRODUCT"));
			obj.setR51_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R51_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR51_stage_3_provisions_sp(rs.getBigDecimal("R51_STAGE_3_PROVISIONS_SP"));
			obj.setR51_total_specific_provisions(rs.getBigDecimal("R51_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR52_product(rs.getString("R52_PRODUCT"));
			obj.setR52_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R52_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR52_stage_3_provisions_sp(rs.getBigDecimal("R52_STAGE_3_PROVISIONS_SP"));
			obj.setR52_total_specific_provisions(rs.getBigDecimal("R52_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR53_product(rs.getString("R53_PRODUCT"));
			obj.setR53_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R53_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR53_stage_3_provisions_sp(rs.getBigDecimal("R53_STAGE_3_PROVISIONS_SP"));
			obj.setR53_total_specific_provisions(rs.getBigDecimal("R53_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR54_product(rs.getString("R54_PRODUCT"));
			obj.setR54_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R54_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR54_stage_3_provisions_sp(rs.getBigDecimal("R54_STAGE_3_PROVISIONS_SP"));
			obj.setR54_total_specific_provisions(rs.getBigDecimal("R54_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR55_product(rs.getString("R55_PRODUCT"));
			obj.setR55_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R55_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR55_stage_3_provisions_sp(rs.getBigDecimal("R55_STAGE_3_PROVISIONS_SP"));
			obj.setR55_total_specific_provisions(rs.getBigDecimal("R55_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR56_product(rs.getString("R56_PRODUCT"));
			obj.setR56_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R56_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR56_stage_3_provisions_sp(rs.getBigDecimal("R56_STAGE_3_PROVISIONS_SP"));
			obj.setR56_total_specific_provisions(rs.getBigDecimal("R56_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR57_product(rs.getString("R57_PRODUCT"));
			obj.setR57_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R57_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR57_stage_3_provisions_sp(rs.getBigDecimal("R57_STAGE_3_PROVISIONS_SP"));
			obj.setR57_total_specific_provisions(rs.getBigDecimal("R57_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR58_product(rs.getString("R58_PRODUCT"));
			obj.setR58_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R58_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR58_stage_3_provisions_sp(rs.getBigDecimal("R58_STAGE_3_PROVISIONS_SP"));
			obj.setR58_total_specific_provisions(rs.getBigDecimal("R58_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR59_product(rs.getString("R59_PRODUCT"));
			obj.setR59_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R59_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR59_stage_3_provisions_sp(rs.getBigDecimal("R59_STAGE_3_PROVISIONS_SP"));
			obj.setR59_total_specific_provisions(rs.getBigDecimal("R59_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR60_product(rs.getString("R60_PRODUCT"));
			obj.setR60_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R60_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR60_stage_3_provisions_sp(rs.getBigDecimal("R60_STAGE_3_PROVISIONS_SP"));
			obj.setR60_total_specific_provisions(rs.getBigDecimal("R60_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR61_product(rs.getString("R61_PRODUCT"));
			obj.setR61_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R61_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR61_stage_3_provisions_sp(rs.getBigDecimal("R61_STAGE_3_PROVISIONS_SP"));
			obj.setR61_total_specific_provisions(rs.getBigDecimal("R61_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setR62_product(rs.getString("R62_PRODUCT"));
			obj.setR62_qualifi_stage_2_provisions_sp(rs.getBigDecimal("R62_QUALIFI_STAGE_2_PROVISIONS_SP"));
			obj.setR62_stage_3_provisions_sp(rs.getBigDecimal("R62_STAGE_3_PROVISIONS_SP"));
			obj.setR62_total_specific_provisions(rs.getBigDecimal("R62_TOTAL_SPECIFIC_PROVISIONS"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// ------------------------------
	// Entity representation for M_SP Detail Table
	// ------------------------------
	public static class M_SP_Detail_Entity implements Serializable {
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportLable;
		private String reportAddlCriteria1;
		private String reportAddlCriteria2;
		private String reportAddlCriteria3;
		private BigDecimal sanctionLimit;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInPula;
		private Date reportDate;
		private String reportName;
		private String createUser;
		private Date createTime;
		private String modifyUser;
		private Date modifyTime;
		private String verifyUser;
		private Date verifyTime;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private String segment;
		private BigDecimal provision;

		public M_SP_Detail_Entity() {
			super();
		}

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

		public String getReportLable() {
			return reportLable;
		}

		public void setReportLable(String reportLable) {
			this.reportLable = reportLable;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
		}

		public String getReportAddlCriteria2() {
			return reportAddlCriteria2;
		}

		public void setReportAddlCriteria2(String reportAddlCriteria2) {
			this.reportAddlCriteria2 = reportAddlCriteria2;
		}

		public String getReportAddlCriteria3() {
			return reportAddlCriteria3;
		}

		public void setReportAddlCriteria3(String reportAddlCriteria3) {
			this.reportAddlCriteria3 = reportAddlCriteria3;
		}

		public BigDecimal getSanctionLimit() {
			return sanctionLimit;
		}

		public void setSanctionLimit(BigDecimal sanctionLimit) {
			this.sanctionLimit = sanctionLimit;
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

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public BigDecimal getProvision() {
			return provision;
		}

		public void setProvision(BigDecimal provision) {
			this.provision = provision;
		}
	}

	// ------------------------------
	// Entity representation for M_SP Summary Table
	// ------------------------------
	public static class M_SP_Summary_Entity implements Serializable {
		private String r9_product;
		private BigDecimal r9_qualifi_stage_2_provisions_sp;
		private BigDecimal r9_stage_3_provisions_sp;
		private BigDecimal r9_total_specific_provisions;
		private String r10_product;
		private BigDecimal r10_qualifi_stage_2_provisions_sp;
		private BigDecimal r10_stage_3_provisions_sp;
		private BigDecimal r10_total_specific_provisions;
		private String r11_product;
		private BigDecimal r11_qualifi_stage_2_provisions_sp;
		private BigDecimal r11_stage_3_provisions_sp;
		private BigDecimal r11_total_specific_provisions;
		private String r12_product;
		private BigDecimal r12_qualifi_stage_2_provisions_sp;
		private BigDecimal r12_stage_3_provisions_sp;
		private BigDecimal r12_total_specific_provisions;
		private String r13_product;
		private BigDecimal r13_qualifi_stage_2_provisions_sp;
		private BigDecimal r13_stage_3_provisions_sp;
		private BigDecimal r13_total_specific_provisions;
		private String r14_product;
		private BigDecimal r14_qualifi_stage_2_provisions_sp;
		private BigDecimal r14_stage_3_provisions_sp;
		private BigDecimal r14_total_specific_provisions;
		private String r15_product;
		private BigDecimal r15_qualifi_stage_2_provisions_sp;
		private BigDecimal r15_stage_3_provisions_sp;
		private BigDecimal r15_total_specific_provisions;
		private String r16_product;
		private BigDecimal r16_qualifi_stage_2_provisions_sp;
		private BigDecimal r16_stage_3_provisions_sp;
		private BigDecimal r16_total_specific_provisions;
		private String r17_product;
		private BigDecimal r17_qualifi_stage_2_provisions_sp;
		private BigDecimal r17_stage_3_provisions_sp;
		private BigDecimal r17_total_specific_provisions;
		private String r18_product;
		private BigDecimal r18_qualifi_stage_2_provisions_sp;
		private BigDecimal r18_stage_3_provisions_sp;
		private BigDecimal r18_total_specific_provisions;
		private String r19_product;
		private BigDecimal r19_qualifi_stage_2_provisions_sp;
		private BigDecimal r19_stage_3_provisions_sp;
		private BigDecimal r19_total_specific_provisions;
		private String r20_product;
		private BigDecimal r20_qualifi_stage_2_provisions_sp;
		private BigDecimal r20_stage_3_provisions_sp;
		private BigDecimal r20_total_specific_provisions;
		private String r21_product;
		private BigDecimal r21_qualifi_stage_2_provisions_sp;
		private BigDecimal r21_stage_3_provisions_sp;
		private BigDecimal r21_total_specific_provisions;
		private String r22_product;
		private BigDecimal r22_qualifi_stage_2_provisions_sp;
		private BigDecimal r22_stage_3_provisions_sp;
		private BigDecimal r22_total_specific_provisions;
		private String r23_product;
		private BigDecimal r23_qualifi_stage_2_provisions_sp;
		private BigDecimal r23_stage_3_provisions_sp;
		private BigDecimal r23_total_specific_provisions;
		private String r24_product;
		private BigDecimal r24_qualifi_stage_2_provisions_sp;
		private BigDecimal r24_stage_3_provisions_sp;
		private BigDecimal r24_total_specific_provisions;
		private String r25_product;
		private BigDecimal r25_qualifi_stage_2_provisions_sp;
		private BigDecimal r25_stage_3_provisions_sp;
		private BigDecimal r25_total_specific_provisions;
		private String r26_product;
		private BigDecimal r26_qualifi_stage_2_provisions_sp;
		private BigDecimal r26_stage_3_provisions_sp;
		private BigDecimal r26_total_specific_provisions;
		private String r27_product;
		private BigDecimal r27_qualifi_stage_2_provisions_sp;
		private BigDecimal r27_stage_3_provisions_sp;
		private BigDecimal r27_total_specific_provisions;
		private String r28_product;
		private BigDecimal r28_qualifi_stage_2_provisions_sp;
		private BigDecimal r28_stage_3_provisions_sp;
		private BigDecimal r28_total_specific_provisions;
		private String r29_product;
		private BigDecimal r29_qualifi_stage_2_provisions_sp;
		private BigDecimal r29_stage_3_provisions_sp;
		private BigDecimal r29_total_specific_provisions;
		private String r30_product;
		private BigDecimal r30_qualifi_stage_2_provisions_sp;
		private BigDecimal r30_stage_3_provisions_sp;
		private BigDecimal r30_total_specific_provisions;
		private String r31_product;
		private BigDecimal r31_qualifi_stage_2_provisions_sp;
		private BigDecimal r31_stage_3_provisions_sp;
		private BigDecimal r31_total_specific_provisions;
		private String r32_product;
		private BigDecimal r32_qualifi_stage_2_provisions_sp;
		private BigDecimal r32_stage_3_provisions_sp;
		private BigDecimal r32_total_specific_provisions;
		private String r33_product;
		private BigDecimal r33_qualifi_stage_2_provisions_sp;
		private BigDecimal r33_stage_3_provisions_sp;
		private BigDecimal r33_total_specific_provisions;
		private String r34_product;
		private BigDecimal r34_qualifi_stage_2_provisions_sp;
		private BigDecimal r34_stage_3_provisions_sp;
		private BigDecimal r34_total_specific_provisions;
		private String r35_product;
		private BigDecimal r35_qualifi_stage_2_provisions_sp;
		private BigDecimal r35_stage_3_provisions_sp;
		private BigDecimal r35_total_specific_provisions;
		private String r36_product;
		private BigDecimal r36_qualifi_stage_2_provisions_sp;
		private BigDecimal r36_stage_3_provisions_sp;
		private BigDecimal r36_total_specific_provisions;
		private String r37_product;
		private BigDecimal r37_qualifi_stage_2_provisions_sp;
		private BigDecimal r37_stage_3_provisions_sp;
		private BigDecimal r37_total_specific_provisions;
		private String r38_product;
		private BigDecimal r38_qualifi_stage_2_provisions_sp;
		private BigDecimal r38_stage_3_provisions_sp;
		private BigDecimal r38_total_specific_provisions;
		private String r39_product;
		private BigDecimal r39_qualifi_stage_2_provisions_sp;
		private BigDecimal r39_stage_3_provisions_sp;
		private BigDecimal r39_total_specific_provisions;
		private String r40_product;
		private BigDecimal r40_qualifi_stage_2_provisions_sp;
		private BigDecimal r40_stage_3_provisions_sp;
		private BigDecimal r40_total_specific_provisions;
		private String r41_product;
		private BigDecimal r41_qualifi_stage_2_provisions_sp;
		private BigDecimal r41_stage_3_provisions_sp;
		private BigDecimal r41_total_specific_provisions;
		private String r42_product;
		private BigDecimal r42_qualifi_stage_2_provisions_sp;
		private BigDecimal r42_stage_3_provisions_sp;
		private BigDecimal r42_total_specific_provisions;
		private String r43_product;
		private BigDecimal r43_qualifi_stage_2_provisions_sp;
		private BigDecimal r43_stage_3_provisions_sp;
		private BigDecimal r43_total_specific_provisions;
		private String r44_product;
		private BigDecimal r44_qualifi_stage_2_provisions_sp;
		private BigDecimal r44_stage_3_provisions_sp;
		private BigDecimal r44_total_specific_provisions;
		private String r45_product;
		private BigDecimal r45_qualifi_stage_2_provisions_sp;
		private BigDecimal r45_stage_3_provisions_sp;
		private BigDecimal r45_total_specific_provisions;
		private String r46_product;
		private BigDecimal r46_qualifi_stage_2_provisions_sp;
		private BigDecimal r46_stage_3_provisions_sp;
		private BigDecimal r46_total_specific_provisions;
		private String r47_product;
		private BigDecimal r47_qualifi_stage_2_provisions_sp;
		private BigDecimal r47_stage_3_provisions_sp;
		private BigDecimal r47_total_specific_provisions;
		private String r48_product;
		private BigDecimal r48_qualifi_stage_2_provisions_sp;
		private BigDecimal r48_stage_3_provisions_sp;
		private BigDecimal r48_total_specific_provisions;
		private String r49_product;
		private BigDecimal r49_qualifi_stage_2_provisions_sp;
		private BigDecimal r49_stage_3_provisions_sp;
		private BigDecimal r49_total_specific_provisions;
		private String r50_product;
		private BigDecimal r50_qualifi_stage_2_provisions_sp;
		private BigDecimal r50_stage_3_provisions_sp;
		private BigDecimal r50_total_specific_provisions;
		private String r51_product;
		private BigDecimal r51_qualifi_stage_2_provisions_sp;
		private BigDecimal r51_stage_3_provisions_sp;
		private BigDecimal r51_total_specific_provisions;
		private String r52_product;
		private BigDecimal r52_qualifi_stage_2_provisions_sp;
		private BigDecimal r52_stage_3_provisions_sp;
		private BigDecimal r52_total_specific_provisions;
		private String r53_product;
		private BigDecimal r53_qualifi_stage_2_provisions_sp;
		private BigDecimal r53_stage_3_provisions_sp;
		private BigDecimal r53_total_specific_provisions;
		private String r54_product;
		private BigDecimal r54_qualifi_stage_2_provisions_sp;
		private BigDecimal r54_stage_3_provisions_sp;
		private BigDecimal r54_total_specific_provisions;
		private String r55_product;
		private BigDecimal r55_qualifi_stage_2_provisions_sp;
		private BigDecimal r55_stage_3_provisions_sp;
		private BigDecimal r55_total_specific_provisions;
		private String r56_product;
		private BigDecimal r56_qualifi_stage_2_provisions_sp;
		private BigDecimal r56_stage_3_provisions_sp;
		private BigDecimal r56_total_specific_provisions;
		private String r57_product;
		private BigDecimal r57_qualifi_stage_2_provisions_sp;
		private BigDecimal r57_stage_3_provisions_sp;
		private BigDecimal r57_total_specific_provisions;
		private String r58_product;
		private BigDecimal r58_qualifi_stage_2_provisions_sp;
		private BigDecimal r58_stage_3_provisions_sp;
		private BigDecimal r58_total_specific_provisions;
		private String r59_product;
		private BigDecimal r59_qualifi_stage_2_provisions_sp;
		private BigDecimal r59_stage_3_provisions_sp;
		private BigDecimal r59_total_specific_provisions;
		private String r60_product;
		private BigDecimal r60_qualifi_stage_2_provisions_sp;
		private BigDecimal r60_stage_3_provisions_sp;
		private BigDecimal r60_total_specific_provisions;
		private String r61_product;
		private BigDecimal r61_qualifi_stage_2_provisions_sp;
		private BigDecimal r61_stage_3_provisions_sp;
		private BigDecimal r61_total_specific_provisions;
		private String r62_product;
		private BigDecimal r62_qualifi_stage_2_provisions_sp;
		private BigDecimal r62_stage_3_provisions_sp;
		private BigDecimal r62_total_specific_provisions;
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SP_Summary_Entity() {
			super();
		}

		public String getR9_product() {
			return r9_product;
		}

		public void setR9_product(String r9_product) {
			this.r9_product = r9_product;
		}

		public BigDecimal getR9_qualifi_stage_2_provisions_sp() {
			return r9_qualifi_stage_2_provisions_sp;
		}

		public void setR9_qualifi_stage_2_provisions_sp(BigDecimal r9_qualifi_stage_2_provisions_sp) {
			this.r9_qualifi_stage_2_provisions_sp = r9_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR9_stage_3_provisions_sp() {
			return r9_stage_3_provisions_sp;
		}

		public void setR9_stage_3_provisions_sp(BigDecimal r9_stage_3_provisions_sp) {
			this.r9_stage_3_provisions_sp = r9_stage_3_provisions_sp;
		}

		public BigDecimal getR9_total_specific_provisions() {
			return r9_total_specific_provisions;
		}

		public void setR9_total_specific_provisions(BigDecimal r9_total_specific_provisions) {
			this.r9_total_specific_provisions = r9_total_specific_provisions;
		}

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public BigDecimal getR10_qualifi_stage_2_provisions_sp() {
			return r10_qualifi_stage_2_provisions_sp;
		}

		public void setR10_qualifi_stage_2_provisions_sp(BigDecimal r10_qualifi_stage_2_provisions_sp) {
			this.r10_qualifi_stage_2_provisions_sp = r10_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR10_stage_3_provisions_sp() {
			return r10_stage_3_provisions_sp;
		}

		public void setR10_stage_3_provisions_sp(BigDecimal r10_stage_3_provisions_sp) {
			this.r10_stage_3_provisions_sp = r10_stage_3_provisions_sp;
		}

		public BigDecimal getR10_total_specific_provisions() {
			return r10_total_specific_provisions;
		}

		public void setR10_total_specific_provisions(BigDecimal r10_total_specific_provisions) {
			this.r10_total_specific_provisions = r10_total_specific_provisions;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_qualifi_stage_2_provisions_sp() {
			return r11_qualifi_stage_2_provisions_sp;
		}

		public void setR11_qualifi_stage_2_provisions_sp(BigDecimal r11_qualifi_stage_2_provisions_sp) {
			this.r11_qualifi_stage_2_provisions_sp = r11_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR11_stage_3_provisions_sp() {
			return r11_stage_3_provisions_sp;
		}

		public void setR11_stage_3_provisions_sp(BigDecimal r11_stage_3_provisions_sp) {
			this.r11_stage_3_provisions_sp = r11_stage_3_provisions_sp;
		}

		public BigDecimal getR11_total_specific_provisions() {
			return r11_total_specific_provisions;
		}

		public void setR11_total_specific_provisions(BigDecimal r11_total_specific_provisions) {
			this.r11_total_specific_provisions = r11_total_specific_provisions;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_qualifi_stage_2_provisions_sp() {
			return r12_qualifi_stage_2_provisions_sp;
		}

		public void setR12_qualifi_stage_2_provisions_sp(BigDecimal r12_qualifi_stage_2_provisions_sp) {
			this.r12_qualifi_stage_2_provisions_sp = r12_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR12_stage_3_provisions_sp() {
			return r12_stage_3_provisions_sp;
		}

		public void setR12_stage_3_provisions_sp(BigDecimal r12_stage_3_provisions_sp) {
			this.r12_stage_3_provisions_sp = r12_stage_3_provisions_sp;
		}

		public BigDecimal getR12_total_specific_provisions() {
			return r12_total_specific_provisions;
		}

		public void setR12_total_specific_provisions(BigDecimal r12_total_specific_provisions) {
			this.r12_total_specific_provisions = r12_total_specific_provisions;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_qualifi_stage_2_provisions_sp() {
			return r13_qualifi_stage_2_provisions_sp;
		}

		public void setR13_qualifi_stage_2_provisions_sp(BigDecimal r13_qualifi_stage_2_provisions_sp) {
			this.r13_qualifi_stage_2_provisions_sp = r13_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR13_stage_3_provisions_sp() {
			return r13_stage_3_provisions_sp;
		}

		public void setR13_stage_3_provisions_sp(BigDecimal r13_stage_3_provisions_sp) {
			this.r13_stage_3_provisions_sp = r13_stage_3_provisions_sp;
		}

		public BigDecimal getR13_total_specific_provisions() {
			return r13_total_specific_provisions;
		}

		public void setR13_total_specific_provisions(BigDecimal r13_total_specific_provisions) {
			this.r13_total_specific_provisions = r13_total_specific_provisions;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_qualifi_stage_2_provisions_sp() {
			return r14_qualifi_stage_2_provisions_sp;
		}

		public void setR14_qualifi_stage_2_provisions_sp(BigDecimal r14_qualifi_stage_2_provisions_sp) {
			this.r14_qualifi_stage_2_provisions_sp = r14_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR14_stage_3_provisions_sp() {
			return r14_stage_3_provisions_sp;
		}

		public void setR14_stage_3_provisions_sp(BigDecimal r14_stage_3_provisions_sp) {
			this.r14_stage_3_provisions_sp = r14_stage_3_provisions_sp;
		}

		public BigDecimal getR14_total_specific_provisions() {
			return r14_total_specific_provisions;
		}

		public void setR14_total_specific_provisions(BigDecimal r14_total_specific_provisions) {
			this.r14_total_specific_provisions = r14_total_specific_provisions;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_qualifi_stage_2_provisions_sp() {
			return r15_qualifi_stage_2_provisions_sp;
		}

		public void setR15_qualifi_stage_2_provisions_sp(BigDecimal r15_qualifi_stage_2_provisions_sp) {
			this.r15_qualifi_stage_2_provisions_sp = r15_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR15_stage_3_provisions_sp() {
			return r15_stage_3_provisions_sp;
		}

		public void setR15_stage_3_provisions_sp(BigDecimal r15_stage_3_provisions_sp) {
			this.r15_stage_3_provisions_sp = r15_stage_3_provisions_sp;
		}

		public BigDecimal getR15_total_specific_provisions() {
			return r15_total_specific_provisions;
		}

		public void setR15_total_specific_provisions(BigDecimal r15_total_specific_provisions) {
			this.r15_total_specific_provisions = r15_total_specific_provisions;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_qualifi_stage_2_provisions_sp() {
			return r16_qualifi_stage_2_provisions_sp;
		}

		public void setR16_qualifi_stage_2_provisions_sp(BigDecimal r16_qualifi_stage_2_provisions_sp) {
			this.r16_qualifi_stage_2_provisions_sp = r16_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR16_stage_3_provisions_sp() {
			return r16_stage_3_provisions_sp;
		}

		public void setR16_stage_3_provisions_sp(BigDecimal r16_stage_3_provisions_sp) {
			this.r16_stage_3_provisions_sp = r16_stage_3_provisions_sp;
		}

		public BigDecimal getR16_total_specific_provisions() {
			return r16_total_specific_provisions;
		}

		public void setR16_total_specific_provisions(BigDecimal r16_total_specific_provisions) {
			this.r16_total_specific_provisions = r16_total_specific_provisions;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_qualifi_stage_2_provisions_sp() {
			return r17_qualifi_stage_2_provisions_sp;
		}

		public void setR17_qualifi_stage_2_provisions_sp(BigDecimal r17_qualifi_stage_2_provisions_sp) {
			this.r17_qualifi_stage_2_provisions_sp = r17_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR17_stage_3_provisions_sp() {
			return r17_stage_3_provisions_sp;
		}

		public void setR17_stage_3_provisions_sp(BigDecimal r17_stage_3_provisions_sp) {
			this.r17_stage_3_provisions_sp = r17_stage_3_provisions_sp;
		}

		public BigDecimal getR17_total_specific_provisions() {
			return r17_total_specific_provisions;
		}

		public void setR17_total_specific_provisions(BigDecimal r17_total_specific_provisions) {
			this.r17_total_specific_provisions = r17_total_specific_provisions;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_qualifi_stage_2_provisions_sp() {
			return r18_qualifi_stage_2_provisions_sp;
		}

		public void setR18_qualifi_stage_2_provisions_sp(BigDecimal r18_qualifi_stage_2_provisions_sp) {
			this.r18_qualifi_stage_2_provisions_sp = r18_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR18_stage_3_provisions_sp() {
			return r18_stage_3_provisions_sp;
		}

		public void setR18_stage_3_provisions_sp(BigDecimal r18_stage_3_provisions_sp) {
			this.r18_stage_3_provisions_sp = r18_stage_3_provisions_sp;
		}

		public BigDecimal getR18_total_specific_provisions() {
			return r18_total_specific_provisions;
		}

		public void setR18_total_specific_provisions(BigDecimal r18_total_specific_provisions) {
			this.r18_total_specific_provisions = r18_total_specific_provisions;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_qualifi_stage_2_provisions_sp() {
			return r19_qualifi_stage_2_provisions_sp;
		}

		public void setR19_qualifi_stage_2_provisions_sp(BigDecimal r19_qualifi_stage_2_provisions_sp) {
			this.r19_qualifi_stage_2_provisions_sp = r19_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR19_stage_3_provisions_sp() {
			return r19_stage_3_provisions_sp;
		}

		public void setR19_stage_3_provisions_sp(BigDecimal r19_stage_3_provisions_sp) {
			this.r19_stage_3_provisions_sp = r19_stage_3_provisions_sp;
		}

		public BigDecimal getR19_total_specific_provisions() {
			return r19_total_specific_provisions;
		}

		public void setR19_total_specific_provisions(BigDecimal r19_total_specific_provisions) {
			this.r19_total_specific_provisions = r19_total_specific_provisions;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_qualifi_stage_2_provisions_sp() {
			return r20_qualifi_stage_2_provisions_sp;
		}

		public void setR20_qualifi_stage_2_provisions_sp(BigDecimal r20_qualifi_stage_2_provisions_sp) {
			this.r20_qualifi_stage_2_provisions_sp = r20_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR20_stage_3_provisions_sp() {
			return r20_stage_3_provisions_sp;
		}

		public void setR20_stage_3_provisions_sp(BigDecimal r20_stage_3_provisions_sp) {
			this.r20_stage_3_provisions_sp = r20_stage_3_provisions_sp;
		}

		public BigDecimal getR20_total_specific_provisions() {
			return r20_total_specific_provisions;
		}

		public void setR20_total_specific_provisions(BigDecimal r20_total_specific_provisions) {
			this.r20_total_specific_provisions = r20_total_specific_provisions;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_qualifi_stage_2_provisions_sp() {
			return r21_qualifi_stage_2_provisions_sp;
		}

		public void setR21_qualifi_stage_2_provisions_sp(BigDecimal r21_qualifi_stage_2_provisions_sp) {
			this.r21_qualifi_stage_2_provisions_sp = r21_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR21_stage_3_provisions_sp() {
			return r21_stage_3_provisions_sp;
		}

		public void setR21_stage_3_provisions_sp(BigDecimal r21_stage_3_provisions_sp) {
			this.r21_stage_3_provisions_sp = r21_stage_3_provisions_sp;
		}

		public BigDecimal getR21_total_specific_provisions() {
			return r21_total_specific_provisions;
		}

		public void setR21_total_specific_provisions(BigDecimal r21_total_specific_provisions) {
			this.r21_total_specific_provisions = r21_total_specific_provisions;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_qualifi_stage_2_provisions_sp() {
			return r22_qualifi_stage_2_provisions_sp;
		}

		public void setR22_qualifi_stage_2_provisions_sp(BigDecimal r22_qualifi_stage_2_provisions_sp) {
			this.r22_qualifi_stage_2_provisions_sp = r22_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR22_stage_3_provisions_sp() {
			return r22_stage_3_provisions_sp;
		}

		public void setR22_stage_3_provisions_sp(BigDecimal r22_stage_3_provisions_sp) {
			this.r22_stage_3_provisions_sp = r22_stage_3_provisions_sp;
		}

		public BigDecimal getR22_total_specific_provisions() {
			return r22_total_specific_provisions;
		}

		public void setR22_total_specific_provisions(BigDecimal r22_total_specific_provisions) {
			this.r22_total_specific_provisions = r22_total_specific_provisions;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_qualifi_stage_2_provisions_sp() {
			return r23_qualifi_stage_2_provisions_sp;
		}

		public void setR23_qualifi_stage_2_provisions_sp(BigDecimal r23_qualifi_stage_2_provisions_sp) {
			this.r23_qualifi_stage_2_provisions_sp = r23_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR23_stage_3_provisions_sp() {
			return r23_stage_3_provisions_sp;
		}

		public void setR23_stage_3_provisions_sp(BigDecimal r23_stage_3_provisions_sp) {
			this.r23_stage_3_provisions_sp = r23_stage_3_provisions_sp;
		}

		public BigDecimal getR23_total_specific_provisions() {
			return r23_total_specific_provisions;
		}

		public void setR23_total_specific_provisions(BigDecimal r23_total_specific_provisions) {
			this.r23_total_specific_provisions = r23_total_specific_provisions;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_qualifi_stage_2_provisions_sp() {
			return r24_qualifi_stage_2_provisions_sp;
		}

		public void setR24_qualifi_stage_2_provisions_sp(BigDecimal r24_qualifi_stage_2_provisions_sp) {
			this.r24_qualifi_stage_2_provisions_sp = r24_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR24_stage_3_provisions_sp() {
			return r24_stage_3_provisions_sp;
		}

		public void setR24_stage_3_provisions_sp(BigDecimal r24_stage_3_provisions_sp) {
			this.r24_stage_3_provisions_sp = r24_stage_3_provisions_sp;
		}

		public BigDecimal getR24_total_specific_provisions() {
			return r24_total_specific_provisions;
		}

		public void setR24_total_specific_provisions(BigDecimal r24_total_specific_provisions) {
			this.r24_total_specific_provisions = r24_total_specific_provisions;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_qualifi_stage_2_provisions_sp() {
			return r25_qualifi_stage_2_provisions_sp;
		}

		public void setR25_qualifi_stage_2_provisions_sp(BigDecimal r25_qualifi_stage_2_provisions_sp) {
			this.r25_qualifi_stage_2_provisions_sp = r25_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR25_stage_3_provisions_sp() {
			return r25_stage_3_provisions_sp;
		}

		public void setR25_stage_3_provisions_sp(BigDecimal r25_stage_3_provisions_sp) {
			this.r25_stage_3_provisions_sp = r25_stage_3_provisions_sp;
		}

		public BigDecimal getR25_total_specific_provisions() {
			return r25_total_specific_provisions;
		}

		public void setR25_total_specific_provisions(BigDecimal r25_total_specific_provisions) {
			this.r25_total_specific_provisions = r25_total_specific_provisions;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_qualifi_stage_2_provisions_sp() {
			return r26_qualifi_stage_2_provisions_sp;
		}

		public void setR26_qualifi_stage_2_provisions_sp(BigDecimal r26_qualifi_stage_2_provisions_sp) {
			this.r26_qualifi_stage_2_provisions_sp = r26_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR26_stage_3_provisions_sp() {
			return r26_stage_3_provisions_sp;
		}

		public void setR26_stage_3_provisions_sp(BigDecimal r26_stage_3_provisions_sp) {
			this.r26_stage_3_provisions_sp = r26_stage_3_provisions_sp;
		}

		public BigDecimal getR26_total_specific_provisions() {
			return r26_total_specific_provisions;
		}

		public void setR26_total_specific_provisions(BigDecimal r26_total_specific_provisions) {
			this.r26_total_specific_provisions = r26_total_specific_provisions;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_qualifi_stage_2_provisions_sp() {
			return r27_qualifi_stage_2_provisions_sp;
		}

		public void setR27_qualifi_stage_2_provisions_sp(BigDecimal r27_qualifi_stage_2_provisions_sp) {
			this.r27_qualifi_stage_2_provisions_sp = r27_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR27_stage_3_provisions_sp() {
			return r27_stage_3_provisions_sp;
		}

		public void setR27_stage_3_provisions_sp(BigDecimal r27_stage_3_provisions_sp) {
			this.r27_stage_3_provisions_sp = r27_stage_3_provisions_sp;
		}

		public BigDecimal getR27_total_specific_provisions() {
			return r27_total_specific_provisions;
		}

		public void setR27_total_specific_provisions(BigDecimal r27_total_specific_provisions) {
			this.r27_total_specific_provisions = r27_total_specific_provisions;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_qualifi_stage_2_provisions_sp() {
			return r28_qualifi_stage_2_provisions_sp;
		}

		public void setR28_qualifi_stage_2_provisions_sp(BigDecimal r28_qualifi_stage_2_provisions_sp) {
			this.r28_qualifi_stage_2_provisions_sp = r28_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR28_stage_3_provisions_sp() {
			return r28_stage_3_provisions_sp;
		}

		public void setR28_stage_3_provisions_sp(BigDecimal r28_stage_3_provisions_sp) {
			this.r28_stage_3_provisions_sp = r28_stage_3_provisions_sp;
		}

		public BigDecimal getR28_total_specific_provisions() {
			return r28_total_specific_provisions;
		}

		public void setR28_total_specific_provisions(BigDecimal r28_total_specific_provisions) {
			this.r28_total_specific_provisions = r28_total_specific_provisions;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_qualifi_stage_2_provisions_sp() {
			return r29_qualifi_stage_2_provisions_sp;
		}

		public void setR29_qualifi_stage_2_provisions_sp(BigDecimal r29_qualifi_stage_2_provisions_sp) {
			this.r29_qualifi_stage_2_provisions_sp = r29_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR29_stage_3_provisions_sp() {
			return r29_stage_3_provisions_sp;
		}

		public void setR29_stage_3_provisions_sp(BigDecimal r29_stage_3_provisions_sp) {
			this.r29_stage_3_provisions_sp = r29_stage_3_provisions_sp;
		}

		public BigDecimal getR29_total_specific_provisions() {
			return r29_total_specific_provisions;
		}

		public void setR29_total_specific_provisions(BigDecimal r29_total_specific_provisions) {
			this.r29_total_specific_provisions = r29_total_specific_provisions;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_qualifi_stage_2_provisions_sp() {
			return r30_qualifi_stage_2_provisions_sp;
		}

		public void setR30_qualifi_stage_2_provisions_sp(BigDecimal r30_qualifi_stage_2_provisions_sp) {
			this.r30_qualifi_stage_2_provisions_sp = r30_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR30_stage_3_provisions_sp() {
			return r30_stage_3_provisions_sp;
		}

		public void setR30_stage_3_provisions_sp(BigDecimal r30_stage_3_provisions_sp) {
			this.r30_stage_3_provisions_sp = r30_stage_3_provisions_sp;
		}

		public BigDecimal getR30_total_specific_provisions() {
			return r30_total_specific_provisions;
		}

		public void setR30_total_specific_provisions(BigDecimal r30_total_specific_provisions) {
			this.r30_total_specific_provisions = r30_total_specific_provisions;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_qualifi_stage_2_provisions_sp() {
			return r31_qualifi_stage_2_provisions_sp;
		}

		public void setR31_qualifi_stage_2_provisions_sp(BigDecimal r31_qualifi_stage_2_provisions_sp) {
			this.r31_qualifi_stage_2_provisions_sp = r31_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR31_stage_3_provisions_sp() {
			return r31_stage_3_provisions_sp;
		}

		public void setR31_stage_3_provisions_sp(BigDecimal r31_stage_3_provisions_sp) {
			this.r31_stage_3_provisions_sp = r31_stage_3_provisions_sp;
		}

		public BigDecimal getR31_total_specific_provisions() {
			return r31_total_specific_provisions;
		}

		public void setR31_total_specific_provisions(BigDecimal r31_total_specific_provisions) {
			this.r31_total_specific_provisions = r31_total_specific_provisions;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public BigDecimal getR32_qualifi_stage_2_provisions_sp() {
			return r32_qualifi_stage_2_provisions_sp;
		}

		public void setR32_qualifi_stage_2_provisions_sp(BigDecimal r32_qualifi_stage_2_provisions_sp) {
			this.r32_qualifi_stage_2_provisions_sp = r32_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR32_stage_3_provisions_sp() {
			return r32_stage_3_provisions_sp;
		}

		public void setR32_stage_3_provisions_sp(BigDecimal r32_stage_3_provisions_sp) {
			this.r32_stage_3_provisions_sp = r32_stage_3_provisions_sp;
		}

		public BigDecimal getR32_total_specific_provisions() {
			return r32_total_specific_provisions;
		}

		public void setR32_total_specific_provisions(BigDecimal r32_total_specific_provisions) {
			this.r32_total_specific_provisions = r32_total_specific_provisions;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_qualifi_stage_2_provisions_sp() {
			return r33_qualifi_stage_2_provisions_sp;
		}

		public void setR33_qualifi_stage_2_provisions_sp(BigDecimal r33_qualifi_stage_2_provisions_sp) {
			this.r33_qualifi_stage_2_provisions_sp = r33_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR33_stage_3_provisions_sp() {
			return r33_stage_3_provisions_sp;
		}

		public void setR33_stage_3_provisions_sp(BigDecimal r33_stage_3_provisions_sp) {
			this.r33_stage_3_provisions_sp = r33_stage_3_provisions_sp;
		}

		public BigDecimal getR33_total_specific_provisions() {
			return r33_total_specific_provisions;
		}

		public void setR33_total_specific_provisions(BigDecimal r33_total_specific_provisions) {
			this.r33_total_specific_provisions = r33_total_specific_provisions;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_qualifi_stage_2_provisions_sp() {
			return r34_qualifi_stage_2_provisions_sp;
		}

		public void setR34_qualifi_stage_2_provisions_sp(BigDecimal r34_qualifi_stage_2_provisions_sp) {
			this.r34_qualifi_stage_2_provisions_sp = r34_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR34_stage_3_provisions_sp() {
			return r34_stage_3_provisions_sp;
		}

		public void setR34_stage_3_provisions_sp(BigDecimal r34_stage_3_provisions_sp) {
			this.r34_stage_3_provisions_sp = r34_stage_3_provisions_sp;
		}

		public BigDecimal getR34_total_specific_provisions() {
			return r34_total_specific_provisions;
		}

		public void setR34_total_specific_provisions(BigDecimal r34_total_specific_provisions) {
			this.r34_total_specific_provisions = r34_total_specific_provisions;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public BigDecimal getR35_qualifi_stage_2_provisions_sp() {
			return r35_qualifi_stage_2_provisions_sp;
		}

		public void setR35_qualifi_stage_2_provisions_sp(BigDecimal r35_qualifi_stage_2_provisions_sp) {
			this.r35_qualifi_stage_2_provisions_sp = r35_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR35_stage_3_provisions_sp() {
			return r35_stage_3_provisions_sp;
		}

		public void setR35_stage_3_provisions_sp(BigDecimal r35_stage_3_provisions_sp) {
			this.r35_stage_3_provisions_sp = r35_stage_3_provisions_sp;
		}

		public BigDecimal getR35_total_specific_provisions() {
			return r35_total_specific_provisions;
		}

		public void setR35_total_specific_provisions(BigDecimal r35_total_specific_provisions) {
			this.r35_total_specific_provisions = r35_total_specific_provisions;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_qualifi_stage_2_provisions_sp() {
			return r36_qualifi_stage_2_provisions_sp;
		}

		public void setR36_qualifi_stage_2_provisions_sp(BigDecimal r36_qualifi_stage_2_provisions_sp) {
			this.r36_qualifi_stage_2_provisions_sp = r36_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR36_stage_3_provisions_sp() {
			return r36_stage_3_provisions_sp;
		}

		public void setR36_stage_3_provisions_sp(BigDecimal r36_stage_3_provisions_sp) {
			this.r36_stage_3_provisions_sp = r36_stage_3_provisions_sp;
		}

		public BigDecimal getR36_total_specific_provisions() {
			return r36_total_specific_provisions;
		}

		public void setR36_total_specific_provisions(BigDecimal r36_total_specific_provisions) {
			this.r36_total_specific_provisions = r36_total_specific_provisions;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public BigDecimal getR37_qualifi_stage_2_provisions_sp() {
			return r37_qualifi_stage_2_provisions_sp;
		}

		public void setR37_qualifi_stage_2_provisions_sp(BigDecimal r37_qualifi_stage_2_provisions_sp) {
			this.r37_qualifi_stage_2_provisions_sp = r37_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR37_stage_3_provisions_sp() {
			return r37_stage_3_provisions_sp;
		}

		public void setR37_stage_3_provisions_sp(BigDecimal r37_stage_3_provisions_sp) {
			this.r37_stage_3_provisions_sp = r37_stage_3_provisions_sp;
		}

		public BigDecimal getR37_total_specific_provisions() {
			return r37_total_specific_provisions;
		}

		public void setR37_total_specific_provisions(BigDecimal r37_total_specific_provisions) {
			this.r37_total_specific_provisions = r37_total_specific_provisions;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public BigDecimal getR38_qualifi_stage_2_provisions_sp() {
			return r38_qualifi_stage_2_provisions_sp;
		}

		public void setR38_qualifi_stage_2_provisions_sp(BigDecimal r38_qualifi_stage_2_provisions_sp) {
			this.r38_qualifi_stage_2_provisions_sp = r38_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR38_stage_3_provisions_sp() {
			return r38_stage_3_provisions_sp;
		}

		public void setR38_stage_3_provisions_sp(BigDecimal r38_stage_3_provisions_sp) {
			this.r38_stage_3_provisions_sp = r38_stage_3_provisions_sp;
		}

		public BigDecimal getR38_total_specific_provisions() {
			return r38_total_specific_provisions;
		}

		public void setR38_total_specific_provisions(BigDecimal r38_total_specific_provisions) {
			this.r38_total_specific_provisions = r38_total_specific_provisions;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public BigDecimal getR39_qualifi_stage_2_provisions_sp() {
			return r39_qualifi_stage_2_provisions_sp;
		}

		public void setR39_qualifi_stage_2_provisions_sp(BigDecimal r39_qualifi_stage_2_provisions_sp) {
			this.r39_qualifi_stage_2_provisions_sp = r39_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR39_stage_3_provisions_sp() {
			return r39_stage_3_provisions_sp;
		}

		public void setR39_stage_3_provisions_sp(BigDecimal r39_stage_3_provisions_sp) {
			this.r39_stage_3_provisions_sp = r39_stage_3_provisions_sp;
		}

		public BigDecimal getR39_total_specific_provisions() {
			return r39_total_specific_provisions;
		}

		public void setR39_total_specific_provisions(BigDecimal r39_total_specific_provisions) {
			this.r39_total_specific_provisions = r39_total_specific_provisions;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public BigDecimal getR40_qualifi_stage_2_provisions_sp() {
			return r40_qualifi_stage_2_provisions_sp;
		}

		public void setR40_qualifi_stage_2_provisions_sp(BigDecimal r40_qualifi_stage_2_provisions_sp) {
			this.r40_qualifi_stage_2_provisions_sp = r40_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR40_stage_3_provisions_sp() {
			return r40_stage_3_provisions_sp;
		}

		public void setR40_stage_3_provisions_sp(BigDecimal r40_stage_3_provisions_sp) {
			this.r40_stage_3_provisions_sp = r40_stage_3_provisions_sp;
		}

		public BigDecimal getR40_total_specific_provisions() {
			return r40_total_specific_provisions;
		}

		public void setR40_total_specific_provisions(BigDecimal r40_total_specific_provisions) {
			this.r40_total_specific_provisions = r40_total_specific_provisions;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_qualifi_stage_2_provisions_sp() {
			return r41_qualifi_stage_2_provisions_sp;
		}

		public void setR41_qualifi_stage_2_provisions_sp(BigDecimal r41_qualifi_stage_2_provisions_sp) {
			this.r41_qualifi_stage_2_provisions_sp = r41_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR41_stage_3_provisions_sp() {
			return r41_stage_3_provisions_sp;
		}

		public void setR41_stage_3_provisions_sp(BigDecimal r41_stage_3_provisions_sp) {
			this.r41_stage_3_provisions_sp = r41_stage_3_provisions_sp;
		}

		public BigDecimal getR41_total_specific_provisions() {
			return r41_total_specific_provisions;
		}

		public void setR41_total_specific_provisions(BigDecimal r41_total_specific_provisions) {
			this.r41_total_specific_provisions = r41_total_specific_provisions;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_qualifi_stage_2_provisions_sp() {
			return r42_qualifi_stage_2_provisions_sp;
		}

		public void setR42_qualifi_stage_2_provisions_sp(BigDecimal r42_qualifi_stage_2_provisions_sp) {
			this.r42_qualifi_stage_2_provisions_sp = r42_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR42_stage_3_provisions_sp() {
			return r42_stage_3_provisions_sp;
		}

		public void setR42_stage_3_provisions_sp(BigDecimal r42_stage_3_provisions_sp) {
			this.r42_stage_3_provisions_sp = r42_stage_3_provisions_sp;
		}

		public BigDecimal getR42_total_specific_provisions() {
			return r42_total_specific_provisions;
		}

		public void setR42_total_specific_provisions(BigDecimal r42_total_specific_provisions) {
			this.r42_total_specific_provisions = r42_total_specific_provisions;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_qualifi_stage_2_provisions_sp() {
			return r43_qualifi_stage_2_provisions_sp;
		}

		public void setR43_qualifi_stage_2_provisions_sp(BigDecimal r43_qualifi_stage_2_provisions_sp) {
			this.r43_qualifi_stage_2_provisions_sp = r43_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR43_stage_3_provisions_sp() {
			return r43_stage_3_provisions_sp;
		}

		public void setR43_stage_3_provisions_sp(BigDecimal r43_stage_3_provisions_sp) {
			this.r43_stage_3_provisions_sp = r43_stage_3_provisions_sp;
		}

		public BigDecimal getR43_total_specific_provisions() {
			return r43_total_specific_provisions;
		}

		public void setR43_total_specific_provisions(BigDecimal r43_total_specific_provisions) {
			this.r43_total_specific_provisions = r43_total_specific_provisions;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_qualifi_stage_2_provisions_sp() {
			return r44_qualifi_stage_2_provisions_sp;
		}

		public void setR44_qualifi_stage_2_provisions_sp(BigDecimal r44_qualifi_stage_2_provisions_sp) {
			this.r44_qualifi_stage_2_provisions_sp = r44_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR44_stage_3_provisions_sp() {
			return r44_stage_3_provisions_sp;
		}

		public void setR44_stage_3_provisions_sp(BigDecimal r44_stage_3_provisions_sp) {
			this.r44_stage_3_provisions_sp = r44_stage_3_provisions_sp;
		}

		public BigDecimal getR44_total_specific_provisions() {
			return r44_total_specific_provisions;
		}

		public void setR44_total_specific_provisions(BigDecimal r44_total_specific_provisions) {
			this.r44_total_specific_provisions = r44_total_specific_provisions;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_qualifi_stage_2_provisions_sp() {
			return r45_qualifi_stage_2_provisions_sp;
		}

		public void setR45_qualifi_stage_2_provisions_sp(BigDecimal r45_qualifi_stage_2_provisions_sp) {
			this.r45_qualifi_stage_2_provisions_sp = r45_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR45_stage_3_provisions_sp() {
			return r45_stage_3_provisions_sp;
		}

		public void setR45_stage_3_provisions_sp(BigDecimal r45_stage_3_provisions_sp) {
			this.r45_stage_3_provisions_sp = r45_stage_3_provisions_sp;
		}

		public BigDecimal getR45_total_specific_provisions() {
			return r45_total_specific_provisions;
		}

		public void setR45_total_specific_provisions(BigDecimal r45_total_specific_provisions) {
			this.r45_total_specific_provisions = r45_total_specific_provisions;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_qualifi_stage_2_provisions_sp() {
			return r46_qualifi_stage_2_provisions_sp;
		}

		public void setR46_qualifi_stage_2_provisions_sp(BigDecimal r46_qualifi_stage_2_provisions_sp) {
			this.r46_qualifi_stage_2_provisions_sp = r46_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR46_stage_3_provisions_sp() {
			return r46_stage_3_provisions_sp;
		}

		public void setR46_stage_3_provisions_sp(BigDecimal r46_stage_3_provisions_sp) {
			this.r46_stage_3_provisions_sp = r46_stage_3_provisions_sp;
		}

		public BigDecimal getR46_total_specific_provisions() {
			return r46_total_specific_provisions;
		}

		public void setR46_total_specific_provisions(BigDecimal r46_total_specific_provisions) {
			this.r46_total_specific_provisions = r46_total_specific_provisions;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_qualifi_stage_2_provisions_sp() {
			return r47_qualifi_stage_2_provisions_sp;
		}

		public void setR47_qualifi_stage_2_provisions_sp(BigDecimal r47_qualifi_stage_2_provisions_sp) {
			this.r47_qualifi_stage_2_provisions_sp = r47_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR47_stage_3_provisions_sp() {
			return r47_stage_3_provisions_sp;
		}

		public void setR47_stage_3_provisions_sp(BigDecimal r47_stage_3_provisions_sp) {
			this.r47_stage_3_provisions_sp = r47_stage_3_provisions_sp;
		}

		public BigDecimal getR47_total_specific_provisions() {
			return r47_total_specific_provisions;
		}

		public void setR47_total_specific_provisions(BigDecimal r47_total_specific_provisions) {
			this.r47_total_specific_provisions = r47_total_specific_provisions;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_qualifi_stage_2_provisions_sp() {
			return r48_qualifi_stage_2_provisions_sp;
		}

		public void setR48_qualifi_stage_2_provisions_sp(BigDecimal r48_qualifi_stage_2_provisions_sp) {
			this.r48_qualifi_stage_2_provisions_sp = r48_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR48_stage_3_provisions_sp() {
			return r48_stage_3_provisions_sp;
		}

		public void setR48_stage_3_provisions_sp(BigDecimal r48_stage_3_provisions_sp) {
			this.r48_stage_3_provisions_sp = r48_stage_3_provisions_sp;
		}

		public BigDecimal getR48_total_specific_provisions() {
			return r48_total_specific_provisions;
		}

		public void setR48_total_specific_provisions(BigDecimal r48_total_specific_provisions) {
			this.r48_total_specific_provisions = r48_total_specific_provisions;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_qualifi_stage_2_provisions_sp() {
			return r49_qualifi_stage_2_provisions_sp;
		}

		public void setR49_qualifi_stage_2_provisions_sp(BigDecimal r49_qualifi_stage_2_provisions_sp) {
			this.r49_qualifi_stage_2_provisions_sp = r49_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR49_stage_3_provisions_sp() {
			return r49_stage_3_provisions_sp;
		}

		public void setR49_stage_3_provisions_sp(BigDecimal r49_stage_3_provisions_sp) {
			this.r49_stage_3_provisions_sp = r49_stage_3_provisions_sp;
		}

		public BigDecimal getR49_total_specific_provisions() {
			return r49_total_specific_provisions;
		}

		public void setR49_total_specific_provisions(BigDecimal r49_total_specific_provisions) {
			this.r49_total_specific_provisions = r49_total_specific_provisions;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public BigDecimal getR50_qualifi_stage_2_provisions_sp() {
			return r50_qualifi_stage_2_provisions_sp;
		}

		public void setR50_qualifi_stage_2_provisions_sp(BigDecimal r50_qualifi_stage_2_provisions_sp) {
			this.r50_qualifi_stage_2_provisions_sp = r50_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR50_stage_3_provisions_sp() {
			return r50_stage_3_provisions_sp;
		}

		public void setR50_stage_3_provisions_sp(BigDecimal r50_stage_3_provisions_sp) {
			this.r50_stage_3_provisions_sp = r50_stage_3_provisions_sp;
		}

		public BigDecimal getR50_total_specific_provisions() {
			return r50_total_specific_provisions;
		}

		public void setR50_total_specific_provisions(BigDecimal r50_total_specific_provisions) {
			this.r50_total_specific_provisions = r50_total_specific_provisions;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public BigDecimal getR51_qualifi_stage_2_provisions_sp() {
			return r51_qualifi_stage_2_provisions_sp;
		}

		public void setR51_qualifi_stage_2_provisions_sp(BigDecimal r51_qualifi_stage_2_provisions_sp) {
			this.r51_qualifi_stage_2_provisions_sp = r51_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR51_stage_3_provisions_sp() {
			return r51_stage_3_provisions_sp;
		}

		public void setR51_stage_3_provisions_sp(BigDecimal r51_stage_3_provisions_sp) {
			this.r51_stage_3_provisions_sp = r51_stage_3_provisions_sp;
		}

		public BigDecimal getR51_total_specific_provisions() {
			return r51_total_specific_provisions;
		}

		public void setR51_total_specific_provisions(BigDecimal r51_total_specific_provisions) {
			this.r51_total_specific_provisions = r51_total_specific_provisions;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public BigDecimal getR52_qualifi_stage_2_provisions_sp() {
			return r52_qualifi_stage_2_provisions_sp;
		}

		public void setR52_qualifi_stage_2_provisions_sp(BigDecimal r52_qualifi_stage_2_provisions_sp) {
			this.r52_qualifi_stage_2_provisions_sp = r52_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR52_stage_3_provisions_sp() {
			return r52_stage_3_provisions_sp;
		}

		public void setR52_stage_3_provisions_sp(BigDecimal r52_stage_3_provisions_sp) {
			this.r52_stage_3_provisions_sp = r52_stage_3_provisions_sp;
		}

		public BigDecimal getR52_total_specific_provisions() {
			return r52_total_specific_provisions;
		}

		public void setR52_total_specific_provisions(BigDecimal r52_total_specific_provisions) {
			this.r52_total_specific_provisions = r52_total_specific_provisions;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public BigDecimal getR53_qualifi_stage_2_provisions_sp() {
			return r53_qualifi_stage_2_provisions_sp;
		}

		public void setR53_qualifi_stage_2_provisions_sp(BigDecimal r53_qualifi_stage_2_provisions_sp) {
			this.r53_qualifi_stage_2_provisions_sp = r53_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR53_stage_3_provisions_sp() {
			return r53_stage_3_provisions_sp;
		}

		public void setR53_stage_3_provisions_sp(BigDecimal r53_stage_3_provisions_sp) {
			this.r53_stage_3_provisions_sp = r53_stage_3_provisions_sp;
		}

		public BigDecimal getR53_total_specific_provisions() {
			return r53_total_specific_provisions;
		}

		public void setR53_total_specific_provisions(BigDecimal r53_total_specific_provisions) {
			this.r53_total_specific_provisions = r53_total_specific_provisions;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public BigDecimal getR54_qualifi_stage_2_provisions_sp() {
			return r54_qualifi_stage_2_provisions_sp;
		}

		public void setR54_qualifi_stage_2_provisions_sp(BigDecimal r54_qualifi_stage_2_provisions_sp) {
			this.r54_qualifi_stage_2_provisions_sp = r54_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR54_stage_3_provisions_sp() {
			return r54_stage_3_provisions_sp;
		}

		public void setR54_stage_3_provisions_sp(BigDecimal r54_stage_3_provisions_sp) {
			this.r54_stage_3_provisions_sp = r54_stage_3_provisions_sp;
		}

		public BigDecimal getR54_total_specific_provisions() {
			return r54_total_specific_provisions;
		}

		public void setR54_total_specific_provisions(BigDecimal r54_total_specific_provisions) {
			this.r54_total_specific_provisions = r54_total_specific_provisions;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public BigDecimal getR55_qualifi_stage_2_provisions_sp() {
			return r55_qualifi_stage_2_provisions_sp;
		}

		public void setR55_qualifi_stage_2_provisions_sp(BigDecimal r55_qualifi_stage_2_provisions_sp) {
			this.r55_qualifi_stage_2_provisions_sp = r55_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR55_stage_3_provisions_sp() {
			return r55_stage_3_provisions_sp;
		}

		public void setR55_stage_3_provisions_sp(BigDecimal r55_stage_3_provisions_sp) {
			this.r55_stage_3_provisions_sp = r55_stage_3_provisions_sp;
		}

		public BigDecimal getR55_total_specific_provisions() {
			return r55_total_specific_provisions;
		}

		public void setR55_total_specific_provisions(BigDecimal r55_total_specific_provisions) {
			this.r55_total_specific_provisions = r55_total_specific_provisions;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public BigDecimal getR56_qualifi_stage_2_provisions_sp() {
			return r56_qualifi_stage_2_provisions_sp;
		}

		public void setR56_qualifi_stage_2_provisions_sp(BigDecimal r56_qualifi_stage_2_provisions_sp) {
			this.r56_qualifi_stage_2_provisions_sp = r56_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR56_stage_3_provisions_sp() {
			return r56_stage_3_provisions_sp;
		}

		public void setR56_stage_3_provisions_sp(BigDecimal r56_stage_3_provisions_sp) {
			this.r56_stage_3_provisions_sp = r56_stage_3_provisions_sp;
		}

		public BigDecimal getR56_total_specific_provisions() {
			return r56_total_specific_provisions;
		}

		public void setR56_total_specific_provisions(BigDecimal r56_total_specific_provisions) {
			this.r56_total_specific_provisions = r56_total_specific_provisions;
		}

		public String getR57_product() {
			return r57_product;
		}

		public void setR57_product(String r57_product) {
			this.r57_product = r57_product;
		}

		public BigDecimal getR57_qualifi_stage_2_provisions_sp() {
			return r57_qualifi_stage_2_provisions_sp;
		}

		public void setR57_qualifi_stage_2_provisions_sp(BigDecimal r57_qualifi_stage_2_provisions_sp) {
			this.r57_qualifi_stage_2_provisions_sp = r57_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR57_stage_3_provisions_sp() {
			return r57_stage_3_provisions_sp;
		}

		public void setR57_stage_3_provisions_sp(BigDecimal r57_stage_3_provisions_sp) {
			this.r57_stage_3_provisions_sp = r57_stage_3_provisions_sp;
		}

		public BigDecimal getR57_total_specific_provisions() {
			return r57_total_specific_provisions;
		}

		public void setR57_total_specific_provisions(BigDecimal r57_total_specific_provisions) {
			this.r57_total_specific_provisions = r57_total_specific_provisions;
		}

		public String getR58_product() {
			return r58_product;
		}

		public void setR58_product(String r58_product) {
			this.r58_product = r58_product;
		}

		public BigDecimal getR58_qualifi_stage_2_provisions_sp() {
			return r58_qualifi_stage_2_provisions_sp;
		}

		public void setR58_qualifi_stage_2_provisions_sp(BigDecimal r58_qualifi_stage_2_provisions_sp) {
			this.r58_qualifi_stage_2_provisions_sp = r58_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR58_stage_3_provisions_sp() {
			return r58_stage_3_provisions_sp;
		}

		public void setR58_stage_3_provisions_sp(BigDecimal r58_stage_3_provisions_sp) {
			this.r58_stage_3_provisions_sp = r58_stage_3_provisions_sp;
		}

		public BigDecimal getR58_total_specific_provisions() {
			return r58_total_specific_provisions;
		}

		public void setR58_total_specific_provisions(BigDecimal r58_total_specific_provisions) {
			this.r58_total_specific_provisions = r58_total_specific_provisions;
		}

		public String getR59_product() {
			return r59_product;
		}

		public void setR59_product(String r59_product) {
			this.r59_product = r59_product;
		}

		public BigDecimal getR59_qualifi_stage_2_provisions_sp() {
			return r59_qualifi_stage_2_provisions_sp;
		}

		public void setR59_qualifi_stage_2_provisions_sp(BigDecimal r59_qualifi_stage_2_provisions_sp) {
			this.r59_qualifi_stage_2_provisions_sp = r59_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR59_stage_3_provisions_sp() {
			return r59_stage_3_provisions_sp;
		}

		public void setR59_stage_3_provisions_sp(BigDecimal r59_stage_3_provisions_sp) {
			this.r59_stage_3_provisions_sp = r59_stage_3_provisions_sp;
		}

		public BigDecimal getR59_total_specific_provisions() {
			return r59_total_specific_provisions;
		}

		public void setR59_total_specific_provisions(BigDecimal r59_total_specific_provisions) {
			this.r59_total_specific_provisions = r59_total_specific_provisions;
		}

		public String getR60_product() {
			return r60_product;
		}

		public void setR60_product(String r60_product) {
			this.r60_product = r60_product;
		}

		public BigDecimal getR60_qualifi_stage_2_provisions_sp() {
			return r60_qualifi_stage_2_provisions_sp;
		}

		public void setR60_qualifi_stage_2_provisions_sp(BigDecimal r60_qualifi_stage_2_provisions_sp) {
			this.r60_qualifi_stage_2_provisions_sp = r60_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR60_stage_3_provisions_sp() {
			return r60_stage_3_provisions_sp;
		}

		public void setR60_stage_3_provisions_sp(BigDecimal r60_stage_3_provisions_sp) {
			this.r60_stage_3_provisions_sp = r60_stage_3_provisions_sp;
		}

		public BigDecimal getR60_total_specific_provisions() {
			return r60_total_specific_provisions;
		}

		public void setR60_total_specific_provisions(BigDecimal r60_total_specific_provisions) {
			this.r60_total_specific_provisions = r60_total_specific_provisions;
		}

		public String getR61_product() {
			return r61_product;
		}

		public void setR61_product(String r61_product) {
			this.r61_product = r61_product;
		}

		public BigDecimal getR61_qualifi_stage_2_provisions_sp() {
			return r61_qualifi_stage_2_provisions_sp;
		}

		public void setR61_qualifi_stage_2_provisions_sp(BigDecimal r61_qualifi_stage_2_provisions_sp) {
			this.r61_qualifi_stage_2_provisions_sp = r61_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR61_stage_3_provisions_sp() {
			return r61_stage_3_provisions_sp;
		}

		public void setR61_stage_3_provisions_sp(BigDecimal r61_stage_3_provisions_sp) {
			this.r61_stage_3_provisions_sp = r61_stage_3_provisions_sp;
		}

		public BigDecimal getR61_total_specific_provisions() {
			return r61_total_specific_provisions;
		}

		public void setR61_total_specific_provisions(BigDecimal r61_total_specific_provisions) {
			this.r61_total_specific_provisions = r61_total_specific_provisions;
		}

		public String getR62_product() {
			return r62_product;
		}

		public void setR62_product(String r62_product) {
			this.r62_product = r62_product;
		}

		public BigDecimal getR62_qualifi_stage_2_provisions_sp() {
			return r62_qualifi_stage_2_provisions_sp;
		}

		public void setR62_qualifi_stage_2_provisions_sp(BigDecimal r62_qualifi_stage_2_provisions_sp) {
			this.r62_qualifi_stage_2_provisions_sp = r62_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR62_stage_3_provisions_sp() {
			return r62_stage_3_provisions_sp;
		}

		public void setR62_stage_3_provisions_sp(BigDecimal r62_stage_3_provisions_sp) {
			this.r62_stage_3_provisions_sp = r62_stage_3_provisions_sp;
		}

		public BigDecimal getR62_total_specific_provisions() {
			return r62_total_specific_provisions;
		}

		public void setR62_total_specific_provisions(BigDecimal r62_total_specific_provisions) {
			this.r62_total_specific_provisions = r62_total_specific_provisions;
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
	}

	// ------------------------------
	// Entity representation for M_SP Archival Detail Table
	// ------------------------------
	public static class M_SP_Archival_Detail_Entity implements Serializable {
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportLable;
		private String reportAddlCriteria1;
		private String reportAddlCriteria2;
		private String reportAddlCriteria3;
		private BigDecimal sanctionLimit;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInPula;
		private Date reportDate;
		private String reportName;
		private String createUser;
		private Date createTime;
		private String modifyUser;
		private Date modifyTime;
		private String verifyUser;
		private Date verifyTime;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private String segment;
		private BigDecimal provision;

		public M_SP_Archival_Detail_Entity() {
			super();
		}

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

		public String getReportLable() {
			return reportLable;
		}

		public void setReportLable(String reportLable) {
			this.reportLable = reportLable;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
		}

		public String getReportAddlCriteria2() {
			return reportAddlCriteria2;
		}

		public void setReportAddlCriteria2(String reportAddlCriteria2) {
			this.reportAddlCriteria2 = reportAddlCriteria2;
		}

		public String getReportAddlCriteria3() {
			return reportAddlCriteria3;
		}

		public void setReportAddlCriteria3(String reportAddlCriteria3) {
			this.reportAddlCriteria3 = reportAddlCriteria3;
		}

		public BigDecimal getSanctionLimit() {
			return sanctionLimit;
		}

		public void setSanctionLimit(BigDecimal sanctionLimit) {
			this.sanctionLimit = sanctionLimit;
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

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public BigDecimal getProvision() {
			return provision;
		}

		public void setProvision(BigDecimal provision) {
			this.provision = provision;
		}
	}

	// ------------------------------
	// Entity representation for M_SP Archival Summary Table
	// ------------------------------
	public static class M_SP_Archival_Summary_Entity implements Serializable {
		private String r9_product;
		private BigDecimal r9_qualifi_stage_2_provisions_sp;
		private BigDecimal r9_stage_3_provisions_sp;
		private BigDecimal r9_total_specific_provisions;
		private String r10_product;
		private BigDecimal r10_qualifi_stage_2_provisions_sp;
		private BigDecimal r10_stage_3_provisions_sp;
		private BigDecimal r10_total_specific_provisions;
		private String r11_product;
		private BigDecimal r11_qualifi_stage_2_provisions_sp;
		private BigDecimal r11_stage_3_provisions_sp;
		private BigDecimal r11_total_specific_provisions;
		private String r12_product;
		private BigDecimal r12_qualifi_stage_2_provisions_sp;
		private BigDecimal r12_stage_3_provisions_sp;
		private BigDecimal r12_total_specific_provisions;
		private String r13_product;
		private BigDecimal r13_qualifi_stage_2_provisions_sp;
		private BigDecimal r13_stage_3_provisions_sp;
		private BigDecimal r13_total_specific_provisions;
		private String r14_product;
		private BigDecimal r14_qualifi_stage_2_provisions_sp;
		private BigDecimal r14_stage_3_provisions_sp;
		private BigDecimal r14_total_specific_provisions;
		private String r15_product;
		private BigDecimal r15_qualifi_stage_2_provisions_sp;
		private BigDecimal r15_stage_3_provisions_sp;
		private BigDecimal r15_total_specific_provisions;
		private String r16_product;
		private BigDecimal r16_qualifi_stage_2_provisions_sp;
		private BigDecimal r16_stage_3_provisions_sp;
		private BigDecimal r16_total_specific_provisions;
		private String r17_product;
		private BigDecimal r17_qualifi_stage_2_provisions_sp;
		private BigDecimal r17_stage_3_provisions_sp;
		private BigDecimal r17_total_specific_provisions;
		private String r18_product;
		private BigDecimal r18_qualifi_stage_2_provisions_sp;
		private BigDecimal r18_stage_3_provisions_sp;
		private BigDecimal r18_total_specific_provisions;
		private String r19_product;
		private BigDecimal r19_qualifi_stage_2_provisions_sp;
		private BigDecimal r19_stage_3_provisions_sp;
		private BigDecimal r19_total_specific_provisions;
		private String r20_product;
		private BigDecimal r20_qualifi_stage_2_provisions_sp;
		private BigDecimal r20_stage_3_provisions_sp;
		private BigDecimal r20_total_specific_provisions;
		private String r21_product;
		private BigDecimal r21_qualifi_stage_2_provisions_sp;
		private BigDecimal r21_stage_3_provisions_sp;
		private BigDecimal r21_total_specific_provisions;
		private String r22_product;
		private BigDecimal r22_qualifi_stage_2_provisions_sp;
		private BigDecimal r22_stage_3_provisions_sp;
		private BigDecimal r22_total_specific_provisions;
		private String r23_product;
		private BigDecimal r23_qualifi_stage_2_provisions_sp;
		private BigDecimal r23_stage_3_provisions_sp;
		private BigDecimal r23_total_specific_provisions;
		private String r24_product;
		private BigDecimal r24_qualifi_stage_2_provisions_sp;
		private BigDecimal r24_stage_3_provisions_sp;
		private BigDecimal r24_total_specific_provisions;
		private String r25_product;
		private BigDecimal r25_qualifi_stage_2_provisions_sp;
		private BigDecimal r25_stage_3_provisions_sp;
		private BigDecimal r25_total_specific_provisions;
		private String r26_product;
		private BigDecimal r26_qualifi_stage_2_provisions_sp;
		private BigDecimal r26_stage_3_provisions_sp;
		private BigDecimal r26_total_specific_provisions;
		private String r27_product;
		private BigDecimal r27_qualifi_stage_2_provisions_sp;
		private BigDecimal r27_stage_3_provisions_sp;
		private BigDecimal r27_total_specific_provisions;
		private String r28_product;
		private BigDecimal r28_qualifi_stage_2_provisions_sp;
		private BigDecimal r28_stage_3_provisions_sp;
		private BigDecimal r28_total_specific_provisions;
		private String r29_product;
		private BigDecimal r29_qualifi_stage_2_provisions_sp;
		private BigDecimal r29_stage_3_provisions_sp;
		private BigDecimal r29_total_specific_provisions;
		private String r30_product;
		private BigDecimal r30_qualifi_stage_2_provisions_sp;
		private BigDecimal r30_stage_3_provisions_sp;
		private BigDecimal r30_total_specific_provisions;
		private String r31_product;
		private BigDecimal r31_qualifi_stage_2_provisions_sp;
		private BigDecimal r31_stage_3_provisions_sp;
		private BigDecimal r31_total_specific_provisions;
		private String r32_product;
		private BigDecimal r32_qualifi_stage_2_provisions_sp;
		private BigDecimal r32_stage_3_provisions_sp;
		private BigDecimal r32_total_specific_provisions;
		private String r33_product;
		private BigDecimal r33_qualifi_stage_2_provisions_sp;
		private BigDecimal r33_stage_3_provisions_sp;
		private BigDecimal r33_total_specific_provisions;
		private String r34_product;
		private BigDecimal r34_qualifi_stage_2_provisions_sp;
		private BigDecimal r34_stage_3_provisions_sp;
		private BigDecimal r34_total_specific_provisions;
		private String r35_product;
		private BigDecimal r35_qualifi_stage_2_provisions_sp;
		private BigDecimal r35_stage_3_provisions_sp;
		private BigDecimal r35_total_specific_provisions;
		private String r36_product;
		private BigDecimal r36_qualifi_stage_2_provisions_sp;
		private BigDecimal r36_stage_3_provisions_sp;
		private BigDecimal r36_total_specific_provisions;
		private String r37_product;
		private BigDecimal r37_qualifi_stage_2_provisions_sp;
		private BigDecimal r37_stage_3_provisions_sp;
		private BigDecimal r37_total_specific_provisions;
		private String r38_product;
		private BigDecimal r38_qualifi_stage_2_provisions_sp;
		private BigDecimal r38_stage_3_provisions_sp;
		private BigDecimal r38_total_specific_provisions;
		private String r39_product;
		private BigDecimal r39_qualifi_stage_2_provisions_sp;
		private BigDecimal r39_stage_3_provisions_sp;
		private BigDecimal r39_total_specific_provisions;
		private String r40_product;
		private BigDecimal r40_qualifi_stage_2_provisions_sp;
		private BigDecimal r40_stage_3_provisions_sp;
		private BigDecimal r40_total_specific_provisions;
		private String r41_product;
		private BigDecimal r41_qualifi_stage_2_provisions_sp;
		private BigDecimal r41_stage_3_provisions_sp;
		private BigDecimal r41_total_specific_provisions;
		private String r42_product;
		private BigDecimal r42_qualifi_stage_2_provisions_sp;
		private BigDecimal r42_stage_3_provisions_sp;
		private BigDecimal r42_total_specific_provisions;
		private String r43_product;
		private BigDecimal r43_qualifi_stage_2_provisions_sp;
		private BigDecimal r43_stage_3_provisions_sp;
		private BigDecimal r43_total_specific_provisions;
		private String r44_product;
		private BigDecimal r44_qualifi_stage_2_provisions_sp;
		private BigDecimal r44_stage_3_provisions_sp;
		private BigDecimal r44_total_specific_provisions;
		private String r45_product;
		private BigDecimal r45_qualifi_stage_2_provisions_sp;
		private BigDecimal r45_stage_3_provisions_sp;
		private BigDecimal r45_total_specific_provisions;
		private String r46_product;
		private BigDecimal r46_qualifi_stage_2_provisions_sp;
		private BigDecimal r46_stage_3_provisions_sp;
		private BigDecimal r46_total_specific_provisions;
		private String r47_product;
		private BigDecimal r47_qualifi_stage_2_provisions_sp;
		private BigDecimal r47_stage_3_provisions_sp;
		private BigDecimal r47_total_specific_provisions;
		private String r48_product;
		private BigDecimal r48_qualifi_stage_2_provisions_sp;
		private BigDecimal r48_stage_3_provisions_sp;
		private BigDecimal r48_total_specific_provisions;
		private String r49_product;
		private BigDecimal r49_qualifi_stage_2_provisions_sp;
		private BigDecimal r49_stage_3_provisions_sp;
		private BigDecimal r49_total_specific_provisions;
		private String r50_product;
		private BigDecimal r50_qualifi_stage_2_provisions_sp;
		private BigDecimal r50_stage_3_provisions_sp;
		private BigDecimal r50_total_specific_provisions;
		private String r51_product;
		private BigDecimal r51_qualifi_stage_2_provisions_sp;
		private BigDecimal r51_stage_3_provisions_sp;
		private BigDecimal r51_total_specific_provisions;
		private String r52_product;
		private BigDecimal r52_qualifi_stage_2_provisions_sp;
		private BigDecimal r52_stage_3_provisions_sp;
		private BigDecimal r52_total_specific_provisions;
		private String r53_product;
		private BigDecimal r53_qualifi_stage_2_provisions_sp;
		private BigDecimal r53_stage_3_provisions_sp;
		private BigDecimal r53_total_specific_provisions;
		private String r54_product;
		private BigDecimal r54_qualifi_stage_2_provisions_sp;
		private BigDecimal r54_stage_3_provisions_sp;
		private BigDecimal r54_total_specific_provisions;
		private String r55_product;
		private BigDecimal r55_qualifi_stage_2_provisions_sp;
		private BigDecimal r55_stage_3_provisions_sp;
		private BigDecimal r55_total_specific_provisions;
		private String r56_product;
		private BigDecimal r56_qualifi_stage_2_provisions_sp;
		private BigDecimal r56_stage_3_provisions_sp;
		private BigDecimal r56_total_specific_provisions;
		private String r57_product;
		private BigDecimal r57_qualifi_stage_2_provisions_sp;
		private BigDecimal r57_stage_3_provisions_sp;
		private BigDecimal r57_total_specific_provisions;
		private String r58_product;
		private BigDecimal r58_qualifi_stage_2_provisions_sp;
		private BigDecimal r58_stage_3_provisions_sp;
		private BigDecimal r58_total_specific_provisions;
		private String r59_product;
		private BigDecimal r59_qualifi_stage_2_provisions_sp;
		private BigDecimal r59_stage_3_provisions_sp;
		private BigDecimal r59_total_specific_provisions;
		private String r60_product;
		private BigDecimal r60_qualifi_stage_2_provisions_sp;
		private BigDecimal r60_stage_3_provisions_sp;
		private BigDecimal r60_total_specific_provisions;
		private String r61_product;
		private BigDecimal r61_qualifi_stage_2_provisions_sp;
		private BigDecimal r61_stage_3_provisions_sp;
		private BigDecimal r61_total_specific_provisions;
		private String r62_product;
		private BigDecimal r62_qualifi_stage_2_provisions_sp;
		private BigDecimal r62_stage_3_provisions_sp;
		private BigDecimal r62_total_specific_provisions;
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SP_Archival_Summary_Entity() {
			super();
		}

		public String getR9_product() {
			return r9_product;
		}

		public void setR9_product(String r9_product) {
			this.r9_product = r9_product;
		}

		public BigDecimal getR9_qualifi_stage_2_provisions_sp() {
			return r9_qualifi_stage_2_provisions_sp;
		}

		public void setR9_qualifi_stage_2_provisions_sp(BigDecimal r9_qualifi_stage_2_provisions_sp) {
			this.r9_qualifi_stage_2_provisions_sp = r9_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR9_stage_3_provisions_sp() {
			return r9_stage_3_provisions_sp;
		}

		public void setR9_stage_3_provisions_sp(BigDecimal r9_stage_3_provisions_sp) {
			this.r9_stage_3_provisions_sp = r9_stage_3_provisions_sp;
		}

		public BigDecimal getR9_total_specific_provisions() {
			return r9_total_specific_provisions;
		}

		public void setR9_total_specific_provisions(BigDecimal r9_total_specific_provisions) {
			this.r9_total_specific_provisions = r9_total_specific_provisions;
		}

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public BigDecimal getR10_qualifi_stage_2_provisions_sp() {
			return r10_qualifi_stage_2_provisions_sp;
		}

		public void setR10_qualifi_stage_2_provisions_sp(BigDecimal r10_qualifi_stage_2_provisions_sp) {
			this.r10_qualifi_stage_2_provisions_sp = r10_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR10_stage_3_provisions_sp() {
			return r10_stage_3_provisions_sp;
		}

		public void setR10_stage_3_provisions_sp(BigDecimal r10_stage_3_provisions_sp) {
			this.r10_stage_3_provisions_sp = r10_stage_3_provisions_sp;
		}

		public BigDecimal getR10_total_specific_provisions() {
			return r10_total_specific_provisions;
		}

		public void setR10_total_specific_provisions(BigDecimal r10_total_specific_provisions) {
			this.r10_total_specific_provisions = r10_total_specific_provisions;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_qualifi_stage_2_provisions_sp() {
			return r11_qualifi_stage_2_provisions_sp;
		}

		public void setR11_qualifi_stage_2_provisions_sp(BigDecimal r11_qualifi_stage_2_provisions_sp) {
			this.r11_qualifi_stage_2_provisions_sp = r11_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR11_stage_3_provisions_sp() {
			return r11_stage_3_provisions_sp;
		}

		public void setR11_stage_3_provisions_sp(BigDecimal r11_stage_3_provisions_sp) {
			this.r11_stage_3_provisions_sp = r11_stage_3_provisions_sp;
		}

		public BigDecimal getR11_total_specific_provisions() {
			return r11_total_specific_provisions;
		}

		public void setR11_total_specific_provisions(BigDecimal r11_total_specific_provisions) {
			this.r11_total_specific_provisions = r11_total_specific_provisions;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_qualifi_stage_2_provisions_sp() {
			return r12_qualifi_stage_2_provisions_sp;
		}

		public void setR12_qualifi_stage_2_provisions_sp(BigDecimal r12_qualifi_stage_2_provisions_sp) {
			this.r12_qualifi_stage_2_provisions_sp = r12_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR12_stage_3_provisions_sp() {
			return r12_stage_3_provisions_sp;
		}

		public void setR12_stage_3_provisions_sp(BigDecimal r12_stage_3_provisions_sp) {
			this.r12_stage_3_provisions_sp = r12_stage_3_provisions_sp;
		}

		public BigDecimal getR12_total_specific_provisions() {
			return r12_total_specific_provisions;
		}

		public void setR12_total_specific_provisions(BigDecimal r12_total_specific_provisions) {
			this.r12_total_specific_provisions = r12_total_specific_provisions;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_qualifi_stage_2_provisions_sp() {
			return r13_qualifi_stage_2_provisions_sp;
		}

		public void setR13_qualifi_stage_2_provisions_sp(BigDecimal r13_qualifi_stage_2_provisions_sp) {
			this.r13_qualifi_stage_2_provisions_sp = r13_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR13_stage_3_provisions_sp() {
			return r13_stage_3_provisions_sp;
		}

		public void setR13_stage_3_provisions_sp(BigDecimal r13_stage_3_provisions_sp) {
			this.r13_stage_3_provisions_sp = r13_stage_3_provisions_sp;
		}

		public BigDecimal getR13_total_specific_provisions() {
			return r13_total_specific_provisions;
		}

		public void setR13_total_specific_provisions(BigDecimal r13_total_specific_provisions) {
			this.r13_total_specific_provisions = r13_total_specific_provisions;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_qualifi_stage_2_provisions_sp() {
			return r14_qualifi_stage_2_provisions_sp;
		}

		public void setR14_qualifi_stage_2_provisions_sp(BigDecimal r14_qualifi_stage_2_provisions_sp) {
			this.r14_qualifi_stage_2_provisions_sp = r14_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR14_stage_3_provisions_sp() {
			return r14_stage_3_provisions_sp;
		}

		public void setR14_stage_3_provisions_sp(BigDecimal r14_stage_3_provisions_sp) {
			this.r14_stage_3_provisions_sp = r14_stage_3_provisions_sp;
		}

		public BigDecimal getR14_total_specific_provisions() {
			return r14_total_specific_provisions;
		}

		public void setR14_total_specific_provisions(BigDecimal r14_total_specific_provisions) {
			this.r14_total_specific_provisions = r14_total_specific_provisions;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_qualifi_stage_2_provisions_sp() {
			return r15_qualifi_stage_2_provisions_sp;
		}

		public void setR15_qualifi_stage_2_provisions_sp(BigDecimal r15_qualifi_stage_2_provisions_sp) {
			this.r15_qualifi_stage_2_provisions_sp = r15_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR15_stage_3_provisions_sp() {
			return r15_stage_3_provisions_sp;
		}

		public void setR15_stage_3_provisions_sp(BigDecimal r15_stage_3_provisions_sp) {
			this.r15_stage_3_provisions_sp = r15_stage_3_provisions_sp;
		}

		public BigDecimal getR15_total_specific_provisions() {
			return r15_total_specific_provisions;
		}

		public void setR15_total_specific_provisions(BigDecimal r15_total_specific_provisions) {
			this.r15_total_specific_provisions = r15_total_specific_provisions;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_qualifi_stage_2_provisions_sp() {
			return r16_qualifi_stage_2_provisions_sp;
		}

		public void setR16_qualifi_stage_2_provisions_sp(BigDecimal r16_qualifi_stage_2_provisions_sp) {
			this.r16_qualifi_stage_2_provisions_sp = r16_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR16_stage_3_provisions_sp() {
			return r16_stage_3_provisions_sp;
		}

		public void setR16_stage_3_provisions_sp(BigDecimal r16_stage_3_provisions_sp) {
			this.r16_stage_3_provisions_sp = r16_stage_3_provisions_sp;
		}

		public BigDecimal getR16_total_specific_provisions() {
			return r16_total_specific_provisions;
		}

		public void setR16_total_specific_provisions(BigDecimal r16_total_specific_provisions) {
			this.r16_total_specific_provisions = r16_total_specific_provisions;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_qualifi_stage_2_provisions_sp() {
			return r17_qualifi_stage_2_provisions_sp;
		}

		public void setR17_qualifi_stage_2_provisions_sp(BigDecimal r17_qualifi_stage_2_provisions_sp) {
			this.r17_qualifi_stage_2_provisions_sp = r17_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR17_stage_3_provisions_sp() {
			return r17_stage_3_provisions_sp;
		}

		public void setR17_stage_3_provisions_sp(BigDecimal r17_stage_3_provisions_sp) {
			this.r17_stage_3_provisions_sp = r17_stage_3_provisions_sp;
		}

		public BigDecimal getR17_total_specific_provisions() {
			return r17_total_specific_provisions;
		}

		public void setR17_total_specific_provisions(BigDecimal r17_total_specific_provisions) {
			this.r17_total_specific_provisions = r17_total_specific_provisions;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_qualifi_stage_2_provisions_sp() {
			return r18_qualifi_stage_2_provisions_sp;
		}

		public void setR18_qualifi_stage_2_provisions_sp(BigDecimal r18_qualifi_stage_2_provisions_sp) {
			this.r18_qualifi_stage_2_provisions_sp = r18_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR18_stage_3_provisions_sp() {
			return r18_stage_3_provisions_sp;
		}

		public void setR18_stage_3_provisions_sp(BigDecimal r18_stage_3_provisions_sp) {
			this.r18_stage_3_provisions_sp = r18_stage_3_provisions_sp;
		}

		public BigDecimal getR18_total_specific_provisions() {
			return r18_total_specific_provisions;
		}

		public void setR18_total_specific_provisions(BigDecimal r18_total_specific_provisions) {
			this.r18_total_specific_provisions = r18_total_specific_provisions;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_qualifi_stage_2_provisions_sp() {
			return r19_qualifi_stage_2_provisions_sp;
		}

		public void setR19_qualifi_stage_2_provisions_sp(BigDecimal r19_qualifi_stage_2_provisions_sp) {
			this.r19_qualifi_stage_2_provisions_sp = r19_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR19_stage_3_provisions_sp() {
			return r19_stage_3_provisions_sp;
		}

		public void setR19_stage_3_provisions_sp(BigDecimal r19_stage_3_provisions_sp) {
			this.r19_stage_3_provisions_sp = r19_stage_3_provisions_sp;
		}

		public BigDecimal getR19_total_specific_provisions() {
			return r19_total_specific_provisions;
		}

		public void setR19_total_specific_provisions(BigDecimal r19_total_specific_provisions) {
			this.r19_total_specific_provisions = r19_total_specific_provisions;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_qualifi_stage_2_provisions_sp() {
			return r20_qualifi_stage_2_provisions_sp;
		}

		public void setR20_qualifi_stage_2_provisions_sp(BigDecimal r20_qualifi_stage_2_provisions_sp) {
			this.r20_qualifi_stage_2_provisions_sp = r20_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR20_stage_3_provisions_sp() {
			return r20_stage_3_provisions_sp;
		}

		public void setR20_stage_3_provisions_sp(BigDecimal r20_stage_3_provisions_sp) {
			this.r20_stage_3_provisions_sp = r20_stage_3_provisions_sp;
		}

		public BigDecimal getR20_total_specific_provisions() {
			return r20_total_specific_provisions;
		}

		public void setR20_total_specific_provisions(BigDecimal r20_total_specific_provisions) {
			this.r20_total_specific_provisions = r20_total_specific_provisions;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_qualifi_stage_2_provisions_sp() {
			return r21_qualifi_stage_2_provisions_sp;
		}

		public void setR21_qualifi_stage_2_provisions_sp(BigDecimal r21_qualifi_stage_2_provisions_sp) {
			this.r21_qualifi_stage_2_provisions_sp = r21_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR21_stage_3_provisions_sp() {
			return r21_stage_3_provisions_sp;
		}

		public void setR21_stage_3_provisions_sp(BigDecimal r21_stage_3_provisions_sp) {
			this.r21_stage_3_provisions_sp = r21_stage_3_provisions_sp;
		}

		public BigDecimal getR21_total_specific_provisions() {
			return r21_total_specific_provisions;
		}

		public void setR21_total_specific_provisions(BigDecimal r21_total_specific_provisions) {
			this.r21_total_specific_provisions = r21_total_specific_provisions;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_qualifi_stage_2_provisions_sp() {
			return r22_qualifi_stage_2_provisions_sp;
		}

		public void setR22_qualifi_stage_2_provisions_sp(BigDecimal r22_qualifi_stage_2_provisions_sp) {
			this.r22_qualifi_stage_2_provisions_sp = r22_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR22_stage_3_provisions_sp() {
			return r22_stage_3_provisions_sp;
		}

		public void setR22_stage_3_provisions_sp(BigDecimal r22_stage_3_provisions_sp) {
			this.r22_stage_3_provisions_sp = r22_stage_3_provisions_sp;
		}

		public BigDecimal getR22_total_specific_provisions() {
			return r22_total_specific_provisions;
		}

		public void setR22_total_specific_provisions(BigDecimal r22_total_specific_provisions) {
			this.r22_total_specific_provisions = r22_total_specific_provisions;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_qualifi_stage_2_provisions_sp() {
			return r23_qualifi_stage_2_provisions_sp;
		}

		public void setR23_qualifi_stage_2_provisions_sp(BigDecimal r23_qualifi_stage_2_provisions_sp) {
			this.r23_qualifi_stage_2_provisions_sp = r23_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR23_stage_3_provisions_sp() {
			return r23_stage_3_provisions_sp;
		}

		public void setR23_stage_3_provisions_sp(BigDecimal r23_stage_3_provisions_sp) {
			this.r23_stage_3_provisions_sp = r23_stage_3_provisions_sp;
		}

		public BigDecimal getR23_total_specific_provisions() {
			return r23_total_specific_provisions;
		}

		public void setR23_total_specific_provisions(BigDecimal r23_total_specific_provisions) {
			this.r23_total_specific_provisions = r23_total_specific_provisions;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_qualifi_stage_2_provisions_sp() {
			return r24_qualifi_stage_2_provisions_sp;
		}

		public void setR24_qualifi_stage_2_provisions_sp(BigDecimal r24_qualifi_stage_2_provisions_sp) {
			this.r24_qualifi_stage_2_provisions_sp = r24_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR24_stage_3_provisions_sp() {
			return r24_stage_3_provisions_sp;
		}

		public void setR24_stage_3_provisions_sp(BigDecimal r24_stage_3_provisions_sp) {
			this.r24_stage_3_provisions_sp = r24_stage_3_provisions_sp;
		}

		public BigDecimal getR24_total_specific_provisions() {
			return r24_total_specific_provisions;
		}

		public void setR24_total_specific_provisions(BigDecimal r24_total_specific_provisions) {
			this.r24_total_specific_provisions = r24_total_specific_provisions;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_qualifi_stage_2_provisions_sp() {
			return r25_qualifi_stage_2_provisions_sp;
		}

		public void setR25_qualifi_stage_2_provisions_sp(BigDecimal r25_qualifi_stage_2_provisions_sp) {
			this.r25_qualifi_stage_2_provisions_sp = r25_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR25_stage_3_provisions_sp() {
			return r25_stage_3_provisions_sp;
		}

		public void setR25_stage_3_provisions_sp(BigDecimal r25_stage_3_provisions_sp) {
			this.r25_stage_3_provisions_sp = r25_stage_3_provisions_sp;
		}

		public BigDecimal getR25_total_specific_provisions() {
			return r25_total_specific_provisions;
		}

		public void setR25_total_specific_provisions(BigDecimal r25_total_specific_provisions) {
			this.r25_total_specific_provisions = r25_total_specific_provisions;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_qualifi_stage_2_provisions_sp() {
			return r26_qualifi_stage_2_provisions_sp;
		}

		public void setR26_qualifi_stage_2_provisions_sp(BigDecimal r26_qualifi_stage_2_provisions_sp) {
			this.r26_qualifi_stage_2_provisions_sp = r26_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR26_stage_3_provisions_sp() {
			return r26_stage_3_provisions_sp;
		}

		public void setR26_stage_3_provisions_sp(BigDecimal r26_stage_3_provisions_sp) {
			this.r26_stage_3_provisions_sp = r26_stage_3_provisions_sp;
		}

		public BigDecimal getR26_total_specific_provisions() {
			return r26_total_specific_provisions;
		}

		public void setR26_total_specific_provisions(BigDecimal r26_total_specific_provisions) {
			this.r26_total_specific_provisions = r26_total_specific_provisions;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_qualifi_stage_2_provisions_sp() {
			return r27_qualifi_stage_2_provisions_sp;
		}

		public void setR27_qualifi_stage_2_provisions_sp(BigDecimal r27_qualifi_stage_2_provisions_sp) {
			this.r27_qualifi_stage_2_provisions_sp = r27_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR27_stage_3_provisions_sp() {
			return r27_stage_3_provisions_sp;
		}

		public void setR27_stage_3_provisions_sp(BigDecimal r27_stage_3_provisions_sp) {
			this.r27_stage_3_provisions_sp = r27_stage_3_provisions_sp;
		}

		public BigDecimal getR27_total_specific_provisions() {
			return r27_total_specific_provisions;
		}

		public void setR27_total_specific_provisions(BigDecimal r27_total_specific_provisions) {
			this.r27_total_specific_provisions = r27_total_specific_provisions;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_qualifi_stage_2_provisions_sp() {
			return r28_qualifi_stage_2_provisions_sp;
		}

		public void setR28_qualifi_stage_2_provisions_sp(BigDecimal r28_qualifi_stage_2_provisions_sp) {
			this.r28_qualifi_stage_2_provisions_sp = r28_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR28_stage_3_provisions_sp() {
			return r28_stage_3_provisions_sp;
		}

		public void setR28_stage_3_provisions_sp(BigDecimal r28_stage_3_provisions_sp) {
			this.r28_stage_3_provisions_sp = r28_stage_3_provisions_sp;
		}

		public BigDecimal getR28_total_specific_provisions() {
			return r28_total_specific_provisions;
		}

		public void setR28_total_specific_provisions(BigDecimal r28_total_specific_provisions) {
			this.r28_total_specific_provisions = r28_total_specific_provisions;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_qualifi_stage_2_provisions_sp() {
			return r29_qualifi_stage_2_provisions_sp;
		}

		public void setR29_qualifi_stage_2_provisions_sp(BigDecimal r29_qualifi_stage_2_provisions_sp) {
			this.r29_qualifi_stage_2_provisions_sp = r29_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR29_stage_3_provisions_sp() {
			return r29_stage_3_provisions_sp;
		}

		public void setR29_stage_3_provisions_sp(BigDecimal r29_stage_3_provisions_sp) {
			this.r29_stage_3_provisions_sp = r29_stage_3_provisions_sp;
		}

		public BigDecimal getR29_total_specific_provisions() {
			return r29_total_specific_provisions;
		}

		public void setR29_total_specific_provisions(BigDecimal r29_total_specific_provisions) {
			this.r29_total_specific_provisions = r29_total_specific_provisions;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_qualifi_stage_2_provisions_sp() {
			return r30_qualifi_stage_2_provisions_sp;
		}

		public void setR30_qualifi_stage_2_provisions_sp(BigDecimal r30_qualifi_stage_2_provisions_sp) {
			this.r30_qualifi_stage_2_provisions_sp = r30_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR30_stage_3_provisions_sp() {
			return r30_stage_3_provisions_sp;
		}

		public void setR30_stage_3_provisions_sp(BigDecimal r30_stage_3_provisions_sp) {
			this.r30_stage_3_provisions_sp = r30_stage_3_provisions_sp;
		}

		public BigDecimal getR30_total_specific_provisions() {
			return r30_total_specific_provisions;
		}

		public void setR30_total_specific_provisions(BigDecimal r30_total_specific_provisions) {
			this.r30_total_specific_provisions = r30_total_specific_provisions;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_qualifi_stage_2_provisions_sp() {
			return r31_qualifi_stage_2_provisions_sp;
		}

		public void setR31_qualifi_stage_2_provisions_sp(BigDecimal r31_qualifi_stage_2_provisions_sp) {
			this.r31_qualifi_stage_2_provisions_sp = r31_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR31_stage_3_provisions_sp() {
			return r31_stage_3_provisions_sp;
		}

		public void setR31_stage_3_provisions_sp(BigDecimal r31_stage_3_provisions_sp) {
			this.r31_stage_3_provisions_sp = r31_stage_3_provisions_sp;
		}

		public BigDecimal getR31_total_specific_provisions() {
			return r31_total_specific_provisions;
		}

		public void setR31_total_specific_provisions(BigDecimal r31_total_specific_provisions) {
			this.r31_total_specific_provisions = r31_total_specific_provisions;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public BigDecimal getR32_qualifi_stage_2_provisions_sp() {
			return r32_qualifi_stage_2_provisions_sp;
		}

		public void setR32_qualifi_stage_2_provisions_sp(BigDecimal r32_qualifi_stage_2_provisions_sp) {
			this.r32_qualifi_stage_2_provisions_sp = r32_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR32_stage_3_provisions_sp() {
			return r32_stage_3_provisions_sp;
		}

		public void setR32_stage_3_provisions_sp(BigDecimal r32_stage_3_provisions_sp) {
			this.r32_stage_3_provisions_sp = r32_stage_3_provisions_sp;
		}

		public BigDecimal getR32_total_specific_provisions() {
			return r32_total_specific_provisions;
		}

		public void setR32_total_specific_provisions(BigDecimal r32_total_specific_provisions) {
			this.r32_total_specific_provisions = r32_total_specific_provisions;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_qualifi_stage_2_provisions_sp() {
			return r33_qualifi_stage_2_provisions_sp;
		}

		public void setR33_qualifi_stage_2_provisions_sp(BigDecimal r33_qualifi_stage_2_provisions_sp) {
			this.r33_qualifi_stage_2_provisions_sp = r33_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR33_stage_3_provisions_sp() {
			return r33_stage_3_provisions_sp;
		}

		public void setR33_stage_3_provisions_sp(BigDecimal r33_stage_3_provisions_sp) {
			this.r33_stage_3_provisions_sp = r33_stage_3_provisions_sp;
		}

		public BigDecimal getR33_total_specific_provisions() {
			return r33_total_specific_provisions;
		}

		public void setR33_total_specific_provisions(BigDecimal r33_total_specific_provisions) {
			this.r33_total_specific_provisions = r33_total_specific_provisions;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_qualifi_stage_2_provisions_sp() {
			return r34_qualifi_stage_2_provisions_sp;
		}

		public void setR34_qualifi_stage_2_provisions_sp(BigDecimal r34_qualifi_stage_2_provisions_sp) {
			this.r34_qualifi_stage_2_provisions_sp = r34_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR34_stage_3_provisions_sp() {
			return r34_stage_3_provisions_sp;
		}

		public void setR34_stage_3_provisions_sp(BigDecimal r34_stage_3_provisions_sp) {
			this.r34_stage_3_provisions_sp = r34_stage_3_provisions_sp;
		}

		public BigDecimal getR34_total_specific_provisions() {
			return r34_total_specific_provisions;
		}

		public void setR34_total_specific_provisions(BigDecimal r34_total_specific_provisions) {
			this.r34_total_specific_provisions = r34_total_specific_provisions;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public BigDecimal getR35_qualifi_stage_2_provisions_sp() {
			return r35_qualifi_stage_2_provisions_sp;
		}

		public void setR35_qualifi_stage_2_provisions_sp(BigDecimal r35_qualifi_stage_2_provisions_sp) {
			this.r35_qualifi_stage_2_provisions_sp = r35_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR35_stage_3_provisions_sp() {
			return r35_stage_3_provisions_sp;
		}

		public void setR35_stage_3_provisions_sp(BigDecimal r35_stage_3_provisions_sp) {
			this.r35_stage_3_provisions_sp = r35_stage_3_provisions_sp;
		}

		public BigDecimal getR35_total_specific_provisions() {
			return r35_total_specific_provisions;
		}

		public void setR35_total_specific_provisions(BigDecimal r35_total_specific_provisions) {
			this.r35_total_specific_provisions = r35_total_specific_provisions;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_qualifi_stage_2_provisions_sp() {
			return r36_qualifi_stage_2_provisions_sp;
		}

		public void setR36_qualifi_stage_2_provisions_sp(BigDecimal r36_qualifi_stage_2_provisions_sp) {
			this.r36_qualifi_stage_2_provisions_sp = r36_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR36_stage_3_provisions_sp() {
			return r36_stage_3_provisions_sp;
		}

		public void setR36_stage_3_provisions_sp(BigDecimal r36_stage_3_provisions_sp) {
			this.r36_stage_3_provisions_sp = r36_stage_3_provisions_sp;
		}

		public BigDecimal getR36_total_specific_provisions() {
			return r36_total_specific_provisions;
		}

		public void setR36_total_specific_provisions(BigDecimal r36_total_specific_provisions) {
			this.r36_total_specific_provisions = r36_total_specific_provisions;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public BigDecimal getR37_qualifi_stage_2_provisions_sp() {
			return r37_qualifi_stage_2_provisions_sp;
		}

		public void setR37_qualifi_stage_2_provisions_sp(BigDecimal r37_qualifi_stage_2_provisions_sp) {
			this.r37_qualifi_stage_2_provisions_sp = r37_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR37_stage_3_provisions_sp() {
			return r37_stage_3_provisions_sp;
		}

		public void setR37_stage_3_provisions_sp(BigDecimal r37_stage_3_provisions_sp) {
			this.r37_stage_3_provisions_sp = r37_stage_3_provisions_sp;
		}

		public BigDecimal getR37_total_specific_provisions() {
			return r37_total_specific_provisions;
		}

		public void setR37_total_specific_provisions(BigDecimal r37_total_specific_provisions) {
			this.r37_total_specific_provisions = r37_total_specific_provisions;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public BigDecimal getR38_qualifi_stage_2_provisions_sp() {
			return r38_qualifi_stage_2_provisions_sp;
		}

		public void setR38_qualifi_stage_2_provisions_sp(BigDecimal r38_qualifi_stage_2_provisions_sp) {
			this.r38_qualifi_stage_2_provisions_sp = r38_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR38_stage_3_provisions_sp() {
			return r38_stage_3_provisions_sp;
		}

		public void setR38_stage_3_provisions_sp(BigDecimal r38_stage_3_provisions_sp) {
			this.r38_stage_3_provisions_sp = r38_stage_3_provisions_sp;
		}

		public BigDecimal getR38_total_specific_provisions() {
			return r38_total_specific_provisions;
		}

		public void setR38_total_specific_provisions(BigDecimal r38_total_specific_provisions) {
			this.r38_total_specific_provisions = r38_total_specific_provisions;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public BigDecimal getR39_qualifi_stage_2_provisions_sp() {
			return r39_qualifi_stage_2_provisions_sp;
		}

		public void setR39_qualifi_stage_2_provisions_sp(BigDecimal r39_qualifi_stage_2_provisions_sp) {
			this.r39_qualifi_stage_2_provisions_sp = r39_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR39_stage_3_provisions_sp() {
			return r39_stage_3_provisions_sp;
		}

		public void setR39_stage_3_provisions_sp(BigDecimal r39_stage_3_provisions_sp) {
			this.r39_stage_3_provisions_sp = r39_stage_3_provisions_sp;
		}

		public BigDecimal getR39_total_specific_provisions() {
			return r39_total_specific_provisions;
		}

		public void setR39_total_specific_provisions(BigDecimal r39_total_specific_provisions) {
			this.r39_total_specific_provisions = r39_total_specific_provisions;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public BigDecimal getR40_qualifi_stage_2_provisions_sp() {
			return r40_qualifi_stage_2_provisions_sp;
		}

		public void setR40_qualifi_stage_2_provisions_sp(BigDecimal r40_qualifi_stage_2_provisions_sp) {
			this.r40_qualifi_stage_2_provisions_sp = r40_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR40_stage_3_provisions_sp() {
			return r40_stage_3_provisions_sp;
		}

		public void setR40_stage_3_provisions_sp(BigDecimal r40_stage_3_provisions_sp) {
			this.r40_stage_3_provisions_sp = r40_stage_3_provisions_sp;
		}

		public BigDecimal getR40_total_specific_provisions() {
			return r40_total_specific_provisions;
		}

		public void setR40_total_specific_provisions(BigDecimal r40_total_specific_provisions) {
			this.r40_total_specific_provisions = r40_total_specific_provisions;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_qualifi_stage_2_provisions_sp() {
			return r41_qualifi_stage_2_provisions_sp;
		}

		public void setR41_qualifi_stage_2_provisions_sp(BigDecimal r41_qualifi_stage_2_provisions_sp) {
			this.r41_qualifi_stage_2_provisions_sp = r41_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR41_stage_3_provisions_sp() {
			return r41_stage_3_provisions_sp;
		}

		public void setR41_stage_3_provisions_sp(BigDecimal r41_stage_3_provisions_sp) {
			this.r41_stage_3_provisions_sp = r41_stage_3_provisions_sp;
		}

		public BigDecimal getR41_total_specific_provisions() {
			return r41_total_specific_provisions;
		}

		public void setR41_total_specific_provisions(BigDecimal r41_total_specific_provisions) {
			this.r41_total_specific_provisions = r41_total_specific_provisions;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_qualifi_stage_2_provisions_sp() {
			return r42_qualifi_stage_2_provisions_sp;
		}

		public void setR42_qualifi_stage_2_provisions_sp(BigDecimal r42_qualifi_stage_2_provisions_sp) {
			this.r42_qualifi_stage_2_provisions_sp = r42_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR42_stage_3_provisions_sp() {
			return r42_stage_3_provisions_sp;
		}

		public void setR42_stage_3_provisions_sp(BigDecimal r42_stage_3_provisions_sp) {
			this.r42_stage_3_provisions_sp = r42_stage_3_provisions_sp;
		}

		public BigDecimal getR42_total_specific_provisions() {
			return r42_total_specific_provisions;
		}

		public void setR42_total_specific_provisions(BigDecimal r42_total_specific_provisions) {
			this.r42_total_specific_provisions = r42_total_specific_provisions;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_qualifi_stage_2_provisions_sp() {
			return r43_qualifi_stage_2_provisions_sp;
		}

		public void setR43_qualifi_stage_2_provisions_sp(BigDecimal r43_qualifi_stage_2_provisions_sp) {
			this.r43_qualifi_stage_2_provisions_sp = r43_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR43_stage_3_provisions_sp() {
			return r43_stage_3_provisions_sp;
		}

		public void setR43_stage_3_provisions_sp(BigDecimal r43_stage_3_provisions_sp) {
			this.r43_stage_3_provisions_sp = r43_stage_3_provisions_sp;
		}

		public BigDecimal getR43_total_specific_provisions() {
			return r43_total_specific_provisions;
		}

		public void setR43_total_specific_provisions(BigDecimal r43_total_specific_provisions) {
			this.r43_total_specific_provisions = r43_total_specific_provisions;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_qualifi_stage_2_provisions_sp() {
			return r44_qualifi_stage_2_provisions_sp;
		}

		public void setR44_qualifi_stage_2_provisions_sp(BigDecimal r44_qualifi_stage_2_provisions_sp) {
			this.r44_qualifi_stage_2_provisions_sp = r44_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR44_stage_3_provisions_sp() {
			return r44_stage_3_provisions_sp;
		}

		public void setR44_stage_3_provisions_sp(BigDecimal r44_stage_3_provisions_sp) {
			this.r44_stage_3_provisions_sp = r44_stage_3_provisions_sp;
		}

		public BigDecimal getR44_total_specific_provisions() {
			return r44_total_specific_provisions;
		}

		public void setR44_total_specific_provisions(BigDecimal r44_total_specific_provisions) {
			this.r44_total_specific_provisions = r44_total_specific_provisions;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_qualifi_stage_2_provisions_sp() {
			return r45_qualifi_stage_2_provisions_sp;
		}

		public void setR45_qualifi_stage_2_provisions_sp(BigDecimal r45_qualifi_stage_2_provisions_sp) {
			this.r45_qualifi_stage_2_provisions_sp = r45_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR45_stage_3_provisions_sp() {
			return r45_stage_3_provisions_sp;
		}

		public void setR45_stage_3_provisions_sp(BigDecimal r45_stage_3_provisions_sp) {
			this.r45_stage_3_provisions_sp = r45_stage_3_provisions_sp;
		}

		public BigDecimal getR45_total_specific_provisions() {
			return r45_total_specific_provisions;
		}

		public void setR45_total_specific_provisions(BigDecimal r45_total_specific_provisions) {
			this.r45_total_specific_provisions = r45_total_specific_provisions;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_qualifi_stage_2_provisions_sp() {
			return r46_qualifi_stage_2_provisions_sp;
		}

		public void setR46_qualifi_stage_2_provisions_sp(BigDecimal r46_qualifi_stage_2_provisions_sp) {
			this.r46_qualifi_stage_2_provisions_sp = r46_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR46_stage_3_provisions_sp() {
			return r46_stage_3_provisions_sp;
		}

		public void setR46_stage_3_provisions_sp(BigDecimal r46_stage_3_provisions_sp) {
			this.r46_stage_3_provisions_sp = r46_stage_3_provisions_sp;
		}

		public BigDecimal getR46_total_specific_provisions() {
			return r46_total_specific_provisions;
		}

		public void setR46_total_specific_provisions(BigDecimal r46_total_specific_provisions) {
			this.r46_total_specific_provisions = r46_total_specific_provisions;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_qualifi_stage_2_provisions_sp() {
			return r47_qualifi_stage_2_provisions_sp;
		}

		public void setR47_qualifi_stage_2_provisions_sp(BigDecimal r47_qualifi_stage_2_provisions_sp) {
			this.r47_qualifi_stage_2_provisions_sp = r47_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR47_stage_3_provisions_sp() {
			return r47_stage_3_provisions_sp;
		}

		public void setR47_stage_3_provisions_sp(BigDecimal r47_stage_3_provisions_sp) {
			this.r47_stage_3_provisions_sp = r47_stage_3_provisions_sp;
		}

		public BigDecimal getR47_total_specific_provisions() {
			return r47_total_specific_provisions;
		}

		public void setR47_total_specific_provisions(BigDecimal r47_total_specific_provisions) {
			this.r47_total_specific_provisions = r47_total_specific_provisions;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_qualifi_stage_2_provisions_sp() {
			return r48_qualifi_stage_2_provisions_sp;
		}

		public void setR48_qualifi_stage_2_provisions_sp(BigDecimal r48_qualifi_stage_2_provisions_sp) {
			this.r48_qualifi_stage_2_provisions_sp = r48_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR48_stage_3_provisions_sp() {
			return r48_stage_3_provisions_sp;
		}

		public void setR48_stage_3_provisions_sp(BigDecimal r48_stage_3_provisions_sp) {
			this.r48_stage_3_provisions_sp = r48_stage_3_provisions_sp;
		}

		public BigDecimal getR48_total_specific_provisions() {
			return r48_total_specific_provisions;
		}

		public void setR48_total_specific_provisions(BigDecimal r48_total_specific_provisions) {
			this.r48_total_specific_provisions = r48_total_specific_provisions;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_qualifi_stage_2_provisions_sp() {
			return r49_qualifi_stage_2_provisions_sp;
		}

		public void setR49_qualifi_stage_2_provisions_sp(BigDecimal r49_qualifi_stage_2_provisions_sp) {
			this.r49_qualifi_stage_2_provisions_sp = r49_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR49_stage_3_provisions_sp() {
			return r49_stage_3_provisions_sp;
		}

		public void setR49_stage_3_provisions_sp(BigDecimal r49_stage_3_provisions_sp) {
			this.r49_stage_3_provisions_sp = r49_stage_3_provisions_sp;
		}

		public BigDecimal getR49_total_specific_provisions() {
			return r49_total_specific_provisions;
		}

		public void setR49_total_specific_provisions(BigDecimal r49_total_specific_provisions) {
			this.r49_total_specific_provisions = r49_total_specific_provisions;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public BigDecimal getR50_qualifi_stage_2_provisions_sp() {
			return r50_qualifi_stage_2_provisions_sp;
		}

		public void setR50_qualifi_stage_2_provisions_sp(BigDecimal r50_qualifi_stage_2_provisions_sp) {
			this.r50_qualifi_stage_2_provisions_sp = r50_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR50_stage_3_provisions_sp() {
			return r50_stage_3_provisions_sp;
		}

		public void setR50_stage_3_provisions_sp(BigDecimal r50_stage_3_provisions_sp) {
			this.r50_stage_3_provisions_sp = r50_stage_3_provisions_sp;
		}

		public BigDecimal getR50_total_specific_provisions() {
			return r50_total_specific_provisions;
		}

		public void setR50_total_specific_provisions(BigDecimal r50_total_specific_provisions) {
			this.r50_total_specific_provisions = r50_total_specific_provisions;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public BigDecimal getR51_qualifi_stage_2_provisions_sp() {
			return r51_qualifi_stage_2_provisions_sp;
		}

		public void setR51_qualifi_stage_2_provisions_sp(BigDecimal r51_qualifi_stage_2_provisions_sp) {
			this.r51_qualifi_stage_2_provisions_sp = r51_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR51_stage_3_provisions_sp() {
			return r51_stage_3_provisions_sp;
		}

		public void setR51_stage_3_provisions_sp(BigDecimal r51_stage_3_provisions_sp) {
			this.r51_stage_3_provisions_sp = r51_stage_3_provisions_sp;
		}

		public BigDecimal getR51_total_specific_provisions() {
			return r51_total_specific_provisions;
		}

		public void setR51_total_specific_provisions(BigDecimal r51_total_specific_provisions) {
			this.r51_total_specific_provisions = r51_total_specific_provisions;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public BigDecimal getR52_qualifi_stage_2_provisions_sp() {
			return r52_qualifi_stage_2_provisions_sp;
		}

		public void setR52_qualifi_stage_2_provisions_sp(BigDecimal r52_qualifi_stage_2_provisions_sp) {
			this.r52_qualifi_stage_2_provisions_sp = r52_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR52_stage_3_provisions_sp() {
			return r52_stage_3_provisions_sp;
		}

		public void setR52_stage_3_provisions_sp(BigDecimal r52_stage_3_provisions_sp) {
			this.r52_stage_3_provisions_sp = r52_stage_3_provisions_sp;
		}

		public BigDecimal getR52_total_specific_provisions() {
			return r52_total_specific_provisions;
		}

		public void setR52_total_specific_provisions(BigDecimal r52_total_specific_provisions) {
			this.r52_total_specific_provisions = r52_total_specific_provisions;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public BigDecimal getR53_qualifi_stage_2_provisions_sp() {
			return r53_qualifi_stage_2_provisions_sp;
		}

		public void setR53_qualifi_stage_2_provisions_sp(BigDecimal r53_qualifi_stage_2_provisions_sp) {
			this.r53_qualifi_stage_2_provisions_sp = r53_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR53_stage_3_provisions_sp() {
			return r53_stage_3_provisions_sp;
		}

		public void setR53_stage_3_provisions_sp(BigDecimal r53_stage_3_provisions_sp) {
			this.r53_stage_3_provisions_sp = r53_stage_3_provisions_sp;
		}

		public BigDecimal getR53_total_specific_provisions() {
			return r53_total_specific_provisions;
		}

		public void setR53_total_specific_provisions(BigDecimal r53_total_specific_provisions) {
			this.r53_total_specific_provisions = r53_total_specific_provisions;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public BigDecimal getR54_qualifi_stage_2_provisions_sp() {
			return r54_qualifi_stage_2_provisions_sp;
		}

		public void setR54_qualifi_stage_2_provisions_sp(BigDecimal r54_qualifi_stage_2_provisions_sp) {
			this.r54_qualifi_stage_2_provisions_sp = r54_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR54_stage_3_provisions_sp() {
			return r54_stage_3_provisions_sp;
		}

		public void setR54_stage_3_provisions_sp(BigDecimal r54_stage_3_provisions_sp) {
			this.r54_stage_3_provisions_sp = r54_stage_3_provisions_sp;
		}

		public BigDecimal getR54_total_specific_provisions() {
			return r54_total_specific_provisions;
		}

		public void setR54_total_specific_provisions(BigDecimal r54_total_specific_provisions) {
			this.r54_total_specific_provisions = r54_total_specific_provisions;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public BigDecimal getR55_qualifi_stage_2_provisions_sp() {
			return r55_qualifi_stage_2_provisions_sp;
		}

		public void setR55_qualifi_stage_2_provisions_sp(BigDecimal r55_qualifi_stage_2_provisions_sp) {
			this.r55_qualifi_stage_2_provisions_sp = r55_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR55_stage_3_provisions_sp() {
			return r55_stage_3_provisions_sp;
		}

		public void setR55_stage_3_provisions_sp(BigDecimal r55_stage_3_provisions_sp) {
			this.r55_stage_3_provisions_sp = r55_stage_3_provisions_sp;
		}

		public BigDecimal getR55_total_specific_provisions() {
			return r55_total_specific_provisions;
		}

		public void setR55_total_specific_provisions(BigDecimal r55_total_specific_provisions) {
			this.r55_total_specific_provisions = r55_total_specific_provisions;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public BigDecimal getR56_qualifi_stage_2_provisions_sp() {
			return r56_qualifi_stage_2_provisions_sp;
		}

		public void setR56_qualifi_stage_2_provisions_sp(BigDecimal r56_qualifi_stage_2_provisions_sp) {
			this.r56_qualifi_stage_2_provisions_sp = r56_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR56_stage_3_provisions_sp() {
			return r56_stage_3_provisions_sp;
		}

		public void setR56_stage_3_provisions_sp(BigDecimal r56_stage_3_provisions_sp) {
			this.r56_stage_3_provisions_sp = r56_stage_3_provisions_sp;
		}

		public BigDecimal getR56_total_specific_provisions() {
			return r56_total_specific_provisions;
		}

		public void setR56_total_specific_provisions(BigDecimal r56_total_specific_provisions) {
			this.r56_total_specific_provisions = r56_total_specific_provisions;
		}

		public String getR57_product() {
			return r57_product;
		}

		public void setR57_product(String r57_product) {
			this.r57_product = r57_product;
		}

		public BigDecimal getR57_qualifi_stage_2_provisions_sp() {
			return r57_qualifi_stage_2_provisions_sp;
		}

		public void setR57_qualifi_stage_2_provisions_sp(BigDecimal r57_qualifi_stage_2_provisions_sp) {
			this.r57_qualifi_stage_2_provisions_sp = r57_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR57_stage_3_provisions_sp() {
			return r57_stage_3_provisions_sp;
		}

		public void setR57_stage_3_provisions_sp(BigDecimal r57_stage_3_provisions_sp) {
			this.r57_stage_3_provisions_sp = r57_stage_3_provisions_sp;
		}

		public BigDecimal getR57_total_specific_provisions() {
			return r57_total_specific_provisions;
		}

		public void setR57_total_specific_provisions(BigDecimal r57_total_specific_provisions) {
			this.r57_total_specific_provisions = r57_total_specific_provisions;
		}

		public String getR58_product() {
			return r58_product;
		}

		public void setR58_product(String r58_product) {
			this.r58_product = r58_product;
		}

		public BigDecimal getR58_qualifi_stage_2_provisions_sp() {
			return r58_qualifi_stage_2_provisions_sp;
		}

		public void setR58_qualifi_stage_2_provisions_sp(BigDecimal r58_qualifi_stage_2_provisions_sp) {
			this.r58_qualifi_stage_2_provisions_sp = r58_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR58_stage_3_provisions_sp() {
			return r58_stage_3_provisions_sp;
		}

		public void setR58_stage_3_provisions_sp(BigDecimal r58_stage_3_provisions_sp) {
			this.r58_stage_3_provisions_sp = r58_stage_3_provisions_sp;
		}

		public BigDecimal getR58_total_specific_provisions() {
			return r58_total_specific_provisions;
		}

		public void setR58_total_specific_provisions(BigDecimal r58_total_specific_provisions) {
			this.r58_total_specific_provisions = r58_total_specific_provisions;
		}

		public String getR59_product() {
			return r59_product;
		}

		public void setR59_product(String r59_product) {
			this.r59_product = r59_product;
		}

		public BigDecimal getR59_qualifi_stage_2_provisions_sp() {
			return r59_qualifi_stage_2_provisions_sp;
		}

		public void setR59_qualifi_stage_2_provisions_sp(BigDecimal r59_qualifi_stage_2_provisions_sp) {
			this.r59_qualifi_stage_2_provisions_sp = r59_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR59_stage_3_provisions_sp() {
			return r59_stage_3_provisions_sp;
		}

		public void setR59_stage_3_provisions_sp(BigDecimal r59_stage_3_provisions_sp) {
			this.r59_stage_3_provisions_sp = r59_stage_3_provisions_sp;
		}

		public BigDecimal getR59_total_specific_provisions() {
			return r59_total_specific_provisions;
		}

		public void setR59_total_specific_provisions(BigDecimal r59_total_specific_provisions) {
			this.r59_total_specific_provisions = r59_total_specific_provisions;
		}

		public String getR60_product() {
			return r60_product;
		}

		public void setR60_product(String r60_product) {
			this.r60_product = r60_product;
		}

		public BigDecimal getR60_qualifi_stage_2_provisions_sp() {
			return r60_qualifi_stage_2_provisions_sp;
		}

		public void setR60_qualifi_stage_2_provisions_sp(BigDecimal r60_qualifi_stage_2_provisions_sp) {
			this.r60_qualifi_stage_2_provisions_sp = r60_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR60_stage_3_provisions_sp() {
			return r60_stage_3_provisions_sp;
		}

		public void setR60_stage_3_provisions_sp(BigDecimal r60_stage_3_provisions_sp) {
			this.r60_stage_3_provisions_sp = r60_stage_3_provisions_sp;
		}

		public BigDecimal getR60_total_specific_provisions() {
			return r60_total_specific_provisions;
		}

		public void setR60_total_specific_provisions(BigDecimal r60_total_specific_provisions) {
			this.r60_total_specific_provisions = r60_total_specific_provisions;
		}

		public String getR61_product() {
			return r61_product;
		}

		public void setR61_product(String r61_product) {
			this.r61_product = r61_product;
		}

		public BigDecimal getR61_qualifi_stage_2_provisions_sp() {
			return r61_qualifi_stage_2_provisions_sp;
		}

		public void setR61_qualifi_stage_2_provisions_sp(BigDecimal r61_qualifi_stage_2_provisions_sp) {
			this.r61_qualifi_stage_2_provisions_sp = r61_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR61_stage_3_provisions_sp() {
			return r61_stage_3_provisions_sp;
		}

		public void setR61_stage_3_provisions_sp(BigDecimal r61_stage_3_provisions_sp) {
			this.r61_stage_3_provisions_sp = r61_stage_3_provisions_sp;
		}

		public BigDecimal getR61_total_specific_provisions() {
			return r61_total_specific_provisions;
		}

		public void setR61_total_specific_provisions(BigDecimal r61_total_specific_provisions) {
			this.r61_total_specific_provisions = r61_total_specific_provisions;
		}

		public String getR62_product() {
			return r62_product;
		}

		public void setR62_product(String r62_product) {
			this.r62_product = r62_product;
		}

		public BigDecimal getR62_qualifi_stage_2_provisions_sp() {
			return r62_qualifi_stage_2_provisions_sp;
		}

		public void setR62_qualifi_stage_2_provisions_sp(BigDecimal r62_qualifi_stage_2_provisions_sp) {
			this.r62_qualifi_stage_2_provisions_sp = r62_qualifi_stage_2_provisions_sp;
		}

		public BigDecimal getR62_stage_3_provisions_sp() {
			return r62_stage_3_provisions_sp;
		}

		public void setR62_stage_3_provisions_sp(BigDecimal r62_stage_3_provisions_sp) {
			this.r62_stage_3_provisions_sp = r62_stage_3_provisions_sp;
		}

		public BigDecimal getR62_total_specific_provisions() {
			return r62_total_specific_provisions;
		}

		public void setR62_total_specific_provisions(BigDecimal r62_total_specific_provisions) {
			this.r62_total_specific_provisions = r62_total_specific_provisions;
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
	}
}
