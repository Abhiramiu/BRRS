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

import javax.transaction.Transactional;

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

import com.bornfire.brrs.entities.BRRS_M_BOP_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_BOP_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_BOP_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_BOP_RESUB_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_BOP_RESUB_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_BOP_Summary_Repo;
import com.bornfire.brrs.entities.M_BOP_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_BOP_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_BOP_Detail_Entity;
import com.bornfire.brrs.entities.M_BOP_RESUB_Detail_Entity;
import com.bornfire.brrs.entities.M_BOP_RESUB_Summary_Entity;
import com.bornfire.brrs.entities.M_BOP_Summary_Entity;




@Component
@Service
public class BRRS_M_BOP_ReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_BOP_ReportService.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	SessionFactory sessionFactory;
	

	
	@Autowired
	BRRS_M_BOP_Summary_Repo M_BOP_Summary_Repo; 
	
	@Autowired
	BRRS_M_BOP_Archival_Summary_Repo M_BOP_Archival_Summary_Repo; 
	
	
	@Autowired
	BRRS_M_BOP_Detail_Repo M_BOP_Detail_Repo;

	@Autowired
	BRRS_M_BOP_Archival_Detail_Repo M_BOP_Archival_Detail_Repo;
	
	@Autowired
	BRRS_M_BOP_RESUB_Summary_Repo M_BOP_resub_summary_repo;
	
