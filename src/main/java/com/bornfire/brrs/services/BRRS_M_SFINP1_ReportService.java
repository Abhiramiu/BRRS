package com.bornfire.brrs.services;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
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

import com.bornfire.brrs.entities.BRRS_M_SFINP1_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP1_Archival_Summary_Manual_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP1_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP1_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP1_Summary_Manual_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP1_Summary_Repo;
//import com.bornfire.brrs.controllers.CBUAE_BRF_ReportsController;
import com.bornfire.brrs.entities.M_SFINP1_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SFINP1_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SFINP1_Archival_Summary_Manual_Entity;
import com.bornfire.brrs.entities.M_SFINP1_Detail_Entity;
import com.bornfire.brrs.entities.M_SFINP1_Summary_Entity;
import com.bornfire.brrs.entities.M_SFINP1_Summary_Manual_Entity;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



@Component
@Service
public class BRRS_M_SFINP1_ReportService {
private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SFINP1_ReportService.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	BRRS_M_SFINP1_Detail_Repo BRRS_M_SFINP1_Detail_Repo;
	
	@Autowired
	BRRS_M_SFINP1_Summary_Repo BRRS_M_SFINP1_Summary_Repo;
	
	@Autowired
	BRRS_M_SFINP1_Summary_Manual_Repo BRRS_M_SFINP1_Summary_Manual_Repo;
	
	@Autowired
	BRRS_M_SFINP1_Archival_Detail_Repo BRRS_M_SFINP1_Archival_Detail_Repo;
	@Autowired
	BRRS_M_SFINP1_Archival_Summary_Repo BRRS_M_SFINP1_Archival_Summary_Repo;
	
