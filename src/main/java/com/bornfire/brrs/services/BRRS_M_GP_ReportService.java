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

import com.bornfire.brrs.entities.BRRS_M_GP_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_GP_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_GP_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_GP_RESUB_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_GP_RESUB_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_GP_Summary_Repo;
import com.bornfire.brrs.entities.M_GP_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_GP_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_GP_Detail_Entity;
import com.bornfire.brrs.entities.M_GP_RESUB_Detail_Entity;
import com.bornfire.brrs.entities.M_GP_RESUB_Summary_Entity;
import com.bornfire.brrs.entities.M_GP_Summary_Entity;

@Component
@Service

public class BRRS_M_GP_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_GP_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_GP_Summary_Repo BRRS_M_GP_Summary_Repo;

	@Autowired
	BRRS_M_GP_Archival_Summary_Repo M_GP_Archival_Summary_Repo;
	
	@Autowired
	BRRS_M_GP_Detail_Repo M_GP_Detail_Repo;
	
	@Autowired
	BRRS_M_GP_Archival_Detail_Repo M_GP_Archival_Detail_Repo;
	
	@Autowired
	BRRS_M_GP_RESUB_Summary_Repo M_GP_resub_summary_repo;
	
    @Autowired
	BRRS_M_GP_RESUB_Detail_Repo M_GP_resub_detail_repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_GPView(
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

	            List<M_GP_Archival_Summary_Entity> T1Master =
	                    M_GP_Archival_Summary_Repo
	                            .getdatabydateListarchival(d1, version);

	            System.out.println("Archival Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- RESUB SUMMARY ----------
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	            List<M_GP_RESUB_Summary_Entity> T1Master =
	                    M_GP_resub_summary_repo
	                            .getdatabydateListarchival(d1, version);

	            System.out.println("Resub Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "resub");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- NORMAL SUMMARY ----------
	        else {

	            List<M_GP_Summary_Entity> T1Master =
	            		BRRS_M_GP_Summary_Repo
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

	                List<M_GP_Archival_Detail_Entity> T1Master =
	                        M_GP_Archival_Detail_Repo
	                                .getdatabydateListarchival(d1, version);

	                System.out.println("Archival Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- RESUB DETAIL ---------------------------------------------------------------------------------------------------------------------------------
	            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	                List<M_GP_RESUB_Detail_Entity> T1Master =
	                        M_GP_resub_detail_repo
	                                .getdatabydateListarchival(d1, version);

	                System.out.println("Resub Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "resub");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- NORMAL DETAIL ----------
	            else {

	            	 List<M_GP_Detail_Entity> T1Master =
		                        M_GP_Detail_Repo
		                                .getdatabydateList(d1);

	                System.out.println("Normal Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }
	        }

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_GP");

	    System.out.println("View set to: " + mv.getViewName());

	    return mv;
	}

	@Transactional
	public void updateReport(M_GP_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // 1️⃣ Fetch existing SUMMARY
	     M_GP_Summary_Entity existingSummary =
	    		 BRRS_M_GP_Summary_Repo.findById(updatedEntity.getReportDate())
	                    .orElseThrow(() -> new RuntimeException(
	                            "Summary record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    // 2️⃣ Fetch or create DETAIL
	      M_GP_Detail_Entity existingDetail =
	            M_GP_Detail_Repo.findById(updatedEntity.getReportDate())
	                    .orElseGet(() -> {
	                          M_GP_Detail_Entity d = new   M_GP_Detail_Entity();
	                        d.setReportDate(updatedEntity.getReportDate());
	                        return d;
	                    });

	  try {
	        // 1️⃣ Loop from R11 to R64 and copy fields
	    	
	        for (int i = 11; i <= 64; i++) {
				
	        	 String prefix = "R" + i + "_";

	            String[] fields = { "PRODUCT","STAGE1_PROVISIONS",
					"QUALIFY_STAGE2_PROVISIONS",
					"TOTAL_GENERAL_PROVISIONS" };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter =
	                              M_GP_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter =
	                              M_GP_Summary_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Method detailSetter =
	                              M_GP_Detail_Entity.class.getMethod(
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
	  BRRS_M_GP_Summary_Repo.save(existingSummary);
	    M_GP_Detail_Repo.save(existingDetail);
		
		}

	
	
	
	
	
	@Transactional
    public void updateResubReport(M_GP_RESUB_Summary_Entity updatedEntity) {

        Date reportDate = updatedEntity.getReport_date();

        // ----------------------------------------------------
        // GET CURRENT VERSION FROM RESUB TABLE
        // ----------------------------------------------------

        BigDecimal maxResubVer =
            M_GP_resub_summary_repo.findMaxVersion(reportDate);

        if (maxResubVer == null)
            throw new RuntimeException("No record for: " + reportDate);

        BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

        Date now = new Date();

        // ====================================================
        // 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
        // ====================================================

        M_GP_RESUB_Summary_Entity resubSummary =
            new M_GP_RESUB_Summary_Entity();

        BeanUtils.copyProperties(updatedEntity, resubSummary,
            "reportDate", "reportVersion", "reportResubDate");

        resubSummary.setReport_date(reportDate);
        resubSummary.setReport_version(newVersion);
        resubSummary.setReportResubDate(now);

        // ====================================================
        // 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
        // ====================================================

        M_GP_RESUB_Detail_Entity resubDetail =
            new M_GP_RESUB_Detail_Entity();

        BeanUtils.copyProperties(updatedEntity, resubDetail,
            "reportDate", "reportVersion", "reportResubDate");

        resubDetail.setReport_date(reportDate);
        resubDetail.setReport_version(newVersion);
        resubDetail.setReportResubDate(now);

        // ====================================================
        // 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
        // ====================================================

        M_GP_Archival_Summary_Entity archSummary =
            new M_GP_Archival_Summary_Entity();

        BeanUtils.copyProperties(updatedEntity, archSummary,
            "reportDate", "reportVersion", "reportResubDate");

        archSummary.setReport_date(reportDate);
        archSummary.setReport_version(newVersion);   // SAME VERSION
        archSummary.setReportResubDate(now);

        // ====================================================
        // 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
        // ====================================================

        M_GP_Archival_Detail_Entity archDetail =
            new M_GP_Archival_Detail_Entity();

        BeanUtils.copyProperties(updatedEntity, archDetail,
            "reportDate", "reportVersion", "reportResubDate");

        archDetail.setReport_date(reportDate);
        archDetail.setReport_version(newVersion);    // SAME VERSION
        archDetail.setReportResubDate(now);

        // ====================================================
        // 6️⃣ SAVE ALL WITH SAME DATA
        // ====================================================

        M_GP_resub_summary_repo.save(resubSummary);
        M_GP_resub_detail_repo.save(resubDetail);

        M_GP_Archival_Summary_Repo.save(archSummary);
        M_GP_Archival_Detail_Repo.save(archDetail);
    }
	
	
	//Archival View
		public List<Object[]> getM_GPArchival() {
			List<Object[]> archivalList = new ArrayList<>();

			try {
				List<M_GP_Archival_Summary_Entity> repoData = M_GP_Archival_Summary_Repo
						.getdatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (M_GP_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					M_GP_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  M_GP  Archival data: " + e.getMessage());
				e.printStackTrace();
			}

			return archivalList;
		}
	
	
	public List<Object[]> getM_GPResub() {
	    List<Object[]> resubList = new ArrayList<>();
	    try {
	        List<M_GP_Archival_Summary_Entity> latestArchivalList =
	        		M_GP_Archival_Summary_Repo.getdatabydateListWithVersion();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (M_GP_Archival_Summary_Entity entity : latestArchivalList) {
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
	        System.err.println("Error fetching M_GP Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return resubList;
	}

	// Normal format Excel

		public byte[] getM_GPExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
					return getExcelM_GPARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				logger.info("Service: Generating RESUB report for version {}", version);

				try {
					// ✅ Redirecting to Resub Excel
					return BRRS_M_GPResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} else {

				if ("email".equalsIgnoreCase(format) && version == null) {
					logger.info("Got format as Email");
					logger.info("Service: Generating Email report for version {}", version);
					return BRRS_M_GPEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
				} else {

					// Fetch data

					List<M_GP_Summary_Entity> dataList = BRRS_M_GP_Summary_Repo
							.getdatabydateList(dateformat.parse(todate));

					if (dataList.isEmpty()) {
						logger.warn("Service: No data found for BRRS_M_GP report. Returning empty result.");
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
								M_GP_Summary_Entity record = dataList.get(i);
								System.out.println("rownumber=" + startRow + i);
								Row row = sheet.getRow(startRow + i);
								if (row == null) {
									row = sheet.createRow(startRow + i);
								}

		// row10
						// Column C

						Cell cell1 = row.getCell(1);
						if (record.getR11_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR11_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR11_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR11_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR11_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR11_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(11);

						cell1 = row.getCell(1);
						if (record.getR12_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR12_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR12_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(12);

						cell1 = row.getCell(1);
						if (record.getR13_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR13_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR13_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(13);
						cell1 = row.getCell(1);
						if (record.getR14_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR14_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR14_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row10
						// Column C
						row = sheet.getRow(14);

						cell1 = row.getCell(1);
						if (record.getR15_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR15_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR15_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR15_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR15_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR15_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(15);

						cell1 = row.getCell(1);
						if (record.getR16_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR16_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR16_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(16);

						cell1 = row.getCell(1);
						if (record.getR17_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR17_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR17_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(17);
						cell1 = row.getCell(1);
						if (record.getR18_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR18_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR18_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row10
						// Column C
						row = sheet.getRow(18);

						cell1 = row.getCell(1);
						if (record.getR19_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR19_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR19_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(19);

						cell1 = row.getCell(1);
						if (record.getR20_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR20_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR20_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(20);

						cell1 = row.getCell(1);
						if (record.getR21_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR21_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR21_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(21);
						cell1 = row.getCell(1);
						if (record.getR22_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR22_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR22_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row10
						// Column C
						row = sheet.getRow(22);

						cell1 = row.getCell(1);
						if (record.getR23_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR23_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR23_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(23);

						cell1 = row.getCell(1);
						if (record.getR24_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR24_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR24_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(24);

						cell1 = row.getCell(1);
						if (record.getR25_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR25_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR25_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(25);
						cell1 = row.getCell(1);
						if (record.getR26_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR26_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR26_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row10
						// Column C
						row = sheet.getRow(26);

						cell1 = row.getCell(1);
						if (record.getR27_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR27_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR27_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(27);

						cell1 = row.getCell(1);
						if (record.getR28_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR28_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR28_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(28);

						cell1 = row.getCell(1);
						if (record.getR29_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR29_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR29_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR29_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR29_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR29_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(29);
						cell1 = row.getCell(1);
						if (record.getR30_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR30_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR30_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row10
						// Column C
						row = sheet.getRow(30);

						cell1 = row.getCell(1);
						if (record.getR31_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR31_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR31_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(31);

						cell1 = row.getCell(1);
						if (record.getR32_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR32_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR32_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row10
						// Column C
						row = sheet.getRow(32);

						cell1 = row.getCell(1);
						if (record.getR33_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.getCell(2);
						if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F
						cell1 = row.getCell(3);
						if (record.getR33_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR33_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column C
						row = sheet.getRow(33);

						cell1 = row.getCell(1);
						if (record.getR34_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR34_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR34_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						row = sheet.getRow(34);

						cell1 = row.getCell(1);
						if (record.getR35_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR35_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR35_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column C
						row = sheet.getRow(35);

						cell1 = row.getCell(1);
						if (record.getR36_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR36_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR36_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column C
						row = sheet.getRow(36);

						cell1 = row.getCell(1);
						if (record.getR37_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR37_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR37_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						row = sheet.getRow(37);

						cell1 = row.getCell(1);
						if (record.getR38_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR38_STAGE1_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR38_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR38_QUALIFY_STAGE2_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR38_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR38_TOTAL_GENERAL_PROVISIONS().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// Column C
						row = sheet.getRow(38);

						cell1 = row.getCell(1);
						if (record.getR39_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR39_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR39_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// ===== R40 =====
						row = sheet.getRow(39);

						cell1 = row.getCell(1);
						if (record.getR40_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR40_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR40_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R41 =====
						row = sheet.getRow(40);

						cell1 = row.getCell(1);
						if (record.getR41_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR41_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR41_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR41_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR41_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR41_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R42 =====
						row = sheet.getRow(41);

						cell1 = row.getCell(1);
						if (record.getR42_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR42_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR42_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R43 =====
						row = sheet.getRow(42);

						cell1 = row.getCell(1);
						if (record.getR43_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR43_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR43_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R44 =====
						row = sheet.getRow(43);

						cell1 = row.getCell(1);
						if (record.getR44_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR44_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR44_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR44_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR44_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR44_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R45 =====
						row = sheet.getRow(44);

						cell1 = row.getCell(1);
						if (record.getR45_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR45_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR45_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// ===== R46 =====
						row = sheet.getRow(45);

						cell1 = row.getCell(1);
						if (record.getR46_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR46_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR46_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R47 =====
						row = sheet.getRow(46);

						cell1 = row.getCell(1);
						if (record.getR47_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR47_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR47_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R48 =====
						row = sheet.getRow(47);

						cell1 = row.getCell(1);
						if (record.getR48_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR48_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR48_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R49 =====
						row = sheet.getRow(48);

						cell1 = row.getCell(1);
						if (record.getR49_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR49_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR49_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR49_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR49_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR49_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R50 =====
						row = sheet.getRow(49);

						cell1 = row.getCell(1);
						if (record.getR50_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR50_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR50_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// ===== R51 =====
						row = sheet.getRow(50);

						cell1 = row.getCell(1);
						if (record.getR51_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR51_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR51_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R52 =====
						row = sheet.getRow(51);

						cell1 = row.getCell(1);
						if (record.getR52_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR52_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR52_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R53 =====
						row = sheet.getRow(52);

						cell1 = row.getCell(1);
						if (record.getR53_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR53_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR53_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR53_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR53_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR53_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R54 =====
						row = sheet.getRow(53);

						cell1 = row.getCell(1);
						if (record.getR54_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR54_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR54_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R55 =====
						row = sheet.getRow(54);

						cell1 = row.getCell(1);
						if (record.getR55_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR55_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR55_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// ===== R56 =====
						row = sheet.getRow(55);

						cell1 = row.getCell(1);
						if (record.getR56_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR56_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR56_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R57 =====
						row = sheet.getRow(56);

						cell1 = row.getCell(1);
						if (record.getR57_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR57_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR57_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR57_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR57_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR57_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R58 =====
						row = sheet.getRow(57);

						cell1 = row.getCell(1);
						if (record.getR58_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR58_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR58_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R59 =====
						row = sheet.getRow(58);

						cell1 = row.getCell(1);
						if (record.getR59_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR59_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR59_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R60 =====
						row = sheet.getRow(59);

						cell1 = row.getCell(1);
						if (record.getR60_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR60_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR60_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R61 =====
						row = sheet.getRow(60);

						cell1 = row.getCell(1);
						if (record.getR61_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR61_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR61_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R62 =====
						row = sheet.getRow(61);

						cell1 = row.getCell(1);
						if (record.getR62_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR62_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR62_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R63 =====
						row = sheet.getRow(62);

						cell1 = row.getCell(1);
						if (record.getR63_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR63_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR63_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ===== R64 =====
						row = sheet.getRow(63);

						cell1 = row.getCell(1);
						if (record.getR64_STAGE1_PROVISIONS() != null) {
							cell1.setCellValue(record.getR64_STAGE1_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(2);
						if (record.getR64_QUALIFY_STAGE2_PROVISIONS() != null) {
							cell1.setCellValue(record.getR64_QUALIFY_STAGE2_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.getCell(3);
						if (record.getR64_TOTAL_GENERAL_PROVISIONS() != null) {
							cell1.setCellValue(record.getR64_TOTAL_GENERAL_PROVISIONS().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
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
		public byte[] BRRS_M_GPEmailExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Email Excel generation process in memory.");
			
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				try {
					// Redirecting to Archival
					return BRRS_M_GPARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				logger.info("Service: Generating RESUB report for version {}", version);

				try {
					// ✅ Redirecting to Resub Excel
					return BRRS_M_GPEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} 
			else {
			List<M_GP_Summary_Entity> dataList = BRRS_M_GP_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_GP report. Returning empty result.");
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

				int startRow = 7;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_GP_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

	//-------------------8
					
					
					Cell cell1 = row.getCell(1);
					if (record.getR12_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					 cell1 = row.getCell(2);
					if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------9
					
					// Column C
					row = sheet.getRow(8);

					cell1 = row.getCell(1);
					if (record.getR13_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------10
				
					row = sheet.getRow(9);
					
					cell1 = row.getCell(1);
					if (record.getR14_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------12
				
				row = sheet.getRow(11);

					cell1 = row.getCell(1);
					if (record.getR16_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
				
				//-------------------13
				
				row = sheet.getRow(12);

					cell1 = row.getCell(1);
					if (record.getR17_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					
						//-------------------14
						
						
						
	row = sheet.getRow(13);

	cell1 = row.getCell(1);
	if (record.getR18_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------15
	row = sheet.getRow(14);

	cell1 = row.getCell(1);
	if (record.getR19_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------16
	row = sheet.getRow(15);

	cell1 = row.getCell(1);
	if (record.getR20_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------17
	row = sheet.getRow(16);

	cell1 = row.getCell(1);
	if (record.getR21_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------18
	row = sheet.getRow(17);

	cell1 = row.getCell(1);
	if (record.getR22_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------19
	row = sheet.getRow(18);

	cell1 = row.getCell(1);
	if (record.getR23_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------20
	row = sheet.getRow(19);

	cell1 = row.getCell(1);
	if (record.getR24_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------21
	row = sheet.getRow(20);

	cell1 = row.getCell(1);
	if (record.getR25_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------22
	row = sheet.getRow(21);

	cell1 = row.getCell(1);
	if (record.getR26_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------23
	row = sheet.getRow(22);

	cell1 = row.getCell(1);
	if (record.getR27_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------24
	row = sheet.getRow(23);

	cell1 = row.getCell(1);
	if (record.getR28_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}



	//-------------------26
	row = sheet.getRow(25);

	cell1 = row.getCell(1);
	if (record.getR30_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------27
	row = sheet.getRow(26);

	cell1 = row.getCell(1);
	if (record.getR31_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------28
	row = sheet.getRow(27);

	cell1 = row.getCell(1);
	if (record.getR32_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------29
	row = sheet.getRow(28);

	cell1 = row.getCell(1);
	if (record.getR33_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------30
	row = sheet.getRow(29);

	cell1 = row.getCell(1);
	if (record.getR34_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------31
	row = sheet.getRow(30);

	cell1 = row.getCell(1);
	if (record.getR35_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------32
	row = sheet.getRow(31);

	cell1 = row.getCell(1);
	if (record.getR36_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------33
	row = sheet.getRow(32);

	cell1 = row.getCell(1);
	if (record.getR37_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}


	//-------------------35



	row = sheet.getRow(34);

	cell1 = row.getCell(1);
	if (record.getR39_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------36
	row = sheet.getRow(35);

	cell1 = row.getCell(1);
	if (record.getR40_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}
		

		//-------------------38
	row = sheet.getRow(37);

	cell1 = row.getCell(1);
	if (record.getR42_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------39
	row = sheet.getRow(38);

	cell1 = row.getCell(1);
	if (record.getR43_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}
		
	//-------------------41	
	row = sheet.getRow(40);

	cell1 = row.getCell(1);
	if (record.getR45_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------42
	row = sheet.getRow(41);

	cell1 = row.getCell(1);
	if (record.getR46_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}	


	//-------------------43
	row = sheet.getRow(42);

	cell1 = row.getCell(1);
	if (record.getR47_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------44
	row = sheet.getRow(43);

	cell1 = row.getCell(1);
	if (record.getR48_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------46
	row = sheet.getRow(45);

	cell1 = row.getCell(1);
	if (record.getR50_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------47
	row = sheet.getRow(46);

	cell1 = row.getCell(1);
	if (record.getR51_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------48
	row = sheet.getRow(47);

	cell1 = row.getCell(1);
	if (record.getR52_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}


	//-------------------50
	row = sheet.getRow(49);

	cell1 = row.getCell(1);
	if (record.getR54_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------51
	row = sheet.getRow(50);

	cell1 = row.getCell(1);
	if (record.getR55_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------52
	row = sheet.getRow(51);

	cell1 = row.getCell(1);
	if (record.getR56_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------54
	row = sheet.getRow(53);

	cell1 = row.getCell(1);
	if (record.getR58_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------55
	row = sheet.getRow(54);

	cell1 = row.getCell(1);
	if (record.getR59_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------56
	row = sheet.getRow(55);

	cell1 = row.getCell(1);
	if (record.getR60_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------57
	row = sheet.getRow(56);

	cell1 = row.getCell(1);
	if (record.getR61_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------58
	row = sheet.getRow(57);

	cell1 = row.getCell(1);
	if (record.getR62_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------59
	row = sheet.getRow(58);

	cell1 = row.getCell(1);
	if (record.getR63_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
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
		public byte[] getExcelM_GPARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory in Archival.");

			if ("email".equalsIgnoreCase(format) && version != null) {
				try {
					// Redirecting to Archival
					return BRRS_M_GPARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} 

			List<M_GP_Archival_Summary_Entity> dataList = M_GP_Archival_Summary_Repo
					.getdatabydateListarchival(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for M_GP report. Returning empty result.");
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
						M_GP_Archival_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

	// row10
										// Column C

										Cell cell1 = row.getCell(1);
										if (record.getR11_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR11_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR11_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR11_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR11_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR11_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(11);

										cell1 = row.getCell(1);
										if (record.getR12_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR12_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR12_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(12);

										cell1 = row.getCell(1);
										if (record.getR13_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR13_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR13_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(13);
										cell1 = row.getCell(1);
										if (record.getR14_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR14_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR14_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(14);

										cell1 = row.getCell(1);
										if (record.getR15_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR15_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR15_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR15_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR15_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR15_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(15);

										cell1 = row.getCell(1);
										if (record.getR16_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR16_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR16_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(16);

										cell1 = row.getCell(1);
										if (record.getR17_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR17_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR17_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(17);
										cell1 = row.getCell(1);
										if (record.getR18_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR18_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR18_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(18);

										cell1 = row.getCell(1);
										if (record.getR19_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR19_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR19_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(19);

										cell1 = row.getCell(1);
										if (record.getR20_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR20_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR20_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(20);

										cell1 = row.getCell(1);
										if (record.getR21_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR21_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR21_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(21);
										cell1 = row.getCell(1);
										if (record.getR22_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR22_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR22_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(22);

										cell1 = row.getCell(1);
										if (record.getR23_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR23_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR23_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(23);

										cell1 = row.getCell(1);
										if (record.getR24_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR24_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR24_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(24);

										cell1 = row.getCell(1);
										if (record.getR25_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR25_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR25_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(25);
										cell1 = row.getCell(1);
										if (record.getR26_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR26_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR26_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(26);

										cell1 = row.getCell(1);
										if (record.getR27_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR27_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR27_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(27);

										cell1 = row.getCell(1);
										if (record.getR28_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR28_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR28_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(28);

										cell1 = row.getCell(1);
										if (record.getR29_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR29_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR29_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR29_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR29_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR29_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(29);
										cell1 = row.getCell(1);
										if (record.getR30_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR30_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR30_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(30);

										cell1 = row.getCell(1);
										if (record.getR31_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR31_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR31_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(31);

										cell1 = row.getCell(1);
										if (record.getR32_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR32_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR32_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(32);

										cell1 = row.getCell(1);
										if (record.getR33_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR33_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR33_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// Column C
										row = sheet.getRow(33);

										cell1 = row.getCell(1);
										if (record.getR34_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR34_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR34_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										row = sheet.getRow(34);

										cell1 = row.getCell(1);
										if (record.getR35_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR35_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR35_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// Column C
										row = sheet.getRow(35);

										cell1 = row.getCell(1);
										if (record.getR36_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR36_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR36_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// Column C
										row = sheet.getRow(36);

										cell1 = row.getCell(1);
										if (record.getR37_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR37_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR37_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										row = sheet.getRow(37);

										cell1 = row.getCell(1);
										if (record.getR38_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR38_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR38_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR38_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR38_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR38_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// Column C
										row = sheet.getRow(38);

										cell1 = row.getCell(1);
										if (record.getR39_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR39_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR39_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R40 =====
										row = sheet.getRow(39);

										cell1 = row.getCell(1);
										if (record.getR40_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR40_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR40_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R41 =====
										row = sheet.getRow(40);

										cell1 = row.getCell(1);
										if (record.getR41_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR41_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR41_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR41_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR41_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR41_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R42 =====
										row = sheet.getRow(41);

										cell1 = row.getCell(1);
										if (record.getR42_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR42_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR42_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R43 =====
										row = sheet.getRow(42);

										cell1 = row.getCell(1);
										if (record.getR43_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR43_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR43_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R44 =====
										row = sheet.getRow(43);

										cell1 = row.getCell(1);
										if (record.getR44_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR44_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR44_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR44_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR44_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR44_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R45 =====
										row = sheet.getRow(44);

										cell1 = row.getCell(1);
										if (record.getR45_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR45_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR45_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R46 =====
										row = sheet.getRow(45);

										cell1 = row.getCell(1);
										if (record.getR46_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR46_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR46_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R47 =====
										row = sheet.getRow(46);

										cell1 = row.getCell(1);
										if (record.getR47_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR47_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR47_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R48 =====
										row = sheet.getRow(47);

										cell1 = row.getCell(1);
										if (record.getR48_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR48_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR48_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R49 =====
										row = sheet.getRow(48);

										cell1 = row.getCell(1);
										if (record.getR49_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR49_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR49_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR49_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR49_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR49_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R50 =====
										row = sheet.getRow(49);

										cell1 = row.getCell(1);
										if (record.getR50_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR50_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR50_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R51 =====
										row = sheet.getRow(50);

										cell1 = row.getCell(1);
										if (record.getR51_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR51_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR51_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R52 =====
										row = sheet.getRow(51);

										cell1 = row.getCell(1);
										if (record.getR52_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR52_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR52_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R53 =====
										row = sheet.getRow(52);

										cell1 = row.getCell(1);
										if (record.getR53_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR53_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR53_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR53_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR53_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR53_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R54 =====
										row = sheet.getRow(53);

										cell1 = row.getCell(1);
										if (record.getR54_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR54_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR54_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R55 =====
										row = sheet.getRow(54);

										cell1 = row.getCell(1);
										if (record.getR55_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR55_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR55_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R56 =====
										row = sheet.getRow(55);

										cell1 = row.getCell(1);
										if (record.getR56_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR56_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR56_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R57 =====
										row = sheet.getRow(56);

										cell1 = row.getCell(1);
										if (record.getR57_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR57_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR57_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR57_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR57_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR57_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R58 =====
										row = sheet.getRow(57);

										cell1 = row.getCell(1);
										if (record.getR58_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR58_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR58_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R59 =====
										row = sheet.getRow(58);

										cell1 = row.getCell(1);
										if (record.getR59_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR59_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR59_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R60 =====
										row = sheet.getRow(59);

										cell1 = row.getCell(1);
										if (record.getR60_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR60_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR60_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R61 =====
										row = sheet.getRow(60);

										cell1 = row.getCell(1);
										if (record.getR61_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR61_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR61_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R62 =====
										row = sheet.getRow(61);

										cell1 = row.getCell(1);
										if (record.getR62_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR62_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR62_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R63 =====
										row = sheet.getRow(62);

										cell1 = row.getCell(1);
										if (record.getR63_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR63_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR63_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R64 =====
										row = sheet.getRow(63);

										cell1 = row.getCell(1);
										if (record.getR64_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR64_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR64_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR64_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR64_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR64_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
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
		public byte[] BRRS_M_GPARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Archival Email Excel generation process in memory.");

			List<M_GP_Archival_Summary_Entity> dataList = M_GP_Archival_Summary_Repo
					.getdatabydateListarchival(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_GP report. Returning empty result.");
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

				int startRow = 7;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_GP_Archival_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}


										//-------------------8
					
					
					Cell cell1 = row.getCell(1);
					if (record.getR12_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					 cell1 = row.getCell(2);
					if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------9
					
					// Column C
					row = sheet.getRow(8);

					cell1 = row.getCell(1);
					if (record.getR13_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------10
				
					row = sheet.getRow(9);
					
					cell1 = row.getCell(1);
					if (record.getR14_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------12
				
				row = sheet.getRow(11);

					cell1 = row.getCell(1);
					if (record.getR16_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
				
				//-------------------13
				
				row = sheet.getRow(12);

					cell1 = row.getCell(1);
					if (record.getR17_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					
						//-------------------14
						
						
						
	row = sheet.getRow(13);

	cell1 = row.getCell(1);
	if (record.getR18_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------15
	row = sheet.getRow(14);

	cell1 = row.getCell(1);
	if (record.getR19_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------16
	row = sheet.getRow(15);

	cell1 = row.getCell(1);
	if (record.getR20_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------17
	row = sheet.getRow(16);

	cell1 = row.getCell(1);
	if (record.getR21_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------18
	row = sheet.getRow(17);

	cell1 = row.getCell(1);
	if (record.getR22_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------19
	row = sheet.getRow(18);

	cell1 = row.getCell(1);
	if (record.getR23_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------20
	row = sheet.getRow(19);

	cell1 = row.getCell(1);
	if (record.getR24_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------21
	row = sheet.getRow(20);

	cell1 = row.getCell(1);
	if (record.getR25_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------22
	row = sheet.getRow(21);

	cell1 = row.getCell(1);
	if (record.getR26_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------23
	row = sheet.getRow(22);

	cell1 = row.getCell(1);
	if (record.getR27_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------24
	row = sheet.getRow(23);

	cell1 = row.getCell(1);
	if (record.getR28_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}



	//-------------------26
	row = sheet.getRow(25);

	cell1 = row.getCell(1);
	if (record.getR30_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------27
	row = sheet.getRow(26);

	cell1 = row.getCell(1);
	if (record.getR31_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------28
	row = sheet.getRow(27);

	cell1 = row.getCell(1);
	if (record.getR32_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------29
	row = sheet.getRow(28);

	cell1 = row.getCell(1);
	if (record.getR33_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------30
	row = sheet.getRow(29);

	cell1 = row.getCell(1);
	if (record.getR34_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------31
	row = sheet.getRow(30);

	cell1 = row.getCell(1);
	if (record.getR35_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------32
	row = sheet.getRow(31);

	cell1 = row.getCell(1);
	if (record.getR36_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------33
	row = sheet.getRow(32);

	cell1 = row.getCell(1);
	if (record.getR37_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}


	//-------------------35



	row = sheet.getRow(34);

	cell1 = row.getCell(1);
	if (record.getR39_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------36
	row = sheet.getRow(35);

	cell1 = row.getCell(1);
	if (record.getR40_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}
		

		//-------------------38
	row = sheet.getRow(37);

	cell1 = row.getCell(1);
	if (record.getR42_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------39
	row = sheet.getRow(38);

	cell1 = row.getCell(1);
	if (record.getR43_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}
		
	//-------------------41	
	row = sheet.getRow(40);

	cell1 = row.getCell(1);
	if (record.getR45_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------42
	row = sheet.getRow(41);

	cell1 = row.getCell(1);
	if (record.getR46_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}	


	//-------------------43
	row = sheet.getRow(42);

	cell1 = row.getCell(1);
	if (record.getR47_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------44
	row = sheet.getRow(43);

	cell1 = row.getCell(1);
	if (record.getR48_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------46
	row = sheet.getRow(45);

	cell1 = row.getCell(1);
	if (record.getR50_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------47
	row = sheet.getRow(46);

	cell1 = row.getCell(1);
	if (record.getR51_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------48
	row = sheet.getRow(47);

	cell1 = row.getCell(1);
	if (record.getR52_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}


	//-------------------50
	row = sheet.getRow(49);

	cell1 = row.getCell(1);
	if (record.getR54_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------51
	row = sheet.getRow(50);

	cell1 = row.getCell(1);
	if (record.getR55_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------52
	row = sheet.getRow(51);

	cell1 = row.getCell(1);
	if (record.getR56_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------54
	row = sheet.getRow(53);

	cell1 = row.getCell(1);
	if (record.getR58_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------55
	row = sheet.getRow(54);

	cell1 = row.getCell(1);
	if (record.getR59_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------56
	row = sheet.getRow(55);

	cell1 = row.getCell(1);
	if (record.getR60_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------57
	row = sheet.getRow(56);

	cell1 = row.getCell(1);
	if (record.getR61_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------58
	row = sheet.getRow(57);

	cell1 = row.getCell(1);
	if (record.getR62_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------59
	row = sheet.getRow(58);

	cell1 = row.getCell(1);
	if (record.getR63_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);

					
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
		public byte[] BRRS_M_GPResubExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

			if ("email".equalsIgnoreCase(format) && version != null) {
				logger.info("Service: Generating RESUB report for version {}", version);

				try {
					// ✅ Redirecting to Resub Excel
					return BRRS_M_GPEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

			List<M_GP_RESUB_Summary_Entity> dataList = M_GP_resub_summary_repo
					.getdatabydateListarchival(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for M_GP report. Returning empty result.");
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

						M_GP_RESUB_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

					// row10
										// Column C

										Cell cell1 = row.getCell(1);
										if (record.getR11_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR11_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR11_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR11_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR11_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR11_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(11);

										cell1 = row.getCell(1);
										if (record.getR12_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR12_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR12_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(12);

										cell1 = row.getCell(1);
										if (record.getR13_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR13_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR13_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(13);
										cell1 = row.getCell(1);
										if (record.getR14_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR14_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR14_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(14);

										cell1 = row.getCell(1);
										if (record.getR15_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR15_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR15_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR15_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR15_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR15_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(15);

										cell1 = row.getCell(1);
										if (record.getR16_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR16_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR16_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(16);

										cell1 = row.getCell(1);
										if (record.getR17_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR17_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR17_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(17);
										cell1 = row.getCell(1);
										if (record.getR18_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR18_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR18_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(18);

										cell1 = row.getCell(1);
										if (record.getR19_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR19_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR19_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(19);

										cell1 = row.getCell(1);
										if (record.getR20_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR20_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR20_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(20);

										cell1 = row.getCell(1);
										if (record.getR21_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR21_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR21_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(21);
										cell1 = row.getCell(1);
										if (record.getR22_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR22_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR22_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(22);

										cell1 = row.getCell(1);
										if (record.getR23_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR23_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR23_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(23);

										cell1 = row.getCell(1);
										if (record.getR24_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR24_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR24_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(24);

										cell1 = row.getCell(1);
										if (record.getR25_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR25_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR25_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(25);
										cell1 = row.getCell(1);
										if (record.getR26_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR26_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR26_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(26);

										cell1 = row.getCell(1);
										if (record.getR27_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR27_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR27_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(27);

										cell1 = row.getCell(1);
										if (record.getR28_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR28_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR28_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(28);

										cell1 = row.getCell(1);
										if (record.getR29_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR29_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR29_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR29_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR29_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR29_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(29);
										cell1 = row.getCell(1);
										if (record.getR30_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR30_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR30_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row10
										// Column C
										row = sheet.getRow(30);

										cell1 = row.getCell(1);
										if (record.getR31_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR31_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR31_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(31);

										cell1 = row.getCell(1);
										if (record.getR32_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR32_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR32_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// row10
										// Column C
										row = sheet.getRow(32);

										cell1 = row.getCell(1);
										if (record.getR33_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column E
										cell1 = row.getCell(2);
										if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// row11
										// Column F
										cell1 = row.getCell(3);
										if (record.getR33_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR33_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// Column C
										row = sheet.getRow(33);

										cell1 = row.getCell(1);
										if (record.getR34_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR34_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR34_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										row = sheet.getRow(34);

										cell1 = row.getCell(1);
										if (record.getR35_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR35_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR35_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// Column C
										row = sheet.getRow(35);

										cell1 = row.getCell(1);
										if (record.getR36_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR36_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR36_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// Column C
										row = sheet.getRow(36);

										cell1 = row.getCell(1);
										if (record.getR37_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR37_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR37_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										row = sheet.getRow(37);

										cell1 = row.getCell(1);
										if (record.getR38_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR38_STAGE1_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR38_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR38_QUALIFY_STAGE2_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR38_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR38_TOTAL_GENERAL_PROVISIONS().doubleValue());

										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// Column C
										row = sheet.getRow(38);

										cell1 = row.getCell(1);
										if (record.getR39_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR39_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR39_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R40 =====
										row = sheet.getRow(39);

										cell1 = row.getCell(1);
										if (record.getR40_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR40_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR40_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R41 =====
										row = sheet.getRow(40);

										cell1 = row.getCell(1);
										if (record.getR41_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR41_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR41_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR41_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR41_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR41_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R42 =====
										row = sheet.getRow(41);

										cell1 = row.getCell(1);
										if (record.getR42_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR42_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR42_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R43 =====
										row = sheet.getRow(42);

										cell1 = row.getCell(1);
										if (record.getR43_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR43_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR43_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R44 =====
										row = sheet.getRow(43);

										cell1 = row.getCell(1);
										if (record.getR44_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR44_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR44_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR44_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR44_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR44_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R45 =====
										row = sheet.getRow(44);

										cell1 = row.getCell(1);
										if (record.getR45_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR45_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR45_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R46 =====
										row = sheet.getRow(45);

										cell1 = row.getCell(1);
										if (record.getR46_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR46_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR46_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R47 =====
										row = sheet.getRow(46);

										cell1 = row.getCell(1);
										if (record.getR47_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR47_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR47_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R48 =====
										row = sheet.getRow(47);

										cell1 = row.getCell(1);
										if (record.getR48_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR48_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR48_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R49 =====
										row = sheet.getRow(48);

										cell1 = row.getCell(1);
										if (record.getR49_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR49_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR49_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR49_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR49_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR49_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R50 =====
										row = sheet.getRow(49);

										cell1 = row.getCell(1);
										if (record.getR50_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR50_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR50_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R51 =====
										row = sheet.getRow(50);

										cell1 = row.getCell(1);
										if (record.getR51_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR51_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR51_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R52 =====
										row = sheet.getRow(51);

										cell1 = row.getCell(1);
										if (record.getR52_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR52_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR52_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R53 =====
										row = sheet.getRow(52);

										cell1 = row.getCell(1);
										if (record.getR53_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR53_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR53_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR53_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR53_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR53_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R54 =====
										row = sheet.getRow(53);

										cell1 = row.getCell(1);
										if (record.getR54_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR54_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR54_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R55 =====
										row = sheet.getRow(54);

										cell1 = row.getCell(1);
										if (record.getR55_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR55_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR55_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}
										// ===== R56 =====
										row = sheet.getRow(55);

										cell1 = row.getCell(1);
										if (record.getR56_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR56_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR56_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R57 =====
										row = sheet.getRow(56);

										cell1 = row.getCell(1);
										if (record.getR57_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR57_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR57_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR57_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR57_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR57_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R58 =====
										row = sheet.getRow(57);

										cell1 = row.getCell(1);
										if (record.getR58_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR58_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR58_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R59 =====
										row = sheet.getRow(58);

										cell1 = row.getCell(1);
										if (record.getR59_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR59_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR59_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R60 =====
										row = sheet.getRow(59);

										cell1 = row.getCell(1);
										if (record.getR60_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR60_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR60_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R61 =====
										row = sheet.getRow(60);

										cell1 = row.getCell(1);
										if (record.getR61_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR61_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR61_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R62 =====
										row = sheet.getRow(61);

										cell1 = row.getCell(1);
										if (record.getR62_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR62_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR62_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R63 =====
										row = sheet.getRow(62);

										cell1 = row.getCell(1);
										if (record.getR63_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR63_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR63_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										// ===== R64 =====
										row = sheet.getRow(63);

										cell1 = row.getCell(1);
										if (record.getR64_STAGE1_PROVISIONS() != null) {
											cell1.setCellValue(record.getR64_STAGE1_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(2);
										if (record.getR64_QUALIFY_STAGE2_PROVISIONS() != null) {
											cell1.setCellValue(record.getR64_QUALIFY_STAGE2_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
										}

										cell1 = row.getCell(3);
										if (record.getR64_TOTAL_GENERAL_PROVISIONS() != null) {
											cell1.setCellValue(record.getR64_TOTAL_GENERAL_PROVISIONS().doubleValue());
										} else {
											cell1.setCellValue("");
											cell1.setCellStyle(textStyle);
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
		public byte[] BRRS_M_GPEmailResubExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Resub Email Excel generation process in memory.");

			List<M_GP_RESUB_Summary_Entity> dataList = M_GP_resub_summary_repo
					.getdatabydateListarchival(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_GP report. Returning empty result.");
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

				int startRow = 7;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_GP_RESUB_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

		//-------------------8
					
					
					Cell cell1 = row.getCell(1);
					if (record.getR12_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					 cell1 = row.getCell(2);
					if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------9
					
					// Column C
					row = sheet.getRow(8);

					cell1 = row.getCell(1);
					if (record.getR13_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------10
				
					row = sheet.getRow(9);
					
					cell1 = row.getCell(1);
					if (record.getR14_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
				//-------------------12
				
				row = sheet.getRow(11);

					cell1 = row.getCell(1);
					if (record.getR16_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
				
				//-------------------13
				
				row = sheet.getRow(12);

					cell1 = row.getCell(1);
					if (record.getR17_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					
						//-------------------14
						
						
						
	row = sheet.getRow(13);

	cell1 = row.getCell(1);
	if (record.getR18_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------15
	row = sheet.getRow(14);

	cell1 = row.getCell(1);
	if (record.getR19_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------16
	row = sheet.getRow(15);

	cell1 = row.getCell(1);
	if (record.getR20_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------17
	row = sheet.getRow(16);

	cell1 = row.getCell(1);
	if (record.getR21_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------18
	row = sheet.getRow(17);

	cell1 = row.getCell(1);
	if (record.getR22_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------19
	row = sheet.getRow(18);

	cell1 = row.getCell(1);
	if (record.getR23_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------20
	row = sheet.getRow(19);

	cell1 = row.getCell(1);
	if (record.getR24_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------21
	row = sheet.getRow(20);

	cell1 = row.getCell(1);
	if (record.getR25_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------22
	row = sheet.getRow(21);

	cell1 = row.getCell(1);
	if (record.getR26_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------23
	row = sheet.getRow(22);

	cell1 = row.getCell(1);
	if (record.getR27_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------24
	row = sheet.getRow(23);

	cell1 = row.getCell(1);
	if (record.getR28_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}



	//-------------------26
	row = sheet.getRow(25);

	cell1 = row.getCell(1);
	if (record.getR30_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------27
	row = sheet.getRow(26);

	cell1 = row.getCell(1);
	if (record.getR31_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------28
	row = sheet.getRow(27);

	cell1 = row.getCell(1);
	if (record.getR32_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------29
	row = sheet.getRow(28);

	cell1 = row.getCell(1);
	if (record.getR33_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------30
	row = sheet.getRow(29);

	cell1 = row.getCell(1);
	if (record.getR34_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------31
	row = sheet.getRow(30);

	cell1 = row.getCell(1);
	if (record.getR35_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------32
	row = sheet.getRow(31);

	cell1 = row.getCell(1);
	if (record.getR36_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------33
	row = sheet.getRow(32);

	cell1 = row.getCell(1);
	if (record.getR37_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}


	//-------------------35



	row = sheet.getRow(34);

	cell1 = row.getCell(1);
	if (record.getR39_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------36
	row = sheet.getRow(35);

	cell1 = row.getCell(1);
	if (record.getR40_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}
		

		//-------------------38
	row = sheet.getRow(37);

	cell1 = row.getCell(1);
	if (record.getR42_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------39
	row = sheet.getRow(38);

	cell1 = row.getCell(1);
	if (record.getR43_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}
		
	//-------------------41	
	row = sheet.getRow(40);

	cell1 = row.getCell(1);
	if (record.getR45_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------42
	row = sheet.getRow(41);

	cell1 = row.getCell(1);
	if (record.getR46_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}	


	//-------------------43
	row = sheet.getRow(42);

	cell1 = row.getCell(1);
	if (record.getR47_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------44
	row = sheet.getRow(43);

	cell1 = row.getCell(1);
	if (record.getR48_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------46
	row = sheet.getRow(45);

	cell1 = row.getCell(1);
	if (record.getR50_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------47
	row = sheet.getRow(46);

	cell1 = row.getCell(1);
	if (record.getR51_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------48
	row = sheet.getRow(47);

	cell1 = row.getCell(1);
	if (record.getR52_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}


	//-------------------50
	row = sheet.getRow(49);

	cell1 = row.getCell(1);
	if (record.getR54_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------51
	row = sheet.getRow(50);

	cell1 = row.getCell(1);
	if (record.getR55_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------52
	row = sheet.getRow(51);

	cell1 = row.getCell(1);
	if (record.getR56_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------54
	row = sheet.getRow(53);

	cell1 = row.getCell(1);
	if (record.getR58_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------55
	row = sheet.getRow(54);

	cell1 = row.getCell(1);
	if (record.getR59_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------56
	row = sheet.getRow(55);

	cell1 = row.getCell(1);
	if (record.getR60_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------57
	row = sheet.getRow(56);

	cell1 = row.getCell(1);
	if (record.getR61_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------58
	row = sheet.getRow(57);

	cell1 = row.getCell(1);
	if (record.getR62_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	//-------------------59
	row = sheet.getRow(58);

	cell1 = row.getCell(1);
	if (record.getR63_STAGE1_PROVISIONS() != null) {
	cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);
	}

	cell1 = row.getCell(2);
	if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
	cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
	} else {
	cell1.setCellValue("");
	cell1.setCellStyle(textStyle);

					
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