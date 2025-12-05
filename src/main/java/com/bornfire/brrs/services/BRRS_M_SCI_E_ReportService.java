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

import com.bornfire.brrs.entities.BRRS_M_SCI_E_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SCI_E_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SCI_E_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SCI_E_Manual_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SCI_E_Manual_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SCI_E_Summary_Repo;
import com.bornfire.brrs.entities.M_SCI_E_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SCI_E_Archival_Manual_Summary_Entity;
import com.bornfire.brrs.entities.M_SCI_E_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SCI_E_Detail_Entity;
import com.bornfire.brrs.entities.M_SCI_E_Manual_Summary_Entity;
import com.bornfire.brrs.entities.M_SCI_E_Summary_Entity;

@Component
@Service

public class BRRS_M_SCI_E_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SCI_E_ReportService.class);

	

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	
	  @Autowired BRRS_M_SCI_E_Detail_Repo brrs_m_sci_e_detail_repo;
	 
	@Autowired
	BRRS_M_SCI_E_Manual_Summary_Repo brrs_m_sci_e_manual_summary_repo;
	
	@Autowired
	BRRS_M_SCI_E_Summary_Repo brrs_m_sci_e_summary_repo;
	
	
	
	
	
	  @Autowired BRRS_M_SCI_E_Archival_Detail_Repo m_sci_e_Archival_Detail_Repo;
	 

	@Autowired
	BRRS_M_SCI_E_Manual_Archival_Summary_Repo m_sci_e_manual_Archival_Summary_Repo;
	
	@Autowired
	BRRS_M_SCI_E_Archival_Summary_Repo brrs_m_sci_e_Archival_summary_repo;
	
	
	
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	
	public ModelAndView getM_SCI_EView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {

		ModelAndView mv = new ModelAndView();
		

		System.out.println("testing");
		System.out.println(version);

		if ("ARCHIVAL".equals(type) && version != null && !version.isEmpty()) {

		    System.out.println("ARCHIVAL MODE");
		    System.out.println("version = " + version);

		    List<M_SCI_E_Archival_Summary_Entity> T1Master = new ArrayList<>();
		    List<M_SCI_E_Archival_Manual_Summary_Entity> T2Master = new ArrayList<>();

		    try {
		        Date dt = dateformat.parse(todate);

		        T1Master = brrs_m_sci_e_Archival_summary_repo.getdatabydateListarchival(dt, version);
		        T2Master = m_sci_e_manual_Archival_Summary_Repo.getdatabydateListarchival(dt, version);

		        System.out.println("T1Master size = " + T1Master.size());
		        System.out.println("T2Master size = " + T2Master.size());

		    } catch (ParseException e) {
		        e.printStackTrace();
		    }

		    mv.addObject("reportsummary", T1Master);
		    mv.addObject("reportsummary1", T2Master);
		} else {

			List<M_SCI_E_Summary_Entity> T1Master = new ArrayList<M_SCI_E_Summary_Entity>();
			List<M_SCI_E_Manual_Summary_Entity> T2Master = new ArrayList<M_SCI_E_Manual_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				
				T1Master = brrs_m_sci_e_summary_repo.getdatabydateList(dateformat.parse(todate));
				T2Master = brrs_m_sci_e_manual_summary_repo.getdatabydateList(dateformat.parse(todate));
				System.out.println("T2Master size " + T2Master.size());
				mv.addObject("report_date", dateformat.format(d1));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		}

	

		mv.setViewName("BRRS/M_SCI_E");

		mv.addObject("displaymode", "summary");

		System.out.println("scv" + mv.getViewName());

		return mv;

	}
	 
	
	
	public ModelAndView getM_SCI_EcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
				List<M_SCI_E_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = m_sci_e_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate, version);
				} else {
					T1Dt1 = m_sci_e_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_SCI_E_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = brrs_m_sci_e_detail_repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate);
				} else {
					T1Dt1 = brrs_m_sci_e_detail_repo.getdatabydateList(parsedDate);
					totalPages = brrs_m_sci_e_detail_repo.getdatacount(parsedDate);
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

	

	
		mv.setViewName("BRRS/M_SCI_E");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}
	
	
	
	

	public byte[] getM_SCI_EExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_SCI_EARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<M_SCI_E_Summary_Entity> dataList = brrs_m_sci_e_summary_repo.getdatabydateList(dateformat.parse(todate));
		List<M_SCI_E_Manual_Summary_Entity> dataList1 = brrs_m_sci_e_manual_summary_repo
				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SCI_E report. Returning empty result.");
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

			int startRow = 10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SCI_E_Summary_Entity record = dataList.get(i);
					M_SCI_E_Manual_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

