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


import com.bornfire.brrs.entities.M_CA3_Summary_Entity;
import com.bornfire.brrs.entities.M_LA1_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LA1_Summary_Entity;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Summary_Entity2;
import com.bornfire.brrs.entities.BRRS_M_CA3_Summary_Repo;
import com.bornfire.brrs.entities.M_CA3_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_CA3_Archival_Summary_Repo;



@Component
@Service

public class BRRS_M_CA3_ReportService {
private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA3_ReportService.class);
	


	@Autowired
	private Environment env;
	
	
	@Autowired
	SessionFactory sessionFactory;
	
	
	
	@Autowired
	BRRS_M_CA3_Summary_Repo BRRS_M_CA3_Summary_Repo;
	
	@Autowired
	BRRS_M_CA3_Archival_Summary_Repo BRRS_M_CA3_Archival_Summary_Repo;
   

	
	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	
	
	public ModelAndView getM_CA3View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_CA3_Archival_Summary_Entity> T1Master = new ArrayList<M_CA3_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_CA3_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);

		} else {

			List<M_CA3_Summary_Entity> T1Master = new ArrayList<M_CA3_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_CA3_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_CA3");

		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;

	}
	
	public void updateReport(M_CA3_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_CA3_Summary_Entity existing = BRRS_M_CA3_Summary_Repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 10; i <= 19; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "AMOUNT" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R20 totals
	        String[] totalFields = { "AMOUNT"};
	        for (String field : totalFields) {
	            String getterName = "getR20_" + field;
	            String setterName = "setR20_" + field;

	            try {
	                Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);
	                Method setter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                Object newValue = getter.invoke(updatedEntity);
	                setter.invoke(existing, newValue);

	            } catch (NoSuchMethodException e) {
	                // Skip if not present
	                continue;
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	    
	    // 3️⃣ Save updated entity
	    BRRS_M_CA3_Summary_Repo.save(existing);
	}	
	
	public void updateReport2(M_CA3_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_CA3_Summary_Entity existing = BRRS_M_CA3_Summary_Repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 24; i <= 27; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "AMOUNT" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R51 totals
	        String[] totalFields = { "AMOUNT"};
	        for (String field : totalFields) {
	            String getterName = "getR28_" + field;
	            String setterName = "setR28_" + field;
	            String getterName1 = "getR29_" + field;
	            String setterName1 = "setR29_" + field;

	            try {
	                Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);
	                Method setter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                Method getter1 = M_CA3_Summary_Entity.class.getMethod(getterName1);
	                Method setter1 = M_CA3_Summary_Entity.class.getMethod(setterName1, getter.getReturnType());

	                Object newValue = getter.invoke(updatedEntity);
	                setter.invoke(existing, newValue);

	            } catch (NoSuchMethodException e) {
	                // Skip if not present
	                continue;
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	    
	    // 3️⃣ Save updated entity
	    BRRS_M_CA3_Summary_Repo.save(existing);
	}	
	
	
	public void updateReport3(M_CA3_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_CA3_Summary_Entity existing = BRRS_M_CA3_Summary_Repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 36; i <= 40; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "AMOUNT" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R41 totals
	        String[] totalFields = { "AMOUNT"};
	        for (String field : totalFields) {
	            String getterName = "getR41_" + field;
	            String setterName = "setR41_" + field;

	            try {
	                Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);
	                Method setter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                Object newValue = getter.invoke(updatedEntity);
	                setter.invoke(existing, newValue);

	            } catch (NoSuchMethodException e) {
	                // Skip if not present
	                continue;
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	    
	    // 3️⃣ Save updated entity
	    BRRS_M_CA3_Summary_Repo.save(existing);
	}	
	
	
	public void updateReport4(M_CA3_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_CA3_Summary_Entity existing = BRRS_M_CA3_Summary_Repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // ✅ Copy R44 and R45 amounts only
	        existing.setR44_AMOUNT(updatedEntity.getR44_AMOUNT());
	        existing.setR45_AMOUNT(updatedEntity.getR45_AMOUNT());
	        existing.setR46_AMOUNT(updatedEntity.getR45_AMOUNT());

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // ✅ Save updated entity
	    BRRS_M_CA3_Summary_Repo.save(existing);
	}
	
	public void updateReport5(M_CA3_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_CA3_Summary_Entity existing = BRRS_M_CA3_Summary_Repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // ✅ Copy R44 and R45 amounts only
	        existing.setR50_AMOUNT(updatedEntity.getR50_AMOUNT());
	        existing.setR51_AMOUNT(updatedEntity.getR51_AMOUNT());
	        existing.setR52_AMOUNT(updatedEntity.getR52_AMOUNT());
	        existing.setR53_AMOUNT(updatedEntity.getR53_AMOUNT());
	        existing.setR54_AMOUNT(updatedEntity.getR54_AMOUNT());
	        existing.setR55_AMOUNT(updatedEntity.getR55_AMOUNT());

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // ✅ Save updated entity
	    BRRS_M_CA3_Summary_Repo.save(existing);
	}
	
	public void updateReport6(M_CA3_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_CA3_Summary_Entity existing = BRRS_M_CA3_Summary_Repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // ✅ Copy R44 and R45 amounts only
	        existing.setR58_AMOUNT(updatedEntity.getR58_AMOUNT());
	        existing.setR59_AMOUNT(updatedEntity.getR59_AMOUNT());
	        existing.setR60_AMOUNT(updatedEntity.getR60_AMOUNT());
	        

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // ✅ Save updated entity
	    BRRS_M_CA3_Summary_Repo.save(existing);
	}
	

	
	

	
public byte[] BRRS_M_CA3Excel(String filename,String reportId, String fromdate, String todate, String currency, String dtltype, String type,String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		
		if (type.equals("ARCHIVAL") & version != null) {
			byte[] ARCHIVALreport = getExcelM_CA3ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
			return ARCHIVALreport;
		}
		List<M_CA3_Summary_Entity> dataList =BRRS_M_CA3_Summary_Repo.getdatabydateList(dateformat.parse(todate)) ;

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRF2.4 report. Returning empty result.");
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
					M_CA3_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

		
					//row10
					// Column b
					Cell cell1 = row.createCell(1);
					if (record.getR10_PRODUCT() != null) {
						cell1.setCellValue(record.getR10_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//column c
					Cell cell2 = row.createCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					
									
					
					//row11
					row = sheet.getRow(10);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR11_PRODUCT() != null) {
						cell1.setCellValue(record.getR11_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row12
					row = sheet.getRow(11);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR12_PRODUCT() != null) {
						cell1.setCellValue(record.getR12_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row13
					row = sheet.getRow(12);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR13_PRODUCT() != null) {
						cell1.setCellValue(record.getR13_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					

					//row14
					row = sheet.getRow(13);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR14_PRODUCT() != null) {
						cell1.setCellValue(record.getR14_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR14_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					

					//row15
					row = sheet.getRow(14);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR15_PRODUCT() != null) {
						cell1.setCellValue(record.getR15_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row16
					row = sheet.getRow(15);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR16_PRODUCT() != null) {
						cell1.setCellValue(record.getR16_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR16_AMOUNT() != null) {
						cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row17
					row = sheet.getRow(16);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR17_PRODUCT() != null) {
						cell1.setCellValue(record.getR17_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row18
					row = sheet.getRow(17);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR18_PRODUCT() != null) {
						cell1.setCellValue(record.getR18_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row19
					row = sheet.getRow(18);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR19_PRODUCT() != null) {
						cell1.setCellValue(record.getR19_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row20
					row = sheet.getRow(19);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR20_PRODUCT() != null) {
						cell1.setCellValue(record.getR20_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell2.setCellValue(record.getR20_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
											}

					//row24
					row = sheet.getRow(23);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR24_PRODUCT() != null) {
						cell1.setCellValue(record.getR24_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR24_AMOUNT() != null) {
						cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row25
					row = sheet.getRow(24);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR25_PRODUCT() != null) {
						cell1.setCellValue(record.getR25_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR25_AMOUNT() != null) {
						cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row26
					row = sheet.getRow(25);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR26_PRODUCT() != null) {
						cell1.setCellValue(record.getR26_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR26_AMOUNT() != null) {
						cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row27
					row = sheet.getRow(26);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR27_PRODUCT() != null) {
						cell1.setCellValue(record.getR27_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR27_AMOUNT() != null) {
						cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row28
					row = sheet.getRow(27);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR28_PRODUCT() != null) {
						cell1.setCellValue(record.getR28_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR28_AMOUNT() != null) {
						cell2.setCellValue(record.getR28_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
											}

					//row29
					row = sheet.getRow(28);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR29_PRODUCT() != null) {
						cell1.setCellValue(record.getR29_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR29_AMOUNT() != null) {
						cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row36
					row = sheet.getRow(35);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR36_PRODUCT() != null) {
						cell1.setCellValue(record.getR36_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR36_AMOUNT() != null) {
						cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//row37
					row = sheet.getRow(36);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR37_PRODUCT() != null) {
						cell1.setCellValue(record.getR37_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR37_AMOUNT() != null) {
						cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row38
					row = sheet.getRow(37);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR38_PRODUCT() != null) {
						cell1.setCellValue(record.getR38_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR38_AMOUNT() != null) {
						cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row39
					row = sheet.getRow(38);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR39_PRODUCT() != null) {
						cell1.setCellValue(record.getR39_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR39_AMOUNT() != null) {
						cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row40
					row = sheet.getRow(39);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR40_PRODUCT() != null) {
						cell1.setCellValue(record.getR40_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR40_AMOUNT() != null) {
						cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row41
					row = sheet.getRow(40);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR41_PRODUCT() != null) {
						cell1.setCellValue(record.getR41_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR41_AMOUNT() != null) {
						cell2.setCellValue(record.getR41_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row44
					row = sheet.getRow(43);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR44_PRODUCT() != null) {
						cell1.setCellValue(record.getR44_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR44_AMOUNT() != null) {
						cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row45
					row = sheet.getRow(44);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR45_PRODUCT() != null) {
						cell1.setCellValue(record.getR45_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR45_AMOUNT() != null) {
						cell2.setCellValue(record.getR45_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row46
					row = sheet.getRow(45);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR46_PRODUCT() != null) {
						cell1.setCellValue(record.getR46_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR46_AMOUNT() != null) {
						cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
			
					} else {
						cell2.setCellValue("");

					}

					//row50
					row = sheet.getRow(49);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR50_PRODUCT() != null) {
						cell1.setCellValue(record.getR50_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR50_AMOUNT() != null) {
						cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row51
					row = sheet.getRow(50);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR51_PRODUCT() != null) {
						cell1.setCellValue(record.getR51_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR51_AMOUNT() != null) {
						cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row52
					row = sheet.getRow(51);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR52_PRODUCT() != null) {
						cell1.setCellValue(record.getR52_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR52_AMOUNT() != null) {
						cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row53
					row = sheet.getRow(52);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR53_PRODUCT() != null) {
						cell1.setCellValue(record.getR53_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR53_AMOUNT() != null) {
						cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row54
					row = sheet.getRow(53);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR54_PRODUCT() != null) {
						cell1.setCellValue(record.getR54_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR54_AMOUNT() != null) {
						cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row55
					row = sheet.getRow(54);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR55_PRODUCT() != null) {
						cell1.setCellValue(record.getR55_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR55_AMOUNT() != null) {
						cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

				

					//row58
					row = sheet.getRow(57);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR58_PRODUCT() != null) {
						cell1.setCellValue(record.getR58_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR58_AMOUNT() != null) {
						cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row59
					row = sheet.getRow(58);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR59_PRODUCT() != null) {
						cell1.setCellValue(record.getR59_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR59_AMOUNT() != null) {
						cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row60
					row = sheet.getRow(14);			
					// Column b
					 cell1 = row.createCell(1);
					if (record.getR15_PRODUCT() != null) {
						cell1.setCellValue(record.getR15_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

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

public List<Object> getM_CA3Archival() {
	List<Object> M_CA3Archivallist = new ArrayList<>();
	try {
		M_CA3Archivallist = BRRS_M_CA3_Archival_Summary_Repo.getM_CA3archival();
		System.out.println("countser" + M_CA3Archivallist.size());
	} catch (Exception e) {
		// Log the exception
		System.err.println("Error fetching M_LA1 Archival data: " + e.getMessage());
		e.printStackTrace();

		// Optionally, you can rethrow it or return empty list
		// throw new RuntimeException("Failed to fetch data", e);
	}
	return M_CA3Archivallist;
}

public byte[] getExcelM_CA3ARCHIVAL(String filename, String reportId, String fromdate, String todate,
		String currency, String dtltype, String type, String version) throws Exception {
	logger.info("Service: Starting Excel generation process in memory.");
	if (type.equals("ARCHIVAL") & version != null) {

	}
	List<M_CA3_Archival_Summary_Entity> dataList = BRRS_M_CA3_Archival_Summary_Repo
			.getdatabydateListarchival(dateformat.parse(todate), version);

	if (dataList.isEmpty()) {
		logger.warn("Service: No data found for M_CA3 report. Returning empty result.");
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

//Create the font
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 8); // size 8
		font.setFontName("Arial");

		CellStyle numberStyle = workbook.createCellStyle();
//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
		numberStyle.setBorderBottom(BorderStyle.THIN);
		numberStyle.setBorderTop(BorderStyle.THIN);
		numberStyle.setBorderLeft(BorderStyle.THIN);
		numberStyle.setBorderRight(BorderStyle.THIN);
		numberStyle.setFont(font);

		CellStyle percentStyle = workbook.createCellStyle();
		percentStyle.cloneStyleFrom(numberStyle);
		percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
		percentStyle.setAlignment(HorizontalAlignment.RIGHT);
		// --- End of Style Definitions ---
		int startRow = 9;

		if (!dataList.isEmpty()) {
			for (int i = 0; i < dataList.size(); i++) {
				M_CA3_Archival_Summary_Entity record = dataList.get(i);
				System.out.println("rownumber=" + startRow + i);
				Row row = sheet.getRow(startRow + i);
				if (row == null) {
					row = sheet.createRow(startRow + i);
				}
				
				//row10
				// Column b
				Cell cell1 = row.createCell(1);
				if (record.getR10_PRODUCT() != null) {
					cell1.setCellValue(record.getR10_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				//column c
				Cell cell2 = row.createCell(2);
				if (record.getR10_AMOUNT() != null) {
					cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}
				
				
								
				
				//row11
				row = sheet.getRow(10);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR11_PRODUCT() != null) {
					cell1.setCellValue(record.getR11_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR11_AMOUNT() != null) {
					cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}
				
				//row12
				row = sheet.getRow(11);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR12_PRODUCT() != null) {
					cell1.setCellValue(record.getR12_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR12_AMOUNT() != null) {
					cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}
				
				//row13
				row = sheet.getRow(12);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR13_PRODUCT() != null) {
					cell1.setCellValue(record.getR13_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				// Column c
				 cell2 = row.createCell(2);
				if (record.getR13_AMOUNT() != null) {
					cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}
				

				//row14
				row = sheet.getRow(13);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR14_PRODUCT() != null) {
					cell1.setCellValue(record.getR14_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR14_AMOUNT() != null) {
					cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}
				

				//row15
				row = sheet.getRow(14);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR15_PRODUCT() != null) {
					cell1.setCellValue(record.getR15_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR15_AMOUNT() != null) {
					cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row16
				row = sheet.getRow(15);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR16_PRODUCT() != null) {
					cell1.setCellValue(record.getR16_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR16_AMOUNT() != null) {
					cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row17
				row = sheet.getRow(16);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR17_PRODUCT() != null) {
					cell1.setCellValue(record.getR17_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR17_AMOUNT() != null) {
					cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row18
				row = sheet.getRow(17);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR18_PRODUCT() != null) {
					cell1.setCellValue(record.getR18_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR18_AMOUNT() != null) {
					cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row19
				row = sheet.getRow(18);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR19_PRODUCT() != null) {
					cell1.setCellValue(record.getR19_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR19_AMOUNT() != null) {
					cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row20
				row = sheet.getRow(19);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR20_PRODUCT() != null) {
					cell1.setCellValue(record.getR20_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.getCell(2);
				if (record.getR20_AMOUNT() != null) {
					cell2.setCellValue(record.getR20_AMOUNT().doubleValue());
					
				} else {
					cell2.setCellValue("");
										}

				//row24
				row = sheet.getRow(23);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR24_PRODUCT() != null) {
					cell1.setCellValue(record.getR24_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR24_AMOUNT() != null) {
					cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row25
				row = sheet.getRow(24);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR25_PRODUCT() != null) {
					cell1.setCellValue(record.getR25_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR25_AMOUNT() != null) {
					cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row26
				row = sheet.getRow(25);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR26_PRODUCT() != null) {
					cell1.setCellValue(record.getR26_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR26_AMOUNT() != null) {
					cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row27
				row = sheet.getRow(26);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR27_PRODUCT() != null) {
					cell1.setCellValue(record.getR27_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR27_AMOUNT() != null) {
					cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row28
				row = sheet.getRow(27);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR28_PRODUCT() != null) {
					cell1.setCellValue(record.getR28_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.getCell(2);
				if (record.getR28_AMOUNT() != null) {
					cell2.setCellValue(record.getR28_AMOUNT().doubleValue());
					
				} else {
					cell2.setCellValue("");
										}

				//row29
				row = sheet.getRow(28);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR29_PRODUCT() != null) {
					cell1.setCellValue(record.getR29_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.getCell(2);
				if (record.getR29_AMOUNT() != null) {
					cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

				} else {
					cell2.setCellValue("");

				}

				//row36
				row = sheet.getRow(35);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR36_PRODUCT() != null) {
					cell1.setCellValue(record.getR36_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR36_AMOUNT() != null) {
					cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}
				//row37
				row = sheet.getRow(36);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR37_PRODUCT() != null) {
					cell1.setCellValue(record.getR37_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR37_AMOUNT() != null) {
					cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row38
				row = sheet.getRow(37);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR38_PRODUCT() != null) {
					cell1.setCellValue(record.getR38_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR38_AMOUNT() != null) {
					cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row39
				row = sheet.getRow(38);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR39_PRODUCT() != null) {
					cell1.setCellValue(record.getR39_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR39_AMOUNT() != null) {
					cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row40
				row = sheet.getRow(39);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR40_PRODUCT() != null) {
					cell1.setCellValue(record.getR40_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR40_AMOUNT() != null) {
					cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row41
				row = sheet.getRow(40);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR41_PRODUCT() != null) {
					cell1.setCellValue(record.getR41_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.getCell(2);
				if (record.getR41_AMOUNT() != null) {
					cell2.setCellValue(record.getR41_AMOUNT().doubleValue());

				} else {
					cell2.setCellValue("");

				}

				//row44
				row = sheet.getRow(43);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR44_PRODUCT() != null) {
					cell1.setCellValue(record.getR44_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR44_AMOUNT() != null) {
					cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row45
				row = sheet.getRow(44);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR45_PRODUCT() != null) {
					cell1.setCellValue(record.getR45_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.getCell(2);
				if (record.getR45_AMOUNT() != null) {
					cell2.setCellValue(record.getR45_AMOUNT().doubleValue());

				} else {
					cell2.setCellValue("");

				}

				//row46
				row = sheet.getRow(45);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR46_PRODUCT() != null) {
					cell1.setCellValue(record.getR46_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.getCell(2);
				if (record.getR46_AMOUNT() != null) {
					cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
		
				} else {
					cell2.setCellValue("");

				}

				//row50
				row = sheet.getRow(49);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR50_PRODUCT() != null) {
					cell1.setCellValue(record.getR50_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR50_AMOUNT() != null) {
					cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row51
				row = sheet.getRow(50);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR51_PRODUCT() != null) {
					cell1.setCellValue(record.getR51_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR51_AMOUNT() != null) {
					cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row52
				row = sheet.getRow(51);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR52_PRODUCT() != null) {
					cell1.setCellValue(record.getR52_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR52_AMOUNT() != null) {
					cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row53
				row = sheet.getRow(52);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR53_PRODUCT() != null) {
					cell1.setCellValue(record.getR53_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR53_AMOUNT() != null) {
					cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row54
				row = sheet.getRow(53);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR54_PRODUCT() != null) {
					cell1.setCellValue(record.getR54_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR54_AMOUNT() != null) {
					cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row55
				row = sheet.getRow(54);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR55_PRODUCT() != null) {
					cell1.setCellValue(record.getR55_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.getCell(2);
				if (record.getR55_AMOUNT() != null) {
					cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

				} else {
					cell2.setCellValue("");

				}

			

				//row58
				row = sheet.getRow(57);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR58_PRODUCT() != null) {
					cell1.setCellValue(record.getR58_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.createCell(2);
				if (record.getR58_AMOUNT() != null) {
					cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
					cell2.setCellStyle(numberStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(textStyle);
				}

				//row59
				row = sheet.getRow(58);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR59_PRODUCT() != null) {
					cell1.setCellValue(record.getR59_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.getCell(2);
				if (record.getR59_AMOUNT() != null) {
					cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

				} else {
					cell2.setCellValue("");

				}

				//row60
				row = sheet.getRow(14);			
				// Column b
				 cell1 = row.createCell(1);
				if (record.getR15_PRODUCT() != null) {
					cell1.setCellValue(record.getR15_PRODUCT());
					cell1.setCellStyle(numberStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}

				// Column c
				 cell2 = row.getCell(2);
				if (record.getR15_AMOUNT() != null) {
					cell2.setCellValue(record.getR15_AMOUNT().doubleValue());

				} else {
					cell2.setCellValue("");

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

