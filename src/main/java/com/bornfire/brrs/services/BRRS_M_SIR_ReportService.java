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

import com.bornfire.brrs.entities.BRRS_M_SIR_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SIR_Archival_Summary_Repo;
import com.bornfire.brrs.entities.M_SIR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SIR_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_SIR_Summary_Repo;
@Service
@Component
public class BRRS_M_SIR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SIR_ReportService.class);


	@Autowired
	private Environment env;
	
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	BRRS_M_SIR_Summary_Repo BRRS_M_SIR_Summary_Repo;
	


	@Autowired
	BRRS_M_SIR_Archival_Summary_Repo BRRS_M_SIR_Archival_Summary_Repo;
	
	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_SIRView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable,String type, String version) {
		System.out.println("Entered service method M_SIR......................");
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;	
		
		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_SIR_Archival_Summary_Entity> T1Master = new ArrayList<M_SIR_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				T1Master = BRRS_M_SIR_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} 
		else {
		List<M_SIR_Summary_Entity> T1Master = new ArrayList<M_SIR_Summary_Entity>();
		try {
			Date d1 = dateformat.parse(todate);
			T1Master=BRRS_M_SIR_Summary_Repo.getdatabydateList(dateformat.parse(todate));
 System.out.println("count "+T1Master.size());		
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/M_SIR");		
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public List<Object> getM_SIRArchival() {
		List<Object> M_SIRArchivallist = new ArrayList<>();
		try {
			M_SIRArchivallist = BRRS_M_SIR_Archival_Summary_Repo.getM_SIRarchival();
			System.out.println("countser" + M_SIRArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_SIR Archival data: " + e.getMessage());
			e.printStackTrace();
		}
		return M_SIRArchivallist;
	}


		public void updateReport(M_SIR_Summary_Entity Entity) {
		System.out.println("Report Date: " + Entity.getReport_date());
		M_SIR_Summary_Entity existing=BRRS_M_SIR_Summary_Repo.findById(Entity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + Entity.getReport_date()));
		try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 13; i <= 17; i++) {
	            String prefix = "R" + i + "_";
	            String[] fields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m",
	                                "risk_6to24m", "capital_6to24m", "amt_gt24m",
	                                "risk_gt24m", "capital_gt24m"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(Entity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }
	        String[] totalFields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m",
                    "risk_6to24m", "capital_6to24m", "amt_gt24m",
                    "risk_gt24m", "capital_gt24m" };
	        for (String field : totalFields) {
	            String getterName = "getR12_" + field;
	            String setterName = "setR12_" + field;

	            try {
	                Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
	                Method setter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                Object newValue = getter.invoke(Entity);
	                setter.invoke(existing, newValue);

	            } catch (NoSuchMethodException e) {
	                // Skip if not present
	                continue;
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

		try {
			
	        for (int i = 19; i <= 23; i++) {
	            String prefix = "R" + i + "_";
	            String[] fields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m",
	                                "risk_6to24m", "capital_6to24m", "amt_gt24m",
	                                "risk_gt24m", "capital_gt24m"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(Entity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }
	        String[] totalFields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m",
                    "risk_6to24m", "capital_6to24m", "amt_gt24m",
                    "risk_gt24m", "capital_gt24m" };
	        for (String field : totalFields) {
	            String getterName = "getR18_" + field;
	            String setterName = "setR18_" + field;

	            try {
	                Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
	                Method setter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                Object newValue = getter.invoke(Entity);
	                setter.invoke(existing, newValue);

	            } catch (NoSuchMethodException e) {
	                // Skip if not present
	                continue;
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
		
		//------------------------------------------------------------------------
try {
			
	for (int i = 24; i <= 26; i++) {
        String prefix = "R" + i + "_";
        String[] fields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m",
                            "risk_6to24m", "capital_6to24m", "amt_gt24m",
                            "risk_gt24m", "capital_gt24m"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(Entity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }
}

catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

try {
	
    for (int i = 28; i <= 32; i++) {
        String prefix = "R" + i + "_";
        String[] fields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m",
                            "risk_6to24m", "capital_6to24m", "amt_gt24m",
                            "risk_gt24m", "capital_gt24m"};

        for (String field : fields) {
            String getterName = "get" + prefix + field;
            String setterName = "set" + prefix + field;

            try {
                Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
                Method setter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

                Object newValue = getter.invoke(Entity);
                setter.invoke(existing, newValue);

            } catch (NoSuchMethodException e) {
                // Skip missing fields
                continue;
            }
        }
    }
    String[] totalFields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m",
            "risk_6to24m", "capital_6to24m", "amt_gt24m",
            "risk_gt24m", "capital_gt24m" };
    for (String field : totalFields) {
        String getterName = "getR27_" + field;
        String setterName = "setR27_" + field;

        try {
            Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
            Method setter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

            Object newValue = getter.invoke(Entity);
            setter.invoke(existing, newValue);

        } catch (NoSuchMethodException e) {
            // Skip if not present
            continue;
        }
    }

} catch (Exception e) {
    throw new RuntimeException("Error while updating report fields", e);
}

try {
    String[] fields = { "capital_6m",
                         "capital_6to24m",
                         "capital_gt24m" };

    String prefix = "R33_";

    for (String field : fields) {
        String getterName = "get" + prefix + field;
        String setterName = "set" + prefix + field;

        try {
            Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
            Method setter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

            Object newValue = getter.invoke(Entity);
            setter.invoke(existing, newValue);

        } catch (NoSuchMethodException e) {
            // Skip missing fields
            continue;
        }
    }
} catch (Exception e) {
    throw new RuntimeException("Error while updating R33 fields", e);
}
try {
    String[] fields = { "tot_spec_risk_ch" }; // 👈 only the suffix

    String prefix = "R35_";

    for (String field : fields) {
        String getterName = "get" + prefix + field;
        String setterName = "set" + prefix + field;

        try {
            Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
            Method setter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

            Object newValue = getter.invoke(Entity);
            setter.invoke(existing, newValue);

        } catch (NoSuchMethodException e) {
            // Skip missing fields
            continue;
        }
    }
} catch (Exception e) {
    throw new RuntimeException("Error while updating R35 fields", e);
}


		// 3️⃣ Save updated entity
		BRRS_M_SIR_Summary_Repo.save(existing);
	}


public List<Object> getM_SIRarchival() {
	List<Object> M_SIR_Archivallist = new ArrayList<>();
	try {
		M_SIR_Archivallist = BRRS_M_SIR_Archival_Summary_Repo.getM_SIRarchival();
		
		System.out.println("countser" + M_SIR_Archivallist.size());
	} catch (Exception e) {
		// Log the exception
		System.err.println("Error fetching M_CA7 Archival data: " + e.getMessage());
		e.printStackTrace();

		// Optionally, you can rethrow it or return empty list
		// throw new RuntimeException("Failed to fetch data", e);
	}
	return M_SIR_Archivallist;
}

	public byte[] getM_SIRExcel(String filename,String reportId, String fromdate, String todate, String currency, String dtltype,String type,String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);
		if (type.equals("ARCHIVAL") & version != null) {
			byte[] ARCHIVALreport = getExcelM_SIRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
			return ARCHIVALreport;
		}
		List<M_SIR_Summary_Entity> dataList =BRRS_M_SIR_Summary_Repo.getdatabydateList(dateformat.parse(todate)) ;

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SIR report. Returning empty result.");
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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SIR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//----------
					
					//row11
					// Column C 
					row=sheet.getRow(11);
					Cell cell1=row.getCell(1);
					if(record.getR12_product() != null) {
						cell1.setCellValue(record.getR12_product());
						
					}else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
						
					}
					
					 cell1 = row.getCell(2);
					if (record.getR12_amt_6m() != null) {
						cell1.setCellValue(record.getR12_amt_6m().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//row11
					// Column E 
					 cell1 = row.getCell(4);
					if (record.getR12_capital_6m() != null) {
						cell1.setCellValue(record.getR12_capital_6m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//row11
					// Column F 
					 cell1 = row.getCell(5);
					if (record.getR12_amt_6to24m() != null) {
						cell1.setCellValue(record.getR12_amt_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
					//row11
					// Column H
					 cell1 = row.getCell(7);
					if (record.getR12_capital_6to24m() != null) {
						cell1.setCellValue(record.getR12_capital_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
					//row11
					// Column I 
					 cell1 = row.getCell(8);
					if (record.getR12_amt_gt24m() != null) {
						cell1.setCellValue(record.getR12_amt_gt24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}												
					//row11
					// Column K
					 cell1 = row.getCell(10);
					if (record.getR12_capital_gt24m() != null) {
						cell1.setCellValue(record.getR12_capital_gt24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}												
//-------------

					//row12
					// Column b
					row=sheet.getRow(12);
					cell1=row.getCell(1);
					if(record.getR13_product() != null) {
						cell1.setCellValue(record.getR13_product());
						
					}else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
						
					}
					//row12
					// Column C 
					
					 cell1 = row.getCell(2);
					if (record.getR13_amt_6m() != null) {
						cell1.setCellValue(record.getR13_amt_6m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column D
//					 cell1 = row.getCell(3);
//					if (record.getR13_risk_6m() != null) {
//						cell1.setCellValue(record.getR13_risk_6m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}
					
					// Column E 
					 cell1 = row.getCell(4);
					if (record.getR13_capital_6m() != null) {
						cell1.setCellValue(record.getR13_capital_6m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column F 
					 cell1 = row.getCell(5);
					if (record.getR13_amt_6to24m() != null) {
						cell1.setCellValue(record.getR13_amt_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//					 cell1 = row.getCell(6);
//					if (record.getR13_risk_6to24m() != null) {
//						cell1.setCellValue(record.getR13_risk_6to24m().doubleValue());
//					
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}					
				
					// Column H
					 cell1 = row.getCell(7);
					if (record.getR13_capital_6to24m() != null) {
						cell1.setCellValue(record.getR13_capital_6to24m().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
				
					// Column I 
					 cell1 = row.getCell(8);
					if (record.getR13_amt_gt24m() != null) {
						cell1.setCellValue(record.getR13_amt_gt24m().doubleValue());
				
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
				
																	
//					 cell1 = row.getCell(9);
//					if (record.getR13_risk_gt24m() != null) {
//						cell1.setCellValue(record.getR13_risk_gt24m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}												
				
					// Column K
					 cell1 = row.getCell(10);
					if (record.getR13_capital_gt24m() != null) {
						cell1.setCellValue(record.getR13_capital_gt24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}			
//-------------
					row=sheet.getRow(13);
					cell1=row.getCell(1);
					if(record.getR14_product() != null) {
						cell1.setCellValue(record.getR14_product());
					
					}else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
						
					}
					//row13
					// Column C 
				
					 cell1 = row.getCell(2);
					if (record.getR14_amt_6m() != null) {
						cell1.setCellValue(record.getR14_amt_6m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column D
//					 cell1 = row.getCell(3);
//					if (record.getR14_risk_6m() != null) {
//						cell1.setCellValue(record.getR14_risk_6m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}
					
					// Column E 
					 cell1 = row.getCell(4);
					if (record.getR14_capital_6m() != null) {
						cell1.setCellValue(record.getR14_capital_6m().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
				
					// Column F 
					 cell1 = row.getCell(5);
					if (record.getR14_amt_6to24m() != null) {
						cell1.setCellValue(record.getR14_amt_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
				
					// Column G 
//					 cell1 = row.getCell(6);
//					if (record.getR14_risk_6to24m() != null) {
//						cell1.setCellValue(record.getR14_risk_6to24m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}					
					
					// Column H
					 cell1 = row.getCell(7);
					if (record.getR14_capital_6to24m() != null) {
						cell1.setCellValue(record.getR14_capital_6to24m().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
				
					// Column I 
					 cell1 = row.getCell(8);
					if (record.getR14_amt_gt24m() != null) {
						cell1.setCellValue(record.getR14_amt_gt24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
				
					// Column J 
//					 cell1 = row.getCell(9);
//					if (record.getR14_risk_gt24m() != null) {
//						cell1.setCellValue(record.getR14_risk_gt24m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}											
					
					// Column K
					 cell1 = row.getCell(10);
					if (record.getR14_capital_gt24m() != null) {
						cell1.setCellValue(record.getR14_capital_gt24m().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}			
//---------------
					
					//row14
					// Column C 
					row=sheet.getRow(14);
					cell1=row.getCell(1);
					if(record.getR15_product() != null) {
						cell1.setCellValue(record.getR15_product());
						
					}else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
						
					}
				
					 cell1 = row.getCell(2);
					if (record.getR15_amt_6m() != null) {
						cell1.setCellValue(record.getR15_amt_6m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column D
//					 cell1 = row.getCell(3);
//					if (record.getR15_risk_6m() != null) {
//						cell1.setCellValue(record.getR15_risk_6m().doubleValue());
//					
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}
				
					// Column E 
					 cell1 = row.getCell(4);
					if (record.getR15_capital_6m() != null) {
						cell1.setCellValue(record.getR15_capital_6m().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column F 
					 cell1 = row.getCell(5);
					if (record.getR15_amt_6to24m() != null) {
						cell1.setCellValue(record.getR15_amt_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					// Column G 
//					 cell1 = row.getCell(6);
//					if (record.getR15_risk_6to24m() != null) {
//						cell1.setCellValue(record.getR15_risk_6to24m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}					
				
					// Column H
					 cell1 = row.getCell(7);
					if (record.getR15_capital_6to24m() != null) {
						cell1.setCellValue(record.getR15_capital_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
					
					// Column I 
					 cell1 = row.getCell(8);
					if (record.getR15_amt_gt24m() != null) {
						cell1.setCellValue(record.getR15_amt_gt24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
				
					// Column J 
//					 cell1 = row.getCell(9);
//					if (record.getR15_risk_gt24m() != null) {
//						cell1.setCellValue(record.getR15_risk_gt24m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}											
					
					// Column K
					 cell1 = row.getCell(10);
					if (record.getR15_capital_gt24m() != null) {
						cell1.setCellValue(record.getR15_capital_gt24m().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
															
//---------------
					row=sheet.getRow(15);
					cell1=row.getCell(1);
					if(record.getR16_product() != null) {
						cell1.setCellValue(record.getR16_product());
						
					}else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
						
					}
					//row15
					// Column C 
					
					 cell1 = row.getCell(2);
					if (record.getR16_amt_6m() != null) {
						cell1.setCellValue(record.getR16_amt_6m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column D
//					 cell1 = row.getCell(3);
//					if (record.getR16_risk_6m() != null) {
//						cell1.setCellValue(record.getR16_risk_6m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}
				
					// Column E 
					 cell1 = row.getCell(4);
					if (record.getR16_capital_6m() != null) {
						cell1.setCellValue(record.getR16_capital_6m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column F 
					 cell1 = row.getCell(5);
					if (record.getR16_amt_6to24m() != null) {
						cell1.setCellValue(record.getR16_amt_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					// Column G 
//					 cell1 = row.getCell(6);
//					if (record.getR16_risk_6to24m() != null) {
//						cell1.setCellValue(record.getR16_risk_6to24m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}					
				
					// Column H
					 cell1 = row.getCell(7);
					if (record.getR16_capital_6to24m() != null) {
						cell1.setCellValue(record.getR16_capital_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
					
					// Column I 
					 cell1 = row.getCell(8);
					if (record.getR16_amt_gt24m() != null) {
						cell1.setCellValue(record.getR16_amt_gt24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
				
					// Column J 
//					 cell1 = row.getCell(9);
//					if (record.getR16_risk_gt24m() != null) {
//						cell1.setCellValue(record.getR16_risk_gt24m().doubleValue());
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}											
					
					// Column K
					 cell1 = row.getCell(10);
					if (record.getR16_capital_gt24m() != null) {
						cell1.setCellValue(record.getR16_capital_gt24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
					
//---------------

//row16
// Column b 
					row=sheet.getRow(16);
					cell1=row.getCell(1);
					if(record.getR17_product() != null) {
						cell1.setCellValue(record.getR17_product());
					}else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
						
					}
//column c
 cell1 = row.getCell(2);
if (record.getR17_amt_6m() != null) {
	cell1.setCellValue(record.getR17_amt_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
// Column D
// cell1 = row.getCell(3);
//if (record.getR17_risk_6m() != null) {
//	cell1.setCellValue(record.getR17_risk_6m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}

// Column E 
 cell1 = row.getCell(4);
if (record.getR17_capital_6m() != null) {
	cell1.setCellValue(record.getR17_capital_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}

// Column F 
 cell1 = row.getCell(5);
if (record.getR17_amt_6to24m() != null) {
	cell1.setCellValue(record.getR17_amt_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}	

// Column G 
// cell1 = row.getCell(6);
//if (record.getR17_risk_6to24m() != null) {
//	cell1.setCellValue(record.getR17_risk_6to24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}					

// Column H
 cell1 = row.getCell(7);
if (record.getR17_capital_6to24m() != null) {
	cell1.setCellValue(record.getR17_capital_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

// Column I 
 cell1 = row.getCell(8);
if (record.getR17_amt_gt24m() != null) {
	cell1.setCellValue(record.getR17_amt_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

//Column J 
// cell1 = row.getCell(9);
//if (record.getR17_risk_gt24m() != null) {
//	cell1.setCellValue(record.getR17_risk_gt24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}											

// Column K
 cell1 = row.getCell(10);
if (record.getR17_capital_gt24m() != null) {
	cell1.setCellValue(record.getR17_capital_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}															
//----------

row=sheet.getRow(17);
cell1=row.getCell(1);
if(record.getR18_product() != null) {
	cell1.setCellValue(record.getR18_product());
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row17
// Column C 

cell1 = row.getCell(2);
if (record.getR18_amt_6m() != null) {
	cell1.setCellValue(record.getR18_amt_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
//row11
// Column E 
 cell1 = row.getCell(4);
if (record.getR18_capital_6m() != null) {
	cell1.setCellValue(record.getR18_capital_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
//row11
// Column F 
 cell1 = row.getCell(5);
if (record.getR18_amt_6to24m() != null) {
	cell1.setCellValue(record.getR18_amt_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					
//row11
// Column H
 cell1 = row.getCell(7);
if (record.getR18_capital_6to24m() != null) {
	cell1.setCellValue(record.getR18_capital_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					
//row11
// Column I 
 cell1 = row.getCell(8);
if (record.getR18_amt_gt24m() != null) {
	cell1.setCellValue(record.getR18_amt_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}												
//row11
// Column K
 cell1 = row.getCell(10);
if (record.getR18_capital_gt24m() != null) {
	cell1.setCellValue(record.getR18_capital_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}												
//---------------
row=sheet.getRow(18);
cell1=row.getCell(1);
if(record.getR19_product() != null) {
	cell1.setCellValue(record.getR19_product());
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row18
//Column C 
cell1 = row.getCell(2);
if (record.getR19_amt_6m() != null) {
	cell1.setCellValue(record.getR19_amt_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR19_risk_6m() != null) {
//	cell1.setCellValue(record.getR19_risk_6m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR19_capital_6m() != null) {
	cell1.setCellValue(record.getR19_capital_6m().doubleValue());
	
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR19_amt_6to24m() != null) {
	cell1.setCellValue(record.getR19_amt_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR19_risk_6to24m() != null) {
//	cell1.setCellValue(record.getR19_risk_6to24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR19_capital_6to24m() != null) {
	cell1.setCellValue(record.getR19_capital_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR19_amt_gt24m() != null) {
	cell1.setCellValue(record.getR19_amt_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR19_risk_gt24m() != null) {
//	cell1.setCellValue(record.getR19_risk_gt24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR19_capital_gt24m() != null) {
	cell1.setCellValue(record.getR19_capital_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}															
//----------
row=sheet.getRow(19);
cell1=row.getCell(1);
if(record.getR20_product() != null) {
	cell1.setCellValue(record.getR20_product());
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row19
//Column C 

cell1 = row.getCell(2);
if (record.getR20_amt_6m() != null) {
	cell1.setCellValue(record.getR20_amt_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR20_risk_6m() != null) {
//	cell1.setCellValue(record.getR20_risk_6m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR20_capital_6m() != null) {
	cell1.setCellValue(record.getR20_capital_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR20_amt_6to24m() != null) {
	cell1.setCellValue(record.getR20_amt_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR20_risk_6to24m() != null) {
//	cell1.setCellValue(record.getR20_risk_6to24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR20_capital_6to24m() != null) {
	cell1.setCellValue(record.getR20_capital_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR20_amt_gt24m() != null) {
	cell1.setCellValue(record.getR20_amt_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR20_risk_gt24m() != null) {
//	cell1.setCellValue(record.getR20_risk_gt24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR20_capital_gt24m() != null) {
	cell1.setCellValue(record.getR20_capital_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}															
//----------					
row=sheet.getRow(20);
cell1=row.getCell(1);
if(record.getR21_product() != null) {
	cell1.setCellValue(record.getR21_product());
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row20
//Column C 

cell1 = row.getCell(2);
if (record.getR21_amt_6m() != null) {
	cell1.setCellValue(record.getR21_amt_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR21_risk_6m() != null) {
//	cell1.setCellValue(record.getR21_risk_6m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR21_capital_6m() != null) {
	cell1.setCellValue(record.getR21_capital_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR21_amt_6to24m() != null) {
	cell1.setCellValue(record.getR21_amt_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR21_risk_6to24m() != null) {
//	cell1.setCellValue(record.getR21_risk_6to24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR21_capital_6to24m() != null) {
	cell1.setCellValue(record.getR21_capital_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR21_amt_gt24m() != null) {
	cell1.setCellValue(record.getR21_amt_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR21_risk_gt24m() != null) {
//	cell1.setCellValue(record.getR21_risk_gt24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR21_capital_gt24m() != null) {
	cell1.setCellValue(record.getR21_capital_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}															
//----------					
					

//----------					
row=sheet.getRow(21);
cell1=row.getCell(1);
if(record.getR22_product() != null) {
	cell1.setCellValue(record.getR22_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row21
//Column C 

cell1 = row.getCell(2);
if (record.getR22_amt_6m() != null) {
cell1.setCellValue(record.getR22_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR22_risk_6m() != null) {
//cell1.setCellValue(record.getR22_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR22_capital_6m() != null) {
cell1.setCellValue(record.getR22_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR22_amt_6to24m() != null) {
cell1.setCellValue(record.getR22_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR22_risk_6to24m() != null) {
//cell1.setCellValue(record.getR22_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR22_capital_6to24m() != null) {
cell1.setCellValue(record.getR22_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR22_amt_gt24m() != null) {
cell1.setCellValue(record.getR22_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR22_risk_gt24m() != null) {
//cell1.setCellValue(record.getR22_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR22_capital_gt24m() != null) {
cell1.setCellValue(record.getR22_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------					

//----------					
row=sheet.getRow(22);
cell1=row.getCell(1);
if(record.getR23_product() != null) {
	cell1.setCellValue(record.getR23_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row22
//Column C 

cell1 = row.getCell(2);
if (record.getR23_amt_6m() != null) {
cell1.setCellValue(record.getR23_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR23_risk_6m() != null) {
//cell1.setCellValue(record.getR23_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR23_capital_6m() != null) {
cell1.setCellValue(record.getR23_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR23_amt_6to24m() != null) {
cell1.setCellValue(record.getR23_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR23_risk_6to24m() != null) {
//cell1.setCellValue(record.getR23_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR23_capital_6to24m() != null) {
cell1.setCellValue(record.getR23_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR23_amt_gt24m() != null) {
cell1.setCellValue(record.getR23_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR23_risk_gt24m() != null) {
//cell1.setCellValue(record.getR23_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR23_capital_gt24m() != null) {
cell1.setCellValue(record.getR23_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------					

row=sheet.getRow(23);
cell1=row.getCell(1);
if(record.getR24_product() != null) {
	cell1.setCellValue(record.getR24_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row23
//Column C 
cell1 = row.getCell(2);
if (record.getR24_amt_6m() != null) {
cell1.setCellValue(record.getR24_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR24_risk_6m() != null) {
//cell1.setCellValue(record.getR24_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR24_capital_6m() != null) {
cell1.setCellValue(record.getR24_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR24_amt_6to24m() != null) {
cell1.setCellValue(record.getR24_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR24_risk_6to24m() != null) {
//cell1.setCellValue(record.getR24_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR24_capital_6to24m() != null) {
cell1.setCellValue(record.getR24_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR24_amt_gt24m() != null) {
cell1.setCellValue(record.getR24_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR24_risk_gt24m() != null) {
//cell1.setCellValue(record.getR24_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR24_capital_gt24m() != null) {
cell1.setCellValue(record.getR24_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------					
					
//----------					

row=sheet.getRow(24);
cell1=row.getCell(1);
if(record.getR25_product() != null) {
	cell1.setCellValue(record.getR25_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row24
//Column C 
cell1 = row.getCell(2);
if (record.getR25_amt_6m() != null) {
cell1.setCellValue(record.getR25_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR25_risk_6m() != null) {
//cell1.setCellValue(record.getR25_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR25_capital_6m() != null) {
cell1.setCellValue(record.getR25_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR25_amt_6to24m() != null) {
cell1.setCellValue(record.getR25_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR25_risk_6to24m() != null) {
//cell1.setCellValue(record.getR25_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR25_capital_6to24m() != null) {
cell1.setCellValue(record.getR25_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR25_amt_gt24m() != null) {
cell1.setCellValue(record.getR25_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR25_risk_gt24m() != null) {
//cell1.setCellValue(record.getR25_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR25_capital_gt24m() != null) {
cell1.setCellValue(record.getR25_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------					
					
								

//----------					


row=sheet.getRow(25);
cell1=row.getCell(1);
if(record.getR26_product() != null) {
	cell1.setCellValue(record.getR26_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row25
//Column C 
cell1 = row.getCell(2);
if (record.getR26_amt_6m() != null) {
cell1.setCellValue(record.getR26_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR26_risk_6m() != null) {
//cell1.setCellValue(record.getR26_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR26_capital_6m() != null) {
cell1.setCellValue(record.getR26_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR26_amt_6to24m() != null) {
cell1.setCellValue(record.getR26_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR26_risk_6to24m() != null) {
//cell1.setCellValue(record.getR26_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR26_capital_6to24m() != null) {
cell1.setCellValue(record.getR26_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR26_amt_gt24m() != null) {
cell1.setCellValue(record.getR26_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR26_risk_gt24m() != null) {
//cell1.setCellValue(record.getR26_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR26_capital_gt24m() != null) {
cell1.setCellValue(record.getR26_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------					

row=sheet.getRow(26);
cell1=row.getCell(1);
if(record.getR27_product() != null) {
	cell1.setCellValue(record.getR27_product());
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row26
// Column C 
 cell1 = row.getCell(2);
if (record.getR27_amt_6m() != null) {
	cell1.setCellValue(record.getR27_amt_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
//row11
// Column E 
 cell1 = row.getCell(4);
if (record.getR27_capital_6m() != null) {
	cell1.setCellValue(record.getR27_capital_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
//row11
// Column F 
 cell1 = row.getCell(5);
if (record.getR27_amt_6to24m() != null) {
	cell1.setCellValue(record.getR27_amt_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					
//row11
// Column H
 cell1 = row.getCell(7);
if (record.getR27_capital_6to24m() != null) {
	cell1.setCellValue(record.getR27_capital_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					
//row11
// Column I 
 cell1 = row.getCell(8);
if (record.getR27_amt_gt24m() != null) {
	cell1.setCellValue(record.getR27_amt_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}												
//row11
// Column K
 cell1 = row.getCell(10);
if (record.getR27_capital_gt24m() != null) {
	cell1.setCellValue(record.getR27_capital_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}												
//-------------
//----------					


row=sheet.getRow(27);
cell1=row.getCell(1);
if(record.getR28_product() != null) {
	cell1.setCellValue(record.getR28_product());
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row27
//Column C 
cell1 = row.getCell(2);
if (record.getR28_amt_6m() != null) {
cell1.setCellValue(record.getR28_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR28_risk_6m() != null) {
//cell1.setCellValue(record.getR28_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR28_capital_6m() != null) {
cell1.setCellValue(record.getR28_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR28_amt_6to24m() != null) {
cell1.setCellValue(record.getR28_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR28_risk_6to24m() != null) {
//cell1.setCellValue(record.getR28_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR28_capital_6to24m() != null) {
cell1.setCellValue(record.getR28_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR28_amt_gt24m() != null) {
cell1.setCellValue(record.getR28_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR28_risk_gt24m() != null) {
//cell1.setCellValue(record.getR28_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR28_capital_gt24m() != null) {
cell1.setCellValue(record.getR28_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------		

//----------					


row=sheet.getRow(28);
cell1=row.getCell(1);
if(record.getR29_product() != null) {
	cell1.setCellValue(record.getR29_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row28
//Column C 
cell1 = row.getCell(2);
if (record.getR29_amt_6m() != null) {
cell1.setCellValue(record.getR29_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR29_risk_6m() != null) {
//cell1.setCellValue(record.getR29_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR29_capital_6m() != null) {
cell1.setCellValue(record.getR29_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR29_amt_6to24m() != null) {
cell1.setCellValue(record.getR29_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR29_risk_6to24m() != null) {
//cell1.setCellValue(record.getR29_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR29_capital_6to24m() != null) {
cell1.setCellValue(record.getR29_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR29_amt_gt24m() != null) {
cell1.setCellValue(record.getR29_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR29_risk_gt24m() != null) {
//cell1.setCellValue(record.getR29_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR29_capital_gt24m() != null) {
cell1.setCellValue(record.getR29_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------		


//----------					


row=sheet.getRow(30);
cell1=row.getCell(1);
if(record.getR31_product() != null) {
	cell1.setCellValue(record.getR31_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row29
//Column C 
row=sheet.getRow(29);
cell1 = row.getCell(2);
if (record.getR30_amt_6m() != null) {
cell1.setCellValue(record.getR30_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR30_risk_6m() != null) {
//cell1.setCellValue(record.getR30_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR30_capital_6m() != null) {
cell1.setCellValue(record.getR30_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR30_amt_6to24m() != null) {
cell1.setCellValue(record.getR30_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR30_risk_6to24m() != null) {
//cell1.setCellValue(record.getR30_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR30_capital_6to24m() != null) {
cell1.setCellValue(record.getR30_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR30_amt_gt24m() != null) {
cell1.setCellValue(record.getR30_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR30_risk_gt24m() != null) {
//cell1.setCellValue(record.getR30_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR30_capital_gt24m() != null) {
cell1.setCellValue(record.getR30_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------		
	
			


//----------					

row=sheet.getRow(30);
cell1=row.getCell(1);
if(record.getR31_product() != null) {
	cell1.setCellValue(record.getR31_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}

//row30
//Column C 
row=sheet.getRow(30);
cell1 = row.getCell(2);
if (record.getR31_amt_6m() != null) {
cell1.setCellValue(record.getR31_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR31_risk_6m() != null) {
//cell1.setCellValue(record.getR31_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR31_capital_6m() != null) {
cell1.setCellValue(record.getR31_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR31_amt_6to24m() != null) {
cell1.setCellValue(record.getR31_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR31_risk_6to24m() != null) {
//cell1.setCellValue(record.getR31_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR31_capital_6to24m() != null) {
cell1.setCellValue(record.getR31_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR31_amt_gt24m() != null) {
cell1.setCellValue(record.getR31_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR31_risk_gt24m() != null) {
//cell1.setCellValue(record.getR31_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR31_capital_gt24m() != null) {
cell1.setCellValue(record.getR31_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------		
	


//----------					


row=sheet.getRow(31);
cell1=row.getCell(1);
if(record.getR32_product() != null) {
	cell1.setCellValue(record.getR32_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row31
//Column C 
row=sheet.getRow(31);
cell1 = row.getCell(2);
if (record.getR32_amt_6m() != null) {
cell1.setCellValue(record.getR32_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR32_risk_6m() != null) {
//cell1.setCellValue(record.getR32_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR32_capital_6m() != null) {
cell1.setCellValue(record.getR32_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR32_amt_6to24m() != null) {
cell1.setCellValue(record.getR32_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR32_risk_6to24m() != null) {
//cell1.setCellValue(record.getR32_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR32_capital_6to24m() != null) {
cell1.setCellValue(record.getR32_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR32_amt_gt24m() != null) {
cell1.setCellValue(record.getR32_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR32_risk_gt24m() != null) {
//cell1.setCellValue(record.getR32_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR32_capital_gt24m() != null) {
cell1.setCellValue(record.getR32_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------						

row=sheet.getRow(32);
cell1=row.getCell(1);
if(record.getR33_product() != null) {
	cell1.setCellValue(record.getR33_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}			

//row31
//Column E
cell1 = row.getCell(4);
if (record.getR33_capital_6m() != null) {
cell1.setCellValue(record.getR33_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
					

//row31
//Column H
row=sheet.getRow(32);
cell1 = row.getCell(7);
if (record.getR33_capital_6to24m() != null) {
cell1.setCellValue(record.getR33_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//row31
//Column K
row=sheet.getRow(32);
cell1 = row.getCell(10);
if (record.getR33_capital_gt24m() != null) {
cell1.setCellValue(record.getR33_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
								

//row34
//Column K
row=sheet.getRow(34);
cell1 = row.getCell(4);
if (record.getR35_tot_spec_risk_ch() != null) {
cell1.setCellValue(record.getR35_tot_spec_risk_ch().doubleValue());} else {
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
	
	public byte[] getExcelM_SIRARCHIVAL(String filename,String reportId, String fromdate, String todate, String currency, String dtltype,String type,String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {
		}
		List<M_SIR_Archival_Summary_Entity> dataList =BRRS_M_SIR_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),version) ;
System.out.println("comming to archival ............."+ type);
System.out.println("excel archival......"+filename);
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SIR report. Returning empty result.");
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
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SIR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//----------
					
					//row11
					// Column C 
					row=sheet.getRow(11);
					Cell cell1=row.getCell(1);
					if(record.getR12_product() != null) {
						cell1.setCellValue(record.getR12_product());
						
					}else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
						
					}
					
					 cell1 = row.getCell(2);
					if (record.getR12_amt_6m() != null) {
						cell1.setCellValue(record.getR12_amt_6m().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//row11
					// Column E 
					 cell1 = row.getCell(4);
					if (record.getR12_capital_6m() != null) {
						cell1.setCellValue(record.getR12_capital_6m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//row11
					// Column F 
					 cell1 = row.getCell(5);
					if (record.getR12_amt_6to24m() != null) {
						cell1.setCellValue(record.getR12_amt_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
					//row11
					// Column H
					 cell1 = row.getCell(7);
					if (record.getR12_capital_6to24m() != null) {
						cell1.setCellValue(record.getR12_capital_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
					//row11
					// Column I 
					 cell1 = row.getCell(8);
					if (record.getR12_amt_gt24m() != null) {
						cell1.setCellValue(record.getR12_amt_gt24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}												
					//row11
					// Column K
					 cell1 = row.getCell(10);
					if (record.getR12_capital_gt24m() != null) {
						cell1.setCellValue(record.getR12_capital_gt24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}												
//-------------

					//row12
					// Column b
					row=sheet.getRow(12);
					cell1=row.getCell(1);
					if(record.getR13_product() != null) {
						cell1.setCellValue(record.getR13_product());
						
					}else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
						
					}
					//row12
					// Column C 
					
					 cell1 = row.getCell(2);
					if (record.getR13_amt_6m() != null) {
						cell1.setCellValue(record.getR13_amt_6m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column D
//					 cell1 = row.getCell(3);
//					if (record.getR13_risk_6m() != null) {
//						cell1.setCellValue(record.getR13_risk_6m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}
					
					// Column E 
					 cell1 = row.getCell(4);
					if (record.getR13_capital_6m() != null) {
						cell1.setCellValue(record.getR13_capital_6m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column F 
					 cell1 = row.getCell(5);
					if (record.getR13_amt_6to24m() != null) {
						cell1.setCellValue(record.getR13_amt_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//					 cell1 = row.getCell(6);
//					if (record.getR13_risk_6to24m() != null) {
//						cell1.setCellValue(record.getR13_risk_6to24m().doubleValue());
//					
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}					
				
					// Column H
					 cell1 = row.getCell(7);
					if (record.getR13_capital_6to24m() != null) {
						cell1.setCellValue(record.getR13_capital_6to24m().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
				
					// Column I 
					 cell1 = row.getCell(8);
					if (record.getR13_amt_gt24m() != null) {
						cell1.setCellValue(record.getR13_amt_gt24m().doubleValue());
				
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
				
																	
//					 cell1 = row.getCell(9);
//					if (record.getR13_risk_gt24m() != null) {
//						cell1.setCellValue(record.getR13_risk_gt24m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}												
				
					// Column K
					 cell1 = row.getCell(10);
					if (record.getR13_capital_gt24m() != null) {
						cell1.setCellValue(record.getR13_capital_gt24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}			
//-------------
					row=sheet.getRow(13);
					cell1=row.getCell(1);
					if(record.getR14_product() != null) {
						cell1.setCellValue(record.getR14_product());
					
					}else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
						
					}
					//row13
					// Column C 
				
					 cell1 = row.getCell(2);
					if (record.getR14_amt_6m() != null) {
						cell1.setCellValue(record.getR14_amt_6m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column D
//					 cell1 = row.getCell(3);
//					if (record.getR14_risk_6m() != null) {
//						cell1.setCellValue(record.getR14_risk_6m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}
					
					// Column E 
					 cell1 = row.getCell(4);
					if (record.getR14_capital_6m() != null) {
						cell1.setCellValue(record.getR14_capital_6m().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
				
					// Column F 
					 cell1 = row.getCell(5);
					if (record.getR14_amt_6to24m() != null) {
						cell1.setCellValue(record.getR14_amt_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
				
					// Column G 
//					 cell1 = row.getCell(6);
//					if (record.getR14_risk_6to24m() != null) {
//						cell1.setCellValue(record.getR14_risk_6to24m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}					
					
					// Column H
					 cell1 = row.getCell(7);
					if (record.getR14_capital_6to24m() != null) {
						cell1.setCellValue(record.getR14_capital_6to24m().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
				
					// Column I 
					 cell1 = row.getCell(8);
					if (record.getR14_amt_gt24m() != null) {
						cell1.setCellValue(record.getR14_amt_gt24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
				
					// Column J 
//					 cell1 = row.getCell(9);
//					if (record.getR14_risk_gt24m() != null) {
//						cell1.setCellValue(record.getR14_risk_gt24m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}											
					
					// Column K
					 cell1 = row.getCell(10);
					if (record.getR14_capital_gt24m() != null) {
						cell1.setCellValue(record.getR14_capital_gt24m().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}			
//---------------
					
					//row14
					// Column C 
					row=sheet.getRow(14);
					cell1=row.getCell(1);
					if(record.getR15_product() != null) {
						cell1.setCellValue(record.getR15_product());
						
					}else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
						
					}
				
					 cell1 = row.getCell(2);
					if (record.getR15_amt_6m() != null) {
						cell1.setCellValue(record.getR15_amt_6m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column D
//					 cell1 = row.getCell(3);
//					if (record.getR15_risk_6m() != null) {
//						cell1.setCellValue(record.getR15_risk_6m().doubleValue());
//					
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}
				
					// Column E 
					 cell1 = row.getCell(4);
					if (record.getR15_capital_6m() != null) {
						cell1.setCellValue(record.getR15_capital_6m().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column F 
					 cell1 = row.getCell(5);
					if (record.getR15_amt_6to24m() != null) {
						cell1.setCellValue(record.getR15_amt_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					// Column G 
//					 cell1 = row.getCell(6);
//					if (record.getR15_risk_6to24m() != null) {
//						cell1.setCellValue(record.getR15_risk_6to24m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}					
				
					// Column H
					 cell1 = row.getCell(7);
					if (record.getR15_capital_6to24m() != null) {
						cell1.setCellValue(record.getR15_capital_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
					
					// Column I 
					 cell1 = row.getCell(8);
					if (record.getR15_amt_gt24m() != null) {
						cell1.setCellValue(record.getR15_amt_gt24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
				
					// Column J 
//					 cell1 = row.getCell(9);
//					if (record.getR15_risk_gt24m() != null) {
//						cell1.setCellValue(record.getR15_risk_gt24m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}											
					
					// Column K
					 cell1 = row.getCell(10);
					if (record.getR15_capital_gt24m() != null) {
						cell1.setCellValue(record.getR15_capital_gt24m().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
															
//---------------
					row=sheet.getRow(15);
					cell1=row.getCell(1);
					if(record.getR16_product() != null) {
						cell1.setCellValue(record.getR16_product());
						
					}else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
						
					}
					//row15
					// Column C 
					
					 cell1 = row.getCell(2);
					if (record.getR16_amt_6m() != null) {
						cell1.setCellValue(record.getR16_amt_6m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column D
//					 cell1 = row.getCell(3);
//					if (record.getR16_risk_6m() != null) {
//						cell1.setCellValue(record.getR16_risk_6m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}
				
					// Column E 
					 cell1 = row.getCell(4);
					if (record.getR16_capital_6m() != null) {
						cell1.setCellValue(record.getR16_capital_6m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column F 
					 cell1 = row.getCell(5);
					if (record.getR16_amt_6to24m() != null) {
						cell1.setCellValue(record.getR16_amt_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					// Column G 
//					 cell1 = row.getCell(6);
//					if (record.getR16_risk_6to24m() != null) {
//						cell1.setCellValue(record.getR16_risk_6to24m().doubleValue());
//						
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}					
				
					// Column H
					 cell1 = row.getCell(7);
					if (record.getR16_capital_6to24m() != null) {
						cell1.setCellValue(record.getR16_capital_6to24m().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
					
					// Column I 
					 cell1 = row.getCell(8);
					if (record.getR16_amt_gt24m() != null) {
						cell1.setCellValue(record.getR16_amt_gt24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}					
				
					// Column J 
//					 cell1 = row.getCell(9);
//					if (record.getR16_risk_gt24m() != null) {
//						cell1.setCellValue(record.getR16_risk_gt24m().doubleValue());
//					} else {
//						cell1.setCellValue("");
//						cell1.setCellStyle(textStyle);
//					}											
					
					// Column K
					 cell1 = row.getCell(10);
					if (record.getR16_capital_gt24m() != null) {
						cell1.setCellValue(record.getR16_capital_gt24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
					
//---------------

//row16
// Column b 
					row=sheet.getRow(16);
					cell1=row.getCell(1);
					if(record.getR17_product() != null) {
						cell1.setCellValue(record.getR17_product());
					}else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
						
					}
//column c
 cell1 = row.getCell(2);
if (record.getR17_amt_6m() != null) {
	cell1.setCellValue(record.getR17_amt_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
// Column D
// cell1 = row.getCell(3);
//if (record.getR17_risk_6m() != null) {
//	cell1.setCellValue(record.getR17_risk_6m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}

// Column E 
 cell1 = row.getCell(4);
if (record.getR17_capital_6m() != null) {
	cell1.setCellValue(record.getR17_capital_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}

// Column F 
 cell1 = row.getCell(5);
if (record.getR17_amt_6to24m() != null) {
	cell1.setCellValue(record.getR17_amt_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}	

// Column G 
// cell1 = row.getCell(6);
//if (record.getR17_risk_6to24m() != null) {
//	cell1.setCellValue(record.getR17_risk_6to24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}					

// Column H
 cell1 = row.getCell(7);
if (record.getR17_capital_6to24m() != null) {
	cell1.setCellValue(record.getR17_capital_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

// Column I 
 cell1 = row.getCell(8);
if (record.getR17_amt_gt24m() != null) {
	cell1.setCellValue(record.getR17_amt_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

//Column J 
// cell1 = row.getCell(9);
//if (record.getR17_risk_gt24m() != null) {
//	cell1.setCellValue(record.getR17_risk_gt24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}											

// Column K
 cell1 = row.getCell(10);
if (record.getR17_capital_gt24m() != null) {
	cell1.setCellValue(record.getR17_capital_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}															
//----------

row=sheet.getRow(17);
cell1=row.getCell(1);
if(record.getR18_product() != null) {
	cell1.setCellValue(record.getR18_product());
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row17
// Column C 

cell1 = row.getCell(2);
if (record.getR18_amt_6m() != null) {
	cell1.setCellValue(record.getR18_amt_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
//row11
// Column E 
 cell1 = row.getCell(4);
if (record.getR18_capital_6m() != null) {
	cell1.setCellValue(record.getR18_capital_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
//row11
// Column F 
 cell1 = row.getCell(5);
if (record.getR18_amt_6to24m() != null) {
	cell1.setCellValue(record.getR18_amt_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					
//row11
// Column H
 cell1 = row.getCell(7);
if (record.getR18_capital_6to24m() != null) {
	cell1.setCellValue(record.getR18_capital_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					
//row11
// Column I 
 cell1 = row.getCell(8);
if (record.getR18_amt_gt24m() != null) {
	cell1.setCellValue(record.getR18_amt_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}												
//row11
// Column K
 cell1 = row.getCell(10);
if (record.getR18_capital_gt24m() != null) {
	cell1.setCellValue(record.getR18_capital_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}												
//---------------
row=sheet.getRow(18);
cell1=row.getCell(1);
if(record.getR19_product() != null) {
	cell1.setCellValue(record.getR19_product());
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row18
//Column C 
cell1 = row.getCell(2);
if (record.getR19_amt_6m() != null) {
	cell1.setCellValue(record.getR19_amt_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR19_risk_6m() != null) {
//	cell1.setCellValue(record.getR19_risk_6m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR19_capital_6m() != null) {
	cell1.setCellValue(record.getR19_capital_6m().doubleValue());
	
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR19_amt_6to24m() != null) {
	cell1.setCellValue(record.getR19_amt_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR19_risk_6to24m() != null) {
//	cell1.setCellValue(record.getR19_risk_6to24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR19_capital_6to24m() != null) {
	cell1.setCellValue(record.getR19_capital_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR19_amt_gt24m() != null) {
	cell1.setCellValue(record.getR19_amt_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR19_risk_gt24m() != null) {
//	cell1.setCellValue(record.getR19_risk_gt24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR19_capital_gt24m() != null) {
	cell1.setCellValue(record.getR19_capital_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}															
//----------
row=sheet.getRow(19);
cell1=row.getCell(1);
if(record.getR20_product() != null) {
	cell1.setCellValue(record.getR20_product());
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row19
//Column C 

cell1 = row.getCell(2);
if (record.getR20_amt_6m() != null) {
	cell1.setCellValue(record.getR20_amt_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR20_risk_6m() != null) {
//	cell1.setCellValue(record.getR20_risk_6m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR20_capital_6m() != null) {
	cell1.setCellValue(record.getR20_capital_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR20_amt_6to24m() != null) {
	cell1.setCellValue(record.getR20_amt_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR20_risk_6to24m() != null) {
//	cell1.setCellValue(record.getR20_risk_6to24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR20_capital_6to24m() != null) {
	cell1.setCellValue(record.getR20_capital_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR20_amt_gt24m() != null) {
	cell1.setCellValue(record.getR20_amt_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR20_risk_gt24m() != null) {
//	cell1.setCellValue(record.getR20_risk_gt24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR20_capital_gt24m() != null) {
	cell1.setCellValue(record.getR20_capital_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}															
//----------					
row=sheet.getRow(20);
cell1=row.getCell(1);
if(record.getR21_product() != null) {
	cell1.setCellValue(record.getR21_product());
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row20
//Column C 

cell1 = row.getCell(2);
if (record.getR21_amt_6m() != null) {
	cell1.setCellValue(record.getR21_amt_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR21_risk_6m() != null) {
//	cell1.setCellValue(record.getR21_risk_6m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR21_capital_6m() != null) {
	cell1.setCellValue(record.getR21_capital_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR21_amt_6to24m() != null) {
	cell1.setCellValue(record.getR21_amt_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR21_risk_6to24m() != null) {
//	cell1.setCellValue(record.getR21_risk_6to24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR21_capital_6to24m() != null) {
	cell1.setCellValue(record.getR21_capital_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR21_amt_gt24m() != null) {
	cell1.setCellValue(record.getR21_amt_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR21_risk_gt24m() != null) {
//	cell1.setCellValue(record.getR21_risk_gt24m().doubleValue());
//} else {
//	cell1.setCellValue("");
//	cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR21_capital_gt24m() != null) {
	cell1.setCellValue(record.getR21_capital_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}															
//----------					
					

//----------					
row=sheet.getRow(21);
cell1=row.getCell(1);
if(record.getR22_product() != null) {
	cell1.setCellValue(record.getR22_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row21
//Column C 

cell1 = row.getCell(2);
if (record.getR22_amt_6m() != null) {
cell1.setCellValue(record.getR22_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR22_risk_6m() != null) {
//cell1.setCellValue(record.getR22_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR22_capital_6m() != null) {
cell1.setCellValue(record.getR22_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR22_amt_6to24m() != null) {
cell1.setCellValue(record.getR22_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR22_risk_6to24m() != null) {
//cell1.setCellValue(record.getR22_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR22_capital_6to24m() != null) {
cell1.setCellValue(record.getR22_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR22_amt_gt24m() != null) {
cell1.setCellValue(record.getR22_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR22_risk_gt24m() != null) {
//cell1.setCellValue(record.getR22_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR22_capital_gt24m() != null) {
cell1.setCellValue(record.getR22_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------					

//----------					
row=sheet.getRow(22);
cell1=row.getCell(1);
if(record.getR23_product() != null) {
	cell1.setCellValue(record.getR23_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row22
//Column C 

cell1 = row.getCell(2);
if (record.getR23_amt_6m() != null) {
cell1.setCellValue(record.getR23_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR23_risk_6m() != null) {
//cell1.setCellValue(record.getR23_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR23_capital_6m() != null) {
cell1.setCellValue(record.getR23_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR23_amt_6to24m() != null) {
cell1.setCellValue(record.getR23_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR23_risk_6to24m() != null) {
//cell1.setCellValue(record.getR23_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR23_capital_6to24m() != null) {
cell1.setCellValue(record.getR23_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR23_amt_gt24m() != null) {
cell1.setCellValue(record.getR23_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR23_risk_gt24m() != null) {
//cell1.setCellValue(record.getR23_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR23_capital_gt24m() != null) {
cell1.setCellValue(record.getR23_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------					

row=sheet.getRow(23);
cell1=row.getCell(1);
if(record.getR24_product() != null) {
	cell1.setCellValue(record.getR24_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row23
//Column C 
cell1 = row.getCell(2);
if (record.getR24_amt_6m() != null) {
cell1.setCellValue(record.getR24_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR24_risk_6m() != null) {
//cell1.setCellValue(record.getR24_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR24_capital_6m() != null) {
cell1.setCellValue(record.getR24_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR24_amt_6to24m() != null) {
cell1.setCellValue(record.getR24_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR24_risk_6to24m() != null) {
//cell1.setCellValue(record.getR24_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR24_capital_6to24m() != null) {
cell1.setCellValue(record.getR24_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR24_amt_gt24m() != null) {
cell1.setCellValue(record.getR24_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR24_risk_gt24m() != null) {
//cell1.setCellValue(record.getR24_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR24_capital_gt24m() != null) {
cell1.setCellValue(record.getR24_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------					
					
//----------					

row=sheet.getRow(24);
cell1=row.getCell(1);
if(record.getR25_product() != null) {
	cell1.setCellValue(record.getR25_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row24
//Column C 
cell1 = row.getCell(2);
if (record.getR25_amt_6m() != null) {
cell1.setCellValue(record.getR25_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR25_risk_6m() != null) {
//cell1.setCellValue(record.getR25_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR25_capital_6m() != null) {
cell1.setCellValue(record.getR25_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR25_amt_6to24m() != null) {
cell1.setCellValue(record.getR25_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR25_risk_6to24m() != null) {
//cell1.setCellValue(record.getR25_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR25_capital_6to24m() != null) {
cell1.setCellValue(record.getR25_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR25_amt_gt24m() != null) {
cell1.setCellValue(record.getR25_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR25_risk_gt24m() != null) {
//cell1.setCellValue(record.getR25_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR25_capital_gt24m() != null) {
cell1.setCellValue(record.getR25_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------					
					
								

//----------					


row=sheet.getRow(25);
cell1=row.getCell(1);
if(record.getR26_product() != null) {
	cell1.setCellValue(record.getR26_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row25
//Column C 
cell1 = row.getCell(2);
if (record.getR26_amt_6m() != null) {
cell1.setCellValue(record.getR26_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR26_risk_6m() != null) {
//cell1.setCellValue(record.getR26_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR26_capital_6m() != null) {
cell1.setCellValue(record.getR26_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR26_amt_6to24m() != null) {
cell1.setCellValue(record.getR26_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR26_risk_6to24m() != null) {
//cell1.setCellValue(record.getR26_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR26_capital_6to24m() != null) {
cell1.setCellValue(record.getR26_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR26_amt_gt24m() != null) {
cell1.setCellValue(record.getR26_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR26_risk_gt24m() != null) {
//cell1.setCellValue(record.getR26_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR26_capital_gt24m() != null) {
cell1.setCellValue(record.getR26_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------					

row=sheet.getRow(26);
cell1=row.getCell(1);
if(record.getR27_product() != null) {
	cell1.setCellValue(record.getR27_product());
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row26
// Column C 
 cell1 = row.getCell(2);
if (record.getR27_amt_6m() != null) {
	cell1.setCellValue(record.getR27_amt_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
//row11
// Column E 
 cell1 = row.getCell(4);
if (record.getR27_capital_6m() != null) {
	cell1.setCellValue(record.getR27_capital_6m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}
//row11
// Column F 
 cell1 = row.getCell(5);
if (record.getR27_amt_6to24m() != null) {
	cell1.setCellValue(record.getR27_amt_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					
//row11
// Column H
 cell1 = row.getCell(7);
if (record.getR27_capital_6to24m() != null) {
	cell1.setCellValue(record.getR27_capital_6to24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}					
//row11
// Column I 
 cell1 = row.getCell(8);
if (record.getR27_amt_gt24m() != null) {
	cell1.setCellValue(record.getR27_amt_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}												
//row11
// Column K
 cell1 = row.getCell(10);
if (record.getR27_capital_gt24m() != null) {
	cell1.setCellValue(record.getR27_capital_gt24m().doubleValue());
} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
}												
//-------------
//----------					


row=sheet.getRow(27);
cell1=row.getCell(1);
if(record.getR28_product() != null) {
	cell1.setCellValue(record.getR28_product());
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row27
//Column C 
cell1 = row.getCell(2);
if (record.getR28_amt_6m() != null) {
cell1.setCellValue(record.getR28_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR28_risk_6m() != null) {
//cell1.setCellValue(record.getR28_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR28_capital_6m() != null) {
cell1.setCellValue(record.getR28_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR28_amt_6to24m() != null) {
cell1.setCellValue(record.getR28_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR28_risk_6to24m() != null) {
//cell1.setCellValue(record.getR28_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR28_capital_6to24m() != null) {
cell1.setCellValue(record.getR28_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR28_amt_gt24m() != null) {
cell1.setCellValue(record.getR28_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR28_risk_gt24m() != null) {
//cell1.setCellValue(record.getR28_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR28_capital_gt24m() != null) {
cell1.setCellValue(record.getR28_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------		

//----------					


row=sheet.getRow(28);
cell1=row.getCell(1);
if(record.getR29_product() != null) {
	cell1.setCellValue(record.getR29_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row28
//Column C 
cell1 = row.getCell(2);
if (record.getR29_amt_6m() != null) {
cell1.setCellValue(record.getR29_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR29_risk_6m() != null) {
//cell1.setCellValue(record.getR29_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR29_capital_6m() != null) {
cell1.setCellValue(record.getR29_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR29_amt_6to24m() != null) {
cell1.setCellValue(record.getR29_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR29_risk_6to24m() != null) {
//cell1.setCellValue(record.getR29_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR29_capital_6to24m() != null) {
cell1.setCellValue(record.getR29_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR29_amt_gt24m() != null) {
cell1.setCellValue(record.getR29_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR29_risk_gt24m() != null) {
//cell1.setCellValue(record.getR29_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR29_capital_gt24m() != null) {
cell1.setCellValue(record.getR29_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------		


//----------					


row=sheet.getRow(30);
cell1=row.getCell(1);
if(record.getR31_product() != null) {
	cell1.setCellValue(record.getR31_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row29
//Column C 
row=sheet.getRow(29);
cell1 = row.getCell(2);
if (record.getR30_amt_6m() != null) {
cell1.setCellValue(record.getR30_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR30_risk_6m() != null) {
//cell1.setCellValue(record.getR30_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR30_capital_6m() != null) {
cell1.setCellValue(record.getR30_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR30_amt_6to24m() != null) {
cell1.setCellValue(record.getR30_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR30_risk_6to24m() != null) {
//cell1.setCellValue(record.getR30_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR30_capital_6to24m() != null) {
cell1.setCellValue(record.getR30_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR30_amt_gt24m() != null) {
cell1.setCellValue(record.getR30_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR30_risk_gt24m() != null) {
//cell1.setCellValue(record.getR30_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR30_capital_gt24m() != null) {
cell1.setCellValue(record.getR30_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------		
	
			


//----------					

row=sheet.getRow(30);
cell1=row.getCell(1);
if(record.getR31_product() != null) {
	cell1.setCellValue(record.getR31_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}

//row30
//Column C 
row=sheet.getRow(30);
cell1 = row.getCell(2);
if (record.getR31_amt_6m() != null) {
cell1.setCellValue(record.getR31_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR31_risk_6m() != null) {
//cell1.setCellValue(record.getR31_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR31_capital_6m() != null) {
cell1.setCellValue(record.getR31_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR31_amt_6to24m() != null) {
cell1.setCellValue(record.getR31_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR31_risk_6to24m() != null) {
//cell1.setCellValue(record.getR31_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR31_capital_6to24m() != null) {
cell1.setCellValue(record.getR31_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR31_amt_gt24m() != null) {
cell1.setCellValue(record.getR31_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR31_risk_gt24m() != null) {
//cell1.setCellValue(record.getR31_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR31_capital_gt24m() != null) {
cell1.setCellValue(record.getR31_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------		
	


//----------					


row=sheet.getRow(31);
cell1=row.getCell(1);
if(record.getR32_product() != null) {
	cell1.setCellValue(record.getR32_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}
//row31
//Column C 
row=sheet.getRow(31);
cell1 = row.getCell(2);
if (record.getR32_amt_6m() != null) {
cell1.setCellValue(record.getR32_amt_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
//Column D
//cell1 = row.getCell(3);
//if (record.getR32_risk_6m() != null) {
//cell1.setCellValue(record.getR32_risk_6m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}

//Column E 
cell1 = row.getCell(4);
if (record.getR32_capital_6m() != null) {
cell1.setCellValue(record.getR32_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//Column F 
cell1 = row.getCell(5);
if (record.getR32_amt_6to24m() != null) {
cell1.setCellValue(record.getR32_amt_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	

//Column G 
//cell1 = row.getCell(6);
//if (record.getR32_risk_6to24m() != null) {
//cell1.setCellValue(record.getR32_risk_6to24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}					

//Column H
cell1 = row.getCell(7);
if (record.getR32_capital_6to24m() != null) {
cell1.setCellValue(record.getR32_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column I 
cell1 = row.getCell(8);
if (record.getR32_amt_gt24m() != null) {
cell1.setCellValue(record.getR32_amt_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}					

//Column J 
//cell1 = row.getCell(9);
//if (record.getR32_risk_gt24m() != null) {
//cell1.setCellValue(record.getR32_risk_gt24m().doubleValue());} else {
//cell1.setCellValue("");
//cell1.setCellStyle(textStyle);
//}											

//Column K
cell1 = row.getCell(10);
if (record.getR32_capital_gt24m() != null) {
cell1.setCellValue(record.getR32_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}															
//----------						

row=sheet.getRow(32);
cell1=row.getCell(1);
if(record.getR33_product() != null) {
	cell1.setCellValue(record.getR33_product());
	cell1.setCellStyle(numberStyle);
}else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	
}			

//row31
//Column E
cell1 = row.getCell(4);
if (record.getR33_capital_6m() != null) {
cell1.setCellValue(record.getR33_capital_6m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
					

//row31
//Column H
row=sheet.getRow(32);
cell1 = row.getCell(7);
if (record.getR33_capital_6to24m() != null) {
cell1.setCellValue(record.getR33_capital_6to24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//row31
//Column K
row=sheet.getRow(32);
cell1 = row.getCell(10);
if (record.getR33_capital_gt24m() != null) {
cell1.setCellValue(record.getR33_capital_gt24m().doubleValue());} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
								

//row34
//Column K
row=sheet.getRow(34);
cell1 = row.getCell(4);
if (record.getR35_tot_spec_risk_ch() != null) {
cell1.setCellValue(record.getR35_tot_spec_risk_ch().doubleValue());} else {
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


