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

import com.bornfire.brrs.entities.BRRS_OFF_BS_ITEMS_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_OFF_BS_ITEMS_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_OFF_BS_ITEMS_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_OFF_BS_ITEMS_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_OFF_BS_ITEMS_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_OFF_BS_ITEMS_Summary_Repo2;
import com.bornfire.brrs.entities.OFF_BS_ITEMS_Archival_Detail_Entity;

import com.bornfire.brrs.entities.OFF_BS_ITEMS_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.OFF_BS_ITEMS_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.OFF_BS_ITEMS_Detail_Entity;

import com.bornfire.brrs.entities.OFF_BS_ITEMS_Summary_Entity1;
import com.bornfire.brrs.entities.OFF_BS_ITEMS_Summary_Entity2;

@Component
@Service

public class BRRS_OFF_BS_ITEMS_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_OFF_BS_ITEMS_ReportService.class);

	

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	
	@Autowired 
	BRRS_OFF_BS_ITEMS_Summary_Repo1 OFF_BS_ITEMS_summary_repo1;
	
	@Autowired 
	BRRS_OFF_BS_ITEMS_Summary_Repo2 OFF_BS_ITEMS_summary_repo2;
	 
	@Autowired
	BRRS_OFF_BS_ITEMS_Archival_Summary_Repo1 OFF_BS_ITEMS_Archival_Summary_Repo1;
	
	@Autowired
	BRRS_OFF_BS_ITEMS_Archival_Summary_Repo2 OFF_BS_ITEMS_Archival_Summary_Repo2;
	
	@Autowired
	BRRS_OFF_BS_ITEMS_Detail_Repo OFF_BS_ITEMS_detail_repo;
	
	
	@Autowired
	BRRS_OFF_BS_ITEMS_Archival_Detail_Repo OFF_BS_ITEMS_Archival_Detail_Repo;
	  
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	
	public ModelAndView getOFF_BS_ITEMSView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		

		System.out.println("testing");
		System.out.println(version);

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {

		    System.out.println("ARCHIVAL MODE");
		    System.out.println("version = " + version);

		    List<OFF_BS_ITEMS_Archival_Summary_Entity1> T1Master = new ArrayList<>();
		    List<OFF_BS_ITEMS_Archival_Summary_Entity2> T2Master = new ArrayList<>();

		 
		    try {
		        Date dt = dateformat.parse(todate);

		        T1Master = OFF_BS_ITEMS_Archival_Summary_Repo1.getdatabydateListarchival(dt, version);
		        T2Master = OFF_BS_ITEMS_Archival_Summary_Repo2.getdatabydateListarchival(dt, version);

		        System.out.println("T1Master size = " + T1Master.size());
		      

		    } catch (ParseException e) {
		        e.printStackTrace();
		    }

		    mv.addObject("reportsummary1", T1Master);
		    mv.addObject("reportsummary2", T2Master);

		 
		} else {

			List<OFF_BS_ITEMS_Summary_Entity1> T1Master = new ArrayList<OFF_BS_ITEMS_Summary_Entity1>();
			List<OFF_BS_ITEMS_Summary_Entity2> T2Master = new ArrayList<OFF_BS_ITEMS_Summary_Entity2>();

	
			try {
				Date d1 = dateformat.parse(todate);
				
				T1Master = OFF_BS_ITEMS_summary_repo1.getdatabydateList(dateformat.parse(todate));
				T2Master = OFF_BS_ITEMS_summary_repo2.getdatabydateList(dateformat.parse(todate));

			
				System.out.println("T1Master size " + T1Master.size());
				mv.addObject("report_date", dateformat.format(d1));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			  mv.addObject("reportsummary1", T1Master);
			    mv.addObject("reportsummary2", T2Master);

		}

	

		mv.setViewName("BRRS/OFF_BS_ITEMS");

		mv.addObject("displaymode", "summary");

		System.out.println("scv" + mv.getViewName());

		return mv;

	}
	 
	
	
	public ModelAndView getOFF_BS_ITEMScurrentDtl(String reportId, String fromdate, String todate, String currency,
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
				List<OFF_BS_ITEMS_Archival_Detail_Entity> T1Dt1;
				
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = OFF_BS_ITEMS_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate, version);
				} else {
					T1Dt1 = OFF_BS_ITEMS_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<OFF_BS_ITEMS_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = OFF_BS_ITEMS_detail_repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate);
				} else {
					T1Dt1 = OFF_BS_ITEMS_detail_repo.getdatabydateList(parsedDate);
					totalPages = OFF_BS_ITEMS_detail_repo.getdatacount(parsedDate);
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

	

	
		mv.setViewName("BRRS/OFF_BS_ITEMS");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}
	
	
	
	

	public byte[] getOFF_BS_ITEMSExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelOFF_BS_ITEMSARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<OFF_BS_ITEMS_Summary_Entity1> dataList = OFF_BS_ITEMS_summary_repo1.getdatabydateList(dateformat.parse(todate));
		List<OFF_BS_ITEMS_Summary_Entity2> dataList1 = OFF_BS_ITEMS_summary_repo2.getdatabydateList(dateformat.parse(todate));
	

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  OFF_BS_ITEMS report. Returning empty result.");
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
					OFF_BS_ITEMS_Summary_Entity1 record = dataList.get(i);
					OFF_BS_ITEMS_Summary_Entity2 record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

  Cell  cellC ,cellD , cellE , cellF , cellG , cellH, cellI;



////----------R12


cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR12_total_book_expo() != null) {
    cellC.setCellValue(record.getR12_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR12_margin_pro() != null) {
    cellD.setCellValue(record.getR12_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR12_book_expo() != null) {
    cellE.setCellValue(record.getR12_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR12_ccf_cont() != null) {
    cellF.setCellValue(record.getR12_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR12_equiv_value() != null) {
    cellG.setCellValue(record.getR12_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR12_rw_obligant() != null) {
    cellH.setCellValue(record.getR12_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR12_rav() != null) {
    cellI.setCellValue(record.getR12_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}


// R13

row = sheet.getRow(12);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR13_total_book_expo() != null) {
    cellC.setCellValue(record.getR13_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR13_margin_pro() != null) {
    cellD.setCellValue(record.getR13_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR13_ccf_cont() != null) {
    cellF.setCellValue(record.getR13_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR13_rw_obligant() != null) {
    cellH.setCellValue(record.getR13_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//--R14
row = sheet.getRow(13);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR14_total_book_expo() != null) {
    cellC.setCellValue(record.getR14_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR14_margin_pro() != null) {
    cellD.setCellValue(record.getR14_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR14_ccf_cont() != null) {
    cellF.setCellValue(record.getR14_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR14_rw_obligant() != null) {
    cellH.setCellValue(record.getR14_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}


//------R15 TO 46

row = sheet.getRow(14);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR15_total_book_expo() != null) {
    cellC.setCellValue(record.getR15_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR15_margin_pro() != null) {
    cellD.setCellValue(record.getR15_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR15_book_expo() != null) {
    cellE.setCellValue(record.getR15_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR15_ccf_cont() != null) {
    cellF.setCellValue(record.getR15_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR15_equiv_value() != null) {
    cellG.setCellValue(record.getR15_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR15_rw_obligant() != null) {
    cellH.setCellValue(record.getR15_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR15_rav() != null) {
    cellI.setCellValue(record.getR15_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

row = sheet.getRow(15);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR16_total_book_expo() != null) {
    cellC.setCellValue(record.getR16_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR16_margin_pro() != null) {
    cellD.setCellValue(record.getR16_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR16_book_expo() != null) {
    cellE.setCellValue(record.getR16_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR16_ccf_cont() != null) {
    cellF.setCellValue(record.getR16_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR16_equiv_value() != null) {
    cellG.setCellValue(record.getR16_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR16_rw_obligant() != null) {
    cellH.setCellValue(record.getR16_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR16_rav() != null) {
    cellI.setCellValue(record.getR16_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

row = sheet.getRow(16);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR17_total_book_expo() != null) {
    cellC.setCellValue(record.getR17_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR17_margin_pro() != null) {
    cellD.setCellValue(record.getR17_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR17_book_expo() != null) {
    cellE.setCellValue(record.getR17_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR17_ccf_cont() != null) {
    cellF.setCellValue(record.getR17_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR17_equiv_value() != null) {
    cellG.setCellValue(record.getR17_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR17_rw_obligant() != null) {
    cellH.setCellValue(record.getR17_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR17_rav() != null) {
    cellI.setCellValue(record.getR17_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

row = sheet.getRow(17);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR18_total_book_expo() != null) {
    cellC.setCellValue(record.getR18_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR18_margin_pro() != null) {
    cellD.setCellValue(record.getR18_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR18_book_expo() != null) {
    cellE.setCellValue(record.getR18_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR18_ccf_cont() != null) {
    cellF.setCellValue(record.getR18_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR18_equiv_value() != null) {
    cellG.setCellValue(record.getR18_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR18_rw_obligant() != null) {
    cellH.setCellValue(record.getR18_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR18_rav() != null) {
    cellI.setCellValue(record.getR18_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

row = sheet.getRow(18);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR19_total_book_expo() != null) {
    cellC.setCellValue(record.getR19_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR19_margin_pro() != null) {
    cellD.setCellValue(record.getR19_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR19_book_expo() != null) {
    cellE.setCellValue(record.getR19_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR19_ccf_cont() != null) {
    cellF.setCellValue(record.getR19_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR19_equiv_value() != null) {
    cellG.setCellValue(record.getR19_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR19_rw_obligant() != null) {
    cellH.setCellValue(record.getR19_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR19_rav() != null) {
    cellI.setCellValue(record.getR19_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

row = sheet.getRow(19);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR20_total_book_expo() != null) {
    cellC.setCellValue(record.getR20_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR20_margin_pro() != null) {
    cellD.setCellValue(record.getR20_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR20_book_expo() != null) {
    cellE.setCellValue(record.getR20_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR20_ccf_cont() != null) {
    cellF.setCellValue(record.getR20_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR20_equiv_value() != null) {
    cellG.setCellValue(record.getR20_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR20_rw_obligant() != null) {
    cellH.setCellValue(record.getR20_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR20_rav() != null) {
    cellI.setCellValue(record.getR20_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

row = sheet.getRow(20);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR21_total_book_expo() != null) {
    cellC.setCellValue(record.getR21_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR21_margin_pro() != null) {
    cellD.setCellValue(record.getR21_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR21_book_expo() != null) {
    cellE.setCellValue(record.getR21_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR21_ccf_cont() != null) {
    cellF.setCellValue(record.getR21_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR21_equiv_value() != null) {
    cellG.setCellValue(record.getR21_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR21_rw_obligant() != null) {
    cellH.setCellValue(record.getR21_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR21_rav() != null) {
    cellI.setCellValue(record.getR21_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

row = sheet.getRow(21);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR22_total_book_expo() != null) {
    cellC.setCellValue(record.getR22_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR22_margin_pro() != null) {
    cellD.setCellValue(record.getR22_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR22_book_expo() != null) {
    cellE.setCellValue(record.getR22_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR22_ccf_cont() != null) {
    cellF.setCellValue(record.getR22_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR22_equiv_value() != null) {
    cellG.setCellValue(record.getR22_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR22_rw_obligant() != null) {
    cellH.setCellValue(record.getR22_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR22_rav() != null) {
    cellI.setCellValue(record.getR22_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

row = sheet.getRow(22);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR23_total_book_expo() != null) {
    cellC.setCellValue(record.getR23_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR23_margin_pro() != null) {
    cellD.setCellValue(record.getR23_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR23_book_expo() != null) {
    cellE.setCellValue(record.getR23_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR23_ccf_cont() != null) {
    cellF.setCellValue(record.getR23_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR23_equiv_value() != null) {
    cellG.setCellValue(record.getR23_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR23_rw_obligant() != null) {
    cellH.setCellValue(record.getR23_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR23_rav() != null) {
    cellI.setCellValue(record.getR23_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

row = sheet.getRow(23);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR24_total_book_expo() != null) {
    cellC.setCellValue(record.getR24_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR24_margin_pro() != null) {
    cellD.setCellValue(record.getR24_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR24_book_expo() != null) {
    cellE.setCellValue(record.getR24_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR24_ccf_cont() != null) {
    cellF.setCellValue(record.getR24_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR24_equiv_value() != null) {
    cellG.setCellValue(record.getR24_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR24_rw_obligant() != null) {
    cellH.setCellValue(record.getR24_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR24_rav() != null) {
    cellI.setCellValue(record.getR24_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

row = sheet.getRow(24);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR25_total_book_expo() != null) {
    cellC.setCellValue(record.getR25_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR25_margin_pro() != null) {
    cellD.setCellValue(record.getR25_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR25_book_expo() != null) {
    cellE.setCellValue(record.getR25_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR25_ccf_cont() != null) {
    cellF.setCellValue(record.getR25_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR25_equiv_value() != null) {
    cellG.setCellValue(record.getR25_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR25_rw_obligant() != null) {
    cellH.setCellValue(record.getR25_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR25_rav() != null) {
    cellI.setCellValue(record.getR25_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

row = sheet.getRow(25);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR26_total_book_expo() != null) {
    cellC.setCellValue(record.getR26_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR26_margin_pro() != null) {
    cellD.setCellValue(record.getR26_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR26_book_expo() != null) {
    cellE.setCellValue(record.getR26_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR26_ccf_cont() != null) {
    cellF.setCellValue(record.getR26_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR26_equiv_value() != null) {
    cellG.setCellValue(record.getR26_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR26_rw_obligant() != null) {
    cellH.setCellValue(record.getR26_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR26_rav() != null) {
    cellI.setCellValue(record.getR26_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

row = sheet.getRow(26);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR27_total_book_expo() != null) {
    cellC.setCellValue(record.getR27_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR27_margin_pro() != null) {
    cellD.setCellValue(record.getR27_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR27_book_expo() != null) {
    cellE.setCellValue(record.getR27_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR27_ccf_cont() != null) {
    cellF.setCellValue(record.getR27_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR27_equiv_value() != null) {
    cellG.setCellValue(record.getR27_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR27_rw_obligant() != null) {
    cellH.setCellValue(record.getR27_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR27_rav() != null) {
    cellI.setCellValue(record.getR27_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

row = sheet.getRow(27);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR28_total_book_expo() != null) {
    cellC.setCellValue(record.getR28_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR28_margin_pro() != null) {
    cellD.setCellValue(record.getR28_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR28_book_expo() != null) {
    cellE.setCellValue(record.getR28_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR28_ccf_cont() != null) {
    cellF.setCellValue(record.getR28_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR28_equiv_value() != null) {
    cellG.setCellValue(record.getR28_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR28_rw_obligant() != null) {
    cellH.setCellValue(record.getR28_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR28_rav() != null) {
    cellI.setCellValue(record.getR28_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(28);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR29_total_book_expo() != null) {
    cellC.setCellValue(record.getR29_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR29_margin_pro() != null) {
    cellD.setCellValue(record.getR29_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR29_book_expo() != null) {
    cellE.setCellValue(record.getR29_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR29_ccf_cont() != null) {
    cellF.setCellValue(record.getR29_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR29_equiv_value() != null) {
    cellG.setCellValue(record.getR29_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR29_rw_obligant() != null) {
    cellH.setCellValue(record.getR29_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR29_rav() != null) {
    cellI.setCellValue(record.getR29_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(29);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR30_total_book_expo() != null) {
    cellC.setCellValue(record.getR30_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR30_margin_pro() != null) {
    cellD.setCellValue(record.getR30_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR30_book_expo() != null) {
    cellE.setCellValue(record.getR30_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR30_ccf_cont() != null) {
    cellF.setCellValue(record.getR30_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR30_equiv_value() != null) {
    cellG.setCellValue(record.getR30_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR30_rw_obligant() != null) {
    cellH.setCellValue(record.getR30_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR30_rav() != null) {
    cellI.setCellValue(record.getR30_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(30);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR31_total_book_expo() != null) {
    cellC.setCellValue(record.getR31_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR31_margin_pro() != null) {
    cellD.setCellValue(record.getR31_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR31_book_expo() != null) {
    cellE.setCellValue(record.getR31_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR31_ccf_cont() != null) {
    cellF.setCellValue(record.getR31_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR31_equiv_value() != null) {
    cellG.setCellValue(record.getR31_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR31_rw_obligant() != null) {
    cellH.setCellValue(record.getR31_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR31_rav() != null) {
    cellI.setCellValue(record.getR31_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(31);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR32_total_book_expo() != null) {
    cellC.setCellValue(record.getR32_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR32_margin_pro() != null) {
    cellD.setCellValue(record.getR32_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR32_book_expo() != null) {
    cellE.setCellValue(record.getR32_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR32_ccf_cont() != null) {
    cellF.setCellValue(record.getR32_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR32_equiv_value() != null) {
    cellG.setCellValue(record.getR32_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR32_rw_obligant() != null) {
    cellH.setCellValue(record.getR32_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR32_rav() != null) {
    cellI.setCellValue(record.getR32_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(32);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR33_total_book_expo() != null) {
    cellC.setCellValue(record.getR33_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR33_margin_pro() != null) {
    cellD.setCellValue(record.getR33_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR33_book_expo() != null) {
    cellE.setCellValue(record.getR33_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR33_ccf_cont() != null) {
    cellF.setCellValue(record.getR33_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR33_equiv_value() != null) {
    cellG.setCellValue(record.getR33_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR33_rw_obligant() != null) {
    cellH.setCellValue(record.getR33_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR33_rav() != null) {
    cellI.setCellValue(record.getR33_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(33);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR34_total_book_expo() != null) {
    cellC.setCellValue(record.getR34_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR34_margin_pro() != null) {
    cellD.setCellValue(record.getR34_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR34_book_expo() != null) {
    cellE.setCellValue(record.getR34_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR34_ccf_cont() != null) {
    cellF.setCellValue(record.getR34_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR34_equiv_value() != null) {
    cellG.setCellValue(record.getR34_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR34_rw_obligant() != null) {
    cellH.setCellValue(record.getR34_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR34_rav() != null) {
    cellI.setCellValue(record.getR34_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(34);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR35_total_book_expo() != null) {
    cellC.setCellValue(record.getR35_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR35_margin_pro() != null) {
    cellD.setCellValue(record.getR35_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR35_book_expo() != null) {
    cellE.setCellValue(record.getR35_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR35_ccf_cont() != null) {
    cellF.setCellValue(record.getR35_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR35_equiv_value() != null) {
    cellG.setCellValue(record.getR35_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR35_rw_obligant() != null) {
    cellH.setCellValue(record.getR35_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR35_rav() != null) {
    cellI.setCellValue(record.getR35_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(35);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR36_total_book_expo() != null) {
    cellC.setCellValue(record.getR36_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR36_margin_pro() != null) {
    cellD.setCellValue(record.getR36_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR36_book_expo() != null) {
    cellE.setCellValue(record.getR36_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR36_ccf_cont() != null) {
    cellF.setCellValue(record.getR36_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR36_equiv_value() != null) {
    cellG.setCellValue(record.getR36_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR36_rw_obligant() != null) {
    cellH.setCellValue(record.getR36_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR36_rav() != null) {
    cellI.setCellValue(record.getR36_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(36);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR37_total_book_expo() != null) {
    cellC.setCellValue(record.getR37_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR37_margin_pro() != null) {
    cellD.setCellValue(record.getR37_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR37_book_expo() != null) {
    cellE.setCellValue(record.getR37_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR37_ccf_cont() != null) {
    cellF.setCellValue(record.getR37_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR37_equiv_value() != null) {
    cellG.setCellValue(record.getR37_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR37_rw_obligant() != null) {
    cellH.setCellValue(record.getR37_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR37_rav() != null) {
    cellI.setCellValue(record.getR37_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(37);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR38_total_book_expo() != null) {
    cellC.setCellValue(record.getR38_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR38_margin_pro() != null) {
    cellD.setCellValue(record.getR38_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR38_book_expo() != null) {
    cellE.setCellValue(record.getR38_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR38_ccf_cont() != null) {
    cellF.setCellValue(record.getR38_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR38_equiv_value() != null) {
    cellG.setCellValue(record.getR38_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR38_rw_obligant() != null) {
    cellH.setCellValue(record.getR38_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR38_rav() != null) {
    cellI.setCellValue(record.getR38_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(38);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR39_total_book_expo() != null) {
    cellC.setCellValue(record.getR39_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR39_margin_pro() != null) {
    cellD.setCellValue(record.getR39_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR39_book_expo() != null) {
    cellE.setCellValue(record.getR39_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR39_ccf_cont() != null) {
    cellF.setCellValue(record.getR39_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR39_equiv_value() != null) {
    cellG.setCellValue(record.getR39_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR39_rw_obligant() != null) {
    cellH.setCellValue(record.getR39_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR39_rav() != null) {
    cellI.setCellValue(record.getR39_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(39);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR40_total_book_expo() != null) {
    cellC.setCellValue(record.getR40_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR40_margin_pro() != null) {
    cellD.setCellValue(record.getR40_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR40_book_expo() != null) {
    cellE.setCellValue(record.getR40_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR40_ccf_cont() != null) {
    cellF.setCellValue(record.getR40_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR40_equiv_value() != null) {
    cellG.setCellValue(record.getR40_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR40_rw_obligant() != null) {
    cellH.setCellValue(record.getR40_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR40_rav() != null) {
    cellI.setCellValue(record.getR40_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(40);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR41_total_book_expo() != null) {
    cellC.setCellValue(record.getR41_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR41_margin_pro() != null) {
    cellD.setCellValue(record.getR41_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR41_book_expo() != null) {
    cellE.setCellValue(record.getR41_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR41_ccf_cont() != null) {
    cellF.setCellValue(record.getR41_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR41_equiv_value() != null) {
    cellG.setCellValue(record.getR41_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR41_rw_obligant() != null) {
    cellH.setCellValue(record.getR41_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR41_rav() != null) {
    cellI.setCellValue(record.getR41_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(41);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR42_total_book_expo() != null) {
    cellC.setCellValue(record.getR42_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR42_margin_pro() != null) {
    cellD.setCellValue(record.getR42_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR42_book_expo() != null) {
    cellE.setCellValue(record.getR42_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR42_ccf_cont() != null) {
    cellF.setCellValue(record.getR42_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR42_equiv_value() != null) {
    cellG.setCellValue(record.getR42_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR42_rw_obligant() != null) {
    cellH.setCellValue(record.getR42_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR42_rav() != null) {
    cellI.setCellValue(record.getR42_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(42);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR43_total_book_expo() != null) {
    cellC.setCellValue(record.getR43_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR43_margin_pro() != null) {
    cellD.setCellValue(record.getR43_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR43_book_expo() != null) {
    cellE.setCellValue(record.getR43_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR43_ccf_cont() != null) {
    cellF.setCellValue(record.getR43_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR43_equiv_value() != null) {
    cellG.setCellValue(record.getR43_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR43_rw_obligant() != null) {
    cellH.setCellValue(record.getR43_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR43_rav() != null) {
    cellI.setCellValue(record.getR43_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(43);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR44_total_book_expo() != null) {
    cellC.setCellValue(record.getR44_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR44_margin_pro() != null) {
    cellD.setCellValue(record.getR44_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR44_book_expo() != null) {
    cellE.setCellValue(record.getR44_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR44_ccf_cont() != null) {
    cellF.setCellValue(record.getR44_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR44_equiv_value() != null) {
    cellG.setCellValue(record.getR44_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR44_rw_obligant() != null) {
    cellH.setCellValue(record.getR44_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR44_rav() != null) {
    cellI.setCellValue(record.getR44_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(44);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR45_total_book_expo() != null) {
    cellC.setCellValue(record.getR45_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR45_margin_pro() != null) {
    cellD.setCellValue(record.getR45_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR45_book_expo() != null) {
    cellE.setCellValue(record.getR45_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR45_ccf_cont() != null) {
    cellF.setCellValue(record.getR45_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR45_equiv_value() != null) {
    cellG.setCellValue(record.getR45_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR45_rw_obligant() != null) {
    cellH.setCellValue(record.getR45_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR45_rav() != null) {
    cellI.setCellValue(record.getR45_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(45);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR46_total_book_expo() != null) {
    cellC.setCellValue(record.getR46_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR46_margin_pro() != null) {
    cellD.setCellValue(record.getR46_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR46_book_expo() != null) {
    cellE.setCellValue(record.getR46_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR46_ccf_cont() != null) {
    cellF.setCellValue(record.getR46_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR46_equiv_value() != null) {
    cellG.setCellValue(record.getR46_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR46_rw_obligant() != null) {
    cellH.setCellValue(record.getR46_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR46_rav() != null) {
    cellI.setCellValue(record.getR46_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}


//-------//R61
row = sheet.getRow(60);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR61_total_book_expo() != null) {
    cellC.setCellValue(record.getR61_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR61_margin_pro() != null) {
    cellD.setCellValue(record.getR61_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR61_book_expo() != null) {
    cellE.setCellValue(record.getR61_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR61_ccf_cont() != null) {
    cellF.setCellValue(record.getR61_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR61_equiv_value() != null) {
    cellG.setCellValue(record.getR61_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR61_rw_obligant() != null) {
    cellH.setCellValue(record.getR61_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR61_rav() != null) {
    cellI.setCellValue(record.getR61_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

//----R62
row = sheet.getRow(61);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR62_total_book_expo() != null) {
    cellC.setCellValue(record.getR62_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR62_margin_pro() != null) {
    cellD.setCellValue(record.getR62_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR62_ccf_cont() != null) {
    cellF.setCellValue(record.getR62_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR62_rw_obligant() != null) {
    cellH.setCellValue(record.getR62_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//R63
row = sheet.getRow(62);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR63_total_book_expo() != null) {
    cellC.setCellValue(record.getR63_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR63_margin_pro() != null) {
    cellD.setCellValue(record.getR63_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR63_book_expo() != null) {
    cellE.setCellValue(record.getR63_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR63_ccf_cont() != null) {
    cellF.setCellValue(record.getR63_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR63_equiv_value() != null) {
    cellG.setCellValue(record.getR63_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR63_rw_obligant() != null) {
    cellH.setCellValue(record.getR63_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR63_rav() != null) {
    cellI.setCellValue(record.getR63_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

//R64
row = sheet.getRow(63);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR64_total_book_expo() != null) {
    cellC.setCellValue(record.getR64_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR64_margin_pro() != null) {
    cellD.setCellValue(record.getR64_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR64_ccf_cont() != null) {
    cellF.setCellValue(record.getR64_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR64_rw_obligant() != null) {
    cellH.setCellValue(record.getR64_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//----R65
row = sheet.getRow(64);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR65_total_book_expo() != null) {
    cellC.setCellValue(record.getR65_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR65_margin_pro() != null) {
    cellD.setCellValue(record.getR65_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR65_ccf_cont() != null) {
    cellF.setCellValue(record.getR65_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR65_rw_obligant() != null) {
    cellH.setCellValue(record.getR65_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

////---------R66
row = sheet.getRow(65);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR66_total_book_expo() != null) {
    cellC.setCellValue(record.getR66_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR66_margin_pro() != null) {
    cellD.setCellValue(record.getR66_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR66_ccf_cont() != null) {
    cellF.setCellValue(record.getR66_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR66_rw_obligant() != null) {
    cellH.setCellValue(record.getR66_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

////---------R67


//---R68
row = sheet.getRow(67);


cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR68_ccf_cont() != null) {
    cellF.setCellValue(record.getR68_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR68_equiv_value() != null) {
    cellG.setCellValue(record.getR68_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR68_rw_obligant() != null) {
    cellH.setCellValue(record.getR68_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

////-----------R69
row = sheet.getRow(68);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR69_total_book_expo() != null) {
    cellC.setCellValue(record.getR69_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR69_margin_pro() != null) {
    cellD.setCellValue(record.getR69_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR69_book_expo() != null) {
    cellE.setCellValue(record.getR69_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR69_ccf_cont() != null) {
    cellF.setCellValue(record.getR69_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR69_equiv_value() != null) {
    cellG.setCellValue(record.getR69_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR69_rw_obligant() != null) {
    cellH.setCellValue(record.getR69_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR69_rav() != null) {
    cellI.setCellValue(record.getR69_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

////--------------R70
row = sheet.getRow(69);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR70_total_book_expo() != null) {
    cellC.setCellValue(record.getR70_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR70_margin_pro() != null) {
    cellD.setCellValue(record.getR70_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR70_ccf_cont() != null) {
    cellF.setCellValue(record.getR70_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}


cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR70_rw_obligant() != null) {
    cellH.setCellValue(record.getR70_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

////--------------R71
row = sheet.getRow(70);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR71_total_book_expo() != null) {
    cellC.setCellValue(record.getR71_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR71_margin_pro() != null) {
    cellD.setCellValue(record.getR71_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR71_ccf_cont() != null) {
    cellF.setCellValue(record.getR71_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR71_rw_obligant() != null) {
    cellH.setCellValue(record.getR71_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//-----R72
row = sheet.getRow(71);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR72_total_book_expo() != null) {
    cellC.setCellValue(record.getR72_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR72_margin_pro() != null) {
    cellD.setCellValue(record.getR72_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR72_ccf_cont() != null) {
    cellF.setCellValue(record.getR72_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR72_rw_obligant() != null) {
    cellH.setCellValue(record.getR72_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

////---------R73


////---------R74


row = sheet.getRow(73);

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR74_ccf_cont() != null) {
    cellF.setCellValue(record.getR74_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR74_equiv_value() != null) {
    cellG.setCellValue(record.getR74_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR74_rw_obligant() != null) {
    cellH.setCellValue(record.getR74_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//--------R75


////----------R76

row = sheet.getRow(75);

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR76_ccf_cont() != null) {
    cellF.setCellValue(record.getR76_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR76_equiv_value() != null) {
    cellG.setCellValue(record.getR76_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR76_rw_obligant() != null) {
    cellH.setCellValue(record.getR76_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

////--------------R77


////----------------R78
row = sheet.getRow(77);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR78_total_book_expo() != null) {
    cellC.setCellValue(record.getR78_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR78_margin_pro() != null) {
    cellD.setCellValue(record.getR78_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR78_book_expo() != null) {
    cellE.setCellValue(record.getR78_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR78_ccf_cont() != null) {
    cellF.setCellValue(record.getR78_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR78_equiv_value() != null) {
    cellG.setCellValue(record.getR78_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR78_rw_obligant() != null) {
    cellH.setCellValue(record.getR78_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR78_rav() != null) {
    cellI.setCellValue(record.getR78_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
row = sheet.getRow(78);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR79_total_book_expo() != null) {
    cellC.setCellValue(record.getR79_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR79_margin_pro() != null) {
    cellD.setCellValue(record.getR79_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR79_book_expo() != null) {
    cellE.setCellValue(record.getR79_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR79_ccf_cont() != null) {
    cellF.setCellValue(record.getR79_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR79_equiv_value() != null) {
    cellG.setCellValue(record.getR79_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR79_rw_obligant() != null) {
    cellH.setCellValue(record.getR79_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR79_rav() != null) {
    cellI.setCellValue(record.getR79_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

////--------------R80
row = sheet.getRow(79);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR80_total_book_expo() != null) {
    cellC.setCellValue(record.getR80_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR80_margin_pro() != null) {
    cellD.setCellValue(record.getR80_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR80_book_expo() != null) {
    cellE.setCellValue(record.getR80_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR80_ccf_cont() != null) {
    cellF.setCellValue(record.getR80_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR80_equiv_value() != null) {
    cellG.setCellValue(record.getR80_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR80_rw_obligant() != null) {
    cellH.setCellValue(record.getR80_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR80_rav() != null) {
    cellI.setCellValue(record.getR80_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

//-----------------R81


//-----------R82
row = sheet.getRow(81);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR82_total_book_expo() != null) {
    cellC.setCellValue(record.getR82_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR82_margin_pro() != null) {
    cellD.setCellValue(record.getR82_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}


cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR82_ccf_cont() != null) {
    cellF.setCellValue(record.getR82_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR82_equiv_value() != null) {
    cellG.setCellValue(record.getR82_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR82_rw_obligant() != null) {
    cellH.setCellValue(record.getR82_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//---------R83
row = sheet.getRow(82);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR83_total_book_expo() != null) {
    cellC.setCellValue(record.getR83_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR83_margin_pro() != null) {
    cellD.setCellValue(record.getR83_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR83_book_expo() != null) {
    cellE.setCellValue(record.getR83_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR83_ccf_cont() != null) {
    cellF.setCellValue(record.getR83_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR83_equiv_value() != null) {
    cellG.setCellValue(record.getR83_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR83_rw_obligant() != null) {
    cellH.setCellValue(record.getR83_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR83_rav() != null) {
    cellI.setCellValue(record.getR83_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

//----------------R84
row = sheet.getRow(83);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR84_total_book_expo() != null) {
    cellC.setCellValue(record.getR84_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR84_margin_pro() != null) {
    cellD.setCellValue(record.getR84_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR84_ccf_cont() != null) {
    cellF.setCellValue(record.getR84_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR84_equiv_value() != null) {
    cellG.setCellValue(record.getR84_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR84_rw_obligant() != null) {
    cellH.setCellValue(record.getR84_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//------------R85


//---------------R100
row = sheet.getRow(99);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR100_total_book_expo() != null) {
    cellC.setCellValue(record.getR100_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR100_margin_pro() != null) {
    cellD.setCellValue(record.getR100_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR100_book_expo() != null) {
    cellE.setCellValue(record.getR100_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR100_ccf_cont() != null) {
    cellF.setCellValue(record.getR100_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR100_equiv_value() != null) {
    cellG.setCellValue(record.getR100_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR100_rw_obligant() != null) {
    cellH.setCellValue(record.getR100_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR100_rav() != null) {
    cellI.setCellValue(record.getR100_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

//-----------R101 
row = sheet.getRow(100);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR101_total_book_expo() != null) {
    cellC.setCellValue(record.getR101_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR101_margin_pro() != null) {
    cellD.setCellValue(record.getR101_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR101_ccf_cont() != null) {
    cellF.setCellValue(record.getR101_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}


cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR101_rw_obligant() != null) {
    cellH.setCellValue(record.getR101_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//------------R102
row = sheet.getRow(101);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR102_total_book_expo() != null) {
    cellC.setCellValue(record.getR102_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR102_margin_pro() != null) {
    cellD.setCellValue(record.getR102_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR102_ccf_cont() != null) {
    cellF.setCellValue(record.getR102_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR102_rw_obligant() != null) {
    cellH.setCellValue(record.getR102_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//----------------R103
row = sheet.getRow(102);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR103_total_book_expo() != null) {
    cellC.setCellValue(record.getR103_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

//cellD = row.getCell(3);
//if (cellD == null) cellD = row.createCell(3);
//if (record.getR103_margin_pro() != null) {
//    cellD.setCellValue(record.getR103_margin_pro().doubleValue());
//} else {
//    cellD.setCellValue(0);
//}


//
//cellF = row.getCell(5);
//if (cellF == null) cellF = row.createCell(5);
//if (record.getR103_ccf_cont() != null) {
//    cellF.setCellValue(record.getR103_ccf_cont().doubleValue());
//} else {
//    cellF.setCellValue(0);
//}
//
//
//
//cellH = row.getCell(7);
//if (cellH == null) cellH = row.createCell(7);
//if (record.getR103_rw_obligant() != null) {
//    cellH.setCellValue(record.getR103_rw_obligant().doubleValue());
//} else {
//    cellH.setCellValue(0);
//}

//-----------------R104

//---------//---------R105


row = sheet.getRow(104);




cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR105_ccf_cont() != null) {
    cellF.setCellValue(record.getR105_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR105_equiv_value() != null) {
    cellG.setCellValue(record.getR105_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR105_rw_obligant() != null) {
    cellH.setCellValue(record.getR105_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//---------//----------------R106
row = sheet.getRow(105);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR106_total_book_expo() != null) {
    cellC.setCellValue(record.getR106_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR106_margin_pro() != null) {
    cellD.setCellValue(record.getR106_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR106_book_expo() != null) {
    cellE.setCellValue(record.getR106_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR106_ccf_cont() != null) {
    cellF.setCellValue(record.getR106_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR106_equiv_value() != null) {
    cellG.setCellValue(record.getR106_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR106_rw_obligant() != null) {
    cellH.setCellValue(record.getR106_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR106_rav() != null) {
    cellI.setCellValue(record.getR106_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

//---------//---------R107
row = sheet.getRow(106);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR107_total_book_expo() != null) {
    cellC.setCellValue(record.getR107_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR107_margin_pro() != null) {
    cellD.setCellValue(record.getR107_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR107_book_expo() != null) {
    cellE.setCellValue(record.getR107_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR107_ccf_cont() != null) {
    cellF.setCellValue(record.getR107_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR107_equiv_value() != null) {
    cellG.setCellValue(record.getR107_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR107_rw_obligant() != null) {
    cellH.setCellValue(record.getR107_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR107_rav() != null) {
    cellI.setCellValue(record.getR107_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

//--------------R108
row = sheet.getRow(107);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR108_total_book_expo() != null) {
    cellC.setCellValue(record.getR108_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR108_margin_pro() != null) {
    cellD.setCellValue(record.getR108_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR108_ccf_cont() != null) {
    cellF.setCellValue(record.getR108_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR108_rw_obligant() != null) {
    cellH.setCellValue(record.getR108_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//---------------R109
row = sheet.getRow(108);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR109_total_book_expo() != null) {
    cellC.setCellValue(record.getR109_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR109_margin_pro() != null) {
    cellD.setCellValue(record.getR109_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR109_ccf_cont() != null) {
    cellF.setCellValue(record.getR109_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR109_rw_obligant() != null) {
    cellH.setCellValue(record.getR109_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//------R110
row = sheet.getRow(109);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR110_total_book_expo() != null) {
    cellC.setCellValue(record.getR110_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR110_margin_pro() != null) {
    cellD.setCellValue(record.getR110_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



//cellF = row.getCell(5);
//if (cellF == null) cellF = row.createCell(5);
//if (record.getR110_ccf_cont() != null) {
//    cellF.setCellValue(record.getR110_ccf_cont().doubleValue());
//} else {
//    cellF.setCellValue(0);
//}



//cellH = row.getCell(7);
//if (cellH == null) cellH = row.createCell(7);
//if (record.getR110_rw_obligant() != null) {
//    cellH.setCellValue(record.getR110_rw_obligant().doubleValue());
//} else {
//    cellH.setCellValue(0);
//}

//---------R111


//--------------R112

row = sheet.getRow(111);

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR112_ccf_cont() != null) {
    cellF.setCellValue(record.getR112_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR112_equiv_value() != null) {
    cellG.setCellValue(record.getR112_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR112_rw_obligant() != null) {
    cellH.setCellValue(record.getR112_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//---------//-------------R113

row = sheet.getRow(112);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR113_total_book_expo() != null) {
    cellC.setCellValue(record.getR113_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR113_margin_pro() != null) {
    cellD.setCellValue(record.getR113_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR113_book_expo() != null) {
    cellE.setCellValue(record.getR113_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR113_ccf_cont() != null) {
    cellF.setCellValue(record.getR113_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR113_equiv_value() != null) {
    cellG.setCellValue(record.getR113_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR113_rw_obligant() != null) {
    cellH.setCellValue(record.getR113_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR113_rav() != null) {
    cellI.setCellValue(record.getR113_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
//-----------R114
row = sheet.getRow(113);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR114_total_book_expo() != null) {
    cellC.setCellValue(record.getR114_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}



cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR114_margin_pro() != null) {
    cellD.setCellValue(record.getR114_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR114_book_expo() != null) {
    cellE.setCellValue(record.getR114_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR114_ccf_cont() != null) {
    cellF.setCellValue(record.getR114_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR114_equiv_value() != null) {
    cellG.setCellValue(record.getR114_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR114_rw_obligant() != null) {
    cellH.setCellValue(record.getR114_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR114_rav() != null) {
    cellI.setCellValue(record.getR114_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

//---------//--------------R115
row = sheet.getRow(114);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR115_total_book_expo() != null) {
    cellC.setCellValue(record.getR115_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR115_margin_pro() != null) {
    cellD.setCellValue(record.getR115_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR115_ccf_cont() != null) {
    cellF.setCellValue(record.getR115_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR115_rw_obligant() != null) {
    cellH.setCellValue(record.getR115_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//--------------R116
row = sheet.getRow(115);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR116_total_book_expo() != null) {
    cellC.setCellValue(record.getR116_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR116_margin_pro() != null) {
    cellD.setCellValue(record.getR116_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR116_ccf_cont() != null) {
    cellF.setCellValue(record.getR116_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR116_rw_obligant() != null) {
    cellH.setCellValue(record.getR116_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//--------------R117
row = sheet.getRow(116);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR117_total_book_expo() != null) {
    cellC.setCellValue(record.getR117_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

//cellD = row.getCell(3);
//if (cellD == null) cellD = row.createCell(3);
//if (record.getR117_margin_pro() != null) {
//    cellD.setCellValue(record.getR117_margin_pro().doubleValue());
//} else {
//    cellD.setCellValue(0);
//}


//
//cellF = row.getCell(5);
//if (cellF == null) cellF = row.createCell(5);
//if (record.getR117_ccf_cont() != null) {
//    cellF.setCellValue(record.getR117_ccf_cont().doubleValue());
//} else {
//    cellF.setCellValue(0);
//}



//cellH = row.getCell(7);
//if (cellH == null) cellH = row.createCell(7);
//if (record.getR117_rw_obligant() != null) {
//    cellH.setCellValue(record.getR117_rw_obligant().doubleValue());
//} else {
//    cellH.setCellValue(0);
//}

//------------R118


//---------------R119

row = sheet.getRow(118);

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR119_ccf_cont() != null) {
    cellF.setCellValue(record.getR119_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR119_equiv_value() != null) {
    cellG.setCellValue(record.getR119_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR119_rw_obligant() != null) {
    cellH.setCellValue(record.getR119_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//---------------R120
row = sheet.getRow(119);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR120_total_book_expo() != null) {
    cellC.setCellValue(record.getR120_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR120_margin_pro() != null) {
    cellD.setCellValue(record.getR120_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR120_book_expo() != null) {
    cellE.setCellValue(record.getR120_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR120_ccf_cont() != null) {
    cellF.setCellValue(record.getR120_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR120_equiv_value() != null) {
    cellG.setCellValue(record.getR120_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR120_rw_obligant() != null) {
    cellH.setCellValue(record.getR120_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR120_rav() != null) {
    cellI.setCellValue(record.getR120_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}


row = sheet.getRow(120);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR121_total_book_expo() != null) {
    cellC.setCellValue(record.getR121_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR121_margin_pro() != null) {
    cellD.setCellValue(record.getR121_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR121_book_expo() != null) {
    cellE.setCellValue(record.getR121_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR121_ccf_cont() != null) {
    cellF.setCellValue(record.getR121_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR121_equiv_value() != null) {
    cellG.setCellValue(record.getR121_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR121_rw_obligant() != null) {
    cellH.setCellValue(record.getR121_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR121_rav() != null) {
    cellI.setCellValue(record.getR121_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

//----------------R122
row = sheet.getRow(121);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR122_total_book_expo() != null) {
    cellC.setCellValue(record.getR122_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR122_margin_pro() != null) {
    cellD.setCellValue(record.getR122_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR122_ccf_cont() != null) {
    cellF.setCellValue(record.getR122_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}


cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR122_rw_obligant() != null) {
    cellH.setCellValue(record.getR122_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//------------R123
row = sheet.getRow(122);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR123_total_book_expo() != null) {
    cellC.setCellValue(record.getR123_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR123_margin_pro() != null) {
    cellD.setCellValue(record.getR123_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}


cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR123_ccf_cont() != null) {
    cellF.setCellValue(record.getR123_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR123_rw_obligant() != null) {
    cellH.setCellValue(record.getR123_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//-----------R124
row = sheet.getRow(123);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR124_total_book_expo() != null) {
    cellC.setCellValue(record.getR124_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR124_margin_pro() != null) {
    cellD.setCellValue(record.getR124_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR124_ccf_cont() != null) {
    cellF.setCellValue(record.getR124_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR124_rw_obligant() != null) {
    cellH.setCellValue(record.getR124_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//-----------R125
//-------------R126

row = sheet.getRow(125);


cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR126_ccf_cont() != null) {
    cellF.setCellValue(record.getR126_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR126_equiv_value() != null) {
    cellG.setCellValue(record.getR126_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR126_rw_obligant() != null) {
    cellH.setCellValue(record.getR126_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//----------R127
row = sheet.getRow(126);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR127_total_book_expo() != null) {
    cellC.setCellValue(record.getR127_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR127_margin_pro() != null) {
    cellD.setCellValue(record.getR127_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR127_book_expo() != null) {
    cellE.setCellValue(record.getR127_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR127_ccf_cont() != null) {
    cellF.setCellValue(record.getR127_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR127_equiv_value() != null) {
    cellG.setCellValue(record.getR127_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR127_rw_obligant() != null) {
    cellH.setCellValue(record.getR127_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR127_rav() != null) {
    cellI.setCellValue(record.getR127_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

//----------R128

row = sheet.getRow(127);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR128_total_book_expo() != null) {
    cellC.setCellValue(record.getR128_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR128_margin_pro() != null) {
    cellD.setCellValue(record.getR128_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR128_ccf_cont() != null) {
    cellF.setCellValue(record.getR128_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR128_rw_obligant() != null) {
    cellH.setCellValue(record.getR128_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

//----------R129
row = sheet.getRow(128);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR129_total_book_expo() != null) {
    cellC.setCellValue(record.getR129_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR129_margin_pro() != null) {
    cellD.setCellValue(record.getR129_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR129_book_expo() != null) {
    cellE.setCellValue(record.getR129_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR129_ccf_cont() != null) {
    cellF.setCellValue(record.getR129_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR129_equiv_value() != null) {
    cellG.setCellValue(record.getR129_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR129_rw_obligant() != null) {
    cellH.setCellValue(record.getR129_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR129_rav() != null) {
    cellI.setCellValue(record.getR129_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
//----R130
row = sheet.getRow(129);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR130_total_book_expo() != null) {
    cellC.setCellValue(record.getR130_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR130_margin_pro() != null) {
    cellD.setCellValue(record.getR130_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR130_book_expo() != null) {
    cellE.setCellValue(record.getR130_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR130_ccf_cont() != null) {
    cellF.setCellValue(record.getR130_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR130_equiv_value() != null) {
    cellG.setCellValue(record.getR130_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR130_rw_obligant() != null) {
    cellH.setCellValue(record.getR130_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR130_rav() != null) {
    cellI.setCellValue(record.getR130_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
//-----R131

row = sheet.getRow(130);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR131_total_book_expo() != null) {
    cellC.setCellValue(record.getR131_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR131_margin_pro() != null) {
    cellD.setCellValue(record.getR131_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR131_book_expo() != null) {
    cellE.setCellValue(record.getR131_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR131_ccf_cont() != null) {
    cellF.setCellValue(record.getR131_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR131_equiv_value() != null) {
    cellG.setCellValue(record.getR131_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR131_rw_obligant() != null) {
    cellH.setCellValue(record.getR131_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR131_rav() != null) {
    cellI.setCellValue(record.getR131_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
//------------R132
row = sheet.getRow(131);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR132_total_book_expo() != null) {
    cellC.setCellValue(record.getR132_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR132_margin_pro() != null) {
    cellD.setCellValue(record.getR132_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR132_book_expo() != null) {
    cellE.setCellValue(record.getR132_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR132_ccf_cont() != null) {
    cellF.setCellValue(record.getR132_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR132_equiv_value() != null) {
    cellG.setCellValue(record.getR132_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR132_rw_obligant() != null) {
    cellH.setCellValue(record.getR132_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR132_rav() != null) {
    cellI.setCellValue(record.getR132_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}

//-------R133

//-------R134 CAL



//------------R135

//----------R148
row = sheet.getRow(147);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR148_total_book_expo() != null) {
    cellC.setCellValue(record.getR148_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR148_margin_pro() != null) {
    cellD.setCellValue(record.getR148_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR148_book_expo() != null) {
    cellE.setCellValue(record.getR148_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR148_ccf_cont() != null) {
    cellF.setCellValue(record.getR148_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR148_equiv_value() != null) {
    cellG.setCellValue(record.getR148_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR148_rw_obligant() != null) {
    cellH.setCellValue(record.getR148_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR148_rav() != null) {
    cellI.setCellValue(record.getR148_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}
//-----------R149
row = sheet.getRow(148);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR149_total_book_expo() != null) {
    cellC.setCellValue(record.getR149_total_book_expo().doubleValue());
} else {
    cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR149_margin_pro() != null) {
    cellD.setCellValue(record.getR149_margin_pro().doubleValue());
} else {
    cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR149_book_expo() != null) {
    cellE.setCellValue(record.getR149_book_expo().doubleValue());
} else {
    cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR149_ccf_cont() != null) {
    cellF.setCellValue(record.getR149_ccf_cont().doubleValue());
} else {
    cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR149_equiv_value() != null) {
    cellG.setCellValue(record.getR149_equiv_value().doubleValue());
} else {
    cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR149_rw_obligant() != null) {
    cellH.setCellValue(record.getR149_rw_obligant().doubleValue());
} else {
    cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR149_rav() != null) {
    cellI.setCellValue(record.getR149_rav().doubleValue());
} else {
    cellI.setCellValue(0);
}


//--------R150
row = sheet.getRow(149);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR150_total_book_expo() != null) {
  cellC.setCellValue(record.getR150_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR150_margin_pro() != null) {
  cellD.setCellValue(record.getR150_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR150_ccf_cont() != null) {
  cellF.setCellValue(record.getR150_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR150_rw_obligant() != null) {
  cellH.setCellValue(record.getR150_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//-----------R151
row = sheet.getRow(150);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR151_total_book_expo() != null) {
  cellC.setCellValue(record1.getR151_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR151_margin_pro() != null) {
  cellD.setCellValue(record1.getR151_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record1.getR151_book_expo() != null) {
  cellE.setCellValue(record1.getR151_book_expo().doubleValue());
} else {
  cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR151_ccf_cont() != null) {
  cellF.setCellValue(record1.getR151_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record1.getR151_equiv_value() != null) {
  cellG.setCellValue(record1.getR151_equiv_value().doubleValue());
} else {
  cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR151_rw_obligant() != null) {
  cellH.setCellValue(record1.getR151_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record1.getR151_rav() != null) {
  cellI.setCellValue(record1.getR151_rav().doubleValue());
} else {
  cellI.setCellValue(0);
}
//------//------------R152
row = sheet.getRow(151);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR152_total_book_expo() != null) {
  cellC.setCellValue(record1.getR152_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR152_margin_pro() != null) {
  cellD.setCellValue(record1.getR152_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR152_ccf_cont() != null) {
  cellF.setCellValue(record1.getR152_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR152_rw_obligant() != null) {
  cellH.setCellValue(record1.getR152_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//-----------R153
row = sheet.getRow(152);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR153_total_book_expo() != null) {
  cellC.setCellValue(record1.getR153_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR153_margin_pro() != null) {
  cellD.setCellValue(record1.getR153_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR153_ccf_cont() != null) {
  cellF.setCellValue(record1.getR153_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR153_rw_obligant() != null) {
  cellH.setCellValue(record1.getR153_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//-----------R154
row = sheet.getRow(153);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR154_total_book_expo() != null) {
  cellC.setCellValue(record1.getR154_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR154_margin_pro() != null) {
  cellD.setCellValue(record1.getR154_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR154_ccf_cont() != null) {
  cellF.setCellValue(record1.getR154_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR154_rw_obligant() != null) {
  cellH.setCellValue(record1.getR154_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//---------R155

//----------R156

row = sheet.getRow(155);

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR156_ccf_cont() != null) {
  cellF.setCellValue(record1.getR156_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record1.getR156_equiv_value() != null) {
  cellG.setCellValue(record1.getR156_equiv_value().doubleValue());
} else {
  cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR156_rw_obligant() != null) {
  cellH.setCellValue(record1.getR156_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//------//-------R157
row = sheet.getRow(156);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR157_total_book_expo() != null) {
  cellC.setCellValue(record1.getR157_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR157_margin_pro() != null) {
  cellD.setCellValue(record1.getR157_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record1.getR157_book_expo() != null) {
  cellE.setCellValue(record1.getR157_book_expo().doubleValue());
} else {
  cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR157_ccf_cont() != null) {
  cellF.setCellValue(record1.getR157_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record1.getR157_equiv_value() != null) {
  cellG.setCellValue(record1.getR157_equiv_value().doubleValue());
} else {
  cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR157_rw_obligant() != null) {
  cellH.setCellValue(record1.getR157_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record1.getR157_rav() != null) {
  cellI.setCellValue(record1.getR157_rav().doubleValue());
} else {
  cellI.setCellValue(0);
}

//------//--------R158

row = sheet.getRow(157);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR158_total_book_expo() != null) {
  cellC.setCellValue(record1.getR158_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR158_margin_pro() != null) {
  cellD.setCellValue(record1.getR158_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}


cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR158_ccf_cont() != null) {
  cellF.setCellValue(record1.getR158_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR158_rw_obligant() != null) {
  cellH.setCellValue(record1.getR158_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//---------R159
row = sheet.getRow(158);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR159_total_book_expo() != null) {
  cellC.setCellValue(record1.getR159_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR159_margin_pro() != null) {
  cellD.setCellValue(record1.getR159_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR159_ccf_cont() != null) {
  cellF.setCellValue(record1.getR159_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR159_rw_obligant() != null) {
  cellH.setCellValue(record1.getR159_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//----------R160
row = sheet.getRow(159);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR160_total_book_expo() != null) {
  cellC.setCellValue(record1.getR160_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR160_margin_pro() != null) {
  cellD.setCellValue(record1.getR160_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR160_ccf_cont() != null) {
  cellF.setCellValue(record1.getR160_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR160_rw_obligant() != null) {
  cellH.setCellValue(record1.getR160_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//--r161
//--------R162


row = sheet.getRow(161);

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR162_ccf_cont() != null) {
  cellF.setCellValue(record1.getR162_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record1.getR162_equiv_value() != null) {
  cellG.setCellValue(record1.getR162_equiv_value().doubleValue());
} else {
  cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR162_rw_obligant() != null) {
  cellH.setCellValue(record1.getR162_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//------//------R163
row = sheet.getRow(162);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR163_total_book_expo() != null) {
  cellC.setCellValue(record1.getR163_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR163_margin_pro() != null) {
  cellD.setCellValue(record1.getR163_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record1.getR163_book_expo() != null) {
  cellE.setCellValue(record1.getR163_book_expo().doubleValue());
} else {
  cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR163_ccf_cont() != null) {
  cellF.setCellValue(record1.getR163_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record1.getR163_equiv_value() != null) {
  cellG.setCellValue(record1.getR163_equiv_value().doubleValue());
} else {
  cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR163_rw_obligant() != null) {
  cellH.setCellValue(record1.getR163_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record1.getR163_rav() != null) {
  cellI.setCellValue(record1.getR163_rav().doubleValue());
} else {
  cellI.setCellValue(0);
}

//------//-------R164
row = sheet.getRow(163);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR164_total_book_expo() != null) {
  cellC.setCellValue(record1.getR164_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR164_margin_pro() != null) {
  cellD.setCellValue(record1.getR164_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR164_ccf_cont() != null) {
  cellF.setCellValue(record1.getR164_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR164_rw_obligant() != null) {
  cellH.setCellValue(record1.getR164_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//------//------R165
row = sheet.getRow(164);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR165_total_book_expo() != null) {
  cellC.setCellValue(record1.getR165_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR165_margin_pro() != null) {
  cellD.setCellValue(record1.getR165_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR165_ccf_cont() != null) {
  cellF.setCellValue(record1.getR165_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR165_rw_obligant() != null) {
  cellH.setCellValue(record1.getR165_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}


//-----------R166
row = sheet.getRow(165);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR166_total_book_expo() != null) {
  cellC.setCellValue(record1.getR166_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR166_margin_pro() != null) {
  cellD.setCellValue(record1.getR166_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR166_ccf_cont() != null) {
  cellF.setCellValue(record1.getR166_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR166_rw_obligant() != null) {
  cellH.setCellValue(record1.getR166_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//-----------R167

//----------R168

row = sheet.getRow(167);

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR168_ccf_cont() != null) {
  cellF.setCellValue(record1.getR168_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record1.getR168_equiv_value() != null) {
  cellG.setCellValue(record1.getR168_equiv_value().doubleValue());
} else {
  cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR168_rw_obligant() != null) {
  cellH.setCellValue(record1.getR168_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//------------R169
row = sheet.getRow(168);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR169_total_book_expo() != null) {
  cellC.setCellValue(record1.getR169_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR169_margin_pro() != null) {
  cellD.setCellValue(record1.getR169_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record1.getR169_book_expo() != null) {
  cellE.setCellValue(record1.getR169_book_expo().doubleValue());
} else {
  cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR169_ccf_cont() != null) {
  cellF.setCellValue(record1.getR169_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record1.getR169_equiv_value() != null) {
  cellG.setCellValue(record1.getR169_equiv_value().doubleValue());
} else {
  cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR169_rw_obligant() != null) {
  cellH.setCellValue(record1.getR169_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record1.getR169_rav() != null) {
  cellI.setCellValue(record1.getR169_rav().doubleValue());
} else {
  cellI.setCellValue(0);
}

//------//--------R170
row = sheet.getRow(169);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR170_total_book_expo() != null) {
  cellC.setCellValue(record1.getR170_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR170_margin_pro() != null) {
  cellD.setCellValue(record1.getR170_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR170_ccf_cont() != null) {
  cellF.setCellValue(record1.getR170_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR170_rw_obligant() != null) {
  cellH.setCellValue(record1.getR170_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//------------R171
row = sheet.getRow(170);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR171_total_book_expo() != null) {
  cellC.setCellValue(record1.getR171_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR171_margin_pro() != null) {
  cellD.setCellValue(record1.getR171_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR171_ccf_cont() != null) {
  cellF.setCellValue(record1.getR171_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR171_rw_obligant() != null) {
  cellH.setCellValue(record1.getR171_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//------------R172
row = sheet.getRow(171);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR172_total_book_expo() != null) {
  cellC.setCellValue(record1.getR172_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR172_margin_pro() != null) {
  cellD.setCellValue(record1.getR172_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR172_ccf_cont() != null) {
  cellF.setCellValue(record1.getR172_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR172_rw_obligant() != null) {
  cellH.setCellValue(record1.getR172_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//---------R173

//------//------R174
row = sheet.getRow(173);


cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR174_ccf_cont() != null) {
  cellF.setCellValue(record1.getR174_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record1.getR174_equiv_value() != null) {
  cellG.setCellValue(record1.getR174_equiv_value().doubleValue());
} else {
  cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR174_rw_obligant() != null) {
  cellH.setCellValue(record1.getR174_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//------------R175

//----------R176

row = sheet.getRow(175);

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR176_ccf_cont() != null) {
  cellF.setCellValue(record1.getR176_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record1.getR176_equiv_value() != null) {
  cellG.setCellValue(record1.getR176_equiv_value().doubleValue());
} else {
  cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR176_rw_obligant() != null) {
  cellH.setCellValue(record1.getR176_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//------//------R177

//------//------R178
row = sheet.getRow(177);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR178_total_book_expo() != null) {
  cellC.setCellValue(record1.getR178_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR178_margin_pro() != null) {
  cellD.setCellValue(record1.getR178_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record1.getR178_book_expo() != null) {
  cellE.setCellValue(record1.getR178_book_expo().doubleValue());
} else {
  cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR178_ccf_cont() != null) {
  cellF.setCellValue(record1.getR178_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record1.getR178_equiv_value() != null) {
  cellG.setCellValue(record1.getR178_equiv_value().doubleValue());
} else {
  cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR178_rw_obligant() != null) {
  cellH.setCellValue(record1.getR178_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record1.getR178_rav() != null) {
  cellI.setCellValue(record1.getR178_rav().doubleValue());
} else {
  cellI.setCellValue(0);
}

//--------R179
row = sheet.getRow(178);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR179_total_book_expo() != null) {
  cellC.setCellValue(record1.getR179_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR179_margin_pro() != null) {
  cellD.setCellValue(record1.getR179_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR179_ccf_cont() != null) {
  cellF.setCellValue(record1.getR179_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR179_rw_obligant() != null) {
  cellH.setCellValue(record1.getR179_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//------//--------R180
row = sheet.getRow(179);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR180_total_book_expo() != null) {
  cellC.setCellValue(record1.getR180_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR180_margin_pro() != null) {
  cellD.setCellValue(record1.getR180_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR180_ccf_cont() != null) {
  cellF.setCellValue(record1.getR180_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR180_rw_obligant() != null) {
  cellH.setCellValue(record1.getR180_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//------//----------R181
row = sheet.getRow(180);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR181_total_book_expo() != null) {
  cellC.setCellValue(record1.getR181_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR181_margin_pro() != null) {
  cellD.setCellValue(record1.getR181_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR181_ccf_cont() != null) {
  cellF.setCellValue(record1.getR181_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}



cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR181_rw_obligant() != null) {
  cellH.setCellValue(record1.getR181_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//------//--------R182

//------//--------R183

row = sheet.getRow(182);

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR183_ccf_cont() != null) {
  cellF.setCellValue(record1.getR183_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record1.getR183_equiv_value() != null) {
  cellG.setCellValue(record1.getR183_equiv_value().doubleValue());
} else {
  cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR183_rw_obligant() != null) {
  cellH.setCellValue(record1.getR183_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//--------R184

//----------R185
row = sheet.getRow(184);

cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR185_total_book_expo() != null) {
  cellC.setCellValue(record1.getR185_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR185_margin_pro() != null) {
  cellD.setCellValue(record1.getR185_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}

cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record1.getR185_book_expo() != null) {
  cellE.setCellValue(record1.getR185_book_expo().doubleValue());
} else {
  cellE.setCellValue(0);
}

cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR185_ccf_cont() != null) {
  cellF.setCellValue(record1.getR185_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record1.getR185_equiv_value() != null) {
  cellG.setCellValue(record1.getR185_equiv_value().doubleValue());
} else {
  cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR185_rw_obligant() != null) {
  cellH.setCellValue(record1.getR185_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record1.getR185_rav() != null) {
  cellI.setCellValue(record1.getR185_rav().doubleValue());
} else {
  cellI.setCellValue(0);
}

//------------R186
row = sheet.getRow(185);



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR186_ccf_cont() != null) {
  cellF.setCellValue(record1.getR186_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record1.getR186_equiv_value() != null) {
  cellG.setCellValue(record1.getR186_equiv_value().doubleValue());
} else {
  cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR186_rw_obligant() != null) {
  cellH.setCellValue(record1.getR186_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

//----------R187

//--------R189
row = sheet.getRow(188);
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record1.getR189_total_book_expo() != null) {
  cellC.setCellValue(record1.getR189_total_book_expo().doubleValue());
} else {
  cellC.setCellValue(0);
}

cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record1.getR189_margin_pro() != null) {
  cellD.setCellValue(record1.getR189_margin_pro().doubleValue());
} else {
  cellD.setCellValue(0);
}



cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record1.getR189_ccf_cont() != null) {
  cellF.setCellValue(record1.getR189_ccf_cont().doubleValue());
} else {
  cellF.setCellValue(0);
}

cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record1.getR189_equiv_value() != null) {
  cellG.setCellValue(record1.getR189_equiv_value().doubleValue());
} else {
  cellG.setCellValue(0);
}

cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record1.getR189_rw_obligant() != null) {
  cellH.setCellValue(record1.getR189_rw_obligant().doubleValue());
} else {
  cellH.setCellValue(0);
}

			
				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	
	public byte[] getExcelOFF_BS_ITEMSARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}
		
		List<OFF_BS_ITEMS_Archival_Summary_Entity1> dataList = OFF_BS_ITEMS_Archival_Summary_Repo1
				.getdatabydateListarchival(dateformat.parse(todate), version);
		

		
		List<OFF_BS_ITEMS_Archival_Summary_Entity2> dataList1 = OFF_BS_ITEMS_Archival_Summary_Repo2
				.getdatabydateListarchival(dateformat.parse(todate), version);
		

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for OFF_BS_ITEMS report. Returning empty result.");
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

			int startRow =10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					OFF_BS_ITEMS_Archival_Summary_Entity1 record = dataList.get(i);
					OFF_BS_ITEMS_Archival_Summary_Entity2 record1 = dataList1.get(i);
				
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					
					
					  Cell  cellC ,cellD , cellE , cellF , cellG , cellH, cellI;



					////----------R12


					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR12_total_book_expo() != null) {
					    cellC.setCellValue(record.getR12_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR12_margin_pro() != null) {
					    cellD.setCellValue(record.getR12_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR12_book_expo() != null) {
					    cellE.setCellValue(record.getR12_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR12_ccf_cont() != null) {
					    cellF.setCellValue(record.getR12_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR12_equiv_value() != null) {
					    cellG.setCellValue(record.getR12_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR12_rw_obligant() != null) {
					    cellH.setCellValue(record.getR12_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR12_rav() != null) {
					    cellI.setCellValue(record.getR12_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}


					// R13

					row = sheet.getRow(12);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR13_total_book_expo() != null) {
					    cellC.setCellValue(record.getR13_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR13_margin_pro() != null) {
					    cellD.setCellValue(record.getR13_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR13_ccf_cont() != null) {
					    cellF.setCellValue(record.getR13_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR13_rw_obligant() != null) {
					    cellH.setCellValue(record.getR13_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//--R14
					row = sheet.getRow(13);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR14_total_book_expo() != null) {
					    cellC.setCellValue(record.getR14_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR14_margin_pro() != null) {
					    cellD.setCellValue(record.getR14_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR14_ccf_cont() != null) {
					    cellF.setCellValue(record.getR14_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR14_rw_obligant() != null) {
					    cellH.setCellValue(record.getR14_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}


					//------R15 TO 46

					row = sheet.getRow(14);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR15_total_book_expo() != null) {
					    cellC.setCellValue(record.getR15_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR15_margin_pro() != null) {
					    cellD.setCellValue(record.getR15_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR15_book_expo() != null) {
					    cellE.setCellValue(record.getR15_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR15_ccf_cont() != null) {
					    cellF.setCellValue(record.getR15_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR15_equiv_value() != null) {
					    cellG.setCellValue(record.getR15_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR15_rw_obligant() != null) {
					    cellH.setCellValue(record.getR15_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR15_rav() != null) {
					    cellI.setCellValue(record.getR15_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					row = sheet.getRow(15);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR16_total_book_expo() != null) {
					    cellC.setCellValue(record.getR16_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR16_margin_pro() != null) {
					    cellD.setCellValue(record.getR16_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR16_book_expo() != null) {
					    cellE.setCellValue(record.getR16_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR16_ccf_cont() != null) {
					    cellF.setCellValue(record.getR16_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR16_equiv_value() != null) {
					    cellG.setCellValue(record.getR16_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR16_rw_obligant() != null) {
					    cellH.setCellValue(record.getR16_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR16_rav() != null) {
					    cellI.setCellValue(record.getR16_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					row = sheet.getRow(16);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR17_total_book_expo() != null) {
					    cellC.setCellValue(record.getR17_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR17_margin_pro() != null) {
					    cellD.setCellValue(record.getR17_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR17_book_expo() != null) {
					    cellE.setCellValue(record.getR17_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR17_ccf_cont() != null) {
					    cellF.setCellValue(record.getR17_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR17_equiv_value() != null) {
					    cellG.setCellValue(record.getR17_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR17_rw_obligant() != null) {
					    cellH.setCellValue(record.getR17_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR17_rav() != null) {
					    cellI.setCellValue(record.getR17_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					row = sheet.getRow(17);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR18_total_book_expo() != null) {
					    cellC.setCellValue(record.getR18_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR18_margin_pro() != null) {
					    cellD.setCellValue(record.getR18_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR18_book_expo() != null) {
					    cellE.setCellValue(record.getR18_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR18_ccf_cont() != null) {
					    cellF.setCellValue(record.getR18_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR18_equiv_value() != null) {
					    cellG.setCellValue(record.getR18_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR18_rw_obligant() != null) {
					    cellH.setCellValue(record.getR18_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR18_rav() != null) {
					    cellI.setCellValue(record.getR18_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					row = sheet.getRow(18);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR19_total_book_expo() != null) {
					    cellC.setCellValue(record.getR19_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR19_margin_pro() != null) {
					    cellD.setCellValue(record.getR19_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR19_book_expo() != null) {
					    cellE.setCellValue(record.getR19_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR19_ccf_cont() != null) {
					    cellF.setCellValue(record.getR19_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR19_equiv_value() != null) {
					    cellG.setCellValue(record.getR19_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR19_rw_obligant() != null) {
					    cellH.setCellValue(record.getR19_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR19_rav() != null) {
					    cellI.setCellValue(record.getR19_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					row = sheet.getRow(19);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR20_total_book_expo() != null) {
					    cellC.setCellValue(record.getR20_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR20_margin_pro() != null) {
					    cellD.setCellValue(record.getR20_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR20_book_expo() != null) {
					    cellE.setCellValue(record.getR20_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR20_ccf_cont() != null) {
					    cellF.setCellValue(record.getR20_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR20_equiv_value() != null) {
					    cellG.setCellValue(record.getR20_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR20_rw_obligant() != null) {
					    cellH.setCellValue(record.getR20_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR20_rav() != null) {
					    cellI.setCellValue(record.getR20_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					row = sheet.getRow(20);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR21_total_book_expo() != null) {
					    cellC.setCellValue(record.getR21_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR21_margin_pro() != null) {
					    cellD.setCellValue(record.getR21_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR21_book_expo() != null) {
					    cellE.setCellValue(record.getR21_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR21_ccf_cont() != null) {
					    cellF.setCellValue(record.getR21_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR21_equiv_value() != null) {
					    cellG.setCellValue(record.getR21_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR21_rw_obligant() != null) {
					    cellH.setCellValue(record.getR21_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR21_rav() != null) {
					    cellI.setCellValue(record.getR21_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					row = sheet.getRow(21);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR22_total_book_expo() != null) {
					    cellC.setCellValue(record.getR22_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR22_margin_pro() != null) {
					    cellD.setCellValue(record.getR22_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR22_book_expo() != null) {
					    cellE.setCellValue(record.getR22_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR22_ccf_cont() != null) {
					    cellF.setCellValue(record.getR22_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR22_equiv_value() != null) {
					    cellG.setCellValue(record.getR22_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR22_rw_obligant() != null) {
					    cellH.setCellValue(record.getR22_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR22_rav() != null) {
					    cellI.setCellValue(record.getR22_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					row = sheet.getRow(22);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR23_total_book_expo() != null) {
					    cellC.setCellValue(record.getR23_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR23_margin_pro() != null) {
					    cellD.setCellValue(record.getR23_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR23_book_expo() != null) {
					    cellE.setCellValue(record.getR23_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR23_ccf_cont() != null) {
					    cellF.setCellValue(record.getR23_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR23_equiv_value() != null) {
					    cellG.setCellValue(record.getR23_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR23_rw_obligant() != null) {
					    cellH.setCellValue(record.getR23_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR23_rav() != null) {
					    cellI.setCellValue(record.getR23_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					row = sheet.getRow(23);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR24_total_book_expo() != null) {
					    cellC.setCellValue(record.getR24_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR24_margin_pro() != null) {
					    cellD.setCellValue(record.getR24_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR24_book_expo() != null) {
					    cellE.setCellValue(record.getR24_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR24_ccf_cont() != null) {
					    cellF.setCellValue(record.getR24_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR24_equiv_value() != null) {
					    cellG.setCellValue(record.getR24_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR24_rw_obligant() != null) {
					    cellH.setCellValue(record.getR24_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR24_rav() != null) {
					    cellI.setCellValue(record.getR24_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					row = sheet.getRow(24);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR25_total_book_expo() != null) {
					    cellC.setCellValue(record.getR25_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR25_margin_pro() != null) {
					    cellD.setCellValue(record.getR25_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR25_book_expo() != null) {
					    cellE.setCellValue(record.getR25_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR25_ccf_cont() != null) {
					    cellF.setCellValue(record.getR25_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR25_equiv_value() != null) {
					    cellG.setCellValue(record.getR25_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR25_rw_obligant() != null) {
					    cellH.setCellValue(record.getR25_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR25_rav() != null) {
					    cellI.setCellValue(record.getR25_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					row = sheet.getRow(25);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR26_total_book_expo() != null) {
					    cellC.setCellValue(record.getR26_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR26_margin_pro() != null) {
					    cellD.setCellValue(record.getR26_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR26_book_expo() != null) {
					    cellE.setCellValue(record.getR26_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR26_ccf_cont() != null) {
					    cellF.setCellValue(record.getR26_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR26_equiv_value() != null) {
					    cellG.setCellValue(record.getR26_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR26_rw_obligant() != null) {
					    cellH.setCellValue(record.getR26_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR26_rav() != null) {
					    cellI.setCellValue(record.getR26_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					row = sheet.getRow(26);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR27_total_book_expo() != null) {
					    cellC.setCellValue(record.getR27_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR27_margin_pro() != null) {
					    cellD.setCellValue(record.getR27_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR27_book_expo() != null) {
					    cellE.setCellValue(record.getR27_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR27_ccf_cont() != null) {
					    cellF.setCellValue(record.getR27_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR27_equiv_value() != null) {
					    cellG.setCellValue(record.getR27_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR27_rw_obligant() != null) {
					    cellH.setCellValue(record.getR27_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR27_rav() != null) {
					    cellI.setCellValue(record.getR27_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					row = sheet.getRow(27);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR28_total_book_expo() != null) {
					    cellC.setCellValue(record.getR28_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR28_margin_pro() != null) {
					    cellD.setCellValue(record.getR28_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR28_book_expo() != null) {
					    cellE.setCellValue(record.getR28_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR28_ccf_cont() != null) {
					    cellF.setCellValue(record.getR28_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR28_equiv_value() != null) {
					    cellG.setCellValue(record.getR28_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR28_rw_obligant() != null) {
					    cellH.setCellValue(record.getR28_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR28_rav() != null) {
					    cellI.setCellValue(record.getR28_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(28);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR29_total_book_expo() != null) {
					    cellC.setCellValue(record.getR29_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR29_margin_pro() != null) {
					    cellD.setCellValue(record.getR29_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR29_book_expo() != null) {
					    cellE.setCellValue(record.getR29_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR29_ccf_cont() != null) {
					    cellF.setCellValue(record.getR29_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR29_equiv_value() != null) {
					    cellG.setCellValue(record.getR29_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR29_rw_obligant() != null) {
					    cellH.setCellValue(record.getR29_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR29_rav() != null) {
					    cellI.setCellValue(record.getR29_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(29);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR30_total_book_expo() != null) {
					    cellC.setCellValue(record.getR30_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR30_margin_pro() != null) {
					    cellD.setCellValue(record.getR30_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR30_book_expo() != null) {
					    cellE.setCellValue(record.getR30_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR30_ccf_cont() != null) {
					    cellF.setCellValue(record.getR30_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR30_equiv_value() != null) {
					    cellG.setCellValue(record.getR30_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR30_rw_obligant() != null) {
					    cellH.setCellValue(record.getR30_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR30_rav() != null) {
					    cellI.setCellValue(record.getR30_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(30);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR31_total_book_expo() != null) {
					    cellC.setCellValue(record.getR31_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR31_margin_pro() != null) {
					    cellD.setCellValue(record.getR31_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR31_book_expo() != null) {
					    cellE.setCellValue(record.getR31_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR31_ccf_cont() != null) {
					    cellF.setCellValue(record.getR31_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR31_equiv_value() != null) {
					    cellG.setCellValue(record.getR31_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR31_rw_obligant() != null) {
					    cellH.setCellValue(record.getR31_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR31_rav() != null) {
					    cellI.setCellValue(record.getR31_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(31);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR32_total_book_expo() != null) {
					    cellC.setCellValue(record.getR32_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR32_margin_pro() != null) {
					    cellD.setCellValue(record.getR32_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR32_book_expo() != null) {
					    cellE.setCellValue(record.getR32_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR32_ccf_cont() != null) {
					    cellF.setCellValue(record.getR32_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR32_equiv_value() != null) {
					    cellG.setCellValue(record.getR32_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR32_rw_obligant() != null) {
					    cellH.setCellValue(record.getR32_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR32_rav() != null) {
					    cellI.setCellValue(record.getR32_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(32);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR33_total_book_expo() != null) {
					    cellC.setCellValue(record.getR33_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR33_margin_pro() != null) {
					    cellD.setCellValue(record.getR33_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR33_book_expo() != null) {
					    cellE.setCellValue(record.getR33_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR33_ccf_cont() != null) {
					    cellF.setCellValue(record.getR33_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR33_equiv_value() != null) {
					    cellG.setCellValue(record.getR33_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR33_rw_obligant() != null) {
					    cellH.setCellValue(record.getR33_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR33_rav() != null) {
					    cellI.setCellValue(record.getR33_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(33);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR34_total_book_expo() != null) {
					    cellC.setCellValue(record.getR34_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR34_margin_pro() != null) {
					    cellD.setCellValue(record.getR34_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR34_book_expo() != null) {
					    cellE.setCellValue(record.getR34_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR34_ccf_cont() != null) {
					    cellF.setCellValue(record.getR34_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR34_equiv_value() != null) {
					    cellG.setCellValue(record.getR34_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR34_rw_obligant() != null) {
					    cellH.setCellValue(record.getR34_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR34_rav() != null) {
					    cellI.setCellValue(record.getR34_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(34);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR35_total_book_expo() != null) {
					    cellC.setCellValue(record.getR35_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR35_margin_pro() != null) {
					    cellD.setCellValue(record.getR35_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR35_book_expo() != null) {
					    cellE.setCellValue(record.getR35_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR35_ccf_cont() != null) {
					    cellF.setCellValue(record.getR35_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR35_equiv_value() != null) {
					    cellG.setCellValue(record.getR35_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR35_rw_obligant() != null) {
					    cellH.setCellValue(record.getR35_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR35_rav() != null) {
					    cellI.setCellValue(record.getR35_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(35);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR36_total_book_expo() != null) {
					    cellC.setCellValue(record.getR36_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR36_margin_pro() != null) {
					    cellD.setCellValue(record.getR36_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR36_book_expo() != null) {
					    cellE.setCellValue(record.getR36_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR36_ccf_cont() != null) {
					    cellF.setCellValue(record.getR36_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR36_equiv_value() != null) {
					    cellG.setCellValue(record.getR36_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR36_rw_obligant() != null) {
					    cellH.setCellValue(record.getR36_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR36_rav() != null) {
					    cellI.setCellValue(record.getR36_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(36);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR37_total_book_expo() != null) {
					    cellC.setCellValue(record.getR37_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR37_margin_pro() != null) {
					    cellD.setCellValue(record.getR37_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR37_book_expo() != null) {
					    cellE.setCellValue(record.getR37_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR37_ccf_cont() != null) {
					    cellF.setCellValue(record.getR37_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR37_equiv_value() != null) {
					    cellG.setCellValue(record.getR37_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR37_rw_obligant() != null) {
					    cellH.setCellValue(record.getR37_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR37_rav() != null) {
					    cellI.setCellValue(record.getR37_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(37);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR38_total_book_expo() != null) {
					    cellC.setCellValue(record.getR38_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR38_margin_pro() != null) {
					    cellD.setCellValue(record.getR38_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR38_book_expo() != null) {
					    cellE.setCellValue(record.getR38_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR38_ccf_cont() != null) {
					    cellF.setCellValue(record.getR38_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR38_equiv_value() != null) {
					    cellG.setCellValue(record.getR38_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR38_rw_obligant() != null) {
					    cellH.setCellValue(record.getR38_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR38_rav() != null) {
					    cellI.setCellValue(record.getR38_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(38);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR39_total_book_expo() != null) {
					    cellC.setCellValue(record.getR39_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR39_margin_pro() != null) {
					    cellD.setCellValue(record.getR39_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR39_book_expo() != null) {
					    cellE.setCellValue(record.getR39_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR39_ccf_cont() != null) {
					    cellF.setCellValue(record.getR39_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR39_equiv_value() != null) {
					    cellG.setCellValue(record.getR39_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR39_rw_obligant() != null) {
					    cellH.setCellValue(record.getR39_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR39_rav() != null) {
					    cellI.setCellValue(record.getR39_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(39);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR40_total_book_expo() != null) {
					    cellC.setCellValue(record.getR40_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR40_margin_pro() != null) {
					    cellD.setCellValue(record.getR40_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR40_book_expo() != null) {
					    cellE.setCellValue(record.getR40_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR40_ccf_cont() != null) {
					    cellF.setCellValue(record.getR40_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR40_equiv_value() != null) {
					    cellG.setCellValue(record.getR40_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR40_rw_obligant() != null) {
					    cellH.setCellValue(record.getR40_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR40_rav() != null) {
					    cellI.setCellValue(record.getR40_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(40);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR41_total_book_expo() != null) {
					    cellC.setCellValue(record.getR41_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR41_margin_pro() != null) {
					    cellD.setCellValue(record.getR41_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR41_book_expo() != null) {
					    cellE.setCellValue(record.getR41_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR41_ccf_cont() != null) {
					    cellF.setCellValue(record.getR41_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR41_equiv_value() != null) {
					    cellG.setCellValue(record.getR41_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR41_rw_obligant() != null) {
					    cellH.setCellValue(record.getR41_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR41_rav() != null) {
					    cellI.setCellValue(record.getR41_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(41);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR42_total_book_expo() != null) {
					    cellC.setCellValue(record.getR42_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR42_margin_pro() != null) {
					    cellD.setCellValue(record.getR42_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR42_book_expo() != null) {
					    cellE.setCellValue(record.getR42_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR42_ccf_cont() != null) {
					    cellF.setCellValue(record.getR42_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR42_equiv_value() != null) {
					    cellG.setCellValue(record.getR42_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR42_rw_obligant() != null) {
					    cellH.setCellValue(record.getR42_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR42_rav() != null) {
					    cellI.setCellValue(record.getR42_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(42);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR43_total_book_expo() != null) {
					    cellC.setCellValue(record.getR43_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR43_margin_pro() != null) {
					    cellD.setCellValue(record.getR43_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR43_book_expo() != null) {
					    cellE.setCellValue(record.getR43_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR43_ccf_cont() != null) {
					    cellF.setCellValue(record.getR43_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR43_equiv_value() != null) {
					    cellG.setCellValue(record.getR43_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR43_rw_obligant() != null) {
					    cellH.setCellValue(record.getR43_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR43_rav() != null) {
					    cellI.setCellValue(record.getR43_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(43);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR44_total_book_expo() != null) {
					    cellC.setCellValue(record.getR44_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR44_margin_pro() != null) {
					    cellD.setCellValue(record.getR44_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR44_book_expo() != null) {
					    cellE.setCellValue(record.getR44_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR44_ccf_cont() != null) {
					    cellF.setCellValue(record.getR44_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR44_equiv_value() != null) {
					    cellG.setCellValue(record.getR44_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR44_rw_obligant() != null) {
					    cellH.setCellValue(record.getR44_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR44_rav() != null) {
					    cellI.setCellValue(record.getR44_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(44);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR45_total_book_expo() != null) {
					    cellC.setCellValue(record.getR45_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR45_margin_pro() != null) {
					    cellD.setCellValue(record.getR45_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR45_book_expo() != null) {
					    cellE.setCellValue(record.getR45_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR45_ccf_cont() != null) {
					    cellF.setCellValue(record.getR45_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR45_equiv_value() != null) {
					    cellG.setCellValue(record.getR45_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR45_rw_obligant() != null) {
					    cellH.setCellValue(record.getR45_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR45_rav() != null) {
					    cellI.setCellValue(record.getR45_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(45);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR46_total_book_expo() != null) {
					    cellC.setCellValue(record.getR46_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR46_margin_pro() != null) {
					    cellD.setCellValue(record.getR46_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR46_book_expo() != null) {
					    cellE.setCellValue(record.getR46_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR46_ccf_cont() != null) {
					    cellF.setCellValue(record.getR46_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR46_equiv_value() != null) {
					    cellG.setCellValue(record.getR46_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR46_rw_obligant() != null) {
					    cellH.setCellValue(record.getR46_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR46_rav() != null) {
					    cellI.setCellValue(record.getR46_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}


					//-------//R61
					row = sheet.getRow(60);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR61_total_book_expo() != null) {
					    cellC.setCellValue(record.getR61_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR61_margin_pro() != null) {
					    cellD.setCellValue(record.getR61_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR61_book_expo() != null) {
					    cellE.setCellValue(record.getR61_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR61_ccf_cont() != null) {
					    cellF.setCellValue(record.getR61_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR61_equiv_value() != null) {
					    cellG.setCellValue(record.getR61_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR61_rw_obligant() != null) {
					    cellH.setCellValue(record.getR61_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR61_rav() != null) {
					    cellI.setCellValue(record.getR61_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					//----R62
					row = sheet.getRow(61);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR62_total_book_expo() != null) {
					    cellC.setCellValue(record.getR62_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR62_margin_pro() != null) {
					    cellD.setCellValue(record.getR62_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR62_ccf_cont() != null) {
					    cellF.setCellValue(record.getR62_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR62_rw_obligant() != null) {
					    cellH.setCellValue(record.getR62_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//R63
					row = sheet.getRow(62);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR63_total_book_expo() != null) {
					    cellC.setCellValue(record.getR63_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR63_margin_pro() != null) {
					    cellD.setCellValue(record.getR63_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR63_book_expo() != null) {
					    cellE.setCellValue(record.getR63_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR63_ccf_cont() != null) {
					    cellF.setCellValue(record.getR63_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR63_equiv_value() != null) {
					    cellG.setCellValue(record.getR63_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR63_rw_obligant() != null) {
					    cellH.setCellValue(record.getR63_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR63_rav() != null) {
					    cellI.setCellValue(record.getR63_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					//R64
					row = sheet.getRow(63);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR64_total_book_expo() != null) {
					    cellC.setCellValue(record.getR64_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR64_margin_pro() != null) {
					    cellD.setCellValue(record.getR64_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR64_ccf_cont() != null) {
					    cellF.setCellValue(record.getR64_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR64_rw_obligant() != null) {
					    cellH.setCellValue(record.getR64_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//----R65
					row = sheet.getRow(64);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR65_total_book_expo() != null) {
					    cellC.setCellValue(record.getR65_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR65_margin_pro() != null) {
					    cellD.setCellValue(record.getR65_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR65_ccf_cont() != null) {
					    cellF.setCellValue(record.getR65_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR65_rw_obligant() != null) {
					    cellH.setCellValue(record.getR65_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					////---------R66
					row = sheet.getRow(65);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR66_total_book_expo() != null) {
					    cellC.setCellValue(record.getR66_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR66_margin_pro() != null) {
					    cellD.setCellValue(record.getR66_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR66_ccf_cont() != null) {
					    cellF.setCellValue(record.getR66_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR66_rw_obligant() != null) {
					    cellH.setCellValue(record.getR66_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					////---------R67


					//---R68
					row = sheet.getRow(67);


					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR68_ccf_cont() != null) {
					    cellF.setCellValue(record.getR68_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR68_equiv_value() != null) {
					    cellG.setCellValue(record.getR68_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR68_rw_obligant() != null) {
					    cellH.setCellValue(record.getR68_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					////-----------R69
					row = sheet.getRow(68);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR69_total_book_expo() != null) {
					    cellC.setCellValue(record.getR69_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR69_margin_pro() != null) {
					    cellD.setCellValue(record.getR69_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR69_book_expo() != null) {
					    cellE.setCellValue(record.getR69_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR69_ccf_cont() != null) {
					    cellF.setCellValue(record.getR69_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR69_equiv_value() != null) {
					    cellG.setCellValue(record.getR69_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR69_rw_obligant() != null) {
					    cellH.setCellValue(record.getR69_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR69_rav() != null) {
					    cellI.setCellValue(record.getR69_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					////--------------R70
					row = sheet.getRow(69);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR70_total_book_expo() != null) {
					    cellC.setCellValue(record.getR70_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR70_margin_pro() != null) {
					    cellD.setCellValue(record.getR70_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR70_ccf_cont() != null) {
					    cellF.setCellValue(record.getR70_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}


					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR70_rw_obligant() != null) {
					    cellH.setCellValue(record.getR70_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					////--------------R71
					row = sheet.getRow(70);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR71_total_book_expo() != null) {
					    cellC.setCellValue(record.getR71_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR71_margin_pro() != null) {
					    cellD.setCellValue(record.getR71_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR71_ccf_cont() != null) {
					    cellF.setCellValue(record.getR71_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR71_rw_obligant() != null) {
					    cellH.setCellValue(record.getR71_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//-----R72
					row = sheet.getRow(71);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR72_total_book_expo() != null) {
					    cellC.setCellValue(record.getR72_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR72_margin_pro() != null) {
					    cellD.setCellValue(record.getR72_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR72_ccf_cont() != null) {
					    cellF.setCellValue(record.getR72_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR72_rw_obligant() != null) {
					    cellH.setCellValue(record.getR72_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					////---------R73


					////---------R74


					row = sheet.getRow(73);

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR74_ccf_cont() != null) {
					    cellF.setCellValue(record.getR74_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR74_equiv_value() != null) {
					    cellG.setCellValue(record.getR74_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR74_rw_obligant() != null) {
					    cellH.setCellValue(record.getR74_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//--------R75


					////----------R76

					row = sheet.getRow(75);

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR76_ccf_cont() != null) {
					    cellF.setCellValue(record.getR76_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR76_equiv_value() != null) {
					    cellG.setCellValue(record.getR76_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR76_rw_obligant() != null) {
					    cellH.setCellValue(record.getR76_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					////--------------R77


					////----------------R78
					row = sheet.getRow(77);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR78_total_book_expo() != null) {
					    cellC.setCellValue(record.getR78_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR78_margin_pro() != null) {
					    cellD.setCellValue(record.getR78_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR78_book_expo() != null) {
					    cellE.setCellValue(record.getR78_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR78_ccf_cont() != null) {
					    cellF.setCellValue(record.getR78_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR78_equiv_value() != null) {
					    cellG.setCellValue(record.getR78_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR78_rw_obligant() != null) {
					    cellH.setCellValue(record.getR78_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR78_rav() != null) {
					    cellI.setCellValue(record.getR78_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					row = sheet.getRow(78);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR79_total_book_expo() != null) {
					    cellC.setCellValue(record.getR79_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR79_margin_pro() != null) {
					    cellD.setCellValue(record.getR79_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR79_book_expo() != null) {
					    cellE.setCellValue(record.getR79_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR79_ccf_cont() != null) {
					    cellF.setCellValue(record.getR79_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR79_equiv_value() != null) {
					    cellG.setCellValue(record.getR79_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR79_rw_obligant() != null) {
					    cellH.setCellValue(record.getR79_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR79_rav() != null) {
					    cellI.setCellValue(record.getR79_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					////--------------R80
					row = sheet.getRow(79);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR80_total_book_expo() != null) {
					    cellC.setCellValue(record.getR80_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR80_margin_pro() != null) {
					    cellD.setCellValue(record.getR80_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR80_book_expo() != null) {
					    cellE.setCellValue(record.getR80_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR80_ccf_cont() != null) {
					    cellF.setCellValue(record.getR80_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR80_equiv_value() != null) {
					    cellG.setCellValue(record.getR80_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR80_rw_obligant() != null) {
					    cellH.setCellValue(record.getR80_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR80_rav() != null) {
					    cellI.setCellValue(record.getR80_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					//-----------------R81


					//-----------R82
					row = sheet.getRow(81);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR82_total_book_expo() != null) {
					    cellC.setCellValue(record.getR82_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR82_margin_pro() != null) {
					    cellD.setCellValue(record.getR82_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}


					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR82_ccf_cont() != null) {
					    cellF.setCellValue(record.getR82_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR82_equiv_value() != null) {
					    cellG.setCellValue(record.getR82_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR82_rw_obligant() != null) {
					    cellH.setCellValue(record.getR82_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//---------R83
					row = sheet.getRow(82);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR83_total_book_expo() != null) {
					    cellC.setCellValue(record.getR83_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR83_margin_pro() != null) {
					    cellD.setCellValue(record.getR83_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR83_book_expo() != null) {
					    cellE.setCellValue(record.getR83_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR83_ccf_cont() != null) {
					    cellF.setCellValue(record.getR83_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR83_equiv_value() != null) {
					    cellG.setCellValue(record.getR83_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR83_rw_obligant() != null) {
					    cellH.setCellValue(record.getR83_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR83_rav() != null) {
					    cellI.setCellValue(record.getR83_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					//----------------R84
					row = sheet.getRow(83);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR84_total_book_expo() != null) {
					    cellC.setCellValue(record.getR84_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR84_margin_pro() != null) {
					    cellD.setCellValue(record.getR84_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR84_ccf_cont() != null) {
					    cellF.setCellValue(record.getR84_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR84_equiv_value() != null) {
					    cellG.setCellValue(record.getR84_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR84_rw_obligant() != null) {
					    cellH.setCellValue(record.getR84_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//------------R85


					//---------------R100
					row = sheet.getRow(99);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR100_total_book_expo() != null) {
					    cellC.setCellValue(record.getR100_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR100_margin_pro() != null) {
					    cellD.setCellValue(record.getR100_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR100_book_expo() != null) {
					    cellE.setCellValue(record.getR100_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR100_ccf_cont() != null) {
					    cellF.setCellValue(record.getR100_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR100_equiv_value() != null) {
					    cellG.setCellValue(record.getR100_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR100_rw_obligant() != null) {
					    cellH.setCellValue(record.getR100_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR100_rav() != null) {
					    cellI.setCellValue(record.getR100_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					//-----------R101 
					row = sheet.getRow(100);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR101_total_book_expo() != null) {
					    cellC.setCellValue(record.getR101_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR101_margin_pro() != null) {
					    cellD.setCellValue(record.getR101_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR101_ccf_cont() != null) {
					    cellF.setCellValue(record.getR101_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}


					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR101_rw_obligant() != null) {
					    cellH.setCellValue(record.getR101_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//------------R102
					row = sheet.getRow(101);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR102_total_book_expo() != null) {
					    cellC.setCellValue(record.getR102_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR102_margin_pro() != null) {
					    cellD.setCellValue(record.getR102_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR102_ccf_cont() != null) {
					    cellF.setCellValue(record.getR102_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR102_rw_obligant() != null) {
					    cellH.setCellValue(record.getR102_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//----------------R103
					row = sheet.getRow(102);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR103_total_book_expo() != null) {
					    cellC.setCellValue(record.getR103_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

//					cellD = row.getCell(3);
//					if (cellD == null) cellD = row.createCell(3);
//					if (record.getR103_margin_pro() != null) {
//					    cellD.setCellValue(record.getR103_margin_pro().doubleValue());
//					} else {
//					    cellD.setCellValue(0);
//					}



//					cellF = row.getCell(5);
//					if (cellF == null) cellF = row.createCell(5);
//					if (record.getR103_ccf_cont() != null) {
//					    cellF.setCellValue(record.getR103_ccf_cont().doubleValue());
//					} else {
//					    cellF.setCellValue(0);
//					}
//
//
//
//					cellH = row.getCell(7);
//					if (cellH == null) cellH = row.createCell(7);
//					if (record.getR103_rw_obligant() != null) {
//					    cellH.setCellValue(record.getR103_rw_obligant().doubleValue());
//					} else {
//					    cellH.setCellValue(0);
//					}

					//-----------------R104

					//---------//---------R105


					row = sheet.getRow(104);




					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR105_ccf_cont() != null) {
					    cellF.setCellValue(record.getR105_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR105_equiv_value() != null) {
					    cellG.setCellValue(record.getR105_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR105_rw_obligant() != null) {
					    cellH.setCellValue(record.getR105_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//---------//----------------R106
					row = sheet.getRow(105);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR106_total_book_expo() != null) {
					    cellC.setCellValue(record.getR106_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR106_margin_pro() != null) {
					    cellD.setCellValue(record.getR106_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR106_book_expo() != null) {
					    cellE.setCellValue(record.getR106_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR106_ccf_cont() != null) {
					    cellF.setCellValue(record.getR106_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR106_equiv_value() != null) {
					    cellG.setCellValue(record.getR106_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR106_rw_obligant() != null) {
					    cellH.setCellValue(record.getR106_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR106_rav() != null) {
					    cellI.setCellValue(record.getR106_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					//---------//---------R107
					row = sheet.getRow(106);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR107_total_book_expo() != null) {
					    cellC.setCellValue(record.getR107_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR107_margin_pro() != null) {
					    cellD.setCellValue(record.getR107_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR107_book_expo() != null) {
					    cellE.setCellValue(record.getR107_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR107_ccf_cont() != null) {
					    cellF.setCellValue(record.getR107_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR107_equiv_value() != null) {
					    cellG.setCellValue(record.getR107_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR107_rw_obligant() != null) {
					    cellH.setCellValue(record.getR107_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR107_rav() != null) {
					    cellI.setCellValue(record.getR107_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					//--------------R108
					row = sheet.getRow(107);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR108_total_book_expo() != null) {
					    cellC.setCellValue(record.getR108_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR108_margin_pro() != null) {
					    cellD.setCellValue(record.getR108_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR108_ccf_cont() != null) {
					    cellF.setCellValue(record.getR108_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR108_rw_obligant() != null) {
					    cellH.setCellValue(record.getR108_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//---------------R109
					row = sheet.getRow(108);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR109_total_book_expo() != null) {
					    cellC.setCellValue(record.getR109_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR109_margin_pro() != null) {
					    cellD.setCellValue(record.getR109_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR109_ccf_cont() != null) {
					    cellF.setCellValue(record.getR109_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR109_rw_obligant() != null) {
					    cellH.setCellValue(record.getR109_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//------R110
					row = sheet.getRow(109);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR110_total_book_expo() != null) {
					    cellC.setCellValue(record.getR110_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR110_margin_pro() != null) {
					    cellD.setCellValue(record.getR110_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



//					cellF = row.getCell(5);
//					if (cellF == null) cellF = row.createCell(5);
//					if (record.getR110_ccf_cont() != null) {
//					    cellF.setCellValue(record.getR110_ccf_cont().doubleValue());
//					} else {
//					    cellF.setCellValue(0);
//					}



//					cellH = row.getCell(7);
//					if (cellH == null) cellH = row.createCell(7);
//					if (record.getR110_rw_obligant() != null) {
//					    cellH.setCellValue(record.getR110_rw_obligant().doubleValue());
//					} else {
//					    cellH.setCellValue(0);
//					}

					//---------R111


					//--------------R112

					row = sheet.getRow(111);

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR112_ccf_cont() != null) {
					    cellF.setCellValue(record.getR112_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR112_equiv_value() != null) {
					    cellG.setCellValue(record.getR112_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR112_rw_obligant() != null) {
					    cellH.setCellValue(record.getR112_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//---------//-------------R113

					row = sheet.getRow(112);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR113_total_book_expo() != null) {
					    cellC.setCellValue(record.getR113_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR113_margin_pro() != null) {
					    cellD.setCellValue(record.getR113_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR113_book_expo() != null) {
					    cellE.setCellValue(record.getR113_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR113_ccf_cont() != null) {
					    cellF.setCellValue(record.getR113_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR113_equiv_value() != null) {
					    cellG.setCellValue(record.getR113_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR113_rw_obligant() != null) {
					    cellH.setCellValue(record.getR113_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR113_rav() != null) {
					    cellI.setCellValue(record.getR113_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					//-----------R114
					row = sheet.getRow(113);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR114_total_book_expo() != null) {
					    cellC.setCellValue(record.getR114_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}



					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR114_margin_pro() != null) {
					    cellD.setCellValue(record.getR114_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR114_book_expo() != null) {
					    cellE.setCellValue(record.getR114_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR114_ccf_cont() != null) {
					    cellF.setCellValue(record.getR114_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR114_equiv_value() != null) {
					    cellG.setCellValue(record.getR114_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR114_rw_obligant() != null) {
					    cellH.setCellValue(record.getR114_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR114_rav() != null) {
					    cellI.setCellValue(record.getR114_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					//---------//--------------R115
					row = sheet.getRow(114);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR115_total_book_expo() != null) {
					    cellC.setCellValue(record.getR115_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR115_margin_pro() != null) {
					    cellD.setCellValue(record.getR115_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR115_ccf_cont() != null) {
					    cellF.setCellValue(record.getR115_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR115_rw_obligant() != null) {
					    cellH.setCellValue(record.getR115_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//--------------R116
					row = sheet.getRow(115);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR116_total_book_expo() != null) {
					    cellC.setCellValue(record.getR116_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR116_margin_pro() != null) {
					    cellD.setCellValue(record.getR116_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR116_ccf_cont() != null) {
					    cellF.setCellValue(record.getR116_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR116_rw_obligant() != null) {
					    cellH.setCellValue(record.getR116_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//--------------R117
					row = sheet.getRow(116);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR117_total_book_expo() != null) {
					    cellC.setCellValue(record.getR117_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

//					cellD = row.getCell(3);
//					if (cellD == null) cellD = row.createCell(3);
//					if (record.getR117_margin_pro() != null) {
//					    cellD.setCellValue(record.getR117_margin_pro().doubleValue());
//					} else {
//					    cellD.setCellValue(0);
//					}



//					cellF = row.getCell(5);
//					if (cellF == null) cellF = row.createCell(5);
//					if (record.getR117_ccf_cont() != null) {
//					    cellF.setCellValue(record.getR117_ccf_cont().doubleValue());
//					} else {
//					    cellF.setCellValue(0);
//					}
//
//
//
//					cellH = row.getCell(7);
//					if (cellH == null) cellH = row.createCell(7);
//					if (record.getR117_rw_obligant() != null) {
//					    cellH.setCellValue(record.getR117_rw_obligant().doubleValue());
//					} else {
//					    cellH.setCellValue(0);
//					}

					//------------R118


					//---------------R119

					row = sheet.getRow(118);

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR119_ccf_cont() != null) {
					    cellF.setCellValue(record.getR119_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR119_equiv_value() != null) {
					    cellG.setCellValue(record.getR119_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR119_rw_obligant() != null) {
					    cellH.setCellValue(record.getR119_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//---------------R120
					row = sheet.getRow(119);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR120_total_book_expo() != null) {
					    cellC.setCellValue(record.getR120_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR120_margin_pro() != null) {
					    cellD.setCellValue(record.getR120_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR120_book_expo() != null) {
					    cellE.setCellValue(record.getR120_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR120_ccf_cont() != null) {
					    cellF.setCellValue(record.getR120_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR120_equiv_value() != null) {
					    cellG.setCellValue(record.getR120_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR120_rw_obligant() != null) {
					    cellH.setCellValue(record.getR120_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR120_rav() != null) {
					    cellI.setCellValue(record.getR120_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}


					row = sheet.getRow(120);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR121_total_book_expo() != null) {
					    cellC.setCellValue(record.getR121_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR121_margin_pro() != null) {
					    cellD.setCellValue(record.getR121_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR121_book_expo() != null) {
					    cellE.setCellValue(record.getR121_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR121_ccf_cont() != null) {
					    cellF.setCellValue(record.getR121_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR121_equiv_value() != null) {
					    cellG.setCellValue(record.getR121_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR121_rw_obligant() != null) {
					    cellH.setCellValue(record.getR121_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR121_rav() != null) {
					    cellI.setCellValue(record.getR121_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					//----------------R122
					row = sheet.getRow(121);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR122_total_book_expo() != null) {
					    cellC.setCellValue(record.getR122_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR122_margin_pro() != null) {
					    cellD.setCellValue(record.getR122_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR122_ccf_cont() != null) {
					    cellF.setCellValue(record.getR122_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}


					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR122_rw_obligant() != null) {
					    cellH.setCellValue(record.getR122_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//------------R123
					row = sheet.getRow(122);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR123_total_book_expo() != null) {
					    cellC.setCellValue(record.getR123_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR123_margin_pro() != null) {
					    cellD.setCellValue(record.getR123_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}


					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR123_ccf_cont() != null) {
					    cellF.setCellValue(record.getR123_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR123_rw_obligant() != null) {
					    cellH.setCellValue(record.getR123_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//-----------R124
					row = sheet.getRow(123);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR124_total_book_expo() != null) {
					    cellC.setCellValue(record.getR124_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR124_margin_pro() != null) {
					    cellD.setCellValue(record.getR124_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR124_ccf_cont() != null) {
					    cellF.setCellValue(record.getR124_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR124_rw_obligant() != null) {
					    cellH.setCellValue(record.getR124_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//-----------R125
					//-------------R126

					row = sheet.getRow(125);


					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR126_ccf_cont() != null) {
					    cellF.setCellValue(record.getR126_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR126_equiv_value() != null) {
					    cellG.setCellValue(record.getR126_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR126_rw_obligant() != null) {
					    cellH.setCellValue(record.getR126_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//----------R127
					row = sheet.getRow(126);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR127_total_book_expo() != null) {
					    cellC.setCellValue(record.getR127_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR127_margin_pro() != null) {
					    cellD.setCellValue(record.getR127_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR127_book_expo() != null) {
					    cellE.setCellValue(record.getR127_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR127_ccf_cont() != null) {
					    cellF.setCellValue(record.getR127_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR127_equiv_value() != null) {
					    cellG.setCellValue(record.getR127_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR127_rw_obligant() != null) {
					    cellH.setCellValue(record.getR127_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR127_rav() != null) {
					    cellI.setCellValue(record.getR127_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					//----------R128

					row = sheet.getRow(127);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR128_total_book_expo() != null) {
					    cellC.setCellValue(record.getR128_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR128_margin_pro() != null) {
					    cellD.setCellValue(record.getR128_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR128_ccf_cont() != null) {
					    cellF.setCellValue(record.getR128_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR128_rw_obligant() != null) {
					    cellH.setCellValue(record.getR128_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					//----------R129
					row = sheet.getRow(128);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR129_total_book_expo() != null) {
					    cellC.setCellValue(record.getR129_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR129_margin_pro() != null) {
					    cellD.setCellValue(record.getR129_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR129_book_expo() != null) {
					    cellE.setCellValue(record.getR129_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR129_ccf_cont() != null) {
					    cellF.setCellValue(record.getR129_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR129_equiv_value() != null) {
					    cellG.setCellValue(record.getR129_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR129_rw_obligant() != null) {
					    cellH.setCellValue(record.getR129_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR129_rav() != null) {
					    cellI.setCellValue(record.getR129_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					//----R130
					row = sheet.getRow(129);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR130_total_book_expo() != null) {
					    cellC.setCellValue(record.getR130_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR130_margin_pro() != null) {
					    cellD.setCellValue(record.getR130_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR130_book_expo() != null) {
					    cellE.setCellValue(record.getR130_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR130_ccf_cont() != null) {
					    cellF.setCellValue(record.getR130_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR130_equiv_value() != null) {
					    cellG.setCellValue(record.getR130_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR130_rw_obligant() != null) {
					    cellH.setCellValue(record.getR130_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR130_rav() != null) {
					    cellI.setCellValue(record.getR130_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					//-----R131

					row = sheet.getRow(130);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR131_total_book_expo() != null) {
					    cellC.setCellValue(record.getR131_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR131_margin_pro() != null) {
					    cellD.setCellValue(record.getR131_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR131_book_expo() != null) {
					    cellE.setCellValue(record.getR131_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR131_ccf_cont() != null) {
					    cellF.setCellValue(record.getR131_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR131_equiv_value() != null) {
					    cellG.setCellValue(record.getR131_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR131_rw_obligant() != null) {
					    cellH.setCellValue(record.getR131_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR131_rav() != null) {
					    cellI.setCellValue(record.getR131_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					//------------R132
					row = sheet.getRow(131);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR132_total_book_expo() != null) {
					    cellC.setCellValue(record.getR132_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR132_margin_pro() != null) {
					    cellD.setCellValue(record.getR132_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR132_book_expo() != null) {
					    cellE.setCellValue(record.getR132_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR132_ccf_cont() != null) {
					    cellF.setCellValue(record.getR132_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR132_equiv_value() != null) {
					    cellG.setCellValue(record.getR132_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR132_rw_obligant() != null) {
					    cellH.setCellValue(record.getR132_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR132_rav() != null) {
					    cellI.setCellValue(record.getR132_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}

					//-------R133

					//-------R134 CAL



					//------------R135

					//----------R148
					row = sheet.getRow(147);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR148_total_book_expo() != null) {
					    cellC.setCellValue(record.getR148_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR148_margin_pro() != null) {
					    cellD.setCellValue(record.getR148_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR148_book_expo() != null) {
					    cellE.setCellValue(record.getR148_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR148_ccf_cont() != null) {
					    cellF.setCellValue(record.getR148_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR148_equiv_value() != null) {
					    cellG.setCellValue(record.getR148_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR148_rw_obligant() != null) {
					    cellH.setCellValue(record.getR148_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR148_rav() != null) {
					    cellI.setCellValue(record.getR148_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					//-----------R149
					row = sheet.getRow(148);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR149_total_book_expo() != null) {
					    cellC.setCellValue(record.getR149_total_book_expo().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR149_margin_pro() != null) {
					    cellD.setCellValue(record.getR149_margin_pro().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR149_book_expo() != null) {
					    cellE.setCellValue(record.getR149_book_expo().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR149_ccf_cont() != null) {
					    cellF.setCellValue(record.getR149_ccf_cont().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR149_equiv_value() != null) {
					    cellG.setCellValue(record.getR149_equiv_value().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR149_rw_obligant() != null) {
					    cellH.setCellValue(record.getR149_rw_obligant().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR149_rav() != null) {
					    cellI.setCellValue(record.getR149_rav().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}


					//--------R150
					row = sheet.getRow(149);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR150_total_book_expo() != null) {
					  cellC.setCellValue(record.getR150_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR150_margin_pro() != null) {
					  cellD.setCellValue(record.getR150_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR150_ccf_cont() != null) {
					  cellF.setCellValue(record.getR150_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR150_rw_obligant() != null) {
					  cellH.setCellValue(record.getR150_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//-----------R151
					row = sheet.getRow(150);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR151_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR151_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR151_margin_pro() != null) {
					  cellD.setCellValue(record1.getR151_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record1.getR151_book_expo() != null) {
					  cellE.setCellValue(record1.getR151_book_expo().doubleValue());
					} else {
					  cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR151_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR151_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record1.getR151_equiv_value() != null) {
					  cellG.setCellValue(record1.getR151_equiv_value().doubleValue());
					} else {
					  cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR151_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR151_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record1.getR151_rav() != null) {
					  cellI.setCellValue(record1.getR151_rav().doubleValue());
					} else {
					  cellI.setCellValue(0);
					}
					//------//------------R152
					row = sheet.getRow(151);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR152_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR152_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR152_margin_pro() != null) {
					  cellD.setCellValue(record1.getR152_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR152_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR152_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR152_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR152_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//-----------R153
					row = sheet.getRow(152);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR153_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR153_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR153_margin_pro() != null) {
					  cellD.setCellValue(record1.getR153_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR153_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR153_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR153_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR153_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//-----------R154
					row = sheet.getRow(153);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR154_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR154_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR154_margin_pro() != null) {
					  cellD.setCellValue(record1.getR154_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR154_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR154_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR154_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR154_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//---------R155

					//----------R156

					row = sheet.getRow(155);

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR156_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR156_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record1.getR156_equiv_value() != null) {
					  cellG.setCellValue(record1.getR156_equiv_value().doubleValue());
					} else {
					  cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR156_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR156_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//------//-------R157
					row = sheet.getRow(156);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR157_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR157_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR157_margin_pro() != null) {
					  cellD.setCellValue(record1.getR157_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record1.getR157_book_expo() != null) {
					  cellE.setCellValue(record1.getR157_book_expo().doubleValue());
					} else {
					  cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR157_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR157_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record1.getR157_equiv_value() != null) {
					  cellG.setCellValue(record1.getR157_equiv_value().doubleValue());
					} else {
					  cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR157_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR157_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record1.getR157_rav() != null) {
					  cellI.setCellValue(record1.getR157_rav().doubleValue());
					} else {
					  cellI.setCellValue(0);
					}

					//------//--------R158

					row = sheet.getRow(157);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR158_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR158_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR158_margin_pro() != null) {
					  cellD.setCellValue(record1.getR158_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}


					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR158_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR158_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR158_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR158_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//---------R159
					row = sheet.getRow(158);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR159_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR159_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR159_margin_pro() != null) {
					  cellD.setCellValue(record1.getR159_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR159_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR159_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR159_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR159_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//----------R160
					row = sheet.getRow(159);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR160_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR160_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR160_margin_pro() != null) {
					  cellD.setCellValue(record1.getR160_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR160_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR160_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR160_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR160_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//--r161
					//--------R162


					row = sheet.getRow(161);

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR162_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR162_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record1.getR162_equiv_value() != null) {
					  cellG.setCellValue(record1.getR162_equiv_value().doubleValue());
					} else {
					  cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR162_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR162_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//------//------R163
					row = sheet.getRow(162);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR163_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR163_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR163_margin_pro() != null) {
					  cellD.setCellValue(record1.getR163_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record1.getR163_book_expo() != null) {
					  cellE.setCellValue(record1.getR163_book_expo().doubleValue());
					} else {
					  cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR163_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR163_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record1.getR163_equiv_value() != null) {
					  cellG.setCellValue(record1.getR163_equiv_value().doubleValue());
					} else {
					  cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR163_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR163_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record1.getR163_rav() != null) {
					  cellI.setCellValue(record1.getR163_rav().doubleValue());
					} else {
					  cellI.setCellValue(0);
					}

					//------//-------R164
					row = sheet.getRow(163);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR164_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR164_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR164_margin_pro() != null) {
					  cellD.setCellValue(record1.getR164_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR164_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR164_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR164_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR164_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//------//------R165
					row = sheet.getRow(164);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR165_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR165_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR165_margin_pro() != null) {
					  cellD.setCellValue(record1.getR165_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR165_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR165_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR165_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR165_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}


					//-----------R166
					row = sheet.getRow(165);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR166_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR166_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR166_margin_pro() != null) {
					  cellD.setCellValue(record1.getR166_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR166_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR166_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR166_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR166_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//-----------R167

					//----------R168

					row = sheet.getRow(167);

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR168_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR168_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record1.getR168_equiv_value() != null) {
					  cellG.setCellValue(record1.getR168_equiv_value().doubleValue());
					} else {
					  cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR168_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR168_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//------------R169
					row = sheet.getRow(168);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR169_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR169_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR169_margin_pro() != null) {
					  cellD.setCellValue(record1.getR169_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record1.getR169_book_expo() != null) {
					  cellE.setCellValue(record1.getR169_book_expo().doubleValue());
					} else {
					  cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR169_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR169_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record1.getR169_equiv_value() != null) {
					  cellG.setCellValue(record1.getR169_equiv_value().doubleValue());
					} else {
					  cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR169_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR169_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record1.getR169_rav() != null) {
					  cellI.setCellValue(record1.getR169_rav().doubleValue());
					} else {
					  cellI.setCellValue(0);
					}

					//------//--------R170
					row = sheet.getRow(169);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR170_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR170_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR170_margin_pro() != null) {
					  cellD.setCellValue(record1.getR170_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR170_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR170_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR170_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR170_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//------------R171
					row = sheet.getRow(170);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR171_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR171_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR171_margin_pro() != null) {
					  cellD.setCellValue(record1.getR171_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR171_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR171_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR171_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR171_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//------------R172
					row = sheet.getRow(171);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR172_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR172_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR172_margin_pro() != null) {
					  cellD.setCellValue(record1.getR172_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR172_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR172_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR172_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR172_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//---------R173

					//------//------R174
					row = sheet.getRow(173);


					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR174_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR174_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record1.getR174_equiv_value() != null) {
					  cellG.setCellValue(record1.getR174_equiv_value().doubleValue());
					} else {
					  cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR174_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR174_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//------------R175

					//----------R176

					row = sheet.getRow(175);

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR176_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR176_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record1.getR176_equiv_value() != null) {
					  cellG.setCellValue(record1.getR176_equiv_value().doubleValue());
					} else {
					  cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR176_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR176_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//------//------R177

					//------//------R178
					row = sheet.getRow(177);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR178_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR178_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR178_margin_pro() != null) {
					  cellD.setCellValue(record1.getR178_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record1.getR178_book_expo() != null) {
					  cellE.setCellValue(record1.getR178_book_expo().doubleValue());
					} else {
					  cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR178_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR178_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record1.getR178_equiv_value() != null) {
					  cellG.setCellValue(record1.getR178_equiv_value().doubleValue());
					} else {
					  cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR178_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR178_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record1.getR178_rav() != null) {
					  cellI.setCellValue(record1.getR178_rav().doubleValue());
					} else {
					  cellI.setCellValue(0);
					}

					//--------R179
					row = sheet.getRow(178);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR179_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR179_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR179_margin_pro() != null) {
					  cellD.setCellValue(record1.getR179_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR179_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR179_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR179_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR179_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//------//--------R180
					row = sheet.getRow(179);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR180_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR180_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR180_margin_pro() != null) {
					  cellD.setCellValue(record1.getR180_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR180_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR180_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR180_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR180_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//------//----------R181
					row = sheet.getRow(180);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR181_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR181_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR181_margin_pro() != null) {
					  cellD.setCellValue(record1.getR181_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR181_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR181_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}



					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR181_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR181_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//------//--------R182

					//------//--------R183

					row = sheet.getRow(182);

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR183_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR183_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record1.getR183_equiv_value() != null) {
					  cellG.setCellValue(record1.getR183_equiv_value().doubleValue());
					} else {
					  cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR183_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR183_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//--------R184

					//----------R185
					row = sheet.getRow(184);

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR185_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR185_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR185_margin_pro() != null) {
					  cellD.setCellValue(record1.getR185_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record1.getR185_book_expo() != null) {
					  cellE.setCellValue(record1.getR185_book_expo().doubleValue());
					} else {
					  cellE.setCellValue(0);
					}

					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR185_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR185_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record1.getR185_equiv_value() != null) {
					  cellG.setCellValue(record1.getR185_equiv_value().doubleValue());
					} else {
					  cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR185_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR185_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record1.getR185_rav() != null) {
					  cellI.setCellValue(record1.getR185_rav().doubleValue());
					} else {
					  cellI.setCellValue(0);
					}

					//------------R186
					row = sheet.getRow(185);



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR186_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR186_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record1.getR186_equiv_value() != null) {
					  cellG.setCellValue(record1.getR186_equiv_value().doubleValue());
					} else {
					  cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR186_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR186_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					//----------R187

					//--------R189
					row = sheet.getRow(188);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record1.getR189_total_book_expo() != null) {
					  cellC.setCellValue(record1.getR189_total_book_expo().doubleValue());
					} else {
					  cellC.setCellValue(0);
					}

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record1.getR189_margin_pro() != null) {
					  cellD.setCellValue(record1.getR189_margin_pro().doubleValue());
					} else {
					  cellD.setCellValue(0);
					}



					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record1.getR189_ccf_cont() != null) {
					  cellF.setCellValue(record1.getR189_ccf_cont().doubleValue());
					} else {
					  cellF.setCellValue(0);
					}

					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record1.getR189_equiv_value() != null) {
					  cellG.setCellValue(record1.getR189_equiv_value().doubleValue());
					} else {
					  cellG.setCellValue(0);
					}

					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record1.getR189_rw_obligant() != null) {
					  cellH.setCellValue(record1.getR189_rw_obligant().doubleValue());
					} else {
					  cellH.setCellValue(0);
					}

					
					
					
				}

			workbook.setForceFormulaRecalculation(true);
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}
	
	
//	public void updateReport(OFF_BS_ITEMS_Summary_Entity1 updatedEntity) {
//
//		OFF_BS_ITEMS_Summary_Entity1 existing = OFF_BS_ITEMS_summary_repo1.findById(updatedEntity.getReport_date()).orElseThrow(
//				() -> new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));
//
//		int[] rows = { };
//
//		String[] fields = { " "};
//
//		try {
//			for (int i : rows) {
//				for (String field : fields) {
//
//					String getterName = "getR" + i + "_" + field;
//					String setterName = "setR" + i + "_" + field;
//
//					try {
//						Method getter = OFF_BS_ITEMS_Summary_Entity1.class.getMethod(getterName);
//						Method setter = OFF_BS_ITEMS_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(updatedEntity);
//						setter.invoke(existing, newValue);
//
//					} catch (NoSuchMethodException e) {
//						// Field not applicable for this row → skip safely
//					}
//				}
//			}
//		} catch (Exception e) {
//			throw new RuntimeException("Error while updating report fields", e);
//		}
//
//		OFF_BS_ITEMS_summary_repo1.save(existing);
//	}
	

	//Archival View
	public List<Object[]> getOFF_BS_ITEMSArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<OFF_BS_ITEMS_Archival_Summary_Entity1> repoData = OFF_BS_ITEMS_Archival_Summary_Repo1
					.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (OFF_BS_ITEMS_Archival_Summary_Entity1 entity : repoData) {
					Object[] row = new Object[] {
							entity.getReport_date(), 
							entity.getReport_version(), 
							 entity.getReportResubDate()
					};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				OFF_BS_ITEMS_Archival_Summary_Entity1 first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  OFF_BS_ITEMS  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	
	
	

	
	
	
	public byte[] getOFF_BS_ITEMSDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for  OFF_BS_ITEMS Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getOFF_BS_ITEMSDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("OFF_BS_ITEMS Details");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "AVERAGE","REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4 ) {  // MONTHLY_INT (3) and CREDIT_EQUIVALENT (4) nd DEBIT_EQUIVALENT(5)
				    cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
				    cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<OFF_BS_ITEMS_Detail_Entity> reportData = OFF_BS_ITEMS_detail_repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (OFF_BS_ITEMS_Detail_Entity item : reportData) {
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
					// AVERAGE
					 balanceCell = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						 if (j != 3 && j != 4 ) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for OFF_BS_ITEMS — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating OFF_BS_ITEMS Excel", e);
			return new byte[0];
		}
	}
	
	
	
	
	
	
	
	public byte[] getOFF_BS_ITEMSDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for OFF_BS_ITEMS ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("OFF_BS_ITEMS Detail");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

// Header row
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "AVERAGE","REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4 ) {  // MONTHLY_INT (3) and CREDIT_EQUIVALENT (4) nd DEBIT_EQUIVALENT(5)
				    cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
				    cell.setCellStyle(headerStyle);
				}
				sheet.setColumnWidth(i, 5000);
			}

// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<OFF_BS_ITEMS_Archival_Detail_Entity> reportData = OFF_BS_ITEMS_Archival_Detail_Repo.getdatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (OFF_BS_ITEMS_Archival_Detail_Entity item : reportData) {
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

					// AVERAGE
					 balanceCell = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						 if (j != 3 && j != 4 ) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for OFF_BS_ITEMS — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating OFF_BS_ITEMS Excel", e);
			return new byte[0];
		}
	}
	
	
	
	
//	 @Autowired BRRS_OFF_BS_ITEMS_Detail_Repo OFF_BS_ITEMS_detail_repo;
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/OFF_BS_ITEMS"); 

		if (acctNo != null) {
			OFF_BS_ITEMS_Detail_Entity OFF_BS_ITEMSEntity = OFF_BS_ITEMS_detail_repo.findByAcctnumber(acctNo);
			if (OFF_BS_ITEMSEntity != null && OFF_BS_ITEMSEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(OFF_BS_ITEMSEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("d_taxData", OFF_BS_ITEMSEntity);
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
     		String average = request.getParameter("average");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			OFF_BS_ITEMS_Detail_Entity existing = OFF_BS_ITEMS_detail_repo.findByAcctnumber(acctNo);
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
			 
			 
			 if (average != null && !average.isEmpty()) {
		            BigDecimal newaverage = new BigDecimal(average);
		            if (existing.getAverage()  == null ||
		                existing.getAverage().compareTo(newaverage) != 0) {
		            	 existing.setAverage(newaverage);
		                isChanged = true;
		                logger.info("Balance updated to {}", newaverage);
		            }
		        }
		        
		if (isChanged) {
				OFF_BS_ITEMS_detail_repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_OFF_BS_ITEMS_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_OFF_BS_ITEMS_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating OFF_BS_ITEMS record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
	
	
	

}