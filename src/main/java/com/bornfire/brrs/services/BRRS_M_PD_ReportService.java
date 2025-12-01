package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.M_PD_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_M_PD_Archival_Detail_Repo;
import com.bornfire.brrs.entities.M_PD_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.M_PD_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.BRRS_M_PD_Detail_Repo;
import com.bornfire.brrs.entities.M_PD_Summary_Entity1;
import com.bornfire.brrs.entities.M_PD_Summary_Entity2;
import com.bornfire.brrs.entities.BRRS_M_PD_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_PD_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_PD_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_PD_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.M_PD_Detail_Entity;


import java.math.BigDecimal;

@Component
@Service
public class BRRS_M_PD_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_PD_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;



	@Autowired
	BRRS_M_PD_Detail_Repo BRRS_M_PD_Detail_Repo;

	@Autowired
	BRRS_M_PD_Summary_Repo1 BRRS_M_PD_Summary_Repo1;
	
	@Autowired
	BRRS_M_PD_Summary_Repo2 BRRS_M_PD_Summary_Repo2;

	@Autowired
	BRRS_M_PD_Archival_Detail_Repo BRRS_M_PD_Archival_Detail_Repo;

	@Autowired
	BRRS_M_PD_Archival_Summary_Repo1 BRRS_M_PD_Archival_Summary_Repo1;
	
	@Autowired
	BRRS_M_PD_Archival_Summary_Repo2 BRRS_M_PD_Archival_Summary_Repo2;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	
	
	

	public ModelAndView getM_PDview(String reportId, String fromdate, String todate, String currency,
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
			
			List<M_PD_Archival_Summary_Entity1> T1Master = new ArrayList<M_PD_Archival_Summary_Entity1>();
			List<M_PD_Archival_Summary_Entity2> T2Master = new ArrayList<M_PD_Archival_Summary_Entity2>();
			
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from brrs1_REPORT_ENTITY a where a.report_date = ?1
				// ", brrs1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_PD_Archival_Summary_Repo1.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = BRRS_M_PD_Archival_Summary_Repo2.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary1", T1Master);
			mv.addObject("reportsummary2", T2Master);
			
			
		} else {
			List<M_PD_Summary_Entity1> T1Master = new ArrayList<M_PD_Summary_Entity1>();
			List<M_PD_Summary_Entity2> T2Master = new ArrayList<M_PD_Summary_Entity2>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from brrs1_REPORT_ENTITY a where a.report_date = ?1
				// ", brrs1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_PD_Summary_Repo1.getdatabydateList(dateformat.parse(todate));
				T2Master = BRRS_M_PD_Summary_Repo2.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary1", T1Master);
			mv.addObject("reportsummary2", T2Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_PD");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public ModelAndView getM_PDcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
				List<M_PD_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_PD_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
				} else {
					T1Dt1 = BRRS_M_PD_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_PD_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_PD_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = BRRS_M_PD_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
					totalPages = BRRS_M_PD_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/M_PD");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}


	public byte[] BRRS_M_PDExcel(String filename, String reportId, String fromdate, String todate, String currency,
									 String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_PDARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<M_PD_Summary_Entity1> dataList1 = BRRS_M_PD_Summary_Repo1.getdatabydateList(dateformat.parse(todate));
		List<M_PD_Summary_Entity2> dataList2 = BRRS_M_PD_Summary_Repo2.getdatabydateList(dateformat.parse(todate));
		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_PD report. Returning empty result.");
			return new byte[0];
		}
		if (dataList2.isEmpty()) {
			logger.warn("Service: No data found for M_PD report. Returning empty result.");
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
			int startRow = 8;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					M_PD_Summary_Entity1 record = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);	
					
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
			
					
					
					
					Cell cell2 = row.createCell(1);
					if (record.getR9_30D_90D_PASTDUE() != null) {
						cell2.setCellValue(record.getR9_30D_90D_PASTDUE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					Cell cell3 = row.createCell(2);
					if (record.getR9_NON_PERFORM_LOANS() != null) {
						cell3.setCellValue(record.getR9_NON_PERFORM_LOANS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					Cell cell4 = row.createCell(3);
					if (record.getR9_NON_ACCRUALS1() != null) {
						cell4.setCellValue(record.getR9_NON_ACCRUALS1().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					Cell cell5 = row.createCell(4);
					if (record.getR9_SPECIFIC_PROV1() != null) {
						cell5.setCellValue(record.getR9_SPECIFIC_PROV1().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					Cell cell6 = row.createCell(5);
					if (record.getR9_NO_OF_ACC1() != null) {
						cell6.setCellValue(record.getR9_NO_OF_ACC1().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					Cell cell7 = row.createCell(6);
					if (record.getR9_90D_180D_PASTDUE() != null) {
						cell7.setCellValue(record.getR9_90D_180D_PASTDUE().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					Cell cell8 = row.createCell(7);
					if (record.getR9_NON_ACCRUALS2() != null) {
						cell8.setCellValue(record.getR9_NON_ACCRUALS2().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					Cell cell9 = row.createCell(8);
					if (record.getR9_SPECIFIC_PROV2() != null) {
						cell9.setCellValue(record.getR9_SPECIFIC_PROV2().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					Cell cell10 = row.createCell(9);
					if (record.getR9_NO_OF_ACC2() != null) {
						cell10.setCellValue(record.getR9_NO_OF_ACC2().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					Cell cell11 = row.createCell(10);
					if (record.getR9_180D_ABOVE_PASTDUE() != null) {
						cell11.setCellValue(record.getR9_180D_ABOVE_PASTDUE().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					Cell cell12 = row.createCell(11);
					if (record.getR9_NON_ACCRUALS3() != null) {
						cell12.setCellValue(record.getR9_NON_ACCRUALS3().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					
					Cell cell13 = row.createCell(12);
					if (record.getR9_SPECIFIC_PROV3() != null) {
						cell13.setCellValue(record.getR9_SPECIFIC_PROV3().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					Cell cell14 = row.createCell(13);
					if (record.getR9_NO_OF_ACC3() != null) {
						cell14.setCellValue(record.getR9_NO_OF_ACC3().doubleValue());
						cell14.setCellStyle(numberStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}
					
					Cell cell15 = row.createCell(17);
					if (record.getR9_VALUE_OF_COLLATERAL() != null) {
						cell15.setCellValue(record.getR9_VALUE_OF_COLLATERAL().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(9);
					
					
					cell2 = row.createCell(1);
					if (record.getR10_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR10_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR10_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR10_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR10_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR10_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR10_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR10_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR10_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR10_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR10_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR10_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR10_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR10_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR10_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR10_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR10_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR10_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR10_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR10_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR10_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR10_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR10_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR10_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR10_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR10_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR10_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR10_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);
					
					
					
					cell2 = row.createCell(1);
					if (record.getR11_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR11_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR11_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR11_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR11_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR11_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR11_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR11_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR11_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR11_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR11_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR11_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR11_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR11_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR11_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR11_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR11_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR11_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR11_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR11_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR11_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR11_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR11_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR11_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR11_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR11_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR11_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR11_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					
					
					cell2 = row.createCell(1);
					if (record.getR13_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR13_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR13_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR13_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR13_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR13_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR13_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR13_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR13_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR13_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR13_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR13_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR13_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR13_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR13_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR13_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR13_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR13_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR13_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR13_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR13_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR13_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR13_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR13_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR13_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR13_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR13_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(13);
					
					cell2 = row.createCell(1);
					if (record.getR14_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR14_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR14_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR14_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR14_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR14_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR14_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR14_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR14_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR14_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR14_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR14_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR14_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR14_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR14_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR14_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR14_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR14_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR14_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR14_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR14_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR14_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR14_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR14_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR14_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR14_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR14_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(14);
					
					cell2 = row.createCell(1);
					if (record.getR15_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR15_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR15_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR15_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR15_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR15_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR15_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR15_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR15_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR15_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR15_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR15_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR15_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR15_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR15_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR15_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR15_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR15_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR15_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR15_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR15_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR15_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR15_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR15_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR15_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR15_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR15_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					
					cell2 = row.createCell(1);
					if (record.getR16_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR16_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR16_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR16_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR16_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR16_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR16_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR16_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR16_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR16_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR16_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR16_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR16_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR16_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR16_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR16_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR16_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR16_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR16_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR16_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR16_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR16_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR16_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR16_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR16_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR16_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR16_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(16);
					
					
					cell2 = row.createCell(1);
					if (record.getR17_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR17_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR17_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR17_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR17_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR17_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR17_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR17_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR17_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR17_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR17_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR17_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR17_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR17_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR17_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR17_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR17_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR17_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR17_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR17_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR17_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR17_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR17_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR17_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR17_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR17_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR17_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					
					cell2 = row.createCell(1);
					if (record.getR18_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR18_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR18_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR18_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR18_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR18_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR18_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR18_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR18_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR18_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR18_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR18_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR18_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR18_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR18_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR18_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR18_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR18_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR18_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR18_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR18_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR18_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR18_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR18_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR18_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR18_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR18_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR18_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(18);
					
					cell2 = row.createCell(1);
					if (record.getR19_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR19_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR19_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR19_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR19_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR19_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR19_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR19_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR19_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR19_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR19_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR19_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR19_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR19_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR19_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR19_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR19_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR19_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR19_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR19_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR19_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR19_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR19_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR19_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR19_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR19_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR19_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR19_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(19);
					

					// ====================== R20 ======================
					cell2 = row.createCell(1);
					if (record.getR20_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR20_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR20_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR20_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR20_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR20_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR20_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR20_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR20_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR20_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR20_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR20_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR20_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR20_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR20_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR20_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR20_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR20_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR20_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR20_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR20_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR20_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR20_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR20_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR20_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR20_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR20_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR20_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					
					// ====================== R21 ======================
					cell2 = row.createCell(1);
					if (record.getR21_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR21_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR21_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR21_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR21_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR21_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR21_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR21_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR21_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR21_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR21_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR21_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR21_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR21_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR21_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR21_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR21_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR21_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR21_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR21_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR21_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR21_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR21_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR21_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR21_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR21_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR21_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR21_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					
					// ====================== R22 ======================
					

					cell2 = row.createCell(1);
					if (record.getR22_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR22_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR22_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR22_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR22_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR22_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR22_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR22_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR22_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR22_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR22_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR22_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR22_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR22_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR22_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR22_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR22_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR22_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR22_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR22_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR22_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR22_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR22_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR22_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR22_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR22_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR22_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR22_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(22);
					
					
					// ====================== R23 ======================
					

					cell2 = row.createCell(1);
					if (record.getR23_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR23_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR23_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR23_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR23_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR23_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR23_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR23_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR23_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR23_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR23_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR23_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR23_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR23_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR23_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR23_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR23_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR23_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR23_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR23_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR23_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR23_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR23_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR23_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR23_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR23_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR23_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR23_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(23);
					
					// ====================== R24 ======================
					

					cell2 = row.createCell(1);
					if (record.getR24_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR24_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR24_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR24_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR24_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR24_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR24_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR24_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR24_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR24_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR24_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR24_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR24_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR24_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR24_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR24_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR24_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR24_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR24_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR24_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR24_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR24_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR24_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR24_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR24_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR24_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR24_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR24_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(24);
					
					// ====================== R25 ======================
					

					cell2 = row.createCell(1);
					if (record.getR25_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR25_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR25_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR25_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR25_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR25_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR25_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR25_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR25_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR25_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR25_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR25_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR25_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR25_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR25_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR25_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR25_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR25_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR25_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR25_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR25_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR25_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR25_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR25_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR25_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR25_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR25_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR25_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(26);
					
					// ====================== R27 ======================
					

					cell2 = row.createCell(1);
					if (record.getR27_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR27_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR27_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR27_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR27_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR27_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR27_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR27_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR27_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR27_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR27_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR27_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR27_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR27_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR27_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR27_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR27_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR27_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR27_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR27_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR27_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR27_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR27_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR27_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR27_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR27_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR27_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR27_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(27);
					
					
					// ====================== R28 ======================
					

					cell2 = row.createCell(1);
					if (record.getR28_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR28_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR28_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR28_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR28_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR28_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR28_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR28_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR28_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR28_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR28_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR28_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR28_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR28_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR28_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR28_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR28_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR28_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR28_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR28_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR28_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR28_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR28_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR28_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR28_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR28_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR28_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR28_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					
					
					// ====================== R29 ======================
					

					cell2 = row.createCell(1);
					if (record.getR29_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR29_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR29_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR29_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR29_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR29_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR29_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR29_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR29_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR29_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR29_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR29_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR29_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR29_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR29_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR29_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR29_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR29_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR29_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR29_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR29_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR29_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR29_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR29_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR29_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR29_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR29_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR29_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R30 ======================
					
					row = sheet.getRow(29);
					cell2 = row.createCell(1);
					if (record.getR30_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR30_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR30_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR30_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR30_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR30_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR30_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR30_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR30_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR30_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR30_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR30_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR30_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR30_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR30_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR30_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR30_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR30_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR30_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR30_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR30_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR30_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR30_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR30_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR30_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR30_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR30_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR30_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					// ====================== R31 ======================
					

					cell2 = row.createCell(1);
					if (record.getR31_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR31_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR31_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR31_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR31_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR31_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR31_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR31_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR31_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR31_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR31_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR31_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR31_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR31_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR31_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR31_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR31_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR31_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR31_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR31_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR31_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR31_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR31_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR31_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR31_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR31_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR31_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR31_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(31);

					// ====================== R32 ======================
					

					cell2 = row.createCell(1);
					if (record.getR32_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR32_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR32_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR32_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR32_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR32_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR32_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR32_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR32_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR32_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR32_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR32_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR32_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR32_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR32_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR32_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR32_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR32_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR32_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR32_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR32_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR32_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR32_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR32_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR32_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR32_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR32_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR32_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(32);

					// ====================== R33 ======================
					

					cell2 = row.createCell(1);
					if (record.getR33_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR33_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR33_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR33_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR33_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR33_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR33_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR33_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR33_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR33_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR33_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR33_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR33_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR33_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR33_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR33_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR33_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR33_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR33_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR33_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR33_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR33_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR33_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR33_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR33_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR33_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR33_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR33_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(33);

					// ====================== R34 ======================
					

					cell2 = row.createCell(1);
					if (record.getR34_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR34_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR34_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR34_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR34_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR34_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR34_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR34_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR34_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR34_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR34_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR34_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR34_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR34_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR34_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR34_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR34_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR34_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR34_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR34_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR34_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR34_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR34_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR34_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR34_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR34_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR34_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR34_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(35);
					
					// ====================== R36 ======================
					

					cell2 = row.createCell(1);
					if (record.getR36_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR36_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR36_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR36_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR36_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR36_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR36_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR36_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR36_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR36_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR36_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR36_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR36_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR36_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR36_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR36_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR36_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR36_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR36_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR36_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR36_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR36_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR36_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR36_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR36_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR36_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR36_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR36_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(36);

					// ====================== R37 ======================
					

					cell2 = row.createCell(1);
					if (record.getR37_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR37_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR37_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR37_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR37_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR37_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR37_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR37_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR37_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR37_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR37_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR37_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR37_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR37_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR37_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR37_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR37_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR37_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR37_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR37_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR37_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR37_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR37_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR37_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR37_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR37_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR37_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR37_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
				}
				
				
				
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
				
			} else {

			}
			
			startRow = 38;
			if (!dataList2.isEmpty()) {
				for (int i = 0; i < dataList2.size(); i++) {
					M_PD_Summary_Entity2 record = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);	
					
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
			
					row = sheet.getRow(38);
					
					Cell cell2 = row.createCell(1);
					if (record.getR39_30D_90D_PASTDUE() != null) {
						cell2.setCellValue(record.getR39_30D_90D_PASTDUE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					Cell cell3 = row.createCell(2);
					if (record.getR39_NON_PERFORM_LOANS() != null) {
						cell3.setCellValue(record.getR39_NON_PERFORM_LOANS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					Cell cell4 = row.createCell(3);
					if (record.getR39_NON_ACCRUALS1() != null) {
						cell4.setCellValue(record.getR39_NON_ACCRUALS1().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					Cell cell5 = row.createCell(4);
					if (record.getR39_SPECIFIC_PROV1() != null) {
						cell5.setCellValue(record.getR39_SPECIFIC_PROV1().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					Cell cell6 = row.createCell(5);
					if (record.getR39_NO_OF_ACC1() != null) {
						cell6.setCellValue(record.getR39_NO_OF_ACC1().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					Cell cell7 = row.createCell(6);
					if (record.getR39_90D_180D_PASTDUE() != null) {
						cell7.setCellValue(record.getR39_90D_180D_PASTDUE().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					Cell cell8 = row.createCell(7);
					if (record.getR39_NON_ACCRUALS2() != null) {
						cell8.setCellValue(record.getR39_NON_ACCRUALS2().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					Cell cell9 = row.createCell(8);
					if (record.getR39_SPECIFIC_PROV2() != null) {
						cell9.setCellValue(record.getR39_SPECIFIC_PROV2().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					Cell cell10 = row.createCell(9);
					if (record.getR39_NO_OF_ACC2() != null) {
						cell10.setCellValue(record.getR39_NO_OF_ACC2().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					Cell cell11 = row.createCell(10);
					if (record.getR39_180D_ABOVE_PASTDUE() != null) {
						cell11.setCellValue(record.getR39_180D_ABOVE_PASTDUE().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					Cell cell12 = row.createCell(11);
					if (record.getR39_NON_ACCRUALS3() != null) {
						cell12.setCellValue(record.getR39_NON_ACCRUALS3().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					
					Cell cell13 = row.createCell(12);
					if (record.getR39_SPECIFIC_PROV3() != null) {
						cell13.setCellValue(record.getR39_SPECIFIC_PROV3().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					Cell cell14 = row.createCell(13);
					if (record.getR39_NO_OF_ACC3() != null) {
						cell14.setCellValue(record.getR39_NO_OF_ACC3().doubleValue());
						cell14.setCellStyle(numberStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}
					
					Cell cell15 = row.createCell(17);
					if (record.getR39_VALUE_OF_COLLATERAL() != null) {
						cell15.setCellValue(record.getR39_VALUE_OF_COLLATERAL().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(39);
					
					
					// ====================== R40 ======================
					

					cell2 = row.createCell(1);
					if (record.getR40_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR40_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR40_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR40_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR40_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR40_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR40_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR40_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR40_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR40_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR40_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR40_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR40_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR40_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR40_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR40_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR40_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR40_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR40_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR40_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR40_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR40_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR40_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR40_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR40_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR40_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR40_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR40_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					
					
					// ====================== R42 ======================
					row = sheet.getRow(41);

					cell2 = row.createCell(1);
					if (record.getR42_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR42_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR42_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR42_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR42_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR42_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR42_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR42_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR42_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR42_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR42_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR42_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR42_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR42_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR42_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR42_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR42_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR42_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR42_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR42_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR42_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR42_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR42_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR42_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR42_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR42_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR42_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR42_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(42);
					
					
					// ====================== R43 ======================
					

					cell2 = row.createCell(1);
					if (record.getR43_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR43_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR43_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR43_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR43_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR43_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR43_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR43_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR43_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR43_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR43_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR43_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR43_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR43_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR43_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR43_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR43_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR43_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR43_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR43_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR43_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR43_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR43_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR43_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR43_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR43_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR43_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR43_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R44 ======================
					row = sheet.getRow(43);

					cell2 = row.createCell(1);
					if (record.getR44_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR44_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR44_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR44_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR44_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR44_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR44_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR44_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR44_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR44_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR44_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR44_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR44_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR44_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR44_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR44_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR44_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR44_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR44_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR44_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR44_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR44_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR44_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR44_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR44_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR44_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR44_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR44_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R45 ======================
					row = sheet.getRow(44);

					cell2 = row.createCell(1);
					if (record.getR45_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR45_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR45_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR45_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR45_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR45_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR45_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR45_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR45_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR45_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR45_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR45_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR45_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR45_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR45_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR45_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR45_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR45_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR45_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR45_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR45_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR45_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR45_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR45_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR45_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR45_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR45_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR45_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(46);
					
					// ====================== R47 ======================
					

					cell2 = row.createCell(1);
					if (record.getR47_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR47_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR47_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR47_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR47_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR47_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR47_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR47_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR47_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR47_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR47_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR47_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR47_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR47_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR47_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR47_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR47_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR47_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR47_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR47_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR47_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR47_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR47_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR47_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR47_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR47_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR47_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR47_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R48 ======================
					row = sheet.getRow(47);

					cell2 = row.createCell(1);
					if (record.getR48_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR48_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR48_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR48_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR48_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR48_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR48_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR48_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR48_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR48_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR48_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR48_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR48_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR48_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR48_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR48_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR48_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR48_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR48_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR48_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR48_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR48_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR48_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR48_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR48_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR48_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR48_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR48_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R49 ======================
					row = sheet.getRow(48);

					cell2 = row.createCell(1);
					if (record.getR49_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR49_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR49_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR49_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR49_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR49_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR49_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR49_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR49_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR49_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR49_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR49_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR49_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR49_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR49_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR49_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR49_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR49_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR49_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR49_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR49_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR49_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR49_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR49_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR49_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR49_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR49_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR49_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(50);
					
					// ====================== R51 ======================
					

					cell2 = row.createCell(1);
					if (record.getR51_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR51_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR51_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR51_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR51_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR51_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR51_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR51_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR51_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR51_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR51_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR51_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR51_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR51_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR51_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR51_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR51_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR51_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR51_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR51_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR51_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR51_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR51_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR51_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR51_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR51_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR51_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR51_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					// ====================== R52 ======================
					
					row = sheet.getRow(51);

					cell2 = row.createCell(1);
					if (record.getR52_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR52_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR52_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR52_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR52_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR52_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR52_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR52_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR52_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR52_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR52_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR52_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR52_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR52_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR52_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR52_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR52_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR52_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR52_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR52_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR52_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR52_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR52_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR52_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR52_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR52_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR52_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR52_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R53 ======================
					row = sheet.getRow(52);

					cell2 = row.createCell(1);
					if (record.getR53_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR53_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR53_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR53_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR53_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR53_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR53_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR53_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR53_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR53_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR53_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR53_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR53_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR53_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR53_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR53_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR53_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR53_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR53_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR53_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR53_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR53_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR53_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR53_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR53_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR53_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR53_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR53_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(54);
					
					// ====================== R55 ======================
					

					cell2 = row.createCell(1);
					if (record.getR55_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR55_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR55_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR55_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR55_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR55_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR55_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR55_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR55_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR55_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR55_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR55_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR55_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR55_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR55_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR55_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR55_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR55_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR55_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR55_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR55_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR55_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR55_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR55_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR55_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR55_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR55_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR55_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R56 ======================
					row = sheet.getRow(55);

					cell2 = row.createCell(1);
					if (record.getR56_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR56_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR56_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR56_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR56_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR56_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR56_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR56_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR56_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR56_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR56_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR56_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR56_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR56_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR56_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR56_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR56_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR56_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR56_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR56_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR56_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR56_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR56_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR56_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR56_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR56_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR56_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR56_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R57 ======================
					row = sheet.getRow(56);

					cell2 = row.createCell(1);
					if (record.getR57_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR57_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR57_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR57_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR57_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR57_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR57_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR57_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR57_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR57_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR57_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR57_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR57_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR57_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR57_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR57_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR57_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR57_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR57_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR57_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR57_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR57_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR57_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR57_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR57_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR57_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR57_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR57_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R58 ======================
					row = sheet.getRow(57);

					cell2 = row.createCell(1);
					if (record.getR58_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR58_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR58_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR58_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR58_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR58_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR58_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR58_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR58_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR58_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR58_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR58_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR58_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR58_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR58_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR58_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR58_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR58_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR58_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR58_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR58_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR58_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR58_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR58_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR58_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR58_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR58_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR58_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R59 ======================
					row = sheet.getRow(58);

					cell2 = row.createCell(1);
					if (record.getR59_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR59_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR59_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR59_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR59_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR59_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR59_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR59_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR59_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR59_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR59_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR59_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR59_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR59_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR59_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR59_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR59_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR59_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR59_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR59_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR59_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR59_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR59_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR59_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR59_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR59_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR59_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR59_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R60 ======================
					row = sheet.getRow(59);

					cell2 = row.createCell(1);
					if (record.getR60_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR60_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR60_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR60_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR60_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR60_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR60_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR60_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR60_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR60_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR60_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR60_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR60_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR60_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR60_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR60_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR60_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR60_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR60_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR60_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR60_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR60_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR60_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR60_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR60_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR60_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR60_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR60_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
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

	public byte[] BRRS_M_PDDetailExcel(String filename, String fromdate, String todate, String currency,
										   String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_PD Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_PDDetails");

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
			List<M_PD_Detail_Entity> reportData = BRRS_M_PD_Detail_Repo.getdatabydateList(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_PD_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCUST_ID());
					row.createCell(1).setCellValue(item.getACCT_NUMBER());
					row.createCell(2).setCellValue(item.getACCT_NAME());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getACCT_BALANCE_IN_PULA() != null) {
						balanceCell.setCellValue(item.getACCT_BALANCE_IN_PULA().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(4).setCellValue(item.getROW_ID());
					row.createCell(5).setCellValue(item.getCOLUMN_ID());
					row.createCell(6)
							.setCellValue(item.getREPORT_DATE() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE())
									: "");
					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_PD — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_PD Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getM_PDArchival() {
		List<Object> M_PDArchivallist = new ArrayList<>();
		List<Object> M_PDArchivallist1 = new ArrayList<>();
		try {
			M_PDArchivallist = BRRS_M_PD_Archival_Summary_Repo1.getM_PDarchival();
			M_PDArchivallist1 = BRRS_M_PD_Archival_Summary_Repo2.getM_PDarchival();
			System.out.println("countser" + M_PDArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_PD Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_PDArchivallist;
	}

	
	public byte[] getExcelM_PDARCHIVAL(String filename, String reportId, String fromdate, String todate,
										   String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_PD_Archival_Summary_Entity1> dataList1 = BRRS_M_PD_Archival_Summary_Repo1
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_PD_Archival_Summary_Entity2> dataList2 = BRRS_M_PD_Archival_Summary_Repo2
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_PD report. Returning empty result.");
			return new byte[0];
		}

		if (dataList2.isEmpty()) {
			logger.warn("Service: No data found for M_PD report. Returning empty result.");
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

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					M_PD_Archival_Summary_Entity1 record = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);	
					
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
			
					
					
					
					Cell cell2 = row.createCell(1);
					if (record.getR9_30D_90D_PASTDUE() != null) {
						cell2.setCellValue(record.getR9_30D_90D_PASTDUE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					Cell cell3 = row.createCell(2);
					if (record.getR9_NON_PERFORM_LOANS() != null) {
						cell3.setCellValue(record.getR9_NON_PERFORM_LOANS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					Cell cell4 = row.createCell(3);
					if (record.getR9_NON_ACCRUALS1() != null) {
						cell4.setCellValue(record.getR9_NON_ACCRUALS1().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					Cell cell5 = row.createCell(4);
					if (record.getR9_SPECIFIC_PROV1() != null) {
						cell5.setCellValue(record.getR9_SPECIFIC_PROV1().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					Cell cell6 = row.createCell(5);
					if (record.getR9_NO_OF_ACC1() != null) {
						cell6.setCellValue(record.getR9_NO_OF_ACC1().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					Cell cell7 = row.createCell(6);
					if (record.getR9_90D_180D_PASTDUE() != null) {
						cell7.setCellValue(record.getR9_90D_180D_PASTDUE().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					Cell cell8 = row.createCell(7);
					if (record.getR9_NON_ACCRUALS2() != null) {
						cell8.setCellValue(record.getR9_NON_ACCRUALS2().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					Cell cell9 = row.createCell(8);
					if (record.getR9_SPECIFIC_PROV2() != null) {
						cell9.setCellValue(record.getR9_SPECIFIC_PROV2().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					Cell cell10 = row.createCell(9);
					if (record.getR9_NO_OF_ACC2() != null) {
						cell10.setCellValue(record.getR9_NO_OF_ACC2().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					Cell cell11 = row.createCell(10);
					if (record.getR9_180D_ABOVE_PASTDUE() != null) {
						cell11.setCellValue(record.getR9_180D_ABOVE_PASTDUE().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					Cell cell12 = row.createCell(11);
					if (record.getR9_NON_ACCRUALS3() != null) {
						cell12.setCellValue(record.getR9_NON_ACCRUALS3().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					
					Cell cell13 = row.createCell(12);
					if (record.getR9_SPECIFIC_PROV3() != null) {
						cell13.setCellValue(record.getR9_SPECIFIC_PROV3().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					Cell cell14 = row.createCell(13);
					if (record.getR9_NO_OF_ACC3() != null) {
						cell14.setCellValue(record.getR9_NO_OF_ACC3().doubleValue());
						cell14.setCellStyle(numberStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}
					
					Cell cell15 = row.createCell(17);
					if (record.getR9_VALUE_OF_COLLATERAL() != null) {
						cell15.setCellValue(record.getR9_VALUE_OF_COLLATERAL().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(9);
					
					
					cell2 = row.createCell(1);
					if (record.getR10_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR10_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR10_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR10_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR10_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR10_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR10_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR10_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR10_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR10_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR10_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR10_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR10_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR10_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR10_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR10_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR10_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR10_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR10_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR10_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR10_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR10_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR10_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR10_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR10_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR10_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR10_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR10_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);
					
					
					
					cell2 = row.createCell(1);
					if (record.getR11_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR11_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR11_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR11_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR11_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR11_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR11_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR11_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR11_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR11_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR11_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR11_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR11_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR11_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR11_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR11_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR11_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR11_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR11_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR11_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR11_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR11_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR11_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR11_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR11_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR11_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR11_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR11_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					
					
					cell2 = row.createCell(1);
					if (record.getR13_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR13_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR13_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR13_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR13_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR13_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR13_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR13_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR13_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR13_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR13_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR13_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR13_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR13_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR13_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR13_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR13_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR13_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR13_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR13_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR13_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR13_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR13_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR13_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR13_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR13_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR13_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(13);
					
					cell2 = row.createCell(1);
					if (record.getR14_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR14_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR14_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR14_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR14_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR14_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR14_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR14_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR14_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR14_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR14_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR14_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR14_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR14_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR14_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR14_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR14_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR14_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR14_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR14_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR14_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR14_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR14_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR14_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR14_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR14_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR14_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(14);
					
					cell2 = row.createCell(1);
					if (record.getR15_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR15_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR15_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR15_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR15_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR15_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR15_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR15_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR15_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR15_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR15_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR15_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR15_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR15_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR15_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR15_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR15_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR15_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR15_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR15_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR15_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR15_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR15_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR15_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR15_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR15_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR15_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					
					cell2 = row.createCell(1);
					if (record.getR16_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR16_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR16_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR16_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR16_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR16_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR16_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR16_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR16_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR16_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR16_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR16_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR16_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR16_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR16_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR16_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR16_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR16_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR16_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR16_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR16_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR16_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR16_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR16_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR16_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR16_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR16_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(16);
					
					
					cell2 = row.createCell(1);
					if (record.getR17_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR17_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR17_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR17_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR17_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR17_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR17_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR17_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR17_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR17_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR17_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR17_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR17_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR17_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR17_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR17_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR17_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR17_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR17_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR17_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR17_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR17_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR17_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR17_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR17_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR17_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR17_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					
					cell2 = row.createCell(1);
					if (record.getR18_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR18_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR18_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR18_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR18_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR18_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR18_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR18_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR18_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR18_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR18_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR18_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR18_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR18_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR18_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR18_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR18_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR18_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR18_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR18_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR18_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR18_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR18_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR18_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR18_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR18_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR18_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR18_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(18);
					
					cell2 = row.createCell(1);
					if (record.getR19_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR19_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR19_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR19_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR19_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR19_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR19_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR19_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR19_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR19_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR19_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR19_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR19_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR19_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR19_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR19_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR19_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR19_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR19_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR19_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR19_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR19_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR19_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR19_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR19_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR19_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR19_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR19_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(19);
					

					// ====================== R20 ======================
					cell2 = row.createCell(1);
					if (record.getR20_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR20_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR20_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR20_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR20_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR20_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR20_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR20_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR20_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR20_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR20_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR20_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR20_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR20_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR20_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR20_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR20_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR20_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR20_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR20_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR20_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR20_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR20_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR20_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR20_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR20_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR20_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR20_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					
					// ====================== R21 ======================
					cell2 = row.createCell(1);
					if (record.getR21_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR21_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR21_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR21_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR21_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR21_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR21_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR21_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR21_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR21_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR21_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR21_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR21_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR21_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR21_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR21_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR21_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR21_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR21_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR21_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR21_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR21_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR21_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR21_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR21_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR21_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR21_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR21_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					
					// ====================== R22 ======================
					

					cell2 = row.createCell(1);
					if (record.getR22_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR22_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR22_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR22_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR22_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR22_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR22_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR22_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR22_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR22_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR22_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR22_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR22_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR22_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR22_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR22_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR22_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR22_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR22_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR22_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR22_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR22_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR22_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR22_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR22_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR22_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR22_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR22_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(22);
					
					
					// ====================== R23 ======================
					

					cell2 = row.createCell(1);
					if (record.getR23_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR23_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR23_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR23_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR23_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR23_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR23_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR23_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR23_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR23_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR23_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR23_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR23_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR23_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR23_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR23_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR23_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR23_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR23_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR23_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR23_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR23_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR23_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR23_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR23_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR23_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR23_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR23_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(23);
					
					// ====================== R24 ======================
					

					cell2 = row.createCell(1);
					if (record.getR24_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR24_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR24_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR24_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR24_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR24_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR24_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR24_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR24_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR24_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR24_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR24_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR24_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR24_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR24_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR24_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR24_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR24_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR24_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR24_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR24_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR24_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR24_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR24_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR24_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR24_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR24_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR24_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(24);
					
					// ====================== R25 ======================
					

					cell2 = row.createCell(1);
					if (record.getR25_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR25_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR25_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR25_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR25_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR25_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR25_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR25_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR25_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR25_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR25_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR25_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR25_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR25_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR25_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR25_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR25_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR25_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR25_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR25_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR25_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR25_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR25_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR25_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR25_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR25_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR25_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR25_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(26);
					
					// ====================== R27 ======================
					

					cell2 = row.createCell(1);
					if (record.getR27_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR27_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR27_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR27_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR27_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR27_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR27_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR27_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR27_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR27_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR27_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR27_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR27_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR27_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR27_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR27_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR27_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR27_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR27_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR27_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR27_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR27_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR27_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR27_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR27_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR27_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR27_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR27_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(27);
					
					
					// ====================== R28 ======================
					

					cell2 = row.createCell(1);
					if (record.getR28_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR28_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR28_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR28_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR28_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR28_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR28_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR28_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR28_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR28_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR28_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR28_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR28_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR28_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR28_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR28_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR28_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR28_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR28_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR28_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR28_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR28_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR28_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR28_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR28_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR28_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR28_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR28_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					
					
					// ====================== R29 ======================
					

					cell2 = row.createCell(1);
					if (record.getR29_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR29_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR29_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR29_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR29_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR29_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR29_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR29_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR29_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR29_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR29_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR29_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR29_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR29_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR29_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR29_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR29_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR29_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR29_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR29_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR29_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR29_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR29_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR29_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR29_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR29_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR29_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR29_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R30 ======================
					
					row = sheet.getRow(29);
					cell2 = row.createCell(1);
					if (record.getR30_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR30_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR30_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR30_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR30_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR30_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR30_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR30_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR30_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR30_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR30_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR30_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR30_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR30_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR30_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR30_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR30_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR30_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR30_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR30_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR30_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR30_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR30_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR30_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR30_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR30_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR30_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR30_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					// ====================== R31 ======================
					

					cell2 = row.createCell(1);
					if (record.getR31_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR31_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR31_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR31_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR31_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR31_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR31_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR31_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR31_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR31_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR31_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR31_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR31_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR31_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR31_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR31_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR31_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR31_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR31_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR31_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR31_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR31_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR31_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR31_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR31_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR31_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR31_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR31_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(31);

					// ====================== R32 ======================
					

					cell2 = row.createCell(1);
					if (record.getR32_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR32_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR32_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR32_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR32_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR32_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR32_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR32_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR32_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR32_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR32_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR32_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR32_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR32_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR32_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR32_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR32_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR32_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR32_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR32_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR32_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR32_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR32_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR32_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR32_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR32_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR32_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR32_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(32);

					// ====================== R33 ======================
					

					cell2 = row.createCell(1);
					if (record.getR33_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR33_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR33_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR33_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR33_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR33_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR33_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR33_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR33_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR33_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR33_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR33_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR33_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR33_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR33_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR33_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR33_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR33_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR33_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR33_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR33_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR33_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR33_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR33_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR33_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR33_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR33_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR33_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(33);

					// ====================== R34 ======================
					

					cell2 = row.createCell(1);
					if (record.getR34_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR34_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR34_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR34_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR34_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR34_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR34_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR34_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR34_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR34_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR34_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR34_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR34_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR34_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR34_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR34_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR34_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR34_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR34_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR34_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR34_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR34_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR34_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR34_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR34_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR34_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR34_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR34_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(35);
					
					// ====================== R36 ======================
					

					cell2 = row.createCell(1);
					if (record.getR36_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR36_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR36_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR36_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR36_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR36_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR36_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR36_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR36_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR36_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR36_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR36_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR36_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR36_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR36_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR36_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR36_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR36_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR36_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR36_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR36_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR36_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR36_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR36_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR36_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR36_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR36_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR36_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(36);

					// ====================== R37 ======================
					

					cell2 = row.createCell(1);
					if (record.getR37_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR37_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR37_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR37_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR37_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR37_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR37_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR37_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR37_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR37_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR37_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR37_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR37_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR37_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR37_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR37_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR37_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR37_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR37_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR37_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR37_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR37_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR37_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR37_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR37_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR37_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR37_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR37_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
				}
				
				
				
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
				
			} else {

			}
			
			startRow = 38;
			if (!dataList2.isEmpty()) {
				for (int i = 0; i < dataList2.size(); i++) {
					M_PD_Archival_Summary_Entity2 record = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);	
					
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
			
					row = sheet.getRow(38);
					
					Cell cell2 = row.createCell(1);
					if (record.getR39_30D_90D_PASTDUE() != null) {
						cell2.setCellValue(record.getR39_30D_90D_PASTDUE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					Cell cell3 = row.createCell(2);
					if (record.getR39_NON_PERFORM_LOANS() != null) {
						cell3.setCellValue(record.getR39_NON_PERFORM_LOANS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					Cell cell4 = row.createCell(3);
					if (record.getR39_NON_ACCRUALS1() != null) {
						cell4.setCellValue(record.getR39_NON_ACCRUALS1().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					Cell cell5 = row.createCell(4);
					if (record.getR39_SPECIFIC_PROV1() != null) {
						cell5.setCellValue(record.getR39_SPECIFIC_PROV1().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					Cell cell6 = row.createCell(5);
					if (record.getR39_NO_OF_ACC1() != null) {
						cell6.setCellValue(record.getR39_NO_OF_ACC1().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					Cell cell7 = row.createCell(6);
					if (record.getR39_90D_180D_PASTDUE() != null) {
						cell7.setCellValue(record.getR39_90D_180D_PASTDUE().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					Cell cell8 = row.createCell(7);
					if (record.getR39_NON_ACCRUALS2() != null) {
						cell8.setCellValue(record.getR39_NON_ACCRUALS2().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					Cell cell9 = row.createCell(8);
					if (record.getR39_SPECIFIC_PROV2() != null) {
						cell9.setCellValue(record.getR39_SPECIFIC_PROV2().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					Cell cell10 = row.createCell(9);
					if (record.getR39_NO_OF_ACC2() != null) {
						cell10.setCellValue(record.getR39_NO_OF_ACC2().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					Cell cell11 = row.createCell(10);
					if (record.getR39_180D_ABOVE_PASTDUE() != null) {
						cell11.setCellValue(record.getR39_180D_ABOVE_PASTDUE().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					Cell cell12 = row.createCell(11);
					if (record.getR39_NON_ACCRUALS3() != null) {
						cell12.setCellValue(record.getR39_NON_ACCRUALS3().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					
					Cell cell13 = row.createCell(12);
					if (record.getR39_SPECIFIC_PROV3() != null) {
						cell13.setCellValue(record.getR39_SPECIFIC_PROV3().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					Cell cell14 = row.createCell(13);
					if (record.getR39_NO_OF_ACC3() != null) {
						cell14.setCellValue(record.getR39_NO_OF_ACC3().doubleValue());
						cell14.setCellStyle(numberStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}
					
					Cell cell15 = row.createCell(17);
					if (record.getR39_VALUE_OF_COLLATERAL() != null) {
						cell15.setCellValue(record.getR39_VALUE_OF_COLLATERAL().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(39);
					
					
					// ====================== R40 ======================
					

					cell2 = row.createCell(1);
					if (record.getR40_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR40_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR40_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR40_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR40_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR40_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR40_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR40_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR40_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR40_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR40_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR40_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR40_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR40_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR40_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR40_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR40_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR40_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR40_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR40_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR40_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR40_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR40_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR40_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR40_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR40_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR40_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR40_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					
					
					// ====================== R42 ======================
					row = sheet.getRow(41);

					cell2 = row.createCell(1);
					if (record.getR42_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR42_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR42_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR42_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR42_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR42_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR42_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR42_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR42_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR42_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR42_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR42_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR42_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR42_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR42_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR42_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR42_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR42_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR42_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR42_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR42_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR42_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR42_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR42_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR42_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR42_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR42_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR42_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(42);
					
					
					// ====================== R43 ======================
					

					cell2 = row.createCell(1);
					if (record.getR43_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR43_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR43_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR43_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR43_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR43_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR43_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR43_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR43_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR43_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR43_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR43_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR43_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR43_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR43_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR43_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR43_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR43_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR43_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR43_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR43_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR43_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR43_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR43_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR43_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR43_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR43_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR43_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R44 ======================
					row = sheet.getRow(43);

					cell2 = row.createCell(1);
					if (record.getR44_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR44_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR44_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR44_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR44_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR44_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR44_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR44_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR44_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR44_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR44_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR44_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR44_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR44_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR44_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR44_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR44_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR44_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR44_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR44_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR44_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR44_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR44_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR44_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR44_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR44_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR44_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR44_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R45 ======================
					row = sheet.getRow(44);

					cell2 = row.createCell(1);
					if (record.getR45_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR45_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR45_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR45_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR45_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR45_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR45_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR45_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR45_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR45_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR45_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR45_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR45_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR45_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR45_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR45_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR45_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR45_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR45_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR45_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR45_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR45_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR45_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR45_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR45_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR45_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR45_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR45_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(46);
					
					// ====================== R47 ======================
					

					cell2 = row.createCell(1);
					if (record.getR47_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR47_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR47_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR47_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR47_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR47_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR47_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR47_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR47_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR47_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR47_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR47_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR47_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR47_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR47_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR47_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR47_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR47_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR47_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR47_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR47_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR47_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR47_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR47_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR47_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR47_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR47_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR47_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R48 ======================
					row = sheet.getRow(47);

					cell2 = row.createCell(1);
					if (record.getR48_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR48_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR48_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR48_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR48_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR48_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR48_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR48_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR48_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR48_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR48_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR48_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR48_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR48_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR48_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR48_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR48_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR48_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR48_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR48_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR48_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR48_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR48_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR48_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR48_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR48_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR48_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR48_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R49 ======================
					row = sheet.getRow(48);

					cell2 = row.createCell(1);
					if (record.getR49_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR49_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR49_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR49_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR49_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR49_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR49_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR49_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR49_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR49_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR49_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR49_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR49_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR49_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR49_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR49_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR49_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR49_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR49_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR49_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR49_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR49_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR49_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR49_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR49_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR49_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR49_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR49_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(50);
					
					// ====================== R51 ======================
					

					cell2 = row.createCell(1);
					if (record.getR51_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR51_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR51_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR51_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR51_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR51_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR51_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR51_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR51_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR51_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR51_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR51_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR51_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR51_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR51_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR51_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR51_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR51_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR51_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR51_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR51_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR51_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR51_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR51_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR51_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR51_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR51_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR51_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					// ====================== R52 ======================
					
					row = sheet.getRow(51);

					cell2 = row.createCell(1);
					if (record.getR52_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR52_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR52_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR52_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR52_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR52_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR52_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR52_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR52_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR52_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR52_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR52_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR52_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR52_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR52_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR52_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR52_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR52_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR52_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR52_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR52_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR52_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR52_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR52_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR52_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR52_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR52_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR52_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R53 ======================
					row = sheet.getRow(52);

					cell2 = row.createCell(1);
					if (record.getR53_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR53_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR53_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR53_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR53_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR53_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR53_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR53_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR53_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR53_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR53_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR53_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR53_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR53_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR53_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR53_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR53_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR53_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR53_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR53_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR53_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR53_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR53_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR53_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR53_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR53_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR53_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR53_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(54);
					
					// ====================== R55 ======================
					
 
					cell2 = row.createCell(1);
					if (record.getR55_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR55_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR55_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR55_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR55_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR55_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR55_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR55_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR55_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR55_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR55_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR55_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR55_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR55_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR55_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR55_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR55_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR55_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR55_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR55_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR55_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR55_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR55_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR55_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR55_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR55_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR55_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR55_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R56 ======================
					row = sheet.getRow(55);

					cell2 = row.createCell(1);
					if (record.getR56_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR56_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR56_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR56_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR56_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR56_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR56_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR56_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR56_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR56_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR56_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR56_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR56_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR56_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR56_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR56_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR56_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR56_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR56_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR56_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR56_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR56_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR56_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR56_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR56_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR56_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR56_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR56_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R57 ======================
					row = sheet.getRow(56);

					cell2 = row.createCell(1);
					if (record.getR57_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR57_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR57_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR57_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR57_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR57_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR57_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR57_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR57_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR57_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR57_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR57_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR57_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR57_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR57_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR57_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR57_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR57_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR57_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR57_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR57_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR57_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR57_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR57_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR57_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR57_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR57_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR57_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R58 ======================
					row = sheet.getRow(57);

					cell2 = row.createCell(1);
					if (record.getR58_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR58_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR58_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR58_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR58_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR58_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR58_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR58_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR58_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR58_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR58_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR58_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR58_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR58_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR58_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR58_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR58_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR58_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR58_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR58_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR58_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR58_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR58_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR58_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR58_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR58_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR58_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR58_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R59 ======================
					row = sheet.getRow(58);

					cell2 = row.createCell(1);
					if (record.getR59_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR59_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR59_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR59_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR59_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR59_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR59_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR59_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR59_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR59_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR59_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR59_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR59_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR59_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR59_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR59_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR59_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR59_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR59_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR59_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR59_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR59_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR59_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR59_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR59_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR59_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR59_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR59_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R60 ======================
					row = sheet.getRow(59);

					cell2 = row.createCell(1);
					if (record.getR60_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR60_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR60_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR60_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR60_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR60_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR60_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR60_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR60_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR60_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR60_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR60_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR60_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR60_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR60_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR60_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR60_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR60_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record.getR60_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record.getR60_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record.getR60_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record.getR60_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record.getR60_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record.getR60_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record.getR60_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record.getR60_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR60_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR60_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
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
			logger.info("Generating Excel for BRRS_M_PD ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_PDDetail");

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
			List<M_PD_Archival_Detail_Entity> reportData = BRRS_M_PD_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_PD_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCUST_ID());
					row.createCell(1).setCellValue(item.getACCT_NUMBER());
					row.createCell(2).setCellValue(item.getACCT_NAME());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getACCT_BALANCE_IN_PULA() != null) {
						balanceCell.setCellValue(item.getACCT_BALANCE_IN_PULA().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getROW_ID());
					row.createCell(5).setCellValue(item.getCOLUMN_ID());
					row.createCell(6)
							.setCellValue(item.getREPORT_DATE() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_PD — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_PDExcel", e);
			return new byte[0];
		}
	}

}