Cell cellC,cellD;    
					
					// row11
					
					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR11_month() != null) {
						cellC.setCellValue(record.getR11_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR11_ytd() != null) {
						cellD.setCellValue(record.getR11_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column 1 - product name
					

					// Column 2 - cross_reference
					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR12_month() != null) {
						cellC.setCellValue(record.getR12_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR12_ytd() != null) {
						cellD.setCellValue(record.getR12_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column 1 - product name
					

					// Column 2 - cross_reference
					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR13_month() != null) {
						cellC.setCellValue(record.getR13_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR13_ytd() != null) {
						cellD.setCellValue(record.getR13_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					

					// row15
					row = sheet.getRow(14); // Row index for R15 (0-based index)

					// Column 1 - product name
					

					// Column 2 - cross_reference
					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR15_month() != null) {
						cellC.setCellValue(record.getR15_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR15_ytd() != null) {
						cellD.setCellValue(record.getR15_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR16_month() != null) {
						cellC.setCellValue(record.getR16_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR16_ytd() != null) {
						cellD.setCellValue(record.getR16_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					

					cellC = row.createCell(2);
					if (record.getR17_month() != null) {
						cellC.setCellValue(record.getR17_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR17_ytd() != null) {
						cellD.setCellValue(record.getR17_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					

					cellC = row.createCell(2);
					if (record.getR18_month() != null) {
						cellC.setCellValue(record.getR18_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR18_ytd() != null) {
						cellD.setCellValue(record.getR18_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					

					cellC = row.createCell(2);
					if (record.getR19_month() != null) {
						cellC.setCellValue(record.getR19_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR19_ytd() != null) {
						cellD.setCellValue(record.getR19_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					

					cellC = row.createCell(2);
					if (record.getR20_month() != null) {
						cellC.setCellValue(record.getR20_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR20_ytd() != null) {
						cellD.setCellValue(record.getR20_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);

					

					cellC = row.createCell(2);
					if (record.getR21_month() != null) {
						cellC.setCellValue(record.getR21_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR21_ytd() != null) {
						cellD.setCellValue(record.getR21_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					

					cellC = row.createCell(2);
					if (record.getR22_month() != null) {
						cellC.setCellValue(record.getR22_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR22_ytd() != null) {
						cellD.setCellValue(record.getR22_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					

					cellC = row.createCell(2);
					if (record.getR23_month() != null) {
						cellC.setCellValue(record.getR23_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR23_ytd() != null) {
						cellD.setCellValue(record.getR23_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					

					cellC = row.createCell(2);
					if (record.getR24_month() != null) {
						cellC.setCellValue(record.getR24_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR24_ytd() != null) {
						cellD.setCellValue(record.getR24_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);

					

					cellC = row.createCell(2);
					if (record.getR25_month() != null) {
						cellC.setCellValue(record.getR25_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR25_ytd() != null) {
						cellD.setCellValue(record.getR25_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					// Column 1 - product name
					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR26_month() != null) {
						cellC.setCellValue(record.getR26_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR26_ytd() != null) {
						cellD.setCellValue(record.getR26_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					
					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR27_month() != null) {
						cellC.setCellValue(record.getR27_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR27_ytd() != null) {
						cellD.setCellValue(record.getR27_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					

					// row29
					row = sheet.getRow(28);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR29_month() != null) {
						cellC.setCellValue(record.getR29_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR29_ytd() != null) {
						cellD.setCellValue(record.getR29_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR30_month() != null) {
						cellC.setCellValue(record.getR30_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR30_ytd() != null) {
						cellD.setCellValue(record.getR30_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);
					
					
					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR31_month() != null) {
						cellC.setCellValue(record.getR31_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR31_ytd() != null) {
						cellD.setCellValue(record.getR31_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// ------------------------- Row 32 -------------------------
					row = sheet.getRow(31);
					// Column 1 - product name
					
					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR32_month() != null) {
						cellC.setCellValue(record.getR32_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR32_ytd() != null) {
						cellD.setCellValue(record.getR32_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// ------------------------- Row 33 -------------------------
					row = sheet.getRow(32);
					
					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR33_month() != null) {
						cellC.setCellValue(record.getR33_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR33_ytd() != null) {
						cellD.setCellValue(record.getR33_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// ------------------------- Row 34 -------------------------
					row = sheet.getRow(33);
					
					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR34_month() != null) {
						cellC.setCellValue(record.getR34_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR34_ytd() != null) {
						cellD.setCellValue(record.getR34_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// ------------------------- Row 35 -------------------------
					row = sheet.getRow(34);
					
					cellC = row.createCell(2);
					if (record.getR35_month() != null) {
						cellC.setCellValue(record.getR35_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					cellD = row.createCell(3);
					if (record.getR35_ytd() != null) {
						cellD.setCellValue(record.getR35_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// ------------------------- Row 36 -------------------------
					row = sheet.getRow(35);

					
					cellC = row.createCell(2);
					if (record.getR36_month() != null) {
						cellC.setCellValue(record.getR36_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					cellD = row.createCell(3);
					if (record.getR36_ytd() != null) {
						cellD.setCellValue(record.getR36_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					

					// ------------------------- Row 38 -------------------------
					row = sheet.getRow(37);

					

					cellC = row.createCell(2);
					if (record.getR38_month() != null) {
						cellC.setCellValue(record.getR38_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR38_ytd() != null) {
						cellD.setCellValue(record.getR38_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}
               
					// row39
					row = sheet.getRow(38);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR39_month() != null) {
					    cellC.setCellValue(record.getR39_month().doubleValue()); // assuming it's BigDecimal or Double
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR39_ytd() != null) {
					    cellD.setCellValue(record.getR39_ytd().doubleValue()); // assuming it's BigDecimal or Double
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					

					
					
					// row42
					row = sheet.getRow(41);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR42_month() != null) {
					    cellC.setCellValue(record.getR42_month().doubleValue()); // assuming it's BigDecimal or Double
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR42_ytd() != null) {
					    cellD.setCellValue(record.getR42_ytd().doubleValue()); // assuming it's BigDecimal or Double
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row43
					row = sheet.getRow(42);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR43_month() != null) {
					    cellC.setCellValue(record.getR43_month().doubleValue()); // assuming it's BigDecimal or Double
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR43_ytd() != null) {
					    cellD.setCellValue(record.getR43_ytd().doubleValue()); // assuming it's BigDecimal or Double
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

				

					// row45
					row = sheet.getRow(44);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR45_month() != null) {
					    cellC.setCellValue(record1.getR45_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR45_ytd() != null) {
					    cellD.setCellValue(record.getR45_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row46
					row = sheet.getRow(45);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR46_month() != null) {
					    cellC.setCellValue(record1.getR46_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR46_ytd() != null) {
					    cellD.setCellValue(record.getR46_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR47_month() != null) {
					    cellC.setCellValue(record.getR47_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR47_ytd() != null) {
					    cellD.setCellValue(record.getR47_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					

					

					// row50
					row = sheet.getRow(49);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR50_month() != null) {
					    cellC.setCellValue(record.getR50_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR50_ytd() != null) {
					    cellD.setCellValue(record.getR50_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row51
					row = sheet.getRow(50);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR51_month() != null) {
					    cellC.setCellValue(record.getR51_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR51_ytd() != null) {
					    cellD.setCellValue(record.getR51_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR52_month() != null) {
					    cellC.setCellValue(record.getR52_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR52_ytd() != null) {
					    cellD.setCellValue(record.getR52_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR53_month() != null) {
					    cellC.setCellValue(record.getR53_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR53_ytd() != null) {
					    cellD.setCellValue(record.getR53_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR54_month() != null) {
					    cellC.setCellValue(record1.getR54_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR54_ytd() != null) {
					    cellD.setCellValue(record.getR54_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR55_month() != null) {
					    cellC.setCellValue(record.getR55_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR55_ytd() != null) {
					    cellD.setCellValue(record.getR55_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					

					

					// row58
					row = sheet.getRow(57);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR58_month() != null) {
					    cellC.setCellValue(record1.getR58_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR58_ytd() != null) {
					    cellD.setCellValue(record.getR58_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR59_month() != null) {
					    cellC.setCellValue(record1.getR59_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR59_ytd() != null) {
					    cellD.setCellValue(record.getR59_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row60
					row = sheet.getRow(59);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR60_month() != null) {
					    cellC.setCellValue(record1.getR60_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR60_ytd() != null) {
					    cellD.setCellValue(record.getR60_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					
					
					
					// row63
					row = sheet.getRow(62);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR63_month() != null) {
					    cellC.setCellValue(record.getR63_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR63_ytd() != null) {
					    cellD.setCellValue(record.getR63_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row64
					row = sheet.getRow(63);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR64_month() != null) {
					    cellC.setCellValue(record.getR64_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR64_ytd() != null) {
					    cellD.setCellValue(record.getR64_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row65
					row = sheet.getRow(64);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR65_month() != null) {
					    cellC.setCellValue(record.getR65_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR65_ytd() != null) {
					    cellD.setCellValue(record.getR65_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row66
					row = sheet.getRow(65);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR66_month() != null) {
					    cellC.setCellValue(record1.getR66_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR66_ytd() != null) {
					    cellD.setCellValue(record.getR66_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row67
					row = sheet.getRow(66);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR67_month() != null) {
					    cellC.setCellValue(record1.getR67_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR67_ytd() != null) {
					    cellD.setCellValue(record.getR67_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row68
					row = sheet.getRow(67);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR68_month() != null) {
					    cellC.setCellValue(record1.getR68_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR68_ytd() != null) {
					    cellD.setCellValue(record.getR68_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row69
					row = sheet.getRow(68);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR69_month() != null) {
					    cellC.setCellValue(record.getR69_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR69_ytd() != null) {
					    cellD.setCellValue(record.getR69_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					

					// row71
					row = sheet.getRow(70);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR71_month() != null) {
					    cellC.setCellValue(record.getR71_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR71_ytd() != null) {
					    cellD.setCellValue(record.getR71_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row72
					row = sheet.getRow(71);

				

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR72_month() != null) {
					    cellC.setCellValue(record.getR72_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR72_ytd() != null) {
					    cellD.setCellValue(record.getR72_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row73
					row = sheet.getRow(72);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR73_month() != null) {
					    cellC.setCellValue(record.getR73_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR73_ytd() != null) {
					    cellD.setCellValue(record.getR73_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row74
					row = sheet.getRow(73);

					
					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR74_month() != null) {
					    cellC.setCellValue(record1.getR74_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR74_ytd() != null) {
					    cellD.setCellValue(record.getR74_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					

					// row76
					row = sheet.getRow(75);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR76_month() != null) {
					    cellC.setCellValue(record.getR76_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR76_ytd() != null) {
					    cellD.setCellValue(record.getR76_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row77
					row = sheet.getRow(76);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR77_month() != null) {
					    cellC.setCellValue(record.getR77_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR77_ytd() != null) {
					    cellD.setCellValue(record.getR77_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row78
					row = sheet.getRow(77);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR78_month() != null) {
					    cellC.setCellValue(record.getR78_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR78_ytd() != null) {
					    cellD.setCellValue(record.getR78_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row79
					row = sheet.getRow(78);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR79_month() != null) {
					    cellC.setCellValue(record.getR79_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR79_ytd() != null) {
					    cellD.setCellValue(record.getR79_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row80
					row = sheet.getRow(79);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR80_month() != null) {
					    cellC.setCellValue(record.getR80_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR80_ytd() != null) {
					    cellD.setCellValue(record.getR80_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					

					

					// row83
					row = sheet.getRow(82);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR83_month() != null) {
					    cellC.setCellValue(record.getR83_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR83_ytd() != null) {
					    cellD.setCellValue(record.getR83_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					
					
					// row85
					row = sheet.getRow(84);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR85_month() != null) {
					    cellC.setCellValue(record1.getR85_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR85_ytd() != null) {
					    cellD.setCellValue(record.getR85_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
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

	
	public byte[] getExcelM_SCI_EARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}
		
		List<M_SCI_E_Archival_Summary_Entity> dataList = brrs_m_sci_e_Archival_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_SCI_E_Archival_Manual_Summary_Entity> dataList1 = m_sci_e_manual_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);


		

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SCI_E report. Returning empty result.");
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

			int startRow = 10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SCI_E_Archival_Summary_Entity record = dataList.get(i);
					M_SCI_E_Archival_Manual_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

Cell cellC,cellD;    
					
					// row11
					
					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR11_month() != null) {
						cellC.setCellValue(record.getR11_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR11_ytd() != null) {
						cellD.setCellValue(record.getR11_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column 1 - product name
					

					// Column 2 - cross_reference
					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR12_month() != null) {
						cellC.setCellValue(record.getR12_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR12_ytd() != null) {
						cellD.setCellValue(record.getR12_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column 1 - product name
					

					// Column 2 - cross_reference
					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR13_month() != null) {
						cellC.setCellValue(record.getR13_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR13_ytd() != null) {
						cellD.setCellValue(record.getR13_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					

					// row15
					row = sheet.getRow(14); // Row index for R15 (0-based index)

					// Column 1 - product name
					

					// Column 2 - cross_reference
					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR15_month() != null) {
						cellC.setCellValue(record.getR15_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR15_ytd() != null) {
						cellD.setCellValue(record.getR15_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR16_month() != null) {
						cellC.setCellValue(record.getR16_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR16_ytd() != null) {
						cellD.setCellValue(record.getR16_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					

					cellC = row.createCell(2);
					if (record.getR17_month() != null) {
						cellC.setCellValue(record.getR17_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR17_ytd() != null) {
						cellD.setCellValue(record.getR17_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					

					cellC = row.createCell(2);
					if (record.getR18_month() != null) {
						cellC.setCellValue(record.getR18_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR18_ytd() != null) {
						cellD.setCellValue(record.getR18_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					

					cellC = row.createCell(2);
					if (record.getR19_month() != null) {
						cellC.setCellValue(record.getR19_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR19_ytd() != null) {
						cellD.setCellValue(record.getR19_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					

					cellC = row.createCell(2);
					if (record.getR20_month() != null) {
						cellC.setCellValue(record.getR20_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR20_ytd() != null) {
						cellD.setCellValue(record.getR20_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);

					

					cellC = row.createCell(2);
					if (record.getR21_month() != null) {
						cellC.setCellValue(record.getR21_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR21_ytd() != null) {
						cellD.setCellValue(record.getR21_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					

					cellC = row.createCell(2);
					if (record.getR22_month() != null) {
						cellC.setCellValue(record.getR22_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR22_ytd() != null) {
						cellD.setCellValue(record.getR22_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					

					cellC = row.createCell(2);
					if (record.getR23_month() != null) {
						cellC.setCellValue(record.getR23_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR23_ytd() != null) {
						cellD.setCellValue(record.getR23_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					

					cellC = row.createCell(2);
					if (record.getR24_month() != null) {
						cellC.setCellValue(record.getR24_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR24_ytd() != null) {
						cellD.setCellValue(record.getR24_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);

					

					cellC = row.createCell(2);
					if (record.getR25_month() != null) {
						cellC.setCellValue(record.getR25_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR25_ytd() != null) {
						cellD.setCellValue(record.getR25_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					// Column 1 - product name
					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR26_month() != null) {
						cellC.setCellValue(record.getR26_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR26_ytd() != null) {
						cellD.setCellValue(record.getR26_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					
					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR27_month() != null) {
						cellC.setCellValue(record.getR27_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR27_ytd() != null) {
						cellD.setCellValue(record.getR27_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					

					// row29
					row = sheet.getRow(28);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR29_month() != null) {
						cellC.setCellValue(record.getR29_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR29_ytd() != null) {
						cellD.setCellValue(record.getR29_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR30_month() != null) {
						cellC.setCellValue(record.getR30_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR30_ytd() != null) {
						cellD.setCellValue(record.getR30_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);
					
					
					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR31_month() != null) {
						cellC.setCellValue(record.getR31_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR31_ytd() != null) {
						cellD.setCellValue(record.getR31_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// ------------------------- Row 32 -------------------------
					row = sheet.getRow(31);
					// Column 1 - product name
					
					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR32_month() != null) {
						cellC.setCellValue(record.getR32_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR32_ytd() != null) {
						cellD.setCellValue(record.getR32_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// ------------------------- Row 33 -------------------------
					row = sheet.getRow(32);
					
					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR33_month() != null) {
						cellC.setCellValue(record.getR33_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR33_ytd() != null) {
						cellD.setCellValue(record.getR33_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// ------------------------- Row 34 -------------------------
					row = sheet.getRow(33);
					
					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR34_month() != null) {
						cellC.setCellValue(record.getR34_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR34_ytd() != null) {
						cellD.setCellValue(record.getR34_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// ------------------------- Row 35 -------------------------
					row = sheet.getRow(34);
					
					cellC = row.createCell(2);
					if (record.getR35_month() != null) {
						cellC.setCellValue(record.getR35_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					cellD = row.createCell(3);
					if (record.getR35_ytd() != null) {
						cellD.setCellValue(record.getR35_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// ------------------------- Row 36 -------------------------
					row = sheet.getRow(35);

					
					cellC = row.createCell(2);
					if (record.getR36_month() != null) {
						cellC.setCellValue(record.getR36_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					cellD = row.createCell(3);
					if (record.getR36_ytd() != null) {
						cellD.setCellValue(record.getR36_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					

					// ------------------------- Row 38 -------------------------
					row = sheet.getRow(37);

					

					cellC = row.createCell(2);
					if (record.getR38_month() != null) {
						cellC.setCellValue(record.getR38_month().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellD = row.createCell(3);
					if (record.getR38_ytd() != null) {
						cellD.setCellValue(record.getR38_ytd().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}
               
					// row39
					row = sheet.getRow(38);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR39_month() != null) {
					    cellC.setCellValue(record.getR39_month().doubleValue()); // assuming it's BigDecimal or Double
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR39_ytd() != null) {
					    cellD.setCellValue(record.getR39_ytd().doubleValue()); // assuming it's BigDecimal or Double
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					

					
					
					// row42
					row = sheet.getRow(41);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR42_month() != null) {
					    cellC.setCellValue(record.getR42_month().doubleValue()); // assuming it's BigDecimal or Double
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR42_ytd() != null) {
					    cellD.setCellValue(record.getR42_ytd().doubleValue()); // assuming it's BigDecimal or Double
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row43
					row = sheet.getRow(42);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR43_month() != null) {
					    cellC.setCellValue(record.getR43_month().doubleValue()); // assuming it's BigDecimal or Double
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR43_ytd() != null) {
					    cellD.setCellValue(record.getR43_ytd().doubleValue()); // assuming it's BigDecimal or Double
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

				

					// row45
					row = sheet.getRow(44);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR45_month() != null) {
					    cellC.setCellValue(record1.getR45_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR45_ytd() != null) {
					    cellD.setCellValue(record.getR45_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row46
					row = sheet.getRow(45);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR46_month() != null) {
					    cellC.setCellValue(record1.getR46_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR46_ytd() != null) {
					    cellD.setCellValue(record.getR46_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR47_month() != null) {
					    cellC.setCellValue(record.getR47_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR47_ytd() != null) {
					    cellD.setCellValue(record.getR47_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					

					

					// row50
					row = sheet.getRow(49);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR50_month() != null) {
					    cellC.setCellValue(record.getR50_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR50_ytd() != null) {
					    cellD.setCellValue(record.getR50_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row51
					row = sheet.getRow(50);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR51_month() != null) {
					    cellC.setCellValue(record.getR51_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR51_ytd() != null) {
					    cellD.setCellValue(record.getR51_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR52_month() != null) {
					    cellC.setCellValue(record.getR52_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR52_ytd() != null) {
					    cellD.setCellValue(record.getR52_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR53_month() != null) {
					    cellC.setCellValue(record.getR53_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR53_ytd() != null) {
					    cellD.setCellValue(record.getR53_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR54_month() != null) {
					    cellC.setCellValue(record1.getR54_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR54_ytd() != null) {
					    cellD.setCellValue(record.getR54_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR55_month() != null) {
					    cellC.setCellValue(record.getR55_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR55_ytd() != null) {
					    cellD.setCellValue(record.getR55_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					

					

					// row58
					row = sheet.getRow(57);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR58_month() != null) {
					    cellC.setCellValue(record1.getR58_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR58_ytd() != null) {
					    cellD.setCellValue(record.getR58_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR59_month() != null) {
					    cellC.setCellValue(record1.getR59_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR59_ytd() != null) {
					    cellD.setCellValue(record.getR59_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row60
					row = sheet.getRow(59);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR60_month() != null) {
					    cellC.setCellValue(record1.getR60_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR60_ytd() != null) {
					    cellD.setCellValue(record.getR60_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					
					
					
					// row63
					row = sheet.getRow(62);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR63_month() != null) {
					    cellC.setCellValue(record.getR63_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR63_ytd() != null) {
					    cellD.setCellValue(record.getR63_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row64
					row = sheet.getRow(63);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR64_month() != null) {
					    cellC.setCellValue(record.getR64_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR64_ytd() != null) {
					    cellD.setCellValue(record.getR64_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row65
					row = sheet.getRow(64);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR65_month() != null) {
					    cellC.setCellValue(record.getR65_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR65_ytd() != null) {
					    cellD.setCellValue(record.getR65_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row66
					row = sheet.getRow(65);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR66_month() != null) {
					    cellC.setCellValue(record1.getR66_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR66_ytd() != null) {
					    cellD.setCellValue(record.getR66_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row67
					row = sheet.getRow(66);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR67_month() != null) {
					    cellC.setCellValue(record1.getR67_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR67_ytd() != null) {
					    cellD.setCellValue(record.getR67_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row68
					row = sheet.getRow(67);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR68_month() != null) {
					    cellC.setCellValue(record1.getR68_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR68_ytd() != null) {
					    cellD.setCellValue(record.getR68_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row69
					row = sheet.getRow(68);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR69_month() != null) {
					    cellC.setCellValue(record.getR69_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR69_ytd() != null) {
					    cellD.setCellValue(record.getR69_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					

					// row71
					row = sheet.getRow(70);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR71_month() != null) {
					    cellC.setCellValue(record.getR71_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR71_ytd() != null) {
					    cellD.setCellValue(record.getR71_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row72
					row = sheet.getRow(71);

				

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR72_month() != null) {
					    cellC.setCellValue(record.getR72_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR72_ytd() != null) {
					    cellD.setCellValue(record.getR72_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row73
					row = sheet.getRow(72);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR73_month() != null) {
					    cellC.setCellValue(record.getR73_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR73_ytd() != null) {
					    cellD.setCellValue(record.getR73_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row74
					row = sheet.getRow(73);

					
					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR74_month() != null) {
					    cellC.setCellValue(record1.getR74_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR74_ytd() != null) {
					    cellD.setCellValue(record.getR74_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					

					// row76
					row = sheet.getRow(75);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR76_month() != null) {
					    cellC.setCellValue(record.getR76_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR76_ytd() != null) {
					    cellD.setCellValue(record.getR76_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row77
					row = sheet.getRow(76);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR77_month() != null) {
					    cellC.setCellValue(record.getR77_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR77_ytd() != null) {
					    cellD.setCellValue(record.getR77_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row78
					row = sheet.getRow(77);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR78_month() != null) {
					    cellC.setCellValue(record.getR78_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR78_ytd() != null) {
					    cellD.setCellValue(record.getR78_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row79
					row = sheet.getRow(78);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR79_month() != null) {
					    cellC.setCellValue(record.getR79_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR79_ytd() != null) {
					    cellD.setCellValue(record.getR79_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row80
					row = sheet.getRow(79);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR80_month() != null) {
					    cellC.setCellValue(record.getR80_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR80_ytd() != null) {
					    cellD.setCellValue(record.getR80_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					

					

					// row83
					row = sheet.getRow(82);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record.getR83_month() != null) {
					    cellC.setCellValue(record.getR83_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR83_ytd() != null) {
					    cellD.setCellValue(record.getR83_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					
					
					// row85
					row = sheet.getRow(84);

					

					// Column 3 - month
					cellC = row.createCell(2);
					if (record1.getR85_month() != null) {
					    cellC.setCellValue(record1.getR85_month().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - ytd
					cellD = row.createCell(3);
					if (record.getR85_ytd() != null) {
					    cellD.setCellValue(record.getR85_ytd().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
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
	
	

	
	
	
	
	
	
	
	
	
	
	public List<Object> getM_SCI_EArchival() {
		List<Object> M_SCI_EArchivallist = new ArrayList<>();
		try {
			M_SCI_EArchivallist = brrs_m_sci_e_Archival_summary_repo.getM_SCI_Earchival();
			M_SCI_EArchivallist = m_sci_e_manual_Archival_Summary_Repo.getM_SCI_Earchival();
			System.out.println("countser" + M_SCI_EArchivallist.size());
			System.out.println("countser" + M_SCI_EArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_SCI_EArchivallist Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_SCI_EArchivallist;
	}
	
	
	
	public byte[] getM_SCI_EDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_SCI_E Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getM_SCI_EDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_SCI_EDetails");

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
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME","MONTHLY_INT", "BALANCE_AMT", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) {  // MONTHLY_INT (3) and BALANCE_AMT (4)
				    cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
				    cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_SCI_E_Detail_Entity> reportData = brrs_m_sci_e_detail_repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SCI_E_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// MONTHLY_INT (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getMonthlyInt() != null) {
						balanceCell.setCellValue(item.getMonthlyInt().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
					}
					balanceCell.setCellStyle(balanceStyle);
					
					// BALANCE_AMT (right aligned, 3 decimal places)
					 balanceCell = row.createCell(4);
					if (item.getBalanceAmt() != null) {
						balanceCell.setCellValue(item.getBalanceAmt().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
					}
					balanceCell.setCellStyle(balanceStyle);

					

					row.createCell(5).setCellValue(item.getReportLable());
					row.createCell(6).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(7)
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
				logger.info("No data found for M_SCI_E — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_SCI_E Excel", e);
			return new byte[0];
		}
	}
	
	
	
	
	
	public byte[] getM_SCI_EDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for M_SCI_E ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_SCI_EDetail");

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
			List<M_SCI_E_Archival_Detail_Entity> reportData = m_sci_e_Archival_Detail_Repo.getdatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SCI_E_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for M_SCI_E — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_SCI_EExcel", e);
			return new byte[0];
		}
	}
	
	
	
	public void updateReport(M_SCI_E_Manual_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    //  Use your query to fetch by date
	    List<M_SCI_E_Manual_Summary_Entity> list = brrs_m_sci_e_manual_summary_repo
	        .getdatabydateList(updatedEntity.getReport_date());

	    M_SCI_E_Manual_Summary_Entity existing;
	    if (list.isEmpty()) {
	        // Record not found — optionally create it
	        System.out.println("No record found for REPORT_DATE: " + updatedEntity.getReport_date());
	        existing = new M_SCI_E_Manual_Summary_Entity();
	        existing.setReport_date(updatedEntity.getReport_date());
	    } else {
	        existing = list.get(0);
	    }

	    try {
	        //  Only for specific row numbers
	        int[] rows = {45, 46, 54, 58, 59, 60, 66, 67, 68, 74, 85};

	        for (int row : rows) {
	            String prefix = "R" + row + "_";

	            // Fields to update
	            String[] fields = {"month"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field; // e.g. getR45_month
	                String setterName = "set" + prefix + field; // e.g. setR45_month

	                try {
	                    Method getter = M_SCI_E_Manual_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_SCI_E_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields gracefully
	                    continue;
	                }
	            }
	        }

	        // Metadata
	        existing.setReport_version(updatedEntity.getReport_version());
	        existing.setReport_frequency(updatedEntity.getReport_frequency());
	        existing.setReport_code(updatedEntity.getReport_code());
	        existing.setReport_desc(updatedEntity.getReport_desc());
	        existing.setEntity_flg(updatedEntity.getEntity_flg());
	        existing.setModify_flg(updatedEntity.getModify_flg());
	        existing.setDel_flg(updatedEntity.getDel_flg());

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating M_SCI_E Summary fields", e);
	    }

	    //  FIRST COMMIT — forces immediate commit
	    brrs_m_sci_e_manual_summary_repo.saveAndFlush(existing);
	    System.out.println("M_SCI_E Summary updated and COMMITTED");

	    //  Execute procedure with updated data
	    String oracleDate = new SimpleDateFormat("dd-MM-yyyy")
	            .format(updatedEntity.getReport_date())
	            .toUpperCase();

	    String sql = "BEGIN BRRS.BRRS_M_SCI_E_SUMMARY_PROCEDURE ('" + oracleDate + "'); END;";
	    jdbcTemplate.execute(sql);

	    System.out.println("Procedure executed for date: " + oracleDate);
	}

	 @Autowired BRRS_M_SCI_E_Detail_Repo m_sci_e_detail_repo;
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_SCI_E"); 

		if (acctNo != null) {
			M_SCI_E_Detail_Entity msciEntity = m_sci_e_detail_repo.findByAcctnumber(acctNo);
			if (msciEntity != null && msciEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(msciEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("msciData", msciEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String monthlyInt = request.getParameter("monthlyInt");
			String balanceAmt = request.getParameter("balanceAmt");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			M_SCI_E_Detail_Entity existing = m_sci_e_detail_repo.findByAcctnumber(acctNo);
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

			 if (monthlyInt != null && !monthlyInt.isEmpty()) {
		            BigDecimal newmonthlyInt = new BigDecimal(monthlyInt);
		            if (existing.getMonthlyInt()  == null ||
		                existing.getMonthlyInt().compareTo(newmonthlyInt) != 0) {
		            	 existing.setMonthlyInt(newmonthlyInt);
		                isChanged = true;
		                logger.info("Balance updated to {}", newmonthlyInt);
		            }
		        }
		        
			 if (balanceAmt != null && !balanceAmt.isEmpty()) {
		            BigDecimal newbalanceAmt = new BigDecimal(balanceAmt);
		            if (existing.getBalanceAmt()  == null ||
		                existing.getBalanceAmt().compareTo(newbalanceAmt) != 0) {
		            	 existing.setBalanceAmt(newbalanceAmt);
		                isChanged = true;
		                logger.info("Balance updated to {}", newbalanceAmt);
		            }
		        }
			 
			if (isChanged) {
				m_sci_e_detail_repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_M_SCI_E_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_M_SCI_E_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating M_SCI_E record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	
	
	
	
	
	

}