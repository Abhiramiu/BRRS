package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.apache.poi.ss.usermodel.VerticalAlignment;
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

import com.bornfire.brrs.entities.BRRS_M_CA7_Summary_Repo;
import com.bornfire.brrs.entities.M_CA7_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_CA7_Archival_Summary_Repo;
import com.bornfire.brrs.entities.M_CA7_Archival_Summary_Entity;


@Component
@Service

public class BRRS_M_CA7_ReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA7_ReportService.class);


	@Autowired
	private Environment env;
	
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	BRRS_M_CA7_Summary_Repo BRRS_M_CA7_Summary_Repo;
	

	@Autowired
	BRRS_M_CA7_Archival_Summary_Repo BRRS_M_CA7_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	public ModelAndView getM_CA7View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable,String type, String version) {
System.out.println("Entered service method M_CA7......................");
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;	
		
			if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_CA7_Archival_Summary_Entity> T1Master = new ArrayList<M_CA7_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				T1Master = BRRS_M_CA7_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else{
		
		 
		List<M_CA7_Summary_Entity> T1Master = new ArrayList<M_CA7_Summary_Entity>();
		try {
			Date d1 = dateformat.parse(todate);
			
			 T1Master=BRRS_M_CA7_Summary_Repo.getdatabydateList(dateformat.parse(todate));
		
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mv.addObject("reportsummary", T1Master);
}
		mv.setViewName("BRRS/M_CA7");		
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;

	}


	public void updateReport(M_CA7_Summary_Entity Entity) {
		System.out.println("Report Date: " + Entity.getReport_date());
		M_CA7_Summary_Entity existing=BRRS_M_CA7_Summary_Repo.findById(Entity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + Entity.getReport_date()));

try {
    String[] fields = { "pre_ifrs_pro", "post_ifrs9_pro", "trans_amt"}; // 👈 only the suffix

    String prefix = "R12_";

    for (String field : fields) {
        String getterName = "get" + prefix + field;
        String setterName = "set" + prefix + field;

        try {
            Method getter = M_CA7_Summary_Entity.class.getMethod(getterName);
            Method setter = M_CA7_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

            Object newValue = getter.invoke(Entity);
            setter.invoke(existing, newValue);

        } catch (NoSuchMethodException e) {
            // Skip missing fields
            continue;
        }
    }
    for (int i = 19; i <= 22; i++) {
        String prefix1 = "R" + i + "_";
        String[] fields1 = { "amt_add_year" + (i - 18) };  
        // R19 -> amt_add_year1, R20 -> amt_add_year2, etc.

        for (String field : fields1) {
            String getterName = "get" + prefix1 + field;
            String setterName = "set" + prefix1 + field;

            try {
                Method getter = M_CA7_Summary_Entity.class.getMethod(getterName);
                Method setter = M_CA7_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

                Object newValue = getter.invoke(Entity);
                setter.invoke(existing, newValue);

            } catch (NoSuchMethodException e) {
                continue;
            }
        }
    }

} catch (Exception e) {
    throw new RuntimeException("Error while updating R35 fields", e);
}


		// 3️⃣ Save updated entity
		BRRS_M_CA7_Summary_Repo.save(existing);
	}

	
	
	
	public byte[] getM_CA7Excel(String filename,String reportId, String fromdate, String todate, String currency, String dtltype,String type,String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_CA7ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}


		List<M_CA7_Summary_Entity> dataList =BRRS_M_CA7_Summary_Repo.getdatabydateList(dateformat.parse(todate)) ;

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA7 report. Returning empty result.");
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
			numberStyle.setAlignment(HorizontalAlignment.CENTER);
			numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA7_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//row12
					// Column b 
					Cell cell1 = row.getCell(1);
					if (record.getR12_pre_ifrs_pro() != null) {
						cell1.setCellValue(record.getR12_pre_ifrs_pro().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//row12
					// Column c
					 cell1 = row.getCell(2);
					if (record.getR12_post_ifrs9_pro() != null) {
						cell1.setCellValue(record.getR12_post_ifrs9_pro().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//row12
					// Column d
					
					 cell1 = row.getCell(3);
					if (record.getR12_trans_amt() != null) {
						cell1.setCellValue(record.getR12_trans_amt().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//row19
					// Column c
						row=sheet.getRow(18);
					 cell1 = row.getCell(2);
						if (record.getR19_cap_year1()!= null) {
							cell1.setCellValue(record.getR19_cap_year1().toString() + "%");
							cell1.setCellStyle(numberStyle); 
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
				

						//row19
						// Column d
					 cell1 = row.getCell(3);
						if (record.getR19_amt_add_year1() != null) {
							cell1.setCellValue(record.getR19_amt_add_year1().doubleValue());
							
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						
						//row20
						// Column c
						row=sheet.getRow(19);
					 cell1 = row.getCell(2);
					 	if (record.getR20_cap_year2()!= null) {
							cell1.setCellValue(record.getR20_cap_year2().toString() + "%");
							cell1.setCellStyle(numberStyle); 
							
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
					 	//row20
						// Column d		
					 cell1 = row.getCell(3);
						if (record.getR20_amt_add_year2() != null) {
							cell1.setCellValue(record.getR20_amt_add_year2().doubleValue());
						
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}	
						//row21
						// Column c
						row=sheet.getRow(20);
					 cell1 = row.getCell(2);
						if (record.getR21_cap_year3()!= null) {
							cell1.setCellValue(record.getR21_cap_year3().toString() + "%");
							cell1.setCellStyle(numberStyle); 
							
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						//row21
						// Column d	
					 cell1 = row.getCell(3);
						if (record.getR21_amt_add_year3() != null) {
							cell1.setCellValue(record.getR21_amt_add_year3().doubleValue());
							
						} else {
						cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						//row22
						// Column c

						row=sheet.getRow(21);
						cell1 = row.getCell(2);
						if (record.getR22_cap_year4()!= null) {
							cell1.setCellValue(record.getR22_cap_year4().doubleValue());
							cell1.setCellStyle(numberStyle); 
							
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						//row22
						// Column d										
					cell1 = row.getCell(3);
						if (record.getR22_amt_add_year4() != null) {
							cell1.setCellValue(record.getR22_amt_add_year4().doubleValue());
							
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
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

public List<Object> getM_CA7Archival() {
	List<Object> M_CA7Archivallist = new ArrayList<>();
	try {
		M_CA7Archivallist = BRRS_M_CA7_Archival_Summary_Repo.getM_CA7archival();
		System.out.println("countser" + M_CA7Archivallist.size());
	} catch (Exception e) {
		// Log the exception
		System.err.println("Error fetching M_CA7 Archival data: " + e.getMessage());
		e.printStackTrace();

		// Optionally, you can rethrow it or return empty list
		// throw new RuntimeException("Failed to fetch data", e);
	}
	return M_CA7Archivallist;
}
	public byte[] getExcelM_CA7ARCHIVAL(String filename, String reportId, String fromdate, String todate,
										   String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_CA7_Archival_Summary_Entity> dataList = BRRS_M_CA7_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SFINP2 report. Returning empty result.");
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
			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			numberStyle.setAlignment(HorizontalAlignment.CENTER);
			numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---


			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA7_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//row12
					// Column b 
					Cell cell1 = row.getCell(1);
					if (record.getR12_pre_ifrs_pro() != null) {
						cell1.setCellValue(record.getR12_pre_ifrs_pro().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//row12
					// Column c
					 cell1 = row.getCell(2);
					if (record.getR12_post_ifrs9_pro() != null) {
						cell1.setCellValue(record.getR12_post_ifrs9_pro().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//row12
					// Column d
					
					 cell1 = row.getCell(3);
					if (record.getR12_trans_amt() != null) {
						cell1.setCellValue(record.getR12_trans_amt().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//row19
					// Column c
						row=sheet.getRow(18);
					 cell1 = row.getCell(2);
						if (record.getR19_cap_year1()!= null) {
							cell1.setCellValue(record.getR19_cap_year1().toString() + "%");
							cell1.setCellStyle(numberStyle); 
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						//row19
						// Column d
					 cell1 = row.getCell(3);
						if (record.getR19_amt_add_year1() != null) {
							cell1.setCellValue(record.getR19_amt_add_year1().doubleValue());
							
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						
						//row20
						// Column c
						row=sheet.getRow(19);
					 cell1 = row.getCell(2);
					 	if (record.getR20_cap_year2()!= null) {
							cell1.setCellValue(record.getR20_cap_year2().toString() + "%");
							cell1.setCellStyle(numberStyle); 
							
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
					 	//row20
						// Column d		
					 cell1 = row.getCell(3);
						if (record.getR20_amt_add_year2() != null) {
							cell1.setCellValue(record.getR20_amt_add_year2().doubleValue());
						
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}	
						//row21
						// Column c
						row=sheet.getRow(20);
					 cell1 = row.getCell(2);
						if (record.getR21_cap_year3()!= null) {
							cell1.setCellValue(record.getR21_cap_year3().toString() + "%");
							cell1.setCellStyle(numberStyle); 
							
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						//row21
						// Column d	
					 cell1 = row.getCell(3);
						if (record.getR21_amt_add_year3() != null) {
							cell1.setCellValue(record.getR21_amt_add_year3().doubleValue());
							
						} else {
						cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						//row22
						// Column c

						row=sheet.getRow(21);
						cell1 = row.getCell(2);
						if (record.getR22_cap_year4()!= null) {
							cell1.setCellValue(record.getR22_cap_year4().doubleValue());
							cell1.setCellStyle(numberStyle); 
							
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						//row22
						// Column d										
					cell1 = row.getCell(3);
						if (record.getR22_amt_add_year4() != null) {
							cell1.setCellValue(record.getR22_amt_add_year4().doubleValue());
							
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
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
	
}

