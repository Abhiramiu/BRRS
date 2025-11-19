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
import org.apache.poi.ss.usermodel.DataFormat;
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

import com.bornfire.brrs.entities.BRRS_M_CA2_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA2_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA2_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA2_Manual_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA2_Manual_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA2_Summary_Repo;
import com.bornfire.brrs.entities.M_CA2_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_CA2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CA2_Detail_Entity;
import com.bornfire.brrs.entities.M_CA2_Manual_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CA2_Manual_Summary_Entity;
import com.bornfire.brrs.entities.M_CA2_Summary_Entity;

@Component
@Service


public class BRRS_M_CA2_ReportService {
private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA2_ReportService.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	BRRS_M_CA2_Detail_Repo BRRS_M_CA2_Detail_Repo;
	
	@Autowired
	BRRS_M_CA2_Summary_Repo BRRS_M_CA2_Summary_Repo;
	
	@Autowired
	BRRS_M_CA2_Archival_Detail_Repo BRRS_M_CA2_Archival_Detail_Repo;

	@Autowired
	BRRS_M_CA2_Archival_Summary_Repo BRRS_M_CA2_Archival_Summary_Repo;
	
	@Autowired
	BRRS_M_CA2_Manual_Archival_Summary_Repo BRRS_M_CA2_Manual_Archival_Summary_Repo;
	
	@Autowired
	BRRS_M_CA2_Manual_Summary_Repo BRRS_M_CA2_Manual_Summary_Repo;
	
	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	public ModelAndView getM_CA2View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		/*Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;	*/

		System.out.println("testing");
		System.out.println(version);

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_CA2_Archival_Summary_Entity> T1Master = new ArrayList<M_CA2_Archival_Summary_Entity>();
			List<M_CA2_Manual_Archival_Summary_Entity> T2Master = new ArrayList<M_CA2_Manual_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				//Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_CA2_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = BRRS_M_CA2_Manual_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		} else {		

		List<M_CA2_Summary_Entity> T1Master = new ArrayList<M_CA2_Summary_Entity>();
		List<M_CA2_Manual_Summary_Entity> T2Master = new ArrayList<M_CA2_Manual_Summary_Entity>();
		
