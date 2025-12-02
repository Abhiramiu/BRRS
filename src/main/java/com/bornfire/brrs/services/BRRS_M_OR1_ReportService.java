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

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_OR1_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_OR1_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_OR1_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_OR1_Summary_Repo;
import com.bornfire.brrs.entities.M_DEP1_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_DEP1_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_DEP1_Detail_Entity;
import com.bornfire.brrs.entities.M_DEP1_Summary_Entity;
import com.bornfire.brrs.entities.M_DEP3_Manual_Summary_Entity;
import com.bornfire.brrs.entities.M_OR1_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_OR1_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_OR1_Detail_Entity;
import com.bornfire.brrs.entities.M_OR1_Summary_Entity;
import com.bornfire.brrs.entities.M_OR2_Summary_Entity;

import java.math.BigDecimal;

@Component
@Service
public class BRRS_M_OR1_ReportService {
private static final Logger logger = LoggerFactory.getLogger(BRRS_M_OR1_ReportService.class);
	


	@Autowired
	private Environment env;
	
	
	@Autowired
	SessionFactory sessionFactory;
	

	@Autowired
	BRRS_M_OR1_Detail_Repo M_OR1_Detail_Repo;
	
	@Autowired
	BRRS_M_OR1_Summary_Repo M_OR1_Summary_Repo;
	
	@Autowired
	BRRS_M_OR1_Archival_Detail_Repo M_OR1_Archival_Detail_Repo;