@Autowired
	BRRS_M_BOP_RESUB_Detail_Repo M_BOP_resub_detail_repo;

	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	
	
	
	public ModelAndView getBRRS_M_BOPview(
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

	            List<M_BOP_Archival_Summary_Entity> T1Master =
	                    M_BOP_Archival_Summary_Repo
	                            .getdatabydateListarchival(d1, version);

	            System.out.println("Archival Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- RESUB SUMMARY ----------
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	            List<M_BOP_RESUB_Summary_Entity> T1Master =
	                    M_BOP_resub_summary_repo
	                            .getdatabydateListarchival(d1, version);

	            System.out.println("Resub Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "resub");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- NORMAL SUMMARY ----------
	        else {

	            List<M_BOP_Summary_Entity> T1Master =
	                    M_BOP_Summary_Repo
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

	                List<M_BOP_Archival_Detail_Entity> T1Master =
	                        M_BOP_Archival_Detail_Repo
	                                .getdatabydateListarchival(d1, version);

	                System.out.println("Archival Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- RESUB DETAIL ----
	            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	                List<M_BOP_RESUB_Detail_Entity> T1Master =
	                        M_BOP_resub_detail_repo
	                                .getdatabydateListarchival(d1, version);

	                System.out.println("Resub Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "resub");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- NORMAL DETAIL ----------
	            else {

	            	 List<M_BOP_Detail_Entity> T1Master =
		                        M_BOP_Detail_Repo
		                                .getdatabydateList(d1);

	                System.out.println("Normal Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }
	        }

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_BOP");

	    System.out.println("View set to: " + mv.getViewName());

	    return mv;
	}
	
	
	
	@Transactional
	public void updateReport(M_BOP_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    // 1️⃣ Fetch existing SUMMARY
	     M_BOP_Summary_Entity existingSummary =
	            M_BOP_Summary_Repo.findById(updatedEntity.getReport_date())
	                    .orElseThrow(() -> new RuntimeException(
	                            "Summary record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    // 2️⃣ Fetch or create DETAIL
	      M_BOP_Detail_Entity existingDetail =
	            M_BOP_Detail_Repo.findById(updatedEntity.getReport_date())
	                    .orElseGet(() -> {
	                          M_BOP_Detail_Entity d = new   M_BOP_Detail_Entity();
	                        d.setReport_date(updatedEntity.getReport_date());
	                        return d;
	                    });

	  try {
	        // 1️⃣ Loop from R10 to R63 and copy fields
	    	
	        for (int i = 13; i <= 36; i++) {
				
	        	 String prefix = "R" + i + "_";

	            String[] fields = { "product",
	        	        "open_position",
	        	        "cpdm_dt_inc",
	        	        "cpdm_dt_dec",
	        	        "net",
	        	        "cpdm_dt_der",
	        	        "cpdm_dt_dto",
	        	        "cp" };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter =
	                              M_BOP_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter =
	                              M_BOP_Summary_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Method detailSetter =
	                              M_BOP_Detail_Entity.class.getMethod(
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
	    M_BOP_Summary_Repo.save(existingSummary);
	    M_BOP_Detail_Repo.save(existingDetail);
	}


	
	
	public List<Object[]> getM_BOPResub() {
	    List<Object[]> resubList = new ArrayList<>();
	    try {
	        List<M_BOP_Archival_Summary_Entity> latestArchivalList =
	        		M_BOP_Archival_Summary_Repo.getdatabydateListWithVersion();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (M_BOP_Archival_Summary_Entity entity : latestArchivalList) {
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
	        System.err.println("Error fetching M_EPR Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return resubList;
	}
	
	@Transactional
    public void updateResubReport(M_BOP_RESUB_Summary_Entity updatedEntity) {

        Date reportDate = updatedEntity.getReport_date();

        // ----------------------------------------------------
        // GET CURRENT VERSION FROM RESUB TABLE
        // ----------------------------------------------------

        BigDecimal maxResubVer =
            M_BOP_resub_summary_repo.findMaxVersion(reportDate);

        if (maxResubVer == null)
            throw new RuntimeException("No record for: " + reportDate);

        BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

        Date now = new Date();

        // ====================================================
        // 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
        // ====================================================

        M_BOP_RESUB_Summary_Entity resubSummary =
            new M_BOP_RESUB_Summary_Entity();

        BeanUtils.copyProperties(updatedEntity, resubSummary,
            "reportDate", "reportVersion", "reportResubDate");

        resubSummary.setReport_date(reportDate);
        resubSummary.setReport_version(newVersion);
        resubSummary.setReportResubDate(now);

        // ====================================================
        // 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
        // ====================================================

        M_BOP_RESUB_Detail_Entity resubDetail =
            new M_BOP_RESUB_Detail_Entity();

        BeanUtils.copyProperties(updatedEntity, resubDetail,
            "reportDate", "reportVersion", "reportResubDate");

        resubDetail.setReport_date(reportDate);
        resubDetail.setReport_version(newVersion);
        resubDetail.setReportResubDate(now);

        // ====================================================
        // 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
        // ====================================================

        M_BOP_Archival_Summary_Entity archSummary =
            new M_BOP_Archival_Summary_Entity();

        BeanUtils.copyProperties(updatedEntity, archSummary,
            "reportDate", "reportVersion", "reportResubDate");

        archSummary.setReport_date(reportDate);
        archSummary.setReport_version(newVersion);   // SAME VERSION
        archSummary.setReportResubDate(now);

        // ====================================================
        // 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
        // ====================================================

        M_BOP_Archival_Detail_Entity archDetail =
            new M_BOP_Archival_Detail_Entity();

        BeanUtils.copyProperties(updatedEntity, archDetail,
            "reportDate", "reportVersion", "reportResubDate");

        archDetail.setReport_date(reportDate);
        archDetail.setReport_version(newVersion);    // SAME VERSION
        archDetail.setReportResubDate(now);

        // ====================================================
        // 6️⃣ SAVE ALL WITH SAME DATA
        // ====================================================

        M_BOP_resub_summary_repo.save(resubSummary);
        M_BOP_resub_detail_repo.save(resubDetail);

        M_BOP_Archival_Summary_Repo.save(archSummary);
        M_BOP_Archival_Detail_Repo.save(archDetail);
    }

	//Archival View
			public List<Object[]> getM_BOPArchival() {
				List<Object[]> archivalList = new ArrayList<>();

				try {
					List<M_BOP_Archival_Summary_Entity> repoData = M_BOP_Archival_Summary_Repo
							.getdatabydateListWithVersion();

					if (repoData != null && !repoData.isEmpty()) {
						for (M_BOP_Archival_Summary_Entity entity : repoData) {
							Object[] row = new Object[] {
									entity.getReport_date(), 
									entity.getReport_version() 
							};
							archivalList.add(row);
						}

						System.out.println("Fetched " + archivalList.size() + " archival records");
						M_BOP_Archival_Summary_Entity first = repoData.get(0);
						System.out.println("Latest archival version: " + first.getReport_version());
					} else {
						System.out.println("No archival data found.");
					}

				} catch (Exception e) {
					System.err.println("Error fetching  M_BOP  Archival data: " + e.getMessage());
					e.printStackTrace();
				}

				return archivalList;
			}
			
	 
			// Normal format Excel

			public byte[] getM_BOPExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
						return getExcelM_BOPARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_BOPResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else {

					if ("email".equalsIgnoreCase(format) && version == null) {
						logger.info("Got format as Email");
						logger.info("Service: Generating Email report for version {}", version);
						return BRRS_M_BOPEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} else {

						// Fetch data

						List<M_BOP_Summary_Entity> dataList = M_BOP_Summary_Repo
								.getdatabydateList(dateformat.parse(todate));

						if (dataList.isEmpty()) {
							logger.warn("Service: No data found for BRRS_M_BOP report. Returning empty result.");
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

							int startRow = 12;

							if (!dataList.isEmpty()) {
								for (int i = 0; i < dataList.size(); i++) {
									M_BOP_Summary_Entity record = dataList.get(i);
									System.out.println("rownumber=" + startRow + i);
									Row row = sheet.getRow(startRow + i);
									if (row == null) {
										row = sheet.createRow(startRow + i);
									}

			//row13
							// Column B
							Cell cell2 = row.createCell(1);
							if (record.getR13_open_position() != null) {
								cell2.setCellValue(record.getR13_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							//row13
							// Column C
							Cell cell3 = row.createCell(2);
							if (record.getR13_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR13_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row13
							// Column D
							Cell cell4 = row.createCell(3);
							if (record.getR13_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR13_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row13
							// Column B
							Cell cell6 = row.createCell(5);
							if (record.getR13_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR13_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(6);
							if (record.getR13_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR13_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							
							//ROW 14
							row = sheet.getRow(13);

							cell2 = row.createCell(1);
							if (record.getR14_open_position() != null) {
								cell2.setCellValue(record.getR14_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row14
							// Column C
							cell3 = row.createCell(2);
							if (record.getR14_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR14_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row14
							// Column D
							cell4 = row.createCell(3);
							if (record.getR14_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR14_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row14
							// Column f
							cell6 = row.createCell(5);
							if (record.getR14_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR14_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row14
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR14_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR14_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							
							

							//ROW 16
							row = sheet.getRow(15);

							
							cell2 = row.createCell(1);
							if (record.getR16_open_position() != null) {
								cell2.setCellValue(record.getR16_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row16
							// Column C
							cell3 = row.createCell(2);
							if (record.getR16_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR16_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row16
							// Column D
							cell4 = row.createCell(3);
							if (record.getR16_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR16_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row16
							// Column f
							cell6 = row.createCell(5);
							if (record.getR16_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR16_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row16
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR16_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR16_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							

							//ROW 17
			row = sheet.getRow(16);

			cell2 = row.createCell(1);
			if (record.getR17_open_position() != null) {
			    cell2.setCellValue(record.getR17_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			// Column C
			cell3 = row.createCell(2);
			if (record.getR17_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR17_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			// Column D
			cell4 = row.createCell(3);
			if (record.getR17_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR17_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			// Column F
			cell6 = row.createCell(5);
			if (record.getR17_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR17_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			// Column G
			cell7 = row.createCell(6);
			if (record.getR17_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR17_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 18
			row = sheet.getRow(17);

			cell2 = row.createCell(1);
			if (record.getR18_open_position() != null) {
			    cell2.setCellValue(record.getR18_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR18_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR18_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR18_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR18_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR18_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR18_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR18_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR18_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 19
			row = sheet.getRow(18);

			cell2 = row.createCell(1);
			if (record.getR19_open_position() != null) {
			    cell2.setCellValue(record.getR19_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR19_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR19_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR19_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR19_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR19_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR19_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR19_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR19_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 20
			row = sheet.getRow(19);

			cell2 = row.createCell(1);
			if (record.getR20_open_position() != null) {
			    cell2.setCellValue(record.getR20_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR20_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR20_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR20_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR20_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR20_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR20_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR20_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR20_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 21
			row = sheet.getRow(20);

			cell2 = row.createCell(1);
			if (record.getR21_open_position() != null) {
			    cell2.setCellValue(record.getR21_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR21_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR21_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR21_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR21_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR21_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR21_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR21_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR21_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 22
			row = sheet.getRow(21);

			cell2 = row.createCell(1);
			if (record.getR22_open_position() != null) {
			    cell2.setCellValue(record.getR22_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR22_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR22_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR22_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR22_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR22_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR22_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR22_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR22_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 23
			row = sheet.getRow(22);

			cell2 = row.createCell(1);
			if (record.getR23_open_position() != null) {
			    cell2.setCellValue(record.getR23_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR23_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR23_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR23_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR23_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR23_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR23_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR23_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR23_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			

			//ROW 25
			row = sheet.getRow(24);

			cell2 = row.createCell(1);
			if (record.getR25_open_position() != null) {
			    cell2.setCellValue(record.getR25_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR25_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR25_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR25_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR25_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			Cell cell5 = row.createCell(4);
			if (record.getR25_net() != null) {
			    cell5.setCellValue(record.getR25_net().doubleValue());
			    cell5.setCellStyle(numberStyle);
			} else {
			    cell5.setCellValue("");
			    cell5.setCellStyle(textStyle);
			}

			cell6 = row.createCell(5);
			if (record.getR25_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR25_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR25_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR25_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			Cell cell8 = row.createCell(7);
			if (record.getR25_cp() != null) {
			    cell8.setCellValue(record.getR25_cp().doubleValue());
			    cell8.setCellStyle(numberStyle);
			} else {
			    cell8.setCellValue("");
			    cell8.setCellStyle(textStyle);
			}

			//ROW 26
			row = sheet.getRow(25);

			cell2 = row.createCell(1);
			if (record.getR26_open_position() != null) {
			    cell2.setCellValue(record.getR26_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR26_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR26_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR26_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR26_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR26_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR26_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR26_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR26_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			

			//ROW 28
			row = sheet.getRow(27);

			cell2 = row.createCell(1);
			if (record.getR28_open_position() != null) {
			    cell2.setCellValue(record.getR28_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR28_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR28_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR28_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR28_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR28_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR28_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR28_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR28_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 29
			row = sheet.getRow(28);

			cell2 = row.createCell(1);
			if (record.getR29_open_position() != null) {
			    cell2.setCellValue(record.getR29_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR29_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR29_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR29_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR29_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			
			cell6 = row.createCell(5);
			if (record.getR29_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR29_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR29_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR29_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 30
			row = sheet.getRow(29);

			cell2 = row.createCell(1);
			if (record.getR30_open_position() != null) {
			    cell2.setCellValue(record.getR30_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR30_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR30_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR30_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR30_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			
			cell6 = row.createCell(5);
			if (record.getR30_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR30_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR30_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR30_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 31
			row = sheet.getRow(30);

			cell2 = row.createCell(1);
			if (record.getR31_open_position() != null) {
			    cell2.setCellValue(record.getR31_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR31_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR31_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR31_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR31_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR31_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR31_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR31_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR31_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 32
			row = sheet.getRow(31);

			cell2 = row.createCell(1);
			if (record.getR32_open_position() != null) {
			    cell2.setCellValue(record.getR32_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR32_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR32_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR32_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR32_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR32_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR32_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR32_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR32_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 33
			row = sheet.getRow(32);

			cell2 = row.createCell(1);
			if (record.getR33_open_position() != null) {
			    cell2.setCellValue(record.getR33_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR33_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR33_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR33_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR33_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR33_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR33_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR33_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR33_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 34
			row = sheet.getRow(33);

			cell2 = row.createCell(1);
			if (record.getR34_open_position() != null) {
			    cell2.setCellValue(record.getR34_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR34_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR34_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR34_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR34_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR34_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR34_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR34_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR34_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 35
			row = sheet.getRow(34);

			cell2 = row.createCell(1);
			if (record.getR35_open_position() != null) {
			    cell2.setCellValue(record.getR35_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR35_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR35_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR35_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR35_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR35_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR35_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR35_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR35_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
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
			public byte[] BRRS_M_BOPEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Email Excel generation process in memory.");
				
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_M_BOPARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_BOPEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 
				else {
				List<M_BOP_Summary_Entity> dataList = M_BOP_Summary_Repo.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_BOP report. Returning empty result.");
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

					int startRow = 12;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_BOP_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

			//row13
							// Column B
							Cell cell2 = row.createCell(1);
							if (record.getR13_open_position() != null) {
								cell2.setCellValue(record.getR13_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							//row13
							// Column C
							Cell cell3 = row.createCell(2);
							if (record.getR13_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR13_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row13
							// Column D
							Cell cell4 = row.createCell(3);
							if (record.getR13_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR13_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row13
							// Column F
							Cell cell6 = row.createCell(5);
							if (record.getR13_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR13_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
		                   // Column G
							Cell cell7 = row.createCell(6);
							if (record.getR13_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR13_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							
							//ROW 14
							row = sheet.getRow(13);
		                     // Column B
							cell2 = row.createCell(1);
							if (record.getR14_open_position() != null) {
								cell2.setCellValue(record.getR14_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row14
							// Column C
							cell3 = row.createCell(2);
							if (record.getR14_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR14_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row14
							// Column D
							cell4 = row.createCell(3);
							if (record.getR14_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR14_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row14
							// Column f
							cell6 = row.createCell(5);
							if (record.getR14_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR14_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row14
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR14_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR14_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							
							

							

		// ROW 15
		row = sheet.getRow(14);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR15_open_position() != null) {
		    cell2.setCellValue(record.getR15_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR15_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR15_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR15_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR15_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR15_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR15_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR15_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR15_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 16
		row = sheet.getRow(15);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR16_open_position() != null) {
		    cell2.setCellValue(record.getR16_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR16_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR16_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR16_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR16_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR16_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR16_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR16_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR16_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 17
		row = sheet.getRow(16);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR17_open_position() != null) {
		    cell2.setCellValue(record.getR17_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR17_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR17_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR17_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR17_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR17_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR17_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR17_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR17_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 18
		row = sheet.getRow(17);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR18_open_position() != null) {
		    cell2.setCellValue(record.getR18_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR18_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR18_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR18_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR18_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR18_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR18_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR18_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR18_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 19
		row = sheet.getRow(18);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR19_open_position() != null) {
		    cell2.setCellValue(record.getR19_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR19_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR19_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR19_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR19_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR19_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR19_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR19_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR19_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 20
		row = sheet.getRow(19);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR20_open_position() != null) {
		    cell2.setCellValue(record.getR20_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR20_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR20_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR20_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR20_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR20_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR20_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR20_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR20_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 21
		row = sheet.getRow(20);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR21_open_position() != null) {
		    cell2.setCellValue(record.getR21_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR21_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR21_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR21_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR21_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR21_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR21_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR21_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR21_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 22
		row = sheet.getRow(21);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR22_open_position() != null) {
		    cell2.setCellValue(record.getR22_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR22_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR22_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR22_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR22_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR22_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR22_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR22_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR22_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 23
		row = sheet.getRow(22);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR23_open_position() != null) {
		    cell2.setCellValue(record.getR23_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR23_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR23_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR23_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR23_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR23_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR23_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR23_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR23_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 24
		row = sheet.getRow(23);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR24_open_position() != null) {
		    cell2.setCellValue(record.getR24_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR24_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR24_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR24_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR24_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR24_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR24_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR24_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR24_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 25
		row = sheet.getRow(24);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR25_open_position() != null) {
		    cell2.setCellValue(record.getR25_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR25_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR25_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR25_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR25_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR25_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR25_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR25_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR25_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 26
		row = sheet.getRow(25);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR26_open_position() != null) {
		    cell2.setCellValue(record.getR26_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR26_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR26_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR26_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR26_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR26_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR26_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR26_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR26_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 27
		row = sheet.getRow(26);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR27_open_position() != null) {
		    cell2.setCellValue(record.getR27_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR27_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR27_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR27_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR27_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR27_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR27_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR27_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR27_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 28
		row = sheet.getRow(27);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR28_open_position() != null) {
		    cell2.setCellValue(record.getR28_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR28_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR28_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR28_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR28_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR28_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR28_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR28_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR28_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 29
		row = sheet.getRow(28);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR29_open_position() != null) {
		    cell2.setCellValue(record.getR29_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR29_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR29_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR29_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR29_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR29_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR29_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR29_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR29_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 30
		row = sheet.getRow(29);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR30_open_position() != null) {
		    cell2.setCellValue(record.getR30_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR30_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR30_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR30_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR30_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR30_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR30_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR30_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR30_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 31
		row = sheet.getRow(30);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR31_open_position() != null) {
		    cell2.setCellValue(record.getR31_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR31_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR31_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR31_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR31_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR31_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR31_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR31_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR31_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 32
		row = sheet.getRow(31);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR32_open_position() != null) {
		    cell2.setCellValue(record.getR32_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR32_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR32_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR32_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR32_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR32_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR32_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR32_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR32_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 33
		row = sheet.getRow(32);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR33_open_position() != null) {
		    cell2.setCellValue(record.getR33_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR33_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR33_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR33_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR33_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR33_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR33_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR33_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR33_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 34
		row = sheet.getRow(33);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR34_open_position() != null) {
		    cell2.setCellValue(record.getR34_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR34_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR34_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR34_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR34_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR34_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR34_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR34_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR34_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 35
		row = sheet.getRow(34);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR35_open_position() != null) {
		    cell2.setCellValue(record.getR35_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR35_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR35_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR35_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR35_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR35_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR35_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR35_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR35_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 36
		row = sheet.getRow(35);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR36_open_position() != null) {
		    cell2.setCellValue(record.getR36_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR36_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR36_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR36_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR36_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR36_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR36_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR36_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR36_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
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
			public byte[] getExcelM_BOPARCHIVAL(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory in Archival.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					try {
						// Redirecting to Archival
						return BRRS_M_BOPARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
					}
				} 

				List<M_BOP_Archival_Summary_Entity> dataList = M_BOP_Archival_Summary_Repo
						.getdatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_BOP report. Returning empty result.");
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

					int startRow = 12;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_BOP_Archival_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

		//row13
							// Column B
							Cell cell2 = row.createCell(1);
							if (record.getR13_open_position() != null) {
								cell2.setCellValue(record.getR13_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							//row13
							// Column C
							Cell cell3 = row.createCell(2);
							if (record.getR13_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR13_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row13
							// Column D
							Cell cell4 = row.createCell(3);
							if (record.getR13_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR13_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row13
							// Column B
							Cell cell6 = row.createCell(5);
							if (record.getR13_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR13_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(6);
							if (record.getR13_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR13_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							
							//ROW 14
							row = sheet.getRow(13);

							cell2 = row.createCell(1);
							if (record.getR14_open_position() != null) {
								cell2.setCellValue(record.getR14_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row14
							// Column C
							cell3 = row.createCell(2);
							if (record.getR14_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR14_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row14
							// Column D
							cell4 = row.createCell(3);
							if (record.getR14_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR14_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row14
							// Column f
							cell6 = row.createCell(5);
							if (record.getR14_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR14_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row14
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR14_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR14_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							
							

							//ROW 16
							row = sheet.getRow(15);

							
							cell2 = row.createCell(1);
							if (record.getR16_open_position() != null) {
								cell2.setCellValue(record.getR16_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row16
							// Column C
							cell3 = row.createCell(2);
							if (record.getR16_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR16_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row16
							// Column D
							cell4 = row.createCell(3);
							if (record.getR16_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR16_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row16
							// Column f
							cell6 = row.createCell(5);
							if (record.getR16_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR16_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row16
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR16_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR16_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							

							//ROW 17
			row = sheet.getRow(16);

			cell2 = row.createCell(1);
			if (record.getR17_open_position() != null) {
			    cell2.setCellValue(record.getR17_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			// Column C
			cell3 = row.createCell(2);
			if (record.getR17_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR17_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			// Column D
			cell4 = row.createCell(3);
			if (record.getR17_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR17_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			// Column F
			cell6 = row.createCell(5);
			if (record.getR17_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR17_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			// Column G
			cell7 = row.createCell(6);
			if (record.getR17_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR17_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 18
			row = sheet.getRow(17);

			cell2 = row.createCell(1);
			if (record.getR18_open_position() != null) {
			    cell2.setCellValue(record.getR18_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR18_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR18_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR18_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR18_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR18_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR18_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR18_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR18_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 19
			row = sheet.getRow(18);

			cell2 = row.createCell(1);
			if (record.getR19_open_position() != null) {
			    cell2.setCellValue(record.getR19_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR19_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR19_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR19_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR19_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR19_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR19_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR19_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR19_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 20
			row = sheet.getRow(19);

			cell2 = row.createCell(1);
			if (record.getR20_open_position() != null) {
			    cell2.setCellValue(record.getR20_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR20_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR20_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR20_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR20_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR20_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR20_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR20_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR20_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 21
			row = sheet.getRow(20);

			cell2 = row.createCell(1);
			if (record.getR21_open_position() != null) {
			    cell2.setCellValue(record.getR21_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR21_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR21_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR21_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR21_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR21_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR21_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR21_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR21_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 22
			row = sheet.getRow(21);

			cell2 = row.createCell(1);
			if (record.getR22_open_position() != null) {
			    cell2.setCellValue(record.getR22_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR22_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR22_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR22_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR22_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR22_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR22_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR22_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR22_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 23
			row = sheet.getRow(22);

			cell2 = row.createCell(1);
			if (record.getR23_open_position() != null) {
			    cell2.setCellValue(record.getR23_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR23_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR23_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR23_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR23_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR23_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR23_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR23_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR23_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			

			//ROW 25
			row = sheet.getRow(24);

			cell2 = row.createCell(1);
			if (record.getR25_open_position() != null) {
			    cell2.setCellValue(record.getR25_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR25_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR25_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR25_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR25_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			Cell cell5 = row.createCell(4);
			if (record.getR25_net() != null) {
			    cell5.setCellValue(record.getR25_net().doubleValue());
			    cell5.setCellStyle(numberStyle);
			} else {
			    cell5.setCellValue("");
			    cell5.setCellStyle(textStyle);
			}

			cell6 = row.createCell(5);
			if (record.getR25_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR25_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR25_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR25_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			Cell cell8 = row.createCell(7);
			if (record.getR25_cp() != null) {
			    cell8.setCellValue(record.getR25_cp().doubleValue());
			    cell8.setCellStyle(numberStyle);
			} else {
			    cell8.setCellValue("");
			    cell8.setCellStyle(textStyle);
			}

			//ROW 26
			row = sheet.getRow(25);

			cell2 = row.createCell(1);
			if (record.getR26_open_position() != null) {
			    cell2.setCellValue(record.getR26_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR26_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR26_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR26_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR26_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR26_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR26_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR26_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR26_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			

			//ROW 28
			row = sheet.getRow(27);

			cell2 = row.createCell(1);
			if (record.getR28_open_position() != null) {
			    cell2.setCellValue(record.getR28_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR28_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR28_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR28_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR28_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR28_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR28_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR28_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR28_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 29
			row = sheet.getRow(28);

			cell2 = row.createCell(1);
			if (record.getR29_open_position() != null) {
			    cell2.setCellValue(record.getR29_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR29_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR29_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR29_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR29_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			
			cell6 = row.createCell(5);
			if (record.getR29_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR29_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR29_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR29_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 30
			row = sheet.getRow(29);

			cell2 = row.createCell(1);
			if (record.getR30_open_position() != null) {
			    cell2.setCellValue(record.getR30_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR30_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR30_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR30_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR30_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			
			cell6 = row.createCell(5);
			if (record.getR30_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR30_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR30_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR30_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 31
			row = sheet.getRow(30);

			cell2 = row.createCell(1);
			if (record.getR31_open_position() != null) {
			    cell2.setCellValue(record.getR31_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR31_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR31_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR31_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR31_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR31_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR31_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR31_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR31_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 32
			row = sheet.getRow(31);

			cell2 = row.createCell(1);
			if (record.getR32_open_position() != null) {
			    cell2.setCellValue(record.getR32_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR32_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR32_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR32_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR32_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR32_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR32_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR32_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR32_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 33
			row = sheet.getRow(32);

			cell2 = row.createCell(1);
			if (record.getR33_open_position() != null) {
			    cell2.setCellValue(record.getR33_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR33_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR33_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR33_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR33_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR33_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR33_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR33_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR33_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 34
			row = sheet.getRow(33);

			cell2 = row.createCell(1);
			if (record.getR34_open_position() != null) {
			    cell2.setCellValue(record.getR34_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR34_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR34_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR34_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR34_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR34_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR34_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR34_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR34_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 35
			row = sheet.getRow(34);

			cell2 = row.createCell(1);
			if (record.getR35_open_position() != null) {
			    cell2.setCellValue(record.getR35_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR35_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR35_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR35_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR35_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR35_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR35_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR35_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR35_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
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
			public byte[] BRRS_M_BOPARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Archival Email Excel generation process in memory.");

				List<M_BOP_Archival_Summary_Entity> dataList = M_BOP_Archival_Summary_Repo
						.getdatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_BOP report. Returning empty result.");
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

					int startRow = 12;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_BOP_Archival_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

			//row13
							// Column B
							Cell cell2 = row.createCell(1);
							if (record.getR13_open_position() != null) {
								cell2.setCellValue(record.getR13_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							//row13
							// Column C
							Cell cell3 = row.createCell(2);
							if (record.getR13_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR13_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row13
							// Column D
							Cell cell4 = row.createCell(3);
							if (record.getR13_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR13_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row13
							// Column F
							Cell cell6 = row.createCell(5);
							if (record.getR13_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR13_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
		                   // Column G
							Cell cell7 = row.createCell(6);
							if (record.getR13_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR13_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							
							//ROW 14
							row = sheet.getRow(13);
		                     // Column B
							cell2 = row.createCell(1);
							if (record.getR14_open_position() != null) {
								cell2.setCellValue(record.getR14_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row14
							// Column C
							cell3 = row.createCell(2);
							if (record.getR14_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR14_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row14
							// Column D
							cell4 = row.createCell(3);
							if (record.getR14_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR14_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row14
							// Column f
							cell6 = row.createCell(5);
							if (record.getR14_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR14_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row14
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR14_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR14_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							
							

							

		// ROW 15
		row = sheet.getRow(14);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR15_open_position() != null) {
		    cell2.setCellValue(record.getR15_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR15_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR15_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR15_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR15_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR15_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR15_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR15_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR15_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 16
		row = sheet.getRow(15);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR16_open_position() != null) {
		    cell2.setCellValue(record.getR16_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR16_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR16_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR16_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR16_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR16_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR16_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR16_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR16_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 17
		row = sheet.getRow(16);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR17_open_position() != null) {
		    cell2.setCellValue(record.getR17_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR17_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR17_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR17_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR17_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR17_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR17_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR17_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR17_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 18
		row = sheet.getRow(17);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR18_open_position() != null) {
		    cell2.setCellValue(record.getR18_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR18_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR18_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR18_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR18_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR18_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR18_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR18_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR18_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 19
		row = sheet.getRow(18);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR19_open_position() != null) {
		    cell2.setCellValue(record.getR19_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR19_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR19_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR19_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR19_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR19_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR19_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR19_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR19_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 20
		row = sheet.getRow(19);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR20_open_position() != null) {
		    cell2.setCellValue(record.getR20_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR20_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR20_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR20_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR20_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR20_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR20_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR20_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR20_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 21
		row = sheet.getRow(20);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR21_open_position() != null) {
		    cell2.setCellValue(record.getR21_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR21_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR21_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR21_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR21_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR21_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR21_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR21_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR21_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 22
		row = sheet.getRow(21);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR22_open_position() != null) {
		    cell2.setCellValue(record.getR22_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR22_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR22_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR22_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR22_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR22_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR22_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR22_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR22_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 23
		row = sheet.getRow(22);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR23_open_position() != null) {
		    cell2.setCellValue(record.getR23_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR23_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR23_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR23_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR23_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR23_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR23_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR23_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR23_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 24
		row = sheet.getRow(23);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR24_open_position() != null) {
		    cell2.setCellValue(record.getR24_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR24_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR24_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR24_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR24_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR24_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR24_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR24_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR24_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 25
		row = sheet.getRow(24);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR25_open_position() != null) {
		    cell2.setCellValue(record.getR25_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR25_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR25_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR25_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR25_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR25_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR25_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR25_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR25_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 26
		row = sheet.getRow(25);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR26_open_position() != null) {
		    cell2.setCellValue(record.getR26_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR26_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR26_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR26_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR26_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR26_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR26_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR26_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR26_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 27
		row = sheet.getRow(26);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR27_open_position() != null) {
		    cell2.setCellValue(record.getR27_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR27_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR27_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR27_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR27_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR27_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR27_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR27_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR27_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 28
		row = sheet.getRow(27);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR28_open_position() != null) {
		    cell2.setCellValue(record.getR28_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR28_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR28_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR28_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR28_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR28_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR28_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR28_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR28_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 29
		row = sheet.getRow(28);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR29_open_position() != null) {
		    cell2.setCellValue(record.getR29_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR29_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR29_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR29_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR29_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR29_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR29_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR29_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR29_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 30
		row = sheet.getRow(29);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR30_open_position() != null) {
		    cell2.setCellValue(record.getR30_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR30_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR30_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR30_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR30_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR30_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR30_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR30_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR30_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 31
		row = sheet.getRow(30);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR31_open_position() != null) {
		    cell2.setCellValue(record.getR31_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR31_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR31_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR31_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR31_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR31_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR31_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR31_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR31_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 32
		row = sheet.getRow(31);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR32_open_position() != null) {
		    cell2.setCellValue(record.getR32_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR32_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR32_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR32_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR32_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR32_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR32_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR32_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR32_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 33
		row = sheet.getRow(32);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR33_open_position() != null) {
		    cell2.setCellValue(record.getR33_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR33_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR33_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR33_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR33_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR33_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR33_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR33_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR33_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 34
		row = sheet.getRow(33);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR34_open_position() != null) {
		    cell2.setCellValue(record.getR34_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR34_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR34_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR34_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR34_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR34_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR34_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR34_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR34_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 35
		row = sheet.getRow(34);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR35_open_position() != null) {
		    cell2.setCellValue(record.getR35_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR35_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR35_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR35_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR35_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR35_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR35_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR35_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR35_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 36
		row = sheet.getRow(35);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR36_open_position() != null) {
		    cell2.setCellValue(record.getR36_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR36_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR36_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR36_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR36_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR36_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR36_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR36_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR36_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
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
			public byte[] BRRS_M_BOPResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

				if ("email".equalsIgnoreCase(format) && version != null) {
					logger.info("Service: Generating RESUB report for version {}", version);

					try {
						// ✅ Redirecting to Resub Excel
						return BRRS_M_BOPEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

					} catch (ParseException e) {
						logger.error("Invalid report date format: {}", fromdate, e);
						throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			}

				List<M_BOP_RESUB_Summary_Entity> dataList = M_BOP_resub_summary_repo
						.getdatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_BOP report. Returning empty result.");
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

					int startRow = 12;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {

							M_BOP_RESUB_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							//row13
							// Column B
							Cell cell2 = row.createCell(1);
							if (record.getR13_open_position() != null) {
								cell2.setCellValue(record.getR13_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							//row13
							// Column C
							Cell cell3 = row.createCell(2);
							if (record.getR13_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR13_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row13
							// Column D
							Cell cell4 = row.createCell(3);
							if (record.getR13_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR13_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row13
							// Column B
							Cell cell6 = row.createCell(5);
							if (record.getR13_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR13_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(6);
							if (record.getR13_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR13_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							
							//ROW 14
							row = sheet.getRow(13);

							cell2 = row.createCell(1);
							if (record.getR14_open_position() != null) {
								cell2.setCellValue(record.getR14_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row14
							// Column C
							cell3 = row.createCell(2);
							if (record.getR14_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR14_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row14
							// Column D
							cell4 = row.createCell(3);
							if (record.getR14_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR14_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row14
							// Column f
							cell6 = row.createCell(5);
							if (record.getR14_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR14_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row14
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR14_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR14_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							
							

							//ROW 16
							row = sheet.getRow(15);

							
							cell2 = row.createCell(1);
							if (record.getR16_open_position() != null) {
								cell2.setCellValue(record.getR16_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row16
							// Column C
							cell3 = row.createCell(2);
							if (record.getR16_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR16_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row16
							// Column D
							cell4 = row.createCell(3);
							if (record.getR16_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR16_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row16
							// Column f
							cell6 = row.createCell(5);
							if (record.getR16_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR16_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row16
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR16_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR16_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							

							//ROW 17
			row = sheet.getRow(16);

			cell2 = row.createCell(1);
			if (record.getR17_open_position() != null) {
			    cell2.setCellValue(record.getR17_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			// Column C
			cell3 = row.createCell(2);
			if (record.getR17_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR17_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			// Column D
			cell4 = row.createCell(3);
			if (record.getR17_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR17_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			// Column F
			cell6 = row.createCell(5);
			if (record.getR17_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR17_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			// Column G
			cell7 = row.createCell(6);
			if (record.getR17_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR17_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 18
			row = sheet.getRow(17);

			cell2 = row.createCell(1);
			if (record.getR18_open_position() != null) {
			    cell2.setCellValue(record.getR18_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR18_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR18_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR18_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR18_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR18_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR18_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR18_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR18_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 19
			row = sheet.getRow(18);

			cell2 = row.createCell(1);
			if (record.getR19_open_position() != null) {
			    cell2.setCellValue(record.getR19_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR19_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR19_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR19_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR19_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR19_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR19_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR19_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR19_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 20
			row = sheet.getRow(19);

			cell2 = row.createCell(1);
			if (record.getR20_open_position() != null) {
			    cell2.setCellValue(record.getR20_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR20_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR20_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR20_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR20_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR20_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR20_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR20_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR20_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 21
			row = sheet.getRow(20);

			cell2 = row.createCell(1);
			if (record.getR21_open_position() != null) {
			    cell2.setCellValue(record.getR21_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR21_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR21_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR21_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR21_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR21_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR21_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR21_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR21_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 22
			row = sheet.getRow(21);

			cell2 = row.createCell(1);
			if (record.getR22_open_position() != null) {
			    cell2.setCellValue(record.getR22_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR22_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR22_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR22_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR22_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR22_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR22_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR22_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR22_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 23
			row = sheet.getRow(22);

			cell2 = row.createCell(1);
			if (record.getR23_open_position() != null) {
			    cell2.setCellValue(record.getR23_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR23_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR23_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR23_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR23_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR23_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR23_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR23_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR23_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			

			//ROW 25
			row = sheet.getRow(24);

			cell2 = row.createCell(1);
			if (record.getR25_open_position() != null) {
			    cell2.setCellValue(record.getR25_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR25_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR25_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR25_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR25_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			Cell cell5 = row.createCell(4);
			if (record.getR25_net() != null) {
			    cell5.setCellValue(record.getR25_net().doubleValue());
			    cell5.setCellStyle(numberStyle);
			} else {
			    cell5.setCellValue("");
			    cell5.setCellStyle(textStyle);
			}

			cell6 = row.createCell(5);
			if (record.getR25_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR25_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR25_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR25_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			Cell cell8 = row.createCell(7);
			if (record.getR25_cp() != null) {
			    cell8.setCellValue(record.getR25_cp().doubleValue());
			    cell8.setCellStyle(numberStyle);
			} else {
			    cell8.setCellValue("");
			    cell8.setCellStyle(textStyle);
			}

			//ROW 26
			row = sheet.getRow(25);

			cell2 = row.createCell(1);
			if (record.getR26_open_position() != null) {
			    cell2.setCellValue(record.getR26_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR26_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR26_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR26_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR26_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR26_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR26_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR26_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR26_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			

			//ROW 28
			row = sheet.getRow(27);

			cell2 = row.createCell(1);
			if (record.getR28_open_position() != null) {
			    cell2.setCellValue(record.getR28_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR28_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR28_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR28_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR28_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR28_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR28_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR28_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR28_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 29
			row = sheet.getRow(28);

			cell2 = row.createCell(1);
			if (record.getR29_open_position() != null) {
			    cell2.setCellValue(record.getR29_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR29_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR29_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR29_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR29_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			
			cell6 = row.createCell(5);
			if (record.getR29_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR29_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR29_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR29_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 30
			row = sheet.getRow(29);

			cell2 = row.createCell(1);
			if (record.getR30_open_position() != null) {
			    cell2.setCellValue(record.getR30_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR30_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR30_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR30_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR30_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			
			cell6 = row.createCell(5);
			if (record.getR30_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR30_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR30_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR30_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 31
			row = sheet.getRow(30);

			cell2 = row.createCell(1);
			if (record.getR31_open_position() != null) {
			    cell2.setCellValue(record.getR31_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR31_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR31_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR31_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR31_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR31_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR31_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR31_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR31_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 32
			row = sheet.getRow(31);

			cell2 = row.createCell(1);
			if (record.getR32_open_position() != null) {
			    cell2.setCellValue(record.getR32_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR32_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR32_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR32_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR32_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR32_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR32_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR32_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR32_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			
			//ROW 33
			row = sheet.getRow(32);

			cell2 = row.createCell(1);
			if (record.getR33_open_position() != null) {
			    cell2.setCellValue(record.getR33_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR33_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR33_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR33_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR33_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR33_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR33_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR33_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR33_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 34
			row = sheet.getRow(33);

			cell2 = row.createCell(1);
			if (record.getR34_open_position() != null) {
			    cell2.setCellValue(record.getR34_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR34_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR34_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR34_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR34_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR34_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR34_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR34_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR34_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
			}

			

			//ROW 35
			row = sheet.getRow(34);

			cell2 = row.createCell(1);
			if (record.getR35_open_position() != null) {
			    cell2.setCellValue(record.getR35_open_position().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(2);
			if (record.getR35_cpdm_dt_inc() != null) {
			    cell3.setCellValue(record.getR35_cpdm_dt_inc().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(3);
			if (record.getR35_cpdm_dt_dec() != null) {
			    cell4.setCellValue(record.getR35_cpdm_dt_dec().doubleValue());
			    cell4.setCellStyle(numberStyle);
			} else {
			    cell4.setCellValue("");
			    cell4.setCellStyle(textStyle);
			}

			

			cell6 = row.createCell(5);
			if (record.getR35_cpdm_dt_der() != null) {
			    cell6.setCellValue(record.getR35_cpdm_dt_der().doubleValue());
			    cell6.setCellStyle(numberStyle);
			} else {
			    cell6.setCellValue("");
			    cell6.setCellStyle(textStyle);
			}

			cell7 = row.createCell(6);
			if (record.getR35_cpdm_dt_dto() != null) {
			    cell7.setCellValue(record.getR35_cpdm_dt_dto().doubleValue());
			    cell7.setCellStyle(numberStyle);
			} else {
			    cell7.setCellValue("");
			    cell7.setCellStyle(textStyle);
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
			public byte[] BRRS_M_BOPEmailResubExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting RESUB Email Excel generation process in memory.");

				List<M_BOP_RESUB_Summary_Entity> dataList = M_BOP_resub_summary_repo
						.getdatabydateListarchival(dateformat.parse(todate), version);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_BOP report. Returning empty result.");
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

					int startRow = 12;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_BOP_RESUB_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

			//row13
							// Column B
							Cell cell2 = row.createCell(1);
							if (record.getR13_open_position() != null) {
								cell2.setCellValue(record.getR13_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							//row13
							// Column C
							Cell cell3 = row.createCell(2);
							if (record.getR13_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR13_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row13
							// Column D
							Cell cell4 = row.createCell(3);
							if (record.getR13_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR13_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row13
							// Column F
							Cell cell6 = row.createCell(5);
							if (record.getR13_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR13_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
		                   // Column G
							Cell cell7 = row.createCell(6);
							if (record.getR13_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR13_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							
							//ROW 14
							row = sheet.getRow(13);
		                     // Column B
							cell2 = row.createCell(1);
							if (record.getR14_open_position() != null) {
								cell2.setCellValue(record.getR14_open_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							//row14
							// Column C
							cell3 = row.createCell(2);
							if (record.getR14_cpdm_dt_inc() != null) {
								cell3.setCellValue(record.getR14_cpdm_dt_inc().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							//row14
							// Column D
							cell4 = row.createCell(3);
							if (record.getR14_cpdm_dt_dec() != null) {
								cell4.setCellValue(record.getR14_cpdm_dt_dec().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							//row14
							// Column f
							cell6 = row.createCell(5);
							if (record.getR14_cpdm_dt_der() != null) {
								cell6.setCellValue(record.getR14_cpdm_dt_der().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							//row14
							//column g
							
							cell7 = row.createCell(6);
							if (record.getR14_cpdm_dt_dto() != null) {
								cell7.setCellValue(record.getR14_cpdm_dt_dto().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
			                

							
							

							

		// ROW 15
		row = sheet.getRow(14);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR15_open_position() != null) {
		    cell2.setCellValue(record.getR15_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR15_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR15_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR15_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR15_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR15_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR15_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR15_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR15_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 16
		row = sheet.getRow(15);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR16_open_position() != null) {
		    cell2.setCellValue(record.getR16_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR16_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR16_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR16_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR16_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR16_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR16_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR16_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR16_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 17
		row = sheet.getRow(16);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR17_open_position() != null) {
		    cell2.setCellValue(record.getR17_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR17_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR17_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR17_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR17_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR17_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR17_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR17_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR17_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 18
		row = sheet.getRow(17);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR18_open_position() != null) {
		    cell2.setCellValue(record.getR18_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR18_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR18_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR18_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR18_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR18_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR18_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR18_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR18_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 19
		row = sheet.getRow(18);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR19_open_position() != null) {
		    cell2.setCellValue(record.getR19_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR19_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR19_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR19_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR19_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR19_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR19_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR19_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR19_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 20
		row = sheet.getRow(19);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR20_open_position() != null) {
		    cell2.setCellValue(record.getR20_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR20_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR20_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR20_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR20_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR20_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR20_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR20_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR20_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 21
		row = sheet.getRow(20);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR21_open_position() != null) {
		    cell2.setCellValue(record.getR21_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR21_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR21_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR21_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR21_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR21_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR21_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR21_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR21_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 22
		row = sheet.getRow(21);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR22_open_position() != null) {
		    cell2.setCellValue(record.getR22_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR22_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR22_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR22_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR22_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR22_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR22_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR22_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR22_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 23
		row = sheet.getRow(22);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR23_open_position() != null) {
		    cell2.setCellValue(record.getR23_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR23_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR23_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR23_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR23_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR23_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR23_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR23_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR23_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 24
		row = sheet.getRow(23);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR24_open_position() != null) {
		    cell2.setCellValue(record.getR24_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR24_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR24_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR24_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR24_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR24_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR24_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR24_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR24_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 25
		row = sheet.getRow(24);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR25_open_position() != null) {
		    cell2.setCellValue(record.getR25_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR25_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR25_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR25_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR25_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR25_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR25_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR25_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR25_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 26
		row = sheet.getRow(25);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR26_open_position() != null) {
		    cell2.setCellValue(record.getR26_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR26_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR26_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR26_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR26_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR26_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR26_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR26_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR26_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 27
		row = sheet.getRow(26);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR27_open_position() != null) {
		    cell2.setCellValue(record.getR27_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR27_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR27_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR27_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR27_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR27_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR27_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR27_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR27_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 28
		row = sheet.getRow(27);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR28_open_position() != null) {
		    cell2.setCellValue(record.getR28_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR28_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR28_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR28_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR28_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR28_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR28_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR28_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR28_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 29
		row = sheet.getRow(28);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR29_open_position() != null) {
		    cell2.setCellValue(record.getR29_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR29_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR29_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR29_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR29_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR29_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR29_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR29_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR29_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 30
		row = sheet.getRow(29);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR30_open_position() != null) {
		    cell2.setCellValue(record.getR30_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR30_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR30_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR30_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR30_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR30_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR30_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR30_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR30_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 31
		row = sheet.getRow(30);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR31_open_position() != null) {
		    cell2.setCellValue(record.getR31_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR31_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR31_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR31_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR31_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR31_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR31_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR31_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR31_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 32
		row = sheet.getRow(31);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR32_open_position() != null) {
		    cell2.setCellValue(record.getR32_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR32_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR32_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR32_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR32_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR32_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR32_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR32_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR32_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 33
		row = sheet.getRow(32);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR33_open_position() != null) {
		    cell2.setCellValue(record.getR33_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR33_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR33_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR33_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR33_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR33_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR33_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR33_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR33_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 34
		row = sheet.getRow(33);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR34_open_position() != null) {
		    cell2.setCellValue(record.getR34_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR34_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR34_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR34_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR34_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR34_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR34_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR34_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR34_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 35
		row = sheet.getRow(34);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR35_open_position() != null) {
		    cell2.setCellValue(record.getR35_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR35_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR35_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR35_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR35_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR35_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR35_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR35_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR35_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
		}

		// ROW 36
		row = sheet.getRow(35);
		// Column B
		cell2 = row.createCell(1);
		if (record.getR36_open_position() != null) {
		    cell2.setCellValue(record.getR36_open_position().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		// Column C
		cell3 = row.createCell(2);
		if (record.getR36_cpdm_dt_inc() != null) {
		    cell3.setCellValue(record.getR36_cpdm_dt_inc().doubleValue());
		    cell3.setCellStyle(numberStyle);
		} else {
		    cell3.setCellValue("");
		    cell3.setCellStyle(textStyle);
		}
		// Column D
		cell4 = row.createCell(3);
		if (record.getR36_cpdm_dt_dec() != null) {
		    cell4.setCellValue(record.getR36_cpdm_dt_dec().doubleValue());
		    cell4.setCellStyle(numberStyle);
		} else {
		    cell4.setCellValue("");
		    cell4.setCellStyle(textStyle);
		}
		// Column F
		cell6 = row.createCell(5);
		if (record.getR36_cpdm_dt_der() != null) {
		    cell6.setCellValue(record.getR36_cpdm_dt_der().doubleValue());
		    cell6.setCellStyle(numberStyle);
		} else {
		    cell6.setCellValue("");
		    cell6.setCellStyle(textStyle);
		}
		// Column G
		cell7 = row.createCell(6);
		if (record.getR36_cpdm_dt_dto() != null) {
		    cell7.setCellValue(record.getR36_cpdm_dt_dto().doubleValue());
		    cell7.setCellStyle(numberStyle);
		} else {
		    cell7.setCellValue("");
		    cell7.setCellStyle(textStyle);
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
