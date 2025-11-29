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

import com.bornfire.brrs.entities.BRRS_M_GALOR_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_GALOR_Archival_Summary1_Repo;
import com.bornfire.brrs.entities.BRRS_M_GALOR_Archival_Summary2_Repo;
import com.bornfire.brrs.entities.BRRS_M_GALOR_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_GALOR_Manual_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_GALOR_Manual_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_GALOR_Summary1_Repo;
import com.bornfire.brrs.entities.BRRS_M_GALOR_Summary2_Repo;
import com.bornfire.brrs.entities.M_GALOR_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_GALOR_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.M_GALOR_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.M_GALOR_Detail_Entity;
import com.bornfire.brrs.entities.M_GALOR_Manual_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_GALOR_Manual_Summary_Entity;
import com.bornfire.brrs.entities.M_GALOR_Summary_Entity1;
import com.bornfire.brrs.entities.M_GALOR_Summary_Entity2;

@Component
@Service

public class BRRS_M_GALOR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_GALOR_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_GALOR_Manual_Archival_Summary_Repo m_galor_Manual_Archival_Summary_Repo;

	@Autowired
	BRRS_M_GALOR_Manual_Summary_Repo m_galor_Manual_Summary_Repo;

	@Autowired
	BRRS_M_GALOR_Archival_Summary1_Repo m_galor_Archival_Summary1_Repo;

	@Autowired
	BRRS_M_GALOR_Archival_Summary2_Repo m_galor_Archival_Summary2_Repo;

	@Autowired
	BRRS_M_GALOR_Summary2_Repo m_galor_Summary2_Repo;

	@Autowired
	BRRS_M_GALOR_Summary1_Repo m_galor_Summary1_Repo;

	@Autowired
	BRRS_M_GALOR_Archival_Detail_Repo m_galor_archival_detail_Repo;
	@Autowired
	BRRS_M_GALOR_Detail_Repo m_galor_detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_GALORView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("testing");
		System.out.println(version);

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_GALOR_Archival_Summary_Entity1> T1Master = new ArrayList<M_GALOR_Archival_Summary_Entity1>();
			List<M_GALOR_Archival_Summary_Entity2> T2Master = new ArrayList<M_GALOR_Archival_Summary_Entity2>();
			List<M_GALOR_Manual_Archival_Summary_Entity> T3Master = new ArrayList<M_GALOR_Manual_Archival_Summary_Entity>();
			System.out.println(version);
			try {

				T1Master = m_galor_Archival_Summary1_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = m_galor_Archival_Summary2_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				T3Master = m_galor_Manual_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),
						version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary1", T1Master);
			mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary3", T3Master);
		} else {

			List<M_GALOR_Summary_Entity1> T1Master = new ArrayList<M_GALOR_Summary_Entity1>();
			List<M_GALOR_Summary_Entity2> T2Master = new ArrayList<M_GALOR_Summary_Entity2>();
			List<M_GALOR_Manual_Summary_Entity> T3Master = new ArrayList<M_GALOR_Manual_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				T1Master = m_galor_Summary1_Repo.getdatabydateList(dateformat.parse(todate));
				T2Master = m_galor_Summary2_Repo.getdatabydateList(dateformat.parse(todate));
				T3Master = m_galor_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				System.out.println("T2Master size " + T2Master.size());
				mv.addObject("report_date", dateformat.format(d1));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary1", T1Master);
			mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary3", T3Master);
		}

		mv.setViewName("BRRS/M_GALOR");

		mv.addObject("displaymode", "summary");

		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getM_GALORcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();

		// Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;

			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLable = null;
			String reportAddlCriteria_1 = null;
			// ✅ Split filter string into rowId & columnId
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLable = parts[0];
					reportAddlCriteria_1 = parts[1];
				}
			}

			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<M_GALOR_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = m_galor_archival_detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1,
							parsedDate, version);
				} else {
					T1Dt1 = m_galor_archival_detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_GALOR_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = m_galor_detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1,
							parsedDate);
				} else {
					T1Dt1 = m_galor_detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
					totalPages = m_galor_detail_Repo.getdatacount(parsedDate);
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

		mv.setViewName("BRRS/M_GALOR");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	public List<Object> getM_GALORArchival() {
		List<Object> M_GALORArchivallist = new ArrayList<>();
		try {
			M_GALORArchivallist = m_galor_Manual_Archival_Summary_Repo.getM_GALORarchival();
			M_GALORArchivallist = m_galor_Archival_Summary1_Repo.getM_GALORarchival();
			M_GALORArchivallist = m_galor_Archival_Summary2_Repo.getM_GALORarchival();
			System.out.println("countser" + M_GALORArchivallist.size());
			System.out.println("countser" + M_GALORArchivallist.size());
			System.out.println("countser" + M_GALORArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_GALOR Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_GALORArchivallist;
	}

	public void updateReport(M_GALOR_Manual_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// Correct way — use TRUNC matching
		List<M_GALOR_Manual_Summary_Entity> list = m_galor_Manual_Summary_Repo
				.getdatabydateList(updatedEntity.getReport_date());

		if (list.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}

		M_GALOR_Manual_Summary_Entity existing = list.get(0);

		try {
			// FULL FIELD LIST
			String[] fields = { "product", "botswana", "south_africa", "sadc", "usa", "uk", "europe", "india", "sydney",
					"uganda", "c10", "c11", "c12", "c13", "c14", "c15", "c16", "total" };

			// FOR 111–114 ONLY → botswana field
			String[] botswanaOnly = { "botswana" };

			// RANGES TO LOOP
			int[][] ranges = { { 22, 23 }, { 57, 58 }, { 60, 61 }, { 64, 65 }, { 67, 68 }, { 111, 114 } // but only
																										// botswana in
																										// this range
			};

			for (int[] range : ranges) {
				for (int i = range[0]; i <= range[1]; i++) {

					String prefix = "R" + i + "_";

					// USE ONLY `botswana` FOR R111–R114
					String[] activeFields = (i >= 111 && i <= 114) ? botswanaOnly : fields;

					for (String field : activeFields) {

						String getterName = "get" + prefix + field;
						String setterName = "set" + prefix + field;

						try {
							Method getter = M_GALOR_Manual_Summary_Entity.class.getMethod(getterName);
							Method setter = M_GALOR_Manual_Summary_Entity.class.getMethod(setterName,
									getter.getReturnType());

							Object newValue = getter.invoke(updatedEntity);
							setter.invoke(existing, newValue);

						} catch (NoSuchMethodException e) {
							// Skip missing fields gracefully
							continue;
						}
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// Save updated entity
		m_galor_Manual_Summary_Repo.save(existing);
	}

	public byte[] getM_GALORExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_GALORARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<M_GALOR_Summary_Entity1> dataList = m_galor_Summary1_Repo.getdatabydateList(dateformat.parse(todate));
		List<M_GALOR_Summary_Entity2> dataList1 = m_galor_Summary2_Repo.getdatabydateList(dateformat.parse(todate));
		List<M_GALOR_Manual_Summary_Entity> dataList2 = m_galor_Manual_Summary_Repo
				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty() && dataList1.isEmpty() && dataList2.isEmpty()) {
			logger.warn("Service: No data found for M_GALOR report. Returning empty result.");
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

			if (!dataList.isEmpty()) {
				populateEntity1Data(sheet, dataList.get(0), textStyle, numberStyle);
			}

			if (!dataList1.isEmpty()) {
				populateEntity2Data(sheet, dataList1.get(0), textStyle, numberStyle);
			}

			if (!dataList2.isEmpty()) {
				populateEntity3Data(sheet, dataList2.get(0), textStyle, numberStyle);
			}

			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

	private void populateEntity1Data(Sheet sheet, M_GALOR_Summary_Entity1 record, CellStyle textStyle,
			CellStyle numberStyle) {

		// R11 - ROW 11 (Index 10)
		Row row = sheet.getRow(10) != null ? sheet.getRow(10) : sheet.createRow(10);

		// Column B - BOTSWANA
		Cell cellB = row.createCell(1);
		if (record.getR11_botswana() != null) {
			cellB.setCellValue(record.getR11_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH AFRICA
		Cell cellC = row.createCell(2);
		if (record.getR11_south_africa() != null) {
			cellC.setCellValue(record.getR11_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		Cell cellD = row.createCell(3);
		if (record.getR11_sadc() != null) {
			cellD.setCellValue(record.getR11_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		Cell cellE = row.createCell(4);
		if (record.getR11_usa() != null) {
			cellE.setCellValue(record.getR11_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		Cell cellF = row.createCell(5);
		if (record.getR11_uk() != null) {
			cellF.setCellValue(record.getR11_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		Cell cellG = row.createCell(6);
		if (record.getR11_europe() != null) {
			cellG.setCellValue(record.getR11_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		Cell cellH = row.createCell(7);
		if (record.getR11_india() != null) {
			cellH.setCellValue(record.getR11_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		Cell cellI = row.createCell(8);
		if (record.getR11_sydney() != null) {
			cellI.setCellValue(record.getR11_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - Uganda
		Cell cellJ = row.createCell(9);
		if (record.getR11_uganda() != null) {
			cellJ.setCellValue(record.getR11_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - 0
		Cell cellK = row.createCell(10);
		if (record.getR11_c10() != null) {
			cellK.setCellValue(record.getR11_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - 0
		Cell cellL = row.createCell(11);
		if (record.getR11_c11() != null) {
			cellL.setCellValue(record.getR11_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - 0
		Cell cellM = row.createCell(12);
		if (record.getR11_c12() != null) {
			cellM.setCellValue(record.getR11_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - 0
		Cell cellN = row.createCell(13);
		if (record.getR11_c13() != null) {
			cellN.setCellValue(record.getR11_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - 0
		Cell cellO = row.createCell(14);
		if (record.getR11_c14() != null) {
			cellO.setCellValue(record.getR11_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - 0
		Cell cellP = row.createCell(15);
		if (record.getR11_c15() != null) {
			cellP.setCellValue(record.getR11_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - 0
		Cell cellQ = row.createCell(16);
		if (record.getR11_c16() != null) {
			cellQ.setCellValue(record.getR11_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R12
		// -------------------------------------------------------

		row = sheet.getRow(11);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR12_botswana() != null) {
			cellB.setCellValue(record.getR12_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR12_south_africa() != null) {
			cellC.setCellValue(record.getR12_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR12_sadc() != null) {
			cellD.setCellValue(record.getR12_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR12_usa() != null) {
			cellE.setCellValue(record.getR12_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR12_uk() != null) {
			cellF.setCellValue(record.getR12_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR12_europe() != null) {
			cellG.setCellValue(record.getR12_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR12_india() != null) {
			cellH.setCellValue(record.getR12_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR12_sydney() != null) {
			cellI.setCellValue(record.getR12_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR12_uganda() != null) {
			cellJ.setCellValue(record.getR12_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR12_c10() != null) {
			cellK.setCellValue(record.getR12_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR12_c11() != null) {
			cellL.setCellValue(record.getR12_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR12_c12() != null) {
			cellM.setCellValue(record.getR12_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR12_c13() != null) {
			cellN.setCellValue(record.getR12_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR12_c14() != null) {
			cellO.setCellValue(record.getR12_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR12_c15() != null) {
			cellP.setCellValue(record.getR12_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR12_c16() != null) {
			cellQ.setCellValue(record.getR12_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R13
		// -------------------------------------------------------

		row = sheet.getRow(12);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR13_botswana() != null) {
			cellB.setCellValue(record.getR13_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR13_south_africa() != null) {
			cellC.setCellValue(record.getR13_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR13_sadc() != null) {
			cellD.setCellValue(record.getR13_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR13_usa() != null) {
			cellE.setCellValue(record.getR13_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR13_uk() != null) {
			cellF.setCellValue(record.getR13_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR13_europe() != null) {
			cellG.setCellValue(record.getR13_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR13_india() != null) {
			cellH.setCellValue(record.getR13_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR13_sydney() != null) {
			cellI.setCellValue(record.getR13_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR13_uganda() != null) {
			cellJ.setCellValue(record.getR13_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR13_c10() != null) {
			cellK.setCellValue(record.getR13_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR13_c11() != null) {
			cellL.setCellValue(record.getR13_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR13_c12() != null) {
			cellM.setCellValue(record.getR13_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR13_c13() != null) {
			cellN.setCellValue(record.getR13_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR13_c14() != null) {
			cellO.setCellValue(record.getR13_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR13_c15() != null) {
			cellP.setCellValue(record.getR13_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR13_c16() != null) {
			cellQ.setCellValue(record.getR13_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R14
		// -------------------------------------------------------

		row = sheet.getRow(13);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR14_botswana() != null) {
			cellB.setCellValue(record.getR14_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR14_south_africa() != null) {
			cellC.setCellValue(record.getR14_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR14_sadc() != null) {
			cellD.setCellValue(record.getR14_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR14_usa() != null) {
			cellE.setCellValue(record.getR14_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR14_uk() != null) {
			cellF.setCellValue(record.getR14_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR14_europe() != null) {
			cellG.setCellValue(record.getR14_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR14_india() != null) {
			cellH.setCellValue(record.getR14_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR14_sydney() != null) {
			cellI.setCellValue(record.getR14_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR14_uganda() != null) {
			cellJ.setCellValue(record.getR14_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR14_c10() != null) {
			cellK.setCellValue(record.getR14_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR14_c11() != null) {
			cellL.setCellValue(record.getR14_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR14_c12() != null) {
			cellM.setCellValue(record.getR14_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR14_c13() != null) {
			cellN.setCellValue(record.getR14_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR14_c14() != null) {
			cellO.setCellValue(record.getR14_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR14_c15() != null) {
			cellP.setCellValue(record.getR14_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR14_c16() != null) {
			cellQ.setCellValue(record.getR14_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R15
		// -------------------------------------------------------

		row = sheet.getRow(14);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR15_botswana() != null) {
			cellB.setCellValue(record.getR15_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR15_south_africa() != null) {
			cellC.setCellValue(record.getR15_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR15_sadc() != null) {
			cellD.setCellValue(record.getR15_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR15_usa() != null) {
			cellE.setCellValue(record.getR15_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR15_uk() != null) {
			cellF.setCellValue(record.getR15_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR15_europe() != null) {
			cellG.setCellValue(record.getR15_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR15_india() != null) {
			cellH.setCellValue(record.getR15_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR15_sydney() != null) {
			cellI.setCellValue(record.getR15_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR15_uganda() != null) {
			cellJ.setCellValue(record.getR15_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR15_c10() != null) {
			cellK.setCellValue(record.getR15_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR15_c11() != null) {
			cellL.setCellValue(record.getR15_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR15_c12() != null) {
			cellM.setCellValue(record.getR15_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR15_c13() != null) {
			cellN.setCellValue(record.getR15_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR15_c14() != null) {
			cellO.setCellValue(record.getR15_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR15_c15() != null) {
			cellP.setCellValue(record.getR15_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR15_c16() != null) {
			cellQ.setCellValue(record.getR15_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R16
		// -------------------------------------------------------

		row = sheet.getRow(15);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR16_botswana() != null) {
			cellB.setCellValue(record.getR16_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR16_south_africa() != null) {
			cellC.setCellValue(record.getR16_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR16_sadc() != null) {
			cellD.setCellValue(record.getR16_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR16_usa() != null) {
			cellE.setCellValue(record.getR16_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR16_uk() != null) {
			cellF.setCellValue(record.getR16_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR16_europe() != null) {
			cellG.setCellValue(record.getR16_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR16_india() != null) {
			cellH.setCellValue(record.getR16_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR16_sydney() != null) {
			cellI.setCellValue(record.getR16_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR16_uganda() != null) {
			cellJ.setCellValue(record.getR16_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR16_c10() != null) {
			cellK.setCellValue(record.getR16_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR16_c11() != null) {
			cellL.setCellValue(record.getR16_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR16_c12() != null) {
			cellM.setCellValue(record.getR16_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR16_c13() != null) {
			cellN.setCellValue(record.getR16_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR16_c14() != null) {
			cellO.setCellValue(record.getR16_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR16_c15() != null) {
			cellP.setCellValue(record.getR16_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR16_c16() != null) {
			cellQ.setCellValue(record.getR16_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R17
		// -------------------------------------------------------

		row = sheet.getRow(16);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR17_botswana() != null) {
			cellB.setCellValue(record.getR17_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR17_south_africa() != null) {
			cellC.setCellValue(record.getR17_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR17_sadc() != null) {
			cellD.setCellValue(record.getR17_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR17_usa() != null) {
			cellE.setCellValue(record.getR17_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR17_uk() != null) {
			cellF.setCellValue(record.getR17_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR17_europe() != null) {
			cellG.setCellValue(record.getR17_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR17_india() != null) {
			cellH.setCellValue(record.getR17_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR17_sydney() != null) {
			cellI.setCellValue(record.getR17_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR17_uganda() != null) {
			cellJ.setCellValue(record.getR17_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR17_c10() != null) {
			cellK.setCellValue(record.getR17_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR17_c11() != null) {
			cellL.setCellValue(record.getR17_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR17_c12() != null) {
			cellM.setCellValue(record.getR17_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR17_c13() != null) {
			cellN.setCellValue(record.getR17_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR17_c14() != null) {
			cellO.setCellValue(record.getR17_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR17_c15() != null) {
			cellP.setCellValue(record.getR17_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR17_c16() != null) {
			cellQ.setCellValue(record.getR17_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R18
		// -------------------------------------------------------

		row = sheet.getRow(17);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR18_botswana() != null) {
			cellB.setCellValue(record.getR18_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR18_south_africa() != null) {
			cellC.setCellValue(record.getR18_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR18_sadc() != null) {
			cellD.setCellValue(record.getR18_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR18_usa() != null) {
			cellE.setCellValue(record.getR18_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR18_uk() != null) {
			cellF.setCellValue(record.getR18_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR18_europe() != null) {
			cellG.setCellValue(record.getR18_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR18_india() != null) {
			cellH.setCellValue(record.getR18_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR18_sydney() != null) {
			cellI.setCellValue(record.getR18_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR18_uganda() != null) {
			cellJ.setCellValue(record.getR18_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR18_c10() != null) {
			cellK.setCellValue(record.getR18_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR18_c11() != null) {
			cellL.setCellValue(record.getR18_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR18_c12() != null) {
			cellM.setCellValue(record.getR18_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR18_c13() != null) {
			cellN.setCellValue(record.getR18_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR18_c14() != null) {
			cellO.setCellValue(record.getR18_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR18_c15() != null) {
			cellP.setCellValue(record.getR18_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR18_c16() != null) {
			cellQ.setCellValue(record.getR18_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R19
		// -------------------------------------------------------

		row = sheet.getRow(18);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR19_botswana() != null) {
			cellB.setCellValue(record.getR19_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR19_south_africa() != null) {
			cellC.setCellValue(record.getR19_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR19_sadc() != null) {
			cellD.setCellValue(record.getR19_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR19_usa() != null) {
			cellE.setCellValue(record.getR19_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR19_uk() != null) {
			cellF.setCellValue(record.getR19_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR19_europe() != null) {
			cellG.setCellValue(record.getR19_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR19_india() != null) {
			cellH.setCellValue(record.getR19_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR19_sydney() != null) {
			cellI.setCellValue(record.getR19_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR19_uganda() != null) {
			cellJ.setCellValue(record.getR19_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR19_c10() != null) {
			cellK.setCellValue(record.getR19_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR19_c11() != null) {
			cellL.setCellValue(record.getR19_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR19_c12() != null) {
			cellM.setCellValue(record.getR19_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR19_c13() != null) {
			cellN.setCellValue(record.getR19_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR19_c14() != null) {
			cellO.setCellValue(record.getR19_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR19_c15() != null) {
			cellP.setCellValue(record.getR19_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR19_c16() != null) {
			cellQ.setCellValue(record.getR19_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R20
		// -------------------------------------------------------

		row = sheet.getRow(19);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR20_botswana() != null) {
			cellB.setCellValue(record.getR20_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR20_south_africa() != null) {
			cellC.setCellValue(record.getR20_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR20_sadc() != null) {
			cellD.setCellValue(record.getR20_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR20_usa() != null) {
			cellE.setCellValue(record.getR20_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR20_uk() != null) {
			cellF.setCellValue(record.getR20_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR20_europe() != null) {
			cellG.setCellValue(record.getR20_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR20_india() != null) {
			cellH.setCellValue(record.getR20_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR20_sydney() != null) {
			cellI.setCellValue(record.getR20_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR20_uganda() != null) {
			cellJ.setCellValue(record.getR20_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR20_c10() != null) {
			cellK.setCellValue(record.getR20_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR20_c11() != null) {
			cellL.setCellValue(record.getR20_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR20_c12() != null) {
			cellM.setCellValue(record.getR20_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR20_c13() != null) {
			cellN.setCellValue(record.getR20_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR20_c14() != null) {
			cellO.setCellValue(record.getR20_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR20_c15() != null) {
			cellP.setCellValue(record.getR20_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR20_c16() != null) {
			cellQ.setCellValue(record.getR20_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R21
		// -------------------------------------------------------

		row = sheet.getRow(20);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR21_botswana() != null) {
			cellB.setCellValue(record.getR21_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR21_south_africa() != null) {
			cellC.setCellValue(record.getR21_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR21_sadc() != null) {
			cellD.setCellValue(record.getR21_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR21_usa() != null) {
			cellE.setCellValue(record.getR21_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR21_uk() != null) {
			cellF.setCellValue(record.getR21_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR21_europe() != null) {
			cellG.setCellValue(record.getR21_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR21_india() != null) {
			cellH.setCellValue(record.getR21_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR21_sydney() != null) {
			cellI.setCellValue(record.getR21_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR21_uganda() != null) {
			cellJ.setCellValue(record.getR21_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR21_c10() != null) {
			cellK.setCellValue(record.getR21_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR21_c11() != null) {
			cellL.setCellValue(record.getR21_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR21_c12() != null) {
			cellM.setCellValue(record.getR21_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR21_c13() != null) {
			cellN.setCellValue(record.getR21_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR21_c14() != null) {
			cellO.setCellValue(record.getR21_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR21_c15() != null) {
			cellP.setCellValue(record.getR21_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR21_c16() != null) {
			cellQ.setCellValue(record.getR21_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R24
		// -------------------------------------------------------

		row = sheet.getRow(23);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR24_botswana() != null) {
			cellB.setCellValue(record.getR24_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR24_south_africa() != null) {
			cellC.setCellValue(record.getR24_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR24_sadc() != null) {
			cellD.setCellValue(record.getR24_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR24_usa() != null) {
			cellE.setCellValue(record.getR24_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR24_uk() != null) {
			cellF.setCellValue(record.getR24_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR24_europe() != null) {
			cellG.setCellValue(record.getR24_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR24_india() != null) {
			cellH.setCellValue(record.getR24_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR24_sydney() != null) {
			cellI.setCellValue(record.getR24_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR24_uganda() != null) {
			cellJ.setCellValue(record.getR24_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR24_c10() != null) {
			cellK.setCellValue(record.getR24_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR24_c11() != null) {
			cellL.setCellValue(record.getR24_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR24_c12() != null) {
			cellM.setCellValue(record.getR24_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR24_c13() != null) {
			cellN.setCellValue(record.getR24_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR24_c14() != null) {
			cellO.setCellValue(record.getR24_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR24_c15() != null) {
			cellP.setCellValue(record.getR24_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR24_c16() != null) {
			cellQ.setCellValue(record.getR24_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R25
		// -------------------------------------------------------

		row = sheet.getRow(24);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR25_botswana() != null) {
			cellB.setCellValue(record.getR25_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR25_south_africa() != null) {
			cellC.setCellValue(record.getR25_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR25_sadc() != null) {
			cellD.setCellValue(record.getR25_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR25_usa() != null) {
			cellE.setCellValue(record.getR25_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR25_uk() != null) {
			cellF.setCellValue(record.getR25_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR25_europe() != null) {
			cellG.setCellValue(record.getR25_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR25_india() != null) {
			cellH.setCellValue(record.getR25_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR25_sydney() != null) {
			cellI.setCellValue(record.getR25_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR25_uganda() != null) {
			cellJ.setCellValue(record.getR25_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR25_c10() != null) {
			cellK.setCellValue(record.getR25_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR25_c11() != null) {
			cellL.setCellValue(record.getR25_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR25_c12() != null) {
			cellM.setCellValue(record.getR25_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR25_c13() != null) {
			cellN.setCellValue(record.getR25_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR25_c14() != null) {
			cellO.setCellValue(record.getR25_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR25_c15() != null) {
			cellP.setCellValue(record.getR25_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR25_c16() != null) {
			cellQ.setCellValue(record.getR25_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R26
		// -------------------------------------------------------

		row = sheet.getRow(25);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR26_botswana() != null) {
			cellB.setCellValue(record.getR26_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR26_south_africa() != null) {
			cellC.setCellValue(record.getR26_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR26_sadc() != null) {
			cellD.setCellValue(record.getR26_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR26_usa() != null) {
			cellE.setCellValue(record.getR26_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR26_uk() != null) {
			cellF.setCellValue(record.getR26_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR26_europe() != null) {
			cellG.setCellValue(record.getR26_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR26_india() != null) {
			cellH.setCellValue(record.getR26_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR26_sydney() != null) {
			cellI.setCellValue(record.getR26_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR26_uganda() != null) {
			cellJ.setCellValue(record.getR26_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR26_c10() != null) {
			cellK.setCellValue(record.getR26_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR26_c11() != null) {
			cellL.setCellValue(record.getR26_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR26_c12() != null) {
			cellM.setCellValue(record.getR26_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR26_c13() != null) {
			cellN.setCellValue(record.getR26_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR26_c14() != null) {
			cellO.setCellValue(record.getR26_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR26_c15() != null) {
			cellP.setCellValue(record.getR26_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR26_c16() != null) {
			cellQ.setCellValue(record.getR26_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R27
		// -------------------------------------------------------

		row = sheet.getRow(26);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR27_botswana() != null) {
			cellB.setCellValue(record.getR27_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR27_south_africa() != null) {
			cellC.setCellValue(record.getR27_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR27_sadc() != null) {
			cellD.setCellValue(record.getR27_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR27_usa() != null) {
			cellE.setCellValue(record.getR27_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR27_uk() != null) {
			cellF.setCellValue(record.getR27_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR27_europe() != null) {
			cellG.setCellValue(record.getR27_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR27_india() != null) {
			cellH.setCellValue(record.getR27_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR27_sydney() != null) {
			cellI.setCellValue(record.getR27_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR27_uganda() != null) {
			cellJ.setCellValue(record.getR27_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR27_c10() != null) {
			cellK.setCellValue(record.getR27_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR27_c11() != null) {
			cellL.setCellValue(record.getR27_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR27_c12() != null) {
			cellM.setCellValue(record.getR27_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR27_c13() != null) {
			cellN.setCellValue(record.getR27_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR27_c14() != null) {
			cellO.setCellValue(record.getR27_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR27_c15() != null) {
			cellP.setCellValue(record.getR27_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR27_c16() != null) {
			cellQ.setCellValue(record.getR27_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R28
		// -------------------------------------------------------

		row = sheet.getRow(27);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR28_botswana() != null) {
			cellB.setCellValue(record.getR28_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR28_south_africa() != null) {
			cellC.setCellValue(record.getR28_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR28_sadc() != null) {
			cellD.setCellValue(record.getR28_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR28_usa() != null) {
			cellE.setCellValue(record.getR28_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR28_uk() != null) {
			cellF.setCellValue(record.getR28_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR28_europe() != null) {
			cellG.setCellValue(record.getR28_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR28_india() != null) {
			cellH.setCellValue(record.getR28_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR28_sydney() != null) {
			cellI.setCellValue(record.getR28_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR28_uganda() != null) {
			cellJ.setCellValue(record.getR28_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR28_c10() != null) {
			cellK.setCellValue(record.getR28_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR28_c11() != null) {
			cellL.setCellValue(record.getR28_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR28_c12() != null) {
			cellM.setCellValue(record.getR28_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR28_c13() != null) {
			cellN.setCellValue(record.getR28_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR28_c14() != null) {
			cellO.setCellValue(record.getR28_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR28_c15() != null) {
			cellP.setCellValue(record.getR28_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR28_c16() != null) {
			cellQ.setCellValue(record.getR28_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R29
		// -------------------------------------------------------

		row = sheet.getRow(28);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR29_botswana() != null) {
			cellB.setCellValue(record.getR29_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR29_south_africa() != null) {
			cellC.setCellValue(record.getR29_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR29_sadc() != null) {
			cellD.setCellValue(record.getR29_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR29_usa() != null) {
			cellE.setCellValue(record.getR29_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR29_uk() != null) {
			cellF.setCellValue(record.getR29_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR29_europe() != null) {
			cellG.setCellValue(record.getR29_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR29_india() != null) {
			cellH.setCellValue(record.getR29_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR29_sydney() != null) {
			cellI.setCellValue(record.getR29_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR29_uganda() != null) {
			cellJ.setCellValue(record.getR29_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR29_c10() != null) {
			cellK.setCellValue(record.getR29_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR29_c11() != null) {
			cellL.setCellValue(record.getR29_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR29_c12() != null) {
			cellM.setCellValue(record.getR29_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR29_c13() != null) {
			cellN.setCellValue(record.getR29_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR29_c14() != null) {
			cellO.setCellValue(record.getR29_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR29_c15() != null) {
			cellP.setCellValue(record.getR29_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR29_c16() != null) {
			cellQ.setCellValue(record.getR29_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R30
		// -------------------------------------------------------

		row = sheet.getRow(29);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR30_botswana() != null) {
			cellB.setCellValue(record.getR30_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR30_south_africa() != null) {
			cellC.setCellValue(record.getR30_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR30_sadc() != null) {
			cellD.setCellValue(record.getR30_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR30_usa() != null) {
			cellE.setCellValue(record.getR30_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR30_uk() != null) {
			cellF.setCellValue(record.getR30_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR30_europe() != null) {
			cellG.setCellValue(record.getR30_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR30_india() != null) {
			cellH.setCellValue(record.getR30_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR30_sydney() != null) {
			cellI.setCellValue(record.getR30_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR30_uganda() != null) {
			cellJ.setCellValue(record.getR30_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR30_c10() != null) {
			cellK.setCellValue(record.getR30_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR30_c11() != null) {
			cellL.setCellValue(record.getR30_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR30_c12() != null) {
			cellM.setCellValue(record.getR30_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR30_c13() != null) {
			cellN.setCellValue(record.getR30_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR30_c14() != null) {
			cellO.setCellValue(record.getR30_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR30_c15() != null) {
			cellP.setCellValue(record.getR30_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR30_c16() != null) {
			cellQ.setCellValue(record.getR30_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R31
		// -------------------------------------------------------

		row = sheet.getRow(30);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR31_botswana() != null) {
			cellB.setCellValue(record.getR31_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR31_south_africa() != null) {
			cellC.setCellValue(record.getR31_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR31_sadc() != null) {
			cellD.setCellValue(record.getR31_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR31_usa() != null) {
			cellE.setCellValue(record.getR31_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR31_uk() != null) {
			cellF.setCellValue(record.getR31_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR31_europe() != null) {
			cellG.setCellValue(record.getR31_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR31_india() != null) {
			cellH.setCellValue(record.getR31_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR31_sydney() != null) {
			cellI.setCellValue(record.getR31_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR31_uganda() != null) {
			cellJ.setCellValue(record.getR31_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR31_c10() != null) {
			cellK.setCellValue(record.getR31_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR31_c11() != null) {
			cellL.setCellValue(record.getR31_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR31_c12() != null) {
			cellM.setCellValue(record.getR31_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR31_c13() != null) {
			cellN.setCellValue(record.getR31_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR31_c14() != null) {
			cellO.setCellValue(record.getR31_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR31_c15() != null) {
			cellP.setCellValue(record.getR31_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR31_c16() != null) {
			cellQ.setCellValue(record.getR31_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R32
		// -------------------------------------------------------

		row = sheet.getRow(31);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR32_botswana() != null) {
			cellB.setCellValue(record.getR32_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR32_south_africa() != null) {
			cellC.setCellValue(record.getR32_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR32_sadc() != null) {
			cellD.setCellValue(record.getR32_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR32_usa() != null) {
			cellE.setCellValue(record.getR32_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR32_uk() != null) {
			cellF.setCellValue(record.getR32_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR32_europe() != null) {
			cellG.setCellValue(record.getR32_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR32_india() != null) {
			cellH.setCellValue(record.getR32_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR32_sydney() != null) {
			cellI.setCellValue(record.getR32_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR32_uganda() != null) {
			cellJ.setCellValue(record.getR32_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR32_c10() != null) {
			cellK.setCellValue(record.getR32_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR32_c11() != null) {
			cellL.setCellValue(record.getR32_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR32_c12() != null) {
			cellM.setCellValue(record.getR32_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR32_c13() != null) {
			cellN.setCellValue(record.getR32_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR32_c14() != null) {
			cellO.setCellValue(record.getR32_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR32_c15() != null) {
			cellP.setCellValue(record.getR32_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR32_c16() != null) {
			cellQ.setCellValue(record.getR32_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R33
		// -------------------------------------------------------

		row = sheet.getRow(32);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR33_botswana() != null) {
			cellB.setCellValue(record.getR33_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR33_south_africa() != null) {
			cellC.setCellValue(record.getR33_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR33_sadc() != null) {
			cellD.setCellValue(record.getR33_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR33_usa() != null) {
			cellE.setCellValue(record.getR33_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR33_uk() != null) {
			cellF.setCellValue(record.getR33_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR33_europe() != null) {
			cellG.setCellValue(record.getR33_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR33_india() != null) {
			cellH.setCellValue(record.getR33_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR33_sydney() != null) {
			cellI.setCellValue(record.getR33_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR33_uganda() != null) {
			cellJ.setCellValue(record.getR33_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR33_c10() != null) {
			cellK.setCellValue(record.getR33_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR33_c11() != null) {
			cellL.setCellValue(record.getR33_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR33_c12() != null) {
			cellM.setCellValue(record.getR33_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR33_c13() != null) {
			cellN.setCellValue(record.getR33_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR33_c14() != null) {
			cellO.setCellValue(record.getR33_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR33_c15() != null) {
			cellP.setCellValue(record.getR33_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR33_c16() != null) {
			cellQ.setCellValue(record.getR33_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R34
		// -------------------------------------------------------

		row = sheet.getRow(33);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR34_botswana() != null) {
			cellB.setCellValue(record.getR34_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR34_south_africa() != null) {
			cellC.setCellValue(record.getR34_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR34_sadc() != null) {
			cellD.setCellValue(record.getR34_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR34_usa() != null) {
			cellE.setCellValue(record.getR34_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR34_uk() != null) {
			cellF.setCellValue(record.getR34_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR34_europe() != null) {
			cellG.setCellValue(record.getR34_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR34_india() != null) {
			cellH.setCellValue(record.getR34_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR34_sydney() != null) {
			cellI.setCellValue(record.getR34_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR34_uganda() != null) {
			cellJ.setCellValue(record.getR34_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR34_c10() != null) {
			cellK.setCellValue(record.getR34_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR34_c11() != null) {
			cellL.setCellValue(record.getR34_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR34_c12() != null) {
			cellM.setCellValue(record.getR34_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR34_c13() != null) {
			cellN.setCellValue(record.getR34_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR34_c14() != null) {
			cellO.setCellValue(record.getR34_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR34_c15() != null) {
			cellP.setCellValue(record.getR34_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR34_c16() != null) {
			cellQ.setCellValue(record.getR34_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R35
		// -------------------------------------------------------

		row = sheet.getRow(34);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR35_botswana() != null) {
			cellB.setCellValue(record.getR35_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR35_south_africa() != null) {
			cellC.setCellValue(record.getR35_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR35_sadc() != null) {
			cellD.setCellValue(record.getR35_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR35_usa() != null) {
			cellE.setCellValue(record.getR35_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR35_uk() != null) {
			cellF.setCellValue(record.getR35_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR35_europe() != null) {
			cellG.setCellValue(record.getR35_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR35_india() != null) {
			cellH.setCellValue(record.getR35_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR35_sydney() != null) {
			cellI.setCellValue(record.getR35_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR35_uganda() != null) {
			cellJ.setCellValue(record.getR35_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR35_c10() != null) {
			cellK.setCellValue(record.getR35_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR35_c11() != null) {
			cellL.setCellValue(record.getR35_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR35_c12() != null) {
			cellM.setCellValue(record.getR35_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR35_c13() != null) {
			cellN.setCellValue(record.getR35_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR35_c14() != null) {
			cellO.setCellValue(record.getR35_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR35_c15() != null) {
			cellP.setCellValue(record.getR35_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR35_c16() != null) {
			cellQ.setCellValue(record.getR35_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R36
		// -------------------------------------------------------

		row = sheet.getRow(35);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR36_botswana() != null) {
			cellB.setCellValue(record.getR36_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR36_south_africa() != null) {
			cellC.setCellValue(record.getR36_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR36_sadc() != null) {
			cellD.setCellValue(record.getR36_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR36_usa() != null) {
			cellE.setCellValue(record.getR36_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR36_uk() != null) {
			cellF.setCellValue(record.getR36_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR36_europe() != null) {
			cellG.setCellValue(record.getR36_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR36_india() != null) {
			cellH.setCellValue(record.getR36_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR36_sydney() != null) {
			cellI.setCellValue(record.getR36_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR36_uganda() != null) {
			cellJ.setCellValue(record.getR36_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR36_c10() != null) {
			cellK.setCellValue(record.getR36_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR36_c11() != null) {
			cellL.setCellValue(record.getR36_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR36_c12() != null) {
			cellM.setCellValue(record.getR36_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR36_c13() != null) {
			cellN.setCellValue(record.getR36_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR36_c14() != null) {
			cellO.setCellValue(record.getR36_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR36_c15() != null) {
			cellP.setCellValue(record.getR36_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR36_c16() != null) {
			cellQ.setCellValue(record.getR36_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R37
		// -------------------------------------------------------

		row = sheet.getRow(36);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR37_botswana() != null) {
			cellB.setCellValue(record.getR37_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR37_south_africa() != null) {
			cellC.setCellValue(record.getR37_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR37_sadc() != null) {
			cellD.setCellValue(record.getR37_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR37_usa() != null) {
			cellE.setCellValue(record.getR37_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR37_uk() != null) {
			cellF.setCellValue(record.getR37_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR37_europe() != null) {
			cellG.setCellValue(record.getR37_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR37_india() != null) {
			cellH.setCellValue(record.getR37_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR37_sydney() != null) {
			cellI.setCellValue(record.getR37_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR37_uganda() != null) {
			cellJ.setCellValue(record.getR37_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR37_c10() != null) {
			cellK.setCellValue(record.getR37_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR37_c11() != null) {
			cellL.setCellValue(record.getR37_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR37_c12() != null) {
			cellM.setCellValue(record.getR37_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR37_c13() != null) {
			cellN.setCellValue(record.getR37_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR37_c14() != null) {
			cellO.setCellValue(record.getR37_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR37_c15() != null) {
			cellP.setCellValue(record.getR37_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR37_c16() != null) {
			cellQ.setCellValue(record.getR37_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R38
		// -------------------------------------------------------

		row = sheet.getRow(37);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR38_botswana() != null) {
			cellB.setCellValue(record.getR38_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR38_south_africa() != null) {
			cellC.setCellValue(record.getR38_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR38_sadc() != null) {
			cellD.setCellValue(record.getR38_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR38_usa() != null) {
			cellE.setCellValue(record.getR38_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR38_uk() != null) {
			cellF.setCellValue(record.getR38_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR38_europe() != null) {
			cellG.setCellValue(record.getR38_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR38_india() != null) {
			cellH.setCellValue(record.getR38_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR38_sydney() != null) {
			cellI.setCellValue(record.getR38_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR38_uganda() != null) {
			cellJ.setCellValue(record.getR38_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR38_c10() != null) {
			cellK.setCellValue(record.getR38_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR38_c11() != null) {
			cellL.setCellValue(record.getR38_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR38_c12() != null) {
			cellM.setCellValue(record.getR38_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR38_c13() != null) {
			cellN.setCellValue(record.getR38_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR38_c14() != null) {
			cellO.setCellValue(record.getR38_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR38_c15() != null) {
			cellP.setCellValue(record.getR38_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR38_c16() != null) {
			cellQ.setCellValue(record.getR38_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R39
		// -------------------------------------------------------

		row = sheet.getRow(38);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR39_botswana() != null) {
			cellB.setCellValue(record.getR39_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR39_south_africa() != null) {
			cellC.setCellValue(record.getR39_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR39_sadc() != null) {
			cellD.setCellValue(record.getR39_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR39_usa() != null) {
			cellE.setCellValue(record.getR39_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR39_uk() != null) {
			cellF.setCellValue(record.getR39_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR39_europe() != null) {
			cellG.setCellValue(record.getR39_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR39_india() != null) {
			cellH.setCellValue(record.getR39_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR39_sydney() != null) {
			cellI.setCellValue(record.getR39_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR39_uganda() != null) {
			cellJ.setCellValue(record.getR39_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR39_c10() != null) {
			cellK.setCellValue(record.getR39_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR39_c11() != null) {
			cellL.setCellValue(record.getR39_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR39_c12() != null) {
			cellM.setCellValue(record.getR39_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR39_c13() != null) {
			cellN.setCellValue(record.getR39_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR39_c14() != null) {
			cellO.setCellValue(record.getR39_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR39_c15() != null) {
			cellP.setCellValue(record.getR39_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR39_c16() != null) {
			cellQ.setCellValue(record.getR39_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R40
		// -------------------------------------------------------

		row = sheet.getRow(39);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR40_botswana() != null) {
			cellB.setCellValue(record.getR40_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR40_south_africa() != null) {
			cellC.setCellValue(record.getR40_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR40_sadc() != null) {
			cellD.setCellValue(record.getR40_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR40_usa() != null) {
			cellE.setCellValue(record.getR40_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR40_uk() != null) {
			cellF.setCellValue(record.getR40_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR40_europe() != null) {
			cellG.setCellValue(record.getR40_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR40_india() != null) {
			cellH.setCellValue(record.getR40_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR40_sydney() != null) {
			cellI.setCellValue(record.getR40_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR40_uganda() != null) {
			cellJ.setCellValue(record.getR40_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR40_c10() != null) {
			cellK.setCellValue(record.getR40_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR40_c11() != null) {
			cellL.setCellValue(record.getR40_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR40_c12() != null) {
			cellM.setCellValue(record.getR40_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR40_c13() != null) {
			cellN.setCellValue(record.getR40_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR40_c14() != null) {
			cellO.setCellValue(record.getR40_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR40_c15() != null) {
			cellP.setCellValue(record.getR40_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR40_c16() != null) {
			cellQ.setCellValue(record.getR40_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R41
		// -------------------------------------------------------

		row = sheet.getRow(40);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR41_botswana() != null) {
			cellB.setCellValue(record.getR41_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR41_south_africa() != null) {
			cellC.setCellValue(record.getR41_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR41_sadc() != null) {
			cellD.setCellValue(record.getR41_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR41_usa() != null) {
			cellE.setCellValue(record.getR41_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR41_uk() != null) {
			cellF.setCellValue(record.getR41_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR41_europe() != null) {
			cellG.setCellValue(record.getR41_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR41_india() != null) {
			cellH.setCellValue(record.getR41_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR41_sydney() != null) {
			cellI.setCellValue(record.getR41_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR41_uganda() != null) {
			cellJ.setCellValue(record.getR41_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR41_c10() != null) {
			cellK.setCellValue(record.getR41_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR41_c11() != null) {
			cellL.setCellValue(record.getR41_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR41_c12() != null) {
			cellM.setCellValue(record.getR41_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR41_c13() != null) {
			cellN.setCellValue(record.getR41_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR41_c14() != null) {
			cellO.setCellValue(record.getR41_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR41_c15() != null) {
			cellP.setCellValue(record.getR41_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR41_c16() != null) {
			cellQ.setCellValue(record.getR41_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R42
		// -------------------------------------------------------

		row = sheet.getRow(41);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR42_botswana() != null) {
			cellB.setCellValue(record.getR42_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR42_south_africa() != null) {
			cellC.setCellValue(record.getR42_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR42_sadc() != null) {
			cellD.setCellValue(record.getR42_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR42_usa() != null) {
			cellE.setCellValue(record.getR42_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR42_uk() != null) {
			cellF.setCellValue(record.getR42_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR42_europe() != null) {
			cellG.setCellValue(record.getR42_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR42_india() != null) {
			cellH.setCellValue(record.getR42_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR42_sydney() != null) {
			cellI.setCellValue(record.getR42_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR42_uganda() != null) {
			cellJ.setCellValue(record.getR42_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR42_c10() != null) {
			cellK.setCellValue(record.getR42_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR42_c11() != null) {
			cellL.setCellValue(record.getR42_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR42_c12() != null) {
			cellM.setCellValue(record.getR42_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR42_c13() != null) {
			cellN.setCellValue(record.getR42_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR42_c14() != null) {
			cellO.setCellValue(record.getR42_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR42_c15() != null) {
			cellP.setCellValue(record.getR42_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR42_c16() != null) {
			cellQ.setCellValue(record.getR42_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R43
		// -------------------------------------------------------

		row = sheet.getRow(42);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR43_botswana() != null) {
			cellB.setCellValue(record.getR43_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR43_south_africa() != null) {
			cellC.setCellValue(record.getR43_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR43_sadc() != null) {
			cellD.setCellValue(record.getR43_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR43_usa() != null) {
			cellE.setCellValue(record.getR43_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR43_uk() != null) {
			cellF.setCellValue(record.getR43_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR43_europe() != null) {
			cellG.setCellValue(record.getR43_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR43_india() != null) {
			cellH.setCellValue(record.getR43_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR43_sydney() != null) {
			cellI.setCellValue(record.getR43_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR43_uganda() != null) {
			cellJ.setCellValue(record.getR43_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR43_c10() != null) {
			cellK.setCellValue(record.getR43_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR43_c11() != null) {
			cellL.setCellValue(record.getR43_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR43_c12() != null) {
			cellM.setCellValue(record.getR43_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR43_c13() != null) {
			cellN.setCellValue(record.getR43_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR43_c14() != null) {
			cellO.setCellValue(record.getR43_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR43_c15() != null) {
			cellP.setCellValue(record.getR43_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR43_c16() != null) {
			cellQ.setCellValue(record.getR43_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R44
		// -------------------------------------------------------

		row = sheet.getRow(43);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR44_botswana() != null) {
			cellB.setCellValue(record.getR44_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR44_south_africa() != null) {
			cellC.setCellValue(record.getR44_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR44_sadc() != null) {
			cellD.setCellValue(record.getR44_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR44_usa() != null) {
			cellE.setCellValue(record.getR44_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR44_uk() != null) {
			cellF.setCellValue(record.getR44_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR44_europe() != null) {
			cellG.setCellValue(record.getR44_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR44_india() != null) {
			cellH.setCellValue(record.getR44_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR44_sydney() != null) {
			cellI.setCellValue(record.getR44_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR44_uganda() != null) {
			cellJ.setCellValue(record.getR44_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR44_c10() != null) {
			cellK.setCellValue(record.getR44_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR44_c11() != null) {
			cellL.setCellValue(record.getR44_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR44_c12() != null) {
			cellM.setCellValue(record.getR44_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR44_c13() != null) {
			cellN.setCellValue(record.getR44_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR44_c14() != null) {
			cellO.setCellValue(record.getR44_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR44_c15() != null) {
			cellP.setCellValue(record.getR44_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR44_c16() != null) {
			cellQ.setCellValue(record.getR44_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R45
		// -------------------------------------------------------

		row = sheet.getRow(44);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR45_botswana() != null) {
			cellB.setCellValue(record.getR45_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR45_south_africa() != null) {
			cellC.setCellValue(record.getR45_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR45_sadc() != null) {
			cellD.setCellValue(record.getR45_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR45_usa() != null) {
			cellE.setCellValue(record.getR45_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR45_uk() != null) {
			cellF.setCellValue(record.getR45_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR45_europe() != null) {
			cellG.setCellValue(record.getR45_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR45_india() != null) {
			cellH.setCellValue(record.getR45_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR45_sydney() != null) {
			cellI.setCellValue(record.getR45_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR45_uganda() != null) {
			cellJ.setCellValue(record.getR45_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR45_c10() != null) {
			cellK.setCellValue(record.getR45_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR45_c11() != null) {
			cellL.setCellValue(record.getR45_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR45_c12() != null) {
			cellM.setCellValue(record.getR45_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR45_c13() != null) {
			cellN.setCellValue(record.getR45_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR45_c14() != null) {
			cellO.setCellValue(record.getR45_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR45_c15() != null) {
			cellP.setCellValue(record.getR45_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR45_c16() != null) {
			cellQ.setCellValue(record.getR45_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R46
		// -------------------------------------------------------

		row = sheet.getRow(45);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR46_botswana() != null) {
			cellB.setCellValue(record.getR46_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR46_south_africa() != null) {
			cellC.setCellValue(record.getR46_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR46_sadc() != null) {
			cellD.setCellValue(record.getR46_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR46_usa() != null) {
			cellE.setCellValue(record.getR46_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR46_uk() != null) {
			cellF.setCellValue(record.getR46_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR46_europe() != null) {
			cellG.setCellValue(record.getR46_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR46_india() != null) {
			cellH.setCellValue(record.getR46_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR46_sydney() != null) {
			cellI.setCellValue(record.getR46_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR46_uganda() != null) {
			cellJ.setCellValue(record.getR46_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR46_c10() != null) {
			cellK.setCellValue(record.getR46_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR46_c11() != null) {
			cellL.setCellValue(record.getR46_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR46_c12() != null) {
			cellM.setCellValue(record.getR46_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR46_c13() != null) {
			cellN.setCellValue(record.getR46_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR46_c14() != null) {
			cellO.setCellValue(record.getR46_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR46_c15() != null) {
			cellP.setCellValue(record.getR46_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR46_c16() != null) {
			cellQ.setCellValue(record.getR46_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R47
		// -------------------------------------------------------

		row = sheet.getRow(46);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR47_botswana() != null) {
			cellB.setCellValue(record.getR47_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR47_south_africa() != null) {
			cellC.setCellValue(record.getR47_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR47_sadc() != null) {
			cellD.setCellValue(record.getR47_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR47_usa() != null) {
			cellE.setCellValue(record.getR47_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR47_uk() != null) {
			cellF.setCellValue(record.getR47_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR47_europe() != null) {
			cellG.setCellValue(record.getR47_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR47_india() != null) {
			cellH.setCellValue(record.getR47_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR47_sydney() != null) {
			cellI.setCellValue(record.getR47_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR47_uganda() != null) {
			cellJ.setCellValue(record.getR47_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR47_c10() != null) {
			cellK.setCellValue(record.getR47_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR47_c11() != null) {
			cellL.setCellValue(record.getR47_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR47_c12() != null) {
			cellM.setCellValue(record.getR47_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR47_c13() != null) {
			cellN.setCellValue(record.getR47_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR47_c14() != null) {
			cellO.setCellValue(record.getR47_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR47_c15() != null) {
			cellP.setCellValue(record.getR47_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR47_c16() != null) {
			cellQ.setCellValue(record.getR47_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R48
		// -------------------------------------------------------

		row = sheet.getRow(47);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR48_botswana() != null) {
			cellB.setCellValue(record.getR48_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR48_south_africa() != null) {
			cellC.setCellValue(record.getR48_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR48_sadc() != null) {
			cellD.setCellValue(record.getR48_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR48_usa() != null) {
			cellE.setCellValue(record.getR48_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR48_uk() != null) {
			cellF.setCellValue(record.getR48_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR48_europe() != null) {
			cellG.setCellValue(record.getR48_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR48_india() != null) {
			cellH.setCellValue(record.getR48_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR48_sydney() != null) {
			cellI.setCellValue(record.getR48_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR48_uganda() != null) {
			cellJ.setCellValue(record.getR48_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR48_c10() != null) {
			cellK.setCellValue(record.getR48_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR48_c11() != null) {
			cellL.setCellValue(record.getR48_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR48_c12() != null) {
			cellM.setCellValue(record.getR48_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR48_c13() != null) {
			cellN.setCellValue(record.getR48_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR48_c14() != null) {
			cellO.setCellValue(record.getR48_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR48_c15() != null) {
			cellP.setCellValue(record.getR48_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR48_c16() != null) {
			cellQ.setCellValue(record.getR48_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R49
		// -------------------------------------------------------

		row = sheet.getRow(48);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR49_botswana() != null) {
			cellB.setCellValue(record.getR49_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR49_south_africa() != null) {
			cellC.setCellValue(record.getR49_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR49_sadc() != null) {
			cellD.setCellValue(record.getR49_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR49_usa() != null) {
			cellE.setCellValue(record.getR49_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR49_uk() != null) {
			cellF.setCellValue(record.getR49_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR49_europe() != null) {
			cellG.setCellValue(record.getR49_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR49_india() != null) {
			cellH.setCellValue(record.getR49_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR49_sydney() != null) {
			cellI.setCellValue(record.getR49_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR49_uganda() != null) {
			cellJ.setCellValue(record.getR49_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR49_c10() != null) {
			cellK.setCellValue(record.getR49_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR49_c11() != null) {
			cellL.setCellValue(record.getR49_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR49_c12() != null) {
			cellM.setCellValue(record.getR49_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR49_c13() != null) {
			cellN.setCellValue(record.getR49_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR49_c14() != null) {
			cellO.setCellValue(record.getR49_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR49_c15() != null) {
			cellP.setCellValue(record.getR49_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR49_c16() != null) {
			cellQ.setCellValue(record.getR49_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R50
		// -------------------------------------------------------

		row = sheet.getRow(49);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR50_botswana() != null) {
			cellB.setCellValue(record.getR50_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR50_south_africa() != null) {
			cellC.setCellValue(record.getR50_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR50_sadc() != null) {
			cellD.setCellValue(record.getR50_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR50_usa() != null) {
			cellE.setCellValue(record.getR50_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR50_uk() != null) {
			cellF.setCellValue(record.getR50_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR50_europe() != null) {
			cellG.setCellValue(record.getR50_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR50_india() != null) {
			cellH.setCellValue(record.getR50_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR50_sydney() != null) {
			cellI.setCellValue(record.getR50_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR50_uganda() != null) {
			cellJ.setCellValue(record.getR50_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR50_c10() != null) {
			cellK.setCellValue(record.getR50_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR50_c11() != null) {
			cellL.setCellValue(record.getR50_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR50_c12() != null) {
			cellM.setCellValue(record.getR50_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR50_c13() != null) {
			cellN.setCellValue(record.getR50_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR50_c14() != null) {
			cellO.setCellValue(record.getR50_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR50_c15() != null) {
			cellP.setCellValue(record.getR50_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR50_c16() != null) {
			cellQ.setCellValue(record.getR50_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R51
		// -------------------------------------------------------

		row = sheet.getRow(50);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR51_botswana() != null) {
			cellB.setCellValue(record.getR51_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR51_south_africa() != null) {
			cellC.setCellValue(record.getR51_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR51_sadc() != null) {
			cellD.setCellValue(record.getR51_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR51_usa() != null) {
			cellE.setCellValue(record.getR51_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR51_uk() != null) {
			cellF.setCellValue(record.getR51_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR51_europe() != null) {
			cellG.setCellValue(record.getR51_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR51_india() != null) {
			cellH.setCellValue(record.getR51_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR51_sydney() != null) {
			cellI.setCellValue(record.getR51_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR51_uganda() != null) {
			cellJ.setCellValue(record.getR51_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR51_c10() != null) {
			cellK.setCellValue(record.getR51_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR51_c11() != null) {
			cellL.setCellValue(record.getR51_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR51_c12() != null) {
			cellM.setCellValue(record.getR51_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR51_c13() != null) {
			cellN.setCellValue(record.getR51_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR51_c14() != null) {
			cellO.setCellValue(record.getR51_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR51_c15() != null) {
			cellP.setCellValue(record.getR51_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR51_c16() != null) {
			cellQ.setCellValue(record.getR51_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R52
		// -------------------------------------------------------

		row = sheet.getRow(51);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR52_botswana() != null) {
			cellB.setCellValue(record.getR52_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR52_south_africa() != null) {
			cellC.setCellValue(record.getR52_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR52_sadc() != null) {
			cellD.setCellValue(record.getR52_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR52_usa() != null) {
			cellE.setCellValue(record.getR52_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR52_uk() != null) {
			cellF.setCellValue(record.getR52_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR52_europe() != null) {
			cellG.setCellValue(record.getR52_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR52_india() != null) {
			cellH.setCellValue(record.getR52_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR52_sydney() != null) {
			cellI.setCellValue(record.getR52_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR52_uganda() != null) {
			cellJ.setCellValue(record.getR52_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR52_c10() != null) {
			cellK.setCellValue(record.getR52_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR52_c11() != null) {
			cellL.setCellValue(record.getR52_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR52_c12() != null) {
			cellM.setCellValue(record.getR52_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR52_c13() != null) {
			cellN.setCellValue(record.getR52_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR52_c14() != null) {
			cellO.setCellValue(record.getR52_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR52_c15() != null) {
			cellP.setCellValue(record.getR52_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR52_c16() != null) {
			cellQ.setCellValue(record.getR52_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R53
		// -------------------------------------------------------

		row = sheet.getRow(52);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR53_botswana() != null) {
			cellB.setCellValue(record.getR53_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR53_south_africa() != null) {
			cellC.setCellValue(record.getR53_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR53_sadc() != null) {
			cellD.setCellValue(record.getR53_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR53_usa() != null) {
			cellE.setCellValue(record.getR53_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR53_uk() != null) {
			cellF.setCellValue(record.getR53_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR53_europe() != null) {
			cellG.setCellValue(record.getR53_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR53_india() != null) {
			cellH.setCellValue(record.getR53_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR53_sydney() != null) {
			cellI.setCellValue(record.getR53_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR53_uganda() != null) {
			cellJ.setCellValue(record.getR53_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR53_c10() != null) {
			cellK.setCellValue(record.getR53_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR53_c11() != null) {
			cellL.setCellValue(record.getR53_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR53_c12() != null) {
			cellM.setCellValue(record.getR53_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR53_c13() != null) {
			cellN.setCellValue(record.getR53_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR53_c14() != null) {
			cellO.setCellValue(record.getR53_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR53_c15() != null) {
			cellP.setCellValue(record.getR53_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR53_c16() != null) {
			cellQ.setCellValue(record.getR53_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R54
		// -------------------------------------------------------

		row = sheet.getRow(53);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR54_botswana() != null) {
			cellB.setCellValue(record.getR54_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR54_south_africa() != null) {
			cellC.setCellValue(record.getR54_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR54_sadc() != null) {
			cellD.setCellValue(record.getR54_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR54_usa() != null) {
			cellE.setCellValue(record.getR54_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR54_uk() != null) {
			cellF.setCellValue(record.getR54_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR54_europe() != null) {
			cellG.setCellValue(record.getR54_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR54_india() != null) {
			cellH.setCellValue(record.getR54_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR54_sydney() != null) {
			cellI.setCellValue(record.getR54_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR54_uganda() != null) {
			cellJ.setCellValue(record.getR54_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR54_c10() != null) {
			cellK.setCellValue(record.getR54_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR54_c11() != null) {
			cellL.setCellValue(record.getR54_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR54_c12() != null) {
			cellM.setCellValue(record.getR54_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR54_c13() != null) {
			cellN.setCellValue(record.getR54_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR54_c14() != null) {
			cellO.setCellValue(record.getR54_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR54_c15() != null) {
			cellP.setCellValue(record.getR54_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR54_c16() != null) {
			cellQ.setCellValue(record.getR54_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R55
		// -------------------------------------------------------

		row = sheet.getRow(54);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR55_botswana() != null) {
			cellB.setCellValue(record.getR55_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR55_south_africa() != null) {
			cellC.setCellValue(record.getR55_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR55_sadc() != null) {
			cellD.setCellValue(record.getR55_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR55_usa() != null) {
			cellE.setCellValue(record.getR55_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR55_uk() != null) {
			cellF.setCellValue(record.getR55_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR55_europe() != null) {
			cellG.setCellValue(record.getR55_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR55_india() != null) {
			cellH.setCellValue(record.getR55_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR55_sydney() != null) {
			cellI.setCellValue(record.getR55_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR55_uganda() != null) {
			cellJ.setCellValue(record.getR55_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR55_c10() != null) {
			cellK.setCellValue(record.getR55_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR55_c11() != null) {
			cellL.setCellValue(record.getR55_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR55_c12() != null) {
			cellM.setCellValue(record.getR55_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR55_c13() != null) {
			cellN.setCellValue(record.getR55_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR55_c14() != null) {
			cellO.setCellValue(record.getR55_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR55_c15() != null) {
			cellP.setCellValue(record.getR55_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR55_c16() != null) {
			cellQ.setCellValue(record.getR55_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R56
		// -------------------------------------------------------

		row = sheet.getRow(55);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR56_botswana() != null) {
			cellB.setCellValue(record.getR56_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR56_south_africa() != null) {
			cellC.setCellValue(record.getR56_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR56_sadc() != null) {
			cellD.setCellValue(record.getR56_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR56_usa() != null) {
			cellE.setCellValue(record.getR56_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR56_uk() != null) {
			cellF.setCellValue(record.getR56_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR56_europe() != null) {
			cellG.setCellValue(record.getR56_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR56_india() != null) {
			cellH.setCellValue(record.getR56_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR56_sydney() != null) {
			cellI.setCellValue(record.getR56_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR56_uganda() != null) {
			cellJ.setCellValue(record.getR56_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR56_c10() != null) {
			cellK.setCellValue(record.getR56_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR56_c11() != null) {
			cellL.setCellValue(record.getR56_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR56_c12() != null) {
			cellM.setCellValue(record.getR56_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR56_c13() != null) {
			cellN.setCellValue(record.getR56_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR56_c14() != null) {
			cellO.setCellValue(record.getR56_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR56_c15() != null) {
			cellP.setCellValue(record.getR56_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR56_c16() != null) {
			cellQ.setCellValue(record.getR56_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R59
		// -------------------------------------------------------

		row = sheet.getRow(58);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record.getR59_botswana() != null) {
			cellB.setCellValue(record.getR59_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record.getR59_south_africa() != null) {
			cellC.setCellValue(record.getR59_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record.getR59_sadc() != null) {
			cellD.setCellValue(record.getR59_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record.getR59_usa() != null) {
			cellE.setCellValue(record.getR59_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record.getR59_uk() != null) {
			cellF.setCellValue(record.getR59_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record.getR59_europe() != null) {
			cellG.setCellValue(record.getR59_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record.getR59_india() != null) {
			cellH.setCellValue(record.getR59_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record.getR59_sydney() != null) {
			cellI.setCellValue(record.getR59_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record.getR59_uganda() != null) {
			cellJ.setCellValue(record.getR59_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record.getR59_c10() != null) {
			cellK.setCellValue(record.getR59_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record.getR59_c11() != null) {
			cellL.setCellValue(record.getR59_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record.getR59_c12() != null) {
			cellM.setCellValue(record.getR59_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record.getR59_c13() != null) {
			cellN.setCellValue(record.getR59_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record.getR59_c14() != null) {
			cellO.setCellValue(record.getR59_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record.getR59_c15() != null) {
			cellP.setCellValue(record.getR59_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record.getR59_c16() != null) {
			cellQ.setCellValue(record.getR59_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

	}

	private void populateEntity2Data(Sheet sheet, M_GALOR_Summary_Entity2 record1, CellStyle textStyle,
			CellStyle numberStyle) {

		// R70 - ROW 70 (Index 69)
		Row row = sheet.getRow(69) != null ? sheet.getRow(69) : sheet.createRow(69);

		Cell cellB, cellC, cellD, cellE, cellF, cellG, cellH, cellI, cellJ, cellK, cellL, cellM, cellN, cellO, cellP,
				cellQ;

		// -------------------------------------------------------
		// ROW R70 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(69);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR70_botswana() != null) {
			cellB.setCellValue(record1.getR70_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR70_south_africa() != null) {
			cellC.setCellValue(record1.getR70_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR70_sadc() != null) {
			cellD.setCellValue(record1.getR70_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR70_usa() != null) {
			cellE.setCellValue(record1.getR70_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR70_uk() != null) {
			cellF.setCellValue(record1.getR70_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR70_europe() != null) {
			cellG.setCellValue(record1.getR70_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR70_india() != null) {
			cellH.setCellValue(record1.getR70_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR70_sydney() != null) {
			cellI.setCellValue(record1.getR70_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR70_uganda() != null) {
			cellJ.setCellValue(record1.getR70_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR70_c10() != null) {
			cellK.setCellValue(record1.getR70_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR70_c11() != null) {
			cellL.setCellValue(record1.getR70_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR70_c12() != null) {
			cellM.setCellValue(record1.getR70_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR70_c13() != null) {
			cellN.setCellValue(record1.getR70_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR70_c14() != null) {
			cellO.setCellValue(record1.getR70_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR70_c15() != null) {
			cellP.setCellValue(record1.getR70_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR70_c16() != null) {
			cellQ.setCellValue(record1.getR70_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R71 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(70);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR71_botswana() != null) {
			cellB.setCellValue(record1.getR71_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR71_south_africa() != null) {
			cellC.setCellValue(record1.getR71_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR71_sadc() != null) {
			cellD.setCellValue(record1.getR71_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR71_usa() != null) {
			cellE.setCellValue(record1.getR71_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR71_uk() != null) {
			cellF.setCellValue(record1.getR71_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR71_europe() != null) {
			cellG.setCellValue(record1.getR71_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR71_india() != null) {
			cellH.setCellValue(record1.getR71_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR71_sydney() != null) {
			cellI.setCellValue(record1.getR71_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR71_uganda() != null) {
			cellJ.setCellValue(record1.getR71_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR71_c10() != null) {
			cellK.setCellValue(record1.getR71_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR71_c11() != null) {
			cellL.setCellValue(record1.getR71_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR71_c12() != null) {
			cellM.setCellValue(record1.getR71_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR71_c13() != null) {
			cellN.setCellValue(record1.getR71_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR71_c14() != null) {
			cellO.setCellValue(record1.getR71_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR71_c15() != null) {
			cellP.setCellValue(record1.getR71_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR71_c16() != null) {
			cellQ.setCellValue(record1.getR71_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R72 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(71);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR72_botswana() != null) {
			cellB.setCellValue(record1.getR72_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR72_south_africa() != null) {
			cellC.setCellValue(record1.getR72_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR72_sadc() != null) {
			cellD.setCellValue(record1.getR72_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR72_usa() != null) {
			cellE.setCellValue(record1.getR72_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR72_uk() != null) {
			cellF.setCellValue(record1.getR72_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR72_europe() != null) {
			cellG.setCellValue(record1.getR72_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR72_india() != null) {
			cellH.setCellValue(record1.getR72_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR72_sydney() != null) {
			cellI.setCellValue(record1.getR72_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR72_uganda() != null) {
			cellJ.setCellValue(record1.getR72_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR72_c10() != null) {
			cellK.setCellValue(record1.getR72_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR72_c11() != null) {
			cellL.setCellValue(record1.getR72_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR72_c12() != null) {
			cellM.setCellValue(record1.getR72_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR72_c13() != null) {
			cellN.setCellValue(record1.getR72_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR72_c14() != null) {
			cellO.setCellValue(record1.getR72_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR72_c15() != null) {
			cellP.setCellValue(record1.getR72_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR72_c16() != null) {
			cellQ.setCellValue(record1.getR72_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R73 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(72);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR73_botswana() != null) {
			cellB.setCellValue(record1.getR73_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR73_south_africa() != null) {
			cellC.setCellValue(record1.getR73_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR73_sadc() != null) {
			cellD.setCellValue(record1.getR73_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR73_usa() != null) {
			cellE.setCellValue(record1.getR73_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR73_uk() != null) {
			cellF.setCellValue(record1.getR73_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR73_europe() != null) {
			cellG.setCellValue(record1.getR73_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR73_india() != null) {
			cellH.setCellValue(record1.getR73_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR73_sydney() != null) {
			cellI.setCellValue(record1.getR73_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR73_uganda() != null) {
			cellJ.setCellValue(record1.getR73_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR73_c10() != null) {
			cellK.setCellValue(record1.getR73_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR73_c11() != null) {
			cellL.setCellValue(record1.getR73_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR73_c12() != null) {
			cellM.setCellValue(record1.getR73_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR73_c13() != null) {
			cellN.setCellValue(record1.getR73_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR73_c14() != null) {
			cellO.setCellValue(record1.getR73_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR73_c15() != null) {
			cellP.setCellValue(record1.getR73_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR73_c16() != null) {
			cellQ.setCellValue(record1.getR73_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R74 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(73);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR74_botswana() != null) {
			cellB.setCellValue(record1.getR74_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR74_south_africa() != null) {
			cellC.setCellValue(record1.getR74_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR74_sadc() != null) {
			cellD.setCellValue(record1.getR74_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR74_usa() != null) {
			cellE.setCellValue(record1.getR74_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR74_uk() != null) {
			cellF.setCellValue(record1.getR74_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR74_europe() != null) {
			cellG.setCellValue(record1.getR74_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR74_india() != null) {
			cellH.setCellValue(record1.getR74_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR74_sydney() != null) {
			cellI.setCellValue(record1.getR74_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR74_uganda() != null) {
			cellJ.setCellValue(record1.getR74_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR74_c10() != null) {
			cellK.setCellValue(record1.getR74_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR74_c11() != null) {
			cellL.setCellValue(record1.getR74_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR74_c12() != null) {
			cellM.setCellValue(record1.getR74_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR74_c13() != null) {
			cellN.setCellValue(record1.getR74_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR74_c14() != null) {
			cellO.setCellValue(record1.getR74_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR74_c15() != null) {
			cellP.setCellValue(record1.getR74_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR74_c16() != null) {
			cellQ.setCellValue(record1.getR74_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R75 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(74);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR75_botswana() != null) {
			cellB.setCellValue(record1.getR75_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR75_south_africa() != null) {
			cellC.setCellValue(record1.getR75_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR75_sadc() != null) {
			cellD.setCellValue(record1.getR75_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR75_usa() != null) {
			cellE.setCellValue(record1.getR75_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR75_uk() != null) {
			cellF.setCellValue(record1.getR75_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR75_europe() != null) {
			cellG.setCellValue(record1.getR75_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR75_india() != null) {
			cellH.setCellValue(record1.getR75_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR75_sydney() != null) {
			cellI.setCellValue(record1.getR75_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR75_uganda() != null) {
			cellJ.setCellValue(record1.getR75_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR75_c10() != null) {
			cellK.setCellValue(record1.getR75_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR75_c11() != null) {
			cellL.setCellValue(record1.getR75_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR75_c12() != null) {
			cellM.setCellValue(record1.getR75_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR75_c13() != null) {
			cellN.setCellValue(record1.getR75_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR75_c14() != null) {
			cellO.setCellValue(record1.getR75_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR75_c15() != null) {
			cellP.setCellValue(record1.getR75_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR75_c16() != null) {
			cellQ.setCellValue(record1.getR75_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R76 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(75);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR76_botswana() != null) {
			cellB.setCellValue(record1.getR76_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR76_south_africa() != null) {
			cellC.setCellValue(record1.getR76_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR76_sadc() != null) {
			cellD.setCellValue(record1.getR76_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR76_usa() != null) {
			cellE.setCellValue(record1.getR76_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR76_uk() != null) {
			cellF.setCellValue(record1.getR76_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR76_europe() != null) {
			cellG.setCellValue(record1.getR76_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR76_india() != null) {
			cellH.setCellValue(record1.getR76_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR76_sydney() != null) {
			cellI.setCellValue(record1.getR76_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR76_uganda() != null) {
			cellJ.setCellValue(record1.getR76_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR76_c10() != null) {
			cellK.setCellValue(record1.getR76_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR76_c11() != null) {
			cellL.setCellValue(record1.getR76_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR76_c12() != null) {
			cellM.setCellValue(record1.getR76_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR76_c13() != null) {
			cellN.setCellValue(record1.getR76_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR76_c14() != null) {
			cellO.setCellValue(record1.getR76_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR76_c15() != null) {
			cellP.setCellValue(record1.getR76_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR76_c16() != null) {
			cellQ.setCellValue(record1.getR76_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R77 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(76);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR77_botswana() != null) {
			cellB.setCellValue(record1.getR77_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR77_south_africa() != null) {
			cellC.setCellValue(record1.getR77_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR77_sadc() != null) {
			cellD.setCellValue(record1.getR77_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR77_usa() != null) {
			cellE.setCellValue(record1.getR77_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR77_uk() != null) {
			cellF.setCellValue(record1.getR77_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR77_europe() != null) {
			cellG.setCellValue(record1.getR77_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR77_india() != null) {
			cellH.setCellValue(record1.getR77_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR77_sydney() != null) {
			cellI.setCellValue(record1.getR77_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR77_uganda() != null) {
			cellJ.setCellValue(record1.getR77_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR77_c10() != null) {
			cellK.setCellValue(record1.getR77_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR77_c11() != null) {
			cellL.setCellValue(record1.getR77_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR77_c12() != null) {
			cellM.setCellValue(record1.getR77_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR77_c13() != null) {
			cellN.setCellValue(record1.getR77_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR77_c14() != null) {
			cellO.setCellValue(record1.getR77_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR77_c15() != null) {
			cellP.setCellValue(record1.getR77_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR77_c16() != null) {
			cellQ.setCellValue(record1.getR77_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R78 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(77);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR78_botswana() != null) {
			cellB.setCellValue(record1.getR78_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR78_south_africa() != null) {
			cellC.setCellValue(record1.getR78_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR78_sadc() != null) {
			cellD.setCellValue(record1.getR78_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR78_usa() != null) {
			cellE.setCellValue(record1.getR78_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR78_uk() != null) {
			cellF.setCellValue(record1.getR78_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR78_europe() != null) {
			cellG.setCellValue(record1.getR78_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR78_india() != null) {
			cellH.setCellValue(record1.getR78_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR78_sydney() != null) {
			cellI.setCellValue(record1.getR78_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR78_uganda() != null) {
			cellJ.setCellValue(record1.getR78_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR78_c10() != null) {
			cellK.setCellValue(record1.getR78_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR78_c11() != null) {
			cellL.setCellValue(record1.getR78_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR78_c12() != null) {
			cellM.setCellValue(record1.getR78_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR78_c13() != null) {
			cellN.setCellValue(record1.getR78_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR78_c14() != null) {
			cellO.setCellValue(record1.getR78_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR78_c15() != null) {
			cellP.setCellValue(record1.getR78_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR78_c16() != null) {
			cellQ.setCellValue(record1.getR78_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R79 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(78);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR79_botswana() != null) {
			cellB.setCellValue(record1.getR79_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR79_south_africa() != null) {
			cellC.setCellValue(record1.getR79_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR79_sadc() != null) {
			cellD.setCellValue(record1.getR79_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR79_usa() != null) {
			cellE.setCellValue(record1.getR79_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR79_uk() != null) {
			cellF.setCellValue(record1.getR79_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR79_europe() != null) {
			cellG.setCellValue(record1.getR79_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR79_india() != null) {
			cellH.setCellValue(record1.getR79_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR79_sydney() != null) {
			cellI.setCellValue(record1.getR79_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR79_uganda() != null) {
			cellJ.setCellValue(record1.getR79_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR79_c10() != null) {
			cellK.setCellValue(record1.getR79_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR79_c11() != null) {
			cellL.setCellValue(record1.getR79_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR79_c12() != null) {
			cellM.setCellValue(record1.getR79_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR79_c13() != null) {
			cellN.setCellValue(record1.getR79_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR79_c14() != null) {
			cellO.setCellValue(record1.getR79_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR79_c15() != null) {
			cellP.setCellValue(record1.getR79_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR79_c16() != null) {
			cellQ.setCellValue(record1.getR79_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R80 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(79);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR80_botswana() != null) {
			cellB.setCellValue(record1.getR80_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR80_south_africa() != null) {
			cellC.setCellValue(record1.getR80_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR80_sadc() != null) {
			cellD.setCellValue(record1.getR80_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR80_usa() != null) {
			cellE.setCellValue(record1.getR80_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR80_uk() != null) {
			cellF.setCellValue(record1.getR80_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR80_europe() != null) {
			cellG.setCellValue(record1.getR80_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR80_india() != null) {
			cellH.setCellValue(record1.getR80_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR80_sydney() != null) {
			cellI.setCellValue(record1.getR80_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR80_uganda() != null) {
			cellJ.setCellValue(record1.getR80_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR80_c10() != null) {
			cellK.setCellValue(record1.getR80_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR80_c11() != null) {
			cellL.setCellValue(record1.getR80_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR80_c12() != null) {
			cellM.setCellValue(record1.getR80_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR80_c13() != null) {
			cellN.setCellValue(record1.getR80_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR80_c14() != null) {
			cellO.setCellValue(record1.getR80_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR80_c15() != null) {
			cellP.setCellValue(record1.getR80_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR80_c16() != null) {
			cellQ.setCellValue(record1.getR80_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R81 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(80);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR81_botswana() != null) {
			cellB.setCellValue(record1.getR81_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR81_south_africa() != null) {
			cellC.setCellValue(record1.getR81_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR81_sadc() != null) {
			cellD.setCellValue(record1.getR81_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR81_usa() != null) {
			cellE.setCellValue(record1.getR81_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR81_uk() != null) {
			cellF.setCellValue(record1.getR81_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR81_europe() != null) {
			cellG.setCellValue(record1.getR81_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR81_india() != null) {
			cellH.setCellValue(record1.getR81_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR81_sydney() != null) {
			cellI.setCellValue(record1.getR81_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR81_uganda() != null) {
			cellJ.setCellValue(record1.getR81_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR81_c10() != null) {
			cellK.setCellValue(record1.getR81_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR81_c11() != null) {
			cellL.setCellValue(record1.getR81_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR81_c12() != null) {
			cellM.setCellValue(record1.getR81_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR81_c13() != null) {
			cellN.setCellValue(record1.getR81_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR81_c14() != null) {
			cellO.setCellValue(record1.getR81_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR81_c15() != null) {
			cellP.setCellValue(record1.getR81_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR81_c16() != null) {
			cellQ.setCellValue(record1.getR81_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R82 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(81);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR82_botswana() != null) {
			cellB.setCellValue(record1.getR82_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR82_south_africa() != null) {
			cellC.setCellValue(record1.getR82_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR82_sadc() != null) {
			cellD.setCellValue(record1.getR82_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR82_usa() != null) {
			cellE.setCellValue(record1.getR82_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR82_uk() != null) {
			cellF.setCellValue(record1.getR82_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR82_europe() != null) {
			cellG.setCellValue(record1.getR82_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR82_india() != null) {
			cellH.setCellValue(record1.getR82_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR82_sydney() != null) {
			cellI.setCellValue(record1.getR82_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR82_uganda() != null) {
			cellJ.setCellValue(record1.getR82_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR82_c10() != null) {
			cellK.setCellValue(record1.getR82_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR82_c11() != null) {
			cellL.setCellValue(record1.getR82_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR82_c12() != null) {
			cellM.setCellValue(record1.getR82_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR82_c13() != null) {
			cellN.setCellValue(record1.getR82_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR82_c14() != null) {
			cellO.setCellValue(record1.getR82_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR82_c15() != null) {
			cellP.setCellValue(record1.getR82_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR82_c16() != null) {
			cellQ.setCellValue(record1.getR82_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R83 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(82);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR83_botswana() != null) {
			cellB.setCellValue(record1.getR83_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR83_south_africa() != null) {
			cellC.setCellValue(record1.getR83_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR83_sadc() != null) {
			cellD.setCellValue(record1.getR83_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR83_usa() != null) {
			cellE.setCellValue(record1.getR83_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR83_uk() != null) {
			cellF.setCellValue(record1.getR83_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR83_europe() != null) {
			cellG.setCellValue(record1.getR83_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR83_india() != null) {
			cellH.setCellValue(record1.getR83_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR83_sydney() != null) {
			cellI.setCellValue(record1.getR83_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR83_uganda() != null) {
			cellJ.setCellValue(record1.getR83_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR83_c10() != null) {
			cellK.setCellValue(record1.getR83_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR83_c11() != null) {
			cellL.setCellValue(record1.getR83_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR83_c12() != null) {
			cellM.setCellValue(record1.getR83_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR83_c13() != null) {
			cellN.setCellValue(record1.getR83_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR83_c14() != null) {
			cellO.setCellValue(record1.getR83_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR83_c15() != null) {
			cellP.setCellValue(record1.getR83_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR83_c16() != null) {
			cellQ.setCellValue(record1.getR83_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R84 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(83);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR84_botswana() != null) {
			cellB.setCellValue(record1.getR84_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR84_south_africa() != null) {
			cellC.setCellValue(record1.getR84_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR84_sadc() != null) {
			cellD.setCellValue(record1.getR84_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR84_usa() != null) {
			cellE.setCellValue(record1.getR84_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR84_uk() != null) {
			cellF.setCellValue(record1.getR84_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR84_europe() != null) {
			cellG.setCellValue(record1.getR84_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR84_india() != null) {
			cellH.setCellValue(record1.getR84_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR84_sydney() != null) {
			cellI.setCellValue(record1.getR84_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR84_uganda() != null) {
			cellJ.setCellValue(record1.getR84_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR84_c10() != null) {
			cellK.setCellValue(record1.getR84_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR84_c11() != null) {
			cellL.setCellValue(record1.getR84_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR84_c12() != null) {
			cellM.setCellValue(record1.getR84_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR84_c13() != null) {
			cellN.setCellValue(record1.getR84_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR84_c14() != null) {
			cellO.setCellValue(record1.getR84_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR84_c15() != null) {
			cellP.setCellValue(record1.getR84_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR84_c16() != null) {
			cellQ.setCellValue(record1.getR84_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R85 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(84);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR85_botswana() != null) {
			cellB.setCellValue(record1.getR85_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR85_south_africa() != null) {
			cellC.setCellValue(record1.getR85_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR85_sadc() != null) {
			cellD.setCellValue(record1.getR85_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR85_usa() != null) {
			cellE.setCellValue(record1.getR85_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR85_uk() != null) {
			cellF.setCellValue(record1.getR85_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR85_europe() != null) {
			cellG.setCellValue(record1.getR85_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR85_india() != null) {
			cellH.setCellValue(record1.getR85_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR85_sydney() != null) {
			cellI.setCellValue(record1.getR85_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR85_uganda() != null) {
			cellJ.setCellValue(record1.getR85_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR85_c10() != null) {
			cellK.setCellValue(record1.getR85_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR85_c11() != null) {
			cellL.setCellValue(record1.getR85_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR85_c12() != null) {
			cellM.setCellValue(record1.getR85_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR85_c13() != null) {
			cellN.setCellValue(record1.getR85_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR85_c14() != null) {
			cellO.setCellValue(record1.getR85_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR85_c15() != null) {
			cellP.setCellValue(record1.getR85_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR85_c16() != null) {
			cellQ.setCellValue(record1.getR85_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R86 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(85);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR86_botswana() != null) {
			cellB.setCellValue(record1.getR86_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR86_south_africa() != null) {
			cellC.setCellValue(record1.getR86_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR86_sadc() != null) {
			cellD.setCellValue(record1.getR86_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR86_usa() != null) {
			cellE.setCellValue(record1.getR86_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR86_uk() != null) {
			cellF.setCellValue(record1.getR86_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR86_europe() != null) {
			cellG.setCellValue(record1.getR86_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR86_india() != null) {
			cellH.setCellValue(record1.getR86_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR86_sydney() != null) {
			cellI.setCellValue(record1.getR86_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR86_uganda() != null) {
			cellJ.setCellValue(record1.getR86_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR86_c10() != null) {
			cellK.setCellValue(record1.getR86_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR86_c11() != null) {
			cellL.setCellValue(record1.getR86_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR86_c12() != null) {
			cellM.setCellValue(record1.getR86_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR86_c13() != null) {
			cellN.setCellValue(record1.getR86_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR86_c14() != null) {
			cellO.setCellValue(record1.getR86_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR86_c15() != null) {
			cellP.setCellValue(record1.getR86_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR86_c16() != null) {
			cellQ.setCellValue(record1.getR86_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R87 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(86);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR87_botswana() != null) {
			cellB.setCellValue(record1.getR87_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR87_south_africa() != null) {
			cellC.setCellValue(record1.getR87_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR87_sadc() != null) {
			cellD.setCellValue(record1.getR87_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR87_usa() != null) {
			cellE.setCellValue(record1.getR87_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR87_uk() != null) {
			cellF.setCellValue(record1.getR87_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR87_europe() != null) {
			cellG.setCellValue(record1.getR87_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR87_india() != null) {
			cellH.setCellValue(record1.getR87_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR87_sydney() != null) {
			cellI.setCellValue(record1.getR87_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR87_uganda() != null) {
			cellJ.setCellValue(record1.getR87_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR87_c10() != null) {
			cellK.setCellValue(record1.getR87_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR87_c11() != null) {
			cellL.setCellValue(record1.getR87_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR87_c12() != null) {
			cellM.setCellValue(record1.getR87_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR87_c13() != null) {
			cellN.setCellValue(record1.getR87_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR87_c14() != null) {
			cellO.setCellValue(record1.getR87_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR87_c15() != null) {
			cellP.setCellValue(record1.getR87_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR87_c16() != null) {
			cellQ.setCellValue(record1.getR87_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R88 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(87);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR88_botswana() != null) {
			cellB.setCellValue(record1.getR88_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR88_south_africa() != null) {
			cellC.setCellValue(record1.getR88_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR88_sadc() != null) {
			cellD.setCellValue(record1.getR88_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR88_usa() != null) {
			cellE.setCellValue(record1.getR88_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR88_uk() != null) {
			cellF.setCellValue(record1.getR88_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR88_europe() != null) {
			cellG.setCellValue(record1.getR88_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR88_india() != null) {
			cellH.setCellValue(record1.getR88_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR88_sydney() != null) {
			cellI.setCellValue(record1.getR88_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR88_uganda() != null) {
			cellJ.setCellValue(record1.getR88_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR88_c10() != null) {
			cellK.setCellValue(record1.getR88_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR88_c11() != null) {
			cellL.setCellValue(record1.getR88_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR88_c12() != null) {
			cellM.setCellValue(record1.getR88_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR88_c13() != null) {
			cellN.setCellValue(record1.getR88_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR88_c14() != null) {
			cellO.setCellValue(record1.getR88_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR88_c15() != null) {
			cellP.setCellValue(record1.getR88_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR88_c16() != null) {
			cellQ.setCellValue(record1.getR88_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R89 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(88);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR89_botswana() != null) {
			cellB.setCellValue(record1.getR89_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR89_south_africa() != null) {
			cellC.setCellValue(record1.getR89_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR89_sadc() != null) {
			cellD.setCellValue(record1.getR89_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR89_usa() != null) {
			cellE.setCellValue(record1.getR89_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR89_uk() != null) {
			cellF.setCellValue(record1.getR89_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR89_europe() != null) {
			cellG.setCellValue(record1.getR89_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR89_india() != null) {
			cellH.setCellValue(record1.getR89_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR89_sydney() != null) {
			cellI.setCellValue(record1.getR89_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR89_uganda() != null) {
			cellJ.setCellValue(record1.getR89_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR89_c10() != null) {
			cellK.setCellValue(record1.getR89_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR89_c11() != null) {
			cellL.setCellValue(record1.getR89_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR89_c12() != null) {
			cellM.setCellValue(record1.getR89_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR89_c13() != null) {
			cellN.setCellValue(record1.getR89_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR89_c14() != null) {
			cellO.setCellValue(record1.getR89_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR89_c15() != null) {
			cellP.setCellValue(record1.getR89_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR89_c16() != null) {
			cellQ.setCellValue(record1.getR89_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R90 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(89);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR90_botswana() != null) {
			cellB.setCellValue(record1.getR90_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR90_south_africa() != null) {
			cellC.setCellValue(record1.getR90_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR90_sadc() != null) {
			cellD.setCellValue(record1.getR90_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR90_usa() != null) {
			cellE.setCellValue(record1.getR90_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR90_uk() != null) {
			cellF.setCellValue(record1.getR90_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR90_europe() != null) {
			cellG.setCellValue(record1.getR90_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR90_india() != null) {
			cellH.setCellValue(record1.getR90_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR90_sydney() != null) {
			cellI.setCellValue(record1.getR90_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR90_uganda() != null) {
			cellJ.setCellValue(record1.getR90_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR90_c10() != null) {
			cellK.setCellValue(record1.getR90_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR90_c11() != null) {
			cellL.setCellValue(record1.getR90_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR90_c12() != null) {
			cellM.setCellValue(record1.getR90_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR90_c13() != null) {
			cellN.setCellValue(record1.getR90_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR90_c14() != null) {
			cellO.setCellValue(record1.getR90_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR90_c15() != null) {
			cellP.setCellValue(record1.getR90_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR90_c16() != null) {
			cellQ.setCellValue(record1.getR90_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R91 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(90);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR91_botswana() != null) {
			cellB.setCellValue(record1.getR91_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR91_south_africa() != null) {
			cellC.setCellValue(record1.getR91_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR91_sadc() != null) {
			cellD.setCellValue(record1.getR91_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR91_usa() != null) {
			cellE.setCellValue(record1.getR91_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR91_uk() != null) {
			cellF.setCellValue(record1.getR91_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR91_europe() != null) {
			cellG.setCellValue(record1.getR91_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR91_india() != null) {
			cellH.setCellValue(record1.getR91_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR91_sydney() != null) {
			cellI.setCellValue(record1.getR91_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR91_uganda() != null) {
			cellJ.setCellValue(record1.getR91_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR91_c10() != null) {
			cellK.setCellValue(record1.getR91_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR91_c11() != null) {
			cellL.setCellValue(record1.getR91_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR91_c12() != null) {
			cellM.setCellValue(record1.getR91_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR91_c13() != null) {
			cellN.setCellValue(record1.getR91_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR91_c14() != null) {
			cellO.setCellValue(record1.getR91_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR91_c15() != null) {
			cellP.setCellValue(record1.getR91_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR91_c16() != null) {
			cellQ.setCellValue(record1.getR91_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R92 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(91);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR92_botswana() != null) {
			cellB.setCellValue(record1.getR92_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR92_south_africa() != null) {
			cellC.setCellValue(record1.getR92_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR92_sadc() != null) {
			cellD.setCellValue(record1.getR92_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR92_usa() != null) {
			cellE.setCellValue(record1.getR92_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR92_uk() != null) {
			cellF.setCellValue(record1.getR92_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR92_europe() != null) {
			cellG.setCellValue(record1.getR92_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR92_india() != null) {
			cellH.setCellValue(record1.getR92_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR92_sydney() != null) {
			cellI.setCellValue(record1.getR92_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR92_uganda() != null) {
			cellJ.setCellValue(record1.getR92_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR92_c10() != null) {
			cellK.setCellValue(record1.getR92_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR92_c11() != null) {
			cellL.setCellValue(record1.getR92_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR92_c12() != null) {
			cellM.setCellValue(record1.getR92_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR92_c13() != null) {
			cellN.setCellValue(record1.getR92_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR92_c14() != null) {
			cellO.setCellValue(record1.getR92_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR92_c15() != null) {
			cellP.setCellValue(record1.getR92_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR92_c16() != null) {
			cellQ.setCellValue(record1.getR92_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R93 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(92);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR93_botswana() != null) {
			cellB.setCellValue(record1.getR93_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR93_south_africa() != null) {
			cellC.setCellValue(record1.getR93_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR93_sadc() != null) {
			cellD.setCellValue(record1.getR93_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR93_usa() != null) {
			cellE.setCellValue(record1.getR93_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR93_uk() != null) {
			cellF.setCellValue(record1.getR93_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR93_europe() != null) {
			cellG.setCellValue(record1.getR93_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR93_india() != null) {
			cellH.setCellValue(record1.getR93_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR93_sydney() != null) {
			cellI.setCellValue(record1.getR93_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR93_uganda() != null) {
			cellJ.setCellValue(record1.getR93_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR93_c10() != null) {
			cellK.setCellValue(record1.getR93_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR93_c11() != null) {
			cellL.setCellValue(record1.getR93_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR93_c12() != null) {
			cellM.setCellValue(record1.getR93_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR93_c13() != null) {
			cellN.setCellValue(record1.getR93_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR93_c14() != null) {
			cellO.setCellValue(record1.getR93_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR93_c15() != null) {
			cellP.setCellValue(record1.getR93_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR93_c16() != null) {
			cellQ.setCellValue(record1.getR93_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R94 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(93);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR94_botswana() != null) {
			cellB.setCellValue(record1.getR94_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR94_south_africa() != null) {
			cellC.setCellValue(record1.getR94_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR94_sadc() != null) {
			cellD.setCellValue(record1.getR94_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR94_usa() != null) {
			cellE.setCellValue(record1.getR94_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR94_uk() != null) {
			cellF.setCellValue(record1.getR94_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR94_europe() != null) {
			cellG.setCellValue(record1.getR94_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR94_india() != null) {
			cellH.setCellValue(record1.getR94_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR94_sydney() != null) {
			cellI.setCellValue(record1.getR94_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR94_uganda() != null) {
			cellJ.setCellValue(record1.getR94_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR94_c10() != null) {
			cellK.setCellValue(record1.getR94_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR94_c11() != null) {
			cellL.setCellValue(record1.getR94_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR94_c12() != null) {
			cellM.setCellValue(record1.getR94_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR94_c13() != null) {
			cellN.setCellValue(record1.getR94_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR94_c14() != null) {
			cellO.setCellValue(record1.getR94_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR94_c15() != null) {
			cellP.setCellValue(record1.getR94_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR94_c16() != null) {
			cellQ.setCellValue(record1.getR94_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R95 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(94);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR95_botswana() != null) {
			cellB.setCellValue(record1.getR95_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR95_south_africa() != null) {
			cellC.setCellValue(record1.getR95_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR95_sadc() != null) {
			cellD.setCellValue(record1.getR95_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR95_usa() != null) {
			cellE.setCellValue(record1.getR95_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR95_uk() != null) {
			cellF.setCellValue(record1.getR95_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR95_europe() != null) {
			cellG.setCellValue(record1.getR95_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR95_india() != null) {
			cellH.setCellValue(record1.getR95_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR95_sydney() != null) {
			cellI.setCellValue(record1.getR95_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR95_uganda() != null) {
			cellJ.setCellValue(record1.getR95_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR95_c10() != null) {
			cellK.setCellValue(record1.getR95_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR95_c11() != null) {
			cellL.setCellValue(record1.getR95_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR95_c12() != null) {
			cellM.setCellValue(record1.getR95_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR95_c13() != null) {
			cellN.setCellValue(record1.getR95_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR95_c14() != null) {
			cellO.setCellValue(record1.getR95_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR95_c15() != null) {
			cellP.setCellValue(record1.getR95_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR95_c16() != null) {
			cellQ.setCellValue(record1.getR95_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R96 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(95);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR96_botswana() != null) {
			cellB.setCellValue(record1.getR96_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR96_south_africa() != null) {
			cellC.setCellValue(record1.getR96_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR96_sadc() != null) {
			cellD.setCellValue(record1.getR96_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR96_usa() != null) {
			cellE.setCellValue(record1.getR96_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR96_uk() != null) {
			cellF.setCellValue(record1.getR96_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR96_europe() != null) {
			cellG.setCellValue(record1.getR96_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR96_india() != null) {
			cellH.setCellValue(record1.getR96_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR96_sydney() != null) {
			cellI.setCellValue(record1.getR96_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR96_uganda() != null) {
			cellJ.setCellValue(record1.getR96_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR96_c10() != null) {
			cellK.setCellValue(record1.getR96_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR96_c11() != null) {
			cellL.setCellValue(record1.getR96_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR96_c12() != null) {
			cellM.setCellValue(record1.getR96_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR96_c13() != null) {
			cellN.setCellValue(record1.getR96_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR96_c14() != null) {
			cellO.setCellValue(record1.getR96_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR96_c15() != null) {
			cellP.setCellValue(record1.getR96_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR96_c16() != null) {
			cellQ.setCellValue(record1.getR96_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R97 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(96);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR97_botswana() != null) {
			cellB.setCellValue(record1.getR97_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR97_south_africa() != null) {
			cellC.setCellValue(record1.getR97_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR97_sadc() != null) {
			cellD.setCellValue(record1.getR97_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR97_usa() != null) {
			cellE.setCellValue(record1.getR97_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR97_uk() != null) {
			cellF.setCellValue(record1.getR97_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR97_europe() != null) {
			cellG.setCellValue(record1.getR97_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR97_india() != null) {
			cellH.setCellValue(record1.getR97_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR97_sydney() != null) {
			cellI.setCellValue(record1.getR97_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR97_uganda() != null) {
			cellJ.setCellValue(record1.getR97_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR97_c10() != null) {
			cellK.setCellValue(record1.getR97_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR97_c11() != null) {
			cellL.setCellValue(record1.getR97_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR97_c12() != null) {
			cellM.setCellValue(record1.getR97_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR97_c13() != null) {
			cellN.setCellValue(record1.getR97_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR97_c14() != null) {
			cellO.setCellValue(record1.getR97_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR97_c15() != null) {
			cellP.setCellValue(record1.getR97_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR97_c16() != null) {
			cellQ.setCellValue(record1.getR97_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R98 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(97);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR98_botswana() != null) {
			cellB.setCellValue(record1.getR98_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR98_south_africa() != null) {
			cellC.setCellValue(record1.getR98_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR98_sadc() != null) {
			cellD.setCellValue(record1.getR98_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR98_usa() != null) {
			cellE.setCellValue(record1.getR98_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR98_uk() != null) {
			cellF.setCellValue(record1.getR98_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR98_europe() != null) {
			cellG.setCellValue(record1.getR98_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR98_india() != null) {
			cellH.setCellValue(record1.getR98_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR98_sydney() != null) {
			cellI.setCellValue(record1.getR98_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR98_uganda() != null) {
			cellJ.setCellValue(record1.getR98_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR98_c10() != null) {
			cellK.setCellValue(record1.getR98_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR98_c11() != null) {
			cellL.setCellValue(record1.getR98_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR98_c12() != null) {
			cellM.setCellValue(record1.getR98_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR98_c13() != null) {
			cellN.setCellValue(record1.getR98_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR98_c14() != null) {
			cellO.setCellValue(record1.getR98_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR98_c15() != null) {
			cellP.setCellValue(record1.getR98_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR98_c16() != null) {
			cellQ.setCellValue(record1.getR98_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R99 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(98);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR99_botswana() != null) {
			cellB.setCellValue(record1.getR99_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR99_south_africa() != null) {
			cellC.setCellValue(record1.getR99_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR99_sadc() != null) {
			cellD.setCellValue(record1.getR99_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR99_usa() != null) {
			cellE.setCellValue(record1.getR99_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR99_uk() != null) {
			cellF.setCellValue(record1.getR99_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR99_europe() != null) {
			cellG.setCellValue(record1.getR99_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR99_india() != null) {
			cellH.setCellValue(record1.getR99_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR99_sydney() != null) {
			cellI.setCellValue(record1.getR99_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR99_uganda() != null) {
			cellJ.setCellValue(record1.getR99_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR99_c10() != null) {
			cellK.setCellValue(record1.getR99_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR99_c11() != null) {
			cellL.setCellValue(record1.getR99_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR99_c12() != null) {
			cellM.setCellValue(record1.getR99_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR99_c13() != null) {
			cellN.setCellValue(record1.getR99_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR99_c14() != null) {
			cellO.setCellValue(record1.getR99_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR99_c15() != null) {
			cellP.setCellValue(record1.getR99_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR99_c16() != null) {
			cellQ.setCellValue(record1.getR99_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R100 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(99);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR100_botswana() != null) {
			cellB.setCellValue(record1.getR100_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR100_south_africa() != null) {
			cellC.setCellValue(record1.getR100_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR100_sadc() != null) {
			cellD.setCellValue(record1.getR100_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR100_usa() != null) {
			cellE.setCellValue(record1.getR100_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR100_uk() != null) {
			cellF.setCellValue(record1.getR100_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR100_europe() != null) {
			cellG.setCellValue(record1.getR100_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR100_india() != null) {
			cellH.setCellValue(record1.getR100_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR100_sydney() != null) {
			cellI.setCellValue(record1.getR100_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR100_uganda() != null) {
			cellJ.setCellValue(record1.getR100_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR100_c10() != null) {
			cellK.setCellValue(record1.getR100_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR100_c11() != null) {
			cellL.setCellValue(record1.getR100_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR100_c12() != null) {
			cellM.setCellValue(record1.getR100_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR100_c13() != null) {
			cellN.setCellValue(record1.getR100_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR100_c14() != null) {
			cellO.setCellValue(record1.getR100_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR100_c15() != null) {
			cellP.setCellValue(record1.getR100_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR100_c16() != null) {
			cellQ.setCellValue(record1.getR100_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R101 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(100);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR101_botswana() != null) {
			cellB.setCellValue(record1.getR101_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR101_south_africa() != null) {
			cellC.setCellValue(record1.getR101_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR101_sadc() != null) {
			cellD.setCellValue(record1.getR101_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR101_usa() != null) {
			cellE.setCellValue(record1.getR101_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR101_uk() != null) {
			cellF.setCellValue(record1.getR101_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR101_europe() != null) {
			cellG.setCellValue(record1.getR101_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR101_india() != null) {
			cellH.setCellValue(record1.getR101_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR101_sydney() != null) {
			cellI.setCellValue(record1.getR101_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR101_uganda() != null) {
			cellJ.setCellValue(record1.getR101_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR101_c10() != null) {
			cellK.setCellValue(record1.getR101_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR101_c11() != null) {
			cellL.setCellValue(record1.getR101_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR101_c12() != null) {
			cellM.setCellValue(record1.getR101_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR101_c13() != null) {
			cellN.setCellValue(record1.getR101_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR101_c14() != null) {
			cellO.setCellValue(record1.getR101_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR101_c15() != null) {
			cellP.setCellValue(record1.getR101_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR101_c16() != null) {
			cellQ.setCellValue(record1.getR101_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R102 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(101);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR102_botswana() != null) {
			cellB.setCellValue(record1.getR102_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR102_south_africa() != null) {
			cellC.setCellValue(record1.getR102_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR102_sadc() != null) {
			cellD.setCellValue(record1.getR102_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR102_usa() != null) {
			cellE.setCellValue(record1.getR102_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR102_uk() != null) {
			cellF.setCellValue(record1.getR102_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR102_europe() != null) {
			cellG.setCellValue(record1.getR102_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR102_india() != null) {
			cellH.setCellValue(record1.getR102_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR102_sydney() != null) {
			cellI.setCellValue(record1.getR102_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR102_uganda() != null) {
			cellJ.setCellValue(record1.getR102_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR102_c10() != null) {
			cellK.setCellValue(record1.getR102_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR102_c11() != null) {
			cellL.setCellValue(record1.getR102_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR102_c12() != null) {
			cellM.setCellValue(record1.getR102_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR102_c13() != null) {
			cellN.setCellValue(record1.getR102_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR102_c14() != null) {
			cellO.setCellValue(record1.getR102_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR102_c15() != null) {
			cellP.setCellValue(record1.getR102_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR102_c16() != null) {
			cellQ.setCellValue(record1.getR102_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R103 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(102);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR103_botswana() != null) {
			cellB.setCellValue(record1.getR103_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR103_south_africa() != null) {
			cellC.setCellValue(record1.getR103_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR103_sadc() != null) {
			cellD.setCellValue(record1.getR103_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR103_usa() != null) {
			cellE.setCellValue(record1.getR103_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR103_uk() != null) {
			cellF.setCellValue(record1.getR103_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR103_europe() != null) {
			cellG.setCellValue(record1.getR103_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR103_india() != null) {
			cellH.setCellValue(record1.getR103_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR103_sydney() != null) {
			cellI.setCellValue(record1.getR103_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR103_uganda() != null) {
			cellJ.setCellValue(record1.getR103_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR103_c10() != null) {
			cellK.setCellValue(record1.getR103_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR103_c11() != null) {
			cellL.setCellValue(record1.getR103_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR103_c12() != null) {
			cellM.setCellValue(record1.getR103_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR103_c13() != null) {
			cellN.setCellValue(record1.getR103_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR103_c14() != null) {
			cellO.setCellValue(record1.getR103_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR103_c15() != null) {
			cellP.setCellValue(record1.getR103_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR103_c16() != null) {
			cellQ.setCellValue(record1.getR103_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R104 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(103);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR104_botswana() != null) {
			cellB.setCellValue(record1.getR104_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR104_south_africa() != null) {
			cellC.setCellValue(record1.getR104_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR104_sadc() != null) {
			cellD.setCellValue(record1.getR104_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR104_usa() != null) {
			cellE.setCellValue(record1.getR104_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR104_uk() != null) {
			cellF.setCellValue(record1.getR104_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR104_europe() != null) {
			cellG.setCellValue(record1.getR104_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR104_india() != null) {
			cellH.setCellValue(record1.getR104_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR104_sydney() != null) {
			cellI.setCellValue(record1.getR104_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR104_uganda() != null) {
			cellJ.setCellValue(record1.getR104_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR104_c10() != null) {
			cellK.setCellValue(record1.getR104_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR104_c11() != null) {
			cellL.setCellValue(record1.getR104_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR104_c12() != null) {
			cellM.setCellValue(record1.getR104_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR104_c13() != null) {
			cellN.setCellValue(record1.getR104_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR104_c14() != null) {
			cellO.setCellValue(record1.getR104_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR104_c15() != null) {
			cellP.setCellValue(record1.getR104_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR104_c16() != null) {
			cellQ.setCellValue(record1.getR104_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R105 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(104);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR105_botswana() != null) {
			cellB.setCellValue(record1.getR105_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR105_south_africa() != null) {
			cellC.setCellValue(record1.getR105_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR105_sadc() != null) {
			cellD.setCellValue(record1.getR105_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR105_usa() != null) {
			cellE.setCellValue(record1.getR105_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR105_uk() != null) {
			cellF.setCellValue(record1.getR105_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR105_europe() != null) {
			cellG.setCellValue(record1.getR105_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR105_india() != null) {
			cellH.setCellValue(record1.getR105_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR105_sydney() != null) {
			cellI.setCellValue(record1.getR105_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR105_uganda() != null) {
			cellJ.setCellValue(record1.getR105_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR105_c10() != null) {
			cellK.setCellValue(record1.getR105_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR105_c11() != null) {
			cellL.setCellValue(record1.getR105_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR105_c12() != null) {
			cellM.setCellValue(record1.getR105_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR105_c13() != null) {
			cellN.setCellValue(record1.getR105_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR105_c14() != null) {
			cellO.setCellValue(record1.getR105_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR105_c15() != null) {
			cellP.setCellValue(record1.getR105_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR105_c16() != null) {
			cellQ.setCellValue(record1.getR105_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R106 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(105);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR106_botswana() != null) {
			cellB.setCellValue(record1.getR106_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR106_south_africa() != null) {
			cellC.setCellValue(record1.getR106_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR106_sadc() != null) {
			cellD.setCellValue(record1.getR106_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR106_usa() != null) {
			cellE.setCellValue(record1.getR106_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR106_uk() != null) {
			cellF.setCellValue(record1.getR106_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR106_europe() != null) {
			cellG.setCellValue(record1.getR106_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR106_india() != null) {
			cellH.setCellValue(record1.getR106_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR106_sydney() != null) {
			cellI.setCellValue(record1.getR106_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR106_uganda() != null) {
			cellJ.setCellValue(record1.getR106_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR106_c10() != null) {
			cellK.setCellValue(record1.getR106_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR106_c11() != null) {
			cellL.setCellValue(record1.getR106_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR106_c12() != null) {
			cellM.setCellValue(record1.getR106_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR106_c13() != null) {
			cellN.setCellValue(record1.getR106_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR106_c14() != null) {
			cellO.setCellValue(record1.getR106_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR106_c15() != null) {
			cellP.setCellValue(record1.getR106_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR106_c16() != null) {
			cellQ.setCellValue(record1.getR106_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R107 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(106);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR107_botswana() != null) {
			cellB.setCellValue(record1.getR107_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR107_south_africa() != null) {
			cellC.setCellValue(record1.getR107_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR107_sadc() != null) {
			cellD.setCellValue(record1.getR107_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR107_usa() != null) {
			cellE.setCellValue(record1.getR107_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR107_uk() != null) {
			cellF.setCellValue(record1.getR107_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR107_europe() != null) {
			cellG.setCellValue(record1.getR107_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR107_india() != null) {
			cellH.setCellValue(record1.getR107_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR107_sydney() != null) {
			cellI.setCellValue(record1.getR107_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR107_uganda() != null) {
			cellJ.setCellValue(record1.getR107_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR107_c10() != null) {
			cellK.setCellValue(record1.getR107_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR107_c11() != null) {
			cellL.setCellValue(record1.getR107_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR107_c12() != null) {
			cellM.setCellValue(record1.getR107_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR107_c13() != null) {
			cellN.setCellValue(record1.getR107_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR107_c14() != null) {
			cellO.setCellValue(record1.getR107_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR107_c15() != null) {
			cellP.setCellValue(record1.getR107_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR107_c16() != null) {
			cellQ.setCellValue(record1.getR107_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R108 --> USING RECORD1 CALCULATE
		// -------------------------------------------------------

		// -------------------------------------------------------
		// ROW R109 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(108);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR109_botswana() != null) {
			cellB.setCellValue(record1.getR109_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR109_south_africa() != null) {
			cellC.setCellValue(record1.getR109_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR109_sadc() != null) {
			cellD.setCellValue(record1.getR109_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR109_usa() != null) {
			cellE.setCellValue(record1.getR109_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR109_uk() != null) {
			cellF.setCellValue(record1.getR109_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR109_europe() != null) {
			cellG.setCellValue(record1.getR109_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR109_india() != null) {
			cellH.setCellValue(record1.getR109_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR109_sydney() != null) {
			cellI.setCellValue(record1.getR109_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR109_uganda() != null) {
			cellJ.setCellValue(record1.getR109_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR109_c10() != null) {
			cellK.setCellValue(record1.getR109_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR109_c11() != null) {
			cellL.setCellValue(record1.getR109_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR109_c12() != null) {
			cellM.setCellValue(record1.getR109_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR109_c13() != null) {
			cellN.setCellValue(record1.getR109_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR109_c14() != null) {
			cellO.setCellValue(record1.getR109_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR109_c15() != null) {
			cellP.setCellValue(record1.getR109_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR109_c16() != null) {
			cellQ.setCellValue(record1.getR109_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R110 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(109);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record1.getR110_botswana() != null) {
			cellB.setCellValue(record1.getR110_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR110_south_africa() != null) {
			cellC.setCellValue(record1.getR110_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR110_sadc() != null) {
			cellD.setCellValue(record1.getR110_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR110_usa() != null) {
			cellE.setCellValue(record1.getR110_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR110_uk() != null) {
			cellF.setCellValue(record1.getR110_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR110_europe() != null) {
			cellG.setCellValue(record1.getR110_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR110_india() != null) {
			cellH.setCellValue(record1.getR110_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR110_sydney() != null) {
			cellI.setCellValue(record1.getR110_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR110_uganda() != null) {
			cellJ.setCellValue(record1.getR110_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR110_c10() != null) {
			cellK.setCellValue(record1.getR110_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR110_c11() != null) {
			cellL.setCellValue(record1.getR110_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR110_c12() != null) {
			cellM.setCellValue(record1.getR110_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR110_c13() != null) {
			cellN.setCellValue(record1.getR110_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR110_c14() != null) {
			cellO.setCellValue(record1.getR110_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR110_c15() != null) {
			cellP.setCellValue(record1.getR110_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR110_c16() != null) {
			cellQ.setCellValue(record1.getR110_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R111 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(110);

		/*
		 * // Column B - BOTSWANA cellB = row.createCell(1); if
		 * (record2.getR111_botswana() != null) {
		 * cellB.setCellValue(record2.getR111_botswana().doubleValue());
		 * cellB.setCellStyle(numberStyle); } else { cellB.setCellValue("");
		 * cellB.setCellStyle(textStyle); }
		 * 
		 */
		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR111_south_africa() != null) {
			cellC.setCellValue(record1.getR111_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR111_sadc() != null) {
			cellD.setCellValue(record1.getR111_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR111_usa() != null) {
			cellE.setCellValue(record1.getR111_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR111_uk() != null) {
			cellF.setCellValue(record1.getR111_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR111_europe() != null) {
			cellG.setCellValue(record1.getR111_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR111_india() != null) {
			cellH.setCellValue(record1.getR111_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR111_sydney() != null) {
			cellI.setCellValue(record1.getR111_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR111_uganda() != null) {
			cellJ.setCellValue(record1.getR111_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR111_c10() != null) {
			cellK.setCellValue(record1.getR111_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR111_c11() != null) {
			cellL.setCellValue(record1.getR111_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR111_c12() != null) {
			cellM.setCellValue(record1.getR111_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR111_c13() != null) {
			cellN.setCellValue(record1.getR111_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR111_c14() != null) {
			cellO.setCellValue(record1.getR111_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR111_c15() != null) {
			cellP.setCellValue(record1.getR111_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR111_c16() != null) {
			cellQ.setCellValue(record1.getR111_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R112 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(111);

		/*
		 * // Column B - BOTSWANA cellB = row.createCell(1); if
		 * (record2.getR112_botswana() != null) {
		 * cellB.setCellValue(record2.getR112_botswana().doubleValue());
		 * cellB.setCellStyle(numberStyle); } else { cellB.setCellValue("");
		 * cellB.setCellStyle(textStyle); }
		 */

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR112_south_africa() != null) {
			cellC.setCellValue(record1.getR112_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR112_sadc() != null) {
			cellD.setCellValue(record1.getR112_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR112_usa() != null) {
			cellE.setCellValue(record1.getR112_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR112_uk() != null) {
			cellF.setCellValue(record1.getR112_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR112_europe() != null) {
			cellG.setCellValue(record1.getR112_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR112_india() != null) {
			cellH.setCellValue(record1.getR112_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR112_sydney() != null) {
			cellI.setCellValue(record1.getR112_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR112_uganda() != null) {
			cellJ.setCellValue(record1.getR112_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR112_c10() != null) {
			cellK.setCellValue(record1.getR112_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR112_c11() != null) {
			cellL.setCellValue(record1.getR112_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR112_c12() != null) {
			cellM.setCellValue(record1.getR112_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR112_c13() != null) {
			cellN.setCellValue(record1.getR112_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR112_c14() != null) {
			cellO.setCellValue(record1.getR112_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR112_c15() != null) {
			cellP.setCellValue(record1.getR112_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR112_c16() != null) {
			cellQ.setCellValue(record1.getR112_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R113 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(112);

		/*
		 * // Column B - BOTSWANA cellB = row.createCell(1); if
		 * (record2.getR113_botswana() != null) {
		 * cellB.setCellValue(record2.getR113_botswana().doubleValue());
		 * cellB.setCellStyle(numberStyle); } else { cellB.setCellValue("");
		 * cellB.setCellStyle(textStyle); }
		 */

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR113_south_africa() != null) {
			cellC.setCellValue(record1.getR113_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR113_sadc() != null) {
			cellD.setCellValue(record1.getR113_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR113_usa() != null) {
			cellE.setCellValue(record1.getR113_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR113_uk() != null) {
			cellF.setCellValue(record1.getR113_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR113_europe() != null) {
			cellG.setCellValue(record1.getR113_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR113_india() != null) {
			cellH.setCellValue(record1.getR113_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR113_sydney() != null) {
			cellI.setCellValue(record1.getR113_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR113_uganda() != null) {
			cellJ.setCellValue(record1.getR113_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR113_c10() != null) {
			cellK.setCellValue(record1.getR113_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR113_c11() != null) {
			cellL.setCellValue(record1.getR113_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR113_c12() != null) {
			cellM.setCellValue(record1.getR113_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR113_c13() != null) {
			cellN.setCellValue(record1.getR113_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR113_c14() != null) {
			cellO.setCellValue(record1.getR113_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR113_c15() != null) {
			cellP.setCellValue(record1.getR113_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR113_c16() != null) {
			cellQ.setCellValue(record1.getR113_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R114 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(113);

		/*
		 * // Column B - BOTSWANA cellB = row.createCell(1); if
		 * (record2.getR114_botswana() != null) {
		 * cellB.setCellValue(record2.getR114_botswana().doubleValue());
		 * cellB.setCellStyle(numberStyle); } else { cellB.setCellValue("");
		 * cellB.setCellStyle(textStyle); }
		 */

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record1.getR114_south_africa() != null) {
			cellC.setCellValue(record1.getR114_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record1.getR114_sadc() != null) {
			cellD.setCellValue(record1.getR114_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record1.getR114_usa() != null) {
			cellE.setCellValue(record1.getR114_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record1.getR114_uk() != null) {
			cellF.setCellValue(record1.getR114_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record1.getR114_europe() != null) {
			cellG.setCellValue(record1.getR114_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record1.getR114_india() != null) {
			cellH.setCellValue(record1.getR114_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record1.getR114_sydney() != null) {
			cellI.setCellValue(record1.getR114_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record1.getR114_uganda() != null) {
			cellJ.setCellValue(record1.getR114_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record1.getR114_c10() != null) {
			cellK.setCellValue(record1.getR114_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record1.getR114_c11() != null) {
			cellL.setCellValue(record1.getR114_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record1.getR114_c12() != null) {
			cellM.setCellValue(record1.getR114_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record1.getR114_c13() != null) {
			cellN.setCellValue(record1.getR114_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record1.getR114_c14() != null) {
			cellO.setCellValue(record1.getR114_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record1.getR114_c15() != null) {
			cellP.setCellValue(record1.getR114_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record1.getR114_c16() != null) {
			cellQ.setCellValue(record1.getR114_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

	}

	private void populateEntity3Data(Sheet sheet, M_GALOR_Manual_Summary_Entity record2, CellStyle textStyle,
			CellStyle numberStyle) {

		// R22 - ROW22 (Index 21)
		Row row = sheet.getRow(21) != null ? sheet.getRow(21) : sheet.createRow(21);

		Cell cellB, cellC, cellD, cellE, cellF, cellG, cellH, cellI, cellJ, cellK, cellL, cellM, cellN, cellO, cellP,
				cellQ;

		// -------------------------------------------------------
		// ROW R22
		// -------------------------------------------------------

		row = sheet.getRow(21);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record2.getR22_botswana() != null) {
			cellB.setCellValue(record2.getR22_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record2.getR22_south_africa() != null) {
			cellC.setCellValue(record2.getR22_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record2.getR22_sadc() != null) {
			cellD.setCellValue(record2.getR22_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record2.getR22_usa() != null) {
			cellE.setCellValue(record2.getR22_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record2.getR22_uk() != null) {
			cellF.setCellValue(record2.getR22_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record2.getR22_europe() != null) {
			cellG.setCellValue(record2.getR22_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record2.getR22_india() != null) {
			cellH.setCellValue(record2.getR22_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record2.getR22_sydney() != null) {
			cellI.setCellValue(record2.getR22_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record2.getR22_uganda() != null) {
			cellJ.setCellValue(record2.getR22_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record2.getR22_c10() != null) {
			cellK.setCellValue(record2.getR22_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record2.getR22_c11() != null) {
			cellL.setCellValue(record2.getR22_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record2.getR22_c12() != null) {
			cellM.setCellValue(record2.getR22_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record2.getR22_c13() != null) {
			cellN.setCellValue(record2.getR22_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record2.getR22_c14() != null) {
			cellO.setCellValue(record2.getR22_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record2.getR22_c15() != null) {
			cellP.setCellValue(record2.getR22_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record2.getR22_c16() != null) {
			cellQ.setCellValue(record2.getR22_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R23
		// -------------------------------------------------------

		row = sheet.getRow(22);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record2.getR23_botswana() != null) {
			cellB.setCellValue(record2.getR23_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record2.getR23_south_africa() != null) {
			cellC.setCellValue(record2.getR23_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record2.getR23_sadc() != null) {
			cellD.setCellValue(record2.getR23_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record2.getR23_usa() != null) {
			cellE.setCellValue(record2.getR23_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record2.getR23_uk() != null) {
			cellF.setCellValue(record2.getR23_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record2.getR23_europe() != null) {
			cellG.setCellValue(record2.getR23_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record2.getR23_india() != null) {
			cellH.setCellValue(record2.getR23_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record2.getR23_sydney() != null) {
			cellI.setCellValue(record2.getR23_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record2.getR23_uganda() != null) {
			cellJ.setCellValue(record2.getR23_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record2.getR23_c10() != null) {
			cellK.setCellValue(record2.getR23_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record2.getR23_c11() != null) {
			cellL.setCellValue(record2.getR23_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record2.getR23_c12() != null) {
			cellM.setCellValue(record2.getR23_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record2.getR23_c13() != null) {
			cellN.setCellValue(record2.getR23_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record2.getR23_c14() != null) {
			cellO.setCellValue(record2.getR23_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record2.getR23_c15() != null) {
			cellP.setCellValue(record2.getR23_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record2.getR23_c16() != null) {
			cellQ.setCellValue(record2.getR23_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R57 --> USING RECORD2
		// -------------------------------------------------------

		row = sheet.getRow(56);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record2.getR57_botswana() != null) {
			cellB.setCellValue(record2.getR57_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record2.getR57_south_africa() != null) {
			cellC.setCellValue(record2.getR57_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record2.getR57_sadc() != null) {
			cellD.setCellValue(record2.getR57_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record2.getR57_usa() != null) {
			cellE.setCellValue(record2.getR57_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record2.getR57_uk() != null) {
			cellF.setCellValue(record2.getR57_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record2.getR57_europe() != null) {
			cellG.setCellValue(record2.getR57_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record2.getR57_india() != null) {
			cellH.setCellValue(record2.getR57_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record2.getR57_sydney() != null) {
			cellI.setCellValue(record2.getR57_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record2.getR57_uganda() != null) {
			cellJ.setCellValue(record2.getR57_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record2.getR57_c10() != null) {
			cellK.setCellValue(record2.getR57_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record2.getR57_c11() != null) {
			cellL.setCellValue(record2.getR57_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record2.getR57_c12() != null) {
			cellM.setCellValue(record2.getR57_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record2.getR57_c13() != null) {
			cellN.setCellValue(record2.getR57_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record2.getR57_c14() != null) {
			cellO.setCellValue(record2.getR57_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record2.getR57_c15() != null) {
			cellP.setCellValue(record2.getR57_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record2.getR57_c16() != null) {
			cellQ.setCellValue(record2.getR57_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R58 --> USING RECORD2
		// -------------------------------------------------------

		row = sheet.getRow(57);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record2.getR58_botswana() != null) {
			cellB.setCellValue(record2.getR58_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record2.getR58_south_africa() != null) {
			cellC.setCellValue(record2.getR58_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record2.getR58_sadc() != null) {
			cellD.setCellValue(record2.getR58_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record2.getR58_usa() != null) {
			cellE.setCellValue(record2.getR58_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record2.getR58_uk() != null) {
			cellF.setCellValue(record2.getR58_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record2.getR58_europe() != null) {
			cellG.setCellValue(record2.getR58_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record2.getR58_india() != null) {
			cellH.setCellValue(record2.getR58_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record2.getR58_sydney() != null) {
			cellI.setCellValue(record2.getR58_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record2.getR58_uganda() != null) {
			cellJ.setCellValue(record2.getR58_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record2.getR58_c10() != null) {
			cellK.setCellValue(record2.getR58_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record2.getR58_c11() != null) {
			cellL.setCellValue(record2.getR58_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record2.getR58_c12() != null) {
			cellM.setCellValue(record2.getR58_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record2.getR58_c13() != null) {
			cellN.setCellValue(record2.getR58_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record2.getR58_c14() != null) {
			cellO.setCellValue(record2.getR58_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record2.getR58_c15() != null) {
			cellP.setCellValue(record2.getR58_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record2.getR58_c16() != null) {
			cellQ.setCellValue(record2.getR58_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R60 --> USING RECORD2
		// -------------------------------------------------------

		row = sheet.getRow(59);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record2.getR60_botswana() != null) {
			cellB.setCellValue(record2.getR60_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record2.getR60_south_africa() != null) {
			cellC.setCellValue(record2.getR60_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record2.getR60_sadc() != null) {
			cellD.setCellValue(record2.getR60_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record2.getR60_usa() != null) {
			cellE.setCellValue(record2.getR60_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record2.getR60_uk() != null) {
			cellF.setCellValue(record2.getR60_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record2.getR60_europe() != null) {
			cellG.setCellValue(record2.getR60_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record2.getR60_india() != null) {
			cellH.setCellValue(record2.getR60_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record2.getR60_sydney() != null) {
			cellI.setCellValue(record2.getR60_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record2.getR60_uganda() != null) {
			cellJ.setCellValue(record2.getR60_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record2.getR60_c10() != null) {
			cellK.setCellValue(record2.getR60_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record2.getR60_c11() != null) {
			cellL.setCellValue(record2.getR60_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record2.getR60_c12() != null) {
			cellM.setCellValue(record2.getR60_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record2.getR60_c13() != null) {
			cellN.setCellValue(record2.getR60_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record2.getR60_c14() != null) {
			cellO.setCellValue(record2.getR60_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record2.getR60_c15() != null) {
			cellP.setCellValue(record2.getR60_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record2.getR60_c16() != null) {
			cellQ.setCellValue(record2.getR60_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R61 --> USING RECORD2
		// -------------------------------------------------------

		row = sheet.getRow(60);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record2.getR61_botswana() != null) {
			cellB.setCellValue(record2.getR61_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record2.getR61_south_africa() != null) {
			cellC.setCellValue(record2.getR61_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record2.getR61_sadc() != null) {
			cellD.setCellValue(record2.getR61_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record2.getR61_usa() != null) {
			cellE.setCellValue(record2.getR61_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record2.getR61_uk() != null) {
			cellF.setCellValue(record2.getR61_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record2.getR61_europe() != null) {
			cellG.setCellValue(record2.getR61_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record2.getR61_india() != null) {
			cellH.setCellValue(record2.getR61_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record2.getR61_sydney() != null) {
			cellI.setCellValue(record2.getR61_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record2.getR61_uganda() != null) {
			cellJ.setCellValue(record2.getR61_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record2.getR61_c10() != null) {
			cellK.setCellValue(record2.getR61_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record2.getR61_c11() != null) {
			cellL.setCellValue(record2.getR61_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record2.getR61_c12() != null) {
			cellM.setCellValue(record2.getR61_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record2.getR61_c13() != null) {
			cellN.setCellValue(record2.getR61_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record2.getR61_c14() != null) {
			cellO.setCellValue(record2.getR61_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record2.getR61_c15() != null) {
			cellP.setCellValue(record2.getR61_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record2.getR61_c16() != null) {
			cellQ.setCellValue(record2.getR61_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R64 --> USING RECORD2
		// -------------------------------------------------------

		row = sheet.getRow(63);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record2.getR64_botswana() != null) {
			cellB.setCellValue(record2.getR64_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record2.getR64_south_africa() != null) {
			cellC.setCellValue(record2.getR64_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record2.getR64_sadc() != null) {
			cellD.setCellValue(record2.getR64_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record2.getR64_usa() != null) {
			cellE.setCellValue(record2.getR64_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record2.getR64_uk() != null) {
			cellF.setCellValue(record2.getR64_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record2.getR64_europe() != null) {
			cellG.setCellValue(record2.getR64_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record2.getR64_india() != null) {
			cellH.setCellValue(record2.getR64_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record2.getR64_sydney() != null) {
			cellI.setCellValue(record2.getR64_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record2.getR64_uganda() != null) {
			cellJ.setCellValue(record2.getR64_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record2.getR64_c10() != null) {
			cellK.setCellValue(record2.getR64_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record2.getR64_c11() != null) {
			cellL.setCellValue(record2.getR64_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record2.getR64_c12() != null) {
			cellM.setCellValue(record2.getR64_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record2.getR64_c13() != null) {
			cellN.setCellValue(record2.getR64_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record2.getR64_c14() != null) {
			cellO.setCellValue(record2.getR64_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record2.getR64_c15() != null) {
			cellP.setCellValue(record2.getR64_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record2.getR64_c16() != null) {
			cellQ.setCellValue(record2.getR64_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R65 --> USING RECORD2
		// -------------------------------------------------------

		row = sheet.getRow(64);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record2.getR65_botswana() != null) {
			cellB.setCellValue(record2.getR65_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record2.getR65_south_africa() != null) {
			cellC.setCellValue(record2.getR65_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record2.getR65_sadc() != null) {
			cellD.setCellValue(record2.getR65_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record2.getR65_usa() != null) {
			cellE.setCellValue(record2.getR65_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record2.getR65_uk() != null) {
			cellF.setCellValue(record2.getR65_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record2.getR65_europe() != null) {
			cellG.setCellValue(record2.getR65_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record2.getR65_india() != null) {
			cellH.setCellValue(record2.getR65_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record2.getR65_sydney() != null) {
			cellI.setCellValue(record2.getR65_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record2.getR65_uganda() != null) {
			cellJ.setCellValue(record2.getR65_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record2.getR65_c10() != null) {
			cellK.setCellValue(record2.getR65_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record2.getR65_c11() != null) {
			cellL.setCellValue(record2.getR65_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record2.getR65_c12() != null) {
			cellM.setCellValue(record2.getR65_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record2.getR65_c13() != null) {
			cellN.setCellValue(record2.getR65_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record2.getR65_c14() != null) {
			cellO.setCellValue(record2.getR65_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record2.getR65_c15() != null) {
			cellP.setCellValue(record2.getR65_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record2.getR65_c16() != null) {
			cellQ.setCellValue(record2.getR65_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R67 --> USING RECORD2
		// -------------------------------------------------------

		row = sheet.getRow(66);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record2.getR67_botswana() != null) {
			cellB.setCellValue(record2.getR67_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record2.getR67_south_africa() != null) {
			cellC.setCellValue(record2.getR67_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record2.getR67_sadc() != null) {
			cellD.setCellValue(record2.getR67_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record2.getR67_usa() != null) {
			cellE.setCellValue(record2.getR67_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record2.getR67_uk() != null) {
			cellF.setCellValue(record2.getR67_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record2.getR67_europe() != null) {
			cellG.setCellValue(record2.getR67_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record2.getR67_india() != null) {
			cellH.setCellValue(record2.getR67_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record2.getR67_sydney() != null) {
			cellI.setCellValue(record2.getR67_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record2.getR67_uganda() != null) {
			cellJ.setCellValue(record2.getR67_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record2.getR67_c10() != null) {
			cellK.setCellValue(record2.getR67_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record2.getR67_c11() != null) {
			cellL.setCellValue(record2.getR67_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record2.getR67_c12() != null) {
			cellM.setCellValue(record2.getR67_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record2.getR67_c13() != null) {
			cellN.setCellValue(record2.getR67_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record2.getR67_c14() != null) {
			cellO.setCellValue(record2.getR67_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record2.getR67_c15() != null) {
			cellP.setCellValue(record2.getR67_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record2.getR67_c16() != null) {
			cellQ.setCellValue(record2.getR67_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R68 --> USING RECORD2
		// -------------------------------------------------------

		row = sheet.getRow(67);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record2.getR68_botswana() != null) {
			cellB.setCellValue(record2.getR68_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// Column C - SOUTH_AFRICA
		cellC = row.createCell(2);
		if (record2.getR68_south_africa() != null) {
			cellC.setCellValue(record2.getR68_south_africa().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// Column D - SADC
		cellD = row.createCell(3);
		if (record2.getR68_sadc() != null) {
			cellD.setCellValue(record2.getR68_sadc().doubleValue());
			cellD.setCellStyle(numberStyle);
		} else {
			cellD.setCellValue("");
			cellD.setCellStyle(textStyle);
		}

		// Column E - USA
		cellE = row.createCell(4);
		if (record2.getR68_usa() != null) {
			cellE.setCellValue(record2.getR68_usa().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}

		// Column F - UK
		cellF = row.createCell(5);
		if (record2.getR68_uk() != null) {
			cellF.setCellValue(record2.getR68_uk().doubleValue());
			cellF.setCellStyle(numberStyle);
		} else {
			cellF.setCellValue("");
			cellF.setCellStyle(textStyle);
		}

		// Column G - EUROPE
		cellG = row.createCell(6);
		if (record2.getR68_europe() != null) {
			cellG.setCellValue(record2.getR68_europe().doubleValue());
			cellG.setCellStyle(numberStyle);
		} else {
			cellG.setCellValue("");
			cellG.setCellStyle(textStyle);
		}

		// Column H - INDIA
		cellH = row.createCell(7);
		if (record2.getR68_india() != null) {
			cellH.setCellValue(record2.getR68_india().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		// Column I - SYDNEY
		cellI = row.createCell(8);
		if (record2.getR68_sydney() != null) {
			cellI.setCellValue(record2.getR68_sydney().doubleValue());
			cellI.setCellStyle(numberStyle);
		} else {
			cellI.setCellValue("");
			cellI.setCellStyle(textStyle);
		}

		// Column J - UGANDA
		cellJ = row.createCell(9);
		if (record2.getR68_uganda() != null) {
			cellJ.setCellValue(record2.getR68_uganda().doubleValue());
			cellJ.setCellStyle(numberStyle);
		} else {
			cellJ.setCellValue("");
			cellJ.setCellStyle(textStyle);
		}

		// Column K - C10
		cellK = row.createCell(10);
		if (record2.getR68_c10() != null) {
			cellK.setCellValue(record2.getR68_c10().doubleValue());
			cellK.setCellStyle(numberStyle);
		} else {
			cellK.setCellValue("");
			cellK.setCellStyle(textStyle);
		}

		// Column L - C11
		cellL = row.createCell(11);
		if (record2.getR68_c11() != null) {
			cellL.setCellValue(record2.getR68_c11().doubleValue());
			cellL.setCellStyle(numberStyle);
		} else {
			cellL.setCellValue("");
			cellL.setCellStyle(textStyle);
		}

		// Column M - C12
		cellM = row.createCell(12);
		if (record2.getR68_c12() != null) {
			cellM.setCellValue(record2.getR68_c12().doubleValue());
			cellM.setCellStyle(numberStyle);
		} else {
			cellM.setCellValue("");
			cellM.setCellStyle(textStyle);
		}

		// Column N - C13
		cellN = row.createCell(13);
		if (record2.getR68_c13() != null) {
			cellN.setCellValue(record2.getR68_c13().doubleValue());
			cellN.setCellStyle(numberStyle);
		} else {
			cellN.setCellValue("");
			cellN.setCellStyle(textStyle);
		}

		// Column O - C14
		cellO = row.createCell(14);
		if (record2.getR68_c14() != null) {
			cellO.setCellValue(record2.getR68_c14().doubleValue());
			cellO.setCellStyle(numberStyle);
		} else {
			cellO.setCellValue("");
			cellO.setCellStyle(textStyle);
		}

		// Column P - C15
		cellP = row.createCell(15);
		if (record2.getR68_c15() != null) {
			cellP.setCellValue(record2.getR68_c15().doubleValue());
			cellP.setCellStyle(numberStyle);
		} else {
			cellP.setCellValue("");
			cellP.setCellStyle(textStyle);
		}

		// Column Q - C16
		cellQ = row.createCell(16);
		if (record2.getR68_c16() != null) {
			cellQ.setCellValue(record2.getR68_c16().doubleValue());
			cellQ.setCellStyle(numberStyle);
		} else {
			cellQ.setCellValue("");
			cellQ.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R111 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(110);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record2.getR111_botswana() != null) {
			cellB.setCellValue(record2.getR111_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R112 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(111);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record2.getR112_botswana() != null) {
			cellB.setCellValue(record2.getR112_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R113 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(112);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record2.getR113_botswana() != null) {
			cellB.setCellValue(record2.getR113_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// -------------------------------------------------------
		// ROW R114 --> USING RECORD1
		// -------------------------------------------------------

		row = sheet.getRow(113);

		// Column B - BOTSWANA
		cellB = row.createCell(1);
		if (record2.getR114_botswana() != null) {
			cellB.setCellValue(record2.getR114_botswana().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

	}

	public byte[] getExcelM_GALORARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_GALOR_Archival_Summary_Entity1> dataList = m_galor_Archival_Summary1_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_GALOR_Archival_Summary_Entity2> dataList1 = m_galor_Archival_Summary2_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_GALOR_Manual_Archival_Summary_Entity> dataList2 = m_galor_Manual_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty() && dataList1.isEmpty() && dataList2.isEmpty()) {
			logger.warn("Service: No data found for M_GALOR report. Returning empty result.");
			return new byte[0];
		}
		// Fetch data

		/*List<M_GALOR_Archival_Summary_Entity1> dataList = m_galor_Archival_Summary1_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_GALOR_Archival_Summary_Entity2> dataList1 = m_galor_Archival_Summary2_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_GALOR_Manual_Archival_Summary_Entity> dataList2 = m_galor_Manual_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_GALOR report. Returning empty result.");
			return new byte[0];
		}*/

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

			if (!dataList.isEmpty()) {
				populateEntity1Data(sheet, dataList.get(0), textStyle, numberStyle);
			}

			if (!dataList1.isEmpty()) {
				populateEntity2Data(sheet, dataList1.get(0), textStyle, numberStyle);
			}

			if (!dataList2.isEmpty()) {
				populateEntity3Data(sheet, dataList2.get(0), textStyle, numberStyle);
			}

			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

			
					private void populateEntity1Data(Sheet sheet, M_GALOR_Archival_Summary_Entity1 record, CellStyle textStyle,
							CellStyle numberStyle) {

						// R11 - ROW 11 (Index 10)
						Row row = sheet.getRow(10) != null ? sheet.getRow(10) : sheet.createRow(10);

						// Column B - BOTSWANA
						Cell cellB = row.createCell(1);
						if (record.getR11_botswana() != null) {
							cellB.setCellValue(record.getR11_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH AFRICA
						Cell cellC = row.createCell(2);
						if (record.getR11_south_africa() != null) {
							cellC.setCellValue(record.getR11_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						Cell cellD = row.createCell(3);
						if (record.getR11_sadc() != null) {
							cellD.setCellValue(record.getR11_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						Cell cellE = row.createCell(4);
						if (record.getR11_usa() != null) {
							cellE.setCellValue(record.getR11_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						Cell cellF = row.createCell(5);
						if (record.getR11_uk() != null) {
							cellF.setCellValue(record.getR11_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						Cell cellG = row.createCell(6);
						if (record.getR11_europe() != null) {
							cellG.setCellValue(record.getR11_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						Cell cellH = row.createCell(7);
						if (record.getR11_india() != null) {
							cellH.setCellValue(record.getR11_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						Cell cellI = row.createCell(8);
						if (record.getR11_sydney() != null) {
							cellI.setCellValue(record.getR11_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - Uganda
						Cell cellJ = row.createCell(9);
						if (record.getR11_uganda() != null) {
							cellJ.setCellValue(record.getR11_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - 0
						Cell cellK = row.createCell(10);
						if (record.getR11_c10() != null) {
							cellK.setCellValue(record.getR11_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - 0
						Cell cellL = row.createCell(11);
						if (record.getR11_c11() != null) {
							cellL.setCellValue(record.getR11_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - 0
						Cell cellM = row.createCell(12);
						if (record.getR11_c12() != null) {
							cellM.setCellValue(record.getR11_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - 0
						Cell cellN = row.createCell(13);
						if (record.getR11_c13() != null) {
							cellN.setCellValue(record.getR11_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - 0
						Cell cellO = row.createCell(14);
						if (record.getR11_c14() != null) {
							cellO.setCellValue(record.getR11_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - 0
						Cell cellP = row.createCell(15);
						if (record.getR11_c15() != null) {
							cellP.setCellValue(record.getR11_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - 0
						Cell cellQ = row.createCell(16);
						if (record.getR11_c16() != null) {
							cellQ.setCellValue(record.getR11_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R12
						// -------------------------------------------------------

						row = sheet.getRow(11);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR12_botswana() != null) {
							cellB.setCellValue(record.getR12_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR12_south_africa() != null) {
							cellC.setCellValue(record.getR12_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR12_sadc() != null) {
							cellD.setCellValue(record.getR12_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR12_usa() != null) {
							cellE.setCellValue(record.getR12_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR12_uk() != null) {
							cellF.setCellValue(record.getR12_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR12_europe() != null) {
							cellG.setCellValue(record.getR12_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR12_india() != null) {
							cellH.setCellValue(record.getR12_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR12_sydney() != null) {
							cellI.setCellValue(record.getR12_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR12_uganda() != null) {
							cellJ.setCellValue(record.getR12_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR12_c10() != null) {
							cellK.setCellValue(record.getR12_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR12_c11() != null) {
							cellL.setCellValue(record.getR12_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR12_c12() != null) {
							cellM.setCellValue(record.getR12_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR12_c13() != null) {
							cellN.setCellValue(record.getR12_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR12_c14() != null) {
							cellO.setCellValue(record.getR12_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR12_c15() != null) {
							cellP.setCellValue(record.getR12_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR12_c16() != null) {
							cellQ.setCellValue(record.getR12_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R13
						// -------------------------------------------------------

						row = sheet.getRow(12);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR13_botswana() != null) {
							cellB.setCellValue(record.getR13_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR13_south_africa() != null) {
							cellC.setCellValue(record.getR13_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR13_sadc() != null) {
							cellD.setCellValue(record.getR13_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR13_usa() != null) {
							cellE.setCellValue(record.getR13_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR13_uk() != null) {
							cellF.setCellValue(record.getR13_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR13_europe() != null) {
							cellG.setCellValue(record.getR13_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR13_india() != null) {
							cellH.setCellValue(record.getR13_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR13_sydney() != null) {
							cellI.setCellValue(record.getR13_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR13_uganda() != null) {
							cellJ.setCellValue(record.getR13_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR13_c10() != null) {
							cellK.setCellValue(record.getR13_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR13_c11() != null) {
							cellL.setCellValue(record.getR13_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR13_c12() != null) {
							cellM.setCellValue(record.getR13_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR13_c13() != null) {
							cellN.setCellValue(record.getR13_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR13_c14() != null) {
							cellO.setCellValue(record.getR13_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR13_c15() != null) {
							cellP.setCellValue(record.getR13_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR13_c16() != null) {
							cellQ.setCellValue(record.getR13_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R14
						// -------------------------------------------------------

						row = sheet.getRow(13);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR14_botswana() != null) {
							cellB.setCellValue(record.getR14_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR14_south_africa() != null) {
							cellC.setCellValue(record.getR14_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR14_sadc() != null) {
							cellD.setCellValue(record.getR14_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR14_usa() != null) {
							cellE.setCellValue(record.getR14_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR14_uk() != null) {
							cellF.setCellValue(record.getR14_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR14_europe() != null) {
							cellG.setCellValue(record.getR14_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR14_india() != null) {
							cellH.setCellValue(record.getR14_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR14_sydney() != null) {
							cellI.setCellValue(record.getR14_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR14_uganda() != null) {
							cellJ.setCellValue(record.getR14_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR14_c10() != null) {
							cellK.setCellValue(record.getR14_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR14_c11() != null) {
							cellL.setCellValue(record.getR14_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR14_c12() != null) {
							cellM.setCellValue(record.getR14_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR14_c13() != null) {
							cellN.setCellValue(record.getR14_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR14_c14() != null) {
							cellO.setCellValue(record.getR14_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR14_c15() != null) {
							cellP.setCellValue(record.getR14_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR14_c16() != null) {
							cellQ.setCellValue(record.getR14_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R15
						// -------------------------------------------------------

						row = sheet.getRow(14);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR15_botswana() != null) {
							cellB.setCellValue(record.getR15_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR15_south_africa() != null) {
							cellC.setCellValue(record.getR15_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR15_sadc() != null) {
							cellD.setCellValue(record.getR15_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR15_usa() != null) {
							cellE.setCellValue(record.getR15_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR15_uk() != null) {
							cellF.setCellValue(record.getR15_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR15_europe() != null) {
							cellG.setCellValue(record.getR15_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR15_india() != null) {
							cellH.setCellValue(record.getR15_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR15_sydney() != null) {
							cellI.setCellValue(record.getR15_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR15_uganda() != null) {
							cellJ.setCellValue(record.getR15_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR15_c10() != null) {
							cellK.setCellValue(record.getR15_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR15_c11() != null) {
							cellL.setCellValue(record.getR15_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR15_c12() != null) {
							cellM.setCellValue(record.getR15_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR15_c13() != null) {
							cellN.setCellValue(record.getR15_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR15_c14() != null) {
							cellO.setCellValue(record.getR15_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR15_c15() != null) {
							cellP.setCellValue(record.getR15_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR15_c16() != null) {
							cellQ.setCellValue(record.getR15_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R16
						// -------------------------------------------------------

						row = sheet.getRow(15);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR16_botswana() != null) {
							cellB.setCellValue(record.getR16_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR16_south_africa() != null) {
							cellC.setCellValue(record.getR16_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR16_sadc() != null) {
							cellD.setCellValue(record.getR16_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR16_usa() != null) {
							cellE.setCellValue(record.getR16_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR16_uk() != null) {
							cellF.setCellValue(record.getR16_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR16_europe() != null) {
							cellG.setCellValue(record.getR16_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR16_india() != null) {
							cellH.setCellValue(record.getR16_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR16_sydney() != null) {
							cellI.setCellValue(record.getR16_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR16_uganda() != null) {
							cellJ.setCellValue(record.getR16_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR16_c10() != null) {
							cellK.setCellValue(record.getR16_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR16_c11() != null) {
							cellL.setCellValue(record.getR16_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR16_c12() != null) {
							cellM.setCellValue(record.getR16_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR16_c13() != null) {
							cellN.setCellValue(record.getR16_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR16_c14() != null) {
							cellO.setCellValue(record.getR16_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR16_c15() != null) {
							cellP.setCellValue(record.getR16_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR16_c16() != null) {
							cellQ.setCellValue(record.getR16_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R17
						// -------------------------------------------------------

						row = sheet.getRow(16);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR17_botswana() != null) {
							cellB.setCellValue(record.getR17_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR17_south_africa() != null) {
							cellC.setCellValue(record.getR17_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR17_sadc() != null) {
							cellD.setCellValue(record.getR17_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR17_usa() != null) {
							cellE.setCellValue(record.getR17_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR17_uk() != null) {
							cellF.setCellValue(record.getR17_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR17_europe() != null) {
							cellG.setCellValue(record.getR17_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR17_india() != null) {
							cellH.setCellValue(record.getR17_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR17_sydney() != null) {
							cellI.setCellValue(record.getR17_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR17_uganda() != null) {
							cellJ.setCellValue(record.getR17_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR17_c10() != null) {
							cellK.setCellValue(record.getR17_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR17_c11() != null) {
							cellL.setCellValue(record.getR17_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR17_c12() != null) {
							cellM.setCellValue(record.getR17_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR17_c13() != null) {
							cellN.setCellValue(record.getR17_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR17_c14() != null) {
							cellO.setCellValue(record.getR17_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR17_c15() != null) {
							cellP.setCellValue(record.getR17_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR17_c16() != null) {
							cellQ.setCellValue(record.getR17_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R18
						// -------------------------------------------------------

						row = sheet.getRow(17);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR18_botswana() != null) {
							cellB.setCellValue(record.getR18_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR18_south_africa() != null) {
							cellC.setCellValue(record.getR18_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR18_sadc() != null) {
							cellD.setCellValue(record.getR18_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR18_usa() != null) {
							cellE.setCellValue(record.getR18_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR18_uk() != null) {
							cellF.setCellValue(record.getR18_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR18_europe() != null) {
							cellG.setCellValue(record.getR18_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR18_india() != null) {
							cellH.setCellValue(record.getR18_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR18_sydney() != null) {
							cellI.setCellValue(record.getR18_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR18_uganda() != null) {
							cellJ.setCellValue(record.getR18_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR18_c10() != null) {
							cellK.setCellValue(record.getR18_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR18_c11() != null) {
							cellL.setCellValue(record.getR18_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR18_c12() != null) {
							cellM.setCellValue(record.getR18_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR18_c13() != null) {
							cellN.setCellValue(record.getR18_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR18_c14() != null) {
							cellO.setCellValue(record.getR18_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR18_c15() != null) {
							cellP.setCellValue(record.getR18_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR18_c16() != null) {
							cellQ.setCellValue(record.getR18_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R19
						// -------------------------------------------------------

						row = sheet.getRow(18);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR19_botswana() != null) {
							cellB.setCellValue(record.getR19_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR19_south_africa() != null) {
							cellC.setCellValue(record.getR19_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR19_sadc() != null) {
							cellD.setCellValue(record.getR19_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR19_usa() != null) {
							cellE.setCellValue(record.getR19_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR19_uk() != null) {
							cellF.setCellValue(record.getR19_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR19_europe() != null) {
							cellG.setCellValue(record.getR19_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR19_india() != null) {
							cellH.setCellValue(record.getR19_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR19_sydney() != null) {
							cellI.setCellValue(record.getR19_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR19_uganda() != null) {
							cellJ.setCellValue(record.getR19_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR19_c10() != null) {
							cellK.setCellValue(record.getR19_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR19_c11() != null) {
							cellL.setCellValue(record.getR19_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR19_c12() != null) {
							cellM.setCellValue(record.getR19_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR19_c13() != null) {
							cellN.setCellValue(record.getR19_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR19_c14() != null) {
							cellO.setCellValue(record.getR19_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR19_c15() != null) {
							cellP.setCellValue(record.getR19_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR19_c16() != null) {
							cellQ.setCellValue(record.getR19_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R20
						// -------------------------------------------------------

						row = sheet.getRow(19);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR20_botswana() != null) {
							cellB.setCellValue(record.getR20_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR20_south_africa() != null) {
							cellC.setCellValue(record.getR20_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR20_sadc() != null) {
							cellD.setCellValue(record.getR20_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR20_usa() != null) {
							cellE.setCellValue(record.getR20_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR20_uk() != null) {
							cellF.setCellValue(record.getR20_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR20_europe() != null) {
							cellG.setCellValue(record.getR20_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR20_india() != null) {
							cellH.setCellValue(record.getR20_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR20_sydney() != null) {
							cellI.setCellValue(record.getR20_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR20_uganda() != null) {
							cellJ.setCellValue(record.getR20_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR20_c10() != null) {
							cellK.setCellValue(record.getR20_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR20_c11() != null) {
							cellL.setCellValue(record.getR20_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR20_c12() != null) {
							cellM.setCellValue(record.getR20_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR20_c13() != null) {
							cellN.setCellValue(record.getR20_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR20_c14() != null) {
							cellO.setCellValue(record.getR20_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR20_c15() != null) {
							cellP.setCellValue(record.getR20_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR20_c16() != null) {
							cellQ.setCellValue(record.getR20_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R21
						// -------------------------------------------------------

						row = sheet.getRow(20);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR21_botswana() != null) {
							cellB.setCellValue(record.getR21_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR21_south_africa() != null) {
							cellC.setCellValue(record.getR21_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR21_sadc() != null) {
							cellD.setCellValue(record.getR21_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR21_usa() != null) {
							cellE.setCellValue(record.getR21_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR21_uk() != null) {
							cellF.setCellValue(record.getR21_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR21_europe() != null) {
							cellG.setCellValue(record.getR21_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR21_india() != null) {
							cellH.setCellValue(record.getR21_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR21_sydney() != null) {
							cellI.setCellValue(record.getR21_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR21_uganda() != null) {
							cellJ.setCellValue(record.getR21_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR21_c10() != null) {
							cellK.setCellValue(record.getR21_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR21_c11() != null) {
							cellL.setCellValue(record.getR21_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR21_c12() != null) {
							cellM.setCellValue(record.getR21_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR21_c13() != null) {
							cellN.setCellValue(record.getR21_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR21_c14() != null) {
							cellO.setCellValue(record.getR21_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR21_c15() != null) {
							cellP.setCellValue(record.getR21_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR21_c16() != null) {
							cellQ.setCellValue(record.getR21_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R24
						// -------------------------------------------------------

						row = sheet.getRow(23);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR24_botswana() != null) {
							cellB.setCellValue(record.getR24_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR24_south_africa() != null) {
							cellC.setCellValue(record.getR24_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR24_sadc() != null) {
							cellD.setCellValue(record.getR24_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR24_usa() != null) {
							cellE.setCellValue(record.getR24_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR24_uk() != null) {
							cellF.setCellValue(record.getR24_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR24_europe() != null) {
							cellG.setCellValue(record.getR24_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR24_india() != null) {
							cellH.setCellValue(record.getR24_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR24_sydney() != null) {
							cellI.setCellValue(record.getR24_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR24_uganda() != null) {
							cellJ.setCellValue(record.getR24_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR24_c10() != null) {
							cellK.setCellValue(record.getR24_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR24_c11() != null) {
							cellL.setCellValue(record.getR24_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR24_c12() != null) {
							cellM.setCellValue(record.getR24_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR24_c13() != null) {
							cellN.setCellValue(record.getR24_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR24_c14() != null) {
							cellO.setCellValue(record.getR24_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR24_c15() != null) {
							cellP.setCellValue(record.getR24_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR24_c16() != null) {
							cellQ.setCellValue(record.getR24_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R25
						// -------------------------------------------------------

						row = sheet.getRow(24);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR25_botswana() != null) {
							cellB.setCellValue(record.getR25_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR25_south_africa() != null) {
							cellC.setCellValue(record.getR25_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR25_sadc() != null) {
							cellD.setCellValue(record.getR25_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR25_usa() != null) {
							cellE.setCellValue(record.getR25_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR25_uk() != null) {
							cellF.setCellValue(record.getR25_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR25_europe() != null) {
							cellG.setCellValue(record.getR25_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR25_india() != null) {
							cellH.setCellValue(record.getR25_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR25_sydney() != null) {
							cellI.setCellValue(record.getR25_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR25_uganda() != null) {
							cellJ.setCellValue(record.getR25_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR25_c10() != null) {
							cellK.setCellValue(record.getR25_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR25_c11() != null) {
							cellL.setCellValue(record.getR25_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR25_c12() != null) {
							cellM.setCellValue(record.getR25_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR25_c13() != null) {
							cellN.setCellValue(record.getR25_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR25_c14() != null) {
							cellO.setCellValue(record.getR25_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR25_c15() != null) {
							cellP.setCellValue(record.getR25_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR25_c16() != null) {
							cellQ.setCellValue(record.getR25_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R26
						// -------------------------------------------------------

						row = sheet.getRow(25);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR26_botswana() != null) {
							cellB.setCellValue(record.getR26_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR26_south_africa() != null) {
							cellC.setCellValue(record.getR26_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR26_sadc() != null) {
							cellD.setCellValue(record.getR26_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR26_usa() != null) {
							cellE.setCellValue(record.getR26_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR26_uk() != null) {
							cellF.setCellValue(record.getR26_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR26_europe() != null) {
							cellG.setCellValue(record.getR26_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR26_india() != null) {
							cellH.setCellValue(record.getR26_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR26_sydney() != null) {
							cellI.setCellValue(record.getR26_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR26_uganda() != null) {
							cellJ.setCellValue(record.getR26_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR26_c10() != null) {
							cellK.setCellValue(record.getR26_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR26_c11() != null) {
							cellL.setCellValue(record.getR26_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR26_c12() != null) {
							cellM.setCellValue(record.getR26_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR26_c13() != null) {
							cellN.setCellValue(record.getR26_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR26_c14() != null) {
							cellO.setCellValue(record.getR26_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR26_c15() != null) {
							cellP.setCellValue(record.getR26_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR26_c16() != null) {
							cellQ.setCellValue(record.getR26_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R27
						// -------------------------------------------------------

						row = sheet.getRow(26);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR27_botswana() != null) {
							cellB.setCellValue(record.getR27_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR27_south_africa() != null) {
							cellC.setCellValue(record.getR27_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR27_sadc() != null) {
							cellD.setCellValue(record.getR27_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR27_usa() != null) {
							cellE.setCellValue(record.getR27_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR27_uk() != null) {
							cellF.setCellValue(record.getR27_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR27_europe() != null) {
							cellG.setCellValue(record.getR27_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR27_india() != null) {
							cellH.setCellValue(record.getR27_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR27_sydney() != null) {
							cellI.setCellValue(record.getR27_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR27_uganda() != null) {
							cellJ.setCellValue(record.getR27_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR27_c10() != null) {
							cellK.setCellValue(record.getR27_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR27_c11() != null) {
							cellL.setCellValue(record.getR27_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR27_c12() != null) {
							cellM.setCellValue(record.getR27_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR27_c13() != null) {
							cellN.setCellValue(record.getR27_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR27_c14() != null) {
							cellO.setCellValue(record.getR27_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR27_c15() != null) {
							cellP.setCellValue(record.getR27_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR27_c16() != null) {
							cellQ.setCellValue(record.getR27_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R28
						// -------------------------------------------------------

						row = sheet.getRow(27);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR28_botswana() != null) {
							cellB.setCellValue(record.getR28_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR28_south_africa() != null) {
							cellC.setCellValue(record.getR28_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR28_sadc() != null) {
							cellD.setCellValue(record.getR28_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR28_usa() != null) {
							cellE.setCellValue(record.getR28_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR28_uk() != null) {
							cellF.setCellValue(record.getR28_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR28_europe() != null) {
							cellG.setCellValue(record.getR28_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR28_india() != null) {
							cellH.setCellValue(record.getR28_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR28_sydney() != null) {
							cellI.setCellValue(record.getR28_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR28_uganda() != null) {
							cellJ.setCellValue(record.getR28_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR28_c10() != null) {
							cellK.setCellValue(record.getR28_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR28_c11() != null) {
							cellL.setCellValue(record.getR28_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR28_c12() != null) {
							cellM.setCellValue(record.getR28_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR28_c13() != null) {
							cellN.setCellValue(record.getR28_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR28_c14() != null) {
							cellO.setCellValue(record.getR28_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR28_c15() != null) {
							cellP.setCellValue(record.getR28_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR28_c16() != null) {
							cellQ.setCellValue(record.getR28_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R29
						// -------------------------------------------------------

						row = sheet.getRow(28);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR29_botswana() != null) {
							cellB.setCellValue(record.getR29_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR29_south_africa() != null) {
							cellC.setCellValue(record.getR29_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR29_sadc() != null) {
							cellD.setCellValue(record.getR29_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR29_usa() != null) {
							cellE.setCellValue(record.getR29_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR29_uk() != null) {
							cellF.setCellValue(record.getR29_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR29_europe() != null) {
							cellG.setCellValue(record.getR29_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR29_india() != null) {
							cellH.setCellValue(record.getR29_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR29_sydney() != null) {
							cellI.setCellValue(record.getR29_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR29_uganda() != null) {
							cellJ.setCellValue(record.getR29_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR29_c10() != null) {
							cellK.setCellValue(record.getR29_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR29_c11() != null) {
							cellL.setCellValue(record.getR29_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR29_c12() != null) {
							cellM.setCellValue(record.getR29_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR29_c13() != null) {
							cellN.setCellValue(record.getR29_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR29_c14() != null) {
							cellO.setCellValue(record.getR29_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR29_c15() != null) {
							cellP.setCellValue(record.getR29_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR29_c16() != null) {
							cellQ.setCellValue(record.getR29_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R30
						// -------------------------------------------------------

						row = sheet.getRow(29);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR30_botswana() != null) {
							cellB.setCellValue(record.getR30_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR30_south_africa() != null) {
							cellC.setCellValue(record.getR30_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR30_sadc() != null) {
							cellD.setCellValue(record.getR30_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR30_usa() != null) {
							cellE.setCellValue(record.getR30_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR30_uk() != null) {
							cellF.setCellValue(record.getR30_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR30_europe() != null) {
							cellG.setCellValue(record.getR30_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR30_india() != null) {
							cellH.setCellValue(record.getR30_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR30_sydney() != null) {
							cellI.setCellValue(record.getR30_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR30_uganda() != null) {
							cellJ.setCellValue(record.getR30_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR30_c10() != null) {
							cellK.setCellValue(record.getR30_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR30_c11() != null) {
							cellL.setCellValue(record.getR30_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR30_c12() != null) {
							cellM.setCellValue(record.getR30_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR30_c13() != null) {
							cellN.setCellValue(record.getR30_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR30_c14() != null) {
							cellO.setCellValue(record.getR30_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR30_c15() != null) {
							cellP.setCellValue(record.getR30_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR30_c16() != null) {
							cellQ.setCellValue(record.getR30_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R31
						// -------------------------------------------------------

						row = sheet.getRow(30);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR31_botswana() != null) {
							cellB.setCellValue(record.getR31_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR31_south_africa() != null) {
							cellC.setCellValue(record.getR31_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR31_sadc() != null) {
							cellD.setCellValue(record.getR31_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR31_usa() != null) {
							cellE.setCellValue(record.getR31_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR31_uk() != null) {
							cellF.setCellValue(record.getR31_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR31_europe() != null) {
							cellG.setCellValue(record.getR31_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR31_india() != null) {
							cellH.setCellValue(record.getR31_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR31_sydney() != null) {
							cellI.setCellValue(record.getR31_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR31_uganda() != null) {
							cellJ.setCellValue(record.getR31_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR31_c10() != null) {
							cellK.setCellValue(record.getR31_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR31_c11() != null) {
							cellL.setCellValue(record.getR31_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR31_c12() != null) {
							cellM.setCellValue(record.getR31_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR31_c13() != null) {
							cellN.setCellValue(record.getR31_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR31_c14() != null) {
							cellO.setCellValue(record.getR31_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR31_c15() != null) {
							cellP.setCellValue(record.getR31_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR31_c16() != null) {
							cellQ.setCellValue(record.getR31_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R32
						// -------------------------------------------------------

						row = sheet.getRow(31);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR32_botswana() != null) {
							cellB.setCellValue(record.getR32_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR32_south_africa() != null) {
							cellC.setCellValue(record.getR32_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR32_sadc() != null) {
							cellD.setCellValue(record.getR32_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR32_usa() != null) {
							cellE.setCellValue(record.getR32_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR32_uk() != null) {
							cellF.setCellValue(record.getR32_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR32_europe() != null) {
							cellG.setCellValue(record.getR32_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR32_india() != null) {
							cellH.setCellValue(record.getR32_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR32_sydney() != null) {
							cellI.setCellValue(record.getR32_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR32_uganda() != null) {
							cellJ.setCellValue(record.getR32_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR32_c10() != null) {
							cellK.setCellValue(record.getR32_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR32_c11() != null) {
							cellL.setCellValue(record.getR32_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR32_c12() != null) {
							cellM.setCellValue(record.getR32_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR32_c13() != null) {
							cellN.setCellValue(record.getR32_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR32_c14() != null) {
							cellO.setCellValue(record.getR32_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR32_c15() != null) {
							cellP.setCellValue(record.getR32_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR32_c16() != null) {
							cellQ.setCellValue(record.getR32_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R33
						// -------------------------------------------------------

						row = sheet.getRow(32);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR33_botswana() != null) {
							cellB.setCellValue(record.getR33_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR33_south_africa() != null) {
							cellC.setCellValue(record.getR33_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR33_sadc() != null) {
							cellD.setCellValue(record.getR33_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR33_usa() != null) {
							cellE.setCellValue(record.getR33_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR33_uk() != null) {
							cellF.setCellValue(record.getR33_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR33_europe() != null) {
							cellG.setCellValue(record.getR33_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR33_india() != null) {
							cellH.setCellValue(record.getR33_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR33_sydney() != null) {
							cellI.setCellValue(record.getR33_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR33_uganda() != null) {
							cellJ.setCellValue(record.getR33_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR33_c10() != null) {
							cellK.setCellValue(record.getR33_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR33_c11() != null) {
							cellL.setCellValue(record.getR33_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR33_c12() != null) {
							cellM.setCellValue(record.getR33_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR33_c13() != null) {
							cellN.setCellValue(record.getR33_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR33_c14() != null) {
							cellO.setCellValue(record.getR33_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR33_c15() != null) {
							cellP.setCellValue(record.getR33_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR33_c16() != null) {
							cellQ.setCellValue(record.getR33_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R34
						// -------------------------------------------------------

						row = sheet.getRow(33);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR34_botswana() != null) {
							cellB.setCellValue(record.getR34_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR34_south_africa() != null) {
							cellC.setCellValue(record.getR34_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR34_sadc() != null) {
							cellD.setCellValue(record.getR34_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR34_usa() != null) {
							cellE.setCellValue(record.getR34_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR34_uk() != null) {
							cellF.setCellValue(record.getR34_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR34_europe() != null) {
							cellG.setCellValue(record.getR34_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR34_india() != null) {
							cellH.setCellValue(record.getR34_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR34_sydney() != null) {
							cellI.setCellValue(record.getR34_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR34_uganda() != null) {
							cellJ.setCellValue(record.getR34_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR34_c10() != null) {
							cellK.setCellValue(record.getR34_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR34_c11() != null) {
							cellL.setCellValue(record.getR34_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR34_c12() != null) {
							cellM.setCellValue(record.getR34_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR34_c13() != null) {
							cellN.setCellValue(record.getR34_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR34_c14() != null) {
							cellO.setCellValue(record.getR34_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR34_c15() != null) {
							cellP.setCellValue(record.getR34_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR34_c16() != null) {
							cellQ.setCellValue(record.getR34_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R35
						// -------------------------------------------------------

						row = sheet.getRow(34);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR35_botswana() != null) {
							cellB.setCellValue(record.getR35_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR35_south_africa() != null) {
							cellC.setCellValue(record.getR35_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR35_sadc() != null) {
							cellD.setCellValue(record.getR35_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR35_usa() != null) {
							cellE.setCellValue(record.getR35_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR35_uk() != null) {
							cellF.setCellValue(record.getR35_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR35_europe() != null) {
							cellG.setCellValue(record.getR35_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR35_india() != null) {
							cellH.setCellValue(record.getR35_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR35_sydney() != null) {
							cellI.setCellValue(record.getR35_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR35_uganda() != null) {
							cellJ.setCellValue(record.getR35_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR35_c10() != null) {
							cellK.setCellValue(record.getR35_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR35_c11() != null) {
							cellL.setCellValue(record.getR35_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR35_c12() != null) {
							cellM.setCellValue(record.getR35_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR35_c13() != null) {
							cellN.setCellValue(record.getR35_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR35_c14() != null) {
							cellO.setCellValue(record.getR35_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR35_c15() != null) {
							cellP.setCellValue(record.getR35_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR35_c16() != null) {
							cellQ.setCellValue(record.getR35_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R36
						// -------------------------------------------------------

						row = sheet.getRow(35);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR36_botswana() != null) {
							cellB.setCellValue(record.getR36_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR36_south_africa() != null) {
							cellC.setCellValue(record.getR36_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR36_sadc() != null) {
							cellD.setCellValue(record.getR36_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR36_usa() != null) {
							cellE.setCellValue(record.getR36_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR36_uk() != null) {
							cellF.setCellValue(record.getR36_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR36_europe() != null) {
							cellG.setCellValue(record.getR36_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR36_india() != null) {
							cellH.setCellValue(record.getR36_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR36_sydney() != null) {
							cellI.setCellValue(record.getR36_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR36_uganda() != null) {
							cellJ.setCellValue(record.getR36_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR36_c10() != null) {
							cellK.setCellValue(record.getR36_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR36_c11() != null) {
							cellL.setCellValue(record.getR36_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR36_c12() != null) {
							cellM.setCellValue(record.getR36_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR36_c13() != null) {
							cellN.setCellValue(record.getR36_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR36_c14() != null) {
							cellO.setCellValue(record.getR36_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR36_c15() != null) {
							cellP.setCellValue(record.getR36_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR36_c16() != null) {
							cellQ.setCellValue(record.getR36_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R37
						// -------------------------------------------------------

						row = sheet.getRow(36);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR37_botswana() != null) {
							cellB.setCellValue(record.getR37_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR37_south_africa() != null) {
							cellC.setCellValue(record.getR37_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR37_sadc() != null) {
							cellD.setCellValue(record.getR37_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR37_usa() != null) {
							cellE.setCellValue(record.getR37_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR37_uk() != null) {
							cellF.setCellValue(record.getR37_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR37_europe() != null) {
							cellG.setCellValue(record.getR37_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR37_india() != null) {
							cellH.setCellValue(record.getR37_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR37_sydney() != null) {
							cellI.setCellValue(record.getR37_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR37_uganda() != null) {
							cellJ.setCellValue(record.getR37_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR37_c10() != null) {
							cellK.setCellValue(record.getR37_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR37_c11() != null) {
							cellL.setCellValue(record.getR37_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR37_c12() != null) {
							cellM.setCellValue(record.getR37_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR37_c13() != null) {
							cellN.setCellValue(record.getR37_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR37_c14() != null) {
							cellO.setCellValue(record.getR37_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR37_c15() != null) {
							cellP.setCellValue(record.getR37_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR37_c16() != null) {
							cellQ.setCellValue(record.getR37_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R38
						// -------------------------------------------------------

						row = sheet.getRow(37);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR38_botswana() != null) {
							cellB.setCellValue(record.getR38_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR38_south_africa() != null) {
							cellC.setCellValue(record.getR38_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR38_sadc() != null) {
							cellD.setCellValue(record.getR38_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR38_usa() != null) {
							cellE.setCellValue(record.getR38_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR38_uk() != null) {
							cellF.setCellValue(record.getR38_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR38_europe() != null) {
							cellG.setCellValue(record.getR38_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR38_india() != null) {
							cellH.setCellValue(record.getR38_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR38_sydney() != null) {
							cellI.setCellValue(record.getR38_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR38_uganda() != null) {
							cellJ.setCellValue(record.getR38_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR38_c10() != null) {
							cellK.setCellValue(record.getR38_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR38_c11() != null) {
							cellL.setCellValue(record.getR38_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR38_c12() != null) {
							cellM.setCellValue(record.getR38_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR38_c13() != null) {
							cellN.setCellValue(record.getR38_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR38_c14() != null) {
							cellO.setCellValue(record.getR38_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR38_c15() != null) {
							cellP.setCellValue(record.getR38_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR38_c16() != null) {
							cellQ.setCellValue(record.getR38_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R39
						// -------------------------------------------------------

						row = sheet.getRow(38);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR39_botswana() != null) {
							cellB.setCellValue(record.getR39_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR39_south_africa() != null) {
							cellC.setCellValue(record.getR39_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR39_sadc() != null) {
							cellD.setCellValue(record.getR39_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR39_usa() != null) {
							cellE.setCellValue(record.getR39_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR39_uk() != null) {
							cellF.setCellValue(record.getR39_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR39_europe() != null) {
							cellG.setCellValue(record.getR39_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR39_india() != null) {
							cellH.setCellValue(record.getR39_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR39_sydney() != null) {
							cellI.setCellValue(record.getR39_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR39_uganda() != null) {
							cellJ.setCellValue(record.getR39_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR39_c10() != null) {
							cellK.setCellValue(record.getR39_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR39_c11() != null) {
							cellL.setCellValue(record.getR39_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR39_c12() != null) {
							cellM.setCellValue(record.getR39_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR39_c13() != null) {
							cellN.setCellValue(record.getR39_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR39_c14() != null) {
							cellO.setCellValue(record.getR39_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR39_c15() != null) {
							cellP.setCellValue(record.getR39_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR39_c16() != null) {
							cellQ.setCellValue(record.getR39_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R40
						// -------------------------------------------------------

						row = sheet.getRow(39);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR40_botswana() != null) {
							cellB.setCellValue(record.getR40_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR40_south_africa() != null) {
							cellC.setCellValue(record.getR40_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR40_sadc() != null) {
							cellD.setCellValue(record.getR40_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR40_usa() != null) {
							cellE.setCellValue(record.getR40_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR40_uk() != null) {
							cellF.setCellValue(record.getR40_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR40_europe() != null) {
							cellG.setCellValue(record.getR40_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR40_india() != null) {
							cellH.setCellValue(record.getR40_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR40_sydney() != null) {
							cellI.setCellValue(record.getR40_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR40_uganda() != null) {
							cellJ.setCellValue(record.getR40_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR40_c10() != null) {
							cellK.setCellValue(record.getR40_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR40_c11() != null) {
							cellL.setCellValue(record.getR40_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR40_c12() != null) {
							cellM.setCellValue(record.getR40_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR40_c13() != null) {
							cellN.setCellValue(record.getR40_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR40_c14() != null) {
							cellO.setCellValue(record.getR40_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR40_c15() != null) {
							cellP.setCellValue(record.getR40_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR40_c16() != null) {
							cellQ.setCellValue(record.getR40_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R41
						// -------------------------------------------------------

						row = sheet.getRow(40);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR41_botswana() != null) {
							cellB.setCellValue(record.getR41_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR41_south_africa() != null) {
							cellC.setCellValue(record.getR41_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR41_sadc() != null) {
							cellD.setCellValue(record.getR41_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR41_usa() != null) {
							cellE.setCellValue(record.getR41_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR41_uk() != null) {
							cellF.setCellValue(record.getR41_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR41_europe() != null) {
							cellG.setCellValue(record.getR41_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR41_india() != null) {
							cellH.setCellValue(record.getR41_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR41_sydney() != null) {
							cellI.setCellValue(record.getR41_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR41_uganda() != null) {
							cellJ.setCellValue(record.getR41_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR41_c10() != null) {
							cellK.setCellValue(record.getR41_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR41_c11() != null) {
							cellL.setCellValue(record.getR41_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR41_c12() != null) {
							cellM.setCellValue(record.getR41_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR41_c13() != null) {
							cellN.setCellValue(record.getR41_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR41_c14() != null) {
							cellO.setCellValue(record.getR41_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR41_c15() != null) {
							cellP.setCellValue(record.getR41_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR41_c16() != null) {
							cellQ.setCellValue(record.getR41_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R42
						// -------------------------------------------------------

						row = sheet.getRow(41);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR42_botswana() != null) {
							cellB.setCellValue(record.getR42_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR42_south_africa() != null) {
							cellC.setCellValue(record.getR42_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR42_sadc() != null) {
							cellD.setCellValue(record.getR42_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR42_usa() != null) {
							cellE.setCellValue(record.getR42_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR42_uk() != null) {
							cellF.setCellValue(record.getR42_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR42_europe() != null) {
							cellG.setCellValue(record.getR42_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR42_india() != null) {
							cellH.setCellValue(record.getR42_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR42_sydney() != null) {
							cellI.setCellValue(record.getR42_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR42_uganda() != null) {
							cellJ.setCellValue(record.getR42_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR42_c10() != null) {
							cellK.setCellValue(record.getR42_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR42_c11() != null) {
							cellL.setCellValue(record.getR42_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR42_c12() != null) {
							cellM.setCellValue(record.getR42_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR42_c13() != null) {
							cellN.setCellValue(record.getR42_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR42_c14() != null) {
							cellO.setCellValue(record.getR42_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR42_c15() != null) {
							cellP.setCellValue(record.getR42_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR42_c16() != null) {
							cellQ.setCellValue(record.getR42_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R43
						// -------------------------------------------------------

						row = sheet.getRow(42);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR43_botswana() != null) {
							cellB.setCellValue(record.getR43_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR43_south_africa() != null) {
							cellC.setCellValue(record.getR43_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR43_sadc() != null) {
							cellD.setCellValue(record.getR43_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR43_usa() != null) {
							cellE.setCellValue(record.getR43_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR43_uk() != null) {
							cellF.setCellValue(record.getR43_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR43_europe() != null) {
							cellG.setCellValue(record.getR43_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR43_india() != null) {
							cellH.setCellValue(record.getR43_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR43_sydney() != null) {
							cellI.setCellValue(record.getR43_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR43_uganda() != null) {
							cellJ.setCellValue(record.getR43_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR43_c10() != null) {
							cellK.setCellValue(record.getR43_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR43_c11() != null) {
							cellL.setCellValue(record.getR43_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR43_c12() != null) {
							cellM.setCellValue(record.getR43_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR43_c13() != null) {
							cellN.setCellValue(record.getR43_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR43_c14() != null) {
							cellO.setCellValue(record.getR43_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR43_c15() != null) {
							cellP.setCellValue(record.getR43_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR43_c16() != null) {
							cellQ.setCellValue(record.getR43_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R44
						// -------------------------------------------------------

						row = sheet.getRow(43);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR44_botswana() != null) {
							cellB.setCellValue(record.getR44_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR44_south_africa() != null) {
							cellC.setCellValue(record.getR44_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR44_sadc() != null) {
							cellD.setCellValue(record.getR44_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR44_usa() != null) {
							cellE.setCellValue(record.getR44_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR44_uk() != null) {
							cellF.setCellValue(record.getR44_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR44_europe() != null) {
							cellG.setCellValue(record.getR44_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR44_india() != null) {
							cellH.setCellValue(record.getR44_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR44_sydney() != null) {
							cellI.setCellValue(record.getR44_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR44_uganda() != null) {
							cellJ.setCellValue(record.getR44_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR44_c10() != null) {
							cellK.setCellValue(record.getR44_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR44_c11() != null) {
							cellL.setCellValue(record.getR44_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR44_c12() != null) {
							cellM.setCellValue(record.getR44_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR44_c13() != null) {
							cellN.setCellValue(record.getR44_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR44_c14() != null) {
							cellO.setCellValue(record.getR44_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR44_c15() != null) {
							cellP.setCellValue(record.getR44_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR44_c16() != null) {
							cellQ.setCellValue(record.getR44_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R45
						// -------------------------------------------------------

						row = sheet.getRow(44);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR45_botswana() != null) {
							cellB.setCellValue(record.getR45_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR45_south_africa() != null) {
							cellC.setCellValue(record.getR45_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR45_sadc() != null) {
							cellD.setCellValue(record.getR45_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR45_usa() != null) {
							cellE.setCellValue(record.getR45_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR45_uk() != null) {
							cellF.setCellValue(record.getR45_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR45_europe() != null) {
							cellG.setCellValue(record.getR45_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR45_india() != null) {
							cellH.setCellValue(record.getR45_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR45_sydney() != null) {
							cellI.setCellValue(record.getR45_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR45_uganda() != null) {
							cellJ.setCellValue(record.getR45_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR45_c10() != null) {
							cellK.setCellValue(record.getR45_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR45_c11() != null) {
							cellL.setCellValue(record.getR45_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR45_c12() != null) {
							cellM.setCellValue(record.getR45_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR45_c13() != null) {
							cellN.setCellValue(record.getR45_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR45_c14() != null) {
							cellO.setCellValue(record.getR45_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR45_c15() != null) {
							cellP.setCellValue(record.getR45_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR45_c16() != null) {
							cellQ.setCellValue(record.getR45_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R46
						// -------------------------------------------------------

						row = sheet.getRow(45);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR46_botswana() != null) {
							cellB.setCellValue(record.getR46_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR46_south_africa() != null) {
							cellC.setCellValue(record.getR46_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR46_sadc() != null) {
							cellD.setCellValue(record.getR46_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR46_usa() != null) {
							cellE.setCellValue(record.getR46_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR46_uk() != null) {
							cellF.setCellValue(record.getR46_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR46_europe() != null) {
							cellG.setCellValue(record.getR46_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR46_india() != null) {
							cellH.setCellValue(record.getR46_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR46_sydney() != null) {
							cellI.setCellValue(record.getR46_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR46_uganda() != null) {
							cellJ.setCellValue(record.getR46_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR46_c10() != null) {
							cellK.setCellValue(record.getR46_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR46_c11() != null) {
							cellL.setCellValue(record.getR46_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR46_c12() != null) {
							cellM.setCellValue(record.getR46_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR46_c13() != null) {
							cellN.setCellValue(record.getR46_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR46_c14() != null) {
							cellO.setCellValue(record.getR46_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR46_c15() != null) {
							cellP.setCellValue(record.getR46_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR46_c16() != null) {
							cellQ.setCellValue(record.getR46_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R47
						// -------------------------------------------------------

						row = sheet.getRow(46);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR47_botswana() != null) {
							cellB.setCellValue(record.getR47_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR47_south_africa() != null) {
							cellC.setCellValue(record.getR47_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR47_sadc() != null) {
							cellD.setCellValue(record.getR47_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR47_usa() != null) {
							cellE.setCellValue(record.getR47_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR47_uk() != null) {
							cellF.setCellValue(record.getR47_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR47_europe() != null) {
							cellG.setCellValue(record.getR47_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR47_india() != null) {
							cellH.setCellValue(record.getR47_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR47_sydney() != null) {
							cellI.setCellValue(record.getR47_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR47_uganda() != null) {
							cellJ.setCellValue(record.getR47_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR47_c10() != null) {
							cellK.setCellValue(record.getR47_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR47_c11() != null) {
							cellL.setCellValue(record.getR47_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR47_c12() != null) {
							cellM.setCellValue(record.getR47_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR47_c13() != null) {
							cellN.setCellValue(record.getR47_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR47_c14() != null) {
							cellO.setCellValue(record.getR47_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR47_c15() != null) {
							cellP.setCellValue(record.getR47_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR47_c16() != null) {
							cellQ.setCellValue(record.getR47_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R48
						// -------------------------------------------------------

						row = sheet.getRow(47);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR48_botswana() != null) {
							cellB.setCellValue(record.getR48_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR48_south_africa() != null) {
							cellC.setCellValue(record.getR48_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR48_sadc() != null) {
							cellD.setCellValue(record.getR48_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR48_usa() != null) {
							cellE.setCellValue(record.getR48_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR48_uk() != null) {
							cellF.setCellValue(record.getR48_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR48_europe() != null) {
							cellG.setCellValue(record.getR48_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR48_india() != null) {
							cellH.setCellValue(record.getR48_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR48_sydney() != null) {
							cellI.setCellValue(record.getR48_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR48_uganda() != null) {
							cellJ.setCellValue(record.getR48_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR48_c10() != null) {
							cellK.setCellValue(record.getR48_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR48_c11() != null) {
							cellL.setCellValue(record.getR48_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR48_c12() != null) {
							cellM.setCellValue(record.getR48_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR48_c13() != null) {
							cellN.setCellValue(record.getR48_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR48_c14() != null) {
							cellO.setCellValue(record.getR48_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR48_c15() != null) {
							cellP.setCellValue(record.getR48_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR48_c16() != null) {
							cellQ.setCellValue(record.getR48_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R49
						// -------------------------------------------------------

						row = sheet.getRow(48);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR49_botswana() != null) {
							cellB.setCellValue(record.getR49_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR49_south_africa() != null) {
							cellC.setCellValue(record.getR49_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR49_sadc() != null) {
							cellD.setCellValue(record.getR49_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR49_usa() != null) {
							cellE.setCellValue(record.getR49_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR49_uk() != null) {
							cellF.setCellValue(record.getR49_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR49_europe() != null) {
							cellG.setCellValue(record.getR49_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR49_india() != null) {
							cellH.setCellValue(record.getR49_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR49_sydney() != null) {
							cellI.setCellValue(record.getR49_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR49_uganda() != null) {
							cellJ.setCellValue(record.getR49_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR49_c10() != null) {
							cellK.setCellValue(record.getR49_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR49_c11() != null) {
							cellL.setCellValue(record.getR49_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR49_c12() != null) {
							cellM.setCellValue(record.getR49_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR49_c13() != null) {
							cellN.setCellValue(record.getR49_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR49_c14() != null) {
							cellO.setCellValue(record.getR49_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR49_c15() != null) {
							cellP.setCellValue(record.getR49_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR49_c16() != null) {
							cellQ.setCellValue(record.getR49_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R50
						// -------------------------------------------------------

						row = sheet.getRow(49);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR50_botswana() != null) {
							cellB.setCellValue(record.getR50_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR50_south_africa() != null) {
							cellC.setCellValue(record.getR50_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR50_sadc() != null) {
							cellD.setCellValue(record.getR50_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR50_usa() != null) {
							cellE.setCellValue(record.getR50_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR50_uk() != null) {
							cellF.setCellValue(record.getR50_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR50_europe() != null) {
							cellG.setCellValue(record.getR50_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR50_india() != null) {
							cellH.setCellValue(record.getR50_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR50_sydney() != null) {
							cellI.setCellValue(record.getR50_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR50_uganda() != null) {
							cellJ.setCellValue(record.getR50_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR50_c10() != null) {
							cellK.setCellValue(record.getR50_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR50_c11() != null) {
							cellL.setCellValue(record.getR50_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR50_c12() != null) {
							cellM.setCellValue(record.getR50_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR50_c13() != null) {
							cellN.setCellValue(record.getR50_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR50_c14() != null) {
							cellO.setCellValue(record.getR50_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR50_c15() != null) {
							cellP.setCellValue(record.getR50_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR50_c16() != null) {
							cellQ.setCellValue(record.getR50_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R51
						// -------------------------------------------------------

						row = sheet.getRow(50);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR51_botswana() != null) {
							cellB.setCellValue(record.getR51_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR51_south_africa() != null) {
							cellC.setCellValue(record.getR51_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR51_sadc() != null) {
							cellD.setCellValue(record.getR51_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR51_usa() != null) {
							cellE.setCellValue(record.getR51_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR51_uk() != null) {
							cellF.setCellValue(record.getR51_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR51_europe() != null) {
							cellG.setCellValue(record.getR51_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR51_india() != null) {
							cellH.setCellValue(record.getR51_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR51_sydney() != null) {
							cellI.setCellValue(record.getR51_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR51_uganda() != null) {
							cellJ.setCellValue(record.getR51_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR51_c10() != null) {
							cellK.setCellValue(record.getR51_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR51_c11() != null) {
							cellL.setCellValue(record.getR51_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR51_c12() != null) {
							cellM.setCellValue(record.getR51_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR51_c13() != null) {
							cellN.setCellValue(record.getR51_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR51_c14() != null) {
							cellO.setCellValue(record.getR51_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR51_c15() != null) {
							cellP.setCellValue(record.getR51_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR51_c16() != null) {
							cellQ.setCellValue(record.getR51_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R52
						// -------------------------------------------------------

						row = sheet.getRow(51);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR52_botswana() != null) {
							cellB.setCellValue(record.getR52_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR52_south_africa() != null) {
							cellC.setCellValue(record.getR52_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR52_sadc() != null) {
							cellD.setCellValue(record.getR52_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR52_usa() != null) {
							cellE.setCellValue(record.getR52_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR52_uk() != null) {
							cellF.setCellValue(record.getR52_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR52_europe() != null) {
							cellG.setCellValue(record.getR52_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR52_india() != null) {
							cellH.setCellValue(record.getR52_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR52_sydney() != null) {
							cellI.setCellValue(record.getR52_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR52_uganda() != null) {
							cellJ.setCellValue(record.getR52_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR52_c10() != null) {
							cellK.setCellValue(record.getR52_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR52_c11() != null) {
							cellL.setCellValue(record.getR52_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR52_c12() != null) {
							cellM.setCellValue(record.getR52_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR52_c13() != null) {
							cellN.setCellValue(record.getR52_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR52_c14() != null) {
							cellO.setCellValue(record.getR52_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR52_c15() != null) {
							cellP.setCellValue(record.getR52_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR52_c16() != null) {
							cellQ.setCellValue(record.getR52_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R53
						// -------------------------------------------------------

						row = sheet.getRow(52);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR53_botswana() != null) {
							cellB.setCellValue(record.getR53_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR53_south_africa() != null) {
							cellC.setCellValue(record.getR53_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR53_sadc() != null) {
							cellD.setCellValue(record.getR53_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR53_usa() != null) {
							cellE.setCellValue(record.getR53_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR53_uk() != null) {
							cellF.setCellValue(record.getR53_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR53_europe() != null) {
							cellG.setCellValue(record.getR53_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR53_india() != null) {
							cellH.setCellValue(record.getR53_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR53_sydney() != null) {
							cellI.setCellValue(record.getR53_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR53_uganda() != null) {
							cellJ.setCellValue(record.getR53_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR53_c10() != null) {
							cellK.setCellValue(record.getR53_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR53_c11() != null) {
							cellL.setCellValue(record.getR53_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR53_c12() != null) {
							cellM.setCellValue(record.getR53_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR53_c13() != null) {
							cellN.setCellValue(record.getR53_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR53_c14() != null) {
							cellO.setCellValue(record.getR53_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR53_c15() != null) {
							cellP.setCellValue(record.getR53_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR53_c16() != null) {
							cellQ.setCellValue(record.getR53_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R54
						// -------------------------------------------------------

						row = sheet.getRow(53);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR54_botswana() != null) {
							cellB.setCellValue(record.getR54_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR54_south_africa() != null) {
							cellC.setCellValue(record.getR54_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR54_sadc() != null) {
							cellD.setCellValue(record.getR54_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR54_usa() != null) {
							cellE.setCellValue(record.getR54_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR54_uk() != null) {
							cellF.setCellValue(record.getR54_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR54_europe() != null) {
							cellG.setCellValue(record.getR54_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR54_india() != null) {
							cellH.setCellValue(record.getR54_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR54_sydney() != null) {
							cellI.setCellValue(record.getR54_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR54_uganda() != null) {
							cellJ.setCellValue(record.getR54_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR54_c10() != null) {
							cellK.setCellValue(record.getR54_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR54_c11() != null) {
							cellL.setCellValue(record.getR54_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR54_c12() != null) {
							cellM.setCellValue(record.getR54_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR54_c13() != null) {
							cellN.setCellValue(record.getR54_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR54_c14() != null) {
							cellO.setCellValue(record.getR54_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR54_c15() != null) {
							cellP.setCellValue(record.getR54_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR54_c16() != null) {
							cellQ.setCellValue(record.getR54_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R55
						// -------------------------------------------------------

						row = sheet.getRow(54);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR55_botswana() != null) {
							cellB.setCellValue(record.getR55_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR55_south_africa() != null) {
							cellC.setCellValue(record.getR55_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR55_sadc() != null) {
							cellD.setCellValue(record.getR55_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR55_usa() != null) {
							cellE.setCellValue(record.getR55_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR55_uk() != null) {
							cellF.setCellValue(record.getR55_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR55_europe() != null) {
							cellG.setCellValue(record.getR55_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR55_india() != null) {
							cellH.setCellValue(record.getR55_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR55_sydney() != null) {
							cellI.setCellValue(record.getR55_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR55_uganda() != null) {
							cellJ.setCellValue(record.getR55_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR55_c10() != null) {
							cellK.setCellValue(record.getR55_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR55_c11() != null) {
							cellL.setCellValue(record.getR55_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR55_c12() != null) {
							cellM.setCellValue(record.getR55_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR55_c13() != null) {
							cellN.setCellValue(record.getR55_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR55_c14() != null) {
							cellO.setCellValue(record.getR55_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR55_c15() != null) {
							cellP.setCellValue(record.getR55_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR55_c16() != null) {
							cellQ.setCellValue(record.getR55_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R56
						// -------------------------------------------------------

						row = sheet.getRow(55);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR56_botswana() != null) {
							cellB.setCellValue(record.getR56_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR56_south_africa() != null) {
							cellC.setCellValue(record.getR56_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR56_sadc() != null) {
							cellD.setCellValue(record.getR56_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR56_usa() != null) {
							cellE.setCellValue(record.getR56_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR56_uk() != null) {
							cellF.setCellValue(record.getR56_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR56_europe() != null) {
							cellG.setCellValue(record.getR56_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR56_india() != null) {
							cellH.setCellValue(record.getR56_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR56_sydney() != null) {
							cellI.setCellValue(record.getR56_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR56_uganda() != null) {
							cellJ.setCellValue(record.getR56_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR56_c10() != null) {
							cellK.setCellValue(record.getR56_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR56_c11() != null) {
							cellL.setCellValue(record.getR56_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR56_c12() != null) {
							cellM.setCellValue(record.getR56_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR56_c13() != null) {
							cellN.setCellValue(record.getR56_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR56_c14() != null) {
							cellO.setCellValue(record.getR56_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR56_c15() != null) {
							cellP.setCellValue(record.getR56_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR56_c16() != null) {
							cellQ.setCellValue(record.getR56_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R59
						// -------------------------------------------------------

						row = sheet.getRow(58);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record.getR59_botswana() != null) {
							cellB.setCellValue(record.getR59_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record.getR59_south_africa() != null) {
							cellC.setCellValue(record.getR59_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record.getR59_sadc() != null) {
							cellD.setCellValue(record.getR59_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record.getR59_usa() != null) {
							cellE.setCellValue(record.getR59_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record.getR59_uk() != null) {
							cellF.setCellValue(record.getR59_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record.getR59_europe() != null) {
							cellG.setCellValue(record.getR59_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record.getR59_india() != null) {
							cellH.setCellValue(record.getR59_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record.getR59_sydney() != null) {
							cellI.setCellValue(record.getR59_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record.getR59_uganda() != null) {
							cellJ.setCellValue(record.getR59_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record.getR59_c10() != null) {
							cellK.setCellValue(record.getR59_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record.getR59_c11() != null) {
							cellL.setCellValue(record.getR59_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record.getR59_c12() != null) {
							cellM.setCellValue(record.getR59_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record.getR59_c13() != null) {
							cellN.setCellValue(record.getR59_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record.getR59_c14() != null) {
							cellO.setCellValue(record.getR59_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record.getR59_c15() != null) {
							cellP.setCellValue(record.getR59_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record.getR59_c16() != null) {
							cellQ.setCellValue(record.getR59_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

					}

					private void populateEntity2Data(Sheet sheet, M_GALOR_Archival_Summary_Entity2 record1, CellStyle textStyle,
							CellStyle numberStyle) {

						// R70 - ROW 70 (Index 69)
						Row row = sheet.getRow(69) != null ? sheet.getRow(69) : sheet.createRow(69);

						Cell cellB, cellC, cellD, cellE, cellF, cellG, cellH, cellI, cellJ, cellK, cellL, cellM, cellN, cellO, cellP,
								cellQ;

						// -------------------------------------------------------
						// ROW R70 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(69);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR70_botswana() != null) {
							cellB.setCellValue(record1.getR70_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR70_south_africa() != null) {
							cellC.setCellValue(record1.getR70_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR70_sadc() != null) {
							cellD.setCellValue(record1.getR70_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR70_usa() != null) {
							cellE.setCellValue(record1.getR70_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR70_uk() != null) {
							cellF.setCellValue(record1.getR70_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR70_europe() != null) {
							cellG.setCellValue(record1.getR70_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR70_india() != null) {
							cellH.setCellValue(record1.getR70_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR70_sydney() != null) {
							cellI.setCellValue(record1.getR70_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR70_uganda() != null) {
							cellJ.setCellValue(record1.getR70_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR70_c10() != null) {
							cellK.setCellValue(record1.getR70_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR70_c11() != null) {
							cellL.setCellValue(record1.getR70_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR70_c12() != null) {
							cellM.setCellValue(record1.getR70_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR70_c13() != null) {
							cellN.setCellValue(record1.getR70_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR70_c14() != null) {
							cellO.setCellValue(record1.getR70_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR70_c15() != null) {
							cellP.setCellValue(record1.getR70_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR70_c16() != null) {
							cellQ.setCellValue(record1.getR70_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R71 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(70);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR71_botswana() != null) {
							cellB.setCellValue(record1.getR71_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR71_south_africa() != null) {
							cellC.setCellValue(record1.getR71_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR71_sadc() != null) {
							cellD.setCellValue(record1.getR71_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR71_usa() != null) {
							cellE.setCellValue(record1.getR71_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR71_uk() != null) {
							cellF.setCellValue(record1.getR71_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR71_europe() != null) {
							cellG.setCellValue(record1.getR71_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR71_india() != null) {
							cellH.setCellValue(record1.getR71_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR71_sydney() != null) {
							cellI.setCellValue(record1.getR71_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR71_uganda() != null) {
							cellJ.setCellValue(record1.getR71_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR71_c10() != null) {
							cellK.setCellValue(record1.getR71_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR71_c11() != null) {
							cellL.setCellValue(record1.getR71_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR71_c12() != null) {
							cellM.setCellValue(record1.getR71_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR71_c13() != null) {
							cellN.setCellValue(record1.getR71_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR71_c14() != null) {
							cellO.setCellValue(record1.getR71_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR71_c15() != null) {
							cellP.setCellValue(record1.getR71_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR71_c16() != null) {
							cellQ.setCellValue(record1.getR71_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R72 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(71);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR72_botswana() != null) {
							cellB.setCellValue(record1.getR72_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR72_south_africa() != null) {
							cellC.setCellValue(record1.getR72_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR72_sadc() != null) {
							cellD.setCellValue(record1.getR72_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR72_usa() != null) {
							cellE.setCellValue(record1.getR72_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR72_uk() != null) {
							cellF.setCellValue(record1.getR72_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR72_europe() != null) {
							cellG.setCellValue(record1.getR72_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR72_india() != null) {
							cellH.setCellValue(record1.getR72_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR72_sydney() != null) {
							cellI.setCellValue(record1.getR72_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR72_uganda() != null) {
							cellJ.setCellValue(record1.getR72_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR72_c10() != null) {
							cellK.setCellValue(record1.getR72_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR72_c11() != null) {
							cellL.setCellValue(record1.getR72_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR72_c12() != null) {
							cellM.setCellValue(record1.getR72_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR72_c13() != null) {
							cellN.setCellValue(record1.getR72_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR72_c14() != null) {
							cellO.setCellValue(record1.getR72_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR72_c15() != null) {
							cellP.setCellValue(record1.getR72_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR72_c16() != null) {
							cellQ.setCellValue(record1.getR72_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R73 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(72);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR73_botswana() != null) {
							cellB.setCellValue(record1.getR73_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR73_south_africa() != null) {
							cellC.setCellValue(record1.getR73_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR73_sadc() != null) {
							cellD.setCellValue(record1.getR73_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR73_usa() != null) {
							cellE.setCellValue(record1.getR73_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR73_uk() != null) {
							cellF.setCellValue(record1.getR73_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR73_europe() != null) {
							cellG.setCellValue(record1.getR73_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR73_india() != null) {
							cellH.setCellValue(record1.getR73_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR73_sydney() != null) {
							cellI.setCellValue(record1.getR73_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR73_uganda() != null) {
							cellJ.setCellValue(record1.getR73_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR73_c10() != null) {
							cellK.setCellValue(record1.getR73_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR73_c11() != null) {
							cellL.setCellValue(record1.getR73_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR73_c12() != null) {
							cellM.setCellValue(record1.getR73_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR73_c13() != null) {
							cellN.setCellValue(record1.getR73_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR73_c14() != null) {
							cellO.setCellValue(record1.getR73_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR73_c15() != null) {
							cellP.setCellValue(record1.getR73_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR73_c16() != null) {
							cellQ.setCellValue(record1.getR73_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R74 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(73);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR74_botswana() != null) {
							cellB.setCellValue(record1.getR74_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR74_south_africa() != null) {
							cellC.setCellValue(record1.getR74_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR74_sadc() != null) {
							cellD.setCellValue(record1.getR74_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR74_usa() != null) {
							cellE.setCellValue(record1.getR74_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR74_uk() != null) {
							cellF.setCellValue(record1.getR74_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR74_europe() != null) {
							cellG.setCellValue(record1.getR74_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR74_india() != null) {
							cellH.setCellValue(record1.getR74_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR74_sydney() != null) {
							cellI.setCellValue(record1.getR74_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR74_uganda() != null) {
							cellJ.setCellValue(record1.getR74_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR74_c10() != null) {
							cellK.setCellValue(record1.getR74_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR74_c11() != null) {
							cellL.setCellValue(record1.getR74_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR74_c12() != null) {
							cellM.setCellValue(record1.getR74_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR74_c13() != null) {
							cellN.setCellValue(record1.getR74_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR74_c14() != null) {
							cellO.setCellValue(record1.getR74_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR74_c15() != null) {
							cellP.setCellValue(record1.getR74_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR74_c16() != null) {
							cellQ.setCellValue(record1.getR74_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R75 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(74);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR75_botswana() != null) {
							cellB.setCellValue(record1.getR75_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR75_south_africa() != null) {
							cellC.setCellValue(record1.getR75_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR75_sadc() != null) {
							cellD.setCellValue(record1.getR75_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR75_usa() != null) {
							cellE.setCellValue(record1.getR75_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR75_uk() != null) {
							cellF.setCellValue(record1.getR75_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR75_europe() != null) {
							cellG.setCellValue(record1.getR75_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR75_india() != null) {
							cellH.setCellValue(record1.getR75_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR75_sydney() != null) {
							cellI.setCellValue(record1.getR75_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR75_uganda() != null) {
							cellJ.setCellValue(record1.getR75_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR75_c10() != null) {
							cellK.setCellValue(record1.getR75_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR75_c11() != null) {
							cellL.setCellValue(record1.getR75_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR75_c12() != null) {
							cellM.setCellValue(record1.getR75_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR75_c13() != null) {
							cellN.setCellValue(record1.getR75_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR75_c14() != null) {
							cellO.setCellValue(record1.getR75_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR75_c15() != null) {
							cellP.setCellValue(record1.getR75_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR75_c16() != null) {
							cellQ.setCellValue(record1.getR75_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R76 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(75);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR76_botswana() != null) {
							cellB.setCellValue(record1.getR76_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR76_south_africa() != null) {
							cellC.setCellValue(record1.getR76_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR76_sadc() != null) {
							cellD.setCellValue(record1.getR76_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR76_usa() != null) {
							cellE.setCellValue(record1.getR76_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR76_uk() != null) {
							cellF.setCellValue(record1.getR76_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR76_europe() != null) {
							cellG.setCellValue(record1.getR76_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR76_india() != null) {
							cellH.setCellValue(record1.getR76_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR76_sydney() != null) {
							cellI.setCellValue(record1.getR76_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR76_uganda() != null) {
							cellJ.setCellValue(record1.getR76_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR76_c10() != null) {
							cellK.setCellValue(record1.getR76_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR76_c11() != null) {
							cellL.setCellValue(record1.getR76_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR76_c12() != null) {
							cellM.setCellValue(record1.getR76_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR76_c13() != null) {
							cellN.setCellValue(record1.getR76_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR76_c14() != null) {
							cellO.setCellValue(record1.getR76_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR76_c15() != null) {
							cellP.setCellValue(record1.getR76_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR76_c16() != null) {
							cellQ.setCellValue(record1.getR76_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R77 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(76);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR77_botswana() != null) {
							cellB.setCellValue(record1.getR77_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR77_south_africa() != null) {
							cellC.setCellValue(record1.getR77_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR77_sadc() != null) {
							cellD.setCellValue(record1.getR77_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR77_usa() != null) {
							cellE.setCellValue(record1.getR77_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR77_uk() != null) {
							cellF.setCellValue(record1.getR77_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR77_europe() != null) {
							cellG.setCellValue(record1.getR77_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR77_india() != null) {
							cellH.setCellValue(record1.getR77_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR77_sydney() != null) {
							cellI.setCellValue(record1.getR77_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR77_uganda() != null) {
							cellJ.setCellValue(record1.getR77_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR77_c10() != null) {
							cellK.setCellValue(record1.getR77_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR77_c11() != null) {
							cellL.setCellValue(record1.getR77_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR77_c12() != null) {
							cellM.setCellValue(record1.getR77_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR77_c13() != null) {
							cellN.setCellValue(record1.getR77_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR77_c14() != null) {
							cellO.setCellValue(record1.getR77_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR77_c15() != null) {
							cellP.setCellValue(record1.getR77_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR77_c16() != null) {
							cellQ.setCellValue(record1.getR77_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R78 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(77);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR78_botswana() != null) {
							cellB.setCellValue(record1.getR78_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR78_south_africa() != null) {
							cellC.setCellValue(record1.getR78_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR78_sadc() != null) {
							cellD.setCellValue(record1.getR78_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR78_usa() != null) {
							cellE.setCellValue(record1.getR78_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR78_uk() != null) {
							cellF.setCellValue(record1.getR78_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR78_europe() != null) {
							cellG.setCellValue(record1.getR78_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR78_india() != null) {
							cellH.setCellValue(record1.getR78_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR78_sydney() != null) {
							cellI.setCellValue(record1.getR78_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR78_uganda() != null) {
							cellJ.setCellValue(record1.getR78_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR78_c10() != null) {
							cellK.setCellValue(record1.getR78_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR78_c11() != null) {
							cellL.setCellValue(record1.getR78_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR78_c12() != null) {
							cellM.setCellValue(record1.getR78_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR78_c13() != null) {
							cellN.setCellValue(record1.getR78_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR78_c14() != null) {
							cellO.setCellValue(record1.getR78_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR78_c15() != null) {
							cellP.setCellValue(record1.getR78_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR78_c16() != null) {
							cellQ.setCellValue(record1.getR78_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R79 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(78);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR79_botswana() != null) {
							cellB.setCellValue(record1.getR79_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR79_south_africa() != null) {
							cellC.setCellValue(record1.getR79_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR79_sadc() != null) {
							cellD.setCellValue(record1.getR79_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR79_usa() != null) {
							cellE.setCellValue(record1.getR79_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR79_uk() != null) {
							cellF.setCellValue(record1.getR79_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR79_europe() != null) {
							cellG.setCellValue(record1.getR79_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR79_india() != null) {
							cellH.setCellValue(record1.getR79_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR79_sydney() != null) {
							cellI.setCellValue(record1.getR79_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR79_uganda() != null) {
							cellJ.setCellValue(record1.getR79_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR79_c10() != null) {
							cellK.setCellValue(record1.getR79_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR79_c11() != null) {
							cellL.setCellValue(record1.getR79_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR79_c12() != null) {
							cellM.setCellValue(record1.getR79_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR79_c13() != null) {
							cellN.setCellValue(record1.getR79_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR79_c14() != null) {
							cellO.setCellValue(record1.getR79_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR79_c15() != null) {
							cellP.setCellValue(record1.getR79_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR79_c16() != null) {
							cellQ.setCellValue(record1.getR79_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R80 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(79);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR80_botswana() != null) {
							cellB.setCellValue(record1.getR80_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR80_south_africa() != null) {
							cellC.setCellValue(record1.getR80_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR80_sadc() != null) {
							cellD.setCellValue(record1.getR80_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR80_usa() != null) {
							cellE.setCellValue(record1.getR80_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR80_uk() != null) {
							cellF.setCellValue(record1.getR80_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR80_europe() != null) {
							cellG.setCellValue(record1.getR80_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR80_india() != null) {
							cellH.setCellValue(record1.getR80_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR80_sydney() != null) {
							cellI.setCellValue(record1.getR80_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR80_uganda() != null) {
							cellJ.setCellValue(record1.getR80_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR80_c10() != null) {
							cellK.setCellValue(record1.getR80_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR80_c11() != null) {
							cellL.setCellValue(record1.getR80_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR80_c12() != null) {
							cellM.setCellValue(record1.getR80_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR80_c13() != null) {
							cellN.setCellValue(record1.getR80_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR80_c14() != null) {
							cellO.setCellValue(record1.getR80_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR80_c15() != null) {
							cellP.setCellValue(record1.getR80_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR80_c16() != null) {
							cellQ.setCellValue(record1.getR80_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R81 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(80);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR81_botswana() != null) {
							cellB.setCellValue(record1.getR81_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR81_south_africa() != null) {
							cellC.setCellValue(record1.getR81_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR81_sadc() != null) {
							cellD.setCellValue(record1.getR81_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR81_usa() != null) {
							cellE.setCellValue(record1.getR81_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR81_uk() != null) {
							cellF.setCellValue(record1.getR81_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR81_europe() != null) {
							cellG.setCellValue(record1.getR81_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR81_india() != null) {
							cellH.setCellValue(record1.getR81_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR81_sydney() != null) {
							cellI.setCellValue(record1.getR81_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR81_uganda() != null) {
							cellJ.setCellValue(record1.getR81_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR81_c10() != null) {
							cellK.setCellValue(record1.getR81_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR81_c11() != null) {
							cellL.setCellValue(record1.getR81_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR81_c12() != null) {
							cellM.setCellValue(record1.getR81_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR81_c13() != null) {
							cellN.setCellValue(record1.getR81_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR81_c14() != null) {
							cellO.setCellValue(record1.getR81_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR81_c15() != null) {
							cellP.setCellValue(record1.getR81_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR81_c16() != null) {
							cellQ.setCellValue(record1.getR81_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R82 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(81);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR82_botswana() != null) {
							cellB.setCellValue(record1.getR82_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR82_south_africa() != null) {
							cellC.setCellValue(record1.getR82_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR82_sadc() != null) {
							cellD.setCellValue(record1.getR82_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR82_usa() != null) {
							cellE.setCellValue(record1.getR82_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR82_uk() != null) {
							cellF.setCellValue(record1.getR82_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR82_europe() != null) {
							cellG.setCellValue(record1.getR82_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR82_india() != null) {
							cellH.setCellValue(record1.getR82_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR82_sydney() != null) {
							cellI.setCellValue(record1.getR82_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR82_uganda() != null) {
							cellJ.setCellValue(record1.getR82_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR82_c10() != null) {
							cellK.setCellValue(record1.getR82_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR82_c11() != null) {
							cellL.setCellValue(record1.getR82_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR82_c12() != null) {
							cellM.setCellValue(record1.getR82_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR82_c13() != null) {
							cellN.setCellValue(record1.getR82_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR82_c14() != null) {
							cellO.setCellValue(record1.getR82_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR82_c15() != null) {
							cellP.setCellValue(record1.getR82_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR82_c16() != null) {
							cellQ.setCellValue(record1.getR82_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R83 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(82);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR83_botswana() != null) {
							cellB.setCellValue(record1.getR83_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR83_south_africa() != null) {
							cellC.setCellValue(record1.getR83_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR83_sadc() != null) {
							cellD.setCellValue(record1.getR83_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR83_usa() != null) {
							cellE.setCellValue(record1.getR83_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR83_uk() != null) {
							cellF.setCellValue(record1.getR83_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR83_europe() != null) {
							cellG.setCellValue(record1.getR83_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR83_india() != null) {
							cellH.setCellValue(record1.getR83_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR83_sydney() != null) {
							cellI.setCellValue(record1.getR83_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR83_uganda() != null) {
							cellJ.setCellValue(record1.getR83_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR83_c10() != null) {
							cellK.setCellValue(record1.getR83_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR83_c11() != null) {
							cellL.setCellValue(record1.getR83_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR83_c12() != null) {
							cellM.setCellValue(record1.getR83_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR83_c13() != null) {
							cellN.setCellValue(record1.getR83_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR83_c14() != null) {
							cellO.setCellValue(record1.getR83_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR83_c15() != null) {
							cellP.setCellValue(record1.getR83_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR83_c16() != null) {
							cellQ.setCellValue(record1.getR83_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R84 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(83);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR84_botswana() != null) {
							cellB.setCellValue(record1.getR84_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR84_south_africa() != null) {
							cellC.setCellValue(record1.getR84_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR84_sadc() != null) {
							cellD.setCellValue(record1.getR84_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR84_usa() != null) {
							cellE.setCellValue(record1.getR84_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR84_uk() != null) {
							cellF.setCellValue(record1.getR84_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR84_europe() != null) {
							cellG.setCellValue(record1.getR84_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR84_india() != null) {
							cellH.setCellValue(record1.getR84_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR84_sydney() != null) {
							cellI.setCellValue(record1.getR84_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR84_uganda() != null) {
							cellJ.setCellValue(record1.getR84_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR84_c10() != null) {
							cellK.setCellValue(record1.getR84_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR84_c11() != null) {
							cellL.setCellValue(record1.getR84_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR84_c12() != null) {
							cellM.setCellValue(record1.getR84_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR84_c13() != null) {
							cellN.setCellValue(record1.getR84_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR84_c14() != null) {
							cellO.setCellValue(record1.getR84_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR84_c15() != null) {
							cellP.setCellValue(record1.getR84_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR84_c16() != null) {
							cellQ.setCellValue(record1.getR84_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R85 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(84);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR85_botswana() != null) {
							cellB.setCellValue(record1.getR85_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR85_south_africa() != null) {
							cellC.setCellValue(record1.getR85_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR85_sadc() != null) {
							cellD.setCellValue(record1.getR85_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR85_usa() != null) {
							cellE.setCellValue(record1.getR85_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR85_uk() != null) {
							cellF.setCellValue(record1.getR85_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR85_europe() != null) {
							cellG.setCellValue(record1.getR85_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR85_india() != null) {
							cellH.setCellValue(record1.getR85_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR85_sydney() != null) {
							cellI.setCellValue(record1.getR85_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR85_uganda() != null) {
							cellJ.setCellValue(record1.getR85_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR85_c10() != null) {
							cellK.setCellValue(record1.getR85_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR85_c11() != null) {
							cellL.setCellValue(record1.getR85_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR85_c12() != null) {
							cellM.setCellValue(record1.getR85_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR85_c13() != null) {
							cellN.setCellValue(record1.getR85_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR85_c14() != null) {
							cellO.setCellValue(record1.getR85_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR85_c15() != null) {
							cellP.setCellValue(record1.getR85_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR85_c16() != null) {
							cellQ.setCellValue(record1.getR85_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R86 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(85);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR86_botswana() != null) {
							cellB.setCellValue(record1.getR86_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR86_south_africa() != null) {
							cellC.setCellValue(record1.getR86_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR86_sadc() != null) {
							cellD.setCellValue(record1.getR86_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR86_usa() != null) {
							cellE.setCellValue(record1.getR86_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR86_uk() != null) {
							cellF.setCellValue(record1.getR86_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR86_europe() != null) {
							cellG.setCellValue(record1.getR86_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR86_india() != null) {
							cellH.setCellValue(record1.getR86_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR86_sydney() != null) {
							cellI.setCellValue(record1.getR86_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR86_uganda() != null) {
							cellJ.setCellValue(record1.getR86_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR86_c10() != null) {
							cellK.setCellValue(record1.getR86_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR86_c11() != null) {
							cellL.setCellValue(record1.getR86_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR86_c12() != null) {
							cellM.setCellValue(record1.getR86_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR86_c13() != null) {
							cellN.setCellValue(record1.getR86_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR86_c14() != null) {
							cellO.setCellValue(record1.getR86_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR86_c15() != null) {
							cellP.setCellValue(record1.getR86_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR86_c16() != null) {
							cellQ.setCellValue(record1.getR86_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R87 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(86);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR87_botswana() != null) {
							cellB.setCellValue(record1.getR87_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR87_south_africa() != null) {
							cellC.setCellValue(record1.getR87_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR87_sadc() != null) {
							cellD.setCellValue(record1.getR87_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR87_usa() != null) {
							cellE.setCellValue(record1.getR87_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR87_uk() != null) {
							cellF.setCellValue(record1.getR87_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR87_europe() != null) {
							cellG.setCellValue(record1.getR87_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR87_india() != null) {
							cellH.setCellValue(record1.getR87_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR87_sydney() != null) {
							cellI.setCellValue(record1.getR87_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR87_uganda() != null) {
							cellJ.setCellValue(record1.getR87_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR87_c10() != null) {
							cellK.setCellValue(record1.getR87_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR87_c11() != null) {
							cellL.setCellValue(record1.getR87_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR87_c12() != null) {
							cellM.setCellValue(record1.getR87_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR87_c13() != null) {
							cellN.setCellValue(record1.getR87_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR87_c14() != null) {
							cellO.setCellValue(record1.getR87_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR87_c15() != null) {
							cellP.setCellValue(record1.getR87_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR87_c16() != null) {
							cellQ.setCellValue(record1.getR87_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R88 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(87);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR88_botswana() != null) {
							cellB.setCellValue(record1.getR88_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR88_south_africa() != null) {
							cellC.setCellValue(record1.getR88_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR88_sadc() != null) {
							cellD.setCellValue(record1.getR88_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR88_usa() != null) {
							cellE.setCellValue(record1.getR88_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR88_uk() != null) {
							cellF.setCellValue(record1.getR88_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR88_europe() != null) {
							cellG.setCellValue(record1.getR88_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR88_india() != null) {
							cellH.setCellValue(record1.getR88_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR88_sydney() != null) {
							cellI.setCellValue(record1.getR88_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR88_uganda() != null) {
							cellJ.setCellValue(record1.getR88_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR88_c10() != null) {
							cellK.setCellValue(record1.getR88_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR88_c11() != null) {
							cellL.setCellValue(record1.getR88_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR88_c12() != null) {
							cellM.setCellValue(record1.getR88_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR88_c13() != null) {
							cellN.setCellValue(record1.getR88_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR88_c14() != null) {
							cellO.setCellValue(record1.getR88_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR88_c15() != null) {
							cellP.setCellValue(record1.getR88_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR88_c16() != null) {
							cellQ.setCellValue(record1.getR88_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R89 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(88);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR89_botswana() != null) {
							cellB.setCellValue(record1.getR89_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR89_south_africa() != null) {
							cellC.setCellValue(record1.getR89_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR89_sadc() != null) {
							cellD.setCellValue(record1.getR89_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR89_usa() != null) {
							cellE.setCellValue(record1.getR89_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR89_uk() != null) {
							cellF.setCellValue(record1.getR89_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR89_europe() != null) {
							cellG.setCellValue(record1.getR89_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR89_india() != null) {
							cellH.setCellValue(record1.getR89_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR89_sydney() != null) {
							cellI.setCellValue(record1.getR89_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR89_uganda() != null) {
							cellJ.setCellValue(record1.getR89_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR89_c10() != null) {
							cellK.setCellValue(record1.getR89_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR89_c11() != null) {
							cellL.setCellValue(record1.getR89_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR89_c12() != null) {
							cellM.setCellValue(record1.getR89_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR89_c13() != null) {
							cellN.setCellValue(record1.getR89_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR89_c14() != null) {
							cellO.setCellValue(record1.getR89_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR89_c15() != null) {
							cellP.setCellValue(record1.getR89_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR89_c16() != null) {
							cellQ.setCellValue(record1.getR89_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R90 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(89);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR90_botswana() != null) {
							cellB.setCellValue(record1.getR90_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR90_south_africa() != null) {
							cellC.setCellValue(record1.getR90_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR90_sadc() != null) {
							cellD.setCellValue(record1.getR90_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR90_usa() != null) {
							cellE.setCellValue(record1.getR90_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR90_uk() != null) {
							cellF.setCellValue(record1.getR90_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR90_europe() != null) {
							cellG.setCellValue(record1.getR90_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR90_india() != null) {
							cellH.setCellValue(record1.getR90_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR90_sydney() != null) {
							cellI.setCellValue(record1.getR90_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR90_uganda() != null) {
							cellJ.setCellValue(record1.getR90_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR90_c10() != null) {
							cellK.setCellValue(record1.getR90_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR90_c11() != null) {
							cellL.setCellValue(record1.getR90_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR90_c12() != null) {
							cellM.setCellValue(record1.getR90_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR90_c13() != null) {
							cellN.setCellValue(record1.getR90_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR90_c14() != null) {
							cellO.setCellValue(record1.getR90_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR90_c15() != null) {
							cellP.setCellValue(record1.getR90_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR90_c16() != null) {
							cellQ.setCellValue(record1.getR90_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R91 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(90);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR91_botswana() != null) {
							cellB.setCellValue(record1.getR91_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR91_south_africa() != null) {
							cellC.setCellValue(record1.getR91_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR91_sadc() != null) {
							cellD.setCellValue(record1.getR91_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR91_usa() != null) {
							cellE.setCellValue(record1.getR91_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR91_uk() != null) {
							cellF.setCellValue(record1.getR91_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR91_europe() != null) {
							cellG.setCellValue(record1.getR91_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR91_india() != null) {
							cellH.setCellValue(record1.getR91_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR91_sydney() != null) {
							cellI.setCellValue(record1.getR91_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR91_uganda() != null) {
							cellJ.setCellValue(record1.getR91_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR91_c10() != null) {
							cellK.setCellValue(record1.getR91_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR91_c11() != null) {
							cellL.setCellValue(record1.getR91_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR91_c12() != null) {
							cellM.setCellValue(record1.getR91_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR91_c13() != null) {
							cellN.setCellValue(record1.getR91_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR91_c14() != null) {
							cellO.setCellValue(record1.getR91_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR91_c15() != null) {
							cellP.setCellValue(record1.getR91_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR91_c16() != null) {
							cellQ.setCellValue(record1.getR91_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R92 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(91);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR92_botswana() != null) {
							cellB.setCellValue(record1.getR92_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR92_south_africa() != null) {
							cellC.setCellValue(record1.getR92_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR92_sadc() != null) {
							cellD.setCellValue(record1.getR92_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR92_usa() != null) {
							cellE.setCellValue(record1.getR92_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR92_uk() != null) {
							cellF.setCellValue(record1.getR92_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR92_europe() != null) {
							cellG.setCellValue(record1.getR92_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR92_india() != null) {
							cellH.setCellValue(record1.getR92_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR92_sydney() != null) {
							cellI.setCellValue(record1.getR92_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR92_uganda() != null) {
							cellJ.setCellValue(record1.getR92_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR92_c10() != null) {
							cellK.setCellValue(record1.getR92_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR92_c11() != null) {
							cellL.setCellValue(record1.getR92_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR92_c12() != null) {
							cellM.setCellValue(record1.getR92_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR92_c13() != null) {
							cellN.setCellValue(record1.getR92_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR92_c14() != null) {
							cellO.setCellValue(record1.getR92_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR92_c15() != null) {
							cellP.setCellValue(record1.getR92_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR92_c16() != null) {
							cellQ.setCellValue(record1.getR92_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R93 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(92);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR93_botswana() != null) {
							cellB.setCellValue(record1.getR93_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR93_south_africa() != null) {
							cellC.setCellValue(record1.getR93_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR93_sadc() != null) {
							cellD.setCellValue(record1.getR93_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR93_usa() != null) {
							cellE.setCellValue(record1.getR93_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR93_uk() != null) {
							cellF.setCellValue(record1.getR93_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR93_europe() != null) {
							cellG.setCellValue(record1.getR93_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR93_india() != null) {
							cellH.setCellValue(record1.getR93_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR93_sydney() != null) {
							cellI.setCellValue(record1.getR93_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR93_uganda() != null) {
							cellJ.setCellValue(record1.getR93_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR93_c10() != null) {
							cellK.setCellValue(record1.getR93_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR93_c11() != null) {
							cellL.setCellValue(record1.getR93_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR93_c12() != null) {
							cellM.setCellValue(record1.getR93_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR93_c13() != null) {
							cellN.setCellValue(record1.getR93_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR93_c14() != null) {
							cellO.setCellValue(record1.getR93_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR93_c15() != null) {
							cellP.setCellValue(record1.getR93_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR93_c16() != null) {
							cellQ.setCellValue(record1.getR93_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R94 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(93);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR94_botswana() != null) {
							cellB.setCellValue(record1.getR94_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR94_south_africa() != null) {
							cellC.setCellValue(record1.getR94_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR94_sadc() != null) {
							cellD.setCellValue(record1.getR94_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR94_usa() != null) {
							cellE.setCellValue(record1.getR94_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR94_uk() != null) {
							cellF.setCellValue(record1.getR94_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR94_europe() != null) {
							cellG.setCellValue(record1.getR94_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR94_india() != null) {
							cellH.setCellValue(record1.getR94_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR94_sydney() != null) {
							cellI.setCellValue(record1.getR94_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR94_uganda() != null) {
							cellJ.setCellValue(record1.getR94_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR94_c10() != null) {
							cellK.setCellValue(record1.getR94_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR94_c11() != null) {
							cellL.setCellValue(record1.getR94_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR94_c12() != null) {
							cellM.setCellValue(record1.getR94_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR94_c13() != null) {
							cellN.setCellValue(record1.getR94_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR94_c14() != null) {
							cellO.setCellValue(record1.getR94_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR94_c15() != null) {
							cellP.setCellValue(record1.getR94_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR94_c16() != null) {
							cellQ.setCellValue(record1.getR94_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R95 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(94);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR95_botswana() != null) {
							cellB.setCellValue(record1.getR95_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR95_south_africa() != null) {
							cellC.setCellValue(record1.getR95_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR95_sadc() != null) {
							cellD.setCellValue(record1.getR95_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR95_usa() != null) {
							cellE.setCellValue(record1.getR95_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR95_uk() != null) {
							cellF.setCellValue(record1.getR95_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR95_europe() != null) {
							cellG.setCellValue(record1.getR95_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR95_india() != null) {
							cellH.setCellValue(record1.getR95_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR95_sydney() != null) {
							cellI.setCellValue(record1.getR95_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR95_uganda() != null) {
							cellJ.setCellValue(record1.getR95_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR95_c10() != null) {
							cellK.setCellValue(record1.getR95_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR95_c11() != null) {
							cellL.setCellValue(record1.getR95_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR95_c12() != null) {
							cellM.setCellValue(record1.getR95_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR95_c13() != null) {
							cellN.setCellValue(record1.getR95_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR95_c14() != null) {
							cellO.setCellValue(record1.getR95_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR95_c15() != null) {
							cellP.setCellValue(record1.getR95_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR95_c16() != null) {
							cellQ.setCellValue(record1.getR95_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R96 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(95);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR96_botswana() != null) {
							cellB.setCellValue(record1.getR96_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR96_south_africa() != null) {
							cellC.setCellValue(record1.getR96_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR96_sadc() != null) {
							cellD.setCellValue(record1.getR96_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR96_usa() != null) {
							cellE.setCellValue(record1.getR96_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR96_uk() != null) {
							cellF.setCellValue(record1.getR96_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR96_europe() != null) {
							cellG.setCellValue(record1.getR96_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR96_india() != null) {
							cellH.setCellValue(record1.getR96_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR96_sydney() != null) {
							cellI.setCellValue(record1.getR96_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR96_uganda() != null) {
							cellJ.setCellValue(record1.getR96_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR96_c10() != null) {
							cellK.setCellValue(record1.getR96_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR96_c11() != null) {
							cellL.setCellValue(record1.getR96_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR96_c12() != null) {
							cellM.setCellValue(record1.getR96_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR96_c13() != null) {
							cellN.setCellValue(record1.getR96_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR96_c14() != null) {
							cellO.setCellValue(record1.getR96_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR96_c15() != null) {
							cellP.setCellValue(record1.getR96_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR96_c16() != null) {
							cellQ.setCellValue(record1.getR96_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R97 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(96);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR97_botswana() != null) {
							cellB.setCellValue(record1.getR97_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR97_south_africa() != null) {
							cellC.setCellValue(record1.getR97_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR97_sadc() != null) {
							cellD.setCellValue(record1.getR97_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR97_usa() != null) {
							cellE.setCellValue(record1.getR97_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR97_uk() != null) {
							cellF.setCellValue(record1.getR97_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR97_europe() != null) {
							cellG.setCellValue(record1.getR97_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR97_india() != null) {
							cellH.setCellValue(record1.getR97_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR97_sydney() != null) {
							cellI.setCellValue(record1.getR97_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR97_uganda() != null) {
							cellJ.setCellValue(record1.getR97_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR97_c10() != null) {
							cellK.setCellValue(record1.getR97_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR97_c11() != null) {
							cellL.setCellValue(record1.getR97_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR97_c12() != null) {
							cellM.setCellValue(record1.getR97_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR97_c13() != null) {
							cellN.setCellValue(record1.getR97_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR97_c14() != null) {
							cellO.setCellValue(record1.getR97_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR97_c15() != null) {
							cellP.setCellValue(record1.getR97_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR97_c16() != null) {
							cellQ.setCellValue(record1.getR97_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R98 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(97);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR98_botswana() != null) {
							cellB.setCellValue(record1.getR98_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR98_south_africa() != null) {
							cellC.setCellValue(record1.getR98_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR98_sadc() != null) {
							cellD.setCellValue(record1.getR98_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR98_usa() != null) {
							cellE.setCellValue(record1.getR98_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR98_uk() != null) {
							cellF.setCellValue(record1.getR98_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR98_europe() != null) {
							cellG.setCellValue(record1.getR98_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR98_india() != null) {
							cellH.setCellValue(record1.getR98_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR98_sydney() != null) {
							cellI.setCellValue(record1.getR98_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR98_uganda() != null) {
							cellJ.setCellValue(record1.getR98_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR98_c10() != null) {
							cellK.setCellValue(record1.getR98_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR98_c11() != null) {
							cellL.setCellValue(record1.getR98_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR98_c12() != null) {
							cellM.setCellValue(record1.getR98_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR98_c13() != null) {
							cellN.setCellValue(record1.getR98_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR98_c14() != null) {
							cellO.setCellValue(record1.getR98_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR98_c15() != null) {
							cellP.setCellValue(record1.getR98_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR98_c16() != null) {
							cellQ.setCellValue(record1.getR98_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R99 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(98);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR99_botswana() != null) {
							cellB.setCellValue(record1.getR99_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR99_south_africa() != null) {
							cellC.setCellValue(record1.getR99_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR99_sadc() != null) {
							cellD.setCellValue(record1.getR99_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR99_usa() != null) {
							cellE.setCellValue(record1.getR99_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR99_uk() != null) {
							cellF.setCellValue(record1.getR99_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR99_europe() != null) {
							cellG.setCellValue(record1.getR99_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR99_india() != null) {
							cellH.setCellValue(record1.getR99_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR99_sydney() != null) {
							cellI.setCellValue(record1.getR99_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR99_uganda() != null) {
							cellJ.setCellValue(record1.getR99_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR99_c10() != null) {
							cellK.setCellValue(record1.getR99_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR99_c11() != null) {
							cellL.setCellValue(record1.getR99_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR99_c12() != null) {
							cellM.setCellValue(record1.getR99_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR99_c13() != null) {
							cellN.setCellValue(record1.getR99_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR99_c14() != null) {
							cellO.setCellValue(record1.getR99_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR99_c15() != null) {
							cellP.setCellValue(record1.getR99_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR99_c16() != null) {
							cellQ.setCellValue(record1.getR99_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R100 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(99);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR100_botswana() != null) {
							cellB.setCellValue(record1.getR100_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR100_south_africa() != null) {
							cellC.setCellValue(record1.getR100_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR100_sadc() != null) {
							cellD.setCellValue(record1.getR100_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR100_usa() != null) {
							cellE.setCellValue(record1.getR100_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR100_uk() != null) {
							cellF.setCellValue(record1.getR100_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR100_europe() != null) {
							cellG.setCellValue(record1.getR100_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR100_india() != null) {
							cellH.setCellValue(record1.getR100_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR100_sydney() != null) {
							cellI.setCellValue(record1.getR100_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR100_uganda() != null) {
							cellJ.setCellValue(record1.getR100_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR100_c10() != null) {
							cellK.setCellValue(record1.getR100_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR100_c11() != null) {
							cellL.setCellValue(record1.getR100_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR100_c12() != null) {
							cellM.setCellValue(record1.getR100_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR100_c13() != null) {
							cellN.setCellValue(record1.getR100_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR100_c14() != null) {
							cellO.setCellValue(record1.getR100_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR100_c15() != null) {
							cellP.setCellValue(record1.getR100_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR100_c16() != null) {
							cellQ.setCellValue(record1.getR100_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R101 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(100);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR101_botswana() != null) {
							cellB.setCellValue(record1.getR101_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR101_south_africa() != null) {
							cellC.setCellValue(record1.getR101_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR101_sadc() != null) {
							cellD.setCellValue(record1.getR101_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR101_usa() != null) {
							cellE.setCellValue(record1.getR101_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR101_uk() != null) {
							cellF.setCellValue(record1.getR101_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR101_europe() != null) {
							cellG.setCellValue(record1.getR101_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR101_india() != null) {
							cellH.setCellValue(record1.getR101_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR101_sydney() != null) {
							cellI.setCellValue(record1.getR101_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR101_uganda() != null) {
							cellJ.setCellValue(record1.getR101_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR101_c10() != null) {
							cellK.setCellValue(record1.getR101_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR101_c11() != null) {
							cellL.setCellValue(record1.getR101_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR101_c12() != null) {
							cellM.setCellValue(record1.getR101_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR101_c13() != null) {
							cellN.setCellValue(record1.getR101_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR101_c14() != null) {
							cellO.setCellValue(record1.getR101_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR101_c15() != null) {
							cellP.setCellValue(record1.getR101_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR101_c16() != null) {
							cellQ.setCellValue(record1.getR101_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R102 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(101);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR102_botswana() != null) {
							cellB.setCellValue(record1.getR102_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR102_south_africa() != null) {
							cellC.setCellValue(record1.getR102_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR102_sadc() != null) {
							cellD.setCellValue(record1.getR102_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR102_usa() != null) {
							cellE.setCellValue(record1.getR102_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR102_uk() != null) {
							cellF.setCellValue(record1.getR102_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR102_europe() != null) {
							cellG.setCellValue(record1.getR102_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR102_india() != null) {
							cellH.setCellValue(record1.getR102_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR102_sydney() != null) {
							cellI.setCellValue(record1.getR102_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR102_uganda() != null) {
							cellJ.setCellValue(record1.getR102_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR102_c10() != null) {
							cellK.setCellValue(record1.getR102_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR102_c11() != null) {
							cellL.setCellValue(record1.getR102_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR102_c12() != null) {
							cellM.setCellValue(record1.getR102_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR102_c13() != null) {
							cellN.setCellValue(record1.getR102_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR102_c14() != null) {
							cellO.setCellValue(record1.getR102_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR102_c15() != null) {
							cellP.setCellValue(record1.getR102_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR102_c16() != null) {
							cellQ.setCellValue(record1.getR102_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R103 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(102);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR103_botswana() != null) {
							cellB.setCellValue(record1.getR103_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR103_south_africa() != null) {
							cellC.setCellValue(record1.getR103_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR103_sadc() != null) {
							cellD.setCellValue(record1.getR103_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR103_usa() != null) {
							cellE.setCellValue(record1.getR103_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR103_uk() != null) {
							cellF.setCellValue(record1.getR103_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR103_europe() != null) {
							cellG.setCellValue(record1.getR103_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR103_india() != null) {
							cellH.setCellValue(record1.getR103_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR103_sydney() != null) {
							cellI.setCellValue(record1.getR103_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR103_uganda() != null) {
							cellJ.setCellValue(record1.getR103_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR103_c10() != null) {
							cellK.setCellValue(record1.getR103_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR103_c11() != null) {
							cellL.setCellValue(record1.getR103_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR103_c12() != null) {
							cellM.setCellValue(record1.getR103_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR103_c13() != null) {
							cellN.setCellValue(record1.getR103_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR103_c14() != null) {
							cellO.setCellValue(record1.getR103_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR103_c15() != null) {
							cellP.setCellValue(record1.getR103_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR103_c16() != null) {
							cellQ.setCellValue(record1.getR103_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R104 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(103);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR104_botswana() != null) {
							cellB.setCellValue(record1.getR104_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR104_south_africa() != null) {
							cellC.setCellValue(record1.getR104_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR104_sadc() != null) {
							cellD.setCellValue(record1.getR104_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR104_usa() != null) {
							cellE.setCellValue(record1.getR104_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR104_uk() != null) {
							cellF.setCellValue(record1.getR104_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR104_europe() != null) {
							cellG.setCellValue(record1.getR104_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR104_india() != null) {
							cellH.setCellValue(record1.getR104_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR104_sydney() != null) {
							cellI.setCellValue(record1.getR104_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR104_uganda() != null) {
							cellJ.setCellValue(record1.getR104_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR104_c10() != null) {
							cellK.setCellValue(record1.getR104_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR104_c11() != null) {
							cellL.setCellValue(record1.getR104_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR104_c12() != null) {
							cellM.setCellValue(record1.getR104_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR104_c13() != null) {
							cellN.setCellValue(record1.getR104_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR104_c14() != null) {
							cellO.setCellValue(record1.getR104_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR104_c15() != null) {
							cellP.setCellValue(record1.getR104_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR104_c16() != null) {
							cellQ.setCellValue(record1.getR104_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R105 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(104);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR105_botswana() != null) {
							cellB.setCellValue(record1.getR105_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR105_south_africa() != null) {
							cellC.setCellValue(record1.getR105_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR105_sadc() != null) {
							cellD.setCellValue(record1.getR105_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR105_usa() != null) {
							cellE.setCellValue(record1.getR105_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR105_uk() != null) {
							cellF.setCellValue(record1.getR105_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR105_europe() != null) {
							cellG.setCellValue(record1.getR105_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR105_india() != null) {
							cellH.setCellValue(record1.getR105_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR105_sydney() != null) {
							cellI.setCellValue(record1.getR105_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR105_uganda() != null) {
							cellJ.setCellValue(record1.getR105_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR105_c10() != null) {
							cellK.setCellValue(record1.getR105_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR105_c11() != null) {
							cellL.setCellValue(record1.getR105_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR105_c12() != null) {
							cellM.setCellValue(record1.getR105_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR105_c13() != null) {
							cellN.setCellValue(record1.getR105_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR105_c14() != null) {
							cellO.setCellValue(record1.getR105_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR105_c15() != null) {
							cellP.setCellValue(record1.getR105_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR105_c16() != null) {
							cellQ.setCellValue(record1.getR105_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R106 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(105);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR106_botswana() != null) {
							cellB.setCellValue(record1.getR106_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR106_south_africa() != null) {
							cellC.setCellValue(record1.getR106_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR106_sadc() != null) {
							cellD.setCellValue(record1.getR106_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR106_usa() != null) {
							cellE.setCellValue(record1.getR106_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR106_uk() != null) {
							cellF.setCellValue(record1.getR106_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR106_europe() != null) {
							cellG.setCellValue(record1.getR106_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR106_india() != null) {
							cellH.setCellValue(record1.getR106_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR106_sydney() != null) {
							cellI.setCellValue(record1.getR106_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR106_uganda() != null) {
							cellJ.setCellValue(record1.getR106_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR106_c10() != null) {
							cellK.setCellValue(record1.getR106_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR106_c11() != null) {
							cellL.setCellValue(record1.getR106_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR106_c12() != null) {
							cellM.setCellValue(record1.getR106_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR106_c13() != null) {
							cellN.setCellValue(record1.getR106_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR106_c14() != null) {
							cellO.setCellValue(record1.getR106_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR106_c15() != null) {
							cellP.setCellValue(record1.getR106_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR106_c16() != null) {
							cellQ.setCellValue(record1.getR106_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R107 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(106);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR107_botswana() != null) {
							cellB.setCellValue(record1.getR107_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR107_south_africa() != null) {
							cellC.setCellValue(record1.getR107_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR107_sadc() != null) {
							cellD.setCellValue(record1.getR107_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR107_usa() != null) {
							cellE.setCellValue(record1.getR107_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR107_uk() != null) {
							cellF.setCellValue(record1.getR107_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR107_europe() != null) {
							cellG.setCellValue(record1.getR107_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR107_india() != null) {
							cellH.setCellValue(record1.getR107_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR107_sydney() != null) {
							cellI.setCellValue(record1.getR107_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR107_uganda() != null) {
							cellJ.setCellValue(record1.getR107_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR107_c10() != null) {
							cellK.setCellValue(record1.getR107_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR107_c11() != null) {
							cellL.setCellValue(record1.getR107_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR107_c12() != null) {
							cellM.setCellValue(record1.getR107_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR107_c13() != null) {
							cellN.setCellValue(record1.getR107_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR107_c14() != null) {
							cellO.setCellValue(record1.getR107_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR107_c15() != null) {
							cellP.setCellValue(record1.getR107_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR107_c16() != null) {
							cellQ.setCellValue(record1.getR107_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R108 --> USING RECORD1 CALCULATE
						// -------------------------------------------------------

						// -------------------------------------------------------
						// ROW R109 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(108);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR109_botswana() != null) {
							cellB.setCellValue(record1.getR109_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR109_south_africa() != null) {
							cellC.setCellValue(record1.getR109_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR109_sadc() != null) {
							cellD.setCellValue(record1.getR109_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR109_usa() != null) {
							cellE.setCellValue(record1.getR109_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR109_uk() != null) {
							cellF.setCellValue(record1.getR109_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR109_europe() != null) {
							cellG.setCellValue(record1.getR109_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR109_india() != null) {
							cellH.setCellValue(record1.getR109_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR109_sydney() != null) {
							cellI.setCellValue(record1.getR109_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR109_uganda() != null) {
							cellJ.setCellValue(record1.getR109_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR109_c10() != null) {
							cellK.setCellValue(record1.getR109_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR109_c11() != null) {
							cellL.setCellValue(record1.getR109_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR109_c12() != null) {
							cellM.setCellValue(record1.getR109_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR109_c13() != null) {
							cellN.setCellValue(record1.getR109_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR109_c14() != null) {
							cellO.setCellValue(record1.getR109_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR109_c15() != null) {
							cellP.setCellValue(record1.getR109_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR109_c16() != null) {
							cellQ.setCellValue(record1.getR109_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R110 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(109);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record1.getR110_botswana() != null) {
							cellB.setCellValue(record1.getR110_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR110_south_africa() != null) {
							cellC.setCellValue(record1.getR110_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR110_sadc() != null) {
							cellD.setCellValue(record1.getR110_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR110_usa() != null) {
							cellE.setCellValue(record1.getR110_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR110_uk() != null) {
							cellF.setCellValue(record1.getR110_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR110_europe() != null) {
							cellG.setCellValue(record1.getR110_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR110_india() != null) {
							cellH.setCellValue(record1.getR110_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR110_sydney() != null) {
							cellI.setCellValue(record1.getR110_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR110_uganda() != null) {
							cellJ.setCellValue(record1.getR110_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR110_c10() != null) {
							cellK.setCellValue(record1.getR110_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR110_c11() != null) {
							cellL.setCellValue(record1.getR110_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR110_c12() != null) {
							cellM.setCellValue(record1.getR110_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR110_c13() != null) {
							cellN.setCellValue(record1.getR110_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR110_c14() != null) {
							cellO.setCellValue(record1.getR110_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR110_c15() != null) {
							cellP.setCellValue(record1.getR110_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR110_c16() != null) {
							cellQ.setCellValue(record1.getR110_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R111 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(110);

						/*
						 * // Column B - BOTSWANA cellB = row.createCell(1); if
						 * (record2.getR111_botswana() != null) {
						 * cellB.setCellValue(record2.getR111_botswana().doubleValue());
						 * cellB.setCellStyle(numberStyle); } else { cellB.setCellValue("");
						 * cellB.setCellStyle(textStyle); }
						 * 
						 */
						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR111_south_africa() != null) {
							cellC.setCellValue(record1.getR111_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR111_sadc() != null) {
							cellD.setCellValue(record1.getR111_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR111_usa() != null) {
							cellE.setCellValue(record1.getR111_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR111_uk() != null) {
							cellF.setCellValue(record1.getR111_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR111_europe() != null) {
							cellG.setCellValue(record1.getR111_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR111_india() != null) {
							cellH.setCellValue(record1.getR111_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR111_sydney() != null) {
							cellI.setCellValue(record1.getR111_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR111_uganda() != null) {
							cellJ.setCellValue(record1.getR111_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR111_c10() != null) {
							cellK.setCellValue(record1.getR111_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR111_c11() != null) {
							cellL.setCellValue(record1.getR111_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR111_c12() != null) {
							cellM.setCellValue(record1.getR111_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR111_c13() != null) {
							cellN.setCellValue(record1.getR111_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR111_c14() != null) {
							cellO.setCellValue(record1.getR111_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR111_c15() != null) {
							cellP.setCellValue(record1.getR111_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR111_c16() != null) {
							cellQ.setCellValue(record1.getR111_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R112 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(111);

						/*
						 * // Column B - BOTSWANA cellB = row.createCell(1); if
						 * (record2.getR112_botswana() != null) {
						 * cellB.setCellValue(record2.getR112_botswana().doubleValue());
						 * cellB.setCellStyle(numberStyle); } else { cellB.setCellValue("");
						 * cellB.setCellStyle(textStyle); }
						 */

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR112_south_africa() != null) {
							cellC.setCellValue(record1.getR112_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR112_sadc() != null) {
							cellD.setCellValue(record1.getR112_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR112_usa() != null) {
							cellE.setCellValue(record1.getR112_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR112_uk() != null) {
							cellF.setCellValue(record1.getR112_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR112_europe() != null) {
							cellG.setCellValue(record1.getR112_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR112_india() != null) {
							cellH.setCellValue(record1.getR112_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR112_sydney() != null) {
							cellI.setCellValue(record1.getR112_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR112_uganda() != null) {
							cellJ.setCellValue(record1.getR112_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR112_c10() != null) {
							cellK.setCellValue(record1.getR112_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR112_c11() != null) {
							cellL.setCellValue(record1.getR112_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR112_c12() != null) {
							cellM.setCellValue(record1.getR112_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR112_c13() != null) {
							cellN.setCellValue(record1.getR112_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR112_c14() != null) {
							cellO.setCellValue(record1.getR112_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR112_c15() != null) {
							cellP.setCellValue(record1.getR112_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR112_c16() != null) {
							cellQ.setCellValue(record1.getR112_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R113 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(112);

						/*
						 * // Column B - BOTSWANA cellB = row.createCell(1); if
						 * (record2.getR113_botswana() != null) {
						 * cellB.setCellValue(record2.getR113_botswana().doubleValue());
						 * cellB.setCellStyle(numberStyle); } else { cellB.setCellValue("");
						 * cellB.setCellStyle(textStyle); }
						 */

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR113_south_africa() != null) {
							cellC.setCellValue(record1.getR113_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR113_sadc() != null) {
							cellD.setCellValue(record1.getR113_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR113_usa() != null) {
							cellE.setCellValue(record1.getR113_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR113_uk() != null) {
							cellF.setCellValue(record1.getR113_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR113_europe() != null) {
							cellG.setCellValue(record1.getR113_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR113_india() != null) {
							cellH.setCellValue(record1.getR113_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR113_sydney() != null) {
							cellI.setCellValue(record1.getR113_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR113_uganda() != null) {
							cellJ.setCellValue(record1.getR113_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR113_c10() != null) {
							cellK.setCellValue(record1.getR113_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR113_c11() != null) {
							cellL.setCellValue(record1.getR113_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR113_c12() != null) {
							cellM.setCellValue(record1.getR113_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR113_c13() != null) {
							cellN.setCellValue(record1.getR113_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR113_c14() != null) {
							cellO.setCellValue(record1.getR113_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR113_c15() != null) {
							cellP.setCellValue(record1.getR113_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR113_c16() != null) {
							cellQ.setCellValue(record1.getR113_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R114 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(113);

						/*
						 * // Column B - BOTSWANA cellB = row.createCell(1); if
						 * (record2.getR114_botswana() != null) {
						 * cellB.setCellValue(record2.getR114_botswana().doubleValue());
						 * cellB.setCellStyle(numberStyle); } else { cellB.setCellValue("");
						 * cellB.setCellStyle(textStyle); }
						 */

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record1.getR114_south_africa() != null) {
							cellC.setCellValue(record1.getR114_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record1.getR114_sadc() != null) {
							cellD.setCellValue(record1.getR114_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record1.getR114_usa() != null) {
							cellE.setCellValue(record1.getR114_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record1.getR114_uk() != null) {
							cellF.setCellValue(record1.getR114_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record1.getR114_europe() != null) {
							cellG.setCellValue(record1.getR114_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record1.getR114_india() != null) {
							cellH.setCellValue(record1.getR114_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record1.getR114_sydney() != null) {
							cellI.setCellValue(record1.getR114_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record1.getR114_uganda() != null) {
							cellJ.setCellValue(record1.getR114_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record1.getR114_c10() != null) {
							cellK.setCellValue(record1.getR114_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record1.getR114_c11() != null) {
							cellL.setCellValue(record1.getR114_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record1.getR114_c12() != null) {
							cellM.setCellValue(record1.getR114_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record1.getR114_c13() != null) {
							cellN.setCellValue(record1.getR114_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record1.getR114_c14() != null) {
							cellO.setCellValue(record1.getR114_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record1.getR114_c15() != null) {
							cellP.setCellValue(record1.getR114_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record1.getR114_c16() != null) {
							cellQ.setCellValue(record1.getR114_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

					}

					private void populateEntity3Data(Sheet sheet, M_GALOR_Manual_Archival_Summary_Entity record2, CellStyle textStyle,
							CellStyle numberStyle) {

						// R22 - ROW22 (Index 21)
						Row row = sheet.getRow(21) != null ? sheet.getRow(21) : sheet.createRow(21);

						Cell cellB, cellC, cellD, cellE, cellF, cellG, cellH, cellI, cellJ, cellK, cellL, cellM, cellN, cellO, cellP,
								cellQ;

						// -------------------------------------------------------
						// ROW R22
						// -------------------------------------------------------

						row = sheet.getRow(21);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record2.getR22_botswana() != null) {
							cellB.setCellValue(record2.getR22_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record2.getR22_south_africa() != null) {
							cellC.setCellValue(record2.getR22_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record2.getR22_sadc() != null) {
							cellD.setCellValue(record2.getR22_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record2.getR22_usa() != null) {
							cellE.setCellValue(record2.getR22_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record2.getR22_uk() != null) {
							cellF.setCellValue(record2.getR22_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record2.getR22_europe() != null) {
							cellG.setCellValue(record2.getR22_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record2.getR22_india() != null) {
							cellH.setCellValue(record2.getR22_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record2.getR22_sydney() != null) {
							cellI.setCellValue(record2.getR22_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record2.getR22_uganda() != null) {
							cellJ.setCellValue(record2.getR22_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record2.getR22_c10() != null) {
							cellK.setCellValue(record2.getR22_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record2.getR22_c11() != null) {
							cellL.setCellValue(record2.getR22_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record2.getR22_c12() != null) {
							cellM.setCellValue(record2.getR22_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record2.getR22_c13() != null) {
							cellN.setCellValue(record2.getR22_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record2.getR22_c14() != null) {
							cellO.setCellValue(record2.getR22_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record2.getR22_c15() != null) {
							cellP.setCellValue(record2.getR22_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record2.getR22_c16() != null) {
							cellQ.setCellValue(record2.getR22_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R23
						// -------------------------------------------------------

						row = sheet.getRow(22);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record2.getR23_botswana() != null) {
							cellB.setCellValue(record2.getR23_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record2.getR23_south_africa() != null) {
							cellC.setCellValue(record2.getR23_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record2.getR23_sadc() != null) {
							cellD.setCellValue(record2.getR23_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record2.getR23_usa() != null) {
							cellE.setCellValue(record2.getR23_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record2.getR23_uk() != null) {
							cellF.setCellValue(record2.getR23_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record2.getR23_europe() != null) {
							cellG.setCellValue(record2.getR23_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record2.getR23_india() != null) {
							cellH.setCellValue(record2.getR23_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record2.getR23_sydney() != null) {
							cellI.setCellValue(record2.getR23_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record2.getR23_uganda() != null) {
							cellJ.setCellValue(record2.getR23_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record2.getR23_c10() != null) {
							cellK.setCellValue(record2.getR23_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record2.getR23_c11() != null) {
							cellL.setCellValue(record2.getR23_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record2.getR23_c12() != null) {
							cellM.setCellValue(record2.getR23_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record2.getR23_c13() != null) {
							cellN.setCellValue(record2.getR23_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record2.getR23_c14() != null) {
							cellO.setCellValue(record2.getR23_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record2.getR23_c15() != null) {
							cellP.setCellValue(record2.getR23_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record2.getR23_c16() != null) {
							cellQ.setCellValue(record2.getR23_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R57 --> USING RECORD2
						// -------------------------------------------------------

						row = sheet.getRow(56);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record2.getR57_botswana() != null) {
							cellB.setCellValue(record2.getR57_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record2.getR57_south_africa() != null) {
							cellC.setCellValue(record2.getR57_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record2.getR57_sadc() != null) {
							cellD.setCellValue(record2.getR57_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record2.getR57_usa() != null) {
							cellE.setCellValue(record2.getR57_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record2.getR57_uk() != null) {
							cellF.setCellValue(record2.getR57_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record2.getR57_europe() != null) {
							cellG.setCellValue(record2.getR57_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record2.getR57_india() != null) {
							cellH.setCellValue(record2.getR57_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record2.getR57_sydney() != null) {
							cellI.setCellValue(record2.getR57_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record2.getR57_uganda() != null) {
							cellJ.setCellValue(record2.getR57_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record2.getR57_c10() != null) {
							cellK.setCellValue(record2.getR57_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record2.getR57_c11() != null) {
							cellL.setCellValue(record2.getR57_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record2.getR57_c12() != null) {
							cellM.setCellValue(record2.getR57_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record2.getR57_c13() != null) {
							cellN.setCellValue(record2.getR57_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record2.getR57_c14() != null) {
							cellO.setCellValue(record2.getR57_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record2.getR57_c15() != null) {
							cellP.setCellValue(record2.getR57_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record2.getR57_c16() != null) {
							cellQ.setCellValue(record2.getR57_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R58 --> USING RECORD2
						// -------------------------------------------------------

						row = sheet.getRow(57);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record2.getR58_botswana() != null) {
							cellB.setCellValue(record2.getR58_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record2.getR58_south_africa() != null) {
							cellC.setCellValue(record2.getR58_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record2.getR58_sadc() != null) {
							cellD.setCellValue(record2.getR58_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record2.getR58_usa() != null) {
							cellE.setCellValue(record2.getR58_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record2.getR58_uk() != null) {
							cellF.setCellValue(record2.getR58_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record2.getR58_europe() != null) {
							cellG.setCellValue(record2.getR58_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record2.getR58_india() != null) {
							cellH.setCellValue(record2.getR58_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record2.getR58_sydney() != null) {
							cellI.setCellValue(record2.getR58_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record2.getR58_uganda() != null) {
							cellJ.setCellValue(record2.getR58_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record2.getR58_c10() != null) {
							cellK.setCellValue(record2.getR58_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record2.getR58_c11() != null) {
							cellL.setCellValue(record2.getR58_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record2.getR58_c12() != null) {
							cellM.setCellValue(record2.getR58_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record2.getR58_c13() != null) {
							cellN.setCellValue(record2.getR58_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record2.getR58_c14() != null) {
							cellO.setCellValue(record2.getR58_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record2.getR58_c15() != null) {
							cellP.setCellValue(record2.getR58_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record2.getR58_c16() != null) {
							cellQ.setCellValue(record2.getR58_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R60 --> USING RECORD2
						// -------------------------------------------------------

						row = sheet.getRow(59);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record2.getR60_botswana() != null) {
							cellB.setCellValue(record2.getR60_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record2.getR60_south_africa() != null) {
							cellC.setCellValue(record2.getR60_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record2.getR60_sadc() != null) {
							cellD.setCellValue(record2.getR60_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record2.getR60_usa() != null) {
							cellE.setCellValue(record2.getR60_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record2.getR60_uk() != null) {
							cellF.setCellValue(record2.getR60_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record2.getR60_europe() != null) {
							cellG.setCellValue(record2.getR60_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record2.getR60_india() != null) {
							cellH.setCellValue(record2.getR60_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record2.getR60_sydney() != null) {
							cellI.setCellValue(record2.getR60_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record2.getR60_uganda() != null) {
							cellJ.setCellValue(record2.getR60_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record2.getR60_c10() != null) {
							cellK.setCellValue(record2.getR60_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record2.getR60_c11() != null) {
							cellL.setCellValue(record2.getR60_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record2.getR60_c12() != null) {
							cellM.setCellValue(record2.getR60_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record2.getR60_c13() != null) {
							cellN.setCellValue(record2.getR60_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record2.getR60_c14() != null) {
							cellO.setCellValue(record2.getR60_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record2.getR60_c15() != null) {
							cellP.setCellValue(record2.getR60_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record2.getR60_c16() != null) {
							cellQ.setCellValue(record2.getR60_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R61 --> USING RECORD2
						// -------------------------------------------------------

						row = sheet.getRow(60);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record2.getR61_botswana() != null) {
							cellB.setCellValue(record2.getR61_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record2.getR61_south_africa() != null) {
							cellC.setCellValue(record2.getR61_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record2.getR61_sadc() != null) {
							cellD.setCellValue(record2.getR61_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record2.getR61_usa() != null) {
							cellE.setCellValue(record2.getR61_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record2.getR61_uk() != null) {
							cellF.setCellValue(record2.getR61_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record2.getR61_europe() != null) {
							cellG.setCellValue(record2.getR61_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record2.getR61_india() != null) {
							cellH.setCellValue(record2.getR61_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record2.getR61_sydney() != null) {
							cellI.setCellValue(record2.getR61_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record2.getR61_uganda() != null) {
							cellJ.setCellValue(record2.getR61_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record2.getR61_c10() != null) {
							cellK.setCellValue(record2.getR61_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record2.getR61_c11() != null) {
							cellL.setCellValue(record2.getR61_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record2.getR61_c12() != null) {
							cellM.setCellValue(record2.getR61_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record2.getR61_c13() != null) {
							cellN.setCellValue(record2.getR61_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record2.getR61_c14() != null) {
							cellO.setCellValue(record2.getR61_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record2.getR61_c15() != null) {
							cellP.setCellValue(record2.getR61_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record2.getR61_c16() != null) {
							cellQ.setCellValue(record2.getR61_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R64 --> USING RECORD2
						// -------------------------------------------------------

						row = sheet.getRow(63);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record2.getR64_botswana() != null) {
							cellB.setCellValue(record2.getR64_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record2.getR64_south_africa() != null) {
							cellC.setCellValue(record2.getR64_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record2.getR64_sadc() != null) {
							cellD.setCellValue(record2.getR64_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record2.getR64_usa() != null) {
							cellE.setCellValue(record2.getR64_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record2.getR64_uk() != null) {
							cellF.setCellValue(record2.getR64_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record2.getR64_europe() != null) {
							cellG.setCellValue(record2.getR64_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record2.getR64_india() != null) {
							cellH.setCellValue(record2.getR64_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record2.getR64_sydney() != null) {
							cellI.setCellValue(record2.getR64_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record2.getR64_uganda() != null) {
							cellJ.setCellValue(record2.getR64_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record2.getR64_c10() != null) {
							cellK.setCellValue(record2.getR64_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record2.getR64_c11() != null) {
							cellL.setCellValue(record2.getR64_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record2.getR64_c12() != null) {
							cellM.setCellValue(record2.getR64_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record2.getR64_c13() != null) {
							cellN.setCellValue(record2.getR64_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record2.getR64_c14() != null) {
							cellO.setCellValue(record2.getR64_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record2.getR64_c15() != null) {
							cellP.setCellValue(record2.getR64_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record2.getR64_c16() != null) {
							cellQ.setCellValue(record2.getR64_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R65 --> USING RECORD2
						// -------------------------------------------------------

						row = sheet.getRow(64);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record2.getR65_botswana() != null) {
							cellB.setCellValue(record2.getR65_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record2.getR65_south_africa() != null) {
							cellC.setCellValue(record2.getR65_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record2.getR65_sadc() != null) {
							cellD.setCellValue(record2.getR65_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record2.getR65_usa() != null) {
							cellE.setCellValue(record2.getR65_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record2.getR65_uk() != null) {
							cellF.setCellValue(record2.getR65_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record2.getR65_europe() != null) {
							cellG.setCellValue(record2.getR65_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record2.getR65_india() != null) {
							cellH.setCellValue(record2.getR65_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record2.getR65_sydney() != null) {
							cellI.setCellValue(record2.getR65_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record2.getR65_uganda() != null) {
							cellJ.setCellValue(record2.getR65_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record2.getR65_c10() != null) {
							cellK.setCellValue(record2.getR65_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record2.getR65_c11() != null) {
							cellL.setCellValue(record2.getR65_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record2.getR65_c12() != null) {
							cellM.setCellValue(record2.getR65_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record2.getR65_c13() != null) {
							cellN.setCellValue(record2.getR65_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record2.getR65_c14() != null) {
							cellO.setCellValue(record2.getR65_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record2.getR65_c15() != null) {
							cellP.setCellValue(record2.getR65_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record2.getR65_c16() != null) {
							cellQ.setCellValue(record2.getR65_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R67 --> USING RECORD2
						// -------------------------------------------------------

						row = sheet.getRow(66);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record2.getR67_botswana() != null) {
							cellB.setCellValue(record2.getR67_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record2.getR67_south_africa() != null) {
							cellC.setCellValue(record2.getR67_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record2.getR67_sadc() != null) {
							cellD.setCellValue(record2.getR67_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record2.getR67_usa() != null) {
							cellE.setCellValue(record2.getR67_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record2.getR67_uk() != null) {
							cellF.setCellValue(record2.getR67_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record2.getR67_europe() != null) {
							cellG.setCellValue(record2.getR67_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record2.getR67_india() != null) {
							cellH.setCellValue(record2.getR67_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record2.getR67_sydney() != null) {
							cellI.setCellValue(record2.getR67_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record2.getR67_uganda() != null) {
							cellJ.setCellValue(record2.getR67_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record2.getR67_c10() != null) {
							cellK.setCellValue(record2.getR67_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record2.getR67_c11() != null) {
							cellL.setCellValue(record2.getR67_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record2.getR67_c12() != null) {
							cellM.setCellValue(record2.getR67_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record2.getR67_c13() != null) {
							cellN.setCellValue(record2.getR67_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record2.getR67_c14() != null) {
							cellO.setCellValue(record2.getR67_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record2.getR67_c15() != null) {
							cellP.setCellValue(record2.getR67_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record2.getR67_c16() != null) {
							cellQ.setCellValue(record2.getR67_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R68 --> USING RECORD2
						// -------------------------------------------------------

						row = sheet.getRow(67);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record2.getR68_botswana() != null) {
							cellB.setCellValue(record2.getR68_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column C - SOUTH_AFRICA
						cellC = row.createCell(2);
						if (record2.getR68_south_africa() != null) {
							cellC.setCellValue(record2.getR68_south_africa().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column D - SADC
						cellD = row.createCell(3);
						if (record2.getR68_sadc() != null) {
							cellD.setCellValue(record2.getR68_sadc().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column E - USA
						cellE = row.createCell(4);
						if (record2.getR68_usa() != null) {
							cellE.setCellValue(record2.getR68_usa().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column F - UK
						cellF = row.createCell(5);
						if (record2.getR68_uk() != null) {
							cellF.setCellValue(record2.getR68_uk().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column G - EUROPE
						cellG = row.createCell(6);
						if (record2.getR68_europe() != null) {
							cellG.setCellValue(record2.getR68_europe().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column H - INDIA
						cellH = row.createCell(7);
						if (record2.getR68_india() != null) {
							cellH.setCellValue(record2.getR68_india().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// Column I - SYDNEY
						cellI = row.createCell(8);
						if (record2.getR68_sydney() != null) {
							cellI.setCellValue(record2.getR68_sydney().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// Column J - UGANDA
						cellJ = row.createCell(9);
						if (record2.getR68_uganda() != null) {
							cellJ.setCellValue(record2.getR68_uganda().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// Column K - C10
						cellK = row.createCell(10);
						if (record2.getR68_c10() != null) {
							cellK.setCellValue(record2.getR68_c10().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// Column L - C11
						cellL = row.createCell(11);
						if (record2.getR68_c11() != null) {
							cellL.setCellValue(record2.getR68_c11().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// Column M - C12
						cellM = row.createCell(12);
						if (record2.getR68_c12() != null) {
							cellM.setCellValue(record2.getR68_c12().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// Column N - C13
						cellN = row.createCell(13);
						if (record2.getR68_c13() != null) {
							cellN.setCellValue(record2.getR68_c13().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// Column O - C14
						cellO = row.createCell(14);
						if (record2.getR68_c14() != null) {
							cellO.setCellValue(record2.getR68_c14().doubleValue());
							cellO.setCellStyle(numberStyle);
						} else {
							cellO.setCellValue("");
							cellO.setCellStyle(textStyle);
						}

						// Column P - C15
						cellP = row.createCell(15);
						if (record2.getR68_c15() != null) {
							cellP.setCellValue(record2.getR68_c15().doubleValue());
							cellP.setCellStyle(numberStyle);
						} else {
							cellP.setCellValue("");
							cellP.setCellStyle(textStyle);
						}

						// Column Q - C16
						cellQ = row.createCell(16);
						if (record2.getR68_c16() != null) {
							cellQ.setCellValue(record2.getR68_c16().doubleValue());
							cellQ.setCellStyle(numberStyle);
						} else {
							cellQ.setCellValue("");
							cellQ.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R111 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(110);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record2.getR111_botswana() != null) {
							cellB.setCellValue(record2.getR111_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R112 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(111);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record2.getR112_botswana() != null) {
							cellB.setCellValue(record2.getR112_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R113 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(112);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record2.getR113_botswana() != null) {
							cellB.setCellValue(record2.getR113_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// -------------------------------------------------------
						// ROW R114 --> USING RECORD1
						// -------------------------------------------------------

						row = sheet.getRow(113);

						// Column B - BOTSWANA
						cellB = row.createCell(1);
						if (record2.getR114_botswana() != null) {
							cellB.setCellValue(record2.getR114_botswana().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

				}
			

		

	public byte[] getM_GALORDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_GALOR Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getM_GALORDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
						type, version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_GALOR_Detail");

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

			// sanction style (right aligned with 3 decimals)
			CellStyle sanctionStyle = workbook.createCellStyle();
			sanctionStyle.setAlignment(HorizontalAlignment.RIGHT);
			sanctionStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			sanctionStyle.setBorderTop(border);
			sanctionStyle.setBorderBottom(border);
			sanctionStyle.setBorderLeft(border);
			sanctionStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NUMBER", "SCHM DESC", "ACCT BALANCE IN PULA", "APPROVED LIMIT",
					"REPORT LABEL", "REPORT ADDL CRITERIA 1", "REPORT ADDL CRITERIA 2", "REPORT ADDL CRITERIA 3",
					"REPORT_DATE" };

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
			List<M_GALOR_Detail_Entity> reportData = m_galor_detail_Repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_GALOR_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLable());
					row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply border style to all cells in the row
					for (int colIndex = 0; colIndex < headers.length; colIndex++) {
						Cell cell = row.getCell(colIndex);
						if (cell != null) {
							if (colIndex == 3) { // ACCT BALANCE
								cell.setCellStyle(balanceStyle);
							} else if (colIndex == 4) { // APPROVED LIMIT
								cell.setCellStyle(sanctionStyle);
							} else {
								cell.setCellStyle(dataStyle);
							}
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_galor — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_GALOR Excel", e);
			return new byte[0];
		}
	}

	public byte[] getM_GALORDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for M_GALOR ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_GALORDetail");

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
			// ACCT BALANCE style (right aligned with 3 decimals)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			List<M_GALOR_Archival_Detail_Entity> reportData = m_galor_archival_detail_Repo
					.getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_GALOR_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLable());
					row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
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
				logger.info("No data found for M_GALOR — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_GALORExcel", e);
			return new byte[0];
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_GALOR"); // ✅ match the report name
		System.out.println("Hello");
		if (acctNo != null) {
			M_GALOR_Detail_Entity Entity = m_galor_detail_Repo.findByAcctnumber(acctNo);
			if (Entity != null && Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_GALOR"); // ✅ match the report name

		if (acctNo != null) {
			M_GALOR_Detail_Entity Entity = m_galor_detail_Repo.findByAcctnumber(acctNo);
			if (Entity != null && Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
				System.out.println(formattedDate);
			}
			mv.addObject("Data", Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("acctBalanceInpula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			M_GALOR_Detail_Entity existing = m_galor_detail_Repo.findByAcctnumber(acctNo);
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
				m_galor_detail_Repo.save(existing);
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

}
