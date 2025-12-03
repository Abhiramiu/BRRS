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

import com.bornfire.brrs.entities.M_PD_Summary_Entity;
import com.bornfire.brrs.entities.M_PD_Summary_Entity2;
import com.bornfire.brrs.entities.M_PD_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_PD_Archival_Summary_Entity2;

import com.bornfire.brrs.entities.BRRS_M_PD_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_PD_Summary_Repo2;

import com.bornfire.brrs.entities.BRRS_M_PD_Archival_Summary_Repo;

import com.bornfire.brrs.entities.M_PD_Detail_Entity;
import com.bornfire.brrs.entities.M_PD_Archival_Detail_Entity;

import com.bornfire.brrs.entities.BRRS_M_PD_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_PD_Archival_Detail_Repo;

import com.bornfire.brrs.entities.M_PD_Manual_Summary_Entity;
import com.bornfire.brrs.entities.M_PD_Manual_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_PD_Manual_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_PD_Manual_Archival_Summary_Repo;

import java.math.BigDecimal;

@Component
@Service
public class BRRS_M_PD_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_PD_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;



	@Autowired
	BRRS_M_PD_Detail_Repo BRRS_M_PD_Detail_Repo;

	@Autowired
	BRRS_M_PD_Summary_Repo BRRS_M_PD_Summary_Repo;
	

	@Autowired
	BRRS_M_PD_Archival_Detail_Repo BRRS_M_PD_Archival_Detail_Repo;

	@Autowired
	BRRS_M_PD_Archival_Summary_Repo BRRS_M_PD_Archival_Summary_Repo1;

	
	@Autowired
	BRRS_M_PD_Manual_Summary_Repo BRRS_M_PD_Manual_Summary_Repo;
	
	@Autowired
	BRRS_M_PD_Manual_Archival_Summary_Repo BRRS_M_PD_Manual_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	
	
	

	public ModelAndView getM_PDview(String reportId, String fromdate, String todate, String currency,
										String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		System.out.println("testing");
		System.out.println(version);

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			
			List<M_PD_Archival_Summary_Entity> T1Master = new ArrayList<M_PD_Archival_Summary_Entity>();
		//	List<M_PD_Archival_Summary_Entity2> T2Master = new ArrayList<M_PD_Archival_Summary_Entity2>();
			List<M_PD_Manual_Archival_Summary_Entity> T3Master = new ArrayList<M_PD_Manual_Archival_Summary_Entity>();
			
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from brrs1_REPORT_ENTITY a where a.report_date = ?1
				// ", brrs1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_PD_Archival_Summary_Repo1.getdatabydateListarchival(dateformat.parse(todate), version);
			//	T2Master = BRRS_M_PD_Archival_Summary_Repo2.getdatabydateListarchival(dateformat.parse(todate), version);
				T3Master = BRRS_M_PD_Manual_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary1", T1Master);
		//	mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary", T3Master);
			
			
		} else {
			List<M_PD_Summary_Entity> T1Master = new ArrayList<M_PD_Summary_Entity>();
		//	List<M_PD_Summary_Entity2> T2Master = new ArrayList<M_PD_Summary_Entity2>();
			List<M_PD_Manual_Summary_Entity> T3Master = new ArrayList<M_PD_Manual_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from brrs1_REPORT_ENTITY a where a.report_date = ?1
				// ", brrs1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_PD_Summary_Repo.getdatabydateList(dateformat.parse(todate));
			//	T2Master = BRRS_M_PD_Summary_Repo2.getdatabydateList(dateformat.parse(todate));
				T3Master = BRRS_M_PD_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary1", T1Master);
		//	mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary", T3Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_PD");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public ModelAndView getM_PDcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		ModelAndView mv = new ModelAndView("BRRS/M_PD");
		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalRecords = 0;

		try {
// ✅ Parse toDate
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

// ✅ Parse filter (reportLable, reportAddlCriteria1)
			String reportLable = null, reportAddlCriteria1 = null, reportAddlCriteria2 = null, reportAddlCriteria3 = null, reportAddlCriteria4 = null;
			if (filter != null && !filter.isEmpty()) {
				String[] parts = filter.split(",", -1);
				reportLable = parts.length > 0 ? parts[0] : null;
				reportAddlCriteria1 = parts.length > 1 ? parts[1] : null;
				reportAddlCriteria2 = parts.length > 2 ? parts[2] : null;
				reportAddlCriteria3 = parts.length > 3 ? parts[3] : null;
				reportAddlCriteria4 = parts.length > 4 ? parts[4] : null;
			}

// ✅ ARCHIVAL DATA BRANCH
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.isEmpty()) {
				logger.info("Fetching ARCHIVAL data for version {}", version);

				List<M_PD_Archival_Detail_Entity> detailList;

// 🔹 Filtered (ROWID + COLUMNID)
				if (reportLable != null && !reportLable.isEmpty()
						&& (isNotEmpty(reportAddlCriteria1) || isNotEmpty(reportAddlCriteria2) || isNotEmpty(reportAddlCriteria3) ||isNotEmpty(reportAddlCriteria4))) {

					logger.info("➡ ARCHIVAL DETAIL QUERY TRIGGERED (with filters)");
					detailList = BRRS_M_PD_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria1, reportAddlCriteria2, reportAddlCriteria3,
							reportAddlCriteria4,parsedDate);

				} else {
					logger.info("➡ ARCHIVAL LIST QUERY TRIGGERED (with pagination)");
					detailList = BRRS_M_PD_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
					totalRecords = BRRS_M_PD_Archival_Detail_Repo.getdatacount(parsedDate);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);
				logger.info("ARCHIVAL COUNT: {}", (detailList != null ? detailList.size() : 0));

			} else {
// ✅ CURRENT DATA BRANCH
				logger.info("Fetching CURRENT data for M_PD");

				List<M_PD_Detail_Entity> detailList;

				if (reportLable != null && !reportLable.isEmpty()
						&& (isNotEmpty(reportAddlCriteria1) || isNotEmpty(reportAddlCriteria2) || isNotEmpty(reportAddlCriteria3) ||isNotEmpty(reportAddlCriteria4))) {

					logger.info("➡ CURRENT DETAIL QUERY TRIGGERED (with filters)");
					detailList = BRRS_M_PD_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria1, reportAddlCriteria2, reportAddlCriteria3, reportAddlCriteria4,parsedDate);

				} else {
					logger.info("➡ CURRENT LIST QUERY TRIGGERED (with pagination)");
					detailList = BRRS_M_PD_Detail_Repo.getdatabydateList(parsedDate);
					totalRecords = BRRS_M_PD_Detail_Repo.getdatacount(parsedDate);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);
				logger.info("CURRENT COUNT: {}", (detailList != null ? detailList.size() : 0));
			}

		} catch (ParseException e) {
			logger.error("Invalid date format: {}", todate, e);
			mv.addObject("errorMessage", "Invalid date format: " + todate);
		} catch (Exception e) {
			logger.error("Unexpected error in getM_PDcurrentDtl", e);
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

// ✅ Common model attributes
		int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		mv.addObject("totalPages", totalPages);
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		logger.info("Total pages calculated: {}", totalPages);
		return mv;
	}
	
	//Helper for null/empty check
		private boolean isNotEmpty(String value) {
			return value != null && !value.trim().isEmpty();
		}


	public void updateReport(M_PD_Manual_Summary_Entity updatedEntity) {
	    System.out.println("Came to services1");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_PD_Manual_Summary_Entity existing = BRRS_M_PD_Manual_Summary_Repo.findById(updatedEntity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	        // ✅ Loop for table 2 fields
	        int[] Rows = {8,12,26,35,38,41,46,50,54,61};
	        for (int i : Rows) {
	            String prefix = "R" + i + "_";
	            String[] fields = {"30D_90D_PASTDUE","NON_PERFORM_LOANS","NON_ACCRUALS1","SPECIFIC_PROV1","NO_OF_ACC1"
	            		,"90D_180D_PASTDUE","NON_ACCRUALS2","SPECIFIC_PROV2","NO_OF_ACC2","180D_ABOVE_PASTDUE","NON_ACCRUALS3"
	            		,"SPECIFIC_PROV3","NO_OF_ACC3","TOTAL_NON_ACCRUAL","TOTAL_DUE_LOANS","TOTAL_PERFORMING_LOAN","VALUE_OF_COLLATERAL"
	            		,"TOTAL_VALUE_NPL","TOTAL_SPECIFIC_PROV","SPECIFIC_PROV_NPL"};

	            for (String field : fields) {
	                try {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    Method getter = M_PD_Manual_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_PD_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing getter/setter gracefully
	                    continue;
	                }
	            }
	        }
	     // Loop rows 5 to 36
	        for (int i = 9; i <= 60; i++) {
	        	if(i == 12 || i == 26 || i == 35 || i == 38 || i == 41 || i == 46 || i == 50 || i == 54)continue;

	            String prefix = "R" + i + "_";

	            String[] fields = {
	                "30D_90D_PASTDUE","NON_PERFORM_LOANS","NON_ACCRUALS1","SPECIFIC_PROV1","NO_OF_ACC1",
	                "90D_180D_PASTDUE","NON_ACCRUALS2","SPECIFIC_PROV2","NO_OF_ACC2","VALUE_OF_COLLATERAL"
	            };

	            for (int f = 0; f < fields.length; f++) {

	                String field = fields[f];

	                try {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    Method getter = M_PD_Manual_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_PD_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing methods
	                    continue;
	                }
	            }
	        }
		     // Loop rows 5 to 36
	        for (int i = 9; i <= 60; i++) {
	        	if(i == 12 || i == 26 || i == 35 || i == 38 || i == 41 || i == 46 || i == 50 || i == 54)continue;

	            String prefix = "R" + i + "_";

	            String[] fields = {
	            		"TOTAL_NON_ACCRUAL","TOTAL_DUE_LOANS","TOTAL_PERFORMING_LOAN","TOTAL_VALUE_NPL","TOTAL_SPECIFIC_PROV","SPECIFIC_PROV_NPL"
	            };

	            for (int f = 0; f < fields.length; f++) {

	                String field = fields[f];

	                try {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    Method getter = M_PD_Manual_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_PD_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing methods
	                    continue;
	                }
	            }
	        }

	        // ✅ Save after all updates
	        BRRS_M_PD_Manual_Summary_Repo.save(existing);

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	}

	


	public byte[] BRRS_M_PDExcel(String filename, String reportId, String fromdate, String todate, String currency,
									 String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_PDARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<M_PD_Summary_Entity> dataList1 = BRRS_M_PD_Summary_Repo.getdatabydateList(dateformat.parse(todate));
		List<M_PD_Manual_Summary_Entity> dataList2 = BRRS_M_PD_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));
		if (dataList1.isEmpty() || dataList2.isEmpty()) {
			logger.warn("Service: No data found for M_PD report. Returning empty result.");
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
			int startRow = 8;

			if (!dataList1.isEmpty() || !dataList2.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					M_PD_Summary_Entity record1 = dataList1.get(i);
					M_PD_Manual_Summary_Entity record = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);	
					
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
			
					
					
					
					Cell cell2 = row.createCell(1);
					if (record.getR9_30D_90D_PASTDUE() != null) {
						cell2.setCellValue(record.getR9_30D_90D_PASTDUE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					Cell cell3 = row.createCell(2);
					if (record.getR9_NON_PERFORM_LOANS() != null) {
						cell3.setCellValue(record.getR9_NON_PERFORM_LOANS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					Cell cell4 = row.createCell(3);
					if (record.getR9_NON_ACCRUALS1() != null) {
						cell4.setCellValue(record.getR9_NON_ACCRUALS1().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					Cell cell5 = row.createCell(4);
					if (record.getR9_SPECIFIC_PROV1() != null) {
						cell5.setCellValue(record.getR9_SPECIFIC_PROV1().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					Cell cell6 = row.createCell(5);
					if (record.getR9_NO_OF_ACC1() != null) {
						cell6.setCellValue(record.getR9_NO_OF_ACC1().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					Cell cell7 = row.createCell(6);
					if (record.getR9_90D_180D_PASTDUE() != null) {
						cell7.setCellValue(record.getR9_90D_180D_PASTDUE().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					Cell cell8 = row.createCell(7);
					if (record.getR9_NON_ACCRUALS2() != null) {
						cell8.setCellValue(record.getR9_NON_ACCRUALS2().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					Cell cell9 = row.createCell(8);
					if (record.getR9_SPECIFIC_PROV2() != null) {
						cell9.setCellValue(record.getR9_SPECIFIC_PROV2().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					Cell cell10 = row.createCell(9);
					if (record.getR9_NO_OF_ACC2() != null) {
						cell10.setCellValue(record.getR9_NO_OF_ACC2().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					Cell cell11 = row.createCell(10);
					if (record1.getR9_180D_ABOVE_PASTDUE() != null) {
						cell11.setCellValue(record1.getR9_180D_ABOVE_PASTDUE().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					Cell cell12 = row.createCell(11);
					if (record1.getR9_NON_ACCRUALS3() != null) {
						cell12.setCellValue(record1.getR9_NON_ACCRUALS3().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					
					Cell cell13 = row.createCell(12);
					if (record1.getR9_SPECIFIC_PROV3() != null) {
						cell13.setCellValue(record1.getR9_SPECIFIC_PROV3().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					Cell cell14 = row.createCell(13);
					if (record1.getR9_NO_OF_ACC3() != null) {
						cell14.setCellValue(record1.getR9_NO_OF_ACC3().doubleValue());
						cell14.setCellStyle(numberStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}
					
					Cell cell15 = row.createCell(17);
					if (record.getR9_VALUE_OF_COLLATERAL() != null) {
						cell15.setCellValue(record.getR9_VALUE_OF_COLLATERAL().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(9);
					
					
					cell2 = row.createCell(1);
					if (record.getR10_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR10_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR10_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR10_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR10_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR10_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR10_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR10_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR10_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR10_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR10_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR10_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR10_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR10_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR10_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR10_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR10_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR10_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR10_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR10_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR10_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR10_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR10_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR10_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR10_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR10_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR10_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR10_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);
					
					
					
					cell2 = row.createCell(1);
					if (record.getR11_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR11_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR11_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR11_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR11_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR11_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR11_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR11_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR11_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR11_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR11_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR11_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR11_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR11_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR11_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR11_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR11_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR11_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR11_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR11_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR11_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR11_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR11_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR11_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR11_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR11_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR11_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR11_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					
					
					cell2 = row.createCell(1);
					if (record.getR13_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR13_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR13_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR13_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR13_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR13_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR13_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR13_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR13_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR13_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR13_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR13_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR13_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR13_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR13_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR13_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR13_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR13_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR13_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR13_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR13_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR13_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR13_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR13_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR13_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR13_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR13_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(13);
					
					cell2 = row.createCell(1);
					if (record.getR14_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR14_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR14_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR14_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR14_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR14_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR14_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR14_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR14_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR14_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR14_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR14_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR14_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR14_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR14_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR14_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR14_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR14_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR14_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR14_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR14_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR14_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR14_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR14_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR14_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR14_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR14_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(14);
					
					cell2 = row.createCell(1);
					if (record.getR15_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR15_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR15_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR15_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR15_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR15_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR15_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR15_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR15_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR15_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR15_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR15_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR15_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR15_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR15_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR15_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR15_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR15_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR15_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR15_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR15_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR15_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR15_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR15_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR15_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR15_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR15_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					
					cell2 = row.createCell(1);
					if (record.getR16_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR16_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR16_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR16_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR16_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR16_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR16_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR16_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR16_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR16_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR16_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR16_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR16_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR16_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR16_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR16_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR16_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR16_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR16_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR16_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR16_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR16_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR16_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR16_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR16_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR16_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR16_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(16);
					
					
					cell2 = row.createCell(1);
					if (record.getR17_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR17_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR17_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR17_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR17_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR17_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR17_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR17_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR17_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR17_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR17_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR17_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR17_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR17_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR17_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR17_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR17_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR17_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR17_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR17_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR17_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR17_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR17_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR17_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR17_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}
					cell15 = row.createCell(17);
					if (record.getR17_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR17_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					
					cell2 = row.createCell(1);
					if (record.getR18_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR18_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR18_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR18_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR18_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR18_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR18_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR18_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR18_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR18_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR18_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR18_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR18_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR18_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR18_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR18_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR18_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR18_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR18_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR18_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR18_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR18_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR18_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR18_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR18_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR18_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR18_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR18_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(18);
					
					cell2 = row.createCell(1);
					if (record.getR19_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR19_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR19_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR19_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR19_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR19_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR19_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR19_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR19_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR19_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR19_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR19_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR19_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR19_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR19_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR19_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR19_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR19_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR19_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR19_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR19_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR19_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR19_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR19_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR19_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR19_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR19_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR19_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(19);
					

					// ====================== R20 ======================
					cell2 = row.createCell(1);
					if (record.getR20_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR20_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR20_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR20_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR20_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR20_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR20_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR20_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR20_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR20_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR20_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR20_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR20_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR20_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR20_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR20_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR20_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR20_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR20_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR20_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR20_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR20_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR20_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR20_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR20_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR20_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR20_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR20_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					
					// ====================== R21 ======================
					cell2 = row.createCell(1);
					if (record.getR21_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR21_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR21_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR21_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR21_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR21_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR21_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR21_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR21_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR21_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR21_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR21_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR21_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR21_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR21_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR21_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR21_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR21_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR21_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR21_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR21_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR21_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR21_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR21_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR21_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR21_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR21_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR21_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					
					// ====================== R22 ======================
					

					cell2 = row.createCell(1);
					if (record.getR22_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR22_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR22_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR22_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR22_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR22_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR22_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR22_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR22_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR22_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR22_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR22_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR22_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR22_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR22_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR22_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR22_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR22_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR22_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR22_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR22_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR22_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR22_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR22_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR22_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR22_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR22_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR22_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(22);
					
					
					// ====================== R23 ======================
					

					cell2 = row.createCell(1);
					if (record.getR23_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR23_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR23_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR23_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR23_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR23_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR23_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR23_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR23_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR23_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR23_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR23_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR23_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR23_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR23_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR23_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR23_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR23_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR23_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR23_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR23_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR23_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR23_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR23_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR23_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR23_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR23_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR23_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(23);
					
					// ====================== R24 ======================
					

					cell2 = row.createCell(1);
					if (record.getR24_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR24_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR24_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR24_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR24_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR24_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR24_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR24_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR24_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR24_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR24_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR24_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR24_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR24_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR24_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR24_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR24_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR24_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR24_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR24_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR24_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR24_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR24_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR24_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR24_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR24_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR24_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR24_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(24);
					
					// ====================== R25 ======================
					

					cell2 = row.createCell(1);
					if (record.getR25_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR25_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR25_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR25_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR25_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR25_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR25_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR25_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR25_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR25_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR25_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR25_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR25_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR25_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR25_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR25_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR25_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR25_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR25_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR25_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR25_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR25_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR25_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR25_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR25_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR25_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR25_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR25_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(26);
					
					// ====================== R27 ======================
					

					cell2 = row.createCell(1);
					if (record.getR27_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR27_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR27_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR27_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR27_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR27_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR27_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR27_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR27_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR27_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR27_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR27_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR27_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR27_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR27_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR27_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR27_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR27_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR27_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR27_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR27_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR27_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR27_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR27_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR27_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR27_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR27_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR27_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(27);
					
					
					// ====================== R28 ======================
					

					cell2 = row.createCell(1);
					if (record.getR28_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR28_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR28_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR28_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR28_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR28_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR28_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR28_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR28_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR28_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR28_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR28_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR28_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR28_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR28_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR28_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR28_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR28_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR28_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR28_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR28_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR28_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR28_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR28_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR28_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR28_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR28_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR28_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					
					
					// ====================== R29 ======================
					

					cell2 = row.createCell(1);
					if (record.getR29_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR29_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR29_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR29_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR29_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR29_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR29_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR29_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR29_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR29_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR29_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR29_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR29_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR29_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR29_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR29_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR29_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR29_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR29_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR29_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR29_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR29_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR29_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR29_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR29_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR29_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR29_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR29_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R30 ======================
					
					row = sheet.getRow(29);
					cell2 = row.createCell(1);
					if (record.getR30_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR30_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR30_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR30_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR30_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR30_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR30_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR30_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR30_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR30_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR30_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR30_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR30_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR30_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR30_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR30_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR30_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR30_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR30_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR30_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR30_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR30_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR30_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR30_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR30_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR30_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR30_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR30_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					// ====================== R31 ======================
					

					cell2 = row.createCell(1);
					if (record.getR31_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR31_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR31_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR31_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR31_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR31_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR31_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR31_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR31_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR31_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR31_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR31_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR31_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR31_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR31_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR31_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR31_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR31_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR31_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR31_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR31_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR31_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR31_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR31_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR31_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR31_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR31_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR31_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(31);

					// ====================== R32 ======================
					

					cell2 = row.createCell(1);
					if (record.getR32_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR32_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR32_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR32_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR32_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR32_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR32_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR32_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR32_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR32_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR32_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR32_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR32_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR32_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR32_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR32_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR32_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR32_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR32_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR32_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR32_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR32_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR32_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR32_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR32_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR32_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR32_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR32_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(32);

					// ====================== R33 ======================
					

					cell2 = row.createCell(1);
					if (record.getR33_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR33_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR33_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR33_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR33_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR33_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR33_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR33_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR33_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR33_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR33_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR33_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR33_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR33_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR33_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR33_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR33_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR33_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR33_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR33_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR33_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR33_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR33_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR33_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR33_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR33_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR33_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR33_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(33);

					// ====================== R34 ======================
					

					cell2 = row.createCell(1);
					if (record.getR34_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR34_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR34_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR34_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR34_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR34_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR34_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR34_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR34_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR34_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR34_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR34_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR34_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR34_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR34_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR34_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR34_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR34_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR34_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR34_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR34_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR34_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR34_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR34_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR34_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR34_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR34_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR34_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(35);
					
					// ====================== R36 ======================
					

					cell2 = row.createCell(1);
					if (record.getR36_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR36_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR36_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR36_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR36_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR36_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR36_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR36_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR36_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR36_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR36_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR36_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR36_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR36_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR36_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR36_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR36_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR36_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR36_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR36_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR36_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR36_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR36_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR36_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR36_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR36_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR36_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR36_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(36);

					// ====================== R37 ======================
					

					cell2 = row.createCell(1);
					if (record.getR37_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR37_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR37_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR37_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR37_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR37_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR37_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR37_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR37_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR37_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR37_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR37_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR37_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR37_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR37_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR37_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR37_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR37_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR37_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR37_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR37_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR37_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR37_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR37_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR37_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR37_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR37_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR37_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
				}
				
				
				
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
				
			} else {

			}
			
			startRow = 38;
			if (!dataList1.isEmpty() || !dataList2.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					M_PD_Summary_Entity record1 = dataList1.get(i);
					M_PD_Manual_Summary_Entity record = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);	
					
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
			
					row = sheet.getRow(38);
					
					Cell cell2 = row.createCell(1);
					if (record.getR39_30D_90D_PASTDUE() != null) {
						cell2.setCellValue(record.getR39_30D_90D_PASTDUE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					Cell cell3 = row.createCell(2);
					if (record.getR39_NON_PERFORM_LOANS() != null) {
						cell3.setCellValue(record.getR39_NON_PERFORM_LOANS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					Cell cell4 = row.createCell(3);
					if (record.getR39_NON_ACCRUALS1() != null) {
						cell4.setCellValue(record.getR39_NON_ACCRUALS1().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					Cell cell5 = row.createCell(4);
					if (record.getR39_SPECIFIC_PROV1() != null) {
						cell5.setCellValue(record.getR39_SPECIFIC_PROV1().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					Cell cell6 = row.createCell(5);
					if (record.getR39_NO_OF_ACC1() != null) {
						cell6.setCellValue(record.getR39_NO_OF_ACC1().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					Cell cell7 = row.createCell(6);
					if (record.getR39_90D_180D_PASTDUE() != null) {
						cell7.setCellValue(record.getR39_90D_180D_PASTDUE().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					Cell cell8 = row.createCell(7);
					if (record.getR39_NON_ACCRUALS2() != null) {
						cell8.setCellValue(record.getR39_NON_ACCRUALS2().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					Cell cell9 = row.createCell(8);
					if (record.getR39_SPECIFIC_PROV2() != null) {
						cell9.setCellValue(record.getR39_SPECIFIC_PROV2().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					Cell cell10 = row.createCell(9);
					if (record.getR39_NO_OF_ACC2() != null) {
						cell10.setCellValue(record.getR39_NO_OF_ACC2().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					Cell cell11 = row.createCell(10);
					if (record1.getR39_180D_ABOVE_PASTDUE() != null) {
						cell11.setCellValue(record1.getR39_180D_ABOVE_PASTDUE().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					Cell cell12 = row.createCell(11);
					if (record1.getR39_NON_ACCRUALS3() != null) {
						cell12.setCellValue(record1.getR39_NON_ACCRUALS3().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					
					Cell cell13 = row.createCell(12);
					if (record1.getR39_SPECIFIC_PROV3() != null) {
						cell13.setCellValue(record1.getR39_SPECIFIC_PROV3().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					Cell cell14 = row.createCell(13);
					if (record1.getR39_NO_OF_ACC3() != null) {
						cell14.setCellValue(record1.getR39_NO_OF_ACC3().doubleValue());
						cell14.setCellStyle(numberStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}
					
					Cell cell15 = row.createCell(17);
					if (record.getR39_VALUE_OF_COLLATERAL() != null) {
						cell15.setCellValue(record.getR39_VALUE_OF_COLLATERAL().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(39);
					
					
					// ====================== R40 ======================
					

					cell2 = row.createCell(1);
					if (record.getR40_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR40_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR40_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR40_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR40_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR40_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR40_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR40_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR40_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR40_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR40_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR40_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR40_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR40_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR40_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR40_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR40_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR40_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR40_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR40_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR40_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR40_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR40_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR40_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR40_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR40_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR40_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR40_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					
					
					// ====================== R42 ======================
					row = sheet.getRow(41);

					cell2 = row.createCell(1);
					if (record.getR42_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR42_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR42_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR42_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR42_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR42_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR42_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR42_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR42_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR42_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR42_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR42_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR42_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR42_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR42_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR42_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR42_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR42_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR42_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR42_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR42_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR42_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR42_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR42_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR42_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR42_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR42_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR42_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(42);
					
					
					// ====================== R43 ======================
					

					cell2 = row.createCell(1);
					if (record.getR43_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR43_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR43_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR43_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR43_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR43_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR43_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR43_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR43_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR43_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR43_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR43_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR43_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR43_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR43_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR43_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR43_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR43_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR43_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR43_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR43_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR43_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR43_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR43_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR43_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR43_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}
					cell15 = row.createCell(17);
					if (record.getR43_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR43_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R44 ======================
					row = sheet.getRow(43);

					cell2 = row.createCell(1);
					if (record.getR44_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR44_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR44_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR44_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR44_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR44_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR44_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR44_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR44_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR44_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR44_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR44_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR44_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR44_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR44_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR44_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR44_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR44_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR44_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR44_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR44_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR44_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR44_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR44_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR44_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR44_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR44_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR44_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R45 ======================
					row = sheet.getRow(44);

					cell2 = row.createCell(1);
					if (record.getR45_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR45_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR45_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR45_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR45_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR45_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR45_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR45_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR45_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR45_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR45_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR45_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR45_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR45_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR45_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR45_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR45_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR45_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR45_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR45_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR45_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR45_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR45_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR45_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR45_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR45_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR45_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR45_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(46);
					
					// ====================== R47 ======================
					

					cell2 = row.createCell(1);
					if (record.getR47_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR47_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR47_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR47_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR47_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR47_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR47_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR47_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR47_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR47_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR47_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR47_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR47_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR47_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR47_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR47_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR47_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR47_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR47_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR47_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR47_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR47_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR47_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR47_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR47_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR47_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR47_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR47_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R48 ======================
					row = sheet.getRow(47);

					cell2 = row.createCell(1);
					if (record.getR48_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR48_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR48_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR48_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR48_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR48_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR48_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR48_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR48_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR48_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR48_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR48_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR48_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR48_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR48_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR48_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR48_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR48_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR48_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR48_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR48_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR48_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR48_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR48_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR48_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR48_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR48_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR48_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R49 ======================
					row = sheet.getRow(48);

					cell2 = row.createCell(1);
					if (record.getR49_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR49_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR49_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR49_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR49_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR49_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR49_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR49_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR49_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR49_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR49_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR49_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR49_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR49_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR49_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR49_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR49_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR49_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR49_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR49_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR49_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR49_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR49_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR49_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR49_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR49_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR49_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR49_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(50);
					
					// ====================== R51 ======================
					

					cell2 = row.createCell(1);
					if (record.getR51_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR51_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR51_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR51_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR51_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR51_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR51_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR51_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR51_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR51_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR51_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR51_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR51_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR51_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR51_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR51_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR51_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR51_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR51_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR51_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR51_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR51_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR51_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR51_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR51_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR51_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}
					cell15 = row.createCell(17);
					if (record.getR51_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR51_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					// ====================== R52 ======================
					
					row = sheet.getRow(51);

					cell2 = row.createCell(1);
					if (record.getR52_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR52_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR52_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR52_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR52_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR52_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR52_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR52_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR52_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR52_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR52_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR52_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR52_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR52_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR52_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR52_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR52_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR52_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR52_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR52_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR52_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR52_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR52_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR52_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR52_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR52_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR52_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR52_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R53 ======================
					row = sheet.getRow(52);

					cell2 = row.createCell(1);
					if (record.getR53_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR53_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR53_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR53_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR53_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR53_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR53_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR53_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR53_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR53_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR53_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR53_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR53_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR53_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR53_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR53_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR53_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR53_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR53_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR53_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR53_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR53_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR53_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR53_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR53_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR53_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR53_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR53_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(54);
					
					// ====================== R55 ======================
					

					cell2 = row.createCell(1);
					if (record.getR55_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR55_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR55_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR55_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR55_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR55_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR55_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR55_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR55_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR55_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR55_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR55_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR55_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR55_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR55_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR55_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR55_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR55_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR55_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR55_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR55_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR55_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR55_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR55_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR55_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR55_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}
					cell15 = row.createCell(17);
					if (record.getR55_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR55_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R56 ======================
					row = sheet.getRow(55);

					cell2 = row.createCell(1);
					if (record.getR56_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR56_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR56_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR56_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR56_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR56_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR56_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR56_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR56_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR56_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR56_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR56_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR56_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR56_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR56_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR56_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR56_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR56_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR56_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR56_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR56_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR56_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR56_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR56_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR56_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR56_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR56_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR56_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R57 ======================
					row = sheet.getRow(56);

					cell2 = row.createCell(1);
					if (record.getR57_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR57_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR57_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR57_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR57_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR57_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR57_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR57_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR57_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR57_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR57_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR57_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR57_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR57_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR57_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR57_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR57_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR57_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR57_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR57_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR57_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR57_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR57_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR57_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR57_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR57_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR57_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR57_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R58 ======================
					row = sheet.getRow(57);

					cell2 = row.createCell(1);
					if (record.getR58_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR58_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR58_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR58_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR58_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR58_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR58_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR58_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR58_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR58_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR58_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR58_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR58_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR58_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR58_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR58_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR58_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR58_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR58_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR58_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR58_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR58_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR58_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR58_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR58_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR58_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR58_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR58_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R59 ======================
					row = sheet.getRow(58);

					cell2 = row.createCell(1);
					if (record.getR59_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR59_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR59_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR59_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR59_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR59_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR59_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR59_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR59_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR59_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR59_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR59_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR59_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR59_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR59_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR59_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR59_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR59_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR59_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR59_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR59_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR59_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR59_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR59_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR59_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR59_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR59_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR59_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R60 ======================
					row = sheet.getRow(59);

					cell2 = row.createCell(1);
					if (record.getR60_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR60_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR60_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR60_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR60_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR60_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR60_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR60_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR60_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR60_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR60_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR60_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR60_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR60_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR60_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR60_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR60_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR60_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR60_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR60_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR60_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR60_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR60_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR60_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR60_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR60_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR60_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR60_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
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

	public byte[] BRRS_M_PDDetailExcel(String filename, String fromdate, String todate, String currency,
										   String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for M_PD Details...");
			System.out.println("came to Detail download service");


			if (type.equals("ARCHIVAL") & version != null) {
			byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
			version);
			return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_PDDetail");

			//Common border style
			BorderStyle border = BorderStyle.THIN;

			//Header style (left aligned)
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

			//Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

			//Default data style (left aligned)
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






			//Header row
			String[] headers = {
			"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "PROVISION", "REPORT LABLE", "REPORT ADDL CRITERIA1"
			 ,"REPORT ADDL CRITERIA2", "REPORT ADDL CRITERIA3", "REPORT ADDL CRITERIA4","REPORT_DATE"
			};

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);

			if (i == 3 || i == 4) { // ACCT BALANCE & for PROVISION
			cell.setCellStyle(rightAlignedHeaderStyle);
			} else {
			cell.setCellStyle(headerStyle);
			}

			sheet.setColumnWidth(i, 5000);
			}

			//Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_PD_Detail_Entity> reportData = BRRS_M_PD_Detail_Repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
			int rowIndex = 1;
			for (M_PD_Detail_Entity item : reportData) {
			XSSFRow row = sheet.createRow(rowIndex++);

			row.createCell(0).setCellValue(item.getCustId());
			row.createCell(1).setCellValue(item.getAcctNumber());
			row.createCell(2).setCellValue(item.getAcctName());

			//ACCT BALANCE (right aligned, 3 decimal places)
			Cell balanceCell = row.createCell(3);
			if (item.getAcctBalanceInpula() != null) {
			balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
			} else {
			balanceCell.setCellValue(0);
			}
			balanceCell.setCellStyle(balanceStyle);
			
			//PROVISION(right aligned, 3 decimal places)
			Cell balanceCell1 = row.createCell(4);
			if (item.getProvision() != null) {
			balanceCell1.setCellValue(item.getProvision().doubleValue());
			} else {
			balanceCell1.setCellValue(0);
			}
			balanceCell1.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLable());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7).setCellValue(item.getReportAddlCriteria2());
					row.createCell(8).setCellValue(item.getReportAddlCriteria3());
					row.createCell(9).setCellValue(item.getReportAddlCriteria4());
					row.createCell(10)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

//					// Apply data style for all other cells
//					for (int j = 0; j < 7; j++) {
//						if (j != 3) {
//							row.getCell(j).setCellStyle(dataStyle);
//						}
//					}
//				}
					// Apply border style to all cells in the row
					for (int colIndex = 0; colIndex < headers.length; colIndex++) {
						Cell cell = row.getCell(colIndex);
						if (cell != null) {
							if (colIndex == 3) { // ACCT BALANCE
								cell.setCellStyle(balanceStyle);
							} else if (colIndex == 4) { // APPROVED LIMIT
								cell.setCellStyle(balanceStyle);
							} else {
								cell.setCellStyle(dataStyle);
							}
						}
					}
				}
			} else {
				logger.info("No data found for M_PD — only header will be written.");
			}

			//Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

			} catch (Exception e) {
			logger.error("Error generating M_PD Excel", e);
			return new byte[0];
			}
			}

	public List<Object> getM_PDArchival() {
		List<Object> M_PDArchivallist = new ArrayList<>();
		List<Object> M_PDArchivallist1 = new ArrayList<>();
		try {
			M_PDArchivallist = BRRS_M_PD_Archival_Summary_Repo1.getM_PDarchival();
			M_PDArchivallist1 = BRRS_M_PD_Manual_Archival_Summary_Repo.getM_PDarchival();
			System.out.println("countser" + M_PDArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_PD Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_PDArchivallist;
	}

	
	public byte[] getExcelM_PDARCHIVAL(String filename, String reportId, String fromdate, String todate,
										   String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_PD_Archival_Summary_Entity> dataList1 = BRRS_M_PD_Archival_Summary_Repo1
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_PD_Manual_Archival_Summary_Entity> dataList2 = BRRS_M_PD_Manual_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_PD report. Returning empty result.");
			return new byte[0];
		}

		if (dataList2.isEmpty()) {
			logger.warn("Service: No data found for M_PD report. Returning empty result.");
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

			int startRow = 8;

			if (!dataList1.isEmpty() || !dataList2.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					M_PD_Archival_Summary_Entity record1 = dataList1.get(i);
					M_PD_Manual_Archival_Summary_Entity record = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);	
					
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
			
					
					
					
					Cell cell2 = row.createCell(1);
					if (record.getR9_30D_90D_PASTDUE() != null) {
						cell2.setCellValue(record.getR9_30D_90D_PASTDUE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					Cell cell3 = row.createCell(2);
					if (record.getR9_NON_PERFORM_LOANS() != null) {
						cell3.setCellValue(record.getR9_NON_PERFORM_LOANS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					Cell cell4 = row.createCell(3);
					if (record.getR9_NON_ACCRUALS1() != null) {
						cell4.setCellValue(record.getR9_NON_ACCRUALS1().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					Cell cell5 = row.createCell(4);
					if (record.getR9_SPECIFIC_PROV1() != null) {
						cell5.setCellValue(record.getR9_SPECIFIC_PROV1().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					Cell cell6 = row.createCell(5);
					if (record.getR9_NO_OF_ACC1() != null) {
						cell6.setCellValue(record.getR9_NO_OF_ACC1().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					Cell cell7 = row.createCell(6);
					if (record.getR9_90D_180D_PASTDUE() != null) {
						cell7.setCellValue(record.getR9_90D_180D_PASTDUE().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					Cell cell8 = row.createCell(7);
					if (record.getR9_NON_ACCRUALS2() != null) {
						cell8.setCellValue(record.getR9_NON_ACCRUALS2().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					Cell cell9 = row.createCell(8);
					if (record.getR9_SPECIFIC_PROV2() != null) {
						cell9.setCellValue(record.getR9_SPECIFIC_PROV2().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					Cell cell10 = row.createCell(9);
					if (record.getR9_NO_OF_ACC2() != null) {
						cell10.setCellValue(record.getR9_NO_OF_ACC2().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					Cell cell11 = row.createCell(10);
					if (record1.getR9_180D_ABOVE_PASTDUE() != null) {
						cell11.setCellValue(record1.getR9_180D_ABOVE_PASTDUE().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					Cell cell12 = row.createCell(11);
					if (record1.getR9_NON_ACCRUALS3() != null) {
						cell12.setCellValue(record1.getR9_NON_ACCRUALS3().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					
					Cell cell13 = row.createCell(12);
					if (record1.getR9_SPECIFIC_PROV3() != null) {
						cell13.setCellValue(record1.getR9_SPECIFIC_PROV3().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					Cell cell14 = row.createCell(13);
					if (record1.getR9_NO_OF_ACC3() != null) {
						cell14.setCellValue(record1.getR9_NO_OF_ACC3().doubleValue());
						cell14.setCellStyle(numberStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}
					
					Cell cell15 = row.createCell(17);
					if (record.getR9_VALUE_OF_COLLATERAL() != null) {
						cell15.setCellValue(record.getR9_VALUE_OF_COLLATERAL().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(9);
					
					
					cell2 = row.createCell(1);
					if (record.getR10_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR10_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR10_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR10_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR10_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR10_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR10_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR10_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR10_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR10_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR10_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR10_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR10_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR10_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR10_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR10_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR10_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR10_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR10_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR10_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR10_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR10_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR10_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR10_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR10_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR10_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR10_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR10_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);
					
					
					
					cell2 = row.createCell(1);
					if (record.getR11_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR11_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR11_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR11_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR11_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR11_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR11_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR11_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR11_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR11_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR11_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR11_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR11_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR11_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR11_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR11_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR11_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR11_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR11_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR11_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR11_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR11_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR11_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR11_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR11_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR11_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR11_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR11_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					
					
					cell2 = row.createCell(1);
					if (record.getR13_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR13_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR13_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR13_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR13_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR13_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR13_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR13_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR13_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR13_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR13_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR13_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR13_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR13_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR13_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR13_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR13_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR13_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR13_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR13_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR13_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR13_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR13_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR13_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR13_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR13_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR13_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(13);
					
					cell2 = row.createCell(1);
					if (record.getR14_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR14_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR14_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR14_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR14_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR14_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR14_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR14_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR14_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR14_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR14_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR14_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR14_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR14_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR14_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR14_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR14_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR14_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR14_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR14_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR14_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR14_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR14_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR14_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR14_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR14_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR14_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(14);
					
					cell2 = row.createCell(1);
					if (record.getR15_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR15_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR15_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR15_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR15_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR15_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR15_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR15_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR15_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR15_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR15_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR15_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR15_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR15_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR15_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR15_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR15_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR15_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR15_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR15_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR15_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR15_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR15_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR15_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR15_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR15_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR15_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					
					cell2 = row.createCell(1);
					if (record.getR16_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR16_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR16_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR16_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR16_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR16_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR16_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR16_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR16_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR16_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR16_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR16_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR16_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR16_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR16_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR16_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR16_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR16_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR16_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR16_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR16_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR16_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR16_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR16_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR16_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR16_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR16_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(16);
					
					
					cell2 = row.createCell(1);
					if (record.getR17_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR17_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR17_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR17_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR17_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR17_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR17_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR17_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR17_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR17_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR17_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR17_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR17_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR17_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR17_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR17_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR17_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR17_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR17_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR17_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR17_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR17_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR17_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR17_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR17_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}
					cell15 = row.createCell(17);
					if (record.getR17_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR17_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					
					cell2 = row.createCell(1);
					if (record.getR18_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR18_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR18_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR18_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR18_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR18_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR18_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR18_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR18_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR18_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR18_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR18_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR18_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR18_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR18_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR18_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR18_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR18_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR18_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR18_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR18_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR18_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR18_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR18_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR18_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR18_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR18_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR18_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(18);
					
					cell2 = row.createCell(1);
					if (record.getR19_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR19_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR19_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR19_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR19_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR19_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR19_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR19_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR19_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR19_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR19_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR19_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR19_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR19_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR19_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR19_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR19_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR19_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR19_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR19_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR19_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR19_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR19_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR19_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR19_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR19_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR19_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR19_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(19);
					

					// ====================== R20 ======================
					cell2 = row.createCell(1);
					if (record.getR20_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR20_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR20_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR20_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR20_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR20_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR20_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR20_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR20_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR20_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR20_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR20_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR20_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR20_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR20_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR20_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR20_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR20_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR20_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR20_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR20_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR20_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR20_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR20_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR20_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR20_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR20_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR20_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					
					// ====================== R21 ======================
					cell2 = row.createCell(1);
					if (record.getR21_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR21_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR21_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR21_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR21_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR21_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR21_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR21_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR21_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR21_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR21_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR21_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR21_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR21_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR21_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR21_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR21_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR21_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR21_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR21_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR21_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR21_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR21_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR21_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR21_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR21_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR21_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR21_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					
					// ====================== R22 ======================
					

					cell2 = row.createCell(1);
					if (record.getR22_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR22_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR22_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR22_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR22_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR22_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR22_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR22_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR22_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR22_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR22_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR22_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR22_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR22_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR22_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR22_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR22_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR22_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR22_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR22_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR22_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR22_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR22_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR22_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR22_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR22_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR22_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR22_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(22);
					
					
					// ====================== R23 ======================
					

					cell2 = row.createCell(1);
					if (record.getR23_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR23_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR23_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR23_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR23_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR23_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR23_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR23_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR23_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR23_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR23_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR23_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR23_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR23_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR23_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR23_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR23_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR23_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR23_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR23_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR23_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR23_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR23_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR23_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR23_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR23_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR23_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR23_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(23);
					
					// ====================== R24 ======================
					

					cell2 = row.createCell(1);
					if (record.getR24_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR24_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR24_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR24_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR24_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR24_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR24_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR24_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR24_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR24_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR24_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR24_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR24_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR24_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR24_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR24_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR24_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR24_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR24_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR24_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR24_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR24_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR24_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR24_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR24_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR24_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR24_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR24_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(24);
					
					// ====================== R25 ======================
					

					cell2 = row.createCell(1);
					if (record.getR25_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR25_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR25_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR25_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR25_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR25_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR25_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR25_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR25_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR25_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR25_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR25_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR25_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR25_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR25_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR25_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR25_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR25_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR25_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR25_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR25_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR25_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR25_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR25_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR25_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR25_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR25_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR25_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(26);
					
					// ====================== R27 ======================
					

					cell2 = row.createCell(1);
					if (record.getR27_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR27_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR27_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR27_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR27_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR27_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR27_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR27_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR27_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR27_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR27_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR27_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR27_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR27_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR27_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR27_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR27_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR27_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR27_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR27_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR27_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR27_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR27_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR27_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR27_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR27_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR27_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR27_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(27);
					
					
					// ====================== R28 ======================
					

					cell2 = row.createCell(1);
					if (record.getR28_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR28_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR28_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR28_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR28_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR28_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR28_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR28_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR28_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR28_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR28_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR28_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR28_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR28_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR28_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR28_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR28_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR28_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR28_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR28_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR28_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR28_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR28_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR28_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR28_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR28_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR28_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR28_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					
					
					// ====================== R29 ======================
					

					cell2 = row.createCell(1);
					if (record.getR29_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR29_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR29_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR29_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR29_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR29_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR29_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR29_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR29_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR29_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR29_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR29_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR29_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR29_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR29_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR29_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR29_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR29_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR29_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR29_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR29_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR29_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR29_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR29_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR29_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR29_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR29_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR29_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R30 ======================
					
					row = sheet.getRow(29);
					cell2 = row.createCell(1);
					if (record.getR30_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR30_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR30_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR30_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR30_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR30_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR30_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR30_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR30_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR30_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR30_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR30_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR30_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR30_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR30_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR30_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR30_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR30_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR30_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR30_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR30_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR30_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR30_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR30_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR30_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR30_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR30_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR30_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					// ====================== R31 ======================
					

					cell2 = row.createCell(1);
					if (record.getR31_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR31_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR31_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR31_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR31_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR31_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR31_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR31_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR31_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR31_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR31_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR31_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR31_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR31_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR31_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR31_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR31_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR31_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR31_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR31_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR31_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR31_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR31_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR31_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR31_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR31_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR31_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR31_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(31);

					// ====================== R32 ======================
					

					cell2 = row.createCell(1);
					if (record.getR32_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR32_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR32_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR32_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR32_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR32_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR32_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR32_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR32_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR32_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR32_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR32_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR32_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR32_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR32_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR32_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR32_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR32_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR32_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR32_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR32_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR32_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR32_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR32_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR32_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR32_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR32_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR32_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(32);

					// ====================== R33 ======================
					

					cell2 = row.createCell(1);
					if (record.getR33_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR33_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR33_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR33_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR33_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR33_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR33_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR33_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR33_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR33_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR33_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR33_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR33_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR33_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR33_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR33_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR33_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR33_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR33_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR33_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR33_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR33_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR33_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR33_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR33_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR33_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR33_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR33_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(33);

					// ====================== R34 ======================
					

					cell2 = row.createCell(1);
					if (record.getR34_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR34_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR34_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR34_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR34_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR34_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR34_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR34_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR34_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR34_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR34_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR34_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR34_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR34_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR34_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR34_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR34_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR34_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR34_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR34_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR34_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR34_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR34_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR34_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR34_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR34_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR34_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR34_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(35);
					
					// ====================== R36 ======================
					

					cell2 = row.createCell(1);
					if (record.getR36_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR36_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR36_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR36_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR36_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR36_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR36_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR36_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR36_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR36_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR36_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR36_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR36_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR36_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR36_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR36_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR36_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR36_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR36_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR36_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR36_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR36_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR36_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR36_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR36_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR36_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR36_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR36_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					row = sheet.getRow(36);

					// ====================== R37 ======================
					

					cell2 = row.createCell(1);
					if (record.getR37_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR37_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR37_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR37_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR37_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR37_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR37_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR37_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR37_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR37_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR37_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR37_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR37_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR37_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR37_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR37_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR37_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR37_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR37_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR37_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR37_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR37_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR37_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR37_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR37_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR37_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR37_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR37_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
				}
				
				
				
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
				
			} else {

			}
			
			startRow = 38;
			if (!dataList1.isEmpty() || !dataList2.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					M_PD_Archival_Summary_Entity record1 = dataList1.get(i);
					M_PD_Manual_Archival_Summary_Entity record = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);	
					
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
			
					row = sheet.getRow(38);
					
					Cell cell2 = row.createCell(1);
					if (record.getR39_30D_90D_PASTDUE() != null) {
						cell2.setCellValue(record.getR39_30D_90D_PASTDUE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					Cell cell3 = row.createCell(2);
					if (record.getR39_NON_PERFORM_LOANS() != null) {
						cell3.setCellValue(record.getR39_NON_PERFORM_LOANS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					Cell cell4 = row.createCell(3);
					if (record.getR39_NON_ACCRUALS1() != null) {
						cell4.setCellValue(record.getR39_NON_ACCRUALS1().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					Cell cell5 = row.createCell(4);
					if (record.getR39_SPECIFIC_PROV1() != null) {
						cell5.setCellValue(record.getR39_SPECIFIC_PROV1().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					Cell cell6 = row.createCell(5);
					if (record.getR39_NO_OF_ACC1() != null) {
						cell6.setCellValue(record.getR39_NO_OF_ACC1().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					Cell cell7 = row.createCell(6);
					if (record.getR39_90D_180D_PASTDUE() != null) {
						cell7.setCellValue(record.getR39_90D_180D_PASTDUE().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					Cell cell8 = row.createCell(7);
					if (record.getR39_NON_ACCRUALS2() != null) {
						cell8.setCellValue(record.getR39_NON_ACCRUALS2().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					Cell cell9 = row.createCell(8);
					if (record.getR39_SPECIFIC_PROV2() != null) {
						cell9.setCellValue(record.getR39_SPECIFIC_PROV2().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					Cell cell10 = row.createCell(9);
					if (record.getR39_NO_OF_ACC2() != null) {
						cell10.setCellValue(record.getR39_NO_OF_ACC2().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					Cell cell11 = row.createCell(10);
					if (record1.getR39_180D_ABOVE_PASTDUE() != null) {
						cell11.setCellValue(record1.getR39_180D_ABOVE_PASTDUE().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					Cell cell12 = row.createCell(11);
					if (record1.getR39_NON_ACCRUALS3() != null) {
						cell12.setCellValue(record1.getR39_NON_ACCRUALS3().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					
					Cell cell13 = row.createCell(12);
					if (record1.getR39_SPECIFIC_PROV3() != null) {
						cell13.setCellValue(record1.getR39_SPECIFIC_PROV3().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					Cell cell14 = row.createCell(13);
					if (record1.getR39_NO_OF_ACC3() != null) {
						cell14.setCellValue(record1.getR39_NO_OF_ACC3().doubleValue());
						cell14.setCellStyle(numberStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}
					
					Cell cell15 = row.createCell(17);
					if (record.getR39_VALUE_OF_COLLATERAL() != null) {
						cell15.setCellValue(record.getR39_VALUE_OF_COLLATERAL().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(39);
					
					
					// ====================== R40 ======================
					

					cell2 = row.createCell(1);
					if (record.getR40_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR40_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR40_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR40_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR40_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR40_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR40_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR40_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR40_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR40_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR40_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR40_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR40_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR40_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR40_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR40_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR40_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR40_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR40_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR40_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR40_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR40_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR40_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR40_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR40_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR40_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR40_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR40_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					
					
					// ====================== R42 ======================
					row = sheet.getRow(41);

					cell2 = row.createCell(1);
					if (record.getR42_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR42_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR42_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR42_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR42_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR42_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR42_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR42_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR42_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR42_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR42_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR42_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR42_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR42_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR42_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR42_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR42_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR42_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR42_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR42_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR42_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR42_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR42_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR42_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR42_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR42_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR42_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR42_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(42);
					
					
					// ====================== R43 ======================
					

					cell2 = row.createCell(1);
					if (record.getR43_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR43_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR43_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR43_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR43_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR43_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR43_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR43_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR43_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR43_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR43_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR43_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR43_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR43_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR43_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR43_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR43_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR43_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR43_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR43_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR43_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR43_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR43_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR43_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR43_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR43_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}
					cell15 = row.createCell(17);
					if (record.getR43_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR43_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R44 ======================
					row = sheet.getRow(43);

					cell2 = row.createCell(1);
					if (record.getR44_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR44_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR44_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR44_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR44_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR44_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR44_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR44_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR44_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR44_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR44_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR44_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR44_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR44_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR44_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR44_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR44_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR44_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR44_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR44_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR44_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR44_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR44_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR44_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR44_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR44_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR44_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR44_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R45 ======================
					row = sheet.getRow(44);

					cell2 = row.createCell(1);
					if (record.getR45_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR45_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR45_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR45_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR45_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR45_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR45_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR45_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR45_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR45_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR45_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR45_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR45_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR45_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR45_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR45_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR45_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR45_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR45_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR45_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR45_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR45_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR45_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR45_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR45_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR45_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR45_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR45_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(46);
					
					// ====================== R47 ======================
					

					cell2 = row.createCell(1);
					if (record.getR47_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR47_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR47_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR47_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR47_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR47_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR47_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR47_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR47_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR47_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR47_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR47_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR47_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR47_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR47_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR47_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR47_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR47_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR47_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR47_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR47_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR47_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR47_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR47_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR47_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR47_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR47_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR47_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R48 ======================
					row = sheet.getRow(47);

					cell2 = row.createCell(1);
					if (record.getR48_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR48_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR48_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR48_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR48_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR48_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR48_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR48_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR48_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR48_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR48_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR48_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR48_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR48_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR48_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR48_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR48_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR48_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR48_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR48_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR48_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR48_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR48_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR48_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR48_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR48_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR48_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR48_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R49 ======================
					row = sheet.getRow(48);

					cell2 = row.createCell(1);
					if (record.getR49_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR49_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR49_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR49_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR49_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR49_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR49_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR49_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR49_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR49_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR49_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR49_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR49_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR49_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR49_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR49_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR49_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR49_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR49_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR49_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR49_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR49_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR49_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR49_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR49_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR49_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR49_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR49_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(50);
					
					// ====================== R51 ======================
					

					cell2 = row.createCell(1);
					if (record.getR51_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR51_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR51_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR51_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR51_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR51_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR51_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR51_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR51_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR51_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR51_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR51_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR51_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR51_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR51_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR51_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR51_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR51_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR51_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR51_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR51_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR51_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR51_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR51_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR51_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR51_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}
					cell15 = row.createCell(17);
					if (record.getR51_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR51_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					
					// ====================== R52 ======================
					
					row = sheet.getRow(51);

					cell2 = row.createCell(1);
					if (record.getR52_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR52_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR52_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR52_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR52_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR52_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR52_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR52_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR52_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR52_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR52_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR52_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR52_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR52_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR52_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR52_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR52_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR52_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR52_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR52_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR52_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR52_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR52_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR52_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR52_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR52_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR52_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR52_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R53 ======================
					row = sheet.getRow(52);

					cell2 = row.createCell(1);
					if (record.getR53_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR53_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR53_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR53_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR53_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR53_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR53_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR53_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR53_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR53_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR53_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR53_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR53_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR53_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR53_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR53_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR53_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR53_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR53_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR53_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR53_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR53_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR53_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR53_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR53_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR53_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR53_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR53_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(54);
					
					// ====================== R55 ======================
					

					cell2 = row.createCell(1);
					if (record.getR55_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR55_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR55_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR55_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR55_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR55_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR55_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR55_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR55_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR55_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR55_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR55_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR55_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR55_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR55_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR55_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR55_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR55_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR55_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR55_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR55_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR55_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR55_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR55_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR55_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR55_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}
					cell15 = row.createCell(17);
					if (record.getR55_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR55_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R56 ======================
					row = sheet.getRow(55);

					cell2 = row.createCell(1);
					if (record.getR56_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR56_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR56_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR56_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR56_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR56_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR56_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR56_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR56_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR56_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR56_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR56_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR56_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR56_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR56_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR56_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR56_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR56_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR56_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR56_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR56_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR56_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR56_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR56_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR56_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR56_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR56_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR56_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R57 ======================
					row = sheet.getRow(56);

					cell2 = row.createCell(1);
					if (record.getR57_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR57_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR57_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR57_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR57_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR57_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR57_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR57_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR57_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR57_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR57_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR57_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR57_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR57_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR57_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR57_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR57_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR57_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR57_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR57_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR57_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR57_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR57_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR57_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR57_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR57_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR57_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR57_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R58 ======================
					row = sheet.getRow(57);

					cell2 = row.createCell(1);
					if (record.getR58_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR58_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR58_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR58_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR58_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR58_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR58_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR58_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR58_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR58_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR58_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR58_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR58_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR58_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR58_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR58_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR58_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR58_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					cell11 = row.createCell(10);
					if (record1.getR58_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR58_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR58_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR58_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR58_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR58_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR58_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR58_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR58_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR58_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R59 ======================
					row = sheet.getRow(58);

					cell2 = row.createCell(1);
					if (record.getR59_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR59_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR59_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR59_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR59_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR59_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR59_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR59_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR59_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR59_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR59_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR59_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR59_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR59_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR59_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR59_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR59_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR59_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR59_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR59_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR59_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR59_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR59_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR59_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR59_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR59_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR59_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR59_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// ====================== R60 ======================
					row = sheet.getRow(59);

					cell2 = row.createCell(1);
					if (record.getR60_30D_90D_PASTDUE() != null) {
					    cell2.setCellValue(record.getR60_30D_90D_PASTDUE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(2);
					if (record.getR60_NON_PERFORM_LOANS() != null) {
					    cell3.setCellValue(record.getR60_NON_PERFORM_LOANS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(3);
					if (record.getR60_NON_ACCRUALS1() != null) {
					    cell4.setCellValue(record.getR60_NON_ACCRUALS1().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(4);
					if (record.getR60_SPECIFIC_PROV1() != null) {
					    cell5.setCellValue(record.getR60_SPECIFIC_PROV1().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(5);
					if (record.getR60_NO_OF_ACC1() != null) {
					    cell6.setCellValue(record.getR60_NO_OF_ACC1().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(6);
					if (record.getR60_90D_180D_PASTDUE() != null) {
					    cell7.setCellValue(record.getR60_90D_180D_PASTDUE().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(7);
					if (record.getR60_NON_ACCRUALS2() != null) {
					    cell8.setCellValue(record.getR60_NON_ACCRUALS2().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(8);
					if (record.getR60_SPECIFIC_PROV2() != null) {
					    cell9.setCellValue(record.getR60_SPECIFIC_PROV2().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(9);
					if (record.getR60_NO_OF_ACC2() != null) {
					    cell10.setCellValue(record.getR60_NO_OF_ACC2().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(10);
					if (record1.getR60_180D_ABOVE_PASTDUE() != null) {
					    cell11.setCellValue(record1.getR60_180D_ABOVE_PASTDUE().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					cell12 = row.createCell(11);
					if (record1.getR60_NON_ACCRUALS3() != null) {
					    cell12.setCellValue(record1.getR60_NON_ACCRUALS3().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					cell13 = row.createCell(12);
					if (record1.getR60_SPECIFIC_PROV3() != null) {
					    cell13.setCellValue(record1.getR60_SPECIFIC_PROV3().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					cell14 = row.createCell(13);
					if (record1.getR60_NO_OF_ACC3() != null) {
					    cell14.setCellValue(record1.getR60_NO_OF_ACC3().doubleValue());
					    cell14.setCellStyle(numberStyle);
					} else {
					    cell14.setCellValue("");
					    cell14.setCellStyle(textStyle);
					}

					cell15 = row.createCell(17);
					if (record.getR60_VALUE_OF_COLLATERAL() != null) {
					    cell15.setCellValue(record.getR60_VALUE_OF_COLLATERAL().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
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
logger.info("Generating Excel for BRRS_M_PD ARCHIVAL Details...");
System.out.println("came to Detail download service");
if (type.equals("ARCHIVAL") & version != null) {

}
XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("M_PDDetail");

//Common border style
BorderStyle border = BorderStyle.THIN;

//Header style (left aligned)
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

//Right-aligned header style for ACCT BALANCE
CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

//Default data style (left aligned)
CellStyle dataStyle = workbook.createCellStyle();
dataStyle.setAlignment(HorizontalAlignment.LEFT);
dataStyle.setBorderTop(border);
dataStyle.setBorderBottom(border);
dataStyle.setBorderLeft(border);
dataStyle.setBorderRight(border);

//ACCT BALANCE style (right aligned with 3 decimals)
CellStyle balanceStyle = workbook.createCellStyle();
balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
balanceStyle.setBorderTop(border);
balanceStyle.setBorderBottom(border);
balanceStyle.setBorderLeft(border);
balanceStyle.setBorderRight(border);


//Header row
String[] headers = {
		"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "PROVISION", "REPORT LABLE", "REPORT ADDL CRITERIA1"
		 ,"REPORT ADDL CRITERIA2", "REPORT ADDL CRITERIA3", "REPORT ADDL CRITERIA4","REPORT_DATE"
};

XSSFRow headerRow = sheet.createRow(0);
for (int i = 0; i < headers.length; i++) {
Cell cell = headerRow.createCell(i);
cell.setCellValue(headers[i]);

if (i == 3 || i == 4) { // ACCT BALANCE
cell.setCellStyle(rightAlignedHeaderStyle);
} else {
cell.setCellStyle(headerStyle);
}

sheet.setColumnWidth(i, 5000);
}

//Get data
Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
List<M_PD_Archival_Detail_Entity> reportData = BRRS_M_PD_Archival_Detail_Repo.getdatabydateList(parsedToDate,version);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (M_PD_Archival_Detail_Entity item : reportData) {
XSSFRow row = sheet.createRow(rowIndex++);

row.createCell(0).setCellValue(item.getCustId());
row.createCell(1).setCellValue(item.getAcctNumber());
row.createCell(2).setCellValue(item.getAcctName());

//ACCT BALANCE (right aligned, 3 decimal places)
Cell balanceCell = row.createCell(3);
if (item.getAcctBalanceInpula() != null) {
balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
} else {
balanceCell.setCellValue(0);
}
balanceCell.setCellStyle(balanceStyle);

//PROVISION(right aligned, 3 decimal places)
Cell balanceCell1 = row.createCell(4);
if (item.getProvision() != null) {
balanceCell1.setCellValue(item.getProvision().doubleValue());
} else {
balanceCell1.setCellValue(0);
}
balanceCell1.setCellStyle(balanceStyle);

		row.createCell(5).setCellValue(item.getReportLable());
		row.createCell(6).setCellValue(item.getReportAddlCriteria1());
		row.createCell(7).setCellValue(item.getReportAddlCriteria2());
		row.createCell(8).setCellValue(item.getReportAddlCriteria3());
		row.createCell(9).setCellValue(item.getReportAddlCriteria4());
		row.createCell(10)
				.setCellValue(item.getReportDate() != null
						? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
						: "");

//		// Apply data style for all other cells
//		for (int j = 0; j < 7; j++) {
//			if (j != 3) {
//				row.getCell(j).setCellStyle(dataStyle);
//			}
//		}
//	}
		// Apply border style to all cells in the row
		for (int colIndex = 0; colIndex < headers.length; colIndex++) {
			Cell cell = row.getCell(colIndex);
			if (cell != null) {
				if (colIndex == 3) { // ACCT BALANCE
					cell.setCellStyle(balanceStyle);
				} else if (colIndex == 4) { // APPROVED LIMIT
					cell.setCellStyle(balanceStyle);
				} else {
					cell.setCellStyle(dataStyle);
				}
			}
		}
	}
}  else {
logger.info("No data found for M_PD — only header will be written.");
}
//Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating M_PD Excel", e);
return new byte[0];
}
}


	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_PD"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	        M_PD_Detail_Entity PDEntity = BRRS_M_PD_Detail_Repo.findByAcctnumber(acctNo);
	        if (PDEntity != null && PDEntity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(PDEntity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	        }
	        mv.addObject("Data", PDEntity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}
	




	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_PD"); // ✅ match the report name

	    if (acctNo != null) {
	        M_PD_Detail_Entity PDEntity = BRRS_M_PD_Detail_Repo.findByAcctnumber(acctNo);
	        if (PDEntity != null && PDEntity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(PDEntity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	            System.out.println(formattedDate);
	        }
	        mv.addObject("Data", PDEntity);
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
	        String provision = request.getParameter("provision");
	        String acctName = request.getParameter("acctName");
	        String reportDateStr = request.getParameter("reportDate");

	        logger.info("Received update for ACCT_NO: {}", acctNo);

	        M_PD_Detail_Entity existing = BRRS_M_PD_Detail_Repo.findByAcctnumber(acctNo);
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
	                logger.info("Provision updated to {}", newProvision);
	            }
	        }
	        
	        if (provision != null && !provision.isEmpty()) {
	            BigDecimal newSanctionLimit = new BigDecimal(provision);
	            if (existing.getProvision() == null ||
	                existing.getProvision().compareTo(newSanctionLimit) != 0) {
	                existing.setProvision(newSanctionLimit);
	                isChanged = true;
	                logger.info("Sanction limit updated to {}", newSanctionLimit);
	            }
	        }

	        if (isChanged) {
	            BRRS_M_PD_Detail_Repo.save(existing);
	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_M_PD_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_M_PD_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
	        logger.error("Error updating M_PD record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}

}