	@Autowired
	BRRS_M_SFINP1_Archival_Summary_Manual_Repo BRRS_M_SFINP1_Archival_Summary_Manual_Repo;

	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	
	public ModelAndView getM_SFINP1View(String reportId, String fromdate, String todate, String currency,
            String dtltype, Pageable pageable, String type, String version) {

ModelAndView mv = new ModelAndView();
Session hs = sessionFactory.getCurrentSession();
System.out.println("🟢 getM_SFINP1View() called");

try {
Date reportDate = dateformat.parse(todate);

// ======= CASE 1: ARCHIVAL =======
if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
System.out.println("📦 Fetching ARCHIVAL data for version: " + version);

List<M_SFINP1_Archival_Summary_Manual_Entity> archivalList =
BRRS_M_SFINP1_Archival_Summary_Manual_Repo.getdatabydateListarchival(reportDate);

mv.addObject("reportsummary", archivalList);

} else {
// ======= CASE 2: MANUAL =======
System.out.println("🧾 Fetching MANUAL data for date: " + todate);

List<M_SFINP1_Summary_Entity> mainList =
BRRS_M_SFINP1_Summary_Repo.getdatabydateList(reportDate);
List<M_SFINP1_Summary_Manual_Entity> manualList =
BRRS_M_SFINP1_Summary_Manual_Repo.getdatabydateList(reportDate);

// ======= Fields to calculate averages =======
String[] fields = {"R14","R34","R37","R39","R43","R50","R51","R52","R57","R59"};

for (String field : fields) {
try {
BigDecimal currentVal = (BigDecimal) hs.createNativeQuery(
"SELECT " + field + "_MONTH_END FROM BRRS_M_SFINP1_SUMMARYTABLE_MANUAL WHERE REPORT_DATE = :reportDate")
.setParameter("reportDate", reportDate)
.getSingleResult();

BigDecimal previousVal = (BigDecimal) hs.createNativeQuery(
"SELECT " + field + "_MONTH_END FROM BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY_MANUAL WHERE REPORT_DATE = ADD_MONTHS(:reportDate,-1)")
.setParameter("reportDate", reportDate)
.getSingleResult();

if (currentVal != null && previousVal != null) {
BigDecimal averageVal = currentVal.add(previousVal)
        .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

// Update the average in main summary table
hs.createNativeQuery(
    "UPDATE BRRS_M_SFINP1_SUMMARYTABLE SET " + field + "_AVERAGE = :avg WHERE REPORT_DATE = :reportDate")
    .setParameter("avg", averageVal)
    .setParameter("reportDate", reportDate)
    .executeUpdate();

System.out.println("✅ " + field + "_AVERAGE updated: " + averageVal);
}

} catch (Exception e) {
System.err.println("❌ Error processing " + field + ": " + e.getMessage());
}
}

// ======= Add to ModelAndView =======
mv.addObject("reportsummary", mainList);
mv.addObject("reportsummary1", manualList);
}

// ======= Common view settings =======
mv.setViewName("BRRS/M_SFINP1");
mv.addObject("displaymode", "summary");
System.out.println("✅ View set to: " + mv.getViewName());

} catch (ParseException e) {
System.err.println("❌ Error parsing todate: " + todate);
e.printStackTrace();
} catch (Exception ex) {
System.err.println("❌ Unexpected error in getM_SFINP1View()");
ex.printStackTrace();
}

return mv;
}


		public ModelAndView getM_SFINP1currentDtl(String reportId, String fromdate, String todate, String currency,
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
				List<M_SFINP1_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_SFINP1_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
				} else {
					T1Dt1 = BRRS_M_SFINP1_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}
				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			} else {
				// 🔹 Current branch
				List<M_SFINP1_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_SFINP1_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = BRRS_M_SFINP1_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
					totalPages = BRRS_M_SFINP1_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/M_SFINP1");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}
		
	public void updateReport(M_SFINP1_Summary_Manual_Entity updatedEntity) {
		    System.out.println("Came to services 1");
		    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		    M_SFINP1_Summary_Manual_Entity existing = BRRS_M_SFINP1_Summary_Manual_Repo.findById(updatedEntity.getREPORT_DATE())
		            .orElseThrow(() -> new RuntimeException(
		                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		    try {
		        
		    	existing.setR14_MONTH_END(updatedEntity.getR14_MONTH_END());
		    	existing.setR34_MONTH_END(updatedEntity.getR34_MONTH_END());
		    	existing.setR37_MONTH_END(updatedEntity.getR37_MONTH_END());
		    	existing.setR39_MONTH_END(updatedEntity.getR39_MONTH_END());
		    	existing.setR43_MONTH_END(updatedEntity.getR43_MONTH_END());
		    	existing.setR50_MONTH_END(updatedEntity.getR50_MONTH_END());
		    	existing.setR51_MONTH_END(updatedEntity.getR51_MONTH_END());
		    	existing.setR52_MONTH_END(updatedEntity.getR52_MONTH_END());
		    	existing.setR57_MONTH_END(updatedEntity.getR57_MONTH_END());
		    	existing.setR59_MONTH_END(updatedEntity.getR59_MONTH_END());


		        // Save back
		        BRRS_M_SFINP1_Summary_Manual_Repo.save(existing);

		    } catch (Exception e) {
		        throw new RuntimeException("Error updating report: " + e.getMessage(), e);
		    }
		}
		
	public byte[] getM_SFINP1Excel(String filename, String reportId, String fromdate, String todate, String currency,
									 String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_SFINP1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		// Fetch data
		List<M_SFINP1_Summary_Entity> dataList =BRRS_M_SFINP1_Summary_Repo.getdatabydateList(dateformat.parse(todate)) ;
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SFINP1 report. Returning empty result.");
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
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SFINP1_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
		
										
					
					Cell cellC, cellD;
					CellStyle originalStyle;
					// ===== Row 10 / Col C =====
					row = sheet.getRow(9);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR10_month_end() != null) cellC.setCellValue(record.getR10_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 10 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR10_average() != null) cellD.setCellValue(record.getR10_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 11 / Col C =====
					row = sheet.getRow(10);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR11_month_end() != null) cellC.setCellValue(record.getR11_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 11 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR11_average() != null) cellD.setCellValue(record.getR11_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 13 / Col C =====
					row = sheet.getRow(12);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR13_month_end() != null) cellC.setCellValue(record.getR13_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 13 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR13_average() != null) cellD.setCellValue(record.getR13_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 14 / Col C =====
					row = sheet.getRow(13);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR14_month_end() != null) cellC.setCellValue(record.getR14_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 14 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR14_average() != null) cellD.setCellValue(record.getR14_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 15 / Col C =====
					row = sheet.getRow(14);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR15_month_end() != null) cellC.setCellValue(record.getR15_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 15 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR15_average() != null) cellD.setCellValue(record.getR15_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 16 / Col C =====
					row = sheet.getRow(15);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR16_month_end() != null) cellC.setCellValue(record.getR16_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 16 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR16_average() != null) cellD.setCellValue(record.getR16_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 17 / Col C =====
					row = sheet.getRow(16);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR17_month_end() != null) cellC.setCellValue(record.getR17_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 17 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR17_average() != null) cellD.setCellValue(record.getR17_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 18 / Col C =====
					row = sheet.getRow(17);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR18_month_end() != null) cellC.setCellValue(record.getR18_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 18 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR18_average() != null) cellD.setCellValue(record.getR18_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 19 / Col C =====
					row = sheet.getRow(18);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR19_month_end() != null) cellC.setCellValue(record.getR19_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 19 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR19_average() != null) cellD.setCellValue(record.getR19_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 22 / Col C =====
					row = sheet.getRow(21);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR22_month_end() != null) cellC.setCellValue(record.getR22_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 22 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR22_average() != null) cellD.setCellValue(record.getR22_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 23 / Col C =====
					row = sheet.getRow(22);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR23_month_end() != null) cellC.setCellValue(record.getR23_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 23 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR23_average() != null) cellD.setCellValue(record.getR23_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 25 / Col C =====
					row = sheet.getRow(24);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR25_month_end() != null) cellC.setCellValue(record.getR25_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 25 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR25_average() != null) cellD.setCellValue(record.getR25_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 26 / Col C =====
					row = sheet.getRow(25);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR26_month_end() != null) cellC.setCellValue(record.getR26_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 26 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR26_average() != null) cellD.setCellValue(record.getR26_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 28 / Col C =====
					row = sheet.getRow(27);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR28_month_end() != null) cellC.setCellValue(record.getR28_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 28 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR28_average() != null) cellD.setCellValue(record.getR28_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 29 / Col C =====
					row = sheet.getRow(28);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR29_month_end() != null) cellC.setCellValue(record.getR29_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 29 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR29_average() != null) cellD.setCellValue(record.getR29_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 30 / Col C =====
					row = sheet.getRow(29);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR30_month_end() != null) cellC.setCellValue(record.getR30_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 30 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR30_average() != null) cellD.setCellValue(record.getR30_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 32 / Col C =====
					row = sheet.getRow(31);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR32_month_end() != null) cellC.setCellValue(record.getR32_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 32 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR32_average() != null) cellD.setCellValue(record.getR32_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 33 / Col C =====
					row = sheet.getRow(32);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR33_month_end() != null) cellC.setCellValue(record.getR33_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 33 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR33_average() != null) cellD.setCellValue(record.getR33_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 34 / Col C =====
					row = sheet.getRow(33);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR34_month_end() != null) cellC.setCellValue(record.getR34_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 34 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR34_average() != null) cellD.setCellValue(record.getR34_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 36 / Col C =====
					row = sheet.getRow(35);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR36_month_end() != null) cellC.setCellValue(record.getR36_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 36 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR36_average() != null) cellD.setCellValue(record.getR36_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 37 / Col C =====
					row = sheet.getRow(36);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR37_month_end() != null) cellC.setCellValue(record.getR37_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 37 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR37_average() != null) cellD.setCellValue(record.getR37_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 39 / Col C =====
					row = sheet.getRow(38);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR39_month_end() != null) cellC.setCellValue(record.getR39_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 39 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR39_average() != null) cellD.setCellValue(record.getR39_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 41 / Col C =====
					row = sheet.getRow(40);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR41_month_end() != null) cellC.setCellValue(record.getR41_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 41 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR41_average() != null) cellD.setCellValue(record.getR41_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 42 / Col C =====
					row = sheet.getRow(41);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR42_month_end() != null) cellC.setCellValue(record.getR42_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 42 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR42_average() != null) cellD.setCellValue(record.getR42_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 43 / Col C =====
					row = sheet.getRow(42);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR43_month_end() != null) cellC.setCellValue(record.getR43_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 43 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR43_average() != null) cellD.setCellValue(record.getR43_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 45 / Col C =====
					row = sheet.getRow(44);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR45_month_end() != null) cellC.setCellValue(record.getR45_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 45 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR45_average() != null) cellD.setCellValue(record.getR45_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 46 / Col C =====
					row = sheet.getRow(45);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR46_month_end() != null) cellC.setCellValue(record.getR46_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 46 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR46_average() != null) cellD.setCellValue(record.getR46_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 47 / Col C =====
					row = sheet.getRow(46);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR47_month_end() != null) cellC.setCellValue(record.getR47_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 47 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR47_average() != null) cellD.setCellValue(record.getR47_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 48 / Col C =====
					row = sheet.getRow(47);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR48_month_end() != null) cellC.setCellValue(record.getR48_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 48 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR48_average() != null) cellD.setCellValue(record.getR48_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 50 / Col C =====
					row = sheet.getRow(49);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR50_month_end() != null) cellC.setCellValue(record.getR50_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 50 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR50_average() != null) cellD.setCellValue(record.getR50_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 51 / Col C =====
					row = sheet.getRow(50);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR51_month_end() != null) cellC.setCellValue(record.getR51_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 51 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR51_average() != null) cellD.setCellValue(record.getR51_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 52 / Col C =====
					row = sheet.getRow(51);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR52_month_end() != null) cellC.setCellValue(record.getR52_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 52 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR52_average() != null) cellD.setCellValue(record.getR52_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 53 / Col C =====
					row = sheet.getRow(52);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR53_month_end() != null) cellC.setCellValue(record.getR53_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 53 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR53_average() != null) cellD.setCellValue(record.getR53_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 54 / Col C =====
					row = sheet.getRow(53);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR54_month_end() != null) cellC.setCellValue(record.getR54_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 54 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR54_average() != null) cellD.setCellValue(record.getR54_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 55 / Col C =====
					row = sheet.getRow(54);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR55_month_end() != null) cellC.setCellValue(record.getR55_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 55 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR55_average() != null) cellD.setCellValue(record.getR55_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 57 / Col C =====
					row = sheet.getRow(56);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR57_month_end() != null) cellC.setCellValue(record.getR57_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 57 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR57_average() != null) cellD.setCellValue(record.getR57_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 58 / Col C =====
					row = sheet.getRow(57);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR58_month_end() != null) cellC.setCellValue(record.getR58_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 58 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR58_average() != null) cellD.setCellValue(record.getR58_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 59 / Col C =====
					row = sheet.getRow(58);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR59_month_end() != null) cellC.setCellValue(record.getR59_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 59 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR59_average() != null) cellD.setCellValue(record.getR59_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 60 / Col C =====
					row = sheet.getRow(59);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR60_month_end() != null) cellC.setCellValue(record.getR60_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 60 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR60_average() != null) cellD.setCellValue(record.getR60_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
//										
//										//R10
//										// Column C
//										Cell cell6 = row.createCell(2);
//										if (record.getR10_month_end() != null) {
//											cell6.setCellValue(record.getR10_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R10
//										// Column D
//										Cell cell7 = row.createCell(3);
//										if (record.getR10_average() != null) {
//											cell7.setCellValue(record.getR10_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//										
//																													
//										row = sheet.getRow(10);
//										
//										//R11
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR11_month_end() != null) {
//											cell6.setCellValue(record.getR11_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R11
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR11_average() != null) {
//											cell7.setCellValue(record.getR11_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
//
////R13	
//										row = sheet.getRow(12);
//										//R13
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR13_month_end() != null) {
//											cell6.setCellValue(record.getR13_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R13
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR13_average() != null) {
//											cell7.setCellValue(record.getR13_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R14		
//										row = sheet.getRow(13);
//										//R14
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR14_month_end() != null) {
//											cell6.setCellValue(record.getR14_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R14
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR14_average() != null) {
//											cell7.setCellValue(record.getR14_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R15			
//										row = sheet.getRow(14);
//										
//										//R15
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR15_month_end() != null) {
//											cell6.setCellValue(record.getR15_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R15
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR15_average() != null) {
//											cell7.setCellValue(record.getR15_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R16			
//										row = sheet.getRow(15);
//										//R16
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR16_month_end() != null) {
//											cell6.setCellValue(record.getR16_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R16
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR16_average() != null) {
//											cell7.setCellValue(record.getR16_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//										//R17				
//										row = sheet.getRow(16);
//										
//										
//										//R17
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR17_month_end() != null) {
//											cell6.setCellValue(record.getR17_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R17
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR17_average() != null) {
//											cell7.setCellValue(record.getR17_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R18				
//										row = sheet.getRow(17);
//										
//										
//										//R18
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR18_month_end() != null) {
//											cell6.setCellValue(record.getR18_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R18
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR18_average() != null) {
//											cell7.setCellValue(record.getR18_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R19				
//										row = sheet.getRow(18);
//										//R19
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR19_month_end() != null) {
//											cell6.setCellValue(record.getR19_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R19
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR19_average() != null) {
//											cell7.setCellValue(record.getR19_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
//
////R22				
//										row = sheet.getRow(21);
//										
//										//R22
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR22_month_end() != null) {
//											cell6.setCellValue(record.getR22_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R22
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR22_average() != null) {
//											cell7.setCellValue(record.getR22_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R23				
//										row = sheet.getRow(22);
//										
//										//R23
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR23_month_end() != null) {
//											cell6.setCellValue(record.getR23_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R23
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR23_average() != null) {
//											cell7.setCellValue(record.getR23_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
//
////R25				
//										row = sheet.getRow(24);
//										
//										
//										//R25
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR25_month_end() != null) {
//											cell6.setCellValue(record.getR25_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R25
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR25_average() != null) {
//											cell7.setCellValue(record.getR25_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R26				
//										row = sheet.getRow(25);
//										//R26
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR26_month_end() != null) {
//											cell6.setCellValue(record.getR26_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R26
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR26_average() != null) {
//											cell7.setCellValue(record.getR26_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
//
////R28				
//										row = sheet.getRow(27);
//										
//										
//										//R28
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR28_month_end() != null) {
//											cell6.setCellValue(record.getR28_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R28
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR28_average() != null) {
//											cell7.setCellValue(record.getR28_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R29				
//										row = sheet.getRow(28);
//										
//										
//										//R29
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR29_month_end() != null) {
//											cell6.setCellValue(record.getR29_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R29
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR29_average() != null) {
//											cell7.setCellValue(record.getR29_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R30				
//										row = sheet.getRow(29);
//										
//										//R30
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR30_month_end() != null) {
//											cell6.setCellValue(record.getR30_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R30
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR30_average() != null) {
//											cell7.setCellValue(record.getR30_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
////R32				
//										row = sheet.getRow(31);
//										
//										
//										//R32
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR32_month_end() != null) {
//											cell6.setCellValue(record.getR32_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R32
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR32_average() != null) {
//											cell7.setCellValue(record.getR32_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R33				
//										row = sheet.getRow(32);
//										
//										
//										//R33
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR33_month_end() != null) {
//											cell6.setCellValue(record.getR33_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R33
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR33_average() != null) {
//											cell7.setCellValue(record.getR33_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R34				
//										row = sheet.getRow(33);
//										
//										
//										//R34
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR34_month_end() != null) {
//											cell6.setCellValue(record.getR34_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R34
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR34_average() != null) {
//											cell7.setCellValue(record.getR34_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
////R36				
//										row = sheet.getRow(35);
//										
//										//R36
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR36_month_end() != null) {
//											cell6.setCellValue(record.getR36_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R36
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR36_average() != null) {
//											cell7.setCellValue(record.getR36_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R37				
//										row = sheet.getRow(36);
//										
//										
//										//R37
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR37_month_end() != null) {
//											cell6.setCellValue(record.getR37_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R37
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR37_average() != null) {
//											cell7.setCellValue(record.getR37_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
////R39				
//										row = sheet.getRow(38);
//										
//										
//										//R39
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR39_month_end() != null) {
//											cell6.setCellValue(record.getR39_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R39
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR39_average() != null) {
//											cell7.setCellValue(record.getR39_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
////R41				
//										row = sheet.getRow(40);
//										
//										
//										//R41
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR41_month_end() != null) {
//											cell6.setCellValue(record.getR41_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R41
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR41_average() != null) {
//											cell7.setCellValue(record.getR41_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R42				
//										row = sheet.getRow(41);
//										
//										
//										//R42
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR42_month_end() != null) {
//											cell6.setCellValue(record.getR42_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R42
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR42_average() != null) {
//											cell7.setCellValue(record.getR42_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R43				
//										row = sheet.getRow(42);
//										
//										
//										//R43
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR43_month_end() != null) {
//											cell6.setCellValue(record.getR43_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R43
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR43_average() != null) {
//											cell7.setCellValue(record.getR43_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
////R45				
//										row = sheet.getRow(44);
//										
//										
//										//R45
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR45_month_end() != null) {
//											cell6.setCellValue(record.getR45_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R45
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR45_average() != null) {
//											cell7.setCellValue(record.getR45_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R46				
//										row = sheet.getRow(45);
//										
//										
//										//R46
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR46_month_end() != null) {
//											cell6.setCellValue(record.getR46_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R46
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR46_average() != null) {
//											cell7.setCellValue(record.getR46_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R47				
//										row = sheet.getRow(46);
//										
//										
//										//R47
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR47_month_end() != null) {
//											cell6.setCellValue(record.getR47_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R47
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR47_average() != null) {
//											cell7.setCellValue(record.getR47_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R48				
//										row = sheet.getRow(47);
//										
//										
//										//R48
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR48_month_end() != null) {
//											cell6.setCellValue(record.getR48_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R48
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR48_average() != null) {
//											cell7.setCellValue(record.getR48_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
//
////R50					
//										row = sheet.getRow(49);
//										
//										//R50
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR50_month_end() != null) {
//											cell6.setCellValue(record.getR50_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R50
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR50_average() != null) {
//											cell7.setCellValue(record.getR50_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R51					
//										row = sheet.getRow(50);
//										
//										
//										//R51
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR51_month_end() != null) {
//											cell6.setCellValue(record.getR51_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R51
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR51_average() != null) {
//											cell7.setCellValue(record.getR51_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R52						
//										row = sheet.getRow(51);
//										
//										
//										//R52
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR52_month_end() != null) {
//											cell6.setCellValue(record.getR52_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R52
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR52_average() != null) {
//											cell7.setCellValue(record.getR52_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R53						
//										row = sheet.getRow(52);
//										
//										
//										//R53
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR53_month_end() != null) {
//											cell6.setCellValue(record.getR53_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R53
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR53_average() != null) {
//											cell7.setCellValue(record.getR53_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R54						
//										row = sheet.getRow(53);
//										
//										
//										//R54
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR54_month_end() != null) {
//											cell6.setCellValue(record.getR54_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R54
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR54_average() != null) {
//											cell7.setCellValue(record.getR54_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R55						
//										row = sheet.getRow(54);
//										
//										
//										//R55
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR55_month_end() != null) {
//											cell6.setCellValue(record.getR55_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R55
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR55_average() != null) {
//											cell7.setCellValue(record.getR55_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
//
////R57						
//										row = sheet.getRow(56);
//										
//										
//										//R57
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR57_month_end() != null) {
//											cell6.setCellValue(record.getR57_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R57
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR57_average() != null) {
//											cell7.setCellValue(record.getR57_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R58						
//										row = sheet.getRow(57);
//										
//										//R58
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR58_month_end() != null) {
//											cell6.setCellValue(record.getR58_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R58
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR58_average() != null) {
//											cell7.setCellValue(record.getR58_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R59						
//										row = sheet.getRow(58);
//										
//										//R59
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR59_month_end() != null) {
//											cell6.setCellValue(record.getR59_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R59
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR59_average() != null) {
//											cell7.setCellValue(record.getR59_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R60						
//										row = sheet.getRow(59);
//										
//										//R60
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR60_month_end() != null) {
//											cell6.setCellValue(record.getR60_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R60
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR60_average() != null) {
//											cell7.setCellValue(record.getR60_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
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
	public byte[] getM_SFINP1DetailExcel(String filename, String fromdate, String todate, String currency,
										   String dtltype, String type, String version) {
	    try {
	        logger.info("Generating Excel for M_SFINP1 Details...");
	        System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
	        XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("M_SFINP1Details");
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
	        String[] headers = {
	            "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID", "REPORT_DATE"
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
	        List<M_SFINP1_Detail_Entity> reportData = BRRS_M_SFINP1_Detail_Repo.getdatabydateList(parsedToDate);
	        if (reportData != null && !reportData.isEmpty()) {
	            int rowIndex = 1;
	            for (M_SFINP1_Detail_Entity item : reportData) {
	                XSSFRow row = sheet.createRow(rowIndex++);
	                row.createCell(0).setCellValue(item.getCustId());
	                row.createCell(1).setCellValue(item.getAcctNumber());
	                row.createCell(2).setCellValue(item.getAcctName());
	                // ACCT BALANCE (right aligned, 3 decimal places)
	                Cell balanceCell = row.createCell(3);
	                if (item.getAcctBalanceInPula() != null) {
	                    balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
	                } else {
	                    balanceCell.setCellValue(0.000);
	                }
	                balanceCell.setCellStyle(balanceStyle);
	                row.createCell(4).setCellValue(item.getRowId());
	                row.createCell(5).setCellValue(item.getColumnId());
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
	            logger.info("No data found for M_SFINP1 — only header will be written.");
	        }
	        // Write to byte[]
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        workbook.write(bos);
	        workbook.close();
	        logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
	        return bos.toByteArray();
	    } catch (Exception e) {
	        logger.error("Error generating M_SFINP1 Excel", e);
	        return new byte[0];
	    }
	}
public List<Object> getM_SFINP1Archival() {
		List<Object> M_SFINP1Archivallist = new ArrayList<>();
		try {
			M_SFINP1Archivallist = BRRS_M_SFINP1_Archival_Summary_Repo.getM_SFINP1archival();
			System.out.println("countser" + M_SFINP1Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_SFINP1 Archival data: " + e.getMessage());
			e.printStackTrace();
			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_SFINP1Archivallist;
	}
	public byte[] getExcelM_SFINP1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
										   String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {
		}
		List<M_SFINP1_Archival_Summary_Entity> dataList = BRRS_M_SFINP1_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SFINP1 report. Returning empty result.");
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
					M_SFINP1_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
		
				
					Cell cellC, cellD;
					CellStyle originalStyle;
					// ===== Row 10 / Col C =====
					row = sheet.getRow(9);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR10_month_end() != null) cellC.setCellValue(record.getR10_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 10 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR10_average() != null) cellD.setCellValue(record.getR10_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 11 / Col C =====
					row = sheet.getRow(10);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR11_month_end() != null) cellC.setCellValue(record.getR11_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 11 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR11_average() != null) cellD.setCellValue(record.getR11_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 13 / Col C =====
					row = sheet.getRow(12);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR13_month_end() != null) cellC.setCellValue(record.getR13_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 13 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR13_average() != null) cellD.setCellValue(record.getR13_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 14 / Col C =====
					row = sheet.getRow(13);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR14_month_end() != null) cellC.setCellValue(record.getR14_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 14 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR14_average() != null) cellD.setCellValue(record.getR14_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 15 / Col C =====
					row = sheet.getRow(14);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR15_month_end() != null) cellC.setCellValue(record.getR15_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 15 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR15_average() != null) cellD.setCellValue(record.getR15_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 16 / Col C =====
					row = sheet.getRow(15);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR16_month_end() != null) cellC.setCellValue(record.getR16_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 16 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR16_average() != null) cellD.setCellValue(record.getR16_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 17 / Col C =====
					row = sheet.getRow(16);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR17_month_end() != null) cellC.setCellValue(record.getR17_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 17 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR17_average() != null) cellD.setCellValue(record.getR17_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 18 / Col C =====
					row = sheet.getRow(17);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR18_month_end() != null) cellC.setCellValue(record.getR18_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 18 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR18_average() != null) cellD.setCellValue(record.getR18_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 19 / Col C =====
					row = sheet.getRow(18);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR19_month_end() != null) cellC.setCellValue(record.getR19_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 19 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR19_average() != null) cellD.setCellValue(record.getR19_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 22 / Col C =====
					row = sheet.getRow(21);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR22_month_end() != null) cellC.setCellValue(record.getR22_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 22 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR22_average() != null) cellD.setCellValue(record.getR22_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 23 / Col C =====
					row = sheet.getRow(22);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR23_month_end() != null) cellC.setCellValue(record.getR23_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 23 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR23_average() != null) cellD.setCellValue(record.getR23_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 25 / Col C =====
					row = sheet.getRow(24);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR25_month_end() != null) cellC.setCellValue(record.getR25_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 25 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR25_average() != null) cellD.setCellValue(record.getR25_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 26 / Col C =====
					row = sheet.getRow(25);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR26_month_end() != null) cellC.setCellValue(record.getR26_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 26 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR26_average() != null) cellD.setCellValue(record.getR26_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 28 / Col C =====
					row = sheet.getRow(27);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR28_month_end() != null) cellC.setCellValue(record.getR28_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 28 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR28_average() != null) cellD.setCellValue(record.getR28_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 29 / Col C =====
					row = sheet.getRow(28);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR29_month_end() != null) cellC.setCellValue(record.getR29_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 29 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR29_average() != null) cellD.setCellValue(record.getR29_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 30 / Col C =====
					row = sheet.getRow(29);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR30_month_end() != null) cellC.setCellValue(record.getR30_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 30 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR30_average() != null) cellD.setCellValue(record.getR30_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 32 / Col C =====
					row = sheet.getRow(31);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR32_month_end() != null) cellC.setCellValue(record.getR32_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 32 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR32_average() != null) cellD.setCellValue(record.getR32_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 33 / Col C =====
					row = sheet.getRow(32);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR33_month_end() != null) cellC.setCellValue(record.getR33_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 33 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR33_average() != null) cellD.setCellValue(record.getR33_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 34 / Col C =====
					row = sheet.getRow(33);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR34_month_end() != null) cellC.setCellValue(record.getR34_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 34 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR34_average() != null) cellD.setCellValue(record.getR34_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 36 / Col C =====
					row = sheet.getRow(35);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR36_month_end() != null) cellC.setCellValue(record.getR36_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 36 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR36_average() != null) cellD.setCellValue(record.getR36_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 37 / Col C =====
					row = sheet.getRow(36);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR37_month_end() != null) cellC.setCellValue(record.getR37_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 37 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR37_average() != null) cellD.setCellValue(record.getR37_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 39 / Col C =====
					row = sheet.getRow(38);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR39_month_end() != null) cellC.setCellValue(record.getR39_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 39 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR39_average() != null) cellD.setCellValue(record.getR39_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 41 / Col C =====
					row = sheet.getRow(40);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR41_month_end() != null) cellC.setCellValue(record.getR41_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 41 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR41_average() != null) cellD.setCellValue(record.getR41_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 42 / Col C =====
					row = sheet.getRow(41);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR42_month_end() != null) cellC.setCellValue(record.getR42_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 42 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR42_average() != null) cellD.setCellValue(record.getR42_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 43 / Col C =====
					row = sheet.getRow(42);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR43_month_end() != null) cellC.setCellValue(record.getR43_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 43 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR43_average() != null) cellD.setCellValue(record.getR43_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 45 / Col C =====
					row = sheet.getRow(44);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR45_month_end() != null) cellC.setCellValue(record.getR45_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 45 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR45_average() != null) cellD.setCellValue(record.getR45_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 46 / Col C =====
					row = sheet.getRow(45);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR46_month_end() != null) cellC.setCellValue(record.getR46_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 46 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR46_average() != null) cellD.setCellValue(record.getR46_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 47 / Col C =====
					row = sheet.getRow(46);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR47_month_end() != null) cellC.setCellValue(record.getR47_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 47 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR47_average() != null) cellD.setCellValue(record.getR47_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 48 / Col C =====
					row = sheet.getRow(47);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR48_month_end() != null) cellC.setCellValue(record.getR48_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 48 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR48_average() != null) cellD.setCellValue(record.getR48_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 50 / Col C =====
					row = sheet.getRow(49);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR50_month_end() != null) cellC.setCellValue(record.getR50_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 50 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR50_average() != null) cellD.setCellValue(record.getR50_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 51 / Col C =====
					row = sheet.getRow(50);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR51_month_end() != null) cellC.setCellValue(record.getR51_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 51 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR51_average() != null) cellD.setCellValue(record.getR51_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 52 / Col C =====
					row = sheet.getRow(51);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR52_month_end() != null) cellC.setCellValue(record.getR52_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 52 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR52_average() != null) cellD.setCellValue(record.getR52_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 53 / Col C =====
					row = sheet.getRow(52);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR53_month_end() != null) cellC.setCellValue(record.getR53_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 53 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR53_average() != null) cellD.setCellValue(record.getR53_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 54 / Col C =====
					row = sheet.getRow(53);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR54_month_end() != null) cellC.setCellValue(record.getR54_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 54 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR54_average() != null) cellD.setCellValue(record.getR54_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 55 / Col C =====
					row = sheet.getRow(54);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR55_month_end() != null) cellC.setCellValue(record.getR55_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 55 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR55_average() != null) cellD.setCellValue(record.getR55_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 57 / Col C =====
					row = sheet.getRow(56);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR57_month_end() != null) cellC.setCellValue(record.getR57_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 57 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR57_average() != null) cellD.setCellValue(record.getR57_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 58 / Col C =====
					row = sheet.getRow(57);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR58_month_end() != null) cellC.setCellValue(record.getR58_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 58 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR58_average() != null) cellD.setCellValue(record.getR58_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 59 / Col C =====
					row = sheet.getRow(58);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR59_month_end() != null) cellC.setCellValue(record.getR59_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 59 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR59_average() != null) cellD.setCellValue(record.getR59_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 60 / Col C =====
					row = sheet.getRow(59);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR60_month_end() != null) cellC.setCellValue(record.getR60_month_end().doubleValue());
					else cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 60 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR60_average() != null) cellD.setCellValue(record.getR60_average().doubleValue());
					else cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
										
//										
//										//R10
//										// Column C
//										Cell cell6 = row.createCell(2);
//										if (record.getR10_month_end() != null) {
//											cell6.setCellValue(record.getR10_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R10
//										// Column D
//										Cell cell7 = row.createCell(3);
//										if (record.getR10_average() != null) {
//											cell7.setCellValue(record.getR10_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//										
//																													
//										row = sheet.getRow(10);
//										
//										//R11
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR11_month_end() != null) {
//											cell6.setCellValue(record.getR11_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R11
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR11_average() != null) {
//											cell7.setCellValue(record.getR11_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
//
////R13	
//										row = sheet.getRow(12);
//										//R13
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR13_month_end() != null) {
//											cell6.setCellValue(record.getR13_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R13
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR13_average() != null) {
//											cell7.setCellValue(record.getR13_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R14		
//										row = sheet.getRow(13);
//										//R14
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR14_month_end() != null) {
//											cell6.setCellValue(record.getR14_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R14
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR14_average() != null) {
//											cell7.setCellValue(record.getR14_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R15			
//										row = sheet.getRow(14);
//										
//										//R15
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR15_month_end() != null) {
//											cell6.setCellValue(record.getR15_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R15
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR15_average() != null) {
//											cell7.setCellValue(record.getR15_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R16			
//										row = sheet.getRow(15);
//										//R16
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR16_month_end() != null) {
//											cell6.setCellValue(record.getR16_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R16
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR16_average() != null) {
//											cell7.setCellValue(record.getR16_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//										//R17				
//										row = sheet.getRow(16);
//										
//										
//										//R17
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR17_month_end() != null) {
//											cell6.setCellValue(record.getR17_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R17
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR17_average() != null) {
//											cell7.setCellValue(record.getR17_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R18				
//										row = sheet.getRow(17);
//										
//										
//										//R18
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR18_month_end() != null) {
//											cell6.setCellValue(record.getR18_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R18
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR18_average() != null) {
//											cell7.setCellValue(record.getR18_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R19				
//										row = sheet.getRow(18);
//										//R19
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR19_month_end() != null) {
//											cell6.setCellValue(record.getR19_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R19
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR19_average() != null) {
//											cell7.setCellValue(record.getR19_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
//
////R22				
//										row = sheet.getRow(21);
//										
//										//R22
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR22_month_end() != null) {
//											cell6.setCellValue(record.getR22_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R22
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR22_average() != null) {
//											cell7.setCellValue(record.getR22_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R23				
//										row = sheet.getRow(22);
//										
//										//R23
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR23_month_end() != null) {
//											cell6.setCellValue(record.getR23_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R23
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR23_average() != null) {
//											cell7.setCellValue(record.getR23_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
//
////R25				
//										row = sheet.getRow(24);
//										
//										
//										//R25
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR25_month_end() != null) {
//											cell6.setCellValue(record.getR25_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R25
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR25_average() != null) {
//											cell7.setCellValue(record.getR25_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R26				
//										row = sheet.getRow(25);
//										//R26
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR26_month_end() != null) {
//											cell6.setCellValue(record.getR26_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R26
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR26_average() != null) {
//											cell7.setCellValue(record.getR26_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
//
////R28				
//										row = sheet.getRow(27);
//										
//										
//										//R28
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR28_month_end() != null) {
//											cell6.setCellValue(record.getR28_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R28
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR28_average() != null) {
//											cell7.setCellValue(record.getR28_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R29				
//										row = sheet.getRow(28);
//										
//										
//										//R29
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR29_month_end() != null) {
//											cell6.setCellValue(record.getR29_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R29
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR29_average() != null) {
//											cell7.setCellValue(record.getR29_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R30				
//										row = sheet.getRow(29);
//										
//										//R30
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR30_month_end() != null) {
//											cell6.setCellValue(record.getR30_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R30
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR30_average() != null) {
//											cell7.setCellValue(record.getR30_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
////R32				
//										row = sheet.getRow(31);
//										
//										
//										//R32
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR32_month_end() != null) {
//											cell6.setCellValue(record.getR32_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R32
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR32_average() != null) {
//											cell7.setCellValue(record.getR32_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R33				
//										row = sheet.getRow(32);
//										
//										
//										//R33
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR33_month_end() != null) {
//											cell6.setCellValue(record.getR33_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R33
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR33_average() != null) {
//											cell7.setCellValue(record.getR33_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R34				
//										row = sheet.getRow(33);
//										
//										
//										//R34
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR34_month_end() != null) {
//											cell6.setCellValue(record.getR34_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R34
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR34_average() != null) {
//											cell7.setCellValue(record.getR34_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
////R36				
//										row = sheet.getRow(35);
//										
//										//R36
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR36_month_end() != null) {
//											cell6.setCellValue(record.getR36_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R36
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR36_average() != null) {
//											cell7.setCellValue(record.getR36_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R37				
//										row = sheet.getRow(36);
//										
//										
//										//R37
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR37_month_end() != null) {
//											cell6.setCellValue(record.getR37_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R37
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR37_average() != null) {
//											cell7.setCellValue(record.getR37_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
////R39				
//										row = sheet.getRow(38);
//										
//										
//										//R39
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR39_month_end() != null) {
//											cell6.setCellValue(record.getR39_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R39
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR39_average() != null) {
//											cell7.setCellValue(record.getR39_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
////R41				
//										row = sheet.getRow(40);
//										
//										
//										//R41
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR41_month_end() != null) {
//											cell6.setCellValue(record.getR41_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R41
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR41_average() != null) {
//											cell7.setCellValue(record.getR41_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R42				
//										row = sheet.getRow(41);
//										
//										
//										//R42
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR42_month_end() != null) {
//											cell6.setCellValue(record.getR42_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R42
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR42_average() != null) {
//											cell7.setCellValue(record.getR42_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R43				
//										row = sheet.getRow(42);
//										
//										
//										//R43
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR43_month_end() != null) {
//											cell6.setCellValue(record.getR43_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R43
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR43_average() != null) {
//											cell7.setCellValue(record.getR43_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
////R45				
//										row = sheet.getRow(44);
//										
//										
//										//R45
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR45_month_end() != null) {
//											cell6.setCellValue(record.getR45_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R45
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR45_average() != null) {
//											cell7.setCellValue(record.getR45_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R46				
//										row = sheet.getRow(45);
//										
//										
//										//R46
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR46_month_end() != null) {
//											cell6.setCellValue(record.getR46_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R46
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR46_average() != null) {
//											cell7.setCellValue(record.getR46_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R47				
//										row = sheet.getRow(46);
//										
//										
//										//R47
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR47_month_end() != null) {
//											cell6.setCellValue(record.getR47_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R47
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR47_average() != null) {
//											cell7.setCellValue(record.getR47_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R48				
//										row = sheet.getRow(47);
//										
//										
//										//R48
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR48_month_end() != null) {
//											cell6.setCellValue(record.getR48_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R48
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR48_average() != null) {
//											cell7.setCellValue(record.getR48_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
//
////R50					
//										row = sheet.getRow(49);
//										
//										//R50
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR50_month_end() != null) {
//											cell6.setCellValue(record.getR50_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R50
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR50_average() != null) {
//											cell7.setCellValue(record.getR50_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R51					
//										row = sheet.getRow(50);
//										
//										
//										//R51
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR51_month_end() != null) {
//											cell6.setCellValue(record.getR51_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R51
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR51_average() != null) {
//											cell7.setCellValue(record.getR51_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R52						
//										row = sheet.getRow(51);
//										
//										
//										//R52
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR52_month_end() != null) {
//											cell6.setCellValue(record.getR52_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R52
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR52_average() != null) {
//											cell7.setCellValue(record.getR52_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R53						
//										row = sheet.getRow(52);
//										
//										
//										//R53
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR53_month_end() != null) {
//											cell6.setCellValue(record.getR53_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R53
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR53_average() != null) {
//											cell7.setCellValue(record.getR53_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R54						
//										row = sheet.getRow(53);
//										
//										
//										//R54
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR54_month_end() != null) {
//											cell6.setCellValue(record.getR54_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R54
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR54_average() != null) {
//											cell7.setCellValue(record.getR54_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R55						
//										row = sheet.getRow(54);
//										
//										
//										//R55
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR55_month_end() != null) {
//											cell6.setCellValue(record.getR55_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R55
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR55_average() != null) {
//											cell7.setCellValue(record.getR55_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
//
//
////R57						
//										row = sheet.getRow(56);
//										
//										
//										//R57
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR57_month_end() != null) {
//											cell6.setCellValue(record.getR57_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R57
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR57_average() != null) {
//											cell7.setCellValue(record.getR57_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R58						
//										row = sheet.getRow(57);
//										
//										//R58
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR58_month_end() != null) {
//											cell6.setCellValue(record.getR58_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R58
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR58_average() != null) {
//											cell7.setCellValue(record.getR58_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R59						
//										row = sheet.getRow(58);
//										
//										//R59
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR59_month_end() != null) {
//											cell6.setCellValue(record.getR59_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R59
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR59_average() != null) {
//											cell7.setCellValue(record.getR59_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
//
////R60						
//										row = sheet.getRow(59);
//										
//										//R60
//										// Column C
//										cell6 = row.createCell(2);
//										if (record.getR60_month_end() != null) {
//											cell6.setCellValue(record.getR60_month_end().doubleValue());
//											cell6.setCellStyle(numberStyle);
//										} else {
//											cell6.setCellValue("");
//											cell6.setCellStyle(textStyle);
//										}
//										
//										//R60
//										// Column D
//										cell7 = row.createCell(3);
//										if (record.getR60_average() != null) {
//											cell7.setCellValue(record.getR60_average().doubleValue());
//											cell7.setCellStyle(numberStyle);
//										} else {
//											cell7.setCellValue("");
//											cell7.setCellStyle(textStyle);
//										}
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
			logger.info("Generating Excel for BRRS_M_SFINP1 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
			}
XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("M_SFINP1Details");
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
	        String[] headers = {
	            "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID", "REPORT_DATE"
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
	     			List<M_SFINP1_Archival_Detail_Entity> reportData = BRRS_M_SFINP1_Archival_Detail_Repo
	     					.getdatabydateList(parsedToDate, version);
	     			if (reportData != null && !reportData.isEmpty()) {
	     				int rowIndex = 1;
	     				for (M_SFINP1_Archival_Detail_Entity item : reportData) {
	     					XSSFRow row = sheet.createRow(rowIndex++);
	     					row.createCell(0).setCellValue(item.getCustId());
	     					row.createCell(1).setCellValue(item.getAcctNumber());
	     					row.createCell(2).setCellValue(item.getAcctName());
	                // ACCT BALANCE (right aligned, 3 decimal places)
	                Cell balanceCell = row.createCell(3);
	                if (item.getAcctBalanceInPula() != null) {
	                    balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
	                } else {
	                    balanceCell.setCellValue(0.000);
	                }
	                balanceCell.setCellStyle(balanceStyle);
	                row.createCell(4).setCellValue(item.getRowId());
	                row.createCell(5).setCellValue(item.getColumnId());
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
	            logger.info("No data found for M_SFINP1 — only header will be written.");
	        }
	        // Write to byte[]
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        workbook.write(bos);
	        workbook.close();
	        logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
	        return bos.toByteArray();
	    } catch (Exception e) {
	        logger.error("Error generating M_SFINP1 Excel", e);
	        return new byte[0];
	    }
	}


public byte[] convertExcelBytesToPdf(byte[] excelBytes) throws Exception {
    try (
        InputStream inputStream = new ByteArrayInputStream(excelBytes);
        Workbook workbook = WorkbookFactory.create(inputStream); // will return XSSFWorkbook
        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()
    ) {
        Sheet sheet = workbook.getSheetAt(0);

        Document document = new Document(PageSize.A3.rotate(), 20, 20, 20, 20);
        PdfWriter.getInstance(document, pdfOut);
        document.open();

        // Get the header row
        Row headerRow = null;
        for (Row r : sheet) {
            if (r != null && r.getLastCellNum() > 0) {
                headerRow = r;
                break;
            }
        }
        if (headerRow == null) throw new IllegalArgumentException("Excel sheet is empty.");

        int firstCol = headerRow.getFirstCellNum();
        int lastCol = headerRow.getLastCellNum();

        PdfPTable table = new PdfPTable(lastCol - firstCol);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        DataFormatter formatter = new DataFormatter();

        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        Set<String> mergedCells = new HashSet<>();

        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            for (int colIndex = firstCol; colIndex < lastCol; colIndex++) {

                String cellKey = rowIndex + "-" + colIndex;
                if (mergedCells.contains(cellKey)) continue;

                Cell cell = row.getCell(colIndex);
                String cellValue = "";

                if (cell != null) {
                    if (cell.getCellType() == CellType.FORMULA) {
                        CellValue eval = evaluator.evaluate(cell);
                        if (eval != null) {
                            switch (eval.getCellType()) {
                                case STRING:  cellValue = eval.getStringValue(); break;
                                case NUMERIC: cellValue = formatter.formatCellValue(cell); break;
                                case BOOLEAN: cellValue = String.valueOf(eval.getBooleanValue()); break;
                                default:      cellValue = "";
                            }
                        }
                    } else {
                        cellValue = formatter.formatCellValue(cell);
                    }
                }

                PdfPCell pdfCell = new PdfPCell(new Phrase(cellValue));

                // Handle merged cells
                for (CellRangeAddress range : mergedRegions) {
                    if (range.isInRange(rowIndex, colIndex)) {
                        int rowspan = range.getLastRow() - range.getFirstRow() + 1;
                        int colspan = range.getLastColumn() - range.getFirstColumn() + 1;

                        if (rowIndex == range.getFirstRow() && colIndex == range.getFirstColumn()) {
                            if (rowspan > 1) pdfCell.setRowspan(rowspan);
                            if (colspan > 1) pdfCell.setColspan(colspan);
                        } else {
                            mergedCells.add(cellKey);
                            pdfCell = null;
                        }
                        break;
                    }
                }

                if (pdfCell == null) continue;

                // Background color (XSSF only)
                BaseColor bgColor = null;
                if (cell != null) {
                    XSSFColor color = (XSSFColor) cell.getCellStyle().getFillForegroundColorColor();
                    if (color != null && color.getRGB() != null) {
                        byte[] rgb = color.getRGB();
                        bgColor = new BaseColor(rgb[0] & 0xFF, rgb[1] & 0xFF, rgb[2] & 0xFF);
                    }
                }

                if (bgColor != null) pdfCell.setBackgroundColor(bgColor);

                pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfCell.setPadding(4f);

                table.addCell(pdfCell);
            }
        }

        document.add(table);
        document.close();
        return pdfOut.toByteArray();
    }
}

}

