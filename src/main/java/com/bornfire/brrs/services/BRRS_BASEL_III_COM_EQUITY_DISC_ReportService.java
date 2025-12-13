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

import com.bornfire.brrs.entities.BASEL_III_COM_EQUITY_DISC_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BASEL_III_COM_EQUITY_DISC_Detail_Entity;
import com.bornfire.brrs.entities.BASEL_III_COM_EQUITY_DISC_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_BASEL_III_COM_EQUITY_DISC_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_BASEL_III_COM_EQUITY_DISC_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_BASEL_III_COM_EQUITY_DISC_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_BASEL_III_COM_EQUITY_DISC_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_CAP_RATIO_BUFFER_Detail_Repo;
import com.bornfire.brrs.entities.CAP_RATIO_BUFFER_Archival_Detail_Entity;
import com.bornfire.brrs.entities.CAP_RATIO_BUFFER_Archival_Summary_Entity;
import com.bornfire.brrs.entities.CAP_RATIO_BUFFER_Detail_Entity;
import com.bornfire.brrs.entities.CAP_RATIO_BUFFER_Summary_Entity;

@Component
@Service

public class BRRS_BASEL_III_COM_EQUITY_DISC_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_BASEL_III_COM_EQUITY_DISC_ReportService.class);

	

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	
	  @Autowired BRRS_BASEL_III_COM_EQUITY_DISC_Summary_Repo
	  B_III_CETD_summary_repo;
	 
	@Autowired
	BRRS_BASEL_III_COM_EQUITY_DISC_Archival_Summary_Repo B_III_CETD_Archival_Summary_Repo;
	
	@Autowired
	BRRS_BASEL_III_COM_EQUITY_DISC_Detail_Repo B_III_CETD_detail_repo;
	
	
	
	
	
	  @Autowired BRRS_BASEL_III_COM_EQUITY_DISC_Archival_Detail_Repo B_III_CETD_Archival_Detail_Repo;
	 

	
	
	
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	
	public ModelAndView getB_III_CETDView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {

		ModelAndView mv = new ModelAndView();
		

		System.out.println("testing");
		System.out.println(version);

		if ("ARCHIVAL".equals(type) && version != null && !version.isEmpty()) {

		    System.out.println("ARCHIVAL MODE");
		    System.out.println("version = " + version);

		    List<BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity> T1Master = new ArrayList<>();
		 
		    try {
		        Date dt = dateformat.parse(todate);

		        T1Master = B_III_CETD_Archival_Summary_Repo.getdatabydateListarchival(dt, version);

		        System.out.println("T1Master size = " + T1Master.size());
		      

		    } catch (ParseException e) {
		        e.printStackTrace();
		    }

		    mv.addObject("reportsummary", T1Master);
		 
		} else {

			List<BASEL_III_COM_EQUITY_DISC_Summary_Entity> T1Master = new ArrayList<BASEL_III_COM_EQUITY_DISC_Summary_Entity>();
		
			try {
				Date d1 = dateformat.parse(todate);
				
				T1Master = B_III_CETD_summary_repo.getdatabydateList(dateformat.parse(todate));
			
				System.out.println("T1Master size " + T1Master.size());
				mv.addObject("report_date", dateformat.format(d1));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			
		}

	

		mv.setViewName("BRRS/B_III_CETD");

		mv.addObject("displaymode", "summary");

		System.out.println("scv" + mv.getViewName());

		return mv;

	}
	 
	
	
	public ModelAndView getB_III_CETDcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
				List<BASEL_III_COM_EQUITY_DISC_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = B_III_CETD_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate, version);
				} else {
					T1Dt1 = B_III_CETD_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<BASEL_III_COM_EQUITY_DISC_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = B_III_CETD_detail_repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate);
				} else {
					T1Dt1 = B_III_CETD_detail_repo.getdatabydateList(parsedDate);
					totalPages = B_III_CETD_detail_repo.getdatacount(parsedDate);
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

	

	
		mv.setViewName("BRRS/B_III_CETD");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}
	
	
	
	

	public byte[] getB_III_CETDExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelB_III_CETDARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<BASEL_III_COM_EQUITY_DISC_Summary_Entity> dataList = B_III_CETD_summary_repo.getdatabydateList(dateformat.parse(todate));
	

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  B_III_CETD report. Returning empty result.");
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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					BASEL_III_COM_EQUITY_DISC_Summary_Entity record = dataList.get(i);
				
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

  
					
					// row7
					// Column C

					
					// R7
					row = sheet.getRow(6);
					Cell cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR7_amount() != null ? record.getR7_amount().doubleValue() : 0);

					// R8
					row = sheet.getRow(7);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR8_amount() != null ? record.getR8_amount().doubleValue() : 0);

					// R9
					row = sheet.getRow(8);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR9_amount() != null ? record.getR9_amount().doubleValue() : 0);

					// R10
					row = sheet.getRow(9);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR10_amount() != null ? record.getR10_amount().doubleValue() : 0);

					// R11
					row = sheet.getRow(10);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR11_amount() != null ? record.getR11_amount().doubleValue() : 0);

					// R12
					row = sheet.getRow(11);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR12_amount() != null ? record.getR12_amount().doubleValue() : 0);

					

					// R14
					row = sheet.getRow(13);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR14_amount() != null ? record.getR14_amount().doubleValue() : 0);

					// R15
					row = sheet.getRow(14);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR15_amount() != null ? record.getR15_amount().doubleValue() : 0);

					// R16
					row = sheet.getRow(15);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR16_amount() != null ? record.getR16_amount().doubleValue() : 0);

					// R17
					row = sheet.getRow(16);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR17_amount() != null ? record.getR17_amount().doubleValue() : 0);

					// R18
					row = sheet.getRow(17);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR18_amount() != null ? record.getR18_amount().doubleValue() : 0);

					// R19
					row = sheet.getRow(18);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR19_amount() != null ? record.getR19_amount().doubleValue() : 0);

					// R20
					row = sheet.getRow(19);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR20_amount() != null ? record.getR20_amount().doubleValue() : 0);

					// R21
					row = sheet.getRow(20);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR21_amount() != null ? record.getR21_amount().doubleValue() : 0);

					// R22
					row = sheet.getRow(21);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR22_amount() != null ? record.getR22_amount().doubleValue() : 0);

					// R23
					row = sheet.getRow(22);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR23_amount() != null ? record.getR23_amount().doubleValue() : 0);

					// R24
					row = sheet.getRow(23);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR24_amount() != null ? record.getR24_amount().doubleValue() : 0);

					// R25
					row = sheet.getRow(24);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR25_amount() != null ? record.getR25_amount().doubleValue() : 0);

					// R26
					row = sheet.getRow(25);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR26_amount() != null ? record.getR26_amount().doubleValue() : 0);

					// R27
					row = sheet.getRow(26);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR27_amount() != null ? record.getR27_amount().doubleValue() : 0);

					// R28
					row = sheet.getRow(27);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR28_amount() != null ? record.getR28_amount().doubleValue() : 0);

					// R29
					row = sheet.getRow(28);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR29_amount() != null ? record.getR29_amount().doubleValue() : 0);

					// R30
					row = sheet.getRow(29);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR30_amount() != null ? record.getR30_amount().doubleValue() : 0);

					// R31
					row = sheet.getRow(30);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR31_amount() != null ? record.getR31_amount().doubleValue() : 0);

					// R32
					row = sheet.getRow(31);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR32_amount() != null ? record.getR32_amount().doubleValue() : 0);

					// R33
					row = sheet.getRow(32);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR33_amount() != null ? record.getR33_amount().doubleValue() : 0);

					// R34
					row = sheet.getRow(33);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR34_amount() != null ? record.getR34_amount().doubleValue() : 0);

					// R35
					row = sheet.getRow(34);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR35_amount() != null ? record.getR35_amount().doubleValue() : 0);

					// R36
					row = sheet.getRow(35);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR36_amount() != null ? record.getR36_amount().doubleValue() : 0);

					

					// R38
					row = sheet.getRow(37);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR38_amount() != null ? record.getR38_amount().doubleValue() : 0);

					// R39
					row = sheet.getRow(38);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR39_amount() != null ? record.getR39_amount().doubleValue() : 0);

					// R40
					row = sheet.getRow(39);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR40_amount() != null ? record.getR40_amount().doubleValue() : 0);

					// R41
					row = sheet.getRow(40);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR41_amount() != null ? record.getR41_amount().doubleValue() : 0);

					// R42
					row = sheet.getRow(41);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR42_amount() != null ? record.getR42_amount().doubleValue() : 0);

					// R43
					row = sheet.getRow(42);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR43_amount() != null ? record.getR43_amount().doubleValue() : 0);

					// R44
					row = sheet.getRow(43);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR44_amount() != null ? record.getR44_amount().doubleValue() : 0);

				

					// R46
					row = sheet.getRow(45);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR46_amount() != null ? record.getR46_amount().doubleValue() : 0);

					// R47
					row = sheet.getRow(46);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR47_amount() != null ? record.getR47_amount().doubleValue() : 0);

					// R48
					row = sheet.getRow(47);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR48_amount() != null ? record.getR48_amount().doubleValue() : 0);

					// R49
					row = sheet.getRow(48);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR49_amount() != null ? record.getR49_amount().doubleValue() : 0);

					// R50
					row = sheet.getRow(49);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR50_amount() != null ? record.getR50_amount().doubleValue() : 0);

					// R51
					row = sheet.getRow(50);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR51_amount() != null ? record.getR51_amount().doubleValue() : 0);

					// R52
					row = sheet.getRow(51);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR52_amount() != null ? record.getR52_amount().doubleValue() : 0);

					// R53
					row = sheet.getRow(52);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR53_amount() != null ? record.getR53_amount().doubleValue() : 0);

					// R54
					row = sheet.getRow(53);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR54_amount() != null ? record.getR54_amount().doubleValue() : 0);

					// R55
					row = sheet.getRow(54);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR55_amount() != null ? record.getR55_amount().doubleValue() : 0);

				

					// R57
					row = sheet.getRow(56);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR57_amount() != null ? record.getR57_amount().doubleValue() : 0);

					// R58
					row = sheet.getRow(57);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR58_amount() != null ? record.getR58_amount().doubleValue() : 0);

					// R59
					row = sheet.getRow(58);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR59_amount() != null ? record.getR59_amount().doubleValue() : 0);

					// R60
					row = sheet.getRow(59);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR60_amount() != null ? record.getR60_amount().doubleValue() : 0);

					// R61
					row = sheet.getRow(60);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR61_amount() != null ? record.getR61_amount().doubleValue() : 0);

					// R62
					row = sheet.getRow(61);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR62_amount() != null ? record.getR62_amount().doubleValue() : 0);

				

					// R64
					row = sheet.getRow(63);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR64_amount() != null ? record.getR64_amount().doubleValue() : 0);

					// R65
					row = sheet.getRow(64);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR65_amount() != null ? record.getR65_amount().doubleValue() : 0);

					// R66
					row = sheet.getRow(65);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR66_amount() != null ? record.getR66_amount().doubleValue() : 0);

					// R67
					row = sheet.getRow(66);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR67_amount() != null ? record.getR67_amount().doubleValue() : 0);

					// R68
					row = sheet.getRow(67);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR68_amount() != null ? record.getR68_amount().doubleValue() : 0);

					// R69
					row = sheet.getRow(68);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR69_amount() != null ? record.getR69_amount().doubleValue() : 0);

					// R70
					row = sheet.getRow(69);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR70_amount() != null ? record.getR70_amount().doubleValue() : 0);

					// R71
					row = sheet.getRow(70);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR71_amount() != null ? record.getR71_amount().doubleValue() : 0);

					// R72
					row = sheet.getRow(71);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR72_amount() != null ? record.getR72_amount().doubleValue() : 0);

					// R73
					row = sheet.getRow(72);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR73_amount() != null ? record.getR73_amount().doubleValue() : 0);

				

					  
					 
					
					

					
				
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

	
	public byte[] getExcelB_III_CETDARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}
		
		List<BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity> dataList = B_III_CETD_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
	

		

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for B_III_CETD report. Returning empty result.");
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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					BASEL_III_COM_EQUITY_DISC_Archival_Summary_Entity record = dataList.get(i);
				
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					// row7
					// Column C

					
					// R7
					row = sheet.getRow(6);
					Cell cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR7_amount() != null ? record.getR7_amount().doubleValue() : 0);

					// R8
					row = sheet.getRow(7);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR8_amount() != null ? record.getR8_amount().doubleValue() : 0);

					// R9
					row = sheet.getRow(8);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR9_amount() != null ? record.getR9_amount().doubleValue() : 0);

					// R10
					row = sheet.getRow(9);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR10_amount() != null ? record.getR10_amount().doubleValue() : 0);

					// R11
					row = sheet.getRow(10);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR11_amount() != null ? record.getR11_amount().doubleValue() : 0);

					// R12
					row = sheet.getRow(11);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR12_amount() != null ? record.getR12_amount().doubleValue() : 0);

					

					// R14
					row = sheet.getRow(13);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR14_amount() != null ? record.getR14_amount().doubleValue() : 0);

					// R15
					row = sheet.getRow(14);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR15_amount() != null ? record.getR15_amount().doubleValue() : 0);

					// R16
					row = sheet.getRow(15);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR16_amount() != null ? record.getR16_amount().doubleValue() : 0);

					// R17
					row = sheet.getRow(16);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR17_amount() != null ? record.getR17_amount().doubleValue() : 0);

					// R18
					row = sheet.getRow(17);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR18_amount() != null ? record.getR18_amount().doubleValue() : 0);

					// R19
					row = sheet.getRow(18);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR19_amount() != null ? record.getR19_amount().doubleValue() : 0);

					// R20
					row = sheet.getRow(19);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR20_amount() != null ? record.getR20_amount().doubleValue() : 0);

					// R21
					row = sheet.getRow(20);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR21_amount() != null ? record.getR21_amount().doubleValue() : 0);

					// R22
					row = sheet.getRow(21);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR22_amount() != null ? record.getR22_amount().doubleValue() : 0);

					// R23
					row = sheet.getRow(22);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR23_amount() != null ? record.getR23_amount().doubleValue() : 0);

					// R24
					row = sheet.getRow(23);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR24_amount() != null ? record.getR24_amount().doubleValue() : 0);

					// R25
					row = sheet.getRow(24);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR25_amount() != null ? record.getR25_amount().doubleValue() : 0);

					// R26
					row = sheet.getRow(25);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR26_amount() != null ? record.getR26_amount().doubleValue() : 0);

					// R27
					row = sheet.getRow(26);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR27_amount() != null ? record.getR27_amount().doubleValue() : 0);

					// R28
					row = sheet.getRow(27);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR28_amount() != null ? record.getR28_amount().doubleValue() : 0);

					// R29
					row = sheet.getRow(28);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR29_amount() != null ? record.getR29_amount().doubleValue() : 0);

					// R30
					row = sheet.getRow(29);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR30_amount() != null ? record.getR30_amount().doubleValue() : 0);

					// R31
					row = sheet.getRow(30);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR31_amount() != null ? record.getR31_amount().doubleValue() : 0);

					// R32
					row = sheet.getRow(31);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR32_amount() != null ? record.getR32_amount().doubleValue() : 0);

					// R33
					row = sheet.getRow(32);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR33_amount() != null ? record.getR33_amount().doubleValue() : 0);

					// R34
					row = sheet.getRow(33);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR34_amount() != null ? record.getR34_amount().doubleValue() : 0);

					// R35
					row = sheet.getRow(34);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR35_amount() != null ? record.getR35_amount().doubleValue() : 0);

					// R36
					row = sheet.getRow(35);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR36_amount() != null ? record.getR36_amount().doubleValue() : 0);

					

					// R38
					row = sheet.getRow(37);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR38_amount() != null ? record.getR38_amount().doubleValue() : 0);

					// R39
					row = sheet.getRow(38);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR39_amount() != null ? record.getR39_amount().doubleValue() : 0);

					// R40
					row = sheet.getRow(39);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR40_amount() != null ? record.getR40_amount().doubleValue() : 0);

					// R41
					row = sheet.getRow(40);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR41_amount() != null ? record.getR41_amount().doubleValue() : 0);

					// R42
					row = sheet.getRow(41);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR42_amount() != null ? record.getR42_amount().doubleValue() : 0);

					// R43
					row = sheet.getRow(42);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR43_amount() != null ? record.getR43_amount().doubleValue() : 0);

					// R44
					row = sheet.getRow(43);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR44_amount() != null ? record.getR44_amount().doubleValue() : 0);

				

					// R46
					row = sheet.getRow(45);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR46_amount() != null ? record.getR46_amount().doubleValue() : 0);

					// R47
					row = sheet.getRow(46);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR47_amount() != null ? record.getR47_amount().doubleValue() : 0);

					// R48
					row = sheet.getRow(47);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR48_amount() != null ? record.getR48_amount().doubleValue() : 0);

					// R49
					row = sheet.getRow(48);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR49_amount() != null ? record.getR49_amount().doubleValue() : 0);

					// R50
					row = sheet.getRow(49);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR50_amount() != null ? record.getR50_amount().doubleValue() : 0);

					// R51
					row = sheet.getRow(50);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR51_amount() != null ? record.getR51_amount().doubleValue() : 0);

					// R52
					row = sheet.getRow(51);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR52_amount() != null ? record.getR52_amount().doubleValue() : 0);

					// R53
					row = sheet.getRow(52);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR53_amount() != null ? record.getR53_amount().doubleValue() : 0);

					// R54
					row = sheet.getRow(53);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR54_amount() != null ? record.getR54_amount().doubleValue() : 0);

					// R55
					row = sheet.getRow(54);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR55_amount() != null ? record.getR55_amount().doubleValue() : 0);

				

					// R57
					row = sheet.getRow(56);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR57_amount() != null ? record.getR57_amount().doubleValue() : 0);

					// R58
					row = sheet.getRow(57);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR58_amount() != null ? record.getR58_amount().doubleValue() : 0);

					// R59
					row = sheet.getRow(58);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR59_amount() != null ? record.getR59_amount().doubleValue() : 0);

					// R60
					row = sheet.getRow(59);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR60_amount() != null ? record.getR60_amount().doubleValue() : 0);

					// R61
					row = sheet.getRow(60);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR61_amount() != null ? record.getR61_amount().doubleValue() : 0);

					// R62
					row = sheet.getRow(61);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR62_amount() != null ? record.getR62_amount().doubleValue() : 0);

				

					// R64
					row = sheet.getRow(63);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR64_amount() != null ? record.getR64_amount().doubleValue() : 0);

					// R65
					row = sheet.getRow(64);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR65_amount() != null ? record.getR65_amount().doubleValue() : 0);

					// R66
					row = sheet.getRow(65);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR66_amount() != null ? record.getR66_amount().doubleValue() : 0);

					// R67
					row = sheet.getRow(66);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR67_amount() != null ? record.getR67_amount().doubleValue() : 0);

					// R68
					row = sheet.getRow(67);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR68_amount() != null ? record.getR68_amount().doubleValue() : 0);

					// R69
					row = sheet.getRow(68);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR69_amount() != null ? record.getR69_amount().doubleValue() : 0);

					// R70
					row = sheet.getRow(69);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR70_amount() != null ? record.getR70_amount().doubleValue() : 0);

					// R71
					row = sheet.getRow(70);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR71_amount() != null ? record.getR71_amount().doubleValue() : 0);

					// R72
					row = sheet.getRow(71);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR72_amount() != null ? record.getR72_amount().doubleValue() : 0);

					// R73
					row = sheet.getRow(72);
					cellC = row.getCell(4);
					if (cellC == null) cellC = row.createCell(4);
					cellC.setCellValue(record.getR73_amount() != null ? record.getR73_amount().doubleValue() : 0);

				

   
					
					
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
	
	

	
	
	
	
	
	
	
	
	
	
	public List<Object> getB_III_CETDArchival() {
		List<Object> B_III_CETDArchivallist = new ArrayList<>();
		try {
			B_III_CETDArchivallist = B_III_CETD_Archival_Summary_Repo.getB_III_CETDarchival();
		
			System.out.println("countser" + B_III_CETDArchivallist.size());
			
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching B_III_CETDArchivallist Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return B_III_CETDArchivallist;
	}
	
	
	
	public byte[] getB_III_CETDDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for  B_III_CETD Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getB_III_CETDDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("B_III_CETDDetails");

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
			List<BASEL_III_COM_EQUITY_DISC_Detail_Entity> reportData = B_III_CETD_detail_repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (BASEL_III_COM_EQUITY_DISC_Detail_Entity item : reportData) {
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

					

					row.createCell(4).setCellValue(item.getReportLabel());
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
				logger.info("No data found for B_III_CETD — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating B_III_CETD Excel", e);
			return new byte[0];
		}
	}
	
	
	
	
	
	
	
	public byte[] getB_III_CETDDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for B_III_CETD ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("B_III_CETDDetail");

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
			List<BASEL_III_COM_EQUITY_DISC_Archival_Detail_Entity> reportData = B_III_CETD_Archival_Detail_Repo.getdatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (BASEL_III_COM_EQUITY_DISC_Archival_Detail_Entity item : reportData) {
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

					
					row.createCell(4).setCellValue(item.getReportLabel());
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
				logger.info("No data found for B_III_CETD — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating B_III_CETD Excel", e);
			return new byte[0];
		}
	}
	
	
	
	
	 @Autowired BRRS_BASEL_III_COM_EQUITY_DISC_Detail_Repo b_III_cetd_detail_repo;
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/B_III_CETD"); 

		if (acctNo != null) {
			BASEL_III_COM_EQUITY_DISC_Detail_Entity b_III_cetdEntity = b_III_cetd_detail_repo.findByAcctnumber(acctNo);
			if (b_III_cetdEntity != null && b_III_cetdEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(b_III_cetdEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("b_III_cetdData", b_III_cetdEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String acctBalanceInpula = request.getParameter("acctBalanceInpula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			BASEL_III_COM_EQUITY_DISC_Detail_Entity existing = b_III_cetd_detail_repo.findByAcctnumber(acctNo);
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

			 if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {
		            BigDecimal newacctBalanceInpula = new BigDecimal(acctBalanceInpula);
		            if (existing.getAcctBalanceInpula()  == null ||
		                existing.getAcctBalanceInpula().compareTo(newacctBalanceInpula) != 0) {
		            	 existing.setAcctBalanceInpula(newacctBalanceInpula);
		                isChanged = true;
		                logger.info("Balance updated to {}", newacctBalanceInpula);
		            }
		        }
		        
			if (isChanged) {
				b_III_cetd_detail_repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_BASEL_III_COM_EQUITY_DISC_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_BASEL_III_COM_EQUITY_DISC_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating CAP_RATIO_BUFFER record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
	
	
	

}