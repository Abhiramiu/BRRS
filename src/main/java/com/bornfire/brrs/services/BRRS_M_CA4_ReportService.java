package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;


import com.bornfire.brrs.entities.BRRS_M_CA4_Archival_Summary_Repo;
import com.bornfire.brrs.entities.M_CA4_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_CA4_Summary_Repo;
import com.bornfire.brrs.entities.M_CA4_Archival_Summary_Entity;

@Component
@Service

public class BRRS_M_CA4_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA4_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;


	@Autowired
	BRRS_M_CA4_Summary_Repo brrs_m_ca4_summary_repo;
	


	@Autowired
	BRRS_M_CA4_Archival_Summary_Repo m_ca4_Archival_Summary_Repo;

	
	
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_CA4View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		
		System.out.println("testing");
		System.out.println(version);
		
		
		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_CA4_Archival_Summary_Entity> T1Master = new ArrayList<M_CA4_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();

				T1Master = m_ca4_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {

		List<M_CA4_Summary_Entity> T1Master = new ArrayList<M_CA4_Summary_Entity>();
		try {
			Date d1 = dateformat.parse(todate);
			// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

			// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
			// ", BRF1_REPORT_ENTITY.class)
			// .setParameter(1, df.parse(todate)).getResultList();
			T1Master = brrs_m_ca4_summary_repo.getdatabydateList(dateformat.parse(todate));
			mv.addObject("report_date", dateformat.format(d1));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_CA4");

		
		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	/*
	 * public ModelAndView getBRRS_M_CA4currentDtl(String reportId, String fromdate,
	 * String todate, String currency, String dtltype, Pageable pageable, String
	 * filter, String type, String version) {
	 * 
	 * int pageSize = pageable.getPageSize(); int currentPage =
	 * pageable.getPageNumber(); int totalPages=0;
	 * 
	 * ModelAndView mv = new ModelAndView();
	 * 
	 * Session hs = sessionFactory.getCurrentSession();
	 * 
	 * 
	 * try { Date parsedDate = null;
	 * 
	 * if (todate != null && !todate.isEmpty()) { parsedDate =
	 * dateformat.parse(todate); }
	 * 
	 * String rowId = null; String columnId = null;
	 * 
	 * // ✅ Split the filter string here if (filter != null && filter.contains(","))
	 * { String[] parts = filter.split(","); if (parts.length >= 2) { rowId =
	 * parts[0]; columnId = parts[1]; } }
	 * 
	 * System.out.println(type); if ("ARCHIVAL".equals(type) && version != null) {
	 * System.out.println(type); // 🔹 Archival branch
	 * List<M_CA4_Archival_Detail_Entity> T1Dt1; if (rowId != null && columnId !=
	 * null) { T1Dt1 = m_ca4_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId,
	 * columnId, parsedDate, version); } else { T1Dt1 =
	 * m_ca4_Archival_Detail_Repo.getdatabydateList(parsedDate, version); }
	 * 
	 * mv.addObject("reportdetails", T1Dt1); mv.addObject("reportmaster12", T1Dt1);
	 * System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
	 * 
	 * } else { // 🔹 Current branch List<M_CA4_Detail_Entity > T1Dt1; if (rowId !=
	 * null && columnId != null) { T1Dt1 =
	 * brrs_m_ca4_detail_repo.GetDataByRowIdAndColumnId(rowId, columnId,
	 * parsedDate); } else { T1Dt1 =
	 * brrs_m_ca4_detail_repo.getdatabydateList(parsedDate, currentPage, pageSize);
	 * totalPages=brrs_m_ca4_detail_repo.getdatacount(parsedDate);
	 * mv.addObject("pagination","YES"); } mv.addObject("reportdetails", T1Dt1);
	 * mv.addObject("reportmaster12", T1Dt1); System.out.println("LISTCOUNT: " +
	 * (T1Dt1 != null ? T1Dt1.size() : 0)); } } catch (ParseException e) {
	 * e.printStackTrace(); mv.addObject("errorMessage", "Invalid date format: " +
	 * todate); } catch (Exception e) { e.printStackTrace();
	 * mv.addObject("errorMessage", "Unexpected error: " + e.getMessage()); }
	 * 
	 * 
	 * 
	 * // Page<Object> T1Dt1Page = new PageImpl<Object>(pagedlist, //
	 * PageRequest.of(currentPage, pageSize), T1Dt1.size()); //
	 * mv.addObject("reportdetails", T1Dt1Page.getContent()); //
	 * mv.addObject("reportmaster1", qr); // mv.addObject("singledetail", new
	 * T1CurProdDetail());
	 * 
	 * mv.setViewName("BRRS/M_CA4"); mv.addObject("displaymode", "Details");
	 * mv.addObject("currentPage", currentPage); System.out.println("totalPages: " +
	 * (int) Math.ceil((double) totalPages / 100));
	 * mv.addObject("totalPages",(int)Math.ceil((double)totalPages / 100));
	 * mv.addObject("reportsflag", "reportsflag"); mv.addObject("menu", reportId);
	 * return mv;
	 * 
	 * }
	 */
	
	public byte[] getBRRS_M_CA4Excel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		
		
		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_CA4ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<M_CA4_Summary_Entity> dataList = brrs_m_ca4_summary_repo
				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA4 report. Returning empty result.");
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
					M_CA4_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					
// row10
					
					
					
					// Column 2 - name of sub1
					Cell cellC = row.createCell(2);
					if (record.getR10_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR10_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}
					
					// Column 3 - name of sub2
					Cell cellD = row.createCell(3);
					if (record.getR10_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR10_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// Column 4 - name of sub3
					Cell cellE = row.createCell(4);
					if (record.getR10_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR10_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}
					
					// Column 5 - name of sub4
					Cell cellF = row.createCell(5);
					if (record.getR10_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR10_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}
					
					// Column 6 - name of sub4
					Cell cellG = row.createCell(6);
					if (record.getR10_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR10_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}
					
					
					
					// row11
					row = sheet.getRow(10);

					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR11_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR11_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR11_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR11_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR11_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR11_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR11_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR11_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR11_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR11_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}
					
					
					
					// row13
					row = sheet.getRow(12);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR13_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR13_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR13_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR13_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR13_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR13_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR13_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR13_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR13_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR13_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					
					
					// row14
					row = sheet.getRow(13);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR14_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR14_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR14_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR14_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR14_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR14_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR14_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR14_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR14_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR14_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}


					
					
					// row17
					row = sheet.getRow(16);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR17_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR17_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR17_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR17_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR17_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					// row18
					row = sheet.getRow(17);

					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR18_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR18_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR18_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR18_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR18_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR18_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR18_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR18_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR18_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR18_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					


					// row19
					row = sheet.getRow(18);

					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR19_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR19_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR19_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR19_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR19_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR19_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR19_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR19_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR19_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR19_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					// row22
					row = sheet.getRow(21);
					

					// Column 3 - name of sub1
					cellC = row.createCell(2);
					if (record.getR22_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - name of sub2
					cellD = row.createCell(3);
					if (record.getR22_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - name of sub3
					cellE = row.createCell(4);
					if (record.getR22_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 6 - name of sub4
					cellF = row.createCell(5);
					if (record.getR22_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 7 - name of sub5
					cellG = row.createCell(6);
					if (record.getR22_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					


					// row23
					row = sheet.getRow(22);

					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR23_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR23_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR23_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR23_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR23_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR23_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR23_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR23_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR23_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR23_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

				

					// row24
					row = sheet.getRow(23);

					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR24_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR24_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR24_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR24_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR24_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR24_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR24_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR24_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR24_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR24_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					// row27
					row = sheet.getRow(26);
				
					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR27_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR27_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR27_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR27_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR27_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR27_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR27_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR27_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR27_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR27_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}
					
					
					// row28
					row = sheet.getRow(27);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR28_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR28_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR28_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR28_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR28_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR28_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR28_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR28_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR28_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR28_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					
					// row29
					row = sheet.getRow(28);
					// Column 1 - item
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR29_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR29_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR29_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR29_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR29_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR29_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR29_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR29_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR29_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR29_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					

					// row32
					row = sheet.getRow(31);
					

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR32_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR32_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR32_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR32_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR32_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR32_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR32_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR32_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR32_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR32_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					
					
					// row33
					row = sheet.getRow(32);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR33_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR33_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR33_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR33_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}
					
					
					// row33
					row = sheet.getRow(32);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR33_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR33_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR33_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR33_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

			

					
					// row34
					row = sheet.getRow(33);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR34_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR34_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR34_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR34_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR34_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR34_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR34_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR34_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR34_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR34_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					

					// ====================== R36 ======================
					// row36
					row = sheet.getRow(35);
				

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR36_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR36_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR36_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR36_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR36_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR36_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR36_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR36_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR36_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR36_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

				

					// ====================== R37 ======================
					row = sheet.getRow(36);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR37_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR37_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR37_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR37_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR37_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR37_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR37_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR37_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR37_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR37_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

				
					
					// ====================== R38 ======================
					row = sheet.getRow(37);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR38_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR38_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR38_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR38_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR38_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR38_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR38_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR38_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR38_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR38_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					// ====================== R39 ======================
					// row39
					row = sheet.getRow(38);
					

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR39_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR39_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR39_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR39_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR39_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR39_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR39_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR39_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR39_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR39_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					

					// ====================== R40 ======================
					row = sheet.getRow(39);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR40_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR40_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR40_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR40_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR40_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR40_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR40_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR40_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR40_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR40_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					
					// =========================
					// Row for R41
					// =========================
					row = sheet.getRow(40);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR41_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR41_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR41_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR41_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR41_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR41_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR41_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR41_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR41_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR41_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					

					// =========================
					// Row for R42
					// =========================
					row = sheet.getRow(41);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR42_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR42_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR42_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR42_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR42_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR42_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR42_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR42_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR42_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR42_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					

					// =========================
					// Row for R43
					// =========================
					row = sheet.getRow(42);
					
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR43_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR43_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR43_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR43_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR43_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

				

					// row44
					// row44
					row = sheet.getRow(43);
					if (row == null) row = sheet.createRow(43);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR44_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR44_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR44_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR44_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR44_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR44_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR44_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR44_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR44_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR44_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR44_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR44_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */


					// row45
					row = sheet.getRow(44);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR45_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR45_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR45_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR45_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR45_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR45_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR45_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR45_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR45_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR45_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					  					 


					// ================= R46 =================
					// row46
					row = sheet.getRow(45);
					

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR46_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR46_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR46_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR46_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR46_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR46_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR46_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR46_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR46_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR46_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					

					// ================= R47 =================
					// row47
					row = sheet.getRow(46);
					
					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR47_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR47_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR47_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR47_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR47_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR47_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR47_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR47_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR47_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR47_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}
					
					// ================= R48 =================
					row = sheet.getRow(47);
					
					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR48_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR48_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}
					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR48_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR48_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR48_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR48_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}
					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR48_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR48_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}
					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR48_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR48_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}
				

					// ================= R49 =================
					// row49
					row = sheet.getRow(48);
					

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR49_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR49_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR49_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR49_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR49_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR49_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR49_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR49_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR49_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR49_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}
					

					// ================= R50 =================
					// row50
					row = sheet.getRow(49);
					
					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR50_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR50_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR50_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR50_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR50_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR50_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR50_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR50_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR50_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR50_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}
					

					// ====================== R51 ======================
					row = sheet.getRow(50);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR51_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR51_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR51_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR51_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR51_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR51_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR51_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR51_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR51_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR51_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					

					// ====================== R52 ======================
					// row52
					row = sheet.getRow(51);
				
					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR52_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR52_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR52_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR52_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR52_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR52_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR52_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR52_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR52_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR52_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR52_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR52_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */


					// ====================== R53 ======================
		
					// row53
					row = sheet.getRow(52);
				

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR53_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR53_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR53_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR53_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR53_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR53_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR53_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR53_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR53_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR53_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR53_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR53_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */
					
					
					
					// row54
					row = sheet.getRow(53);
			
					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR54_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR54_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR54_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR54_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR54_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR54_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR54_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR54_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR54_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR54_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR54_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR54_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */


					// row55
					row = sheet.getRow(54);
			

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR55_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR55_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR55_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR55_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR55_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR55_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR55_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR55_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR55_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR55_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR55_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR55_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */


					// row56
					row = sheet.getRow(55);
					

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR56_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR56_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR56_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR56_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR56_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR56_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR56_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR56_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR56_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR56_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR56_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR56_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */


					// row57
					row = sheet.getRow(56);
					

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR57_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR57_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR57_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR57_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR57_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR57_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR57_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR57_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR57_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR57_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR57_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR57_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

  
					// row58
					row = sheet.getRow(57);
			

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR58_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR58_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR58_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR58_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR58_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR58_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR58_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR58_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR58_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR58_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
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
	

	/*
	 * public byte[] getBRRS_M_CA4DetailExcel(String filename, String fromdate,
	 * String todate, String currency, String dtltype, String type, String version)
	 * { try { logger.info("Generating Excel for BRRS_M_CA4 Details...");
	 * System.out.println("came to Detail download service");
	 * 
	 * if (type.equals("ARCHIVAL") & version != null) { byte[] ARCHIVALreport =
	 * getM_CA4DetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
	 * type, version); return ARCHIVALreport; }
	 * 
	 * XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("BRRS_M_CA4Details");
	 * 
	 * // Common border style BorderStyle border = BorderStyle.THIN;
	 * 
	 * // Header style (left aligned) CellStyle headerStyle =
	 * workbook.createCellStyle(); Font headerFont = workbook.createFont();
	 * headerFont.setBold(true); headerFont.setFontHeightInPoints((short) 10);
	 * headerStyle.setFont(headerFont);
	 * headerStyle.setAlignment(HorizontalAlignment.LEFT);
	 * headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	 * headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	 * headerStyle.setBorderTop(border); headerStyle.setBorderBottom(border);
	 * headerStyle.setBorderLeft(border); headerStyle.setBorderRight(border);
	 * 
	 * // Right-aligned header style for ACCT BALANCE CellStyle
	 * rightAlignedHeaderStyle = workbook.createCellStyle();
	 * rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
	 * rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
	 * 
	 * // Default data style (left aligned) CellStyle dataStyle =
	 * workbook.createCellStyle(); dataStyle.setAlignment(HorizontalAlignment.LEFT);
	 * dataStyle.setBorderTop(border); dataStyle.setBorderBottom(border);
	 * dataStyle.setBorderLeft(border); dataStyle.setBorderRight(border);
	 * 
	 * // ACCT BALANCE style (right aligned with 3 decimals) CellStyle balanceStyle
	 * = workbook.createCellStyle();
	 * balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
	 * balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
	 * balanceStyle.setBorderTop(border); balanceStyle.setBorderBottom(border);
	 * balanceStyle.setBorderLeft(border); balanceStyle.setBorderRight(border);
	 * 
	 * // Header row String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME",
	 * "ACCT BALANCE", "ROWID", "COLUMNID", "REPORT_DATE" };
	 * 
	 * XSSFRow headerRow = sheet.createRow(0); for (int i = 0; i < headers.length;
	 * i++) { Cell cell = headerRow.createCell(i); cell.setCellValue(headers[i]);
	 * 
	 * if (i == 3) { // ACCT BALANCE cell.setCellStyle(rightAlignedHeaderStyle); }
	 * else { cell.setCellStyle(headerStyle); }
	 * 
	 * sheet.setColumnWidth(i, 5000); }
	 * 
	 * // Get data Date parsedToDate = new
	 * SimpleDateFormat("dd/MM/yyyy").parse(todate); List<M_CA4_Detail_Entity>
	 * reportData = brrs_m_ca4_detail_repo.getdatabydateList(parsedToDate);
	 * 
	 * if (reportData != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (M_CA4_Detail_Entity item : reportData) { XSSFRow row =
	 * sheet.createRow(rowIndex++);
	 * 
	 * row.createCell(0).setCellValue(item.getCustId());
	 * row.createCell(1).setCellValue(item.getAcctNumber());
	 * row.createCell(2).setCellValue(item.getAcctName());
	 * 
	 * // ACCT BALANCE (right aligned, 3 decimal places) Cell balanceCell =
	 * row.createCell(3); if (item.getAcctBalanceInPula() != null) {
	 * balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue()); } else {
	 * balanceCell.setCellValue(0.000); } balanceCell.setCellStyle(balanceStyle);
	 * 
	 * row.createCell(4).setCellValue(item.getRowId());
	 * row.createCell(5).setCellValue(item.getColumnId());
	 * row.createCell(6).setCellValue( item.getReportDate() != null ? new
	 * SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : "" );
	 * 
	 * // Apply data style for all other cells for (int j = 0; j < 7; j++) { if (j
	 * != 3) { row.getCell(j).setCellStyle(dataStyle); } } } } else {
	 * logger.info("No data found for BRRS_M_CA4 — only header will be written."); }
	 * 
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * 
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray();
	 * 
	 * } catch (Exception e) { logger.error("Error generating BRRS_M_CA4 Excel", e);
	 * return new byte[0]; } }
	 */
	
	
	public List<Object> getM_CA4Archival() {
		List<Object> M_CA4Archivallist = new ArrayList<>();
		try {
			M_CA4Archivallist = m_ca4_Archival_Summary_Repo.getM_CA4archival();
			System.out.println("countser" + M_CA4Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_CA4 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_CA4Archivallist;
	}
	
	public byte[] getExcelM_CA4ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_CA4_Archival_Summary_Entity> dataList = m_ca4_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA4 report. Returning empty result.");
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
					M_CA4_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					
// row10
					
					
					
					// Column 2 - name of sub1
					Cell cellC = row.createCell(2);
					if (record.getR10_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR10_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}
					
					// Column 3 - name of sub2
					Cell cellD = row.createCell(3);
					if (record.getR10_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR10_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// Column 4 - name of sub3
					Cell cellE = row.createCell(4);
					if (record.getR10_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR10_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}
					
					// Column 5 - name of sub4
					Cell cellF = row.createCell(5);
					if (record.getR10_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR10_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}
					
					// Column 6 - name of sub4
					Cell cellG = row.createCell(6);
					if (record.getR10_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR10_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}
					
					
					
					// row11
					row = sheet.getRow(10);

					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR11_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR11_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR11_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR11_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR11_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR11_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR11_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR11_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR11_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR11_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}
					
					
					
					// row13
					row = sheet.getRow(12);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR13_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR13_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR13_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR13_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR13_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR13_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR13_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR13_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR13_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR13_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					
					
					// row14
					row = sheet.getRow(13);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR14_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR14_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR14_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR14_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR14_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR14_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR14_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR14_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR14_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR14_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}


					
					
					// row17
					row = sheet.getRow(16);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR17_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR17_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR17_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR17_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR17_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					// row18
					row = sheet.getRow(17);

					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR18_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR18_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR18_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR18_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR18_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR18_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR18_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR18_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR18_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR18_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					


					// row19
					row = sheet.getRow(18);

					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR19_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR19_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR19_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR19_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR19_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR19_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR19_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR19_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR19_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR19_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					// row22
					row = sheet.getRow(21);
					

					// Column 3 - name of sub1
					cellC = row.createCell(2);
					if (record.getR22_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - name of sub2
					cellD = row.createCell(3);
					if (record.getR22_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - name of sub3
					cellE = row.createCell(4);
					if (record.getR22_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 6 - name of sub4
					cellF = row.createCell(5);
					if (record.getR22_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 7 - name of sub5
					cellG = row.createCell(6);
					if (record.getR22_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					


					// row23
					row = sheet.getRow(22);

					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR23_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR23_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR23_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR23_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR23_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR23_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR23_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR23_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR23_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR23_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

				

					// row24
					row = sheet.getRow(23);

					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR24_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR24_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR24_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR24_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR24_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR24_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR24_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR24_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR24_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR24_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					// row27
					row = sheet.getRow(26);
				
					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR27_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR27_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR27_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR27_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR27_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR27_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR27_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR27_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR27_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR27_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}
					
					
					// row28
					row = sheet.getRow(27);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR28_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR28_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR28_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR28_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR28_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR28_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR28_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR28_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR28_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR28_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					
					// row29
					row = sheet.getRow(28);
					// Column 1 - item
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR29_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR29_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR29_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR29_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR29_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR29_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR29_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR29_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR29_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR29_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					

					// row32
					row = sheet.getRow(31);
					

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR32_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR32_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR32_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR32_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR32_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR32_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR32_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR32_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR32_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR32_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					
					
					// row33
					row = sheet.getRow(32);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR33_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR33_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR33_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR33_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}
					
					
					// row33
					row = sheet.getRow(32);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR33_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR33_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR33_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR33_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

			

					
					// row34
					row = sheet.getRow(33);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR34_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR34_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR34_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR34_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR34_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR34_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR34_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR34_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR34_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR34_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					

					// ====================== R36 ======================
					// row36
					row = sheet.getRow(35);
				

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR36_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR36_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR36_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR36_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR36_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR36_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR36_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR36_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR36_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR36_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

				

					// ====================== R37 ======================
					row = sheet.getRow(36);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR37_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR37_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR37_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR37_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR37_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR37_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR37_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR37_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR37_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR37_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

				
					
					// ====================== R38 ======================
					row = sheet.getRow(37);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR38_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR38_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR38_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR38_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR38_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR38_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR38_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR38_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR38_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR38_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					// ====================== R39 ======================
					// row39
					row = sheet.getRow(38);
					

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR39_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR39_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR39_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR39_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR39_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR39_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR39_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR39_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR39_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR39_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					

					// ====================== R40 ======================
					row = sheet.getRow(39);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR40_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR40_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR40_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR40_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR40_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR40_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR40_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR40_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR40_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR40_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					
					// =========================
					// Row for R41
					// =========================
					row = sheet.getRow(40);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR41_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR41_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR41_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR41_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR41_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR41_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR41_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR41_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR41_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR41_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					

					// =========================
					// Row for R42
					// =========================
					row = sheet.getRow(41);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR42_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR42_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR42_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR42_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR42_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR42_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR42_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR42_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR42_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR42_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					

					// =========================
					// Row for R43
					// =========================
					row = sheet.getRow(42);
					
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR43_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR43_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR43_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR43_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR43_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

				

					// row44
					// row44
					row = sheet.getRow(43);
					if (row == null) row = sheet.createRow(43);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR44_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR44_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR44_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR44_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR44_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR44_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR44_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR44_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR44_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR44_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR44_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR44_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */


					// row45
					row = sheet.getRow(44);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR45_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR45_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR45_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR45_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR45_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR45_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR45_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR45_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR45_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR45_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					
					  					 


					// ================= R46 =================
					// row46
					row = sheet.getRow(45);
					

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR46_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR46_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR46_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR46_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR46_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR46_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR46_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR46_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR46_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR46_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					

					// ================= R47 =================
					// row47
					row = sheet.getRow(46);
					
					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR47_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR47_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR47_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR47_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR47_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR47_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR47_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR47_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR47_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR47_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}
					
					// ================= R48 =================
					row = sheet.getRow(47);
					
					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR48_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR48_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}
					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR48_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR48_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR48_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR48_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}
					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR48_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR48_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}
					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR48_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR48_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}
				

					// ================= R49 =================
					// row49
					row = sheet.getRow(48);
					

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR49_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR49_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR49_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR49_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR49_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR49_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR49_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR49_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR49_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR49_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}
					

					// ================= R50 =================
					// row50
					row = sheet.getRow(49);
					
					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR50_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR50_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR50_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR50_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR50_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR50_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR50_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR50_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR50_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR50_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}
					

					// ====================== R51 ======================
					row = sheet.getRow(50);
					

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR51_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR51_amt_name_of_sub1().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR51_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR51_amt_name_of_sub2().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR51_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR51_amt_name_of_sub3().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR51_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR51_amt_name_of_sub4().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR51_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR51_amt_name_of_sub5().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					

					// ====================== R52 ======================
					// row52
					row = sheet.getRow(51);
				
					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR52_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR52_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR52_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR52_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR52_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR52_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR52_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR52_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR52_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR52_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR52_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR52_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */


					// ====================== R53 ======================
		
					// row53
					row = sheet.getRow(52);
				

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR53_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR53_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR53_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR53_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR53_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR53_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR53_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR53_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR53_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR53_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR53_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR53_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */
					
					
					
					// row54
					row = sheet.getRow(53);
			
					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR54_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR54_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR54_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR54_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR54_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR54_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR54_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR54_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR54_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR54_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR54_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR54_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */


					// row55
					row = sheet.getRow(54);
			

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR55_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR55_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR55_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR55_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR55_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR55_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR55_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR55_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR55_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR55_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR55_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR55_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */


					// row56
					row = sheet.getRow(55);
					

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR56_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR56_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR56_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR56_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR56_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR56_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR56_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR56_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR56_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR56_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR56_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR56_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */


					// row57
					row = sheet.getRow(56);
					

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR57_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR57_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR57_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR57_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR57_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR57_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR57_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR57_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR57_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR57_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR57_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR57_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

  
					// row58
					row = sheet.getRow(57);
			

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR58_amt_name_of_sub1() != null) {
					    cellC.setCellValue(record.getR58_amt_name_of_sub1().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR58_amt_name_of_sub2() != null) {
					    cellD.setCellValue(record.getR58_amt_name_of_sub2().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR58_amt_name_of_sub3() != null) {
					    cellE.setCellValue(record.getR58_amt_name_of_sub3().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR58_amt_name_of_sub4() != null) {
					    cellF.setCellValue(record.getR58_amt_name_of_sub4().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR58_amt_name_of_sub5() != null) {
					    cellG.setCellValue(record.getR58_amt_name_of_sub5().doubleValue());
					} else {
					    cellG.setCellValue(0);
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

	
	
	
	
	public void updateReport(M_CA4_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_CA4_Summary_Entity existing = brrs_m_ca4_summary_repo.findById(updatedEntity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	        // 1️⃣ Loop from R10 to R58 and copy fields
	    	
	        for (int i = 10; i <= 58; i++) {
				
	        	 String prefix = "R" + i + "_";

	            String[] fields = {"item","amt_name_of_sub1", "amt_name_of_sub2", "amt_name_of_sub3", "amt_name_of_sub4","amt_name_of_sub5","tot_amt" };

	            for (String field : fields) {
	            	   String getterName = "get" + prefix + field; // e.g., getR10_item
	                   String setterName = "set" + prefix + field; // e.g., setR10_item

	                try {
	                    Method getter = M_CA4_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_CA4_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	       

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save updated entity
	    brrs_m_ca4_summary_repo.save(existing);
	}

  
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}