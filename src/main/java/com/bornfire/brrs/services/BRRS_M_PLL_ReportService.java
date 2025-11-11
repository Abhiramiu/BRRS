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

import com.bornfire.brrs.entities.BRRS_M_PLL_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_PLL_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_PLL_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_PLL_Summary_Repo;
import com.bornfire.brrs.entities.M_PLL_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_PLL_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_PLL_Detail_Entity;
import com.bornfire.brrs.entities.M_PLL_Summary_Entity;

@Component
@Service

public class BRRS_M_PLL_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_PLL_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_PLL_Detail_Repo BRRS_M_PLL_Detail_Repo;

	@Autowired
	BRRS_M_PLL_Summary_Repo BRRS_M_PLL_Summary_Repo;

	@Autowired
	BRRS_M_PLL_Archival_Detail_Repo BRRS_M_PLL_Archival_Detail_Repo;

	@Autowired
	BRRS_M_PLL_Archival_Summary_Repo BRRS_M_PLL_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_PLLView(String reportId, String fromdate, String todate, String currency, String dtltype,
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
			List<M_PLL_Archival_Summary_Entity> T1Master = new ArrayList<M_PLL_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_PLL_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),
						version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {

			List<M_PLL_Summary_Entity> T1Master = new ArrayList<M_PLL_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_PLL_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_PLL");

		// mv.addObject("reportsummary", T1Master);
		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;
	}

	public ModelAndView getM_PLLcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
				List<M_PLL_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_PLL_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate,
							version);
				} else {
					T1Dt1 = BRRS_M_PLL_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_PLL_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_PLL_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = BRRS_M_PLL_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
					totalPages = BRRS_M_PLL_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/M_PLL");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	public byte[] getM_PLLExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_PLLARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<M_PLL_Summary_Entity> dataList = BRRS_M_PLL_Summary_Repo.getdatabydateList(dateformat.parse(todate));

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

			return out.toByteArray();
		}
	}

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
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
			List<M_PLL_Detail_Entity> reportData = BRRS_M_PLL_Detail_Repo.getdatabydateList(parsedToDate);

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
						balanceCell.setCellValue(0.000);
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

	public List<Object> getM_PLLArchival() {
		List<Object> M_PLLArchivallist = new ArrayList<>();
		try {
			M_PLLArchivallist = BRRS_M_PLL_Archival_Summary_Repo.getM_PLLarchival();
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

	public byte[] getExcelM_PLLARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_PLL_Archival_Summary_Entity> dataList = BRRS_M_PLL_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

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

			return out.toByteArray();
		}
	}

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
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
			List<M_PLL_Archival_Detail_Entity> reportData = BRRS_M_PLL_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);

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
						balanceCell.setCellValue(0.000);
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
//            BRRS_M_PLL_Detail_Repo.save(existing);
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
	private BRRS_M_PLL_Detail_Repo M_PLL_Detail_Repo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_PLL"); 

		if (acctNo != null) {
			M_PLL_Detail_Entity mpllEntity = M_PLL_Detail_Repo.findByAcctNumber(acctNo);
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
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("provision");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			M_PLL_Detail_Entity existing = M_PLL_Detail_Repo.findByAcctNumber(acctNo);
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
				M_PLL_Detail_Repo.save(existing);
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

}
