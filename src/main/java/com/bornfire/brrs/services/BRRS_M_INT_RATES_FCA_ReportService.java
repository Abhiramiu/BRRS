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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_INT_RATES_FCA_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_FCA_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_FCA_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_FCA_RESUB_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_FCA_RESUB_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_FCA_Summary_Repo;
import com.bornfire.brrs.entities.M_INT_RATES_FCA_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_FCA_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_FCA_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_FCA_RESUB_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_FCA_RESUB_Summary_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_FCA_Summary_Entity;


import org.springframework.beans.BeanUtils;

@Component
@Service

public class BRRS_M_INT_RATES_FCA_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_INT_RATES_FCA_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_INT_RATES_FCA_RESUB_Summary_Repo M_INT_RATES_FCA_resub_summary_repo;
	
@Autowired
	BRRS_M_INT_RATES_FCA_RESUB_Detail_Repo M_INT_RATES_FCA_resub_detail_repo;

	

	@Autowired
	BRRS_M_INT_RATES_FCA_Summary_Repo M_INT_RATES_FCA_Summary_Repo;

	@Autowired
	BRRS_M_INT_RATES_FCA_Archival_Summary_Repo M_INT_RATES_FCA_Archival_Summary_Repo;

	@Autowired
	BRRS_M_INT_RATES_FCA_Detail_Repo M_INT_RATES_FCA_Detail_Repo;

	@Autowired
	BRRS_M_INT_RATES_FCA_Archival_Detail_Repo M_INT_RATES_FCA_Archival_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getINT_RATES_FCAView(
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

	            List<M_INT_RATES_FCA_Archival_Summary_Entity> T1Master =
	                    M_INT_RATES_FCA_Archival_Summary_Repo
	                            .getdatabydateListarchival(d1, version);

	            System.out.println("Archival Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- RESUB SUMMARY ----------
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	            List<M_INT_RATES_FCA_RESUB_Summary_Entity> T1Master =
	                    M_INT_RATES_FCA_resub_summary_repo
	                            .getdatabydateListarchival(d1, version);

	            System.out.println("Resub Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "resub");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- NORMAL SUMMARY ----------
	        else {

	            List<M_INT_RATES_FCA_Summary_Entity> T1Master =
	                    M_INT_RATES_FCA_Summary_Repo
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

	                List<M_INT_RATES_FCA_Archival_Detail_Entity> T1Master =
	                        M_INT_RATES_FCA_Archival_Detail_Repo
	                                .getdatabydateListarchival(d1, version);

	                System.out.println("Archival Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- RESUB DETAIL ---------------------------------------------------------------------------------------------------------------------------------
	            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	                List<M_INT_RATES_FCA_RESUB_Detail_Entity> T1Master =
	                        M_INT_RATES_FCA_resub_detail_repo
	                                .getdatabydateListarchival(d1, version);

	                System.out.println("Resub Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "resub");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- NORMAL DETAIL ----------
	            else {

	            	 List<M_INT_RATES_FCA_Detail_Entity> T1Master =
		                        M_INT_RATES_FCA_Detail_Repo
		                                .getdatabydateList(d1);

	                System.out.println("Normal Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }
	        }

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_INT_RATES_FCA");

	    System.out.println("View set to: " + mv.getViewName());

	    return mv;
	}
	
	@Transactional
	public void updateReport(  M_INT_RATES_FCA_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // 1️⃣ Fetch existing SUMMARY
	     M_INT_RATES_FCA_Summary_Entity existingSummary =
	            M_INT_RATES_FCA_Summary_Repo.findById(updatedEntity.getReportDate())
	                    .orElseThrow(() -> new RuntimeException(
	                            "Summary record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    // 2️⃣ Fetch or create DETAIL
	      M_INT_RATES_FCA_Detail_Entity existingDetail =
	            M_INT_RATES_FCA_Detail_Repo.findById(updatedEntity.getReportDate())
	                    .orElseGet(() -> {
	                          M_INT_RATES_FCA_Detail_Entity d = new   M_INT_RATES_FCA_Detail_Entity();
	                        d.setReport_date(updatedEntity.getReportDate());
	                        return d;
	                    });

	   try {
			// 1️⃣ Loop through R14 to R100
			for (int i = 10; i <= 15; i++) {
				String prefix = "R" + i + "_";

				String[] fields = {"CURRENT",
				        "CALL",
				        "SAVINGS",
				        "NOTICE_0_31_DAYS",
				        "NOTICE_32_88_DAYS",
				        "91_DEPOSIT_DAY",
				        "FD_1_6_MONTHS",
				        "FD_7_12_MONTHS",
				        "FD_13_18_MONTHS",
				        "FD_19_24_MONTHS",
				        "FD_OVER_24_MONTHS",
				        "TOTAL"};

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter =
	                              M_INT_RATES_FCA_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter =
	                              M_INT_RATES_FCA_Summary_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Method detailSetter =
	                              M_INT_RATES_FCA_Detail_Entity.class.getMethod(
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
	    M_INT_RATES_FCA_Summary_Repo.save(existingSummary);
	    M_INT_RATES_FCA_Detail_Repo.save(existingDetail);
	}

	
	// RESUB VIEW

	
		public List<Object[]> getM_INT_RATES_FCAResub() {
		    List<Object[]> resubList = new ArrayList<>();
		    try {
		        List<M_INT_RATES_FCA_Archival_Summary_Entity> latestArchivalList =
		        		M_INT_RATES_FCA_Archival_Summary_Repo.getdatabydateListWithVersion();

		        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
		            for (M_INT_RATES_FCA_Archival_Summary_Entity entity : latestArchivalList) {
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
		        System.err.println("Error fetching M_INT_RATES_FCA Resub data: " + e.getMessage());
		        e.printStackTrace();
		    }
		    return resubList;
		}

		
		@Transactional
	    public void updateResubReport(M_INT_RATES_FCA_RESUB_Summary_Entity updatedEntity) {

	        Date reportDate = updatedEntity.getReport_date();

	        // ----------------------------------------------------
	        // GET CURRENT VERSION FROM RESUB TABLE
	        // ----------------------------------------------------

	        BigDecimal maxResubVer =
	            M_INT_RATES_FCA_resub_summary_repo.findMaxVersion(reportDate);

	        if (maxResubVer == null)
	            throw new RuntimeException("No record for: " + reportDate);

	        BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

	        Date now = new Date();

	        // ====================================================
	        // 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
	        // ====================================================

	        M_INT_RATES_FCA_RESUB_Summary_Entity resubSummary =
	            new M_INT_RATES_FCA_RESUB_Summary_Entity();

	        BeanUtils.copyProperties(updatedEntity, resubSummary,
	            "reportDate", "reportVersion", "reportResubDate");

	        resubSummary.setReport_date(reportDate);
	        resubSummary.setReport_version(newVersion);
	        resubSummary.setReportResubDate(now);

	        // ====================================================
	        // 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
	        // ====================================================

	        M_INT_RATES_FCA_RESUB_Detail_Entity resubDetail =
	            new M_INT_RATES_FCA_RESUB_Detail_Entity();

	        BeanUtils.copyProperties(updatedEntity, resubDetail,
	            "reportDate", "reportVersion", "reportResubDate");

	        resubDetail.setReport_date(reportDate);
	        resubDetail.setReport_version(newVersion);
	        resubDetail.setReportResubDate(now);

	        // ====================================================
	        // 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
	        // ====================================================

	        M_INT_RATES_FCA_Archival_Summary_Entity archSummary =
	            new M_INT_RATES_FCA_Archival_Summary_Entity();

	        BeanUtils.copyProperties(updatedEntity, archSummary,
	            "reportDate", "reportVersion", "reportResubDate");

	        archSummary.setReport_date(reportDate);
	        archSummary.setReport_version(newVersion);   // SAME VERSION
	        archSummary.setReportResubDate(now);

	        // ====================================================
	        // 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
	        // ====================================================

	        M_INT_RATES_FCA_Archival_Detail_Entity archDetail =
	            new M_INT_RATES_FCA_Archival_Detail_Entity();

	        BeanUtils.copyProperties(updatedEntity, archDetail,
	            "reportDate", "reportVersion", "reportResubDate");

	        archDetail.setReport_date(reportDate);
	        archDetail.setReport_version(newVersion);    // SAME VERSION
	        archDetail.setReportResubDate(now);

	        // ====================================================
	        // 6️⃣ SAVE ALL WITH SAME DATA
	        // ====================================================

	        M_INT_RATES_FCA_resub_summary_repo.save(resubSummary);
	        M_INT_RATES_FCA_resub_detail_repo.save(resubDetail);

	        M_INT_RATES_FCA_Archival_Summary_Repo.save(archSummary);
	        M_INT_RATES_FCA_Archival_Detail_Repo.save(archDetail);
	    }
		
		
		//Archival View
			public List<Object[]> getM_INT_RATES_FCAArchival() {
				List<Object[]> archivalList = new ArrayList<>();

				try {
					List<M_INT_RATES_FCA_Archival_Summary_Entity> repoData = M_INT_RATES_FCA_Archival_Summary_Repo
							.getdatabydateListWithVersion();

					if (repoData != null && !repoData.isEmpty()) {
						for (M_INT_RATES_FCA_Archival_Summary_Entity entity : repoData) {
							Object[] row = new Object[] {
									entity.getReport_date(), 
									entity.getReport_version() 
							};
							archivalList.add(row);
						}

						System.out.println("Fetched " + archivalList.size() + " archival records");
						M_INT_RATES_FCA_Archival_Summary_Entity first = repoData.get(0);
						System.out.println("Latest archival version: " + first.getReport_version());
					} else {
						System.out.println("No archival data found.");
					}

				} catch (Exception e) {
					System.err.println("Error fetching  M_INT_RATES_FCA  Archival data: " + e.getMessage());
					e.printStackTrace();
				}

				return archivalList;
			}
		
	

	

			// Normal format Excel

			public byte[] getM_INTRATESFCAExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
						return getExcelM_INTRATESFCAARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_INT_RATES_FCAResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else {

					if ("email".equalsIgnoreCase(format) && version == null) {
						logger.info("Got format as Email");
						logger.info("Service: Generating Email report for version {}", version);
						return BRRS_M_INT_RATES_FCAEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} else {

						// Fetch data

						List<M_INT_RATES_FCA_Summary_Entity> dataList = M_INT_RATES_FCA_Summary_Repo
								.getdatabydateList(dateformat.parse(todate));

						if (dataList.isEmpty()) {
							logger.warn("Service: No data found for BRRS_M_INT_RATES_FCA report. Returning empty result.");
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

							int startRow = 9;

							if (!dataList.isEmpty()) {
								for (int i = 0; i < dataList.size(); i++) {
									M_INT_RATES_FCA_Summary_Entity record = dataList.get(i);
									System.out.println("rownumber=" + startRow + i);
									Row row = sheet.getRow(startRow + i);
									if (row == null) {
										row = sheet.createRow(startRow + i);
									}

			Cell cell1 = row.createCell(1);
							if (record.getR10_CURRENT() != null) {
								cell1.setCellValue(record.getR10_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							Cell cell2 = row.createCell(2);
							if (record.getR10_CALL() != null) {
								cell2.setCellValue(record.getR10_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(3);
							if (record.getR10_SAVINGS() != null) {
								cell3.setCellValue(record.getR10_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							Cell cell4 = row.createCell(4);
							if (record.getR10_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							Cell cell5 = row.createCell(5);
							if (record.getR10_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							Cell cell6 = row.createCell(6);
							if (record.getR10_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR10_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(7);
							if (record.getR10_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR10_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							Cell cell8 = row.createCell(8);
							if (record.getR10_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR10_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							Cell cell9 = row.createCell(9);
							if (record.getR10_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR10_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							Cell cell10 = row.createCell(10);
							if (record.getR10_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR10_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							Cell cell11 = row.createCell(11);
							if (record.getR10_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							Cell cell12 = row.createCell(12);
							if (record.getR10_TOTAL() != null) {
								cell12.setCellValue(record.getR10_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R11 ----------
							row = sheet.getRow(10);
							if (row == null) {
								row = sheet.createRow(10);
							}

							cell1 = row.createCell(1);
							if (record.getR11_CURRENT() != null) {
								cell1.setCellValue(record.getR11_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR11_CALL() != null) {
								cell2.setCellValue(record.getR11_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR11_SAVINGS() != null) {
								cell3.setCellValue(record.getR11_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR11_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR11_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR11_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR11_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR11_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR11_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR11_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR11_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR11_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR11_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR11_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR11_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR11_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR11_TOTAL() != null) {
								cell12.setCellValue(record.getR11_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}


							// ---------- R12 ----------
							row = sheet.getRow(11);
							if (row == null) {
								row = sheet.createRow(11);
							}

							cell1 = row.createCell(1);
							if (record.getR12_CURRENT() != null) {
								cell1.setCellValue(record.getR12_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR12_CALL() != null) {
								cell2.setCellValue(record.getR12_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR12_SAVINGS() != null) {
								cell3.setCellValue(record.getR12_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR12_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR12_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR12_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR12_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR12_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR12_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR12_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR12_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR12_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR12_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR12_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR12_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR12_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR12_TOTAL() != null) {
								cell12.setCellValue(record.getR12_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}


							// ---------- R13 ----------
							row = sheet.getRow(12);
							if (row == null) {
								row = sheet.createRow(12);
							}

							cell1 = row.createCell(1);
							if (record.getR13_CURRENT() != null) {
								cell1.setCellValue(record.getR13_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR13_CALL() != null) {
								cell2.setCellValue(record.getR13_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR13_SAVINGS() != null) {
								cell3.setCellValue(record.getR13_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR13_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR13_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR13_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR13_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR13_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR13_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR13_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR13_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR13_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR13_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR13_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR13_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR13_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR13_TOTAL() != null) {
								cell12.setCellValue(record.getR13_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}


							// ---------- R14 ----------
							row = sheet.getRow(13);
							if (row == null) {
								row = sheet.createRow(13);
							}

							cell1 = row.createCell(1);
							if (record.getR14_CURRENT() != null) {
								cell1.setCellValue(record.getR14_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR14_CALL() != null) {
								cell2.setCellValue(record.getR14_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR14_SAVINGS() != null) {
								cell3.setCellValue(record.getR14_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR14_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR14_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR14_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR14_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR14_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR14_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR14_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR14_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR14_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR14_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR14_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR14_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR14_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR14_TOTAL() != null) {
								cell12.setCellValue(record.getR14_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}
							
							// ---------- R15 ----------
							row = sheet.getRow(14);
							if (row == null) {
							    row = sheet.createRow(14);
							}

							cell1 = row.createCell(1);
							if (record.getR15_CURRENT() != null) {
							    cell1.setCellValue(record.getR15_CURRENT().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(numberStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR15_CALL() != null) {
							    cell2.setCellValue(record.getR15_CALL().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(numberStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR15_SAVINGS() != null) {
							    cell3.setCellValue(record.getR15_SAVINGS().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(numberStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR15_NOTICE_0_31_DAYS() != null) {
							    cell4.setCellValue(record.getR15_NOTICE_0_31_DAYS().doubleValue());
							    cell4.setCellStyle(numberStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(numberStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR15_NOTICE_32_88_DAYS() != null) {
							    cell5.setCellValue(record.getR15_NOTICE_32_88_DAYS().doubleValue());
							    cell5.setCellStyle(numberStyle);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(numberStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR15_91_DEPOSIT_DAY() != null) {
							    cell6.setCellValue(record.getR15_91_DEPOSIT_DAY().doubleValue());
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(numberStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR15_FD_1_6_MONTHS() != null) {
							    cell7.setCellValue(record.getR15_FD_1_6_MONTHS().doubleValue());
							    cell7.setCellStyle(numberStyle);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(numberStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR15_FD_7_12_MONTHS() != null) {
							    cell8.setCellValue(record.getR15_FD_7_12_MONTHS().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(numberStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR15_FD_13_18_MONTHS() != null) {
							    cell9.setCellValue(record.getR15_FD_13_18_MONTHS().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(numberStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR15_FD_19_24_MONTHS() != null) {
							    cell10.setCellValue(record.getR15_FD_19_24_MONTHS().doubleValue());
							    cell10.setCellStyle(numberStyle);
							} else {
							    cell10.setCellValue("");
							    cell10.setCellStyle(numberStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR15_FD_OVER_24_MONTHS() != null) {
							    cell11.setCellValue(record.getR15_FD_OVER_24_MONTHS().doubleValue());
							    cell11.setCellStyle(numberStyle);
							} else {
							    cell11.setCellValue("");
							    cell11.setCellStyle(numberStyle);
							}

							cell12 = row.createCell(12);
							if (record.getR15_TOTAL() != null) {
							    cell12.setCellValue(record.getR15_TOTAL().doubleValue());
							    cell12.setCellStyle(numberStyle);
							} else {
							    cell12.setCellValue("");
							    cell12.setCellStyle(numberStyle);
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

			// Normal Email Excel
			public byte[] BRRS_M_INT_RATES_FCAEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Email Excel generation process in memory.");
				
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_M_INT_RATES_FCAARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_INT_RATES_FCAEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 
				else {
				List<M_INT_RATES_FCA_Summary_Entity> dataList = M_INT_RATES_FCA_Summary_Repo.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_INT_RATES_FCA report. Returning empty result.");
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
							M_INT_RATES_FCA_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

		Cell cell1 = row.createCell(1);
							if (record.getR10_CURRENT() != null) {
								cell1.setCellValue(record.getR10_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							Cell cell2 = row.createCell(2);
							if (record.getR10_CALL() != null) {
								cell2.setCellValue(record.getR10_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(3);
							if (record.getR10_SAVINGS() != null) {
								cell3.setCellValue(record.getR10_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							Cell cell4 = row.createCell(4);
							if (record.getR10_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							Cell cell5 = row.createCell(5);
							if (record.getR10_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							Cell cell6 = row.createCell(6);
							if (record.getR10_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR10_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(7);
							if (record.getR10_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR10_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							Cell cell8 = row.createCell(8);
							if (record.getR10_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR10_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							Cell cell9 = row.createCell(9);
							if (record.getR10_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR10_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							Cell cell10 = row.createCell(10);
							if (record.getR10_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR10_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							Cell cell11 = row.createCell(11);
							if (record.getR10_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							Cell cell12 = row.createCell(12);
							if (record.getR10_TOTAL() != null) {
								cell12.setCellValue(record.getR10_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R11 ----------
							row = sheet.getRow(10);
							if (row == null) {
								row = sheet.createRow(10);
							}

							cell1 = row.createCell(1);
							if (record.getR11_CURRENT() != null) {
								cell1.setCellValue(record.getR11_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR11_CALL() != null) {
								cell2.setCellValue(record.getR11_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR11_SAVINGS() != null) {
								cell3.setCellValue(record.getR11_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR11_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR11_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR11_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR11_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR11_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR11_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR11_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR11_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR11_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR11_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR11_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR11_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR11_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							cell12 = row.createCell(12);
							if (record.getR11_TOTAL() != null) {
								cell12.setCellValue(record.getR11_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R12 ----------
							row = sheet.getRow(11);
							if (row == null) {
								row = sheet.createRow(11);
							}

							cell1 = row.createCell(1);
							if (record.getR12_CURRENT() != null) {
								cell1.setCellValue(record.getR12_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR12_CALL() != null) {
								cell2.setCellValue(record.getR12_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR12_SAVINGS() != null) {
								cell3.setCellValue(record.getR12_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR12_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR12_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR12_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR12_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR12_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR12_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR12_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR12_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR12_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR12_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR12_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR12_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR12_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							cell12 = row.createCell(12);
							if (record.getR12_TOTAL() != null) {
								cell12.setCellValue(record.getR12_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R13 ----------
							row = sheet.getRow(12);
							if (row == null) {
								row = sheet.createRow(12);
							}

							cell1 = row.createCell(1);
							if (record.getR13_CURRENT() != null) {
								cell1.setCellValue(record.getR13_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR13_CALL() != null) {
								cell2.setCellValue(record.getR13_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR13_SAVINGS() != null) {
								cell3.setCellValue(record.getR13_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR13_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR13_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR13_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR13_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR13_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR13_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR13_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR13_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR13_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR13_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR13_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR13_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR13_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
						 cell12 = row.createCell(12);
							if (record.getR13_TOTAL() != null) {
								cell12.setCellValue(record.getR13_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R14 ----------
							row = sheet.getRow(13);
							if (row == null) {
								row = sheet.createRow(13);
							}

							cell1 = row.createCell(1);
							if (record.getR14_CURRENT() != null) {
								cell1.setCellValue(record.getR14_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR14_CALL() != null) {
								cell2.setCellValue(record.getR14_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR14_SAVINGS() != null) {
								cell3.setCellValue(record.getR14_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR14_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR14_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR14_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR14_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR14_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR14_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR14_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR14_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR14_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR14_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR14_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR14_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR14_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
						 cell12 = row.createCell(12);
							if (record.getR14_TOTAL() != null) {
								cell12.setCellValue(record.getR14_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}
							
							
							// ---------- R15 ----------
							row = sheet.getRow(14);
							if (row == null) {
							    row = sheet.createRow(14);
							}

							cell1 = row.createCell(1);
							if (record.getR15_CURRENT() != null) {
							    cell1.setCellValue(record.getR15_CURRENT().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR15_CALL() != null) {
							    cell2.setCellValue(record.getR15_CALL().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR15_SAVINGS() != null) {
							    cell3.setCellValue(record.getR15_SAVINGS().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR15_NOTICE_0_31_DAYS() != null) {
							    cell4.setCellValue(record.getR15_NOTICE_0_31_DAYS().doubleValue());
							    cell4.setCellStyle(numberStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR15_NOTICE_32_88_DAYS() != null) {
							    cell5.setCellValue(record.getR15_NOTICE_32_88_DAYS().doubleValue());
							    cell5.setCellStyle(numberStyle);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR15_91_DEPOSIT_DAY() != null) {
							    cell6.setCellValue(record.getR15_91_DEPOSIT_DAY().doubleValue());
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR15_FD_1_6_MONTHS() != null) {
							    cell7.setCellValue(record.getR15_FD_1_6_MONTHS().doubleValue());
							    cell7.setCellStyle(numberStyle);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR15_FD_7_12_MONTHS() != null) {
							    cell8.setCellValue(record.getR15_FD_7_12_MONTHS().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR15_FD_13_18_MONTHS() != null) {
							    cell9.setCellValue(record.getR15_FD_13_18_MONTHS().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR15_FD_19_24_MONTHS() != null) {
							    cell10.setCellValue(record.getR15_FD_19_24_MONTHS().doubleValue());
							    cell10.setCellStyle(numberStyle);
							} else {
							    cell10.setCellValue("");
							    cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR15_FD_OVER_24_MONTHS() != null) {
							    cell11.setCellValue(record.getR15_FD_OVER_24_MONTHS().doubleValue());
							    cell11.setCellStyle(numberStyle);
							} else {
							    cell11.setCellValue("");
							    cell11.setCellStyle(textStyle);
							}

							cell12 = row.createCell(12);
							if (record.getR15_TOTAL() != null) {
							    cell12.setCellValue(record.getR15_TOTAL().doubleValue());
							    cell12.setCellStyle(numberStyle);
							} else {
							    cell12.setCellValue("");
							    cell12.setCellStyle(textStyle);
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
			
			
			
			// Archival format excel
			public byte[] getExcelM_INTRATESFCAARCHIVAL(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory in Archival.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_M_INT_RATES_FCAARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 

				List<M_INT_RATES_FCA_Archival_Summary_Entity> dataList = M_INT_RATES_FCA_Archival_Summary_Repo
						.getdatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_INT_RATES_FCA report. Returning empty result.");
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
							M_INT_RATES_FCA_Archival_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

			Cell cell1 = row.createCell(1);
							if (record.getR10_CURRENT() != null) {
								cell1.setCellValue(record.getR10_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							Cell cell2 = row.createCell(2);
							if (record.getR10_CALL() != null) {
								cell2.setCellValue(record.getR10_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(3);
							if (record.getR10_SAVINGS() != null) {
								cell3.setCellValue(record.getR10_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							Cell cell4 = row.createCell(4);
							if (record.getR10_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							Cell cell5 = row.createCell(5);
							if (record.getR10_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							Cell cell6 = row.createCell(6);
							if (record.getR10_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR10_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(7);
							if (record.getR10_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR10_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							Cell cell8 = row.createCell(8);
							if (record.getR10_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR10_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							Cell cell9 = row.createCell(9);
							if (record.getR10_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR10_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							Cell cell10 = row.createCell(10);
							if (record.getR10_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR10_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							Cell cell11 = row.createCell(11);
							if (record.getR10_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							Cell cell12 = row.createCell(12);
							if (record.getR10_TOTAL() != null) {
								cell12.setCellValue(record.getR10_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R11 ----------
							row = sheet.getRow(10);
							if (row == null) {
								row = sheet.createRow(10);
							}

							cell1 = row.createCell(1);
							if (record.getR11_CURRENT() != null) {
								cell1.setCellValue(record.getR11_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR11_CALL() != null) {
								cell2.setCellValue(record.getR11_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR11_SAVINGS() != null) {
								cell3.setCellValue(record.getR11_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR11_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR11_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR11_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR11_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR11_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR11_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR11_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR11_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR11_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR11_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR11_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR11_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR11_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR11_TOTAL() != null) {
								cell12.setCellValue(record.getR11_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}


							// ---------- R12 ----------
							row = sheet.getRow(11);
							if (row == null) {
								row = sheet.createRow(11);
							}

							cell1 = row.createCell(1);
							if (record.getR12_CURRENT() != null) {
								cell1.setCellValue(record.getR12_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR12_CALL() != null) {
								cell2.setCellValue(record.getR12_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR12_SAVINGS() != null) {
								cell3.setCellValue(record.getR12_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR12_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR12_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR12_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR12_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR12_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR12_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR12_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR12_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR12_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR12_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR12_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR12_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR12_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR12_TOTAL() != null) {
								cell12.setCellValue(record.getR12_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}


							// ---------- R13 ----------
							row = sheet.getRow(12);
							if (row == null) {
								row = sheet.createRow(12);
							}

							cell1 = row.createCell(1);
							if (record.getR13_CURRENT() != null) {
								cell1.setCellValue(record.getR13_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR13_CALL() != null) {
								cell2.setCellValue(record.getR13_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR13_SAVINGS() != null) {
								cell3.setCellValue(record.getR13_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR13_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR13_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR13_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR13_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR13_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR13_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR13_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR13_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR13_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR13_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR13_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR13_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR13_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR13_TOTAL() != null) {
								cell12.setCellValue(record.getR13_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}


							// ---------- R14 ----------
							row = sheet.getRow(13);
							if (row == null) {
								row = sheet.createRow(13);
							}

							cell1 = row.createCell(1);
							if (record.getR14_CURRENT() != null) {
								cell1.setCellValue(record.getR14_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR14_CALL() != null) {
								cell2.setCellValue(record.getR14_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR14_SAVINGS() != null) {
								cell3.setCellValue(record.getR14_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR14_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR14_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR14_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR14_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR14_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR14_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR14_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR14_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR14_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR14_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR14_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR14_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR14_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							cell12 = row.createCell(12);
							if (record.getR14_TOTAL() != null) {
								cell12.setCellValue(record.getR14_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}
							
							// ---------- R15 ----------
							row = sheet.getRow(14);
							if (row == null) {
							    row = sheet.createRow(14);
							}

							cell1 = row.createCell(1);
							if (record.getR15_CURRENT() != null) {
							    cell1.setCellValue(record.getR15_CURRENT().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(numberStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR15_CALL() != null) {
							    cell2.setCellValue(record.getR15_CALL().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(numberStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR15_SAVINGS() != null) {
							    cell3.setCellValue(record.getR15_SAVINGS().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(numberStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR15_NOTICE_0_31_DAYS() != null) {
							    cell4.setCellValue(record.getR15_NOTICE_0_31_DAYS().doubleValue());
							    cell4.setCellStyle(numberStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(numberStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR15_NOTICE_32_88_DAYS() != null) {
							    cell5.setCellValue(record.getR15_NOTICE_32_88_DAYS().doubleValue());
							    cell5.setCellStyle(numberStyle);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(numberStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR15_91_DEPOSIT_DAY() != null) {
							    cell6.setCellValue(record.getR15_91_DEPOSIT_DAY().doubleValue());
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(numberStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR15_FD_1_6_MONTHS() != null) {
							    cell7.setCellValue(record.getR15_FD_1_6_MONTHS().doubleValue());
							    cell7.setCellStyle(numberStyle);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(numberStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR15_FD_7_12_MONTHS() != null) {
							    cell8.setCellValue(record.getR15_FD_7_12_MONTHS().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(numberStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR15_FD_13_18_MONTHS() != null) {
							    cell9.setCellValue(record.getR15_FD_13_18_MONTHS().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(numberStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR15_FD_19_24_MONTHS() != null) {
							    cell10.setCellValue(record.getR15_FD_19_24_MONTHS().doubleValue());
							    cell10.setCellStyle(numberStyle);
							} else {
							    cell10.setCellValue("");
							    cell10.setCellStyle(numberStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR15_FD_OVER_24_MONTHS() != null) {
							    cell11.setCellValue(record.getR15_FD_OVER_24_MONTHS().doubleValue());
							    cell11.setCellStyle(numberStyle);
							} else {
							    cell11.setCellValue("");
							    cell11.setCellStyle(numberStyle);
							}

							cell12 = row.createCell(12);
							if (record.getR15_TOTAL() != null) {
							    cell12.setCellValue(record.getR15_TOTAL().doubleValue());
							    cell12.setCellStyle(numberStyle);
							} else {
							    cell12.setCellValue("");
							    cell12.setCellStyle(numberStyle);
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



		/// Downloaded for Archival & Resub
			public byte[] BRRS_M_INT_RATES_FCAResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

				if (type.equals("RESUB") & version != null) {

				}

				List<M_INT_RATES_FCA_RESUB_Summary_Entity> dataList = M_INT_RATES_FCA_resub_summary_repo
						.getdatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_INT_RATES_FCA report. Returning empty result.");
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

							M_INT_RATES_FCA_RESUB_Summary_Entity  record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

									Cell cell1 = row.createCell(1);
						if (record.getR10_CURRENT() != null) {
							cell1.setCellValue(record.getR10_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						Cell cell2 = row.createCell(2);
						if (record.getR10_CALL() != null) {
							cell2.setCellValue(record.getR10_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(3);
						if (record.getR10_SAVINGS() != null) {
							cell3.setCellValue(record.getR10_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						Cell cell4 = row.createCell(4);
						if (record.getR10_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						Cell cell5 = row.createCell(5);
						if (record.getR10_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						Cell cell6 = row.createCell(6);
						if (record.getR10_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR10_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						Cell cell7 = row.createCell(7);
						if (record.getR10_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR10_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						Cell cell8 = row.createCell(8);
						if (record.getR10_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR10_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						Cell cell9 = row.createCell(9);
						if (record.getR10_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR10_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						Cell cell10 = row.createCell(10);
						if (record.getR10_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR10_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						Cell cell11 = row.createCell(11);
						if (record.getR10_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						
						Cell cell12 = row.createCell(12);
						if (record.getR10_TOTAL() != null) {
							cell12.setCellValue(record.getR10_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// ---------- R11 ----------
						row = sheet.getRow(10);
						if (row == null) {
							row = sheet.createRow(10);
						}

						cell1 = row.createCell(1);
						if (record.getR11_CURRENT() != null) {
							cell1.setCellValue(record.getR11_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR11_CALL() != null) {
							cell2.setCellValue(record.getR11_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR11_SAVINGS() != null) {
							cell3.setCellValue(record.getR11_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR11_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR11_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR11_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR11_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR11_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR11_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR11_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR11_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR11_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR11_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR11_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR11_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR11_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						cell12 = row.createCell(12);
						if (record.getR11_TOTAL() != null) {
							cell12.setCellValue(record.getR11_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}


						// ---------- R12 ----------
						row = sheet.getRow(11);
						if (row == null) {
							row = sheet.createRow(11);
						}

						cell1 = row.createCell(1);
						if (record.getR12_CURRENT() != null) {
							cell1.setCellValue(record.getR12_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR12_CALL() != null) {
							cell2.setCellValue(record.getR12_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR12_SAVINGS() != null) {
							cell3.setCellValue(record.getR12_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR12_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR12_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR12_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR12_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR12_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR12_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR12_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR12_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR12_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR12_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR12_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR12_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR12_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						cell12 = row.createCell(12);
						if (record.getR12_TOTAL() != null) {
							cell12.setCellValue(record.getR12_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}


						// ---------- R13 ----------
						row = sheet.getRow(12);
						if (row == null) {
							row = sheet.createRow(12);
						}

						cell1 = row.createCell(1);
						if (record.getR13_CURRENT() != null) {
							cell1.setCellValue(record.getR13_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR13_CALL() != null) {
							cell2.setCellValue(record.getR13_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR13_SAVINGS() != null) {
							cell3.setCellValue(record.getR13_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR13_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR13_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR13_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR13_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR13_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR13_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR13_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR13_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR13_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR13_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR13_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR13_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR13_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						cell12 = row.createCell(12);
						if (record.getR13_TOTAL() != null) {
							cell12.setCellValue(record.getR13_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}


						// ---------- R14 ----------
						row = sheet.getRow(13);
						if (row == null) {
							row = sheet.createRow(13);
						}

						cell1 = row.createCell(1);
						if (record.getR14_CURRENT() != null) {
							cell1.setCellValue(record.getR14_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR14_CALL() != null) {
							cell2.setCellValue(record.getR14_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR14_SAVINGS() != null) {
							cell3.setCellValue(record.getR14_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR14_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR14_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR14_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR14_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR14_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR14_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR14_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR14_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR14_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR14_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR14_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR14_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR14_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						cell12 = row.createCell(12);
						if (record.getR14_TOTAL() != null) {
							cell12.setCellValue(record.getR14_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}
						
						// ---------- R15 ----------
						row = sheet.getRow(14);
						if (row == null) {
						    row = sheet.createRow(14);
						}

						cell1 = row.createCell(1);
						if (record.getR15_CURRENT() != null) {
						    cell1.setCellValue(record.getR15_CURRENT().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(numberStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR15_CALL() != null) {
						    cell2.setCellValue(record.getR15_CALL().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(numberStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR15_SAVINGS() != null) {
						    cell3.setCellValue(record.getR15_SAVINGS().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(numberStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR15_NOTICE_0_31_DAYS() != null) {
						    cell4.setCellValue(record.getR15_NOTICE_0_31_DAYS().doubleValue());
						    cell4.setCellStyle(numberStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(numberStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR15_NOTICE_32_88_DAYS() != null) {
						    cell5.setCellValue(record.getR15_NOTICE_32_88_DAYS().doubleValue());
						    cell5.setCellStyle(numberStyle);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(numberStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR15_91_DEPOSIT_DAY() != null) {
						    cell6.setCellValue(record.getR15_91_DEPOSIT_DAY().doubleValue());
						    cell6.setCellStyle(numberStyle);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(numberStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR15_FD_1_6_MONTHS() != null) {
						    cell7.setCellValue(record.getR15_FD_1_6_MONTHS().doubleValue());
						    cell7.setCellStyle(numberStyle);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(numberStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR15_FD_7_12_MONTHS() != null) {
						    cell8.setCellValue(record.getR15_FD_7_12_MONTHS().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(numberStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR15_FD_13_18_MONTHS() != null) {
						    cell9.setCellValue(record.getR15_FD_13_18_MONTHS().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(numberStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR15_FD_19_24_MONTHS() != null) {
						    cell10.setCellValue(record.getR15_FD_19_24_MONTHS().doubleValue());
						    cell10.setCellStyle(numberStyle);
						} else {
						    cell10.setCellValue("");
						    cell10.setCellStyle(numberStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR15_FD_OVER_24_MONTHS() != null) {
						    cell11.setCellValue(record.getR15_FD_OVER_24_MONTHS().doubleValue());
						    cell11.setCellStyle(numberStyle);
						} else {
						    cell11.setCellValue("");
						    cell11.setCellStyle(numberStyle);
						}

						cell12 = row.createCell(12);
						if (record.getR15_TOTAL() != null) {
						    cell12.setCellValue(record.getR15_TOTAL().doubleValue());
						    cell12.setCellStyle(numberStyle);
						} else {
						    cell12.setCellValue("");
						    cell12.setCellStyle(numberStyle);
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

			// Archival Email Excel
			public byte[] BRRS_M_INT_RATES_FCAARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Archival Email Excel generation process in memory.");

				List<M_INT_RATES_FCA_Archival_Summary_Entity> dataList = M_INT_RATES_FCA_Archival_Summary_Repo
						.getdatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_INT_RATES_FCA report. Returning empty result.");
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
							M_INT_RATES_FCA_Archival_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

			
								Cell cell1 = row.createCell(1);
							if (record.getR10_CURRENT() != null) {
								cell1.setCellValue(record.getR10_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							Cell cell2 = row.createCell(2);
							if (record.getR10_CALL() != null) {
								cell2.setCellValue(record.getR10_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(3);
							if (record.getR10_SAVINGS() != null) {
								cell3.setCellValue(record.getR10_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							Cell cell4 = row.createCell(4);
							if (record.getR10_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							Cell cell5 = row.createCell(5);
							if (record.getR10_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							Cell cell6 = row.createCell(6);
							if (record.getR10_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR10_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(7);
							if (record.getR10_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR10_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							Cell cell8 = row.createCell(8);
							if (record.getR10_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR10_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							Cell cell9 = row.createCell(9);
							if (record.getR10_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR10_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							Cell cell10 = row.createCell(10);
							if (record.getR10_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR10_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							Cell cell11 = row.createCell(11);
							if (record.getR10_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							Cell cell12 = row.createCell(12);
							if (record.getR10_TOTAL() != null) {
								cell12.setCellValue(record.getR10_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R11 ----------
							row = sheet.getRow(10);
							if (row == null) {
								row = sheet.createRow(10);
							}

							cell1 = row.createCell(1);
							if (record.getR11_CURRENT() != null) {
								cell1.setCellValue(record.getR11_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR11_CALL() != null) {
								cell2.setCellValue(record.getR11_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR11_SAVINGS() != null) {
								cell3.setCellValue(record.getR11_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR11_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR11_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR11_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR11_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR11_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR11_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR11_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR11_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR11_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR11_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR11_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR11_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR11_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							cell12 = row.createCell(12);
							if (record.getR11_TOTAL() != null) {
								cell12.setCellValue(record.getR11_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R12 ----------
							row = sheet.getRow(11);
							if (row == null) {
								row = sheet.createRow(11);
							}

							cell1 = row.createCell(1);
							if (record.getR12_CURRENT() != null) {
								cell1.setCellValue(record.getR12_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR12_CALL() != null) {
								cell2.setCellValue(record.getR12_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR12_SAVINGS() != null) {
								cell3.setCellValue(record.getR12_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR12_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR12_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR12_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR12_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR12_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR12_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR12_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR12_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR12_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR12_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR12_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR12_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR12_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
							cell12 = row.createCell(12);
							if (record.getR12_TOTAL() != null) {
								cell12.setCellValue(record.getR12_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R13 ----------
							row = sheet.getRow(12);
							if (row == null) {
								row = sheet.createRow(12);
							}

							cell1 = row.createCell(1);
							if (record.getR13_CURRENT() != null) {
								cell1.setCellValue(record.getR13_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR13_CALL() != null) {
								cell2.setCellValue(record.getR13_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR13_SAVINGS() != null) {
								cell3.setCellValue(record.getR13_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR13_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR13_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR13_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR13_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR13_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR13_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR13_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR13_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR13_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR13_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR13_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR13_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR13_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
						 cell12 = row.createCell(12);
							if (record.getR13_TOTAL() != null) {
								cell12.setCellValue(record.getR13_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// ---------- R14 ----------
							row = sheet.getRow(13);
							if (row == null) {
								row = sheet.createRow(13);
							}

							cell1 = row.createCell(1);
							if (record.getR14_CURRENT() != null) {
								cell1.setCellValue(record.getR14_CURRENT().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR14_CALL() != null) {
								cell2.setCellValue(record.getR14_CALL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR14_SAVINGS() != null) {
								cell3.setCellValue(record.getR14_SAVINGS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR14_NOTICE_0_31_DAYS() != null) {
								cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR14_NOTICE_32_88_DAYS() != null) {
								cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR14_91_DEPOSIT_DAY() != null) {
								cell6.setCellValue(record.getR14_91_DEPOSIT_DAY().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR14_FD_1_6_MONTHS() != null) {
								cell7.setCellValue(record.getR14_FD_1_6_MONTHS().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR14_FD_7_12_MONTHS() != null) {
								cell8.setCellValue(record.getR14_FD_7_12_MONTHS().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR14_FD_13_18_MONTHS() != null) {
								cell9.setCellValue(record.getR14_FD_13_18_MONTHS().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR14_FD_19_24_MONTHS() != null) {
								cell10.setCellValue(record.getR14_FD_19_24_MONTHS().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR14_FD_OVER_24_MONTHS() != null) {
								cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}
							
						 cell12 = row.createCell(12);
							if (record.getR14_TOTAL() != null) {
								cell12.setCellValue(record.getR14_TOTAL().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}
							
							
							// ---------- R15 ----------
							row = sheet.getRow(14);
							if (row == null) {
							    row = sheet.createRow(14);
							}

							cell1 = row.createCell(1);
							if (record.getR15_CURRENT() != null) {
							    cell1.setCellValue(record.getR15_CURRENT().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR15_CALL() != null) {
							    cell2.setCellValue(record.getR15_CALL().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR15_SAVINGS() != null) {
							    cell3.setCellValue(record.getR15_SAVINGS().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR15_NOTICE_0_31_DAYS() != null) {
							    cell4.setCellValue(record.getR15_NOTICE_0_31_DAYS().doubleValue());
							    cell4.setCellStyle(numberStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record.getR15_NOTICE_32_88_DAYS() != null) {
							    cell5.setCellValue(record.getR15_NOTICE_32_88_DAYS().doubleValue());
							    cell5.setCellStyle(numberStyle);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record.getR15_91_DEPOSIT_DAY() != null) {
							    cell6.setCellValue(record.getR15_91_DEPOSIT_DAY().doubleValue());
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record.getR15_FD_1_6_MONTHS() != null) {
							    cell7.setCellValue(record.getR15_FD_1_6_MONTHS().doubleValue());
							    cell7.setCellStyle(numberStyle);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record.getR15_FD_7_12_MONTHS() != null) {
							    cell8.setCellValue(record.getR15_FD_7_12_MONTHS().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record.getR15_FD_13_18_MONTHS() != null) {
							    cell9.setCellValue(record.getR15_FD_13_18_MONTHS().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							cell10 = row.createCell(10);
							if (record.getR15_FD_19_24_MONTHS() != null) {
							    cell10.setCellValue(record.getR15_FD_19_24_MONTHS().doubleValue());
							    cell10.setCellStyle(numberStyle);
							} else {
							    cell10.setCellValue("");
							    cell10.setCellStyle(textStyle);
							}

							cell11 = row.createCell(11);
							if (record.getR15_FD_OVER_24_MONTHS() != null) {
							    cell11.setCellValue(record.getR15_FD_OVER_24_MONTHS().doubleValue());
							    cell11.setCellStyle(numberStyle);
							} else {
							    cell11.setCellValue("");
							    cell11.setCellStyle(textStyle);
							}

							cell12 = row.createCell(12);
							if (record.getR15_TOTAL() != null) {
							    cell12.setCellValue(record.getR15_TOTAL().doubleValue());
							    cell12.setCellStyle(numberStyle);
							} else {
							    cell12.setCellValue("");
							    cell12.setCellStyle(textStyle);
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

			// Resub Format excel
			public byte[] BRRS_M_INT_RATES_FCAResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_INT_RATES_FCAEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			}

				List<M_INT_RATES_FCA_RESUB_Summary_Entity> dataList = M_INT_RATES_FCA_resub_summary_repo
						.getdatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_INT_RATES_FCA report. Returning empty result.");
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

							M_INT_RATES_FCA_RESUB_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

										Cell cell1 = row.createCell(1);
						if (record.getR10_CURRENT() != null) {
							cell1.setCellValue(record.getR10_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						Cell cell2 = row.createCell(2);
						if (record.getR10_CALL() != null) {
							cell2.setCellValue(record.getR10_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(3);
						if (record.getR10_SAVINGS() != null) {
							cell3.setCellValue(record.getR10_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						Cell cell4 = row.createCell(4);
						if (record.getR10_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						Cell cell5 = row.createCell(5);
						if (record.getR10_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						Cell cell6 = row.createCell(6);
						if (record.getR10_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR10_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						Cell cell7 = row.createCell(7);
						if (record.getR10_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR10_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						Cell cell8 = row.createCell(8);
						if (record.getR10_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR10_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						Cell cell9 = row.createCell(9);
						if (record.getR10_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR10_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						Cell cell10 = row.createCell(10);
						if (record.getR10_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR10_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						Cell cell11 = row.createCell(11);
						if (record.getR10_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						
						Cell cell12 = row.createCell(12);
						if (record.getR10_TOTAL() != null) {
							cell12.setCellValue(record.getR10_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// ---------- R11 ----------
						row = sheet.getRow(10);
						if (row == null) {
							row = sheet.createRow(10);
						}

						cell1 = row.createCell(1);
						if (record.getR11_CURRENT() != null) {
							cell1.setCellValue(record.getR11_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR11_CALL() != null) {
							cell2.setCellValue(record.getR11_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR11_SAVINGS() != null) {
							cell3.setCellValue(record.getR11_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR11_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR11_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR11_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR11_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR11_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR11_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR11_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR11_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR11_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR11_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR11_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR11_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR11_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						cell12 = row.createCell(12);
						if (record.getR11_TOTAL() != null) {
							cell12.setCellValue(record.getR11_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}


						// ---------- R12 ----------
						row = sheet.getRow(11);
						if (row == null) {
							row = sheet.createRow(11);
						}

						cell1 = row.createCell(1);
						if (record.getR12_CURRENT() != null) {
							cell1.setCellValue(record.getR12_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR12_CALL() != null) {
							cell2.setCellValue(record.getR12_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR12_SAVINGS() != null) {
							cell3.setCellValue(record.getR12_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR12_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR12_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR12_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR12_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR12_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR12_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR12_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR12_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR12_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR12_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR12_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR12_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR12_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						cell12 = row.createCell(12);
						if (record.getR12_TOTAL() != null) {
							cell12.setCellValue(record.getR12_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}


						// ---------- R13 ----------
						row = sheet.getRow(12);
						if (row == null) {
							row = sheet.createRow(12);
						}

						cell1 = row.createCell(1);
						if (record.getR13_CURRENT() != null) {
							cell1.setCellValue(record.getR13_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR13_CALL() != null) {
							cell2.setCellValue(record.getR13_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR13_SAVINGS() != null) {
							cell3.setCellValue(record.getR13_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR13_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR13_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR13_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR13_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR13_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR13_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR13_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR13_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR13_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR13_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR13_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR13_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR13_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						cell12 = row.createCell(12);
						if (record.getR13_TOTAL() != null) {
							cell12.setCellValue(record.getR13_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}


						// ---------- R14 ----------
						row = sheet.getRow(13);
						if (row == null) {
							row = sheet.createRow(13);
						}

						cell1 = row.createCell(1);
						if (record.getR14_CURRENT() != null) {
							cell1.setCellValue(record.getR14_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR14_CALL() != null) {
							cell2.setCellValue(record.getR14_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR14_SAVINGS() != null) {
							cell3.setCellValue(record.getR14_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR14_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR14_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR14_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR14_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR14_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR14_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR14_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR14_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR14_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR14_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR14_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR14_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR14_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						cell12 = row.createCell(12);
						if (record.getR14_TOTAL() != null) {
							cell12.setCellValue(record.getR14_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}
						
						// ---------- R15 ----------
						row = sheet.getRow(14);
						if (row == null) {
						    row = sheet.createRow(14);
						}

						cell1 = row.createCell(1);
						if (record.getR15_CURRENT() != null) {
						    cell1.setCellValue(record.getR15_CURRENT().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(numberStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR15_CALL() != null) {
						    cell2.setCellValue(record.getR15_CALL().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(numberStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR15_SAVINGS() != null) {
						    cell3.setCellValue(record.getR15_SAVINGS().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(numberStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR15_NOTICE_0_31_DAYS() != null) {
						    cell4.setCellValue(record.getR15_NOTICE_0_31_DAYS().doubleValue());
						    cell4.setCellStyle(numberStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(numberStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR15_NOTICE_32_88_DAYS() != null) {
						    cell5.setCellValue(record.getR15_NOTICE_32_88_DAYS().doubleValue());
						    cell5.setCellStyle(numberStyle);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(numberStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR15_91_DEPOSIT_DAY() != null) {
						    cell6.setCellValue(record.getR15_91_DEPOSIT_DAY().doubleValue());
						    cell6.setCellStyle(numberStyle);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(numberStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR15_FD_1_6_MONTHS() != null) {
						    cell7.setCellValue(record.getR15_FD_1_6_MONTHS().doubleValue());
						    cell7.setCellStyle(numberStyle);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(numberStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR15_FD_7_12_MONTHS() != null) {
						    cell8.setCellValue(record.getR15_FD_7_12_MONTHS().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(numberStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR15_FD_13_18_MONTHS() != null) {
						    cell9.setCellValue(record.getR15_FD_13_18_MONTHS().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(numberStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR15_FD_19_24_MONTHS() != null) {
						    cell10.setCellValue(record.getR15_FD_19_24_MONTHS().doubleValue());
						    cell10.setCellStyle(numberStyle);
						} else {
						    cell10.setCellValue("");
						    cell10.setCellStyle(numberStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR15_FD_OVER_24_MONTHS() != null) {
						    cell11.setCellValue(record.getR15_FD_OVER_24_MONTHS().doubleValue());
						    cell11.setCellStyle(numberStyle);
						} else {
						    cell11.setCellValue("");
						    cell11.setCellStyle(numberStyle);
						}

						cell12 = row.createCell(12);
						if (record.getR15_TOTAL() != null) {
						    cell12.setCellValue(record.getR15_TOTAL().doubleValue());
						    cell12.setCellStyle(numberStyle);
						} else {
						    cell12.setCellValue("");
						    cell12.setCellStyle(numberStyle);
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
			public byte[] BRRS_M_INT_RATES_FCAEmailResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Archival Email Excel generation process in memory.");

				List<M_INT_RATES_FCA_RESUB_Summary_Entity> dataList = M_INT_RATES_FCA_resub_summary_repo
						.getdatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_INT_RATES_FCA report. Returning empty result.");
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
							M_INT_RATES_FCA_RESUB_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

			Cell cell1 = row.createCell(1);
						if (record.getR10_CURRENT() != null) {
							cell1.setCellValue(record.getR10_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						Cell cell2 = row.createCell(2);
						if (record.getR10_CALL() != null) {
							cell2.setCellValue(record.getR10_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(3);
						if (record.getR10_SAVINGS() != null) {
							cell3.setCellValue(record.getR10_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						Cell cell4 = row.createCell(4);
						if (record.getR10_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						Cell cell5 = row.createCell(5);
						if (record.getR10_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						Cell cell6 = row.createCell(6);
						if (record.getR10_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR10_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						Cell cell7 = row.createCell(7);
						if (record.getR10_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR10_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						Cell cell8 = row.createCell(8);
						if (record.getR10_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR10_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						Cell cell9 = row.createCell(9);
						if (record.getR10_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR10_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						Cell cell10 = row.createCell(10);
						if (record.getR10_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR10_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						Cell cell11 = row.createCell(11);
						if (record.getR10_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						
						Cell cell12 = row.createCell(12);
						if (record.getR10_TOTAL() != null) {
							cell12.setCellValue(record.getR10_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// ---------- R11 ----------
						row = sheet.getRow(10);
						if (row == null) {
							row = sheet.createRow(10);
						}

						cell1 = row.createCell(1);
						if (record.getR11_CURRENT() != null) {
							cell1.setCellValue(record.getR11_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR11_CALL() != null) {
							cell2.setCellValue(record.getR11_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR11_SAVINGS() != null) {
							cell3.setCellValue(record.getR11_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR11_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR11_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR11_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR11_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR11_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR11_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR11_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR11_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR11_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR11_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR11_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR11_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR11_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						
						cell12 = row.createCell(12);
						if (record.getR11_TOTAL() != null) {
							cell12.setCellValue(record.getR11_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// ---------- R12 ----------
						row = sheet.getRow(11);
						if (row == null) {
							row = sheet.createRow(11);
						}

						cell1 = row.createCell(1);
						if (record.getR12_CURRENT() != null) {
							cell1.setCellValue(record.getR12_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR12_CALL() != null) {
							cell2.setCellValue(record.getR12_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR12_SAVINGS() != null) {
							cell3.setCellValue(record.getR12_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR12_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR12_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR12_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR12_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR12_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR12_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR12_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR12_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR12_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR12_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR12_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR12_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR12_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						
						cell12 = row.createCell(12);
						if (record.getR12_TOTAL() != null) {
							cell12.setCellValue(record.getR12_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// ---------- R13 ----------
						row = sheet.getRow(12);
						if (row == null) {
							row = sheet.createRow(12);
						}

						cell1 = row.createCell(1);
						if (record.getR13_CURRENT() != null) {
							cell1.setCellValue(record.getR13_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR13_CALL() != null) {
							cell2.setCellValue(record.getR13_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR13_SAVINGS() != null) {
							cell3.setCellValue(record.getR13_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR13_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR13_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR13_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR13_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR13_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR13_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR13_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR13_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR13_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR13_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR13_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR13_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR13_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						
					 cell12 = row.createCell(12);
						if (record.getR13_TOTAL() != null) {
							cell12.setCellValue(record.getR13_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// ---------- R14 ----------
						row = sheet.getRow(13);
						if (row == null) {
							row = sheet.createRow(13);
						}

						cell1 = row.createCell(1);
						if (record.getR14_CURRENT() != null) {
							cell1.setCellValue(record.getR14_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR14_CALL() != null) {
							cell2.setCellValue(record.getR14_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR14_SAVINGS() != null) {
							cell3.setCellValue(record.getR14_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR14_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR14_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR14_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR14_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR14_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR14_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR14_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR14_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR14_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR14_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR14_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR14_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR14_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						
					 cell12 = row.createCell(12);
						if (record.getR14_TOTAL() != null) {
							cell12.setCellValue(record.getR14_TOTAL().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}
						
						
						// ---------- R15 ----------
						row = sheet.getRow(14);
						if (row == null) {
						    row = sheet.createRow(14);
						}

						cell1 = row.createCell(1);
						if (record.getR15_CURRENT() != null) {
						    cell1.setCellValue(record.getR15_CURRENT().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR15_CALL() != null) {
						    cell2.setCellValue(record.getR15_CALL().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR15_SAVINGS() != null) {
						    cell3.setCellValue(record.getR15_SAVINGS().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR15_NOTICE_0_31_DAYS() != null) {
						    cell4.setCellValue(record.getR15_NOTICE_0_31_DAYS().doubleValue());
						    cell4.setCellStyle(numberStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR15_NOTICE_32_88_DAYS() != null) {
						    cell5.setCellValue(record.getR15_NOTICE_32_88_DAYS().doubleValue());
						    cell5.setCellStyle(numberStyle);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR15_91_DEPOSIT_DAY() != null) {
						    cell6.setCellValue(record.getR15_91_DEPOSIT_DAY().doubleValue());
						    cell6.setCellStyle(numberStyle);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR15_FD_1_6_MONTHS() != null) {
						    cell7.setCellValue(record.getR15_FD_1_6_MONTHS().doubleValue());
						    cell7.setCellStyle(numberStyle);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR15_FD_7_12_MONTHS() != null) {
						    cell8.setCellValue(record.getR15_FD_7_12_MONTHS().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR15_FD_13_18_MONTHS() != null) {
						    cell9.setCellValue(record.getR15_FD_13_18_MONTHS().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR15_FD_19_24_MONTHS() != null) {
						    cell10.setCellValue(record.getR15_FD_19_24_MONTHS().doubleValue());
						    cell10.setCellStyle(numberStyle);
						} else {
						    cell10.setCellValue("");
						    cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR15_FD_OVER_24_MONTHS() != null) {
						    cell11.setCellValue(record.getR15_FD_OVER_24_MONTHS().doubleValue());
						    cell11.setCellStyle(numberStyle);
						} else {
						    cell11.setCellValue("");
						    cell11.setCellStyle(textStyle);
						}

						cell12 = row.createCell(12);
						if (record.getR15_TOTAL() != null) {
						    cell12.setCellValue(record.getR15_TOTAL().doubleValue());
						    cell12.setCellStyle(numberStyle);
						} else {
						    cell12.setCellValue("");
						    cell12.setCellStyle(textStyle);
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