		try {
			//Date d1 = dateformat.parse(todate);
			// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

			//T1Master = hs.createQuery("from  BRF1_REPORT_ENTITY a where a.report_date = ?1 ", BRF1_REPORT_ENTITY.class)
				//	.setParameter(1, df.parse(todate)).getResultList();
			 T1Master=BRRS_M_CA2_Summary_Repo.getdatabydateList(dateformat.parse(todate));
			 T2Master=BRRS_M_CA2_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));
			 
		
		} catch (ParseException e) {
			e.printStackTrace();
		}	
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_CA2");
		
		//mv.addObject("reportsummary", T1Master);
		//mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		//mv.addObject("reportsflag", "reportsflag");
		//mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;
		}
		public ModelAndView getM_CA2currentDtl(String reportId, String fromdate, String todate, String currency,
				  String dtltype, Pageable pageable, String Filter, String type, String version) {

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
				List<M_CA2_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_CA2_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
				} else {
					T1Dt1 = BRRS_M_CA2_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_CA2_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_CA2_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = BRRS_M_CA2_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
					totalPages = BRRS_M_CA2_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/M_CA2");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

		
		public void updateReport(M_CA2_Manual_Summary_Entity updatedEntity) {
		    System.out.println("Came to services1");
		    System.out.println("Report Date: " + updatedEntity.getReport_date());

		    M_CA2_Manual_Summary_Entity existing = BRRS_M_CA2_Manual_Summary_Repo.findById(updatedEntity.getReport_date())
		            .orElseThrow(() -> new RuntimeException(
		                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		    try {
		        // ✅ Loop for amount_2 fields
		        int[] amount2Rows = {11, 32, 42, 43, 44, 46};
		        for (int i : amount2Rows) {
		            String prefix = "R" + i + "_";
		            String[] fields = {"amount_2"};

		            for (String field : fields) {
		                try {
		                    String getterName = "get" + prefix + field;
		                    String setterName = "set" + prefix + field;

		                    Method getter = M_CA2_Manual_Summary_Entity.class.getMethod(getterName);
		                    Method setter = M_CA2_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

		                    Object newValue = getter.invoke(updatedEntity);
		                    setter.invoke(existing, newValue);

		                } catch (NoSuchMethodException e) {
		                    // Skip missing getter/setter gracefully
		                    continue;
		                }
		            }
		        }

		        // ✅ Loop for amount_1 fields
		        int[] amount1Rows = {13, 14, 15, 16, 18, 19, 21};
		        for (int i : amount1Rows) {
		            String prefix = "R" + i + "_";
		            String[] fields = {"amount_1"};

		            for (String field : fields) {
		                try {
		                    String getterName = "get" + prefix + field;
		                    String setterName = "set" + prefix + field;

		                    Method getter = M_CA2_Manual_Summary_Entity.class.getMethod(getterName);
		                    Method setter = M_CA2_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

		                    Object newValue = getter.invoke(updatedEntity);
		                    setter.invoke(existing, newValue);

		                } catch (NoSuchMethodException e) {
		                    continue;
		                }
		            }
		        }

		        // ✅ Save after all updates
		        BRRS_M_CA2_Manual_Summary_Repo.save(existing);

		    } catch (Exception e) {
		        throw new RuntimeException("Error while updating report fields", e);
		    }
		}

		
	public byte[] getM_CA2Excel(String filename, String reportId, String fromdate, String todate, String currency,
									 String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_CA2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<M_CA2_Summary_Entity> dataList =BRRS_M_CA2_Summary_Repo.getdatabydateList(dateformat.parse(todate)) ;
		List<M_CA2_Manual_Summary_Entity> dataList1 =BRRS_M_CA2_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate)) ;

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA2 report. Returning empty result.");
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
			font.setFontHeightInPoints((short)8); // size 8
			font.setFontName("Arial");    

			CellStyle numberStyle = workbook.createCellStyle();
			//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

			int startRow = 9;

			if (!dataList.isEmpty() || !dataList1.isEmpty()) {
			    for (int i = 0; i < dataList.size(); i++) {
			        M_CA2_Summary_Entity record = dataList.get(i);
			        M_CA2_Manual_Summary_Entity record1 = dataList1.get(i);
			        System.out.println("rownumber=" + (startRow + i));

			        Row row;
			        Cell cell3, cell4;
			        CellStyle originalStyle;

			        // ===== Row 10 / Col D =====
			        row = sheet.getRow(9);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR10_amount_2() != null) cell4.setCellValue(record.getR10_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 11 / Col D =====
			        row = sheet.getRow(10);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR11_amount_2() != null) cell4.setCellValue(record1.getR11_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 13 / Col C =====
			        row = sheet.getRow(12);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR13_amount_1() != null) cell3.setCellValue(record1.getR13_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 14 / Col C =====
			        row = sheet.getRow(13);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR14_amount_1() != null) cell3.setCellValue(record1.getR14_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 15 / Col C =====
			        row = sheet.getRow(14);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR15_amount_1() != null) cell3.setCellValue(record1.getR15_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 16 / Col C =====
			        row = sheet.getRow(15);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR16_amount_1() != null) cell3.setCellValue(record1.getR16_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 18 / Col C =====
			        row = sheet.getRow(17);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR18_amount_1() != null) cell3.setCellValue(record1.getR18_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 19 / Col C =====
			        row = sheet.getRow(18);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR19_amount_1() != null) cell3.setCellValue(record1.getR19_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 20 / Col C =====
			        row = sheet.getRow(19);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record.getR20_amount_1() != null) cell3.setCellValue(record.getR20_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 21 / Col C =====
			        row = sheet.getRow(20);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR21_amount_1() != null) cell3.setCellValue(record1.getR21_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 22 / Col D =====
			        row = sheet.getRow(21);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR22_amount_2() != null) cell4.setCellValue(record.getR22_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 23 / Col D =====
			        row = sheet.getRow(22);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR23_amount_2() != null) cell4.setCellValue(record.getR23_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 24 / Col D =====
			        row = sheet.getRow(23);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR24_amount_2() != null) cell4.setCellValue(record.getR24_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 25 / Col D =====
			        row = sheet.getRow(24);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR25_amount_2() != null) cell4.setCellValue(record.getR25_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 31 / Col D =====
			        row = sheet.getRow(30);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR31_amount_2() != null) cell4.setCellValue(record.getR31_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 32 / Col D =====
			        row = sheet.getRow(31);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR32_amount_2() != null) cell4.setCellValue(record1.getR32_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 33 / Col D =====
			        row = sheet.getRow(32);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR33_amount_2() != null) cell4.setCellValue(record.getR33_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 34 / Col D =====
			        row = sheet.getRow(33);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR34_amount_2() != null) cell4.setCellValue(record.getR34_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 41 / Col D =====
			        row = sheet.getRow(40);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR41_amount_2() != null) cell4.setCellValue(record.getR41_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 42 / Col D =====
			        row = sheet.getRow(41);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR42_amount_2() != null) cell4.setCellValue(record1.getR42_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 43 / Col D =====
			        row = sheet.getRow(42);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR43_amount_2() != null) cell4.setCellValue(record1.getR43_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 44 / Col D =====
			        row = sheet.getRow(43);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR44_amount_2() != null) cell4.setCellValue(record1.getR44_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 45 / Col D =====
			        row = sheet.getRow(44);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR45_amount_2() != null) cell4.setCellValue(record.getR45_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 46 / Col D =====
			        row = sheet.getRow(45);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR46_amount_2() != null) cell4.setCellValue(record1.getR46_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 47 / Col D =====
			        row = sheet.getRow(46);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR47_amount_2() != null) cell4.setCellValue(record.getR47_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);	 
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
	
	public byte[] getM_CA2DetailExcel(String filename, String fromdate, String todate, String currency,
			   String dtltype, String type, String version) {
try {
logger.info("Generating Excel for M_CA2 Details...");
System.out.println("came to Detail download service");


if (type.equals("ARCHIVAL") & version != null) {
byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
version);
return ARCHIVALreport;
}

XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("M_CA2Detail");

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

//ACCT BALANCE style (right aligned with thousand separator)
CellStyle balanceStyle = workbook.createCellStyle();
balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
balanceStyle.setBorderTop(border);
balanceStyle.setBorderBottom(border);
balanceStyle.setBorderLeft(border);
balanceStyle.setBorderRight(border);






// Header row
String[] headers = {
"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "ROWID", "COLUMNID", "REPORT_DATE"
};

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
List<M_CA2_Detail_Entity> reportData = BRRS_M_CA2_Detail_Repo.getdatabydateList(parsedToDate);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (M_CA2_Detail_Entity item : reportData) {
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
	logger.info("No data found for M_CA2 — only header will be written.");
}

// Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating M_CA2 Excel", e);
return new byte[0];
}
}
//	public byte[] getM_CA2DetailExcel(String filename, String fromdate, String todate, String currency,
//										   String dtltype, String type, String version) {
//	    try {
//	        logger.info("Generating Excel for M_CA2 Details...");
//	        System.out.println("came to Detail download service");
//
//			if (type.equals("ARCHIVAL") & version != null) {
//				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
//						version);
//				return ARCHIVALreport;
//			}
//
//	        XSSFWorkbook workbook = new XSSFWorkbook();
//	        XSSFSheet sheet = workbook.createSheet("M_CA2Details");
//
//	        // Common border style
//	        BorderStyle border = BorderStyle.THIN;
//
//	        // Header style (left aligned)
//	        CellStyle headerStyle = workbook.createCellStyle();
//	        Font headerFont = workbook.createFont();
//	        headerFont.setBold(true);
//	        headerFont.setFontHeightInPoints((short) 10);
//	        headerStyle.setFont(headerFont);
//	        headerStyle.setAlignment(HorizontalAlignment.LEFT);
//	        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//	        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//	        headerStyle.setBorderTop(border);
//	        headerStyle.setBorderBottom(border);
//	        headerStyle.setBorderLeft(border);
//	        headerStyle.setBorderRight(border);
//
//	        // Right-aligned header style for ACCT BALANCE
//	        CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
//	        rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
//	        rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
//
//	        // Default data style (left aligned)
//	        CellStyle dataStyle = workbook.createCellStyle();
//	        dataStyle.setAlignment(HorizontalAlignment.LEFT);
//	        dataStyle.setBorderTop(border);
//	        dataStyle.setBorderBottom(border);
//	        dataStyle.setBorderLeft(border);
//	        dataStyle.setBorderRight(border);
//
//	        // ACCT BALANCE style (right aligned with 3 decimals)
//	        CellStyle balanceStyle = workbook.createCellStyle();
//	        balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
//	        balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
//	        balanceStyle.setBorderTop(border);
//	        balanceStyle.setBorderBottom(border);
//	        balanceStyle.setBorderLeft(border);
//	        balanceStyle.setBorderRight(border);
//
//	        // Header row
//	        String[] headers = {
//	            "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID", "REPORT_DATE"
//	        };
//
//	        XSSFRow headerRow = sheet.createRow(0);
//	        for (int i = 0; i < headers.length; i++) {
//	            Cell cell = headerRow.createCell(i);
//	            cell.setCellValue(headers[i]);
//
//	            if (i == 3) { // ACCT BALANCE
//	                cell.setCellStyle(rightAlignedHeaderStyle);
//	            } else {
//	                cell.setCellStyle(headerStyle);
//	            }
//
//	            sheet.setColumnWidth(i, 5000);
//	        }
//
//	        // Get data
//	        Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
//	        List<M_CA2_Detail_Entity> reportData = BRRS_M_CA2_Detail_Repo.getdatabydateList(parsedToDate);
//
//	        if (reportData != null && !reportData.isEmpty()) {
//	            int rowIndex = 1;
//	            for (M_CA2_Detail_Entity item : reportData) {
//	                XSSFRow row = sheet.createRow(rowIndex++);
//
//	                row.createCell(0).setCellValue(item.getCustId());
//	                row.createCell(1).setCellValue(item.getAcctNumber());
//	                row.createCell(2).setCellValue(item.getAcctName());
//
//	                // ACCT BALANCE (right aligned, 3 decimal places)
//	                Cell balanceCell = row.createCell(3);
//	                if (item.getAcctBalanceInPula() != null) {
//	                    balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
//	                } else {
//	                    balanceCell.setCellValue(0.000);
//	                }
//	                balanceCell.setCellStyle(balanceStyle);
//
//	                row.createCell(4).setCellValue(item.getRowId());
//	                row.createCell(5).setCellValue(item.getColumnId());
//	                row.createCell(6).setCellValue(
//	                    item.getReportDate() != null ?
//	                    new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : ""
//	                );
//
//	                // Apply data style for all other cells
//	                for (int j = 0; j < 7; j++) {
//	                    if (j != 3) {
//	                        row.getCell(j).setCellStyle(dataStyle);
//	                    }
//	                }
//	            }
//	        } else {
//	            logger.info("No data found for M_CA2 — only header will be written.");
//	        }
//
//	        // Write to byte[]
//	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//	        workbook.write(bos);
//	        workbook.close();
//
//	        logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
//	        return bos.toByteArray();
//
//	    } catch (Exception e) {
//	        logger.error("Error generating M_CA2 Excel", e);
//	        return new byte[0];
//	    }
//	}

public List<Object> getM_CA2Archival() {
		List<Object> M_CA2Archivallist = new ArrayList<>();
		try {
			M_CA2Archivallist = BRRS_M_CA2_Archival_Summary_Repo.getM_CA2archival();
			M_CA2Archivallist = BRRS_M_CA2_Manual_Archival_Summary_Repo.getM_CA2archival();
			System.out.println("countser" + M_CA2Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_CA2 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_CA2Archivallist;
	}

	public byte[] getExcelM_CA2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
										   String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_CA2_Archival_Summary_Entity> dataList = BRRS_M_CA2_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_CA2_Manual_Archival_Summary_Entity> dataList1 = BRRS_M_CA2_Manual_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty() || dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_CA2 report. Returning empty result.");
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

			if (!dataList.isEmpty() || !dataList1.isEmpty()) {
			    for (int i = 0; i < dataList.size(); i++) {
			        M_CA2_Archival_Summary_Entity record = dataList.get(i);
			        M_CA2_Manual_Archival_Summary_Entity record1 = dataList1.get(i);
			        System.out.println("rownumber=" + (startRow + i));

			        Row row;
			        Cell cell3, cell4;
			        CellStyle originalStyle;

			        // ===== Row 10 / Col D =====
			        row = sheet.getRow(9);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR10_amount_2() != null) cell4.setCellValue(record.getR10_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 11 / Col D =====
			        row = sheet.getRow(10);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR11_amount_2() != null) cell4.setCellValue(record1.getR11_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 13 / Col C =====
			        row = sheet.getRow(12);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR13_amount_1() != null) cell3.setCellValue(record1.getR13_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 14 / Col C =====
			        row = sheet.getRow(13);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR14_amount_1() != null) cell3.setCellValue(record1.getR14_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 15 / Col C =====
			        row = sheet.getRow(14);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR15_amount_1() != null) cell3.setCellValue(record1.getR15_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 16 / Col C =====
			        row = sheet.getRow(15);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR16_amount_1() != null) cell3.setCellValue(record1.getR16_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 18 / Col C =====
			        row = sheet.getRow(17);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR18_amount_1() != null) cell3.setCellValue(record1.getR18_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 19 / Col C =====
			        row = sheet.getRow(18);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR19_amount_1() != null) cell3.setCellValue(record1.getR19_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 20 / Col C =====
			        row = sheet.getRow(19);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record.getR20_amount_1() != null) cell3.setCellValue(record.getR20_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 21 / Col C =====
			        row = sheet.getRow(20);
			        cell3 = row.getCell(2);
			        if (cell3 == null) cell3 = row.createCell(2);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR21_amount_1() != null) cell3.setCellValue(record1.getR21_amount_1().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);

			        // ===== Row 22 / Col D =====
			        row = sheet.getRow(21);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR22_amount_2() != null) cell4.setCellValue(record.getR22_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 23 / Col D =====
			        row = sheet.getRow(22);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR23_amount_2() != null) cell4.setCellValue(record.getR23_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 24 / Col D =====
			        row = sheet.getRow(23);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR24_amount_2() != null) cell4.setCellValue(record.getR24_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 25 / Col D =====
			        row = sheet.getRow(24);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR25_amount_2() != null) cell4.setCellValue(record.getR25_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 31 / Col D =====
			        row = sheet.getRow(30);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR31_amount_2() != null) cell4.setCellValue(record.getR31_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 32 / Col D =====
			        row = sheet.getRow(31);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR32_amount_2() != null) cell4.setCellValue(record1.getR32_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 33 / Col D =====
			        row = sheet.getRow(32);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR33_amount_2() != null) cell4.setCellValue(record.getR33_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 34 / Col D =====
			        row = sheet.getRow(33);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR34_amount_2() != null) cell4.setCellValue(record.getR34_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 41 / Col D =====
			        row = sheet.getRow(40);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR41_amount_2() != null) cell4.setCellValue(record.getR41_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 42 / Col D =====
			        row = sheet.getRow(41);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR42_amount_2() != null) cell4.setCellValue(record1.getR42_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 43 / Col D =====
			        row = sheet.getRow(42);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR43_amount_2() != null) cell4.setCellValue(record1.getR43_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 44 / Col D =====
			        row = sheet.getRow(43);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR44_amount_2() != null) cell4.setCellValue(record1.getR44_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 45 / Col D =====
			        row = sheet.getRow(44);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR45_amount_2() != null) cell4.setCellValue(record.getR45_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 46 / Col D =====
			        row = sheet.getRow(45);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR46_amount_2() != null) cell4.setCellValue(record1.getR46_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);

			        // ===== Row 47 / Col D =====
			        row = sheet.getRow(46);
			        cell4 = row.getCell(3);
			        if (cell4 == null) cell4 = row.createCell(3);
			        originalStyle = cell4.getCellStyle();
			        if (record.getR47_amount_2() != null) cell4.setCellValue(record.getR47_amount_2().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);	 
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
			logger.info("Generating Excel for BRRS_M_CA2 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
 XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("M_CA2Detail");

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
	        balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
	        balanceStyle.setBorderTop(border);
	        balanceStyle.setBorderBottom(border);
	        balanceStyle.setBorderLeft(border);
	        balanceStyle.setBorderRight(border);


	     // Header row
	     String[] headers = {
	     "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "ROWID", "COLUMNID", "REPORT_DATE"
	     };

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
	     List<M_CA2_Archival_Detail_Entity> reportData = BRRS_M_CA2_Archival_Detail_Repo.getdatabydateList(parsedToDate,version);

	     if (reportData != null && !reportData.isEmpty()) {
	     int rowIndex = 1;
	     for (M_CA2_Archival_Detail_Entity item : reportData) {
	     XSSFRow row = sheet.createRow(rowIndex++);

	     row.createCell(0).setCellValue(item.getCustId());
	     row.createCell(1).setCellValue(item.getAcctNumber());
	     row.createCell(2).setCellValue(item.getAcctName());

	  // ACCT BALANCE (right aligned, 3 decimal places with comma separator)
	     Cell balanceCell = row.createCell(3);

	     if (item.getAcctBalanceInPula() != null) {
	         balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
	     } else {
	         balanceCell.setCellValue(0);
	     }

	     // Create style with thousand separator and decimal point
	     DataFormat format = workbook.createDataFormat();

	     // Format: 1,234,567
	     balanceStyle.setDataFormat(format.getFormat("#,##0"));

	     // Right alignment (optional)
	     balanceStyle.setAlignment(HorizontalAlignment.RIGHT);

	     balanceCell.setCellStyle(balanceStyle);

	     row.createCell(4).setCellValue(item.getReportLable());
	     row.createCell(5).setCellValue(item.getReportAddlCriteria1());
	     row.createCell(6).setCellValue(
	     item.getReportDate() != null ?
	     new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : ""
	     );

	     // Apply data style for all other cells
	     for (int j = 0; j < 7; j++) {
	     if (j != 3) {
	     row.getCell(j).setCellStyle(dataStyle);
	     }
	     }
	     }
	     } else {
	     logger.info("No data found for M_CA2 — only header will be written.");
	     }
	        // Write to byte[]
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        workbook.write(bos);
	        workbook.close();

	        logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
	        return bos.toByteArray();

	    } catch (Exception e) {
	        logger.error("Error generating M_CA2 Excel", e);
	        return new byte[0];
	    }
	}

@Autowired
private JdbcTemplate jdbcTemplate;

public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
    ModelAndView mv = new ModelAndView("BRRS/M_CA2"); // ✅ match the report name
    System.out.println("Hello");
    if (acctNo != null) {
    	M_CA2_Detail_Entity la1Entity = BRRS_M_CA2_Detail_Repo.findByAcctnumber(acctNo);
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
    ModelAndView mv = new ModelAndView("BRRS/M_CA2"); // ✅ match the report name

    if (acctNo != null) {
        M_CA2_Detail_Entity la1Entity = BRRS_M_CA2_Detail_Repo.findByAcctnumber(acctNo);
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
public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
    try {
        String acctNo = request.getParameter("acctNumber");
        String provisionStr = request.getParameter("acctBalanceInPula");
        String acctName = request.getParameter("acctName");
        String reportDateStr = request.getParameter("reportDate");

        logger.info("Received update for ACCT_NO: {}", acctNo);

        M_CA2_Detail_Entity existing = BRRS_M_CA2_Detail_Repo.findByAcctnumber(acctNo);
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
            if (existing.getAcctBalanceInPula() == null ||
                existing.getAcctBalanceInPula().compareTo(newProvision) != 0) {
                existing.setAcctBalanceInPula(newProvision);
                isChanged = true;
                logger.info("Balance updated to {}", newProvision);
            }
        }
        
        

        if (isChanged) {
        	BRRS_M_CA2_Detail_Repo.save(existing);
            logger.info("Record updated successfully for account {}", acctNo);

            // Format date for procedure
            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

            // Run summary procedure after commit
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    try {
                        logger.info("Transaction committed — calling BRRS_M_CA2_SUMMARY_PROCEDURE({})",
                                formattedDate);
                        jdbcTemplate.update("BEGIN BRRS_M_CA2_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
        logger.error("Error updating M_CA2 record", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating record: " + e.getMessage());
    }
}

}

