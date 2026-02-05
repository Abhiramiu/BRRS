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
import java.util.Locale;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
/*import org.apache.poi.ss.usermodel.FillPatternType;*/
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
/*import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;*/
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;*/
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_GMIRT_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_GMIRT_Archival_SummaryM_Repo;
import com.bornfire.brrs.entities.BRRS_M_GMIRT_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_GMIRT_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_GMIRT_SummaryM_Repo;
import com.bornfire.brrs.entities.BRRS_M_GMIRT_Summary_Repo;
import com.bornfire.brrs.entities.M_EPR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_EPR_Summary_Entity;
import com.bornfire.brrs.entities.M_GMIRT_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_GMIRT_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_GMIRT_Detail_Entity;
import com.bornfire.brrs.entities.M_GMIRT_M_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_GMIRT_M_Summary_Entity;
import com.bornfire.brrs.entities.M_GMIRT_Summary_Entity;
import com.bornfire.brrs.entities.M_LIQ_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_LIQ_Archival_Manual_Summary_Entity;
import com.bornfire.brrs.entities.M_LIQ_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LIQ_Detail_Entity;
import com.bornfire.brrs.entities.M_LIQ_Manual_Summary_Entity;
import com.bornfire.brrs.entities.M_LIQ_Summary_Entity;

@Component
@Service