	@Autowired
	BRRS_M_OR1_Archival_Summary_Repo M_OR1_Archival_Summary_Repo;
	
	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	
	public ModelAndView getM_OR1View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {

		ModelAndView mv = new ModelAndView();
/*		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;*/

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_OR1_Archival_Summary_Entity> T1Master = new ArrayList<M_OR1_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_OR1_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);

		} else {

			List<M_OR1_Summary_Entity> T1Master = new ArrayList<M_OR1_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_OR1_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_OR1");

		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getM_OR1currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLable = null;
			String reportAddlCriteria1 = null;

			// ✅ Split the filter string here
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLable = parts[0];
					reportAddlCriteria1 = parts[1];
				}
			}

			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				System.out.println(version);
				// 🔹 Archival branch
				List<M_OR1_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria1 != null) {
					T1Dt1 = M_OR1_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria1, parsedDate, version);
				} else {
					T1Dt1 = M_OR1_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
					totalPages = M_OR1_Detail_Repo.getdatacount(parsedDate);
					System.out.println(T1Dt1.size());
					mv.addObject("pagination", "YES");

				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				System.out.println("Praveen");
				// 🔹 Current branch
				List<M_OR1_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria1 != null) {
					T1Dt1 = M_OR1_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria1, parsedDate);
				} else {
					T1Dt1 = M_OR1_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
					totalPages = M_OR1_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/M_OR1");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}
	
	
	public void updateReport(M_OR1_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("report_date: " + updatedEntity.getReport_date());

	    M_OR1_Summary_Entity existing = M_OR1_Summary_Repo.findById(updatedEntity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	    	// Loop from R11 to R50 and copy fields
	    	int[] rows = new int[56];
	    	for (int k = 0, r = 10; r <= 22; r++, k++) {
	    	    rows[k] = r;
	    	}

	    	for (int i : rows) {
	    	    String prefix = "R" + i + "_";   // Use capital R (same as your working code)
	    	    String[] fields = {"gross_income"};

	    	    for (String field : fields) {
	    	        try {
	    	            String getterName = "get" + prefix + field;
	    	            String setterName = "set" + prefix + field;

	    	            Method getter = M_OR1_Summary_Entity.class.getMethod(getterName);
	    	            Method setter = M_OR1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	    	            Object newValue = getter.invoke(updatedEntity);
	    	            setter.invoke(existing, newValue);

	    	        } catch (NoSuchMethodException e) {
	    	            // Skip missing getter/setter gracefully
	    	            continue;
	    	        }
	    	    }
	    	}


	        // Loop from R17 to R30 and copy fields
	    	// Loop from R23 to R34 and copy fields
	    	int[] rows2 = new int[12];
	    	for (int k = 0, r = 23; r <= 34; r++, k++) {
	    	    rows2[k] = r;
	    	}

	    	for (int i : rows2) {
	    	    String prefix = "R" + i + "_";   // FIX: Capital R (same as your working model)
	    	    String[] fields = {"gross_income"};

	    	    for (String field : fields) {
	    	        try {
	    	            String getterName = "get" + prefix + field;
	    	            String setterName = "set" + prefix + field;

	    	            Method getter = M_OR1_Summary_Entity.class.getMethod(getterName);
	    	            Method setter = M_OR1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	    	            Object newValue = getter.invoke(updatedEntity);
	    	            setter.invoke(existing, newValue);

	    	        } catch (NoSuchMethodException e) {
	    	            // Skip missing getter/setter gracefully
	    	            continue;
	    	        }
	    	    }
	    	}


	    	// Loop from R36 to R46 and copy fields
	    	int[] rows3 = new int[11];
	    	for (int k = 0, r = 36; r <= 46; r++, k++) {
	    	    rows3[k] = r;
	    	}

	    	for (int i : rows3) {
	    	    String prefix = "R" + i + "_";   // FIXED: Capital 'R'
	    	    String[] fields = {"gross_income"};

	    	    for (String field : fields) {
	    	        try {
	    	            String getterName = "get" + prefix + field;
	    	            String setterName = "set" + prefix + field;

	    	            Method getter = M_OR1_Summary_Entity.class.getMethod(getterName);
	    	            Method setter = M_OR1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	    	            Object newValue = getter.invoke(updatedEntity);
	    	            setter.invoke(existing, newValue);

	    	        } catch (NoSuchMethodException e) {
	    	            // Skip missing getter/setter gracefully
	    	            continue;
	    	        }
	    	    }
	    	}


	        int[] Rows = {22,35,48};
	        for (int i : Rows) {
	            String prefix = "R" + i + "_";
	            String[] fields = {"gross_income" };

	            for (String field : fields) {
	                try {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    Method getter = M_OR1_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_OR1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing getter/setter gracefully
	                    continue;
	                }
	            }
	        }
	        
	        int[] Rows1 = {50,52,54};
	        for (int i : Rows1) {
	            String prefix = "R" + i + "_";
	            String[] fields = {"aggregate_gross_income" };

	            for (String field : fields) {
	                try {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    Method getter = M_OR1_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_OR1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing getter/setter gracefully
	                    continue;
	                }
	            }
	        }

	        int[] Rows2 = {56};
	        for (int i : Rows2) {
	            String prefix = "R" + i + "_";
	            String[] fields = {"risk_weight_factor" };

	            for (String field : fields) {
	                try {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    Method getter = M_OR1_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_OR1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing getter/setter gracefully
	                    continue;
	                }
	            }
	        }
	            
	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // Save updated entity
	    System.out.println("abc");
	    M_OR1_Summary_Repo.save(existing);
	}
	
	
	
	

	public byte[] BRRS_M_OR1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);
		if (type.equals("ARCHIVAL") & version != null) {
			byte[] ARCHIVALreport = getExcelM_OR1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
			return ARCHIVALreport;
		}

		List<M_OR1_Summary_Entity> dataList = M_OR1_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M-OR1 report. Returning empty result.");
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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---
			
			int startRow = 9;
			
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_OR1_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					//row11
					// Column C
					Cell cell3 = row.createCell(3);
					if (record.getR10_gross_income() != null) {
						cell3.setCellValue(record.getR10_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(10);
					cell3 = row.createCell(3);
					if (record.getR11_gross_income() != null) {
					    cell3.setCellValue(record.getR11_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);
					cell3 = row.createCell(3);
					if (record.getR12_gross_income() != null) {
					    cell3.setCellValue(record.getR12_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell3 = row.createCell(3);
					if (record.getR13_gross_income() != null) {
					    cell3.setCellValue(record.getR13_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell3 = row.createCell(3);
					if (record.getR14_gross_income() != null) {
					    cell3.setCellValue(record.getR14_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					cell3 = row.createCell(3);
					if (record.getR15_gross_income() != null) {
					    cell3.setCellValue(record.getR15_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					cell3 = row.createCell(3);
					if (record.getR16_gross_income() != null) {
					    cell3.setCellValue(record.getR16_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell3 = row.createCell(3);
					if (record.getR17_gross_income() != null) {
					    cell3.setCellValue(record.getR17_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell3 = row.createCell(3);
					if (record.getR18_gross_income() != null) {
					    cell3.setCellValue(record.getR18_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell3 = row.createCell(3);
					if (record.getR19_gross_income() != null) {
					    cell3.setCellValue(record.getR19_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell3 = row.createCell(3);
					if (record.getR20_gross_income() != null) {
					    cell3.setCellValue(record.getR20_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell3 = row.createCell(3);
					if (record.getR21_gross_income() != null) {
					    cell3.setCellValue(record.getR21_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell3 = row.createCell(3);
					if (record.getR22_gross_income() != null) {
					    cell3.setCellValue(record.getR22_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell3 = row.createCell(3);
					if (record.getR23_gross_income() != null) {
					    cell3.setCellValue(record.getR23_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell3 = row.createCell(3);
					if (record.getR24_gross_income() != null) {
					    cell3.setCellValue(record.getR24_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell3 = row.createCell(3);
					if (record.getR25_gross_income() != null) {
					    cell3.setCellValue(record.getR25_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell3 = row.createCell(3);
					if (record.getR26_gross_income() != null) {
					    cell3.setCellValue(record.getR26_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell3 = row.createCell(3);
					if (record.getR27_gross_income() != null) {
					    cell3.setCellValue(record.getR27_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell3 = row.createCell(3);
					if (record.getR28_gross_income() != null) {
					    cell3.setCellValue(record.getR28_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell3 = row.createCell(3);
					if (record.getR29_gross_income() != null) {
					    cell3.setCellValue(record.getR29_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell3 = row.createCell(3);
					if (record.getR30_gross_income() != null) {
					    cell3.setCellValue(record.getR30_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell3 = row.createCell(3);
					if (record.getR31_gross_income() != null) {
					    cell3.setCellValue(record.getR31_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell3 = row.createCell(3);
					if (record.getR32_gross_income() != null) {
					    cell3.setCellValue(record.getR32_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell3 = row.createCell(3);
					if (record.getR33_gross_income() != null) {
					    cell3.setCellValue(record.getR33_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell3 = row.createCell(3);
					if (record.getR34_gross_income() != null) {
					    cell3.setCellValue(record.getR34_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell3 = row.createCell(3);
					if (record.getR35_gross_income() != null) {
					    cell3.setCellValue(record.getR35_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell3 = row.createCell(3);
					if (record.getR36_gross_income() != null) {
					    cell3.setCellValue(record.getR36_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell3 = row.createCell(3);
					if (record.getR37_gross_income() != null) {
					    cell3.setCellValue(record.getR37_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					cell3 = row.createCell(3);
					if (record.getR38_gross_income() != null) {
					    cell3.setCellValue(record.getR38_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
					cell3 = row.createCell(3);
					if (record.getR39_gross_income() != null) {
					    cell3.setCellValue(record.getR39_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell3 = row.createCell(3);
					if (record.getR40_gross_income() != null) {
					    cell3.setCellValue(record.getR40_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					cell3 = row.createCell(3);
					if (record.getR41_gross_income() != null) {
					    cell3.setCellValue(record.getR41_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell3 = row.createCell(3);
					if (record.getR42_gross_income() != null) {
					    cell3.setCellValue(record.getR42_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell3 = row.createCell(3);
					if (record.getR43_gross_income() != null) {
					    cell3.setCellValue(record.getR43_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell3 = row.createCell(3);
					if (record.getR44_gross_income() != null) {
					    cell3.setCellValue(record.getR44_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					cell3 = row.createCell(3);
					if (record.getR45_gross_income() != null) {
					    cell3.setCellValue(record.getR45_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell3 = row.createCell(3);
					if (record.getR46_gross_income() != null) {
					    cell3.setCellValue(record.getR46_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell3 = row.createCell(3);
					if (record.getR47_gross_income() != null) {
					    cell3.setCellValue(record.getR47_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell3 = row.createCell(3);
					if (record.getR48_gross_income() != null) {
					    cell3.setCellValue(record.getR48_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(48);
					cell3 = row.createCell(3);
					if (record.getR49_gross_income() != null) {
					    cell3.setCellValue(record.getR49_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(49);
					cell3 = row.createCell(4);
					if (record.getR50_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR50_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(50);
					cell3 = row.createCell(4);
					if (record.getR51_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR51_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(51);
					cell3 = row.createCell(4);
					if (record.getR52_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR52_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(52);
					cell3 = row.createCell(4);
					if (record.getR53_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR53_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(53);
					cell3 = row.createCell(4);
					if (record.getR54_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR54_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(54);
					cell3 = row.createCell(5);
					if (record.getR55_risk_weight_factor() != null) {
					    cell3.setCellValue(record.getR55_risk_weight_factor().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(55);
					cell3 = row.createCell(5);
					if (record.getR56_risk_weight_factor() != null) {
					    cell3.setCellValue(record.getR56_risk_weight_factor().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
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

	public byte[] BRRS_M_OR1DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_OR1 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_OR1Details");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT_LABLE", "REPORT_ADDL_CRITERIA_1",
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
			List<M_OR1_Detail_Entity> reportData = M_OR1_Detail_Repo.getdatabydateList(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_OR1_Detail_Entity item : reportData) {
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
				logger.info("No data found for BRRS_M_OR1 — only header will be written.");
			}
// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_OR1 Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getM_OR1Archival() {
		List<Object> M_OR1Archivallist = new ArrayList<>();
		try {
			M_OR1Archivallist = M_OR1_Archival_Summary_Repo.getM_OR1archival();
			System.out.println("countser" + M_OR1Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_OR1 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_OR1Archivallist;
	}

	public byte[] getExcelM_OR1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_OR1_Archival_Summary_Entity> dataList = M_OR1_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M-OR1 report. Returning empty result.");
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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---
			
            int startRow = 9;
			
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_OR1_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					//row11
					// Column C
					Cell cell3 = row.createCell(3);
					if (record.getR10_gross_income() != null) {
						cell3.setCellValue(record.getR10_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(10);
					cell3 = row.createCell(3);
					if (record.getR11_gross_income() != null) {
					    cell3.setCellValue(record.getR11_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);
					cell3 = row.createCell(3);
					if (record.getR12_gross_income() != null) {
					    cell3.setCellValue(record.getR12_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell3 = row.createCell(3);
					if (record.getR13_gross_income() != null) {
					    cell3.setCellValue(record.getR13_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell3 = row.createCell(3);
					if (record.getR14_gross_income() != null) {
					    cell3.setCellValue(record.getR14_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					cell3 = row.createCell(3);
					if (record.getR15_gross_income() != null) {
					    cell3.setCellValue(record.getR15_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					cell3 = row.createCell(3);
					if (record.getR16_gross_income() != null) {
					    cell3.setCellValue(record.getR16_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell3 = row.createCell(3);
					if (record.getR17_gross_income() != null) {
					    cell3.setCellValue(record.getR17_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell3 = row.createCell(3);
					if (record.getR18_gross_income() != null) {
					    cell3.setCellValue(record.getR18_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell3 = row.createCell(3);
					if (record.getR19_gross_income() != null) {
					    cell3.setCellValue(record.getR19_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell3 = row.createCell(3);
					if (record.getR20_gross_income() != null) {
					    cell3.setCellValue(record.getR20_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell3 = row.createCell(3);
					if (record.getR21_gross_income() != null) {
					    cell3.setCellValue(record.getR21_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell3 = row.createCell(3);
					if (record.getR22_gross_income() != null) {
					    cell3.setCellValue(record.getR22_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell3 = row.createCell(3);
					if (record.getR23_gross_income() != null) {
					    cell3.setCellValue(record.getR23_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell3 = row.createCell(3);
					if (record.getR24_gross_income() != null) {
					    cell3.setCellValue(record.getR24_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell3 = row.createCell(3);
					if (record.getR25_gross_income() != null) {
					    cell3.setCellValue(record.getR25_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell3 = row.createCell(3);
					if (record.getR26_gross_income() != null) {
					    cell3.setCellValue(record.getR26_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell3 = row.createCell(3);
					if (record.getR27_gross_income() != null) {
					    cell3.setCellValue(record.getR27_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell3 = row.createCell(3);
					if (record.getR28_gross_income() != null) {
					    cell3.setCellValue(record.getR28_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell3 = row.createCell(3);
					if (record.getR29_gross_income() != null) {
					    cell3.setCellValue(record.getR29_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell3 = row.createCell(3);
					if (record.getR30_gross_income() != null) {
					    cell3.setCellValue(record.getR30_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell3 = row.createCell(3);
					if (record.getR31_gross_income() != null) {
					    cell3.setCellValue(record.getR31_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell3 = row.createCell(3);
					if (record.getR32_gross_income() != null) {
					    cell3.setCellValue(record.getR32_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell3 = row.createCell(3);
					if (record.getR33_gross_income() != null) {
					    cell3.setCellValue(record.getR33_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell3 = row.createCell(3);
					if (record.getR34_gross_income() != null) {
					    cell3.setCellValue(record.getR34_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell3 = row.createCell(3);
					if (record.getR35_gross_income() != null) {
					    cell3.setCellValue(record.getR35_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell3 = row.createCell(3);
					if (record.getR36_gross_income() != null) {
					    cell3.setCellValue(record.getR36_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell3 = row.createCell(3);
					if (record.getR37_gross_income() != null) {
					    cell3.setCellValue(record.getR37_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					cell3 = row.createCell(3);
					if (record.getR38_gross_income() != null) {
					    cell3.setCellValue(record.getR38_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
					cell3 = row.createCell(3);
					if (record.getR39_gross_income() != null) {
					    cell3.setCellValue(record.getR39_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell3 = row.createCell(3);
					if (record.getR40_gross_income() != null) {
					    cell3.setCellValue(record.getR40_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					cell3 = row.createCell(3);
					if (record.getR41_gross_income() != null) {
					    cell3.setCellValue(record.getR41_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell3 = row.createCell(3);
					if (record.getR42_gross_income() != null) {
					    cell3.setCellValue(record.getR42_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell3 = row.createCell(3);
					if (record.getR43_gross_income() != null) {
					    cell3.setCellValue(record.getR43_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell3 = row.createCell(3);
					if (record.getR44_gross_income() != null) {
					    cell3.setCellValue(record.getR44_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					cell3 = row.createCell(3);
					if (record.getR45_gross_income() != null) {
					    cell3.setCellValue(record.getR45_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell3 = row.createCell(3);
					if (record.getR46_gross_income() != null) {
					    cell3.setCellValue(record.getR46_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell3 = row.createCell(3);
					if (record.getR47_gross_income() != null) {
					    cell3.setCellValue(record.getR47_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell3 = row.createCell(3);
					if (record.getR48_gross_income() != null) {
					    cell3.setCellValue(record.getR48_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(48);
					cell3 = row.createCell(3);
					if (record.getR49_gross_income() != null) {
					    cell3.setCellValue(record.getR49_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(49);
					cell3 = row.createCell(4);
					if (record.getR50_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR50_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(50);
					cell3 = row.createCell(4);
					if (record.getR51_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR51_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(51);
					cell3 = row.createCell(4);
					if (record.getR52_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR52_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(52);
					cell3 = row.createCell(4);
					if (record.getR53_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR53_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(53);
					cell3 = row.createCell(4);
					if (record.getR54_aggregate_gross_income() != null) {
					    cell3.setCellValue(record.getR54_aggregate_gross_income().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(54);
					cell3 = row.createCell(5);
					if (record.getR55_risk_weight_factor() != null) {
					    cell3.setCellValue(record.getR55_risk_weight_factor().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(55);
					cell3 = row.createCell(5);
					if (record.getR56_risk_weight_factor() != null) {
					    cell3.setCellValue(record.getR56_risk_weight_factor().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
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
			logger.info("Generating Excel for BRRS_M_OR1 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MOR2Detail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT_LABLE", "REPORT_ADDL_CRITERIA_1",
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
			List<M_OR1_Archival_Detail_Entity> reportData = M_OR1_Archival_Detail_Repo.getdatabydateList(parsedToDate,
					version);
			System.out.println("Size");
			System.out.println(reportData.size());
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_OR1_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for BRRS_M-OR1 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_OR1Excel", e);
			return new byte[0];
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_OR1"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	    	M_OR1_Detail_Entity la1Entity = M_OR1_Detail_Repo.findByAcctnumber(acctNo);
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
    ModelAndView mv = new ModelAndView("BRRS/M_OR1"); // ✅ match the report name

    if (acctNo != null) {
        M_OR1_Detail_Entity la1Entity = M_OR1_Detail_Repo.findByAcctnumber(acctNo);
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
        String provisionStr = request.getParameter("acctBalanceInpula");
        String acctName = request.getParameter("acctName");
        String reportDateStr = request.getParameter("reportDate");

        logger.info("Received update for ACCT_NO: {}", acctNo);

        M_OR1_Detail_Entity existing = M_OR1_Detail_Repo.findByAcctnumber(acctNo);
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
            if (existing.getAcctBalanceInpula() == null ||
                existing.getAcctBalanceInpula().compareTo(newProvision) != 0) {
                existing.setAcctBalanceInpula(newProvision);
                isChanged = true;
                logger.info("Balance updated to {}", newProvision);
            }
        }
        
        

        if (isChanged) {
        	M_OR1_Detail_Repo.save(existing);
            logger.info("Record updated successfully for account {}", acctNo);

            // Format date for procedure
            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

            // Run summary procedure after commit
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    try {
                        logger.info("Transaction committed — calling BRRS_M_OR1_SUMMARY_PROCEDURE({})",
                                formattedDate);
                        jdbcTemplate.update("BEGIN BRRS_M_OR1_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
        logger.error("Error updating M_OR1 record", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating record: " + e.getMessage());
    }
}


}
