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
// import java.util.ArrayList;  // SHOW WARNING HERE
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

// import javax.servlet.http.HttpServletRequest; // SHOW WARNING HERE
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_OR2_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_OR2_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_OR2_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_OR2_RESUB_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_OR2_RESUB_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_OR2_Summary_Repo;
import com.bornfire.brrs.entities.M_OR2_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_OR2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_OR2_Detail_Entity;
import com.bornfire.brrs.entities.M_OR2_RESUB_Detail_Entity;
import com.bornfire.brrs.entities.M_OR2_RESUB_Summary_Entity;
import com.bornfire.brrs.entities.M_OR2_Summary_Entity;


@Component
@Service
public class BRRS_M_OR2_ReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_OR2_ReportService.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	SessionFactory sessionFactory;
	
	
	@Autowired
	BRRS_M_OR2_Summary_Repo M_OR2_Summary_Repo; 
	
	@Autowired
	BRRS_M_OR2_Archival_Summary_Repo M_OR2_Archival_Summary_Repo; 
	
	@Autowired
	BRRS_M_OR2_Detail_Repo M_OR2_Detail_Repo; 
	
	@Autowired
	BRRS_M_OR2_Archival_Detail_Repo  M_OR2_Archival_Detail_Repo;
	
	@Autowired
	BRRS_M_OR2_RESUB_Summary_Repo M_OR2_resub_summary_repo;
	
    @Autowired
	BRRS_M_OR2_RESUB_Detail_Repo M_OR2_resub_detail_repo;
	

	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_OR2View(
	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    int pageSize = pageable.getPageSize();
	    int currentPage = pageable.getPageNumber();
	    int startItem = currentPage * pageSize;

	    try {

	        // Parse only once
	        Date d1 = dateformat.parse(todate);

	        System.out.println("======= VIEW DEBUG =======");
	        System.out.println("TYPE      : " + type);
	        System.out.println("DTLTYPE   : " + dtltype);
	        System.out.println("DATE      : " + d1);
	        System.out.println("VERSION   : " + version);
	        System.out.println("==========================");

	        // ===========================================================
	        //SUMMARY SECTION
	        // ===========================================================

	        // ---------- ARCHIVAL SUMMARY ----------
	        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

	            List<M_OR2_Archival_Summary_Entity> T1Master =
	                    M_OR2_Archival_Summary_Repo
	                            .getdatabydateListarchival(d1, version);

	            System.out.println("Archival Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- RESUB SUMMARY ----------
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	            List<M_OR2_RESUB_Summary_Entity> T1Master =
	                    M_OR2_resub_summary_repo
	                            .getdatabydateListarchival(d1, version);

	            System.out.println("Resub Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "resub");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- NORMAL SUMMARY ----------
	        else {

	            List<M_OR2_Summary_Entity> T1Master =
	                    M_OR2_Summary_Repo
	                            .getdatabydateList(d1);

	            System.out.println("Normal Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ===========================================================
	        // DETAIL SECTION
	        // ===========================================================

	        if ("detail".equalsIgnoreCase(dtltype)) {

	            // ---------- ARCHIVAL DETAIL ----------
	            if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

	                List<M_OR2_Archival_Detail_Entity> T1Master =
	                        M_OR2_Archival_Detail_Repo
	                                .getdatabydateListarchival(d1, version);

	                System.out.println("Archival Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- RESUB DETAIL -------
	            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	                List<M_OR2_RESUB_Detail_Entity> T1Master =
	                        M_OR2_resub_detail_repo
	                                .getdatabydateListarchival(d1, version);

	                System.out.println("Resub Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "resub");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- NORMAL DETAIL ----------
	            else {

	            	 List<M_OR2_Detail_Entity> T1Master =
		                        M_OR2_Detail_Repo
		                                .getdatabydateList(d1);

	                System.out.println("Normal Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }
	        }

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_OR2");

	    System.out.println("View set to: " + mv.getViewName());

	    return mv;
	}
	
	
	
	
	
	public List<Object[]> getM_OR2Resub() {
	    List<Object[]> resubList = new ArrayList<>();
	    try {
	        List<M_OR2_Archival_Summary_Entity> latestArchivalList =
	        		M_OR2_Archival_Summary_Repo.getdatabydateListWithVersion();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (M_OR2_Archival_Summary_Entity entity : latestArchivalList) {
	                resubList.add(new Object[] {
	                    entity.getReport_date(),
	                    entity.getReport_version(),
	                    entity.getReportResubDate()
	                });
	            }
	            System.out.println("Fetched " + resubList.size() + " record(s)");
	        } else {
	            System.out.println("No archival data found.");
	        }

	    } catch (Exception e) {
	        System.err.println("Error fetching M_OR2 Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return resubList;
	}

	
	@Transactional
	public void updateReport(M_OR2_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // 1️⃣ Fetch existing SUMMARY
	     M_OR2_Summary_Entity existingSummary =
	            M_OR2_Summary_Repo.findById(updatedEntity.getReportDate())
	                    .orElseThrow(() -> new RuntimeException(
	                            "Summary record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    // 2️⃣ Fetch or create DETAIL
	      M_OR2_Detail_Entity existingDetail =
	            M_OR2_Detail_Repo.findById(updatedEntity.getReportDate())
	                    .orElseGet(() -> {
	                          M_OR2_Detail_Entity d = new   M_OR2_Detail_Entity();
	                        d.setReportDate(updatedEntity.getReportDate());
	                        return d;
	                    });

	  try {
	        // 1️⃣ Loop from R12 to R65 and copy fields
	    	
	        for (int i = 12; i <= 65; i++) {
				
	        	 String prefix = "R" + i + "_";

	            String[] fields = { "corporate_finance",
	            		"trading_and_sales",
	            		"retail_banking",
	            		"commercial_banking",
	            		"payments_and_settlements",
	            		"agency_services",
	            		"asset_management",
	            		"retail_brokerage" };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter =
	                              M_OR2_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter =
	                              M_OR2_Summary_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Method detailSetter =
	                              M_OR2_Detail_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);

	                    // ✅ set into SUMMARY
	                    summarySetter.invoke(existingSummary, newValue);

	                    // ✅ set into DETAIL
	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    // skip missing fields safely
	                    continue;
	                }
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save BOTH (same transaction)
	    M_OR2_Summary_Repo.save(existingSummary);
	    M_OR2_Detail_Repo.save(existingDetail);
		
		}
	
	@Transactional
    public void updateResubReport(M_OR2_RESUB_Summary_Entity updatedEntity) {

        Date reportDate = updatedEntity.getReport_date();

        // ----------------------------------------------------
        // GET CURRENT VERSION FROM RESUB TABLE
        // ----------------------------------------------------

        BigDecimal maxResubVer =
            M_OR2_resub_summary_repo.findMaxVersion(reportDate);

        if (maxResubVer == null)
            throw new RuntimeException("No record for: " + reportDate);

        BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

        Date now = new Date();

        // ====================================================
        // 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
        // ====================================================

        M_OR2_RESUB_Summary_Entity resubSummary =
            new M_OR2_RESUB_Summary_Entity();

        BeanUtils.copyProperties(updatedEntity, resubSummary,
            "reportDate", "reportVersion", "reportResubDate");

        resubSummary.setReport_date(reportDate);
        resubSummary.setReport_version(newVersion);
        resubSummary.setReportResubDate(now);

        // ====================================================
        // 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
        // ====================================================

        M_OR2_RESUB_Detail_Entity resubDetail =
            new M_OR2_RESUB_Detail_Entity();

        BeanUtils.copyProperties(updatedEntity, resubDetail,
            "reportDate", "reportVersion", "reportResubDate");

        resubDetail.setReport_date(reportDate);
        resubDetail.setReport_version(newVersion);
        resubDetail.setReportResubDate(now);

        // ====================================================
        // 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
        // ====================================================

        M_OR2_Archival_Summary_Entity archSummary =
            new M_OR2_Archival_Summary_Entity();

        BeanUtils.copyProperties(updatedEntity, archSummary,
            "reportDate", "reportVersion", "reportResubDate");

        archSummary.setReport_date(reportDate);
        archSummary.setReport_version(newVersion);   // SAME VERSION
        archSummary.setReportResubDate(now);

        // ====================================================
        // 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
        // ====================================================

        M_OR2_Archival_Detail_Entity archDetail =
            new M_OR2_Archival_Detail_Entity();

        BeanUtils.copyProperties(updatedEntity, archDetail,
            "reportDate", "reportVersion", "reportResubDate");

        archDetail.setReport_date(reportDate);
        archDetail.setReport_version(newVersion);    // SAME VERSION
        archDetail.setReportResubDate(now);

        // ====================================================
        // 6️⃣ SAVE ALL WITH SAME DATA
        // ====================================================

        M_OR2_resub_summary_repo.save(resubSummary);
        M_OR2_resub_detail_repo.save(resubDetail);

        M_OR2_Archival_Summary_Repo.save(archSummary);
        M_OR2_Archival_Detail_Repo.save(archDetail);
    }
	
	
	//Archival View
public List<Object[]> getM_OR2Archival() {
	List<Object[]> archivalList = new ArrayList<>();

	try {
		List<M_OR2_Archival_Summary_Entity> repoData = M_OR2_Archival_Summary_Repo
				.getdatabydateListWithVersion();

		if (repoData != null && !repoData.isEmpty()) {
			for (M_OR2_Archival_Summary_Entity entity : repoData) {
				Object[] row = new Object[] {
						entity.getReport_date(), 
						entity.getReport_version(),
	                    entity.getReportResubDate()
				};
				archivalList.add(row);
			}

			System.out.println("Fetched " + archivalList.size() + " archival records");
			M_OR2_Archival_Summary_Entity first = repoData.get(0);
			System.out.println("Latest archival version: " + first.getReport_version());
		} else {
			System.out.println("No archival data found.");
		}

	} catch (Exception e) {
		System.err.println("Error fetching  M_OR2  Archival data: " + e.getMessage());
		e.printStackTrace();
	}

	return archivalList;
}

	
	
public byte[] getM_OR2Excel(String filename, String reportId, String fromdate, String todate, String currency,
		String dtltype, String type, String format, BigDecimal version) throws Exception {
	logger.info("Service: Starting Excel generation process in memory.");

	System.out.println("======= VIEW SCREEN =======");
	System.out.println("TYPE      : " + type);
	System.out.println("FORMAT      : " + format);
	System.out.println("DTLTYPE   : " + dtltype);
	System.out.println("DATE      : " + dateformat.parse(todate));
	System.out.println("VERSION   : " + version);
	System.out.println("==========================");
	
	
	// ARCHIVAL check
	if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
		try {
			// Redirecting to Archival
			return getExcelM_OR2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
		} catch (ParseException e) {
			logger.error("Invalid report date format: {}", fromdate, e);
			throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
		}
	} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
		logger.info("Service: Generating RESUB report for version {}", version);

		try {
			// ✅ Redirecting to Resub Excel
			return BRRS_M_OR2ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

		} catch (ParseException e) {
			logger.error("Invalid report date format: {}", fromdate, e);
			throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
		}
	} else {

		if ("email".equalsIgnoreCase(format) && version == null) {
			logger.info("Got format as Email");
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_M_OR2EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		} else {

			// Fetch data

			List<M_OR2_Summary_Entity> dataList = M_OR2_Summary_Repo
					.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_OR2 report. Returning empty result.");
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
				throw new SecurityException("Template file exists but is not readable (check permissions): "
						+ templatePath.toAbsolutePath());
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
						M_OR2_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

	//row12
				// Column D 
				Cell cell4 = row.createCell(3);
				if (record.getR12_corporate_finance() != null) {
					cell4.setCellValue(record.getR12_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row12
				// Column E 
				Cell cell5 = row.createCell(4);
				if (record.getR12_trading_and_sales() != null) {
					cell5.setCellValue(record.getR12_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				} 
				
				
				//row12
				// Column F
				Cell cell6 = row.createCell(5);
				if (record.getR12_retail_banking() != null) {
					cell6.setCellValue(record.getR12_retail_banking().doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				//row12
				// Column G
				Cell cell7 = row.createCell(6);
				if (record.getR12_commercial_banking()  != null) {
					cell7.setCellValue(record.getR12_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
				//row12
				// Column H
				Cell cell8 = row.createCell(7);
				if (record.getR12_payments_and_settlements()  != null) {
					cell8.setCellValue(record.getR12_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				//row12
				// Column I
				Cell cell9 = row.createCell(8);
				if (record.getR12_agency_services() != null) {
					cell9.setCellValue(record.getR12_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				//row12
				// Column J
				Cell cell10 = row.createCell(9);
				if (record.getR12_asset_management()  != null) {
					cell10.setCellValue(record.getR12_asset_management() .doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
				//row12
				// Column K
				Cell cell11 = row.createCell(10);
				if (record.getR12_retail_brokerage() != null) {
					cell11.setCellValue(record.getR12_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				
				//row13
				row = sheet.getRow(12);
				
				// Column D
				 cell4 = row.createCell(3);
				if (record.getR13_corporate_finance() != null) {
					cell4.setCellValue(record.getR13_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR13_trading_and_sales() != null) {
					cell5.setCellValue(record.getR13_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR13_retail_banking()  != null) {
					cell6.setCellValue(record.getR13_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR13_commercial_banking() != null) {
					cell7.setCellValue(record.getR13_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR13_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR13_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR13_agency_services() != null) {
					cell9.setCellValue(record.getR13_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR13_asset_management() != null) {
					cell10.setCellValue(record.getR13_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR13_retail_brokerage() != null) {
					cell11.setCellValue(record.getR13_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				
				
				
				//row14
				row = sheet.getRow(13);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR14_corporate_finance() != null) {
					cell4.setCellValue(record.getR14_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR14_trading_and_sales() != null) {
					cell5.setCellValue(record.getR14_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR14_retail_banking()  != null) {
					cell6.setCellValue(record.getR14_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR14_commercial_banking() != null) {
					cell7.setCellValue(record.getR14_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR14_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR14_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR14_agency_services() != null) {
					cell9.setCellValue(record.getR14_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR14_asset_management() != null) {
					cell10.setCellValue(record.getR14_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR14_retail_brokerage() != null) {
					cell11.setCellValue(record.getR14_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row15
				row = sheet.getRow(14);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR15_corporate_finance() != null) {
					cell4.setCellValue(record.getR15_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR15_trading_and_sales() != null) {
					cell5.setCellValue(record.getR15_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR15_retail_banking()  != null) {
					cell6.setCellValue(record.getR15_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR15_commercial_banking() != null) {
					cell7.setCellValue(record.getR15_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR15_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR15_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR15_agency_services() != null) {
					cell9.setCellValue(record.getR15_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR15_asset_management() != null) {
					cell10.setCellValue(record.getR15_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR15_retail_brokerage() != null) {
					cell11.setCellValue(record.getR15_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row16
				row = sheet.getRow(15);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR16_corporate_finance() != null) {
					cell4.setCellValue(record.getR16_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR16_trading_and_sales() != null) {
					cell5.setCellValue(record.getR16_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR16_retail_banking()  != null) {
					cell6.setCellValue(record.getR16_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR16_commercial_banking() != null) {
					cell7.setCellValue(record.getR16_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR16_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR16_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR16_agency_services() != null) {
					cell9.setCellValue(record.getR16_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR16_asset_management() != null) {
					cell10.setCellValue(record.getR16_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR16_retail_brokerage() != null) {
					cell11.setCellValue(record.getR16_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row17
				row = sheet.getRow(16);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR17_corporate_finance() != null) {
					cell4.setCellValue(record.getR17_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR17_trading_and_sales() != null) {
					cell5.setCellValue(record.getR17_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR17_retail_banking()  != null) {
					cell6.setCellValue(record.getR17_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR17_commercial_banking() != null) {
					cell7.setCellValue(record.getR17_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR17_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR17_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR17_agency_services() != null) {
					cell9.setCellValue(record.getR17_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR17_asset_management() != null) {
					cell10.setCellValue(record.getR17_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR17_retail_brokerage() != null) {
					cell11.setCellValue(record.getR17_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				
				//row18
				row = sheet.getRow(17);
				
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR18_corporate_finance() != null) {
					cell4.setCellValue(record.getR18_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR18_trading_and_sales() != null) {
					cell5.setCellValue(record.getR18_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR18_retail_banking()  != null) {
					cell6.setCellValue(record.getR18_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR18_commercial_banking() != null) {
					cell7.setCellValue(record.getR18_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR18_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR18_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR18_agency_services() != null) {
					cell9.setCellValue(record.getR18_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR18_asset_management() != null) {
					cell10.setCellValue(record.getR18_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR18_retail_brokerage() != null) {
					cell11.setCellValue(record.getR18_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row19
				row = sheet.getRow(18);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR19_corporate_finance() != null) {
					cell4.setCellValue(record.getR19_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR19_trading_and_sales() != null) {
					cell5.setCellValue(record.getR19_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR19_retail_banking()  != null) {
					cell6.setCellValue(record.getR19_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR19_commercial_banking() != null) {
					cell7.setCellValue(record.getR19_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR19_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR19_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR19_agency_services() != null) {
					cell9.setCellValue(record.getR19_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR19_asset_management() != null) {
					cell10.setCellValue(record.getR19_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR19_retail_brokerage() != null) {
					cell11.setCellValue(record.getR19_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row20
				row = sheet.getRow(19);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR20_corporate_finance() != null) {
					cell4.setCellValue(record.getR20_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR20_trading_and_sales() != null) {
					cell5.setCellValue(record.getR20_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR20_retail_banking()  != null) {
					cell6.setCellValue(record.getR20_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR20_commercial_banking() != null) {
					cell7.setCellValue(record.getR20_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR20_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR20_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR20_agency_services() != null) {
					cell9.setCellValue(record.getR20_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR20_asset_management() != null) {
					cell10.setCellValue(record.getR20_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR20_retail_brokerage() != null) {
					cell11.setCellValue(record.getR20_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				
				//row21
				row = sheet.getRow(20);
				
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR21_corporate_finance() != null) {
					cell4.setCellValue(record.getR21_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR21_trading_and_sales() != null) {
					cell5.setCellValue(record.getR21_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR21_retail_banking()  != null) {
					cell6.setCellValue(record.getR21_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR21_commercial_banking() != null) {
					cell7.setCellValue(record.getR21_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR21_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR21_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR21_agency_services() != null) {
					cell9.setCellValue(record.getR21_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR21_asset_management() != null) {
					cell10.setCellValue(record.getR21_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR21_retail_brokerage() != null) {
					cell11.setCellValue(record.getR21_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				
				//row22
				row = sheet.getRow(21);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR22_corporate_finance() != null) {
					cell4.setCellValue(record.getR22_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR22_trading_and_sales() != null) {
					cell5.setCellValue(record.getR22_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR22_retail_banking()  != null) {
					cell6.setCellValue(record.getR22_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR22_commercial_banking() != null) {
					cell7.setCellValue(record.getR22_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR22_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR22_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR22_agency_services() != null) {
					cell9.setCellValue(record.getR22_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR22_asset_management() != null) {
					cell10.setCellValue(record.getR22_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR22_retail_brokerage() != null) {
					cell11.setCellValue(record.getR22_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row23
				row = sheet.getRow(22);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR23_corporate_finance() != null) {
					cell4.setCellValue(record.getR23_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR23_trading_and_sales() != null) {
					cell5.setCellValue(record.getR23_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR23_retail_banking()  != null) {
					cell6.setCellValue(record.getR23_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR23_commercial_banking() != null) {
					cell7.setCellValue(record.getR23_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR23_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR23_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR23_agency_services() != null) {
					cell9.setCellValue(record.getR23_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR23_asset_management() != null) {
					cell10.setCellValue(record.getR23_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR23_retail_brokerage() != null) {
					cell11.setCellValue(record.getR23_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				
				//row28
				row = sheet.getRow(27);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR28_corporate_finance() != null) {
					cell4.setCellValue(record.getR28_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR28_trading_and_sales() != null) {
					cell5.setCellValue(record.getR28_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR28_retail_banking()  != null) {
					cell6.setCellValue(record.getR28_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR28_commercial_banking() != null) {
					cell7.setCellValue(record.getR28_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR28_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR28_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR28_agency_services() != null) {
					cell9.setCellValue(record.getR28_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR28_asset_management() != null) {
					cell10.setCellValue(record.getR28_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR28_retail_brokerage() != null) {
					cell11.setCellValue(record.getR28_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				
				//row29
				row = sheet.getRow(28);
				
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR29_corporate_finance() != null) {
					cell4.setCellValue(record.getR29_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR29_trading_and_sales() != null) {
					cell5.setCellValue(record.getR29_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR29_retail_banking()  != null) {
					cell6.setCellValue(record.getR29_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR29_commercial_banking() != null) {
					cell7.setCellValue(record.getR29_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR29_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR29_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR29_agency_services() != null) {
					cell9.setCellValue(record.getR29_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR29_asset_management() != null) {
					cell10.setCellValue(record.getR29_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR29_retail_brokerage() != null) {
					cell11.setCellValue(record.getR29_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row30
				row = sheet.getRow(29);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR30_corporate_finance() != null) {
					cell4.setCellValue(record.getR30_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR30_trading_and_sales() != null) {
					cell5.setCellValue(record.getR30_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR30_retail_banking()  != null) {
					cell6.setCellValue(record.getR30_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR30_commercial_banking() != null) {
					cell7.setCellValue(record.getR30_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR30_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR30_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR30_agency_services() != null) {
					cell9.setCellValue(record.getR30_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR30_asset_management() != null) {
					cell10.setCellValue(record.getR30_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR30_retail_brokerage() != null) {
					cell11.setCellValue(record.getR30_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row31
				row = sheet.getRow(30);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR31_corporate_finance() != null) {
					cell4.setCellValue(record.getR31_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR31_trading_and_sales() != null) {
					cell5.setCellValue(record.getR31_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR31_retail_banking()  != null) {
					cell6.setCellValue(record.getR31_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR31_commercial_banking() != null) {
					cell7.setCellValue(record.getR31_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR31_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR31_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR31_agency_services() != null) {
					cell9.setCellValue(record.getR31_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR31_asset_management() != null) {
					cell10.setCellValue(record.getR31_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR31_retail_brokerage() != null) {
					cell11.setCellValue(record.getR31_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				
				//row32
				row = sheet.getRow(31);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR32_corporate_finance() != null) {
					cell4.setCellValue(record.getR32_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR32_trading_and_sales() != null) {
					cell5.setCellValue(record.getR32_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR32_retail_banking()  != null) {
					cell6.setCellValue(record.getR32_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR32_commercial_banking() != null) {
					cell7.setCellValue(record.getR32_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR32_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR32_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR32_agency_services() != null) {
					cell9.setCellValue(record.getR32_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR32_asset_management() != null) {
					cell10.setCellValue(record.getR32_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR32_retail_brokerage() != null) {
					cell11.setCellValue(record.getR32_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row33
				row = sheet.getRow(32);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR33_corporate_finance() != null) {
					cell4.setCellValue(record.getR33_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR33_trading_and_sales() != null) {
					cell5.setCellValue(record.getR33_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR33_retail_banking()  != null) {
					cell6.setCellValue(record.getR33_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR33_commercial_banking() != null) {
					cell7.setCellValue(record.getR33_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR33_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR33_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR33_agency_services() != null) {
					cell9.setCellValue(record.getR33_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR33_asset_management() != null) {
					cell10.setCellValue(record.getR33_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR33_retail_brokerage() != null) {
					cell11.setCellValue(record.getR33_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row34
				row = sheet.getRow(33);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR34_corporate_finance() != null) {
					cell4.setCellValue(record.getR34_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR34_trading_and_sales() != null) {
					cell5.setCellValue(record.getR34_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR34_retail_banking()  != null) {
					cell6.setCellValue(record.getR34_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR34_commercial_banking() != null) {
					cell7.setCellValue(record.getR34_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR34_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR34_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR34_agency_services() != null) {
					cell9.setCellValue(record.getR34_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR34_asset_management() != null) {
					cell10.setCellValue(record.getR34_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR34_retail_brokerage() != null) {
					cell11.setCellValue(record.getR34_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				
				//row35
				row = sheet.getRow(34);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR35_corporate_finance() != null) {
					cell4.setCellValue(record.getR35_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR35_trading_and_sales() != null) {
					cell5.setCellValue(record.getR35_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR35_retail_banking()  != null) {
					cell6.setCellValue(record.getR35_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR35_commercial_banking() != null) {
					cell7.setCellValue(record.getR35_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR35_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR35_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR35_agency_services() != null) {
					cell9.setCellValue(record.getR35_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR35_asset_management() != null) {
					cell10.setCellValue(record.getR35_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR35_retail_brokerage() != null) {
					cell11.setCellValue(record.getR35_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row 36
				row = sheet.getRow(35);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR36_corporate_finance() != null) {
					cell4.setCellValue(record.getR36_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR36_trading_and_sales() != null) {
					cell5.setCellValue(record.getR36_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR36_retail_banking()  != null) {
					cell6.setCellValue(record.getR36_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR36_commercial_banking() != null) {
					cell7.setCellValue(record.getR36_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR36_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR36_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR36_agency_services() != null) {
					cell9.setCellValue(record.getR36_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR36_asset_management() != null) {
					cell10.setCellValue(record.getR36_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR36_retail_brokerage() != null) {
					cell11.setCellValue(record.getR36_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				
				//row37
				row = sheet.getRow(36);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR37_corporate_finance() != null) {
					cell4.setCellValue(record.getR37_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR37_trading_and_sales() != null) {
					cell5.setCellValue(record.getR37_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR37_retail_banking()  != null) {
					cell6.setCellValue(record.getR37_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR37_commercial_banking() != null) {
					cell7.setCellValue(record.getR37_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR37_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR37_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR37_agency_services() != null) {
					cell9.setCellValue(record.getR37_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR37_asset_management() != null) {
					cell10.setCellValue(record.getR37_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR37_retail_brokerage() != null) {
					cell11.setCellValue(record.getR37_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				
				//row38
				row = sheet.getRow(37);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR38_corporate_finance() != null) {
					cell4.setCellValue(record.getR38_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR38_trading_and_sales() != null) {
					cell5.setCellValue(record.getR38_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR38_retail_banking()  != null) {
					cell6.setCellValue(record.getR38_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR38_commercial_banking() != null) {
					cell7.setCellValue(record.getR38_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR38_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR38_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR38_agency_services() != null) {
					cell9.setCellValue(record.getR38_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR38_asset_management() != null) {
					cell10.setCellValue(record.getR38_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR38_retail_brokerage() != null) {
					cell11.setCellValue(record.getR38_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				
				//row 39
				row = sheet.getRow(38);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR39_corporate_finance() != null) {
					cell4.setCellValue(record.getR39_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR39_trading_and_sales() != null) {
					cell5.setCellValue(record.getR39_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR39_retail_banking()  != null) {
					cell6.setCellValue(record.getR39_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR39_commercial_banking() != null) {
					cell7.setCellValue(record.getR39_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR39_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR39_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR39_agency_services() != null) {
					cell9.setCellValue(record.getR39_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR39_asset_management() != null) {
					cell10.setCellValue(record.getR39_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR39_retail_brokerage() != null) {
					cell11.setCellValue(record.getR39_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row44
				row = sheet.getRow(43);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR44_corporate_finance() != null) {
					cell4.setCellValue(record.getR44_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR44_trading_and_sales() != null) {
					cell5.setCellValue(record.getR44_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR44_retail_banking()  != null) {
					cell6.setCellValue(record.getR44_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR44_commercial_banking() != null) {
					cell7.setCellValue(record.getR44_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR44_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR44_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR44_agency_services() != null) {
					cell9.setCellValue(record.getR44_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR44_asset_management() != null) {
					cell10.setCellValue(record.getR44_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR44_retail_brokerage() != null) {
					cell11.setCellValue(record.getR44_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row45
				row = sheet.getRow(44);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR45_corporate_finance() != null) {
					cell4.setCellValue(record.getR45_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR45_trading_and_sales() != null) {
					cell5.setCellValue(record.getR45_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR45_retail_banking()  != null) {
					cell6.setCellValue(record.getR45_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR45_commercial_banking() != null) {
					cell7.setCellValue(record.getR45_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR45_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR45_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR45_agency_services() != null) {
					cell9.setCellValue(record.getR45_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR45_asset_management() != null) {
					cell10.setCellValue(record.getR45_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR45_retail_brokerage() != null) {
					cell11.setCellValue(record.getR45_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row46
				row = sheet.getRow(45);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR46_corporate_finance() != null) {
					cell4.setCellValue(record.getR46_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR46_trading_and_sales() != null) {
					cell5.setCellValue(record.getR46_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR46_retail_banking()  != null) {
					cell6.setCellValue(record.getR46_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR46_commercial_banking() != null) {
					cell7.setCellValue(record.getR46_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR46_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR46_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR46_agency_services() != null) {
					cell9.setCellValue(record.getR46_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR46_asset_management() != null) {
					cell10.setCellValue(record.getR46_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR46_retail_brokerage() != null) {
					cell11.setCellValue(record.getR46_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row47
				row = sheet.getRow(46);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR47_corporate_finance() != null) {
					cell4.setCellValue(record.getR47_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR47_trading_and_sales() != null) {
					cell5.setCellValue(record.getR47_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR47_retail_banking()  != null) {
					cell6.setCellValue(record.getR47_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR47_commercial_banking() != null) {
					cell7.setCellValue(record.getR47_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR47_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR47_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR47_agency_services() != null) {
					cell9.setCellValue(record.getR47_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR47_asset_management() != null) {
					cell10.setCellValue(record.getR47_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR47_retail_brokerage() != null) {
					cell11.setCellValue(record.getR47_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row48
				row = sheet.getRow(47);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR48_corporate_finance() != null) {
					cell4.setCellValue(record.getR48_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR48_trading_and_sales() != null) {
					cell5.setCellValue(record.getR48_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR48_retail_banking()  != null) {
					cell6.setCellValue(record.getR48_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR48_commercial_banking() != null) {
					cell7.setCellValue(record.getR48_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR48_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR48_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR48_agency_services() != null) {
					cell9.setCellValue(record.getR48_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR48_asset_management() != null) {
					cell10.setCellValue(record.getR48_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR48_retail_brokerage() != null) {
					cell11.setCellValue(record.getR48_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				
				//row49
				row = sheet.getRow(48);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR49_corporate_finance() != null) {
					cell4.setCellValue(record.getR49_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR49_trading_and_sales() != null) {
					cell5.setCellValue(record.getR49_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR49_retail_banking()  != null) {
					cell6.setCellValue(record.getR49_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR49_commercial_banking() != null) {
					cell7.setCellValue(record.getR49_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR49_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR49_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR49_agency_services() != null) {
					cell9.setCellValue(record.getR49_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR49_asset_management() != null) {
					cell10.setCellValue(record.getR49_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR49_retail_brokerage() != null) {
					cell11.setCellValue(record.getR49_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row50
				row = sheet.getRow(49);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR50_corporate_finance() != null) {
					cell4.setCellValue(record.getR50_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR50_trading_and_sales() != null) {
					cell5.setCellValue(record.getR50_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR50_retail_banking()  != null) {
					cell6.setCellValue(record.getR50_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR50_commercial_banking() != null) {
					cell7.setCellValue(record.getR50_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR50_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR50_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR50_agency_services() != null) {
					cell9.setCellValue(record.getR50_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR50_asset_management() != null) {
					cell10.setCellValue(record.getR50_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR50_retail_brokerage() != null) {
					cell11.setCellValue(record.getR50_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row51
				row = sheet.getRow(50);
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR51_corporate_finance() != null) {
					cell4.setCellValue(record.getR51_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR51_trading_and_sales() != null) {
					cell5.setCellValue(record.getR51_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR51_retail_banking()  != null) {
					cell6.setCellValue(record.getR51_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR51_commercial_banking() != null) {
					cell7.setCellValue(record.getR51_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR51_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR51_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR51_agency_services() != null) {
					cell9.setCellValue(record.getR51_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR51_asset_management() != null) {
					cell10.setCellValue(record.getR51_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR51_retail_brokerage() != null) {
					cell11.setCellValue(record.getR51_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row52
				row = sheet.getRow(51);
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR52_corporate_finance() != null) {
					cell4.setCellValue(record.getR52_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR52_trading_and_sales() != null) {
					cell5.setCellValue(record.getR52_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR52_retail_banking()  != null) {
					cell6.setCellValue(record.getR52_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR52_commercial_banking() != null) {
					cell7.setCellValue(record.getR52_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR52_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR52_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR52_agency_services() != null) {
					cell9.setCellValue(record.getR52_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR52_asset_management() != null) {
					cell10.setCellValue(record.getR52_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR52_retail_brokerage() != null) {
					cell11.setCellValue(record.getR52_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row53
				row = sheet.getRow(52);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR53_corporate_finance() != null) {
					cell4.setCellValue(record.getR53_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR53_trading_and_sales() != null) {
					cell5.setCellValue(record.getR53_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR53_retail_banking()  != null) {
					cell6.setCellValue(record.getR53_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR53_commercial_banking() != null) {
					cell7.setCellValue(record.getR53_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR53_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR53_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR53_agency_services() != null) {
					cell9.setCellValue(record.getR53_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR53_asset_management() != null) {
					cell10.setCellValue(record.getR53_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR53_retail_brokerage() != null) {
					cell11.setCellValue(record.getR53_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row54
				row = sheet.getRow(53);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR54_corporate_finance() != null) {
					cell4.setCellValue(record.getR54_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR54_trading_and_sales() != null) {
					cell5.setCellValue(record.getR54_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR54_retail_banking()  != null) {
					cell6.setCellValue(record.getR54_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR54_commercial_banking() != null) {
					cell7.setCellValue(record.getR54_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR54_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR54_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR54_agency_services() != null) {
					cell9.setCellValue(record.getR54_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR54_asset_management() != null) {
					cell10.setCellValue(record.getR54_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR54_retail_brokerage() != null) {
					cell11.setCellValue(record.getR54_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
				}
				
				//row55
				row = sheet.getRow(54);
				
				 // Column D
				 cell4 = row.createCell(3);
				if (record.getR55_corporate_finance() != null) {
					cell4.setCellValue(record.getR55_corporate_finance().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				
				// Column E
				 cell5 = row.createCell(4);
				if (record.getR55_trading_and_sales() != null) {
					cell5.setCellValue(record.getR55_trading_and_sales().doubleValue());
					cell5.setCellStyle(numberStyle);
				} else {
					cell5.setCellValue("");
					cell5.setCellStyle(textStyle);
				}
				
				
				
				// Column F
				 cell6 = row.createCell(5);
				if (record.getR55_retail_banking()  != null) {
					cell6.setCellValue(record.getR55_retail_banking() .doubleValue());
					cell6.setCellStyle(numberStyle);
				} else {
					cell6.setCellValue("");
					cell6.setCellStyle(textStyle);
				}
				
				
				// Column G
				 cell7 = row.createCell(6);
				if (record.getR55_commercial_banking() != null) {
					cell7.setCellValue(record.getR55_commercial_banking().doubleValue());
					cell7.setCellStyle(numberStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(textStyle);
				}
				
			
				// Column H
				 cell8 = row.createCell(7);
				if (record.getR55_payments_and_settlements() != null) {
					cell8.setCellValue(record.getR55_payments_and_settlements().doubleValue());
					cell8.setCellStyle(numberStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(textStyle);
				}
				
				
				// Column I
				 cell9 = row.createCell(8);
				if (record.getR55_agency_services() != null) {
					cell9.setCellValue(record.getR55_agency_services().doubleValue());
					cell9.setCellStyle(numberStyle);
				} else {
					cell9.setCellValue("");
					cell9.setCellStyle(textStyle);
				}
				
				
				
				
				
				
				
			
				// Column J
				 cell10 = row.createCell(9);
				if (record.getR55_asset_management() != null) {
					cell10.setCellValue(record.getR55_asset_management().doubleValue());
					cell10.setCellStyle(numberStyle);
				} else {
					cell10.setCellValue("");
					cell10.setCellStyle(textStyle);
				}
				
			
				// Column K
				 cell11 = row.createCell(10);
				if (record.getR55_retail_brokerage() != null) {
					cell11.setCellValue(record.getR55_retail_brokerage().doubleValue());
					cell11.setCellStyle(numberStyle);
				} else {
					cell11.setCellValue("");
					cell11.setCellStyle(textStyle);
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
	}
}
	
//Archival format excel
	public byte[] getExcelM_OR2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_OR2ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} 

		List<M_OR2_Archival_Summary_Entity> dataList = M_OR2_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_OR2 report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
//This specific exception will be caught by the controller.
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
//A specific exception for permission errors.
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

//This try-with-resources block is perfect. It guarantees all resources are
//closed automatically.
		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

//--- Style Definitions ---
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
//--- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_OR2_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

//row12
					// Column D 
					Cell cell4 = row.createCell(3);
					if (record.getR12_corporate_finance() != null) {
						cell4.setCellValue(record.getR12_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row12
					// Column E 
					Cell cell5 = row.createCell(4);
					if (record.getR12_trading_and_sales() != null) {
						cell5.setCellValue(record.getR12_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					} 
					
					
					//row12
					// Column F
					Cell cell6 = row.createCell(5);
					if (record.getR12_retail_banking() != null) {
						cell6.setCellValue(record.getR12_retail_banking().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					//row12
					// Column G
					Cell cell7 = row.createCell(6);
					if (record.getR12_commercial_banking()  != null) {
						cell7.setCellValue(record.getR12_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					//row12
					// Column H
					Cell cell8 = row.createCell(7);
					if (record.getR12_payments_and_settlements()  != null) {
						cell8.setCellValue(record.getR12_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					//row12
					// Column I
					Cell cell9 = row.createCell(8);
					if (record.getR12_agency_services() != null) {
						cell9.setCellValue(record.getR12_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					//row12
					// Column J
					Cell cell10 = row.createCell(9);
					if (record.getR12_asset_management()  != null) {
						cell10.setCellValue(record.getR12_asset_management() .doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					//row12
					// Column K
					Cell cell11 = row.createCell(10);
					if (record.getR12_retail_brokerage() != null) {
						cell11.setCellValue(record.getR12_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row13
					row = sheet.getRow(12);
					
					// Column D
					 cell4 = row.createCell(3);
					if (record.getR13_corporate_finance() != null) {
						cell4.setCellValue(record.getR13_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR13_trading_and_sales() != null) {
						cell5.setCellValue(record.getR13_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR13_retail_banking()  != null) {
						cell6.setCellValue(record.getR13_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR13_commercial_banking() != null) {
						cell7.setCellValue(record.getR13_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR13_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR13_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR13_agency_services() != null) {
						cell9.setCellValue(record.getR13_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR13_asset_management() != null) {
						cell10.setCellValue(record.getR13_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR13_retail_brokerage() != null) {
						cell11.setCellValue(record.getR13_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					
					
					//row14
					row = sheet.getRow(13);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR14_corporate_finance() != null) {
						cell4.setCellValue(record.getR14_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR14_trading_and_sales() != null) {
						cell5.setCellValue(record.getR14_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR14_retail_banking()  != null) {
						cell6.setCellValue(record.getR14_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR14_commercial_banking() != null) {
						cell7.setCellValue(record.getR14_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR14_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR14_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR14_agency_services() != null) {
						cell9.setCellValue(record.getR14_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR14_asset_management() != null) {
						cell10.setCellValue(record.getR14_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR14_retail_brokerage() != null) {
						cell11.setCellValue(record.getR14_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row15
					row = sheet.getRow(14);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR15_corporate_finance() != null) {
						cell4.setCellValue(record.getR15_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR15_trading_and_sales() != null) {
						cell5.setCellValue(record.getR15_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR15_retail_banking()  != null) {
						cell6.setCellValue(record.getR15_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR15_commercial_banking() != null) {
						cell7.setCellValue(record.getR15_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR15_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR15_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR15_agency_services() != null) {
						cell9.setCellValue(record.getR15_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR15_asset_management() != null) {
						cell10.setCellValue(record.getR15_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR15_retail_brokerage() != null) {
						cell11.setCellValue(record.getR15_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row16
					row = sheet.getRow(15);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR16_corporate_finance() != null) {
						cell4.setCellValue(record.getR16_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR16_trading_and_sales() != null) {
						cell5.setCellValue(record.getR16_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR16_retail_banking()  != null) {
						cell6.setCellValue(record.getR16_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR16_commercial_banking() != null) {
						cell7.setCellValue(record.getR16_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR16_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR16_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR16_agency_services() != null) {
						cell9.setCellValue(record.getR16_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR16_asset_management() != null) {
						cell10.setCellValue(record.getR16_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR16_retail_brokerage() != null) {
						cell11.setCellValue(record.getR16_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row17
					row = sheet.getRow(16);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR17_corporate_finance() != null) {
						cell4.setCellValue(record.getR17_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR17_trading_and_sales() != null) {
						cell5.setCellValue(record.getR17_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR17_retail_banking()  != null) {
						cell6.setCellValue(record.getR17_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR17_commercial_banking() != null) {
						cell7.setCellValue(record.getR17_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR17_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR17_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR17_agency_services() != null) {
						cell9.setCellValue(record.getR17_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR17_asset_management() != null) {
						cell10.setCellValue(record.getR17_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR17_retail_brokerage() != null) {
						cell11.setCellValue(record.getR17_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row18
					row = sheet.getRow(17);
					
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR18_corporate_finance() != null) {
						cell4.setCellValue(record.getR18_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR18_trading_and_sales() != null) {
						cell5.setCellValue(record.getR18_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR18_retail_banking()  != null) {
						cell6.setCellValue(record.getR18_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR18_commercial_banking() != null) {
						cell7.setCellValue(record.getR18_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR18_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR18_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR18_agency_services() != null) {
						cell9.setCellValue(record.getR18_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR18_asset_management() != null) {
						cell10.setCellValue(record.getR18_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR18_retail_brokerage() != null) {
						cell11.setCellValue(record.getR18_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row19
					row = sheet.getRow(18);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR19_corporate_finance() != null) {
						cell4.setCellValue(record.getR19_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR19_trading_and_sales() != null) {
						cell5.setCellValue(record.getR19_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR19_retail_banking()  != null) {
						cell6.setCellValue(record.getR19_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR19_commercial_banking() != null) {
						cell7.setCellValue(record.getR19_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR19_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR19_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR19_agency_services() != null) {
						cell9.setCellValue(record.getR19_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR19_asset_management() != null) {
						cell10.setCellValue(record.getR19_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR19_retail_brokerage() != null) {
						cell11.setCellValue(record.getR19_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row20
					row = sheet.getRow(19);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR20_corporate_finance() != null) {
						cell4.setCellValue(record.getR20_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR20_trading_and_sales() != null) {
						cell5.setCellValue(record.getR20_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR20_retail_banking()  != null) {
						cell6.setCellValue(record.getR20_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR20_commercial_banking() != null) {
						cell7.setCellValue(record.getR20_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR20_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR20_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR20_agency_services() != null) {
						cell9.setCellValue(record.getR20_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR20_asset_management() != null) {
						cell10.setCellValue(record.getR20_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR20_retail_brokerage() != null) {
						cell11.setCellValue(record.getR20_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row21
					row = sheet.getRow(20);
					
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR21_corporate_finance() != null) {
						cell4.setCellValue(record.getR21_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR21_trading_and_sales() != null) {
						cell5.setCellValue(record.getR21_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR21_retail_banking()  != null) {
						cell6.setCellValue(record.getR21_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR21_commercial_banking() != null) {
						cell7.setCellValue(record.getR21_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR21_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR21_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR21_agency_services() != null) {
						cell9.setCellValue(record.getR21_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR21_asset_management() != null) {
						cell10.setCellValue(record.getR21_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR21_retail_brokerage() != null) {
						cell11.setCellValue(record.getR21_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row22
					row = sheet.getRow(21);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR22_corporate_finance() != null) {
						cell4.setCellValue(record.getR22_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR22_trading_and_sales() != null) {
						cell5.setCellValue(record.getR22_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR22_retail_banking()  != null) {
						cell6.setCellValue(record.getR22_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR22_commercial_banking() != null) {
						cell7.setCellValue(record.getR22_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR22_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR22_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR22_agency_services() != null) {
						cell9.setCellValue(record.getR22_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR22_asset_management() != null) {
						cell10.setCellValue(record.getR22_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR22_retail_brokerage() != null) {
						cell11.setCellValue(record.getR22_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row23
					row = sheet.getRow(22);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR23_corporate_finance() != null) {
						cell4.setCellValue(record.getR23_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR23_trading_and_sales() != null) {
						cell5.setCellValue(record.getR23_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR23_retail_banking()  != null) {
						cell6.setCellValue(record.getR23_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR23_commercial_banking() != null) {
						cell7.setCellValue(record.getR23_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR23_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR23_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR23_agency_services() != null) {
						cell9.setCellValue(record.getR23_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR23_asset_management() != null) {
						cell10.setCellValue(record.getR23_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR23_retail_brokerage() != null) {
						cell11.setCellValue(record.getR23_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row28
					row = sheet.getRow(27);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR28_corporate_finance() != null) {
						cell4.setCellValue(record.getR28_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR28_trading_and_sales() != null) {
						cell5.setCellValue(record.getR28_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR28_retail_banking()  != null) {
						cell6.setCellValue(record.getR28_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR28_commercial_banking() != null) {
						cell7.setCellValue(record.getR28_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR28_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR28_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR28_agency_services() != null) {
						cell9.setCellValue(record.getR28_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR28_asset_management() != null) {
						cell10.setCellValue(record.getR28_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR28_retail_brokerage() != null) {
						cell11.setCellValue(record.getR28_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row29
					row = sheet.getRow(28);
					
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR29_corporate_finance() != null) {
						cell4.setCellValue(record.getR29_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR29_trading_and_sales() != null) {
						cell5.setCellValue(record.getR29_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR29_retail_banking()  != null) {
						cell6.setCellValue(record.getR29_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR29_commercial_banking() != null) {
						cell7.setCellValue(record.getR29_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR29_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR29_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR29_agency_services() != null) {
						cell9.setCellValue(record.getR29_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR29_asset_management() != null) {
						cell10.setCellValue(record.getR29_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR29_retail_brokerage() != null) {
						cell11.setCellValue(record.getR29_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row30
					row = sheet.getRow(29);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR30_corporate_finance() != null) {
						cell4.setCellValue(record.getR30_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR30_trading_and_sales() != null) {
						cell5.setCellValue(record.getR30_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR30_retail_banking()  != null) {
						cell6.setCellValue(record.getR30_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR30_commercial_banking() != null) {
						cell7.setCellValue(record.getR30_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR30_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR30_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR30_agency_services() != null) {
						cell9.setCellValue(record.getR30_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR30_asset_management() != null) {
						cell10.setCellValue(record.getR30_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR30_retail_brokerage() != null) {
						cell11.setCellValue(record.getR30_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row31
					row = sheet.getRow(30);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR31_corporate_finance() != null) {
						cell4.setCellValue(record.getR31_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR31_trading_and_sales() != null) {
						cell5.setCellValue(record.getR31_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR31_retail_banking()  != null) {
						cell6.setCellValue(record.getR31_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR31_commercial_banking() != null) {
						cell7.setCellValue(record.getR31_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR31_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR31_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR31_agency_services() != null) {
						cell9.setCellValue(record.getR31_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR31_asset_management() != null) {
						cell10.setCellValue(record.getR31_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR31_retail_brokerage() != null) {
						cell11.setCellValue(record.getR31_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row32
					row = sheet.getRow(31);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR32_corporate_finance() != null) {
						cell4.setCellValue(record.getR32_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR32_trading_and_sales() != null) {
						cell5.setCellValue(record.getR32_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR32_retail_banking()  != null) {
						cell6.setCellValue(record.getR32_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR32_commercial_banking() != null) {
						cell7.setCellValue(record.getR32_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR32_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR32_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR32_agency_services() != null) {
						cell9.setCellValue(record.getR32_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR32_asset_management() != null) {
						cell10.setCellValue(record.getR32_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR32_retail_brokerage() != null) {
						cell11.setCellValue(record.getR32_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row33
					row = sheet.getRow(32);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR33_corporate_finance() != null) {
						cell4.setCellValue(record.getR33_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR33_trading_and_sales() != null) {
						cell5.setCellValue(record.getR33_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR33_retail_banking()  != null) {
						cell6.setCellValue(record.getR33_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR33_commercial_banking() != null) {
						cell7.setCellValue(record.getR33_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR33_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR33_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR33_agency_services() != null) {
						cell9.setCellValue(record.getR33_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR33_asset_management() != null) {
						cell10.setCellValue(record.getR33_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR33_retail_brokerage() != null) {
						cell11.setCellValue(record.getR33_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row34
					row = sheet.getRow(33);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR34_corporate_finance() != null) {
						cell4.setCellValue(record.getR34_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR34_trading_and_sales() != null) {
						cell5.setCellValue(record.getR34_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR34_retail_banking()  != null) {
						cell6.setCellValue(record.getR34_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR34_commercial_banking() != null) {
						cell7.setCellValue(record.getR34_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR34_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR34_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR34_agency_services() != null) {
						cell9.setCellValue(record.getR34_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR34_asset_management() != null) {
						cell10.setCellValue(record.getR34_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR34_retail_brokerage() != null) {
						cell11.setCellValue(record.getR34_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row35
					row = sheet.getRow(34);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR35_corporate_finance() != null) {
						cell4.setCellValue(record.getR35_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR35_trading_and_sales() != null) {
						cell5.setCellValue(record.getR35_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR35_retail_banking()  != null) {
						cell6.setCellValue(record.getR35_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR35_commercial_banking() != null) {
						cell7.setCellValue(record.getR35_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR35_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR35_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR35_agency_services() != null) {
						cell9.setCellValue(record.getR35_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR35_asset_management() != null) {
						cell10.setCellValue(record.getR35_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR35_retail_brokerage() != null) {
						cell11.setCellValue(record.getR35_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row 36
					row = sheet.getRow(35);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR36_corporate_finance() != null) {
						cell4.setCellValue(record.getR36_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR36_trading_and_sales() != null) {
						cell5.setCellValue(record.getR36_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR36_retail_banking()  != null) {
						cell6.setCellValue(record.getR36_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR36_commercial_banking() != null) {
						cell7.setCellValue(record.getR36_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR36_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR36_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR36_agency_services() != null) {
						cell9.setCellValue(record.getR36_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR36_asset_management() != null) {
						cell10.setCellValue(record.getR36_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR36_retail_brokerage() != null) {
						cell11.setCellValue(record.getR36_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row37
					row = sheet.getRow(36);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR37_corporate_finance() != null) {
						cell4.setCellValue(record.getR37_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR37_trading_and_sales() != null) {
						cell5.setCellValue(record.getR37_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR37_retail_banking()  != null) {
						cell6.setCellValue(record.getR37_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR37_commercial_banking() != null) {
						cell7.setCellValue(record.getR37_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR37_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR37_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR37_agency_services() != null) {
						cell9.setCellValue(record.getR37_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR37_asset_management() != null) {
						cell10.setCellValue(record.getR37_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR37_retail_brokerage() != null) {
						cell11.setCellValue(record.getR37_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row38
					row = sheet.getRow(37);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR38_corporate_finance() != null) {
						cell4.setCellValue(record.getR38_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR38_trading_and_sales() != null) {
						cell5.setCellValue(record.getR38_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR38_retail_banking()  != null) {
						cell6.setCellValue(record.getR38_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR38_commercial_banking() != null) {
						cell7.setCellValue(record.getR38_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR38_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR38_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR38_agency_services() != null) {
						cell9.setCellValue(record.getR38_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR38_asset_management() != null) {
						cell10.setCellValue(record.getR38_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR38_retail_brokerage() != null) {
						cell11.setCellValue(record.getR38_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row 39
					row = sheet.getRow(38);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR39_corporate_finance() != null) {
						cell4.setCellValue(record.getR39_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR39_trading_and_sales() != null) {
						cell5.setCellValue(record.getR39_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR39_retail_banking()  != null) {
						cell6.setCellValue(record.getR39_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR39_commercial_banking() != null) {
						cell7.setCellValue(record.getR39_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR39_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR39_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR39_agency_services() != null) {
						cell9.setCellValue(record.getR39_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR39_asset_management() != null) {
						cell10.setCellValue(record.getR39_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR39_retail_brokerage() != null) {
						cell11.setCellValue(record.getR39_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row44
					row = sheet.getRow(43);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR44_corporate_finance() != null) {
						cell4.setCellValue(record.getR44_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR44_trading_and_sales() != null) {
						cell5.setCellValue(record.getR44_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR44_retail_banking()  != null) {
						cell6.setCellValue(record.getR44_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR44_commercial_banking() != null) {
						cell7.setCellValue(record.getR44_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR44_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR44_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR44_agency_services() != null) {
						cell9.setCellValue(record.getR44_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR44_asset_management() != null) {
						cell10.setCellValue(record.getR44_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR44_retail_brokerage() != null) {
						cell11.setCellValue(record.getR44_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row45
					row = sheet.getRow(44);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR45_corporate_finance() != null) {
						cell4.setCellValue(record.getR45_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR45_trading_and_sales() != null) {
						cell5.setCellValue(record.getR45_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR45_retail_banking()  != null) {
						cell6.setCellValue(record.getR45_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR45_commercial_banking() != null) {
						cell7.setCellValue(record.getR45_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR45_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR45_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR45_agency_services() != null) {
						cell9.setCellValue(record.getR45_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR45_asset_management() != null) {
						cell10.setCellValue(record.getR45_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR45_retail_brokerage() != null) {
						cell11.setCellValue(record.getR45_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row46
					row = sheet.getRow(45);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR46_corporate_finance() != null) {
						cell4.setCellValue(record.getR46_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR46_trading_and_sales() != null) {
						cell5.setCellValue(record.getR46_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR46_retail_banking()  != null) {
						cell6.setCellValue(record.getR46_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR46_commercial_banking() != null) {
						cell7.setCellValue(record.getR46_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR46_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR46_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR46_agency_services() != null) {
						cell9.setCellValue(record.getR46_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR46_asset_management() != null) {
						cell10.setCellValue(record.getR46_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR46_retail_brokerage() != null) {
						cell11.setCellValue(record.getR46_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row47
					row = sheet.getRow(46);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR47_corporate_finance() != null) {
						cell4.setCellValue(record.getR47_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR47_trading_and_sales() != null) {
						cell5.setCellValue(record.getR47_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR47_retail_banking()  != null) {
						cell6.setCellValue(record.getR47_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR47_commercial_banking() != null) {
						cell7.setCellValue(record.getR47_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR47_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR47_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR47_agency_services() != null) {
						cell9.setCellValue(record.getR47_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR47_asset_management() != null) {
						cell10.setCellValue(record.getR47_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR47_retail_brokerage() != null) {
						cell11.setCellValue(record.getR47_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row48
					row = sheet.getRow(47);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR48_corporate_finance() != null) {
						cell4.setCellValue(record.getR48_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR48_trading_and_sales() != null) {
						cell5.setCellValue(record.getR48_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR48_retail_banking()  != null) {
						cell6.setCellValue(record.getR48_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR48_commercial_banking() != null) {
						cell7.setCellValue(record.getR48_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR48_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR48_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR48_agency_services() != null) {
						cell9.setCellValue(record.getR48_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR48_asset_management() != null) {
						cell10.setCellValue(record.getR48_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR48_retail_brokerage() != null) {
						cell11.setCellValue(record.getR48_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row49
					row = sheet.getRow(48);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR49_corporate_finance() != null) {
						cell4.setCellValue(record.getR49_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR49_trading_and_sales() != null) {
						cell5.setCellValue(record.getR49_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR49_retail_banking()  != null) {
						cell6.setCellValue(record.getR49_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR49_commercial_banking() != null) {
						cell7.setCellValue(record.getR49_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR49_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR49_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR49_agency_services() != null) {
						cell9.setCellValue(record.getR49_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR49_asset_management() != null) {
						cell10.setCellValue(record.getR49_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR49_retail_brokerage() != null) {
						cell11.setCellValue(record.getR49_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row50
					row = sheet.getRow(49);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR50_corporate_finance() != null) {
						cell4.setCellValue(record.getR50_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR50_trading_and_sales() != null) {
						cell5.setCellValue(record.getR50_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR50_retail_banking()  != null) {
						cell6.setCellValue(record.getR50_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR50_commercial_banking() != null) {
						cell7.setCellValue(record.getR50_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR50_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR50_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR50_agency_services() != null) {
						cell9.setCellValue(record.getR50_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR50_asset_management() != null) {
						cell10.setCellValue(record.getR50_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR50_retail_brokerage() != null) {
						cell11.setCellValue(record.getR50_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row51
					row = sheet.getRow(50);
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR51_corporate_finance() != null) {
						cell4.setCellValue(record.getR51_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR51_trading_and_sales() != null) {
						cell5.setCellValue(record.getR51_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR51_retail_banking()  != null) {
						cell6.setCellValue(record.getR51_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR51_commercial_banking() != null) {
						cell7.setCellValue(record.getR51_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR51_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR51_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR51_agency_services() != null) {
						cell9.setCellValue(record.getR51_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR51_asset_management() != null) {
						cell10.setCellValue(record.getR51_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR51_retail_brokerage() != null) {
						cell11.setCellValue(record.getR51_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row52
					row = sheet.getRow(51);
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR52_corporate_finance() != null) {
						cell4.setCellValue(record.getR52_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR52_trading_and_sales() != null) {
						cell5.setCellValue(record.getR52_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR52_retail_banking()  != null) {
						cell6.setCellValue(record.getR52_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR52_commercial_banking() != null) {
						cell7.setCellValue(record.getR52_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR52_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR52_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR52_agency_services() != null) {
						cell9.setCellValue(record.getR52_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR52_asset_management() != null) {
						cell10.setCellValue(record.getR52_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR52_retail_brokerage() != null) {
						cell11.setCellValue(record.getR52_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row53
					row = sheet.getRow(52);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR53_corporate_finance() != null) {
						cell4.setCellValue(record.getR53_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR53_trading_and_sales() != null) {
						cell5.setCellValue(record.getR53_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR53_retail_banking()  != null) {
						cell6.setCellValue(record.getR53_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR53_commercial_banking() != null) {
						cell7.setCellValue(record.getR53_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR53_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR53_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR53_agency_services() != null) {
						cell9.setCellValue(record.getR53_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR53_asset_management() != null) {
						cell10.setCellValue(record.getR53_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR53_retail_brokerage() != null) {
						cell11.setCellValue(record.getR53_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row54
					row = sheet.getRow(53);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR54_corporate_finance() != null) {
						cell4.setCellValue(record.getR54_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR54_trading_and_sales() != null) {
						cell5.setCellValue(record.getR54_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR54_retail_banking()  != null) {
						cell6.setCellValue(record.getR54_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR54_commercial_banking() != null) {
						cell7.setCellValue(record.getR54_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR54_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR54_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR54_agency_services() != null) {
						cell9.setCellValue(record.getR54_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR54_asset_management() != null) {
						cell10.setCellValue(record.getR54_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR54_retail_brokerage() != null) {
						cell11.setCellValue(record.getR54_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row55
					row = sheet.getRow(54);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR55_corporate_finance() != null) {
						cell4.setCellValue(record.getR55_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR55_trading_and_sales() != null) {
						cell5.setCellValue(record.getR55_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR55_retail_banking()  != null) {
						cell6.setCellValue(record.getR55_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR55_commercial_banking() != null) {
						cell7.setCellValue(record.getR55_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR55_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR55_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR55_agency_services() != null) {
						cell9.setCellValue(record.getR55_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR55_asset_management() != null) {
						cell10.setCellValue(record.getR55_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR55_retail_brokerage() != null) {
						cell11.setCellValue(record.getR55_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					} 
				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

//Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}


/// Resub Format excel
	public byte[] BRRS_M_OR2ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OR2EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
		}
	}

		List<M_OR2_RESUB_Summary_Entity> dataList = M_OR2_resub_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_OR2 report. Returning empty result.");
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

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_OR2_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//row12
					// Column D 
					Cell cell4 = row.createCell(3);
					if (record.getR12_corporate_finance() != null) {
						cell4.setCellValue(record.getR12_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row12
					// Column E 
					Cell cell5 = row.createCell(4);
					if (record.getR12_trading_and_sales() != null) {
						cell5.setCellValue(record.getR12_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					} 
					
					
					//row12
					// Column F
					Cell cell6 = row.createCell(5);
					if (record.getR12_retail_banking() != null) {
						cell6.setCellValue(record.getR12_retail_banking().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					//row12
					// Column G
					Cell cell7 = row.createCell(6);
					if (record.getR12_commercial_banking()  != null) {
						cell7.setCellValue(record.getR12_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					//row12
					// Column H
					Cell cell8 = row.createCell(7);
					if (record.getR12_payments_and_settlements()  != null) {
						cell8.setCellValue(record.getR12_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					//row12
					// Column I
					Cell cell9 = row.createCell(8);
					if (record.getR12_agency_services() != null) {
						cell9.setCellValue(record.getR12_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					//row12
					// Column J
					Cell cell10 = row.createCell(9);
					if (record.getR12_asset_management()  != null) {
						cell10.setCellValue(record.getR12_asset_management() .doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					//row12
					// Column K
					Cell cell11 = row.createCell(10);
					if (record.getR12_retail_brokerage() != null) {
						cell11.setCellValue(record.getR12_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row13
					row = sheet.getRow(12);
					
					// Column D
					 cell4 = row.createCell(3);
					if (record.getR13_corporate_finance() != null) {
						cell4.setCellValue(record.getR13_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR13_trading_and_sales() != null) {
						cell5.setCellValue(record.getR13_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR13_retail_banking()  != null) {
						cell6.setCellValue(record.getR13_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR13_commercial_banking() != null) {
						cell7.setCellValue(record.getR13_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR13_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR13_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR13_agency_services() != null) {
						cell9.setCellValue(record.getR13_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR13_asset_management() != null) {
						cell10.setCellValue(record.getR13_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR13_retail_brokerage() != null) {
						cell11.setCellValue(record.getR13_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					
					
					//row14
					row = sheet.getRow(13);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR14_corporate_finance() != null) {
						cell4.setCellValue(record.getR14_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR14_trading_and_sales() != null) {
						cell5.setCellValue(record.getR14_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR14_retail_banking()  != null) {
						cell6.setCellValue(record.getR14_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR14_commercial_banking() != null) {
						cell7.setCellValue(record.getR14_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR14_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR14_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR14_agency_services() != null) {
						cell9.setCellValue(record.getR14_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR14_asset_management() != null) {
						cell10.setCellValue(record.getR14_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR14_retail_brokerage() != null) {
						cell11.setCellValue(record.getR14_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row15
					row = sheet.getRow(14);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR15_corporate_finance() != null) {
						cell4.setCellValue(record.getR15_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR15_trading_and_sales() != null) {
						cell5.setCellValue(record.getR15_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR15_retail_banking()  != null) {
						cell6.setCellValue(record.getR15_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR15_commercial_banking() != null) {
						cell7.setCellValue(record.getR15_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR15_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR15_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR15_agency_services() != null) {
						cell9.setCellValue(record.getR15_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR15_asset_management() != null) {
						cell10.setCellValue(record.getR15_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR15_retail_brokerage() != null) {
						cell11.setCellValue(record.getR15_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row16
					row = sheet.getRow(15);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR16_corporate_finance() != null) {
						cell4.setCellValue(record.getR16_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR16_trading_and_sales() != null) {
						cell5.setCellValue(record.getR16_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR16_retail_banking()  != null) {
						cell6.setCellValue(record.getR16_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR16_commercial_banking() != null) {
						cell7.setCellValue(record.getR16_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR16_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR16_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR16_agency_services() != null) {
						cell9.setCellValue(record.getR16_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR16_asset_management() != null) {
						cell10.setCellValue(record.getR16_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR16_retail_brokerage() != null) {
						cell11.setCellValue(record.getR16_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row17
					row = sheet.getRow(16);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR17_corporate_finance() != null) {
						cell4.setCellValue(record.getR17_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR17_trading_and_sales() != null) {
						cell5.setCellValue(record.getR17_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR17_retail_banking()  != null) {
						cell6.setCellValue(record.getR17_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR17_commercial_banking() != null) {
						cell7.setCellValue(record.getR17_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR17_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR17_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR17_agency_services() != null) {
						cell9.setCellValue(record.getR17_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR17_asset_management() != null) {
						cell10.setCellValue(record.getR17_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR17_retail_brokerage() != null) {
						cell11.setCellValue(record.getR17_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row18
					row = sheet.getRow(17);
					
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR18_corporate_finance() != null) {
						cell4.setCellValue(record.getR18_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR18_trading_and_sales() != null) {
						cell5.setCellValue(record.getR18_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR18_retail_banking()  != null) {
						cell6.setCellValue(record.getR18_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR18_commercial_banking() != null) {
						cell7.setCellValue(record.getR18_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR18_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR18_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR18_agency_services() != null) {
						cell9.setCellValue(record.getR18_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR18_asset_management() != null) {
						cell10.setCellValue(record.getR18_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR18_retail_brokerage() != null) {
						cell11.setCellValue(record.getR18_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row19
					row = sheet.getRow(18);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR19_corporate_finance() != null) {
						cell4.setCellValue(record.getR19_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR19_trading_and_sales() != null) {
						cell5.setCellValue(record.getR19_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR19_retail_banking()  != null) {
						cell6.setCellValue(record.getR19_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR19_commercial_banking() != null) {
						cell7.setCellValue(record.getR19_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR19_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR19_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR19_agency_services() != null) {
						cell9.setCellValue(record.getR19_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR19_asset_management() != null) {
						cell10.setCellValue(record.getR19_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR19_retail_brokerage() != null) {
						cell11.setCellValue(record.getR19_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row20
					row = sheet.getRow(19);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR20_corporate_finance() != null) {
						cell4.setCellValue(record.getR20_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR20_trading_and_sales() != null) {
						cell5.setCellValue(record.getR20_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR20_retail_banking()  != null) {
						cell6.setCellValue(record.getR20_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR20_commercial_banking() != null) {
						cell7.setCellValue(record.getR20_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR20_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR20_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR20_agency_services() != null) {
						cell9.setCellValue(record.getR20_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR20_asset_management() != null) {
						cell10.setCellValue(record.getR20_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR20_retail_brokerage() != null) {
						cell11.setCellValue(record.getR20_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row21
					row = sheet.getRow(20);
					
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR21_corporate_finance() != null) {
						cell4.setCellValue(record.getR21_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR21_trading_and_sales() != null) {
						cell5.setCellValue(record.getR21_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR21_retail_banking()  != null) {
						cell6.setCellValue(record.getR21_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR21_commercial_banking() != null) {
						cell7.setCellValue(record.getR21_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR21_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR21_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR21_agency_services() != null) {
						cell9.setCellValue(record.getR21_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR21_asset_management() != null) {
						cell10.setCellValue(record.getR21_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR21_retail_brokerage() != null) {
						cell11.setCellValue(record.getR21_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row22
					row = sheet.getRow(21);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR22_corporate_finance() != null) {
						cell4.setCellValue(record.getR22_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR22_trading_and_sales() != null) {
						cell5.setCellValue(record.getR22_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR22_retail_banking()  != null) {
						cell6.setCellValue(record.getR22_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR22_commercial_banking() != null) {
						cell7.setCellValue(record.getR22_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR22_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR22_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR22_agency_services() != null) {
						cell9.setCellValue(record.getR22_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR22_asset_management() != null) {
						cell10.setCellValue(record.getR22_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR22_retail_brokerage() != null) {
						cell11.setCellValue(record.getR22_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row23
					row = sheet.getRow(22);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR23_corporate_finance() != null) {
						cell4.setCellValue(record.getR23_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR23_trading_and_sales() != null) {
						cell5.setCellValue(record.getR23_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR23_retail_banking()  != null) {
						cell6.setCellValue(record.getR23_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR23_commercial_banking() != null) {
						cell7.setCellValue(record.getR23_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR23_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR23_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR23_agency_services() != null) {
						cell9.setCellValue(record.getR23_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR23_asset_management() != null) {
						cell10.setCellValue(record.getR23_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR23_retail_brokerage() != null) {
						cell11.setCellValue(record.getR23_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row28
					row = sheet.getRow(27);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR28_corporate_finance() != null) {
						cell4.setCellValue(record.getR28_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR28_trading_and_sales() != null) {
						cell5.setCellValue(record.getR28_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR28_retail_banking()  != null) {
						cell6.setCellValue(record.getR28_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR28_commercial_banking() != null) {
						cell7.setCellValue(record.getR28_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR28_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR28_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR28_agency_services() != null) {
						cell9.setCellValue(record.getR28_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR28_asset_management() != null) {
						cell10.setCellValue(record.getR28_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR28_retail_brokerage() != null) {
						cell11.setCellValue(record.getR28_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row29
					row = sheet.getRow(28);
					
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR29_corporate_finance() != null) {
						cell4.setCellValue(record.getR29_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR29_trading_and_sales() != null) {
						cell5.setCellValue(record.getR29_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR29_retail_banking()  != null) {
						cell6.setCellValue(record.getR29_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR29_commercial_banking() != null) {
						cell7.setCellValue(record.getR29_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR29_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR29_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR29_agency_services() != null) {
						cell9.setCellValue(record.getR29_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR29_asset_management() != null) {
						cell10.setCellValue(record.getR29_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR29_retail_brokerage() != null) {
						cell11.setCellValue(record.getR29_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row30
					row = sheet.getRow(29);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR30_corporate_finance() != null) {
						cell4.setCellValue(record.getR30_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR30_trading_and_sales() != null) {
						cell5.setCellValue(record.getR30_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR30_retail_banking()  != null) {
						cell6.setCellValue(record.getR30_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR30_commercial_banking() != null) {
						cell7.setCellValue(record.getR30_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR30_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR30_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR30_agency_services() != null) {
						cell9.setCellValue(record.getR30_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR30_asset_management() != null) {
						cell10.setCellValue(record.getR30_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR30_retail_brokerage() != null) {
						cell11.setCellValue(record.getR30_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row31
					row = sheet.getRow(30);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR31_corporate_finance() != null) {
						cell4.setCellValue(record.getR31_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR31_trading_and_sales() != null) {
						cell5.setCellValue(record.getR31_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR31_retail_banking()  != null) {
						cell6.setCellValue(record.getR31_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR31_commercial_banking() != null) {
						cell7.setCellValue(record.getR31_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR31_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR31_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR31_agency_services() != null) {
						cell9.setCellValue(record.getR31_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR31_asset_management() != null) {
						cell10.setCellValue(record.getR31_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR31_retail_brokerage() != null) {
						cell11.setCellValue(record.getR31_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row32
					row = sheet.getRow(31);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR32_corporate_finance() != null) {
						cell4.setCellValue(record.getR32_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR32_trading_and_sales() != null) {
						cell5.setCellValue(record.getR32_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR32_retail_banking()  != null) {
						cell6.setCellValue(record.getR32_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR32_commercial_banking() != null) {
						cell7.setCellValue(record.getR32_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR32_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR32_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR32_agency_services() != null) {
						cell9.setCellValue(record.getR32_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR32_asset_management() != null) {
						cell10.setCellValue(record.getR32_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR32_retail_brokerage() != null) {
						cell11.setCellValue(record.getR32_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row33
					row = sheet.getRow(32);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR33_corporate_finance() != null) {
						cell4.setCellValue(record.getR33_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR33_trading_and_sales() != null) {
						cell5.setCellValue(record.getR33_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR33_retail_banking()  != null) {
						cell6.setCellValue(record.getR33_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR33_commercial_banking() != null) {
						cell7.setCellValue(record.getR33_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR33_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR33_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR33_agency_services() != null) {
						cell9.setCellValue(record.getR33_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR33_asset_management() != null) {
						cell10.setCellValue(record.getR33_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR33_retail_brokerage() != null) {
						cell11.setCellValue(record.getR33_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row34
					row = sheet.getRow(33);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR34_corporate_finance() != null) {
						cell4.setCellValue(record.getR34_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR34_trading_and_sales() != null) {
						cell5.setCellValue(record.getR34_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR34_retail_banking()  != null) {
						cell6.setCellValue(record.getR34_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR34_commercial_banking() != null) {
						cell7.setCellValue(record.getR34_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR34_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR34_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR34_agency_services() != null) {
						cell9.setCellValue(record.getR34_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR34_asset_management() != null) {
						cell10.setCellValue(record.getR34_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR34_retail_brokerage() != null) {
						cell11.setCellValue(record.getR34_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row35
					row = sheet.getRow(34);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR35_corporate_finance() != null) {
						cell4.setCellValue(record.getR35_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR35_trading_and_sales() != null) {
						cell5.setCellValue(record.getR35_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR35_retail_banking()  != null) {
						cell6.setCellValue(record.getR35_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR35_commercial_banking() != null) {
						cell7.setCellValue(record.getR35_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR35_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR35_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR35_agency_services() != null) {
						cell9.setCellValue(record.getR35_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR35_asset_management() != null) {
						cell10.setCellValue(record.getR35_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR35_retail_brokerage() != null) {
						cell11.setCellValue(record.getR35_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row 36
					row = sheet.getRow(35);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR36_corporate_finance() != null) {
						cell4.setCellValue(record.getR36_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR36_trading_and_sales() != null) {
						cell5.setCellValue(record.getR36_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR36_retail_banking()  != null) {
						cell6.setCellValue(record.getR36_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR36_commercial_banking() != null) {
						cell7.setCellValue(record.getR36_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR36_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR36_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR36_agency_services() != null) {
						cell9.setCellValue(record.getR36_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR36_asset_management() != null) {
						cell10.setCellValue(record.getR36_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR36_retail_brokerage() != null) {
						cell11.setCellValue(record.getR36_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row37
					row = sheet.getRow(36);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR37_corporate_finance() != null) {
						cell4.setCellValue(record.getR37_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR37_trading_and_sales() != null) {
						cell5.setCellValue(record.getR37_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR37_retail_banking()  != null) {
						cell6.setCellValue(record.getR37_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR37_commercial_banking() != null) {
						cell7.setCellValue(record.getR37_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR37_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR37_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR37_agency_services() != null) {
						cell9.setCellValue(record.getR37_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR37_asset_management() != null) {
						cell10.setCellValue(record.getR37_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR37_retail_brokerage() != null) {
						cell11.setCellValue(record.getR37_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row38
					row = sheet.getRow(37);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR38_corporate_finance() != null) {
						cell4.setCellValue(record.getR38_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR38_trading_and_sales() != null) {
						cell5.setCellValue(record.getR38_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR38_retail_banking()  != null) {
						cell6.setCellValue(record.getR38_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR38_commercial_banking() != null) {
						cell7.setCellValue(record.getR38_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR38_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR38_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR38_agency_services() != null) {
						cell9.setCellValue(record.getR38_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR38_asset_management() != null) {
						cell10.setCellValue(record.getR38_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR38_retail_brokerage() != null) {
						cell11.setCellValue(record.getR38_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row 39
					row = sheet.getRow(38);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR39_corporate_finance() != null) {
						cell4.setCellValue(record.getR39_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR39_trading_and_sales() != null) {
						cell5.setCellValue(record.getR39_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR39_retail_banking()  != null) {
						cell6.setCellValue(record.getR39_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR39_commercial_banking() != null) {
						cell7.setCellValue(record.getR39_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR39_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR39_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR39_agency_services() != null) {
						cell9.setCellValue(record.getR39_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR39_asset_management() != null) {
						cell10.setCellValue(record.getR39_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR39_retail_brokerage() != null) {
						cell11.setCellValue(record.getR39_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row44
					row = sheet.getRow(43);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR44_corporate_finance() != null) {
						cell4.setCellValue(record.getR44_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR44_trading_and_sales() != null) {
						cell5.setCellValue(record.getR44_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR44_retail_banking()  != null) {
						cell6.setCellValue(record.getR44_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR44_commercial_banking() != null) {
						cell7.setCellValue(record.getR44_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR44_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR44_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR44_agency_services() != null) {
						cell9.setCellValue(record.getR44_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR44_asset_management() != null) {
						cell10.setCellValue(record.getR44_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR44_retail_brokerage() != null) {
						cell11.setCellValue(record.getR44_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row45
					row = sheet.getRow(44);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR45_corporate_finance() != null) {
						cell4.setCellValue(record.getR45_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR45_trading_and_sales() != null) {
						cell5.setCellValue(record.getR45_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR45_retail_banking()  != null) {
						cell6.setCellValue(record.getR45_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR45_commercial_banking() != null) {
						cell7.setCellValue(record.getR45_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR45_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR45_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR45_agency_services() != null) {
						cell9.setCellValue(record.getR45_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR45_asset_management() != null) {
						cell10.setCellValue(record.getR45_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR45_retail_brokerage() != null) {
						cell11.setCellValue(record.getR45_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row46
					row = sheet.getRow(45);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR46_corporate_finance() != null) {
						cell4.setCellValue(record.getR46_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR46_trading_and_sales() != null) {
						cell5.setCellValue(record.getR46_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR46_retail_banking()  != null) {
						cell6.setCellValue(record.getR46_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR46_commercial_banking() != null) {
						cell7.setCellValue(record.getR46_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR46_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR46_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR46_agency_services() != null) {
						cell9.setCellValue(record.getR46_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR46_asset_management() != null) {
						cell10.setCellValue(record.getR46_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR46_retail_brokerage() != null) {
						cell11.setCellValue(record.getR46_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row47
					row = sheet.getRow(46);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR47_corporate_finance() != null) {
						cell4.setCellValue(record.getR47_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR47_trading_and_sales() != null) {
						cell5.setCellValue(record.getR47_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR47_retail_banking()  != null) {
						cell6.setCellValue(record.getR47_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR47_commercial_banking() != null) {
						cell7.setCellValue(record.getR47_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR47_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR47_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR47_agency_services() != null) {
						cell9.setCellValue(record.getR47_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR47_asset_management() != null) {
						cell10.setCellValue(record.getR47_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR47_retail_brokerage() != null) {
						cell11.setCellValue(record.getR47_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row48
					row = sheet.getRow(47);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR48_corporate_finance() != null) {
						cell4.setCellValue(record.getR48_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR48_trading_and_sales() != null) {
						cell5.setCellValue(record.getR48_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR48_retail_banking()  != null) {
						cell6.setCellValue(record.getR48_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR48_commercial_banking() != null) {
						cell7.setCellValue(record.getR48_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR48_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR48_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR48_agency_services() != null) {
						cell9.setCellValue(record.getR48_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR48_asset_management() != null) {
						cell10.setCellValue(record.getR48_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR48_retail_brokerage() != null) {
						cell11.setCellValue(record.getR48_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row49
					row = sheet.getRow(48);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR49_corporate_finance() != null) {
						cell4.setCellValue(record.getR49_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR49_trading_and_sales() != null) {
						cell5.setCellValue(record.getR49_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR49_retail_banking()  != null) {
						cell6.setCellValue(record.getR49_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR49_commercial_banking() != null) {
						cell7.setCellValue(record.getR49_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR49_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR49_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR49_agency_services() != null) {
						cell9.setCellValue(record.getR49_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR49_asset_management() != null) {
						cell10.setCellValue(record.getR49_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR49_retail_brokerage() != null) {
						cell11.setCellValue(record.getR49_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row50
					row = sheet.getRow(49);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR50_corporate_finance() != null) {
						cell4.setCellValue(record.getR50_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR50_trading_and_sales() != null) {
						cell5.setCellValue(record.getR50_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR50_retail_banking()  != null) {
						cell6.setCellValue(record.getR50_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR50_commercial_banking() != null) {
						cell7.setCellValue(record.getR50_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR50_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR50_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR50_agency_services() != null) {
						cell9.setCellValue(record.getR50_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR50_asset_management() != null) {
						cell10.setCellValue(record.getR50_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR50_retail_brokerage() != null) {
						cell11.setCellValue(record.getR50_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row51
					row = sheet.getRow(50);
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR51_corporate_finance() != null) {
						cell4.setCellValue(record.getR51_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR51_trading_and_sales() != null) {
						cell5.setCellValue(record.getR51_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR51_retail_banking()  != null) {
						cell6.setCellValue(record.getR51_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR51_commercial_banking() != null) {
						cell7.setCellValue(record.getR51_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR51_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR51_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR51_agency_services() != null) {
						cell9.setCellValue(record.getR51_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR51_asset_management() != null) {
						cell10.setCellValue(record.getR51_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR51_retail_brokerage() != null) {
						cell11.setCellValue(record.getR51_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row52
					row = sheet.getRow(51);
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR52_corporate_finance() != null) {
						cell4.setCellValue(record.getR52_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR52_trading_and_sales() != null) {
						cell5.setCellValue(record.getR52_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR52_retail_banking()  != null) {
						cell6.setCellValue(record.getR52_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR52_commercial_banking() != null) {
						cell7.setCellValue(record.getR52_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR52_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR52_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR52_agency_services() != null) {
						cell9.setCellValue(record.getR52_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR52_asset_management() != null) {
						cell10.setCellValue(record.getR52_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR52_retail_brokerage() != null) {
						cell11.setCellValue(record.getR52_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row53
					row = sheet.getRow(52);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR53_corporate_finance() != null) {
						cell4.setCellValue(record.getR53_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR53_trading_and_sales() != null) {
						cell5.setCellValue(record.getR53_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR53_retail_banking()  != null) {
						cell6.setCellValue(record.getR53_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR53_commercial_banking() != null) {
						cell7.setCellValue(record.getR53_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR53_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR53_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR53_agency_services() != null) {
						cell9.setCellValue(record.getR53_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR53_asset_management() != null) {
						cell10.setCellValue(record.getR53_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR53_retail_brokerage() != null) {
						cell11.setCellValue(record.getR53_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row54
					row = sheet.getRow(53);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR54_corporate_finance() != null) {
						cell4.setCellValue(record.getR54_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR54_trading_and_sales() != null) {
						cell5.setCellValue(record.getR54_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR54_retail_banking()  != null) {
						cell6.setCellValue(record.getR54_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR54_commercial_banking() != null) {
						cell7.setCellValue(record.getR54_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR54_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR54_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR54_agency_services() != null) {
						cell9.setCellValue(record.getR54_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR54_asset_management() != null) {
						cell10.setCellValue(record.getR54_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR54_retail_brokerage() != null) {
						cell11.setCellValue(record.getR54_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row55
					row = sheet.getRow(54);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR55_corporate_finance() != null) {
						cell4.setCellValue(record.getR55_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR55_trading_and_sales() != null) {
						cell5.setCellValue(record.getR55_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR55_retail_banking()  != null) {
						cell6.setCellValue(record.getR55_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR55_commercial_banking() != null) {
						cell7.setCellValue(record.getR55_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR55_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR55_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR55_agency_services() != null) {
						cell9.setCellValue(record.getR55_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR55_asset_management() != null) {
						cell10.setCellValue(record.getR55_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR55_retail_brokerage() != null) {
						cell11.setCellValue(record.getR55_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
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




	// Normal Email Excel
		public byte[] BRRS_M_OR2EmailExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Email Excel generation process in memory.");
			
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				try {
					// Redirecting to Archival
					return BRRS_M_OR2ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				logger.info("Service: Generating RESUB report for version {}", version);

				try {
					// ✅ Redirecting to Resub Excel
					return BRRS_M_OR2EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} 
			else {
			List<M_OR2_Summary_Entity> dataList = M_OR2_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_OR2 report. Returning empty result.");
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

				int startRow = 11;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_OR2_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

		//row12
						// Column C
						Cell cell4 = row.createCell(2);
						if (record.getR12_corporate_finance() != null) {
							cell4.setCellValue(record.getR12_corporate_finance().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}
						
						//row12
						// Column D
						Cell cell5 = row.createCell(3);
						if (record.getR12_trading_and_sales() != null) {
							cell5.setCellValue(record.getR12_trading_and_sales().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						} 
						
						
						//row12
						// Column E
						Cell cell6 = row.createCell(4);
						if (record.getR12_retail_banking() != null) {
							cell6.setCellValue(record.getR12_retail_banking().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}
						
						//row12
						// Column F
						Cell cell7 = row.createCell(5);
						if (record.getR12_commercial_banking()  != null) {
							cell7.setCellValue(record.getR12_commercial_banking().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}
						
						//row12
						// Column G
						Cell cell8 = row.createCell(6);
						if (record.getR12_payments_and_settlements()  != null) {
							cell8.setCellValue(record.getR12_payments_and_settlements().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}
						
						//row12
						// Column H
						Cell cell9 = row.createCell(7);
						if (record.getR12_agency_services() != null) {
							cell9.setCellValue(record.getR12_agency_services().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}
						
						
						//row12
						// Column I
						Cell cell10 = row.createCell(8);
						if (record.getR12_asset_management()  != null) {
							cell10.setCellValue(record.getR12_asset_management() .doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}
						
						//row12
						// Column J
						Cell cell11 = row.createCell(9);
						if (record.getR12_retail_brokerage() != null) {
							cell11.setCellValue(record.getR12_retail_brokerage().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						
						
						
						//row13
						row = sheet.getRow(12);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR13_corporate_finance() != null) {
				cell4.setCellValue(record.getR13_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row13
			// Column D
			cell5 = row.createCell(3);
			if (record.getR13_trading_and_sales() != null) {
				cell5.setCellValue(record.getR13_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row13
			// Column E
			cell6 = row.createCell(4);
			if (record.getR13_retail_banking() != null) {
				cell6.setCellValue(record.getR13_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row13
			// Column F
			cell7 = row.createCell(5);
			if (record.getR13_commercial_banking() != null) {
				cell7.setCellValue(record.getR13_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row13
			// Column G
			cell8 = row.createCell(6);
			if (record.getR13_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR13_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row13
			// Column H
			cell9 = row.createCell(7);
			if (record.getR13_agency_services() != null) {
				cell9.setCellValue(record.getR13_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row13
			// Column I
			cell10 = row.createCell(8);
			if (record.getR13_asset_management() != null) {
				cell10.setCellValue(record.getR13_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row13
			// Column J
			cell11 = row.createCell(9);
			if (record.getR13_retail_brokerage() != null) {
				cell11.setCellValue(record.getR13_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row14
			row = sheet.getRow(13);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR14_corporate_finance() != null) {
				cell4.setCellValue(record.getR14_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row14
			// Column D
			cell5 = row.createCell(3);
			if (record.getR14_trading_and_sales() != null) {
				cell5.setCellValue(record.getR14_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row14
			// Column E
			cell6 = row.createCell(4);
			if (record.getR14_retail_banking() != null) {
				cell6.setCellValue(record.getR14_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row14
			// Column F
			cell7 = row.createCell(5);
			if (record.getR14_commercial_banking() != null) {
				cell7.setCellValue(record.getR14_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row14
			// Column G
			cell8 = row.createCell(6);
			if (record.getR14_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR14_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row14
			// Column H
			cell9 = row.createCell(7);
			if (record.getR14_agency_services() != null) {
				cell9.setCellValue(record.getR14_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row14
			// Column I
			cell10 = row.createCell(8);
			if (record.getR14_asset_management() != null) {
				cell10.setCellValue(record.getR14_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row14
			// Column J
			cell11 = row.createCell(9);
			if (record.getR14_retail_brokerage() != null) {
				cell11.setCellValue(record.getR14_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row15
			row = sheet.getRow(14);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR15_corporate_finance() != null) {
				cell4.setCellValue(record.getR15_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row15
			// Column D
			cell5 = row.createCell(3);
			if (record.getR15_trading_and_sales() != null) {
				cell5.setCellValue(record.getR15_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row15
			// Column E
			cell6 = row.createCell(4);
			if (record.getR15_retail_banking() != null) {
				cell6.setCellValue(record.getR15_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row15
			// Column F
			cell7 = row.createCell(5);
			if (record.getR15_commercial_banking() != null) {
				cell7.setCellValue(record.getR15_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row15
			// Column G
			cell8 = row.createCell(6);
			if (record.getR15_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR15_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row15
			// Column H
			cell9 = row.createCell(7);
			if (record.getR15_agency_services() != null) {
				cell9.setCellValue(record.getR15_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row15
			// Column I
			cell10 = row.createCell(8);
			if (record.getR15_asset_management() != null) {
				cell10.setCellValue(record.getR15_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row15
			// Column J
			cell11 = row.createCell(9);
			if (record.getR15_retail_brokerage() != null) {
				cell11.setCellValue(record.getR15_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row16
			row = sheet.getRow(15);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR16_corporate_finance() != null) {
				cell4.setCellValue(record.getR16_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row16
			// Column D
			cell5 = row.createCell(3);
			if (record.getR16_trading_and_sales() != null) {
				cell5.setCellValue(record.getR16_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row16
			// Column E
			cell6 = row.createCell(4);
			if (record.getR16_retail_banking() != null) {
				cell6.setCellValue(record.getR16_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row16
			// Column F
			cell7 = row.createCell(5);
			if (record.getR16_commercial_banking() != null) {
				cell7.setCellValue(record.getR16_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row16
			// Column G
			cell8 = row.createCell(6);
			if (record.getR16_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR16_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row16
			// Column H
			cell9 = row.createCell(7);
			if (record.getR16_agency_services() != null) {
				cell9.setCellValue(record.getR16_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row16
			// Column I
			cell10 = row.createCell(8);
			if (record.getR16_asset_management() != null) {
				cell10.setCellValue(record.getR16_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row16
			// Column J
			cell11 = row.createCell(9);
			if (record.getR16_retail_brokerage() != null) {
				cell11.setCellValue(record.getR16_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row17
			row = sheet.getRow(16);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR17_corporate_finance() != null) {
				cell4.setCellValue(record.getR17_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row17
			// Column D
			cell5 = row.createCell(3);
			if (record.getR17_trading_and_sales() != null) {
				cell5.setCellValue(record.getR17_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row17
			// Column E
			cell6 = row.createCell(4);
			if (record.getR17_retail_banking() != null) {
				cell6.setCellValue(record.getR17_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row17
			// Column F
			cell7 = row.createCell(5);
			if (record.getR17_commercial_banking() != null) {
				cell7.setCellValue(record.getR17_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row17
			// Column G
			cell8 = row.createCell(6);
			if (record.getR17_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR17_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row17
			// Column H
			cell9 = row.createCell(7);
			if (record.getR17_agency_services() != null) {
				cell9.setCellValue(record.getR17_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row17
			// Column I
			cell10 = row.createCell(8);
			if (record.getR17_asset_management() != null) {
				cell10.setCellValue(record.getR17_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row17
			// Column J
			cell11 = row.createCell(9);
			if (record.getR17_retail_brokerage() != null) {
				cell11.setCellValue(record.getR17_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row18
			row = sheet.getRow(17);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR18_corporate_finance() != null) {
				cell4.setCellValue(record.getR18_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row18
			// Column D
			cell5 = row.createCell(3);
			if (record.getR18_trading_and_sales() != null) {
				cell5.setCellValue(record.getR18_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row18
			// Column E
			cell6 = row.createCell(4);
			if (record.getR18_retail_banking() != null) {
				cell6.setCellValue(record.getR18_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row18
			// Column F
			cell7 = row.createCell(5);
			if (record.getR18_commercial_banking() != null) {
				cell7.setCellValue(record.getR18_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row18
			// Column G
			cell8 = row.createCell(6);
			if (record.getR18_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR18_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row18
			// Column H
			cell9 = row.createCell(7);
			if (record.getR18_agency_services() != null) {
				cell9.setCellValue(record.getR18_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row18
			// Column I
			cell10 = row.createCell(8);
			if (record.getR18_asset_management() != null) {
				cell10.setCellValue(record.getR18_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row18
			// Column J
			cell11 = row.createCell(9);
			if (record.getR18_retail_brokerage() != null) {
				cell11.setCellValue(record.getR18_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row19
			row = sheet.getRow(18);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR19_corporate_finance() != null) {
				cell4.setCellValue(record.getR19_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row19
			// Column D
			cell5 = row.createCell(3);
			if (record.getR19_trading_and_sales() != null) {
				cell5.setCellValue(record.getR19_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row19
			// Column E
			cell6 = row.createCell(4);
			if (record.getR19_retail_banking() != null) {
				cell6.setCellValue(record.getR19_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row19
			// Column F
			cell7 = row.createCell(5);
			if (record.getR19_commercial_banking() != null) {
				cell7.setCellValue(record.getR19_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row19
			// Column G
			cell8 = row.createCell(6);
			if (record.getR19_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR19_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row19
			// Column H
			cell9 = row.createCell(7);
			if (record.getR19_agency_services() != null) {
				cell9.setCellValue(record.getR19_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row19
			// Column I
			cell10 = row.createCell(8);
			if (record.getR19_asset_management() != null) {
				cell10.setCellValue(record.getR19_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row19
			// Column J
			cell11 = row.createCell(9);
			if (record.getR19_retail_brokerage() != null) {
				cell11.setCellValue(record.getR19_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row20
			row = sheet.getRow(19);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR20_corporate_finance() != null) {
				cell4.setCellValue(record.getR20_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row20
			// Column D
			cell5 = row.createCell(3);
			if (record.getR20_trading_and_sales() != null) {
				cell5.setCellValue(record.getR20_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row20
			// Column E
			cell6 = row.createCell(4);
			if (record.getR20_retail_banking() != null) {
				cell6.setCellValue(record.getR20_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row20
			// Column F
			cell7 = row.createCell(5);
			if (record.getR20_commercial_banking() != null) {
				cell7.setCellValue(record.getR20_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row20
			// Column G
			cell8 = row.createCell(6);
			if (record.getR20_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR20_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row20
			// Column H
			cell9 = row.createCell(7);
			if (record.getR20_agency_services() != null) {
				cell9.setCellValue(record.getR20_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row20
			// Column I
			cell10 = row.createCell(8);
			if (record.getR20_asset_management() != null) {
				cell10.setCellValue(record.getR20_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row20
			// Column J
			cell11 = row.createCell(9);
			if (record.getR20_retail_brokerage() != null) {
				cell11.setCellValue(record.getR20_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row21
			row = sheet.getRow(20);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR21_corporate_finance() != null) {
				cell4.setCellValue(record.getR21_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row21
			// Column D
			cell5 = row.createCell(3);
			if (record.getR21_trading_and_sales() != null) {
				cell5.setCellValue(record.getR21_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row21
			// Column E
			cell6 = row.createCell(4);
			if (record.getR21_retail_banking() != null) {
				cell6.setCellValue(record.getR21_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row21
			// Column F
			cell7 = row.createCell(5);
			if (record.getR21_commercial_banking() != null) {
				cell7.setCellValue(record.getR21_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row21
			// Column G
			cell8 = row.createCell(6);
			if (record.getR21_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR21_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row21
			// Column H
			cell9 = row.createCell(7);
			if (record.getR21_agency_services() != null) {
				cell9.setCellValue(record.getR21_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row21
			// Column I
			cell10 = row.createCell(8);
			if (record.getR21_asset_management() != null) {
				cell10.setCellValue(record.getR21_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row21
			// Column J
			cell11 = row.createCell(9);
			if (record.getR21_retail_brokerage() != null) {
				cell11.setCellValue(record.getR21_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row22
			row = sheet.getRow(21);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR22_corporate_finance() != null) {
				cell4.setCellValue(record.getR22_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row22
			// Column D
			cell5 = row.createCell(3);
			if (record.getR22_trading_and_sales() != null) {
				cell5.setCellValue(record.getR22_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row22
			// Column E
			cell6 = row.createCell(4);
			if (record.getR22_retail_banking() != null) {
				cell6.setCellValue(record.getR22_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row22
			// Column F
			cell7 = row.createCell(5);
			if (record.getR22_commercial_banking() != null) {
				cell7.setCellValue(record.getR22_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row22
			// Column G
			cell8 = row.createCell(6);
			if (record.getR22_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR22_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row22
			// Column H
			cell9 = row.createCell(7);
			if (record.getR22_agency_services() != null) {
				cell9.setCellValue(record.getR22_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row22
			// Column I
			cell10 = row.createCell(8);
			if (record.getR22_asset_management() != null) {
				cell10.setCellValue(record.getR22_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row22
			// Column J
			cell11 = row.createCell(9);
			if (record.getR22_retail_brokerage() != null) {
				cell11.setCellValue(record.getR22_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row23
			row = sheet.getRow(22);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR23_corporate_finance() != null) {
				cell4.setCellValue(record.getR23_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row23
			// Column D
			cell5 = row.createCell(3);
			if (record.getR23_trading_and_sales() != null) {
				cell5.setCellValue(record.getR23_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row23
			// Column E
			cell6 = row.createCell(4);
			if (record.getR23_retail_banking() != null) {
				cell6.setCellValue(record.getR23_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row23
			// Column F
			cell7 = row.createCell(5);
			if (record.getR23_commercial_banking() != null) {
				cell7.setCellValue(record.getR23_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row23
			// Column G
			cell8 = row.createCell(6);
			if (record.getR23_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR23_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row23
			// Column H
			cell9 = row.createCell(7);
			if (record.getR23_agency_services() != null) {
				cell9.setCellValue(record.getR23_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row23
			// Column I
			cell10 = row.createCell(8);
			if (record.getR23_asset_management() != null) {
				cell10.setCellValue(record.getR23_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row23
			// Column J
			cell11 = row.createCell(9);
			if (record.getR23_retail_brokerage() != null) {
				cell11.setCellValue(record.getR23_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}
			
			
			
			
			
			
			
				//row24
				row = sheet.getRow(23);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR28_corporate_finance() != null) {
				cell4.setCellValue(record.getR28_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row28
			// Column D
			cell5 = row.createCell(3);
			if (record.getR28_trading_and_sales() != null) {
				cell5.setCellValue(record.getR28_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row28
			// Column E
			cell6 = row.createCell(4);
			if (record.getR28_retail_banking() != null) {
				cell6.setCellValue(record.getR28_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			
			  //row28 // Column F 
			cell7 = row.createCell(5); if
			  (record.getR28_commercial_banking() != null) {
			  cell7.setCellValue(record.getR28_commercial_banking().doubleValue());
			  cell7.setCellStyle(numberStyle); } else { cell7.setCellValue("");
			  cell7.setCellStyle(textStyle); }
			  
			  //row28 // Column G 
			cell8 = row.createCell(6); if
			  (record.getR28_payments_and_settlements() != null) {
			  cell8.setCellValue(record.getR28_payments_and_settlements().doubleValue());
			  cell8.setCellStyle(numberStyle); } else { cell8.setCellValue("");
			  cell8.setCellStyle(textStyle); }
			  
			  //row28 // Column H 
			cell9 = row.createCell(7); if
			  (record.getR28_agency_services() != null) {
			  cell9.setCellValue(record.getR28_agency_services().doubleValue());
			  cell9.setCellStyle(numberStyle); } else { cell9.setCellValue("");
			  cell9.setCellStyle(textStyle); }
			 

			//row28
			// Column I
			cell10 = row.createCell(8);
			if (record.getR28_asset_management() != null) {
				cell10.setCellValue(record.getR28_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row28
			// Column J
			cell11 = row.createCell(9);
			if (record.getR28_retail_brokerage() != null) {
				cell11.setCellValue(record.getR28_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row25
				row = sheet.getRow(24);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR29_corporate_finance() != null) {
				cell4.setCellValue(record.getR29_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row29
			// Column D
			cell5 = row.createCell(3);
			if (record.getR29_trading_and_sales() != null) {
				cell5.setCellValue(record.getR29_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row29
			// Column E
			cell6 = row.createCell(4);
			if (record.getR29_retail_banking() != null) {
				cell6.setCellValue(record.getR29_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row29
			// Column F
			cell7 = row.createCell(5);
			if (record.getR29_commercial_banking() != null) {
				cell7.setCellValue(record.getR29_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}
		
			//row29
			// Column G
			cell8 = row.createCell(6);
			if (record.getR29_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR29_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}
		
			//row29
			// Column H
			cell9 = row.createCell(7);
			if (record.getR29_agency_services() != null) {
				cell9.setCellValue(record.getR29_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row29
			// Column I
			cell10 = row.createCell(8);
			if (record.getR29_asset_management() != null) {
				cell10.setCellValue(record.getR29_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row29
			// Column J
			cell11 = row.createCell(9);
			if (record.getR29_retail_brokerage() != null) {
				cell11.setCellValue(record.getR29_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row26
				row = sheet.getRow(25);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR30_corporate_finance() != null) {
				cell4.setCellValue(record.getR30_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row30
			// Column D
			cell5 = row.createCell(3);
			if (record.getR30_trading_and_sales() != null) {
				cell5.setCellValue(record.getR30_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row30
			// Column E
			cell6 = row.createCell(4);
			if (record.getR30_retail_banking() != null) {
				cell6.setCellValue(record.getR30_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row30
			// Column F
			cell7 = row.createCell(5);
			if (record.getR30_commercial_banking() != null) {
				cell7.setCellValue(record.getR30_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}
		
			//row30
			// Column G
			cell8 = row.createCell(6);
			if (record.getR30_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR30_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}
		
			//row30
			// Column H
			cell9 = row.createCell(7);
			if (record.getR30_agency_services() != null) {
				cell9.setCellValue(record.getR30_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row30
			// Column I
			cell10 = row.createCell(8);
			if (record.getR30_asset_management() != null) {
				cell10.setCellValue(record.getR30_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row30
			// Column J
			cell11 = row.createCell(9);
			if (record.getR30_retail_brokerage() != null) {
				cell11.setCellValue(record.getR30_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row27
				row = sheet.getRow(26);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR31_corporate_finance() != null) {
				cell4.setCellValue(record.getR31_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row31
			// Column D
			cell5 = row.createCell(3);
			if (record.getR31_trading_and_sales() != null) {
				cell5.setCellValue(record.getR31_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row31
			// Column E
			cell6 = row.createCell(4);
			if (record.getR31_retail_banking() != null) {
				cell6.setCellValue(record.getR31_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row31
			// Column F
			cell7 = row.createCell(5);
			if (record.getR31_commercial_banking() != null) {
				cell7.setCellValue(record.getR31_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}
		
			//row31
			// Column G
			cell8 = row.createCell(6);
			if (record.getR31_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR31_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}
		
			//row31
			// Column H
			cell9 = row.createCell(7);
			if (record.getR31_agency_services() != null) {
				cell9.setCellValue(record.getR31_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row31
			// Column I
			cell10 = row.createCell(8);
			if (record.getR31_asset_management() != null) {
				cell10.setCellValue(record.getR31_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row31
			// Column J
			cell11 = row.createCell(9);
			if (record.getR31_retail_brokerage() != null) {
				cell11.setCellValue(record.getR31_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row28
				row = sheet.getRow(27);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR32_corporate_finance() != null) {
				cell4.setCellValue(record.getR32_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row32
			// Column D
			cell5 = row.createCell(3);
			if (record.getR32_trading_and_sales() != null) {
				cell5.setCellValue(record.getR32_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row32
			// Column E
			cell6 = row.createCell(4);
			if (record.getR32_retail_banking() != null) {
				cell6.setCellValue(record.getR32_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row32
			// Column F
			cell7 = row.createCell(5);
			if (record.getR32_commercial_banking() != null) {
				cell7.setCellValue(record.getR32_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}
		
			//row32
			// Column G
			cell8 = row.createCell(6);
			if (record.getR32_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR32_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}
		
			//row32
			// Column H
			cell9 = row.createCell(7);
			if (record.getR32_agency_services() != null) {
				cell9.setCellValue(record.getR32_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row32
			// Column I
			cell10 = row.createCell(8);
			if (record.getR32_asset_management() != null) {
				cell10.setCellValue(record.getR32_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row32
			// Column J
			cell11 = row.createCell(9);
			if (record.getR32_retail_brokerage() != null) {
				cell11.setCellValue(record.getR32_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

				//row29
				row = sheet.getRow(28);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR33_corporate_finance() != null) {
				cell4.setCellValue(record.getR33_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row33
			// Column D
			cell5 = row.createCell(3);
			if (record.getR33_trading_and_sales() != null) {
				cell5.setCellValue(record.getR33_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row33
			// Column E
			cell6 = row.createCell(4);
			if (record.getR33_retail_banking() != null) {
				cell6.setCellValue(record.getR33_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row33
			// Column F
			cell7 = row.createCell(5);
			if (record.getR33_commercial_banking() != null) {
				cell7.setCellValue(record.getR33_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}
		
			//row33
			// Column G
			cell8 = row.createCell(6);
			if (record.getR33_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR33_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}
		
			//row33
			// Column H
			cell9 = row.createCell(7);
			if (record.getR33_agency_services() != null) {
				cell9.setCellValue(record.getR33_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row33
			// Column I
			cell10 = row.createCell(8);
			if (record.getR33_asset_management() != null) {
				cell10.setCellValue(record.getR33_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row33
			// Column J
			cell11 = row.createCell(9);
			if (record.getR33_retail_brokerage() != null) {
				cell11.setCellValue(record.getR33_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

				//row30
				row = sheet.getRow(29);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR34_corporate_finance() != null) {
				cell4.setCellValue(record.getR34_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row34
			// Column D
			cell5 = row.createCell(3);
			if (record.getR34_trading_and_sales() != null) {
				cell5.setCellValue(record.getR34_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row34
			// Column E
			cell6 = row.createCell(4);
			if (record.getR34_retail_banking() != null) {
				cell6.setCellValue(record.getR34_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row34
			// Column F
			cell7 = row.createCell(5);
			if (record.getR34_commercial_banking() != null) {
				cell7.setCellValue(record.getR34_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row34
			// Column G
			cell8 = row.createCell(6);
			if (record.getR34_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR34_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row34
			// Column H
			cell9 = row.createCell(7);
			if (record.getR34_agency_services() != null) {
				cell9.setCellValue(record.getR34_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row34
			// Column I
			cell10 = row.createCell(8);
			if (record.getR34_asset_management() != null) {
				cell10.setCellValue(record.getR34_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row34
			// Column J
			cell11 = row.createCell(9);
			if (record.getR34_retail_brokerage() != null) {
				cell11.setCellValue(record.getR34_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			//row31
				row = sheet.getRow(30);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR35_corporate_finance() != null) {
				cell4.setCellValue(record.getR35_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row35
			// Column D
			cell5 = row.createCell(3);
			if (record.getR35_trading_and_sales() != null) {
				cell5.setCellValue(record.getR35_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row35
			// Column E
			cell6 = row.createCell(4);
			if (record.getR35_retail_banking() != null) {
				cell6.setCellValue(record.getR35_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row35
			// Column F
			cell7 = row.createCell(5);
			if (record.getR35_commercial_banking() != null) {
				cell7.setCellValue(record.getR35_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row35
			// Column G
			cell8 = row.createCell(6);
			if (record.getR35_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR35_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row35
			// Column H
			cell9 = row.createCell(7);
			if (record.getR35_agency_services() != null) {
				cell9.setCellValue(record.getR35_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row35
			// Column I
			cell10 = row.createCell(8);
			if (record.getR35_asset_management() != null) {
				cell10.setCellValue(record.getR35_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row35
			// Column J
			cell11 = row.createCell(9);
			if (record.getR35_retail_brokerage() != null) {
				cell11.setCellValue(record.getR35_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

				//row32
				row = sheet.getRow(31);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR36_corporate_finance() != null) {
				cell4.setCellValue(record.getR36_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row36
			// Column D
			cell5 = row.createCell(3);
			if (record.getR36_trading_and_sales() != null) {
				cell5.setCellValue(record.getR36_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row36
			// Column E
			cell6 = row.createCell(4);
			if (record.getR36_retail_banking() != null) {
				cell6.setCellValue(record.getR36_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row36
			// Column F
			cell7 = row.createCell(5);
			if (record.getR36_commercial_banking() != null) {
				cell7.setCellValue(record.getR36_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row36
			// Column G
			cell8 = row.createCell(6);
			if (record.getR36_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR36_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row36
			// Column H
			cell9 = row.createCell(7);
			if (record.getR36_agency_services() != null) {
				cell9.setCellValue(record.getR36_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row36
			// Column I
			cell10 = row.createCell(8);
			if (record.getR36_asset_management() != null) {
				cell10.setCellValue(record.getR36_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row36
			// Column J
			cell11 = row.createCell(9);
			if (record.getR36_retail_brokerage() != null) {
				cell11.setCellValue(record.getR36_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

		    //row33
			row = sheet.getRow(32);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR37_corporate_finance() != null) {
				cell4.setCellValue(record.getR37_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row37
			// Column D
			cell5 = row.createCell(3);
			if (record.getR37_trading_and_sales() != null) {
				cell5.setCellValue(record.getR37_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row37
			// Column E
			cell6 = row.createCell(4);
			if (record.getR37_retail_banking() != null) {
				cell6.setCellValue(record.getR37_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row37
			// Column F
			cell7 = row.createCell(5);
			if (record.getR37_commercial_banking() != null) {
				cell7.setCellValue(record.getR37_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row37
			// Column G
			cell8 = row.createCell(6);
			if (record.getR37_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR37_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row37
			// Column H
			cell9 = row.createCell(7);
			if (record.getR37_agency_services() != null) {
				cell9.setCellValue(record.getR37_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row37
			// Column I
			cell10 = row.createCell(8);
			if (record.getR37_asset_management() != null) {
				cell10.setCellValue(record.getR37_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row37
			// Column J
			cell11 = row.createCell(9);
			if (record.getR37_retail_brokerage() != null) {
				cell11.setCellValue(record.getR37_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

		 //row34
			row = sheet.getRow(33);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR38_corporate_finance() != null) {
				cell4.setCellValue(record.getR38_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row38
			// Column D
			cell5 = row.createCell(3);
			if (record.getR38_trading_and_sales() != null) {
				cell5.setCellValue(record.getR38_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row38
			// Column E
			cell6 = row.createCell(4);
			if (record.getR38_retail_banking() != null) {
				cell6.setCellValue(record.getR38_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row38
			// Column F
			cell7 = row.createCell(5);
			if (record.getR38_commercial_banking() != null) {
				cell7.setCellValue(record.getR38_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row38
			// Column G
			cell8 = row.createCell(6);
			if (record.getR38_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR38_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row38
			// Column H
			cell9 = row.createCell(7);
			if (record.getR38_agency_services() != null) {
				cell9.setCellValue(record.getR38_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row38
			// Column I
			cell10 = row.createCell(8);
			if (record.getR38_asset_management() != null) {
				cell10.setCellValue(record.getR38_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row38
			// Column J
			cell11 = row.createCell(9);
			if (record.getR38_retail_brokerage() != null) {
				cell11.setCellValue(record.getR38_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			 //row35
			row = sheet.getRow(34);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR39_corporate_finance() != null) {
				cell4.setCellValue(record.getR39_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row39
			// Column D
			cell5 = row.createCell(3);
			if (record.getR39_trading_and_sales() != null) {
				cell5.setCellValue(record.getR39_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row39
			// Column E
			cell6 = row.createCell(4);
			if (record.getR39_retail_banking() != null) {
				cell6.setCellValue(record.getR39_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row39
			// Column F
			cell7 = row.createCell(5);
			if (record.getR39_commercial_banking() != null) {
				cell7.setCellValue(record.getR39_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row39
			// Column G
			cell8 = row.createCell(6);
			if (record.getR39_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR39_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row39
			// Column H
			cell9 = row.createCell(7);
			if (record.getR39_agency_services() != null) {
				cell9.setCellValue(record.getR39_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row39
			// Column I
			cell10 = row.createCell(8);
			if (record.getR39_asset_management() != null) {
				cell10.setCellValue(record.getR39_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row39
			// Column J
			cell11 = row.createCell(9);
			if (record.getR39_retail_brokerage() != null) {
				cell11.setCellValue(record.getR39_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			
			 //row36
			row = sheet.getRow(35);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR44_corporate_finance() != null) {
				cell4.setCellValue(record.getR44_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row44
			// Column D
			cell5 = row.createCell(3);
			if (record.getR44_trading_and_sales() != null) {
				cell5.setCellValue(record.getR44_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row44
			// Column E
			cell6 = row.createCell(4);
			if (record.getR44_retail_banking() != null) {
				cell6.setCellValue(record.getR44_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row44
			// Column F
			cell7 = row.createCell(5);
			if (record.getR44_commercial_banking() != null) {
				cell7.setCellValue(record.getR44_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row44
			// Column G
			cell8 = row.createCell(6);
			if (record.getR44_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR44_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row44
			// Column H
			cell9 = row.createCell(7);
			if (record.getR44_agency_services() != null) {
				cell9.setCellValue(record.getR44_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row44
			// Column I
			cell10 = row.createCell(8);
			if (record.getR44_asset_management() != null) {
				cell10.setCellValue(record.getR44_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row44
			// Column J
			cell11 = row.createCell(9);
			if (record.getR44_retail_brokerage() != null) {
				cell11.setCellValue(record.getR44_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			 //row37
			row = sheet.getRow(36);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR45_corporate_finance() != null) {
				cell4.setCellValue(record.getR45_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row45
			// Column D
			cell5 = row.createCell(3);
			if (record.getR45_trading_and_sales() != null) {
				cell5.setCellValue(record.getR45_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row45
			// Column E
			cell6 = row.createCell(4);
			if (record.getR45_retail_banking() != null) {
				cell6.setCellValue(record.getR45_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row45
			// Column F
			cell7 = row.createCell(5);
			if (record.getR45_commercial_banking() != null) {
				cell7.setCellValue(record.getR45_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row45
			// Column G
			cell8 = row.createCell(6);
			if (record.getR45_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR45_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row45
			// Column H
			cell9 = row.createCell(7);
			if (record.getR45_agency_services() != null) {
				cell9.setCellValue(record.getR45_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row45
			// Column I
			cell10 = row.createCell(8);
			if (record.getR45_asset_management() != null) {
				cell10.setCellValue(record.getR45_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row45
			// Column J
			cell11 = row.createCell(9);
			if (record.getR45_retail_brokerage() != null) {
				cell11.setCellValue(record.getR45_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

		 //row38
			row = sheet.getRow(37);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR46_corporate_finance() != null) {
				cell4.setCellValue(record.getR46_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row46
			// Column D
			cell5 = row.createCell(3);
			if (record.getR46_trading_and_sales() != null) {
				cell5.setCellValue(record.getR46_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row46
			// Column E
			cell6 = row.createCell(4);
			if (record.getR46_retail_banking() != null) {
				cell6.setCellValue(record.getR46_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row46
			// Column F
			cell7 = row.createCell(5);
			if (record.getR46_commercial_banking() != null) {
				cell7.setCellValue(record.getR46_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row46
			// Column G
			cell8 = row.createCell(6);
			if (record.getR46_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR46_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row46
			// Column H
			cell9 = row.createCell(7);
			if (record.getR46_agency_services() != null) {
				cell9.setCellValue(record.getR46_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row46
			// Column I
			cell10 = row.createCell(8);
			if (record.getR46_asset_management() != null) {
				cell10.setCellValue(record.getR46_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row46
			// Column J
			cell11 = row.createCell(9);
			if (record.getR46_retail_brokerage() != null) {
				cell11.setCellValue(record.getR46_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			 //row39
			row = sheet.getRow(38);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR47_corporate_finance() != null) {
				cell4.setCellValue(record.getR47_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row47
			// Column D
			cell5 = row.createCell(3);
			if (record.getR47_trading_and_sales() != null) {
				cell5.setCellValue(record.getR47_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row47
			// Column E
			cell6 = row.createCell(4);
			if (record.getR47_retail_banking() != null) {
				cell6.setCellValue(record.getR47_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row47
			// Column F
			cell7 = row.createCell(5);
			if (record.getR47_commercial_banking() != null) {
				cell7.setCellValue(record.getR47_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row47
			// Column G
			cell8 = row.createCell(6);
			if (record.getR47_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR47_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row47
			// Column H
			cell9 = row.createCell(7);
			if (record.getR47_agency_services() != null) {
				cell9.setCellValue(record.getR47_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row47
			// Column I
			cell10 = row.createCell(8);
			if (record.getR47_asset_management() != null) {
				cell10.setCellValue(record.getR47_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row47
			// Column J
			cell11 = row.createCell(9);
			if (record.getR47_retail_brokerage() != null) {
				cell11.setCellValue(record.getR47_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

		 //row40
			row = sheet.getRow(39);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR48_corporate_finance() != null) {
				cell4.setCellValue(record.getR48_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row48
			// Column D
			cell5 = row.createCell(3);
			if (record.getR48_trading_and_sales() != null) {
				cell5.setCellValue(record.getR48_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row48
			// Column E
			cell6 = row.createCell(4);
			if (record.getR48_retail_banking() != null) {
				cell6.setCellValue(record.getR48_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row48
			// Column F
			cell7 = row.createCell(5);
			if (record.getR48_commercial_banking() != null) {
				cell7.setCellValue(record.getR48_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row48
			// Column G
			cell8 = row.createCell(6);
			if (record.getR48_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR48_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row48
			// Column H
			cell9 = row.createCell(7);
			if (record.getR48_agency_services() != null) {
				cell9.setCellValue(record.getR48_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row48
			// Column I
			cell10 = row.createCell(8);
			if (record.getR48_asset_management() != null) {
				cell10.setCellValue(record.getR48_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row48
			// Column J
			cell11 = row.createCell(9);
			if (record.getR48_retail_brokerage() != null) {
				cell11.setCellValue(record.getR48_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			 //row41
			row = sheet.getRow(40);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR49_corporate_finance() != null) {
				cell4.setCellValue(record.getR49_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row49
			// Column D
			cell5 = row.createCell(3);
			if (record.getR49_trading_and_sales() != null) {
				cell5.setCellValue(record.getR49_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row49
			// Column E
			cell6 = row.createCell(4);
			if (record.getR49_retail_banking() != null) {
				cell6.setCellValue(record.getR49_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row49
			// Column F
			cell7 = row.createCell(5);
			if (record.getR49_commercial_banking() != null) {
				cell7.setCellValue(record.getR49_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row49
			// Column G
			cell8 = row.createCell(6);
			if (record.getR49_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR49_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row49
			// Column H
			cell9 = row.createCell(7);
			if (record.getR49_agency_services() != null) {
				cell9.setCellValue(record.getR49_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row49
			// Column I
			cell10 = row.createCell(8);
			if (record.getR49_asset_management() != null) {
				cell10.setCellValue(record.getR49_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row49
			// Column J
			cell11 = row.createCell(9);
			if (record.getR49_retail_brokerage() != null) {
				cell11.setCellValue(record.getR49_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

				 //row42
			row = sheet.getRow(41);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR50_corporate_finance() != null) {
				cell4.setCellValue(record.getR50_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row50
			// Column D
			cell5 = row.createCell(3);
			if (record.getR50_trading_and_sales() != null) {
				cell5.setCellValue(record.getR50_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row50
			// Column E
			cell6 = row.createCell(4);
			if (record.getR50_retail_banking() != null) {
				cell6.setCellValue(record.getR50_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row50
			// Column F
			cell7 = row.createCell(5);
			if (record.getR50_commercial_banking() != null) {
				cell7.setCellValue(record.getR50_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row50
			// Column G
			cell8 = row.createCell(6);
			if (record.getR50_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR50_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row50
			// Column H
			cell9 = row.createCell(7);
			if (record.getR50_agency_services() != null) {
				cell9.setCellValue(record.getR50_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row50
			// Column I
			cell10 = row.createCell(8);
			if (record.getR50_asset_management() != null) {
				cell10.setCellValue(record.getR50_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row50
			// Column J
			cell11 = row.createCell(9);
			if (record.getR50_retail_brokerage() != null) {
				cell11.setCellValue(record.getR50_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			 //row43
			row = sheet.getRow(42);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR51_corporate_finance() != null) {
				cell4.setCellValue(record.getR51_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row51
			// Column D
			cell5 = row.createCell(3);
			if (record.getR51_trading_and_sales() != null) {
				cell5.setCellValue(record.getR51_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row51
			// Column E
			cell6 = row.createCell(4);
			if (record.getR51_retail_banking() != null) {
				cell6.setCellValue(record.getR51_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row51
			// Column F
			cell7 = row.createCell(5);
			if (record.getR51_commercial_banking() != null) {
				cell7.setCellValue(record.getR51_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row51
			// Column G
			cell8 = row.createCell(6);
			if (record.getR51_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR51_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row51
			// Column H
			cell9 = row.createCell(7);
			if (record.getR51_agency_services() != null) {
				cell9.setCellValue(record.getR51_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row51
			// Column I
			cell10 = row.createCell(8);
			if (record.getR51_asset_management() != null) {
				cell10.setCellValue(record.getR51_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row51
			// Column J
			cell11 = row.createCell(9);
			if (record.getR51_retail_brokerage() != null) {
				cell11.setCellValue(record.getR51_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			 //row44
			row = sheet.getRow(43);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR52_corporate_finance() != null) {
				cell4.setCellValue(record.getR52_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row52
			// Column D
			cell5 = row.createCell(3);
			if (record.getR52_trading_and_sales() != null) {
				cell5.setCellValue(record.getR52_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row52
			// Column E
			cell6 = row.createCell(4);
			if (record.getR52_retail_banking() != null) {
				cell6.setCellValue(record.getR52_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row52
			// Column F
			cell7 = row.createCell(5);
			if (record.getR52_commercial_banking() != null) {
				cell7.setCellValue(record.getR52_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row52
			// Column G
			cell8 = row.createCell(6);
			if (record.getR52_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR52_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row52
			// Column H
			cell9 = row.createCell(7);
			if (record.getR52_agency_services() != null) {
				cell9.setCellValue(record.getR52_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row52
			// Column I
			cell10 = row.createCell(8);
			if (record.getR52_asset_management() != null) {
				cell10.setCellValue(record.getR52_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row52
			// Column J
			cell11 = row.createCell(9);
			if (record.getR52_retail_brokerage() != null) {
				cell11.setCellValue(record.getR52_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			 //row45
			row = sheet.getRow(44);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR53_corporate_finance() != null) {
				cell4.setCellValue(record.getR53_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row53
			// Column D
			cell5 = row.createCell(3);
			if (record.getR53_trading_and_sales() != null) {
				cell5.setCellValue(record.getR53_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row53
			// Column E
			cell6 = row.createCell(4);
			if (record.getR53_retail_banking() != null) {
				cell6.setCellValue(record.getR53_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row53
			// Column F
			cell7 = row.createCell(5);
			if (record.getR53_commercial_banking() != null) {
				cell7.setCellValue(record.getR53_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row53
			// Column G
			cell8 = row.createCell(6);
			if (record.getR53_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR53_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row53
			// Column H
			cell9 = row.createCell(7);
			if (record.getR53_agency_services() != null) {
				cell9.setCellValue(record.getR53_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row53
			// Column I
			cell10 = row.createCell(8);
			if (record.getR53_asset_management() != null) {
				cell10.setCellValue(record.getR53_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row53
			// Column J
			cell11 = row.createCell(9);
			if (record.getR53_retail_brokerage() != null) {
				cell11.setCellValue(record.getR53_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			 //row46
			row = sheet.getRow(45);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR54_corporate_finance() != null) {
				cell4.setCellValue(record.getR54_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row54
			// Column D
			cell5 = row.createCell(3);
			if (record.getR54_trading_and_sales() != null) {
				cell5.setCellValue(record.getR54_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row54
			// Column E
			cell6 = row.createCell(4);
			if (record.getR54_retail_banking() != null) {
				cell6.setCellValue(record.getR54_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row54
			// Column F
			cell7 = row.createCell(5);
			if (record.getR54_commercial_banking() != null) {
				cell7.setCellValue(record.getR54_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row54
			// Column G
			cell8 = row.createCell(6);
			if (record.getR54_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR54_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row54
			// Column H
			cell9 = row.createCell(7);
			if (record.getR54_agency_services() != null) {
				cell9.setCellValue(record.getR54_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row54
			// Column I
			cell10 = row.createCell(8);
			if (record.getR54_asset_management() != null) {
				cell10.setCellValue(record.getR54_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row54
			// Column J
			cell11 = row.createCell(9);
			if (record.getR54_retail_brokerage() != null) {
				cell11.setCellValue(record.getR54_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
			}

			 //row47
			row = sheet.getRow(46);
			// Column C
			cell4 = row.createCell(2);
			if (record.getR55_corporate_finance() != null) {
				cell4.setCellValue(record.getR55_corporate_finance().doubleValue());
				cell4.setCellStyle(numberStyle);
			} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
			}

			//row55
			// Column D
			cell5 = row.createCell(3);
			if (record.getR55_trading_and_sales() != null) {
				cell5.setCellValue(record.getR55_trading_and_sales().doubleValue());
				cell5.setCellStyle(numberStyle);
			} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
			}

			//row55
			// Column E
			cell6 = row.createCell(4);
			if (record.getR55_retail_banking() != null) {
				cell6.setCellValue(record.getR55_retail_banking().doubleValue());
				cell6.setCellStyle(numberStyle);
			} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
			}

			//row55
			// Column F
			cell7 = row.createCell(5);
			if (record.getR55_commercial_banking() != null) {
				cell7.setCellValue(record.getR55_commercial_banking().doubleValue());
				cell7.setCellStyle(numberStyle);
			} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
			}

			//row55
			// Column G
			cell8 = row.createCell(6);
			if (record.getR55_payments_and_settlements() != null) {
				cell8.setCellValue(record.getR55_payments_and_settlements().doubleValue());
				cell8.setCellStyle(numberStyle);
			} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
			}

			//row55
			// Column H
			cell9 = row.createCell(7);
			if (record.getR55_agency_services() != null) {
				cell9.setCellValue(record.getR55_agency_services().doubleValue());
				cell9.setCellStyle(numberStyle);
			} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
			}

			//row55
			// Column I
			cell10 = row.createCell(8);
			if (record.getR55_asset_management() != null) {
				cell10.setCellValue(record.getR55_asset_management().doubleValue());
				cell10.setCellStyle(numberStyle);
			} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
			}

			//row55
			// Column J
			cell11 = row.createCell(9);
			if (record.getR55_retail_brokerage() != null) {
				cell11.setCellValue(record.getR55_retail_brokerage().doubleValue());
				cell11.setCellStyle(numberStyle);
			} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
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
		}
		
		




		// Archival Email Excel
		public byte[] BRRS_M_OR2ARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Archival Email Excel generation process in memory.");

			List<M_OR2_Archival_Summary_Entity> dataList = M_OR2_Archival_Summary_Repo
					.getdatabydateListarchival(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_OR2 report. Returning empty result.");
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

				int startRow = 11;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_OR2_Archival_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}


						//row12
					// Column C
					Cell cell4 = row.createCell(2);
					if (record.getR12_corporate_finance() != null) {
						cell4.setCellValue(record.getR12_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row12
					// Column D
					Cell cell5 = row.createCell(3);
					if (record.getR12_trading_and_sales() != null) {
						cell5.setCellValue(record.getR12_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					} 
					
					
					//row12
					// Column E
					Cell cell6 = row.createCell(4);
					if (record.getR12_retail_banking() != null) {
						cell6.setCellValue(record.getR12_retail_banking().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					//row12
					// Column F
					Cell cell7 = row.createCell(5);
					if (record.getR12_commercial_banking()  != null) {
						cell7.setCellValue(record.getR12_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					//row12
					// Column G
					Cell cell8 = row.createCell(6);
					if (record.getR12_payments_and_settlements()  != null) {
						cell8.setCellValue(record.getR12_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					//row12
					// Column H
					Cell cell9 = row.createCell(7);
					if (record.getR12_agency_services() != null) {
						cell9.setCellValue(record.getR12_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					//row12
					// Column I
					Cell cell10 = row.createCell(8);
					if (record.getR12_asset_management()  != null) {
						cell10.setCellValue(record.getR12_asset_management() .doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					//row12
					// Column J
					Cell cell11 = row.createCell(9);
					if (record.getR12_retail_brokerage() != null) {
						cell11.setCellValue(record.getR12_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					
					//row13
					row = sheet.getRow(12);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR13_corporate_finance() != null) {
			cell4.setCellValue(record.getR13_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row13
		// Column D
		cell5 = row.createCell(3);
		if (record.getR13_trading_and_sales() != null) {
			cell5.setCellValue(record.getR13_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row13
		// Column E
		cell6 = row.createCell(4);
		if (record.getR13_retail_banking() != null) {
			cell6.setCellValue(record.getR13_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row13
		// Column F
		cell7 = row.createCell(5);
		if (record.getR13_commercial_banking() != null) {
			cell7.setCellValue(record.getR13_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row13
		// Column G
		cell8 = row.createCell(6);
		if (record.getR13_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR13_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row13
		// Column H
		cell9 = row.createCell(7);
		if (record.getR13_agency_services() != null) {
			cell9.setCellValue(record.getR13_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row13
		// Column I
		cell10 = row.createCell(8);
		if (record.getR13_asset_management() != null) {
			cell10.setCellValue(record.getR13_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row13
		// Column J
		cell11 = row.createCell(9);
		if (record.getR13_retail_brokerage() != null) {
			cell11.setCellValue(record.getR13_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row14
		row = sheet.getRow(13);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR14_corporate_finance() != null) {
			cell4.setCellValue(record.getR14_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row14
		// Column D
		cell5 = row.createCell(3);
		if (record.getR14_trading_and_sales() != null) {
			cell5.setCellValue(record.getR14_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row14
		// Column E
		cell6 = row.createCell(4);
		if (record.getR14_retail_banking() != null) {
			cell6.setCellValue(record.getR14_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row14
		// Column F
		cell7 = row.createCell(5);
		if (record.getR14_commercial_banking() != null) {
			cell7.setCellValue(record.getR14_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row14
		// Column G
		cell8 = row.createCell(6);
		if (record.getR14_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR14_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row14
		// Column H
		cell9 = row.createCell(7);
		if (record.getR14_agency_services() != null) {
			cell9.setCellValue(record.getR14_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row14
		// Column I
		cell10 = row.createCell(8);
		if (record.getR14_asset_management() != null) {
			cell10.setCellValue(record.getR14_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row14
		// Column J
		cell11 = row.createCell(9);
		if (record.getR14_retail_brokerage() != null) {
			cell11.setCellValue(record.getR14_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row15
		row = sheet.getRow(14);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR15_corporate_finance() != null) {
			cell4.setCellValue(record.getR15_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row15
		// Column D
		cell5 = row.createCell(3);
		if (record.getR15_trading_and_sales() != null) {
			cell5.setCellValue(record.getR15_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row15
		// Column E
		cell6 = row.createCell(4);
		if (record.getR15_retail_banking() != null) {
			cell6.setCellValue(record.getR15_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row15
		// Column F
		cell7 = row.createCell(5);
		if (record.getR15_commercial_banking() != null) {
			cell7.setCellValue(record.getR15_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row15
		// Column G
		cell8 = row.createCell(6);
		if (record.getR15_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR15_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row15
		// Column H
		cell9 = row.createCell(7);
		if (record.getR15_agency_services() != null) {
			cell9.setCellValue(record.getR15_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row15
		// Column I
		cell10 = row.createCell(8);
		if (record.getR15_asset_management() != null) {
			cell10.setCellValue(record.getR15_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row15
		// Column J
		cell11 = row.createCell(9);
		if (record.getR15_retail_brokerage() != null) {
			cell11.setCellValue(record.getR15_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row16
		row = sheet.getRow(15);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR16_corporate_finance() != null) {
			cell4.setCellValue(record.getR16_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row16
		// Column D
		cell5 = row.createCell(3);
		if (record.getR16_trading_and_sales() != null) {
			cell5.setCellValue(record.getR16_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row16
		// Column E
		cell6 = row.createCell(4);
		if (record.getR16_retail_banking() != null) {
			cell6.setCellValue(record.getR16_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row16
		// Column F
		cell7 = row.createCell(5);
		if (record.getR16_commercial_banking() != null) {
			cell7.setCellValue(record.getR16_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row16
		// Column G
		cell8 = row.createCell(6);
		if (record.getR16_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR16_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row16
		// Column H
		cell9 = row.createCell(7);
		if (record.getR16_agency_services() != null) {
			cell9.setCellValue(record.getR16_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row16
		// Column I
		cell10 = row.createCell(8);
		if (record.getR16_asset_management() != null) {
			cell10.setCellValue(record.getR16_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row16
		// Column J
		cell11 = row.createCell(9);
		if (record.getR16_retail_brokerage() != null) {
			cell11.setCellValue(record.getR16_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row17
		row = sheet.getRow(16);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR17_corporate_finance() != null) {
			cell4.setCellValue(record.getR17_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row17
		// Column D
		cell5 = row.createCell(3);
		if (record.getR17_trading_and_sales() != null) {
			cell5.setCellValue(record.getR17_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row17
		// Column E
		cell6 = row.createCell(4);
		if (record.getR17_retail_banking() != null) {
			cell6.setCellValue(record.getR17_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row17
		// Column F
		cell7 = row.createCell(5);
		if (record.getR17_commercial_banking() != null) {
			cell7.setCellValue(record.getR17_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row17
		// Column G
		cell8 = row.createCell(6);
		if (record.getR17_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR17_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row17
		// Column H
		cell9 = row.createCell(7);
		if (record.getR17_agency_services() != null) {
			cell9.setCellValue(record.getR17_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row17
		// Column I
		cell10 = row.createCell(8);
		if (record.getR17_asset_management() != null) {
			cell10.setCellValue(record.getR17_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row17
		// Column J
		cell11 = row.createCell(9);
		if (record.getR17_retail_brokerage() != null) {
			cell11.setCellValue(record.getR17_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row18
		row = sheet.getRow(17);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR18_corporate_finance() != null) {
			cell4.setCellValue(record.getR18_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row18
		// Column D
		cell5 = row.createCell(3);
		if (record.getR18_trading_and_sales() != null) {
			cell5.setCellValue(record.getR18_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row18
		// Column E
		cell6 = row.createCell(4);
		if (record.getR18_retail_banking() != null) {
			cell6.setCellValue(record.getR18_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row18
		// Column F
		cell7 = row.createCell(5);
		if (record.getR18_commercial_banking() != null) {
			cell7.setCellValue(record.getR18_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row18
		// Column G
		cell8 = row.createCell(6);
		if (record.getR18_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR18_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row18
		// Column H
		cell9 = row.createCell(7);
		if (record.getR18_agency_services() != null) {
			cell9.setCellValue(record.getR18_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row18
		// Column I
		cell10 = row.createCell(8);
		if (record.getR18_asset_management() != null) {
			cell10.setCellValue(record.getR18_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row18
		// Column J
		cell11 = row.createCell(9);
		if (record.getR18_retail_brokerage() != null) {
			cell11.setCellValue(record.getR18_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row19
		row = sheet.getRow(18);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR19_corporate_finance() != null) {
			cell4.setCellValue(record.getR19_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row19
		// Column D
		cell5 = row.createCell(3);
		if (record.getR19_trading_and_sales() != null) {
			cell5.setCellValue(record.getR19_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row19
		// Column E
		cell6 = row.createCell(4);
		if (record.getR19_retail_banking() != null) {
			cell6.setCellValue(record.getR19_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row19
		// Column F
		cell7 = row.createCell(5);
		if (record.getR19_commercial_banking() != null) {
			cell7.setCellValue(record.getR19_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row19
		// Column G
		cell8 = row.createCell(6);
		if (record.getR19_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR19_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row19
		// Column H
		cell9 = row.createCell(7);
		if (record.getR19_agency_services() != null) {
			cell9.setCellValue(record.getR19_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row19
		// Column I
		cell10 = row.createCell(8);
		if (record.getR19_asset_management() != null) {
			cell10.setCellValue(record.getR19_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row19
		// Column J
		cell11 = row.createCell(9);
		if (record.getR19_retail_brokerage() != null) {
			cell11.setCellValue(record.getR19_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row20
		row = sheet.getRow(19);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR20_corporate_finance() != null) {
			cell4.setCellValue(record.getR20_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row20
		// Column D
		cell5 = row.createCell(3);
		if (record.getR20_trading_and_sales() != null) {
			cell5.setCellValue(record.getR20_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row20
		// Column E
		cell6 = row.createCell(4);
		if (record.getR20_retail_banking() != null) {
			cell6.setCellValue(record.getR20_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row20
		// Column F
		cell7 = row.createCell(5);
		if (record.getR20_commercial_banking() != null) {
			cell7.setCellValue(record.getR20_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row20
		// Column G
		cell8 = row.createCell(6);
		if (record.getR20_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR20_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row20
		// Column H
		cell9 = row.createCell(7);
		if (record.getR20_agency_services() != null) {
			cell9.setCellValue(record.getR20_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row20
		// Column I
		cell10 = row.createCell(8);
		if (record.getR20_asset_management() != null) {
			cell10.setCellValue(record.getR20_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row20
		// Column J
		cell11 = row.createCell(9);
		if (record.getR20_retail_brokerage() != null) {
			cell11.setCellValue(record.getR20_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row21
		row = sheet.getRow(20);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR21_corporate_finance() != null) {
			cell4.setCellValue(record.getR21_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row21
		// Column D
		cell5 = row.createCell(3);
		if (record.getR21_trading_and_sales() != null) {
			cell5.setCellValue(record.getR21_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row21
		// Column E
		cell6 = row.createCell(4);
		if (record.getR21_retail_banking() != null) {
			cell6.setCellValue(record.getR21_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row21
		// Column F
		cell7 = row.createCell(5);
		if (record.getR21_commercial_banking() != null) {
			cell7.setCellValue(record.getR21_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row21
		// Column G
		cell8 = row.createCell(6);
		if (record.getR21_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR21_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row21
		// Column H
		cell9 = row.createCell(7);
		if (record.getR21_agency_services() != null) {
			cell9.setCellValue(record.getR21_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row21
		// Column I
		cell10 = row.createCell(8);
		if (record.getR21_asset_management() != null) {
			cell10.setCellValue(record.getR21_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row21
		// Column J
		cell11 = row.createCell(9);
		if (record.getR21_retail_brokerage() != null) {
			cell11.setCellValue(record.getR21_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row22
		row = sheet.getRow(21);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR22_corporate_finance() != null) {
			cell4.setCellValue(record.getR22_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row22
		// Column D
		cell5 = row.createCell(3);
		if (record.getR22_trading_and_sales() != null) {
			cell5.setCellValue(record.getR22_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row22
		// Column E
		cell6 = row.createCell(4);
		if (record.getR22_retail_banking() != null) {
			cell6.setCellValue(record.getR22_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row22
		// Column F
		cell7 = row.createCell(5);
		if (record.getR22_commercial_banking() != null) {
			cell7.setCellValue(record.getR22_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row22
		// Column G
		cell8 = row.createCell(6);
		if (record.getR22_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR22_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row22
		// Column H
		cell9 = row.createCell(7);
		if (record.getR22_agency_services() != null) {
			cell9.setCellValue(record.getR22_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row22
		// Column I
		cell10 = row.createCell(8);
		if (record.getR22_asset_management() != null) {
			cell10.setCellValue(record.getR22_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row22
		// Column J
		cell11 = row.createCell(9);
		if (record.getR22_retail_brokerage() != null) {
			cell11.setCellValue(record.getR22_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row23
		row = sheet.getRow(22);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR23_corporate_finance() != null) {
			cell4.setCellValue(record.getR23_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row23
		// Column D
		cell5 = row.createCell(3);
		if (record.getR23_trading_and_sales() != null) {
			cell5.setCellValue(record.getR23_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row23
		// Column E
		cell6 = row.createCell(4);
		if (record.getR23_retail_banking() != null) {
			cell6.setCellValue(record.getR23_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row23
		// Column F
		cell7 = row.createCell(5);
		if (record.getR23_commercial_banking() != null) {
			cell7.setCellValue(record.getR23_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row23
		// Column G
		cell8 = row.createCell(6);
		if (record.getR23_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR23_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row23
		// Column H
		cell9 = row.createCell(7);
		if (record.getR23_agency_services() != null) {
			cell9.setCellValue(record.getR23_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row23
		// Column I
		cell10 = row.createCell(8);
		if (record.getR23_asset_management() != null) {
			cell10.setCellValue(record.getR23_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row23
		// Column J
		cell11 = row.createCell(9);
		if (record.getR23_retail_brokerage() != null) {
			cell11.setCellValue(record.getR23_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
			//row24
			row = sheet.getRow(23);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR28_corporate_finance() != null) {
			cell4.setCellValue(record.getR28_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row28
		// Column D
		cell5 = row.createCell(3);
		if (record.getR28_trading_and_sales() != null) {
			cell5.setCellValue(record.getR28_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row28
		// Column E
		cell6 = row.createCell(4);
		if (record.getR28_retail_banking() != null) {
			cell6.setCellValue(record.getR28_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		 cell7 = row.createCell(6);
		if (record.getR28_commercial_banking() != null) {
			cell7.setCellValue(record.getR28_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR28_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR28_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR28_agency_services() != null) {
			cell9.setCellValue(record.getR28_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		

		//row28
		// Column I
		cell10 = row.createCell(8);
		if (record.getR28_asset_management() != null) {
			cell10.setCellValue(record.getR28_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row28
		// Column J
		cell11 = row.createCell(9);
		if (record.getR28_retail_brokerage() != null) {
			cell11.setCellValue(record.getR28_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row25
			row = sheet.getRow(24);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR29_corporate_finance() != null) {
			cell4.setCellValue(record.getR29_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row29
		// Column D
		cell5 = row.createCell(3);
		if (record.getR29_trading_and_sales() != null) {
			cell5.setCellValue(record.getR29_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row29
		// Column E
		cell6 = row.createCell(4);
		if (record.getR29_retail_banking() != null) {
			cell6.setCellValue(record.getR29_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row29
		// Column F
		cell7 = row.createCell(5);
		if (record.getR29_commercial_banking() != null) {
			cell7.setCellValue(record.getR29_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
	
		//row29
		// Column G
		cell8 = row.createCell(6);
		if (record.getR29_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR29_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
	
		//row29
		// Column H
		cell9 = row.createCell(7);
		if (record.getR29_agency_services() != null) {
			cell9.setCellValue(record.getR29_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row29
		// Column I
		cell10 = row.createCell(8);
		if (record.getR29_asset_management() != null) {
			cell10.setCellValue(record.getR29_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row29
		// Column J
		cell11 = row.createCell(9);
		if (record.getR29_retail_brokerage() != null) {
			cell11.setCellValue(record.getR29_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row26
			row = sheet.getRow(25);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR30_corporate_finance() != null) {
			cell4.setCellValue(record.getR30_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row30
		// Column D
		cell5 = row.createCell(3);
		if (record.getR30_trading_and_sales() != null) {
			cell5.setCellValue(record.getR30_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row30
		// Column E
		cell6 = row.createCell(4);
		if (record.getR30_retail_banking() != null) {
			cell6.setCellValue(record.getR30_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row30
		// Column F
		cell7 = row.createCell(5);
		if (record.getR30_commercial_banking() != null) {
			cell7.setCellValue(record.getR30_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
	
		//row30
		// Column G
		cell8 = row.createCell(6);
		if (record.getR30_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR30_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
	
		//row30
		// Column H
		cell9 = row.createCell(7);
		if (record.getR30_agency_services() != null) {
			cell9.setCellValue(record.getR30_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row30
		// Column I
		cell10 = row.createCell(8);
		if (record.getR30_asset_management() != null) {
			cell10.setCellValue(record.getR30_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row30
		// Column J
		cell11 = row.createCell(9);
		if (record.getR30_retail_brokerage() != null) {
			cell11.setCellValue(record.getR30_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row27
			row = sheet.getRow(26);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR31_corporate_finance() != null) {
			cell4.setCellValue(record.getR31_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row31
		// Column D
		cell5 = row.createCell(3);
		if (record.getR31_trading_and_sales() != null) {
			cell5.setCellValue(record.getR31_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row31
		// Column E
		cell6 = row.createCell(4);
		if (record.getR31_retail_banking() != null) {
			cell6.setCellValue(record.getR31_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row31
		// Column F
		cell7 = row.createCell(5);
		if (record.getR31_commercial_banking() != null) {
			cell7.setCellValue(record.getR31_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
	
		//row31
		// Column G
		cell8 = row.createCell(6);
		if (record.getR31_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR31_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
	
		//row31
		// Column H
		cell9 = row.createCell(7);
		if (record.getR31_agency_services() != null) {
			cell9.setCellValue(record.getR31_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row31
		// Column I
		cell10 = row.createCell(8);
		if (record.getR31_asset_management() != null) {
			cell10.setCellValue(record.getR31_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row31
		// Column J
		cell11 = row.createCell(9);
		if (record.getR31_retail_brokerage() != null) {
			cell11.setCellValue(record.getR31_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row28
			row = sheet.getRow(27);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR32_corporate_finance() != null) {
			cell4.setCellValue(record.getR32_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row32
		// Column D
		cell5 = row.createCell(3);
		if (record.getR32_trading_and_sales() != null) {
			cell5.setCellValue(record.getR32_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row32
		// Column E
		cell6 = row.createCell(4);
		if (record.getR32_retail_banking() != null) {
			cell6.setCellValue(record.getR32_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row32
		// Column F
		cell7 = row.createCell(5);
		if (record.getR32_commercial_banking() != null) {
			cell7.setCellValue(record.getR32_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
	
		//row32
		// Column G
		cell8 = row.createCell(6);
		if (record.getR32_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR32_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
	
		//row32
		// Column H
		cell9 = row.createCell(7);
		if (record.getR32_agency_services() != null) {
			cell9.setCellValue(record.getR32_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row32
		// Column I
		cell10 = row.createCell(8);
		if (record.getR32_asset_management() != null) {
			cell10.setCellValue(record.getR32_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row32
		// Column J
		cell11 = row.createCell(9);
		if (record.getR32_retail_brokerage() != null) {
			cell11.setCellValue(record.getR32_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

			//row29
			row = sheet.getRow(28);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR33_corporate_finance() != null) {
			cell4.setCellValue(record.getR33_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row33
		// Column D
		cell5 = row.createCell(3);
		if (record.getR33_trading_and_sales() != null) {
			cell5.setCellValue(record.getR33_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row33
		// Column E
		cell6 = row.createCell(4);
		if (record.getR33_retail_banking() != null) {
			cell6.setCellValue(record.getR33_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row33
		// Column F
		cell7 = row.createCell(5);
		if (record.getR33_commercial_banking() != null) {
			cell7.setCellValue(record.getR33_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
	
		//row33
		// Column G
		cell8 = row.createCell(6);
		if (record.getR33_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR33_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
	
		//row33
		// Column H
		cell9 = row.createCell(7);
		if (record.getR33_agency_services() != null) {
			cell9.setCellValue(record.getR33_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row33
		// Column I
		cell10 = row.createCell(8);
		if (record.getR33_asset_management() != null) {
			cell10.setCellValue(record.getR33_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row33
		// Column J
		cell11 = row.createCell(9);
		if (record.getR33_retail_brokerage() != null) {
			cell11.setCellValue(record.getR33_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

			//row30
			row = sheet.getRow(29);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR34_corporate_finance() != null) {
			cell4.setCellValue(record.getR34_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row34
		// Column D
		cell5 = row.createCell(3);
		if (record.getR34_trading_and_sales() != null) {
			cell5.setCellValue(record.getR34_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row34
		// Column E
		cell6 = row.createCell(4);
		if (record.getR34_retail_banking() != null) {
			cell6.setCellValue(record.getR34_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row34
		// Column F
		cell7 = row.createCell(5);
		if (record.getR34_commercial_banking() != null) {
			cell7.setCellValue(record.getR34_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row34
		// Column G
		cell8 = row.createCell(6);
		if (record.getR34_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR34_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row34
		// Column H
		cell9 = row.createCell(7);
		if (record.getR34_agency_services() != null) {
			cell9.setCellValue(record.getR34_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row34
		// Column I
		cell10 = row.createCell(8);
		if (record.getR34_asset_management() != null) {
			cell10.setCellValue(record.getR34_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row34
		// Column J
		cell11 = row.createCell(9);
		if (record.getR34_retail_brokerage() != null) {
			cell11.setCellValue(record.getR34_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row31
			row = sheet.getRow(30);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR35_corporate_finance() != null) {
			cell4.setCellValue(record.getR35_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row35
		// Column D
		cell5 = row.createCell(3);
		if (record.getR35_trading_and_sales() != null) {
			cell5.setCellValue(record.getR35_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row35
		// Column E
		cell6 = row.createCell(4);
		if (record.getR35_retail_banking() != null) {
			cell6.setCellValue(record.getR35_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row35
		// Column F
		cell7 = row.createCell(5);
		if (record.getR35_commercial_banking() != null) {
			cell7.setCellValue(record.getR35_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row35
		// Column G
		cell8 = row.createCell(6);
		if (record.getR35_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR35_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row35
		// Column H
		cell9 = row.createCell(7);
		if (record.getR35_agency_services() != null) {
			cell9.setCellValue(record.getR35_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row35
		// Column I
		cell10 = row.createCell(8);
		if (record.getR35_asset_management() != null) {
			cell10.setCellValue(record.getR35_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row35
		// Column J
		cell11 = row.createCell(9);
		if (record.getR35_retail_brokerage() != null) {
			cell11.setCellValue(record.getR35_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

			//row32
			row = sheet.getRow(31);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR36_corporate_finance() != null) {
			cell4.setCellValue(record.getR36_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row36
		// Column D
		cell5 = row.createCell(3);
		if (record.getR36_trading_and_sales() != null) {
			cell5.setCellValue(record.getR36_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row36
		// Column E
		cell6 = row.createCell(4);
		if (record.getR36_retail_banking() != null) {
			cell6.setCellValue(record.getR36_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row36
		// Column F
		cell7 = row.createCell(5);
		if (record.getR36_commercial_banking() != null) {
			cell7.setCellValue(record.getR36_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row36
		// Column G
		cell8 = row.createCell(6);
		if (record.getR36_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR36_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row36
		// Column H
		cell9 = row.createCell(7);
		if (record.getR36_agency_services() != null) {
			cell9.setCellValue(record.getR36_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row36
		// Column I
		cell10 = row.createCell(8);
		if (record.getR36_asset_management() != null) {
			cell10.setCellValue(record.getR36_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row36
		// Column J
		cell11 = row.createCell(9);
		if (record.getR36_retail_brokerage() != null) {
			cell11.setCellValue(record.getR36_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

	    //row33
		row = sheet.getRow(32);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR37_corporate_finance() != null) {
			cell4.setCellValue(record.getR37_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row37
		// Column D
		cell5 = row.createCell(3);
		if (record.getR37_trading_and_sales() != null) {
			cell5.setCellValue(record.getR37_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row37
		// Column E
		cell6 = row.createCell(4);
		if (record.getR37_retail_banking() != null) {
			cell6.setCellValue(record.getR37_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row37
		// Column F
		cell7 = row.createCell(5);
		if (record.getR37_commercial_banking() != null) {
			cell7.setCellValue(record.getR37_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row37
		// Column G
		cell8 = row.createCell(6);
		if (record.getR37_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR37_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row37
		// Column H
		cell9 = row.createCell(7);
		if (record.getR37_agency_services() != null) {
			cell9.setCellValue(record.getR37_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row37
		// Column I
		cell10 = row.createCell(8);
		if (record.getR37_asset_management() != null) {
			cell10.setCellValue(record.getR37_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row37
		// Column J
		cell11 = row.createCell(9);
		if (record.getR37_retail_brokerage() != null) {
			cell11.setCellValue(record.getR37_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

	 //row34
		row = sheet.getRow(33);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR38_corporate_finance() != null) {
			cell4.setCellValue(record.getR38_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row38
		// Column D
		cell5 = row.createCell(3);
		if (record.getR38_trading_and_sales() != null) {
			cell5.setCellValue(record.getR38_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row38
		// Column E
		cell6 = row.createCell(4);
		if (record.getR38_retail_banking() != null) {
			cell6.setCellValue(record.getR38_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row38
		// Column F
		cell7 = row.createCell(5);
		if (record.getR38_commercial_banking() != null) {
			cell7.setCellValue(record.getR38_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row38
		// Column G
		cell8 = row.createCell(6);
		if (record.getR38_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR38_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row38
		// Column H
		cell9 = row.createCell(7);
		if (record.getR38_agency_services() != null) {
			cell9.setCellValue(record.getR38_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row38
		// Column I
		cell10 = row.createCell(8);
		if (record.getR38_asset_management() != null) {
			cell10.setCellValue(record.getR38_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row38
		// Column J
		cell11 = row.createCell(9);
		if (record.getR38_retail_brokerage() != null) {
			cell11.setCellValue(record.getR38_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row35
		row = sheet.getRow(34);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR39_corporate_finance() != null) {
			cell4.setCellValue(record.getR39_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row39
		// Column D
		cell5 = row.createCell(3);
		if (record.getR39_trading_and_sales() != null) {
			cell5.setCellValue(record.getR39_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row39
		// Column E
		cell6 = row.createCell(4);
		if (record.getR39_retail_banking() != null) {
			cell6.setCellValue(record.getR39_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row39
		// Column F
		cell7 = row.createCell(5);
		if (record.getR39_commercial_banking() != null) {
			cell7.setCellValue(record.getR39_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row39
		// Column G
		cell8 = row.createCell(6);
		if (record.getR39_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR39_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row39
		// Column H
		cell9 = row.createCell(7);
		if (record.getR39_agency_services() != null) {
			cell9.setCellValue(record.getR39_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row39
		// Column I
		cell10 = row.createCell(8);
		if (record.getR39_asset_management() != null) {
			cell10.setCellValue(record.getR39_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row39
		// Column J
		cell11 = row.createCell(9);
		if (record.getR39_retail_brokerage() != null) {
			cell11.setCellValue(record.getR39_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		
		 //row36
		row = sheet.getRow(35);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR44_corporate_finance() != null) {
			cell4.setCellValue(record.getR44_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row44
		// Column D
		cell5 = row.createCell(3);
		if (record.getR44_trading_and_sales() != null) {
			cell5.setCellValue(record.getR44_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row44
		// Column E
		cell6 = row.createCell(4);
		if (record.getR44_retail_banking() != null) {
			cell6.setCellValue(record.getR44_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row44
		// Column F
		cell7 = row.createCell(5);
		if (record.getR44_commercial_banking() != null) {
			cell7.setCellValue(record.getR44_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row44
		// Column G
		cell8 = row.createCell(6);
		if (record.getR44_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR44_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row44
		// Column H
		cell9 = row.createCell(7);
		if (record.getR44_agency_services() != null) {
			cell9.setCellValue(record.getR44_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row44
		// Column I
		cell10 = row.createCell(8);
		if (record.getR44_asset_management() != null) {
			cell10.setCellValue(record.getR44_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row44
		// Column J
		cell11 = row.createCell(9);
		if (record.getR44_retail_brokerage() != null) {
			cell11.setCellValue(record.getR44_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row37
		row = sheet.getRow(36);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR45_corporate_finance() != null) {
			cell4.setCellValue(record.getR45_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row45
		// Column D
		cell5 = row.createCell(3);
		if (record.getR45_trading_and_sales() != null) {
			cell5.setCellValue(record.getR45_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row45
		// Column E
		cell6 = row.createCell(4);
		if (record.getR45_retail_banking() != null) {
			cell6.setCellValue(record.getR45_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row45
		// Column F
		cell7 = row.createCell(5);
		if (record.getR45_commercial_banking() != null) {
			cell7.setCellValue(record.getR45_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row45
		// Column G
		cell8 = row.createCell(6);
		if (record.getR45_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR45_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row45
		// Column H
		cell9 = row.createCell(7);
		if (record.getR45_agency_services() != null) {
			cell9.setCellValue(record.getR45_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row45
		// Column I
		cell10 = row.createCell(8);
		if (record.getR45_asset_management() != null) {
			cell10.setCellValue(record.getR45_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row45
		// Column J
		cell11 = row.createCell(9);
		if (record.getR45_retail_brokerage() != null) {
			cell11.setCellValue(record.getR45_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

	 //row38
		row = sheet.getRow(37);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR46_corporate_finance() != null) {
			cell4.setCellValue(record.getR46_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row46
		// Column D
		cell5 = row.createCell(3);
		if (record.getR46_trading_and_sales() != null) {
			cell5.setCellValue(record.getR46_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row46
		// Column E
		cell6 = row.createCell(4);
		if (record.getR46_retail_banking() != null) {
			cell6.setCellValue(record.getR46_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row46
		// Column F
		cell7 = row.createCell(5);
		if (record.getR46_commercial_banking() != null) {
			cell7.setCellValue(record.getR46_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row46
		// Column G
		cell8 = row.createCell(6);
		if (record.getR46_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR46_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row46
		// Column H
		cell9 = row.createCell(7);
		if (record.getR46_agency_services() != null) {
			cell9.setCellValue(record.getR46_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row46
		// Column I
		cell10 = row.createCell(8);
		if (record.getR46_asset_management() != null) {
			cell10.setCellValue(record.getR46_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row46
		// Column J
		cell11 = row.createCell(9);
		if (record.getR46_retail_brokerage() != null) {
			cell11.setCellValue(record.getR46_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row39
		row = sheet.getRow(38);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR47_corporate_finance() != null) {
			cell4.setCellValue(record.getR47_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row47
		// Column D
		cell5 = row.createCell(3);
		if (record.getR47_trading_and_sales() != null) {
			cell5.setCellValue(record.getR47_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row47
		// Column E
		cell6 = row.createCell(4);
		if (record.getR47_retail_banking() != null) {
			cell6.setCellValue(record.getR47_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row47
		// Column F
		cell7 = row.createCell(5);
		if (record.getR47_commercial_banking() != null) {
			cell7.setCellValue(record.getR47_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row47
		// Column G
		cell8 = row.createCell(6);
		if (record.getR47_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR47_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row47
		// Column H
		cell9 = row.createCell(7);
		if (record.getR47_agency_services() != null) {
			cell9.setCellValue(record.getR47_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row47
		// Column I
		cell10 = row.createCell(8);
		if (record.getR47_asset_management() != null) {
			cell10.setCellValue(record.getR47_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row47
		// Column J
		cell11 = row.createCell(9);
		if (record.getR47_retail_brokerage() != null) {
			cell11.setCellValue(record.getR47_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

	 //row40
		row = sheet.getRow(39);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR48_corporate_finance() != null) {
			cell4.setCellValue(record.getR48_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row48
		// Column D
		cell5 = row.createCell(3);
		if (record.getR48_trading_and_sales() != null) {
			cell5.setCellValue(record.getR48_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row48
		// Column E
		cell6 = row.createCell(4);
		if (record.getR48_retail_banking() != null) {
			cell6.setCellValue(record.getR48_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row48
		// Column F
		cell7 = row.createCell(5);
		if (record.getR48_commercial_banking() != null) {
			cell7.setCellValue(record.getR48_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row48
		// Column G
		cell8 = row.createCell(6);
		if (record.getR48_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR48_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row48
		// Column H
		cell9 = row.createCell(7);
		if (record.getR48_agency_services() != null) {
			cell9.setCellValue(record.getR48_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row48
		// Column I
		cell10 = row.createCell(8);
		if (record.getR48_asset_management() != null) {
			cell10.setCellValue(record.getR48_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row48
		// Column J
		cell11 = row.createCell(9);
		if (record.getR48_retail_brokerage() != null) {
			cell11.setCellValue(record.getR48_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row41
		row = sheet.getRow(40);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR49_corporate_finance() != null) {
			cell4.setCellValue(record.getR49_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row49
		// Column D
		cell5 = row.createCell(3);
		if (record.getR49_trading_and_sales() != null) {
			cell5.setCellValue(record.getR49_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row49
		// Column E
		cell6 = row.createCell(4);
		if (record.getR49_retail_banking() != null) {
			cell6.setCellValue(record.getR49_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row49
		// Column F
		cell7 = row.createCell(5);
		if (record.getR49_commercial_banking() != null) {
			cell7.setCellValue(record.getR49_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row49
		// Column G
		cell8 = row.createCell(6);
		if (record.getR49_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR49_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row49
		// Column H
		cell9 = row.createCell(7);
		if (record.getR49_agency_services() != null) {
			cell9.setCellValue(record.getR49_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row49
		// Column I
		cell10 = row.createCell(8);
		if (record.getR49_asset_management() != null) {
			cell10.setCellValue(record.getR49_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row49
		// Column J
		cell11 = row.createCell(9);
		if (record.getR49_retail_brokerage() != null) {
			cell11.setCellValue(record.getR49_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

			 //row42
		row = sheet.getRow(41);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR50_corporate_finance() != null) {
			cell4.setCellValue(record.getR50_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row50
		// Column D
		cell5 = row.createCell(3);
		if (record.getR50_trading_and_sales() != null) {
			cell5.setCellValue(record.getR50_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row50
		// Column E
		cell6 = row.createCell(4);
		if (record.getR50_retail_banking() != null) {
			cell6.setCellValue(record.getR50_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row50
		// Column F
		cell7 = row.createCell(5);
		if (record.getR50_commercial_banking() != null) {
			cell7.setCellValue(record.getR50_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row50
		// Column G
		cell8 = row.createCell(6);
		if (record.getR50_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR50_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row50
		// Column H
		cell9 = row.createCell(7);
		if (record.getR50_agency_services() != null) {
			cell9.setCellValue(record.getR50_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row50
		// Column I
		cell10 = row.createCell(8);
		if (record.getR50_asset_management() != null) {
			cell10.setCellValue(record.getR50_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row50
		// Column J
		cell11 = row.createCell(9);
		if (record.getR50_retail_brokerage() != null) {
			cell11.setCellValue(record.getR50_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row43
		row = sheet.getRow(42);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR51_corporate_finance() != null) {
			cell4.setCellValue(record.getR51_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row51
		// Column D
		cell5 = row.createCell(3);
		if (record.getR51_trading_and_sales() != null) {
			cell5.setCellValue(record.getR51_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row51
		// Column E
		cell6 = row.createCell(4);
		if (record.getR51_retail_banking() != null) {
			cell6.setCellValue(record.getR51_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row51
		// Column F
		cell7 = row.createCell(5);
		if (record.getR51_commercial_banking() != null) {
			cell7.setCellValue(record.getR51_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row51
		// Column G
		cell8 = row.createCell(6);
		if (record.getR51_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR51_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row51
		// Column H
		cell9 = row.createCell(7);
		if (record.getR51_agency_services() != null) {
			cell9.setCellValue(record.getR51_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row51
		// Column I
		cell10 = row.createCell(8);
		if (record.getR51_asset_management() != null) {
			cell10.setCellValue(record.getR51_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row51
		// Column J
		cell11 = row.createCell(9);
		if (record.getR51_retail_brokerage() != null) {
			cell11.setCellValue(record.getR51_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row44
		row = sheet.getRow(43);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR52_corporate_finance() != null) {
			cell4.setCellValue(record.getR52_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row52
		// Column D
		cell5 = row.createCell(3);
		if (record.getR52_trading_and_sales() != null) {
			cell5.setCellValue(record.getR52_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row52
		// Column E
		cell6 = row.createCell(4);
		if (record.getR52_retail_banking() != null) {
			cell6.setCellValue(record.getR52_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row52
		// Column F
		cell7 = row.createCell(5);
		if (record.getR52_commercial_banking() != null) {
			cell7.setCellValue(record.getR52_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row52
		// Column G
		cell8 = row.createCell(6);
		if (record.getR52_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR52_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row52
		// Column H
		cell9 = row.createCell(7);
		if (record.getR52_agency_services() != null) {
			cell9.setCellValue(record.getR52_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row52
		// Column I
		cell10 = row.createCell(8);
		if (record.getR52_asset_management() != null) {
			cell10.setCellValue(record.getR52_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row52
		// Column J
		cell11 = row.createCell(9);
		if (record.getR52_retail_brokerage() != null) {
			cell11.setCellValue(record.getR52_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row45
		row = sheet.getRow(44);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR53_corporate_finance() != null) {
			cell4.setCellValue(record.getR53_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row53
		// Column D
		cell5 = row.createCell(3);
		if (record.getR53_trading_and_sales() != null) {
			cell5.setCellValue(record.getR53_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row53
		// Column E
		cell6 = row.createCell(4);
		if (record.getR53_retail_banking() != null) {
			cell6.setCellValue(record.getR53_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row53
		// Column F
		cell7 = row.createCell(5);
		if (record.getR53_commercial_banking() != null) {
			cell7.setCellValue(record.getR53_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row53
		// Column G
		cell8 = row.createCell(6);
		if (record.getR53_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR53_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row53
		// Column H
		cell9 = row.createCell(7);
		if (record.getR53_agency_services() != null) {
			cell9.setCellValue(record.getR53_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row53
		// Column I
		cell10 = row.createCell(8);
		if (record.getR53_asset_management() != null) {
			cell10.setCellValue(record.getR53_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row53
		// Column J
		cell11 = row.createCell(9);
		if (record.getR53_retail_brokerage() != null) {
			cell11.setCellValue(record.getR53_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row46
		row = sheet.getRow(45);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR54_corporate_finance() != null) {
			cell4.setCellValue(record.getR54_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row54
		// Column D
		cell5 = row.createCell(3);
		if (record.getR54_trading_and_sales() != null) {
			cell5.setCellValue(record.getR54_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row54
		// Column E
		cell6 = row.createCell(4);
		if (record.getR54_retail_banking() != null) {
			cell6.setCellValue(record.getR54_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row54
		// Column F
		cell7 = row.createCell(5);
		if (record.getR54_commercial_banking() != null) {
			cell7.setCellValue(record.getR54_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row54
		// Column G
		cell8 = row.createCell(6);
		if (record.getR54_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR54_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row54
		// Column H
		cell9 = row.createCell(7);
		if (record.getR54_agency_services() != null) {
			cell9.setCellValue(record.getR54_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row54
		// Column I
		cell10 = row.createCell(8);
		if (record.getR54_asset_management() != null) {
			cell10.setCellValue(record.getR54_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row54
		// Column J
		cell11 = row.createCell(9);
		if (record.getR54_retail_brokerage() != null) {
			cell11.setCellValue(record.getR54_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row47
		row = sheet.getRow(46);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR55_corporate_finance() != null) {
			cell4.setCellValue(record.getR55_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row55
		// Column D
		cell5 = row.createCell(3);
		if (record.getR55_trading_and_sales() != null) {
			cell5.setCellValue(record.getR55_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row55
		// Column E
		cell6 = row.createCell(4);
		if (record.getR55_retail_banking() != null) {
			cell6.setCellValue(record.getR55_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row55
		// Column F
		cell7 = row.createCell(5);
		if (record.getR55_commercial_banking() != null) {
			cell7.setCellValue(record.getR55_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row55
		// Column G
		cell8 = row.createCell(6);
		if (record.getR55_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR55_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row55
		// Column H
		cell9 = row.createCell(7);
		if (record.getR55_agency_services() != null) {
			cell9.setCellValue(record.getR55_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row55
		// Column I
		cell10 = row.createCell(8);
		if (record.getR55_asset_management() != null) {
			cell10.setCellValue(record.getR55_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row55
		// Column J
		cell11 = row.createCell(9);
		if (record.getR55_retail_brokerage() != null) {
			cell11.setCellValue(record.getR55_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
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


		
		// Resub Email Excel
		public byte[] BRRS_M_OR2EmailResubExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Resub Email Excel generation process in memory.");

			List<M_OR2_RESUB_Summary_Entity> dataList = M_OR2_resub_summary_repo
					.getdatabydateListarchival(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_OR2 report. Returning empty result.");
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

				int startRow = 11;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_OR2_RESUB_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

			//row12
					// Column C
					Cell cell4 = row.createCell(2);
					if (record.getR12_corporate_finance() != null) {
						cell4.setCellValue(record.getR12_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row12
					// Column D
					Cell cell5 = row.createCell(3);
					if (record.getR12_trading_and_sales() != null) {
						cell5.setCellValue(record.getR12_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					} 
					
					
					//row12
					// Column E
					Cell cell6 = row.createCell(4);
					if (record.getR12_retail_banking() != null) {
						cell6.setCellValue(record.getR12_retail_banking().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					//row12
					// Column F
					Cell cell7 = row.createCell(5);
					if (record.getR12_commercial_banking()  != null) {
						cell7.setCellValue(record.getR12_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					//row12
					// Column G
					Cell cell8 = row.createCell(6);
					if (record.getR12_payments_and_settlements()  != null) {
						cell8.setCellValue(record.getR12_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					//row12
					// Column H
					Cell cell9 = row.createCell(7);
					if (record.getR12_agency_services() != null) {
						cell9.setCellValue(record.getR12_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					//row12
					// Column I
					Cell cell10 = row.createCell(8);
					if (record.getR12_asset_management()  != null) {
						cell10.setCellValue(record.getR12_asset_management() .doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					//row12
					// Column J
					Cell cell11 = row.createCell(9);
					if (record.getR12_retail_brokerage() != null) {
						cell11.setCellValue(record.getR12_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					
					//row13
					row = sheet.getRow(12);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR13_corporate_finance() != null) {
			cell4.setCellValue(record.getR13_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row13
		// Column D
		cell5 = row.createCell(3);
		if (record.getR13_trading_and_sales() != null) {
			cell5.setCellValue(record.getR13_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row13
		// Column E
		cell6 = row.createCell(4);
		if (record.getR13_retail_banking() != null) {
			cell6.setCellValue(record.getR13_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row13
		// Column F
		cell7 = row.createCell(5);
		if (record.getR13_commercial_banking() != null) {
			cell7.setCellValue(record.getR13_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row13
		// Column G
		cell8 = row.createCell(6);
		if (record.getR13_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR13_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row13
		// Column H
		cell9 = row.createCell(7);
		if (record.getR13_agency_services() != null) {
			cell9.setCellValue(record.getR13_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row13
		// Column I
		cell10 = row.createCell(8);
		if (record.getR13_asset_management() != null) {
			cell10.setCellValue(record.getR13_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row13
		// Column J
		cell11 = row.createCell(9);
		if (record.getR13_retail_brokerage() != null) {
			cell11.setCellValue(record.getR13_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row14
		row = sheet.getRow(13);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR14_corporate_finance() != null) {
			cell4.setCellValue(record.getR14_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row14
		// Column D
		cell5 = row.createCell(3);
		if (record.getR14_trading_and_sales() != null) {
			cell5.setCellValue(record.getR14_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row14
		// Column E
		cell6 = row.createCell(4);
		if (record.getR14_retail_banking() != null) {
			cell6.setCellValue(record.getR14_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row14
		// Column F
		cell7 = row.createCell(5);
		if (record.getR14_commercial_banking() != null) {
			cell7.setCellValue(record.getR14_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row14
		// Column G
		cell8 = row.createCell(6);
		if (record.getR14_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR14_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row14
		// Column H
		cell9 = row.createCell(7);
		if (record.getR14_agency_services() != null) {
			cell9.setCellValue(record.getR14_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row14
		// Column I
		cell10 = row.createCell(8);
		if (record.getR14_asset_management() != null) {
			cell10.setCellValue(record.getR14_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row14
		// Column J
		cell11 = row.createCell(9);
		if (record.getR14_retail_brokerage() != null) {
			cell11.setCellValue(record.getR14_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row15
		row = sheet.getRow(14);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR15_corporate_finance() != null) {
			cell4.setCellValue(record.getR15_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row15
		// Column D
		cell5 = row.createCell(3);
		if (record.getR15_trading_and_sales() != null) {
			cell5.setCellValue(record.getR15_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row15
		// Column E
		cell6 = row.createCell(4);
		if (record.getR15_retail_banking() != null) {
			cell6.setCellValue(record.getR15_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row15
		// Column F
		cell7 = row.createCell(5);
		if (record.getR15_commercial_banking() != null) {
			cell7.setCellValue(record.getR15_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row15
		// Column G
		cell8 = row.createCell(6);
		if (record.getR15_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR15_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row15
		// Column H
		cell9 = row.createCell(7);
		if (record.getR15_agency_services() != null) {
			cell9.setCellValue(record.getR15_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row15
		// Column I
		cell10 = row.createCell(8);
		if (record.getR15_asset_management() != null) {
			cell10.setCellValue(record.getR15_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row15
		// Column J
		cell11 = row.createCell(9);
		if (record.getR15_retail_brokerage() != null) {
			cell11.setCellValue(record.getR15_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row16
		row = sheet.getRow(15);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR16_corporate_finance() != null) {
			cell4.setCellValue(record.getR16_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row16
		// Column D
		cell5 = row.createCell(3);
		if (record.getR16_trading_and_sales() != null) {
			cell5.setCellValue(record.getR16_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row16
		// Column E
		cell6 = row.createCell(4);
		if (record.getR16_retail_banking() != null) {
			cell6.setCellValue(record.getR16_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row16
		// Column F
		cell7 = row.createCell(5);
		if (record.getR16_commercial_banking() != null) {
			cell7.setCellValue(record.getR16_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row16
		// Column G
		cell8 = row.createCell(6);
		if (record.getR16_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR16_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row16
		// Column H
		cell9 = row.createCell(7);
		if (record.getR16_agency_services() != null) {
			cell9.setCellValue(record.getR16_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row16
		// Column I
		cell10 = row.createCell(8);
		if (record.getR16_asset_management() != null) {
			cell10.setCellValue(record.getR16_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row16
		// Column J
		cell11 = row.createCell(9);
		if (record.getR16_retail_brokerage() != null) {
			cell11.setCellValue(record.getR16_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row17
		row = sheet.getRow(16);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR17_corporate_finance() != null) {
			cell4.setCellValue(record.getR17_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row17
		// Column D
		cell5 = row.createCell(3);
		if (record.getR17_trading_and_sales() != null) {
			cell5.setCellValue(record.getR17_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row17
		// Column E
		cell6 = row.createCell(4);
		if (record.getR17_retail_banking() != null) {
			cell6.setCellValue(record.getR17_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row17
		// Column F
		cell7 = row.createCell(5);
		if (record.getR17_commercial_banking() != null) {
			cell7.setCellValue(record.getR17_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row17
		// Column G
		cell8 = row.createCell(6);
		if (record.getR17_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR17_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row17
		// Column H
		cell9 = row.createCell(7);
		if (record.getR17_agency_services() != null) {
			cell9.setCellValue(record.getR17_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row17
		// Column I
		cell10 = row.createCell(8);
		if (record.getR17_asset_management() != null) {
			cell10.setCellValue(record.getR17_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row17
		// Column J
		cell11 = row.createCell(9);
		if (record.getR17_retail_brokerage() != null) {
			cell11.setCellValue(record.getR17_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row18
		row = sheet.getRow(17);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR18_corporate_finance() != null) {
			cell4.setCellValue(record.getR18_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row18
		// Column D
		cell5 = row.createCell(3);
		if (record.getR18_trading_and_sales() != null) {
			cell5.setCellValue(record.getR18_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row18
		// Column E
		cell6 = row.createCell(4);
		if (record.getR18_retail_banking() != null) {
			cell6.setCellValue(record.getR18_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row18
		// Column F
		cell7 = row.createCell(5);
		if (record.getR18_commercial_banking() != null) {
			cell7.setCellValue(record.getR18_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row18
		// Column G
		cell8 = row.createCell(6);
		if (record.getR18_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR18_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row18
		// Column H
		cell9 = row.createCell(7);
		if (record.getR18_agency_services() != null) {
			cell9.setCellValue(record.getR18_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row18
		// Column I
		cell10 = row.createCell(8);
		if (record.getR18_asset_management() != null) {
			cell10.setCellValue(record.getR18_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row18
		// Column J
		cell11 = row.createCell(9);
		if (record.getR18_retail_brokerage() != null) {
			cell11.setCellValue(record.getR18_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row19
		row = sheet.getRow(18);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR19_corporate_finance() != null) {
			cell4.setCellValue(record.getR19_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row19
		// Column D
		cell5 = row.createCell(3);
		if (record.getR19_trading_and_sales() != null) {
			cell5.setCellValue(record.getR19_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row19
		// Column E
		cell6 = row.createCell(4);
		if (record.getR19_retail_banking() != null) {
			cell6.setCellValue(record.getR19_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row19
		// Column F
		cell7 = row.createCell(5);
		if (record.getR19_commercial_banking() != null) {
			cell7.setCellValue(record.getR19_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row19
		// Column G
		cell8 = row.createCell(6);
		if (record.getR19_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR19_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row19
		// Column H
		cell9 = row.createCell(7);
		if (record.getR19_agency_services() != null) {
			cell9.setCellValue(record.getR19_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row19
		// Column I
		cell10 = row.createCell(8);
		if (record.getR19_asset_management() != null) {
			cell10.setCellValue(record.getR19_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row19
		// Column J
		cell11 = row.createCell(9);
		if (record.getR19_retail_brokerage() != null) {
			cell11.setCellValue(record.getR19_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row20
		row = sheet.getRow(19);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR20_corporate_finance() != null) {
			cell4.setCellValue(record.getR20_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row20
		// Column D
		cell5 = row.createCell(3);
		if (record.getR20_trading_and_sales() != null) {
			cell5.setCellValue(record.getR20_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row20
		// Column E
		cell6 = row.createCell(4);
		if (record.getR20_retail_banking() != null) {
			cell6.setCellValue(record.getR20_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row20
		// Column F
		cell7 = row.createCell(5);
		if (record.getR20_commercial_banking() != null) {
			cell7.setCellValue(record.getR20_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row20
		// Column G
		cell8 = row.createCell(6);
		if (record.getR20_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR20_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row20
		// Column H
		cell9 = row.createCell(7);
		if (record.getR20_agency_services() != null) {
			cell9.setCellValue(record.getR20_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row20
		// Column I
		cell10 = row.createCell(8);
		if (record.getR20_asset_management() != null) {
			cell10.setCellValue(record.getR20_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row20
		// Column J
		cell11 = row.createCell(9);
		if (record.getR20_retail_brokerage() != null) {
			cell11.setCellValue(record.getR20_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row21
		row = sheet.getRow(20);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR21_corporate_finance() != null) {
			cell4.setCellValue(record.getR21_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row21
		// Column D
		cell5 = row.createCell(3);
		if (record.getR21_trading_and_sales() != null) {
			cell5.setCellValue(record.getR21_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row21
		// Column E
		cell6 = row.createCell(4);
		if (record.getR21_retail_banking() != null) {
			cell6.setCellValue(record.getR21_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row21
		// Column F
		cell7 = row.createCell(5);
		if (record.getR21_commercial_banking() != null) {
			cell7.setCellValue(record.getR21_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row21
		// Column G
		cell8 = row.createCell(6);
		if (record.getR21_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR21_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row21
		// Column H
		cell9 = row.createCell(7);
		if (record.getR21_agency_services() != null) {
			cell9.setCellValue(record.getR21_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row21
		// Column I
		cell10 = row.createCell(8);
		if (record.getR21_asset_management() != null) {
			cell10.setCellValue(record.getR21_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row21
		// Column J
		cell11 = row.createCell(9);
		if (record.getR21_retail_brokerage() != null) {
			cell11.setCellValue(record.getR21_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row22
		row = sheet.getRow(21);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR22_corporate_finance() != null) {
			cell4.setCellValue(record.getR22_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row22
		// Column D
		cell5 = row.createCell(3);
		if (record.getR22_trading_and_sales() != null) {
			cell5.setCellValue(record.getR22_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row22
		// Column E
		cell6 = row.createCell(4);
		if (record.getR22_retail_banking() != null) {
			cell6.setCellValue(record.getR22_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row22
		// Column F
		cell7 = row.createCell(5);
		if (record.getR22_commercial_banking() != null) {
			cell7.setCellValue(record.getR22_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row22
		// Column G
		cell8 = row.createCell(6);
		if (record.getR22_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR22_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row22
		// Column H
		cell9 = row.createCell(7);
		if (record.getR22_agency_services() != null) {
			cell9.setCellValue(record.getR22_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row22
		// Column I
		cell10 = row.createCell(8);
		if (record.getR22_asset_management() != null) {
			cell10.setCellValue(record.getR22_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row22
		// Column J
		cell11 = row.createCell(9);
		if (record.getR22_retail_brokerage() != null) {
			cell11.setCellValue(record.getR22_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row23
		row = sheet.getRow(22);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR23_corporate_finance() != null) {
			cell4.setCellValue(record.getR23_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row23
		// Column D
		cell5 = row.createCell(3);
		if (record.getR23_trading_and_sales() != null) {
			cell5.setCellValue(record.getR23_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row23
		// Column E
		cell6 = row.createCell(4);
		if (record.getR23_retail_banking() != null) {
			cell6.setCellValue(record.getR23_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row23
		// Column F
		cell7 = row.createCell(5);
		if (record.getR23_commercial_banking() != null) {
			cell7.setCellValue(record.getR23_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row23
		// Column G
		cell8 = row.createCell(6);
		if (record.getR23_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR23_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row23
		// Column H
		cell9 = row.createCell(7);
		if (record.getR23_agency_services() != null) {
			cell9.setCellValue(record.getR23_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row23
		// Column I
		cell10 = row.createCell(8);
		if (record.getR23_asset_management() != null) {
			cell10.setCellValue(record.getR23_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row23
		// Column J
		cell11 = row.createCell(9);
		if (record.getR23_retail_brokerage() != null) {
			cell11.setCellValue(record.getR23_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
			//row24
			row = sheet.getRow(23);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR28_corporate_finance() != null) {
			cell4.setCellValue(record.getR28_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row28
		// Column D
		cell5 = row.createCell(3);
		if (record.getR28_trading_and_sales() != null) {
			cell5.setCellValue(record.getR28_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row28
		// Column E
		cell6 = row.createCell(4);
		if (record.getR28_retail_banking() != null) {
			cell6.setCellValue(record.getR28_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		 cell7 = row.createCell(6);
		if (record.getR28_commercial_banking() != null) {
			cell7.setCellValue(record.getR28_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR28_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR28_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR28_agency_services() != null) {
			cell9.setCellValue(record.getR28_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		

		//row28
		// Column I
		cell10 = row.createCell(8);
		if (record.getR28_asset_management() != null) {
			cell10.setCellValue(record.getR28_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row28
		// Column J
		cell11 = row.createCell(9);
		if (record.getR28_retail_brokerage() != null) {
			cell11.setCellValue(record.getR28_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row25
			row = sheet.getRow(24);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR29_corporate_finance() != null) {
			cell4.setCellValue(record.getR29_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row29
		// Column D
		cell5 = row.createCell(3);
		if (record.getR29_trading_and_sales() != null) {
			cell5.setCellValue(record.getR29_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row29
		// Column E
		cell6 = row.createCell(4);
		if (record.getR29_retail_banking() != null) {
			cell6.setCellValue(record.getR29_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row29
		// Column F
		cell7 = row.createCell(5);
		if (record.getR29_commercial_banking() != null) {
			cell7.setCellValue(record.getR29_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
	
		//row29
		// Column G
		cell8 = row.createCell(6);
		if (record.getR29_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR29_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
	
		//row29
		// Column H
		cell9 = row.createCell(7);
		if (record.getR29_agency_services() != null) {
			cell9.setCellValue(record.getR29_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row29
		// Column I
		cell10 = row.createCell(8);
		if (record.getR29_asset_management() != null) {
			cell10.setCellValue(record.getR29_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row29
		// Column J
		cell11 = row.createCell(9);
		if (record.getR29_retail_brokerage() != null) {
			cell11.setCellValue(record.getR29_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row26
			row = sheet.getRow(25);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR30_corporate_finance() != null) {
			cell4.setCellValue(record.getR30_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row30
		// Column D
		cell5 = row.createCell(3);
		if (record.getR30_trading_and_sales() != null) {
			cell5.setCellValue(record.getR30_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row30
		// Column E
		cell6 = row.createCell(4);
		if (record.getR30_retail_banking() != null) {
			cell6.setCellValue(record.getR30_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row30
		// Column F
		cell7 = row.createCell(5);
		if (record.getR30_commercial_banking() != null) {
			cell7.setCellValue(record.getR30_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
	
		//row30
		// Column G
		cell8 = row.createCell(6);
		if (record.getR30_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR30_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
	
		//row30
		// Column H
		cell9 = row.createCell(7);
		if (record.getR30_agency_services() != null) {
			cell9.setCellValue(record.getR30_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row30
		// Column I
		cell10 = row.createCell(8);
		if (record.getR30_asset_management() != null) {
			cell10.setCellValue(record.getR30_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row30
		// Column J
		cell11 = row.createCell(9);
		if (record.getR30_retail_brokerage() != null) {
			cell11.setCellValue(record.getR30_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row27
			row = sheet.getRow(26);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR31_corporate_finance() != null) {
			cell4.setCellValue(record.getR31_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row31
		// Column D
		cell5 = row.createCell(3);
		if (record.getR31_trading_and_sales() != null) {
			cell5.setCellValue(record.getR31_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row31
		// Column E
		cell6 = row.createCell(4);
		if (record.getR31_retail_banking() != null) {
			cell6.setCellValue(record.getR31_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row31
		// Column F
		cell7 = row.createCell(5);
		if (record.getR31_commercial_banking() != null) {
			cell7.setCellValue(record.getR31_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
	
		//row31
		// Column G
		cell8 = row.createCell(6);
		if (record.getR31_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR31_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
	
		//row31
		// Column H
		cell9 = row.createCell(7);
		if (record.getR31_agency_services() != null) {
			cell9.setCellValue(record.getR31_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row31
		// Column I
		cell10 = row.createCell(8);
		if (record.getR31_asset_management() != null) {
			cell10.setCellValue(record.getR31_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row31
		// Column J
		cell11 = row.createCell(9);
		if (record.getR31_retail_brokerage() != null) {
			cell11.setCellValue(record.getR31_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row28
			row = sheet.getRow(27);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR32_corporate_finance() != null) {
			cell4.setCellValue(record.getR32_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row32
		// Column D
		cell5 = row.createCell(3);
		if (record.getR32_trading_and_sales() != null) {
			cell5.setCellValue(record.getR32_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row32
		// Column E
		cell6 = row.createCell(4);
		if (record.getR32_retail_banking() != null) {
			cell6.setCellValue(record.getR32_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row32
		// Column F
		cell7 = row.createCell(5);
		if (record.getR32_commercial_banking() != null) {
			cell7.setCellValue(record.getR32_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
	
		//row32
		// Column G
		cell8 = row.createCell(6);
		if (record.getR32_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR32_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
	
		//row32
		// Column H
		cell9 = row.createCell(7);
		if (record.getR32_agency_services() != null) {
			cell9.setCellValue(record.getR32_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row32
		// Column I
		cell10 = row.createCell(8);
		if (record.getR32_asset_management() != null) {
			cell10.setCellValue(record.getR32_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row32
		// Column J
		cell11 = row.createCell(9);
		if (record.getR32_retail_brokerage() != null) {
			cell11.setCellValue(record.getR32_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

			//row29
			row = sheet.getRow(28);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR33_corporate_finance() != null) {
			cell4.setCellValue(record.getR33_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row33
		// Column D
		cell5 = row.createCell(3);
		if (record.getR33_trading_and_sales() != null) {
			cell5.setCellValue(record.getR33_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row33
		// Column E
		cell6 = row.createCell(4);
		if (record.getR33_retail_banking() != null) {
			cell6.setCellValue(record.getR33_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row33
		// Column F
		cell7 = row.createCell(5);
		if (record.getR33_commercial_banking() != null) {
			cell7.setCellValue(record.getR33_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
	
		//row33
		// Column G
		cell8 = row.createCell(6);
		if (record.getR33_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR33_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
	
		//row33
		// Column H
		cell9 = row.createCell(7);
		if (record.getR33_agency_services() != null) {
			cell9.setCellValue(record.getR33_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row33
		// Column I
		cell10 = row.createCell(8);
		if (record.getR33_asset_management() != null) {
			cell10.setCellValue(record.getR33_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row33
		// Column J
		cell11 = row.createCell(9);
		if (record.getR33_retail_brokerage() != null) {
			cell11.setCellValue(record.getR33_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

			//row30
			row = sheet.getRow(29);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR34_corporate_finance() != null) {
			cell4.setCellValue(record.getR34_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row34
		// Column D
		cell5 = row.createCell(3);
		if (record.getR34_trading_and_sales() != null) {
			cell5.setCellValue(record.getR34_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row34
		// Column E
		cell6 = row.createCell(4);
		if (record.getR34_retail_banking() != null) {
			cell6.setCellValue(record.getR34_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row34
		// Column F
		cell7 = row.createCell(5);
		if (record.getR34_commercial_banking() != null) {
			cell7.setCellValue(record.getR34_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row34
		// Column G
		cell8 = row.createCell(6);
		if (record.getR34_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR34_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row34
		// Column H
		cell9 = row.createCell(7);
		if (record.getR34_agency_services() != null) {
			cell9.setCellValue(record.getR34_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row34
		// Column I
		cell10 = row.createCell(8);
		if (record.getR34_asset_management() != null) {
			cell10.setCellValue(record.getR34_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row34
		// Column J
		cell11 = row.createCell(9);
		if (record.getR34_retail_brokerage() != null) {
			cell11.setCellValue(record.getR34_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		//row31
			row = sheet.getRow(30);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR35_corporate_finance() != null) {
			cell4.setCellValue(record.getR35_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row35
		// Column D
		cell5 = row.createCell(3);
		if (record.getR35_trading_and_sales() != null) {
			cell5.setCellValue(record.getR35_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row35
		// Column E
		cell6 = row.createCell(4);
		if (record.getR35_retail_banking() != null) {
			cell6.setCellValue(record.getR35_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row35
		// Column F
		cell7 = row.createCell(5);
		if (record.getR35_commercial_banking() != null) {
			cell7.setCellValue(record.getR35_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row35
		// Column G
		cell8 = row.createCell(6);
		if (record.getR35_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR35_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row35
		// Column H
		cell9 = row.createCell(7);
		if (record.getR35_agency_services() != null) {
			cell9.setCellValue(record.getR35_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row35
		// Column I
		cell10 = row.createCell(8);
		if (record.getR35_asset_management() != null) {
			cell10.setCellValue(record.getR35_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row35
		// Column J
		cell11 = row.createCell(9);
		if (record.getR35_retail_brokerage() != null) {
			cell11.setCellValue(record.getR35_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

			//row32
			row = sheet.getRow(31);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR36_corporate_finance() != null) {
			cell4.setCellValue(record.getR36_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row36
		// Column D
		cell5 = row.createCell(3);
		if (record.getR36_trading_and_sales() != null) {
			cell5.setCellValue(record.getR36_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row36
		// Column E
		cell6 = row.createCell(4);
		if (record.getR36_retail_banking() != null) {
			cell6.setCellValue(record.getR36_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row36
		// Column F
		cell7 = row.createCell(5);
		if (record.getR36_commercial_banking() != null) {
			cell7.setCellValue(record.getR36_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row36
		// Column G
		cell8 = row.createCell(6);
		if (record.getR36_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR36_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row36
		// Column H
		cell9 = row.createCell(7);
		if (record.getR36_agency_services() != null) {
			cell9.setCellValue(record.getR36_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row36
		// Column I
		cell10 = row.createCell(8);
		if (record.getR36_asset_management() != null) {
			cell10.setCellValue(record.getR36_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row36
		// Column J
		cell11 = row.createCell(9);
		if (record.getR36_retail_brokerage() != null) {
			cell11.setCellValue(record.getR36_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

	    //row33
		row = sheet.getRow(32);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR37_corporate_finance() != null) {
			cell4.setCellValue(record.getR37_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row37
		// Column D
		cell5 = row.createCell(3);
		if (record.getR37_trading_and_sales() != null) {
			cell5.setCellValue(record.getR37_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row37
		// Column E
		cell6 = row.createCell(4);
		if (record.getR37_retail_banking() != null) {
			cell6.setCellValue(record.getR37_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row37
		// Column F
		cell7 = row.createCell(5);
		if (record.getR37_commercial_banking() != null) {
			cell7.setCellValue(record.getR37_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row37
		// Column G
		cell8 = row.createCell(6);
		if (record.getR37_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR37_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row37
		// Column H
		cell9 = row.createCell(7);
		if (record.getR37_agency_services() != null) {
			cell9.setCellValue(record.getR37_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row37
		// Column I
		cell10 = row.createCell(8);
		if (record.getR37_asset_management() != null) {
			cell10.setCellValue(record.getR37_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row37
		// Column J
		cell11 = row.createCell(9);
		if (record.getR37_retail_brokerage() != null) {
			cell11.setCellValue(record.getR37_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

	 //row34
		row = sheet.getRow(33);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR38_corporate_finance() != null) {
			cell4.setCellValue(record.getR38_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row38
		// Column D
		cell5 = row.createCell(3);
		if (record.getR38_trading_and_sales() != null) {
			cell5.setCellValue(record.getR38_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row38
		// Column E
		cell6 = row.createCell(4);
		if (record.getR38_retail_banking() != null) {
			cell6.setCellValue(record.getR38_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row38
		// Column F
		cell7 = row.createCell(5);
		if (record.getR38_commercial_banking() != null) {
			cell7.setCellValue(record.getR38_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row38
		// Column G
		cell8 = row.createCell(6);
		if (record.getR38_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR38_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row38
		// Column H
		cell9 = row.createCell(7);
		if (record.getR38_agency_services() != null) {
			cell9.setCellValue(record.getR38_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row38
		// Column I
		cell10 = row.createCell(8);
		if (record.getR38_asset_management() != null) {
			cell10.setCellValue(record.getR38_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row38
		// Column J
		cell11 = row.createCell(9);
		if (record.getR38_retail_brokerage() != null) {
			cell11.setCellValue(record.getR38_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row35
		row = sheet.getRow(34);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR39_corporate_finance() != null) {
			cell4.setCellValue(record.getR39_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row39
		// Column D
		cell5 = row.createCell(3);
		if (record.getR39_trading_and_sales() != null) {
			cell5.setCellValue(record.getR39_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row39
		// Column E
		cell6 = row.createCell(4);
		if (record.getR39_retail_banking() != null) {
			cell6.setCellValue(record.getR39_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row39
		// Column F
		cell7 = row.createCell(5);
		if (record.getR39_commercial_banking() != null) {
			cell7.setCellValue(record.getR39_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row39
		// Column G
		cell8 = row.createCell(6);
		if (record.getR39_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR39_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row39
		// Column H
		cell9 = row.createCell(7);
		if (record.getR39_agency_services() != null) {
			cell9.setCellValue(record.getR39_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row39
		// Column I
		cell10 = row.createCell(8);
		if (record.getR39_asset_management() != null) {
			cell10.setCellValue(record.getR39_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row39
		// Column J
		cell11 = row.createCell(9);
		if (record.getR39_retail_brokerage() != null) {
			cell11.setCellValue(record.getR39_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		
		 //row36
		row = sheet.getRow(35);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR44_corporate_finance() != null) {
			cell4.setCellValue(record.getR44_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row44
		// Column D
		cell5 = row.createCell(3);
		if (record.getR44_trading_and_sales() != null) {
			cell5.setCellValue(record.getR44_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row44
		// Column E
		cell6 = row.createCell(4);
		if (record.getR44_retail_banking() != null) {
			cell6.setCellValue(record.getR44_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row44
		// Column F
		cell7 = row.createCell(5);
		if (record.getR44_commercial_banking() != null) {
			cell7.setCellValue(record.getR44_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row44
		// Column G
		cell8 = row.createCell(6);
		if (record.getR44_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR44_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row44
		// Column H
		cell9 = row.createCell(7);
		if (record.getR44_agency_services() != null) {
			cell9.setCellValue(record.getR44_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row44
		// Column I
		cell10 = row.createCell(8);
		if (record.getR44_asset_management() != null) {
			cell10.setCellValue(record.getR44_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row44
		// Column J
		cell11 = row.createCell(9);
		if (record.getR44_retail_brokerage() != null) {
			cell11.setCellValue(record.getR44_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row37
		row = sheet.getRow(36);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR45_corporate_finance() != null) {
			cell4.setCellValue(record.getR45_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row45
		// Column D
		cell5 = row.createCell(3);
		if (record.getR45_trading_and_sales() != null) {
			cell5.setCellValue(record.getR45_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row45
		// Column E
		cell6 = row.createCell(4);
		if (record.getR45_retail_banking() != null) {
			cell6.setCellValue(record.getR45_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row45
		// Column F
		cell7 = row.createCell(5);
		if (record.getR45_commercial_banking() != null) {
			cell7.setCellValue(record.getR45_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row45
		// Column G
		cell8 = row.createCell(6);
		if (record.getR45_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR45_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row45
		// Column H
		cell9 = row.createCell(7);
		if (record.getR45_agency_services() != null) {
			cell9.setCellValue(record.getR45_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row45
		// Column I
		cell10 = row.createCell(8);
		if (record.getR45_asset_management() != null) {
			cell10.setCellValue(record.getR45_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row45
		// Column J
		cell11 = row.createCell(9);
		if (record.getR45_retail_brokerage() != null) {
			cell11.setCellValue(record.getR45_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

	 //row38
		row = sheet.getRow(37);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR46_corporate_finance() != null) {
			cell4.setCellValue(record.getR46_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row46
		// Column D
		cell5 = row.createCell(3);
		if (record.getR46_trading_and_sales() != null) {
			cell5.setCellValue(record.getR46_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row46
		// Column E
		cell6 = row.createCell(4);
		if (record.getR46_retail_banking() != null) {
			cell6.setCellValue(record.getR46_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row46
		// Column F
		cell7 = row.createCell(5);
		if (record.getR46_commercial_banking() != null) {
			cell7.setCellValue(record.getR46_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row46
		// Column G
		cell8 = row.createCell(6);
		if (record.getR46_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR46_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row46
		// Column H
		cell9 = row.createCell(7);
		if (record.getR46_agency_services() != null) {
			cell9.setCellValue(record.getR46_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row46
		// Column I
		cell10 = row.createCell(8);
		if (record.getR46_asset_management() != null) {
			cell10.setCellValue(record.getR46_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row46
		// Column J
		cell11 = row.createCell(9);
		if (record.getR46_retail_brokerage() != null) {
			cell11.setCellValue(record.getR46_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row39
		row = sheet.getRow(38);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR47_corporate_finance() != null) {
			cell4.setCellValue(record.getR47_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row47
		// Column D
		cell5 = row.createCell(3);
		if (record.getR47_trading_and_sales() != null) {
			cell5.setCellValue(record.getR47_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row47
		// Column E
		cell6 = row.createCell(4);
		if (record.getR47_retail_banking() != null) {
			cell6.setCellValue(record.getR47_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row47
		// Column F
		cell7 = row.createCell(5);
		if (record.getR47_commercial_banking() != null) {
			cell7.setCellValue(record.getR47_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row47
		// Column G
		cell8 = row.createCell(6);
		if (record.getR47_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR47_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row47
		// Column H
		cell9 = row.createCell(7);
		if (record.getR47_agency_services() != null) {
			cell9.setCellValue(record.getR47_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row47
		// Column I
		cell10 = row.createCell(8);
		if (record.getR47_asset_management() != null) {
			cell10.setCellValue(record.getR47_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row47
		// Column J
		cell11 = row.createCell(9);
		if (record.getR47_retail_brokerage() != null) {
			cell11.setCellValue(record.getR47_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

	 //row40
		row = sheet.getRow(39);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR48_corporate_finance() != null) {
			cell4.setCellValue(record.getR48_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row48
		// Column D
		cell5 = row.createCell(3);
		if (record.getR48_trading_and_sales() != null) {
			cell5.setCellValue(record.getR48_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row48
		// Column E
		cell6 = row.createCell(4);
		if (record.getR48_retail_banking() != null) {
			cell6.setCellValue(record.getR48_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row48
		// Column F
		cell7 = row.createCell(5);
		if (record.getR48_commercial_banking() != null) {
			cell7.setCellValue(record.getR48_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row48
		// Column G
		cell8 = row.createCell(6);
		if (record.getR48_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR48_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row48
		// Column H
		cell9 = row.createCell(7);
		if (record.getR48_agency_services() != null) {
			cell9.setCellValue(record.getR48_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row48
		// Column I
		cell10 = row.createCell(8);
		if (record.getR48_asset_management() != null) {
			cell10.setCellValue(record.getR48_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row48
		// Column J
		cell11 = row.createCell(9);
		if (record.getR48_retail_brokerage() != null) {
			cell11.setCellValue(record.getR48_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row41
		row = sheet.getRow(40);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR49_corporate_finance() != null) {
			cell4.setCellValue(record.getR49_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row49
		// Column D
		cell5 = row.createCell(3);
		if (record.getR49_trading_and_sales() != null) {
			cell5.setCellValue(record.getR49_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row49
		// Column E
		cell6 = row.createCell(4);
		if (record.getR49_retail_banking() != null) {
			cell6.setCellValue(record.getR49_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row49
		// Column F
		cell7 = row.createCell(5);
		if (record.getR49_commercial_banking() != null) {
			cell7.setCellValue(record.getR49_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row49
		// Column G
		cell8 = row.createCell(6);
		if (record.getR49_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR49_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row49
		// Column H
		cell9 = row.createCell(7);
		if (record.getR49_agency_services() != null) {
			cell9.setCellValue(record.getR49_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row49
		// Column I
		cell10 = row.createCell(8);
		if (record.getR49_asset_management() != null) {
			cell10.setCellValue(record.getR49_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row49
		// Column J
		cell11 = row.createCell(9);
		if (record.getR49_retail_brokerage() != null) {
			cell11.setCellValue(record.getR49_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

			 //row42
		row = sheet.getRow(41);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR50_corporate_finance() != null) {
			cell4.setCellValue(record.getR50_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row50
		// Column D
		cell5 = row.createCell(3);
		if (record.getR50_trading_and_sales() != null) {
			cell5.setCellValue(record.getR50_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row50
		// Column E
		cell6 = row.createCell(4);
		if (record.getR50_retail_banking() != null) {
			cell6.setCellValue(record.getR50_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row50
		// Column F
		cell7 = row.createCell(5);
		if (record.getR50_commercial_banking() != null) {
			cell7.setCellValue(record.getR50_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row50
		// Column G
		cell8 = row.createCell(6);
		if (record.getR50_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR50_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row50
		// Column H
		cell9 = row.createCell(7);
		if (record.getR50_agency_services() != null) {
			cell9.setCellValue(record.getR50_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row50
		// Column I
		cell10 = row.createCell(8);
		if (record.getR50_asset_management() != null) {
			cell10.setCellValue(record.getR50_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row50
		// Column J
		cell11 = row.createCell(9);
		if (record.getR50_retail_brokerage() != null) {
			cell11.setCellValue(record.getR50_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row43
		row = sheet.getRow(42);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR51_corporate_finance() != null) {
			cell4.setCellValue(record.getR51_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row51
		// Column D
		cell5 = row.createCell(3);
		if (record.getR51_trading_and_sales() != null) {
			cell5.setCellValue(record.getR51_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row51
		// Column E
		cell6 = row.createCell(4);
		if (record.getR51_retail_banking() != null) {
			cell6.setCellValue(record.getR51_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row51
		// Column F
		cell7 = row.createCell(5);
		if (record.getR51_commercial_banking() != null) {
			cell7.setCellValue(record.getR51_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row51
		// Column G
		cell8 = row.createCell(6);
		if (record.getR51_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR51_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row51
		// Column H
		cell9 = row.createCell(7);
		if (record.getR51_agency_services() != null) {
			cell9.setCellValue(record.getR51_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row51
		// Column I
		cell10 = row.createCell(8);
		if (record.getR51_asset_management() != null) {
			cell10.setCellValue(record.getR51_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row51
		// Column J
		cell11 = row.createCell(9);
		if (record.getR51_retail_brokerage() != null) {
			cell11.setCellValue(record.getR51_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row44
		row = sheet.getRow(43);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR52_corporate_finance() != null) {
			cell4.setCellValue(record.getR52_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row52
		// Column D
		cell5 = row.createCell(3);
		if (record.getR52_trading_and_sales() != null) {
			cell5.setCellValue(record.getR52_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row52
		// Column E
		cell6 = row.createCell(4);
		if (record.getR52_retail_banking() != null) {
			cell6.setCellValue(record.getR52_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row52
		// Column F
		cell7 = row.createCell(5);
		if (record.getR52_commercial_banking() != null) {
			cell7.setCellValue(record.getR52_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row52
		// Column G
		cell8 = row.createCell(6);
		if (record.getR52_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR52_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row52
		// Column H
		cell9 = row.createCell(7);
		if (record.getR52_agency_services() != null) {
			cell9.setCellValue(record.getR52_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row52
		// Column I
		cell10 = row.createCell(8);
		if (record.getR52_asset_management() != null) {
			cell10.setCellValue(record.getR52_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row52
		// Column J
		cell11 = row.createCell(9);
		if (record.getR52_retail_brokerage() != null) {
			cell11.setCellValue(record.getR52_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row45
		row = sheet.getRow(44);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR53_corporate_finance() != null) {
			cell4.setCellValue(record.getR53_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row53
		// Column D
		cell5 = row.createCell(3);
		if (record.getR53_trading_and_sales() != null) {
			cell5.setCellValue(record.getR53_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row53
		// Column E
		cell6 = row.createCell(4);
		if (record.getR53_retail_banking() != null) {
			cell6.setCellValue(record.getR53_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row53
		// Column F
		cell7 = row.createCell(5);
		if (record.getR53_commercial_banking() != null) {
			cell7.setCellValue(record.getR53_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row53
		// Column G
		cell8 = row.createCell(6);
		if (record.getR53_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR53_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row53
		// Column H
		cell9 = row.createCell(7);
		if (record.getR53_agency_services() != null) {
			cell9.setCellValue(record.getR53_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row53
		// Column I
		cell10 = row.createCell(8);
		if (record.getR53_asset_management() != null) {
			cell10.setCellValue(record.getR53_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row53
		// Column J
		cell11 = row.createCell(9);
		if (record.getR53_retail_brokerage() != null) {
			cell11.setCellValue(record.getR53_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row46
		row = sheet.getRow(45);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR54_corporate_finance() != null) {
			cell4.setCellValue(record.getR54_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row54
		// Column D
		cell5 = row.createCell(3);
		if (record.getR54_trading_and_sales() != null) {
			cell5.setCellValue(record.getR54_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row54
		// Column E
		cell6 = row.createCell(4);
		if (record.getR54_retail_banking() != null) {
			cell6.setCellValue(record.getR54_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row54
		// Column F
		cell7 = row.createCell(5);
		if (record.getR54_commercial_banking() != null) {
			cell7.setCellValue(record.getR54_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row54
		// Column G
		cell8 = row.createCell(6);
		if (record.getR54_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR54_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row54
		// Column H
		cell9 = row.createCell(7);
		if (record.getR54_agency_services() != null) {
			cell9.setCellValue(record.getR54_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row54
		// Column I
		cell10 = row.createCell(8);
		if (record.getR54_asset_management() != null) {
			cell10.setCellValue(record.getR54_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row54
		// Column J
		cell11 = row.createCell(9);
		if (record.getR54_retail_brokerage() != null) {
			cell11.setCellValue(record.getR54_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}

		 //row47
		row = sheet.getRow(46);
		// Column C
		cell4 = row.createCell(2);
		if (record.getR55_corporate_finance() != null) {
			cell4.setCellValue(record.getR55_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		//row55
		// Column D
		cell5 = row.createCell(3);
		if (record.getR55_trading_and_sales() != null) {
			cell5.setCellValue(record.getR55_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		//row55
		// Column E
		cell6 = row.createCell(4);
		if (record.getR55_retail_banking() != null) {
			cell6.setCellValue(record.getR55_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		//row55
		// Column F
		cell7 = row.createCell(5);
		if (record.getR55_commercial_banking() != null) {
			cell7.setCellValue(record.getR55_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		//row55
		// Column G
		cell8 = row.createCell(6);
		if (record.getR55_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR55_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		//row55
		// Column H
		cell9 = row.createCell(7);
		if (record.getR55_agency_services() != null) {
			cell9.setCellValue(record.getR55_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		//row55
		// Column I
		cell10 = row.createCell(8);
		if (record.getR55_asset_management() != null) {
			cell10.setCellValue(record.getR55_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		//row55
		// Column J
		cell11 = row.createCell(9);
		if (record.getR55_retail_brokerage() != null) {
			cell11.setCellValue(record.getR55_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
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
		




}
	
	
