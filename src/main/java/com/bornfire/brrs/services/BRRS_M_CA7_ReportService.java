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
import java.util.Optional;

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
import com.bornfire.brrs.entities.M_CA6_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.M_CA6_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.M_CA7_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12G_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12G_Summary_Entity;
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
		try {
			Date d1 = dateformat.parse(todate);

	 // ---------- CASE 1: ARCHIVAL ----------
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
            List<M_CA7_Archival_Summary_Entity> T1Master = 
            		BRRS_M_CA7_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
            List<M_CA7_Archival_Summary_Entity> T1Master =
            		BRRS_M_CA7_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
            List<M_CA7_Summary_Entity> T1Master = 
            		BRRS_M_CA7_Summary_Repo.getdatabydateListWithVersion(todate);
            System.out.println("T1Master Size "+T1Master.size());
            mv.addObject("reportsummary", T1Master);
        }

		} catch (ParseException e) {
			e.printStackTrace();
		}
			
		mv.setViewName("BRRS/M_CA7");		
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;

	}


	public void updateReport(M_CA7_Summary_Entity Entity) {
		System.out.println("Report Date: " + Entity.getReportDate());
		M_CA7_Summary_Entity existing=BRRS_M_CA7_Summary_Repo.findById(Entity.getReportDate())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + Entity.getReportDate()));

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


logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
Date reportDate = dateformat.parse(todate);
// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_CA7ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}


		// RESUB check
		else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
		logger.info("Service: Generating RESUB report for version {}", version);


		List<M_CA7_Archival_Summary_Entity> T1Master =
				BRRS_M_CA7_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);


		
		// Generate Excel for RESUB
		return BRRS_M_CA7ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
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
					/*
					 * row=sheet.getRow(18); cell1 = row.getCell(2); if (record.getR19_cap_year1()!=
					 * null) { cell1.setCellValue(record.getR19_cap_year1().toString() + "%");
					 * cell1.setCellStyle(numberStyle); } else { cell1.setCellValue("");
					 * cell1.setCellStyle(textStyle); }
					 */
				

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
						/*
						 * row=sheet.getRow(19); cell1 = row.getCell(2); if (record.getR20_cap_year2()!=
						 * null) { cell1.setCellValue(record.getR20_cap_year2().toString() + "%");
						 * cell1.setCellStyle(numberStyle);
						 * 
						 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
						 */
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
						/*
						 * row=sheet.getRow(20); cell1 = row.getCell(2); if (record.getR21_cap_year3()!=
						 * null) { cell1.setCellValue(record.getR21_cap_year3().toString() + "%");
						 * cell1.setCellStyle(numberStyle);
						 * 
						 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
						 */
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

	public byte[] getExcelM_CA7ARCHIVAL(String filename, String reportId, String fromdate, String todate,
										   String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_CA7_Archival_Summary_Entity> dataList = BRRS_M_CA7_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_ca7 report. Returning empty result.");
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
					/*
					 * row=sheet.getRow(18); cell1 = row.getCell(2); if (record.getR19_cap_year1()!=
					 * null) { cell1.setCellValue(record.getR19_cap_year1().toString() + "%");
					 * cell1.setCellStyle(numberStyle); } else { cell1.setCellValue("");
					 * cell1.setCellStyle(textStyle); }
					 */
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
						/*
						 * row=sheet.getRow(19); cell1 = row.getCell(2); if (record.getR20_cap_year2()!=
						 * null) { cell1.setCellValue(record.getR20_cap_year2().toString() + "%");
						 * cell1.setCellStyle(numberStyle);
						 * 
						 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
						 */
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
						/*
						 * row=sheet.getRow(20); cell1 = row.getCell(2); if (record.getR21_cap_year3()!=
						 * null) { cell1.setCellValue(record.getR21_cap_year3().toString() + "%");
						 * cell1.setCellStyle(numberStyle);
						 * 
						 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
						 */
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
	

	/// RESUB VIEW
	public List<Object[]> getM_CA7Resub() {
	List<Object[]> resubList = new ArrayList<>();
	try {
	List<M_CA7_Archival_Summary_Entity> latestArchivalList = 
			BRRS_M_CA7_Archival_Summary_Repo.getdatabydateListWithVersionAll();

	if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	for (M_CA7_Archival_Summary_Entity entity : latestArchivalList) {
	Object[] row = new Object[] {
	entity.getReportDate(),
	entity.getReportVersion()
	};
	resubList.add(row);
	}
	System.out.println("Fetched " + resubList.size() + " record(s)");
	} else {
	System.out.println("No archival data found.");
	}
	} catch (Exception e) {
	System.err.println("Error fetching M_CA7 Resub data: " + e.getMessage());
	e.printStackTrace();
	}
	return resubList;
	}
		
		
		//Archival View
		public List<Object[]> getM_CA7Archival() {
			List<Object[]> archivalList = new ArrayList<>();

			try {
				List<M_CA7_Archival_Summary_Entity> repoData = BRRS_M_CA7_Archival_Summary_Repo
						.getdatabydateListWithVersionAll();

				if (repoData != null && !repoData.isEmpty()) {
					for (M_CA7_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReportDate(), 
								entity.getReportVersion() 
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					M_CA7_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReportVersion());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching M_CA7 Archival data: " + e.getMessage());
				e.printStackTrace();
			}

			return archivalList;
		}
	// Resubmit the values , latest version and Resub Date
		public void updateReportReSub(M_CA7_Summary_Entity updatedEntity) {
			System.out.println("Came to Resub Service");
			System.out.println("Report Date: " + updatedEntity.getReportDate());

			Date reportDate = updatedEntity.getReportDate();
			int newVersion = 1;

			try {
				// Fetch the latest archival version for this report date
				Optional<M_CA7_Archival_Summary_Entity> latestArchivalOpt = BRRS_M_CA7_Archival_Summary_Repo
						.getLatestArchivalVersionByDate(reportDate);

				// Determine next version number
				if (latestArchivalOpt.isPresent()) {
					M_CA7_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
					try {
						newVersion = Integer.parseInt(latestArchival.getReportVersion()) + 1;
					} catch (NumberFormatException e) {
						System.err.println("Invalid version format. Defaulting to version 1");
						newVersion = 1;
					}
				} else {
					System.out.println("No previous archival found for date: " + reportDate);
				}

				// Prevent duplicate version number
				boolean exists = BRRS_M_CA7_Archival_Summary_Repo
						.findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
						.isPresent();

				if (exists) {
					throw new RuntimeException("Version " + newVersion + " already exists for report date " + reportDate);
				}

				// Copy summary entity to archival entity
				M_CA7_Archival_Summary_Entity archivalEntity = new M_CA7_Archival_Summary_Entity();
				org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

				archivalEntity.setReportDate(reportDate);
				archivalEntity.setReportVersion(String.valueOf(newVersion));
				archivalEntity.setReportResubDate(new Date());

				System.out.println("Saving new archival version: " + newVersion);

				// Save new version to repository
				BRRS_M_CA7_Archival_Summary_Repo.save(archivalEntity);

				System.out.println(" Saved archival version successfully: " + newVersion);

			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error while creating archival resubmission record", e);
			}
		}
		/// Downloaded for Archival & Resub
		public byte[] BRRS_M_CA7ResubExcel(String filename, String reportId, String fromdate,
	        String todate, String currency, String dtltype,
	        String type, String version) throws Exception {

	    logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

	    if (type.equals("RESUB") & version != null) {
	       
	    }

	    List<M_CA7_Archival_Summary_Entity> dataList1 =
	        BRRS_M_CA7_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

	    if (dataList1.isEmpty()) {
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

				if (!dataList1.isEmpty()) {
					for (int i = 0; i < dataList1.size(); i++) {
						M_CA7_Archival_Summary_Entity record = dataList1.get(i);
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
						/*
						 * row=sheet.getRow(18); cell1 = row.getCell(2); if (record.getR19_cap_year1()!=
						 * null) { cell1.setCellValue(record.getR19_cap_year1().toString() + "%");
						 * cell1.setCellStyle(numberStyle); } else { cell1.setCellValue("");
						 * cell1.setCellStyle(textStyle); }
						 */

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
							/*
							 * row=sheet.getRow(19); cell1 = row.getCell(2); if (record.getR20_cap_year2()!=
							 * null) { cell1.setCellValue(record.getR20_cap_year2().toString() + "%");
							 * cell1.setCellStyle(numberStyle);
							 * 
							 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
							 */
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
							/*
							 * row=sheet.getRow(20); cell1 = row.getCell(2); if (record.getR21_cap_year3()!=
							 * null) { cell1.setCellValue(record.getR21_cap_year3().toString() + "%");
							 * cell1.setCellStyle(numberStyle);
							 * 
							 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
							 */
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