public class BRRS_M_GMIRT_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_GMIRT_ReportService.class);

	

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	
	  @Autowired BRRS_M_GMIRT_Detail_Repo brrs_m_gmirt_detail_repo;
	 
	@Autowired
	BRRS_M_GMIRT_SummaryM_Repo brrs_m_gmirtM_summary_repo;
	
	@Autowired
	BRRS_M_GMIRT_Summary_Repo brrs_m_gmirt_summary_repo;
	
	
	
	
	
	  @Autowired BRRS_M_GMIRT_Archival_Detail_Repo m_gmirt_Archival_Detail_Repo;
	 

	@Autowired
	BRRS_M_GMIRT_Archival_SummaryM_Repo m_gmirtM_Archival_Summary_Repo;
	
	@Autowired
	BRRS_M_GMIRT_Archival_Summary_Repo brrs_m_gmirt_Archival_summary_repo;
	
	
	
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	
	public ModelAndView getM_GMIRTView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		

		System.out.println("testing");
		System.out.println(version);

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_GMIRT_Archival_Summary_Entity> T1Master = new ArrayList<M_GMIRT_Archival_Summary_Entity>();
			List<M_GMIRT_M_Archival_Summary_Entity> T2Master = new ArrayList<M_GMIRT_M_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				

				T1Master = brrs_m_gmirt_Archival_summary_repo.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = m_gmirtM_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),
						version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		} else {

			List<M_GMIRT_Summary_Entity> T1Master = new ArrayList<M_GMIRT_Summary_Entity>();
			List<M_GMIRT_M_Summary_Entity> T2Master = new ArrayList<M_GMIRT_M_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				
				T1Master = brrs_m_gmirt_summary_repo.getdatabydateList(dateformat.parse(todate));
				T2Master = brrs_m_gmirtM_summary_repo.getdatabydateList(dateformat.parse(todate));
				System.out.println("T2Master size " + T2Master.size());
				mv.addObject("report_date", dateformat.format(d1));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		}

	

		mv.setViewName("BRRS/M_GMIRT");

		mv.addObject("displaymode", "summary");

		System.out.println("scv" + mv.getViewName());

		return mv;

	}
	 
	/*
	
	public ModelAndView getM_GMIRTcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();

		//Session hs = sessionFactory.getCurrentSession();

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
				List<M_GMIRT_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = m_gmirt_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate, version);
				} else {
					T1Dt1 = m_gmirt_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_GMIRT_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = brrs_m_gmirt_detail_repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate);
				} else {
					T1Dt1 = brrs_m_gmirt_detail_repo.getdatabydateList(parsedDate, currentPage, pageSize);
					totalPages = brrs_m_gmirt_detail_repo.getdatacount(parsedDate);
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

	

	
		mv.setViewName("BRRS/M_GMIRT");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}
	
	*/
	
	

	public byte[] getM_GMIRTExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null ) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_GMIRTARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<M_GMIRT_Summary_Entity> dataList = brrs_m_gmirt_summary_repo.getdatabydateList(dateformat.parse(todate));
		List<M_GMIRT_M_Summary_Entity> dataList1 = brrs_m_gmirtM_summary_repo
				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_GMIRT report. Returning empty result.");
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

			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_GMIRT_Summary_Entity record = dataList.get(i);
					M_GMIRT_M_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row9
					// Column E
					Cell cellE = row.createCell(4);
					if (record.getR9_pula() != null) {
						cellE.setCellValue(record.getR9_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row9
					// Column F
					Cell cellF = row.createCell(5);
					if (record.getR9_usd() != null) {
						cellF.setCellValue(record.getR9_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row9
					// Column G
					Cell cellG = row.createCell(6);
					if (record.getR9_zar() != null) {
						cellG.setCellValue(record.getR9_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row9
					// Column H
					Cell cellH = row.createCell(7);
					if (record.getR9_gbp() != null) {
						cellH.setCellValue(record.getR9_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row9
					// Column I
					Cell cellI = row.createCell(8);
					if (record.getR9_euro() != null) {
						cellI.setCellValue(record.getR9_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row9
					// Column J
					Cell cellJ = row.createCell(9);
					if (record.getR9_jpy() != null) {
						cellJ.setCellValue(record.getR9_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column K
					Cell cellK = row.createCell(10);
					if (record.getR9_rupee() != null) {
						cellK.setCellValue(record.getR9_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column L
					Cell cellL = row.createCell(11);
					if (record.getR9_renminbi() != null) {
						cellL.setCellValue(record.getR9_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}
					
					// row9
					// Column M
					Cell cellM = row.createCell(12);
					if (record.getR9_other() != null) {
						cellM.setCellValue(record.getR9_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}
					
					
					// row9
					// Column N
					Cell cellN = row.createCell(13);
					if (record1.getR9_tot_cap_req() != null) {
						cellN.setCellValue(record1.getR9_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}
				
										
					// row10
					
					// Column E
					row = sheet.getRow(9);
					 cellE = row.createCell(4);
					if (record.getR10_pula() != null) {
					    cellE.setCellValue(record.getR10_pula().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// row10
					// Column F
					 cellF = row.createCell(5);
					if (record.getR10_usd() != null) {
					    cellF.setCellValue(record.getR10_usd().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row10
					// Column G
					 cellG = row.createCell(6);
					if (record.getR10_zar() != null) {
					    cellG.setCellValue(record.getR10_zar().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row10
					// Column H
			       cellH = row.createCell(7);
					if (record.getR10_gbp() != null) {
					    cellH.setCellValue(record.getR10_gbp().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}

					// row10
					// Column I
					 cellI = row.createCell(8);
					if (record.getR10_euro() != null) {
					    cellI.setCellValue(record.getR10_euro().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row10
					// Column J
				     cellJ = row.createCell(9);
					if (record.getR10_jpy() != null) {
					    cellJ.setCellValue(record.getR10_jpy().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row10
					// Column K
					 cellK = row.createCell(10);
					if (record.getR10_rupee() != null) {
					    cellK.setCellValue(record.getR10_rupee().doubleValue());
					    cellK.setCellStyle(numberStyle);
					} else {
					    cellK.setCellValue("");
					    cellK.setCellStyle(textStyle);
					}

					// row10
					// Column L
					 cellL = row.createCell(11);
					if (record.getR10_renminbi() != null) {
					    cellL.setCellValue(record.getR10_renminbi().doubleValue());
					    cellL.setCellStyle(numberStyle);
					} else {
					    cellL.setCellValue("");
					    cellL.setCellStyle(textStyle);
					}

					// row10
					// Column M
					 cellM = row.createCell(12);
					if (record.getR10_other() != null) {
					    cellM.setCellValue(record.getR10_other().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// row10
					// Column N
					 cellN = row.createCell(13);
					if (record.getR10_tot_cap_req() != null) {
					    cellN.setCellValue(record.getR10_tot_cap_req().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}

					// row11
					// Column E
					row = sheet.getRow(10);
					cellE = row.createCell(4);
					if (record.getR11_pula() != null) {
					    cellE.setCellValue(record.getR11_pula().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cellF = row.createCell(5);
					if (record.getR11_usd() != null) {
					    cellF.setCellValue(record.getR11_usd().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cellG = row.createCell(6);
					if (record.getR11_zar() != null) {
					    cellG.setCellValue(record.getR11_zar().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cellH = row.createCell(7);
					if (record.getR11_gbp() != null) {
					    cellH.setCellValue(record.getR11_gbp().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}

					// row11
					// Column I
					cellI = row.createCell(8);
					if (record.getR11_euro() != null) {
					    cellI.setCellValue(record.getR11_euro().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row11
					// Column J
					cellJ = row.createCell(9);
					if (record.getR11_jpy() != null) {
					    cellJ.setCellValue(record.getR11_jpy().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cellK = row.createCell(10);
					if (record.getR11_rupee() != null) {
					    cellK.setCellValue(record.getR11_rupee().doubleValue());
					    cellK.setCellStyle(numberStyle);
					} else {
					    cellK.setCellValue("");
					    cellK.setCellStyle(textStyle);
					}

					// row11
					// Column L
					cellL = row.createCell(11);
					if (record.getR11_renminbi() != null) {
					    cellL.setCellValue(record.getR11_renminbi().doubleValue());
					    cellL.setCellStyle(numberStyle);
					} else {
					    cellL.setCellValue("");
					    cellL.setCellStyle(textStyle);
					}

					// row11
					// Column M
					cellM = row.createCell(12);
					if (record.getR11_other() != null) {
					    cellM.setCellValue(record.getR11_other().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// row11
					// Column N
					cellN = row.createCell(13);
					if (record.getR11_tot_cap_req() != null) {
					    cellN.setCellValue(record.getR11_tot_cap_req().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
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
	
	
	
	
	public byte[] getExcelM_GMIRTARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_GMIRT_Archival_Summary_Entity> dataList = brrs_m_gmirt_Archival_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_GMIRT_M_Archival_Summary_Entity> dataList1 = m_gmirtM_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_GMIRT report. Returning empty result.");
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

			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_GMIRT_Archival_Summary_Entity record = dataList.get(i);
					M_GMIRT_M_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row9
					// Column E
					Cell cellE = row.createCell(4);
					if (record.getR9_pula() != null) {
						cellE.setCellValue(record.getR9_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row9
					// Column F
					Cell cellF = row.createCell(5);
					if (record.getR9_usd() != null) {
						cellF.setCellValue(record.getR9_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row9
					// Column G
					Cell cellG = row.createCell(6);
					if (record.getR9_zar() != null) {
						cellG.setCellValue(record.getR9_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row9
					// Column H
					Cell cellH = row.createCell(7);
					if (record.getR9_gbp() != null) {
						cellH.setCellValue(record.getR9_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row9
					// Column I
					Cell cellI = row.createCell(8);
					if (record.getR9_euro() != null) {
						cellI.setCellValue(record.getR9_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row9
					// Column J
					Cell cellJ = row.createCell(9);
					if (record.getR9_jpy() != null) {
						cellJ.setCellValue(record.getR9_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column K
					Cell cellK = row.createCell(10);
					if (record.getR9_rupee() != null) {
						cellK.setCellValue(record.getR9_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column L
					Cell cellL = row.createCell(11);
					if (record.getR9_renminbi() != null) {
						cellL.setCellValue(record.getR9_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}
					
					// row9
					// Column M
					Cell cellM = row.createCell(12);
					if (record.getR9_other() != null) {
						cellM.setCellValue(record.getR9_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}
					
					
					// row9
					// Column N
					Cell cellN = row.createCell(13);
					if (record1.getR9_tot_cap_req() != null) {
						cellN.setCellValue(record1.getR9_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}
				
										
					// row10
					
					// Column E
					row = sheet.getRow(9);
					 cellE = row.createCell(4);
					if (record.getR10_pula() != null) {
					    cellE.setCellValue(record.getR10_pula().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// row10
					// Column F
					 cellF = row.createCell(5);
					if (record.getR10_usd() != null) {
					    cellF.setCellValue(record.getR10_usd().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row10
					// Column G
					 cellG = row.createCell(6);
					if (record.getR10_zar() != null) {
					    cellG.setCellValue(record.getR10_zar().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row10
					// Column H
			       cellH = row.createCell(7);
					if (record.getR10_gbp() != null) {
					    cellH.setCellValue(record.getR10_gbp().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}

					// row10
					// Column I
					 cellI = row.createCell(8);
					if (record.getR10_euro() != null) {
					    cellI.setCellValue(record.getR10_euro().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row10
					// Column J
				     cellJ = row.createCell(9);
					if (record.getR10_jpy() != null) {
					    cellJ.setCellValue(record.getR10_jpy().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row10
					// Column K
					 cellK = row.createCell(10);
					if (record.getR10_rupee() != null) {
					    cellK.setCellValue(record.getR10_rupee().doubleValue());
					    cellK.setCellStyle(numberStyle);
					} else {
					    cellK.setCellValue("");
					    cellK.setCellStyle(textStyle);
					}

					// row10
					// Column L
					 cellL = row.createCell(11);
					if (record.getR10_renminbi() != null) {
					    cellL.setCellValue(record.getR10_renminbi().doubleValue());
					    cellL.setCellStyle(numberStyle);
					} else {
					    cellL.setCellValue("");
					    cellL.setCellStyle(textStyle);
					}

					// row10
					// Column M
					 cellM = row.createCell(12);
					if (record.getR10_other() != null) {
					    cellM.setCellValue(record.getR10_other().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// row10
					// Column N
					 cellN = row.createCell(13);
					if (record.getR10_tot_cap_req() != null) {
					    cellN.setCellValue(record.getR10_tot_cap_req().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}

					// row11
					// Column E
					row = sheet.getRow(10);
					cellE = row.createCell(4);
					if (record.getR11_pula() != null) {
					    cellE.setCellValue(record.getR11_pula().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cellF = row.createCell(5);
					if (record.getR11_usd() != null) {
					    cellF.setCellValue(record.getR11_usd().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cellG = row.createCell(6);
					if (record.getR11_zar() != null) {
					    cellG.setCellValue(record.getR11_zar().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cellH = row.createCell(7);
					if (record.getR11_gbp() != null) {
					    cellH.setCellValue(record.getR11_gbp().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}

					// row11
					// Column I
					cellI = row.createCell(8);
					if (record.getR11_euro() != null) {
					    cellI.setCellValue(record.getR11_euro().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row11
					// Column J
					cellJ = row.createCell(9);
					if (record.getR11_jpy() != null) {
					    cellJ.setCellValue(record.getR11_jpy().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cellK = row.createCell(10);
					if (record.getR11_rupee() != null) {
					    cellK.setCellValue(record.getR11_rupee().doubleValue());
					    cellK.setCellStyle(numberStyle);
					} else {
					    cellK.setCellValue("");
					    cellK.setCellStyle(textStyle);
					}

					// row11
					// Column L
					cellL = row.createCell(11);
					if (record.getR11_renminbi() != null) {
					    cellL.setCellValue(record.getR11_renminbi().doubleValue());
					    cellL.setCellStyle(numberStyle);
					} else {
					    cellL.setCellValue("");
					    cellL.setCellStyle(textStyle);
					}

					// row11
					// Column M
					cellM = row.createCell(12);
					if (record.getR11_other() != null) {
					    cellM.setCellValue(record.getR11_other().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// row11
					// Column N
					cellN = row.createCell(13);
					if (record.getR11_tot_cap_req() != null) {
					    cellN.setCellValue(record.getR11_tot_cap_req().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
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

	
	
	public List<Object> getM_GMIRTArchival() {
		List<Object> M_GMIRTArchivallist = new ArrayList<>();
		try {
			M_GMIRTArchivallist = brrs_m_gmirt_Archival_summary_repo.getM_GMIRTarchival();
			M_GMIRTArchivallist = m_gmirtM_Archival_Summary_Repo.getM_GMIRTarchival();
			System.out.println("countser" + M_GMIRTArchivallist.size());
			System.out.println("countser" + M_GMIRTArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_GMIRTArchivallist Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_GMIRTArchivallist;
	}
	
	
	/*
	public byte[] getM_GMIRTDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_GMIRT Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getM_GMIRTDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_GMIRTDetails");

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
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			List<M_GMIRT_Detail_Entity> reportData = brrs_m_gmirt_detail_repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_GMIRT_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
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
				logger.info("No data found for M_GMIRT — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_GMIRT Excel", e);
			return new byte[0];
		}
	}
	
	
	
	
	
	public byte[] getM_GMIRTDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for M_GMIRT ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_GMIRTDetail");

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
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE",  "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			List<M_GMIRT_Archival_Detail_Entity> reportData = m_gmirt_Archival_Detail_Repo.getdatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_GMIRT_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					 row.createCell(2).setCellValue(item.getAcctName()); 

// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
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
				logger.info("No data found for M_GMIRT — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_GMIRTExcel", e);
			return new byte[0];
		}
	}
	*/
	
	
	public void updateReport(M_GMIRT_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_GMIRT_Summary_Entity existing = brrs_m_gmirt_summary_repo.findById(updatedEntity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	        // 1️⃣ Loop from R11 to R23 and copy fields

	        for (int i = 9; i <= 12; i++) {

	            String prefix = "R" + i + "_";

	            String[] fields = {"currency", "pula", "usd", "zar", "gbp", "euro", "jpy",
	                    "rupee", "renminbi", "other", "tot_cap_req"};

	            for (String field : fields) {

	                // 🚫 Skip only R9_tot_cap_req
	                if (i == 9 && field.equals("tot_cap_req")) {
	                    continue;   // skip this field only
	                }

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_GMIRT_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_GMIRT_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    continue; // skip field that does not exist
	                }
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save updated entity
	    brrs_m_gmirt_summary_repo.save(existing);
	}

	
	
	
	
	
	
	
	

}