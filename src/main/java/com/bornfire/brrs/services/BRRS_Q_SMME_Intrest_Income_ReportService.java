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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;
import com.bornfire.brrs.entities.BRRS_Q_SMME_Intrest_Income_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_SMME_Intrest_Income_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Q_SMME_Intrest_Income_Detail_Repo;
import com.bornfire.brrs.entities.Q_SMME_Intrest_Income_Archival_Detail_Entity;
import com.bornfire.brrs.entities.Q_SMME_Intrest_Income_Archival_Summary_Entity;
import com.bornfire.brrs.entities.Q_SMME_Intrest_Income_Detail_Entity;
import com.bornfire.brrs.entities.Q_SMME_Intrest_Income_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_Q_SMME_Intrest_Income_Summary_Repo;


@Component
@Service

public class BRRS_Q_SMME_Intrest_Income_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_SMME_Intrest_Income_ReportService.class);

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	private Environment env;
	@Autowired
	BRRS_Q_SMME_Intrest_Income_Detail_Repo q_SMME_Detail_Repo;
	
	@Autowired
	BRRS_Q_SMME_Intrest_Income_Summary_Repo q_SMME_Summary_Repo;

	
	@Autowired
	BRRS_Q_SMME_Intrest_Income_Archival_Detail_Repo Q_SMME_Archival_Detail_Repo;

	@Autowired
	BRRS_Q_SMME_Intrest_Income_Archival_Summary_Repo Q_SMME_Archival_Summary_Repo;


	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_Q_SMMEView(String reportId, String fromdate, String todate, 
			String currency, String dtltype, Pageable pageable, String type, BigDecimal version)  {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		System.out.println("testing");
		System.out.println(version);

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<Q_SMME_Intrest_Income_Archival_Summary_Entity> T1Master = new ArrayList<Q_SMME_Intrest_Income_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);
				T1Master = Q_SMME_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
				

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {
			List<Q_SMME_Intrest_Income_Summary_Entity> T1Master = new ArrayList<Q_SMME_Intrest_Income_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				T1Master = q_SMME_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/Q_SMME");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	
	public ModelAndView getBRRS_Q_SMMEcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {
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
			String reportLabel = null;
			String reportAddlCriteria1 = null;
			// ✅ Split the filter string here
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLabel = parts[0];
					reportAddlCriteria1 = parts[1];
				}
			}
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				System.out.println(version);
				// 🔹 Archival branch
				List<Q_SMME_Intrest_Income_Archival_Detail_Entity> T1Dt1;
				if (reportLabel != null && reportAddlCriteria1 != null) {
					T1Dt1 = Q_SMME_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate, version);
				} else {
					T1Dt1 = Q_SMME_Archival_Detail_Repo.getdatabydateList(todate, version);
					totalPages = q_SMME_Detail_Repo.getdatacount(parsedDate);
					System.out.println(T1Dt1.size());
					mv.addObject("pagination", "YES");
				}
				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			} else {
				
				// 🔹 Current branch
				List<Q_SMME_Intrest_Income_Detail_Entity> T1Dt1;
				if (reportLabel != null && reportAddlCriteria1 != null) {
					T1Dt1 = q_SMME_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);
					System.out.println(T1Dt1.size());
				} else {
					T1Dt1 = q_SMME_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
					System.out.println(T1Dt1.size());
					totalPages = q_SMME_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/Q_SMME");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}



	public byte[] getQ_SMMEExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		System.out.println(type+"   "+version);
		if ("ARCHIVAL".equalsIgnoreCase(type) && version.compareTo(BigDecimal.ZERO) > 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getSummaryExcelARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			
		}

		// Fetch data
		List<Q_SMME_Intrest_Income_Summary_Entity> dataList = q_SMME_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_SMME report. Returning empty result.");
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
			int startRow = 15;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_SMME_Intrest_Income_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					row = sheet.getRow(15);
					// row16
					// Column 1
					Cell R15cell2 = row.createCell(1);
					if (record.getR16_res_carry_amt() != null) {
						R15cell2.setCellValue(record.getR16_res_carry_amt().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}
					// Column 2
					Cell R15cell3 = row.createCell(2);
					if (record.getR16_non_res_carry_amt() != null) {
						R15cell3.setCellValue(record.getR16_non_res_carry_amt().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

					// row17
					// Column 1
					row = sheet.getRow(16);

					Cell R16cell2 = row.createCell(1);
					if (record.getR17_res_carry_amt() != null) {
						R16cell2.setCellValue(record.getR17_res_carry_amt().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}
					// Column 2
					Cell R16cell3 = row.createCell(2);
					if (record.getR17_non_res_carry_amt() != null) {
						R16cell3.setCellValue(record.getR17_non_res_carry_amt().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

					// ================== ROW 18 ==================
					// Column 1
					row = sheet.getRow(17);

					Cell R17cell2 = row.createCell(1);
					if (record.getR18_res_carry_amt() != null) {
						R17cell2.setCellValue(record.getR18_res_carry_amt().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

					// Column 2
					Cell R17cell3 = row.createCell(2);
					if (record.getR18_non_res_carry_amt() != null) {
						R17cell3.setCellValue(record.getR18_non_res_carry_amt().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}

					// ================== ROW 19 ==================
					row = sheet.getRow(18);
					Cell R18cell2 = row.createCell(1);
					if (record.getR19_res_carry_amt() != null) {
						R18cell2.setCellValue(record.getR19_res_carry_amt().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

					Cell R18cell3 = row.createCell(2);
					if (record.getR19_non_res_carry_amt() != null) {
						R18cell3.setCellValue(record.getR19_non_res_carry_amt().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}
					// ================== ROW 20 ==================
					row = sheet.getRow(19);
					Cell R19cell2 = row.createCell(1);
					if (record.getR20_res_carry_amt() != null) {
						R19cell2.setCellValue(record.getR20_res_carry_amt().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}

					Cell R19cell3 = row.createCell(2);
					if (record.getR20_non_res_carry_amt() != null) {
						R19cell3.setCellValue(record.getR20_non_res_carry_amt().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}

					// ================== ROW 21 ==================
					row = sheet.getRow(20);
					Cell R20cell2 = row.createCell(1);
					if (record.getR21_res_carry_amt() != null) {
						R20cell2.setCellValue(record.getR21_res_carry_amt().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}

					Cell R20cell3 = row.createCell(2);
					if (record.getR21_non_res_carry_amt() != null) {
						R20cell3.setCellValue(record.getR21_non_res_carry_amt().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}

					// ================== ROW 22 ==================
					row = sheet.getRow(21);
					Cell R21cell2 = row.createCell(1);
					if (record.getR22_res_carry_amt() != null) {
						R21cell2.setCellValue(record.getR22_res_carry_amt().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}

					Cell R21cell3 = row.createCell(2);
					if (record.getR22_non_res_carry_amt() != null) {
						R21cell3.setCellValue(record.getR22_non_res_carry_amt().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================== ROW 23 ==================
					row = sheet.getRow(22);
					Cell R22cell2 = row.createCell(1);
					if (record.getR23_res_carry_amt() != null) {
						R22cell2.setCellValue(record.getR23_res_carry_amt().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}

					Cell R22cell3 = row.createCell(2);
					if (record.getR23_non_res_carry_amt() != null) {
						R22cell3.setCellValue(record.getR23_non_res_carry_amt().doubleValue());
						R22cell3.setCellStyle(numberStyle);
					} else {
						R22cell3.setCellValue("");
						R22cell3.setCellStyle(textStyle);
					}

					// ================== ROW 24 ==================
					row = sheet.getRow(23);
					Cell R23cell2 = row.createCell(1);
					if (record.getR24_res_carry_amt() != null) {
						R23cell2.setCellValue(record.getR24_res_carry_amt().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					Cell R23cell3 = row.createCell(2);
					if (record.getR24_non_res_carry_amt() != null) {
						R23cell3.setCellValue(record.getR24_non_res_carry_amt().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================== ROW 25 ==================
					row = sheet.getRow(24);
					Cell R24cell2 = row.createCell(1);
					if (record.getR25_res_carry_amt() != null) {
						R24cell2.setCellValue(record.getR25_res_carry_amt().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					Cell R24cell3 = row.createCell(2);
					if (record.getR25_non_res_carry_amt() != null) {
						R24cell3.setCellValue(record.getR25_non_res_carry_amt().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================== ROW 26 ==================
					row = sheet.getRow(25);
					Cell R25cell2 = row.createCell(1);
					if (record.getR26_res_carry_amt() != null) {
						R25cell2.setCellValue(record.getR26_res_carry_amt().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					Cell R25cell3 = row.createCell(2);
					if (record.getR26_non_res_carry_amt() != null) {
						R25cell3.setCellValue(record.getR26_non_res_carry_amt().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// ================== ROW 27 ==================
					row = sheet.getRow(26);
					Cell R26cell2 = row.createCell(1);
					if (record.getR27_res_carry_amt() != null) {
						R26cell2.setCellValue(record.getR27_res_carry_amt().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					Cell R26cell3 = row.createCell(2);
					if (record.getR27_non_res_carry_amt() != null) {
						R26cell3.setCellValue(record.getR27_non_res_carry_amt().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================== ROW 28 ==================
					row = sheet.getRow(27);
					Cell R27cell2 = row.createCell(1);
					if (record.getR28_res_carry_amt() != null) {
						R27cell2.setCellValue(record.getR28_res_carry_amt().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					Cell R27cell3 = row.createCell(2);
					if (record.getR28_non_res_carry_amt() != null) {
						R27cell3.setCellValue(record.getR28_non_res_carry_amt().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);

					}
					// 29 is Calculation Part
					// ================== ROW 30 ==================
					row = sheet.getRow(29);
					Cell R29cell2 = row.createCell(1);
					if (record.getR30_res_carry_amt() != null) {
						R29cell2.setCellValue(record.getR30_res_carry_amt().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					Cell R29cell3 = row.createCell(2);
					if (record.getR30_non_res_carry_amt() != null) {
						R29cell3.setCellValue(record.getR30_non_res_carry_amt().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}

					// ================== ROW 31 ==================
					row = sheet.getRow(30);
					Cell R30cell2 = row.createCell(1);
					if (record.getR31_res_carry_amt() != null) {
						R30cell2.setCellValue(record.getR31_res_carry_amt().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					Cell R30cell3 = row.createCell(2);
					if (record.getR31_non_res_carry_amt() != null) {
						R30cell3.setCellValue(record.getR31_non_res_carry_amt().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}

					// ================== ROW 32 ==================
					row = sheet.getRow(31);
					Cell R31cell2 = row.createCell(1);
					if (record.getR32_res_carry_amt() != null) {
						R31cell2.setCellValue(record.getR32_res_carry_amt().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					Cell R31cell3 = row.createCell(2);
					if (record.getR32_non_res_carry_amt() != null) {
						R31cell3.setCellValue(record.getR32_non_res_carry_amt().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// 33 is Calculation Part
					// ================== ROW 34 ==================
					row = sheet.getRow(33);
					Cell R33cell2 = row.createCell(1);
					if (record.getR34_res_carry_amt() != null) {
						R33cell2.setCellValue(record.getR34_res_carry_amt().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					Cell R33cell3 = row.createCell(2);
					if (record.getR34_non_res_carry_amt() != null) {
						R33cell3.setCellValue(record.getR34_non_res_carry_amt().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// ================== ROW 35 ==================
					row = sheet.getRow(34);
					Cell R34cell2 = row.createCell(1);
					if (record.getR35_res_carry_amt() != null) {
						R34cell2.setCellValue(record.getR35_res_carry_amt().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					Cell R34cell3 = row.createCell(2);
					if (record.getR35_non_res_carry_amt() != null) {
						R34cell3.setCellValue(record.getR35_non_res_carry_amt().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// 37 is Calculation Part

					// ================== ROW 36 ==================
					row = sheet.getRow(35);
					Cell R35cell2 = row.createCell(1);
					if (record.getR36_res_carry_amt() != null) {
						R35cell2.setCellValue(record.getR36_res_carry_amt().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}

					Cell R35cell3 = row.createCell(2);
					if (record.getR36_non_res_carry_amt() != null) {
						R35cell3.setCellValue(record.getR36_non_res_carry_amt().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// ================== ROW 38 ==================
					row = sheet.getRow(37);
					Cell R37cell2 = row.createCell(1);
					if (record.getR38_res_carry_amt() != null) {
						R37cell2.setCellValue(record.getR38_res_carry_amt().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}

					Cell R37cell3 = row.createCell(2);
					if (record.getR38_non_res_carry_amt() != null) {
						R37cell3.setCellValue(record.getR38_non_res_carry_amt().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// ================== ROW 39 ==================
					row = sheet.getRow(38);
					Cell R38cell2 = row.createCell(1);
					if (record.getR39_res_carry_amt() != null) {
						R38cell2.setCellValue(record.getR39_res_carry_amt().doubleValue());
						R38cell2.setCellStyle(numberStyle);
					} else {
						R38cell2.setCellValue("");
						R38cell2.setCellStyle(textStyle);
					}

					Cell R38cell3 = row.createCell(2);
					if (record.getR39_non_res_carry_amt() != null) {
						R38cell3.setCellValue(record.getR39_non_res_carry_amt().doubleValue());
						R38cell3.setCellStyle(numberStyle);
					} else {
						R38cell3.setCellValue("");
						R38cell3.setCellStyle(textStyle);
					}

					// ================== ROW 40 ==================
					row = sheet.getRow(39);
					Cell R39cell2 = row.createCell(1);
					if (record.getR40_res_carry_amt() != null) {
						R39cell2.setCellValue(record.getR40_res_carry_amt().doubleValue());
						R39cell2.setCellStyle(numberStyle);
					} else {
						R39cell2.setCellValue("");
						R39cell2.setCellStyle(textStyle);
					}

					Cell R39cell3 = row.createCell(2);
					if (record.getR40_non_res_carry_amt() != null) {
						R39cell3.setCellValue(record.getR40_non_res_carry_amt().doubleValue());
						R39cell3.setCellStyle(numberStyle);
					} else {
						R39cell3.setCellValue("");
						R39cell3.setCellStyle(textStyle);
					}

					// ================== ROW 41 ==================
					row = sheet.getRow(40);
					Cell R40cell2 = row.createCell(1);
					if (record.getR41_res_carry_amt() != null) {
						R40cell2.setCellValue(record.getR41_res_carry_amt().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					Cell R40cell3 = row.createCell(2);
					if (record.getR41_non_res_carry_amt() != null) {
						R40cell3.setCellValue(record.getR41_non_res_carry_amt().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// Row 42
					row = sheet.getRow(41);
					Cell R41cell2 = row.createCell(1);
					if (record.getR42_res_carry_amt() != null) {
						R41cell2.setCellValue(record.getR42_res_carry_amt().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					Cell R41cell3 = row.createCell(2);
					if (record.getR42_non_res_carry_amt() != null) {
						R41cell3.setCellValue(record.getR42_non_res_carry_amt().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}

					// Row 43
					row = sheet.getRow(42);
					Cell R42cell2 = row.createCell(1);
					if (record.getR43_res_carry_amt() != null) {
						R42cell2.setCellValue(record.getR43_res_carry_amt().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					Cell R42cell3 = row.createCell(2);
					if (record.getR43_non_res_carry_amt() != null) {
						R42cell3.setCellValue(record.getR43_non_res_carry_amt().doubleValue());
						R42cell3.setCellStyle(numberStyle);
					} else {
						R42cell3.setCellValue("");
						R42cell3.setCellStyle(textStyle);
					}
					// 44 is Calculation Part

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

	public byte[] BRRS_Q_SMMEDetailExcel(String filename, String fromdate, String todate, String currency,
										   String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRSQ_SMME Details...");
			System.out.println("came to Detail download service");
			System.out.println("Gopika");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_Q_SMMEDetails");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA",  "REPORT LABEL", "REPORT ADDL CRITERIA1",
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
			List<Q_SMME_Intrest_Income_Detail_Entity> reportData = q_SMME_Detail_Repo.getdatabydateList(parsedToDate);
			
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Q_SMME_Intrest_Income_Detail_Entity item : reportData) {
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
					row.createCell(4).setCellValue(item.getReportLabel());
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
				logger.info("No data found for BRRS_Q_SMME — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_Q_SMME Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getQ_SMMEArchival() {
		List<Object> Q_SMMEArchivallist = new ArrayList<>();
		try {
			Q_SMMEArchivallist = Q_SMME_Archival_Summary_Repo.getQ_SMMEarchival();
			System.out.println("countser" + Q_SMMEArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_SFINP2 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return Q_SMMEArchivallist;
	}

	public byte[] getSummaryExcelARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println("Gopika Excel Archival");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<Q_SMME_Intrest_Income_Archival_Summary_Entity> dataList = Q_SMME_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_SMME report. Returning empty result.");
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

			int startRow = 15;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_SMME_Intrest_Income_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					row = sheet.getRow(15);
					// row16
					// Column 1
					Cell R15cell2 = row.createCell(1);
					if (record.getR16_res_carry_amt() != null) {
						R15cell2.setCellValue(record.getR16_res_carry_amt().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}
					// Column 2
					Cell R15cell3 = row.createCell(2);
					if (record.getR16_non_res_carry_amt() != null) {
						R15cell3.setCellValue(record.getR16_non_res_carry_amt().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

					// row17
					// Column 1
					row = sheet.getRow(16);

					Cell R16cell2 = row.createCell(1);
					if (record.getR17_res_carry_amt() != null) {
						R16cell2.setCellValue(record.getR17_res_carry_amt().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}
					// Column 2
					Cell R16cell3 = row.createCell(2);
					if (record.getR17_non_res_carry_amt() != null) {
						R16cell3.setCellValue(record.getR17_non_res_carry_amt().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

					// ================== ROW 18 ==================
					// Column 1
					row = sheet.getRow(17);

					Cell R17cell2 = row.createCell(1);
					if (record.getR18_res_carry_amt() != null) {
						R17cell2.setCellValue(record.getR18_res_carry_amt().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

					// Column 2
					Cell R17cell3 = row.createCell(2);
					if (record.getR18_non_res_carry_amt() != null) {
						R17cell3.setCellValue(record.getR18_non_res_carry_amt().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}

					// ================== ROW 19 ==================
					row = sheet.getRow(18);
					Cell R18cell2 = row.createCell(1);
					if (record.getR19_res_carry_amt() != null) {
						R18cell2.setCellValue(record.getR19_res_carry_amt().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

					Cell R18cell3 = row.createCell(2);
					if (record.getR19_non_res_carry_amt() != null) {
						R18cell3.setCellValue(record.getR19_non_res_carry_amt().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}
					// ================== ROW 20 ==================
					row = sheet.getRow(19);
					Cell R19cell2 = row.createCell(1);
					if (record.getR20_res_carry_amt() != null) {
						R19cell2.setCellValue(record.getR20_res_carry_amt().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}

					Cell R19cell3 = row.createCell(2);
					if (record.getR20_non_res_carry_amt() != null) {
						R19cell3.setCellValue(record.getR20_non_res_carry_amt().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}

					// ================== ROW 21 ==================
					row = sheet.getRow(20);
					Cell R20cell2 = row.createCell(1);
					if (record.getR21_res_carry_amt() != null) {
						R20cell2.setCellValue(record.getR21_res_carry_amt().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}

					Cell R20cell3 = row.createCell(2);
					if (record.getR21_non_res_carry_amt() != null) {
						R20cell3.setCellValue(record.getR21_non_res_carry_amt().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}

					// ================== ROW 22 ==================
					row = sheet.getRow(21);
					Cell R21cell2 = row.createCell(1);
					if (record.getR22_res_carry_amt() != null) {
						R21cell2.setCellValue(record.getR22_res_carry_amt().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}

					Cell R21cell3 = row.createCell(2);
					if (record.getR22_non_res_carry_amt() != null) {
						R21cell3.setCellValue(record.getR22_non_res_carry_amt().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================== ROW 23 ==================
					row = sheet.getRow(22);
					Cell R22cell2 = row.createCell(1);
					if (record.getR23_res_carry_amt() != null) {
						R22cell2.setCellValue(record.getR23_res_carry_amt().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}

					Cell R22cell3 = row.createCell(2);
					if (record.getR23_non_res_carry_amt() != null) {
						R22cell3.setCellValue(record.getR23_non_res_carry_amt().doubleValue());
						R22cell3.setCellStyle(numberStyle);
					} else {
						R22cell3.setCellValue("");
						R22cell3.setCellStyle(textStyle);
					}

					// ================== ROW 24 ==================
					row = sheet.getRow(23);
					Cell R23cell2 = row.createCell(1);
					if (record.getR24_res_carry_amt() != null) {
						R23cell2.setCellValue(record.getR24_res_carry_amt().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					Cell R23cell3 = row.createCell(2);
					if (record.getR24_non_res_carry_amt() != null) {
						R23cell3.setCellValue(record.getR24_non_res_carry_amt().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================== ROW 25 ==================
					row = sheet.getRow(24);
					Cell R24cell2 = row.createCell(1);
					if (record.getR25_res_carry_amt() != null) {
						R24cell2.setCellValue(record.getR25_res_carry_amt().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					Cell R24cell3 = row.createCell(2);
					if (record.getR25_non_res_carry_amt() != null) {
						R24cell3.setCellValue(record.getR25_non_res_carry_amt().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================== ROW 26 ==================
					row = sheet.getRow(25);
					Cell R25cell2 = row.createCell(1);
					if (record.getR26_res_carry_amt() != null) {
						R25cell2.setCellValue(record.getR26_res_carry_amt().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					Cell R25cell3 = row.createCell(2);
					if (record.getR26_non_res_carry_amt() != null) {
						R25cell3.setCellValue(record.getR26_non_res_carry_amt().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// ================== ROW 27 ==================
					row = sheet.getRow(26);
					Cell R26cell2 = row.createCell(1);
					if (record.getR27_res_carry_amt() != null) {
						R26cell2.setCellValue(record.getR27_res_carry_amt().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					Cell R26cell3 = row.createCell(2);
					if (record.getR27_non_res_carry_amt() != null) {
						R26cell3.setCellValue(record.getR27_non_res_carry_amt().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================== ROW 28 ==================
					row = sheet.getRow(27);
					Cell R27cell2 = row.createCell(1);
					if (record.getR28_res_carry_amt() != null) {
						R27cell2.setCellValue(record.getR28_res_carry_amt().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					Cell R27cell3 = row.createCell(2);
					if (record.getR28_non_res_carry_amt() != null) {
						R27cell3.setCellValue(record.getR28_non_res_carry_amt().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);

					}
					// 29 is Calculation Part
					// ================== ROW 30 ==================
					row = sheet.getRow(29);
					Cell R29cell2 = row.createCell(1);
					if (record.getR30_res_carry_amt() != null) {
						R29cell2.setCellValue(record.getR30_res_carry_amt().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					Cell R29cell3 = row.createCell(2);
					if (record.getR30_non_res_carry_amt() != null) {
						R29cell3.setCellValue(record.getR30_non_res_carry_amt().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}

					// ================== ROW 31 ==================
					row = sheet.getRow(30);
					Cell R30cell2 = row.createCell(1);
					if (record.getR31_res_carry_amt() != null) {
						R30cell2.setCellValue(record.getR31_res_carry_amt().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					Cell R30cell3 = row.createCell(2);
					if (record.getR31_non_res_carry_amt() != null) {
						R30cell3.setCellValue(record.getR31_non_res_carry_amt().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}

					// ================== ROW 32 ==================
					row = sheet.getRow(31);
					Cell R31cell2 = row.createCell(1);
					if (record.getR32_res_carry_amt() != null) {
						R31cell2.setCellValue(record.getR32_res_carry_amt().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					Cell R31cell3 = row.createCell(2);
					if (record.getR32_non_res_carry_amt() != null) {
						R31cell3.setCellValue(record.getR32_non_res_carry_amt().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// 33 is Calculation Part
					// ================== ROW 34 ==================
					row = sheet.getRow(33);
					Cell R33cell2 = row.createCell(1);
					if (record.getR34_res_carry_amt() != null) {
						R33cell2.setCellValue(record.getR34_res_carry_amt().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					Cell R33cell3 = row.createCell(2);
					if (record.getR34_non_res_carry_amt() != null) {
						R33cell3.setCellValue(record.getR34_non_res_carry_amt().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// ================== ROW 35 ==================
					row = sheet.getRow(34);
					Cell R34cell2 = row.createCell(1);
					if (record.getR35_res_carry_amt() != null) {
						R34cell2.setCellValue(record.getR35_res_carry_amt().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					Cell R34cell3 = row.createCell(2);
					if (record.getR35_non_res_carry_amt() != null) {
						R34cell3.setCellValue(record.getR35_non_res_carry_amt().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// 37 is Calculation Part

					// ================== ROW 36 ==================
					row = sheet.getRow(35);
					Cell R35cell2 = row.createCell(1);
					if (record.getR36_res_carry_amt() != null) {
						R35cell2.setCellValue(record.getR36_res_carry_amt().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}

					Cell R35cell3 = row.createCell(2);
					if (record.getR36_non_res_carry_amt() != null) {
						R35cell3.setCellValue(record.getR36_non_res_carry_amt().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// ================== ROW 38 ==================
					row = sheet.getRow(37);
					Cell R37cell2 = row.createCell(1);
					if (record.getR38_res_carry_amt() != null) {
						R37cell2.setCellValue(record.getR38_res_carry_amt().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}

					Cell R37cell3 = row.createCell(2);
					if (record.getR38_non_res_carry_amt() != null) {
						R37cell3.setCellValue(record.getR38_non_res_carry_amt().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// ================== ROW 39 ==================
					row = sheet.getRow(38);
					Cell R38cell2 = row.createCell(1);
					if (record.getR39_res_carry_amt() != null) {
						R38cell2.setCellValue(record.getR39_res_carry_amt().doubleValue());
						R38cell2.setCellStyle(numberStyle);
					} else {
						R38cell2.setCellValue("");
						R38cell2.setCellStyle(textStyle);
					}

					Cell R38cell3 = row.createCell(2);
					if (record.getR39_non_res_carry_amt() != null) {
						R38cell3.setCellValue(record.getR39_non_res_carry_amt().doubleValue());
						R38cell3.setCellStyle(numberStyle);
					} else {
						R38cell3.setCellValue("");
						R38cell3.setCellStyle(textStyle);
					}

					// ================== ROW 40 ==================
					row = sheet.getRow(39);
					Cell R39cell2 = row.createCell(1);
					if (record.getR40_res_carry_amt() != null) {
						R39cell2.setCellValue(record.getR40_res_carry_amt().doubleValue());
						R39cell2.setCellStyle(numberStyle);
					} else {
						R39cell2.setCellValue("");
						R39cell2.setCellStyle(textStyle);
					}

					Cell R39cell3 = row.createCell(2);
					if (record.getR40_non_res_carry_amt() != null) {
						R39cell3.setCellValue(record.getR40_non_res_carry_amt().doubleValue());
						R39cell3.setCellStyle(numberStyle);
					} else {
						R39cell3.setCellValue("");
						R39cell3.setCellStyle(textStyle);
					}

					// ================== ROW 41 ==================
					row = sheet.getRow(40);
					Cell R40cell2 = row.createCell(1);
					if (record.getR41_res_carry_amt() != null) {
						R40cell2.setCellValue(record.getR41_res_carry_amt().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					Cell R40cell3 = row.createCell(2);
					if (record.getR41_non_res_carry_amt() != null) {
						R40cell3.setCellValue(record.getR41_non_res_carry_amt().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// Row 42
					row = sheet.getRow(41);
					Cell R41cell2 = row.createCell(1);
					if (record.getR42_res_carry_amt() != null) {
						R41cell2.setCellValue(record.getR42_res_carry_amt().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					Cell R41cell3 = row.createCell(2);
					if (record.getR42_non_res_carry_amt() != null) {
						R41cell3.setCellValue(record.getR42_non_res_carry_amt().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}

					// Row 43
					row = sheet.getRow(42);
					Cell R42cell2 = row.createCell(1);
					if (record.getR43_res_carry_amt() != null) {
						R42cell2.setCellValue(record.getR43_res_carry_amt().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					Cell R42cell3 = row.createCell(2);
					if (record.getR43_non_res_carry_amt() != null) {
						R42cell3.setCellValue(record.getR43_non_res_carry_amt().doubleValue());
						R42cell3.setCellStyle(numberStyle);
					} else {
						R42cell3.setCellValue("");
						R42cell3.setCellStyle(textStyle);
					}
					// 44 is Calculation Part


				}workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}
public byte[] getDetailExcelARCHIVAL(String filename,
                                     String fromdate,
                                     String todate,
                                     String currency,
                                     String dtltype,
                                     String type,
                                     String version) {
    try {
        logger.info("Generating Excel for BRRS_Q_SMME ARCHIVAL Details...");
        System.out.println("came to Detail download service");

        // --- Create workbook and sheet ---
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Q_SMMEDetail");

        BorderStyle border = BorderStyle.THIN;

        // Header style
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

        // Data style
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.LEFT);
        dataStyle.setBorderTop(border);
        dataStyle.setBorderBottom(border);
        dataStyle.setBorderLeft(border);
        dataStyle.setBorderRight(border);

        // Balance style
        CellStyle balanceStyle = workbook.createCellStyle();
        balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
        balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
        balanceStyle.setBorderTop(border);
        balanceStyle.setBorderBottom(border);
        balanceStyle.setBorderLeft(border);
        balanceStyle.setBorderRight(border);

        // --- Header row ---
        String[] headers = {"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE",  "REPORT LABEL", "REPORT ADDL CRITERIA1", "REPORT_DATE"};
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

        // --- Fetch data from DB ---
       // Date parsedToDate = new SimpleDateFormat("dd-MM-yyyy").parse(todate); // ✅ match with controller
        List<Q_SMME_Intrest_Income_Archival_Detail_Entity> reportData =
                Q_SMME_Archival_Detail_Repo.getdatabydateList(todate, version);

        logger.info("Fetched {} rows from DB for ARCHIVAL", reportData != null ? reportData.size() : 0);

        if (reportData != null && !reportData.isEmpty()) {
            int rowIndex = 1;
            for (Q_SMME_Intrest_Income_Archival_Detail_Entity item : reportData) {
                XSSFRow row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(item.getCustId());
                row.createCell(1).setCellValue(item.getAcctNumber());
                row.createCell(2).setCellValue(item.getAcctName());

                // Balance
                Cell balanceCell = row.createCell(3);
                balanceCell.setCellValue(item.getAcctBalanceInPula() != null
                        ? item.getAcctBalanceInPula().doubleValue()
                        : 0);
                balanceCell.setCellStyle(balanceStyle);

                row.createCell(4).setCellValue(item.getReportLabel());
                row.createCell(5).setCellValue(item.getReportAddlCriteria1());
                row.createCell(6).setCellValue(item.getReportDate() != null
                        ? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
                        : "");

                // Apply data style except balance column
                for (int j = 0; j < 7; j++) {
                    if (j != 3) {
                        row.getCell(j).setCellStyle(dataStyle);
                    }
                }
            }
        } else {
            logger.info("⚠️ No data found for BRRS_Q_SMME ARCHIVAL — only header will be written.");
        }

        // --- Write to byte[] ---
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
        return bos.toByteArray();

    } catch (Exception e) {
        logger.error("Error generating BRRS_Q_SMME Excel", e);
        return new byte[0]; 
    }
}

	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/Q_SMME"); 

		if (acctNo != null) {
			Q_SMME_Intrest_Income_Detail_Entity q_smmeIi = q_SMME_Detail_Repo.findByAcctnumber(acctNo);
			if (q_smmeIi != null && q_smmeIi.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(q_smmeIi.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Q_SMMEData", q_smmeIi);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}
	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String acctBalanceInpula = request.getParameter("acctBalanceInPula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate"); // yyyy-MM-dd from HTML

			logger.info("Received update for ACCT_NO: {}", acctNo);

			Q_SMME_Intrest_Income_Detail_Entity existing = q_SMME_Detail_Repo.findByAcctnumber(acctNo);

			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Record not found for update.");
			}

			boolean isChanged = false;

			// Update account name
			if (acctName != null && !acctName.isEmpty() &&
					!acctName.equals(existing.getAcctName())) {

				existing.setAcctName(acctName);
				isChanged = true;
				logger.info("Updated acctName → {}", acctName);
			}

			// Update Pula balance
			if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {

				BigDecimal newBalance = new BigDecimal(acctBalanceInpula.replace(",", ""));

				if (existing.getAcctBalanceInPula() == null ||
						existing.getAcctBalanceInPula().compareTo(newBalance) != 0) {

					existing.setAcctBalanceInPula(newBalance);
					isChanged = true;
					logger.info("Updated acctBalanceInPula → {}", newBalance);
				}
			}

			if (!isChanged) {
				logger.info("No changes detected for ACCT_NO {}", acctNo);
				return ResponseEntity.ok("No changes were made.");
			}

			// Save updated data
			q_SMME_Detail_Repo.save(existing);

			logger.info("Record updated successfully for ACCT_NO {}", acctNo);

			// Format date "yyyy-MM-dd" → "dd-MM-yyyy"
			String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
					.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

			// Register after-commit callback
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					try {
						logger.info("AFTER COMMIT → Executing BRRS_Q_SMME_INTREST_INCOME_SUMMARY_PROCEDURE({})",
								formattedDate);

						jdbcTemplate.update(
								"BEGIN BRRS_Q_SMME_INTREST_INCOME_SUMMARY_PROCEDURE(?); END;",
								formattedDate);

					} catch (Exception e) {
						logger.error("Error executing after-commit procedure", e);
					}
				}
			});

			return ResponseEntity.ok("Record updated successfully!");

		} catch (Exception e) {
			logger.error("Error updating Q_SMME_Ii record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	
	

}
