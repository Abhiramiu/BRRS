package com.bornfire.brrs.services;

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
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_LCR_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LCR_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_LCR_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LCR_Summary_Repo;
import com.bornfire.brrs.entities.M_LCR_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_LCR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LCR_Detail_Entity;
import com.bornfire.brrs.entities.M_LCR_Summary_Entity;

@Component
@Service

public class BRRS_M_LCR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LCR_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_LCR_Detail_Repo BRRS_M_LCR_Detail_Repo;

	@Autowired
	BRRS_M_LCR_Summary_Repo BRRS_M_LCR_Summary_Repo;

	@Autowired
	BRRS_M_LCR_Archival_Detail_Repo BRRS_M_LCR_Archival_Detail_Repo;

	@Autowired
	BRRS_M_LCR_Archival_Summary_Repo BRRS_M_LCR_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_LCRView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		System.out.println("testing");
		System.out.println(version);

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_LCR_Archival_Summary_Entity> T1Master = new ArrayList<M_LCR_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_LCR_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),
						version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {

			List<M_LCR_Summary_Entity> T1Master = new ArrayList<M_LCR_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_LCR_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_LCR");

		// mv.addObject("reportsummary", T1Master);
		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;
	}

	public ModelAndView getM_LCRcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();
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
			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<M_LCR_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_LCR_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate,
							version);
				} else {
					T1Dt1 = BRRS_M_LCR_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_LCR_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_LCR_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = BRRS_M_LCR_Detail_Repo.getdatabydateList(parsedDate);
					totalPages = BRRS_M_LCR_Detail_Repo.getdatacount(parsedDate);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				
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
		mv.setViewName("BRRS/M_LCR");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	public byte[] getM_LCRExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_LCRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<M_LCR_Summary_Entity> dataList = BRRS_M_LCR_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LCR report. Returning empty result.");
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
					M_LCR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R12
					// Column E
					Cell cell4 = row.getCell(4);
					if (record.getR12_bob_total_amount() != null) {
						cell4.setCellValue(record.getR12_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					
					// R13
					// Column E
					cell4 = row.getCell(4);
					if (record.getR13_bob_total_amount() != null) {
						cell4.setCellValue(record.getR13_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(13);
					
					// R14
					// Column E
					cell4 = row.getCell(4);
					if (record.getR14_bob_total_amount() != null) {
						cell4.setCellValue(record.getR14_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(14);
					// R15
					// Column E
					cell4 = row.getCell(4);
					if (record.getR15_bob_total_amount() != null) {
						cell4.setCellValue(record.getR15_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(19);
					// R20
					// Column E
					cell4 = row.getCell(4);
					if (record.getR20_bob_total_amount() != null) {
						cell4.setCellValue(record.getR20_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(20);
					// R21
					// Column E
					cell4 = row.getCell(4);
					if (record.getR21_bob_total_amount() != null) {
						cell4.setCellValue(record.getR21_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(30);
					// R31
					// Column E
					cell4 = row.getCell(4);
					if (record.getR31_bob_total_amount() != null) {
						cell4.setCellValue(record.getR31_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(33);
					// R34
					// Column E
					cell4 = row.getCell(4);
					if (record.getR34_bob_total_amount() != null) {
						cell4.setCellValue(record.getR34_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(34);
					// R35
					// Column E
					cell4 = row.getCell(4);
					if (record.getR35_bob_total_amount() != null) {
						cell4.setCellValue(record.getR35_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(35);
					// R36
					// Column E
					cell4 = row.getCell(4);
					if (record.getR36_bob_total_amount() != null) {
						cell4.setCellValue(record.getR36_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(36);
					// R37
					// Column E
					cell4 = row.getCell(4);
					if (record.getR37_bob_total_amount() != null) {
						cell4.setCellValue(record.getR37_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(37);
					// R38
					// Column E
					cell4 = row.getCell(4);
					if (record.getR38_bob_total_amount() != null) {
						cell4.setCellValue(record.getR38_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(39);
					// R40
					// Column E
					cell4 = row.getCell(4);
					if (record.getR40_bob_total_amount() != null) {
						cell4.setCellValue(record.getR40_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(40);
					// R41
					// Column E
					cell4 = row.getCell(4);
					if (record.getR41_bob_total_amount() != null) {
						cell4.setCellValue(record.getR41_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(41);
					// R42
					// Column E
					cell4 = row.getCell(4);
					if (record.getR42_bob_total_amount() != null) {
						cell4.setCellValue(record.getR42_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(42);
					// R43
					// Column E
					cell4 = row.getCell(4);
					if (record.getR43_bob_total_amount() != null) {
						cell4.setCellValue(record.getR43_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(44);
					// R45
					// Column E
					cell4 = row.getCell(4);
					if (record.getR45_bob_total_amount() != null) {
						cell4.setCellValue(record.getR45_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(45);
					// R46
					// Column E
					cell4 = row.getCell(4);
					if (record.getR46_bob_total_amount() != null) {
						cell4.setCellValue(record.getR46_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(46);
					// R47
					// Column E
					cell4 = row.getCell(4);
					if (record.getR47_bob_total_amount() != null) {
						cell4.setCellValue(record.getR47_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(47);
					// R48
					// Column E
					cell4 = row.getCell(4);
					if (record.getR48_bob_total_amount() != null) {
						cell4.setCellValue(record.getR48_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(48);
					// R49
					// Column E
					cell4 = row.getCell(4);
					if (record.getR49_bob_total_amount() != null) {
						cell4.setCellValue(record.getR49_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(49);
					// R50
					// Column E
					cell4 = row.getCell(4);
					if (record.getR50_bob_total_amount() != null) {
						cell4.setCellValue(record.getR50_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(50);
					// R51
					// Column E
					cell4 = row.getCell(4);
					if (record.getR51_bob_total_amount() != null) {
						cell4.setCellValue(record.getR51_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(51);
					// R52
					// Column E
					cell4 = row.getCell(4);
					if (record.getR52_bob_total_amount() != null) {
						cell4.setCellValue(record.getR52_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(52);
					// R53
					// Column E
					cell4 = row.getCell(4);
					if (record.getR53_bob_total_amount() != null) {
						cell4.setCellValue(record.getR53_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(54);
					// R55
					// Column E
					cell4 = row.getCell(4);
					if (record.getR55_bob_total_amount() != null) {
						cell4.setCellValue(record.getR55_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(55);
					// R56
					// Column E
					cell4 = row.getCell(4);
					if (record.getR56_bob_total_amount() != null) {
						cell4.setCellValue(record.getR56_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(56);
					// R57
					// Column E
					cell4 = row.getCell(4);
					if (record.getR57_bob_total_amount() != null) {
						cell4.setCellValue(record.getR57_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(57);
					// R58
					// Column E
					cell4 = row.getCell(4);
					if (record.getR58_bob_total_amount() != null) {
						cell4.setCellValue(record.getR58_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(58);
					// R59
					// Column E
					cell4 = row.getCell(4);
					if (record.getR59_bob_total_amount() != null) {
						cell4.setCellValue(record.getR59_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(59);
					// R60
					// Column E
					cell4 = row.getCell(4);
					if (record.getR60_bob_total_amount() != null) {
						cell4.setCellValue(record.getR60_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(60);
					// R61
					// Column E
					cell4 = row.getCell(4);
					if (record.getR61_bob_total_amount() != null) {
						cell4.setCellValue(record.getR61_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(61);
					// R62
					// Column E
					cell4 = row.getCell(4);
					if (record.getR62_bob_total_amount() != null) {
						cell4.setCellValue(record.getR62_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(52);
					// R63
					// Column E
					cell4 = row.getCell(4);
					if (record.getR63_bob_total_amount() != null) {
						cell4.setCellValue(record.getR63_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(64);
					// R65
					// Column E
					cell4 = row.getCell(4);
					if (record.getR65_bob_total_amount() != null) {
						cell4.setCellValue(record.getR65_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(65);
					// R66
					// Column E
					cell4 = row.getCell(4);
					if (record.getR66_bob_total_amount() != null) {
						cell4.setCellValue(record.getR66_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(66);
					// R67
					// Column E
					cell4 = row.getCell(4);
					if (record.getR67_bob_total_amount() != null) {
						cell4.setCellValue(record.getR67_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(67);
					// R68
					// Column E
					cell4 = row.getCell(4);
					if (record.getR68_bob_total_amount() != null) {
						cell4.setCellValue(record.getR68_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(68);
					// R69
					// Column E
					cell4 = row.getCell(4);
					if (record.getR69_bob_total_amount() != null) {
						cell4.setCellValue(record.getR69_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(73);
					// R74
					// Column E
					cell4 = row.getCell(4);
					if (record.getR74_bob_total_amount() != null) {
						cell4.setCellValue(record.getR74_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(74);
					// R75
					// Column E
					cell4 = row.getCell(4);
					if (record.getR75_bob_total_amount() != null) {
						cell4.setCellValue(record.getR75_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(75);
					// R76
					// Column E
					cell4 = row.getCell(4);
					if (record.getR76_bob_total_amount() != null) {
						cell4.setCellValue(record.getR76_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(76);
					// R77
					// Column E
					cell4 = row.getCell(4);
					if (record.getR77_bob_total_amount() != null) {
						cell4.setCellValue(record.getR77_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(77);
					// R78
					// Column E
					cell4 = row.getCell(4);
					if (record.getR78_bob_total_amount() != null) {
						cell4.setCellValue(record.getR78_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(78);
					// R79
					// Column E
					cell4 = row.getCell(4);
					if (record.getR79_bob_total_amount() != null) {
						cell4.setCellValue(record.getR79_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(80);
					// R81
					// Column E
					cell4 = row.getCell(4);
					if (record.getR81_bob_total_amount() != null) {
						cell4.setCellValue(record.getR81_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(81);
					// R82
					// Column E
					cell4 = row.getCell(4);
					if (record.getR82_bob_total_amount() != null) {
						cell4.setCellValue(record.getR82_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(82);
					// R83
					// Column E
					cell4 = row.getCell(4);
					if (record.getR83_bob_total_amount() != null) {
						cell4.setCellValue(record.getR83_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(83);
					// R84
					// Column E
					cell4 = row.getCell(4);
					if (record.getR84_bob_total_amount() != null) {
						cell4.setCellValue(record.getR84_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(84);
					// R74
					// Column E
					cell4 = row.getCell(4);
					if (record.getR85_bob_total_amount() != null) {
						cell4.setCellValue(record.getR85_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(87);
					// R88
					// Column E
					cell4 = row.getCell(4);
					if (record.getR88_bob_total_amount() != null) {
						cell4.setCellValue(record.getR88_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(88);
					// R89
					// Column E
					cell4 = row.getCell(4);
					if (record.getR89_bob_total_amount() != null) {
						cell4.setCellValue(record.getR89_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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

	public byte[] getM_LCRDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_LCR Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LCRDetails");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCOUNT BALANCE IN PULA", "ROWID", "COLUMNID",
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
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_LCR_Detail_Entity> reportData = BRRS_M_LCR_Detail_Repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LCR_Detail_Entity item : reportData) {
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

					row.createCell(4).setCellValue(item.getReportLable());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
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
				logger.info("No data found for M_LCR — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_LCR Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getM_LCRArchival() {
		List<Object> M_LCRArchivallist = new ArrayList<>();
		try {
			M_LCRArchivallist = BRRS_M_LCR_Archival_Summary_Repo.getM_LCRarchival();
			System.out.println("countser" + M_LCRArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_LCR Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_LCRArchivallist;
	}

	public byte[] getExcelM_LCRARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_LCR_Archival_Summary_Entity> dataList = BRRS_M_LCR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LCR report. Returning empty result.");
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
					M_LCR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R12
					// Column E
					Cell cell4 = row.getCell(4);
					if (record.getR12_bob_total_amount() != null) {
						cell4.setCellValue(record.getR12_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					
					// R13
					// Column E
					cell4 = row.getCell(4);
					if (record.getR13_bob_total_amount() != null) {
						cell4.setCellValue(record.getR13_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(13);
					
					// R14
					// Column E
					cell4 = row.getCell(4);
					if (record.getR14_bob_total_amount() != null) {
						cell4.setCellValue(record.getR14_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(14);
					// R15
					// Column E
					cell4 = row.getCell(4);
					if (record.getR15_bob_total_amount() != null) {
						cell4.setCellValue(record.getR15_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(19);
					// R20
					// Column E
					cell4 = row.getCell(4);
					if (record.getR20_bob_total_amount() != null) {
						cell4.setCellValue(record.getR20_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(20);
					// R21
					// Column E
					cell4 = row.getCell(4);
					if (record.getR21_bob_total_amount() != null) {
						cell4.setCellValue(record.getR21_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(30);
					// R31
					// Column E
					cell4 = row.getCell(4);
					if (record.getR31_bob_total_amount() != null) {
						cell4.setCellValue(record.getR31_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(33);
					// R34
					// Column E
					cell4 = row.getCell(4);
					if (record.getR34_bob_total_amount() != null) {
						cell4.setCellValue(record.getR34_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(34);
					// R35
					// Column E
					cell4 = row.getCell(4);
					if (record.getR35_bob_total_amount() != null) {
						cell4.setCellValue(record.getR35_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(35);
					// R36
					// Column E
					cell4 = row.getCell(4);
					if (record.getR36_bob_total_amount() != null) {
						cell4.setCellValue(record.getR36_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(36);
					// R37
					// Column E
					cell4 = row.getCell(4);
					if (record.getR37_bob_total_amount() != null) {
						cell4.setCellValue(record.getR37_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(37);
					// R38
					// Column E
					cell4 = row.getCell(4);
					if (record.getR38_bob_total_amount() != null) {
						cell4.setCellValue(record.getR38_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(39);
					// R40
					// Column E
					cell4 = row.getCell(4);
					if (record.getR40_bob_total_amount() != null) {
						cell4.setCellValue(record.getR40_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(40);
					// R41
					// Column E
					cell4 = row.getCell(4);
					if (record.getR41_bob_total_amount() != null) {
						cell4.setCellValue(record.getR41_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(41);
					// R42
					// Column E
					cell4 = row.getCell(4);
					if (record.getR42_bob_total_amount() != null) {
						cell4.setCellValue(record.getR42_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(42);
					// R43
					// Column E
					cell4 = row.getCell(4);
					if (record.getR43_bob_total_amount() != null) {
						cell4.setCellValue(record.getR43_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(44);
					// R45
					// Column E
					cell4 = row.getCell(4);
					if (record.getR45_bob_total_amount() != null) {
						cell4.setCellValue(record.getR45_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(45);
					// R46
					// Column E
					cell4 = row.getCell(4);
					if (record.getR46_bob_total_amount() != null) {
						cell4.setCellValue(record.getR46_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(46);
					// R47
					// Column E
					cell4 = row.getCell(4);
					if (record.getR47_bob_total_amount() != null) {
						cell4.setCellValue(record.getR47_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(47);
					// R48
					// Column E
					cell4 = row.getCell(4);
					if (record.getR48_bob_total_amount() != null) {
						cell4.setCellValue(record.getR48_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(48);
					// R49
					// Column E
					cell4 = row.getCell(4);
					if (record.getR49_bob_total_amount() != null) {
						cell4.setCellValue(record.getR49_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(49);
					// R50
					// Column E
					cell4 = row.getCell(4);
					if (record.getR50_bob_total_amount() != null) {
						cell4.setCellValue(record.getR50_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(50);
					// R51
					// Column E
					cell4 = row.getCell(4);
					if (record.getR51_bob_total_amount() != null) {
						cell4.setCellValue(record.getR51_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(51);
					// R52
					// Column E
					cell4 = row.getCell(4);
					if (record.getR52_bob_total_amount() != null) {
						cell4.setCellValue(record.getR52_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(52);
					// R53
					// Column E
					cell4 = row.getCell(4);
					if (record.getR53_bob_total_amount() != null) {
						cell4.setCellValue(record.getR53_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(54);
					// R55
					// Column E
					cell4 = row.getCell(4);
					if (record.getR55_bob_total_amount() != null) {
						cell4.setCellValue(record.getR55_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(55);
					// R56
					// Column E
					cell4 = row.getCell(4);
					if (record.getR56_bob_total_amount() != null) {
						cell4.setCellValue(record.getR56_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(56);
					// R57
					// Column E
					cell4 = row.getCell(4);
					if (record.getR57_bob_total_amount() != null) {
						cell4.setCellValue(record.getR57_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(57);
					// R58
					// Column E
					cell4 = row.getCell(4);
					if (record.getR58_bob_total_amount() != null) {
						cell4.setCellValue(record.getR58_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(58);
					// R59
					// Column E
					cell4 = row.getCell(4);
					if (record.getR59_bob_total_amount() != null) {
						cell4.setCellValue(record.getR59_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(59);
					// R60
					// Column E
					cell4 = row.getCell(4);
					if (record.getR60_bob_total_amount() != null) {
						cell4.setCellValue(record.getR60_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(60);
					// R61
					// Column E
					cell4 = row.getCell(4);
					if (record.getR61_bob_total_amount() != null) {
						cell4.setCellValue(record.getR61_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(61);
					// R62
					// Column E
					cell4 = row.getCell(4);
					if (record.getR62_bob_total_amount() != null) {
						cell4.setCellValue(record.getR62_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(52);
					// R63
					// Column E
					cell4 = row.getCell(4);
					if (record.getR63_bob_total_amount() != null) {
						cell4.setCellValue(record.getR63_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(64);
					// R65
					// Column E
					cell4 = row.getCell(4);
					if (record.getR65_bob_total_amount() != null) {
						cell4.setCellValue(record.getR65_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(65);
					// R66
					// Column E
					cell4 = row.getCell(4);
					if (record.getR66_bob_total_amount() != null) {
						cell4.setCellValue(record.getR66_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(66);
					// R67
					// Column E
					cell4 = row.getCell(4);
					if (record.getR67_bob_total_amount() != null) {
						cell4.setCellValue(record.getR67_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(67);
					// R68
					// Column E
					cell4 = row.getCell(4);
					if (record.getR68_bob_total_amount() != null) {
						cell4.setCellValue(record.getR68_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(68);
					// R69
					// Column E
					cell4 = row.getCell(4);
					if (record.getR69_bob_total_amount() != null) {
						cell4.setCellValue(record.getR69_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(73);
					// R74
					// Column E
					cell4 = row.getCell(4);
					if (record.getR74_bob_total_amount() != null) {
						cell4.setCellValue(record.getR74_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(74);
					// R75
					// Column E
					cell4 = row.getCell(4);
					if (record.getR75_bob_total_amount() != null) {
						cell4.setCellValue(record.getR75_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(75);
					// R76
					// Column E
					cell4 = row.getCell(4);
					if (record.getR76_bob_total_amount() != null) {
						cell4.setCellValue(record.getR76_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(76);
					// R77
					// Column E
					cell4 = row.getCell(4);
					if (record.getR77_bob_total_amount() != null) {
						cell4.setCellValue(record.getR77_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(77);
					// R78
					// Column E
					cell4 = row.getCell(4);
					if (record.getR78_bob_total_amount() != null) {
						cell4.setCellValue(record.getR78_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(78);
					// R79
					// Column E
					cell4 = row.getCell(4);
					if (record.getR79_bob_total_amount() != null) {
						cell4.setCellValue(record.getR79_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(80);
					// R81
					// Column E
					cell4 = row.getCell(4);
					if (record.getR81_bob_total_amount() != null) {
						cell4.setCellValue(record.getR81_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(81);
					// R82
					// Column E
					cell4 = row.getCell(4);
					if (record.getR82_bob_total_amount() != null) {
						cell4.setCellValue(record.getR82_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(82);
					// R83
					// Column E
					cell4 = row.getCell(4);
					if (record.getR83_bob_total_amount() != null) {
						cell4.setCellValue(record.getR83_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(83);
					// R84
					// Column E
					cell4 = row.getCell(4);
					if (record.getR84_bob_total_amount() != null) {
						cell4.setCellValue(record.getR84_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(84);
					// R74
					// Column E
					cell4 = row.getCell(4);
					if (record.getR85_bob_total_amount() != null) {
						cell4.setCellValue(record.getR85_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(87);
					// R88
					// Column E
					cell4 = row.getCell(4);
					if (record.getR88_bob_total_amount() != null) {
						cell4.setCellValue(record.getR88_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(88);
					// R89
					// Column E
					cell4 = row.getCell(4);
					if (record.getR89_bob_total_amount() != null) {
						cell4.setCellValue(record.getR89_bob_total_amount().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_LCR ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LCRDetails");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "PROVISION AMOUNT", "ROWID", "COLUMNID",
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
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_LCR_Archival_Detail_Entity> reportData = BRRS_M_LCR_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LCR_Archival_Detail_Entity item : reportData) {
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

					row.createCell(4).setCellValue(item.getReportLable());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
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
				logger.info("No data found for M_LCR — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_LCR Excel", e);
			return new byte[0];
		}
	}

//public boolean updateProvision(M_LCR_Detail_Entity mLCRData) {
//    try {
//        M_LCR_Detail_Entity existing = BRRS_M_LCR_Detail_Repo.findByAcctNumber(mLCRData.getAcctNumber());
//        
//        System.out.println("came to services");
//        if (existing != null) {
//            existing.setProvision(mLCRData.getProvision());
//            existing.setAcctName(mLCRData.getAcctName());
//            
//            
//            BRRS_M_LCR_Detail_Repo.save(existing);
//            
//            return true;
//        } else {
//            System.out.println("Record not found for Account No: " + mLCRData.getAcctNumber());
//            return false;
//        }
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        return false;
//    }
//}

	@Autowired
	private BRRS_M_LCR_Detail_Repo M_LCR_Detail_Repo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_LCR"); 

		if (acctNo != null) {
			M_LCR_Detail_Entity mLCREntity = M_LCR_Detail_Repo.findByAcctnumber(acctNo);
			if (mLCREntity != null && mLCREntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(mLCREntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", mLCREntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("acctBalanceInPula");
			String provisionStr1 = request.getParameter("debitequivalent");
			String provisionStr2 = request.getParameter("emi");
			String provisionStr3 = request.getParameter("creditequivalent");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			M_LCR_Detail_Entity existing = M_LCR_Detail_Repo.findByAcctnumber(acctNo);
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
				if (existing.getAcctBalanceInPula() == null || existing.getAcctBalanceInPula().compareTo(newProvision) != 0) {
					existing.setAcctBalanceInPula(newProvision);
					isChanged = true;
					logger.info("Provision updated to {}", newProvision);
				}
			}
			
			if (provisionStr1 != null && !provisionStr1.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr1);
				if (existing.getDebitequivalent() == null || existing.getDebitequivalent().compareTo(newProvision) != 0) {
					existing.setDebitequivalent(newProvision);
					isChanged = true;
					logger.info("Provision updated to {}", newProvision);
				}
			}
			
			if (provisionStr2 != null && !provisionStr2.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr2);
				if (existing.getEmi() == null || existing.getEmi().compareTo(newProvision) != 0) {
					existing.setEmi(newProvision);
					isChanged = true;
					logger.info("Provision updated to {}", newProvision);
				}
			}
			
			if (provisionStr3 != null && !provisionStr3.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr3);
				if (existing.getCreditequivalent() == null || existing.getCreditequivalent().compareTo(newProvision) != 0) {
					existing.setCreditequivalent(newProvision);
					isChanged = true;
					logger.info("Provision updated to {}", newProvision);
				}
			}


			if (isChanged) {
				M_LCR_Detail_Repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_M_LCR_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_M_LCR_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating M_LCR record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

}
