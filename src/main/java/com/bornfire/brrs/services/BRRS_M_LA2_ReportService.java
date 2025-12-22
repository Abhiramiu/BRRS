package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_LA2_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA2_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_PLL_Detail_Repo;
import com.bornfire.brrs.entities.M_LA2_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_LA2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LA2_Detail_Entity;
import com.bornfire.brrs.entities.M_LA2_Detail_Repo;
import com.bornfire.brrs.entities.M_LA2_Detail_Repo_Archival;
import com.bornfire.brrs.entities.M_LA2_Summary_Entity;
import com.bornfire.brrs.entities.M_PLL_Detail_Entity;


@Component
@Service
public class BRRS_M_LA2_ReportService {


	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LA2_ReportService.class);

	@Autowired
	private M_LA2_Detail_Repo repo;

	@Autowired
	private M_LA2_Detail_Repo_Archival ArchivalDetailrepo;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	
	@Autowired
	BRRS_M_LA2_Summary_Repo	BRRS_M_LA2_Summary_Repo;

	@Autowired
	BRRS_M_LA2_Archival_Summary_Repo M_LA2_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_LA2View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_LA2_Archival_Summary_Entity> T1Master = new ArrayList<M_LA2_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_LA2_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {
			List<M_LA2_Summary_Entity> T1Master = new ArrayList<M_LA2_Summary_Entity>();
			
			
			try {
				Date d1 = dateformat.parse(todate);

				T1Master = BRRS_M_LA2_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				
				System.out.println("Size of t1master is :"+T1Master.size());
				
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			
		}

		
		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_LA2");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public ModelAndView getM_La2currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

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
				List<M_LA2_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = ArchivalDetailrepo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate,
							version);
				} else {
					T1Dt1 = ArchivalDetailrepo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_LA2_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = repo.getdatabydateList(parsedDate, currentPage, pageSize);
					System.out.println("la1 size is : "+ T1Dt1.size());
					totalPages = repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/M_LA2");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}
	

	public void updateDetailFromForm(Date reportDate, Map<String, String> params) {

	    System.out.println("Updating LA2 detail table");

	    for (Map.Entry<String, String> entry : params.entrySet()) {

	        String key = entry.getKey();
	        String value = entry.getValue();

	        // Only process TOTAL fields
	        if (!key.matches("R\\d+_C\\d+_TOTAL")) {
	            continue;
	        }

	        String[] parts = key.split("_");
	        String reportLabel = parts[0];          // R12
	        String addlCriteria = parts[1];         // C2

	        BigDecimal amount =
	                (value == null || value.isEmpty())
	                        ? BigDecimal.ZERO
	                        : new BigDecimal(value);

	        List<M_LA2_Detail_Entity> rows =
	                repo.findByReportDateAndReportLableAndReportAddlCriteria1(
	                        reportDate, reportLabel, addlCriteria
	                );

	        for (M_LA2_Detail_Entity row : rows) {
	            row.setAcctBalanceInPula(amount);
	            row.setModifyFlg("Y");
	        }

	        repo.saveAll(rows);
	    }
	    
	    callSummaryProcedure(reportDate);
	}

	private void callSummaryProcedure(Date reportDate) {

	    String sql = "{ call BRRS_M_LA2_SUMMARY_PROCEDURE(?) }";

	    jdbcTemplate.update(connection -> {
	        CallableStatement cs = connection.prepareCall(sql);

	        // Force exact format expected by procedure
	        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	        sdf.setLenient(false);

	        String formattedDate = sdf.format(reportDate);

	        cs.setString(1, formattedDate);  // 🔥 THIS IS MANDATORY
	        return cs;
	    });

	    System.out.println("✅ Summary procedure executed for date: " +
	            new SimpleDateFormat("dd-MM-yyyy").format(reportDate));
	}

	/*public void updateReport(M_LA2_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_LA2_Summary_Entity existing = BRRS_M_LA2_Summary_Repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 12; i <= 25; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = {  "TOTAL"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_LA2_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_LA2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R26 totals
	        String[] totalFields = { "TOTAL" };
	        for (String field : totalFields) {
	            String getterName = "getR26_" + field;
	            String setterName = "setR26_" + field;

	            try {
	                Method getter = M_LA2_Summary_Entity.class.getMethod(getterName);
	                Method setter = M_LA2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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
	    BRRS_M_LA2_Summary_Repo.save(existing);
	
	}*/
	
	public List<Object> getM_LA2Archival() {
		List<Object> M_LA2Archivallist = new ArrayList<>();
		try {
			M_LA2Archivallist = M_LA2_Archival_Summary_Repo.getM_LA2archival();
			System.out.println("countser" + M_LA2Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_LA2 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_LA2Archivallist;
	}
	
	public void updateArchivalReport(M_LA2_Archival_Summary_Entity updatedEntity) {
		
		    System.out.println("Came to services 1");
		    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		    M_LA2_Archival_Summary_Entity existing = M_LA2_Archival_Summary_Repo.findById(updatedEntity.getREPORT_DATE())
		            .orElseThrow(() -> new RuntimeException(
		                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		    try {
		        // 1️⃣ Loop from R12 to R25 and copy fields
		        for (int i = 12; i <= 25; i++) {
		            String prefix = "R" + i + "_";

		            String[] fields = {"TOTAL"};

		            for (String field : fields) {
		                String getterName = "get" + prefix + field;
		                String setterName = "set" + prefix + field;

		                try {
		                    Method getter = M_LA2_Archival_Summary_Entity.class.getMethod(getterName);
		                    Method setter = M_LA2_Archival_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

		                    Object newValue = getter.invoke(updatedEntity);
		                    setter.invoke(existing, newValue);

		                } catch (NoSuchMethodException e) {
		                    // Skip missing fields
		                    continue;
		                }
		            }
		        }

		        // 2️⃣ Handle R26 totals
		        String[] totalFields = { "TOTAL" };
		        for (String field : totalFields) {
		            String getterName = "getR26_" + field;
		            String setterName = "setR26_" + field;

		            try {
		                Method getter = M_LA2_Archival_Summary_Entity.class.getMethod(getterName);
		                Method setter = M_LA2_Archival_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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
		    System.out.println("Testing 1");

		    // 3️⃣ Save updated entity
		    M_LA2_Archival_Summary_Repo.save(existing);
		
		}
	
	public byte[] BRRS_M_LA2Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_LA2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<M_LA2_Summary_Entity> dataList = BRRS_M_LA2_Summary_Repo.getdatabydateList(dateformat.parse(todate));

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
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}

		if (!Files.isReadable(templatePath)) {
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
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LA2_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// row12
					// Column B
					Cell cell2 = row.createCell(1);
					if (record.getR12_TOTAL() != null) {
						cell2.setCellValue(record.getR12_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// row13
					row = sheet.getRow(12);
					// Column B
					Cell R13cell2 = row.getCell(1);
					if (record.getR13_TOTAL() != null) {
						R13cell2.setCellValue(record.getR13_TOTAL().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);

					}
					
					// row14
					row = sheet.getRow(13);
					// Column B
					Cell R14cell2 = row.getCell(1);
					if (record.getR14_TOTAL() != null) {
						R14cell2.setCellValue(record.getR14_TOTAL().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);

					}
					
					// row15
					row = sheet.getRow(14);
					// Column B
					Cell R15cell2 = row.getCell(1);
					if (record.getR15_TOTAL() != null) {
						R15cell2.setCellValue(record.getR15_TOTAL().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);

					}
					
					// row16
					row = sheet.getRow(15);
					// Column B
					Cell R16cell2 = row.getCell(1);
					if (record.getR16_TOTAL() != null) {
						R16cell2.setCellValue(record.getR16_TOTAL().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);

					}
					
					// row17
					row = sheet.getRow(16);
					// Column B
					Cell R17cell2 = row.getCell(1);
					if (record.getR17_TOTAL() != null) {
						R17cell2.setCellValue(record.getR17_TOTAL().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);

					}
					
					// row18
					row = sheet.getRow(17);
					// Column B
					Cell R18cell2 = row.getCell(1);
					if (record.getR18_TOTAL() != null) {
						R18cell2.setCellValue(record.getR18_TOTAL().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);

					}
					
					// row19
					row = sheet.getRow(18);
					// Column B
					Cell R19cell2 = row.getCell(1);
					if (record.getR19_TOTAL() != null) {
						R19cell2.setCellValue(record.getR19_TOTAL().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);

					}
					
					// row20
					row = sheet.getRow(19);
					// Column B
					Cell R20cell2 = row.getCell(1);
					if (record.getR20_TOTAL() != null) {
						R20cell2.setCellValue(record.getR20_TOTAL().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);

					}
					
					// row21
					row = sheet.getRow(20);
					// Column B
					Cell R21cell2 = row.getCell(1);
					if (record.getR21_TOTAL() != null) {
						R21cell2.setCellValue(record.getR21_TOTAL().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);

					}
					
					// row22
					row = sheet.getRow(21);
					// Column B
					Cell R22cell2 = row.getCell(1);
					if (record.getR22_TOTAL() != null) {
						R22cell2.setCellValue(record.getR22_TOTAL().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);

					}
					
					// row23
					row = sheet.getRow(22);
					// Column B
					Cell R23cell2 = row.getCell(1);
					if (record.getR23_TOTAL() != null) {
						R23cell2.setCellValue(record.getR23_TOTAL().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);

					}
					
					// row24
					row = sheet.getRow(23);
					// Column B
					Cell R24cell2 = row.getCell(1);
					if (record.getR24_TOTAL() != null) {
						R24cell2.setCellValue(record.getR24_TOTAL().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);

					}
					
					
					// row25
					row = sheet.getRow(24);
					// Column B
					Cell R25cell2 = row.getCell(1);
					if (record.getR25_TOTAL() != null) {
						R25cell2.setCellValue(record.getR25_TOTAL().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);

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
	
	public byte[] getExcelM_LA2ARCHIVAL(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);
		if (type.equals("ARCHIVAL") && version != null) {

		}
		List<M_LA2_Archival_Summary_Entity> dataList = M_LA2_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA2 report. Returning empty result.");
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
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LA2_Archival_Summary_Entity record = dataList.get(i);
					

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column B
					Cell cell2 = row.createCell(1);
					if (record.getR12_TOTAL() != null) {
						cell2.setCellValue(record.getR12_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// row13
					row = sheet.getRow(12);
					// Column B
					Cell R13cell2 = row.getCell(1);
					if (record.getR13_TOTAL() != null) {
						R13cell2.setCellValue(record.getR13_TOTAL().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);

					}
					
					// row14
					row = sheet.getRow(13);
					// Column B
					Cell R14cell2 = row.getCell(1);
					if (record.getR14_TOTAL() != null) {
						R14cell2.setCellValue(record.getR14_TOTAL().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);

					}
					
					// row15
					row = sheet.getRow(14);
					// Column B
					Cell R15cell2 = row.getCell(1);
					if (record.getR15_TOTAL() != null) {
						R15cell2.setCellValue(record.getR15_TOTAL().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);

					}
					
					// row16
					row = sheet.getRow(15);
					// Column B
					Cell R16cell2 = row.getCell(1);
					if (record.getR16_TOTAL() != null) {
						R16cell2.setCellValue(record.getR16_TOTAL().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);

					}
					
					// row17
					row = sheet.getRow(16);
					// Column B
					Cell R17cell2 = row.getCell(1);
					if (record.getR17_TOTAL() != null) {
						R17cell2.setCellValue(record.getR17_TOTAL().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);

					}
					
					// row18
					row = sheet.getRow(17);
					// Column B
					Cell R18cell2 = row.getCell(1);
					if (record.getR18_TOTAL() != null) {
						R18cell2.setCellValue(record.getR18_TOTAL().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);

					}
					
					// row19
					row = sheet.getRow(18);
					// Column B
					Cell R19cell2 = row.getCell(1);
					if (record.getR19_TOTAL() != null) {
						R19cell2.setCellValue(record.getR19_TOTAL().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);

					}
					
					// row20
					row = sheet.getRow(19);
					// Column B
					Cell R20cell2 = row.getCell(1);
					if (record.getR20_TOTAL() != null) {
						R20cell2.setCellValue(record.getR20_TOTAL().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);

					}
					
					// row21
					row = sheet.getRow(20);
					// Column B
					Cell R21cell2 = row.getCell(1);
					if (record.getR21_TOTAL() != null) {
						R21cell2.setCellValue(record.getR21_TOTAL().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);

					}
					
					// row22
					row = sheet.getRow(21);
					// Column B
					Cell R22cell2 = row.getCell(1);
					if (record.getR22_TOTAL() != null) {
						R22cell2.setCellValue(record.getR22_TOTAL().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);

					}
					
					// row23
					row = sheet.getRow(22);
					// Column B
					Cell R23cell2 = row.getCell(1);
					if (record.getR23_TOTAL() != null) {
						R23cell2.setCellValue(record.getR23_TOTAL().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);

					}
					
					// row24
					row = sheet.getRow(23);
					// Column B
					Cell R24cell2 = row.getCell(1);
					if (record.getR24_TOTAL() != null) {
						R24cell2.setCellValue(record.getR24_TOTAL().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);

					}
					
					
					// row25
					row = sheet.getRow(24);
					// Column B
					Cell R25cell2 = row.getCell(1);
					if (record.getR25_TOTAL() != null) {
						R25cell2.setCellValue(record.getR25_TOTAL().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);

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
